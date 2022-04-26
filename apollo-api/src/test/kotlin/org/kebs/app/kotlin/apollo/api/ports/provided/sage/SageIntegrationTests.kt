package org.kebs.app.kotlin.apollo.api.ports.provided.sage

import mu.KotlinLogging
import org.jasypt.encryption.StringEncryptor
import org.junit.Assert
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.service.BillStatus
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.invoice.CorporateCustomerAccounts
import org.kebs.app.kotlin.apollo.store.repo.IBillPaymentsRepository
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

    @Autowired
    lateinit var billPaymentRepository: IBillPaymentsRepository

    @Autowired
    lateinit var jasyptStringEncryptor: StringEncryptor

    @Test
    fun encryptPasswords() {
        KotlinLogging.logger { }.info("USER: " + jasyptStringEncryptor.encrypt("BSKAPI"))
        KotlinLogging.logger { }.info("PWD: " + jasyptStringEncryptor.encrypt("P@\$\$word2021"))
    }

    @Test
    fun demandNoteSubmission() {
        this.demandNoteRepository.findFirstByPaymentStatusAndCdRefNoIsNotNullOrderByCreatedOnDesc(0)?.let { demandNote ->
            val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
            KotlinLogging.logger { }.info("CdRef No: ${demandNote.cdRefNo}")
            sageService.postInvoiceTransactionToSage(demandNote, "test", map)
            Assert.assertEquals(1, demandNote.postingStatus)
            Assert.assertNotNull("Expected remote reference", demandNote.postingReference)
            Thread.sleep(TimeUnit.SECONDS.toMillis(20))
        } ?: throw ExpectedDataNotFound("Could not find a single payment")
    }

    @Test
    fun invoiceSubmission() {
        this.billPaymentRepository.findFirstByBillStatusAndPaymentStatusAndPostingStatusOrderByCreateOnDesc(BillStatus.CLOSED.status, 0, 0)?.let { billPayment ->
            val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
            KotlinLogging.logger { }.info("Invoice Number No: ${billPayment.invoiceNumber}")
            sageService.postInvoiceTransactionToSage(billPayment, "test", CorporateCustomerAccounts(), map)
            Assert.assertEquals(1, billPayment.postingStatus)
            Assert.assertNotNull("Expected remote reference", billPayment.paymentRequestReference)
            Thread.sleep(TimeUnit.SECONDS.toMillis(20))
        } ?: throw ExpectedDataNotFound("Could not find a single bill")
    }
}