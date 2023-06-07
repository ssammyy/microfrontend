package org.kebs.app.kotlin.apollo.api.flux.ports.provided.dao

import mu.KotlinLogging
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.kebs.app.kotlin.apollo.store.repo.IIntegrationConfigurationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.junit.jupiter.SpringExtension

/**
 * @project apollo
 * @author Kenneth KZM Muhia<muhia></muhia>@muhia.org>
 * @date 3/27/21 7:35 AM
 */
@ExtendWith(SpringExtension::class)
@SpringBootTest
class DaoServiceTest {

    @Autowired
    lateinit var integRepo: IIntegrationConfigurationRepository


    @Test
    fun base256EncodeTest() {
        val input = "120-04-2022T12:45:18"
        integRepo.findByIdOrNull(26L)
            ?.let {
                KotlinLogging.logger { }.info(base256Encode(input, it))
            }

    }
}

