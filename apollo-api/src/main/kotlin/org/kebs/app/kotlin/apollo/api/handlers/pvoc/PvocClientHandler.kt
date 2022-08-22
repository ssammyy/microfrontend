package org.kebs.app.kotlin.apollo.api.handlers.pvoc

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.payload.ApiResponseModel
import org.kebs.app.kotlin.apollo.api.payload.ResponseCodes
import org.kebs.app.kotlin.apollo.api.payload.extractPage
import org.kebs.app.kotlin.apollo.api.payload.request.*
import org.kebs.app.kotlin.apollo.api.service.BillingService
import org.kebs.app.kotlin.apollo.api.service.DaoValidatorService
import org.kebs.app.kotlin.apollo.api.service.PvocAgentService
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.paramOrNull

@Component
class PvocClientHandler(
        private val pvocService: PvocAgentService,
        private val billingService: BillingService,
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
            val form = req.body(RfcEntityForm::class.java)
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
            response.message = "Invalid COI request data"
        }
        return ServerResponse.ok().body(response)
    }

    fun foreignCocRfc(req: ServerRequest): ServerResponse {
        val response = ApiResponseModel()
        try {
            val form = req.body(RfcEntityForm::class.java)
            validatorService.validateInputWithInjectedValidator(form)?.let {
                response.message = "Request validation failed"
                response.errors = it
                response.responseCode = ResponseCodes.INVALID_CODE
                response.data = form
                response
            } ?: run {
                return ServerResponse.ok().body(pvocService.receiveRfcCoc(form))
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to decode data", ex)
            response.responseCode = ResponseCodes.FAILED_CODE
            response.message = "Invalid COC request data"
        }
        return ServerResponse.ok().body(response)
    }

    fun foreignCorRfc(req: ServerRequest): ServerResponse {
        val response = ApiResponseModel()
        try {
            val form = req.body(RfcCorForm::class.java)
            validatorService.validateInputWithInjectedValidator(form)?.let {
                response.message = "Request validation failed"
                response.errors = it
                response.responseCode = ResponseCodes.INVALID_CODE
                response.data = form
                response
            } ?: run {
                return ServerResponse.ok().body(pvocService.receiveRfcCor(form))
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to decode data", ex)
            response.responseCode = ResponseCodes.FAILED_CODE
            response.message = "Invalid COR request data"
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

    fun idfRequestDataWithItems(req: ServerRequest): ServerResponse {
        var response = ApiResponseModel()
        try {
            val ucrNumber = req.paramOrNull("ucr")
            val idfNumber = req.paramOrNull("idf")
            if (StringUtils.hasLength(idfNumber) || StringUtils.hasLength(ucrNumber)) {
                response = pvocService.getIdfData(idfNumber, ucrNumber)
            } else {
                response.message = "ucr or idf parameters are required"
                response.responseCode = ResponseCodes.INVALID_CODE
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to add  idf data", ex)
            response.responseCode = ResponseCodes.FAILED_CODE
            response.message = "Invalid request data"
        }
        return ServerResponse.ok().body(response)
    }

    fun rfcRequestDataWithItems(req: ServerRequest): ServerResponse {
        var response = ApiResponseModel()
        try {
            val ucrNumber = req.paramOrNull("ucr")
            val rfcNumber = req.paramOrNull("rfc")
            val documentType = req.paramOrNull("doc_type")
            if (StringUtils.hasLength(rfcNumber) || StringUtils.hasLength(ucrNumber)) {
                response = pvocService.getRfcData(rfcNumber, ucrNumber, documentType)
            } else {
                response.message = "ucr or rfc parameters are required"
                response.responseCode = ResponseCodes.INVALID_CODE
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to add  RFC data", ex)
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

    fun kebsPartnerQueries(req: ServerRequest): ServerResponse {
        var response = ApiResponseModel()
        try {
            val pg = extractPage(req)
            val status = req.paramOrNull("status")?.toIntOrNull()
            response = pvocService.retrievePartnerQueries(status ?: 0, pg)
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Query response failed", ex)
            response.responseCode = ResponseCodes.FAILED_CODE
            response.message = "Invalid request data"
        }
        return ServerResponse.ok().body(response)
    }

    fun pvocTimelineIssues(req: ServerRequest): ServerResponse {
        var response = ApiResponseModel()
        try {
            response = pvocService.timelineIssues(req.param("yearMonth"))
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Time line issues failed", ex)
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Failed to process request"
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

    fun listRiskProfile(req: ServerRequest): ServerResponse {
        var response = ApiResponseModel()
        try {
            val dateStr = req.paramOrNull("cat_date")
            val pg = extractPage(req)
            KotlinLogging.logger { }.info("Risk profile: $dateStr")
            response = pvocService.getRiskProfile(null, dateStr, pg)
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Risk profile failed", ex)
            response.responseCode = ResponseCodes.FAILED_CODE
            response.message = "Invalid request data"
        }
        return ServerResponse.ok().body(response)
    }

    fun listMonthlyInvoiceDetails(req: ServerRequest): ServerResponse {
        var response = ApiResponseModel()
        try {
            val dateStr = req.pathVariable("inv_date")
            val pg = extractPage(req)
            KotlinLogging.logger { }.info("Monthly invoice for month: $dateStr")
            response = billingService.getPvocPartnerInvoice(dateStr, pg)
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Risk profile failed", ex)
            response.responseCode = ResponseCodes.FAILED_CODE
            response.message = "Invalid request data"
        }
        return ServerResponse.ok().body(response)
    }
}