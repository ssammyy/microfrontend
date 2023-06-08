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

package org.kebs.app.kotlin.apollo.adaptor.kafka.producer.service

import mu.KotlinLogging
import org.apache.kafka.clients.producer.ProducerRecord
import org.kebs.app.kotlin.apollo.common.ports.required.ISubmitToEventBus
import org.kebs.app.kotlin.apollo.config.service.messaging.kafka.ProducerConfigure
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate
import org.springframework.kafka.requestreply.RequestReplyFuture
import org.springframework.stereotype.Service
import java.util.*

@EnableKafka
@Service
class SendToKafkaQueue(
        private val producerConfigure: ProducerConfigure,
        private val replyingKafkaTemplate: ReplyingKafkaTemplate<String, Any, Any>

) : ISubmitToEventBus<Any, String, Any?> {
    override fun submitAsyncRequestToBus(request: Any, topic: String) {
        try {
            val submit = producerConfigure.kafkaTemplate().send(topic, request)
            KotlinLogging.logger { }.info("submitted to ${submit.get().producerRecord.topic()}")

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
//                    e.printStackTrace()
        }
    }

    override fun submitSyncRequestToBus(request: Any, topic: String): Any? {
        return try {

            val key = UUID.randomUUID().toString().substring(0, 8)
            val record: ProducerRecord<String, Any> = ProducerRecord(topic, key, request)
            val future: RequestReplyFuture<String, Any, Any> = replyingKafkaTemplate.sendAndReceive(record)
            future.get()
        } catch (e: Exception) {
            KotlinLogging.logger { }.error { e }
            null
        }

    }
}