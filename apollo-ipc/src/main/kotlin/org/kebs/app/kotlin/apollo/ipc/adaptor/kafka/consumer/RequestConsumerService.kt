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
import org.kebs.app.kotlin.apollo.common.exceptions.ProcessSelectionActorNotFoundException
import org.kebs.app.kotlin.apollo.config.adaptor.akka.config.ActorSpringExtension
import org.kebs.app.kotlin.apollo.ipc.dto.KafkaRequest
import org.kebs.app.kotlin.apollo.ipc.ports.provided.CustomGenericObjectMapper
import org.kebs.app.kotlin.apollo.ipc.ports.provided.TransactionReferenceGenerator
import org.kebs.app.kotlin.apollo.store.model.ServiceMapsEntity
import org.kebs.app.kotlin.apollo.store.model.ServiceRequestsEntity
import org.kebs.app.kotlin.apollo.store.repo.IServiceMapsRepository
import org.kebs.app.kotlin.apollo.store.repo.IServiceRequestsRepository
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.KafkaHeaders.*
import org.springframework.messaging.handler.annotation.Header
import org.springframework.stereotype.Service
import java.sql.Date
import java.sql.Timestamp
import java.time.Instant
import java.util.concurrent.CompletableFuture
import kotlin.reflect.full.createInstance


@Service
@EncryptablePropertySource("file:\${CONFIG_PATH}/kafka.properties")
class RequestConsumerService(
    private val extension: ActorSpringExtension,
    private val actorSystem: ActorSystem,
    private val mapsRepository: IServiceMapsRepository,
    private val serviceRequestsRepository: IServiceRequestsRepository
) {

    final var processorSelectionActor: ActorRef? = null

    init {
        processorSelectionActor =
            actorSystem.actorOf(extension[actorSystem].props("processorSelectionActor"), "processorSelectionActor")

    }

    private fun transactionReferenceGenerator(serviceMapsEntity: ServiceMapsEntity): String {
        val result: String
        val generateTransactionReference = TransactionReferenceGenerator(
                serviceMapsEntity.transactionRefLength,
                serviceMapsEntity.secureRandom,
                serviceMapsEntity.messageDigestAlgorithm
        )
        result = generateTransactionReference.generateTransactionReference()
        return result

    }

    @KafkaListener(
            clientIdPrefix = "requestConsumer",
            topics = ["#{'\${org.kebs.app.kotlin.apollo.kafka.consumer.topics.list}'.split(',')}"],
            groupId = "\${org.kebs.app.kotlin.apollo.kafka.consumer.groupId}"
    )
    fun requestConsumer(
            requests: List<Any>, @Header(RECEIVED_PARTITION_ID) partitions: Int,
            @Header(OFFSET) offsets: Long,
            @Header(TIMESTAMP_TYPE) timestampType: String,
            @Header(RECEIVED_TOPIC) topic: String,
            @Header(name = RECEIVED_MESSAGE_KEY, required = false) messageKey: String?,
            @Header(RECEIVED_TIMESTAMP) timestamp: Long
    ) {

        KotlinLogging.logger {}.info("received message=List with partition-offset=[$partitions-$offsets] Headers[$topic,$messageKey,$timestampType,$timestamp]")


        mapsRepository.findByServiceTopic(topic)
                ?.let { s ->
                    requests.forEach { request ->
                        KotlinLogging.logger {}.info("received message=${request} with partition-offset=[$partitions-$offsets] Headers[$topic,$messageKey,$timestampType,$timestamp]")
                        val kafkaRequest = KafkaRequest()
                        with(kafkaRequest) {
                            kafkaPartitionId = partitions
                            kafkaOffset = offsets
                            kafkaTopic = topic
                            messageKey?.let { kafkaMessageKey = it }
                            kafkaTimestampType = timestampType
                            kafkaTimestamp = timestamp
                            eventBusSubmitDate = Instant.now()
                            processingStartDate = Instant.now()
                            transactionReference = transactionReferenceGenerator(s)
                            serviceMapsDto = s

                        }
                        try {

                            when {

                                s.toMap > s.inactiveStatus -> {
                                    s.objectMappedTo?.let {
                                        var output = Class.forName(s.objectMappedTo).kotlin.createInstance()
                                        val customGenericObjectMapper = CustomGenericObjectMapper()
                                        output = customGenericObjectMapper.forward(request, output)
                                        kafkaRequest.payload = output
                                    }

                                }
                                else -> {
                                    kafkaRequest.payload = request
                                }

                            }






                            try {
                                var serviceRequests = ServiceRequestsEntity()

                                with(serviceRequests) {
                                    serviceMapsId = s
                                    createdBy = kafkaRequest.transactionReference
                                    createdOn = Timestamp.from(Instant.now())
                                    kafkaPartitionId = kafkaRequest.kafkaPartitionId
                                    kafkaOffset = kafkaRequest.kafkaOffset
                                    kafkaTopic = kafkaRequest.kafkaTopic
                                    kafkaMessageKey = kafkaRequest.kafkaMessageKey
                                    kafkaTimestampType = kafkaRequest.kafkaTimestampType
                                    kafkaTimestamp = kafkaRequest.kafkaTimestamp
                                    eventBusSubmitDate = Timestamp.from(kafkaRequest.eventBusSubmitDate)
                                    processingStartDate = Timestamp.from(kafkaRequest.processingStartDate)
                                    transactionReference = kafkaRequest.transactionReference
                                    requestDate = Date(java.util.Date().time)
                                    status = s.initStatus
                                    serviceRequests = serviceRequestsRepository.save(serviceRequests)
                                    serviceRequests.id?.let { kafkaRequest.id = it }


                                }
                            } catch (e: Exception) {
                                KotlinLogging.logger { }.error(e.message, e)
                                kafkaRequest.responseMessage = kafkaRequest.responseMessage + ": " + e.message
                            }

                            processorSelectionActor
                                    ?.let { processSelector ->
                                        processSelector.tell(kafkaRequest, processSelector)

                                    }
                                    ?: throw ProcessSelectionActorNotFoundException("Main process selection Actor cannot be empty or null, request aborted")


//                            s.actorClass?.let { actorBean ->
//                                val actorRef = actorSystem.actorOf(extension.props(actorBean), actorBean)
//                                actorRef.tell(kafkaRequest, actorRef)
//                            }


                        } catch (e: Exception) {
                            KotlinLogging.logger { }.error(e.message)
                            KotlinLogging.logger { }.debug(e.message, e)
                        }


                    }
                }


    }

    @KafkaListener(
            clientIdPrefix = "asyncRequestConsumer",
            topics = ["#{'\${org.kebs.app.kotlin.apollo.kafka.consumer.async.topics.list}'.split(',')}"],
            groupId = "\${org.kebs.app.kotlin.apollo.kafka.consumer.groupId}"
    )
    fun asyncRequestConsumer(
            requests: Any, @Header(RECEIVED_PARTITION_ID) partitions: Int,
            @Header(OFFSET) offsets: Long,
            @Header(TIMESTAMP_TYPE) timestampType: String,
            @Header(RECEIVED_TOPIC) topic: String,
            @Header(name = RECEIVED_MESSAGE_KEY, required = false) messageKey: String?,
            @Header(RECEIVED_TIMESTAMP) timestamp: Long
    ): Any? {


        val future: CompletableFuture<Any>? = null
        mapsRepository.findByServiceTopic(topic)
                ?.let { s ->


                    val kafkaRequest = KafkaRequest()
                    with(kafkaRequest) {
                        kafkaPartitionId = partitions
                        kafkaOffset = offsets
                        kafkaTopic = topic
                        messageKey?.let { kafkaMessageKey = it }
                        kafkaTimestampType = timestampType
                        kafkaTimestamp = timestamp
                        eventBusSubmitDate = Instant.now()
                        processingStartDate = Instant.now()
                        transactionReference = transactionReferenceGenerator(s)
                        payload = requests

                    }
                    s.actorClass?.let { actorBean ->
                        /**
                         * Fix the Future
                         */
                        val asyncActorRef = actorSystem.actorOf(extension[actorSystem].props(actorBean), actorBean)
                        asyncActorRef.tell(kafkaRequest, asyncActorRef)
//                        future = Patterns.ask(asyncActorRef, kafkaRequest, Duration.of(kafkaProperties.asyncActorTimeout, ChronoUnit.MILLIS)).toCompletableFuture()
                    }


                }

        return future?.thenApply { o: Any? -> o }?.join()


    }


}