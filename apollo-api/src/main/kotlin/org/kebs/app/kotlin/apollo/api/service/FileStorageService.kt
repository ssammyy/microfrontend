package org.kebs.app.kotlin.apollo.api.service

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.payload.ApiResponseModel
import org.kebs.app.kotlin.apollo.api.payload.ResponseCodes
import org.kebs.app.kotlin.apollo.api.ports.provided.sftp.SFTPService
import org.kebs.app.kotlin.apollo.store.model.SdlFactoryVisitReportsUploadEntity
import org.kebs.app.kotlin.apollo.store.model.SftpTransmissionEntity
import org.kebs.app.kotlin.apollo.store.repo.ISdlFactoryVisitReportsUploadEntityRepository
import org.kebs.app.kotlin.apollo.store.repo.ISftpTransmissionEntityRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.sql.Timestamp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@Service
class FileStorageService(
        private val sftpRepository: ISftpTransmissionEntityRepository,
        private val ftpService: SFTPService,
        private var iSdlFactoryVisitReportsUploadEntityRepository: ISdlFactoryVisitReportsUploadEntityRepository
) {
    val dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    fun todaysFileStats(date: String?): ApiResponseModel {
        var referenceDate = date
        if (referenceDate.isNullOrEmpty()) {
            referenceDate = dateFormat.format(LocalDate.now())
        }
        KotlinLogging.logger { }.debug("Date: $referenceDate")
        val response = ApiResponseModel()
        try {
            response.data = sftpRepository.findStatisticsForDate(referenceDate!!)
            response.responseCode = ResponseCodes.SUCCESS_CODE
            response.message = "Success"
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to load data", ex)
            response.responseCode = ResponseCodes.FAILED_CODE
            response.message = "Failed, could not retrieve data"
        }

        return response
    }

    fun loadFilesById(messageId: Long): ApiResponseModel {
        val response = ApiResponseModel()
        this.sftpRepository.findByIdOrNull(messageId)?.let {
            try {
                response.data = this.ftpService.getUploadedDownloadedFile(it.fileType, it.flowDirection, it.transactionStatus == 1)
                response.responseCode = ResponseCodes.SUCCESS_CODE
                response.message = "Succces"
            } catch (ex: Exception) {
                response.responseCode = ex.localizedMessage
                response.message = "Succces"
            }
        } ?: run {
            response.message = "Not found"
            response.responseCode = ResponseCodes.NOT_FOUND
            response
        }
        return response
    }

    fun loadFilesByStatus(statusOpt: Optional<String>, fileName: Optional<String>, date: String?, flowDirection: String?, page: PageRequest): ApiResponseModel {
        val response = ApiResponseModel()
        var referenceDate = date
        if (referenceDate.isNullOrEmpty()) {
            referenceDate = dateFormat.format(LocalDate.now())
        }
        val data: Page<SftpTransmissionEntity>
        // Date
        data = when {
            fileName.isPresent->{
                this.sftpRepository.findFirstByFilenameContainingOrderByCreatedOnDesc(fileName.get(), page)
            }
            statusOpt.isPresent -> {
                val status = statusOpt.get().toInt()
                if (status > 0) {
                    this.sftpRepository.findByTransactionStatusInAndFlowDirectionOrderByCreatedOnDesc(listOf(status), flowDirection, page)
                } else {
                    this.sftpRepository.findByTransactionStatusNotInAndFlowDirectionOrderByCreatedOnDesc(listOf(1), flowDirection, page)
                }
            }
            else -> {
                this.sftpRepository.findByTransactionStatusNotInAndFlowDirectionOrderByCreatedOnDesc(listOf(1, 0), flowDirection, page)
            }
        }
        response.data = data.toList()
        response.pageNo = data.number

        response.totalCount = data.totalElements
        response.totalPages = data.totalPages
        response.message = "Success"
        response.responseCode = ResponseCodes.SUCCESS_CODE
        return response
    }

    @Throws(IOException::class)
    fun store(file: MultipartFile, reportId: Long, createdBy: String, createdOn: Timestamp) {
        val fileName = file.originalFilename?.let { StringUtils.cleanPath(it) }
        val sdlFactoryVisitReportsUploadEntity = SdlFactoryVisitReportsUploadEntity()
        sdlFactoryVisitReportsUploadEntity.data = file.bytes
        sdlFactoryVisitReportsUploadEntity.name = fileName
        KotlinLogging.logger { }.info { "Uploaded file name is " + fileName }
        sdlFactoryVisitReportsUploadEntity.type = file.contentType
        sdlFactoryVisitReportsUploadEntity.reportId = reportId
        sdlFactoryVisitReportsUploadEntity.createdBy = createdBy
        sdlFactoryVisitReportsUploadEntity.createdOn = createdOn
//        val FileDB = SdlFactoryVisitReportsUploadEntity(name = fileName, file.contentType, file.bytes)
        iSdlFactoryVisitReportsUploadEntityRepository.save(sdlFactoryVisitReportsUploadEntity)
        KotlinLogging.logger { }.info { "Saved uploaded file OK" }
    }

    fun resendFile(messageId: Long): ApiResponseModel {
        val response = ApiResponseModel()
        this.sftpRepository.findByIdOrNull(messageId)?.let {
            try {
                if ("OUT".equals(it.flowDirection, false) && !it.filename.isNullOrEmpty()) {
                    if (this.ftpService.resubmitFile(it)) {
                        response.data = true
                        response.responseCode = ResponseCodes.SUCCESS_CODE
                        response.message = "File resubmitted successfully"
                    } else {
                        response.responseCode = ResponseCodes.FAILED_CODE
                        response.message = "File resubmitted failed"
                    }
                } else {
                    response.responseCode = ResponseCodes.NOT_IMPLEMENTED
                    response.message = "Incoming files cannot be resubmitted"
                }
            } catch (ex: Exception) {
                response.responseCode = ResponseCodes.FAILED_CODE
                response.message = "Failed to resubmit file: ${ex.message}"
            }
        } ?: run {
            response.message = "File Not found"
            response.responseCode = ResponseCodes.NOT_FOUND
            response
        }
        return response
    }
}