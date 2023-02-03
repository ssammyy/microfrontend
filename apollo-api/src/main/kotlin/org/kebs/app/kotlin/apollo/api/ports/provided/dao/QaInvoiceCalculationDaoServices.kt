package org.kebs.app.kotlin.apollo.api.ports.provided.dao

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.common.utils.generateRandomText
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.ServiceMapsEntity
import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import org.kebs.app.kotlin.apollo.store.model.qa.*
import org.kebs.app.kotlin.apollo.store.model.registration.CompanyProfileEntity
import org.kebs.app.kotlin.apollo.store.repo.di.ICfgMoneyTypeCodesRepository
import org.kebs.app.kotlin.apollo.store.repo.qa.IPermitApplicationsRepository
import org.kebs.app.kotlin.apollo.store.repo.qa.IPermitRatingRepository
import org.kebs.app.kotlin.apollo.store.repo.qa.IQaInvoiceDetailsRepository
import org.kebs.app.kotlin.apollo.store.repo.qa.IQaInvoiceMasterDetailsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.sql.Timestamp
import java.time.Instant

@Service
class QaInvoiceCalculationDaoServices(
    private val applicationMapProperties: ApplicationMapProperties,
//    private val iTurnOverRatesRepo: ITurnOverRatesRepository,
    private val iMoneyTypeCodesRepo: ICfgMoneyTypeCodesRepository,
    private val iPermitRatingRepo: IPermitRatingRepository,
    private val permitRepo: IPermitApplicationsRepository,
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
        productNumber: Long,
        plantDetail: ManufacturePlantDetailsEntity
    ): QaInvoiceMasterDetailsEntity {
        val map = commonDaoServices.serviceMapDetails(appId)
        val permitType = qaDaoServices.findPermitType(permit.permitType ?: throw Exception("INVALID PERMIT TYPE ID"))
        BigDecimal(permitType.numberOfYears ?: throw Exception("INVALID NUMBER OF YEARS"))
        commonDaoServices.findUserByID(permit.userId ?: throw Exception("MISSING USER ID ON PERMIT DETAILS"))

        val ratesMap = iPermitRatingRepo.findAllByStatus(map.activeStatus) ?: throw Exception("SMARK RATE SHOULD NOT BE NULL")
        val selectedRate = ratesMap.firstOrNull { manufactureTurnOver > (it.min ?: BigDecimal.ZERO) && manufactureTurnOver <= (it.max ?: throw NullValueNotAllowedException("Max needs to be defined")) } ?: throw NullValueNotAllowedException("Rate not found")

        KotlinLogging.logger { }.info { "selected Rate fixed cost = ${selectedRate.id} and  ${selectedRate.firmType}" }

        var invoiceMaster = generateInvoiceMasterDetail(permit, map, user)

        when {
            applicationMapProperties.mapQASmarkLargeFirmsTurnOverId == selectedRate.id -> {
                calculatePaymentSMarkLargeFirm(permit, invoiceMaster, map, user, plantDetail, selectedRate)
            }
            applicationMapProperties.mapQASmarkMediumTurnOverId == selectedRate.id -> {
                calculatePaymentSMarkMediumOrSmallFirm(
                    permit,
                    invoiceMaster,
                    map,
                    user,
                    plantDetail,
                    selectedRate,
                    productNumber
                )
            }
            applicationMapProperties.mapQASmarkJuakaliTurnOverId == selectedRate.id -> {
                calculatePaymentSMarkMediumOrSmallFirm(
                    permit,
                    invoiceMaster,
                    map,
                    user,
                    plantDetail,
                    selectedRate,
                    productNumber
                )
            }
        }

        if (permit.fmarkGenerateStatus == 1) {
            calculatePaymentFMarkOnSMark(permit, user, qaDaoServices.findPermitType(applicationMapProperties.mapQAPermitTypeIdFmark), invoiceMaster)
        }


        invoiceMaster = calculateTotalInvoiceAmountToPay(invoiceMaster, permitType, user)


        KotlinLogging.logger { }.info { "invoice Master total Amount = ${invoiceMaster.totalAmount}" }

        return invoiceMaster
    }

    fun calculatePaymentSMarkAfterFirmUpgrade(
        permit: PermitApplicationsEntity,
        user: UsersEntity,
        manufactureTurnOver: BigDecimal,
        productNumber: Long,
        plantDetail: ManufacturePlantDetailsEntity
    ): QaInvoiceMasterDetailsEntity {
        val map = commonDaoServices.serviceMapDetails(appId)
        val permitType = qaDaoServices.findPermitType(permit.permitType ?: throw Exception("INVALID PERMIT TYPE ID"))
        BigDecimal(permitType.numberOfYears ?: throw Exception("INVALID NUMBER OF YEARS"))
        commonDaoServices.findUserByID(permit.userId ?: throw Exception("MISSING USER ID ON PERMIT DETAILS"))

        val ratesMap = iPermitRatingRepo.findAllByStatus(map.activeStatus) ?: throw Exception("SMARK RATE SHOULD NOT BE NULL")
        val selectedRate = ratesMap.firstOrNull { manufactureTurnOver > (it.min ?: BigDecimal.ZERO) && manufactureTurnOver <= (it.max ?: throw NullValueNotAllowedException("Max needs to be defined")) } ?: throw NullValueNotAllowedException("Rate not found")

        KotlinLogging.logger { }.info { "selected Rate fixed cost = ${selectedRate.id} and  ${selectedRate.firmType}" }

        var invoiceMaster = generateInvoiceMasterDetail(permit, map, user)

        when {
            applicationMapProperties.mapQASmarkLargeFirmsTurnOverId == selectedRate.id -> {
                calculatePaymentSMarkLargeFirm(permit, invoiceMaster, map, user, plantDetail, selectedRate)
            }
            applicationMapProperties.mapQASmarkMediumTurnOverId == selectedRate.id -> {
                calculatePaymentSMarkMediumOrSmallFirm(
                    permit,
                    invoiceMaster,
                    map,
                    user,
                    plantDetail,
                    selectedRate,
                    productNumber
                )
            }
            applicationMapProperties.mapQASmarkJuakaliTurnOverId == selectedRate.id -> {
                calculatePaymentSMarkMediumOrSmallFirm(
                    permit,
                    invoiceMaster,
                    map,
                    user,
                    plantDetail,
                    selectedRate,
                    productNumber
                )
            }
        }


        invoiceMaster = calculateTotalInvoiceAmountToPayAfterUpGarde(invoiceMaster, permitType, user)


        KotlinLogging.logger { }.info { "invoice Master total Amount = ${invoiceMaster.totalAmount}" }

        return invoiceMaster
    }


    fun calculateTotalInvoiceAmountToPay(
        invoiceMaster: QaInvoiceMasterDetailsEntity,
        permitType: PermitTypesEntity,
        user: UsersEntity
    ): QaInvoiceMasterDetailsEntity {
        var totalAmountPayable: BigDecimal = BigDecimal.ZERO
        qaInvoiceDetailsRepo.findByInvoiceMasterId(invoiceMaster.id)
            ?.let { invoiceDetailsList ->
                invoiceDetailsList.forEach { invoice ->
                    totalAmountPayable = totalAmountPayable.plus(
                        invoice.itemAmount ?: throw ExpectedDataNotFound("INVOICE AMOUNT IS NULL")
                    )
                }
            } ?: throw ExpectedDataNotFound("NO QA INVOICE DETAILS FOUND")

        val totalAmountTaxPayable = totalAmountPayable.multiply(permitType.taxRate)

        with(invoiceMaster) {
            paymentStatus = 0
            taxAmount = totalAmountTaxPayable
            subTotalBeforeTax = totalAmountPayable
            totalAmount = totalAmountPayable.plus(totalAmountTaxPayable)
            modifiedOn = Timestamp.from(Instant.now())
            modifiedBy = commonDaoServices.concatenateName(user)
        }

        return qaInvoiceMasterDetailsRepo.save(invoiceMaster)
    }

    fun calculateTotalInvoiceAmountToPayAfterUpGarde(
        invoiceMaster: QaInvoiceMasterDetailsEntity,
        permitType: PermitTypesEntity,
        user: UsersEntity
    ): QaInvoiceMasterDetailsEntity {
        var totalAmountPayable: BigDecimal = BigDecimal.ZERO
        qaInvoiceDetailsRepo.findByInvoiceMasterId(invoiceMaster.id)
            ?.let { invoiceDetailsList ->
                invoiceDetailsList.forEach { invoice ->
                    qaInvoiceMasterDetailsRepo.findByPermitIdAndVarField10IsNull(invoiceMaster.permitId ?: throw Exception("PERMIT ID MISSING"))
                        ?.let {masterInvoicePrevious->
                            totalAmountPayable = masterInvoicePrevious.subTotalBeforeTax?.minus(invoice.itemAmount ?: throw ExpectedDataNotFound("INVOICE AMOUNT IS NULL"))!!
                    }
                }
            } ?: throw ExpectedDataNotFound("NO QA INVOICE DETAILS FOUND")

        val totalAmountTaxPayable = totalAmountPayable.multiply(permitType.taxRate)

        with(invoiceMaster) {
            paymentStatus = 0
            taxAmount = totalAmountTaxPayable
            subTotalBeforeTax = totalAmountPayable
            totalAmount = totalAmountPayable.plus(totalAmountTaxPayable)
            modifiedOn = Timestamp.from(Instant.now())
            modifiedBy = commonDaoServices.concatenateName(user)
        }

        return qaInvoiceMasterDetailsRepo.save(invoiceMaster)
    }

    fun generateInvoiceMasterDetail(
        permit: PermitApplicationsEntity,
        map: ServiceMapsEntity,
        user: UsersEntity
    ): QaInvoiceMasterDetailsEntity {
        var invoiceMaster = QaInvoiceMasterDetailsEntity().apply {
            permitId = permit.id
            permitRefNumber = permit.permitRefNumber
            userId = permit.userId
            invoiceRef = "KIMSREF${generateRandomText(3, map.secureRandom, map.messageDigestAlgorithm, true).toUpperCase()}"
            generatedDate = Timestamp.from(Instant.now())
            itemCount = 1
            paymentStatus = 0
            status = 1
            createdOn = Timestamp.from(Instant.now())
            createdBy = commonDaoServices.concatenateName(user)
        }

        invoiceMaster = qaInvoiceMasterDetailsRepo.save(invoiceMaster)
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
            invoiceRef = "KIMSREF${generateRandomText(3, map.secureRandom, map.messageDigestAlgorithm, true).toUpperCase()}"
            generatedDate = Timestamp.from(Instant.now())
            itemCount = 1
            status = 1
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
            permitStatus = 1
            status = 1
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

    fun calculatePaymentInspectionFees(
        user: UsersEntity,
        permitType: PermitTypesEntity,
        manufactureTurnOver: BigDecimal,
        plantDetail: ManufacturePlantDetailsEntity,
        map: ServiceMapsEntity,
    ): QaInvoiceMasterDetailsEntity {
        val ratesMap = iPermitRatingRepo.findAllByStatus(map.activeStatus) ?: throw Exception("SMARK RATE SHOULD NOT BE NULL")
        val selectedRate = ratesMap.firstOrNull { manufactureTurnOver > (it.min ?: BigDecimal.ZERO) && manufactureTurnOver <= (it.max ?: throw NullValueNotAllowedException("Max needs to be defined")) } ?: throw NullValueNotAllowedException("Rate not found")

        KotlinLogging.logger { }.info { "selected Rate fixed cost = ${selectedRate.id} and  ${selectedRate.firmType}" }

        val tokenGenerated = "TOKEN${generateRandomText(3, map.secureRandom, map.messageDigestAlgorithm, true).toUpperCase()}"

        var invoiceMaster = QaInvoiceMasterDetailsEntity().apply {
            userId = user.id
            invoiceRef = "KIMSREF${generateRandomText(3, map.secureRandom, map.messageDigestAlgorithm, true).toUpperCase()}"
            generatedDate = Timestamp.from(Instant.now())
            itemCount = 1
            status = 1
            createdOn = Timestamp.from(Instant.now())
            createdBy = commonDaoServices.concatenateName(user)
        }
        invoiceMaster = qaInvoiceMasterDetailsRepo.save(invoiceMaster)

        var invoiceDetailsInspectionFee = QaInvoiceDetailsEntity().apply {
            invoiceMasterId = invoiceMaster.id
            umo = "PER"
            generatedDate = Timestamp.from(Instant.now())
            tokenValue = tokenGenerated
            itemDescName = "INSPECTION FEE"
            itemQuantity = BigDecimal.valueOf(1)
            itemAmount = selectedRate.firmFee?.multiply(selectedRate.validity?.toBigDecimal())
            inspectionStatus = 1
            status = 1
            createdOn = Timestamp.from(Instant.now())
            createdBy = commonDaoServices.concatenateName(user)
        }

        invoiceDetailsInspectionFee = qaInvoiceDetailsRepo.save(invoiceDetailsInspectionFee)

        with(invoiceMaster) {
            description = invoiceDetailsInspectionFee.itemDescName
            modifiedOn = Timestamp.from(Instant.now())
            modifiedBy = commonDaoServices.concatenateName(user)
        }

        invoiceMaster = qaInvoiceMasterDetailsRepo.save(invoiceMaster)

        invoiceMaster = calculateTotalInvoiceAmountToPay(invoiceMaster, permitType, user)

        with(plantDetail) {
            tokenGiven = "TOKEN${generateRandomText(3, map.secureRandom, map.messageDigestAlgorithm, true).toUpperCase()}"
            invoiceSharedId = invoiceDetailsInspectionFee.id
            inspectionFeeStatus = 0
//            paidDate = commonDaoServices.getCurrentDate()
//            endingDate = commonDaoServices.addYearsToCurrentDate(selectedRate.validity ?: throw Exception("INVALID NUMBER OF YEARS"))
        }

        qaDaoServices.updatePlantDetails(map, user, plantDetail)

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


        val taxAmountToPay = applicationCostValue?.let { permitType.taxRate?.times(it) }
        val amountToPay = taxAmountToPay?.let { applicationCostValue.plus(it) }

        var invoiceMaster = QaInvoiceMasterDetailsEntity().apply {
            permitId = permit.id
            permitRefNumber = permit.permitRefNumber
            userId = permit.userId
            invoiceRef =
                "KIMSREF${generateRandomText(3, map.secureRandom, map.messageDigestAlgorithm, true).toUpperCase()}"
            generatedDate = Timestamp.from(Instant.now())
            itemCount = 1
            status = 1
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
            itemAmount = applicationCostValue
            permitStatus = 0
            fmarkStatus = 1
            status = 1
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

    fun calculatePaymentFMarkOnSMark(
        permit: PermitApplicationsEntity,
        user: UsersEntity,
        permitType: PermitTypesEntity,
        invoiceMaster: QaInvoiceMasterDetailsEntity
    ): QaInvoiceDetailsEntity {
        val numberOfYears = permitType.numberOfYears?.toBigDecimal() ?: throw Exception("INVALID NUMBER OF YEARS")
        val applicationCostValue: BigDecimal? = permitType.fmarkAmount?.times(numberOfYears)

        var invoiceDetails = QaInvoiceDetailsEntity().apply {
            invoiceMasterId = invoiceMaster.id
            generatedDate = Timestamp.from(Instant.now())
            umo = "PER"
            itemDescName = permitType.itemInvoiceDesc
            itemQuantity = BigDecimal.ZERO
            itemAmount = applicationCostValue
            fmarkStatus = 1
            status = 1
            createdOn = Timestamp.from(Instant.now())
            createdBy = commonDaoServices.concatenateName(user)
        }

        invoiceDetails = qaInvoiceDetailsRepo.save(invoiceDetails)


        return invoiceDetails
    }

    fun calculatePaymentOtherDetails(
        permit: PermitApplicationsEntity,
        user: UsersEntity,
        invoiceDetailsOthers: QaInvoiceDetailsEntity
    ): Pair<QaInvoiceDetailsEntity, QaInvoiceMasterDetailsEntity> {

        qaInvoiceMasterDetailsRepo.findByPermitIdAndVarField10IsNull(permit.id ?: throw Exception("PERMIT ID MISSING"))
            ?.let { invoiceMaster ->
                var invoiceDetails = invoiceDetailsOthers.apply {
                    invoiceMasterId = invoiceMaster.id
                    generatedDate = Timestamp.from(Instant.now())
                    umo = "PER"
                    itemQuantity = BigDecimal.ZERO
                    status = 1
                    createdOn = Timestamp.from(Instant.now())
                    createdBy = commonDaoServices.concatenateName(user)
                }
                invoiceDetails = qaInvoiceDetailsRepo.save(invoiceDetails)

                return Pair(invoiceDetails, invoiceMaster)
            } ?: throw ExpectedDataNotFound("NO INVOICE MASTER FOUND WITH PERMIT REF NUMBER ${permit.permitRefNumber}")


    }


    fun calculatePaymentSMarkLargeFirm(
        permit: PermitApplicationsEntity,
        invoiceMaster: QaInvoiceMasterDetailsEntity,
        map: ServiceMapsEntity,
        user: UsersEntity,
        plantDetail: ManufacturePlantDetailsEntity,
        selectedRate: PermitRatingEntity
    ) {

        val tokenGenerated = "TOKEN${generateRandomText(3, map.secureRandom, map.messageDigestAlgorithm, true).toUpperCase()}"
        when {
            plantDetail.paidDate == null && plantDetail.endingDate == null && plantDetail.inspectionFeeStatus == null && plantDetail.tokenGiven == null && plantDetail.invoiceSharedId == null -> {
                 throw ExpectedDataNotFound("Kindly Pay the Inspection fees First before submitting current application")
            }
            commonDaoServices.getCurrentDate() > plantDetail.paidDate && commonDaoServices.getCurrentDate() < plantDetail.endingDate && plantDetail.inspectionFeeStatus == 1 && plantDetail.tokenGiven != null && plantDetail.invoiceSharedId != null -> {

                val invoiceDetailsPermitFee = QaInvoiceDetailsEntity().apply {
                    invoiceMasterId = invoiceMaster.id
                    tokenValue = plantDetail.tokenGiven
                    umo = "PER"
                    generatedDate = Timestamp.from(Instant.now())
                    itemDescName = selectedRate.invoiceDesc
                    itemQuantity = BigDecimal.valueOf(1)
                    itemAmount = selectedRate.productFee?.multiply(selectedRate.validity?.toBigDecimal())
                    permitStatus = 1
                    status = 1
                    createdOn = Timestamp.from(Instant.now())
                    createdBy = commonDaoServices.concatenateName(user)
                }

                qaInvoiceDetailsRepo.save(invoiceDetailsPermitFee)
            }
            commonDaoServices.getCurrentDate() > plantDetail.paidDate && commonDaoServices.getCurrentDate() > plantDetail.endingDate && plantDetail.inspectionFeeStatus == 1 && plantDetail.tokenGiven != null && plantDetail.invoiceSharedId != null -> {
                throw ExpectedDataNotFound("Kindly Pay the Inspection fees First before submitting current application")
            }
            else -> {
                throw ExpectedDataNotFound("INVALID INVOICE CALCULATION DETAILS FOR LARGE FIRM")
            }
        }
    }

    fun calculatePaymentSMarkLargeFirmUpgrade(
        permit: PermitApplicationsEntity,
        invoiceMaster: QaInvoiceMasterDetailsEntity,
        map: ServiceMapsEntity,
        user: UsersEntity,
        plantDetail: ManufacturePlantDetailsEntity,
        selectedRate: PermitRatingEntity
    ) {

        val tokenGenerated = "TOKEN${generateRandomText(3, map.secureRandom, map.messageDigestAlgorithm, true).toUpperCase()}"
        when {
            plantDetail.paidDate == null && plantDetail.endingDate == null && plantDetail.inspectionFeeStatus == null && plantDetail.tokenGiven == null && plantDetail.invoiceSharedId == null -> {
                throw ExpectedDataNotFound("Kindly Pay the Inspection fees First before submitting current application")
            }
            commonDaoServices.getCurrentDate() > plantDetail.paidDate && commonDaoServices.getCurrentDate() < plantDetail.endingDate && plantDetail.inspectionFeeStatus == 1 && plantDetail.tokenGiven != null && plantDetail.invoiceSharedId != null -> {

                val invoiceDetailsPermitFee = QaInvoiceDetailsEntity().apply {
                    invoiceMasterId = invoiceMaster.id
                    tokenValue = plantDetail.tokenGiven
                    umo = "PER"
                    generatedDate = Timestamp.from(Instant.now())
                    itemDescName = selectedRate.invoiceDesc
                    itemQuantity = BigDecimal.valueOf(1)
                    itemAmount = selectedRate.productFee?.multiply(selectedRate.validity?.toBigDecimal())
                    permitStatus = 1
                    status = 1
                    createdOn = Timestamp.from(Instant.now())
                    createdBy = commonDaoServices.concatenateName(user)
                }

                qaInvoiceDetailsRepo.save(invoiceDetailsPermitFee)
            }
            commonDaoServices.getCurrentDate() > plantDetail.paidDate && commonDaoServices.getCurrentDate() > plantDetail.endingDate && plantDetail.inspectionFeeStatus == 1 && plantDetail.tokenGiven != null && plantDetail.invoiceSharedId != null -> {
                throw ExpectedDataNotFound("Kindly Pay the Inspection fees First before submitting current application")
            }
            else -> {
                throw ExpectedDataNotFound("INVALID INVOICE CALCULATION DETAILS FOR LARGE FIRM")
            }
        }
    }

    private fun generateInvoiceForCurrentTime(
        invoiceMaster: QaInvoiceMasterDetailsEntity,
        selectedRate: PermitRatingEntity,
        user: UsersEntity,
        plantDetail: ManufacturePlantDetailsEntity,
        map: ServiceMapsEntity,
        permit: PermitApplicationsEntity,
        tokenGenerated: String
    ) {
        var invoiceDetailsInspectionFee = QaInvoiceDetailsEntity().apply {
            invoiceMasterId = invoiceMaster.id
            umo = "PER"
            generatedDate = Timestamp.from(Instant.now())
            tokenValue = tokenGenerated
            itemDescName = "INSPECTION FEE"
            itemQuantity = BigDecimal.valueOf(1)
            itemAmount = selectedRate.firmFee?.multiply(selectedRate.validity?.toBigDecimal())
            inspectionStatus = 1
            status = 1
            createdOn = Timestamp.from(Instant.now())
            createdBy = commonDaoServices.concatenateName(user)
        }

        invoiceDetailsInspectionFee = qaInvoiceDetailsRepo.save(invoiceDetailsInspectionFee)

        val invoiceDetailsPermitFee = QaInvoiceDetailsEntity().apply {
            invoiceMasterId = invoiceMaster.id
            umo = "PER"
            generatedDate = Timestamp.from(Instant.now())
            itemDescName = selectedRate.invoiceDesc
            tokenValue = tokenGenerated
            itemQuantity = BigDecimal.valueOf(1)
            itemAmount = selectedRate.productFee?.multiply(selectedRate.validity?.toBigDecimal())
            permitStatus = 1
            status = 1
            createdOn = Timestamp.from(Instant.now())
            createdBy = commonDaoServices.concatenateName(user)
        }

        qaInvoiceDetailsRepo.save(invoiceDetailsPermitFee)

        with(plantDetail) {
            tokenGiven = "TOKEN${generateRandomText(3, map.secureRandom, map.messageDigestAlgorithm, true).toUpperCase()}"
            invoiceSharedId = invoiceDetailsInspectionFee.id
            inspectionFeeStatus = 1
            paidDate = commonDaoServices.getCurrentDate()
            endingDate = commonDaoServices.addYearsToCurrentDate(selectedRate.validity ?: throw Exception("INVALID NUMBER OF YEARS"))
        }

        qaDaoServices.updatePlantDetails(map, user, plantDetail)

        permit.apply {
            permitFeeToken = tokenGenerated
        }
        qaDaoServices.permitUpdateDetails(permit, map, user)
    }

    fun calculatePaymentSMarkMediumOrSmallFirm(
        permit: PermitApplicationsEntity,
        invoiceMaster: QaInvoiceMasterDetailsEntity,
        map: ServiceMapsEntity,
        user: UsersEntity,
        plantDetail: ManufacturePlantDetailsEntity,
        selectedRate: PermitRatingEntity,
        productNumber: Long
    ) {

        val tokenGenerated = "TOKEN${generateRandomText(3, map.secureRandom, map.messageDigestAlgorithm, true).toUpperCase()}"
        val maxProductNumber = selectedRate.countBeforeFree ?: throw Exception("MISSING COUNT BEFORE FEE VALUE")

        if (productNumber <= maxProductNumber) {
            if (plantDetail.tokenGiven == null && plantDetail.invoiceSharedId == null) {
                generateInvoice4SmallAndMedium(
                    invoiceMaster,
                    tokenGenerated,
                    selectedRate,
                    user,
                    plantDetail,
                    map,
                    permit
                )
            } else if (plantDetail.tokenGiven != null && plantDetail.invoiceSharedId != null) {
                val invoiceDetailsPermitFee = QaInvoiceDetailsEntity().apply {
                    invoiceMasterId = invoiceMaster.id
                    umo = "PER"
                    generatedDate = Timestamp.from(Instant.now())
                    tokenValue = plantDetail.tokenGiven
                    itemDescName = selectedRate.invoiceDesc
                    itemQuantity = BigDecimal.valueOf(1)
                    itemAmount = selectedRate.productFee?.multiply(selectedRate.validity?.toBigDecimal())
                    permitStatus = 1
                    status = 1
                    createdOn = Timestamp.from(Instant.now())
                    createdBy = commonDaoServices.concatenateName(user)
                }

                qaInvoiceDetailsRepo.save(invoiceDetailsPermitFee)

                permit.apply {
                    permitFeeToken = plantDetail.tokenGiven
                }
                qaDaoServices.permitUpdateDetails(permit, map, user)

            } else {
                throw ExpectedDataNotFound("INVALID INVOICE CALCULATION DETAILS FOR MEDIUM/SMALL FIRM")
            }
        } else if (productNumber > maxProductNumber) {
            val invoiceDetailsPermitFee = QaInvoiceDetailsEntity().apply {
                invoiceMasterId = invoiceMaster.id
                umo = "PER"
                generatedDate = Timestamp.from(Instant.now())
                tokenValue = tokenGenerated
                itemDescName = "EXTRA PRODUCT"
                itemQuantity = BigDecimal.valueOf(1)
                itemAmount = selectedRate.extraProductFee?.multiply(selectedRate.validity?.toBigDecimal())
                permitStatus = 1
                status = 1
                createdOn = Timestamp.from(Instant.now())
                createdBy = commonDaoServices.concatenateName(user)
            }

            qaInvoiceDetailsRepo.save(invoiceDetailsPermitFee)

            permit.apply {
                permitFeeToken = tokenGenerated
            }
            qaDaoServices.permitUpdateDetails(permit, map, user)
        }


    }

    private fun generateInvoice4SmallAndMedium(
        invoiceMaster: QaInvoiceMasterDetailsEntity,
        tokenGenerated: String,
        selectedRate: PermitRatingEntity,
        user: UsersEntity,
        plantDetail: ManufacturePlantDetailsEntity,
        map: ServiceMapsEntity,
        permit: PermitApplicationsEntity
    ) {
        var invoiceDetailsPermitFee = QaInvoiceDetailsEntity().apply {
            invoiceMasterId = invoiceMaster.id
            umo = "PER"
            generatedDate = Timestamp.from(Instant.now())
            tokenValue = tokenGenerated
            itemDescName = selectedRate.invoiceDesc
            itemQuantity = BigDecimal.valueOf(1)
            permitStatus = 1
            status = 1
            itemAmount = selectedRate.countBeforeFee?.multiply(selectedRate.validity?.toBigDecimal())
            createdOn = Timestamp.from(Instant.now())
            createdBy = commonDaoServices.concatenateName(user)
        }

        invoiceDetailsPermitFee = qaInvoiceDetailsRepo.save(invoiceDetailsPermitFee)

        with(plantDetail) {
            tokenGiven = tokenGenerated
            invoiceSharedId = invoiceDetailsPermitFee.id
            //                inspectionFeeStatus = 1
            paidDate = commonDaoServices.getCurrentDate()
            endingDate = commonDaoServices.addYearsToCurrentDate(
                selectedRate.validity ?: throw Exception("INVALID NUMBER OF YEARS")
            )
        }
        qaDaoServices.updatePlantDetails(map, user, plantDetail)

        permit.apply {
            permitFeeToken = tokenGenerated
        }
        qaDaoServices.permitUpdateDetails(permit, map, user)
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun updatePermitWithZeroInvoicesAmount() {

        /**
         *  Find all company with firm category of small and medium and add them together to filter for those with
         *  same token that acted as promotion or discount so that they can be able to be moved to next process
         *
         * */
        val companyListMediumType =
            qaDaoServices.findAllCompanyWithTurnOverID(applicationMapProperties.mapQASmarkMediumTurnOverId, 1)
        val companyListSmallType =
            qaDaoServices.findAllCompanyWithTurnOverID(applicationMapProperties.mapQASmarkJuakaliTurnOverId, 1)

        val allCompanyTogether = mutableListOf<CompanyProfileEntity>()
        allCompanyTogether.addAll(companyListSmallType)
        allCompanyTogether.addAll(companyListMediumType)

        for (cp in allCompanyTogether) {
            try {
                //Find all branches per Company Found
                val allBranches = qaDaoServices.findAllPlantDetailsWithCompanyDetailsAndStatus(
                    cp.id ?: throw Exception("Missing company ID"), 1
                )
                try {

                    for (br in allBranches) {
                        //find invoice Details associated with that company
                        val invoiceDetail = qaInvoiceDetailsRepo.findByIdOrNull(br.invoiceSharedId)
                        val masterDetails = qaInvoiceMasterDetailsRepo.findByIdOrNull(invoiceDetail?.invoiceMasterId)
                        if (masterDetails?.paymentStatus == 10) {
                            val permitsWithToken = qaDaoServices.findAllPermitListWithToken(
                                br.tokenGiven ?: throw Exception("Missing TOKEN")
                            )
                            try {
                                //update all permit details found with that company details
                                for (pd in permitsWithToken) {
                                    if (pd.paidStatus != 10) {
                                        //find masterDetails invoice with id of permit
                                        val masterInvoiceDetails = qaDaoServices.findPermitInvoiceByPermitID(
                                            pd.id ?: throw Exception("Missing Permit ID")
                                        )
                                        if (masterInvoiceDetails.totalAmount == BigDecimal.ZERO) {
                                            //Update permit paid status to 1
                                            with(pd) {
                                                paidStatus = 1
                                                permitStatus = applicationMapProperties.mapQaStatusPaymentDone
                                            }
                                            permitRepo.save(pd)

                                            with(masterInvoiceDetails) {
                                                receiptNo = masterDetails.receiptNo
                                                batchInvoiceNo = masterDetails.batchInvoiceNo
                                                paymentStatus = masterDetails.paymentStatus
                                            }
                                            qaInvoiceMasterDetailsRepo.save(masterInvoiceDetails)
                                        }

                                    }
                                }
                            } catch (e: Exception) {
                                KotlinLogging.logger { }.trace(e.message)
                                KotlinLogging.logger { }.debug(e.message, e)

                                continue
                            }
                        }
                    }
                } catch (e: Exception) {
                    KotlinLogging.logger { }.trace(e.message)
                    KotlinLogging.logger { }.debug(e.message, e)

                    continue
                }


            } catch (e: Exception) {
                KotlinLogging.logger { }.error(e.message)
                KotlinLogging.logger { }.debug(e.message, e)

                continue
            }

        }

    }
}
