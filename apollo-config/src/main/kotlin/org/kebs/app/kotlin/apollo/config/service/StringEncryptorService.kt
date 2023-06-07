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

package org.kebs.app.kotlin.apollo.config.service

import org.jasypt.encryption.StringEncryptor
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig
import org.kebs.app.kotlin.apollo.config.security.JasyptProperties
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class StringEncryptorService(
    private val jasyptProperties: JasyptProperties
) {
    @Bean("jasyptStringEncryptor")
    fun stringEncryptor(): StringEncryptor {
        val encryptor = PooledPBEStringEncryptor()
        val config = SimpleStringPBEConfig()
        config.password = jasyptProperties.password
        config.algorithm = jasyptProperties.algorithm
        config.keyObtentionIterations = jasyptProperties.keyObtentionIterations
        config.poolSize = jasyptProperties.poolSize
        config.providerName = jasyptProperties.providerName
        config.setSaltGeneratorClassName(jasyptProperties.saltGeneratorClassname)
        config.stringOutputType = jasyptProperties.stringOutputType
        encryptor.setConfig(config)
        return encryptor
    }
}