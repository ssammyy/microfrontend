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

package org.kebs.app.kotlin.apollo.config.service.messaging.kafka

import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.kebs.app.kotlin.apollo.config.properties.kafka.ConsumerKafkaProperties
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.support.serializer.JsonSerializer
import java.util.*

@Configuration
class ProducerConfigure(
    private val prop: ConsumerKafkaProperties
) {

    private fun producerConfig(): Map<String, Any> {
        val configProps: MutableMap<String, Any> = HashMap()
        configProps[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = prop.bootStrapServer
        configProps[ProducerConfig.COMPRESSION_TYPE_CONFIG] = prop.producerCompressionTypes
        configProps[ProducerConfig.RETRIES_CONFIG] = prop.producerRetries
        configProps[ProducerConfig.ACKS_CONFIG] = prop.producerAcks
        configProps[ProducerConfig.MAX_REQUEST_SIZE_CONFIG] = prop.maxRequestSizeConfig
        configProps[ProducerConfig.BATCH_SIZE_CONFIG] = prop.batchSize
        configProps[ProducerConfig.LINGER_MS_CONFIG] = prop.lingerConfig
        configProps[ProducerConfig.BUFFER_MEMORY_CONFIG] = prop.bufferMemory
//        configProps[ProducerConfig.TRANSACTIONAL_ID_DOC] = "transactional.id"
        configProps[ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION] =
            prop.maxInFlightRequestsPerConnection
        configProps[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java.name
        configProps[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = JsonSerializer::class.java.name

        return configProps
    }

    private fun producerFactory(): ProducerFactory<String?, Any?> {
        return DefaultKafkaProducerFactory(producerConfig())
    }

    fun kafkaTemplate(): KafkaTemplate<String?, Any?> {
        return KafkaTemplate(producerFactory())
    }

}

