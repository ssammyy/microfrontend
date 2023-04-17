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

package org.kebs.app.kotlin.apollo.adaptor.kafka.producer

import mu.KotlinLogging
import org.junit.Test
import org.kebs.app.kotlin.apollo.adaptor.kafka.producer.service.SendToKafkaQueue
import org.kebs.app.kotlin.apollo.common.dto.BaseRequest
import org.springframework.beans.factory.annotation.Autowired
//import org.junit.Test


class KafkaProducerApplicationTest {

    @Autowired
    lateinit var sendToKafkaQueue: SendToKafkaQueue




    private fun mockRequest(): BaseRequest {
        val result = BaseRequest()
        with(result) {
            id = 1L
            name = "dummyRequest"
            description = "A Dummy Request"
            status = 0
            topic = "dummyRequest"
        }

        return result
    }

    @Test
    fun contextLoads() {
    }

    @Test
    fun shouldSendDataToKafka() {


        val topic = "request-topic"
        try {
            val response = sendToKafkaQueue.submitAsyncRequestToBus(mockRequest(), topic)
            KotlinLogging.logger { }.info { "$response" }
        } catch (e: Exception) {
            KotlinLogging.logger { }.error { e }
        }


    }




}
