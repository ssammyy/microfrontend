package org.kebs.app.kotlin.apollo.api.service


import com.fasterxml.jackson.databind.ObjectMapper
import com.hazelcast.internal.util.QuickMath.bytesToHex
import mu.KotlinLogging
import org.apache.commons.codec.binary.Base32
import org.apache.commons.net.util.Base64
import org.kebs.app.kotlin.apollo.api.payload.ApiClientForm
import org.kebs.app.kotlin.apollo.api.payload.ApiClientUpdateForm
import org.kebs.app.kotlin.apollo.api.payload.ApiResponseModel
import org.kebs.app.kotlin.apollo.api.payload.ResponseCodes
import org.kebs.app.kotlin.apollo.api.payload.response.ApiClientDao
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.store.model.external.SystemApiClient
import org.kebs.app.kotlin.apollo.store.repo.external.ApiClientRepo
import org.springframework.data.domain.PageRequest
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.security.SecureRandom
import java.sql.Timestamp
import java.time.Instant
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec


data class CallbackResult(
        var data: Any, var type: String, var checksum: String
)

@Service
class ApiClientService(
        val apiClientRepo: ApiClientRepo,
        val passwordEncoder: PasswordEncoder,
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

    fun postToUrl(data: Any, url: String) {
        KotlinLogging.logger { }.info("Send to client URL[$url]: ${objectMapper.writeValueAsString(data)}")
    }

    fun publishCallback(data: Any, clientId: String) {
        val client = this.apiClientRepo.findByClientId(clientId)
        if (client.isPresent) {
            val clientDetails = client.get()
            clientDetails.callbackURL?.let {
                val checksum = calculateChecksum(data, clientId)
                this.postToUrl(CallbackResult(data, "CALLBACK", checksum), it)
                this.postToUrl(data, it)
            }
        }
    }

    fun calculateChecksum(data: Any, clientId: String): String {
        val secretKeySpec = SecretKeySpec(clientId.toByteArray(), "HmacSHA256")
        val mac: Mac = Mac.getInstance("HmacSHA256")
        mac.init(secretKeySpec)
        return bytesToHex(mac.doFinal(objectMapper.writeValueAsBytes(data)))
    }

    fun publishCallbackEvent(data: Any, clientId: String, eventName: String) {
        val client = this.apiClientRepo.findByClientId(clientId)
        if (client.isPresent) {
            val clientDetails = client.get()
            clientDetails.eventsURL?.let {
                val checksum = calculateChecksum(data, clientId)
                this.postToUrl(CallbackResult(data, eventName, checksum), it)
            }
        }
    }
}
