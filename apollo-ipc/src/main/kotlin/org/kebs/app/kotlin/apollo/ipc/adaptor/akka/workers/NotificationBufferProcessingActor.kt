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

import akka.actor.AbstractActor
import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.common.exceptions.InvalidInputException
import org.kebs.app.kotlin.apollo.store.model.NotificationsBufferEntity
import org.kebs.app.kotlin.apollo.store.repo.INotificationsBufferRepository
import org.kebs.app.kotlin.apollo.store.repo.INotificationsRepository
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Scope
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.lang.reflect.Method

@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
class NotificationBufferProcessingActor(
        private val context: ApplicationContext,
        private val notificationsRepository: INotificationsRepository,
        private val bufferRepository: INotificationsBufferRepository
) : AbstractActor() {
    override fun createReceive(): Receive {
        return receiveBuilder()
                .match(NotificationsBufferEntity::class.java) { buffer ->
                    try {
                        //selectUseCase(buffer)
                    } catch (e: Exception) {
                        self().tell(e, self())

                    } finally {
                        context().stop(self)
                    }


                }.match(Exception::class.java) { e ->
                    /**
                     * Send an alert to support
                     */
                    try {
                        sendAlertToSupport(e)
                    } catch (e: Exception) {
                        KotlinLogging.logger { }.error(e.message, e)
                        KotlinLogging.logger { }.debug(e.message, e)
                        context().stop(self)
                    }


                }
                .build()
    }

    private fun sendAlertToSupport(e: Exception?) {
        KotlinLogging.logger { }.info { "Error escalation to support not yet implemented for ${e?.message}" }
        TODO("Not yet implemented")
    }

//    private fun selectUseCase(buffer: NotificationsBufferEntity?) {
//        notificationsRepository.findByIdOrNull(buffer?.notificationId)
//                ?.let { notification ->
//                    try {
//                        when (buffer?.status) {
//                            notification.serviceRequestId?.initStatus -> {
//                                notification.beanProcessorName
//                                        ?.let { beanName ->
//                                            context.getBean(beanName)
//                                                    .let { bean ->
//                                                        val beanClazz = bean::class.java
//                                                        val method: Method? = notification.methodName?.let { beanClazz.getMethod(it, NotificationsBufferEntity::class.java) }
//                                                        method?.invoke(bean, buffer)
//                                                    }
//                                        }
//
//                            }
//                            else -> {
//                                throw InvalidInputException("Received invalid notification request [id=${buffer?.id}]")
//
//                            }
//                        }
//
//
//                    } catch (e: Exception) {
//                        KotlinLogging.logger { }.error(e.message, e)
//                        KotlinLogging.logger { }.debug(e.message, e)
//                        buffer?.status = notification.serviceMapId?.exceptionStatus
//                        buffer?.responseStatus = notification.serviceMapId?.exceptionStatusCode
//                        buffer?.responseMessage = "${notification.serviceMapId}: ${e.message}"
//
//                    }
//
//                    buffer?.let { bufferRepository.save(it) }
//
//
//                }
//
//
//    }
}
