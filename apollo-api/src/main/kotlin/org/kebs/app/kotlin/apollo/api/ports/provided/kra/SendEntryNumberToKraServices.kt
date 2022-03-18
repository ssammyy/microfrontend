package org.kebs.app.kotlin.apollo.api.ports.provided.kra

import kotlinx.coroutines.runBlocking
import org.jasypt.encryption.StringEncryptor
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DaoService
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.InvoiceDaoService
import org.kebs.app.kotlin.apollo.api.ports.provided.kra.request.KraDetails
import org.kebs.app.kotlin.apollo.api.ports.provided.kra.request.KraHeader
import org.kebs.app.kotlin.apollo.api.ports.provided.kra.request.KraRequest
import org.kebs.app.kotlin.apollo.api.ports.provided.kra.response.KraResponse
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.KraEntryNumberRequestLogEntity
import org.kebs.app.kotlin.apollo.store.model.LogKraEntryNumberRequestEntity
import org.kebs.app.kotlin.apollo.store.model.ServiceMapsEntity
import org.kebs.app.kotlin.apollo.store.model.WorkflowTransactionsEntity
import org.kebs.app.kotlin.apollo.store.repo.IKraEntryNumberRequestLogEntityRepository
import org.kebs.app.kotlin.apollo.store.repo.ILogKraEntryNumberRequestEntityRepository
import org.springframework.stereotype.Service
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


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
            val headerBody = KraHeader().apply {
                transmissionDate = commonDaoServices.getTimestamp()
                loginId  = jasyptStringEncryptor.decrypt(config.username)
                password = jasyptStringEncryptor.decrypt(config.password)
                hash = kraDataEncryption(numberRecords+password+loginId)
                noOfRecords = numberRecords
            }
            val detailBody = KraDetails().apply {
                entryNumber = companyProfile.entryNumber
                kraPin = companyProfile.kraPin
                manufacturerName = companyProfile.name
                registrationDate = companyProfile.createdOn?.let { commonDaoServices.convertTimestampToKraValidDate(it) }
                status = if (companyProfile.manufactureStatus == 1) "Active" else "Inactive"
            }

            val rootRequest = KraRequest().apply {
                header = headerBody
                details = detailBody
            }

            var transactionsRequest = KraEntryNumberRequestLogEntity()
            with(transactionsRequest) {
                requestHash = headerBody.hash
                requestTransmissionDate = headerBody.transmissionDate?.let { commonDaoServices.convertTimestampToKraValidDate(it) }
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
                    rootRequest,
                    config,
                    null,
                    null
            )
            val response: Triple<WorkflowTransactionsEntity, KraResponse?, io.ktor.client.statement.HttpResponse?> =
                    daoService.processResponses(resp, log, configUrl, config)

            with(transactionsRequest) {
                responseStatus = response.second?.status
                responseResponseCode = response.second?.responseCode
                responseMessage = response.second?.message
                status = 1
                updateBy = user
                updatedOn = commonDaoServices.getTimestamp()
            }

            iKraEntryNumberRequestLogEntityRepository.save(transactionsRequest)
        }
    }


    fun kraDataEncryption(hashedData: String): String {
        fun encryptThisString(input: String): String {
            return try {
                val md = MessageDigest.getInstance("SHA-256")
                val messageDigest = md.digest(input.toByteArray())
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