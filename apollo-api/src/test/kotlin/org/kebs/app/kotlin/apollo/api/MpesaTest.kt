package org.kebs.app.kotlin.apollo.api


import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.ktor.client.statement.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import org.codehaus.jackson.annotate.JsonProperty
import org.jasypt.encryption.StringEncryptor
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DaoService
import org.kebs.app.kotlin.apollo.api.ports.provided.mpesa.MPesaService
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.repo.IBatchJobDetailsRepository
import org.kebs.app.kotlin.apollo.store.repo.IIntegrationConfigurationRepository
import org.kebs.app.kotlin.apollo.store.repo.IMpesaTransactionsRepository
import org.kebs.app.kotlin.apollo.store.repo.IWorkflowTransactionsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.sql.Timestamp
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*


@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MpesaTest {

    @Autowired
    lateinit var daoService: DaoService

    @Autowired
    lateinit var applicationMapProperties: ApplicationMapProperties

    @Autowired
    lateinit var commonDaoServices: CommonDaoServices

    @Autowired
    lateinit var iMpesaTransactionsRepo: IMpesaTransactionsRepository

    @Autowired
    lateinit var configurationRepository: IIntegrationConfigurationRepository

    @Autowired
    lateinit var batchJobRepository: IBatchJobDetailsRepository

    @Autowired
    lateinit var logsRepo: IWorkflowTransactionsRepository

    @Autowired
    lateinit var jasyptStringEncryptor: StringEncryptor

    @Autowired
    lateinit var mpesaServices: MPesaService

//
//    @Test
//    fun mpesaTest() {
//        mpesaServices.mainMpesaTransaction("1", "254715668934",809, "kpaul7747@gmail.com", null)
//    }

    @Test
    fun hashString() {
        val plainText = listOf("BSKL3tm31n2021", "foo")

        plainText.forEach {
            val hashed = jasyptStringEncryptor.encrypt(it)
            KotlinLogging.logger { }.info { "my hashed value =$it =  $hashed" }
        }


    }

    @Test
    fun unHashString() {
        val hashed = listOf("dNQ60pPzbf+4J3+33XXDZUyF8zgpohBv")
        hashed.forEach {
            val plainText = jasyptStringEncryptor.decrypt(it)
            KotlinLogging.logger { }.info { plainText }
        }

    }

    @Test
    fun postApiCallTest() {
        val appId = applicationMapProperties.mapImportInspection
        val map = commonDaoServices.serviceMapDetails(appId)
        var config = findIntegrationConfigurationEntity(applicationMapProperties.mapMpesaConfigIntegration)
        val push = findBatchJobDetails(applicationMapProperties.mapMpesaConfigIntegrationPushJob)
        val callBack = findBatchJobDetails(applicationMapProperties.mapMpesaConfigIntegrationCallBackJob)
        val loginUrl = config.url.toString()
        val pushUrl = push.jobUri.toString()
        val callBackUrl = callBack.jobUri.toString()

        runBlocking {

            val transactionsRequest = MpesaValidationRequest()
            transactionsRequest.transactionReference = config.transactionReference
            transactionsRequest.account = config.account
            transactionsRequest.accountReference = config.accountReference

            val transactionRef = daoService.generateTransactionReference()
            val log = daoService.createTransactionLog(0, transactionRef)
            log.integrationRequest = daoService.mapper().writeValueAsString(transactionsRequest)

            if (commonDaoServices.getTimestamp()>config.tokenTimeExpires){
                config = loginRequest(loginUrl,transactionRef, config)
            }else{
                val headerParameters = mutableMapOf<String, String>()
                config.token.let { headerParameters["Authorization"] =it}
                KotlinLogging.logger {  }.info("$headerParameters")

                val response =  pushRequest(pushUrl,"1","254715668934",transactionRef,config,headerParameters).second
                response?.merchantRequestID
                        ?.let { merchantCode ->
                            val checkOutCode = response.checkoutRequestID.toString()
                            val mpesaTransaction = mpesaTransactionEntity(810L, merchantCode, "kpaul7747@gmail.com", checkOutCode, map)
                            KotlinLogging.logger { }.info { "MPESA TRANSACTION DONE ID = ${mpesaTransaction.id}" }

                            delay(20000L);
                            var hasResults = false
                            while (!hasResults) {
                                val data2 = callBackRequest(callBackUrl, transactionRef, config, headerParameters)
                                val response2 = data2.second
                                response2?.merchantRequestID
                                        ?.let { merchantCode2 ->
                                            (response2.checkoutRequestID?.let { commonDaoServices.makeAnyNotBeNull(it) } as String).apply {
                                                updateMpesaTransactionEntity(merchantCode2, this, response2, map)
                                            }
                                            hasResults = true
                                        }
                                delay(1000L);

                            }
                        }
                        ?: run {
                            log.transactionStatus = 20
                            log.transactionCompletedDate = Timestamp.from(Instant.now())
                            KotlinLogging.logger { }.error("Failed")
                        }

            }

        }
    }

    private fun findBatchJobDetails(batchJobID: Long): BatchJobDetails {
        batchJobRepository.findByIdOrNull(batchJobID)
                ?.let {
                    return it
                }
                ?:throw ExpectedDataNotFound("Batch Job Details With the following ID $batchJobID, does not exist")
    }

    private fun findIntegrationConfigurationEntity(configID: Long): IntegrationConfigurationEntity {
        configurationRepository.findByIdOrNull(configID)
                ?.let {
                    return it
                }
                ?:throw ExpectedDataNotFound("Configuration With the following ID $configID, does not exist")
    }

    private fun saveTokenToConfigIntegration(respToken: String, config: IntegrationConfigurationEntity): IntegrationConfigurationEntity {
        with(config) {
            token = respToken
            tokenTimeGenerated = commonDaoServices.getTimestamp()
            tokenTimeExpires = getExpirationTime(tokenTimeLapse)
        }
        return configurationRepository.save(config)
    }

    private fun getExpirationTime(expirationTime: Int): Timestamp? {
        val cal = Calendar.getInstance()
        cal.add(Calendar.HOUR_OF_DAY, expirationTime)
        val dateFormat: DateFormat = SimpleDateFormat("MM/dd/yyyy HH:mm:ss")
        val timeResult = dateFormat.format(cal.time)
        return Timestamp.valueOf(timeResult)
    }

    private fun getCurrentTime(): String {
        val dateFormat: DateFormat = SimpleDateFormat("MM/dd/yyyy HH:mm:ss")
        val date = Date()
        val timeResult = dateFormat.format(date)
        return timeResult
    }

    private fun sampleExtractTokenFromHeader(tokenResponse: HttpResponse?): String? {
        tokenResponse
                ?.let { response ->
                    return response.headers["Authorization"]
                }
                ?: throw NullValueNotAllowedException("Response cannot be empty")
    }

    private fun updateMpesaTransactionEntity(merchantCode: String, checkOutCode: String, response: MpesaValidationResponse, map: ServiceMapsEntity): MpesaTransactionEntity {
        val mpesaTransaction = findMpesaTransactionEntity(merchantCode, checkOutCode)
        with(mpesaTransaction) {
            phonenumber = response.responsePhoneNumber.toString()
            mpesareceiptnumber = response.responseReceiptNumber?.toString()
            amount = response.responseAmount?.toBigDecimalOrNull()
            transactiondate = commonDaoServices.getCurrentDate()
            usedTransactionReference = map.activeStatus
            status = map.activeStatus
            modifiedBy = createdBy
            modifiedOn = commonDaoServices.getTimestamp()
        }
        return iMpesaTransactionsRepo.save(mpesaTransaction)
    }

    private suspend fun callBackRequest(url: String, transactionRef: String, config: IntegrationConfigurationEntity, headerParameters: MutableMap<String, String>): Pair<WorkflowTransactionsEntity, MpesaValidationResponse?> {
        val validationRequest = MpesaValidationRequest()
        validationRequest.transactionReference = config.transactionReference
        validationRequest.account = config.account
        validationRequest.accountReference = config.accountReference

        val log2 = daoService.createTransactionLog(0, "${transactionRef}_2")
        val resp1 = daoService.getHttpResponseFromPostCall(false, url, null, validationRequest, config, null, headerParameters)
        return daoService.processResponses(resp1, log2, url, config)
    }

    private suspend fun pushRequest(url: String, amount: String, phoneNumber: String, transactionRef: String, config: IntegrationConfigurationEntity, headerParameters: MutableMap<String, String>): Pair<WorkflowTransactionsEntity, MpesaPushResponse?> {
        val request = MpesaPushRequest()
        request.transactionReference = config.transactionReference
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

        daoService.createTransactionLog(0, "${transactionRef}_0")
        val tokenResponse = daoService.getHttpResponseFromPostCall(false, url, null, loginRequest, config, null, null)
        var respToken = sampleExtractTokenFromHeader(tokenResponse)
        if (respToken.isNullOrEmpty()){
            respToken = ""
        }
        return saveTokenToConfigIntegration(respToken, config)
    }

//@Test
//fun getApiCallTest() {
//
//    configurationRepository.findByIdOrNull(3L)
//            ?.let { config ->
//                config.createdOn = Timestamp.from(Instant.now())
//                config.modifiedOn = Timestamp.from(Instant.now())
//                configurationRepository.save(config)
//                runBlocking {
//                    config.url?.let { url ->
//
//                        val log = daoService.createTransactionLog(0, "test")
//                        val params = mapOf(Pair("registration_number", "CPR/2013/117374"))
//                        log.integrationRequest = "$params"
//                        val resp = daoService.getHttpResponseFromGetCall(true, url, config, null, params, null)
//                        val data = daoService.processResponses<BrsLookUpResponse>(resp, log, url, config)
//                        KotlinLogging.logger { }.debug("${data.second?.count} ${data.second?.records?.get(0)?.partners?.get(0)?.name}")
//
//                        logsRepo.save(data.first)
//                    }
//                }
//            }
//
//}
//}


    private fun mpesaTransactionEntity(invoiceID: Long, merchantCode: String, userName: String, checkOutCode: String, map: ServiceMapsEntity): MpesaTransactionEntity {
        val mpesaTransaction = MpesaTransactionEntity()
        with(mpesaTransaction) {
            merchantRequestId = merchantCode
            checkoutRequestId = checkOutCode
            status = map.activeStatus
            usedTransactionReference = map.inactiveStatus
            createdBy = userName
            createdOn = Timestamp.from(Instant.now())
            invoiceId = invoiceID
        }
        return iMpesaTransactionsRepo.save(mpesaTransaction)
    }

    private fun findMpesaTransactionEntity(merchantCode: String, checkOutCode: String): MpesaTransactionEntity {
        iMpesaTransactionsRepo.findByMerchantRequestIdAndCheckoutRequestId(merchantCode, checkOutCode)
                ?.let { transaction ->
                    return transaction
                }
                ?: throw ExpectedDataNotFound("Transaction With the following $merchantCode and $checkOutCode, does not exist")
    }


    class MpesaValidationResponse {
        val responseDescription: String? = null
        val merchantRequestID: String? = null
        val checkoutRequestID: String? = null
        val resultCode: String? = null
        val resultDesc: String? = null
        val responseCode: String? = null
        val responsePhoneNumber: String? = null
        val responseReceiptNumber: String? = null
        val responseAmount: String? = null
    }

    class MpesaValidationRequest {

        var account: String? = null
        var transactionReference: String? = null
        var accountReference: String? = null
    }

    class MpesaPushRequest {
        var subscriberId: String? = null
        var amount: String? = null
        var account: String? = null
        var transactionReference: String? = null
//        var reference: String? = null

    }

    class MpesaLoginRequest {
        var username: String? = null
        var password: String? = null
    }

    class MpesaTransactionsRequest {
        var username: String? = null
        var password: String? = null
    }

    @JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy::class)
    class MpesaPushResponse {
        @JsonProperty("MerchantRequestID")
        val merchantRequestID: String? = null

        @JsonProperty("CheckoutRequestID")
        val checkoutRequestID: String? = null

        @JsonProperty("ResponseCode")
        val responseCode: String? = null

        @JsonProperty("ResponseDescription")
        val responseDescription: String? = null

        @JsonProperty("DefaultError")
        val defaultError: String? = null

        @JsonProperty("CustomerMessage")
        val customerMessage: String? = null

        @JsonProperty("ResultCode")
        val resultCode: String? = null

        @JsonProperty("ResultDesc")
        val resultDesc: String? = null
    }


}

