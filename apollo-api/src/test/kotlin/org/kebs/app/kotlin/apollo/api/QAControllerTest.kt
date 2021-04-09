/////*
//// *
//// *  *
//// *  *
//// *  *  *    Copyright (c) ${YEAR}.   BSK Global Technologies
//// *  *  *
//// *  *  *    Licensed under the Apache License, Version 2.0 (the "License");
//// *  *  *    you may not use this file except in compliance with the License.
//// *  *  *    You may obtain a copy of the License at
//// *  *  *
//// *  *  *       http://www.apache.org/licenses/LICENSE-2.0
//// *  *  *
//// *  *  *    Unless required by applicable law or agreed to in writing, software
//// *  *  *    distributed under the License is distributed on an "AS IS" BASIS,
//// *  *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//// *  *  *   See the License for the specific language governing permissions and
//// *  *  *   limitations under the License.
//// *  *
//// *
//// */
////
////package org.kebs.app.kotlin.apollo.api
////
////import org.junit.jupiter.api.Test
////import org.junit.runner.RunWith
////import org.kebs.app.kotlin.apollo.adaptor.kafka.producer.service.SendToKafkaQueue
////import org.kebs.app.kotlin.apollo.api.ports.provided.dao.QualityAssuranceDaoServices
////import org.kebs.app.kotlin.apollo.store.model.PermitApplicationEntity
////import org.kebs.app.kotlin.apollo.store.repo.*
////import org.springframework.beans.factory.annotation.Autowired
////import org.springframework.boot.test.context.SpringBootTest
////import org.springframework.test.context.junit4.SpringRunner
////
////
////@SpringBootTest
////@RunWith(SpringRunner::class)
////class QAControllerTest {
////
////    @Autowired
////    lateinit var sendToKafkaQueue: SendToKafkaQueue
////
////    @Autowired
////    lateinit var usersRepo: IUserRepository
////
////    @Autowired
////    lateinit var iTariffsDetailsRepository: ITariffsDetailsRepository
////
//////    @Autowired
//////    lateinit var iPvocPatnersCountries: IPvocPatnersCountries
////
////    @Autowired
////    lateinit var iPermitRepository: IPermitRepository
////
////    @Autowired
////    lateinit var daoservises: QualityAssuranceDaoServices
////
//////    @Autowired
//////    lateinit var iSampleCollectForm: ISampleCollectForm
////
////    @Autowired
////    lateinit var iManufacturerProductRepository: IManufacturerProductRepository
////
////    @Autowired
////    lateinit var iManufacturerProductBrandRepository: IManufacturerProductBrandRepository
////
////    @Autowired
////    lateinit var permitTypesEntityRepository: IPermitTypesEntityRepository
////
////    @Autowired
//////    lateinit var imanufacturerRepository: IManufacturerRepository
////
////
////    @Test
////    fun savePermitApplication() {
////        val permitApply = PermitApplicationEntity()
////        with(permitApply){
////            description = "This is a smark test Drive"
////            permitNumber = "DRF145855"
////            tradeMark = "bsk.supp.temp@gmail.com"
////            ksNumber = "varField1"
////        }
////
////        usersRepo.findByUserName("gkingori139@gmail.com")
////                ?.let { loggedInUser ->
////
////                    daoservises.updatePermit(permitApply,loggedInUser, daoservises.serviceMapDetails(), 4981)
////                }
////
////
////
////
////
////    }
//////
//////    fun getALLPermit() {
//////        val manufactureProductName: String = "Animal Feeds"
//////        val productBrandName: String = "Elianto Corn Oil"
//////        KotlinLogging.logger { }.info { "productIdNo Details  = " + iManufacturerProductBrandRepository.findByBrandName(productBrandName)?.productId.toString() }
//////        KotlinLogging.logger { }.info { "Results = " + (iPermitRepository.findByManufacturerId(1L)) }
//////    }
////
////
//////    @Test
//////    fun getAllPermitTypes() {
//////        val permitType = PermitTypesEntity()
//////
//////         val itemDetails = iConsignmentItems.findByIdOrNull(3)
////////        val maxValueLength1 = 10
////////        val first4 = itemDetails?.countryOfOrgin?.let { iPvocPatnersCountries.findByCountryName(it) }
//////        val countrNAME = itemDetails?.countryOfOrgin
//////        val first4 = countrNAME?.let { iPvocPatnersCountries.findByCountryNameContainingIgnoreCase(it)?.countryName }
////////        val secondAfterFirst4 = itemDetails?.itemHsCode?.substring(4,6)
////////        val afterSecondAfterFirst4 = itemDetails?.itemHsCode?.substring(6,8)
////////        val addToString = first4+"."+secondAfterFirst4+"."+afterSecondAfterFirst4
////////        permitType.id = permitTypesEntityRepository.findByTypeName("DMARK").toString().toInt()
////////        val count = permitTypesEntityRepository.findAll()
//////
//////
//////        KotlinLogging.logger { }.info { "ALl Permit stypes = " + first4 }
//////
//////    }
//////
//////    @Test
//////    fun MPESA() {
//////        val consumerKey = "rVOP1tEOosgDjV9sopKGGX2QfxQmqkxu" //consumer key
//////
//////        val consumerSecret = "mbIJOGRWMJdqWTVt" //consumer secret
//////
//////
//////        val onlinePayment = OnlinePayment()
//////        onlinePayment.accountReference = "KEBS"
//////        onlinePayment.businessShortCode = 600734
//////        onlinePayment.callbackURL = "callbackUrl"
//////        onlinePayment.amount = "1"
//////        onlinePayment.partyA = "25415668934"
//////        onlinePayment.partyB = "174379"
//////        onlinePayment.phoneNumber = "25415668934"
//////        onlinePayment.password = "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919"
//////        onlinePayment.timestamp = Timestamp.from(Instant.now()).toString()
//////        onlinePayment.transactionType = "PayBillOnline"
//////        onlinePayment.transactionDescription = "TEST KEBS"
//////
//////        val mpesa: Mpesa = MpesaClient(consumerKey, consumerSecret) // initializing mpesa client
//////        mpesa.authenticate().accessToken.enqueue(object : Callback<AccessToken?> {
//////            override fun onResponse(call: Call<AccessToken?>, response: Response<AccessToken?>) {
//////                if (response.isSuccessful) {
//////                    assert(response.body() != null)
//////                    val accessToken = response.body()!!.accessToken
//////                    mpesa.stkPush(accessToken).initiateOnlinePayment(onlinePayment).enqueue(object : Callback<OnlinePaymentResponse?> {
//////                        override fun onResponse(call: Call<OnlinePaymentResponse?>, response: Response<OnlinePaymentResponse?>) {}
//////                        override fun onFailure(call: Call<OnlinePaymentResponse?>, throwable: Throwable) {}
//////                    })
//////                }
//////            }
//////
//////            override fun onFailure(call: Call<AccessToken?>, throwable: Throwable) {
//////                throw MpesaException("Unable to authenticate Mpesa client", throwable)
//////            }
//////        })
//////    }
////
////
////}
////
//
//
//
//
//
//package org.kebs.app.kotlin.apollo.api
//
//
//import com.fasterxml.jackson.databind.PropertyNamingStrategy
//import com.fasterxml.jackson.databind.annotation.JsonNaming
//import io.ktor.client.statement.*
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.runBlocking
//import mu.KotlinLogging
//import org.codehaus.jackson.annotate.JsonProperty
//import org.jasypt.encryption.StringEncryptor
//import org.junit.jupiter.api.Test
//import org.junit.jupiter.api.extension.ExtendWith
//import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
//import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DaoService
//import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
//import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
//import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
//import org.kebs.app.kotlin.apollo.store.model.*
//import org.kebs.app.kotlin.apollo.store.repo.IBatchJobDetailsRepository
//import org.kebs.app.kotlin.apollo.store.repo.IIntegrationConfigurationRepository
//import org.kebs.app.kotlin.apollo.store.repo.IMpesaTransactionsRepository
//import org.kebs.app.kotlin.apollo.store.repo.IWorkflowTransactionsRepository
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.data.repository.findByIdOrNull
//import org.springframework.test.context.junit.jupiter.SpringExtension
//import java.sql.Timestamp
//import java.text.DateFormat
//import java.text.SimpleDateFormat
//import java.time.Instant
//import java.util.*
//
//
//@ExtendWith(SpringExtension::class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//class MpesaTest {
//
//    @Autowired
//    lateinit var daoService: DaoService
//
//    @Autowired
//    lateinit var applicationMapProperties: ApplicationMapProperties
//
//    @Autowired
//    lateinit var commonDaoServices: CommonDaoServices
//
//    @Autowired
//    lateinit var iMpesaTransactionsRepo: IMpesaTransactionsRepository
//
//    @Autowired
//    lateinit var configurationRepository: IIntegrationConfigurationRepository
//
//    @Autowired
//    lateinit var batchJobRepository: IBatchJobDetailsRepository
//
//    @Autowired
//    lateinit var logsRepo: IWorkflowTransactionsRepository
//
//    @Autowired
//    lateinit var jasyptStringEncryptor: StringEncryptor
//
////    @Test
////    @Throws(IOException::class)
////    fun whenSendPostRequest_thenCorrect() {
////        val formBody: RequestBody = FormBody.Builder()
////                .add("username", "test")
////                .add("password", "test")
////                .build()
////        val request = Request.Builder()
////                .url("https://127.0.0.1:9026/integ/login")
////                .post(formBody)
////                .build()
////    }
//
//    @Test
//    fun postApiCallTest() {
//        val appId = applicationMapProperties.mapImportInspection
//        val map = commonDaoServices.serviceMapDetails(appId)
//        var config = findIntegrationConfigurationEntity(applicationMapProperties.mapMpesaConfigIntegration.toLong())
//        val push = findBatchJobDetails(applicationMapProperties.mapMpesaConfigIntegrationPushJob.toLong())
//        val callBack = findBatchJobDetails(applicationMapProperties.mapMpesaConfigIntegrationCallBackJob.toLong())
//        val loginUrl = config.url.toString()
//        val pushUrl = push.jobUri.toString()
//        val callBackUrl = callBack.jobUri.toString()
//
//        runBlocking {
//
//            val transactionsRequest = MpesaValidationRequest()
//            transactionsRequest.transactionReference = config.varField3
//            transactionsRequest.account = config.varField1
//            transactionsRequest.accountReference = config.varField2
//
//            val transactionRef = daoService.generateTransactionReference()
//            val log = daoService.createTransactionLog(0, transactionRef)
//            log.integrationRequest = daoService.mapper().writeValueAsString(transactionsRequest)
//
//            if (config.varField7==getCurrentTime()){
//                config = loginRequest(loginUrl,transactionRef, config)
//            }else{
//                val headerParameters = mutableMapOf<String, String>()
//                val token = config.varField4.toString()
//                headerParameters["Authorization"] = token
//
//                val response =  pushRequest(pushUrl,"1","254715668934",transactionRef,config,headerParameters).second
//                response?.merchantRequestID
//                        ?.let { merchantCode ->
//                            val checkOutCode = response.checkoutRequestID.toString()
//                            val mpesaTransaction = mpesaTransactionEntity(810L, merchantCode, "kpaul7747@gmail.com", checkOutCode, map)
//                            KotlinLogging.logger { }.info { "MPESA TRANSACTION DONE ID = ${mpesaTransaction.id}" }
//
//                            delay(20000L);
//                            var hasResults = false
//                            while (!hasResults) {
//                                val data2 = callBackRequest(callBackUrl, transactionRef, config, headerParameters)
//                                val response2 = data2.second
//                                response2?.merchantRequestID
//                                        ?.let { merchantCode2 ->
//                                            val checkOutCode2 = (response2.checkoutRequestID?.let { commonDaoServices.makeAnyNotBeNull(it) } as String).apply {
//                                                updateMpesaTransactionEntity(merchantCode2, this, response2, map)
//                                            }
//                                            hasResults = true
//                                        }
//                                delay(1000L);
//
//                            }
//                        }
//                        ?: run {
//                            log.transactionStatus = 20
//                            log.transactionCompletedDate = Timestamp.from(Instant.now())
//                            KotlinLogging.logger { }.error("Failed")
//                        }
//
//
//            }
//
//
//
//        }
//
//
//        findIntegrationConfigurationEntity()
//                ?.let { config ->
//                    //push request configs
//                    findBatchJobDetails()
//                            ?.let { push ->
//                                //call request config
//                                batchJobRepository.findByIdOrNull(applicationMapProperties.mapMpesaConfigIntegrationCallBackJob.toLong())
//                                        ?.let { callBack ->
//                                            runBlocking {
//                                                config.url?.let { loginUrl ->
//                                                    push.jobUri?.let { pushUrl ->
//                                                        callBack.jobUri?.let { callBackUrl ->
//                                                            //                            val header = RequestHeader("KEBS", "xgg33", jasyptStringEncryptor.decrypt(config.username), jasyptStringEncryptor.decrypt(config.password))
////                            val request = ValidationRequestBody("EPA/1140/13", "2100082")
//
////                            val validationRequest = ValidationRequest(header, request)
//                                                            val appId = applicationMapProperties.mapImportInspection
//                                                            val map = commonDaoServices.serviceMapDetails(appId)
//                                                            val request = MpesaPushRequest()
//                                                            request.transactionReference = config.varField3
//                                                            request.account = config.varField1
//                                                            request.amount = "1"
//                                                            request.subscriberId = "254715668934"
//                                                            val headerParameters = mutableMapOf<String, String>()
//
//
//                                                            val token = config.varField4.toString()
//                                                            headerParameters["Authorization"] = token
//                                                            val transactionRef = daoService.generateTransactionReference()
//                                                            val log = daoService.createTransactionLog(0, transactionRef)
//                                                            log.integrationRequest = daoService.mapper().writeValueAsString(request)
//
//                                                            val resp = daoService.getHttpResponseFromPostCall(false, pushUrl, null, request, config, null, headerParameters)
////                                                            KotlinLogging.logger { }.info { ObjectMapper().writeValueAsString(resp) }
//
//                                                            val data = daoService.processResponses<MpesaPushResponse>(resp, log, pushUrl, config)
//
//                                                            KotlinLogging.logger { }.info { daoService.mapper().writeValueAsString(data.second) }
//                                                            val response = data.second
//                                                            response?.merchantRequestID
//                                                                    ?.let { merchantCode ->
//                                                                        val checkOutCode = response.checkoutRequestID?.let { commonDaoServices.makeAnyNotBeNull(it) } as String
//                                                                        val invoiceID: Long = 810L
//                                                                        val userName: String = "kpaul7747@gmail.com"
//                                                                        val mpesaTransaction = mpesaTransactionEntity(invoiceID, merchantCode, userName, checkOutCode, map)
//                                                                        KotlinLogging.logger { }.info { "MPESA TRANSACTION DONE ID = ${mpesaTransaction.id}" }
//
//                                                                        delay(20000L);
//                                                                        var hasResults = false
//                                                                        while (!hasResults) {
////                                                                            val response2 = callBackRequest(log, request, callBackUrl, transactionRef, config, headerParameters)
//                                                                            val data2 = callBackRequest(callBackUrl, transactionRef, config, headerParameters)
//                                                                            val response2 = data2.second
//                                                                            response2?.merchantRequestID
//                                                                                    ?.let { merchantCode2 ->
//                                                                                        val checkOutCode2 = response2.checkoutRequestID?.let { commonDaoServices.makeAnyNotBeNull(it) } as String
//                                                                                        updateMpesaTransactionEntity(merchantCode2, checkOutCode2, response2, map)
//                                                                                        hasResults = true
//                                                                                    }
//                                                                            KotlinLogging.logger { }.info(data.second.toString())
//                                                                            logsRepo.save(data.first)
//                                                                            print("->");
//                                                                            // TODO: Find out if should use delay or Thread.sleep
////                                                                            Thread.sleep(1000);
//                                                                            delay(1000L);
//
//                                                                        }
//                                                                    }
//                                                                    ?: run {
//                                                                        log.transactionStatus = 20
//                                                                        log.transactionCompletedDate = Timestamp.from(Instant.now())
//                                                                        KotlinLogging.logger { }.error("Failed")
//                                                                    }
//
//                                                            KotlinLogging.logger { }.info(data.second.toString())
//                                                            logsRepo.save(data.first)
//
//                                                        }
//                                                    }
//
//                                                }
//
//                                            }
//                                        }
//
//                            }
//
//                }
////        KotlinLogging.logger { }.info("Pausing")
////        Thread.sleep(21000);
////        KotlinLogging.logger { }.info("Paused")
////        Thread.sleep(1000);
//    }
//
//    private fun findBatchJobDetails(batchJobID: Long): BatchJobDetails {
//        batchJobRepository.findByIdOrNull(applicationMapProperties.mapMpesaConfigIntegrationPushJob.toLong())
//                ?.let {
//                    return it
//                }
//                ?: ExpectedDataNotFound("Batch Job Details With the following ID $batchJobID, does not exist")
//    }
//
//    private fun findIntegrationConfigurationEntity(configID: Long): IntegrationConfigurationEntity {
//        configurationRepository.findByIdOrNull(applicationMapProperties.mapMpesaConfigIntegrationPushJob.toLong())
//                ?.let {
//                    return it
//                }
//                ?: ExpectedDataNotFound("Configuration With the following ID $configID, does not exist")
//    }
//
//    private fun saveTokenToConfigIntegration(respToken: String?, config: IntegrationConfigurationEntity): IntegrationConfigurationEntity {
//        with(config) {
//            varField4 = respToken
//            varField5 = getCurrentTime()
//            varField7 = varField6?.toInt()?.let { getExpirationTime(it) }
//        }
//        return configurationRepository.save(config)
//    }
//
//    private fun getExpirationTime(expirationTime: Int): String {
//        val cal = Calendar.getInstance()
//        cal.add(Calendar.HOUR_OF_DAY, expirationTime)
//        val dateFormat: DateFormat = SimpleDateFormat("MM/dd/yyyy HH:mm:ss")
//        val timeResult = dateFormat.format(cal.time)
//        println(timeResult)
//        return timeResult
//    }
//
//    private fun getCurrentTime(): String {
//        val dateFormat: DateFormat = SimpleDateFormat("MM/dd/yyyy HH:mm:ss")
//        val date = Date()
//        val timeResult = dateFormat.format(date)
//        println(timeResult)
//        return timeResult
//    }
//
//    private fun sampleExtractTokenFromHeader(tokenResponse: HttpResponse?): String? {
//        tokenResponse
//                ?.let { response ->
//                    return response.headers["Authorization"]
//                }
//                ?: throw NullValueNotAllowedException("Response cannot be empty")
//    }
//
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
//
//    private suspend fun callBackRequest(url: String, transactionRef: String, config: IntegrationConfigurationEntity, headerParameters: MutableMap<String, String>): Pair<WorkflowTransactionsEntity, MpesaValidationResponse?> {
//        val validationRequest = MpesaValidationRequest()
//        validationRequest.transactionReference = config.varField3
//        validationRequest.account = config.varField1
//        validationRequest.accountReference = config.varField2
//
//        val log2 = daoService.createTransactionLog(0, "${transactionRef}_2")
//        val resp1 = daoService.getHttpResponseFromPostCall(false, url, null, validationRequest, config, null, headerParameters)
//        return daoService.processResponses(resp1, log2, url, config)
//    }
//
//    private suspend fun pushRequest(url: String, amount: String, phoneNumber: String, transactionRef: String, config: IntegrationConfigurationEntity, headerParameters: MutableMap<String, String>): Pair<WorkflowTransactionsEntity, MpesaPushResponse?> {
//        val request = MpesaPushRequest()
//        request.transactionReference = config.varField3
//        request.account = config.varField1
//        request.amount = amount
//        request.subscriberId = phoneNumber
//
//        val log = daoService.createTransactionLog(0, "${transactionRef}_1")
//        val resp = daoService.getHttpResponseFromPostCall(false, url, null, request, config, null, headerParameters)
//        return daoService.processResponses(resp, log, url, config)
//    }
//
//    private suspend fun loginRequest(url: String, transactionRef: String, config: IntegrationConfigurationEntity): IntegrationConfigurationEntity {
//        val loginRequest = MpesaLoginRequest()
//        loginRequest.username = jasyptStringEncryptor.decrypt(config.username)
//        loginRequest.password = jasyptStringEncryptor.decrypt(config.password)
//
//        val log2 = daoService.createTransactionLog(0, "${transactionRef}_0")
//        val tokenResponse = daoService.getHttpResponseFromPostCall(false, url, null, loginRequest, config, null, null)
//
//        val respToken = sampleExtractTokenFromHeader(tokenResponse)
//        return saveTokenToConfigIntegration(respToken, config)
//    }
//
////@Test
////fun getApiCallTest() {
////
////    configurationRepository.findByIdOrNull(3L)
////            ?.let { config ->
////                config.createdOn = Timestamp.from(Instant.now())
////                config.modifiedOn = Timestamp.from(Instant.now())
////                configurationRepository.save(config)
////                runBlocking {
////                    config.url?.let { url ->
////
////                        val log = daoService.createTransactionLog(0, "test")
////                        val params = mapOf(Pair("registration_number", "CPR/2013/117374"))
////                        log.integrationRequest = "$params"
////                        val resp = daoService.getHttpResponseFromGetCall(true, url, config, null, params, null)
////                        val data = daoService.processResponses<BrsLookUpResponse>(resp, log, url, config)
////                        KotlinLogging.logger { }.debug("${data.second?.count} ${data.second?.records?.get(0)?.partners?.get(0)?.name}")
////
////                        logsRepo.save(data.first)
////                    }
////                }
////            }
////
////}
////}
//
//
//    private fun mpesaTransactionEntity(invoiceID: Long, merchantCode: String, userName: String, checkOutCode: String, map: ServiceMapsEntity): MpesaTransactionEntity {
//        val mpesaTransaction = MpesaTransactionEntity()
//        with(mpesaTransaction) {
//            merchantRequestId = merchantCode
//            checkoutRequestId = checkOutCode
//            status = map.activeStatus
//            usedTransactionReference = map.inactiveStatus
//            createdBy = userName
//            createdOn = Timestamp.from(Instant.now())
//            invoiceId = invoiceID
//        }
//        return iMpesaTransactionsRepo.save(mpesaTransaction)
//    }
//
//    private fun findMpesaTransactionEntity(merchantCode: String, checkOutCode: String): MpesaTransactionEntity {
//        iMpesaTransactionsRepo.findByMerchantRequestIdAndCheckoutRequestId(merchantCode, checkOutCode)
//                ?.let { transaction ->
//                    return transaction
//                }
//                ?: throw ExpectedDataNotFound("Transaction With the following $merchantCode and $checkOutCode, does not exist")
//    }
//
//
//    class MpesaValidationResponse {
//        val responseDescription: String? = null
//        val merchantRequestID: String? = null
//        val checkoutRequestID: String? = null
//        val resultCode: String? = null
//        val resultDesc: String? = null
//        val responseCode: String? = null
//        val responsePhoneNumber: String? = null
//        val responseReceiptNumber: String? = null
//        val responseAmount: String? = null
//    }
//
//    class MpesaValidationRequest {
//
//        var account: String? = null
//        var transactionReference: String? = null
//        var accountReference: String? = null
//    }
//
//    class MpesaPushRequest {
//        var subscriberId: String? = null
//        var amount: String? = null
//        var account: String? = null
//        var transactionReference: String? = null
////        var reference: String? = null
//
//    }
//
//    class MpesaLoginRequest {
//        var username: String? = null
//        var password: String? = null
//    }
//
//    class MpesaTransactionsRequest {
//        var username: String? = null
//        var password: String? = null
//    }
//
//    @JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy::class)
//    class MpesaPushResponse {
//        @JsonProperty("MerchantRequestID")
//        val merchantRequestID: String? = null
//
//        @JsonProperty("CheckoutRequestID")
//        val checkoutRequestID: String? = null
//
//        @JsonProperty("ResponseCode")
//        val responseCode: String? = null
//
//        @JsonProperty("ResponseDescription")
//        val responseDescription: String? = null
//
//        @JsonProperty("DefaultError")
//        val defaultError: String? = null
//
//        @JsonProperty("CustomerMessage")
//        val customerMessage: String? = null
//
//        @JsonProperty("ResultCode")
//        val resultCode: String? = null
//
//        @JsonProperty("ResultDesc")
//        val resultDesc: String? = null
//    }
//
//
//}
//
