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

package org.kebs.app.kotlin.apollo.config.service.messaging.kafka

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.kebs.app.kotlin.apollo.config.properties.kafka.ConsumerKafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.config.KafkaListenerContainerFactory
import org.springframework.kafka.core.*
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer
import org.springframework.kafka.listener.ContainerProperties
import org.springframework.kafka.listener.KafkaMessageListenerContainer
import org.springframework.kafka.listener.SeekToCurrentBatchErrorHandler
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.kafka.support.serializer.JsonSerializer
import org.springframework.util.backoff.FixedBackOff
import java.util.*

@Configuration
class ConsumerConfigurer(
        private val prop: ConsumerKafkaProperties
) {

    @Bean(name = ["ConsumerProducerConfig"])
    fun producerConfig(): Map<String, Any> {
        val configProps: MutableMap<String, Any> = HashMap()
        //        configProps.put("spring.json.trusted.packages", customKafkaProperties.getTrustedPackages());
        configProps[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = prop.bootStrapServer
        configProps[ProducerConfig.COMPRESSION_TYPE_CONFIG] = prop.producerCompressionTypes
        configProps[ProducerConfig.RETRIES_CONFIG] = prop.producerRetries
        configProps[ProducerConfig.ACKS_CONFIG] = prop.producerAcks
        configProps[ProducerConfig.MAX_REQUEST_SIZE_CONFIG] = prop.maxRequestSizeConfig
        configProps[ProducerConfig.BATCH_SIZE_CONFIG] = prop.batchSize
        configProps[ProducerConfig.LINGER_MS_CONFIG] = prop.lingerConfig
        configProps[ProducerConfig.BUFFER_MEMORY_CONFIG] = prop.bufferMemory


        configProps[ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION] =
                prop.maxInFlightRequestsPerConnection
        configProps[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java.name
        configProps[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = JsonSerializer::class.java.name

        return configProps
    }


    @Bean
    fun consumerConfig(): Map<String, Any> {
        val props: MutableMap<String, Any> = HashMap()
        props["spring.json.trusted.packages"] = prop.trustedPackages
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = prop.bootStrapServer
        props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java.name
        props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = JsonDeserializer::class.java.name
        props[ConsumerConfig.GROUP_ID_CONFIG] = prop.groupId
        props[ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG] = prop.enableAutoCommit
//        props[ConsumerConfig.ISOLATION_LEVEL_CONFIG] = "read_committed"
        props[ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG] = prop.autoCommit
        props[ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG] = prop.sessionTimeout
        props[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = prop.offsetReset
        props[ConsumerConfig.FETCH_MIN_BYTES_CONFIG] = prop.consumerFetchMinBytes
        props[ConsumerConfig.MAX_POLL_RECORDS_CONFIG] = prop.maxPollRecordsConfig
        props[ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG] = prop.maxPollIntervalMsConfig
        props[ConsumerConfig.CLIENT_ID_CONFIG] = prop.clientIdConfig
        return props
    }

    @Bean
    fun producerFactory(): ProducerFactory<String, Any> {
        return DefaultKafkaProducerFactory(producerConfig())
    }

//    @Bean
//    fun container(cf: ConsumerFactory<String, Any>): KafkaMessageListenerContainer<String, Any> {
//
//
//        val containerProperties = ContainerProperties(prop.requestReplyTopic)
//        containerProperties.isMissingTopicsFatal = false
//        return KafkaMessageListenerContainer(cf, containerProperties)
//
//    }

//    @Bean
//    fun consumerFactory(): ConsumerFactory<String, Any> {
//
//        return DefaultKafkaConsumerFactory(consumerConfig().toMutableMap())
//    }

    @Bean
    fun consumerFactory(): ConsumerFactory<String, Any> {
//        return new DefaultKafkaConsumerFactory<>(consumerConfigs(), new StringDeserializer(), new JsonDeserializer<>(Object.class));
        return DefaultKafkaConsumerFactory(consumerConfig())
    }

//    @Bean
//    fun kafkaTemplate(): KafkaTemplate<String?, Any?> {
//        val result = KafkaTemplate(producerFactory())
//        with(result) {
//            defaultTopic = prop.requestTopic
//
//        }
//        return result
//    }

//    @Bean
//    fun kafkaListenerContainerFactory(): KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String?, Any?>>? {
//        val factory =
//                ConcurrentKafkaListenerContainerFactory<String, Any>()
//        factory.setConcurrency(prop.concurrency)
//        factory.consumerFactory = consumerFactory()
//        factory.setErrorHandler(SeekToCurrentErrorHandler())
//        factory.setReplyTemplate(replyTemplate(producerFactory(), factory))
//        KotlinLogging.logger { }.info { "Apollo KafkaListenerContainerFactory built..." }
//        return factory
//    }

//    @Bean
//    fun replyKafkaTemplate(): ReplyingKafkaTemplate<String?, Any?, Any> {
//        val requestReplyKafkaTemplate =
//            ReplyingKafkaTemplate(
//                producerFactory(),
//                container(consumerFactory())
//            )
//        requestReplyKafkaTemplate.defaultTopic = properties.requestTopic
////        requestReplyKafkaTemplate.setReplyTimeout(consumerKafkaProperties.requestReplyTimeoutMs.toLong())
//        return requestReplyKafkaTemplate
//    }

//    private fun startContainer(): Boolean {
//        return try {
//            val container: KafkaMessageListenerContainer<String, Any> = container(consumerFactory())
//            container.start()
//            true
//        } catch (e: Exception) {
//            KotlinLogging.logger { }.error { e }
//            false
//
//        }
//
//
//    }
//
//    override fun run(args: ApplicationArguments?) {
//        try {
//            KotlinLogging.logger { }.info { "Starting kafka listener container......" }
//            startContainer()
//        } catch (e: java.lang.Exception) {
//            KotlinLogging.logger { }.error { e }
//        }
//    }

    @Bean
    fun factory(): ConcurrentKafkaListenerContainerFactory<String, Any> {
        return ConcurrentKafkaListenerContainerFactory<String, Any>()
    }

    @Bean
    fun replyContainer(cf: ConsumerFactory<String?, Any>): KafkaMessageListenerContainer<String, Any> {
        val containerProperties = ContainerProperties(prop.requestReplyTopic)
        return KafkaMessageListenerContainer(cf, containerProperties)
    }

    @Bean
    fun replyingKafkaTemplate(
            pf: ProducerFactory<String, Any>,
            factory: ConcurrentKafkaListenerContainerFactory<String, Any>,
            replyContainer: KafkaMessageListenerContainer<String, Any>
    ): ReplyingKafkaTemplate<String, Any?, Any?>? {
//        val replyContainer: ConcurrentMessageListenerContainer<String, Any> =
//            factory.createContainer(prop.requestReplyTopic)
        replyContainer.containerProperties.isMissingTopicsFatal = false
        replyContainer.containerProperties.groupId = prop.groupId
        return ReplyingKafkaTemplate(pf, replyContainer)
    }

    @Bean
    fun replyTemplate(
            pf: ProducerFactory<String, Any>,
            factory: ConcurrentKafkaListenerContainerFactory<String, Any>
    ): KafkaTemplate<String, Any> {
        val kafkaTemplate: KafkaTemplate<String, Any> = KafkaTemplate(pf)
        factory.containerProperties.isMissingTopicsFatal = false
        factory.setReplyTemplate(kafkaTemplate)
        return kafkaTemplate
    }

    fun kafkaTemplate(): KafkaTemplate<String, Any?> {
        return KafkaTemplate(producerFactory())
    }


    @Bean
    fun kafkaListenerContainerFactory(): KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Any?>> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, Any>()
        factory.consumerFactory = consumerFactory()
        factory.isBatchListener = true
        val batchErrorHandler = SeekToCurrentBatchErrorHandler()
//        val batchErrorHandler = CustomErrorHandler()
        val backOff = FixedBackOff(500, FixedBackOff.DEFAULT_INTERVAL)
        batchErrorHandler.setBackOff(backOff)
//        batchErrorHandler.setLogLevel(KafkaException.Level.FATAL)
        factory.setBatchErrorHandler(batchErrorHandler)
//        factory.containerProperties.consumerRebalanceListener.onPartitionsAssigned()
//        val errorHandler = SeekToCurrentErrorHandler()
//        factory.setErrorHandler(errorHandler)
        return factory
    }


}