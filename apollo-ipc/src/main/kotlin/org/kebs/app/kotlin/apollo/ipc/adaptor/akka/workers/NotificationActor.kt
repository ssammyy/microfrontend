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

import akka.actor.AbstractLoggingActor
import mu.KotlinLogging

import org.kebs.app.kotlin.apollo.store.model.ServiceRequestsEntity
import org.kebs.app.kotlin.apollo.store.repo.INotificationTypesRepository
import org.kebs.app.kotlin.apollo.store.repo.INotificationsRepository
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Scope
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.lang.reflect.Method

@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
class NotificationActor(
        val context: ApplicationContext,
        private val notificationsRepository: INotificationsRepository,
        private val notificationTypesRepository: INotificationTypesRepository
) : AbstractLoggingActor() {

    override fun createReceive(): Receive {
        return receiveBuilder()
                .match(ServiceRequestsEntity::class.java) { serviceRequestsEntity ->
                    try {
                        selectUseCase(serviceRequestsEntity)
                    } catch (e: Exception) {
                        context().sender().tell(serviceRequestsEntity, self())
                    }
                    context().stop(self)
                }.build()
    }

    private fun selectUseCase(serviceRequestsEntity: ServiceRequestsEntity) {
        notificationTypesRepository.findByIdOrNull(serviceRequestsEntity.notificationType)
                ?.let { type ->
                    notificationsRepository.findFirstByRequestTopicAndEventStatusAndNotificationTypeOrderByNotificationType(serviceRequestsEntity.kafkaTopic, serviceRequestsEntity.responseStatus, type)
                            ?.let {
                                KotlinLogging.logger { }.info(it.beanProcessorName)
                                it.beanProcessorName
                                        ?.let { beanName ->
                                            context.getBean(beanName)
                                                    .let { bean ->
                                                        val beanClazz = bean::class.java
                                                        val method: Method = beanClazz.getMethod(it.methodName!!, ServiceRequestsEntity::class.java)
                                                        method.invoke(bean, serviceRequestsEntity)
                                                    }

                                        }

                            }
                            ?: throw Exception("Could not find notification with topic: ${serviceRequestsEntity.kafkaTopic} and status ${serviceRequestsEntity.responseStatus}")

                }
                ?: throw Exception("Could not find notificationType with topic: ${serviceRequestsEntity.kafkaTopic} and status ${serviceRequestsEntity.responseStatus}")

    }
}