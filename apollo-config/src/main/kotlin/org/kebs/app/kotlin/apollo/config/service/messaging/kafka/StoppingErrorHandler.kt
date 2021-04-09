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

import mu.KotlinLogging
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.common.TopicPartition
import org.springframework.kafka.config.KafkaListenerEndpointRegistry
import org.springframework.kafka.listener.ConsumerAwareBatchErrorHandler
import org.springframework.kafka.listener.ErrorHandler

class CustomErrorHandler : ConsumerAwareBatchErrorHandler {

//    private val recoveries = DeadLetterPublishingRecoverer(kafkaTemplate) { r, _ -> TopicPartition(r.topic() + "-dlq", -1) }

    /**
     * Custom seek function which will seek to the next (max + 1) offset for each [TopicPartition] in the [ConsumerRecords].
     *
     * @author Mitchel Nijdam
     */
    fun <K, V> ConsumerRecords<K, V>.seekToNext(consumer: Consumer<*, *>) {
        this.toList()
                .groupBy { TopicPartition(it.topic(), it.partition()) }
                .mapValues { (_, value) ->
                    value.maxByOrNull { it.offset() }?.offset()
                }.forEach { (topicPartition, maxOffset) ->
                consumer.seek(topicPartition, maxOffset ?: 0 + 1)
                }
    }

    /**
     * Custom seek function which will seek to the first/smallest offset for each [TopicPartition] in all [ConsumerRecords].
     */
    fun <K, V> ConsumerRecords<K, V>.seekToCurrent(consumer: Consumer<*, *>) {
        this.toList()
                .groupBy { TopicPartition(it.topic(), it.partition()) }
                .mapValues { (_, value) ->
                    value.minByOrNull { it.offset() }?.offset()
                }.forEach { (topicPartition, minOffset) ->
                consumer.seek(topicPartition, minOffset ?: 1)
                }
    }

    // makes sure the offset is committed to kafka after the error handler finished without exceptions
    override fun isAckAfterHandle(): Boolean {
        return true
    }


    override fun handle(thrownException: java.lang.Exception?, data: ConsumerRecords<*, *>?, consumer: Consumer<*, *>?) {
        if (data == null) {
            throw NullPointerException("Empty record")
        } else {
            KotlinLogging.logger { }.debug("Error while processing $data message=${thrownException?.message}", thrownException)
            KotlinLogging.logger { }.error("Error while processing $data message=${thrownException?.message}")
//            data.forEach { recoveries.accept(it, thrownException) }
            consumer?.let { data.seekToNext(it) }


        }
    }
}

//@Component
class StoppingErrorHandler(
        private var kafkaListenerEndpointRegistry: KafkaListenerEndpointRegistry
) : ErrorHandler {

    fun <K, V> ConsumerRecords<K, V>.seekToNext(consumer: Consumer<*, *>) {
        this.toList()
                .groupBy { TopicPartition(it.topic(), it.partition()) }
                .mapValues { (_, value) ->
                    value.maxByOrNull { it.offset() }?.offset()
                }.forEach { (topicPartition, maxOffset) ->
                consumer.seek(topicPartition, maxOffset ?: 0 + 1)
                }
    }


    override fun handle(thrownException: Exception, record: ConsumerRecord<*, *>?) {
        if (record == null) {

            throw NullPointerException("Empty record")

        } else {
            KotlinLogging.logger { }.error("Error while processing $record message=${thrownException.message}", thrownException)
//            record?.let { it. }


        }

//        kafkaListenerEndpointRegistry.stop()
    }
}