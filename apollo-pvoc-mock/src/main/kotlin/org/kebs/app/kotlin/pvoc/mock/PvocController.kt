package org.kebs.app.kotlin.pvoc.mock

import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/callback")
class PvocController(
    private val mapper: ObjectMapper
) {
    @PostMapping("all")
    fun idfDocumentCallback(@RequestBody data: Map<String, Any>): Map<String, Any> {
        KotlinLogging.logger { }.info("Event Documents: ${mapper.writeValueAsString(data)}")
        val result = mutableMapOf<String, Any>()
        result["response_code"] = "00"
        result["message"] = "Test message"
        return result
    }
}