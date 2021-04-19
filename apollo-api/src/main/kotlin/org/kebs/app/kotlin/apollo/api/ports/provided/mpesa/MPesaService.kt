package org.kebs.app.kotlin.apollo.api.ports.provided.mpesa


import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import org.jasypt.encryption.StringEncryptor
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DaoService
import org.kebs.app.kotlin.apollo.api.ports.provided.mpesa.requests.MpesaLoginRequest
import org.kebs.app.kotlin.apollo.api.ports.provided.mpesa.requests.MpesaPushRequest
import org.kebs.app.kotlin.apollo.api.ports.provided.mpesa.requests.MpesaTransactionsRequest
import org.kebs.app.kotlin.apollo.api.ports.provided.mpesa.response.MpesaPushResponse
import org.kebs.app.kotlin.apollo.common.dto.eac.responses.TokenRequestResult
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.repo.IIntegrationConfigurationRepository
import org.kebs.app.kotlin.apollo.store.repo.IMpesaTransactionsRepository
import org.springframework.stereotype.Service
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
        private  val applicationMapProperties: ApplicationMapProperties,
        private val configurationRepository: IIntegrationConfigurationRepository

) {


    fun mainMpesaTransaction(
        amount: String,
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
//        val callBack = commonDaoServices.findBatchJobDetails(applicationMapProperties.mapMpesaConfigIntegrationCallBackJob)
        val loginUrl = config.url.toString()
        val pushUrl = push.jobUri.toString()
//        val callBackUrl = callBack.jobUri.toString()

        runBlocking {

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
                if (token.isNullOrEmpty()) {
                    config = loginRequest(loginUrl, transactionRef, config)
                }
            }
            when {
                commonDaoServices.getTimestamp() > config.tokenTimeExpires -> {
                    config = loginRequest(loginUrl, transactionRef, config)
                }
                else -> {
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

//    private fun sampleExtractTokenFromHeader(tokenResponse: HttpResponse?): String? {
//        tokenResponse
//                ?.let { response ->
//                    return response.headers["Authorization"]
//                }
//                ?: throw NullValueNotAllowedException("Response cannot be empty")
//    }

//    private fun updateMpesaTransactionEntity(merchantCode: String, checkOutCode: String, response: MpesaValidationResponse, map: ServiceMapsEntity): MpesaTransactionEntity {
//        val mpesaTransaction = findMpesaTransactionEntity(merchantCode, checkOutCode)
//        with(mpesaTransaction) {
//            phonenumber = response.responsePhoneNumber.toString()
//            mpesareceiptnumber = response.responseReceiptNumber?.toString()
//            amount = response.responseAmount?.toBigDecimalOrNull()
//            transactiondate = commonDaoServices.getCurrentDate()
//            usedTransactionReference = map.activeStatus
//            status = map.activeStatus
//            modifiedBy = createdBy
//            modifiedOn = commonDaoServices.getTimestamp()
//        }
//        return iMpesaTransactionsRepo.save(mpesaTransaction)
//    }

//    private suspend fun callBackRequest(url: String, transactionRef: String, config: IntegrationConfigurationEntity, headerParameters: MutableMap<String, String>): Pair<WorkflowTransactionsEntity, MpesaValidationResponse?> {
//        val validationRequest = MpesaValidationRequest()
//        validationRequest.transactionReference = config.transactionReference
//        validationRequest.account = config.account
//        validationRequest.accountReference = config.accountReference
//
//        val log2 = daoService.createTransactionLog(0, "${transactionRef}_2")
//        val resp1 = daoService.getHttpResponseFromPostCall(false, url, null, validationRequest, config, null, headerParameters)
//        return daoService.processResponses(resp1, log2, url, config)
//    }

    private suspend fun pushRequest(
        url: String,
        amount: String,
        phoneNumber: String,
        invoiceReference: String,
        transactionRef: String,
        config: IntegrationConfigurationEntity,
        headerParameters: MutableMap<String, String>
    ): Pair<WorkflowTransactionsEntity, MpesaPushResponse?> {
        val request = MpesaPushRequest()
        request.transactionReference = invoiceReference
        request.account = config.account
        request.amount = amount
        request.subscriberId = phoneNumber

        val log = daoService.createTransactionLog(0, "${transactionRef}_1")
        val resp = daoService.getHttpResponseFromPostCall(false, url, null, request, config, null, headerParameters)
        return daoService.processResponses(resp, log, url, config)
    }

    private suspend fun loginRequest(url: String, transactionRef: String, config: IntegrationConfigurationEntity): IntegrationConfigurationEntity {
        val loginRequest = MpesaLoginRequest()
        loginRequest.username = jasyptStringEncryptor.decrypt(config.username)
        loginRequest.password = jasyptStringEncryptor.decrypt(config.password)

        val log2 = daoService.createTransactionLog(0, "${transactionRef}_0")
        val tokenResponse = daoService.getHttpResponseFromPostCall(false, url, null, loginRequest, config, null, null)
        val data = daoService.processResponses<String>(tokenResponse, log2, url, config)
        val tokenResult = data.second ?: throw ExpectedDataNotFound("TOKEN NOT FOUND")
//        var respToken = sampleExtractTokenFromHeader(tokenResponse)
//        if (tokenResult.isNullOrEmpty()){
//            respToken = ""
//        }
        return saveTokenToConfigIntegration(tokenResult, config)
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
                        //                Toast.makeText(getApplicationContext(), "Safaricom Number", Toast.LENGTH_LONG).show()
                        val replPhone3: String
                        //                phoneCompany = "safaricom"
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
                    else -> {
                        patt = Pattern.compile(airtel)
                        match = patt.matcher(replPhone2)
                        when {
                            match.find() -> {
                                //                    Toast.makeText(getApplicationContext(), "Airtel Number", Toast.LENGTH_LONG).show()
                                val replPhone3: String
                                //                    phoneCompany = "airtel"
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
                            else -> {

                                patt = Pattern.compile(telkom)
                                match = patt.matcher(replPhone2)
                                when {
                                    match.find() -> {
                                        //                        Toast.makeText(getApplicationContext(), "Telkom Number", Toast.LENGTH_LONG).show()
                                        val replPhone3: String
                                        //                        phoneCompany = "telkom"
                                        when {
                                            replPhone2.startsWith("0") -> {
                                                replPhone3 = replPhone2.replaceFirst("0".toRegex(), "\\254")
                                                //                            Log.e("TAG phone starts 0", replPhone3)
                                                validPhoneNo = replPhone3
                                            }
                                            replPhone2.startsWith("7") -> {
                                                replPhone3 = replPhone2.replaceFirst("7".toRegex(), "\\254")
                                                //                            Log.e("TAG phone starts 7", replPhone3)
                                                validPhoneNo = replPhone3
                                            }
                                            replPhone2.startsWith("+") -> {
                                                validPhoneNo = replPhone2.replace("[\\-+.^:,]".toRegex(), "")
                                                //                            Log.e("TAG phone number +", validPhoneNo)
                                            }
                                            else -> {
                                                validPhoneNo = replPhone2
                                            }
                                        }
                                    }
                                    else -> {
                                        //                        Toast.makeText(getApplicationContext(), "Please enter a valid mobile number 'Safaricom only'", Toast.LENGTH_LONG).show()
                                        //                        Log.e("TAG phone No not check", replPhone2)
                                        //                        moveToContact(view)
                                    }
                                }
                            }
                        }
                    }
                }
            }
            else -> {
//            Toast.makeText(getApplicationContext(), "Please enter a mobile number ", Toast.LENGTH_LONG).show()
//            moveToContact(view)
            }
        }
        return validPhoneNo
    }


}