package org.kebs.app.kotlin.apollo.api.handlers.pvoc

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.payload.ApiResponseModel
import org.kebs.app.kotlin.apollo.api.payload.ResponseCodes
import org.kebs.app.kotlin.apollo.api.payload.extractPage
import org.kebs.app.kotlin.apollo.api.payload.request.ExemptionStatusForm
import org.kebs.app.kotlin.apollo.api.service.DaoValidatorService
import org.kebs.app.kotlin.apollo.api.service.PvocService
import org.springframework.stereotype.Component
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse

@Component
class PvocExemptionHandler(
        private val pvocService: PvocService,
        private val validator: DaoValidatorService
) {

    fun updateExemptionStatus(req: ServerRequest): ServerResponse {
        var response = ApiResponseModel()
        try {
            val exemptionId = req.pathVariable("exemptionId").toLong()
            val form = req.body(ExemptionStatusForm::class.java)
            validator.validateInputWithInjectedValidator(form)?.let {
                response.responseCode = ResponseCodes.INVALID_CODE
                response.message = "Request has errors, please make corrections"
                response.errors = it

            } ?: run {
                response = this.pvocService.approveDeferRejectExemption(exemptionId, form.certificateValidity!!, form.status!!, form.taskId!!, form.remarks!!)
                response
            }
        } catch (ex: Exception) {
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Request failed"
        }
        return ServerResponse.ok().body(response)
    }

    fun exemptionApplications(req: ServerRequest): ServerResponse {
        var response = ApiResponseModel()
        try {
            val applicationStatus = req.pathVariable("applicationStatus")
            val keywords = req.param("keyword")
            response = this.pvocService.listOrSearchApplicationExceptions(applicationStatus.trim().toUpperCase(), keywords.orElse(null), extractPage(req))
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to process request", ex)
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Failed"
        }
        return ServerResponse.ok().body(response)
    }

    fun exemptionApplicationDetails(req: ServerRequest): ServerResponse {
        var response = ApiResponseModel()
        try {
            val exemptionId = req.pathVariable("exemptionId").toLong()
            response = this.pvocService.retrieveExemptionApplicationDetails(exemptionId)
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to process request", ex)
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Failed"
        }
        return ServerResponse.ok().body(response)
    }

}