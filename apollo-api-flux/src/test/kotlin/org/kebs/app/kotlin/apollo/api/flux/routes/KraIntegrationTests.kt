package org.kebs.app.kotlin.apollo.api.flux.routes

import io.netty.handler.ssl.SslContextBuilder
import io.netty.handler.ssl.util.InsecureTrustManagerFactory
import mu.KotlinLogging
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.kebs.app.kotlin.apollo.api.flux.ports.provided.dao.DaoService
import org.kebs.app.kotlin.apollo.common.dto.kra.request.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.netty.http.client.HttpClient
import java.math.BigDecimal
import java.sql.Date
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDate

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class KraIntegrationTests {
    @Autowired
    lateinit var daoService: DaoService

    private val baseUrl = "http://127.0.0.1:8005"
    private val loginUri = "$baseUrl/api/integ/login"
    private val validationUri = "$baseUrl/api/kra/receiveSL2Payment"

    @Test
    fun givenSl2PayloadProcessTheRequest() {
        val sL2PaymentReceiveSL2PaymentRequest = ReceiveSL2PaymentRequest()
        val header = ReceiveSL2PaymentHeader()
        header.bank = "test"
        header.manufacturerName = "test"
        header.paymentSlipNo = "test"
        header.paymentSlipNo = "test"
        header.paymentType = "CASH"
        header.paymentSlipDate = Date(java.util.Date().time)
        header.totalDeclAmt = BigDecimal("110000")
        header.totalPenaltyAmt = BigDecimal("30000")
        header.totalPaymentAmt = BigDecimal("80000")
        header.entryNo = "12"
        header.kraPin = "1234567"
        header.bankRefNo = "1234567"
        header.paymentDate = Date(java.util.Date().time)
        sL2PaymentReceiveSL2PaymentRequest.header = header
        sL2PaymentReceiveSL2PaymentRequest.hash = "123445"
        sL2PaymentReceiveSL2PaymentRequest.loginId = "vmuriuki"
        sL2PaymentReceiveSL2PaymentRequest.password = "password"
        sL2PaymentReceiveSL2PaymentRequest.transmissionDate = Timestamp.from(Instant.now())
        val detail = ReceiveSL2PaymentDetails()
        val paymentDeclarations = mutableListOf<ReceiveSL2PaymentDeclaration>()
        repeat((0..3).count()) {
            val paymentDeclaration = ReceiveSL2PaymentDeclaration()
            paymentDeclaration.commodityType = "FOOD"
            paymentDeclaration.periodFrom = Date.valueOf(LocalDate.of(2021, 2, 1))
            paymentDeclaration.periodTo = Date.valueOf(LocalDate.of(2021, 2, 28))
            paymentDeclaration.qtyManf = BigDecimal("5")
            paymentDeclaration.exFactVal = BigDecimal("50000")
            paymentDeclaration.levyPaid = BigDecimal("20000")
            paymentDeclarations.add(paymentDeclaration)
        }
        val penaltyDeclarations = mutableListOf<ReceiveSL2PaymentPenalty>()
        repeat((0..2).count()) {
            val penaltyDeclaration = ReceiveSL2PaymentPenalty()
            penaltyDeclaration.periodFrom = Date.valueOf(LocalDate.of(2021, 2, 1))
            penaltyDeclaration.periodTo = Date.valueOf(LocalDate.of(2021, 2, 28))
            penaltyDeclaration.penaltyOrderNo = "test"
            penaltyDeclaration.penaltyPaid = BigDecimal("10000")
            penaltyDeclarations.add(penaltyDeclaration)
        }

        detail.declaration = paymentDeclarations
        detail.penalty = penaltyDeclarations
        sL2PaymentReceiveSL2PaymentRequest.details = detail

        val payload = daoService.mapper().writeValueAsString(sL2PaymentReceiveSL2PaymentRequest)

        KotlinLogging.logger { }.info(payload)

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
            .header(
                HttpHeaders.AUTHORIZATION,
                "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ2bXVyaXVraSIsImlzcyI6ImtpbXMua2Vicy5vcmciLCJpYXQiOjE2MTQ2OTA0MTMsImV4cCI6MTYxNDY5NjQxMywiYWRkcmVzcyI6IjEyNy4wLjAuMSIsInVzZXJBZ2VudCI6IltBcGFjaGUtSHR0cENsaWVudC80LjUuMTMgKEphdmEvMTEuMC4xMCldIiwicm9sZXMiOiJBVVRIT1JJVElFU19SRUFELEFVVEhPUklUSUVTX1ZJRVcsQVVUSE9SSVRJRVNfV1JJVEUsQVVUSE9SSVRZX0xJU1QsQVVUSE9SSVRZX1dSSVRFLENEX1NVUEVSVklTT1JfREVMRVRFLENEX1NVUEVSVklTT1JfTU9ESUZZLENEX1NVUEVSVklTT1JfUkVBRCxDT1VOVElFU19MSVNULENPVU5USUVTX1ZJRVcsQ09VTlRJRVNfV1JJVEUsREVQQVJUTUVOVFNfTElTVCxERVBBUlRNRU5UU19WSUVXLERFUEFSVE1FTlRTX1dSSVRFLERFU0lHTkFUSU9OU19MSVNULERFU0lHTkFUSU9OU19WSUVXLERFU0lHTkFUSU9OU19XUklURSxESVJFQ1RPUkFURVNfTElTVCxESVJFQ1RPUkFURVNfVklFVyxESVJFQ1RPUkFURVNfV1JJVEUsRElWSVNJT05TX0xJU1QsRElWSVNJT05TX1ZJRVcsRElWSVNJT05TX1dSSVRFLE1TX0NPTVBMQUlOVF9BQ0NFUFQsTVNfSE9EX01PRElGWSxNU19IT0RfUkVBRCxQRVJNSVRfQVBQTElDQVRJT04sUFZPQ19BUFBMSUNBVElPTl9SRUFELFJCQUNfQVNTSUdOX0FVVEhPUklaQVRJT04sUkJBQ19BU1NJR05fUk9MRSxSQkFDX1JFVk9LRV9BVVRIT1JJWkFUSU9OLFJCQUNfUkVWT0tFX1JPTEUsUkJBQ19ST0xFX0FVVEhPUklUSUVTX0xJU1QsUkJBQ19ST0xFX0FVVEhPUklUSUVTX1ZJRVcsUkJBQ19VU0VSX1JPTEVTX1ZJRVcsUkVHSU9OU19MSVNULFJFR0lPTlNfVklFVyxSRUdJT05TX1dSSVRFLFJPTEVTX0xJU1QsUk9MRVNfVklFVyxST0xFU19XUklURSxTRUNUSU9OU19MSVNULFNFQ1RJT05TX1ZJRVcsU0VDVElPTlNfV1JJVEUsU0xfQVBQUk9WRV9WSVNJVF9SRVBPUlQsU0xfTUFOVUZBQ1RVUkVSU19WSUVXLFNVQlNFQ1RJT05TX0wxX0xJU1QsU1VCU0VDVElPTlNfTDFfVklFVyxTVUJTRUNUSU9OU19MMV9XUklURSxTVUJTRUNUSU9OU19MMl9MSVNULFNVQlNFQ1RJT05TX0wyX1ZJRVcsU1VCU0VDVElPTlNfTDJfV1JJVEUsU1VCX1JFR0lPTlNfTElTVCxTVUJfUkVHSU9OU19WSUVXLFNVQl9SRUdJT05TX1dSSVRFLFNZU0FETUlOX1ZJRVcsVElUTEVTX0xJU1QsVElUTEVTX1ZJRVcsVElUTEVTX1dSSVRFLFRPV05TX0xJU1QsVE9XTlNfVklFVyxUT1dOU19XUklURSxVU0VSU19MSVNULFVTRVJTX1ZJRVcsVVNFUlNfV1JJVEUsVVNFUl9NQU5BR0VNRU5UX0RFTEVURSxVU0VSX01BTkFHRU1FTlRfTU9ESUZZLFVTRVJfTUFOQUdFTUVOVF9SRUFELFVTRVJfVFlQRVNfTElTVCxVU0VSX1RZUEVTX1ZJRVcsVVNFUl9UWVBFU19XUklURSJ9.dEpeX6rJduKt8i9fn6Kks5AiNhHsIKJI7-RZvvtBhY9MHNbKkg3s2G2J5EdX-ozNYxnk7yhwDE3b02fuLTLsgQ"
            )
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(payload)
            .exchange()
            .expectStatus().isBadRequest
            .expectBody()
            .returnResult()


        response.responseBody?.let { KotlinLogging.logger { }.info(it.decodeToString()) }
    }
}
