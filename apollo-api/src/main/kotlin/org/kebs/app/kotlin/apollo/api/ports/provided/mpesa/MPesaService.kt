package org.kebs.app.kotlin.apollo.api.ports.provided.mpesa


import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import io.ktor.client.statement.*
import org.jasypt.encryption.StringEncryptor
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DaoService
import org.kebs.app.kotlin.apollo.api.ports.provided.mpesa.requests.MpesaPushRequest
import org.kebs.app.kotlin.apollo.api.ports.provided.mpesa.requests.MpesaTransactionsRequest
import org.kebs.app.kotlin.apollo.api.ports.provided.mpesa.requests.RequestHeader
import org.kebs.app.kotlin.apollo.api.ports.provided.mpesa.response.MpesaPushResponse
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.repo.IIntegrationConfigurationRepository
import org.kebs.app.kotlin.apollo.store.repo.IMpesaTransactionsRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.sql.Timestamp
import java.time.Instant
import java.util.regex.Matcher
import java.util.regex.Pattern


@Service
class MPesaService(
    private val iMpesaTransactionsRepo: IMpesaTransactionsRepository,
    private val commonDaoServices: CommonDaoServices,
    private val jasyptStringEncryptor: StringEncryptor,
    private val daoService: DaoService,
    private val applicationMapProperties: ApplicationMapProperties,
    private val configurationRepository: IIntegrationConfigurationRepository

) {


    fun mainMpesaTransaction(
        amount: BigDecimal,
        phoneNumber: String,
        invoiceReference: String,
        userName: String,
        invoiceSource: String
    ) {
        val appId = applicationMapProperties.mapInvoiceTransactions
        val map = commonDaoServices.serviceMapDetails(appId)
        var config =
            commonDaoServices.findIntegrationConfigurationEntity(applicationMapProperties.mapMpesaConfigIntegration)
        val push = commonDaoServices.findBatchJobDetails(applicationMapProperties.mapMpesaConfigIntegrationPushJob)
        val loginUrl = config.url.toString()
        val pushUrl = push.jobUri.toString()

        runBlocking {

            checkIfAmountReachedPerTransaction(amount)

            val transactionsRequest = MpesaTransactionsRequest()
            with(transactionsRequest) {
                username = userName
                subscriberId = phoneNumber
                amountToPay = amount
                transactionDate = commonDaoServices.getTimestamp()
                transactionReference = invoiceReference
            }

            val transactionRef = daoService.generateTransactionReference()
            val log = daoService.createTransactionLog(0, transactionRef)
            log.integrationRequest = daoService.mapper().writeValueAsString(transactionsRequest)
            config.token.let { token ->
                if (token.isEmpty()) {
                    config = loginRequest(loginUrl, transactionRef, config)
                }
            }
            if (commonDaoServices.getTimestamp() > config.tokenTimeExpires) {
                config = loginRequest(loginUrl, transactionRef, config)
            }

            val headerParameters = mutableMapOf<String, String>()
            config.token.let { headerParameters["Authorization"] = it }
            KotlinLogging.logger { }.info("$headerParameters")

            val response = pushRequest(
                pushUrl,
                amount,
                phoneNumber,
                invoiceReference,
                transactionRef,
                config,
                headerParameters
            ).second

            response?.merchantRequestID
                ?.let { merchantCode ->
                    val checkOutCode = response.checkoutRequestID.toString()
                    val mpesaTransaction = mpesaTransactionEntity(
                        invoiceReference,
                        invoiceSource,
                        merchantCode,
                        userName,
                        checkOutCode,
                        map
                    )
                    KotlinLogging.logger { }.info { "MPESA TRANSACTION DONE ID = ${mpesaTransaction.id}" }
                }
                ?: run {
                    log.transactionStatus = 20
                    log.transactionCompletedDate = Timestamp.from(Instant.now())
                    KotlinLogging.logger { }.error("Failed")
                }

        }
    }

    private fun saveTokenToConfigIntegration(
        respToken: String,
        config: IntegrationConfigurationEntity
    ): IntegrationConfigurationEntity {
        with(config) {
            token = respToken
            tokenTimeGenerated = commonDaoServices.getTimestamp()
            tokenTimeExpires = commonDaoServices.getExpirationTime(tokenTimeLapse)
        }
        return configurationRepository.save(config)
    }

    private fun sampleExtractTokenFromHeader(tokenResponse: HttpResponse?): String? {
        tokenResponse
            ?.let { response ->
                return response.headers["Authorization"]
            }
            ?: throw NullValueNotAllowedException("Response cannot be empty")
    }

    private fun checkIfAmountReachedPerTransaction(amount: BigDecimal) {
        if (amount > applicationMapProperties.mapMpesaConfigIntegrationMaxAmount) {
            throw ExpectedDataNotFound("THE AMOUNT TO PAY IS MORE THAN TRANSACTION AMOUNT APPROVED BY SAFARICOM")
        }
    }

    private suspend fun pushRequest(
        url: String,
        amount: BigDecimal,
        phoneNumber: String,
        invoiceReference: String,
        transactionRef: String,
        config: IntegrationConfigurationEntity,
        headerParameters: MutableMap<String, String>
    ): Triple<WorkflowTransactionsEntity, MpesaPushResponse?, HttpResponse?> {
        val request = MpesaPushRequest()
        request.transactionReference = invoiceReference
        request.account = config.account
        request.amount = amount
        request.subscriberId = phoneNumber

        val log = daoService.createTransactionLog(0, "${transactionRef}_1")
        val resp = daoService.getHttpResponseFromPostCall(false, url, null, request, config, null, headerParameters)
        return daoService.processResponses(resp, log, url, config)
    }

    private suspend fun loginRequest(
        url: String,
        transactionRef: String,
        config: IntegrationConfigurationEntity
    ): IntegrationConfigurationEntity {
        val loginRequest = RequestHeader()
        loginRequest.username = jasyptStringEncryptor.decrypt(config.username)
        loginRequest.password = jasyptStringEncryptor.decrypt(config.password)

        daoService.createTransactionLog(0, "${transactionRef}_0")
        val tokenResponse = daoService.getHttpResponseFromPostCall(false, url, null, loginRequest, config, null, null)
        var respToken = sampleExtractTokenFromHeader(tokenResponse)
        if (respToken.isNullOrEmpty()) {
            respToken = ""
        }
        return saveTokenToConfigIntegration(respToken, config)
    }

    private fun mpesaTransactionEntity(
        invoiceReference: String,
        InvoiceSource: String,
        merchantCode: String,
        userName: String,
        checkOutCode: String,
        map: ServiceMapsEntity
    ): MpesaTransactionEntity {
        val mpesaTransaction = MpesaTransactionEntity()
        with(mpesaTransaction) {
            merchantRequestId = merchantCode
            checkoutRequestId = checkOutCode
            status = map.activeStatus
            usedTransactionReference = map.inactiveStatus
            createdBy = userName
            createdOn = Timestamp.from(Instant.now())
            invoiceId = invoiceReference
            invoiceSource = InvoiceSource
        }
        return iMpesaTransactionsRepo.save(mpesaTransaction)
    }
//
//    private fun findMpesaTransactionEntity(merchantCode: String, checkOutCode: String): MpesaTransactionEntity {
//        iMpesaTransactionsRepo.findByMerchantRequestIdAndCheckoutRequestId(merchantCode, checkOutCode)
//                ?.let { transaction ->
//                    return transaction
//                }
//                ?: throw ExpectedDataNotFound("Transaction With the following $merchantCode and $checkOutCode, does not exist")
//    }

    fun findMpesaTransactionEntityByInvoice(invoiceID: Long, invoiceSource: String): MpesaTransactionEntity {
        iMpesaTransactionsRepo.findByInvoiceIdAndInvoiceSource(invoiceID, invoiceSource)
            ?.let { transaction ->
                return transaction
            }
            ?: throw ExpectedDataNotFound("Transaction With the following $invoiceID and $invoiceSource, does not exist")
    }


    fun sanitizePhoneNumber(inputPhone: String): String? {
        var validPhoneNo: String? = "Fasle"
        val safaricom = "^(?:254|\\+254|0)?(7(?:(?:[129][0-9])|(?:0[0-9])|(?:6[8-9])|(?:5[7-9])|(?:4[5-6])|(?:4[8])|(4[0-3]))[0-9]{6})$"
        val telkom = "^(?:254|\\+254|0)?(7(?:(?:[7][0-9]))[0-9]{6})$"
        val airtel = "^(?:254|\\+254|0)?(7(?:(?:[3][0-9])|(?:5[0-6])|(?:6[2])|(8[0-9]))[0-9]{6})$"
        var patt: Pattern
        var match: Matcher
        when {
            inputPhone.isNotEmpty() -> {
                val replPhone1 = inputPhone.trim { it <= ' ' }
                val replPhone2 = replPhone1.replace("\\s".toRegex(), "")
                patt = Pattern.compile(safaricom)
                match = patt.matcher(replPhone2)
                when {
                    match.find() -> {
                        val replPhone3: String
                        when {
                            replPhone2.startsWith("0") -> {
                                replPhone3 = replPhone2.replaceFirst("0".toRegex(), "\\254")
                                //                        Log.e("TAG phone starts 0", replPhone3)
                                validPhoneNo = replPhone3
                            }
                            replPhone2.startsWith("7") -> {
                                replPhone3 = replPhone2.replaceFirst("7".toRegex(), "\\254")
                                //                        Log.e("TAG phone starts 7", replPhone3)
                                validPhoneNo = replPhone3
                            }
                            replPhone2.startsWith("+") -> {
                                validPhoneNo = replPhone2.replace("[\\-+.^:,]".toRegex(), "")
                                //                        Log.e("TAG phone number +", validPhoneNo)
                            }
                            else -> {
                                validPhoneNo = replPhone2
                            }
                        }
                    }
                }
            }
            else -> {
                 throw ExpectedDataNotFound("Please Enter a valid Safaricom Phone Number")
            }
        }
        return validPhoneNo
    }


}