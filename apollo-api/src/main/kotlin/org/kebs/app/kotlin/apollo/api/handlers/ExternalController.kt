package org.kebs.app.kotlin.apollo.api.handlers

import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.apache.http.HttpStatus
import org.kebs.app.kotlin.apollo.api.payload.ApiResponseModel
import org.kebs.app.kotlin.apollo.api.payload.ResponseCodes
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DestinationInspectionDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.ReportsDaoService
import org.kebs.app.kotlin.apollo.api.service.InvoicePaymentService
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletResponse

/**
 * Used to expose endpoints that do not need authentication
 *
 * @author jmungai
 */
@RestController
@RequestMapping("/api/v1/public/download")
class ExternalController(
    private val applicationMapProperties: ApplicationMapProperties,
    private val reportsDaoService: ReportsDaoService,
    private val daoServices: DestinationInspectionDaoServices,
    private val invoicePaymentService: InvoicePaymentService,
    private val commonDaoServices: CommonDaoServices,
    private val objectMapper: ObjectMapper,
    private val utils: UtilitiesHandler
) {
    val kebsLogoPath = commonDaoServices.resolveAbsoluteFilePath(applicationMapProperties.mapKebsLogoPath)

    @CrossOrigin
    @GetMapping("/demand/note/receipt/{demandNoteRef}")
    fun downloadDemandNotePaymentReceipt(
        @PathVariable("demandNoteRef") demandNoteId: String,
        @RequestParam("ucr") ucrNumber: String,
        httResponse: HttpServletResponse
    ) {
        val response = ApiResponseModel()
        response.responseCode = ResponseCodes.INVALID_CODE
        response.message = "Not implemented!"
        httResponse.status = HttpStatus.SC_BAD_REQUEST
        httResponse.writer.write(objectMapper.writeValueAsString(response))
    }

    @CrossOrigin
    @GetMapping("/demand/note/{demandNoteRef}")
    fun downloadDemandNote(
        @PathVariable("demandNoteRef") demandNoteId: String,
        @RequestParam("ucr") ucrNumber: String,
        httResponse: HttpServletResponse
    ) {
        val response = ApiResponseModel()
        try {
            val demandNoteDetails = invoicePaymentService.invoiceDetails(demandNoteId, ucrNumber)
            val demandNoteItemList = daoServices.findDemandNoteItemDetails(demandNoteDetails.first)
            val map = demandNoteDetails.second
            map["imagePath"] = kebsLogoPath ?: ""
            val extractReport = reportsDaoService.extractReport(
                map,
                "classpath:reports/KebsDemandNoteItems.jrxml",
                demandNoteItemList
            )
            val demandNoteNumber = map["demandNoteNo"] as String
            // Response with file
            utils.download(extractReport, "EXT-DEMAND-NOTE-${demandNoteId}-${demandNoteNumber}.pdf", httResponse)
        } catch (ex: ExpectedDataNotFound) {
            response.responseCode = ResponseCodes.INVALID_CODE
            response.message = ex.localizedMessage
            httResponse.status = HttpStatus.SC_BAD_REQUEST
            httResponse.writer.write(objectMapper.writeValueAsString(response))
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("External demand noted download failed", ex)
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Failed to generate demand note"
            httResponse.status = HttpStatus.SC_INTERNAL_SERVER_ERROR
            httResponse.writer.write(objectMapper.writeValueAsString(response))
        }
    }
}