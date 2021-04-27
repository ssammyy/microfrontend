package org.kebs.app.kotlin.apollo.api.ports.provided.sms

import mu.KotlinLogging
import org.jasypt.encryption.StringEncryptor
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate


@Service
class SmsServiceImpl(private val applicationMapProperties: ApplicationMapProperties,
                     private val commonDaoServices: CommonDaoServices,
                     private val jasyptStringEncryptor: StringEncryptor): ISmsService {

    final var config =
        commonDaoServices.findIntegrationConfigurationEntity(applicationMapProperties.mapSmsConfigIntegration)

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
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED)
        //Add params
        val map = LinkedMultiValueMap<Any, Any>()
        map.add("txtMobile", phone)
        map.add("txtMsg", message)
        //Build request
        val request = HttpEntity(map, headers)
        //Make request
        val response: ResponseEntity<String> = restTemplate.postForEntity<String>(
            url, request, String::class.java)

        KotlinLogging.logger {  }.info { "Response received: ${response}" }

        if (response.statusCode.is2xxSuccessful) {
            return true
        }
        return false
    }
}