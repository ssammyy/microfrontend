package org.kebs.app.kotlin.apollo.config

import mu.KotlinLogging
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

    @Test
    fun contextLoads() {

    }

    @Test
    fun readValuesTest() {
        KotlinLogging.logger { }.info("${jpaConnectionProperties.cachePrepStmts} ${jpaConnectionProperties.prepStmtCacheSize}")

    }

}