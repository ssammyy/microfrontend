package org.kebs.app.kotlin.apollo.api.ports.provided.sage

import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import org.apache.commons.codec.binary.Base64
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import org.apache.http.HttpHeaders
import org.apache.http.HttpResponse
import org.apache.http.NameValuePair
import org.apache.http.client.ClientProtocolException
import org.apache.http.client.ResponseHandler
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.apache.http.impl.client.LaxRedirectStrategy
import org.apache.http.message.BasicNameValuePair
import org.jasypt.encryption.StringEncryptor
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DaoService
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.InvoiceDaoService
import org.kebs.app.kotlin.apollo.api.ports.provided.mpesa.requests.MpesaTransactionsRequest
import org.kebs.app.kotlin.apollo.api.ports.provided.mpesa.response.MpesaPushResponse
import org.kebs.app.kotlin.apollo.api.ports.provided.sage.requests.RootRequest
import org.kebs.app.kotlin.apollo.api.ports.provided.sage.response.RootResponse
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import org.kebs.app.kotlin.apollo.store.model.WorkflowTransactionsEntity
import org.kebs.app.kotlin.apollo.store.model.invoice.LogStgPaymentReconciliationDetailsToSageEntity
import org.kebs.app.kotlin.apollo.store.repo.ILogStgPaymentReconciliationDetailsToSageRepo
import org.springframework.stereotype.Service
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.math.BigDecimal
import java.net.URL
import java.sql.Timestamp
import java.time.Instant
import javax.activation.MimetypesFileTypeMap


@Service
class PostInvoiceToSageServices(
    private val jasyptStringEncryptor: StringEncryptor,
    private val applicationMapProperties: ApplicationMapProperties,
    private val iLogStgPaymentReconciliationDetailsToSageRepo: ILogStgPaymentReconciliationDetailsToSageRepo,
    private val commonDaoServices: CommonDaoServices,
    private val daoService: DaoService,
    private val invoiceDaoService: InvoiceDaoService
) {
    fun postInvoiceTransactionToSage(stgID: Long, user: UsersEntity) {
        val config =
            commonDaoServices.findIntegrationConfigurationEntity(applicationMapProperties.mapSageConfigIntegration)
        val configUrl = config.url ?: throw Exception("URL CANNOT BE NULL")
        val invoiceFound = invoiceDaoService.findInvoiceStgReconciliationDetailsByID(stgID)
        runBlocking {

            val rootRequest = RootRequest()
            with(rootRequest.header) {
                this?.serviceName = "Thirdparty"
                this?.messageID = invoiceFound.referenceCode
                this?.connectionID = jasyptStringEncryptor.decrypt(config.username)
                this?.connectionPassword = jasyptStringEncryptor.decrypt(config.password)
            }
            with(rootRequest.request) {
                this?.documentNo = invoiceFound.referenceCode
                this?.documentDate = invoiceFound.invoiceDate.toString()
                this?.docType = 1
                this?.currencyCode = "KES"
                this?.customerCode = "HQS-0662"
                this?.customerName = invoiceFound.customerName
                this?.invoiceDesc = "Laboratory Analysis Fees"
                this?.revenueAcc = "10020-100-04"
                this?.revenueAccDesc = "Laboratory Analysis Fees- Testing Dept"
                this?.taxable = 1
                this?.invoiceAmnt = invoiceFound.invoiceAmount ?: throw Exception("INVOICE AMOUNT CANNOT BE NULL")
            }


            var transactionsRequest = LogStgPaymentReconciliationDetailsToSageEntity()
            with(transactionsRequest) {
                stgPaymentId = invoiceFound.id
                serviceName = rootRequest.header?.serviceName
                requestMessageId = rootRequest.header?.messageID
                connectionId = rootRequest.header?.connectionID
                connectionPassword = rootRequest.header?.connectionPassword
                requestDocumentNo = rootRequest.request?.documentNo
                documentDate = rootRequest.request?.documentDate
                docType = rootRequest.request?.docType.toString()
                currencyCode = rootRequest.request?.currencyCode
                customerCode = rootRequest.request?.customerCode
                customerName = rootRequest.request?.customerName
                invoiceDesc = rootRequest.request?.invoiceDesc
                revenueAcc = rootRequest.request?.revenueAcc
                revenueAccDesc = rootRequest.request?.revenueAccDesc
                taxable = rootRequest.request?.taxable.toString()
                invoiceAmnt = rootRequest.request?.invoiceAmnt.toString()
                status = 0
                createdBy = commonDaoServices.concatenateName(user)
                createdOn = commonDaoServices.getTimestamp()
            }

            transactionsRequest = iLogStgPaymentReconciliationDetailsToSageRepo.save(transactionsRequest)

            val headerParameters = mutableMapOf<String, String>()
            headerParameters["serviceName"] = rootRequest.header?.serviceName.toString()
            headerParameters["messageID"] = rootRequest.header?.messageID.toString()
            headerParameters["connectionID"] = rootRequest.header?.connectionID.toString()
            headerParameters["connectionPassword"] = rootRequest.header?.connectionPassword.toString()

            val log = daoService.createTransactionLog(0, "${invoiceFound.referenceCode}_1")
            val resp = daoService.getHttpResponseFromPostCall(
                false,
                configUrl,
                null,
                rootRequest.request,
                config,
                null,
                headerParameters
            )
            val response: Triple<WorkflowTransactionsEntity, RootResponse?, io.ktor.client.statement.HttpResponse?> =
                daoService.processResponses(resp, log, configUrl, config)

            with(transactionsRequest) {
                responseMessageId = response.second?.header?.messageID
                statusCode = response.second?.header?.statusCode
                statusDescription = response.second?.header?.statusDescription
                responseDocumentNo = response.second?.response?.documentNo
                responseDate = response.second?.response?.responseDate
                status = 1
                modifiedBy = commonDaoServices.concatenateName(user)
                modifiedOn = commonDaoServices.getTimestamp()
            }

            transactionsRequest = iLogStgPaymentReconciliationDetailsToSageRepo.save(transactionsRequest)
        }
    }
}