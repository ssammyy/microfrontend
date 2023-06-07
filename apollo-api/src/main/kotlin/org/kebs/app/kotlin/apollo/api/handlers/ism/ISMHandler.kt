package org.kebs.app.kotlin.apollo.api.handlers.ism

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.payload.ApiResponseModel
import org.kebs.app.kotlin.apollo.api.payload.ResponseCodes
import org.kebs.app.kotlin.apollo.api.payload.extractPage
import org.kebs.app.kotlin.apollo.api.payload.request.ISMApprovalRequestForm
import org.kebs.app.kotlin.apollo.api.payload.request.InternationalStandardMarkForm
import org.kebs.app.kotlin.apollo.api.payload.request.InternationalStandardMarkRequestsForm
import org.kebs.app.kotlin.apollo.api.service.DaoValidatorService
import org.kebs.app.kotlin.apollo.api.service.InternationalStandardMarkService
import org.springframework.stereotype.Component
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse

@Component
class ISMHandler(
        private val ismService: InternationalStandardMarkService,
        private val validationService: DaoValidatorService
) {

    fun approveRejectISMApplication(req: ServerRequest): ServerResponse {
        val response = ApiResponseModel()
        try {
            val form = req.body(ISMApprovalRequestForm::class.java)
            validationService.validateInputWithInjectedValidator(form)?.let {
                response.errors = it
                response.message = "Validation failed, please fix highlighted errors"
                response.responseCode = ResponseCodes.FAILED_CODE
                response
            } ?: run {
                return ServerResponse.ok().body(ismService.approveRejectIsm(form.requestId!!, form.remarks!!, form.approved))
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to  process ISM approval requests", ex)
            response.responseCode = ResponseCodes.INVALID_CODE
            response.message = "Failed to process request"
        }
        return ServerResponse.ok().body(response)
    }

    fun listIsmRequests(req: ServerRequest): ServerResponse {
        val response = ApiResponseModel()
        try {
            val page = extractPage(req)
            val status = req.pathVariable("requestStatus").toInt()
            return ServerResponse.ok().body(ismService.listApplications(status, page))
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to  get requests", ex)
            response.responseCode = ResponseCodes.INVALID_CODE
            response.message = "Failed to process request"
        }
        return ServerResponse.ok().body(response)
    }

    fun getIsmRequests(req: ServerRequest): ServerResponse {
        val response = ApiResponseModel()
        try {
            val requestId = req.pathVariable("requestId").toLong()
            return ServerResponse.ok().body(ismService.getIsmApplication(requestId))
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to  get requests", ex)
            response.responseCode = ResponseCodes.INVALID_CODE
            response.message = "Failed to process request"
        }
        return ServerResponse.ok().body(response)
    }

    fun createIsmRequests(req: ServerRequest): ServerResponse {
        val response = ApiResponseModel()
        try {
            val form = req.body(InternationalStandardMarkForm::class.java)
            this.validationService.validateInputWithInjectedValidator(form)?.let {
                response.message = "Invalid request data"
                response.responseCode = ResponseCodes.INVALID_CODE
                response.errors = it
                response.data = form
                response
            } ?: run {
                return ServerResponse.ok().body(ismService.applyForISM(form))
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to  get requests", ex)
            response.responseCode = ResponseCodes.INVALID_CODE
            response.message = "Failed to process request"
        }
        return ServerResponse.ok().body(response)
    }

    fun listExternalUserApplications(req: ServerRequest): ServerResponse {
        val response = ApiResponseModel()
        try {
            val form = req.body(InternationalStandardMarkRequestsForm::class.java)
            this.validationService.validateInputWithInjectedValidator(form)?.let {
                response.errors = it
                response.data = form
                response.message = "Invalid request data"
                response.responseCode = ResponseCodes.INVALID_CODE
                response
            } ?: run {
                return ServerResponse.ok().body(ismService.getCustomerIsmApplications(form.emailAddress, extractPage(req)))
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to  get requests", ex)
            response.responseCode = ResponseCodes.INVALID_CODE
            response.message = "Failed to process request"
        }
        return ServerResponse.ok().body(response)
    }
}