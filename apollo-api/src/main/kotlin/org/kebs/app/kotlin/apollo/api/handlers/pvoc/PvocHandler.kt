package org.kebs.app.kotlin.apollo.api.handlers.pvoc

import mu.KotlinLogging
import okhttp3.internal.toNonNegativeInt
import org.codehaus.jackson.map.ObjectMapper
import org.flowable.common.engine.api.FlowableObjectNotFoundException
import org.kebs.app.kotlin.apollo.api.controllers.diControllers.pvoc.ExceptionPayload
import org.kebs.app.kotlin.apollo.api.controllers.diControllers.pvoc.ExceptionRenewalPayload
import org.kebs.app.kotlin.apollo.api.handlers.forms.WaiverApplication
import org.kebs.app.kotlin.apollo.api.payload.ApiResponseModel
import org.kebs.app.kotlin.apollo.api.payload.ResponseCodes
import org.kebs.app.kotlin.apollo.api.service.DaoValidatorService
import org.kebs.app.kotlin.apollo.api.service.PvocService
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.MultipartHttpServletRequest
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse

@Component
class PvocHandler(
        private val pvocService: PvocService,
        private val mapper: ObjectMapper,
        private val validator: DaoValidatorService

) {
    fun checkExemptionEligibility(req: ServerRequest): ServerResponse {
        val response = this.pvocService.checkExemptionApplicable()
        return ServerResponse.ok().body(response)
    }

    fun waiverCategories(req: ServerRequest): ServerResponse {
        return ServerResponse.ok().body(this.pvocService.listWaiverCategories())
    }

    fun exemptionHistory(req: ServerRequest): ServerResponse {
        var currentPage: Int = 0
        var pageSize: Int = 20
        req.param("page").ifPresent { p -> currentPage = p.toNonNegativeInt(0) }
        req.param("size").ifPresent { p -> pageSize = p.toNonNegativeInt(20) }
        if (pageSize > 100) {
            pageSize = 100
        }
        val response = this.pvocService.exemptionApplicationHistory(currentPage, pageSize);
        return ServerResponse.ok().body(response)
    }

    fun manufacturerExemptionHistory(req: ServerRequest): ServerResponse {
        var currentPage: Int = 0
        var pageSize: Int = 20
        req.param("page").ifPresent { p -> currentPage = p.toNonNegativeInt(0) }
        req.param("size").ifPresent { p -> pageSize = p.toNonNegativeInt(20) }
        if (pageSize > 100) {
            pageSize = 100
        }
        val status = req.param("status").orElse(null)
        val response = this.pvocService.manufacturerExemptionApplicationHistory(status, currentPage, pageSize);
        return ServerResponse.ok().body(response)
    }

    fun exemptionApplication(req: ServerRequest): ServerResponse {
        var response: ApiResponseModel
        try {
            val multipartRequest = (req.servletRequest() as? MultipartHttpServletRequest)
                    ?: throw ResponseStatusException(
                            HttpStatus.NOT_ACCEPTABLE,
                            "Request is not a multipart request"
                    )

            val data = multipartRequest.getParameter("data")
            val exemption: ExceptionPayload = this.mapper.readValue(data, ExceptionPayload::class.java)
            // Validate and save data
            val errors = this.validator.validateInputWithInjectedValidator(exemption)
            if (errors == null) {
                val documents: List<MultipartFile> = multipartRequest.getFiles("files")
                response = pvocService.saveApplicationExemption(exemption, documents)
            } else {
                response = ApiResponseModel()
                response.responseCode = ResponseCodes.INVALID_CODE
                response.data = exemption
                response.errors = errors
                response.message = "Request data is invalid"
            }
        } catch (ex: ResponseStatusException) {
            response = ApiResponseModel()
            response.message = "Invalid request data: " + ex.reason
            response.responseCode = ResponseCodes.FAILED_CODE
        } catch (ex: Exception) {
            KotlinLogging.logger {}.error("Exemption request failed:", ex)
            response = ApiResponseModel()
            response.message = "Failed to process request"
            response.responseCode = ResponseCodes.FAILED_CODE
        }
        return ServerResponse.ok().body(response)
    }

    fun viewExemption(req: ServerRequest): ServerResponse {
        var response: ApiResponseModel
        req.pathVariable("exemptionId").let { exemptionId ->
            val id = exemptionId.toLongOrNull()
            if (id != null) {
                response = this.pvocService.retrieveMyExemptionApplicationById(id)
            } else {
                response = ApiResponseModel()
                response.responseCode = ResponseCodes.INVALID_CODE
                response.message = "Invalid identifier"
            }
        }
        return ServerResponse.ok().body(response)
    }

    fun renewExemptionCertificate(req: ServerRequest): ServerResponse {
        var response: ApiResponseModel
        req.pathVariable("exemptionId").let { exemptionId ->
            val id = exemptionId.toLongOrNull()
            if (id != null) {
                val renewal = req.body(ExceptionRenewalPayload::class.java)
                response = this.pvocService.applyRenewExemptionCertificateRenewal(id, renewal)
            } else {
                response = ApiResponseModel()
                response.responseCode = ResponseCodes.INVALID_CODE
                response.message = "Invalid identifier"
            }
        }
        return ServerResponse.ok().body(response)
    }

    fun viewWaiver(req: ServerRequest): ServerResponse {
        var response: ApiResponseModel
        req.pathVariable("waiverId").let { taskId ->
            val id = taskId.toLongOrNull()
            if (id != null) {
                response = this.pvocService.retrieveWaiverById(id)
            } else {
                response = ApiResponseModel()
                response.responseCode = ResponseCodes.INVALID_CODE
                response.message = "Invalid identifier"
            }
        }
        return ServerResponse.ok().body(response)
    }

    fun waiversApplication(req: ServerRequest): ServerResponse {
        var response: ApiResponseModel
        try {
            val multipartRequest = (req.servletRequest() as? MultipartHttpServletRequest)
                    ?: throw ResponseStatusException(
                            HttpStatus.NOT_ACCEPTABLE,
                            "Request is not a multipart request"
                    )

            val data = multipartRequest.getParameter("data")
            val waiver: WaiverApplication = this.mapper.readValue(data, WaiverApplication::class.java)
            // Validate and save data
            val errors = this.validator.validateInputWithInjectedValidator(waiver)
            if (errors == null) {
                val documents: List<MultipartFile> = multipartRequest.getFiles("files")
                response = this.pvocService.applyOrRenewWaiver(waiver, documents)
            } else {
                response = ApiResponseModel()
                response.responseCode = ResponseCodes.INVALID_CODE
                response.data = waiver
                response.errors = errors
                response.message = "Request data is invalid"
            }
        } catch (ex: FlowableObjectNotFoundException) {
            response = ApiResponseModel()
            response.message = "Waiver process defination not found"
            response.errors = ex.localizedMessage
            response.responseCode = ResponseCodes.FAILED_CODE
        } catch (ex: ResponseStatusException) {
            response = ApiResponseModel()
            response.message = "Invalid request data: " + ex.reason
            response.responseCode = ResponseCodes.FAILED_CODE
        } catch (ex: Exception) {
            KotlinLogging.logger {}.error("Failed to process request", ex)
            response = ApiResponseModel()
            response.message = "Failed to process request"
            response.responseCode = ResponseCodes.FAILED_CODE
        }
        return ServerResponse.ok().body(response)
    }

    fun manufacturerWaiverHistory(req: ServerRequest): ServerResponse {
        var currentPage: Int = 0
        var pageSize: Int = 20
        req.param("page").ifPresent { p -> currentPage = p.toNonNegativeInt(0) }
        req.param("size").ifPresent { p -> pageSize = p.toNonNegativeInt(20) }
        if (pageSize > 100) {
            pageSize = 100
        }
        val status = req.param("status").orElse("new")
        val response = this.pvocService.manufacturerWaiverApplicationHistory(status, currentPage, pageSize)
        return ServerResponse.ok().body(response)
    }

    fun waiverHistory(req: ServerRequest): ServerResponse {

        var currentPage: Int = 0
        var pageSize: Int = 20
        req.param("page").ifPresent { p -> currentPage = p.toNonNegativeInt(0) }
        req.param("size").ifPresent { p -> pageSize = p.toNonNegativeInt(20) }
        if (pageSize > 100) {
            pageSize = 100
        }
        val response = this.pvocService.waiverApplicationHistory(currentPage, pageSize);
        return ServerResponse.ok().body(response)
    }


}