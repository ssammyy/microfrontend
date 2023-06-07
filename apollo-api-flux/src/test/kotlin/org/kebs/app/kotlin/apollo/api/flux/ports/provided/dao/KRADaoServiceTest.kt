package org.kebs.app.kotlin.apollo.api.flux.ports.provided.dao

import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import org.jasypt.encryption.StringEncryptor
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.kebs.app.kotlin.apollo.common.dto.brs.response.BrsLookUpResponse
import org.kebs.app.kotlin.apollo.common.dto.kappa.requests.RequestHeader
import org.kebs.app.kotlin.apollo.common.dto.kappa.requests.ValidationRequest
import org.kebs.app.kotlin.apollo.common.dto.kappa.requests.ValidationRequestBody
import org.kebs.app.kotlin.apollo.common.dto.kappa.response.ValidationResponse
import org.kebs.app.kotlin.apollo.config.properties.jpa.JpaConnectionProperties
import org.kebs.app.kotlin.apollo.store.repo.IIntegrationConfigurationRepository
import org.kebs.app.kotlin.apollo.store.repo.IJwtTokensRegisterRepository
import org.kebs.app.kotlin.apollo.store.repo.IWorkflowTransactionsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.sql.Timestamp
import java.time.Instant


@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class KRADaoServiceTest {

    @Autowired
    lateinit var daoService: DaoService

    @Autowired
    lateinit var configurationRepository: IIntegrationConfigurationRepository

    @Autowired
    lateinit var tokenRegistryRepo: IJwtTokensRegisterRepository

    @Autowired
    lateinit var logsRepo: IWorkflowTransactionsRepository


    @Autowired
    lateinit var jpaConnectionProperties: JpaConnectionProperties

    @Autowired
    lateinit var jasyptStringEncryptor: StringEncryptor


    @Test
    fun readValuesTest() {
        KotlinLogging.logger { }.info("${jpaConnectionProperties.cachePrepStmts} ${jpaConnectionProperties.prepStmtCacheSize}")

    }


    @Test
    fun retrieveDataTest() {
        tokenRegistryRepo.findByIdOrNull(222L)
            ?.let { t ->
                KotlinLogging.logger { }.info("${t.userName}")
            }

    }

    @Test
    fun postApiCallTest() {
        configurationRepository.findByIdOrNull(22L)
            ?.let { config ->
                runBlocking {
                    config.url?.let { url ->
                        val header = RequestHeader("KEBS", "xgg33", jasyptStringEncryptor.decrypt(config.username), jasyptStringEncryptor.decrypt(config.password))
                        val request = ValidationRequestBody("EPA/1140/13", "2100082")

                        val validationRequest = ValidationRequest(header, request)
                        val transactionRef = daoService.generateTransactionReference()
                        val log = daoService.createTransactionLog(0, transactionRef)
                        log.integrationRequest = daoService.mapper().writeValueAsString(validationRequest)
                        val resp = daoService.getHttpResponseFromPostCall(url, null, validationRequest, config, null, null)
                        val data = daoService.processResponses<ValidationResponse>(resp, log, url, config)
                        KotlinLogging.logger { }.info(data.second?.response?.institutionName)
                        logsRepo.save(data.first)


                    }


                }
            }


    }

    @Test
    fun getApiCallTest() {
        configurationRepository.findByIdOrNull(3L)
            ?.let { config ->
                config.createdOn = Timestamp.from(Instant.now())
                config.modifiedOn = Timestamp.from(Instant.now())
                configurationRepository.save(config)

                runBlocking {
                    config.url?.let { url ->

                        val log = daoService.createTransactionLog(0, "test")
                        val params = mapOf(Pair("registration_number", "PVT-DLU36K7"))
                        log.integrationRequest = "$params"
                        val resp = daoService.getHttpResponseFromGetCall(true, url, config, null, params, null)
                        val data = daoService.processResponses<BrsLookUpResponse>(resp, log, url, config)
                        KotlinLogging.logger { }.info("${data.second?.count} ${data.second?.records?.get(0)?.partners?.get(0)?.name}")

                        logsRepo.save(data.first)
                    }
                }
            }

    }


}




