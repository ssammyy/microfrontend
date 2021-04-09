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

package org.kebs.app.kotlin.apollo.config.properties.kafka

import com.ulisesbocchio.jasyptspringboot.annotation.EncryptablePropertySource
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
@EncryptablePropertySource("file:\${CONFIG_PATH}/kafka.properties")
class ConsumerKafkaProperties {
    val asyncActorTimeout: Long = 1000

    @Value("\${org.kebs.app.kotlin.apollo.kafka.consumer.max.poll.inter.ms.config}")
    val maxPollIntervalMsConfig: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.kafka.consumer.concurrency}")
    val concurrency: Int = 1

    @Value("\${org.kebs.app.kotlin.apollo.kafka.consumer.client.id.config}")
    var clientIdConfig: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.kafka.consumer.trusted.packages}")
    val trustedPackages: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.kafka.consumer.bootstrap.server}")
    var bootStrapServer: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.kafka.consumer.groupId}")
    var groupId: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.kafka.consumer.enable.auto.commit}")
    var enableAutoCommit: Boolean = false

    @Value("\${org.kebs.app.kotlin.apollo.kafka.consumer.auto.commit}")
    var autoCommit: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.kafka.consumer.session.timeout}")
    var sessionTimeout: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.kafka.consumer.poll.timeout}")
    var pollTimeout: Long = 0L

    @Value("\${org.kebs.app.kotlin.apollo.kafka.consumer.offset.reset}")
    var offsetReset: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.kafka.consumer.consumer.fetch.min.bytes}")
    var consumerFetchMinBytes: Int = 0

    @Value("\${org.kebs.app.kotlin.apollo.kafka.consumer.max.poll.records.config}")
    var maxPollRecordsConfig: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.kafka.consumer.request.reply.topic}")
    var requestReplyTopic: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.kafka.consumer.service.topics}")
    var serviceTopics: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.kafka.consumer.request.topic}")
    var requestTopic: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.kafka.consumer.request.reply.timeout.ms}")
    var requestReplyTimeoutMs: Int = 0

    @Value("\${org.kebs.app.kotlin.apollo.kafka.consumer.topics.list}")
    var topicsList: Array<String> = arrayOf()

    @Value("\${org.kebs.app.kotlin.apollo.kafka.consumer.async.topics.list}")
    var asyncTopicsList: Array<String> = arrayOf()

    @Value("\${org.kebs.app.kotlin.apollo.kafka.consumer.notification.select.usecase.topics.list}")
    var notificationSelectUseCaseTopics: Array<String> = arrayOf()

    @Value("\${org.kebs.app.kotlin.apollo.kafka.consumer.notification.staging.topics.list}")
    var notificationStagingTopics: Array<String> = arrayOf()

    @Value("\${org.kebs.app.kotlin.apollo.kafka.consumer.notification.sending.topics.list}")
    var notificationSendingTopics: Array<String> = arrayOf()

    @Value("\${org.kebs.app.kotlin.apollo.kafka.producer.max.in.flight.requests.per.connection}")
    var maxInFlightRequestsPerConnection = "5"

    @Value("\${org.kebs.app.kotlin.apollo.kafka.producer.buffer.memory}")
    var bufferMemory: Int = 60000000

    @Value("\${org.kebs.app.kotlin.apollo.kafka.producer.linger.config}")
    var lingerConfig: Int = 10

    @Value("\${org.kebs.app.kotlin.apollo.kafka.producer.batch.size}")
    var batchSize: Int = 10

    @Value("\${org.kebs.app.kotlin.apollo.kafka.producer.max.request.size.config}")
    var maxRequestSizeConfig: String = "231072000"

    @Value("\${org.kebs.app.kotlin.apollo.kafka.producer.producer.acks}")
    var producerAcks: String = "0"

    @Value("\${org.kebs.app.kotlin.apollo.kafka.producer.producer.retries}")
    var producerRetries: String = "1"

    @Value("\${org.kebs.app.kotlin.apollo.kafka.producer.producer.compression.types}")
    var producerCompressionTypes: String = "lz4"


}