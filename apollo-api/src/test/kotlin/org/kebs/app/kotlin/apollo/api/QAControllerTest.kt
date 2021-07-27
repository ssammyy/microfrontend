package org.kebs.app.kotlin.apollo.api


import mu.KotlinLogging
import org.jasypt.encryption.StringEncryptor
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DaoService
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.QADaoServices
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.repo.IBatchJobDetailsRepository
import org.kebs.app.kotlin.apollo.store.repo.IIntegrationConfigurationRepository
import org.kebs.app.kotlin.apollo.store.repo.IMpesaTransactionsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.math.BigDecimal
import java.sql.Date
import java.sql.Timestamp
import java.time.Instant


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

    val smarkRate = PermitRatingDto().apply {
        id = 1L
        min = BigDecimal.ZERO
        max = BigDecimal.valueOf(200000L)
        productFee = BigDecimal.ZERO
        firmFee = BigDecimal.ZERO
        extraProductFee = BigDecimal.valueOf(5000L)
        countBeforeFee = BigDecimal.valueOf(5000L)
        countBeforeFree = 3
        validity = 2
        invoiceDesc = "PRODUCT CERTIFICATION(STANDARDIZATION MARK)"
        turnOver = BigDecimal.valueOf(150000L)
    }

    val mediumRate = PermitRatingDto().apply {
        id = 2L
        min = BigDecimal.valueOf(200000L)
        max = BigDecimal.valueOf(500000L)
        firmFee = BigDecimal.ZERO
        productFee = BigDecimal.ZERO
        extraProductFee = BigDecimal.valueOf(5000L)
        countBeforeFee = BigDecimal.valueOf(10000L)
        countBeforeFree = 3
        validity = 2
        invoiceDesc = "PRODUCT CERTIFICATION(STANDARDIZATION MARK)"
        turnOver = BigDecimal.valueOf(150000L)
    }

    val largeRate = PermitRatingDto().apply {
        id = 3L
        min = BigDecimal.valueOf(500000L)
        max = BigDecimal.valueOf(50000000000000000L)
        firmFee = BigDecimal.valueOf(20000L)
        productFee = BigDecimal.valueOf(7500L)
        extraProductFee = BigDecimal.ZERO
        countBeforeFee = BigDecimal.valueOf(10000L)
        countBeforeFree = 3
        validity = 2
        invoiceDesc = "PRODUCT CERTIFICATION(STANDARDIZATION MARK)"
        turnOver = BigDecimal.valueOf(150000L)
    }

    @Test
    fun invoiceGenerationGivenMultiplePermitsAppliedForTest() {
    }

    @Test
    fun invoiceGenerationGivenTurnOverAndProductCountTest() {
        val ratesMap = mutableListOf<PermitRatingDto>()
        val permitApplied = 5
        val productNumber = 5
        val testTurnOver = BigDecimal.valueOf(500000.01)

        ratesMap.add(smarkRate)
        ratesMap.add(mediumRate)
        ratesMap.add(largeRate)

        val selectedRate = ratesMap.filter {
            testTurnOver > it.min ?: BigDecimal.ZERO && testTurnOver <= it.max ?: throw NullValueNotAllowedException("Max needs to be defined")
        }.firstOrNull() ?: throw NullValueNotAllowedException("Rate not found")
        KotlinLogging.logger { }.info { "selectedRate fixed cost = ${selectedRate.id}" }

        val invoiceMaster = InvoiceMasterDto().apply {
            id = 2L
            invoiceRef = "REFINVOICE1258GGH"
            generatedDate = Timestamp.from(Instant.now())
            itemCount = 1
        }
        val fixedAmount = selectedRate.fixedCost ?: BigDecimal.ZERO
        val inspectionFee = selectedRate.firmFee ?: BigDecimal.ZERO
        val countBeforeDiscountFee = selectedRate.countBeforeFee ?: BigDecimal.ZERO
        val extraProducts =
            if (productNumber - (selectedRate.countBeforeFree ?: 0) > 0) productNumber - (selectedRate.countBeforeFree
                ?: 0) else 0
        val feeAfterDiscount = selectedRate.extraProductFee?.multiply(BigDecimal(extraProducts))

        var invoiceDetails = InvoiceDetailsDto().apply {
            id = 2L
            invoiceMasterId = invoiceMaster.id
            generatedDate = Timestamp.from(Instant.now())
            itemDesc = selectedRate.invoiceDesc
            quantity = BigDecimal.ZERO
            amount =
                fixedAmount?.plus(inspectionFee ?: BigDecimal.ZERO)?.plus(countBeforeDiscountFee ?: BigDecimal.ZERO)
                    ?.plus(feeAfterDiscount ?: BigDecimal.ZERO)
        }

        val detailsMap = mutableListOf<InvoiceDetailsDto>()
        detailsMap.add(invoiceDetails)
        invoiceDetails = InvoiceDetailsDto().apply {
            id = 3L
            invoiceMasterId = invoiceMaster.id
            generatedDate = Timestamp.from(Instant.now())
            itemDesc = "Per Diem"
            quantity = BigDecimal.valueOf(2L)
            amount = BigDecimal(3000)
        }
        detailsMap.add(invoiceDetails)

        invoiceMaster.totalAmount = detailsMap.sumOf { it.amount ?: BigDecimal.ZERO }

        KotlinLogging.logger { }.info { " invoiceMaster totalAmount = ${invoiceMaster.totalAmount}" }
//        (1..permitApplied).forEach {
//
//        }
    }


}

class PermitRatingDto {
    var id: Long? = null
    var min: BigDecimal? = null
    var max: BigDecimal? = null
    var fixedCost: BigDecimal? = null
    var firmFee: BigDecimal? = null
    var productFee: BigDecimal? = null
    var extraProductFee: BigDecimal? = null
    var countBeforeFee: BigDecimal? = null
    var countBeforeFree: Int? = null
    var validity: Int? = null
    var turnOver: BigDecimal? = null
    var invoiceDesc: String? = null
}

class InvoiceMasterDto {
    var id: Long? = null
    var invoiceRef: String? = null
    var generatedDate: Timestamp? = null
    var itemCount: Int? = null
    var totalAmount: BigDecimal? = null
    var amountInWords: String? = null
    var invoiceDetails: List<InvoiceDetailsDto>? = null
}

class InvoiceDetailsDto {
    var id: Long? = null
    var invoiceMasterId: Long? = null
    var generatedDate: Timestamp? = null
    var itemDesc: String? = null
    var quantity: BigDecimal? = null
    var amount: BigDecimal? = null
}
