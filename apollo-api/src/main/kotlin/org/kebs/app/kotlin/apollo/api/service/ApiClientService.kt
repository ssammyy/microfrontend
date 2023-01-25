package org.kebs.app.kotlin.apollo.api.service


import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.hazelcast.internal.util.QuickMath.bytesToHex
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.features.json.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.content.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import org.apache.commons.codec.binary.Base32
import org.apache.commons.net.util.Base64
import org.apache.http.conn.ssl.NoopHostnameVerifier
import org.jasypt.encryption.StringEncryptor
import org.kebs.app.kotlin.apollo.api.payload.ApiClientForm
import org.kebs.app.kotlin.apollo.api.payload.ApiClientUpdateForm
import org.kebs.app.kotlin.apollo.api.payload.ApiResponseModel
import org.kebs.app.kotlin.apollo.api.payload.ResponseCodes
import org.kebs.app.kotlin.apollo.api.payload.response.ApiClientDao
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DaoService
import org.kebs.app.kotlin.apollo.store.model.external.ApiClientEvents
import org.kebs.app.kotlin.apollo.store.model.external.SystemApiClient
import org.kebs.app.kotlin.apollo.store.repo.external.ApiClientEventsRepo
import org.kebs.app.kotlin.apollo.store.repo.external.ApiClientRepo
import org.springframework.data.domain.PageRequest
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.security.SecureRandom
import java.sql.Timestamp
import java.time.Instant
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import javax.net.ssl.SSLContext


data class CallbackResult(
    var data: Any, var type: String, var checksum: String
)

@Service
class ApiClientService(
    val apiClientRepo: ApiClientRepo,
    val apiClientEventRepo: ApiClientEventsRepo,
    val passwordEncoder: PasswordEncoder,
    private val jasyptStringEncryptor: StringEncryptor,
    val commonDaoServices: CommonDaoServices,
    val objectMapper: ObjectMapper
) {

    fun generateRandom(size: Int, userFriendly: Boolean = false): String {
        val byteArray: ByteArray = ByteArray(size * 3)
        SecureRandom().nextBytes(byteArray)
        if (userFriendly) {
            val result = Base32().encodeAsString(byteArray)
            return result.replace("=", "").substring(0, size)
        } else {
            return Base64.encodeBase64String(byteArray).substring(0, size)
        }
    }

    fun listAndSearchApplicationClients(clientName: String?, page: PageRequest): ApiResponseModel {
        val response = ApiResponseModel()
        clientName?.let {
            val clients = apiClientRepo.findByClientNameContains(clientName, page)
            response.data = ApiClientDao.fromList(clients.toList())
            response.responseCode = ResponseCodes.SUCCESS_CODE
            response.pageNo = clients.number
            response.totalCount = clients.totalElements
            response.totalPages = clients.totalPages
            response.message = "Search result"
            response
        } ?: run {
            val clients = apiClientRepo.findAll(page)
            response.data = ApiClientDao.fromList(clients.toList())
            response.responseCode = ResponseCodes.SUCCESS_CODE
            response.message = "Success"
            response.pageNo = clients.number
            response.totalCount = clients.totalElements
            response.totalPages = clients.totalPages
            response
        }
        return response
    }

    fun createApiClient(form: ApiClientForm): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val apiClient = SystemApiClient()
            val map = mutableMapOf<String, Any?>()
            apiClient.callbackURL = form.callbackURL
            apiClient.eventsURL = form.eventsURL
            apiClient.clientName = form.clientName
            apiClient.clientType = form.clientType
            apiClient.clientRole = form.clientRole
            apiClient.descriptions = form.descriptions
            apiClient.clientBlocked = 0
            apiClient.status = 1
            apiClient.createdBy = commonDaoServices.checkLoggedInUser()
            apiClient.createdOn = Timestamp.from(Instant.now())
            apiClient.clientId = "${form.clientType}${generateRandom(15, true)}"
            val apiSecret = generateRandom(15, true)
            apiClient.clientSecret = this.passwordEncoder.encode(apiSecret)
            val saved = this.apiClientRepo.save(apiClient)
            map["client_id"] = apiClient.clientId
            map["client_secret"] = apiSecret
            map["record_id"] = saved.id
            response.responseCode = ResponseCodes.SUCCESS_CODE
            response.message = "Client created"
            response.data = map
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to add client:", ex)
            response.responseCode = ResponseCodes.FAILED_CODE
            response.message = "Failed to process request"
            response.errors = ex.toString()
        }
        return response
    }

    fun updateApiClient(form: ApiClientForm, clientId: Long): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val client = this.apiClientRepo.findById(clientId)
            if (client.isPresent) {
                val apiClient = client.get()
                val map = mutableMapOf<String, Any?>()
                apiClient.callbackURL = form.callbackURL
                apiClient.eventsURL = form.eventsURL
                apiClient.descriptions = form.descriptions
                apiClient.clientName = form.clientName
                apiClient.clientType = form.clientType
                apiClient.clientRole = form.clientRole
                val saved = this.apiClientRepo.save(apiClient)
                map["client_id"] = apiClient.clientId
                map["record_id"] = saved.id
                response.responseCode = ResponseCodes.SUCCESS_CODE
                response.message = "Client updated"
                response.data = map
            } else {
                response.responseCode = ResponseCodes.NOT_FOUND
                response.message = "Client does not exist"
            }
        } catch (ex: Exception) {
            response.responseCode = ResponseCodes.FAILED_CODE
            response.message = "Failed to process request"
            response.errors = ex.toString()
        }
        return response
    }

    fun updateClientStatus(form: ApiClientUpdateForm): ApiResponseModel {
        val response = ApiResponseModel()
        val client = this.apiClientRepo.findByClientId(form.clientId!!)
        if (client.isPresent) {
            val c = client.get()
            c.varField1 = form.remarks
            if (c.status == 4) {
                response.message = "Account deleted and cannot be updated"
                response.responseCode = ResponseCodes.FAILED_CODE
                return response
            }
            when (form.actionCode) {
                "BLOCK" -> {
                    c.clientBlocked = 1
                    this.apiClientRepo.save(c)
                    response.message = "Account blocked"
                    response.responseCode = ResponseCodes.SUCCESS_CODE
                }
                "UNBLOCK" -> {
                    c.clientBlocked = 0
                    this.apiClientRepo.save(c)
                    response.message = "Account unblocked"
                    response.responseCode = ResponseCodes.SUCCESS_CODE
                }
                "DELETE" -> {
                    c.clientBlocked = 2
                    c.status = 4
                    this.apiClientRepo.save(c)
                    response.message = "Account deleted"
                    response.responseCode = ResponseCodes.SUCCESS_CODE
                }
                else -> {
                    response.message = "Unsurported action code: ${form.actionCode}"
                    response.responseCode = ResponseCodes.FAILED_CODE
                }
            }
        } else {
            throw UsernameNotFoundException("client id or secret are invalid")
        }
        return response
    }

    fun authenticateClient(clientId: String, clientSecret: String): SystemApiClient? {
        val client = this.apiClientRepo.findByClientId(clientId)
        if (client.isPresent) {
            val c = client.get()
            if (this.passwordEncoder.matches(clientSecret, c.clientSecret)) {
                return c
            } else {
                throw UsernameNotFoundException("client id or secret are invalid")
            }
        } else {
            throw UsernameNotFoundException("client id or secret are invalid")
        }
    }

    fun getApiClient(clientId: Long): SystemApiClient? {
        val client = this.apiClientRepo.findById(clientId)
        if (client.isPresent) {
            return client.get()
        }
        return null
    }

    fun getClientDetails(clientId: Long): ApiClientDao? {
        val client = this.apiClientRepo.findById(clientId)
        if (client.isPresent) {
            return ApiClientDao.fromEntity(client.get())
        }
        return null
    }

    suspend fun postToUrl(
        data: Any,
        url: String,
        authMode: String = "basic",
        headerParams: Map<String, String>? = null
    ): Boolean {
        KotlinLogging.logger { }.info("Send to client URL[$url]: ${objectMapper.writeValueAsString(data)}")
        val result = this.getHttpResponseFromPostCall(true, url, authMode, data, authParams = headerParams)
        return result?.status == HttpStatusCode.OK
    }

    private fun buildClient(
        auth: Boolean = false,
        authHeader: String?,
        config: Map<String, String>?,
    ): HttpClient {
        return HttpClient(Apache) {
            expectSuccess = false
            engine {
                followRedirects = false
                customizeClient {
                    setSSLContext(
                        SSLContext.getInstance("TLS")
                            .apply {
                                init(null, arrayOf(DaoService.TrustAllX509TrustManager()), SecureRandom())
                            }
                    )
                    setSSLHostnameVerifier(NoopHostnameVerifier())
                }
            }
            install(JsonFeature) {
                serializer = GsonSerializer()
            }
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }
            when (auth) {
                true -> {
                    when (authHeader) {
                        "basic" -> {
                            install(Auth) {
                                basic {
                                    username = config?.getOrDefault("username", "") ?: ""
                                    password = config?.getOrDefault("password", "") ?: ""
                                    sendWithoutRequest = true
                                }
                            }
                        }
                        "digest" -> {
                            install(Auth) {
                                digest {
                                    username = config?.getOrDefault("username", "") ?: ""
                                    password = config?.getOrDefault("password", "") ?: ""
                                    realm = config?.getOrDefault("realm", "") ?: ""
                                }
                            }

                        }
                        else -> {
                            KotlinLogging.logger { }.info("Invalid Authentication, ignoring")
                        }
                    }
                }
                else -> KotlinLogging.logger { }.info("Authentication NOT REQUIRED")

            }
        }
    }

    fun mapper(excludeNull: Boolean = false): ObjectMapper {
        val mapper = ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true)
        when (excludeNull) {
            true -> mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
        }
        mapper.nodeFactory = JsonNodeFactory.withExactBigDecimals(true)
        mapper.setTimeZone(TimeZone.getDefault())
        return mapper
    }

    suspend fun getHttpResponseFromPostCall(
        auth: Boolean,
        finalUrl: String,
        authMode: String?,
        payload: Any?,
        authParams: Map<String, String>? = null,
        headerParams: Map<String, String>? = null,
    ): HttpResponse? {
        return buildClient(auth, authMode ?: "Bearer", authParams)?.request<HttpResponse>(finalUrl) {
            method = HttpMethod.Post
            headerParams?.forEach { (p, v) -> header(p, v) }
            payload?.let {
                body = TextContent(mapper().writeValueAsString(it), ContentType.Application.Json)
            }

        }.call.response.call.response
    }

    suspend fun publishCallback(data: Any, clientId: String, eventName: String? = null) {
        val client = this.apiClientRepo.findByClientId(clientId)
        var success = false
        if (client.isPresent) {
            val clientDetails = client.get()
            clientDetails.eventsURL?.let {
                val checksum = calculateChecksum(data, clientId)
                val authHeader = when {
                    !clientDetails.callbackUsername.isNullOrEmpty() && !clientDetails.callbackPassword.isNullOrEmpty() -> {
                        val dd = mutableMapOf<String, String>()
                        dd["username"] = clientDetails.callbackUsername.orEmpty()
                        dd["password"] = clientDetails.callbackPassword.orEmpty()
                        dd
                    }
                    else -> null
                }
                val requestData = CallbackResult(data, "CALLBACK", checksum)
                success = this.postToUrl(requestData, it, "basic", authHeader)
                if (!success) {
                    addCallbackResult(requestData, clientId, eventName ?: "ANY", clientDetails.id ?: 0, checksum)
                }
            }
        }
    }

    fun calculateChecksum(data: Any, clientId: String): String {
        val secretKeySpec = SecretKeySpec(clientId.toByteArray(), "HmacSHA256")
        val mac: Mac = Mac.getInstance("HmacSHA256")
        mac.init(secretKeySpec)
        val hex = bytesToHex(mac.doFinal(objectMapper.writeValueAsBytes(data)))
        return hex.toUpperCase().substring(hex.length - 6, hex.length)
    }

    fun publishCallbackEvent(data: Any, clientDetails: SystemApiClient, eventName: String): Boolean {
        var success = false
        runBlocking {
            clientDetails.eventsURL?.let {
                val checksum = calculateChecksum(data, clientDetails.clientId ?: "")
                val authHeader = when {
                    !clientDetails.callbackUsername.isNullOrEmpty() && !clientDetails.callbackPassword.isNullOrEmpty() -> {
                        val dd = mutableMapOf<String, String>()
                        dd["username"] = clientDetails.callbackUsername.orEmpty()
                        dd["password"] = clientDetails.callbackPassword.orEmpty()
                        dd
                    }
                    else -> null
                }
                val requestData = CallbackResult(data, eventName, checksum)
                success = postToUrl(requestData, it, "basic", authHeader)
                if (!success) {
                    addCallbackResult(
                        requestData,
                        clientDetails.clientId ?: "",
                        eventName,
                        clientDetails.id ?: 0,
                        checksum
                    )
                }
            } ?: kotlin.run {
                // Ignore event for endpoints without events url and just succeed
                success = true
            }
        }
        return success
    }

    fun publishCallbackEvent(data: Any, clientId: String, eventName: String): Boolean {
        val client = this.apiClientRepo.findByClientId(clientId)
        var success = false
        if (client.isPresent) {
            val clientDetails = client.get()
            success = this.publishCallbackEvent(data, clientDetails, eventName)
        }
        return success
    }

    fun addCallbackResult(requestData: Any, envId: String, eventName: String, clientId: Long, checksum: String) {
        val events = ApiClientEvents()
        events.agentId = clientId
        events.eventContents = objectMapper.writeValueAsString(requestData)
        events.eventName = eventName
        events.status = 0
        events.createdOn = commonDaoServices.getTimestamp()
        events.createdBy = envId
        events.modifiedOn = commonDaoServices.getTimestamp()
        events.modifiedBy = envId
        apiClientEventRepo.save(events)
    }
}
