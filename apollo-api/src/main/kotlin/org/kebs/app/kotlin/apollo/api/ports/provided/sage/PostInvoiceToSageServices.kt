package org.kebs.app.kotlin.apollo.api.ports.provided.sage

import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import mu.KotlinLogging
import org.jasypt.encryption.StringEncryptor
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DaoService
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.InvoiceDaoService
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.QADaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.sage.requests.*
import org.kebs.app.kotlin.apollo.api.ports.provided.sage.response.PaymentStatusResult
import org.kebs.app.kotlin.apollo.api.ports.provided.sage.response.RootResponse
import org.kebs.app.kotlin.apollo.api.ports.provided.sage.response.SageInvoicePostingResponseResult
import org.kebs.app.kotlin.apollo.api.ports.provided.sage.response.SagePostingResponseResult
import org.kebs.app.kotlin.apollo.common.dto.kappa.response.NotificationResponseValue
import org.kebs.app.kotlin.apollo.common.dto.sage.response.SageNotificationResponse
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.exceptions.InvalidInputException
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.common.utils.generateRandomText
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.CdDemandNoteEntity
import org.kebs.app.kotlin.apollo.store.model.ServiceMapsEntity
import org.kebs.app.kotlin.apollo.store.model.WorkflowTransactionsEntity
import org.kebs.app.kotlin.apollo.store.model.invoice.BillPayments
import org.kebs.app.kotlin.apollo.store.model.invoice.CorporateCustomerAccounts
import org.kebs.app.kotlin.apollo.store.model.invoice.InvoiceBatchDetailsEntity
import org.kebs.app.kotlin.apollo.store.model.invoice.LogStgPaymentReconciliationDetailsToSageEntity
import org.kebs.app.kotlin.apollo.store.repo.IInvoiceResponceStatusEntityRepo
import org.kebs.app.kotlin.apollo.store.repo.ILogStgPaymentReconciliationDetailsToSageRepo
import org.kebs.app.kotlin.apollo.store.repo.IStagingPaymentReconciliationRepo
import org.kebs.app.kotlin.apollo.store.repo.IWorkflowTransactionsRepository
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.DisabledException
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.Instant


@Service
class PostInvoiceToSageServices(
    private val invoiceResponceStatusEntityRepo: IInvoiceResponceStatusEntityRepo,
    private val stagingRepo: IStagingPaymentReconciliationRepo,
    private val sageRepo: ILogStgPaymentReconciliationDetailsToSageRepo,
    private val jasyptStringEncryptor: StringEncryptor,
    private val applicationMapProperties: ApplicationMapProperties,
    private val iLogStgPaymentReconciliationDetailsToSageRepo: ILogStgPaymentReconciliationDetailsToSageRepo,
    private val commonDaoServices: CommonDaoServices,
    private val logsRepo: IWorkflowTransactionsRepository,
    private val daoService: DaoService,
    private val qaDaoServices: QADaoServices,
    private val invoiceDaoService: InvoiceDaoService
) {
    val invoiceStatus = invoiceResponceStatusEntityRepo.findByStatus(1)

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
            // Add bill reference number if applicable
            tmpRequest.billRefNumber = ""
            if (demandNote.billId != null) {
                invoiceDaoService.findBillDetails(demandNote.billId!!)?.let {
                    tmpRequest.billRefNumber = it.billRefNumber
                }
            }
            requestBody["header"] = headerBody
            requestBody["request"] = tmpRequest
            requestBody["details"] = RequestItems.fromList(invoiceDaoService.findDemandNoteItemsCdId(demandNote.id!!))
            // Send and log request
            val log = daoService.createTransactionLog(0, "${demandNote.demandNoteNumber}_1")
            val resp = daoService.getHttpResponseFromPostCall(
                    false,
                    "$configUrl/bsk/urls/inspectionfee.php ",
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

    fun postInvoiceTransactionToSage(billPayment: BillPayments, user: String, corporate: CorporateCustomerAccounts, map: ServiceMapsEntity) {
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
            val tmpRequest = SageInvoiceRequest.fromEntity(billPayment, corporate)
            requestBody["header"] = headerBody
            requestBody["request"] = tmpRequest
            requestBody["details"] = InvoiceRequestItems.fromList(invoiceDaoService.findBillTransactions(billPayment.id), "Account Line")
            // Send and log request
            val log = daoService.createTransactionLog(0, "${billPayment.billNumber}_1")
            val resp = daoService.getHttpResponseFromPostCall(
                    false,
                    "$configUrl/m-api/urls/sageinvoice.php",
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
            val response: Triple<WorkflowTransactionsEntity, SageInvoicePostingResponseResult?, io.ktor.client.statement.HttpResponse?> =
                    daoService.processResponses(resp, log, configUrl, config)
            billPayment.varField2 = response.second?.response?.demandNoteNo
            billPayment.varField3 = response.second?.response?.responseDate?.toString()
            // update response
            with(billPayment) {
                varField1 = response.second?.header?.messageID
                varField2 = response.second?.header?.statusCode?.toString()
                varField3 = response.second?.header?.statusDescription
                paymentRequestReference = response.second?.response?.demandNoteNo?.toUpperCase()
                paymentRequestDate = response.second?.response?.responseDate
                varField4 = response.second?.response?.responseDate?.toString()
                postingStatus = when (response.second?.header?.statusCode) {
                    200 -> map.activeStatus
                    else -> map.invalidStatus
                }
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


    fun postInvoiceTransactionToSageQa(stgID: Long, invoiceAccountDetails: InvoiceDaoService.InvoiceAccountDetails, user: String, map: ServiceMapsEntity) {
        val config = commonDaoServices.findIntegrationConfigurationEntity(applicationMapProperties.mapSageConfigIntegrationQa)
        val configUrl = config.url ?: throw Exception("URL CANNOT BE NULL")
        val invoiceFound = invoiceDaoService.findInvoiceStgReconciliationDetailsByID(stgID)
        runBlocking {

            val headerBody = SageQAHeader().apply {
                serviceName = "BSKApp"
                messageID = "BSK"
                connectionID = jasyptStringEncryptor.decrypt(config.username)
                connectionPassword = jasyptStringEncryptor.decrypt(config.password)
            }
            val requestBody = SageQARequest().apply {
                BatchNo = ""
                DocumentDate = commonDaoServices.convertTimestampToKeswsValidDate(commonDaoServices.getTimestamp())
                InvoiceType = 4
                ServiceType = "QA"
                CurrencyCode = "KES"
                CustomerCode = null
                CustomerName = invoiceAccountDetails.accountName
                InvoiceDesc = "QA ${invoiceFound.referenceCode} Billing"
                InvoiceAmnt = invoiceFound.invoiceAmount
                TaxPINNo = invoiceAccountDetails.accountNumber

            }

            val detailBody = SageQADetails().apply {
                RevenueAcc = invoiceAccountDetails.reveneCode
                RevenueAccDesc = invoiceAccountDetails.revenueDesc
                Taxable = 1
                MAmount = invoiceFound.invoiceAmount
                TaxAmount = invoiceFound.invoiceTaxAmount
            }

            val list = mutableListOf<SageQADetails>()
            list.add(detailBody)


            val rootRequest = SageQARequestBody().apply {
                header = headerBody
                request = requestBody
                details = list
            }

            var transactionsRequest = LogStgPaymentReconciliationDetailsToSageEntity()
            with(transactionsRequest) {
                stgPaymentId = invoiceFound.id
                serviceName = rootRequest.header?.serviceName
                requestMessageId = rootRequest.header?.messageID
                connectionId = rootRequest.header?.connectionID
                connectionPassword = rootRequest.header?.connectionPassword
//                requestDocumentNo = rootRequest.request?.documentNo
                documentDate = rootRequest.request?.DocumentDate.toString()
//                docType = rootRequest.request?.docType.toString()
                currencyCode = rootRequest.request?.CurrencyCode
                customerCode = rootRequest.request?.CustomerCode
                customerName = rootRequest.request?.CustomerName
                invoiceDesc = rootRequest.request?.InvoiceDesc
                revenueAcc = detailBody.RevenueAcc
                revenueAccDesc = detailBody.RevenueAccDesc
                taxable = rootRequest.details!![0].Taxable.toString()
                invoiceAmnt = rootRequest.details!![0].MAmount.toString()
                status = 0
                createdBy = user
                createdOn = commonDaoServices.getTimestamp()
            }

            transactionsRequest = withContext(Dispatchers.IO) {
                iLogStgPaymentReconciliationDetailsToSageRepo.save(transactionsRequest)
            }


            val log = daoService.createTransactionLog(0, "${invoiceFound.referenceCode}_1")
            val resp = daoService.getHttpResponseFromPostCall(false, configUrl, null, rootRequest,
                config,
                null,
                null
            )
            val response: Triple<WorkflowTransactionsEntity, SageQaPostingResponseResult?, io.ktor.client.statement.HttpResponse?> =
                daoService.processResponses(resp, log, configUrl, config)

            with(transactionsRequest) {
                responseMessageId = response.second?.header?.messageID
                statusCode = response.second?.header?.statusCode.toString()
                statusDescription = response.second?.header?.statusDescription
                responseDocumentNo = response.second?.response?.documentNo
                responseDate = response.second?.response?.responseDate
                status = 1
                modifiedBy = user
                modifiedOn = commonDaoServices.getTimestamp()
            }

            withContext(Dispatchers.IO) {
                iLogStgPaymentReconciliationDetailsToSageRepo.save(transactionsRequest)
            }

            when (response.second?.header?.statusCode) {
                200 -> {
                    with(invoiceFound){
                        sageInvoiceNumber = response.second?.response?.documentNo
                    }
                    val stgReconDetails = invoiceDaoService.updateStgReconciliationTableDetails(invoiceFound,user)

                    var batchInvoiceDetails = invoiceDaoService.findInvoiceBatchDetails(stgReconDetails.invoiceId?:throw  ExpectedDataNotFound("Missing Invoice Batch ID"))
                    with(batchInvoiceDetails){
                        sageInvoiceNumber = response.second?.response?.documentNo
                        KotlinLogging.logger { }.error { "Sage Invoice Number: $sageInvoiceNumber" }

                    }
                    batchInvoiceDetails =invoiceDaoService.updateInvoiceBatchDetails(batchInvoiceDetails,user)

                    var qaBatchInvoice = qaDaoServices.findBatchInvoicesWithRefNO(batchInvoiceDetails.batchNumber?:throw  ExpectedDataNotFound("Missing Invoice QA Ref No"))
                    with(qaBatchInvoice){
                        sageInvoiceNumber = response.second?.response?.documentNo
                    }


                    qaDaoServices.updateQAInvoiceBatchDetails(qaBatchInvoice,user)
                }
                else -> {
                    val gson = Gson()
                    KotlinLogging.logger {  }.error { "Request Response: ${gson.toJson(response.second)}" }
                }
            }

        }
    }


    fun processPaymentSageNotification(value: SageNotificationResponse): NotificationResponseValue {
        val result = NotificationResponseValue()
        val loggedInUSer = commonDaoServices.loggedInUserDetails()
        val log = daoService.createTransactionLog(0, daoService.generateTransactionReference())
        try {
            log.integrationRequest = daoService.mapper().writeValueAsString(value)
            /**
             * Attempt to log in
             */
            try {
//                reactiveAuthenticationManager.authenticate(
//                    UsernamePasswordAuthenticationToken(
//                        value.header?.connectionID,
//                        value.header?.connectionPassword
//                    )
//                ).awaitSingleOrNull()

                value.request?.billReferenceNo
                    ?.let { code ->
                        stagingRepo.findBySageInvoiceNumber(code)
                            ?.let { record ->
                                when {
                                    record.transactionId != null -> {
                                        result.responseDate = Timestamp.from(Instant.now())
                                        result.statusCode = invoiceStatus.duplicateTransaction
                                        result.statusDescription = record.statusDescription
                                        return result

                                    }
                                    else -> {
                                        log.integrationRequest = daoService.mapper().writeValueAsString(value)
                                        result.billReferenceCode = record.sageInvoiceNumber
                                        record.paidAmount = value.request?.paymentAmount
                                        record.paymentSource = value.request?.paymentCode
                                        record.paymentTransactionDate = value.request?.paymentDate
                                        record.transactionId = value.request?.paymentReferenceNo
                                        record.customerName = record.customerName?.let { value.header?.messageID }
                                            ?: "${record.customerName}|${value.header?.messageID}"
                                        record.extras = record.extras?.let { "${value.request?.additionalInfo}" }
                                            ?: "${record.extras}|${value.request?.additionalInfo}"
                                        record.paymentTablesUpdatedStatus = 1
                                        record.modifiedBy = commonDaoServices.concatenateName(loggedInUSer)
                                        record.modifiedOn = commonDaoServices.getTimestamp()
                                        stagingRepo.save(record)
                                        /**
                                         * Sage Update Details on sage logs
                                         * */
                                        /**
                                         * Sage Update Details on sage logs
                                         * */
                                        sageRepo.findByStgPaymentId(record.id ?: throw Exception("MISSING STAGING ID"))
                                            ?.let { sage ->
                                                sage.status = 10
                                                sage.description = daoService.mapper().writeValueAsString(value)
                                                sage.modifiedBy = commonDaoServices.concatenateName(loggedInUSer)
                                                sage.modifiedOn = commonDaoServices.getTimestamp()
                                                sageRepo.save(sage)
                                            }
                                        /**
                                         * TODO: Transverse to a different status as a payment has now been received
                                         */
                                        /**
                                         * TODO: Transverse to a different status as a payment has now been received
                                         */

                                        result.responseDate = Timestamp.from(Instant.now())
                                        result.statusCode = invoiceStatus.success
                                        result.statusDescription = record.statusDescription


                                        log.integrationResponse = daoService.mapper().writeValueAsString(result)
                                        log.responseStatus = invoiceStatus.success
                                        log.responseMessage = result.billReferenceCode
                                        log.transactionCompletedDate = Timestamp.from(Instant.now())
                                        logsRepo.save(log)
                                        return result

                                    }
                                }

                            }
                            ?: run {
                                result.responseDate = Timestamp.from(Instant.now())
                                result.statusCode = invoiceStatus.notFound
                                result.statusDescription = ""
                                return result
                            }
                    }
                    ?: throw NullValueNotAllowedException("Invalid Reference code")

//                validateCredentialsAndLogToDataStore(paymentRequest, log, result)


            } catch (e: DisabledException) {
                throw InvalidInputException("90004,NOK, Invalid account status")
            } catch (e: BadCredentialsException) {
                throw InvalidInputException("90004,NOK, Invalid credentials")
            } catch (e: Exception) {
                throw InvalidInputException("90004,NOK, ${e.message}")

            }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            log.responseStatus = invoiceStatus.error
            log.responseMessage = e.message


            result.responseDate = Timestamp.from(Instant.now())
            result.statusCode = log.responseStatus
            result.statusDescription = log.responseMessage

            log.integrationResponse = daoService.mapper().writeValueAsString(result)

            log.transactionCompletedDate = Timestamp.from(Instant.now())
            logsRepo.save(log)
            throw e

        }
    }
}
