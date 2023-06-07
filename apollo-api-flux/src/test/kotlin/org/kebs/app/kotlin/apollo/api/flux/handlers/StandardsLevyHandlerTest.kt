package org.kebs.app.kotlin.apollo.api.flux.handlers

import mu.KotlinLogging
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class StandardsLevyHandlerTest {
    private val baseUrl = "http://127.0.0.1:8005"
    private val validationUri = "$baseUrl/api/kra/send/entryNumber/2/start"

    @Test
    fun givenApiEndPoint_ThenOk() {
        val client = WebTestClient.bindToServer()
            .baseUrl(baseUrl)
            .build()
        val response = client
            .post()
            .uri(validationUri)
            .header(HttpHeaders.AUTHORIZATION,
                "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ2bXVyaXVraSIsImlzcyI6ImtpbXMua2Vicy5vcmciLCJpYXQiOjE2MDk0Mzc4MTYsImV4cCI6MTYwOTQ0MzgxNiwiYWRkcmVzcyI6IjEyNy4wLjAuMSIsInVzZXJBZ2VudCI6IltjdXJsLzcuNzEuMV0iLCJyb2xlcyI6IkFVVEhPUklUSUVTX1JFQUQsQVVUSE9SSVRJRVNfV1JJVEUsQ0RfU1VQRVJWSVNPUl9ERUxFVEUsQ0RfU1VQRVJWSVNPUl9NT0RJRlksQ0RfU1VQRVJWSVNPUl9SRUFELE1TX0NPTVBMQUlOVF9BQ0NFUFQsTVNfSE9EX01PRElGWSxNU19IT0RfUkVBRCxQVk9DX0FQUExJQ0FUSU9OX1JFQUQsVVNFUl9NQU5BR0VNRU5UX0RFTEVURSxVU0VSX01BTkFHRU1FTlRfTU9ESUZZLFVTRVJfTUFOQUdFTUVOVF9SRUFEIn0.jIEZIZ6DJCDfzXne0cwNSeWnZIZZKdQ-76UabA5toUJL-6yWtzsQyjNjZuJ2v5dEDTWic9IWVHv5Wp5wAOjEyw")
//            .contentType(MediaType.APPLICATION_JSON)
//            .bodyValue(userStr)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .returnResult()


        response.responseBody?.let { KotlinLogging.logger { }.info(it.decodeToString()) }

    }
}