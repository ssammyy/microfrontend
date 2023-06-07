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

package org.kebs.app.kotlin.apollo.config.properties.integ

import com.ulisesbocchio.jasyptspringboot.annotation.EncryptablePropertySource
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
@EncryptablePropertySource(value = ["file:\${CONFIG_PATH}/async-http-client.properties"])
class AsyncHttpClientProperties {
    @Value("\${org.app.properties.ahc.max.total.connections}")
    val maxTotalConnections: Int? = null
    @Value("\${org.app.properties.ahc.max.total.connections.per.host}")
    val maxTotalConnectionsPerHost: Int? = null
    @Value("\${org.app.properties.ahc.max.idle.time}")
    val maxIdleTime: Int? = null
    @Value("\${org.app.properties.ahc.request.timeout}")
    val requestTimeout: Int? = null
    @Value("\${org.app.properties.ahc.compression.enforced}")
    val isCompressionEnforced: Boolean = false
    @Value("\${org.app.properties.ahc.pooledconnectionidletimeout}")
    val pooledconnectionidletimeout: Int? = null


}
