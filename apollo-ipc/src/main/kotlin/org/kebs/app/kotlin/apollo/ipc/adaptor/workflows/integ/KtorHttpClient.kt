/*
 *
 *  *
 *  *
 *  *  *    Copyright (c) ${YEAR}.   BSK Global Technologies
 *  *  *
 *  *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *    you may not use this file except in compliance with the License.
 *  *  *    You may obtain a copy of the License at
 *  *  *
 *  *  *       http://www.apache.org/licenses/LICENSE-2.0
 *  *  *
 *  *  *    Unless required by applicable law or agreed to in writing, software
 *  *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *   See the License for the specific language governing permissions and
 *  *  *   limitations under the License.
 *  *
 *
 */

package org.kebs.app.kotlin.apollo.ipc.adaptor.workflows.integ

import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.features.json.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import mu.KotlinLogging
import org.apache.http.conn.ssl.NoopHostnameVerifier
import org.apache.http.conn.ssl.TrustSelfSignedStrategy
import org.apache.http.ssl.SSLContextBuilder
import org.jasypt.encryption.StringEncryptor
import org.kebs.app.kotlin.apollo.store.model.IntegrationConfigurationEntity
import org.kebs.app.kotlin.apollo.store.model.ServiceMapsEntity
import org.kebs.app.kotlin.apollo.store.model.WorkflowTransactionsEntity
import org.kebs.app.kotlin.apollo.store.repo.IIntegrationConfigurationRepository
import org.kebs.app.kotlin.apollo.store.repo.IServiceMapsWorkflowsRepository
import org.springframework.stereotype.Component

@Component
class KtorHttpClient(
    private val serviceMapsWorkflowsRepository: IServiceMapsWorkflowsRepository,
    private val integrationConfigurationRepository: IIntegrationConfigurationRepository,
    private val jasyptStringEncryptor: StringEncryptor
) {

    suspend fun sendRequests(
        config: IntegrationConfigurationEntity?, payload: Any? = null, log: WorkflowTransactionsEntity?, mapsEntity: ServiceMapsEntity?, bodyParams: MutableMap<String, String>? = null,
        headerParams: MutableMap<String, String>? = null,
    ) = coroutineScope {
        try {
            val deferred = log?.methodId?.id?.let { workflowId ->
                buildClient(workflowId)?.let { client ->
                    config?.url?.let { url ->
                        async {
                            client.request<HttpResponse>(url) {
                                method = HttpMethod.parse(config.clientMethod)
                                headerParams?.forEach { (p, v) -> header(p, v) }
                                bodyParams?.forEach { (p, v) -> parameter(p, v) }
                                payload?.let { p -> body = p }
//                                log.integrationRequest = build().toString()
                            }
                        }

                    }
                }
            }
            val httpResponse = deferred?.await()
            KotlinLogging.logger {  }.info("Response: $httpResponse")
            log?.integrationResponse = httpResponse.toString()
            when (httpResponse?.status?.value) {
                in (config?.okLower!!..config.okUpper) -> {
                    log?.integrationResponse = httpResponse?.readText()
                    KotlinLogging.logger { }.info { "received response $httpResponse, code= ${httpResponse?.status?.value}, status= ${httpResponse?.status}" }
                    log?.responseMessage = "response $httpResponse, code= ${httpResponse?.status?.value}, status= ${httpResponse?.status}"
                    log?.responseStatus = mapsEntity?.successStatusCode
                    log?.transactionStatus = mapsEntity?.successStatus!!
                }
                else -> {
                    KotlinLogging.logger { }.error { "Received code= ${httpResponse?.status?.value}, code= ${httpResponse?.status}" }
                    log?.responseMessage = "Received response= $httpResponse code= ${httpResponse?.status?.value}, code= ${httpResponse?.status}"
                    log?.responseStatus = mapsEntity?.failedStatusCode
                    log?.transactionStatus = mapsEntity?.failedStatus!!


                }
            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error { e }
            log?.responseMessage = e.message
            log?.responseStatus = mapsEntity?.exceptionStatusCode!!
            log?.transactionStatus = mapsEntity.exceptionStatus
        }
        return@coroutineScope log
    }

    fun buildClient(workFlowId: Long): HttpClient? {

        var client: HttpClient? = null

        serviceMapsWorkflowsRepository.findById(workFlowId)
            .ifPresent { workflow ->
                workflow.serviceMapsId?.let { map ->
                    integrationConfigurationRepository.findByWorkflowId(workflow.id)
                        ?.let { config ->
                            client = HttpClient(Apache)
                            {
                                engine {
                                    followRedirects = config.followRedirects > map.inactiveStatus
                                    socketTimeout = config.socketTimeout
                                    connectTimeout = config.connectTimeout
                                    connectionRequestTimeout = config.connectionRequestTimeout
                                    customizeClient {
                                        setMaxConnPerRoute(config.maxConnPerRoute)
                                        setMaxConnTotal(config.maxConnTotal)
                                        setSSLContext(
                                            SSLContextBuilder.create()
                                                .loadTrustMaterial(TrustSelfSignedStrategy())
                                                .build()
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

                                when (config.clientAuthentication) {
                                    map.basicAuthenticationValue -> {
                                        install(Auth) {
                                            basic {

                                                username = jasyptStringEncryptor.decrypt(config.username)
                                                password = jasyptStringEncryptor.decrypt(config.password)
                                                sendWithoutRequest = true
                                            }
                                        }


                                    }
                                    map.digestAuthenticationValue -> {
                                        install(Auth) {
                                            digest {
                                                username = jasyptStringEncryptor.decrypt(config.username)
                                                password = jasyptStringEncryptor.decrypt(config.password)
                                                realm = config.clientAuthenticationRealm
//                                                sendWithoutRequest = true
                                            }
                                        }

                                    }
                                    else -> {
                                        KotlinLogging.logger { }.error { "Invalid Authentication, ignoring" }
                                    }
                                }

                            }

                        }


                }
            }


        return client
    }
}