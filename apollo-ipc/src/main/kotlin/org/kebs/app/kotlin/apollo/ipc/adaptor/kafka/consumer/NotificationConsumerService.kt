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

package org.kebs.app.kotlin.apollo.ipc.adaptor.kafka.consumer

import akka.actor.ActorRef
import akka.actor.ActorSystem
import com.ulisesbocchio.jasyptspringboot.annotation.EncryptablePropertySource
import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.common.exceptions.InvalidValueException
import org.kebs.app.kotlin.apollo.common.exceptions.ProcessSelectionActorNotFoundException
import org.kebs.app.kotlin.apollo.common.utils.composeUsingSpel
import org.kebs.app.kotlin.apollo.common.utils.placeHolderMapper
import org.kebs.app.kotlin.apollo.common.utils.replacePrefixedItemsWithObjectValues
import org.kebs.app.kotlin.apollo.config.adaptor.akka.config.ActorSpringExtension
import org.kebs.app.kotlin.apollo.config.service.messaging.kafka.ConsumerConfigurer
import org.kebs.app.kotlin.apollo.store.model.NotificationsBufferEntity
import org.kebs.app.kotlin.apollo.store.model.NotificationsEntity
import org.kebs.app.kotlin.apollo.store.model.ServiceRequestsEntity
import org.kebs.app.kotlin.apollo.store.repo.INotificationsBufferRepository
import org.kebs.app.kotlin.apollo.store.repo.INotificationsRepository
import org.kebs.app.kotlin.apollo.store.repo.IServiceRequestsRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.Instant

@Service
@EncryptablePropertySource("file:\${CONFIG_PATH}/kafka.properties")
class NotificationConsumerService(
    extension: ActorSpringExtension,
    actorSystem: ActorSystem,
    private val notificationsRepo: INotificationsRepository,
    private val consumerConfigurer: ConsumerConfigurer,
    private val serviceRequestsRepository: IServiceRequestsRepository,
    private val bufferRepository: INotificationsBufferRepository
) {

    final var processorSelectionActor: ActorRef? = null

    init {
        processorSelectionActor = actorSystem.actorOf(
            extension[actorSystem].props("processorSelectionActor"),
            "processorSelectionNotificationsActor"
        )
    }


    /**
     * Execute the predefined bean for this notification type
     */
    @KafkaListener(
            clientIdPrefix = "notificationUseCaseSelection",
            topics = ["#{'\${org.kebs.app.kotlin.apollo.kafka.consumer.notification.select.usecase.topics.list}'.split(',')}"],
            groupId = "\${org.kebs.app.kotlin.apollo.kafka.consumer.groupId}"
    )
    fun notificationUseCaseSelection(
            requests: List<ServiceRequestsEntity>,
            @Header(KafkaHeaders.RECEIVED_PARTITION_ID) partitions: Int,
            @Header(KafkaHeaders.OFFSET) offsets: Long,
            @Header(KafkaHeaders.TIMESTAMP_TYPE) timestampType: String,
            @Header(KafkaHeaders.RECEIVED_TOPIC) topic: String,
            @Header(name = KafkaHeaders.RECEIVED_MESSAGE_KEY, required = false) messageKey: String?,
            @Header(KafkaHeaders.RECEIVED_TIMESTAMP) timestamp: Long
    ) {
        try {
            KotlinLogging.logger {}.info("received message=List with partition-offset=[$partitions-$offsets] Headers[$topic,$messageKey,$timestampType,$timestamp]")
            requests.forEach { request ->
                notificationsRepo.findByServiceRequestStatusAndStatus(request.status, request.serviceMapsId?.activeStatus)
                        ?.let { notifications ->
                            notifications.forEach { notificationsEntity ->
                                notificationsEntity.requestTopic
                                        ?.let { topic ->
                                            request.notificationType = notificationsEntity.id
                                            serviceRequestsRepository.save(request)

                                            consumerConfigurer.kafkaTemplate().send(topic, request)
                                        }
                                        ?: throw InvalidValueException("RequestTopic for Notification[id=${notificationsEntity.id} is is not set up correctly")
                            }


                        }

            }
        } catch (e: Exception) {
            KotlinLogging.logger { }.debug(e.message, e)
            KotlinLogging.logger { }.error(e.message)
        }

    }


    @KafkaListener(
            clientIdPrefix = "sendStagedNotification",
            topics = ["#{'\${org.kebs.app.kotlin.apollo.kafka.consumer.notification.sending.topics.list}'.split(',')}"],
            groupId = "\${org.kebs.app.kotlin.apollo.kafka.consumer.groupId}"
    )
    fun sendStagedNotification(
            requests: List<NotificationsBufferEntity>,
            @Header(KafkaHeaders.RECEIVED_PARTITION_ID) partitions: Int,
            @Header(KafkaHeaders.OFFSET) offsets: Long,
            @Header(KafkaHeaders.TIMESTAMP_TYPE) timestampType: String,
            @Header(KafkaHeaders.RECEIVED_TOPIC) topic: String,
            @Header(name = KafkaHeaders.RECEIVED_MESSAGE_KEY, required = false) messageKey: String?,
            @Header(KafkaHeaders.RECEIVED_TIMESTAMP) timestamp: Long
    ) {

        requests.forEach { request ->

            processorSelectionActor
                    ?.let { processSelector ->
                        processSelector.tell(request, processSelector)
                    }
                    ?: throw ProcessSelectionActorNotFoundException("Main process selection Actor cannot be empty or null, request aborted")


        }

    }

    /**
     * Execute the predefined bean for this notification type
     */

    @KafkaListener(
            clientIdPrefix = "stageNotification",
            topics = ["#{'\${org.kebs.app.kotlin.apollo.kafka.consumer.notification.staging.topics.list}'.split(',')}"],
            groupId = "\${org.kebs.app.kotlin.apollo.kafka.consumer.groupId}"
    )
    fun stageNotification(
            requests: List<ServiceRequestsEntity>,
            @Header(KafkaHeaders.RECEIVED_PARTITION_ID) partitions: Int,
            @Header(KafkaHeaders.OFFSET) offsets: Long,
            @Header(KafkaHeaders.TIMESTAMP_TYPE) timestampType: String,
            @Header(KafkaHeaders.RECEIVED_TOPIC) topic: String,
            @Header(name = KafkaHeaders.RECEIVED_MESSAGE_KEY, required = false) messageKey: String?,
            @Header(KafkaHeaders.RECEIVED_TIMESTAMP) timestamp: Long
    ) {
        KotlinLogging.logger {}.info("received message=List with partition-offset=[$partitions-$offsets] Headers[$topic,$messageKey,$timestampType,$timestamp]")
        requests.forEach { request ->
            notificationsRepo.findByIdOrNull(request.notificationType)
                    ?.let { notification ->
                        /**
                         * Stage
                         */
                        request.messageParamList
                                ?.let { list ->
                                    notification.notificationType?.delimiter
                                            ?.let { list.split(it) }
                                            ?.forEachIndexed { _, r ->
                                                /**
                                                 * Send the bufferedNotification for processing by an actor
                                                 */
                                                notification.actorClass?.let { actorBean ->
                                                    processorSelectionActor
                                                            ?.let { processSelector ->
                                                                processSelector.tell(generateBufferedNotification(request, notification, r, actorBean), processSelector)
                                                            }
                                                            ?: throw ProcessSelectionActorNotFoundException("Main process selection Actor cannot be empty or null, request aborted")
                                                }


//                                                notification.actorClass?.let { actorBean ->
//                                                    val actorRef = actorSystem.actorOf(extension.props(actorBean), actorBean)
//                                                    actorRef.tell(generateBufferedNotification(request, notification, r, actorBean), actorRef)
//                                                }
                                                request.responseMessage = "${request.responseMessage}: Notification request created and sent for processing"
                                                serviceRequestsRepository.save(request)


                                            }
                                }


                        /**
                         * Send
                         */
                    }
                    ?: throw InvalidValueException("NotificationType not set for ServiceRequest[id=${request.id} possible issue on the previous step")


        }


    }

    private fun generateBufferedNotification(sr: ServiceRequestsEntity, notification: NotificationsEntity, recipient: String, actor: String): NotificationsBufferEntity {
        var buffer = NotificationsBufferEntity()
        with(buffer) {
            messageBody = composeMessage(sr, notification)
            subject = notification.subject
            attachment = sr.attachmentPath
            sender = notification.sender
            this.recipient = recipient
            status = sr.serviceMapsId?.initStatus
            createdOn = Timestamp.from(Instant.now())
            createdBy = sr.transactionReference
            notificationId = notification.id
            serviceRequestId = sr.id
            actorClassName = actor
        }
        buffer = bufferRepository.save(buffer)
        return buffer
    }

    private fun composeMessage(request: ServiceRequestsEntity, notification: NotificationsEntity): String? {
        val p = notification.notificationType?.let { notif ->
            notif.delimiter?.let {
                notification.spelProcessor?.split(it)?.replacePrefixedItemsWithObjectValues(request, notif.beanprefix, notif.beanprefixreplacement) { d, p ->
                    composeUsingSpel(d, p)
                }
            }?.toTypedArray()
        }
        return p?.let { placeHolderMapper(input = notification.description, parameters = it) }


    }

}