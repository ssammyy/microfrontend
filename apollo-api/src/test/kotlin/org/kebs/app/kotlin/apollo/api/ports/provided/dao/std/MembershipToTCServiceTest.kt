package org.kebs.app.kotlin.apollo.api.ports.provided.dao.std

import mu.KotlinLogging
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.kebs.app.kotlin.apollo.common.utils.generateRandomText
import java.security.SecureRandom

class MembershipToTCServiceTest {


    @Test
    fun password(): Unit {
        val password = generateRandomText(12, "SHA1PRNG", "SHA-512")

        println(password)
        KotlinLogging.logger { }.info { "complaint = ${password} " }
    }

}
