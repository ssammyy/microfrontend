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

package org.kebs.app.kotlin.apollo.ipc.adaptor.akka

import akka.actor.AbstractActor
import akka.actor.ActorSystem
import akka.actor.InvalidActorNameException
import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.common.exceptions.MissingConfigurationException
import org.kebs.app.kotlin.apollo.common.exceptions.ServiceMapNotFoundException
import org.kebs.app.kotlin.apollo.config.adaptor.akka.config.ActorSpringExtension
import org.kebs.app.kotlin.apollo.config.adaptor.akka.interfaces.Actor
import org.kebs.app.kotlin.apollo.ipc.dto.KafkaRequest
import org.kebs.app.kotlin.apollo.store.model.NotificationsBufferEntity


@Actor
class ProcessorSelectionActor(
    private val extension: ActorSpringExtension,
    private val actorSystem: ActorSystem
) : AbstractActor() {

//    private final val supervisorStrategyMaxRetries = akkaProperties.supervisorStrategyMaxRetries ?: 10
//    private final val supervisorStrategyMaxRetriesTimeRange = akkaProperties.supervisorStrategyMaxRetriesTimeRange ?: 1L
//
//    var behavior: Behavior<String> = Behaviors.empty()
//
//    fun supervision() {
//
//
//        // #multiple
//        Behaviors.supervise(
//            Behaviors.supervise(behavior)
//                .onFailure(IllegalStateException::class.java, akka.actor.typed.SupervisorStrategy.stop())
//        )
//            .onFailure(IllegalArgumentException::class.java, akka.actor.typed.SupervisorStrategy.stop())
//
//        // #multiple
//    }

//    private val strategy: SupervisorStrategy = OneForOneStrategy(supervisorStrategyMaxRetries, Duration.create(supervisorStrategyMaxRetriesTimeRange, TimeUnit.MINUTES),
//            DeciderBuilder
//                    .match(ArithmeticException::class.java) { SupervisorStrategy.stop() }
//                    .match(NullPointerException::class.java) { SupervisorStrategy.stop() }
//                    .match(IllegalArgumentException::class.java) { SupervisorStrategy.stop() }
//                    .matchAny { SupervisorStrategy.makeDecider() .escalate() }
//                    .build())

//    override fun supervisorStrategy(): SupervisorStrategy? {
//        return strategy
//    }

    override fun createReceive(): Receive {

        return receiveBuilder()
            .match(KafkaRequest::class.java) { request ->
                try {
                    request.serviceMapsDto?.let { map ->
                        map.actorClass?.let { actorBean ->
                            val actorRef = actorSystem.actorOf(extension.get(actorSystem).props(actorBean), actorBean)
//                                val testActor = actorSystem.actorOf(extension.get(actorSystem).props("testActor"), "dummyActor")

                            actorRef.tell(request, actorRef)
                        }
                            ?: throw ServiceMapNotFoundException("ServiceMap configuration for topic ${request.topic} not found")
                    }

                } catch (e: ServiceMapNotFoundException) {
                    self().tell(e, self())
                } catch (e: Exception) {
                    self().tell(e, self())
                }

            }
            .match(NotificationsBufferEntity::class.java) { b ->

                b.actorClassName
                    ?.let { actorBean ->

                        try {
                            val actorRef =
                                actorSystem.actorOf(extension[actorSystem].props(actorBean), "${actorBean}_${b.id}")
                            actorRef.tell(b, actorRef)
                        } catch (e: InvalidActorNameException) {
                            KotlinLogging.logger { }.error("Possible duplicate request ${e.message} ")
                            KotlinLogging.logger { }.debug(e.message, e)

                        }


                    }
                    ?: throw MissingConfigurationException("Received a BufferedNotification [id=${b.id}] with a null actorName, request aborted ")

            }
            .match(ServiceMapNotFoundException::class.java) { e ->
                KotlinLogging.logger { }.error(e.message, e)
                /**
                 * Send alert
                 */

            }.match(Exception::class.java) { e ->
                KotlinLogging.logger { }.error(e.message)
                KotlinLogging.logger { }.debug(e.message, e)
                /**
                 * Send alert
                 */

            }
            .build()
    }

}