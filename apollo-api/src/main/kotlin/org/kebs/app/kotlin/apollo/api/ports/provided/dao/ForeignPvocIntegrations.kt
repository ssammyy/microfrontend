package org.kebs.app.kotlin.apollo.api.ports.provided.dao

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.payload.request.*
import org.kebs.app.kotlin.apollo.api.service.BillingService
import org.kebs.app.kotlin.apollo.api.service.PvocMonitoringService
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.model.invoice.BillTransactionsEntity
import org.kebs.app.kotlin.apollo.store.model.pvc.*
import org.kebs.app.kotlin.apollo.store.repo.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import java.math.BigDecimal
import java.sql.Timestamp
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.transaction.Transactional

@Service
class ForeignPvocIntegrations(
        private val cocRepo: ICocsRepository,
        private val iCocItemRepository: ICocItemRepository,
        private val corsBakRepository: ICorsBakRepository,
        private val rfcRepository: IRfcEntityRepo,
        private val rfcItemRepository: IRfcItemsRepository,
        private val rfcCorRepository: IRfcCorRepository,
        private val riskProfileRepository: IRiskProfileRepository,
        private val timelinesConfigurationRepository: IPvocTimelinesConfigurationRepository,
        private val timelinesRepository: IPvocTimelinesDataEntityRepository,
        private val sealIssuesRepository: IPvocSealIssuesEntityRepository,
        private val timelinePenaltiesRepository: IPvocTimelinePenaltiesRepository,
        private val idfsRepository: IdfsEntityRepository,
        private val idfsItemRepository: IdfItemsEntityRepository,
        private val commonDaoServices: CommonDaoServices,
        private val billingService: BillingService,
        private val properties: ApplicationMapProperties,
        private val monitoringService: PvocMonitoringService
) {
    private final val YEAR_MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyyMM")
    private final val REPORT_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd")

    @Transactional
    fun foreignCoc(
            user: PvocPartnersEntity,
            coc: CocEntityForm,
            map: ServiceMapsEntity,
    ): CocsEntity? {
        coc.ucrNumber?.let { ucrNumber ->
            cocRepo.findByUcrNumberAndCocTypeAndVersion(ucrNumber, "COC", coc.version)
                    ?.let { coc ->
                        return null
                    }
        }
                ?: kotlin.run {
                    var localCoc = CocsEntity()
                    KotlinLogging.logger { }.debug("Starting background task")
                    with(localCoc) {
                        coiNumber = coc.cocNumber ?: "UNKNOWN"
                        cocNumber = coc.cocNumber?.toUpperCase()
                        idfNumber = coc.idfNumber ?: "UNKNOWN"
                        rfiNumber = coc.rfiNumber ?: "UNKNOWN"
                        ucrNumber = coc.ucrNumber
                        acceptableDocDate = coc.acceptableDocDate
                        finalDocDate = coc.finalDocDate
                        rfcDate = coc.rfcDate
                        shipmentQuantityDelivered = "UNKNOWN"
                        cocIssueDate = coc.cocIssueDate
                        clean = "Y"
                        cocIssueDate = coc.cocIssueDate
                        cocRemarks = coc.cocRemarks ?: "NA"
                        coiRemarks = "UNKNOWN"
                        issuingOffice = coc.issuingOffice ?: "UNKNOWN"
                        importerName = coc.importerName
                        importerPin = coc.importerPin
                        importerAddress1 = coc.importerAddress1
                        importerAddress2 = coc.importerAddress2 ?: "UNKNOWN"
                        importerCity = coc.importerCity ?: "UNKNOWN"
                        importerCountry = coc.importerCountry ?: "UNKNOWN"
                        importerZipCode = "UNKNOWN"
                        importerTelephoneNumber = coc.importerTelephoneNumber ?: "UNKNOWN"
                        importerFaxNumber = coc.importerFaxNumber ?: "UNKNOWN"
                        importerEmail = coc.importerEmail ?: "UNKNOWN"
                        exporterName = coc.exporterName ?: "UNKNOWN"
                        exporterPin = coc.importerPin ?: "UNKNOWN"
                        exporterAddress1 = coc.exporterAddress1 ?: "UNKOWN"
                        exporterAddress2 = coc.exporterAddress2 ?: "UNKNOWN"
                        exporterCity = coc.exporterCity ?: "UNKNOWN"
                        exporterCountry = coc.exporterCountry ?: "UNKNOWN"
                        exporterZipCode = coc.exporterZipCode ?: "UNKNOWN"
                        exporterTelephoneNumber = coc.exporterTelephoneNumber ?: "UNKOWN"
                        exporterFaxNumber = coc.exporterFaxNumber ?: "UNKOWN"
                        exporterEmail = coc.exporterEmail ?: "UNKNOWN"
                        placeOfInspection = coc.placeOfInspection ?: "UNKNOWN"
                        dateOfInspection = coc.dateOfInspection
                        portOfDestination = coc.portOfDestination ?: "UNKOWN"
                        shipmentMode = coc.shipmentMode ?: "UNKNOWN"
                        countryOfSupply = coc.countryOfSupply ?: "UNKNOWN"
                        finalInvoiceFobValue = coc.finalInvoiceFobValue ?: 0.0
                        finalInvoiceExchangeRate = coc.finalInvoiceExchangeRate ?: 0.0
                        finalInvoiceCurrency = coc.finalInvoiceCurrency ?: properties.applicationCurrencyCode
                        finalInvoiceNumber = coc.finalInvoiceNumber ?: "NA"
                        finalInvoiceDate = coc.finalInvoiceDate ?: commonDaoServices.getTimestamp()
                        shipmentSealNumbers = coc.shipmentSealNumbers ?: "UNKNOWN"
                        shipmentContainerNumber = coc.shipmentContainerNumber ?: "UNKNOWN"
                        shipmentGrossWeight = coc.shipmentGrossWeight?.toString() ?: "UNKNOWN"
                        cocRemarks = coc.cocRemarks ?: "UNKNOWN"
                        route = coc.route ?: "Z"
                        partner = user.id
                        reviewStatus = 0
                        version = coc.version ?: 1
                        cocType = "COC"
                        documentsType = "F"
                        productCategory = "UNKNOWN"
                        partner = null
                        createdBy = commonDaoServices.loggedInUserAuthentication().name
                        createdOn = commonDaoServices.getTimestamp()
                    }
                    // Add invoice details
                    localCoc = cocRepo.save(localCoc)
                    KotlinLogging.logger { }.info { "FOREIGN = ${localCoc.id}, items=${coc.cocItems?.size}" }
                    this.checkCocCoiDocumentCompliance(localCoc, user, "COC")
                    foreignCocItems(coc.cocItems, localCoc, map)
                    return localCoc
                }

    }

    @Transactional
    fun foreignNcr(
            user: PvocPartnersEntity,
            ncr: NcrEntityForm,
            map: ServiceMapsEntity,
    ): CocsEntity? {
        ncr.ucrNumber?.let { ucrNumber ->
            cocRepo.findByUcrNumberAndCocTypeAndVersion(ucrNumber, "NCR", ncr.version)
                    ?.let { coc ->
                        return null
                    }
        }
                ?: kotlin.run {
                    var foreignNcr = CocsEntity()
                    KotlinLogging.logger { }.debug("Starting background task")
                    with(foreignNcr) {
                        coiNumber = ncr.ncrNumber ?: "UNKNOWN"
                        cocNumber = ncr.ncrNumber?.toUpperCase()
                        idfNumber = ncr.idfNumber ?: "UNKNOWN"
                        rfiNumber = ncr.rfiNumber ?: "UNKNOWN"
                        ucrNumber = ncr.ucrNumber
                        rfcDate = ncr.rfcDate
                        acceptableDocDate = ncr.acceptableDocDate
                        finalDocDate = ncr.finalDocDate
                        shipmentQuantityDelivered = "UNKNOWN"
                        cocIssueDate = ncr.ncrIssueDate
                        clean = "N"
                        cocRemarks = ncr.ncrRemarks ?: "NA"
                        coiRemarks = "UNKNOWN"
                        issuingOffice = ncr.issuingOffice ?: "UNKNOWN"
                        importerName = ncr.importerName
                        importerPin = ncr.importerPin
                        importerAddress1 = ncr.importerAddress1
                        importerAddress2 = ncr.importerAddress2 ?: "UNKNOWN"
                        importerCity = ncr.importerCity ?: "UNKNOWN"
                        importerCountry = ncr.importerCountry ?: "UNKNOWN"
                        importerZipCode = "UNKNOWN"
                        importerTelephoneNumber = ncr.importerTelephoneNumber ?: "UNKNOWN"
                        importerFaxNumber = ncr.importerFaxNumber ?: "UNKNOWN"
                        importerEmail = ncr.importerEmail ?: "UNKNOWN"
                        exporterName = ncr.exporterName ?: "UNKNOWN"
                        exporterPin = ncr.importerPin ?: "UNKNOWN"
                        exporterAddress1 = ncr.exporterAddress1 ?: "UNKOWN"
                        exporterAddress2 = ncr.exporterAddress2 ?: "UNKNOWN"
                        exporterCity = ncr.exporterCity ?: "UNKNOWN"
                        exporterCountry = ncr.exporterCountry ?: "UNKNOWN"
                        exporterZipCode = ncr.exporterZipCode ?: "UNKNOWN"
                        exporterTelephoneNumber = ncr.exporterTelephoneNumber ?: "UNKOWN"
                        exporterFaxNumber = ncr.exporterFaxNumber ?: "UNKOWN"
                        exporterEmail = ncr.exporterEmail ?: "UNKNOWN"
                        placeOfInspection = ncr.placeOfInspection ?: "UNKNOWN"
                        dateOfInspection = ncr.dateOfInspection
                        portOfDestination = ncr.portOfDestination ?: "UNKOWN"
                        shipmentMode = ncr.shipmentMode ?: "UNKNOWN"
                        countryOfSupply = ncr.countryOfSupply ?: "UNKNOWN"
                        finalInvoiceFobValue = ncr.finalInvoiceFobValue ?: 0.0
                        finalInvoiceNumber = ncr.finalInvoiceNumber ?: "NA"
                        finalInvoiceExchangeRate = ncr.finalInvoiceExchangeRate ?: 0.0
                        finalInvoiceCurrency = ncr.finalInvoiceCurrency ?: properties.applicationCurrencyCode
                        finalInvoiceDate = ncr.finalInvoiceDate ?: commonDaoServices.getTimestamp()
                        shipmentSealNumbers = ncr.shipmentSealNumbers ?: "UNKNOWN"
                        shipmentContainerNumber = ncr.shipmentContainerNumber ?: "UNKNOWN"
                        shipmentGrossWeight = ncr.shipmentGrossWeight?.toString() ?: "UNKNOWN"
                        cocRemarks = ncr.ncrRemarks ?: "UNKNOWN"
                        cocIssueDate = ncr.ncrIssueDate
                        route = ncr.route ?: "Z"
                        partner = user.id
                        reviewStatus = 0
                        version = ncr.version ?: 1
                        cocType = "NCR"
                        documentsType = "F"
                        productCategory = "UNKNOWN"
                        partner = null
                        createdBy = commonDaoServices.loggedInUserAuthentication().name
                        createdOn = commonDaoServices.getTimestamp()
                    }
                    // Add invoice details
                    foreignNcr = cocRepo.save(foreignNcr)
                    KotlinLogging.logger { }.info { "Foreign NCR = ${foreignNcr.id}" }
                    this.checkCocCoiDocumentCompliance(foreignNcr, user, "NCR")
                    foreignCocItems(ncr.ncrItems, foreignNcr, map)
                    return foreignNcr
                }

    }

    fun foreignCocItems(cocItems: List<CocItem>?, localCoc: CocsEntity, map: ServiceMapsEntity) {
        cocItems?.let { items ->
            for (item in items) {
                var localCocItems = CocItemsEntity()
                with(localCocItems) {
                    cocId = localCoc.id
                    shipmentLineNumber = item.shipmentLineNumber
                    shipmentLineHscode = item.shipmentLineHscode
                    shipmentLineQuantity = item.shipmentLineQuantity.let { BigDecimal.valueOf(it) }
                            ?: BigDecimal.ZERO
                    shipmentLineUnitofMeasure = item.shipmentLineUnitofMeasure
                    shipmentLineDescription = item.shipmentLineDescription
                    shipmentLineVin = item.shipmentLineVin ?: "UNKNOWN"
                    shipmentLineStickerNumber = item.shipmentLineStickerNumber ?: "UNKNOWN"
                    shipmentLineIcs = item.shipmentLineIcs ?: "UNKNOWN"
                    shipmentLineStandardsReference = item.shipmentLineStandardsReference ?: "UNKNOWN"
                    shipmentLineLicenceReference = item.shipmentLineLicenceReference ?: "UNKNOWN"
                    shipmentLineRegistration = item.shipmentLineRegistration ?: "UNKNOWN"
                    ownerPin = localCoc.importerPin ?: "UNKNOWN"
                    ownerName = localCoc.importerName ?: "UNKNOWN"
                    status = map.activeStatus
                    shipmentLineBrandName = item.shipmentBrandName ?: "UNKNOWN"
                    createdBy = commonDaoServices.loggedInUserAuthentication().name
                    createdOn = commonDaoServices.getTimestamp()
                }
                localCocItems = iCocItemRepository.save(localCocItems)
                KotlinLogging.logger { }.info { "Generated foreign coc item WITH id = ${localCocItems.id}" }
            }
        }
    }

    @Transactional
    fun foreignCoi(
            user: PvocPartnersEntity,
            coi: CoiEntityForm,
            map: ServiceMapsEntity,
    ): CocsEntity? {
        coi.ucrNumber?.let { ucrNumber ->
            cocRepo.findByUcrNumberAndCocTypeAndVersion(ucrNumber, "COI", coi.version)
                    ?.let { coc ->
                        return null
                    }
        } ?: kotlin.run {
            var localCoi = CocsEntity()
            KotlinLogging.logger { }.debug("Starting background task")
            try {
                with(localCoi) {
                    cocNumber = coi.coiNumber?.toUpperCase()
                    coiNumber = coi.coiNumber?.toUpperCase()
                    idfNumber = coi.idfNumber ?: "UNKNOWN"
                    rfiNumber = coi.rfiNumber ?: "UNKNOWN"
                    ucrNumber = coi.ucrNumber
                    acceptableDocDate = coi.acceptableDocDate
                    finalDocDate = coi.finalDocDate
                    rfcDate = coi.rfcDate
                    shipmentQuantityDelivered = "UNKNOWN"
                    cocIssueDate = coi.coiIssueDate
                    clean = "Y"
                    cocRemarks = coiRemarks ?: "NA"
                    coiRemarks = coi.coiRemarks ?: "UNKNOWN"
                    issuingOffice = coi.issuingOffice ?: "UNKNOWN"
                    importerName = coi.importerName
                    importerPin = coi.importerPin
                    importerAddress1 = coi.importerAddress1
                    importerAddress2 = coi.importerAddress2 ?: "UNKNOWN"
                    importerCity = coi.importerCity ?: "UNKNOWN"
                    importerCountry = coi.importerCountry ?: "UNKNOWN"
                    importerZipCode = coi.importerZipCode ?: "UNKNOWN"
                    importerTelephoneNumber = coi.importerTelephoneNumber ?: "UNKNOWN"
                    importerFaxNumber = coi.importerFaxNumber ?: "UNKNOWN"
                    importerEmail = coi.importerEmail ?: "UNKNOWN"
                    exporterName = coi.exporterName ?: "UNKNOWN"
                    exporterPin = coi.importerPin ?: "UNKNOWN"
                    exporterAddress1 = coi.exporterAddress1 ?: "UNKOWN"
                    exporterAddress2 = coi.exporterAddress2 ?: "UNKNOWN"
                    exporterCity = coi.exporterCity ?: "UNKNOWN"
                    exporterCountry = coi.exporterCountry ?: "UNKNOWN"
                    exporterZipCode = coi.exporterZipCode ?: "UNKNOWN"
                    exporterTelephoneNumber = coi.exporterTelephoneNumber ?: "UNKOWN"
                    exporterFaxNumber = coi.exporterFaxNumber ?: "UNKOWN"
                    exporterEmail = coi.exporterEmail ?: "UNKNOWN"
                    placeOfInspection = coi.placeOfInspection ?: "UNKNOWN"
                    dateOfInspection = coi.dateOfInspection
                    portOfDestination = coi.portOfDestination ?: "UNKOWN"
                    shipmentMode = coi.shipmentMode ?: "UNKNOWN"
                    countryOfSupply = coi.countryOfSupply ?: "UNKNOWN"
                    finalInvoiceFobValue = coi.finalInvoiceFobValue ?: 0.0
                    finalInvoiceNumber = coi.finalInvoiceNumber ?: "NA"
                    finalInvoiceExchangeRate = coi.finalInvoiceExchangeRate ?: 0.0
                    finalInvoiceCurrency = coi.finalInvoiceCurrency ?: properties.applicationCurrencyCode
                    finalInvoiceDate = coi.finalInvoiceDate ?: commonDaoServices.getTimestamp()
                    shipmentSealNumbers = coi.shipmentSealNumbers ?: "UNKNOWN"
                    shipmentContainerNumber = coi.shipmentContainerNumber ?: "UNKNOWN"
                    shipmentGrossWeight = coi.shipmentGrossWeight?.toString() ?: "UNKNOWN"
                    coiRemarks = coi.coiRemarks ?: "UNKNOWN"
                    route = coi.route ?: "Z"
                    version = coi.version ?: 1
                    cocType = "COI"
                    documentsType = "F"
                    productCategory = "UNKNOWN"
                    partner = user.id
                    reviewStatus = 0
                    createdBy = commonDaoServices.loggedInUserAuthentication().name
                    createdOn = commonDaoServices.getTimestamp()
                }
                // Add invoice details
                localCoi = cocRepo.save(localCoi)
                KotlinLogging.logger { }.info { "Foreign COI = ${localCoi.id}" }
                this.checkCocCoiDocumentCompliance(localCoi, user, "COI")
                foreignCoiItems(coi.coiItems, localCoi, map)
            } catch (e: Exception) {
                KotlinLogging.logger { }.debug("Threw error from forward express callback")
                KotlinLogging.logger { }.debug(e.message)
                KotlinLogging.logger { }.debug(e.toString())
            }
            return localCoi
        }

    }

    fun foreignCoiItems(coiItems: List<CoiItem>?, localCoi: CocsEntity, map: ServiceMapsEntity) {
        coiItems?.let { items ->
            for (item in items) {
                var localCocItems = CocItemsEntity()
                with(localCocItems) {
                    cocId = localCoi.id
                    shipmentLineNumber = item.shipmentLineNumber ?: 0
                    shipmentLineHscode = item.shipmentLineHsCode ?: ""
                    declaredHsCode = item.declaredHsCode
                    shipmentLineQuantity = item.shipmentLineQuantity?.let { BigDecimal.valueOf(it) } ?: BigDecimal.ZERO
                    shipmentLineUnitofMeasure = item.shipmentLineUnitofMeasure
                    shipmentLineDescription = item.shipmentLineDescription
                    shipmentLineVin = item.shipmentLineVin ?: "UNKNOWN"
                    shipmentLineStickerNumber = item.shipmentLineStickerNumber ?: "UNKNOWN"
                    shipmentLineIcs = item.shipmentLineIcs ?: "UNKNOWN"
                    shipmentLineStandardsReference = item.shipmentLineStandardsReference ?: "UNKNOWN"
                    shipmentLineLicenceReference = item.shipmentLineLicenceReference ?: "UNKNOWN"
                    shipmentLineRegistration = item.shipmentLineRegistration ?: "UNKNOWN"
                    ownerPin = localCoi.importerPin ?: "UNKNOWN"
                    ownerName = localCoi.importerName ?: "UNKNOWN"
                    status = map.activeStatus
                    shipmentLineBrandName = item.shipmentLineBrandName ?: "UNKNOWN"
                    createdBy = commonDaoServices.loggedInUserAuthentication().name
                    createdOn = commonDaoServices.getTimestamp()
                }
                localCocItems = iCocItemRepository.save(localCocItems)
                KotlinLogging.logger { }.info { "Generated Foreign coc item WITH id = ${localCocItems.id}" }
            }
        }
    }

    /**
     * Apply penalty configured or apply the default 15% penalty
     */
    private fun calculatePenalty(amount: BigDecimal?, first: Boolean, route: String?, user: PvocPartnersEntity): BigDecimal? {
        return this.timelinePenaltiesRepository.findByPartnerTypeAndRoute(user.partnerType, route
                ?: "OTHER")?.let { penalty ->
            if (first) {
                return (amount?.times(penalty.firstPenalty ?: BigDecimal.ZERO))?.divide(BigDecimal.valueOf(100))
            }
            return (amount?.times(penalty.applicablePenalty ?: BigDecimal.ZERO))?.divide(BigDecimal.valueOf(100))
        } ?: amount?.times(BigDecimal.valueOf(15).divide(BigDecimal.valueOf(100)))
    }

    private fun checkTimelineIssue(timeline: PvocTimelinesDataEntity, transaction: BillTransactionsEntity, user: PvocPartnersEntity, version: Long?): Boolean {
        val rfcToIspectionDays = timeline.rfcDate?.let { rfcDate ->
            timeline.dateOfInspection?.let { inspectionDate ->
                Duration.between(rfcDate.toLocalDateTime(), inspectionDate.toLocalDateTime()).toDays()
            } ?: 0
        } ?: 0
        val rfcToIsuanceDays = timeline.rfcDate?.let { rfcDate ->
            timeline.docIssueDate?.let { corIssuance ->
                Duration.between(rfcDate.toLocalDateTime(), corIssuance.toLocalDateTime()).toDays()
            } ?: 0
        } ?: 0
        val inspectionToIssueDays = timeline.dateOfInspection?.let { inspectionDate ->
            timeline.docIssueDate?.let { issueDate ->
                Duration.between(issueDate.toLocalDateTime(), inspectionDate.toLocalDateTime()).toDays()
            } ?: 0
        } ?: 0
        val paymentToIssueDate = timeline.paymentDate?.let { paymentDate ->
            timeline.docIssueDate?.let { issueDate ->
                Duration.between(issueDate.toLocalDateTime(), paymentDate.toLocalDateTime()).toDays()
            } ?: 0
        } ?: 0
        val acceptableDocToIssue = timeline.accDocumentsSubmissionDate?.let { bookingDate ->
            timeline.docIssueDate?.let { issueDate ->
                Duration.between(issueDate.toLocalDateTime(), bookingDate.toLocalDateTime()).toDays()
            } ?: 0
        } ?: 0

        val finalDocToIssue = timeline.finalDocumentsSubmissionDate?.let { bookingDate ->
            timeline.docIssueDate?.let { issueDate ->
                Duration.between(issueDate.toLocalDateTime(), bookingDate.toLocalDateTime()).toDays()
            } ?: 0
        } ?: 0
        this.timelinesConfigurationRepository.findByDocumentType(timeline.certType ?: "OTHER")?.let { config ->
            if (rfcToIspectionDays > config.rfcToInspectionMax) {
                timeline.rfcToInspectionViolation = true
                timeline.rfcToInspectionDays = rfcToIspectionDays
            }

            if (rfcToIsuanceDays > config.rfcToInsuanceMax) {
                timeline.rfcToIssuanceViolation = true
                timeline.rfcToIssuanceDays = rfcToIspectionDays
            }

            if (inspectionToIssueDays > config.inspectionToIssuanceMax) {
                timeline.inspectionToIssuanceViolation = true
                timeline.inspectionToIssuanceDays = inspectionToIssueDays
            }

            if (paymentToIssueDate > config.paymentToIssuanceMax) {
                timeline.paymentToIssuanceViolation = true
                timeline.paymentToIssuanceDays = paymentToIssueDate
            }

            if (acceptableDocToIssue > config.acceptableDocToIssuanceMax) {
                timeline.accDocumentsToIssuanceViolation = true
                timeline.accDocumentsToIssuanceDays = acceptableDocToIssue
            }
            if (finalDocToIssue > config.finalDocToInspectionMax) {
                timeline.finalDocumentsToIssuanceViolation = true
                timeline.finalDocumentsToIssuanceDays = finalDocToIssue
            }

        } ?: run {
            if (rfcToIspectionDays > 14) {
                timeline.rfcToInspectionViolation = true
                timeline.rfcToInspectionDays = rfcToIspectionDays
            }

            if (rfcToIsuanceDays > 14) {
                timeline.rfcToIssuanceViolation = true
                timeline.rfcToIssuanceDays = rfcToIspectionDays
            }

            if (inspectionToIssueDays > 14) {
                timeline.inspectionToIssuanceViolation = true
                timeline.inspectionToIssuanceDays = inspectionToIssueDays
            }

            if (paymentToIssueDate > 14) {
                timeline.paymentToIssuanceViolation = true
                timeline.paymentToIssuanceDays = paymentToIssueDate
            }

            if (acceptableDocToIssue > 14) {
                timeline.accDocumentsToIssuanceViolation = true
                timeline.accDocumentsToIssuanceDays = acceptableDocToIssue
            }
            if (finalDocToIssue > 14) {
                timeline.finalDocumentsToIssuanceViolation = true
                timeline.finalDocumentsToIssuanceDays = finalDocToIssue
            }
        }
        // TRX: Add billing details
        val rate: BigDecimal
        if ((user.chargeRate ?: BigDecimal.ZERO) > BigDecimal.ZERO) {
            rate = user.chargeRate ?: BigDecimal.ZERO
        } else {
            rate = user.partnerType?.chargeAmount ?: BigDecimal.ZERO
        }
        transaction.amount = transaction.fobAmount?.let { fob -> fob.times(rate).divide(BigDecimal.valueOf(100)) }
        // Skip add transaction when amount is less than or equal to zero or version is greater than one
        if (BigDecimal.ZERO < transaction.amount) {
            transaction.rate = rate
            if (version ?: 0 <= 1) {
                val trx = billingService.registerPvocTransaction(transaction, user)
                KotlinLogging.logger { }.info("Transaction added: ${trx?.tempReceiptNumber}")
            } else {
                KotlinLogging.logger { }.info("Transaction versioned: $version")
            }
        } else {
            KotlinLogging.logger { }.warn("Transaction did not have any amount: ${transaction.amount}")
        }
        // Save timeline if any violation is
        var timeAdded = false
        if (timeline.rfcToInspectionViolation || timeline.rfcToIssuanceViolation || timeline.inspectionToIssuanceViolation || timeline.paymentToIssuanceViolation || timeline.accDocumentsToIssuanceViolation || timeline.finalDocumentsToIssuanceViolation) {
            val date = LocalDateTime.now()
            timeline.royaltyValue = transaction.amount
            timeline.currencyCode = transaction.currency ?: properties.applicationCurrencyCode
            timeline.applicablePenalty = calculatePenalty(transaction.amount, true, timeline.route, user)
            timeline.createdOn = Timestamp.valueOf(date)
            timeline.createdBy = commonDaoServices.loggedInUserAuthentication().name
            timeline.modifiedOn = Timestamp.valueOf(date)
            timeline.modifiedBy = commonDaoServices.loggedInUserAuthentication().name
            timeline.recordYearMonth = YEAR_MONTH_FORMATTER.format(date)
            timeline.riskStatus = 1
            timeline.monitoringId = this.monitoringService.findMonitoringRecord(timeline.recordYearMonth!!, user).id
            this.timelinesRepository.save(timeline)
            timeAdded = true
        }


        return timeAdded
    }

    fun checkCocCoiDocumentCompliance(entity: CocsEntity, user: PvocPartnersEntity, documentType: String) {
        val transaction = BillTransactionsEntity()
        transaction.fobAmount = BigDecimal.valueOf(entity.finalInvoiceFobValue)
        transaction.description = "$documentType PVOC charges"
        transaction.currency = entity.finalInvoiceCurrency
        transaction.varField1 = "PVOC$documentType"
        transaction.invoiceNumber = entity.finalInvoiceNumber
        transaction.transactionId = entity.id.toString()

        val timeline = PvocTimelinesDataEntity()
        timeline.certType = documentType
        timeline.ucrNumber = entity.ucrNumber
        timeline.route = entity.route
        timeline.docConfirmationDate = entity.acceptableDocDate
        timeline.dateOfInspection = entity.dateOfInspection
        timeline.finalDocumentsSubmissionDate = entity.finalDocDate?.let { Timestamp(it.time) }
        timeline.requestDateOfInspection = entity.dateOfInspection
        timeline.paymentDate = entity.finalInvoiceDate
        timeline.rfcDate = entity.rfcDate?.let { Timestamp(it.time) }
        timeline.partnerId = user.id
        timeline.recordId = entity.id

        when (documentType) {
            "COC" -> {
                timeline.docIssueDate = entity.cocIssueDate
                timeline.certNumber = entity.cocNumber
            }
            "COI" -> {
                timeline.docIssueDate = entity.coiIssueDate
                timeline.certNumber = entity.coiNumber
            }
            "NCR" -> {
                timeline.docIssueDate = entity.cocIssueDate
                timeline.certNumber = entity.cocNumber
            }
        }
        // 1. Check timeline issues
        if (checkTimelineIssue(timeline, transaction, user, entity.version)) {
            KotlinLogging.logger { }.info("Added $documentType timeline issue: ${timeline.id}")
        }
        // 2. Check seal issues
        if ("A".equals(entity.route, true) && (entity.shipmentContainerNumber == null || "NA".equals(entity.shipmentContainerNumber, true) || "UNKNOWN".equals(entity.shipmentContainerNumber, true))) {
            val date = LocalDateTime.now()
            val sealIssue = PvocSealIssuesEntity()
            sealIssue.partnerId = user.id
            sealIssue.recordId = entity.id
            sealIssue.certType = documentType
            sealIssue.certNumber = timeline.certNumber
            sealIssue.route = entity.route
            sealIssue.ucrNumber = entity.ucrNumber
            sealIssue.recordYearMonth = YEAR_MONTH_FORMATTER.format(date)
            sealIssue.riskStatus = 1
            sealIssue.status = 1
            sealIssue.monitoringId = this.monitoringService.findMonitoringRecord(sealIssue.recordYearMonth!!, user).id
            sealIssue.createdBy = commonDaoServices.loggedInUserAuthentication().name
            sealIssue.createdOn = Timestamp.valueOf(date)
            sealIssue.modifiedOn = Timestamp.valueOf(date)
            this.sealIssuesRepository.save(sealIssue)
        }
        // 3. Check product categorization issues
        // TODO: add when categorization date is received
    }

    fun checkCorDocumentCompliance(entity: CorsBakEntity, rfcDate: Timestamp?, user: PvocPartnersEntity) {
        val transaction = BillTransactionsEntity()
        transaction.fobAmount = entity.inspectionFee?.let { BigDecimal.valueOf(it) }
        transaction.currency = entity.inspectionFeeCurrency
        transaction.description = "COR PVOC charges"
        transaction.invoiceNumber = entity.inspectionFeeReceipt

        val timeline = PvocTimelinesDataEntity()
        timeline.docConfirmationDate = entity.acceptableDocDate
        timeline.finalDocumentsSubmissionDate = entity.finalDocDate?.let { Timestamp(it.time) }
        timeline.rfcDate = rfcDate?.let { Timestamp(it.time) }
        timeline.docIssueDate = entity.corIssueDate
        timeline.dateOfInspection = entity.inspectionDate
        timeline.paymentDate = entity.inspectionFeePaymentDate
        timeline.certType = "COR"
        timeline.ucrNumber = entity.ucrNumber
        timeline.requestDateOfInspection = entity.inspectionDate
        timeline.route = entity.route
        timeline.certNumber = entity.corNumber
        timeline.partnerId = user.id
        timeline.recordId = entity.id
        transaction.transactionId = entity.id.toString()
        if (checkTimelineIssue(timeline, transaction, user, entity.version)) {
            KotlinLogging.logger { }.info("Added COR timeline issue: ${timeline.id}")
        }
    }

    @Transactional
    fun foreignCor(
            cor: CorEntityForm,
            s: ServiceMapsEntity,
            user: PvocPartnersEntity,
    ): CorsBakEntity? {
        var localCor = CorsBakEntity()
        //Get CD Item by cd doc id
        this.corsBakRepository.findByChasisNumberAndVersion(cor.chasisNumber!!, cor.version)?.let {
            return null
        } ?: run {
            // Fill checklist details
            with(localCor) {
                corNumber = cor.corNumber
                corIssueDate = cor.corIssueDate ?: commonDaoServices.getTimestamp()
                countryOfSupply = cor.countryOfSupply
                inspectionCenter = cor.inspectionCenter
                exporterName = cor.exporterName
                exporterAddress1 = cor.exporterAddress1
                exporterAddress2 = cor.exporterAddress2
                exporterEmail = cor.exporterEmail
                acceptableDocDate = cor.acceptableDocDate
                finalDocDate = cor.finalDocDate
                applicationBookingDate = cor.applicationBookingDate ?: commonDaoServices.getTimestamp()
                inspectionDate = cor.inspectionDate ?: commonDaoServices.getTimestamp()
                make = cor.make
                model = cor.model ?: "UNKNOWN"
                engineNumber = cor.engineNumber
                engineCapacity = cor.engineCapacity
                yearOfManufacture = cor.yearOfManufacture
                yearOfFirstRegistration = cor.yearOfFirstRegistration ?: "UNKNOWN"
                inspectionMileage = cor.inspectionMileage ?: "UNKNOWN"
                unitsOfMileage = cor.unitsOfMileage ?: "KM"
                inspectionRemarks = cor.inspectionRemarks ?: "UNKNOWN"
                previousRegistrationNumber = cor.previousRegistrationNumber ?: "NA"
                previousCountryOfRegistration = cor.previousCountryOfRegistration ?: "test"
                chasisNumber = cor.chasisNumber
                tareWeight = cor.tareWeight ?: 0.0
                loadCapacity = cor.loadCapacity ?: 0.0
                grossWeight = cor.grossWeight ?: 0.0
                numberOfAxles = cor.numberOfAxles ?: 0
                typeOfVehicle = cor.typeOfVehicle
                numberOfPassangers = cor.numberOfPassengers ?: 0
                typeOfBody = cor.typeOfBody ?: "NA"
                bodyColor = cor.bodyColor ?: "NA"
                fuelType = cor.fuelType ?: "NA"
                customsIeNo = "NA"
                transmission = cor.transmission ?: "NA"
                route = cor.route
                version = cor.version ?: 1
                approvalStatus = "!"
                ucrNumber = cor.ucrNumber ?: ""
                inspectionFeeCurrency = cor.inspectionFeeCurrency ?: "USD"
                inspectionFee = cor.inspectionFee ?: 0.0
                inspectionFeeReceipt = cor.inspectionFeeReceipt ?: "NA"
                inspectionOfficer = cor.inspectionOfficer ?: "NA"
                inspectionFeeExchangeRate = cor.inspectionFeeExchangeRate ?: 0.0
                inspectionFeePaymentDate = cor.inspectionFeePaymentDate ?: commonDaoServices.getTimestamp()
                inspectionRemarks = cor.inspectionRemarks ?: "No Remarks"
                status = s.activeStatus
                partner = user.id
                createdBy = commonDaoServices.loggedInUserAuthentication().name
                createdOn = commonDaoServices.getTimestamp()
            }
            KotlinLogging.logger { }.info("COR: $localCor")
            localCor = corsBakRepository.save(localCor)
            // Check document compliance
            this.checkCorDocumentCompliance(localCor, cor.rfcDate, user)
            KotlinLogging.logger { }.info { "Save Foreign CoR WITH id = ${localCor.id}" }
        }
        return localCor
    }


    @Transactional
    fun foreignRfc(rfc: RfcEntityForm, documentType: String, s: ServiceMapsEntity, user: PvocPartnersEntity): RfcEntity? {
        val rfcEntity = RfcEntity()
        val auth = this.commonDaoServices.loggedInUserAuthentication()
        this.rfcRepository.findByRfcNumber(rfc.rfcNumber!!)?.let {
            return null
        } ?: run {
            rfc.fillDetails(rfcEntity)
            rfcEntity.rfcDocumentType = documentType
            rfcEntity.partner = user.id
            rfcEntity.status = s.activeStatus.toLong()

            rfcEntity.createdBy = auth.name
            rfcEntity.createdOn = Timestamp.from(Instant.now())
            rfcEntity.modifiedBy = auth.name
            rfcEntity.modifiedOn = Timestamp.from(Instant.now())
            val saved = this.rfcRepository.save(rfcEntity)
            rfc.items?.let { items ->
                for (item in items) {
                    val rfcItem = RfcItemEntity()
                    rfcItem.rfcId = saved.id
                    rfcItem.declaredHsCode = item.declaredHsCode
                    rfcItem.itemQuantity = item.itemQuantity?.toString() ?: "0"
                    rfcItem.productDescription = item.productDescription
                    rfcItem.ownerName = item.ownerName
                    rfcItem.ownerPin = item.ownerPin
                    rfcItem.createdBy = auth.name
                    rfcItem.createdOn = Timestamp.from(Instant.now())
                    rfcItem.modifiedBy = auth.name
                    rfcItem.modifiedOn = Timestamp.from(Instant.now())
                    this.rfcItemRepository.save(rfcItem)
                }
            } ?: throw ExpectedDataNotFound("$documentType items are required")
        }
        return rfcEntity
    }

    @Transactional
    fun foreignRfcCor(rfc: RfcCorForm, s: ServiceMapsEntity, user: PvocPartnersEntity): RfcCorEntity? {
        val rfcEntity = RfcCorEntity()
        val auth = this.commonDaoServices.loggedInUserAuthentication()
        this.rfcCorRepository.findByRfcNumber(rfc.rfcNumber!!)?.let {
            return null
        } ?: run {
            rfc.fillCorRfc(rfcEntity)
            rfcEntity.partner = user.id
            rfcEntity.status = s.activeStatus.toLong()
            rfcEntity.createdBy = auth.name
            rfcEntity.createdOn = Timestamp.from(Instant.now())
            rfcEntity.modifiedBy = auth.name
            rfcEntity.modifiedOn = Timestamp.from(Instant.now())
            val saved = this.rfcCorRepository.save(rfcEntity)
        }
        return rfcEntity
    }

    fun getIdfData(idfNumber: String, ucrNumber: String): IdfEntityForm {
        val idfsEntity = when {
            StringUtils.hasLength(ucrNumber) -> this.idfsRepository.findFirstByUcr(ucrNumber)
            StringUtils.hasLength(idfNumber) -> this.idfsRepository.findFirstByIdfNumber(idfNumber)
            else -> null
        }
        return idfsEntity?.let {
            val items = idfsItemRepository.findByIdfId(it.id)
            IdfEntityForm.fromEntity(idfsEntity, items)
        } ?: throw ExpectedDataNotFound("IDF with UCR or IDF number not found")
    }

    fun getRfcData(rfcNumber: String, ucrNumber: String, documentType: String): Any {
        when (documentType.toUpperCase()) {
            "COR" -> {
                val rfcEntity = when {
                    StringUtils.hasLength(ucrNumber) -> this.rfcCorRepository.findByUcrNumber(ucrNumber)
                    StringUtils.hasLength(rfcNumber) -> this.rfcCorRepository.findByRfcNumber(rfcNumber)
                    else -> null
                }
                return rfcEntity?.let {
                    RfcCorForm.fromEntity(rfcEntity)
                } ?: throw ExpectedDataNotFound("COR RFC with UCR or RFC number not found")
            }
            else -> {
                val rfcEntity = when {
                    StringUtils.hasLength(ucrNumber) -> this.rfcRepository.findByUcrNumber(ucrNumber)
                    StringUtils.hasLength(rfcNumber) -> this.rfcRepository.findByRfcNumber(rfcNumber)
                    else -> null
                }
                return rfcEntity?.let {
                    val items = rfcItemRepository.findByRfcId(it.id)
                    RfcEntityForm.fromEntity(rfcEntity, items)
                } ?: throw ExpectedDataNotFound("$documentType RFC with UCR or RFC number not found")
            }
        }
    }

    @Transactional
    fun foreignIdfData(idf: IdfEntityForm, s: ServiceMapsEntity, user: PvocPartnersEntity): IdfsEntity? {
        val idfEntity = IdfsEntity()
        val auth = this.commonDaoServices.loggedInUserAuthentication()
        this.idfsRepository.findFirstByUcr(idf.ucrNumber!!)?.let {
            return null
        } ?: run {
            idfEntity.idfNumber = idf.idfNumber
            idfEntity.ucr = idf.ucrNumber
            idfEntity.importerName = idf.importerName
            idfEntity.importerAddress = idf.importerAddress
            idfEntity.importerFax = idf.importerFaxNumber
            idfEntity.importerTelephoneNumber = idf.importerTelephoneNumber
            idfEntity.importerEmail = idf.importerEmail
            idfEntity.importerContactName = idf.importerContactName
            idfEntity.sellerName = idf.exporterName
            idfEntity.sellerAddress = idf.exporterAddress
            idfEntity.sellerFax = idf.exporterFaxNumber
            idfEntity.sellerTelephoneNumber = idf.exporterTelephoneNumber
            idfEntity.sellerContactName = idf.exporterContactName
            idfEntity.sellerEmail = idf.exporterEmail
            idfEntity.countryOfSupply = idf.countryOfSupply
            idfEntity.portOfDischarge = idf.portOfDischarge
            idfEntity.portOfCustomsClearance = idf.portOfCustomsClearance
            idfEntity.modeOfTransport = idf.modeOfTransport
            idfEntity.countryOfSupply = idf.countryOfSupply
            idfEntity.comesa = idf.comesa
            idfEntity.invoiceNumber = idf.invoiceNumber
            idfEntity.invoiceDate = idf.invoiceDate
            idfEntity.currency = idf.currency
            idfEntity.exchangeRate = idf.exchangeRate ?: 0.0
            idfEntity.fobValue = idf.fobValue ?: 0.0
            idfEntity.freight = idf.freight ?: 0.0
            idfEntity.insurance = idf.insurance ?: 0.0
            idfEntity.observations = idf.observations ?: "NA"
            idfEntity.otherCharges = idf.otherCharges ?: 0.0
            idfEntity.usedStatus = when (idf.usedStatus) {
                true -> 1
                else -> 0
            }
            idfEntity.total = idf.total ?: 0.0
            idfEntity.partner = user.id

            idfEntity.status = s.activeStatus

            val saved = this.idfsRepository.save(idfEntity)
            idf.items?.let { items ->
                for (item in items) {
                    val idfItem = IdfItemsEntity()
                    idfItem.idfId = saved.id
                    idfItem.itemDescription = item.itemDescription
                    idfItem.quantity = item.quantity ?: 0
                    idfItem.hsCode = item.hsCode
                    idfItem.unitOfMeasure = item.unitOfMeasure
                    idfItem.newUsed = when (item.used) {
                        true -> "USED"
                        else -> "NEW"
                    }
                    idfItem.applicableStandard = item.applicableStandard
                    idfItem.itemCost = item.itemCost ?: 0
                    idfItem.createdBy = auth.name
                    idfItem.createdOn = Timestamp.from(Instant.now())
                    idfItem.modifiedBy = auth.name
                    idfItem.modifiedOn = Timestamp.from(Instant.now())
                    this.idfsItemRepository.save(idfItem)
                }
            }
        }
        return idfEntity
    }

    fun foreignRiskProfile(rfc: RiskProfileForm, s: ServiceMapsEntity, user: PvocPartnersEntity): RiskProfileEntity? {
        val auth = this.commonDaoServices.loggedInUserAuthentication()
        val risk = RiskProfileEntity()
        risk.brandName = rfc.brandName
        risk.categorizationDate = rfc.categorizationDate
        risk.countryOfSupply = rfc.countryOfSupply
        risk.exporterName = rfc.exporterName
        risk.exporterPin = rfc.exporterPin
        risk.hsCode = rfc.hsCode
        risk.importerName = rfc.importerName
        risk.importerPin = rfc.importerPin
        risk.riskDescription = rfc.riskDescription
        risk.riskLevel = rfc.riskLevel
        risk.remarks = rfc.remarks
        risk.partnerId = user.id
        risk.manufacturer = rfc.manufacturer
        risk.traderName = rfc.traderName
        risk.productDescription = rfc.productDescription
        risk.status = s.activeStatus.toLong()
        risk.createdBy = auth.name
        risk.createdOn = Timestamp.from(Instant.now())
        risk.modifiedBy = auth.name
        risk.modifiedOn = Timestamp.from(Instant.now())
        return this.riskProfileRepository.save(risk)
    }

    fun listRiskProfile(clientId: String, dateStr: String?, pg: PageRequest): Page<RiskProfileEntity> {
        val requestDate = when {
            StringUtils.hasLength(dateStr) -> {
                REPORT_DATE_FORMATTER.parse(dateStr.orEmpty())
                dateStr.orEmpty()
            }
            else -> REPORT_DATE_FORMATTER.format(LocalDateTime.now())
        }
        KotlinLogging.logger { }.info("Risk Profile: $requestDate")
        return this.riskProfileRepository.findByCategorizationDateAndCreatedByNotEquals(requestDate, clientId, pg)
    }
}
