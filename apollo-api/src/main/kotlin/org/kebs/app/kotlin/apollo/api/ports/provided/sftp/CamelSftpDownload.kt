package org.kebs.app.kotlin.apollo.api.ports.provided.sftp

import com.fasterxml.jackson.databind.DeserializationFeature
import mu.KotlinLogging
import org.apache.camel.Exchange
import org.apache.camel.ProducerTemplate
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.component.file.GenericFile
import org.apache.camel.model.dataformat.JacksonXMLDataFormat
import org.apache.http.client.utils.URIBuilder
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.*
import org.kebs.app.kotlin.apollo.common.dto.kesws.receive.*
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.config.properties.camel.CamelFtpProperties
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.SftpTransmissionEntity
import org.kebs.app.kotlin.apollo.store.repo.ISftpTransmissionEntityRepository
import org.springframework.context.ApplicationEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.core.io.Resource
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import java.io.File
import java.net.URI
import java.nio.file.Files
import java.nio.file.Paths
import java.sql.Timestamp
import java.time.Instant
import java.util.*
import java.util.concurrent.TimeUnit

val directUploadEndpoint = "direct:save-ftp-file"
val documentTypeHeader = "documentDirection"
val unprocesable = "UNPROCESSABLE"
val maxRetries: Long = 30

class FileDetails {
    var fileType: String? = null
    var fileName: String? = null
    var remoteReference: String? = null
    var fileVersion: String? = null
    var fileDate: String? = null
    var fileCode: String? = null
    var errorResponse: Boolean = false

    // Response have file version as different
    // 2 for responses
    fun isResponse(): Boolean {
        return !"1".equals(fileVersion)
    }

    companion object {
        fun parseFileDetails(fileName: String): FileDetails {
            val fileProps = fileName.split("-")
            val fileDetails = FileDetails()
            fileDetails.fileType = fileProps[0]
            fileDetails.remoteReference = fileProps[1]
            fileDetails.fileName = fileName
            fileDetails.fileVersion = fileProps[2]
            fileDetails.fileDate = fileProps[3]
            fileDetails.fileCode = fileProps[4]
            return fileDetails
        }
    }
}

class KeswFileEvents(data: FileDetails) : ApplicationEvent(data) {

}

@Service
class SFTPService(
        private val iDFDaoService: IDFDaoService,
        private val declarationDaoService: DeclarationDaoService,
        private val manifestDaoService: ManifestDaoService,
        private val destinationInspectionDaoServices: DestinationInspectionDaoServices,
        private val consignmentDocumentDaoService: ConsignmentDocumentDaoService,
        private val producerTemplate: ProducerTemplate,
        private val eventPublisher: ApplicationEventPublisher,
        private val properties: CamelFtpProperties,
        private val resourceLoader: ResourceLoader,
        private val applicationMapProperties: ApplicationMapProperties,
        private val sftpRepository: ISftpTransmissionEntityRepository
) {
    val ignoreDocuments = setOf(applicationMapProperties.mapKeswsErrorDocument, applicationMapProperties.mapKeswsUcrResDoctype)
    fun uploadFile(file: File, move: Boolean = false): Boolean {
        try {
            val fileCp = Paths.get(properties.outboundDirectory, file.name).toFile()
            if (move) {
                // Move file to outbound folder
                file.renameTo(fileCp)
            } else {
                // Copy to destination
                file.copyTo(fileCp, overwrite = true)
            }
            KotlinLogging.logger { }.info("Sending file manually")
            return true
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to send file via exchange: ", ex)
        }
        return false
    }

    fun resubmitFile(fileDetails: SftpTransmissionEntity): Boolean {
        val successPath = Paths.get(properties.preMove, fileDetails.filename)
        val failedPath = Paths.get(properties.outboundDirectory, "error", fileDetails.filename)
        if (Files.exists(successPath)) {
            return this.uploadFile(successPath.toFile(), move = true)
        } else if (Files.exists(failedPath)) {
            return this.uploadFile(failedPath.toFile(), move = true)
        }
        return false
    }

    fun getUploadedDownloadedFile(fileName: String?, flowDirection: String?, successful: Boolean): String {
        when (flowDirection) {
            "IN" -> {
                return ""
            }
            "OUT" -> {
                try {
                    val resouce: Resource
                    if (successful) {
                        resouce = this.resourceLoader.getResource("${this.properties.preMove}/${fileName}")
                    } else {
                        resouce = this.resourceLoader.getResource("${this.properties.outboundDirectory}/error/${fileName}")
                    }
                    return resouce.file.readText()
                } catch (ex: Exception) {
                    KotlinLogging.logger { }.error("Failed to open file")
                    throw ExpectedDataNotFound("Could not find file specified")
                }

            }
            else -> {
                throw ExpectedDataNotFound("invalid message direction: $flowDirection")
            }
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
        KotlinLogging.logger { }
            .info("Manifest Document: ${exchange.message.headers} | Content: ${exchange.message.body}|")
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
        KotlinLogging.logger { }
            .info("UCR Res Document: ${exchange.message.headers} | Content: ${exchange.message.body}|")
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
        KotlinLogging.logger { }.info("IDF Base Document Type: ${exchange.message.headers} | Content: ${exchange.message.body}|")
        val baseDocumentResponse = exchange.message.body as BaseDocumentResponse
        val docSaved = iDFDaoService.mapBaseDocumentToIDF(baseDocumentResponse)
        KotlinLogging.logger { }.info("IDF Base Document Type: ${exchange.message.headers} | Save status: ${docSaved}|")
    }

    /**
     * Process ERR_MSG documents
     *
     */
    fun processErrorDocument(exchange: Exchange) {
        KotlinLogging.logger { }.info("Error Document: ${exchange.message.headers} | Content: ${exchange.message.body}|")
        val keswsErrorResponse = exchange.message.body as KeswsErrorResponse
        val log = keswsErrorResponse.documentDetails?.fileName?.let { sftpRepository.findFirstByFilenameOrderByCreatedOn(it) }
        log?.let { fileLog ->
            fileLog.keswErrorCode = keswsErrorResponse.errorInformation?.errorCode
            fileLog.keswErrorMessage = keswsErrorResponse.errorInformation?.errorDescription
            sftpRepository.save(fileLog)
            // Publish error message from KESW
            keswsErrorResponse.documentDetails?.fileName?.let {
                val data = FileDetails.parseFileDetails(it)
                data.errorResponse = true
                this.eventPublisher.publishEvent(KeswFileEvents(data))
            }
        } ?: run {
            KotlinLogging.logger { }.warn("Could not attach error message to any document")
            log
        }
    }

    /**
     * Update retries if file exists
     */
    fun addDownloadProcessing(exchange: Exchange) {
        val fileName = exchange.`in`.getHeader("CamelFileName") as String
        this.sftpRepository.findFirstByFilenameOrderByCreatedOn(fileName)?.let { log ->
            log.retryCount = (log.retryCount ?: 0) + 1
            log.retries = maxRetries
            log.lastUpdated = Date()
            log.flowDirection = exchange.message.getHeader(documentTypeHeader) as String
            this.sftpRepository.save(log)
            log
        } ?: run {
            val log = SftpTransmissionEntity()
            log.transactionDate = Date()
            log.retries = 0
            log.retryCount = 0
            log.transactionStartDate = Timestamp.from(Instant.now())
            log.callingMethod = Thread.currentThread().name
            log.flowDirection = exchange.message.getHeader(documentTypeHeader) as String
            try {
                when (exchange.`in`.body) {
                    is GenericFile<*> -> {
                        val file = exchange.message.body as GenericFile<*>
                        val fileProps = file.fileName.split("-")
                        log.fileType = fileProps[0]
                        log.remoteReference = fileProps[1]
                        log.filename = fileName
                        log.varField2 = file.fileLength.toString()
                    }
                    is File -> {
                        val file = exchange.message.body as File
                        val fileProps = file.name.split("-")
                        log.fileType = fileProps[0]
                        log.remoteReference = fileProps[1]
                        log.filename = fileName
                        log.varField2 = file.length().toString()
                    }
                    else -> {
                        val fileProps = fileName.split("-")
                        log.fileType = fileProps[0]
                        log.remoteReference = fileProps[1]
                        log.filename = fileName
                    }
                }
                // Ignore Saving Error and Response documents
                if (ignoreDocuments.contains(log.fileType)) {
                    return
                }
                log.transactionReference = exchange.exchangeId
                log.transactionStatus = 1
                log.responseMessage = "Successfully "
                log.responseStatus = "00"
                log.transactionCompletedDate = Timestamp.from(Instant.now())
                sftpRepository.save(log)
            } catch (ex: ClassCastException) {
                KotlinLogging.logger { }.warn("FAILED to save on error", ex)
            } catch (e: Exception) {
                log.transactionStatus = 10
                log.responseMessage = e.message
                log.transactionCompletedDate = Timestamp.from(Instant.now())
                KotlinLogging.logger { }.error("An error occurred while downloading sftp files in the inner loop: ", e)
                try {
                    sftpRepository.save(log)
                } catch (ex: Exception) {
                    KotlinLogging.logger { }.error("FAILED to save on error", ex)
                }
            }
            log
        }

    }

    fun fileReceivedResponse(exchange: Exchange) {
        val fileName = exchange.`in`.getHeader("CamelFileName") as String
        sftpRepository.findFirstByFilenameOrderByCreatedOn(fileName)?.let { log ->
            val logData = log
            // Save response as success
            log.flowDirection = exchange.message.getHeader(documentTypeHeader) as String
            logData.keswErrorCode = "00"
            logData.keswErrorMessage = fileName
            logData.kesResultDate = Timestamp.from(Instant.now())
            sftpRepository.save(logData)
            // Publish event on response received
            this.eventPublisher.publishEvent(KeswFileEvents(FileDetails.parseFileDetails(fileName)))
        }
    }

    fun errorProcessingRequest(exchange: Exchange) {
        val caused = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Throwable::class.java)
        KotlinLogging.logger { }.error("ERROR: ", caused)
        sftpRepository.findFirstByTransactionReference(exchange.exchangeId)?.let { log ->
            try {
                val logData = log
                log.flowDirection = exchange.message.getHeader(documentTypeHeader) as String
                logData.transactionStatus = 20
                var errorMessage: String? = exchange.message.getHeader("error") as String
                if (!StringUtils.hasLength(errorMessage)) {
                    errorMessage = caused.message
                }
                // Trim extra characters
                if (StringUtils.hasLength(errorMessage) && (errorMessage?.length ?: 0) > 4001) {
                    errorMessage = errorMessage?.substring(0, 4000)
                }
                logData.responseMessage = errorMessage
                logData.responseStatus = "99"
                logData.transactionCompletedDate = Timestamp.from(Instant.now())
                sftpRepository.save(logData)
            } catch (ex: Exception) {
                KotlinLogging.logger { }.error("Failed to update file details", ex)
            }
        } ?: run {
            KotlinLogging.logger { }.warn("Could not attach error message to any document", caused)
            caused
        }
    }

    fun checkIsResponse(exchange: Exchange): Boolean {
        exchange.message.getHeader("CamelFileName")?.let {
            val details = FileDetails.parseFileDetails(it as String)
            return details.isResponse()
        }
        return false
    }

    fun successfulProcessingRequest(exchange: Exchange) {
        sftpRepository.findFirstByTransactionReference(exchange.exchangeId)?.let { log ->
            val logData = log
            log.flowDirection = exchange.message.getHeader(documentTypeHeader) as String
            logData.transactionStatus = 1
            logData.responseMessage = "Successful Processing"
            logData.responseStatus = "00"
            logData.transactionCompletedDate = Timestamp.from(Instant.now())
            sftpRepository.save(logData)
        }
    }
}

/**
 * Camel component to download files from FTP server and unmarshal to expected class
 * Ref: https://access.redhat.com/documentation/en-us/red_hat_jboss_fuse/6.2/html/apache_camel_component_reference/idu-ftp2
 */
@Component
class CamelSftpDownload(
        private val properties: CamelFtpProperties,
        private val applicationMapProperties: ApplicationMapProperties,
) : RouteBuilder() {
    private val ftpBuilder = URIBuilder()
            .setScheme(properties.scheme)
            .setHost(properties.host)
            .setPort(properties.port)
            .setPath(properties.path)
    private lateinit var fromFtpUrl: URI

    init {
        ftpBuilder
                .addParameter("username", properties.userName)
                .addParameter("password", properties.password)
                .addParameter("passiveMode", properties.passiveMode)
                .addParameter("initialDelay", properties.initialDelay)
                .addParameter("delay", properties.delay)
                .addParameter("sortBy", "file:modified") // Oldest file first
                .addParameter("noop", "false")
//                .addParameter("implicit",properties.implicitSecurity)
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
                .log(simple("Processing file.... \${in.headers.CamelFileName}").text)
                .onException(Exception::class.java)
                .bean(SFTPService::class.java, "errorProcessingRequest")
                .end()
                .setHeader(documentTypeHeader, constant("IN"))
                .bean(SFTPService::class.java, "addDownloadProcessing")
//                .choice()
//                .log(simple("Processing file response message.... \${in.headers.CamelFileName}").text)
//                .`when`().method(SFTPService::class.java, "checkIsResponse") // Check if it is a response file, and process response accordingly
//                .bean(SFTPService::class.java, "fileReceivedResponse")
//                .otherwise()
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
                .`when`(header("CamelFileName").startsWith(applicationMapProperties.mapKeswsErrorDocument))
                .unmarshal(createXmlMapper(KeswsErrorResponse::class.java))
                .bean(SFTPService::class.java, "processErrorDocument")
                .otherwise()
                .removeHeader(documentTypeHeader)
                .setHeader(documentTypeHeader, constant(unprocesable))
                .setHeader("error", simple("Invalid file received: \${in.headers.CamelFileName}"))
                .log("Invalid file \${file:name} complete.")
                .setHeader("moveFailed", simple("\${in.headers.CamelFileName}"))
                .throwException(java.lang.Exception(header("error").toString()))
                .end()
                .bean(SFTPService::class.java, "successfulProcessingRequest") // Save Download status
//                .end()
                .end()
//        // Link to save file to database
//        from(wireTapResult)
//                .log("Saving document \${in.headers.CamelFileName} details")
//                .bean(SFTPService::class.java, "addDownloadProcessing")
//                .log("File details saved to database");
    }
}

/**
 * Endpoint to upload files to SFTP
 *
 */
@Component
class CamelSftpUpload(
        private val properties: CamelFtpProperties,
) : RouteBuilder() {
    private val ftpBuilder = URIBuilder()
            .setScheme(properties.scheme)
            .setHost(properties.host)
            .setPort(properties.port)
            .setPath(properties.uploadDirectory)
            .addParameter("username", properties.userName)
            .addParameter("password", properties.password)
    private val fileBuilder = URIBuilder()
            .setScheme("file")
            .setPath(Paths.get("", properties.outboundDirectory)
                    .toAbsolutePath()
                    .toString())
            .setParameter("runLoggingLevel", properties.logLevel)
            .setParameter("delete", "false")
            .setParameter("move", Paths.get("", properties.uploadPreMove)
                    .toAbsolutePath()
                    .toString())
            .setParameter("moveFailed", "error")
            .setParameter("antInclude", properties.antInclude)
            .setParameter("autoCreate", "true")
    private lateinit var uploadURI: URI
    val documentTypeHeader = "documentDirection"

    init {
        ftpBuilder.addParameter("autoCreate", "false")
        uploadURI = ftpBuilder.build()
    }

    override fun configure() {
        // Upload file manualy
        from(directUploadEndpoint)
                .setHeader(documentTypeHeader, constant("OUT"))
                .log("Manual: Uploading file to FTP server: \${in.headers.CamelFileName}")
                .to(uploadURI.toString())
                .bean(SFTPService::class.java, "successfulProcessingRequest") // Save Download status
                .log("Manual: Uploaded file to FTP server: \${in.headers.CamelFileName}")
        // Upload file to FTP from folder
        from(fileBuilder.build().toString())
                .setHeader(documentTypeHeader, constant("OUT"))
                .bean(SFTPService::class.java, "addDownloadProcessing")
                .log("Uploading file to FTP server: \${in.headers.CamelFileName}")
                .to(uploadURI.toString())
                .log("Uploaded file to FTP server: \${in.headers.CamelFileName}")
    }
}
