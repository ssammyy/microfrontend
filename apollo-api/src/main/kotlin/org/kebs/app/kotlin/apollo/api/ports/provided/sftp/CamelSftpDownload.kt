package org.kebs.app.kotlin.apollo.api.ports.provided.sftp

import com.fasterxml.jackson.databind.DeserializationFeature
import mu.KotlinLogging
import org.apache.camel.Exchange
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.model.dataformat.JacksonXMLDataFormat
import org.apache.http.client.utils.URIBuilder
import org.kebs.app.kotlin.apollo.common.dto.kesws.receive.ConsignmentDocument
import org.kebs.app.kotlin.apollo.config.properties.camel.CamelFtpProperties
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import java.net.URI
import java.util.concurrent.TimeUnit

@Service
@Profile("default")
class SFTPService {
    fun downloadAndProcessFile(exchange: Exchange) {
        KotlinLogging.logger { }.info("File: ${exchange.message.headers} | Content: ${exchange.message.body}|")
    }
}

@Component
@Profile("default")
class CamelSftpDownload(
        private val properties: CamelFtpProperties
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
                        .addParameter("timeUnit",TimeUnit.MILLISECONDS.name)
                        .addParameter("useFixedDelay","true")
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

    override fun configure() {
        KotlinLogging.logger { }.info("DEBUG: $fromFtpUrl")
        val documentFormat = JacksonXMLDataFormat()
//        xmlMapper.configure(SerializationFeature.WRITE_SELF_REFERENCES_AS_NULL, false)
//        xmlMapper.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false)
//        xmlMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
//        xmlMapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false)
//        xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
//        xmlMapper.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true)
//        xmlMapper.configure(SerializationFeature.WRITE_SELF_REFERENCES_AS_NULL, false)
//        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT)


        documentFormat.disableFeatures = arrayOf(
                DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES.name,
                DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES.name).joinToString(",")
        documentFormat.enableFeatures = arrayOf(
                DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS.name,
                DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT.name).joinToString(",")
        documentFormat.unmarshalType = ConsignmentDocument::class.java

        from(fromFtpUrl.toString())
                .log("Polling.... \${in.body} \${in.headers.CamelFileName}")
                .setHeader("fileName").simple("\${in.headers.CamelFileName}")
                .unmarshal(documentFormat)
                .bean(SFTPService::class.java, "downloadAndProcessFile")
                .log("Downloaded file \${file:name} complete.")
    }
}