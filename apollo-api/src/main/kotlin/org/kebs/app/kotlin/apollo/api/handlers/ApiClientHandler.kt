package org.kebs.app.kotlin.apollo.api.handlers

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.payload.*
import org.kebs.app.kotlin.apollo.api.security.bearer.OauthClientAuthenticationToken
import org.kebs.app.kotlin.apollo.api.security.jwt.JwtTokenService
import org.kebs.app.kotlin.apollo.api.service.ApiClientService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import java.util.concurrent.TimeUnit

@Service
class ApiClientHandler(
        private val apiClient: ApiClientService,
        private val tokenService: JwtTokenService,
        private val authenticationManager: AuthenticationManager
) {
    fun authenticateApiClient(request: ServerRequest): ServerResponse {
        val apiResponse = ApiResponseModel()
        try {
            val form = request.body(OauthClientLoginForm::class.java)
            when (form.grantType?.toLowerCase()) {
                "client_credentials" -> {
                    val auth = authenticationManager.authenticate(OauthClientAuthenticationToken(form.clientId!!, form.clientSecret!!,form.grantType!!))
                    val clientToken = tokenService.generateClientToken(auth, request)
                    val clientTokenResult = OauthClientLoginResult()
                    clientTokenResult.accessToken = clientToken
                    clientTokenResult.tokenType = "Bearer"
                    clientTokenResult.expiresIn = TimeUnit.DAYS.toSeconds(1)
                    return ServerResponse.ok().body(clientTokenResult)
                }
                else -> {
                    apiResponse.message = "Invalid grant type"
                    apiResponse.responseCode = ResponseCodes.FAILED_CODE
                }
            }
        } catch (ex: Exception) {
            KotlinLogging.logger {  }.error("Failed to process request",ex)
            apiResponse.message = "Invalid client id and/or secret"
            apiResponse.responseCode = ResponseCodes.EXCEPTION_STATUS
        }
        return ServerResponse.ok().body(apiResponse)
    }

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
            KotlinLogging.logger { }.info("RequestData: ${form.toString()}")
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
            val clientId = request.pathVariable("clientId").toLong()
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