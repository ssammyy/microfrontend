package org.kebs.app.kotlin.apollo.api.handlers

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.payload.*
import org.kebs.app.kotlin.apollo.api.service.ApiClientService
import org.springframework.stereotype.Service
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse

@Service
class ApiClientHandler(
        private val apiClient: ApiClientService
) {
    fun loadClients(request: ServerRequest): ServerResponse {
        val clientName = request.param("client_name")
        val page = extractPage(request)
        return ServerResponse.ok()
                .body(apiClient.listAndSearchApplicationClients(clientName.orElse(null), page))
    }

    fun updateApiClientStatus(request: ServerRequest): ServerResponse {
        var response = ApiResponseModel()
        try {
            val form = request.body(ApiClientUpdateForm::class.java)
            val r = this.apiClient.updateClientStatus(form)
            response = r
        } catch (ex: Exception) {
            response.responseCode = ResponseCodes.FAILED_CODE
            response.message = "Request failed, please try again later"
            response.errors = ex.toString()
        }
        return ServerResponse.ok().body(response)
    }

    fun addApiClient(request: ServerRequest): ServerResponse {
        var response = ApiResponseModel()
        try {
            val form = request.body(ApiClientForm::class.java)
            KotlinLogging.logger {  }.info("RequestData: ${form.toString()}")
            val r = this.apiClient.createApiClient(form)
            response = r
        } catch (ex: Exception) {
            response.responseCode = ResponseCodes.FAILED_CODE
            response.message = "Request failed, please try again later"
            response.errors = ex.toString()
        }
        return ServerResponse.ok().body(response)
    }

    fun updateApiClient(request: ServerRequest): ServerResponse {
        var response = ApiResponseModel()
        try {
            val clientId=request.pathVariable("clientId").toLong()
            val form = request.body(ApiClientForm::class.java)
            val r = this.apiClient.updateApiClient(form, clientId)
            response = r
        } catch (ex: Exception) {
            response.responseCode = ResponseCodes.FAILED_CODE
            response.message = "Request failed, please try again later"
            response.errors = ex.toString()
        }
        return ServerResponse.ok().body(response)
    }
}