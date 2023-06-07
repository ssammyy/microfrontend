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

import mu.KotlinLogging
import org.jasypt.encryption.StringEncryptor
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.kebs.app.kotlin.apollo.config.properties.jpa.JpaConnectionProperties
import org.kebs.app.kotlin.apollo.config.properties.notifs.NotifsProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension


@ExtendWith(SpringExtension::class)
@SpringBootTest
internal class HashingImplementationTest {
    @Autowired
    lateinit var jasyptStringEncryptor: StringEncryptor


    @Autowired
    lateinit var jpaConnectionProperties: JpaConnectionProperties

    @Autowired
    lateinit var notifsProperties: NotifsProperties

    @Test
    fun unHashFromPropertyFile() {
        KotlinLogging.logger { }.trace ( jpaConnectionProperties.generateStatistics )
        KotlinLogging.logger { }.trace ( notifsProperties.password )
    }

    @Test
    fun hashString() {
        val plainText = listOf("apollo", "kebsuat", "4Wow99")

        plainText.forEach {
            val hashed = jasyptStringEncryptor.encrypt(it)
            KotlinLogging.logger { }.info { "my hashed value = $hashed" }
        }


    }

    @Test
    fun unHashString() {
        val hashed = listOf("AbqmSDf+HCN58dVia0M7jA==", "AbqmSDf+HCN58dVia0M7jA==")
        hashed.forEach {
            val plainText = jasyptStringEncryptor.decrypt(it)
            KotlinLogging.logger { }.info { plainText }
        }

    }


}
