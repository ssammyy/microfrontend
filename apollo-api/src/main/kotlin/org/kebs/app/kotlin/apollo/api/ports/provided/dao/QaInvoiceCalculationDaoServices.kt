package org.kebs.app.kotlin.apollo.api.ports.provided.dao

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.common.utils.generateRandomText
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import org.kebs.app.kotlin.apollo.store.model.qa.*
import org.kebs.app.kotlin.apollo.store.repo.di.ICfgMoneyTypeCodesRepository
import org.kebs.app.kotlin.apollo.store.repo.qa.IPermitRatingRepository
import org.kebs.app.kotlin.apollo.store.repo.qa.IQaInvoiceDetailsRepository
import org.kebs.app.kotlin.apollo.store.repo.qa.IQaInvoiceMasterDetailsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.sql.Timestamp
import java.time.Instant

@Service
class QaInvoiceCalculationDaoServices(
    private val applicationMapProperties: ApplicationMapProperties,
//    private val iTurnOverRatesRepo: ITurnOverRatesRepository,
    private val iMoneyTypeCodesRepo: ICfgMoneyTypeCodesRepository,
    private val iPermitRatingRepo: IPermitRatingRepository,
    private val qaInvoiceDetailsRepo: IQaInvoiceDetailsRepository,
    private val qaInvoiceMasterDetailsRepo: IQaInvoiceMasterDetailsRepository,
    private val commonDaoServices: CommonDaoServices
//    private val qADaoServices: QADaoServices
) {

    @Lazy
    @Autowired
    lateinit var qaDaoServices: QADaoServices

    var appId = applicationMapProperties.mapQualityAssurance


    fun calculatePaymentSMark(
        permit: PermitApplicationsEntity,
        user: UsersEntity,
        manufactureTurnOver: BigDecimal,
        productNumber: Int,
        plantDetail: ManufacturePlantDetailsEntity
    ): QaInvoiceMasterDetailsEntity {
        val map = commonDaoServices.serviceMapDetails(appId)
        val permitType = qaDaoServices.findPermitType(permit.permitType ?: throw Exception("INVALID PERMIT TYPE ID"))
        val numberOfYears = BigDecimal(permitType.numberOfYears ?: throw Exception("INVALID NUMBER OF YEARS"))
        val userDetails =
            commonDaoServices.findUserByID(permit.userId ?: throw Exception("MISSING USER ID ON PERMIT DETAILS"))

        val ratesMap =
            iPermitRatingRepo.findAllByStatus(map.activeStatus) ?: throw Exception("SMARK RATE SHOULD NOT BE NULL")
        val selectedRate = ratesMap.filter {
            manufactureTurnOver > it.min ?: BigDecimal.ZERO && manufactureTurnOver <= it.max ?: throw NullValueNotAllowedException(
                "Max needs to be defined"
            )
        }.firstOrNull() ?: throw NullValueNotAllowedException("Rate not found")

        KotlinLogging.logger { }.info { "selected Rate fixed cost = ${selectedRate.id} and  ${selectedRate.firmType}" }


        val inspectionFee = selectedRate.firmFee ?: BigDecimal.ZERO.multiply(numberOfYears)
        val countBeforeDiscountFee = selectedRate.countBeforeFee ?: BigDecimal.ZERO.multiply(numberOfYears)
        val extraProducts =
            if (productNumber - (selectedRate.countBeforeFree ?: 0) > 0) productNumber - (selectedRate.countBeforeFree
                ?: 0) else 0
        val feeAfterDiscount =
            selectedRate.extraProductFee?.multiply(BigDecimal(extraProducts))?.multiply(numberOfYears)

        var stgAmount = BigDecimal.ZERO
        when {
            plantDetail.paidDate == null && plantDetail.endingDate == null && plantDetail.inspectionFeeStatus == null -> {
                stgAmount = inspectionFee ?: BigDecimal.ZERO.plus(countBeforeDiscountFee ?: BigDecimal.ZERO)
                    .plus(feeAfterDiscount ?: BigDecimal.ZERO)
                with(plantDetail) {
                    inspectionFeeStatus = 1
                    paidDate = commonDaoServices.getCurrentDate()
                    endingDate = commonDaoServices.addYearsToCurrentDate(
                        permitType.numberOfYears ?: throw Exception("INVALID NUMBER OF YEARS")
                    )
                }
                qaDaoServices.updatePlantDetails(map, user, plantDetail)
            }
            commonDaoServices.getCurrentDate() > plantDetail.paidDate && commonDaoServices.getCurrentDate() < plantDetail.endingDate && plantDetail.inspectionFeeStatus == 1 -> {
                stgAmount = countBeforeDiscountFee ?: BigDecimal.ZERO.plus(feeAfterDiscount ?: BigDecimal.ZERO)
            }
        }


        var invoiceMaster = QaInvoiceMasterDetailsEntity().apply {
            permitId = permit.id
            permitRefNumber = permit.permitRefNumber
            userId = permit.userId
            invoiceRef =
                "KIMSREF${generateRandomText(3, map.secureRandom, map.messageDigestAlgorithm, true).toUpperCase()}"
            generatedDate = Timestamp.from(Instant.now())
            itemCount = 1
            status = 0
            createdOn = Timestamp.from(Instant.now())
            createdBy = commonDaoServices.concatenateName(user)
        }

        invoiceMaster = qaInvoiceMasterDetailsRepo.save(invoiceMaster)

        var invoiceDetails = QaInvoiceDetailsEntity().apply {
            invoiceMasterId = invoiceMaster.id
            umo = "PER"
            generatedDate = Timestamp.from(Instant.now())
            itemDescName = selectedRate.invoiceDesc
            itemQuantity = BigDecimal.ZERO
            itemAmount = stgAmount
            createdOn = Timestamp.from(Instant.now())
            createdBy = commonDaoServices.concatenateName(user)
        }

        invoiceDetails = qaInvoiceDetailsRepo.save(invoiceDetails)

        with(invoiceMaster) {
            description = invoiceDetails.itemDescName
            paymentStatus = 0
            taxAmount = invoiceDetails.itemAmount ?: BigDecimal.ZERO.multiply(selectedRate.taxRate)
            subTotalBeforeTax = invoiceDetails.itemAmount
            totalAmount = invoiceDetails.itemAmount ?: BigDecimal.ZERO.plus(taxAmount ?: BigDecimal.ZERO)
            modifiedOn = Timestamp.from(Instant.now())
            modifiedBy = commonDaoServices.concatenateName(user)
        }

        invoiceMaster = qaInvoiceMasterDetailsRepo.save(invoiceMaster)

        KotlinLogging.logger { }.info { "invoiceMaster total Amount = ${invoiceMaster.totalAmount}" }

        return invoiceMaster
    }

    fun calculatePaymentDMark(
        permit: PermitApplicationsEntity,
        user: UsersEntity,
        permitType: PermitTypesEntity
    ): QaInvoiceMasterDetailsEntity {
        val map = commonDaoServices.serviceMapDetails(appId)
        val numberOfYears = permitType.numberOfYears?.toBigDecimal() ?: throw Exception("INVALID NUMBER OF YEARS")

        var stgAmount: BigDecimal? = null
        var taxAmountToPay: BigDecimal? = null
        var amountToPay: BigDecimal? = null
        val inspectionCostValue: BigDecimal? = null

        when (permit.permitForeignStatus) {
            applicationMapProperties.mapQaDmarkDomesticStatus -> {

                val applicationCostValue = permitType.dmarkLocalAmount?.times(numberOfYears)

                stgAmount = applicationCostValue
                taxAmountToPay = stgAmount?.let { permitType.taxRate?.times(it) }
                amountToPay = taxAmountToPay?.let { stgAmount?.plus(it) }

            }
            applicationMapProperties.mapQaDmarkForeginStatus -> {
                val foreignAmountCalculated =
                    iMoneyTypeCodesRepo.findByTypeCode(applicationMapProperties.mapUssRateName)?.typeCodeValue?.toBigDecimal()
                val applicationCostValue = permitType.dmarkForeignAmount?.times(numberOfYears)
                    ?.times(foreignAmountCalculated ?: throw Exception("INVALID AMOUNT CONVERSION RATE"))

                stgAmount = applicationCostValue
                taxAmountToPay = stgAmount?.let { permitType.taxRate?.times(it) }
                amountToPay = taxAmountToPay?.let { stgAmount?.plus(it) }

            }
        }

        var invoiceMaster = QaInvoiceMasterDetailsEntity().apply {
            permitId = permit.id
            permitRefNumber = permit.permitRefNumber
            userId = permit.userId
            invoiceRef =
                "KIMSREF${generateRandomText(3, map.secureRandom, map.messageDigestAlgorithm, true).toUpperCase()}"
            generatedDate = Timestamp.from(Instant.now())
            itemCount = 1
            status = 0
            createdOn = Timestamp.from(Instant.now())
            createdBy = commonDaoServices.concatenateName(user)
        }

        invoiceMaster = qaInvoiceMasterDetailsRepo.save(invoiceMaster)

        var invoiceDetails = QaInvoiceDetailsEntity().apply {
            invoiceMasterId = invoiceMaster.id
            generatedDate = Timestamp.from(Instant.now())
            umo = "PER"
            itemDescName = permitType.itemInvoiceDesc
            itemQuantity = BigDecimal.ZERO
            itemAmount = stgAmount
            createdOn = Timestamp.from(Instant.now())
            createdBy = commonDaoServices.concatenateName(user)
        }

        invoiceDetails = qaInvoiceDetailsRepo.save(invoiceDetails)

        with(invoiceMaster) {
            description = invoiceDetails.itemDescName
            paymentStatus = 0
            taxAmount = taxAmountToPay
            subTotalBeforeTax = invoiceDetails.itemAmount
            totalAmount = amountToPay
            modifiedOn = Timestamp.from(Instant.now())
            modifiedBy = commonDaoServices.concatenateName(user)
        }

        invoiceMaster = qaInvoiceMasterDetailsRepo.save(invoiceMaster)

        KotlinLogging.logger { }.info { "invoiceMaster total Amount = ${invoiceMaster.totalAmount}" }

        return invoiceMaster
    }

    fun calculatePaymentFMark(
        permit: PermitApplicationsEntity,
        user: UsersEntity,
        permitType: PermitTypesEntity
    ): QaInvoiceMasterDetailsEntity {
        val map = commonDaoServices.serviceMapDetails(appId)
        val numberOfYears = permitType.numberOfYears?.toBigDecimal() ?: throw Exception("INVALID NUMBER OF YEARS")

        val applicationCostValue: BigDecimal? = permitType.fmarkAmount?.times(numberOfYears)


        val stgAmount = applicationCostValue
        val taxAmountToPay = stgAmount?.let { permitType.taxRate?.times(it) }
        val amountToPay = taxAmountToPay?.let { stgAmount.plus(it) }

        var invoiceMaster = QaInvoiceMasterDetailsEntity().apply {
            permitId = permit.id
            permitRefNumber = permit.permitRefNumber
            userId = permit.userId
            invoiceRef =
                "KIMSREF${generateRandomText(3, map.secureRandom, map.messageDigestAlgorithm, true).toUpperCase()}"
            generatedDate = Timestamp.from(Instant.now())
            itemCount = 1
            status = 0
            createdOn = Timestamp.from(Instant.now())
            createdBy = commonDaoServices.concatenateName(user)
        }

        invoiceMaster = qaInvoiceMasterDetailsRepo.save(invoiceMaster)

        var invoiceDetails = QaInvoiceDetailsEntity().apply {
            invoiceMasterId = invoiceMaster.id
            generatedDate = Timestamp.from(Instant.now())
            umo = "PER"
            itemDescName = permitType.itemInvoiceDesc
            itemQuantity = BigDecimal.ZERO
            itemAmount = stgAmount
            createdOn = Timestamp.from(Instant.now())
            createdBy = commonDaoServices.concatenateName(user)
        }

        invoiceDetails = qaInvoiceDetailsRepo.save(invoiceDetails)

        with(invoiceMaster) {
            description = invoiceDetails.itemDescName
            paymentStatus = 0
            taxAmount = taxAmountToPay
            subTotalBeforeTax = invoiceDetails.itemAmount
            totalAmount = amountToPay
            modifiedOn = Timestamp.from(Instant.now())
            modifiedBy = commonDaoServices.concatenateName(user)
        }

        invoiceMaster = qaInvoiceMasterDetailsRepo.save(invoiceMaster)

        KotlinLogging.logger { }.info { "invoiceMaster total Amount = ${invoiceMaster.totalAmount}" }

        return invoiceMaster
    }
}