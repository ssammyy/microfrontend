package org.kebs.app.kotlin.apollo.api.handlers.pvoc

import org.kebs.app.kotlin.apollo.api.payload.ApiClientForm
import org.kebs.app.kotlin.apollo.api.payload.ApiResponseModel
import org.kebs.app.kotlin.apollo.api.payload.ResponseCodes
import org.kebs.app.kotlin.apollo.api.payload.extractPage
import org.kebs.app.kotlin.apollo.api.payload.request.PvocPartnersForms
import org.kebs.app.kotlin.apollo.api.service.PvocPartnerService
import org.springframework.stereotype.Component
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse

@Component
class PvocPartnersHandler(
        private val partnerService: PvocPartnerService
) {

    fun createPartner(req: ServerRequest): ServerResponse {
        var response = ApiResponseModel()
        try {
            val form = req.body(PvocPartnersForms::class.java)
            response = this.partnerService.addPartnerDetails(form)
        } catch (ex: Exception) {
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
            response = this.partnerService.updatePartnerDetails(form, partnerId)
        } catch (ex: Exception) {
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Failed to update partner details"
            response.errors = ex.toString()
        }
        return ServerResponse.ok().body(response)
    }

    fun listPartners(req: ServerRequest): ServerResponse {
        val keywords = req.param("keywords")
        val page = extractPage(req)
        return ServerResponse.ok().body(partnerService.listPartners(keywords.orElse(null), page))
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

}