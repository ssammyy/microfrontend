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

package org.kebs.app.kotlin.apollo.config.properties.cache

import com.ulisesbocchio.jasyptspringboot.annotation.EncryptablePropertySource
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration


@Configuration
@EncryptablePropertySource(value = ["file:\${CONFIG_PATH}/hazelcast-cache.properties"])
class HazelCastCacheProperties {
    @Value("\${org.app.properties.cache.hazelcast.default.instance.name}")
    val defaultInstanceName: String? = null

    @Value("\${org.app.properties.cache.hazelcast.default.name}")
    val defaultName: String? = null

    @Value("\${org.app.properties.cache.hazelcast.default.ttl}")
    val defaultTtl: Int? = null

    @Value("\${org.app.properties.cache.hazelcast.posts.name}")
    val postsName: String? = null

    @Value("\${org.app.properties.cache.hazelcast.posts.ttl}")
    val postsTtl: Int? = null
    @Value("\${org.app.properties.cache.hazelcast.paged.posts.name}")
    val pagedPostsName: String? = null
    @Value("\${org.app.properties.cache.hazelcast.pagedPosts.cache.ttl}")
    val pagedPostsCacheTtl: Int? = null
    @Value("\${org.app.properties.cache.hazelcast.site.statistics.name}")
    val siteStatisticsName: String? = null
    @Value("\${org.app.properties.cache.hazelcast.site.statistics.ttl}")
    val siteStatisticsTtl: Int? = null
    @Value("\${org.app.properties.cache.hazelcast.default.backup.count}")
    val defaultBackupCount: Int? = null
    @Value("\${org.app.properties.cache.hazelcast.posts.backup.count}")
    val postsBackupCount: Int? = null
    @Value("\${org.app.properties.cache.hazelcast.paged.posts.backup.count}")
    val pagedPostsBackupCount: Int? = null
    @Value("\${org.app.properties.cache.hazelcast.site.statistics.backup.count}")
    val siteStatisticsBackupCount: Int? = null
    @Value("\${org.app.properties.cache.hazelcast.default.size.max}")
    val defaultSizeMax: Int? = null
    @Value("\${org.app.properties.cache.hazelcast.posts.ttl.size.max}")
    val postsTtlSizeMax: Int? = null
    @Value("\${org.app.properties.cache.hazelcast.paged.posts.size.max}")
    val pagedPostsSizeMax: Int? = null
    @Value("\${org.app.properties.cache.hazelcast.posts.size.max}")
    val postsSizeMax: Int? = null
    @Value("\${org.app.properties.cache.hazelcast.site.statistics.size.max}")
    val siteStatisticsSizeMax: Int? = null
    @Value("\${org.app.properties.cache.hazelcast.near.cache.max.size}")
    val nearCacheMaxSize: Int? = null
    @Value("\${org.app.properties.cache.hazelcast.nearCache.max.idle.seconds}")
    val nearCacheMaxIdleSeconds: Int? = null
    @Value("\${org.app.properties.cache.hazelcast.near.cache.ttl}")
    val nearCacheTtl: Int? = null
    @Value("\${org.app.properties.cache.hazelcast.logging.type}")
    val loggingType: String? = null
    @Value("\${org.app.properties.cache.hazelcast.tcp.ip.timeout}")
    val tcpIpTimeout: String? = null
    @Value("\${org.app.properties.cache.hazelcast.tcp.ip.enabled}")
    val tcpIpEnabled: Boolean = false
    @Value("\${org.app.properties.cache.hazelcast.phone.home.enabled}")
    val phoneHomeEnabled: String? = null
    @Value("\${org.app.properties.cache.hazelcast.diagnostics.enabled}")
    val diagnosticsEnabled: String? = null
    @Value("\${org.app.properties.cache.hazelcast.multicast.enabled}")
    val multicastEnabled: Boolean = false
    @Value("\${org.app.properties.cache.hazelcast.multicast.trusted.interface.ip}")
    val multicastTrustedInterfaceIp: String? = null
    @Value("\${org.app.properties.cache.hazelcast.tcpip.members}")
    val tcpipMembers: MutableList<String>? = null
    @Value("\${org.app.properties.cache.hazelcast.tcpip.member.required}")
    val tcpipMemberRequired: String? = null
    @Value("\${org.app.properties.cache.hazelcast.interfaces.allowed.enabled}")
    val interfacesAllowedEnabled: Boolean = false
    @Value("\${org.app.properties.cache.hazelcast.interfaces.allowed.interface}")
    val interfacesAllowedInterface: String? = null
    @Value("\${org.app.properties.cache.hazelcast.interfaces.disallowed.enabled}")
    val interfacesDisallowedEnabled: String? = null
    @Value("\${org.app.properties.cache.hazelcast.interfaces.disallowed.interface}")
    val interfacesDisallowedInterface: String? = null
    @Value("\${org.app.properties.cache.hazelcast.hot.restart.persistence.base.dir}")
    val hotRestartPersistenceBaseDir: String? = null
}
