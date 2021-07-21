package org.kebs.app.kotlin.apollo.api


import mu.KotlinLogging
import org.jasypt.encryption.StringEncryptor
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DaoService
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.QADaoServices
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.repo.IBatchJobDetailsRepository
import org.kebs.app.kotlin.apollo.store.repo.IIntegrationConfigurationRepository
import org.kebs.app.kotlin.apollo.store.repo.IMpesaTransactionsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension


@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class QAControllerTest {

    @Autowired
    lateinit var daoService: DaoService

    @Autowired
    lateinit var applicationMapProperties: ApplicationMapProperties

    @Autowired
    lateinit var iMpesaTransactionsRepo: IMpesaTransactionsRepository

    @Autowired
    lateinit var configurationRepository: IIntegrationConfigurationRepository

    @Autowired
    lateinit var batchJobRepository: IBatchJobDetailsRepository

    @Autowired
    lateinit var commonDaoServices: CommonDaoServices

    @Autowired
    lateinit var jasyptStringEncryptor: StringEncryptor

    @Autowired
    lateinit var  qaDaoServices: QADaoServices

    @Test
    fun complaintDetails() {
        val appId = applicationMapProperties.mapMarketSurveillance
        val map = commonDaoServices.serviceMapDetails(appId)

        val allUnpaidInvoices= qaDaoServices.findALlPermitInvoicesCreatedByUserWithNoPaymentStatus(1765, map.inactiveStatus)
        val complaint = qaDaoServices.listPermitsInvoices(allUnpaidInvoices,203, map)
        KotlinLogging.logger { }.info { "complaint = $complaint " }
    }

    @Test
    fun paymentDetails() {
        val appId = applicationMapProperties.mapMarketSurveillance
        val map = commonDaoServices.serviceMapDetails(appId)

        val allUnpaidInvoices =
            qaDaoServices.calculatePayment(
                qaDaoServices.findPermitBYID(1215),
                map,
                commonDaoServices.findUserByID(2046)
            )
//        KotlinLogging.logger { }.info { "complaint = ${complaint.toString()} " }
    }

    @Test
    fun permitDetails() {
        val appId = applicationMapProperties.mapMarketSurveillance
        val map = commonDaoServices.serviceMapDetails(appId)

        val allUnpaidInvoices =
            qaDaoServices.permitRejectedVersionCreation(842, map, commonDaoServices.findUserByID(2046))
//        KotlinLogging.logger { }.info { "complaint = ${complaint.toString()} " }
    }


}

