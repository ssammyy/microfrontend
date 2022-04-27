package org.kebs.app.kotlin.apollo.api.ports.provided.kra

import com.hazelcast.internal.util.QuickMath.bytesToHex
import io.ktor.client.statement.*
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import org.jasypt.encryption.StringEncryptor
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DaoService
import org.kebs.app.kotlin.apollo.api.ports.provided.kra.request.*
import org.kebs.app.kotlin.apollo.api.ports.provided.kra.request.KraHeader.Companion.globalVar
import org.kebs.app.kotlin.apollo.api.ports.provided.kra.response.KraResponse
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.IntegrationConfigurationEntity
import org.kebs.app.kotlin.apollo.store.model.KraEntryNumberRequestLogEntity
import org.kebs.app.kotlin.apollo.store.model.ServiceMapsEntity
import org.kebs.app.kotlin.apollo.store.model.WorkflowTransactionsEntity
import org.kebs.app.kotlin.apollo.store.repo.IKraEntryNumberRequestLogEntityRepository
import org.kebs.app.kotlin.apollo.store.repo.ILogKraEntryNumberRequestEntityRepository
import org.springframework.stereotype.Service
import java.math.BigInteger
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.Instant
import kotlin.collections.set


@Service
class SendEntryNumberToKraServices(
    private val jasyptStringEncryptor: StringEncryptor,
    private val applicationMapProperties: ApplicationMapProperties,
    private val iLogKraEntryNumberRequestRepo: ILogKraEntryNumberRequestEntityRepository,
    private val iKraEntryNumberRequestLogEntityRepository: IKraEntryNumberRequestLogEntityRepository,
    private val commonDaoServices: CommonDaoServices,
    private val daoService: DaoService
) {
    fun postEntryNumberTransactionToKra(companyProfileID: Long, user: String, map: ServiceMapsEntity) {
        val config = commonDaoServices.findIntegrationConfigurationEntity(applicationMapProperties.mapKraConfigIntegration)
        val configUrl = config.url ?: throw Exception("URL CANNOT BE NULL FOR KRA")
        val companyProfile = commonDaoServices.findCompanyProfileWithID(companyProfileID)
        runBlocking {

            val numberRecords = "1"
            val recordNumber= 1

            var transDate = commonDaoServices.getTimestamp()
            val headerBody = KraHeader().apply {
                globalVar = SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss").format(transDate)
                transmissionDate = globalVar
                loginId = jasyptStringEncryptor.decrypt(config.username)
                password = jasyptStringEncryptor.decrypt(config.password)
                noOfRecords = numberRecords
                val join = numberRecords+globalVar
                hash = kraDataEncryption(join)
             //   KotlinLogging.logger { }.info { "Input :  = $join" }
            }
            val detailBody = KraDetails().apply {
                entryNumber = companyProfile.entryNumber
                kraPin = companyProfile.kraPin
                manufacturerName = companyProfile.name
                registrationDate =
                    companyProfile.createdOn?.let { commonDaoServices.convertTimestampToKraValidDate(it) }
                status = if (companyProfile.manufactureStatus == 1) "Active" else "Inactive"
            }

            val list= mutableListOf<KraDetails>()
            list.add(detailBody)

            val rootRequest = KraRequest().apply {
                header = headerBody
                details = list
            }

            val requestBody = JSONObject()
            requestBody["REQUEST"] = rootRequest

            var transactionsRequest = KraEntryNumberRequestLogEntity().apply  {
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
            val response: Triple<WorkflowTransactionsEntity, KraResponse?, HttpResponse?> =
                daoService.processResponses(resp, log, configUrl, config)

           // if (response.second?.responseCode=="90000"){

                transactionsRequest.apply {
                    responseStatus = response.second?.status
                    responseResponseCode = response.second?.responseCode
                    responseMessage = response.second?.message
                    status = 1
                    updateBy = user
                    updatedOn = commonDaoServices.getTimestamp()
                }
                iKraEntryNumberRequestLogEntityRepository.save(transactionsRequest)

            KotlinLogging.logger { }.trace { "Response ${response.second?.message}" }
        //           }else{
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




            // KotlinLogging.logger {  }.info { "Response received: $response" }
        }
    }





    fun kraDataEncryption(hashedData: String): String {
        fun encryptThisString(input: String): String {
            return try {
                val md = MessageDigest.getInstance("SHA-256")
                val  messageDigest = md.digest(input.toByteArray(StandardCharsets.UTF_8))
                //val messageDigest = md.digest(input.toByteArray())
                val no = BigInteger(1, messageDigest)
                var hashtext = no.toString(16)
                while (hashtext.length < 32) {
                    hashtext = "0$hashtext"
                }
                hashtext
            } catch (e: NoSuchAlgorithmException) {
                throw RuntimeException(e)
            }
        }
        return encryptThisString(hashedData)

    }


}







