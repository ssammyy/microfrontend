package org.kebs.app.kotlin.apollo.api.ports.provided.sage

import kotlinx.coroutines.runBlocking
import org.jasypt.encryption.StringEncryptor
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DaoService
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.InvoiceDaoService
import org.kebs.app.kotlin.apollo.api.ports.provided.sage.requests.*
import org.kebs.app.kotlin.apollo.api.ports.provided.sage.response.PaymentStatusResult
import org.kebs.app.kotlin.apollo.api.ports.provided.sage.response.RootResponse
import org.kebs.app.kotlin.apollo.api.ports.provided.sage.response.SagePostingResponseResult
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.utils.generateRandomText
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.CdDemandNoteEntity
import org.kebs.app.kotlin.apollo.store.model.ServiceMapsEntity
import org.kebs.app.kotlin.apollo.store.model.WorkflowTransactionsEntity
import org.kebs.app.kotlin.apollo.store.model.invoice.InvoiceBatchDetailsEntity
import org.kebs.app.kotlin.apollo.store.model.invoice.LogStgPaymentReconciliationDetailsToSageEntity
import org.kebs.app.kotlin.apollo.store.repo.ILogStgPaymentReconciliationDetailsToSageRepo
import org.springframework.stereotype.Service


@Service
class PostInvoiceToSageServices(
        private val jasyptStringEncryptor: StringEncryptor,
        private val applicationMapProperties: ApplicationMapProperties,
        private val iLogStgPaymentReconciliationDetailsToSageRepo: ILogStgPaymentReconciliationDetailsToSageRepo,
        private val commonDaoServices: CommonDaoServices,
        private val daoService: DaoService,
        private val invoiceDaoService: InvoiceDaoService
) {
    fun postInvoiceTransactionToSage(stgID: Long, user: String, map: ServiceMapsEntity) {
        val config =
                commonDaoServices.findIntegrationConfigurationEntity(applicationMapProperties.mapSageConfigIntegration)
        val configUrl = config.url ?: throw Exception("URL CANNOT BE NULL")
        val invoiceFound = invoiceDaoService.findInvoiceStgReconciliationDetailsByID(stgID)
        runBlocking {

            val headerBody = Header().apply {
                serviceName = "BSKApp"
                messageID = "SAGEREF${generateRandomText(3, map.secureRandom, map.messageDigestAlgorithm, true).toUpperCase()}"
                connectionID = jasyptStringEncryptor.decrypt(config.username)
                connectionPassword = jasyptStringEncryptor.decrypt(config.password)

            }
            val requestBody = Request().apply {
                documentNo = invoiceFound.referenceCode
                documentDate = invoiceFound.invoiceDate
                docType = 1
                currencyCode = "KES"
                customerCode = "HQS-0662"
                customerName = invoiceFound.customerName
                invoiceDesc = "Laboratory Analysis Fees"
                revenueAcc = "10020-100-04"
                revenueAccDesc = "Laboratory Analysis Fees- Testing Dept"
                taxable = 1
                invoiceAmnt = invoiceFound.invoiceAmount ?: throw Exception("INVOICE AMOUNT CANNOT BE NULL")
            }

            val rootRequest = RootRequest().apply {
                header = headerBody
                request = requestBody
            }

            var transactionsRequest = LogStgPaymentReconciliationDetailsToSageEntity()
            with(transactionsRequest) {
                stgPaymentId = invoiceFound.id
                serviceName = rootRequest.header?.serviceName
                requestMessageId = rootRequest.header?.messageID
                connectionId = rootRequest.header?.connectionID
                connectionPassword = rootRequest.header?.connectionPassword
                requestDocumentNo = rootRequest.request?.documentNo
                documentDate = rootRequest.request?.documentDate.toString()
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
                createdBy = user
                createdOn = commonDaoServices.getTimestamp()
            }

            transactionsRequest = iLogStgPaymentReconciliationDetailsToSageRepo.save(transactionsRequest)

            val headerParameters = mutableMapOf<String, String>()
            headerParameters["serviceName"] = rootRequest.header?.serviceName.toString()
            headerParameters["messageID"] = rootRequest.header?.messageID.toString()
            headerParameters["connectionID"] = rootRequest.header?.connectionID.toString()
            headerParameters["connectionPassword"] = rootRequest.header?.connectionPassword.toString()

//            val requestBody = Gson().toJson(rootRequest.request)

//            println("DATA BEING SENT =$requestBody")

            val log = daoService.createTransactionLog(0, "${invoiceFound.referenceCode}_1")
            val resp = daoService.getHttpResponseFromPostCall(
                    false,
                    configUrl,
                    null,
                    rootRequest,
                    config,
                    null,
                    null
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
                modifiedBy = user
                modifiedOn = commonDaoServices.getTimestamp()
            }

            iLogStgPaymentReconciliationDetailsToSageRepo.save(transactionsRequest)
        }
    }

    fun postInvoiceTransactionToSage(demandNote: CdDemandNoteEntity, user: String, map: ServiceMapsEntity) {
        val config = commonDaoServices.findIntegrationConfiguration("SAGE_API_CLIENT")
        val configUrl = config.url ?: throw Exception("URL CANNOT BE NULL")
        runBlocking {

            val headerBody = Header().apply {
                serviceName = config.account
                messageID = "SAGEREF${generateRandomText(3, map.secureRandom, map.messageDigestAlgorithm, true).toUpperCase()}"
                connectionID = jasyptStringEncryptor.decrypt(config.username)
                connectionPassword = jasyptStringEncryptor.decrypt(config.password)

            }

            val requestBody = mutableMapOf<String, Any>()
            val tmpRequest = SageRequest.fromEntity(demandNote)
            requestBody["header"] = headerBody
            requestBody["request"] = tmpRequest
            requestBody["details"] = RequestItems.fromList(invoiceDaoService.findDemandNoteItemsCdId(demandNote.id!!))
            // Send and log request
            val log = daoService.createTransactionLog(0, "${demandNote.demandNoteNumber}_1")
            val resp = daoService.getHttpResponseFromPostCall(
                    false,
                    configUrl + "urls/inspectionfee.php ",
                    null,
                    requestBody,
                    config,
                    null,
                    null
            )
            // Check response code
            if (resp == null || resp.status.value != 200) {
                throw ExpectedDataNotFound("Received invalid response[${resp?.status?.value}]:${resp?.status?.description}")
            }
            val response: Triple<WorkflowTransactionsEntity, SagePostingResponseResult?, io.ktor.client.statement.HttpResponse?> =
                    daoService.processResponses(resp, log, configUrl, config)
            demandNote.varField2 = response.second?.response?.demandNoteNo
            demandNote.varField3 = response.second?.response?.responseDate?.toString()
            // update response
            with(demandNote) {
                varField5 = response.second?.header?.messageID
                varField6 = response.second?.header?.statusCode?.toString()
                varField7 = response.second?.header?.statusDescription
                postingReference = response.second?.response?.demandNoteNo?.toUpperCase()
                varField9 = response.second?.response?.responseDate?.toString()
                postingStatus = when (response.second?.header?.statusCode) {
                    200 -> map.activeStatus
                    else -> map.invalidStatus
                }
                modifiedBy = user
                modifiedOn = commonDaoServices.getTimestamp()
            }
        }
    }

    fun checkTransactionStatus(invoiceDetails: InvoiceBatchDetailsEntity, map: ServiceMapsEntity): PaymentStatusResult {
        val config = commonDaoServices.findIntegrationConfigurationEntity(applicationMapProperties.mapSageConfigIntegration)
        val configUrl = config.url ?: throw Exception("URL CANNOT BE NULL")
        val headerBody = Header().apply {
            serviceName = "BSKApp"
            messageID = "SAGEREF${generateRandomText(3, map.secureRandom, map.messageDigestAlgorithm, true).toUpperCase()}"
            connectionID = jasyptStringEncryptor.decrypt(config.username)
            connectionPassword = jasyptStringEncryptor.decrypt(config.password)

        }
        val requestBody = PaymentStatusBody().apply {
            documentNo = invoiceDetails.batchNumber
            paymentReferenceNo = invoiceDetails.receiptNumber
        }
        val statusRequest = PaymentStatusRequest().apply {
            header = headerBody
            request = requestBody
        }

//        val resp = daoService.getHttpResponseFromPostCall(
//                false,
//                configUrl,
//                null,
//                statusRequest,
//                config,
//                null,
//                null
//        )

        return PaymentStatusResult()

    }
}