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

package org.kebs.app.kotlin.apollo.config.security

import com.ulisesbocchio.jasyptspringboot.annotation.EncryptablePropertySource
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration


@Configuration
@EncryptablePropertySource("file:\${CONFIG_PATH}/organization.properties")
class JasyptProperties {
    @Value("\${org.app.properties.jasypt.encryptor.password}")
    lateinit var password: String

    @Value("\${org.app.properties.jasypt.encryptor.algorithm}")
    val algorithm: String? = null

    @Value("\${org.app.properties.jasypt.encryptor.keyObtentionIterations}")
    val keyObtentionIterations: Int? = null

    @Value("\${org.app.properties.jasypt.encryptor.poolSize}")
    val poolSize: Int? = null

    @Value("\${org.app.properties.jasypt.encryptor.providerName}")
    val providerName: String? = null

    @Value("\${org.app.properties.jasypt.encryptor.saltGeneratorClassname}")
    val saltGeneratorClassname: String? = null

    @Value("\${org.app.properties.jasypt.encryptor.stringOutputType}")
    val stringOutputType: String? = null
}
