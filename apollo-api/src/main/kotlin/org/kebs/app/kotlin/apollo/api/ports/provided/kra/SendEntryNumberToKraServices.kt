package org.kebs.app.kotlin.apollo.api.ports.provided.kra

import com.google.gson.Gson
import io.ktor.client.statement.*
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import org.jasypt.encryption.StringEncryptor
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import org.json.simple.JSONObject
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DaoService
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.StandardLevyService
import org.kebs.app.kotlin.apollo.api.ports.provided.kra.request.*
import org.kebs.app.kotlin.apollo.api.ports.provided.kra.request.KraHeader.Companion.globalVar
import org.kebs.app.kotlin.apollo.api.ports.provided.kra.request.KraPenaltyRequest.Companion.globalVariable
import org.kebs.app.kotlin.apollo.api.ports.provided.sage.requests.*
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.KraEntryNumberRequestLogEntity
import org.kebs.app.kotlin.apollo.store.model.KraPenaltyDetailsRequestLogEntity
import org.kebs.app.kotlin.apollo.store.model.ServiceMapsEntity
import org.kebs.app.kotlin.apollo.store.model.WorkflowTransactionsEntity
import org.kebs.app.kotlin.apollo.store.model.registration.CompanyProfileEntity
import org.kebs.app.kotlin.apollo.store.model.std.PenaltyDetails
import org.kebs.app.kotlin.apollo.store.repo.*
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.BigInteger
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.set


@Service
class SendEntryNumberToKraServices(
    private val jasyptStringEncryptor: StringEncryptor,
    private val applicationMapProperties: ApplicationMapProperties,
    private val iLogKraEntryNumberRequestRepo: ILogKraEntryNumberRequestEntityRepository,
    private val iKraEntryNumberRequestLogEntityRepository: IKraEntryNumberRequestLogEntityRepository,
    private val iKraPaymentDetailsRequestLogEntityRepository: IKraPaymentDetailsRequestLogEntityRepository,
    private val iKraPenaltyDetailsRequestLogEntityRepository: IKraPenaltyDetailsRequestLogEntityRepository,
    private val commonDaoServices: CommonDaoServices,
    private val daoService: DaoService,
    private val companyRepo: ICompanyProfileRepository,
    private val standardLevyService: StandardLevyService
) {
    fun postEntryNumberTransactionToKra(companyProfileID: Long, user: String, map: ServiceMapsEntity): KraEntryNumberRequestLogEntity? {
        var resultSaved: KraEntryNumberRequestLogEntity? =null
        val config =
            commonDaoServices.findIntegrationConfigurationEntity(applicationMapProperties.mapKraConfigIntegration)
        val configUrl = config.url ?: throw Exception("URL CANNOT BE NULL FOR KRA")
        val companyProfile = commonDaoServices.findCompanyProfileWithID(companyProfileID)
        runBlocking {

            val numberRecords = "1"
            val recordNumber = 1
            //val test="19-04-2023T15:59:31"

            var transDate = commonDaoServices.getTimestamp()
            val headerBody = KraHeader().apply {
                globalVar = SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss").format(transDate)
                transmissionDate = globalVar
                loginId = jasyptStringEncryptor.decrypt(config.username)
                password = jasyptStringEncryptor.decrypt(config.password)
                noOfRecords = numberRecords
                val join = numberRecords + globalVar
                hash = kraDataEncryption(join)
            }
            val detailBody = KraDetails().apply {
                entryNumber = companyProfile.entryNumber
                kraPin = companyProfile.kraPin
                manufacturerName = companyProfile.name
                registrationDate =
                    companyProfile.createdOn?.let { commonDaoServices.convertTimestampToKraValidDate(it) }
                status = if (companyProfile.manufactureStatus == 1) "Active" else "Inactive"
            }

            val list = mutableListOf<KraDetails>()
            list.add(detailBody)

            val rootRequest = KraRequest().apply {
                header = headerBody
                details = list
            }
            val requestBody = JSONObject()
            requestBody["REQUEST"] = rootRequest

            var transactionsRequest = KraEntryNumberRequestLogEntity().apply {
                requestHash = headerBody.hash
                requestTransmissionDate = globalVar
                requestNoOfRecords = headerBody.noOfRecords
                requestEntryNumber = detailBody.entryNumber
                requestKraPin = detailBody.kraPin
                requestManufacturerName = detailBody.manufacturerName
                requestRegistrationDate = detailBody.registrationDate
                requestManufactureStatus = detailBody.status
                status = 0
                createdBy = user
                createdOn = commonDaoServices.getTimestamp()
            }

            transactionsRequest = iKraEntryNumberRequestLogEntityRepository.save(transactionsRequest)


            val log = daoService.createTransactionLog(0, "${companyProfile.entryNumber}_1")
            val resp = daoService.getHttpResponseFromPostCall(
                false,
                configUrl,
                null,
                requestBody,
                config,
                null,
                null
            )
            val gson = Gson()
            val response: Triple<WorkflowTransactionsEntity, KraEntryNumberResponse?, HttpResponse?> =
                daoService.processResponses(resp, log, configUrl, config)
            KotlinLogging.logger { }.info { "Request response: ${gson.toJson(response.second)}" }
            if (response.second?.response?.responseCode == "90000") {

                transactionsRequest.apply {
                    responseStatus = response.second?.response?.status
                    responseResponseCode = response.second?.response?.responseCode
                    responseMessage = response.second?.response?.message
                    status = 1
                    updateBy = user
                    updatedOn = commonDaoServices.getTimestamp()
                }
                resultSaved=   iKraEntryNumberRequestLogEntityRepository.save(transactionsRequest)


            }else if(response.second?.response?.responseCode == "90001"){
                transactionsRequest.apply {
                    responseStatus = response.second?.response?.status
                    responseResponseCode = response.second?.response?.responseCode
                    responseMessage = response.second?.response?.message
                    status = 0
                    updateBy = user
                    updatedOn = commonDaoServices.getTimestamp()
                }
                resultSaved=   iKraEntryNumberRequestLogEntityRepository.save(transactionsRequest)

            }else if(response.second?.response?.responseCode == "90002"){
                transactionsRequest.apply {
                    responseStatus = response.second?.response?.status
                    responseResponseCode = response.second?.response?.responseCode
                    responseMessage = response.second?.response?.message
                    status = 0
                    updateBy = user
                    updatedOn = commonDaoServices.getTimestamp()
                }
                resultSaved=   iKraEntryNumberRequestLogEntityRepository.save(transactionsRequest)

            }else if(response.second?.response?.responseCode == "90003"){
                transactionsRequest.apply {
                    responseStatus = response.second?.response?.status
                    responseResponseCode = response.second?.response?.responseCode
                    responseMessage = response.second?.response?.message
                    status = 0
                    updateBy = user
                    updatedOn = commonDaoServices.getTimestamp()
                }
                resultSaved=   iKraEntryNumberRequestLogEntityRepository.save(transactionsRequest)

            }else if(response.second?.response?.responseCode == "90004"){
                transactionsRequest.apply {
                    responseStatus = response.second?.response?.status
                    responseResponseCode = response.second?.response?.responseCode
                    responseMessage = response.second?.response?.message
                    status = 0
                    updateBy = user
                    updatedOn = commonDaoServices.getTimestamp()
                }
                resultSaved=   iKraEntryNumberRequestLogEntityRepository.save(transactionsRequest)

            }
            else {
                transactionsRequest.apply {
                    responseStatus = "NOK"
                    responseResponseCode = "90005"
                    responseMessage = "No Response From KRA"
                    updateBy = user
                    updatedOn = commonDaoServices.getTimestamp()
                }
                resultSaved =   iKraEntryNumberRequestLogEntityRepository.save(transactionsRequest)
            }

        }

        return resultSaved
    }

//    fun postEntryNumberToKra( ): KraEntryNumberRequestLogEntity? {
//        var resultSaved: KraEntryNumberRequestLogEntity? =null
//        val config =
//            commonDaoServices.findIntegrationConfigurationEntity(applicationMapProperties.mapKraConfigIntegration)
//        val configUrl = config.url ?: throw Exception("URL CANNOT BE NULL FOR KRA")
//
//        runBlocking {
//
//            val entryNumberDetails = iKraEntryNumberRequestLogEntityRepository.getEntryNumberDetails()
//
//            for (entryNumberDetail in entryNumberDetails) {
//                val companyProfile = entryNumberDetail.requestKraPin?.let {
//                    commonDaoServices.findCompanyProfileWithKraPins(
//                        it
//                    )
//                }
//                val numberRecords = entryNumberDetail.requestEntryNumber
//                val headerBody = KraHeader().apply {
//                    globalVar =
//                        SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss").format(entryNumberDetail.requestTransmissionDate)
//                    transmissionDate = globalVar
//                    loginId = jasyptStringEncryptor.decrypt(config.username)
//                    password = jasyptStringEncryptor.decrypt(config.password)
//                    noOfRecords = numberRecords
//                    val join = numberRecords + globalVar
//                    hash = kraDataEncryption(join)
//                }
//                val detailBody = KraDetails().apply {
//                    entryNumber = entryNumberDetail.requestEntryNumber
//                    kraPin = entryNumberDetail.requestKraPin
//                    manufacturerName = entryNumberDetail.requestManufacturerName
//                    if (companyProfile != null) {
//                        registrationDate =
//                            companyProfile.createdOn?.let { commonDaoServices.convertTimestampToKraValidDate(it) }
//                    }
//                    if (companyProfile != null) {
//                        status = if (companyProfile.manufactureStatus == 1) "Active" else "Inactive"
//                    }
//                }
//
//                val list = mutableListOf<KraDetails>()
//                list.add(detailBody)
//
//                val rootRequest = KraRequest().apply {
//                    header = headerBody
//                    details = list
//                }
//                val requestBody = JSONObject()
//                requestBody["REQUEST"] = rootRequest
//
//
//                var transactionsRequest = KraEntryNumberRequestLogEntity().apply {
//                    requestHash = headerBody.hash
//                    requestTransmissionDate = globalVar
//                    requestNoOfRecords = headerBody.noOfRecords
//                    requestEntryNumber = detailBody.entryNumber
//                    requestKraPin = detailBody.kraPin
//                    requestManufacturerName = detailBody.manufacturerName
//                    requestRegistrationDate = detailBody.registrationDate
//                    requestManufactureStatus = detailBody.status
//                    status = 0
//                    id = entryNumberDetail.id
//                    createdOn = commonDaoServices.getTimestamp()
//                }
//
//                transactionsRequest = iKraEntryNumberRequestLogEntityRepository.save(transactionsRequest)
//
//
//                val log = daoService.createTransactionLog(0, "${companyProfile?.entryNumber}_1")
//                val resp = daoService.getHttpResponseFromPostCall(
//                    false,
//                    configUrl,
//                    null,
//                    requestBody,
//                    config,
//                    null,
//                    null
//                )
//                val gson = Gson()
//                val response: Triple<WorkflowTransactionsEntity, KraEntryNumberResponse?, HttpResponse?> =
//                    daoService.processResponses(resp, log, configUrl, config)
//                KotlinLogging.logger { }.info { "Request response: ${gson.toJson(response.second)}" }
//                if (response.second?.response?.responseCode == "90000") {
//
//                    transactionsRequest.apply {
//                        responseStatus = response.second?.response?.status
//                        responseResponseCode = response.second?.response?.responseCode
//                        responseMessage = response.second?.response?.message
//                        status = 1
//                        updateBy = "System"
//                        updatedOn = commonDaoServices.getTimestamp()
//                    }
//                    resultSaved = iKraEntryNumberRequestLogEntityRepository.save(transactionsRequest)
//
//
//                } else if (response.second?.response?.responseCode == "90001") {
//                    transactionsRequest.apply {
//                        responseStatus = response.second?.response?.status
//                        responseResponseCode = response.second?.response?.responseCode
//                        responseMessage = response.second?.response?.message
//                        status = 0
//                        updateBy = "System"
//                        updatedOn = commonDaoServices.getTimestamp()
//                    }
//                    resultSaved = iKraEntryNumberRequestLogEntityRepository.save(transactionsRequest)
//
//                } else if (response.second?.response?.responseCode == "90002") {
//                    transactionsRequest.apply {
//                        responseStatus = response.second?.response?.status
//                        responseResponseCode = response.second?.response?.responseCode
//                        responseMessage = response.second?.response?.message
//                        status = 0
//                        updateBy = "System"
//                        updatedOn = commonDaoServices.getTimestamp()
//                    }
//                    resultSaved = iKraEntryNumberRequestLogEntityRepository.save(transactionsRequest)
//
//                } else if (response.second?.response?.responseCode == "90003") {
//                    transactionsRequest.apply {
//                        responseStatus = response.second?.response?.status
//                        responseResponseCode = response.second?.response?.responseCode
//                        responseMessage = response.second?.response?.message
//                        status = 0
//                        updateBy = "System"
//                        updatedOn = commonDaoServices.getTimestamp()
//                    }
//                    resultSaved = iKraEntryNumberRequestLogEntityRepository.save(transactionsRequest)
//
//                } else if (response.second?.response?.responseCode == "90004") {
//                    transactionsRequest.apply {
//                        responseStatus = response.second?.response?.status
//                        responseResponseCode = response.second?.response?.responseCode
//                        responseMessage = response.second?.response?.message
//                        status = 0
//                        updateBy = "System"
//                        updatedOn = commonDaoServices.getTimestamp()
//                    }
//                    resultSaved = iKraEntryNumberRequestLogEntityRepository.save(transactionsRequest)
//
//                } else {
//                    transactionsRequest.apply {
//                        responseStatus = "NOK"
//                        responseResponseCode = "90005"
//                        responseMessage = "No Response From KRA"
//                        updateBy = "System"
//                        updatedOn = commonDaoServices.getTimestamp()
//                    }
//                    resultSaved = iKraEntryNumberRequestLogEntityRepository.save(transactionsRequest)
//                }
//            }
//
//        }
//            return resultSaved
//    }

//    fun postPenaltyDetailsToKra( ): KraPenaltyDetailsRequestLogEntity? {
//        var resultSaved: KraPenaltyDetailsRequestLogEntity? =null
//        val config = commonDaoServices.findIntegrationConfigurationEntity(applicationMapProperties.mapKraPenaltyConfigIntegration)
//        val configUrl = config.url ?: throw Exception("URL CANNOT BE NULL FOR KRA")
//        //val companyProfile = commonDaoServices.findCompanyProfileWithID(companyProfileID)
//        runBlocking {
//            val recordCount=companyRepo.findPenaltyCount()
//            val transDate = commonDaoServices.getTimestamp()
//            val headerBody = PenaltyHeader().apply {
//                globalVar = SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss").format(transDate)
//                transmissionDate = globalVar
//                loginId = jasyptStringEncryptor.decrypt(config.username)
//                password = jasyptStringEncryptor.decrypt(config.password)
//                noOfRecords = recordCount
//                val join = recordCount + globalVar
//                hash = kraDataEncryption(join)
//            }
//            val penaltyDetails= mapKraPenaltyDto(companyRepo.getPenaltyDetails())
////            for (penaltyDetail in penaltyDetails){
////
////            }
//            val rootRequest = PenaltyRequest().apply {
//                HEADER = headerBody
//                PENALTYINFO = penaltyDetails
//            }
//            val requestBody = JSONObject()
//            requestBody["REQUEST"] = rootRequest
//            val gson = Gson()
//            KotlinLogging.logger { }.info { "PENALTY REQUEST BODY" + gson.toJson(requestBody) }
//
//            var transactionsRequest = KraPenaltyDetailsRequestLogEntity().apply {
//                requestHash = headerBody.hash
//                requestTransmissionDate = globalVar
//                requestNoOfRecords = headerBody.noOfRecords
//                status = 0
//                createdBy = "System"
//                createdOn = commonDaoServices.getTimestamp()
//            }
//
//            transactionsRequest = iKraPenaltyDetailsRequestLogEntityRepository.save(transactionsRequest)
//            val lodCode= 798348034
//            val log = daoService.createTransactionLog(0, "${lodCode}_1")
//
//            val resp = daoService.getHttpResponseFromPostCall(
//                false,
//                configUrl,
//                null,
//                requestBody,
//                config,
//                null,
//                null
//            )
//            val response: Triple<WorkflowTransactionsEntity, KraEntryNumberResponse?, HttpResponse?> =
//                daoService.processResponses(resp, log, configUrl, config)
//
//            KotlinLogging.logger { }.info { "Request response: ${gson.toJson(response.second)}" }
//            if (response.second?.response?.responseCode == "90000") {
//
//                transactionsRequest.apply {
//                    responseStatus = response.second?.response?.status
//                    responseResponseCode = response.second?.response?.responseCode
//                    responseMessage = response.second?.response?.message
//                    status = 1
//                    updatedOn = commonDaoServices.getTimestamp()
//                }
//                resultSaved=   iKraPenaltyDetailsRequestLogEntityRepository.save(transactionsRequest)
//                standardLevyService.updatePenaltyDetails()
//
//
//            } else {
//                transactionsRequest.apply {
//                    responseStatus = response.second?.response?.status
//                    responseResponseCode = response.second?.response?.responseCode
//                    responseMessage = response.second?.response?.message
//                    updatedOn = commonDaoServices.getTimestamp()
//                }
//                resultSaved =   iKraPenaltyDetailsRequestLogEntityRepository.save(transactionsRequest)
//            }
//
//        }
//        return resultSaved
//
//
//    }

    fun postPenaltyDetailsToKra( ): KraPenaltyDetailsRequestLogEntity? {
        var resultSaved: KraPenaltyDetailsRequestLogEntity? =null
        val config = commonDaoServices.findIntegrationConfigurationEntity(applicationMapProperties.mapKraPenaltyConfigIntegration)
        val configUrl = config.url ?: throw Exception("URL CANNOT BE NULL FOR KRA")
        //val companyProfile = commonDaoServices.findCompanyProfileWithID(companyProfileID)
        runBlocking {
            val recordCount="1"
            val transDate = commonDaoServices.getTimestamp()
            val headerBody = PenaltyHeader().apply {
                globalVar = SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss").format(transDate)
                transmissionDate = globalVar
                loginId = jasyptStringEncryptor.decrypt(config.username)
                password = jasyptStringEncryptor.decrypt(config.password)
                noOfRecords = recordCount
                val join = recordCount + globalVar
                hash = kraDataEncryption(join)
            }
            val penaltyDetails=companyRepo.getPenaltyDetails()
            //val penaltyDetails= mapKraPenaltyDto(companyRepo.getPenaltyDetails())
            for (penaltyDetail in penaltyDetails){
                val detailBody = KraPenaltyRequest().apply {
                    entryNo = penaltyDetail.getEntryNo()
                    periodTo = penaltyDetail.getPeriodTo()?.let { commonDaoServices.convertDateToKRADate(it) }
                    periodFrom = penaltyDetail.getPeriodFrom()?.let { commonDaoServices.convertDateToKRADate(it) }
                    PenaltyOrderNo = penaltyDetail.getPenaltyOrderNo()
                    penaltyPayable = penaltyDetail.getPenaltyPayable()
                    kraPin = penaltyDetail.getkraPin()
                    globalVariable = SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss").format(penaltyDetail.getPenaltyGenDate())
                    penaltyGenDate = globalVariable
                    //penaltyGenDate = SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss").format(penaltyDetail.getPenaltyGenDate())
//                    penaltyGenDate = SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss").format(penaltyDetail.getPenaltyGenDate()
//                        ?.let { convertStringToDate(it) })
                    manufacName = penaltyDetail.getManufacName()
                }
                val list = mutableListOf<KraPenaltyRequest>()
                list.add(detailBody)

                val rootRequest = PenaltyRequest().apply {
                    HEADER = headerBody
                    PENALTYINFO = list
                }

                val requestBody = JSONObject()
                requestBody["REQUEST"] = rootRequest
                val gson = Gson()
                KotlinLogging.logger { }.info { "PENALTY REQUEST BODY" + gson.toJson(requestBody) }

                var transactionsRequest = KraPenaltyDetailsRequestLogEntity().apply {
                    requestHash = headerBody.hash
                    requestTransmissionDate = globalVar
                    requestNoOfRecords = headerBody.noOfRecords
                    status = 0
                    createdBy = "System"
                    createdOn = commonDaoServices.getTimestamp()
                }
                transactionsRequest = iKraPenaltyDetailsRequestLogEntityRepository.save(transactionsRequest)
                val lodCode= 798348034
                val log = daoService.createTransactionLog(0, "${lodCode}_1")

                val resp = daoService.getHttpResponseFromPostCall(
                    false,
                    configUrl,
                    null,
                    requestBody,
                    config,
                    null,
                    null
                )
                val response: Triple<WorkflowTransactionsEntity, KraEntryNumberResponse?, HttpResponse?> =
                    daoService.processResponses(resp, log, configUrl, config)

                KotlinLogging.logger { }.info { "Request response: ${gson.toJson(response.second)}" }
                if (response.second?.response?.responseCode == "90000") {

                    transactionsRequest.apply {
                        responseStatus = response.second?.response?.status
                        responseResponseCode = response.second?.response?.responseCode
                        responseMessage = response.second?.response?.message
                        status = 1
                        updatedOn = commonDaoServices.getTimestamp()
                    }
                    resultSaved=   iKraPenaltyDetailsRequestLogEntityRepository.save(transactionsRequest)
                    penaltyDetail.getPenaltyOrderNo()?.let { companyRepo.updatePenaltyStatus(it) }
                    //standardLevyService.updatePenaltyDetails()


                } else {
                    transactionsRequest.apply {
                        responseStatus = response.second?.response?.status
                        responseResponseCode = response.second?.response?.responseCode
                        responseMessage = response.second?.response?.message
                        updatedOn = commonDaoServices.getTimestamp()
                    }
                    resultSaved =   iKraPenaltyDetailsRequestLogEntityRepository.save(transactionsRequest)
                    penaltyDetail.getPenaltyOrderNo()?.let { companyRepo.updatePenaltyStatusNotSent(it) }
                }
            }
        }
        return resultSaved
    }

 fun mapKraPenaltyDto(penaltyDetails: MutableList<PenaltyDetails>): MutableList<KraPenaltyRequest> {
     val penaltyDetailsDto = mutableListOf<KraPenaltyRequest>()

     penaltyDetails.forEach { p->
         val krap = KraPenaltyRequest().apply {
             entryNo = p.getEntryNo()
             periodTo = p.getPeriodTo()?.let { commonDaoServices.convertDateToKRADate(it) }
             periodFrom = p.getPeriodFrom()?.let { commonDaoServices.convertDateToKRADate(it) }
             PenaltyOrderNo = p.getPenaltyOrderNo()
             penaltyPayable = p.getPenaltyPayable()
             kraPin = p.getkraPin()
             globalVariable = SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss").format(p.getPenaltyGenDate())
             penaltyGenDate = globalVariable
//             penaltyGenDate = SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss").format(p.getPenaltyGenDate()
//                 ?.let { convertStringToDate(it) })
             manufacName = p.getManufacName()
         }
         penaltyDetailsDto.add(krap)
     }

     return penaltyDetailsDto
 }


    fun convertStringToDate(dateTime: String): Date {

//        val dateTimeFormatOut: DateTimeFormatter = DateTimeFormat.forPattern("MM/dd/yyyy")
//        val dateTime = "11/27/2020 05:35:00"
//        val datetimeformat: DateTimeFormatter = DateTimeFormat.forPattern("MM/dd/yyyy")
////
//        val joda_time: DateTime = datetimeformat.parseDateTime(dateTime)
////        println("joda_time : $joda_time")
//
//        val dateTimeFormatOut: DateTimeFormatter = DateTimeFormat.forPattern("MM-dd-yyyy")
//        System.out.println("date time format out:  " + dateTimeFormatOut.print(joda_time))
//        return dateTimeFormatOut.parseDateTime(joda_time.toString())

//        val sDate1 = "31/12/1998"
        return SimpleDateFormat("dd/MM/yyyy").parse(dateTime)
    }

    fun kraDataEncryption(hashedData: String): String {
        fun encryptThisString(input: String): String {
            return try {
                val md = MessageDigest.getInstance("SHA-256")
                val messageDigest = md.digest(input.toByteArray(StandardCharsets.UTF_8))
                //val messageDigest = md.digest(input.toByteArray())
                val no = BigInteger(1, messageDigest)
                var hashtext = no.toString(16)
//                val len=hashtext.length
//                KotlinLogging.logger { }.info { "my hashed length =  $len" }
                while (hashtext.length < 64) {
                    hashtext = "0$hashtext"
                }

                hashtext
            } catch (e: NoSuchAlgorithmException) {
                throw RuntimeException(e)
            }
        }
        return encryptThisString(hashedData)

    }


    fun postCustomerDetailsToSage() {
        val config =
            commonDaoServices.findIntegrationConfigurationEntity(applicationMapProperties.mapSageConfigIntegrationQa)
        val configUrl = config.url ?: throw Exception("URL CANNOT BE NULL FOR SAGE")
        KotlinLogging.logger { }.info { "Request Body url: ${configUrl}" }
        //  val companyProfile = commonDaoServices.findCompanyProfileWithID(companyProfileID)
        runBlocking {

            val numberRecords = "BSKAccount"
            val recordNumber = 1
            val invoiceAmnt = 11160


            var transDate = commonDaoServices.getTimestamp()
            val headerBody = SageQAHeader().apply {
                globalVar = SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss").format(transDate)
                serviceName = "BSKApp"
                connectionPassword = jasyptStringEncryptor.decrypt(config.password)
                messageID = "BSK"
                connectionID = "BSKAccount"
            }
            val requestBody = SageQARequest().apply {
                BatchNo = ""
                DocumentDate = SimpleDateFormat("dd-mm-yyyy").format(transDate)
                InvoiceType = 4
                ServiceType = "QA"
                CurrencyCode = "KES"
                CustomerCode = "HQS-0662"
                CustomerName = "UNGA LIMITED"
                InvoiceDesc = "Standards Billing"
                InvoiceAmnt = BigDecimal(invoiceAmnt)
                TaxPINNo = "PL2435353QC"

            }
            val detailBody = SageQADetails().apply {
                RevenueAcc = "10170"
                RevenueAccDesc = "QA"
                Taxable = 1
                MAmount = BigDecimal(11160)
                TaxAmount = BigDecimal(11160)


            }
            val list = mutableListOf<SageQADetails>()
            list.add(detailBody)

            val rootRequest = SageQARequestBody().apply {
                header = headerBody
                request = requestBody
                details = list
            }


            var requestBd = JSONObject()
            requestBd["header"] = headerBody
            requestBd["request"] = requestBody




            requestBd["details"] = list


            // val requestBody = JSONObject()
            //requestBody["REQUEST"] = rootRequest


//            var transactionsRequest = KraEntryNumberRequestLogEntity().apply {
//                requestHash = headerBody.hash
//                requestTransmissionDate = globalVar
//                requestNoOfRecords = headerBody.noOfRecords
//                requestEntryNumber = detailBody.entryNumber
//                requestKraPin = detailBody.kraPin
//                requestManufacturerName = detailBody.manufacturerName
//                requestRegistrationDate = detailBody.registrationDate
//                requestManufactureStatus = detailBody.status
//                status = 0
//                createdBy = user
//                createdOn = commonDaoServices.getTimestamp()
//            }
//
//
//            transactionsRequest = iKraEntryNumberRequestLogEntityRepository.save(transactionsRequest)

            val log = daoService.createTransactionLog(0, "${rootRequest.request?.TaxPINNo}_1")


            //val log = daoService.createTransactionLog(0, "${companyProfile.entryNumber}_1")
            val resp = daoService.getHttpResponseFromPostCall(
                false,
                configUrl,
                null,
                rootRequest,
                config,
                null,
                null
            )
//            val headerSage = HeaderSage()
//            val responseSage =ResponseSage()
//
//            val rootSage = QASageRoot().apply {
//                header =headerSage
//                response = responseSage
//
//            }responseSage

            val gson = Gson()
            val response: Triple<WorkflowTransactionsEntity, SageQaPostingResponseResult?, HttpResponse?> =
                daoService.processResponses(resp, log, configUrl, config)

            KotlinLogging.logger { }.info { "Request respones: ${gson.toJson(response.second)}" }


//            val response: Triple<WorkflowTransactionsEntity, KraResponse?, HttpResponse?> = daoService.processResponses(resp, log, configUrl, config)

            // if (response.second?.responseCode=="90000"){

//            transactionsRequest.apply {
//                responseStatus = response.second?.status
//                responseResponseCode = response.second?.responseCode
//                responseMessage = response.second?.message
//                status = 1
//                updateBy = user
//                updatedOn = commonDaoServices.getTimestamp()
//            }
//            iKraEntryNumberRequestLogEntityRepository.save(transactionsRequest)
//
            //  KotlinLogging.logger { }.trace { "Response ${response.second?.message}" }
//            val gson = Gson()Gson

            KotlinLogging.logger { }.info { "Request Body: ${gson.toJson(rootRequest)}" }
//            //           }else{
//                transactionsRequest.apply {
//                    responseStatus = response.second?.status
//                    responseResponseCode = response.second?.responseCode
//                    responseMessage = response.second?.message
//                    updateBy = user
//                    updatedOn = commonDaoServices.getTimestamp()
//                }
//                iKraEntryNumberRequestLogEntityRepository.save(transactionsRequest)
//
//                KotlinLogging.logger { }.trace { "Response ${response.second?.message}" }
//            }


            //  KotlinLogging.logger {  }.info { "Response received: $response" }
        }
    }
}

