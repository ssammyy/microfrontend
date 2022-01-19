package org.kebs.app.kotlin.apollo.api.handlers.pvoc

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.payload.ApiResponseModel
import org.kebs.app.kotlin.apollo.api.payload.ResponseCodes
import org.kebs.app.kotlin.apollo.api.payload.extractPage
import org.kebs.app.kotlin.apollo.api.service.PvocService
import org.springframework.stereotype.Component
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse

@Component
class PvocWaiverHandler(
        private val pvocService: PvocService
) {
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