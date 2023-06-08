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

package org.kebs.app.kotlin.apollo.ipc.adaptor.akka.workers

import akka.actor.AbstractLoggingActor
import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.config.properties.kafka.ConsumerKafkaProperties
import org.kebs.app.kotlin.apollo.config.service.messaging.kafka.ConsumerConfigurer
import org.kebs.app.kotlin.apollo.ipc.dto.KafkaRequest
import org.kebs.app.kotlin.apollo.store.model.ServiceMapsWorkflowsFunctionsVwEntity
import org.kebs.app.kotlin.apollo.store.model.ServiceRequestsEntity
import org.kebs.app.kotlin.apollo.store.repo.IServiceMapsWorkflowsFunctionsVwRepository
import org.kebs.app.kotlin.apollo.store.repo.IServiceRequestsRepository
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Scope
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import java.lang.reflect.Method

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
class ServiceRequestProcessingActor(
        private val applicationContext: ApplicationContext,
        private val serviceRequestsRepository: IServiceRequestsRepository,
        private val functionsVwRepository: IServiceMapsWorkflowsFunctionsVwRepository,
        private val consumerConfigurer: ConsumerConfigurer,
        private val kafkaProperties: ConsumerKafkaProperties
) : AbstractLoggingActor() {


    override fun createReceive(): Receive {
        return receiveBuilder()
                .match(KafkaRequest::class.java) { request ->
                    try {
                        serviceRequestsRepository.findByIdOrNull(request.id)
                                ?.let { sr ->
                                    when {
                                        sr.status != sr.serviceMapsId?.initStatus -> {
                                            sr.status = sr.serviceMapsId?.failedStatus
                                            sr.responseStatus = sr.serviceMapsId?.failedStatusCode
                                            sr.responseMessage = "Received already processed result, dropping it"

                                        }
                                        else -> {

                                            sr.serviceMapsId?.id?.let { mapId ->
                                                functionsVwRepository.findByServiceMapOrderBySequenceNumber(mapId.toInt())
                                                        ?.let { functions ->
                                                            val functionsSorted = functions.sortedWith(compareBy(ServiceMapsWorkflowsFunctionsVwEntity::sequenceNumber, ServiceMapsWorkflowsFunctionsVwEntity::executionOrder))
                                                            for (function in functionsSorted) {
                                                                try {
                                                                    var result: Int? = null
                                                                    sr.previousStage = sr.currentStage
                                                                    sr.currentSequence = function.sequenceNumber
                                                                    sr.previousSequence = sr.currentSequence
                                                                    sr.currentStage = function.executionOrder

                                                                    function.beanName?.let { beanName ->
                                                                        applicationContext.getBean(beanName)
                                                                                .let { serviceBean ->
                                                                                    val beanClazz = serviceBean::class
                                                                                    KotlinLogging.logger { }.trace { beanClazz.qualifiedName }
                                                                                    val m: Method = beanClazz.java.getMethod(function.methodName, ServiceRequestsEntity::class.java, request.payload!!::class.java, function.workflowId::class.java)
                                                                                    //                                                                result = functionToInvoke?.call(activator, sr, request.payload, function.workflowId) as Int?
                                                                                    result = m.invoke(serviceBean, sr, request.payload, function.workflowId) as Int?
                                                                                    when (result) {
                                                                                        null -> {
                                                                                            result = sr.serviceMapsId?.exceptionStatus
                                                                                        }
                                                                                    }
                                                                                }
                                                                    }
                                                                    if (result != sr.serviceMapsId?.successStatus) {
                                                                        break
                                                                    }

                                                                    sr.status = result
                                                                } catch (e: Exception) {
                                                                    KotlinLogging.logger { }.error(e.message, e)
                                                                    KotlinLogging.logger { }.debug(e.message, e)
                                                                    sr.status = sr.serviceMapsId?.exceptionStatus
                                                                    sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
                                                                    sr.responseMessage = e.message
                                                                }

                                                            }

                                                        }
                                            }


                                        }
                                    }
                                    serviceRequestsRepository.save(sr)

                                    /**
                                     *  Pass @param sr to the notification actor with the appropriate details
                                     *  Required params: sr.notificationType, sr.responseStatus, sr.kafkaTopic, serviceRequestsEntity.messageParamList & ...
                                     *  ... other fields as required by spel_processor variable of the notification being sent
                                     *  NB: Currently send notification logic can only send one type of notification
                                     *  therefore if you choose to send SMS(s), set serviceRequestsEntity.messageParamList as a string of CSV {phoneNos}
                                     *  alternatively if sending email, set serviceRequestsEntity.messageParamList as a string of CSV {email addresses}
                                     *  TODO: Pass the notification thru kafka
                                     *  TODO: Allow multiple types of notifications to be sent at a time, if necessary
                                     *  TODO: Implement alfresco/ sharepoint to manage documents (I favor sharepoint)
                                     *
                                     *  val actorRef =
                                     *   actorSystem.actorOf(extension.props(notifsProperties.notifsActorName), notifsProperties.notifsActorName)
                                     *   actorRef.tell(sr, actorRef)
                                     *
                                     *
                                     *  I propose to ammend this and return it to Kafka for notification cycle to commence
                                     *  so that the flow becomes
                                     *      submit ServiceRequest to Topic
                                     *           Consumer stages the messages by replacing the parameterized values using values on the serviceRequest
                                     *              Hands over the message to the NotificationBufferProcessingActor for usecase selection and onward processing
                                     */
                                    consumerConfigurer.kafkaTemplate().send(kafkaProperties.notificationSelectUseCaseTopics[0], sr)


                                }
                                ?: throw NullValueNotAllowedException("Missing ServiceRequest for $request, notify administrator to check")
                        context().stop(self)

                    } catch (e: Exception) {
                        sender().tell(request, sender)
                    } finally {
                        context().stop(self())
                    }


                }
                .build()
    }


}