package org.kebs.app.kotlin.apollo.api.ports.provided.dao


import com.ctc.wstx.api.WstxInputProperties
import com.ctc.wstx.api.WstxOutputProperties
import com.ctc.wstx.stax.WstxInputFactory
import com.ctc.wstx.stax.WstxOutputFactory
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.dataformat.csv.CsvSchema
import com.fasterxml.jackson.dataformat.xml.XmlFactory
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.common.io.Files
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.features.json.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.content.*
import io.ktor.http.*
import mu.KotlinLogging
import org.apache.http.conn.ssl.NoopHostnameVerifier
import org.jasypt.encryption.StringEncryptor
import org.kebs.app.kotlin.apollo.api.ports.provided.sftp.SftpServiceImpl
import org.kebs.app.kotlin.apollo.common.dto.CocsItemsEntityDto
import org.kebs.app.kotlin.apollo.common.dto.CorItemsEntityDto
import org.kebs.app.kotlin.apollo.common.dto.CurrencyExchangeRatesEntityDto
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.exceptions.InvalidValueException
import org.kebs.app.kotlin.apollo.common.utils.generateRandomText
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.config.security.ssl.SslContextFactory
import org.kebs.app.kotlin.apollo.store.customdto.*
import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.repo.ICocItemsRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.data.jpa.repository.Modifying
import org.springframework.http.codec.json.AbstractJackson2Decoder
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.Validator
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.remoteAddressOrNull
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.Reader
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDateTime
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
class DaoService(
        private val sslContextFactory: SslContextFactory,
        private val jasyptStringEncryptor: StringEncryptor,
        private val cocItemsRepository: ICocItemsRepository,
        private val applicationMapProperties: ApplicationMapProperties,
        private val sftpService: SftpServiceImpl
) {


    @Bean
    @Primary
    fun springValidator(): Validator? {
        return LocalValidatorFactoryBean()
    }

    fun generateTransactionReference(
            length: Int = 12,
            secureRandomAlgorithm: String = "SHA1PRNG",
            messageDigestAlgorithm: String = "SHA-512", prefix: Boolean = false,
    ): String {
        return generateRandomText(length, secureRandomAlgorithm, messageDigestAlgorithm, false)
    }

    @Bean
    fun jacksonDecoder(): AbstractJackson2Decoder = Jackson2JsonDecoder()

    fun mapper(excludeNull: Boolean = false): ObjectMapper {
        val mapper = ObjectMapper()
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

    final suspend inline fun <reified T : Any> processResponses(
            response: HttpResponse?,
            log: WorkflowTransactionsEntity,
            url: String,
            config: IntegrationConfigurationEntity,
    ): Triple<WorkflowTransactionsEntity, T?, HttpResponse?> {
        var res: T? = null
        try {


            response
                    ?.let { r ->
                        when {
                            r.status.value >= config.okLower && r.status.value <= config.okUpper -> {

                            val responseText = r.readText()

                            KotlinLogging.logger { }.error(responseText)

                            log.integrationResponse = responseText
                            val message =
                                "Received [HttpStatus = ${r.status.value}, Text = ${r.status.description}, URL = $url]!"
                            KotlinLogging.logger { }.info(message)
                            res = mapper().readValue<T>(responseText)

                            log.responseMessage = log.responseMessage?.let { "${it}||$message" } ?: message
                            log.responseStatus = config.successCode


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


        return Triple(log, res, response)

    }

    suspend fun getHttpResponseFromPostCall(
            auth: Boolean,
            finalUrl: String,
            accessToken: String?,
            payload: Any?,
            config: IntegrationConfigurationEntity?,
            bodyParams: Map<String, String>? = null,
            headerParams: Map<String, String>? = null,
    ): HttpResponse? {
        return buildClient(auth, config)
                ?.request<HttpResponse>(finalUrl) {
                    method = HttpMethod.Post
                    headerParams?.forEach { (p, v) -> header(p, v) }
                    bodyParams?.forEach { (p, v) -> parameter(p, v) }
//                header("Authorization", "Bearer $accessToken")
//                header("Accept", "application/json")

                    payload?.let {
                        body = TextContent(mapper().writeValueAsString(it), ContentType.Application.Json)
                    }

                }?.call?.response?.call?.response
    }

    suspend fun getHttpResponseFromGetCall(
        auth: Boolean,
        url: String,
        config: IntegrationConfigurationEntity,
        payload: Any? = null,
        bodyParams: Map<String, String?>? = null,
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
                install(JsonFeature) {
                    serializer = GsonSerializer()
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
                    else -> KotlinLogging.logger { }.info("Authentication NOT REQUIRED")

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

    fun validateHash(hash: String?): Boolean {
        /**
         * TODO: Validate the hash whenever it gets provided
         */
        return true
    }

    private fun evaluateFinalStatus(
            log: WorkflowTransactionsEntity,
            config: IntegrationConfigurationEntity,
            jobDetails: BatchJobDetails
    ): Int? {
        return when (log.responseStatus) {
            config.exceptionCode -> jobDetails.endExceptionStatus
            config.failureCode -> jobDetails.endFailureStatus
            config.successCode -> jobDetails.endSuccessStatus
            else -> jobDetails.endExceptionStatus
        }


    }

    val csvMapper = CsvMapper().apply {
        configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        registerModule(KotlinModule())
    }

    fun readExchangeRatesFromController(separator: Char, reader: Reader) =
            readCsvFile<CurrencyExchangeRatesEntityDto>(separator, reader)

    fun readCocFileFromController(separator: Char, reader: Reader) =
            readCsvFile<CocsItemsEntityDto>(separator, reader)

    fun readCorFileFromController(separator: Char, reader: Reader) = readCsvFile<CorItemsEntityDto>(separator, reader)

    private inline fun <reified T> readCsvFile(separator: Char, reader: Reader): List<T> {
//        FileReader(fileName).use { reader ->
        return csvMapper
                .readerFor(T::class.java)
                .with(
                        CsvSchema
                                .emptySchema()
                                .withColumnSeparator(separator)
                                .withLineSeparator("\n")
                                .withHeader()
                )
                .readValues<T>(reader)
                .readAll()
                .toList()
    }

    val xmlMapper: ObjectMapper = run {
        val iFactory: XMLInputFactory = WstxInputFactory()
        iFactory.setProperty(WstxInputProperties.P_MAX_ATTRIBUTE_SIZE, 32000)
        val oFactory: XMLOutputFactory = WstxOutputFactory()
        oFactory.setProperty(WstxOutputProperties.P_OUTPUT_CDATA_AS_TEXT, true)
        val xf = XmlFactory(iFactory, oFactory)
        val xmlMapper: ObjectMapper = XmlMapper(xf)
                .registerModule(KotlinModule())
        xmlMapper.configure(SerializationFeature.WRITE_SELF_REFERENCES_AS_NULL, false)
        xmlMapper.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false)
        xmlMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        xmlMapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false)
        xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        xmlMapper.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true)
        xmlMapper.configure(SerializationFeature.WRITE_SELF_REFERENCES_AS_NULL, false)
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT)
        /**
         * This line causes an exception to be thrown if the test has more than one thread!
         */
//        xmlMapper.configure(SerializationFeature.INDENT_OUTPUT, true)
        xmlMapper
    }

    fun createKesWsFileName(filePrefix: String, documentIdentifier: String): String {
        val current = LocalDateTime.now()

        val formatter = DateTimeFormatter.ofPattern("yyyymmddhhmmss")
        val formatted = current.format(formatter)

        var finalFileName = filePrefix
                .plus("-")
                .plus(documentIdentifier)
                .plus("-1-B-")
                .plus(formatted)
                .plus(".xml")
        finalFileName = finalFileName.replace("\\s".toRegex(), "")

        return finalFileName
    }


    class TrustAllX509TrustManager : X509TrustManager {
        override fun getAcceptedIssuers(): Array<X509Certificate?> = arrayOfNulls(0)

        override fun checkClientTrusted(certs: Array<X509Certificate?>?, authType: String?) {}

        override fun checkServerTrusted(certs: Array<X509Certificate?>?, authType: String?) {}
    }
}

