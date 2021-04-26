package org.kebs.app.kotlin.apollo.api.ports.provided.sms

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate


@Service
class SmsServiceImpl(private val applicationMapProperties: ApplicationMapProperties): ISmsService {

    /*
    Replace with values from DB
     */
    val url = "http://10.10.0.146/sms_kernel/bulk_smssend.php"
    val username = "test"
    val password = "test"

    override fun sendSms(phone: String, message: String): Boolean {
        val restTemplate = RestTemplate()
        //Create Header
        val headers = HttpHeaders()
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