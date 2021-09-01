package org.kebs.app.kotlin.apollo.api.handlers

import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.apache.http.HttpStatus
import org.kebs.app.kotlin.apollo.api.payload.ApiResponseModel
import org.kebs.app.kotlin.apollo.api.payload.ResponseCodes
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DestinationInspectionDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.ReportsDaoService
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.di.DiUploadsEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/api/v1/download")
class GeneralController(
        private val applicationMapProperties: ApplicationMapProperties,
        private val reportsDaoService: ReportsDaoService,
        private val daoServices: DestinationInspectionDaoServices,
) {

    @GetMapping("/attachments/{uploadId}")
    fun downloadConsignmentDocumentAttachment(@PathVariable("uploadId") uploadId:Long, httResponse: HttpServletResponse) {
        val diUpload: DiUploadsEntity = daoServices.findDiUploadById(uploadId)
        diUpload.document?.let {
            // Response with file
            httResponse.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"ATTACHMENT-${diUpload.id}-${diUpload.name}\"")
            httResponse.setContentLength(it.size)
            httResponse.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
            httResponse.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
            httResponse.outputStream
                    .let { responseOutputStream ->
                        responseOutputStream.write(it)
                        responseOutputStream.close()
                    }
        } ?: throw ExpectedDataNotFound("Attachment file not found")
    }

    @CrossOrigin
    @GetMapping("/demand/note/{demandNoteId}")
    fun downloadDemandNote(@PathVariable("demandNoteId") demandNoteId: Long, httResponse: HttpServletResponse) {
        val response = ApiResponseModel()
        try {
            var map = hashMapOf<String, Any>()

            val demandNote = daoServices.findDemandNoteWithID(demandNoteId)
            val demandNoteItemList = demandNote?.id?.let { daoServices.findDemandNoteItemDetails(it) }
                    ?: throw ExpectedDataNotFound("No List Of Details Available does not exist")

            map["preparedBy"] = demandNote.generatedBy.toString()
            map["datePrepared"] = demandNote.dateGenerated.toString()
            map["demandNoteNo"] = demandNote.demandNoteNumber.toString()
            map["importerName"] = demandNote.nameImporter.toString()
            map["importerAddress"] = demandNote.address.toString()
            map["importerTelephone"] = demandNote.telephone.toString()
            map["ablNo"] = demandNote.entryAblNumber.toString()
            map["totalAmount"] = demandNote.totalAmount.toString()
            map["receiptNo"] = demandNote.receiptNo.toString()

            map = reportsDaoService.addBankAndMPESADetails(map)

            val extractReport = reportsDaoService.extractReport(
                    map,
                    applicationMapProperties.mapReportDemandNoteWithItemsPath,
                    demandNoteItemList
            )
            // Response with file
            httResponse.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"NOTE-${demandNoteId}-${demandNote.demandNoteNumber}.pdf\"")
            httResponse.setContentLength(extractReport.size().toLong().toInt())
            httResponse.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
            httResponse.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
            httResponse.outputStream
                    .let { responseOutputStream ->
                        responseOutputStream.write(extractReport.toByteArray())
                        responseOutputStream.close()
                    }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Download failed", ex)
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Failed to generate demand note"
            httResponse.status = HttpStatus.SC_INTERNAL_SERVER_ERROR
            httResponse.writer.write(ObjectMapper().writeValueAsString(response))
        }

    }
}