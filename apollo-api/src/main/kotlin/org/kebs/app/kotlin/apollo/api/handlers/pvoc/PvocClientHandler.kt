package org.kebs.app.kotlin.apollo.api.handlers.pvoc

import com.fasterxml.jackson.databind.exc.InvalidFormatException
import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.payload.ApiResponseModel
import org.kebs.app.kotlin.apollo.api.payload.ResponseCodes
import org.kebs.app.kotlin.apollo.api.payload.request.*
import org.kebs.app.kotlin.apollo.api.service.DaoValidatorService
import org.kebs.app.kotlin.apollo.api.service.PvocAgentService
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.stereotype.Component
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse

@Component
class PvocClientHandler(
        private val pvocService: PvocAgentService,
        private val validatorService: DaoValidatorService
) {
    fun foreignCoi(req: ServerRequest): ServerResponse {
        val response = ApiResponseModel()
        try {
            val form = req.body(CoiEntityForm::class.java)
            validatorService.validateInputWithInjectedValidator(form)?.let {
                response.message = "Request validation failed"
                response.errors = it
                response.responseCode = ResponseCodes.INVALID_CODE
                response.data = form
                response
            } ?: run {
                return ServerResponse.ok().body(pvocService.receiveCoi(form))
            }
        } catch (ex: HttpMessageNotReadableException) {
            KotlinLogging.logger { }.error("Failed to process foreign COI", ex)
            response.responseCode = ResponseCodes.FAILED_CODE
            response.message = "Invalid request data"
            response.errors = mapOf(Pair("body", ex.mostSpecificCause.message))
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to process foreign COI", ex)
            response.responseCode = ResponseCodes.FAILED_CODE
            response.message = "Invalid request data"
        }
        return ServerResponse.ok().body(response)
    }

    fun foreignCoc(req: ServerRequest): ServerResponse {
        val response = ApiResponseModel()
        try {
            val form = req.body(CocEntityForm::class.java)
            validatorService.validateInputWithInjectedValidator(form)?.let {
                response.message = "Request validation failed"
                response.errors = it
                response.responseCode = ResponseCodes.INVALID_CODE
                response.data = form
                response
            } ?: run {
                return ServerResponse.ok().body(pvocService.receiveCoc(form))
            }
        } catch (ex: HttpMessageNotReadableException) {
            KotlinLogging.logger { }.error("Failed to process foreign COI:", ex)
            response.responseCode = ResponseCodes.FAILED_CODE
            response.message = "Invalid request data"
            response.errors = mapOf(Pair("body", ex.mostSpecificCause.message))
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to receive COC", ex)
            response.responseCode = ResponseCodes.FAILED_CODE
            response.message = "Invalid request data"
        }
        return ServerResponse.ok().body(response)
    }

    fun foreignCor(req: ServerRequest): ServerResponse {
        val response = ApiResponseModel()
        try {
            val form = req.body(CorEntityForm::class.java)
            validatorService.validateInputWithInjectedValidator(form)?.let {
                response.message = "Request validation failed"
                response.errors = it
                response.responseCode = ResponseCodes.INVALID_CODE
                response.data = form
                response
            } ?: run {
                return ServerResponse.ok().body(pvocService.receiveCor(form))
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to add COR", ex)
            response.responseCode = ResponseCodes.FAILED_CODE
            response.message = "Invalid request data"
        }
        return ServerResponse.ok().body(response)
    }

    fun foreignNcr(req: ServerRequest): ServerResponse {
        val response = ApiResponseModel()
        try {
            val form = req.body(NcrEntityForm::class.java)
            validatorService.validateInputWithInjectedValidator(form)?.let {
                response.message = "Request validation failed"
                response.errors = it
                response.responseCode = ResponseCodes.INVALID_CODE
                response.data = form
                response
            } ?: run {
                return ServerResponse.ok().body(pvocService.receiveNcr(form))
            }
        } catch (ex: Exception) {
            response.responseCode = ResponseCodes.FAILED_CODE
            response.message = "Invalid request data"
        }
        return ServerResponse.ok().body(response)
    }

    fun foreignCoiRfc(req: ServerRequest): ServerResponse {
        val response = ApiResponseModel()
        try {
            val form = req.body(RfcCoiEntityForm::class.java)
            validatorService.validateInputWithInjectedValidator(form)?.let {
                response.message = "Request validation failed"
                response.errors = it
                response.responseCode = ResponseCodes.INVALID_CODE
                response.data = form
                response
            } ?: run {
                return ServerResponse.ok().body(pvocService.receiveRfcCoi(form))
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to decode data", ex)
            response.responseCode = ResponseCodes.FAILED_CODE
            response.message = "Invalid request data"
        }
        return ServerResponse.ok().body(response)
    }

    fun idfDataWithItems(req: ServerRequest): ServerResponse {
        val response = ApiResponseModel()
        try {
            val form = req.body(IdfEntityForm::class.java)
            validatorService.validateInputWithInjectedValidator(form)?.let {
                response.message = "Request validation failed"
                response.errors = it
                response.responseCode = ResponseCodes.INVALID_CODE
                response.data = form
                response
            } ?: run {
                return ServerResponse.ok().body(pvocService.addIdfData(form))
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to add  idf data", ex)
            response.responseCode = ResponseCodes.FAILED_CODE
            response.message = "Invalid request data"
        }
        return ServerResponse.ok().body(response)
    }

    fun pvocPartnerQueryRequest(req: ServerRequest): ServerResponse {
        val response = ApiResponseModel()
        try {
            val form = req.body(PvocKebsQueryForm::class.java)
            validatorService.validateInputWithInjectedValidator(form)?.let {
                response.message = "Request validation failed"
                response.errors = it
                response.responseCode = ResponseCodes.INVALID_CODE
                response.data = form
                response
            } ?: run {
                return ServerResponse.ok().body(pvocService.receivePartnerQuery(form))
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Partner query failed", ex)
            response.responseCode = ResponseCodes.FAILED_CODE
            response.message = "Invalid request data"
        }
        return ServerResponse.ok().body(response)
    }

    fun pvocPartnerQueryResponse(req: ServerRequest): ServerResponse {
        val response = ApiResponseModel()
        try {
            val form = req.body(PvocQueryResponse::class.java)
            validatorService.validateInputWithInjectedValidator(form)?.let {
                response.message = "Request validation failed"
                response.errors = it
                response.responseCode = ResponseCodes.INVALID_CODE
                response.data = form
                response
            } ?: run {
                return ServerResponse.ok().body(pvocService.receivePartnerQueryResponse(form))
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Query response failed", ex)
            response.responseCode = ResponseCodes.FAILED_CODE
            response.message = "Invalid request data"
        }
        return ServerResponse.ok().body(response)
    }

    fun riskProfile(req: ServerRequest): ServerResponse {
        val response = ApiResponseModel()
        try {
            val form = req.body(RiskProfileForm::class.java)
            validatorService.validateInputWithInjectedValidator(form)?.let {
                response.message = "Request validation failed"
                response.errors = it
                response.responseCode = ResponseCodes.INVALID_CODE
                response.data = form
                response
            } ?: run {
                return ServerResponse.ok().body(pvocService.addRiskProfile(form))
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Risk profile failed", ex)
            response.responseCode = ResponseCodes.FAILED_CODE
            response.message = "Invalid request data"
        }
        return ServerResponse.ok().body(response)
    }
}