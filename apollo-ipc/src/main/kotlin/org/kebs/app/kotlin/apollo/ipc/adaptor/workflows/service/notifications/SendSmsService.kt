/*
 *
 * $$$$$$$\   $$$$$$\  $$\   $$\        $$$$$$\  $$\       $$$$$$\  $$$$$$$\   $$$$$$\  $$\
 * $$  __$$\ $$  __$$\ $$ | $$  |      $$  __$$\ $$ |     $$  __$$\ $$  __$$\ $$  __$$\ $$ |
 * $$ |  $$ |$$ /  \__|$$ |$$  /       $$ /  \__|$$ |     $$ /  $$ |$$ |  $$ |$$ /  $$ |$$ |
 * $$$$$$$\ |\$$$$$\  $$$$$  /        $$ |$$$$\ $$ |     $$ |  $$ |$$$$$$$\ |$$$$$$$$ |$$ |
 * $$  __$$\  \____$$\ $$  $$<         $$ |\_$$ |$$ |     $$ |  $$ |$$  __$$\ $$  __$$ |$$ |
 * $$ |  $$ |$$\   $$ |$$ |\$\        $$ |  $$ |$$ |     $$ |  $$ |$$ |  $$ |$$ |  $$ |$$ |
 * $$$$$$$  |\$$$$$  |$$ | \$\       \$$$$$  |$$$$$$$$\ $$$$$$  |$$$$$$$  |$$ |  $$ |$$$$$$$$\
 * \_______/  \______/ \__|  \__|       \______/ \________|\______/ \_______/ \__|  \__|\________|
 * $$$$$$$$\ $$$$$$$$\  $$$$$$\  $$\   $$\ $$\   $$\  $$$$$$\  $$\       $$$$$$\   $$$$$$\  $$$$$$\ $$$$$$$$\  $$$$$$\
 * \__$$  __|$$  _____|$$  __$$\ $$ |  $$ |$$$\  $$ |$$  __$$\ $$ |     $$  __$$\ $$  __$$\ \_$$  _|$$  _____|$$  __$$\
 *    $$ |   $$ |      $$ /  \__|$$ |  $$ |$$$$\ $$ |$$ /  $$ |$$ |     $$ /  $$ |$$ /  \__|  $$ |  $$ |      $$ /  \__|
 *    $$ |   $$$$$\    $$ |      $$$$$$$$ |$$ $$\$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |$$$$\   $$ |  $$$$$\    \$$$$$\
 *    $$ |   $$  __|   $$ |      $$  __$$ |$$ \$$$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |\_$$ |  $$ |  $$  __|    \____$$\
 *    $$ |   $$ |      $$ |  $$\ $$ |  $$ |$$ |\$$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |  $$ |  $$ |  $$ |      $$\   $$ |
 *    $$ |   $$$$$$$$\ \$$$$$  |$$ |  $$ |$$ | \$ | $$$$$$  |$$$$$$$$\ $$$$$$  |\$$$$$  |$$$$$$\ $$$$$$$$\ \$$$$$  |
 *    \__|   \________| \______/ \__|  \__|\__|  \__| \______/ \________|\______/  \______/ \______|\________| \______/
 *
 *
 *
 *
 *
 *   Copyright (c) 2020.  BSK
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package org.kebs.app.kotlin.apollo.ipc.adaptor.workflows.service.notifications

import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.config.properties.notifs.NotifsProperties
import org.kebs.app.kotlin.apollo.ipc.adaptor.workflows.integ.KtorHttpClient
import org.kebs.app.kotlin.apollo.ipc.dto.MessageParams
import org.kebs.app.kotlin.apollo.ipc.dto.MtSmsRequest
import org.kebs.app.kotlin.apollo.store.model.NotificationsBufferEntity
import org.kebs.app.kotlin.apollo.store.model.ServiceRequestsEntity
import org.kebs.app.kotlin.apollo.store.model.WorkflowTransactionsEntity
import org.kebs.app.kotlin.apollo.store.repo.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.Instant
import java.util.*

@Service
class SendSmsService(
        private val ktorHttpClient: KtorHttpClient,
        private val serviceMapsWorkflowsRepository: IServiceMapsWorkflowsRepository,
        private val integrationConfigurationRepository: IIntegrationConfigurationRepository,
        private val serviceMapsRepository: IServiceMapsRepository,
        private val notifsProperties: NotifsProperties,
        private val utils: NotificationUtils,
        private val serviceRequestRepo: IServiceRequestsRepository,
        private val notificationsRepo: INotificationsRepository,
        private val buffersRepo: INotificationsBufferRepository
) {
    fun sendSmsUsingKtorClient(buffer: NotificationsBufferEntity?) {
        var log = WorkflowTransactionsEntity()


        serviceRequestRepo.findByIdOrNull(buffer?.serviceRequestId)
                ?.let { request ->
                    with(log) {
                        transactionDate = java.sql.Date(Date().time)
                        transactionStartDate = Timestamp.from(Instant.now())
                        serviceRequests = request
                        retried = 0
                        request.serviceMapsId?.initStatus?.let { transactionStatus = it }
                        createdBy = buffer?.createdBy
                        createdOn = Timestamp.from(Instant.now())
                    }
                    notificationsRepo.findByIdOrNull(buffer?.notificationId)
                            ?.let { notif ->
                                integrationConfigurationRepository.findByIdOrNull(notif.notificationType?.integrationId)
                                        ?.let { config ->
                                            try {
                                                log.currentSequence = request.currentStage
                                                log.executionOrder = request.currentSequence
                                                log.transactionReference = "${request.transactionReference}_${log.currentSequence}_${log.executionOrder}"
                                                log.integrationId = config
                                                val messageParams = MessageParams()
                                                buffer?.recipient?.let { messageParams.number = it }
                                                buffer?.messageBody?.let { messageParams.text = it }

                                                val smsRequest = MtSmsRequest()
                                                smsRequest.senderId = config.sender
                                                smsRequest.clientId = config.username
                                                smsRequest.apiKey = config.password
                                                smsRequest.messageParameters = listOf(messageParams)
                                                val headerParams: MutableMap<String, String> = mutableMapOf()
                                                headerParams[notifsProperties.notifsContentTypeKey] = notifsProperties.notifsContentTypeValue
                                                headerParams[notifsProperties.notifsContentAcessKey] = config.varField1!!
                                                val serviceMaps = serviceMapsRepository.findByIdAndStatus(notifsProperties.notifsServiceMapsId.toInt(), notifsProperties.notifsServiceMapsActiveStatus.toInt())!!
                                                log = runBlocking { ktorHttpClient.sendRequests(config, smsRequest, log, serviceMaps, null, headerParams)!! }

                                                KotlinLogging.logger { }.info("Response payLoad: ${log.integrationResponse} ")

                                                when (log.responseStatus) {
                                                    serviceMaps.successStatusCode -> buffer?.status = serviceMaps.successStatus
                                                    serviceMaps.failedStatusCode -> buffer?.status = serviceMaps.failedStatus
                                                }

                                            } catch (e: Exception) {
                                                KotlinLogging.logger { }.error(e.message)
                                                KotlinLogging.logger { }.debug(e.message, e)
                                                buffer?.responseMessage = e.message
                                                buffer?.responseStatus = request.serviceMapsId?.exceptionStatusCode
                                                buffer?.status = request.serviceMapsId?.exceptionStatus

                                            }


                                        }
                            }

                }



        buffer?.let { buffersRepo.save(it) }


    }

    fun sendSmsUsingKtorClient(request: ServiceRequestsEntity, notificationsBuffer: NotificationsBufferEntity) {
        val workFlowId = notifsProperties.notifsWorkflowId.toLongOrNull()
        var log = WorkflowTransactionsEntity()
        utils.stageSmsNotification(notificationsBuffer, request)
        try {
            with(log) {
                transactionDate = java.sql.Date(Date().time)
                transactionStartDate = Timestamp.from(Instant.now())
                serviceRequests = request
                retried = 0
                transactionStatus = 0
                createdBy = request.transactionReference
                createdOn = Timestamp.from(Instant.now())
            }

            serviceMapsWorkflowsRepository.findByIdOrNull(workFlowId!!)
                    ?.let { workFlow ->
                        log.methodId = workFlow
                        integrationConfigurationRepository.findByWorkflowId(workFlow.id)
                                ?.let { config ->
                                    log.currentSequence = request.currentStage
                                    log.executionOrder = request.currentSequence
                                    log.transactionReference = "${request.transactionReference}_${log.currentSequence}_${log.executionOrder}"
                                    log.integrationId = config
                                    val messageParams = MessageParams()
                                    messageParams.number = notificationsBuffer.recipient!!
                                    messageParams.text = notificationsBuffer.messageBody!!
                                    val smsRequest = MtSmsRequest()
                                    smsRequest.senderId = config.sender!!
                                    smsRequest.clientId = config.username
                                    smsRequest.apiKey = config.password
                                    smsRequest.messageParameters = listOf(messageParams)
                                    val headerParams: MutableMap<String, String> = mutableMapOf()
                                    headerParams[notifsProperties.notifsContentTypeKey] = notifsProperties.notifsContentTypeValue
                                    headerParams[notifsProperties.notifsContentAcessKey] = config.varField1!!
                                    val serviceMaps = serviceMapsRepository.findByIdAndStatus(notifsProperties.notifsServiceMapsId.toInt(), notifsProperties.notifsServiceMapsActiveStatus.toInt())!!
                                    log = runBlocking { ktorHttpClient.sendRequests(config, smsRequest, log, serviceMaps, null, headerParams)!! }

                                    KotlinLogging.logger { }.info("Response payLoad: ${log.integrationResponse} ")

                                    when (log.responseStatus) {
                                        serviceMaps.successStatusCode -> notificationsBuffer.status = serviceMaps.successStatus
                                        serviceMaps.failedStatusCode -> notificationsBuffer.status = serviceMaps.failedStatus
                                    }


                                }

                    }
        } catch (e: Exception) {
            e.printStackTrace()
            throw RuntimeException("Could not send Sms. Exception: ${e.message!!}")
        }
    }
}