package org.kebs.app.kotlin.apollo.api.flux.routes

import io.netty.handler.ssl.SslContextBuilder
import io.netty.handler.ssl.util.InsecureTrustManagerFactory
import mu.KotlinLogging
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.kebs.app.kotlin.apollo.api.flux.ports.provided.dao.DaoService
import org.kebs.app.kotlin.apollo.common.dto.kappa.requests.ValidationRequestValue
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.netty.http.client.HttpClient

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class KappaPaymentsDaoServiceTest {
    @Autowired
    lateinit var daoService: DaoService

    private val baseUrl = "http://127.0.0.1:8005"
    private val loginUri = "$baseUrl/api/integ/login"
    private val validationUri = "$baseUrl/api/payments/validate/"

    @Test
    fun givenInvalidReferenceNumberReturnRequestAndErrorResponse() {
        val value = ValidationRequestValue("12345")


        val userStr = daoService.mapper().writeValueAsString(value)
        KotlinLogging.logger { }.info(userStr)

        val sslContext = SslContextBuilder
            .forClient()
            .trustManager(InsecureTrustManagerFactory.INSTANCE)
            .build()
        HttpClient.create().secure { t -> t.sslContext(sslContext) }


        val client = WebTestClient.bindToServer()
            .baseUrl(baseUrl)
            .build()

        val response = client
            .post()
            .uri(validationUri)
            .header(HttpHeaders.AUTHORIZATION,
                "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ2bXVyaXVraSIsImlzcyI6ImtpbXMua2Vicy5vcmciLCJpYXQiOjE2MDkxNjA2OTgsImV4cCI6MTYwOTE2MTI5OCwiYWRkcmVzcyI6IjEyNy4wLjAuMSIsInVzZXJBZ2VudCI6IltjdXJsLzcuNzEuMV0iLCJyb2xlcyI6IkFVVEhPUklUSUVTX1JFQUQsQVVUSE9SSVRJRVNfV1JJVEUsQ0RfU1VQRVJWSVNPUl9ERUxFVEUsQ0RfU1VQRVJWSVNPUl9NT0RJRlksQ0RfU1VQRVJWSVNPUl9SRUFELE1TX0NPTVBMQUlOVF9BQ0NFUFQsTVNfSE9EX01PRElGWSxNU19IT0RfUkVBRCxQVk9DX0FQUExJQ0FUSU9OX1JFQUQsVVNFUl9NQU5BR0VNRU5UX0RFTEVURSxVU0VSX01BTkFHRU1FTlRfTU9ESUZZLFVTRVJfTUFOQUdFTUVOVF9SRUFEIn0.10l9Yy4edkKEwuOM5kPf1ddoMGaXW-drkMXCdpbK_XaFbK7UQ70bovvY2ObYMTSteYeVA_aDwayoTxWCZs6zxA")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(userStr)
            .exchange()
            .expectStatus().isBadRequest
            .expectBody()
            .returnResult()


        response.responseBody?.let { KotlinLogging.logger { }.info(it.decodeToString()) }


    }
}