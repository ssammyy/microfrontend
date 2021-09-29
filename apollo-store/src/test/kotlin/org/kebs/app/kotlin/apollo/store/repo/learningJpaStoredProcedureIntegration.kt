package org.kebs.app.kotlin.apollo.store.repo

import mu.KotlinLogging
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.kebs.app.kotlin.apollo.store.repo.qa.IPermitApplicationsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension


@ExtendWith(SpringExtension::class)
@SpringBootTest
class StoredProcedureCallTests {
    @Autowired
    lateinit var procCall: IPermitApplicationsRepository

    @Test
    fun migratePermitsToNewUserTest() {
        val response = procCall.migratePermitsToNewUser(2046L, "10333", 378L)
        KotlinLogging.logger { }.info("The response is $response")

    }
}

