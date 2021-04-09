package org.kebs.app.kotlin.apollo.api.flux

import mu.KotlinLogging
import org.jasypt.encryption.StringEncryptor
import org.junit.jupiter.api.Test
import org.kebs.app.kotlin.apollo.config.properties.notifs.NotifsProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ApiFluxApplicationTests {
    @Autowired
    lateinit var jasyptStringEncryptor: StringEncryptor


    @Autowired
    lateinit var notifsProperties: NotifsProperties

    @Test
    fun unHashFromPropertyFile() {

        KotlinLogging.logger { }.trace(notifsProperties.password)
    }

    @Test
    fun hashString() {
        val plainText = listOf("apollo", "BSKL3tm31n2021")

        plainText.forEach {
            val hashed = jasyptStringEncryptor.encrypt(it)
            KotlinLogging.logger { }.info { "my hashed value = $hashed" }
        }


    }

    @Test
    fun contextLoads() {
    }

}
