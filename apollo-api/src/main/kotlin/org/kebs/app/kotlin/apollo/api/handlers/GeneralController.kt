package org.kebs.app.kotlin.apollo.api.handlers

import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.apache.http.HttpStatus
import org.kebs.app.kotlin.apollo.api.handlers.reports.ReportRequest
import org.kebs.app.kotlin.apollo.api.payload.ApiResponseModel
import org.kebs.app.kotlin.apollo.api.payload.ResponseCodes
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DestinationInspectionDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.ReportsDaoService
import org.kebs.app.kotlin.apollo.api.ports.provided.lims.LimsServices
import org.kebs.app.kotlin.apollo.api.service.*
import org.kebs.app.kotlin.apollo.api.service.reports.DIReports
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.di.DiUploadsEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import java.io.ByteArrayOutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.servlet.http.HttpServletResponse
import kotlin.random.Random

@RestController
@RequestMapping("/api/v1/download")
class GeneralController(
    private val applicationMapProperties: ApplicationMapProperties,
    private val reportsDaoService: ReportsDaoService,
    private val diServices: DestinationInspectionService,
    private val daoServices: DestinationInspectionDaoServices,
    private val checklistService: ChecklistService,
    private val invoicePaymentService: InvoicePaymentService,
    private val commonDaoServices: CommonDaoServices,
    private val limsServices: LimsServices,
    private val objectMapper: ObjectMapper,
    private val auctionService: AuctionService,
    private val pvocService: PvocService,
    private val diReports: DIReports,
    private val utils: UtilitiesHandler
) {
    val monthFormatter = DateTimeFormatter.ofPattern("MMMM")
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MMMM/yyyy")
    val checkMark = commonDaoServices.resolveAbsoluteFilePath(applicationMapProperties.mapCheckmarkImagePath)
    val smarkImage = commonDaoServices.resolveAbsoluteFilePath(applicationMapProperties.mapSmarkImagePath)
    val kebsLogoPath = commonDaoServices.resolveAbsoluteFilePath(applicationMapProperties.mapKebsLogoPath)

    @GetMapping("/lims/lab-result/pdf")
    fun downloadLimsReport(
        @RequestParam("bsNumber") bsNumber: String,
        @RequestParam("fileName") fileName: String,
        httResponse: HttpServletResponse
    ) {
        try {
            val file = limsServices.mainFunctionLimsGetPDF(bsNumber, fileName)
            httResponse.contentType = commonDaoServices.getFileTypeByMimetypesFileTypeMap(file.name)
            httResponse.setHeader(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"${file.name}\";")
            httResponse.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
            httResponse.setContentLengthLong(file.length())
            httResponse.outputStream
                .let { responseOutputStream ->
                    responseOutputStream.write(file.readBytes())
                    responseOutputStream.close()
                    file.deleteOnExit()
                }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("FAILED to download", ex)
            httResponse.status = HttpStatus.SC_INTERNAL_SERVER_ERROR
            httResponse.writer.println("PDF Download failed")
        }
    }

    @GetMapping("/all/checklist/{downloadId}")
    fun downloadAllChecklist(@PathVariable("downloadId") downloadId: String, httResponse: HttpServletResponse) {
        try {
            val map = hashMapOf<String, Any>()
            val fileName = "ALL_CHECKLIST.pdf"
            map["imagePath"] = kebsLogoPath ?: ""
            map["CheckMark"] = checkMark ?: ""
            map.putAll(this.checklistService.getAllChecklistDetails(downloadId))
            val dataSources = map["dataSources"] as HashMap<String, List<Any>>

            val stream =
                reportsDaoService.extractReportMapDataSource(map, "classpath:reports/allChecklist.jrxml", dataSources)
            utils.download(stream, fileName, httResponse)
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("FAILED TO READ DATA", ex)
            httResponse.status = HttpStatus.SC_INTERNAL_SERVER_ERROR
            httResponse.writer.println("Request failed")
        }
    }

    @GetMapping("/checklist/{docType}/{downloadId}")
    fun downloadChecklist(
        @PathVariable("docType") docType: String,
        @PathVariable("downloadId") downloadId: Long,
        httResponse: HttpServletResponse
    ) {
        try {
            val map = hashMapOf<String, Any>()
//        map["ITEM_ID"] = id
            var sampleCollect: List<Any>? = null
            map["imagePath"] = kebsLogoPath ?: ""
            map["CheckMark"] = checkMark ?: ""
            val fileName = "${docType.toUpperCase()}_$downloadId.pdf"
            when {
                docType.equals("sampleCollectionForm") -> {
                    map.putAll(this.checklistService.getScfDetails(downloadId))
                }
                docType.equals("sampleSubmissionFormV2") -> {
                    map.putAll(this.checklistService.getSsfDetails(downloadId))
                }
                docType.equals("agrochemChecklist") -> {
                    map.putAll(this.checklistService.getAgrochemChecklistDetails(downloadId))
                }
                docType.equals("allAgrochemChecklist") -> {
                    map.putAll(this.checklistService.getAgrochemChecklistDetails(downloadId))
                    sampleCollect = map["items"] as List<Any>
                }
                docType.equals("allEngineringChecklist") -> {
                    map.putAll(this.checklistService.getEngineeringChecklistDetails(downloadId))
                    sampleCollect = map["items"] as List<Any>
                }
                docType.equals("allOtherChecklist") -> {
                    map.putAll(this.checklistService.getOtherChecklistDetails(downloadId))
                    sampleCollect = map["items"] as List<Any>
                }
                docType.equals("allVehicleChecklist") -> {
                    map.putAll(this.checklistService.getVehicleChecklistDetails(downloadId))
                    sampleCollect = map["items"] as List<Any>
                }
                docType.equals("allChecklist") -> {

                }
                else -> {
                    httResponse.status = 500
                    httResponse.writer.println("Invalid document type")
                    return
                }
            }
            val designPath = "classpath:reports/$docType.jrxml"
            KotlinLogging.logger { }.info("Print report: $designPath: $map")
            val stream = sampleCollect?.let {
                val items = hashMapOf<String, List<Any>>()
                items["itemDatasource"] = sampleCollect
                reportsDaoService.extractReportMapDataSource(map, designPath, items)
            } ?: run {
                reportsDaoService.extractReportEmptyDataSource(map, designPath)
            }
            utils.download(stream, fileName, httResponse)
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("FAILED TO READ DATA", ex)
            httResponse.status = HttpStatus.SC_INTERNAL_SERVER_ERROR
            httResponse.writer.println("Request failed")
        }

    }

    @GetMapping("/cor/{corId}")
    fun downloadCertificateOfRoadWorthines(@PathVariable("corId") corId: Long, httResponse: HttpServletResponse) {
        try {
            val data = diServices.createLocalCorReportMap(corId)
            data["imagePath"] = kebsLogoPath ?: ""
            val fileName = "LOCAL-COR-".plus(data["corNumber"] as String).plus(".pdf")
            KotlinLogging.logger { }.info("COR DATA: $data")
            val pdfStream =
                reportsDaoService.extractReportEmptyDataSource(data, "classpath:reports/LocalCoRReport.jrxml")
            utils.download(pdfStream, fileName, httResponse)
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("FAILED TO DOWNLOAD COC", ex)
        }
        httResponse.status = 500
        httResponse.writer.println("Invalid COR identifier or COI")
    }

    @GetMapping("/coc-coi/{cocCoiId}")
    fun downloadCertificateOfConformance(@PathVariable("cocCoiId") cocCoiId: Long, httResponse: HttpServletResponse) {
        try {
            val data = diServices.createLocalCocReportMap(cocCoiId)
            data["imagePath"] = kebsLogoPath

            val items = data["dataSources"] as HashMap<String, List<Any>>

            KotlinLogging.logger { }.info("COC DATA: $data")
            val pdfStream: ByteArrayOutputStream
            val cocType = data["CoCType"] as String
            val fileName = "LOCAL-${cocType.toUpperCase()}-".plus(data["CocNo"] as String).plus(".pdf")
            when (cocType) {
                "COI" -> {
                    pdfStream = reportsDaoService.extractReportMapDataSource(
                        data,
                        "classpath:reports/LocalCoiReport.jrxml",
                        items
                    )
                }
                "NCR" -> {
                    pdfStream =
                        reportsDaoService.extractReportMapDataSource(data, "classpath:reports/NcrReport.jrxml", items)
                }
                else -> {
                    pdfStream = reportsDaoService.extractReportMapDataSource(
                        data,
                        "classpath:reports/LocalCoCReport.jrxml",
                        items
                    )
                }
            }
            utils.download(pdfStream, fileName, httResponse)
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("FAILED TO DOWNLOAD COC/COI/NCR", ex)
            httResponse.status = 500
            httResponse.writer.println("Invalid COC/COI/NCR identifier")
        }

    }


    @GetMapping("/ministry/checklist/{checklistId}")
    fun downloadChecklist(@PathVariable("checklistId") inspectionId: Long, httResponse: HttpServletResponse) {
        try {
            this.checklistService.findInspectionMotorVehicleById(inspectionId)?.let { inspectionGeneral ->
                inspectionGeneral.ministryReportFile?.let {
                    val name = "MINISTRY_CHECKLIST-${inspectionId}.pdf"
                    utils.downloadBytes(it, name, httResponse)
                } ?: throw ExpectedDataNotFound("Ministry file has not been uploaded")
            } ?: throw ExpectedDataNotFound("MVIR not found")
            return
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("CHECKLIST DOWNLOAD FAILED", ex)
        }
        httResponse.status = HttpStatus.SC_INTERNAL_SERVER_ERROR
        httResponse.writer.println("INVALID checklist")
    }

    @GetMapping("/ministry/mvir/{checklistId}")
    fun downloadMVIR(@PathVariable("checklistId") inspectionId: Long, httResponse: HttpServletResponse) {
        try {
            val data = checklistService.mvirRportData(inspectionId)
            data["imagePath"] = kebsLogoPath ?: ""
//            data["bgImagePath"] = ""
            val pdfStream = reportsDaoService.extractReportEmptyDataSource(
                data,
                "classpath:reports/motorVehicleInspectionReport.jrxml"
            )
            val serialNumber = data["mvirNum"] as String
            val fileName = "MVIR-${serialNumber}.pdf"
            utils.download(pdfStream, fileName, httResponse)

        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("MVIR DOWNLOAD FAILED", ex)
            httResponse.status = HttpStatus.SC_INTERNAL_SERVER_ERROR
            httResponse.writer.println("INVALID checklist")
        }
    }

    /*
   Get Ministry inspection report
    */
    @RequestMapping(value = ["/motor/inspection/report/{mvInspectionChecklistId}"], method = [RequestMethod.GET])
    fun motorInspectionReport(
        httpResponse: HttpServletResponse,
        @PathVariable(value = "mvInspectionChecklistId") mvInspectionChecklistid: Long
    ) {
        try {
            val data = this.checklistService.motorVehicleInspectionReportDetails(mvInspectionChecklistid)
            data["imagePath"] = kebsLogoPath ?: ""
            val pdfStream = reportsDaoService.extractReportEmptyDataSource(
                data,
                applicationMapProperties.mapReportMotorVehicleChecklistPath
            )
            val serialNumber = data.getOrDefault("SerialNo", Random(mvInspectionChecklistid).nextLong())
            val fileName = "INSPECTION_REPORT_${serialNumber}.pdf"
            utils.download(pdfStream, fileName, httpResponse)
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
                map["imagePath"] = kebsLogoPath ?: ""
                map["EngineCapacity"] = mvInspectionChecklist.engineNoCapacity.toString()
                map["ManufactureDate"] = mvInspectionChecklist.manufactureDate.toString()
                map["OdometerReading"] = mvInspectionChecklist.odemetreReading.toString()
                map["RegistrationDate"] = mvInspectionChecklist.registrationDate.toString()
                map["ChassisNo"] = mvInspectionChecklist.chassisNo.toString()

            } ?: throw ExpectedDataNotFound("Motor Vehicle Inspection Checklist does not exist")

            val stream = reportsDaoService.extractReportEmptyDataSource(
                map,
                applicationMapProperties.mapReportMinistryChecklistPath
            )
            utils.download(stream, "MINISTRY-CHECKLIST-${checklistId}.pdf", httResponse)
            return
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("MINISTRY CHECKLIST", ex)
            response.message = ex.localizedMessage
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
        }
        httResponse.status = HttpStatus.SC_INTERNAL_SERVER_ERROR
        httResponse.writer.println(response.message)
    }

    @GetMapping("/waiver/attachment/{uploadId}")
    fun downloadWaiverAttachment(@PathVariable("uploadId") uploadId: String, httResponse: HttpServletResponse) {
        pvocService.findPvoceDocument(uploadId)?.let { diUpload ->
            // Response with file
            diUpload.documentType?.let { doc ->
                utils.downloadBytes(doc, "ATTACHMENT-${diUpload.id}-${diUpload.name?.toUpperCase()}", httResponse)
            } ?: throw ExpectedDataNotFound("Attachment file not found")
        } ?: throw ExpectedDataNotFound("Attachment file not found")
    }

    @GetMapping("/attachments/{uploadId}")
    fun downloadConsignmentDocumentAttachment(
        @PathVariable("uploadId") uploadId: Long,
        httResponse: HttpServletResponse
    ) {
        val diUpload: DiUploadsEntity = daoServices.findDiUploadById(uploadId)
        diUpload.document?.let {
            // Response with file
            utils.downloadBytes(it, "ATTACHMENT-${diUpload.id}-${diUpload.name}", httResponse)
        } ?: throw ExpectedDataNotFound("Attachment file not found")
    }

    @GetMapping("/auction/attachment/{uploadId}/{auctionId}")
    fun downloadAuctionAttachment(
        @PathVariable("uploadId") recordId: Long,
        @PathVariable("auctionId") auctionId: Long,
        httResponse: HttpServletResponse
    ) {
        auctionService.downloadAuctionAttachment(auctionId, recordId)?.let {
            // Response with file
            it.document?.let { doc ->
                utils.downloadBytes(doc, "AUCTION-ATTACHMENT-${it.id}-${it.name?.toUpperCase()}", httResponse)
            } ?: throw ExpectedDataNotFound("Attachment file not found")
        } ?: throw ExpectedDataNotFound("Attachment file not found")
    }

    @GetMapping("/auction/report/{startDate}/{endDate}")
    fun downloadAuctionReport(
        @PathVariable("startDate") startDate: String,
        @PathVariable("endDate") endDate: String,
        @RequestParam("status", required = true, defaultValue = "all") status: String,
        httResponse: HttpServletResponse
    ) {
        val response = ApiResponseModel()
        try {
            val records = auctionService.downloadAuctionReport(startDate, endDate, status)
            if (records.isEmpty()) {
                response.responseCode = ResponseCodes.EXCEPTION_STATUS
                response.message = "No records found for the dates ${startDate} to ${endDate}"
                httResponse.status = HttpStatus.SC_NOT_FOUND
                httResponse.writer.write(ObjectMapper().writeValueAsString(response))
                return
            }
            // Create Report
            val data = HashMap<String, Any>()
            data["imagePath"] = kebsLogoPath ?: ""
            val startTimestamp = auctionService.getReportTimestamp(startDate, true)
            val endTimestamp = auctionService.getReportTimestamp(endDate, false)
            if (endTimestamp.year == startTimestamp.year && endTimestamp.month == startTimestamp.month) {
                data["aucMonth"] = monthFormatter.format(startTimestamp.toLocalDateTime())
            } else {
                data["aucMonth"] =
                    monthFormatter.format(startTimestamp.toLocalDateTime()) + " to " + monthFormatter.format(
                        endTimestamp.toLocalDateTime()
                    )
            }
            data["reportDate"] =
                dateFormatter.format(startTimestamp.toLocalDateTime()) + " - " + dateFormatter.format(endTimestamp.toLocalDateTime())
            // Create datasource report
            val dataSource = HashMap<String, List<Any>>()
            dataSource["itemDataSource"] = records
            // Create a pdfStream
            val pdfStream = reportsDaoService.extractReportMapDataSource(
                data,
                "classpath:reports/auctionGoodsReport.jrxml",
                dataSource
            )
            val serialNumber = "${startDate}-${endDate}"
            val fileName = "AUCTION-GOODS-${serialNumber}.pdf"
            utils.download(pdfStream, fileName, httResponse)
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Auction report", ex)
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Failed to generate auction report"
            httResponse.status = HttpStatus.SC_INTERNAL_SERVER_ERROR
            httResponse.writer.write(objectMapper.writeValueAsString(response))
        }
    }

    @CrossOrigin
    @GetMapping("/demand/note/{demandNoteId}")
    fun downloadDemandNote(@PathVariable("demandNoteId") demandNoteId: Long, httResponse: HttpServletResponse) {
        val response = ApiResponseModel()
        try {
            val demandNoteItemList = daoServices.findDemandNoteItemDetails(demandNoteId)
            val map = invoicePaymentService.invoiceDetails(demandNoteId)
            map["imagePath"] = kebsLogoPath ?: ""
            val extractReport = reportsDaoService.extractReport(
                map,
                "classpath:reports/KebsDemandNoteItems.jrxml",
                demandNoteItemList
            )
            val demandNoteNumber = map["demandNoteNo"] as String
            // Response with file
            utils.download(extractReport, "DEMAND-NOTE-${demandNoteId}-${demandNoteNumber}.pdf", httResponse)
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Download failed", ex)
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Failed to generate demand note"
            httResponse.status = HttpStatus.SC_INTERNAL_SERVER_ERROR
            httResponse.writer.write(objectMapper.writeValueAsString(response))
        }

    }

    @CrossOrigin
    @PostMapping("/reports")
    fun downloadDocumentReport(@RequestBody() form: ReportRequest, httResponse: HttpServletResponse) {
        val response = ApiResponseModel()
        try {
            val reportDetails = diReports.downloadReport(form.filters!!, form.reportName!!)
            if (reportDetails.responseCode == ResponseCodes.SUCCESS_CODE) {
                KotlinLogging.logger {}.info("Download Report with count: ${reportDetails.totalCount}")
                val date = LocalDateTime.now()
                val reportDate = commonDaoServices.convertDateToString(date, "dd-MM-yyyy-HH")
                val fileName = "${form.reportName?.toUpperCase()}-${reportDate}.xlsx"
                val data = reportDetails.data as Map<String, Any>
                val extractReport = reportsDaoService.extractXlsReport(
                    fileName,
                    form.reportName!!,
                    commonDaoServices.convertDateToString(date, "dd-MMM-yyyy HH:mm"),
                    data.get("data") as ArrayList<Map<String, Any>>,
                    data.get("fields") as Map<String, String>
                )
                // Response with file
                utils.downloadFile(extractReport, fileName, httResponse)
            } else {
                httResponse.status = HttpStatus.SC_BAD_REQUEST
                httResponse.contentType = MediaType.APPLICATION_JSON_VALUE
                httResponse.writer.write(objectMapper.writeValueAsString(reportDetails))
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Download failed", ex)
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Failed to generate demand note"
            httResponse.contentType = MediaType.APPLICATION_JSON_VALUE
            httResponse.status = HttpStatus.SC_BAD_REQUEST
            httResponse.writer.write(objectMapper.writeValueAsString(response))
        }

    }
}