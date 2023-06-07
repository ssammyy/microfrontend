package org.kebs.app.kotlin.apollo.api.handlers.pvoc

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.payload.ApiResponseModel
import org.kebs.app.kotlin.apollo.api.payload.ResponseCodes
import org.kebs.app.kotlin.apollo.api.payload.extractPage
import org.kebs.app.kotlin.apollo.api.payload.request.ComplaintStatusForm
import org.kebs.app.kotlin.apollo.api.payload.request.WaiverStatusForm
import org.kebs.app.kotlin.apollo.api.service.DaoValidatorService
import org.kebs.app.kotlin.apollo.api.service.PvocService
import org.springframework.stereotype.Component
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse

@Component
class PvocWaiverHandler(
        private val pvocService: PvocService,
        private val validator: DaoValidatorService
) {
    fun waiverTaskUpdate(req: ServerRequest): ServerResponse{
        var response = ApiResponseModel()
        try {
            val waiverId = req.pathVariable("waiverId").toLong()
            val form = req.body(WaiverStatusForm::class.java)
            this.validator.validateInputWithInjectedValidator(form)?.let {
                response.errors = it
                response.responseCode = ResponseCodes.INVALID_CODE
                response.message = "Please correct errors"
                response
            } ?: run {
                response = this.pvocService.updateWaiverTaskStatus(waiverId, form.action!!, form.taskId!!, form.remarks!!)
                response
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to approve complaint task", ex)
            response.message = "Request failed"
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
        }
        return ServerResponse.ok().body(response)
    }
    fun waiverApplications(req: ServerRequest): ServerResponse {
        val complaintStatus = req.pathVariable("applicationStatus")
        val keywords = req.param("keywords")
        val response = this.pvocService.kimsWaiverApplications(complaintStatus.toUpperCase(),keywords.orElse(null), extractPage(req))
        return ServerResponse.ok().body(response)
    }

    fun waiverApplicationDetails(req: ServerRequest): ServerResponse {
        var response = ApiResponseModel()
        try {
            val waiverId = req.pathVariable("waiverId").toLong()
            response = this.pvocService.retrieveWaiverById(waiverId)
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to fetch waiver",ex)
            response.message = "Request failed"
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
        }
        return ServerResponse.ok().body(response)
    }
}