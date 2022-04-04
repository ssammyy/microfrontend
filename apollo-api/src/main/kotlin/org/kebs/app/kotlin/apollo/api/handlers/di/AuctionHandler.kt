package org.kebs.app.kotlin.apollo.api.handlers.di

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.payload.ApiResponseModel
import org.kebs.app.kotlin.apollo.api.payload.ResponseCodes
import org.kebs.app.kotlin.apollo.api.payload.extractPage
import org.kebs.app.kotlin.apollo.api.payload.request.AuctionAssignForm
import org.kebs.app.kotlin.apollo.api.payload.request.AuctionDemandNoteForm
import org.kebs.app.kotlin.apollo.api.payload.request.AuctionForm
import org.kebs.app.kotlin.apollo.api.service.AuctionService
import org.kebs.app.kotlin.apollo.api.service.DaoValidatorService
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartHttpServletRequest
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.paramOrNull
import java.sql.Date
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Component
class AuctionHandler(
        private val validationService: DaoValidatorService,
        private val auctionService: AuctionService
) {
    val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    fun listAuctionCategory(req: ServerRequest): ServerResponse {
        return ServerResponse.ok()
                .body(auctionService.listAuctionCategories())
    }

    fun uploadAuctionAttachments(req: ServerRequest): ServerResponse {
        var response = ApiResponseModel()
        try {
            val multipartRequest = (req.servletRequest() as? MultipartHttpServletRequest)
                    ?: throw ResponseStatusException(
                            HttpStatus.NOT_ACCEPTABLE,
                            "Request is not a multipart request"
                    )
            val auctionId = req.pathVariable("auctionId").toLong()
            val multipartFile = multipartRequest.getFile("file")
            val remarks = multipartRequest.getParameter("remarks")
            if (multipartFile != null) {
                remarks?.let {
                    response = auctionService.uploadAttachment(multipartFile, remarks, auctionId)
                    response
                } ?: run {
                    response.errors = mapOf(Pair("remarks", "Please add file remarks"))
                    response.responseCode = ResponseCodes.INVALID_CODE
                    response.message = "Invalid request received"
                    response
                }
            } else {
                response.errors = mapOf(Pair("file", "Please select at least one file"))
                response.responseCode = ResponseCodes.INVALID_CODE
                response.message = "Please select upload file"
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to add audit", ex)
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Failed to add auction"
        }
        return ServerResponse.ok()
                .body(response)
    }

    fun uploadAuctionCsv(req: ServerRequest): ServerResponse {
        var response = ApiResponseModel()
        try {
            val multipartRequest = (req.servletRequest() as? MultipartHttpServletRequest)
                    ?: throw ResponseStatusException(
                            HttpStatus.NOT_ACCEPTABLE,
                            "Request is not a multipart request"
                    )
            val multipartFile = multipartRequest.getFile("file")
            val fileType = multipartRequest.getParameter("file_type")
            val cfsCode = multipartRequest.getParameter("cfsCode")
            val categoryCode = multipartRequest.getParameter("categoryCode")
            val listingDate = dateFormatter.parse(multipartRequest.getParameter("listingDate"))
            if (multipartFile != null) {
                listingDate?.let {
                    response = auctionService.uploadAuctionGoods(multipartFile, fileType, categoryCode, java.sql.Date.valueOf(LocalDate.from(listingDate)), cfsCode)
                    response
                } ?: run {
                    response.errors = mapOf(Pair("listingDate", "Please select auction listing date"))
                    response.responseCode = ResponseCodes.INVALID_CODE
                    response.message = "Invalid request received"
                    response
                }
            } else {
                response.errors = mapOf(Pair("file", "Please select auction listing date"))
                response.responseCode = ResponseCodes.INVALID_CODE
                response.message = "Please select upload file"
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to add audit", ex)
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Failed to add auction"
        }
        return ServerResponse.ok()
                .body(response)
    }

    fun addAuctionRequest(req: ServerRequest): ServerResponse {
        var response = ApiResponseModel()
        try {
            val form = req.body(AuctionForm::class.java)
            validationService.validateInputWithInjectedValidator(form)?.let {
                response.errors = it
                response.responseCode = ResponseCodes.INVALID_CODE
                response.message = "Invalid request received"
                response
            } ?: run {
                response = auctionService.addAuctionRequest(form)
                response
            }
        } catch (ex: ExpectedDataNotFound) {
            response.responseCode = ResponseCodes.FAILED_CODE
            response.message = ex.localizedMessage
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to add audit", ex)
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Failed to add auction"
        }
        return ServerResponse.ok()
                .body(response)
    }

    fun listAuctions(req: ServerRequest): ServerResponse {
        var response = ApiResponseModel()
        try {
            val auctionType = req.pathVariable("auctionType")
            when (auctionType) {
                "search" -> {
                    val keyword = req.paramOrNull("keyword")
                    if (StringUtils.hasLength(keyword)) {
                        response = auctionService.listAuctionGood(keyword, auctionType, extractPage(req))
                    } else {
                        response.message = "Key word is required"
                        response.responseCode = ResponseCodes.FAILED_CODE
                    }
                }
                else -> {
                    response = auctionService.listAuctionGood(null, auctionType, extractPage(req))
                }
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to list auctions", ex)
            response.responseCode = ResponseCodes.FAILED_CODE
            response.message = "Request failed"
        }
        return ServerResponse.ok()
                .body(response)
    }

    fun findAuctionGoodById(req: ServerRequest): ServerResponse {
        var response = ApiResponseModel()
        try {
            val auctionId = req.pathVariable("auctionId").toLong()
            response = auctionService.auctionGoodDetails(auctionId)
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to process request", ex)
            response.message = "Invalid identifier"
            response.responseCode = ResponseCodes.INVALID_CODE
        }
        return ServerResponse.ok().body(response)
    }

    fun assignAuctionRequest(req: ServerRequest): ServerResponse {
        var response = ApiResponseModel()
        try {
            val auctionId = req.pathVariable("auctionId").toLong()
            val form = req.body(AuctionAssignForm::class.java)
            validationService.validateInputWithInjectedValidator(form)?.let {
                response.message = "Invalid request data"
                response.responseCode = ResponseCodes.FAILED_CODE
                response.errors = it
                response.data = form
                response
            } ?: run {
                response = auctionService.assignAuctionRequest(auctionId, form.remarks!!, form.officerId!!, form.reassign)
                response
            }
        } catch (ex: Exception) {
            response.message = "Invalid identifier"
            response.responseCode = ResponseCodes.INVALID_CODE
        }
        return ServerResponse.ok().body(response)
    }
    fun generateDemandNoteRequest(req: ServerRequest): ServerResponse {
        var response = ApiResponseModel()
        try {
            val auctionId = req.pathVariable("auctionId").toLong()
            val form = req.body(AuctionDemandNoteForm::class.java)
            validationService.validateInputWithInjectedValidator(form)?.let {
                response.message = "Invalid request data"
                response.responseCode = ResponseCodes.FAILED_CODE
                response.errors = it
                response.data = form
                response
            } ?: run {
                response = auctionService.requestPayment(auctionId, form.remarks!!, form.feeId!!)
                response
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to process request", ex)
            response.message = "Invalid identifier"
            response.responseCode = ResponseCodes.INVALID_CODE
        }
        return ServerResponse.ok().body(response)
    }

    fun updateAuctionStatus(req: ServerRequest): ServerResponse {
        var response = ApiResponseModel()
        try {
            val auctionId = req.pathVariable("auctionId").toLong()
            val multipartRequest = (req.servletRequest() as? MultipartHttpServletRequest)
                    ?: throw ResponseStatusException(
                            HttpStatus.NOT_ACCEPTABLE,
                            "Request is not a multipart request"
                    )
            val multipartFile = multipartRequest.getFile("file")
            val remarks = multipartRequest.getParameter("remarks")
            val witnessName = multipartRequest.getParameter("witnessName")
            val witnessEmail = multipartRequest.getParameter("witnessEmail")
            val witnessDesignation = multipartRequest.getParameter("witnessDesignation")
            val approval = multipartRequest.getParameter("approve").toBoolean()
            response = auctionService.approveRejectAuctionGood(auctionId, multipartFile, approval, remarks, witnessName, witnessDesignation, witnessEmail)
        } catch (ex: ResponseStatusException) {
            response.responseCode = ResponseCodes.FAILED_CODE
            response.message = ex.localizedMessage
        } catch (ex: ExpectedDataNotFound) {
            response.responseCode = ResponseCodes.FAILED_CODE
            response.message = ex.localizedMessage
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to process request", ex)
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Request failed, please try again"
        }
        return ServerResponse.ok()
                .body(response)
    }
}