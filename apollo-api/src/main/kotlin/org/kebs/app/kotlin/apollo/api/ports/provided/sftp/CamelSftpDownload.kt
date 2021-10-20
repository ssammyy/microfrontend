package org.kebs.app.kotlin.apollo.api.ports.provided.sftp

import com.fasterxml.jackson.databind.DeserializationFeature
import mu.KotlinLogging
import org.apache.camel.Exchange
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.model.dataformat.JacksonXMLDataFormat
import org.apache.http.client.utils.URIBuilder
import org.kebs.app.kotlin.apollo.common.dto.kesws.receive.*
import org.kebs.app.kotlin.apollo.config.properties.camel.CamelFtpProperties
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import java.net.URI
import java.util.concurrent.TimeUnit
import java.lang.Class as Class

@Service
@Profile("default")
class SFTPService {
    fun processConsignmentDocument(exchange: Exchange) {
        KotlinLogging.logger { }.info("File: ${exchange.message.headers} | Content: ${exchange.message.body}|")
    }

    fun processDocumentResponses(exchange: Exchange) {
        KotlinLogging.logger { }.info("UCR RES: ${exchange.message.headers} | Content: ${exchange.message.body}|")
    }

    fun processDeclarationDocument(exchange: Exchange) {
        KotlinLogging.logger { }.info("Declaration Document: ${exchange.message.headers} | Content: ${exchange.message.body}|")
    }

    fun processAirManifestDocument(exchange: Exchange) {
        KotlinLogging.logger { }.info("Air Manifest Document: ${exchange.message.headers} | Content: ${exchange.message.body}|")
    }

    fun processManifestDocument(exchange: Exchange) {
        KotlinLogging.logger { }.info("Manifest Document: ${exchange.message.headers} | Content: ${exchange.message.body}|")
    }

    fun processDeclarationResDocument(exchange: Exchange) {
        KotlinLogging.logger { }.info("Manifest Document: ${exchange.message.headers} | Content: ${exchange.message.body}|")
    }

    fun processBaseDocumentType(exchange: Exchange) {
        KotlinLogging.logger { }.info("Base Document Type: ${exchange.message.headers} | Content: ${exchange.message.body}|")
    }
}

@Component
@Profile("default")
class CamelSftpDownload(
        private val properties: CamelFtpProperties,
        private val applicationMapProperties: ApplicationMapProperties,
) : RouteBuilder() {
    private val ftpBuilder = URIBuilder()
            .setScheme(properties.scheme)
            .setHost(properties.host)
            .setPort(properties.port)
            .setPath(properties.path)
    private lateinit var fromFtpUrl: URI;

    init {
        ftpBuilder
                .addParameter("username", properties.userName)
                .addParameter("password", properties.password)
                .addParameter("passiveMode", properties.passiveMode)
                .addParameter("initialDelay", properties.initialDelay)
                .addParameter("delay", properties.delay)
                .addParameter("noop", "false")
                .addParameter("move", properties.move)
                .addParameter("preMove", properties.preMove)
                .addParameter("streamDownload", "true") // Avoid in memory loading of files
                .addParameter("localWorkDirectory", "/tmp") // Needed with streamDownload
                .addParameter("download", "true") // Get file into exchange body
        when (properties.scheme) {
            "ftps" -> {
                ftpBuilder.addParameter("runLoggingLevel", properties.logLevel)
                        .addParameter("moveFailed", properties.moveFailed)
                        .addParameter("maximumReconnectAttempts", "50")
                        .addParameter("binary", "true")
                        .addParameter("startingDirectoryMustExist", "true")
                        .addParameter("sendEmptyMessageWhenIdle", "false")
                        .addParameter("siteCommand", "pwd\nls -lrth")
                        .addParameter("include", properties.antInclude)
                        .addParameter("autoCreate", "false")
            }
            "sftp" -> {
                ftpBuilder.addParameter("runLoggingLevel", properties.logLevel)
                        .addParameter("readLock", properties.readLock)
                        .addParameter("autoCreate", "false")
                        .addParameter("timeUnit", TimeUnit.MILLISECONDS.name)
                        .addParameter("useFixedDelay", "true")
                        .addParameter("antInclude", properties.antInclude)
                        .addParameter("strictHostKeyChecking", "yes")
                        .addParameter("useUserKnownHostsFile", properties.useUserKnownHostsFile)
                        .addParameter("readLockMinAge", properties.readLockMinAge)
                        .addParameter("readLockTimeout", properties.readLockTimeout)
                        .addParameter("readLockCheckInterval", properties.readLockCheckInterval)
            }
        }
        ftpBuilder.addParameter("stepwise", properties.stepwise)
        this.fromFtpUrl = ftpBuilder.build()

    }

    fun <T> createXmlMapper(classz: Class<T>): JacksonXMLDataFormat {
        val documentFormat = JacksonXMLDataFormat()
        documentFormat.disableFeatures = arrayOf(
                DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES.name,
                DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES.name).joinToString(",")
        documentFormat.enableFeatures = arrayOf(
                DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS.name,
                DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT.name).joinToString(",")
        documentFormat.unmarshalType = classz
        return documentFormat
    }

    override fun configure() {
        KotlinLogging.logger { }.debug("DEBUG: $fromFtpUrl")

        from(fromFtpUrl.toString())
                .log("Processing file.... \${in.headers.CamelFileName}")
                .choice()
                .`when`(header("CamelFileName").startsWith(applicationMapProperties.mapKeswsCdDoctype)) // Process consignment documents
                .unmarshal(createXmlMapper(ConsignmentDocument::class.java))
                .bean(SFTPService::class.java, "processConsignmentDocument")
                .log("Downloaded file \${in.headers.CamelFileName} processed.")
                .`when`(header("CamelFileName").startsWith(applicationMapProperties.mapKeswsUcrResDoctype))
                .unmarshal(createXmlMapper(UCRNumberMessage::class.java))
                .bean(SFTPService::class.java, "processDocumentResponses")
                .log("UCR Response file \${in.headers.CamelFileName} processed.")
                .`when`(header("CamelFileName").startsWith(applicationMapProperties.mapKeswsUcrResDoctype))
                .unmarshal(createXmlMapper(DeclarationVerificationMessage::class.java))
                .bean(SFTPService::class.java, "processDeclarationResDocument")
                .log("Declaration document \${in.headers.CamelFileName} processed.")
                .`when`(header("CamelFileName").startsWith(applicationMapProperties.mapKeswsAirManifestDoctype))
                .unmarshal(createXmlMapper(ManifestDocumentMessage::class.java))
                .bean(SFTPService::class.java, "processAirManifestDocument")
                .log("Air Manifest document \${in.headers.CamelFileName} processed.")
                .`when`(header("CamelFileName").startsWith(applicationMapProperties.mapKeswsManifestDoctype))
                .unmarshal(createXmlMapper(ManifestDocumentMessage::class.java))
                .bean(SFTPService::class.java, "processManifestDocument")
                .log("Manifest document \${in.headers.CamelFileName} processed.")
                .`when`(header("CamelFileName").startsWith(applicationMapProperties.mapKeswsDeclarationDoctype))
                .unmarshal(createXmlMapper(DeclarationDocumentMessage::class.java))
                .bean(SFTPService::class.java, "processManifestDocument")
                .log("Manifest document \${in.headers.CamelFileName} processed.")
                .`when`(header("CamelFileName").startsWith(applicationMapProperties.mapKeswsBaseDocumentDoctype))
                .unmarshal(createXmlMapper(BaseDocumentResponse::class.java))
                .bean(SFTPService::class.java, "processBaseDocumentType")
                .log("Base document type \${in.headers.CamelFileName} processed.")
                .otherwise()
                .setHeader("error", header("CamelFileName"))
                .log("Invalid file \${file:name} complete.")
                .endChoice()
    }
}