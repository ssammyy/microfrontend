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

package org.kebs.app.kotlin.apollo.config.adaptor.akka.config

import akka.actor.ActorSystem
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import org.kebs.app.kotlin.apollo.config.properties.akka.AkkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AkkaConfig(
    private val akkaProperties: AkkaProperties,
) {
    @Bean
    fun actorSystem(): ActorSystem? {
        return ActorSystem.create(akkaProperties.actorSystem)
//        try {
//             ActorSystem.create(akkaProperties.actorSystem)
//        }catch (e:Exception){
//            KotlinLogging.logger {  }.error(e.message,e)
//            null
//        }
        //        springExtension.initialize(applicationContext)

    }

    @Bean
    fun akkaConfiguration(): Config {
        return ConfigFactory.load()
    }
}