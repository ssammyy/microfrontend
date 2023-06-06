package org.kebs.app.kotlin.apollo.api.handlers.pvoc

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.handlers.UtilitiesHandler
import org.kebs.app.kotlin.apollo.api.payload.ApiClientForm
import org.kebs.app.kotlin.apollo.api.payload.ApiResponseModel
import org.kebs.app.kotlin.apollo.api.payload.ResponseCodes
import org.kebs.app.kotlin.apollo.api.payload.extractPage
import org.kebs.app.kotlin.apollo.api.payload.request.PvocPartnersForms
import org.kebs.app.kotlin.apollo.api.service.DaoValidatorService
import org.kebs.app.kotlin.apollo.api.service.PvocPartnerService
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartHttpServletRequest
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.paramOrNull

@Component
class PvocPartnersHandler(
    private val partnerService: PvocPartnerService,
    private val validatorService: DaoValidatorService,
    private val utils: UtilitiesHandler
) {

    fun listPartnerCountries(req: ServerRequest): ServerResponse {
        return ServerResponse.ok().body(partnerService.listPartnerCountries())
    }
    fun listPartnerTypes(req: ServerRequest): ServerResponse {
        return ServerResponse.ok().body(partnerService.listPartnerCategories())
    }

    fun createPartner(req: ServerRequest): ServerResponse {
        var response = ApiResponseModel()
        try {
            val form = req.body(PvocPartnersForms::class.java)
            validatorService.validateInputWithInjectedValidator(form)?.let {
                response.errors = it
                response.message = "Please correct the errors"
                response.responseCode = ResponseCodes.INVALID_CODE
                response
            } ?: run {
                response = this.partnerService.addPartnerDetails(form)
                response
            }
        } catch (ex: ExpectedDataNotFound) {
            KotlinLogging.logger { }.error("Request processing reversed: ${ex.message}")
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = ex.localizedMessage
            response.errors = ex.toString()
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to add partner", ex)
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Failed to create partner"
            response.errors = ex.toString()
        }
        return ServerResponse.ok().body(response)
    }

    fun updatePartnerDetails(req: ServerRequest): ServerResponse {
        var response = ApiResponseModel()
        try {
            val partnerId = req.pathVariable("partnerId").toLong()
            val form = req.body(PvocPartnersForms::class.java)
            this.validatorService.validateInputWithInjectedValidator(form)?.let {
                response.responseCode = ResponseCodes.INVALID_CODE
                response.message = "Invalid request data"
                response.errors = it
                response
            } ?: run {
                response = this.partnerService.updatePartnerDetails(form, partnerId)
                response
            }
        } catch (ex: Exception) {
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Failed to update partner details"
            response.errors = ex.toString()
        }
        return ServerResponse.ok().body(response)
    }

    fun listPartnerNames(req: ServerRequest): ServerResponse{
        var response=ApiResponseModel()
        try {
            response = partnerService.listPartnerNames()
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to list partners", ex)
            response.responseCode = ResponseCodes.EXCEPTION_STATUS

            response.message = "Request failed, please try again later"
        }
        return ServerResponse.ok().body(response)
    }

    fun explodeFileToJson(req: ServerRequest): ServerResponse {
        var response = ApiResponseModel()
        val multipartRequest = (req.servletRequest() as? MultipartHttpServletRequest)
        if (multipartRequest != null) {
            multipartRequest.getFile("file")?.let { multipartFile ->
                val fileType = req.paramOrNull("type")
                response.data = utils.processCsvOrExcelFile(multipartFile, fileType)
                response
            } ?: run {
                response.responseCode = ResponseCodes.FAILED_CODE
                response.message = "Please select file to upload"
                response
            }
        } else {
            response.responseCode = ResponseCodes.INVALID_CODE
            response.message = "Request is not a multipart request"
        }
        return ServerResponse.ok().body(response)
    }

    fun listPartners(req: ServerRequest): ServerResponse {
        var response = ApiResponseModel()
        try {
            val keywords = req.param("keywords")
            val page = extractPage(req)
            response = partnerService.listPartners(keywords.orElse(null), page)
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to list partners", ex)
            response.responseCode = ResponseCodes.EXCEPTION_STATUS

            response.message="Request failed, please try again later"
        }
        return ServerResponse.ok().body(response)
    }

    fun getPartnerDetails(req: ServerRequest): ServerResponse {
        var response = ApiResponseModel()
        try {
            val partnerId = req.pathVariable("partnerId").toLong()
            response = this.partnerService.getPartnerDetails(partnerId)
        } catch (ex: Exception) {
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Failed to update partner details"
            response.errors = ex.toString()
        }
        return ServerResponse.ok().body(response)
    }

    fun createPartnerApiClient(req: ServerRequest): ServerResponse {
        var response = ApiResponseModel()
        try {
            val partnerId = req.pathVariable("partnerId").toLong()
            val form = req.body(ApiClientForm::class.java)
            response = this.partnerService.addPartnerApiClient(form, partnerId)
        } catch (ex: Exception) {
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Failed to update corporate account"
            response.errors = ex.toString()
        }
        return ServerResponse.ok().body(response)
    }

    fun updatePartnerApiClientCredentials(req: ServerRequest): ServerResponse {
        var response = ApiResponseModel()
        try {
            val partnerId = req.pathVariable("partnerId").toLong()
            response = this.partnerService.updateApiClientCredentials(partnerId)
        } catch (ex: Exception) {
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Failed to update api client credentials"
            response.errors = ex.toString()
        }
        return ServerResponse.ok().body(response)
    }

}