package org.kebs.app.kotlin.apollo.api.handlers.pvoc

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.payload.ApiResponseModel
import org.kebs.app.kotlin.apollo.api.payload.ResponseCodes
import org.kebs.app.kotlin.apollo.api.payload.request.KebsPvocQueryForm
import org.kebs.app.kotlin.apollo.api.payload.request.KebsQueryResponse
import org.kebs.app.kotlin.apollo.api.payload.request.KebsQueryResponseForm
import org.kebs.app.kotlin.apollo.api.payload.request.PvocQueryConclusion
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DestinationInspectionDaoServices
import org.kebs.app.kotlin.apollo.api.service.DaoValidatorService
import org.kebs.app.kotlin.apollo.api.service.PvocAgentService
import org.springframework.stereotype.Component
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse

@Component
class PvocQueryHandler(
        private val pvocService: PvocAgentService,
        private val validatorService: DaoValidatorService,
        private val diDaoService: DestinationInspectionDaoServices
) {

    fun pvocPartnerQueryResponse(req: ServerRequest): ServerResponse {
        val response = ApiResponseModel()
        try {
            val form = req.body(KebsQueryResponseForm::class.java)
            validatorService.validateInputWithInjectedValidator(form)?.let {
                response.message = "Request validation failed"
                response.errors = it
                response.responseCode = ResponseCodes.INVALID_CODE
                response.data = form
                response
            } ?: run {
                return ServerResponse.ok().body(pvocService.sendPartnerQueryResponse(form))
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("KEBS Query response failed", ex)
            response.responseCode = ResponseCodes.FAILED_CODE
            response.message = "Invalid request data"
        }
        return ServerResponse.ok().body(response)
    }


    fun pvocPartnerQueryRequest(req: ServerRequest): ServerResponse {
        val response = ApiResponseModel()
        try {
            val form = req.body(KebsPvocQueryForm::class.java)
            validatorService.validateInputWithInjectedValidator(form)?.let {
                response.message = "Request validation failed"
                response.errors = it
                response.responseCode = ResponseCodes.INVALID_CODE
                response.data = form
                response
            } ?: run {
                this.diDaoService.findCdWithUcrNumber(form.ucrNumber!!)?.let {
                    return ServerResponse.ok().body(pvocService.sendPartnerQuery(form))
                } ?: run {
                    response.responseCode = ResponseCodes.NOT_FOUND
                    response.message = "Invalid consignment does not exist"
                }
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("KEBS Query request failed", ex)
            response.responseCode = ResponseCodes.FAILED_CODE
            response.message = "Invalid request data"
        }
        return ServerResponse.ok().body(response)
    }

    fun pvocPartnerQueryConclusion(req: ServerRequest): ServerResponse {
        val response = ApiResponseModel()
        try {
            val form = req.body(PvocQueryConclusion::class.java)
            validatorService.validateInputWithInjectedValidator(form)?.let {
                response.message = "Request validation failed"
                response.errors = it
                response.responseCode = ResponseCodes.INVALID_CODE
                response.data = form
                response
            } ?: run {
                return ServerResponse.ok().body(pvocService.receiveKebsQueryConclusion(form))
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("KEBS Query conclusion failed", ex)
            response.responseCode = ResponseCodes.FAILED_CODE
            response.message = "Invalid request data"
        }
        return ServerResponse.ok().body(response)
    }

}