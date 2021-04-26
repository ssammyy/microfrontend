package org.kebs.app.kotlin.apollo.api.ports.provided.dao

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.ports.provided.mpesa.MPesaService
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.common.utils.generateRandomText
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.model.qa.*
import org.kebs.app.kotlin.apollo.store.model.registration.CompanyProfileEntity
import org.kebs.app.kotlin.apollo.store.repo.*
import org.kebs.app.kotlin.apollo.store.repo.qa.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.sql.Timestamp
import java.time.Instant
import java.time.temporal.ChronoUnit

@Service
class QADaoServices(
    private val applicationMapProperties: ApplicationMapProperties,
    private val commonDaoServices: CommonDaoServices,
    private val productsRepo: IProductsRepository,
    private val iTurnOverRatesRepository: ITurnOverRatesRepository,
    private val iManufacturePaymentDetailsRepository: IManufacturerPaymentDetailsRepository,
    private val sampleStandardsRepository: ISampleStandardsRepository,
    private val invoiceDaoService: InvoiceDaoService,
    private val paymentUnitsRepository: ICfgKebsPermitPaymentUnitsRepository,
    private val serviceRequestsRepository: IServiceRequestsRepository,
    private val permitRepo: IPermitApplicationsRepository,
    private val sta3Repo: IQaSta3EntityRepository,
    private val invoiceRepository: IInvoiceRepository,
    private val sta10Repo: IQaSta10EntityRepository,
    private val productsManufactureSTA10Repo: IQaProductBrandEntityRepository,
    private val rawMaterialsSTA10Repo: IQaRawMaterialRepository,
    private val machinePlantsSTA10Repo: IQaMachineryRepository,
    private val manufacturingProcessSTA10Repo: IQaManufactureProcessRepository,
    private val permitTypesRepo: IPermitTypesEntityRepository,
    private val mpesaServices: MPesaService
) {

    final var appId = applicationMapProperties.mapQualityAssurance

    val permitList = "redirect:/api/qa/permits-list?permitTypeID"
    val permitDetails = "redirect:/api/qa/permit-details?permitID"
    val sta10Details = "redirect:/api/qa/view-sta10?permitID"

    fun findPermitTypesList(status: Int): List<PermitTypesEntity> {
        permitTypesRepo.findByStatus(status)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No Permit Type List found")
    }

    fun findPermitType(id: Long): PermitTypesEntity {
        permitTypesRepo.findByIdOrNull(id)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No Permit Type found with the following [ID=$id]")
    }

    fun findAllUserPermitWithPermitType(user: UsersEntity, permitType: Long): List<PermitApplicationsEntity> {
        val userId = user.id ?: throw ExpectedDataNotFound("No ID Found")
        permitRepo.findByUserIdAndPermitType(userId, permitType)
            ?.let { permitList ->
                return permitList
            }

            ?: throw ExpectedDataNotFound("No Permit Found for the following user with USERNAME = ${user.userName}")
    }

    fun findPermitBYID(id: Long): PermitApplicationsEntity {
        permitRepo.findByIdOrNull(id)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No Permit Type found with the following [ID=$id]")
    }


    fun findSta3BYID(id: Long): QaSta3Entity {
        sta3Repo.findByIdOrNull(id)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No Sta3 found with the following [ID=$id]")
    }

    fun findSta10BYID(id: Long): QaSta10Entity {
        sta10Repo.findByIdOrNull(id)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No STA10 found with the following [ID=$id]")
    }

    fun findPermitBYUserIDAndId(id: Long, userId: Long): PermitApplicationsEntity {
        permitRepo.findByIdAndUserId(id, userId)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No Permit Type found with the following [ID=$id]")
    }

    fun findPermitInvoiceByPermitID(id: Long, userId: Long): InvoiceEntity {
        invoiceRepository.findByPermitIdAndUserId(id, userId)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No Invoice found with the following [PERMIT ID=$id  and LoggedIn User]")
    }

    fun findSTA3WithPermitIDBY(permitId: Long): QaSta3Entity {
        sta3Repo.findByPermitId(permitId)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No STA3 found with the following [permit id=$permitId]")
    }

    fun findSTA10WithPermitIDBY(permitId: Long): QaSta10Entity {
        sta10Repo.findByPermitId(permitId)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No STA10 found with the following [permit id=$permitId]")
    }

    fun findProductsManufactureWithSTA10ID(sta10Id: Long): List<QaProductManufacturedEntity>? {
        return productsManufactureSTA10Repo.findBySta10Id(sta10Id)
    }


    fun findRawMaterialsWithSTA10ID(sta10Id: Long): List<QaRawMaterialEntity>? {
        return rawMaterialsSTA10Repo.findBySta10Id(sta10Id)
    }

    fun findMachinePlantsWithSTA10ID(sta10Id: Long): List<QaMachineryEntity>? {
        return machinePlantsSTA10Repo.findBySta10Id(sta10Id)
    }

    fun findManufacturingProcessesWithSTA10ID(sta10Id: Long): List<QaManufacturingProcessEntity>? {
        return manufacturingProcessSTA10Repo.findBySta10Id(sta10Id)
    }

    fun permitSave(
        permits: PermitApplicationsEntity,
        permitTypeDetails: PermitTypesEntity,
        user: UsersEntity,
        map: ServiceMapsEntity
    ): PermitApplicationsEntity {

        with(permits) {
            userId = user.id
            productName = product?.let { commonDaoServices.findProductByID(it).name }
            permitType = permitTypeDetails.id
            permitNumber = "${permitTypeDetails.markNumber}${
                generateRandomText(
                    5,
                    map.secureRandom,
                    map.messageDigestAlgorithm,
                    false
                )
            }".toUpperCase()
            enabled = map.initStatus
            status = map.activeStatus
            createdBy = commonDaoServices.concatenateName(user)
            createdOn = commonDaoServices.getTimestamp()
        }
        return permitRepo.save(permits)
    }

    fun sta3NewSave(
        permitNewID: Long,
        qaSta3Details: QaSta3Entity,
        user: UsersEntity,
        map: ServiceMapsEntity
    ): QaSta3Entity {

        with(qaSta3Details) {
            permitId = permitNewID
            status = map.activeStatus
            createdBy = commonDaoServices.concatenateName(user)
            createdOn = commonDaoServices.getTimestamp()
        }
        return sta3Repo.save(qaSta3Details)
    }

    fun sta10NewSave(
        permitNewID: Long,
        qaSta10Details: QaSta10Entity,
        user: UsersEntity,
        map: ServiceMapsEntity
    ): QaSta10Entity {

        with(qaSta10Details) {
            totalNumberPersonnel = totalNumberMale?.let { totalNumberFemale?.plus(it) }
            town = town?.let { commonDaoServices.findTownEntityByTownId(it, map.activeStatus).id }
            county = county?.let { commonDaoServices.findCountiesEntityByCountyId(it, map.activeStatus).id }
            region = county?.let { commonDaoServices.findCountiesEntityByCountyId(it, map.activeStatus).regionId }
            permitId = permitNewID
            applicationDate = commonDaoServices.getCurrentDate()
            status = map.activeStatus
            createdBy = commonDaoServices.concatenateName(user)
            createdOn = commonDaoServices.getTimestamp()
        }
        return sta10Repo.save(qaSta10Details)
    }

    fun sta10ManufactureProductNewSave(
        qaSta10ID: Long,
        productManufacturedDetails: QaProductManufacturedEntity,
        user: UsersEntity,
        map: ServiceMapsEntity
    ): QaProductManufacturedEntity {

        with(productManufacturedDetails) {
            sta10Id = qaSta10ID
            status = map.activeStatus
            createdBy = commonDaoServices.concatenateName(user)
            createdOn = commonDaoServices.getTimestamp()
        }
        return productsManufactureSTA10Repo.save(productManufacturedDetails)
    }

    fun sta10RawMaterialsNewSave(
        qaSta10ID: Long,
        rawMaterialsDetails: QaRawMaterialEntity,
        user: UsersEntity,
        map: ServiceMapsEntity
    ): QaRawMaterialEntity {

        with(rawMaterialsDetails) {
            sta10Id = qaSta10ID
            status = map.activeStatus
            createdBy = commonDaoServices.concatenateName(user)
            createdOn = commonDaoServices.getTimestamp()
        }
        return rawMaterialsSTA10Repo.save(rawMaterialsDetails)
    }

    fun sta10MachinePlantNewSave(
        qaSta10ID: Long,
        machinePlantsDetails: QaMachineryEntity,
        user: UsersEntity,
        map: ServiceMapsEntity
    ): QaMachineryEntity {

        with(machinePlantsDetails) {
            sta10Id = qaSta10ID
            status = map.activeStatus
            createdBy = commonDaoServices.concatenateName(user)
            createdOn = commonDaoServices.getTimestamp()
        }
        return machinePlantsSTA10Repo.save(machinePlantsDetails)
    }

    fun sta10ManufacturingProcessNewSave(
        qaSta10ID: Long,
        manufacturingProcessDetails: QaManufacturingProcessEntity,
        user: UsersEntity,
        map: ServiceMapsEntity
    ): QaManufacturingProcessEntity {

        with(manufacturingProcessDetails) {
            sta10Id = qaSta10ID
            status = map.activeStatus
            createdBy = commonDaoServices.concatenateName(user)
            createdOn = commonDaoServices.getTimestamp()
        }
        return manufacturingProcessSTA10Repo.save(manufacturingProcessDetails)
    }


    fun permitUpdateDetails(permits: PermitApplicationsEntity, user: UsersEntity): PermitApplicationsEntity {

        with(permits) {
            modifiedBy = commonDaoServices.concatenateName(user)
            modifiedOn = commonDaoServices.getTimestamp()
        }
        return permitRepo.save(permits)
    }

    fun invoiceUpdateDetails(invoice: InvoiceEntity, user: UsersEntity): InvoiceEntity {

        with(invoice) {
            modifiedBy = commonDaoServices.concatenateName(user)
            modifiedOn = commonDaoServices.getTimestamp()
        }
        return invoiceRepository.save(invoice)
    }

    fun permitInvoiceCalculation(
        s: ServiceMapsEntity,
        user: UsersEntity,
        permit: PermitApplicationsEntity,
        permitType: PermitTypesEntity
    ): ServiceRequestsEntity {

        var sr = commonDaoServices.createServiceRequest(s)
        try {

            val userCompany = user.id?.let { commonDaoServices.findCompanyProfile(it) } ?: throw NullValueNotAllowedException("Company Details For User with [ID = ${user.id}] , does Not exist")

            val invoiceGenerated = invoiceGen(permit,userCompany, user, permitType)

            val invoiceNumber = invoiceGenerated.invoiceNumber ?: throw NullValueNotAllowedException("Invoice Number Is Missing For Invoice with [ID = ${invoiceGenerated.id}]")
            val batchInvoiceDetail = invoiceDaoService.createBatchInvoiceDetails(user, invoiceNumber)
            val updateBatchInvoiceDetail = invoiceDaoService.addInvoiceDetailsToBatchInvoice(invoiceGenerated, applicationMapProperties.mapInvoiceTransactionsForPermit, user, batchInvoiceDetail)

            //Todo: Payment selection
            val manufactureDetails = commonDaoServices.findCompanyProfile(user.id!!)
            val myAccountDetails = InvoiceDaoService.InvoiceAccountDetails()
            with(myAccountDetails) {
                accountName = manufactureDetails.name
                accountNumber = manufactureDetails.kraPin
                currency = applicationMapProperties.mapInvoiceTransactionsLocalCurrencyPrefix
            }

            invoiceDaoService.createPaymentDetailsOnStgReconciliationTable(user, updateBatchInvoiceDetail, myAccountDetails)

            sr.payload = "User[id= ${userCompany.userId}]"
            sr.names = "${userCompany.name} ${userCompany.kraPin}"

            sr.responseStatus = sr.serviceMapsId?.successStatusCode
            sr.responseMessage = "Success ${sr.payload}"
            sr.status = s.successStatus
            sr = serviceRequestsRepository.save(sr)
            sr.processingEndDate = Timestamp.from(Instant.now())

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
//            KotlinLogging.logger { }.trace(e.message, e)
            sr.status = sr.serviceMapsId?.exceptionStatus
            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            sr.responseMessage = e.message
            sr = serviceRequestsRepository.save(sr)

        }

        KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
        return sr
    }

    fun permitInvoiceSTKPush(
        s: ServiceMapsEntity,
        user: UsersEntity,
        phoneNumber: String,
        invoice: InvoiceEntity
    ): ServiceRequestsEntity {

        var sr = commonDaoServices.createServiceRequest(s)
        try {

            //TODO: PAYMENT METHOD UPDATE THE AMOUNT BY REMOVING THE STATIC VALUE
            user.userName?.let { invoice.invoiceNumber?.let { it1 -> mpesaServices.sanitizePhoneNumber(phoneNumber)?.let { it2 ->
                mpesaServices.mainMpesaTransaction("10",
                    it2, it1, it, applicationMapProperties.mapInvoiceTransactionsForPermit)
            } } }

            sr.payload = "User[id= ${user.id}]"
            sr.names = "$phoneNumber} ${invoice.invoiceNumber}"

            sr.responseStatus = sr.serviceMapsId?.successStatusCode
            sr.responseMessage = "Success ${sr.payload}"
            sr.status = s.successStatus
            sr = serviceRequestsRepository.save(sr)
            sr.processingEndDate = Timestamp.from(Instant.now())

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
//            KotlinLogging.logger { }.trace(e.message, e)
            sr.status = sr.serviceMapsId?.exceptionStatus
            sr.responseStatus = sr.serviceMapsId?.exceptionStatusCode
            sr.responseMessage = e.message
            sr = serviceRequestsRepository.save(sr)

        }

        KotlinLogging.logger { }.trace("${sr.id} ${sr.responseStatus}")
        return sr
    }


    //Todo: CHECK THE METHODE AGAIN AFTER DEMO
    fun invoiceGen(permits: PermitApplicationsEntity, entity: CompanyProfileEntity, user: UsersEntity, permitType: PermitTypesEntity): InvoiceEntity {
        val map = commonDaoServices.serviceMapDetails(appId)
        val invoice = InvoiceEntity()
        with(invoice) {

            /**
             * Get rid of hard coded data
             */
            conditions = "Must be paid in 30 days"
            createdOn = Timestamp.from(Instant.now())
            status = 0
            map.tokenExpiryHours?.let { expiryDate = Timestamp.from(Instant.now().plus(it, ChronoUnit.HOURS)) }

            signature = commonDaoServices.concatenateName(user)
            createdBy = commonDaoServices.concatenateName(user)
            val generatedPayments = permits.let { calculatePayment(it) }
            amount = generatedPayments[3]
            applicationCost = generatedPayments[2]
            val cost: BigDecimal? = generatedPayments[0]
            standardCost = cost
            inspectionCost = generatedPayments[1]
            fmarkStatus = generatedPayments[4]
            fmarkCost = generatedPayments[5]
            tax = generatedPayments[6]
            businessName = entity.name
            permitId = permits.id
            userId = user.id
            invoiceNumber ="${permitType.markNumber}${generateRandomText(3, map.secureRandom, map.messageDigestAlgorithm, true).toUpperCase()}"
        }

        return invoiceRepository.save(invoice)


    }


    //Todo: CHECK THE METHODE AGAIN AFTER DEMO
    fun calculatePayment(permit: PermitApplicationsEntity): MutableList<BigDecimal?> {
//        val manufactureId = 100L
        KotlinLogging.logger { }.info { "ManufacturerId, ${permit.userId}" }
        val manufactureId = permit.userId
//        val manufactureTurnOver: BigDecimal? = manufactureId.let { it?.let { it1 -> iManufacturePaymentDetailsRepository.findByManufacturerIdAndStatus(it1, 1)?.turnOverAmount } }
        val manufactureTurnOver = permit.userId?.let { commonDaoServices.findCompanyProfile(it).yearlyTurnover }
        KotlinLogging.logger { }.info { manufactureTurnOver }
        var amountToPay: BigDecimal? = null
        var taxAmount: BigDecimal? = null

        val m = mutableListOf<BigDecimal?>()
        var fmarkCost: BigDecimal? = null
        val paymentUnits = paymentUnitsRepository.findByIdOrNull(2)
        KotlinLogging.logger { }.info { paymentUnits?.standardStandardCost }
        var fmark: BigDecimal? = null


        if (manufactureTurnOver != null) {

            if (manufactureTurnOver > iTurnOverRatesRepository.findByIdAndFirmType(1, "Large firms")?.lowerLimit) {
                val applicationCost: BigDecimal? = paymentUnits?.standardApplicationCost?.toBigDecimal()
                val noOf =
                    permit.productSubCategory?.let { sampleStandardsRepository.findBySubCategoryId(it)?.noOfPages }
//                val noOf = sampleStandardsRepository.findBySubCategoryId(permit.productSubCategory?.id)?.noOfPages))
                val standardCost: BigDecimal? = (paymentUnits?.standardStandardCost?.times(noOf!!))?.toBigDecimal()
//                val inspectionCost: BigDecimal? = permit.noOfSitesProducingTheBrand?.let { paymentUnits?.standardInspectionCost?.times(it)?.toBigDecimal() }
                val inspectionCost: BigDecimal? = paymentUnits?.standardInspectionCost?.toBigDecimal()
                var stgAmt: BigDecimal? = null
                if (permit.product == 61L) {
                    stgAmt = standardCost?.plus(inspectionCost!!)?.let { applicationCost?.plus(it) }
                        ?.let { 2.toBigDecimal().times(it) }
                    fmark = 1.toBigDecimal()
                    fmarkCost = standardCost?.plus(inspectionCost!!)?.let { applicationCost?.plus(it) }
                    taxAmount = stgAmt?.let { 0.16.toBigDecimal().times(it) }
                    amountToPay = taxAmount?.let { stgAmt?.plus(it) }
                } else {
                    KotlinLogging.logger { }.info { "second loop, ${permit.product}" }
                    stgAmt = standardCost?.plus(inspectionCost!!)?.let { applicationCost?.plus(it) }
                    fmark = 0.toBigDecimal()
                    taxAmount = stgAmt?.let { 0.16.toBigDecimal().times(it) }
                    amountToPay = taxAmount?.let { stgAmt?.plus(it) }
                }
//                amountToPay = standardCost?.plus(inspectionCost!!)?.let { applicationCost?.plus(it) }
                KotlinLogging.logger { }.info { "Manufacturer turnover, $manufactureTurnOver" }
                KotlinLogging.logger { }.info { "Total Amount To Pay   = " + amountToPay?.toDouble() }

                m.add(standardCost)
                m.add(inspectionCost)
                m.add(applicationCost)
                m.add(amountToPay)
                m.add(fmark)
                m.add(fmarkCost)
                m.add(taxAmount)

            } else {
                KotlinLogging.logger { }.info { "Turnover is less than 500000" }
            }
//            else if (manufactureTurnOver < iTurnOverRatesRepository.findByIdAndFirmType(2, "Medium Enterprises")?.upperLimit && manufactureTurnOver > iTurnOverRatesRepository.findByIdAndFirmType(2, "Medium Enterprises")?.lowerLimit) {
//                fixAmountToPay = iTurnOverRatesRepository.findByIdAndFirmType(2, "Medium Enterprises")?.fixedAmountToPay
//                variableAmountToPay = iTurnOverRatesRepository.findByIdAndFirmType(2, "Medium Enterprises")?.variableAmountToPay
//                if (totalProducts != null) {
//                    if (totalProducts > 3) {
//                        val remProduct: Int? = totalProducts.minus(3)
//                        amountToPay = (remProduct?.toBigDecimal()?.multiply(variableAmountToPay))?.add(fixAmountToPay)
//                        KotlinLogging.logger { }.info { "Total Amount To Pay for Medium Enterprises = " + remProduct + " =" + amountToPay?.toDouble() }
//                    } else {
//                        amountToPay = fixAmountToPay
//                        KotlinLogging.logger { }.info { "Total Amount To Pay for Medium Enterprises = " + amountToPay?.toDouble() }
//                    }
//                }


        } else {
            KotlinLogging.logger { }.info { "Turnover is less than 500000" }
        }
//        else if (manufactureTurnOver < iTurnOverRatesRepository.findByIdAndFirmType(3, "Jua kali and small Enterprises")?.upperLimit) {
//                fixAmountToPay = iTurnOverRatesRepository.findByIdAndFirmType(3, "Jua kali and small Enterprises")?.fixedAmountToPay
//                variableAmountToPay = iTurnOverRatesRepository.findByIdAndFirmType(3, "Jua kali and small Enterprises")?.variableAmountToPay
//                if (totalProducts != null) {
//                    if (totalProducts > 3) {
//                        val remProduct: Int? = totalProducts.minus(3)
//                        amountToPay = (remProduct?.toBigDecimal()?.multiply(variableAmountToPay))?.add(fixAmountToPay)
//                        KotlinLogging.logger { }.info { "Total Amount To Pay For Jua kali and small Enterprises  = " + remProduct + " =" + amountToPay?.toDouble() }
//                    } else {
//                        amountToPay = fixAmountToPay
//                        KotlinLogging.logger { }.info { "Total Amount To Pay For Jua kali and small Enterprises  = " + amountToPay?.toDouble() }
//                    }
//                }
//            }
//
//        }
        /**
        //         * Save the payment Details
        //         */
//        val myPermit = permit
//        with(myPermit) {
//            totalAmountToPay = amountToPay.toString()
//            KotlinLogging.logger { }.info { "Total Amount To Pay and save = " + amountToPay?.toDouble() }
//        }
//        iPermitRepository.save(permit)
        return m
    }

}