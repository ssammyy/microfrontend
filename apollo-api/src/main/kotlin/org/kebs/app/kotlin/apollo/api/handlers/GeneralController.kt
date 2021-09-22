package org.kebs.app.kotlin.apollo.api.handlers

import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.apache.http.HttpStatus
import org.kebs.app.kotlin.apollo.api.payload.ApiResponseModel
import org.kebs.app.kotlin.apollo.api.payload.ResponseCodes
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DestinationInspectionDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.ReportsDaoService
import org.kebs.app.kotlin.apollo.api.service.ChecklistService
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.di.DiUploadsEntity
import org.springframework.core.io.ResourceLoader
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import java.io.ByteArrayOutputStream
import javax.servlet.http.HttpServletResponse
import kotlin.random.Random

@RestController
@RequestMapping("/api/v1/download")
class GeneralController(
        private val applicationMapProperties: ApplicationMapProperties,
        private val reportsDaoService: ReportsDaoService,
        private val daoServices: DestinationInspectionDaoServices,
        private val checklistService: ChecklistService,
        private val resourceLoader: ResourceLoader
) {

    @GetMapping("/all/checklist/{downloadId}")
    fun downloadAllChecklist(@PathVariable("downloadId") downloadId: String, httResponse: HttpServletResponse) {
        var imageFile = ""
        try {
//            val resource = resourceLoader.getResource("classpath:static/images/KEBS_SMARK.png")
            val resource = resourceLoader.getResource("file:\${CONFIG_PATH}/reports/images/KEBS_SMARK.png")
            imageFile = resource.file.toString()
        } catch (e: Exception) {
            KotlinLogging.logger { }.info("Incorrect path ${e.message}")
        }
        try{
        val map = hashMapOf<String, Any>()
//        map["ITEM_ID"] = id
            val fileName = "ALL_CHECKLIST.pdf"
        val sampleCollect = listOf<Any>()
        map["imagePath"] = imageFile
        map.putAll(this.checklistService.getAllChecklistDetails(downloadId))
        val stream = reportsDaoService.extractReportEmptyDataSource(map, "classpath:reports/allChecklist.jrxml")
        download(stream, fileName, httResponse)
    } catch (ex: Exception) {
        KotlinLogging.logger { }.error("FAILED TO READ DATA", ex)
        httResponse.status = HttpStatus.SC_INTERNAL_SERVER_ERROR
        httResponse.writer.println("Request failed")
    }
    }
    @GetMapping("/checklist/{docType}/{downloadId}")
    fun downloadChecklist(@PathVariable("docType") docType: String, @PathVariable("downloadId") downloadId: Long, httResponse: HttpServletResponse) {
        var imageFile = ""
        try {
//            val resource = resourceLoader.getResource("classpath:static/images/KEBS_SMARK.png")
            val resource = resourceLoader.getResource("file:\${CONFIG_PATH}/reports/images/KEBS_SMARK.png")
            imageFile = resource.file.toString()
        } catch (e: Exception) {
            KotlinLogging.logger { }.info("Incorrect path ${e.message}")
        }
        try {
            val map = hashMapOf<String, Any>()
//        map["ITEM_ID"] = id
            val sampleCollect = listOf<Any>()
            map["imagePath"] = imageFile
            val fileName = "${docType.toUpperCase()}_$downloadId.pdf"
            when {
                docType.equals("sampleCollectionForm") -> {
                    map.putAll(this.checklistService.getScfDetails(downloadId))
                }
                docType.equals("sampleSubmissionForm") -> {
                    map.putAll(this.checklistService.getSsfDetails(downloadId))
                }
                docType.equals("agrochemChecklist") -> {
                    map.putAll(this.checklistService.getAgrochemChecklistDetails(downloadId))
                }
                docType.equals("allAgrochemChecklist") -> {
                    map.putAll(this.checklistService.getAgrochemChecklistDetails(downloadId))
                }
                docType.equals("allEngineringChecklist") -> {
                    map.putAll(this.checklistService.getEngineeringChecklistDetails(downloadId))
                }
                docType.equals("allOtherChecklist") -> {
                    map.putAll(this.checklistService.getOtherChecklistDetails(downloadId))
                }
                docType.equals("allVehicleChecklist") -> {
                    map.putAll(this.checklistService.getOtherChecklistDetails(downloadId))
                }
                docType.equals("allChecklist") -> {

                }
                else -> {
                    httResponse.status = 500
                    httResponse.writer.println("Invalid document type")
                    return
                }
            }
            val designPath="classpath:reports/$docType.jrxml"
            KotlinLogging.logger { }.info("Print report: $designPath: $map")
            val stream = reportsDaoService.extractReportEmptyDataSource(map, designPath)
            download(stream, fileName, httResponse)
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("FAILED TO READ DATA", ex)
            httResponse.status = HttpStatus.SC_INTERNAL_SERVER_ERROR
            httResponse.writer.println("Request failed")
        }

    }

    @GetMapping("/cor/{corId}")
    fun downloadCertificateOfRoadWorthines(@PathVariable("corId") corId: Long, httResponse: HttpServletResponse) {
        daoServices.findCorById(corId)?.let { cor ->
            cor.localCorFile?.let { file ->
                //Create FileDTO Object
                httResponse.setHeader(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"LOCAL_COR_${cor.corNumber}.pdf\";")
                httResponse.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
                httResponse.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
                httResponse.setContentLength(file.size)
                httResponse.outputStream
                        .let { responseOutputStream ->
                            responseOutputStream.write(file)
                            responseOutputStream.close()
                        }
                return
            }
        }
        httResponse.status = 500
        httResponse.writer.println("Invalid COR identifier or COI")
    }

    @GetMapping("/coc/{cocId}")
    fun downloadCertificateOfConformance(@PathVariable("cocId") cocId: Long, httResponse: HttpServletResponse) {
        daoServices.findCOCById(cocId)?.let { coc ->
            coc.localCocFile?.let { file ->
                //Create FileDTO Object

                httResponse.setHeader(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"LOCAL_COC_${coc.cocNumber}.pdf\";")
                httResponse.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
                httResponse.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
                httResponse.setContentLength(file.size)
                httResponse.outputStream
                        .let { responseOutputStream ->
                            responseOutputStream.write(file)
                            responseOutputStream.close()
                        }
                return
            }
        }
        httResponse.status = 500
        httResponse.writer.println("Invalid COC identifier")
    }


    @GetMapping("/ministry/checklist/{checklistId}")
    fun downloadChecklist(@PathVariable("checklistId") inspectionId: Long, httResponse: HttpServletResponse) {
        try {
            this.checklistService.findInspectionMotorVehicleById(inspectionId)?.let { inspectionGeneral ->
                inspectionGeneral.ministryReportFile?.let {
                    val name = "MINISTRY_CHECKLIST-${inspectionId}.pdf"
                    downloadBytes(it, name, httResponse)
                } ?: throw ExpectedDataNotFound("Ministry file has not been uploaded")
            } ?: throw ExpectedDataNotFound("MVIR not found")
            return
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("CHECKLIST DOWNLOAD FAILED", ex)
        }
        httResponse.status = HttpStatus.SC_INTERNAL_SERVER_ERROR
        httResponse.writer.println("INVALID checklist")
    }

    /*
   Get Ministry inspection report
    */
    @RequestMapping(value = ["/motor/inspection/report/{mvInspectionChecklistId}"], method = [RequestMethod.GET])
    fun motorInspectionReport(httpResponse: HttpServletResponse, @PathVariable(value = "mvInspectionChecklistId") mvInspectionChecklistid: Long) {
        try {
            val data = this.checklistService.motorVehicleInspectionReportDetails(mvInspectionChecklistid)
            val pdfStream = reportsDaoService.extractReportEmptyDataSource(data, applicationMapProperties.mapReportMotorVehicleChecklistPath)
            val serialNumber = data.getOrDefault("SerialNo", Random(mvInspectionChecklistid).nextLong())
            val fileName = "INSPECTION_REPORT_${serialNumber}.pdf"
            download(pdfStream, fileName, httpResponse)
        } catch (ex: Exception) {
            val response = ApiResponseModel()
            KotlinLogging.logger { }.error("Download failed", ex)
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Could not download checklist"
            // Error
            httpResponse.status = HttpStatus.SC_INTERNAL_SERVER_ERROR
            httpResponse.writer.write(ObjectMapper().writeValueAsString(response))
        }
    }

    @GetMapping("/ministry/checklist/unfilled/{checklistId}")
    fun downloadMinistryChecklist(@PathVariable("checklistId") checklistId: Long, httResponse: HttpServletResponse) {
        val map = hashMapOf<String, Any>()
        val response = ApiResponseModel()
        try {
            checklistService.findInspectionMotorVehicleById(checklistId)?.let { mvInspectionChecklist ->
                map["VehicleMake"] = mvInspectionChecklist.makeVehicle.toString()
                map["EngineCapacity"] = mvInspectionChecklist.engineNoCapacity.toString()
                map["ManufactureDate"] = mvInspectionChecklist.manufactureDate.toString()
                map["OdometerReading"] = mvInspectionChecklist.odemetreReading.toString()
                map["RegistrationDate"] = mvInspectionChecklist.registrationDate.toString()
                map["ChassisNo"] = mvInspectionChecklist.chassisNo.toString()

            } ?: throw ExpectedDataNotFound("Motor Vehicle Inspection Checklist does not exist")

            val stream = reportsDaoService.extractReportEmptyDataSource(map, applicationMapProperties.mapReportMinistryChecklistPath)
            download(stream, "MINISTRY-CHECKLIST-${checklistId}.pdf", httResponse)
            return
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("MINISTRY CHECKLIST", ex)
            response.message = ex.localizedMessage
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
        }
        httResponse.status = HttpStatus.SC_INTERNAL_SERVER_ERROR
        httResponse.writer.println(response.message)
    }

    fun download(stream: ByteArrayOutputStream, fileName: String, httResponse: HttpServletResponse): Boolean {
        httResponse.setHeader("Content-Disposition", "inline; filename=\"${fileName}\";")
        httResponse.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
        httResponse.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
        httResponse.setContentLengthLong(stream.size().toLong())
        httResponse.outputStream
                .let { responseOutputStream ->
                    responseOutputStream.write(stream.toByteArray())
                    responseOutputStream.close()
                }
        return true
    }

    fun downloadBytes(bytes: ByteArray, fileName: String, httResponse: HttpServletResponse): Boolean {
        httResponse.setHeader("Content-Disposition", "inline; filename=\"${fileName}\";")
        httResponse.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
        httResponse.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
        httResponse.setContentLength(bytes.size)
        httResponse.outputStream
                .let { responseOutputStream ->
                    responseOutputStream.write(bytes)
                    responseOutputStream.close()
                }
        return true
    }


    @GetMapping("/attachments/{uploadId}")
    fun downloadConsignmentDocumentAttachment(@PathVariable("uploadId") uploadId: Long, httResponse: HttpServletResponse) {
        val diUpload: DiUploadsEntity = daoServices.findDiUploadById(uploadId)
        diUpload.document?.let {
            // Response with file
            downloadBytes(it, "ATTACHMENT-${diUpload.id}-${diUpload.name}", httResponse)
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

            map = reportsDaoService.addBankAndMPESADetails(map, "")

            val extractReport = reportsDaoService.extractReport(
                    map,
                    applicationMapProperties.mapReportDemandNoteWithItemsPath,
                    demandNoteItemList
            )
            // Response with file
            download(extractReport, "NOTE-${demandNoteId}-${demandNote.demandNoteNumber}.pdf", httResponse)
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Download failed", ex)
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Failed to generate demand note"
            httResponse.status = HttpStatus.SC_INTERNAL_SERVER_ERROR
            httResponse.writer.write(ObjectMapper().writeValueAsString(response))
        }

    }
}