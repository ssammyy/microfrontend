package org.kebs.app.kotlin.apollo.api.ports.provided.sage

import mu.KotlinLogging
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.repo.di.IDemandNoteRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import java.util.concurrent.TimeUnit

@SpringBootTest
@RunWith(SpringRunner::class)
class SageIntegrationTests {
    @Autowired
    lateinit var sageService: PostInvoiceToSageServices

    @Autowired
    lateinit var applicationMapProperties: ApplicationMapProperties

    @Autowired
    lateinit var commonDaoServices: CommonDaoServices

    @Autowired
    lateinit var demandNoteRepository: IDemandNoteRepository

    @Test
    fun demandNoteSubmission() {
        this.demandNoteRepository.findFirstByPaymentStatusAndCdRefNoIsNotNullOrderByCreatedOnDesc(0)?.let { demandNote ->
            val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
            KotlinLogging.logger { }.info("CdRef No: ${demandNote.cdRefNo}")
            sageService.postInvoiceTransactionToSage(demandNote, "test", map)
            Thread.sleep(TimeUnit.SECONDS.toMillis(20))
        } ?: throw ExpectedDataNotFound("Could not find a single payment")
    }
}