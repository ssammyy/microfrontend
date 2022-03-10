package org.kebs.app.kotlin.apollo.config

import mu.KotlinLogging
import org.jasypt.encryption.StringEncryptor
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.kebs.app.kotlin.apollo.config.properties.jpa.JpaConnectionProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
class ConfigApplicationTest {
    @Autowired
    lateinit var jpaConnectionProperties: JpaConnectionProperties

    @Autowired
    lateinit var jasyptStringEncryptor: StringEncryptor

    @Test
    fun contextLoads() {
        KotlinLogging.logger { }.info("USER: " + jasyptStringEncryptor.encrypt("BSKAPI"))
        KotlinLogging.logger { }.info("PWD: " + jasyptStringEncryptor.encrypt("P@\$\$word2021"))
    }

    @Test
    fun readValuesTest() {
        KotlinLogging.logger { }.info("${jpaConnectionProperties.cachePrepStmts} ${jpaConnectionProperties.prepStmtCacheSize}")

    }

}