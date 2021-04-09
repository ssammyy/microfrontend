/*
 *                            Copyright (c) 2020.  BSK
 *                            Licensed under the Apache License, Version 2.0 (the "License");
 *                            you may not use this file except in compliance with the License.
 *                            You may obtain a copy of the License at
 *
 *                                http://www.apache.org/licenses/LICENSE-2.0
 *
 *                            Unless required by applicable law or agreed to in writing, software
 *                            distributed under the License is distributed on an "AS IS" BASIS,
 *                            WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *                            See the License for the specific language governing permissions and
 *                            limitations under the License.
 */

package org.kebs.app.kotlin.apollo.ipc.adaptor.workflows.service.notifications

import mu.KotlinLogging
import org.apache.commons.text.ExtendedMessageFormat
import org.kebs.app.kotlin.apollo.store.model.NotificationsBufferEntity
import org.kebs.app.kotlin.apollo.store.model.ServiceRequestsEntity
import org.kebs.app.kotlin.apollo.store.repo.INotificationTypesRepository
import org.kebs.app.kotlin.apollo.store.repo.INotificationsRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.memberProperties

@Component
class NotificationUtils(private val notificationRepository: INotificationsRepository, private val notificationTypesRepository: INotificationTypesRepository) {

    fun stageEmailNotification(notificationsBuffer: NotificationsBufferEntity, request: ServiceRequestsEntity): String {
        var response = ""
        notificationTypesRepository.findByIdOrNull(request.notificationType)
                ?.let { type ->

                    notificationRepository.findFirstByRequestTopicAndEventStatusAndNotificationTypeOrderByNotificationType(request.kafkaTopic, request.responseStatus, type)
                            ?.let {
                                with(notificationsBuffer) {
                                    sender = it.sender!!
                                    subject = it.subject!!
                                    messageBody = composeMessageUsingReflection(request)
                                    KotlinLogging.logger { }.info("Composed Message: $messageBody")
//                    response = ExtendedMessageFormat.format(
//                        it.get().template,
//                        arrayOf(notificationsBuffer.subject, notificationsBuffer.messageBody)
//                    )
                                    response = messageBody as String
//                    KotlinLogging.logger {  }.info("Response Message: $response")
                                }
                            }

                }

        return response
    }

    fun stageSmsNotification(notificationsBuffer: NotificationsBufferEntity, request: ServiceRequestsEntity): String {
        var response = ""
        notificationTypesRepository.findByIdOrNull(request.notificationType)
                ?.let { type ->
                    notificationRepository.findFirstByRequestTopicAndEventStatusAndNotificationTypeOrderByNotificationType(request.kafkaTopic, request.responseStatus, type)
                            ?.let {
                                with(notificationsBuffer) {
                                    messageBody = composeMessageUsingReflection(request)
                                    KotlinLogging.logger { }.info("Composed Message: $messageBody")
                                    response = messageBody!!
                                }
                            }

                }

        return response
    }

    @Suppress("UNCHECKED_CAST")
    fun composeMessageUsingReflection(request: ServiceRequestsEntity): String {

        var v: String
        val values: MutableList<String> = mutableListOf()
        var response = ""

        try {
            notificationTypesRepository.findByIdOrNull(request.notificationType)
                    ?.let { type ->
                        notificationRepository.findFirstByRequestTopicAndEventStatusAndNotificationTypeOrderByNotificationType(request.kafkaTopic, request.responseStatus, type)
                                ?.let {
                                    type.delimiter?.let { delimiter ->
                                        it.spelProcessor?.split(delimiter)?.forEach { expression ->
                                            val f = request::class.memberProperties.first { p -> p.name.equals(expression, true) }
                                            val o = (f as KMutableProperty1<Any, Any>).get(request)
                                            v = when (o.javaClass.name) {
                                                "java.lang.String" -> o as String
                                                "java.lang.Integer" -> String.format("%,2d", o as Int)
                                                "java.math.BigDecimal" -> String.format(
                                                        "%,.2f",
                                                        (o as BigDecimal).setScale(2, RoundingMode.DOWN)
                                                )
                                                "java.math.BigInteger" -> String.format("%,.2d", o as BigInteger)
                                                "java.sql.Timestamp" -> SimpleDateFormat("dd-MMM-yyyy").format(o as Timestamp)
                                                "java.time.Instant" -> DateTimeFormatter.ofPattern("dd-MMM-yyyy").withLocale(Locale.getDefault()).withZone(
                                                        ZoneId.systemDefault()
                                                ).format(o as Instant)
                                                "java.util.Date" -> SimpleDateFormat("dd-MMM-yyyy").format(o as Date)
                                                else -> try {
                                                    o.toString()
                                                } catch (e: Exception) {
                                                    KotlinLogging.logger { }.error(e.message, e)
                                                    "0"
                                                }
                                            }
                                            values.add(v.trim { it <= ' ' })
                                        }
                                    }
                                    it.description
                                            ?.let { desc ->
                                                response = ExtendedMessageFormat.format(desc, *values.toTypedArray())
                                            }

                                }
                    }


        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
            throw Exception("Exception occurred while parsing notification [${e.message}]")
        }
        return response
    }
}