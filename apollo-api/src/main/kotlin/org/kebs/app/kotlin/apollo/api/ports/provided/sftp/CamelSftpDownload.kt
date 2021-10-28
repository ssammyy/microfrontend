package org.kebs.app.kotlin.apollo.api.ports.provided.sftp

import com.fasterxml.jackson.databind.DeserializationFeature
import mu.KotlinLogging
import org.apache.camel.Exchange
import org.apache.camel.ProducerTemplate
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.model.dataformat.JacksonXMLDataFormat
import org.apache.http.client.utils.URIBuilder
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.*
import org.kebs.app.kotlin.apollo.common.dto.kesws.receive.*
import org.kebs.app.kotlin.apollo.config.properties.camel.CamelFtpProperties
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import java.net.URI
import java.nio.file.Paths
import java.util.concurrent.TimeUnit


@Service
@Profile("!prod")
class SFTPService(
        private val iDFDaoService: IDFDaoService,
        private val declarationDaoService: DeclarationDaoService,
        private val manifestDaoService: ManifestDaoService,
        private val destinationInspectionDaoServices: DestinationInspectionDaoServices,
        private val consignmentDocumentDaoService: ConsignmentDocumentDaoService,
        private val producerTemplate: ProducerTemplate
) {
    fun uploadFile(fileName: String) {
        try {
            val endpointName = "direct:uploadFileToFtp"
            val exchange: Exchange = producerTemplate.camelContext.getEndpoint(endpointName).createExchange()
            exchange.message.headers.put("fileName", fileName)
            // Deliver message
            producerTemplate.send(endpointName, exchange)
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Faile to send file via exchange: ", ex)
        }
    }

    fun processConsignmentDocumentType(exchange: Exchange) {
        KotlinLogging.logger { }.info("CD File: ${exchange.message.headers} | Content: ${exchange.message.body}|")
        val consignmentDoc = exchange.message.body as ConsignmentDocument
        // TOD: see this document saving xml
        val updated = consignmentDocumentDaoService.insertConsignmentDetailsFromXml(consignmentDoc, byteArrayOf())
        KotlinLogging.logger { }.info("CD File: ${exchange.message.headers} | Save Status: ${updated}|")
    }

    fun processDocumentResponses(exchange: Exchange) {
        KotlinLogging.logger { }.info("Manifest Document: ${exchange.message.headers} | Content: ${exchange.message.body}|")
        val ucrNumberMessage = exchange.message.body as UCRNumberMessage
        val baseDocRefNo = ucrNumberMessage.data?.dataIn?.sadId
        val ucrNumber = ucrNumberMessage.data?.dataIn?.ucrNumber
        if (baseDocRefNo == null || ucrNumber == null) {
            KotlinLogging.logger { }.error { "BaseDocRef Number or UcrNumber missing" }
            throw Exception("BaseDocRef Number or UcrNumber missing")
        }
        val idfUpdated = iDFDaoService.updateIdfUcrNumber(baseDocRefNo, ucrNumber)
        KotlinLogging.logger { }.info("Manifest Document: ${exchange.message.headers} | Saved: ${idfUpdated}|")
    }

    fun processDeclarationVerificationDocumentType(exchange: Exchange) {
        KotlinLogging.logger { }.info("Verification Document Type: ${exchange.message.headers} | Content: ${exchange.message.body}|")
        val declarationVerificationDocumentMessage = exchange.message.body as DeclarationVerificationMessage
        val docSaved = destinationInspectionDaoServices.updateCdVerificationSchedule(declarationVerificationDocumentMessage)
        KotlinLogging.logger { }.info("Verification Document Type: ${exchange.message.headers} | Saved Status: ${docSaved}|")
    }

    fun processUcrResultDocument(exchange: Exchange) {
        KotlinLogging.logger { }.info("UCR Res Document: ${exchange.message.headers} | Content: ${exchange.message.body}|")
        val ucrNumberMessage = exchange.message.body as UCRNumberMessage
        val baseDocRefNo = ucrNumberMessage.data?.dataIn?.sadId
        val ucrNumber = ucrNumberMessage.data?.dataIn?.ucrNumber
        if (baseDocRefNo == null || ucrNumber == null) {
            KotlinLogging.logger { }.error {
                "BaseDocRef Number or Uc" +
                        "rNumber missing"
            }
            throw Exception("BaseDocRef Number or UcrNumber missing")
        }
        val idfUpdated = iDFDaoService.updateIdfUcrNumber(baseDocRefNo, ucrNumber)
        KotlinLogging.logger { }.info("UCR Res Document: ${exchange.message.headers} | Saved Status: ${idfUpdated}|")
    }

    fun processAirManifestDocument(exchange: Exchange) {
        KotlinLogging.logger { }.info("Air Manifest Document: ${exchange.message.headers} | Content: ${exchange.message.body}|")
        val manifestDocumentMessage = exchange.message.body as ManifestDocumentMessage
        val docSaved = manifestDaoService.mapManifestMessageToManifestEntity(manifestDocumentMessage)
        KotlinLogging.logger { }.info("Air Manifest Document: ${exchange.message.headers} | Saved Status: ${docSaved}|")
    }

    fun processManifestDocument(exchange: Exchange) {
        KotlinLogging.logger { }.info("Manifest Document: ${exchange.message.headers} | Content: ${exchange.message.body}|")
        val manifestDocumentMessage = exchange.message.body as ManifestDocumentMessage
        val docSaved = manifestDaoService.mapManifestMessageToManifestEntity(manifestDocumentMessage)
        KotlinLogging.logger { }.info("Manifest Document: ${exchange.message.headers} | Saved Status: ${docSaved}|")
    }

    fun processDeclarationDocumentType(exchange: Exchange) {
        KotlinLogging.logger { }.info("DECLARATION DOC RES: ${exchange.message.headers} | Content: ${exchange.message.body}|")
        val declarationDocumentMessage = exchange.message.body as DeclarationDocumentMessage
        val docSaved = declarationDaoService.mapDeclarationMessageToEntities(declarationDocumentMessage)
        KotlinLogging.logger { }.info("DECLARATION DOC RES: ${exchange.message.headers} | Result: ${docSaved}|")
    }

    fun processBaseDocumentType(exchange: Exchange) {
        KotlinLogging.logger { }.info("Base Document Type: ${exchange.message.headers} | Content: ${exchange.message.body}|")
        val baseDocumentResponse = exchange.message.body as BaseDocumentResponse
        val docSaved = iDFDaoService.mapBaseDocumentToIDF(baseDocumentResponse)
        KotlinLogging.logger { }.info("Base Document Type: ${exchange.message.headers} | Save status: ${docSaved}|")
    }
}

/**
 * Camel component to download files from FTP server and unmarshal to expected class
 * Ref: https://access.redhat.com/documentation/en-us/red_hat_jboss_fuse/6.2/html/apache_camel_component_reference/idu-ftp2
 */
@Component
@Profile("!prod")
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
    val keswsDocTypes = listOf(applicationMapProperties.mapKeswsBaseDocumentDoctype, applicationMapProperties.mapKeswsUcrResDoctype,
            applicationMapProperties.mapKeswsDeclarationDoctype, applicationMapProperties.mapKeswsManifestDoctype, applicationMapProperties.mapKeswsAirManifestDoctype,
            applicationMapProperties.mapKeswsCdDoctype, applicationMapProperties.mapKeswsDeclarationVerificationDoctype)

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
                        .addParameter("maxMessagesPerPoll", "10")
                        .addParameter("moveFailed", properties.moveFailed)
                        .addParameter("autoCreate", "false")
                        .addParameter("greedy", "true") // If you find file, don't delay the next check for more files
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
                .bean(SFTPService::class.java, "processConsignmentDocumentType")
                .log("Downloaded file \${in.headers.CamelFileName} processed.")
                .`when`(header("CamelFileName").startsWith(applicationMapProperties.mapKeswsUcrResDoctype))
                .unmarshal(createXmlMapper(UCRNumberMessage::class.java))
                .bean(SFTPService::class.java, "processUcrResultDocument")
                .log("UCR Response file \${in.headers.CamelFileName} processed.")
                .`when`(header("CamelFileName").startsWith(applicationMapProperties.mapKeswsDeclarationVerificationDoctype))
                .unmarshal(createXmlMapper(DeclarationVerificationMessage::class.java))
                .bean(SFTPService::class.java, "processDeclarationVerificationDocumentType")
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
                .bean(SFTPService::class.java, "processDeclarationDocumentType")
                .log("Manifest document \${in.headers.CamelFileName} processed.")
                .`when`(header("CamelFileName").startsWith(applicationMapProperties.mapKeswsBaseDocumentDoctype))
                .unmarshal(createXmlMapper(BaseDocumentResponse::class.java))
                .bean(SFTPService::class.java, "processBaseDocumentType")
                .log("Base document type \${in.headers.CamelFileName} processed.")
                .otherwise()
                .setHeader("error", simple("Invalid file received: \${in.headers.CamelFileName}"))
                .log("Invalid file \${file:name} complete.")
                .setHeader("moveFailed", simple( "\${in.headers.CamelFileName}"))
                .throwException(java.lang.Exception(header("error").toString()))
                .endChoice()
                .end()
    }
}

/**
 * Endpoint to upload files to SFTP
 *
 */
@Component
@Profile("!prod")
class CamelSftpUpload(
        private val properties: CamelFtpProperties,
) : RouteBuilder() {
    private val ftpBuilder = URIBuilder()
            .setScheme(properties.scheme)
            .setHost(properties.host)
            .setPort(properties.port)
            .setPath(properties.uploadDirectory)
    private val fileBuilder = URIBuilder()
            .setScheme("file")
            .setPath(Paths.get("")
                    .toAbsolutePath()
                    .toString() + properties.outboundDirectory)
            .setParameter("runLoggingLevel", properties.logLevel)
            .setParameter("delete", "true")
            .setParameter("moveFailed", "error")
            .setParameter("antInclude", properties.antInclude)
            .setParameter("autoCreate", "true")
    private lateinit var uploadURI: URI

    init {
        ftpBuilder.addParameter("autoCreate", "false")
        uploadURI = ftpBuilder.build()
    }

    override fun configure() {
        // Upload file to FTP
        from(fileBuilder.build().toString())
                .log("Uploading file to FTP server: \${in.headers.CamelFileName}")
                .to(uploadURI.toString())
                .log("Uploaded file to FTP server: \${in.headers.CamelFileName}")
    }
}