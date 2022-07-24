package org.kebs.app.kotlin.apollo.api.ports.provided.dao

import com.ctc.wstx.api.WstxInputProperties
import com.ctc.wstx.api.WstxOutputProperties
import com.ctc.wstx.stax.WstxInputFactory
import com.ctc.wstx.stax.WstxOutputFactory
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.dataformat.xml.XmlFactory
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.content.*
import io.ktor.http.*
import mu.KotlinLogging
import org.apache.http.conn.ssl.NoopHostnameVerifier
import org.jasypt.encryption.StringEncryptor
import org.kebs.app.kotlin.apollo.common.dto.kra.request.Request

import org.kebs.app.kotlin.apollo.common.exceptions.InvalidValueException
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.common.utils.generateRandomText
import org.kebs.app.kotlin.apollo.store.model.*
import org.springframework.data.jpa.repository.Modifying
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.remoteAddressOrNull
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.sql.Timestamp
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.criteria.CriteriaUpdate
import javax.persistence.criteria.Path
import javax.persistence.criteria.Root
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.XMLOutputFactory


@Service
class DaoFluxService(
    private val jasyptStringEncryptor: StringEncryptor,
    @PersistenceContext
    private val entityManager: EntityManager,
) {


//    @Bean
//    @Primary
//    fun springValidator(): Validator? {
//        return LocalValidatorFactoryBean()
//    }

    fun generateTransactionReference(
        length: Int = 12,
        secureRandomAlgorithm: String = "SHA1PRNG",
        messageDigestAlgorithm: String = "SHA-512", prefix: Boolean = false,
    ): String {
        return generateRandomText(length, secureRandomAlgorithm, messageDigestAlgorithm, false)
    }


//    @Bean
//    fun jacksonDecoder() = Jackson2JsonDecoder()

    fun mapper(excludeNull: Boolean = false): ObjectMapper {
        val mapper = ObjectMapper()
//            .configure(SerializationFeature.WRAP_ROOT_VALUE, true)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true)
//            .configure(SerializationFeature.WRITE_SELF_REFERENCES_AS_NULL, false)
        when (excludeNull) {
            true -> mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
        }
        mapper.nodeFactory = JsonNodeFactory.withExactBigDecimals(true)
        mapper.setTimeZone(TimeZone.getDefault())


        return mapper
    }


    suspend fun invalidGetOnPostUrl(req: ServerRequest): ServerResponse =
        ServerResponse.badRequest().body("Operation not supported")


    fun extractHostnameFromRequest(req: ServerRequest): String {
        return req.remoteAddressOrNull()?.let { it.address.hostAddress ?: "Anonymous" } ?: "Anonymous"
    }

    fun extractTimestampString(now: Instant): String? =
        DateTimeFormatter.ofPattern("yyyyMMddHHmmss").withLocale(Locale.getDefault()).withZone(ZoneId.systemDefault())
            .format(now)

    final suspend inline fun <reified T> processResponses(
        response: HttpResponse?,
        log: WorkflowTransactionsEntity,
        url: String,
        config: IntegrationConfigurationEntity,
    ): Pair<WorkflowTransactionsEntity, T?> {
        var res: T? = null
        try {


            response
                ?.let { r ->
                    when {
                        r.status.value >= config.okLower && r.status.value <= config.okUpper -> {

                            log.integrationResponse = r.readText()
                            val message =
                                "Received [HttpStatus = ${r.status.value}, Text = ${r.status.description}, URL = $url]!"
                            KotlinLogging.logger { }.info(message)
                            res = mapper().readValue(log.integrationResponse, T::class.java)

                            log.responseMessage = log.responseMessage?.let { "${it}||$message" } ?: message
//                            log.responseStatus = config.successCode


                            log.transactionCompletedDate = Timestamp.from(Instant.now())

                        }
                        else -> {
                            log.integrationResponse = r.readText()
                            val message =
                                "Received [HttpStatus = ${r.status.value}, Text = ${r.status.description}, URL = $url]!"
                            KotlinLogging.logger { }.info(message)
                            log.responseMessage = log.responseMessage?.let { "${it}||${message}" } ?: message
                            log.responseStatus = config.failureCode


                            log.transactionCompletedDate = Timestamp.from(Instant.now())

                        }

                    }


                }
                ?: throw InvalidValueException("Null Response received")
        } catch (e: Exception) {
            KotlinLogging.logger { }.error("Received [Exception = ${e.message}, URL = $url]!")
            KotlinLogging.logger { }.debug("Received [Exception = ${e.message}, URL = $url]!", e)
//            res = daoService.mapper().readValue(r.readText(), c.java)
            log.responseMessage = log.responseMessage?.let { "${it}||${e.message}" } ?: e.message

            log.responseStatus = config.exceptionCode
            log.transactionCompletedDate = Timestamp.from(Instant.now())

        }


        return Pair(log, res)

    }

    fun xmlMapper(): ObjectMapper {
        val ifactory: XMLInputFactory = WstxInputFactory()

        ifactory.setProperty(WstxInputProperties.P_MAX_ATTRIBUTE_SIZE, 32000)

        val ofactory: XMLOutputFactory = WstxOutputFactory()

        ofactory.setProperty(WstxOutputProperties.P_OUTPUT_CDATA_AS_TEXT, true)
        val xf = XmlFactory(ifactory, ofactory)
        val xmlMapper: ObjectMapper = XmlMapper(xf)
            .registerModule(KotlinModule())
//        xmlMapper.registerModule(JodaModule())

        xmlMapper.configure(SerializationFeature.WRITE_SELF_REFERENCES_AS_NULL, false)
        xmlMapper.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false)
        xmlMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        xmlMapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false)
        xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

        xmlMapper.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true)
        xmlMapper.configure(SerializationFeature.WRITE_SELF_REFERENCES_AS_NULL, false)
        /**
         * This line causes an exception to be thrown if the test has more than one thread!
         */
//        xmlMapper.configure(SerializationFeature.INDENT_OUTPUT, true)
        return xmlMapper
    }

    suspend fun getHttpResponseFromPostCall(
        finalUrl: String,
        accessToken: String?,
        payload: Any?,
        config: IntegrationConfigurationEntity?,
        bodyParams: Map<String, String>? = null,
        headerParams: Map<String, String>? = null,
        log: WorkflowTransactionsEntity? = null
    ): HttpResponse? {
        val client = buildClient(false, config)
        return client
            ?.request<HttpResponse>(finalUrl) {
                method = HttpMethod.Post
                headerParams?.forEach { (p, v) -> header(p, v) }
                bodyParams?.forEach { (p, v) -> parameter(p, v) }
                accessToken
                    ?.let {
                        header("Authorization", "Bearer $it")
//                        header("Accept", "application/json")
                    }

                log?.transactionStartDate = Timestamp.from(Instant.now())

                config?.bodyParams
                    ?.let { type ->
                        when {
                            type.contains("json", ignoreCase = true) -> {
                                contentType(ContentType.Application.Json)
                            }
                            type.contains("xml", ignoreCase = true) -> {
                                contentType(ContentType.Application.Xml)
                            }
                            else -> contentType(ContentType.Application.Any)
                        }

                    }




                payload
                    ?.let {
                        config?.bodyParams
                            ?.let { type ->
                                body = when {
                                    type.contains("json", ignoreCase = true) -> {
                                        val x = mapper().writeValueAsString(it) ?: throw NullValueNotAllowedException("Empty body detected after aborting")
                                        log?.integrationRequest = x
                                        x


                                    }
                                    type.contains("xml", ignoreCase = true) -> {
                                        val x = xmlMapper().writeValueAsString(it) ?: throw NullValueNotAllowedException("Empty body detected after mapper aborting")
                                        log?.integrationRequest = x
                                        x
                                    }
                                    else -> it

                                }
                            }


                    }
                    ?: throw NullValueNotAllowedException("Empty payload detected")
//                log?.let { logsRepo.save(it) }

            }?.call?.response?.call?.response


    }

    suspend fun getHttpResponseFromGetCall(
        auth: Boolean,
        url: String,
        config: IntegrationConfigurationEntity,
        payload: Any? = null,
        bodyParams: Map<String, String>? = null,
        headerParams: Map<String, String>? = null,
    ): HttpResponse? {
        return buildClient(auth, config)?.get<HttpResponse>(url) {
            method = HttpMethod.Get
            payload?.let { b ->
                config.bodyParams?.let { type ->
                    body = when {
                        type.contains("json", ignoreCase = true) -> {
                            TextContent(mapper().writeValueAsString(b), ContentType.Application.Json)
                        }
                        type.contains("xml", ignoreCase = true) -> {
                            TextContent(mapper().writeValueAsString(b), ContentType.Application.Xml)
                        }
                        else -> b

                    }
                }

            }
            headerParams?.forEach { (p, v) -> header(p, v) }
            bodyParams?.forEach { (p, v) -> parameter(p, v) }


        }?.call?.response
    }

    private fun buildClient(
        auth: Boolean = false,
        config: IntegrationConfigurationEntity?,
    ): HttpClient? {
        return config?.let { cfg ->
            HttpClient(Apache) {
                expectSuccess = false
                engine {
                    followRedirects = true
                    socketTimeout = cfg.socketTimeout
                    connectTimeout = cfg.connectTimeout
                    connectionRequestTimeout = cfg.connectionRequestTimeout
                    customizeClient {
                        setMaxConnPerRoute(cfg.maxConnPerRoute)
                        setMaxConnTotal(cfg.maxConnTotal)
                        /**
                         * TODO: Ensure to restore security before prod
                         * setSSLContext(
                         * sslContextFactory.createSslContext()
                         * )
                         */

                        setSSLContext(
                            SSLContext.getInstance("TLS")
                                .apply {
                                    init(null, arrayOf(TrustAllX509TrustManager()), SecureRandom())
                                }
                        )
                        setSSLHostnameVerifier(NoopHostnameVerifier())
                    }
                }

                install(Logging) {
                    logger = Logger.DEFAULT
                    level = LogLevel.ALL
                }
                when (auth) {
                    true -> {
                        when (config.clientAuthentication) {
                            "basic" -> {
                                install(Auth) {
                                    basic {

                                        username = jasyptStringEncryptor.decrypt(config.username)
                                        password = jasyptStringEncryptor.decrypt(config.password)
                                        sendWithoutRequest = true
                                    }
                                }
                            }
                            "digest" -> {
                                install(Auth) {
                                    digest {
                                        username = jasyptStringEncryptor.decrypt(config.username)
                                        password = jasyptStringEncryptor.decrypt(config.password)
                                        realm = config.clientAuthenticationRealm

                                    }
                                }

                            }
                            else -> {
                                throw InvalidValueException("Invalid Authentication, ignoring")
                            }
                        }
                    }
                    else -> KotlinLogging.logger { }.debug("Authentication NOT REQUIRED")
                }
            }
        }

    }

    fun createTransactionLog(initStatus: Int, ref: String?): WorkflowTransactionsEntity {
        val log = WorkflowTransactionsEntity()
        with(log) {
            transactionDate = java.sql.Date(Date().time)
            transactionStartDate = Timestamp.from(Instant.now())

            retried = 0
            transactionStatus = initStatus
            createdBy = ref
            transactionReference = ref
            createdOn = Timestamp.from(Instant.now())

        }
        return log
    }

    fun validateHash(hash: Request): Boolean {
        /**
         * TODO: Validate the hash whenever it gets provided
         */
        return true
    }

    @Transactional
    @Modifying
    fun postJobProcessingRecordsCleanUpPenalty(
        jobDetails: BatchJobDetails,
        manufacturersProcessed: MutableList<Long?>,
        log: WorkflowTransactionsEntity,
        transmissionDate: Timestamp?,
        config: IntegrationConfigurationEntity,
    ): Int {

        val cb = entityManager.criteriaBuilder

        val update: CriteriaUpdate<StagingStandardsLevyManufacturerPenalty> = cb.createCriteriaUpdate(StagingStandardsLevyManufacturerPenalty::class.java)
        val root: Root<StagingStandardsLevyManufacturerPenalty> = update.from(StagingStandardsLevyManufacturerPenalty::class.java)
        val finalStatus: Int? = evaluateFinalStatus(log, config, jobDetails)

        update
            .set("status", finalStatus)
            .set("transmissionDate", transmissionDate)
            .set("description", log.transactionReference)
            .set("modifiedBy", log.transactionReference)
            .set("modifiedOn", Timestamp.from(Instant.now()))
        val status: Path<Int?> = root.get("status")
        val manufacturerId: Path<Long?> = root.get("manufacturerId")


        update.where(
            cb.and(
                cb.equal(status, jobDetails.startStatus),
                manufacturerId.`in`(manufacturersProcessed)

            )
        )

        val results = entityManager.createQuery(update).executeUpdate()

        return results
    }

    @Transactional
    @Modifying
    fun postJobProcessingRecordsCleanUp(
        jobDetails: BatchJobDetails,
        manufacturersProcessed: MutableList<Long?>,
        log: WorkflowTransactionsEntity,
        transmissionDate: Timestamp?,
        config: IntegrationConfigurationEntity,
    ): Int {

        val cb = entityManager.criteriaBuilder

        val update: CriteriaUpdate<StagingStandardsLevyManufacturerEntryNumber> = cb.createCriteriaUpdate(StagingStandardsLevyManufacturerEntryNumber::class.java)
        val root: Root<StagingStandardsLevyManufacturerEntryNumber> = update.from(StagingStandardsLevyManufacturerEntryNumber::class.java)
        val finalStatus: Int? = evaluateFinalStatus(log, config, jobDetails)

        update
            .set("status", finalStatus)
            .set("transmissionDate", transmissionDate)
            .set("description", log.transactionReference)
            .set("modifiedBy", log.transactionReference)
            .set("modifiedOn", Timestamp.from(Instant.now()))
        val status: Path<Int?> = root.get("status")
        val manufacturerId: Path<Long?> = root.get("manufacturerId")


        update.where(
            cb.and(
                cb.equal(status, jobDetails.startStatus),
                manufacturerId.`in`(manufacturersProcessed)

            )
        )

        val results = entityManager.createQuery(update).executeUpdate()

        return results
    }

    private fun evaluateFinalStatus(log: WorkflowTransactionsEntity, config: IntegrationConfigurationEntity, jobDetails: BatchJobDetails): Int? {
        return when (log.responseStatus) {
            config.exceptionCode -> jobDetails.endExceptionStatus
            config.failureCode -> jobDetails.endFailureStatus
            config.successCode -> jobDetails.endSuccessStatus
            else -> jobDetails.endExceptionStatus
        }


    }
}

class TrustAllX509TrustManager : X509TrustManager {
    override fun getAcceptedIssuers(): Array<X509Certificate?> = arrayOfNulls(0)

    override fun checkClientTrusted(certs: Array<X509Certificate?>?, authType: String?) {}

    override fun checkServerTrusted(certs: Array<X509Certificate?>?, authType: String?) {}
}
