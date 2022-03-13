package org.kebs.app.kotlin.apollo.api.ports.provided.sms

import mu.KotlinLogging
import org.jasypt.encryption.StringEncryptor
import org.kebs.app.kotlin.apollo.api.errors.GenericRuntimeException
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.repo.IIntegrationConfigurationRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.UnknownHttpStatusCodeException

@Service
class SmsServiceImpl(
    private val applicationMapProperties: ApplicationMapProperties,
    private val jasyptStringEncryptor: StringEncryptor,
    private val configurationRepository: IIntegrationConfigurationRepository
) : ISmsService {

    final var config = configurationRepository.findByIdOrNull(applicationMapProperties.mapSmsConfigIntegration)
        ?: throw ExpectedDataNotFound("Configuration With the following ID ${applicationMapProperties.mapSmsConfigIntegration}, does not exist")


    val url = config.url.toString()
    val username = jasyptStringEncryptor.decrypt(config.username)
    val password = jasyptStringEncryptor.decrypt(config.password)

    override fun sendSms(phone: String, message: String): Boolean {
        val restTemplate = RestTemplate()
        //Create Header
        val username = username
        val password = password
        val headers = HttpHeaders()
        headers.setBasicAuth(username, password)
        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED
        //Add params
        val map = LinkedMultiValueMap<Any, Any>()
        map.add("txtMobile", phone)
        map.add("txtMsg", message)
        //Build request
        val request = HttpEntity(map, headers)
        //Make request
        try {
            KotlinLogging.logger { }.debug("SMS: $message")
            val response: ResponseEntity<String> = restTemplate.postForEntity<String>(
                url, request, String::class.java)
            KotlinLogging.logger {  }.info { "Response received: ${response}" }
            return response.statusCode.is2xxSuccessful
        } catch (exception: HttpClientErrorException) {
            KotlinLogging.logger {  }.error { "HttpClientErrorException: ${exception}" }
            throw GenericRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Could not send OTP, try again later")
        } catch (exception: HttpServerErrorException) {
            KotlinLogging.logger {  }.error { "HttpServerErrorException: ${exception}" }
            throw GenericRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Could not send OTP, try again later")
        } catch (exception: UnknownHttpStatusCodeException) {
            KotlinLogging.logger {  }.error { "UnknownHttpStatusCodeException: ${exception}" }
            throw GenericRuntimeException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Could not send OTP, try again later")
        }
    }
}