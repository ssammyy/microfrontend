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

package org.kebs.app.kotlin.apollo.store.config.cache

import com.hazelcast.config.Config
import com.hazelcast.config.EvictionPolicy
import com.hazelcast.config.MapConfig
import com.hazelcast.config.NearCacheConfig
import com.hazelcast.core.Hazelcast
import com.hazelcast.core.HazelcastInstance
import org.kebs.app.kotlin.apollo.config.properties.cache.HazelCastCacheProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.hazelcast.HazelcastKeyValueAdapter
import org.springframework.data.keyvalue.core.KeyValueOperations
import org.springframework.data.keyvalue.core.KeyValueTemplate


@Configuration
//@ConditionalOnProperty(prefix = "using.spring.hazelcast", name = ["enabled"], havingValue = "true")
class HazelcastCacheConfigurer(
    private val properties: HazelCastCacheProperties
) : Config(properties.defaultInstanceName) {

    @Bean
    fun hazelCastConfig(): Config? {
        val config = Config()
        config.instanceName = properties.defaultInstanceName
        config.setProperty("hazelcast.logging.type", properties.loggingType)
        config.setProperty("hazelcast.diagnostics.enabled", properties.diagnosticsEnabled)
        config.setProperty("hazelcast.phone.home.enabled", properties.phoneHomeEnabled)
        config.setProperty("hazelcast.socket.server.bind.any", "false")
        val networkConfig = config.networkConfig
//        val interfacesConfig = networkConfig.interfaces
//        interfacesConfig.setInterfaces(Collections.singletonList(properties.multicastTrustedInterfaceIp)).isEnabled = true
        val joinConfig = networkConfig.join
        val ints = mutableSetOf<String>()
        properties.multicastTrustedInterfaceIp?.let { interfaces ->
            interfaces.split(",").stream().forEach { ints.add(it) }
            joinConfig.multicastConfig
                .setLoopbackModeEnabled(true)
                .setEnabled(true)
                .setTrustedInterfaces(ints)
        }

        networkConfig.join = joinConfig

        config.networkConfig = networkConfig

//        joinConfig.tcpIpConfig.setMembers(properties.tcpipMembers).setEnabled(true)
        val defaultCache = MapConfig()
        defaultCache.name = properties.defaultName
        properties.defaultTtl?.let { defaultCache.timeToLiveSeconds = it }

        defaultCache.evictionConfig.evictionPolicy = EvictionPolicy.LFU
        properties.defaultBackupCount?.let { defaultCache.backupCount = it }
//        properties.defaultSizeMax?.let { defaultCache.maxSizeConfig.size = it }
        val nearCacheConfig = NearCacheConfig()
        properties.nearCacheMaxIdleSeconds
            ?.let { maxIdleSeconds ->
                nearCacheConfig.maxIdleSeconds = maxIdleSeconds
                properties.nearCacheTtl?.let { nearCacheConfig.setTimeToLiveSeconds(it) }

            }

        defaultCache.nearCacheConfig = nearCacheConfig
        config.mapConfigs[properties.defaultName] = defaultCache
//        val mergePolicyConfig = MergePolicyConfig()
//        properties.defaultBackupCount?.let {
//            mergePolicyConfig
//                    .setPolicy("com.hazelcast.map.merge.PutIfAbsentMapMergePolicy")
//                    .batchSize = it
//
//        }


        return config
    }

//    fun managementCenterConfig(): ManagementCenterConfig? {
//        val manCenterCfg = ManagementCenterConfig()
//        manCenterCfg. isEnabled = true
//        manCenterCfg.url = "http://localhost:8080/mancenter"
//        manCenterCfg.updateInterval = 5
//        return manCenterCfg
//    }


    fun hazelcastInstance(): HazelcastInstance {
        return Hazelcast.getOrCreateHazelcastInstance(hazelCastConfig())
    }

    @Bean
    fun keyValueTemplate(): KeyValueOperations? {
        return KeyValueTemplate(hazelcastKeyValueAdapter())
    }


    @Bean
    fun hazelcastKeyValueAdapter(): HazelcastKeyValueAdapter {

        return HazelcastKeyValueAdapter(hazelcastInstance())
    }

}

