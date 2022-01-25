package org.kebs.app.kotlin.apollo.api.ports.provided.dao

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.payload.request.*
import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.model.pvc.PvocPartnersEntity
import org.kebs.app.kotlin.apollo.store.repo.*
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.sql.Timestamp
import java.time.Instant

@Service
class ForeignPvocIntegrations(
        private val cocRepo: ICocsRepository,
        private val iCocItemRepository: ICocItemRepository,
        private val corsBakRepository: ICorsBakRepository,
        private val rfcCoiRepository: IRfcCoiRepository,
        private val rfcCoiItemRepository: IRfcCoiItemsRepository,
        private val riskProfileRepository: IRiskProfileRepository,
        private val idfsRepository: IdfsEntityRepository,
        private val idfsItemRepository: IdfItemsEntityRepository,
        private val commonDaoServices: CommonDaoServices
) {

    fun foreignCoc(
            user: PvocPartnersEntity,
            coc: CocEntityForm,
            map: ServiceMapsEntity,
    ): CocsEntity? {
        coc.ucrNumber?.let { ucrNumber ->
            cocRepo.findByUcrNumberAndCocType(ucrNumber, "COC")
                    ?.let { coc ->
                        return null
                    }
        }
                ?: kotlin.run {
                    var localCoc = CocsEntity()
                    KotlinLogging.logger { }.debug("Starting background task")
                    try {
                        with(localCoc) {
                            coiNumber = "UNKNOWN"
                            cocNumber = coc.cocNumber?.toUpperCase()
                            idfNumber = coc.idfNumber ?: "UNKNOWN"
                            rfiNumber = "UNKNOWN"
                            ucrNumber = coc.ucrNumber
                            rfcDate = commonDaoServices.getTimestamp()
                            shipmentQuantityDelivered = "UNKNOWN"
                            cocIssueDate = commonDaoServices.getTimestamp()
                            clean = "Y"
                            cocRemarks = coiRemarks ?: "NA"
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
                            finalInvoiceCurrency = coc.finalInvoiceCurrency ?: "KES"
                            finalInvoiceDate = coc.finalInvoiceDate ?: commonDaoServices.getTimestamp()
                            shipmentSealNumbers = coc.shipmentSealNumbers ?: "UNKNOWN"
                            shipmentContainerNumber = coc.shipmentContainerNumber ?: "UNKNOWN"
                            shipmentGrossWeight = coc.shipmentGrossWeight ?: "UNKNOWN"
                            cocRemarks = coc.cocRemarks ?: "UNKNOWN"
                            route = coc.route ?: "Z"
                            partner = user.id
                            version = coc.version ?: 1
                            cocType = "COC"
                            documentsType="F"
                            productCategory = "UNKNOWN"
                            partner = null
                            createdBy = commonDaoServices.loggedInUserAuthentication().name
                            createdOn = commonDaoServices.getTimestamp()
                        }
                        // Add invoice details
                        localCoc = cocRepo.save(localCoc)
                        KotlinLogging.logger { }.info { "localCoc = ${localCoc.id}" }
                        foreignCocItems(coc.cocItems, localCoc, map)
                    } catch (e: Exception) {
                        KotlinLogging.logger { }.debug("Threw error from forward express callback")
                        KotlinLogging.logger { }.debug(e.message)
                        KotlinLogging.logger { }.debug(e.toString())
                    }
                    return localCoc
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
                    shipmentLineQuantity = item.shipmentLineQuantity.let { BigDecimal.valueOf(it) } ?: BigDecimal.ZERO
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
                KotlinLogging.logger { }.info { "Generated Local coc item WITH id = ${localCocItems.id}" }
            }
        }
    }

    fun foreignCoi(
            user: PvocPartnersEntity,
            coc: CoiEntityForm,
            map: ServiceMapsEntity,
    ): CocsEntity? {
        coc.ucrNumber?.let { ucrNumber ->
            cocRepo.findByUcrNumberAndCocType(ucrNumber, "COI")
                    ?.let { coc ->
                        return null
                    }
        } ?: kotlin.run {
            var localCoc = CocsEntity()
            KotlinLogging.logger { }.debug("Starting background task")
            try {
                with(localCoc) {
                    coiNumber = "UNKNOWN"
                    coiNumber = coc.coiNumber?.toUpperCase()
                    idfNumber = coc.idfNumber ?: "UNKNOWN"
                    rfiNumber = "UNKNOWN"
                    ucrNumber = coc.ucrNumber
                    rfcDate = commonDaoServices.getTimestamp()
                    shipmentQuantityDelivered = "UNKNOWN"
                    cocIssueDate = commonDaoServices.getTimestamp()
                    clean = "Y"
                    cocRemarks = coiRemarks ?: "NA"
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
                    finalInvoiceCurrency = coc.finalInvoiceCurrency ?: "KES"
                    finalInvoiceDate = coc.finalInvoiceDate ?: commonDaoServices.getTimestamp()
                    shipmentSealNumbers = coc.shipmentSealNumbers ?: "UNKNOWN"
                    shipmentContainerNumber = coc.shipmentContainerNumber ?: "UNKNOWN"
                    shipmentGrossWeight = coc.shipmentGrossWeight ?: "UNKNOWN"
                    cocRemarks = coc.cocRemarks ?: "UNKNOWN"
                    route = coc.route ?: "Z"
                    version = coc.version ?: 1
                    cocType = "COI"
                    documentsType="F"
                    productCategory = "UNKNOWN"
                    partner = user.id
                    createdBy = commonDaoServices.loggedInUserAuthentication().name
                    createdOn = commonDaoServices.getTimestamp()
                }
                // Add invoice details
                localCoc = cocRepo.save(localCoc)
                KotlinLogging.logger { }.info { "localCoc = ${localCoc.id}" }
                foreignCoiItems(coc.coiItems, localCoc, map)
            } catch (e: Exception) {
                KotlinLogging.logger { }.debug("Threw error from forward express callback")
                KotlinLogging.logger { }.debug(e.message)
                KotlinLogging.logger { }.debug(e.toString())
            }
            return localCoc
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
                KotlinLogging.logger { }.info { "Generated Local coc item WITH id = ${localCocItems.id}" }
            }
        }
    }

    fun foreignCor(
            cor: CorEntityForm,
            s: ServiceMapsEntity,
            user: PvocPartnersEntity,
    ): CorsBakEntity? {
        var localCor = CorsBakEntity()
        //Get CD Item by cd doc id
        this.corsBakRepository.findByChasisNumber(cor.chasisNumber!!)?.let {
            return null
        } ?: run {
            // Fill checklist details
            with(localCor) {
                corNumber = cor.corNumber
                corIssueDate = commonDaoServices.getTimestamp()
                countryOfSupply = cor.countryOfSupply
                inspectionCenter = cor.inspectionCenter
                exporterName = cor.exporterName
                exporterAddress1 = cor.exporterAddress1
                exporterAddress2 = cor.exporterAddress2
                exporterEmail = cor.exporterEmail
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
                tareWeight = cor.tareWeight ?: 0
                loadCapacity = cor.loadCapacity ?: 0
                grossWeight = cor.grossWeight ?: 0
                numberOfAxles = cor.numberOfAxles ?: 0
                typeOfVehicle = cor.typeOfVehicle
                numberOfPassangers = cor.numberOfPassengers ?: 0
                typeOfBody = cor.typeOfBody ?: "NA"
                bodyColor = cor.bodyColor ?: "NA"
                fuelType = cor.fuelType ?: "NA"
                customsIeNo = "NA"
                transmission = cor.transmission ?: "NA"
                version = cor.version ?: 1
                approvalStatus = "!"
                ucrNumber = cor.ucrNumber ?: ""
                inspectionFeeCurrency = cor.inspectionFeeCurrency ?: "USD"
                inspectionFee = cor.inspectionFee ?: 0.0
                inspectionOfficer = cor.inspectionOfficer ?: "NA"
                inspectionFeeExchangeRate = cor.inspectionFeeExchangeRate ?: 0
                inspectionFeePaymentDate = cor.inspectionFeePaymentDate ?: commonDaoServices.getTimestamp()
                inspectionRemarks = cor.inspectionRemarks ?: "No Remarks"
                status = s.activeStatus
                partner = user.id
                createdBy = commonDaoServices.loggedInUserAuthentication().name
                createdOn = commonDaoServices.getTimestamp()
            }
            KotlinLogging.logger { }.info("COR: $localCor")
            localCor = corsBakRepository.save(localCor)
            KotlinLogging.logger { }.info { "Save Foreign CoR WITH id = ${localCor.id}" }
        }
        return localCor
    }

    fun foreignRfcCoi(rfc: RfcCoiEntityForm, s: ServiceMapsEntity, user: PvocPartnersEntity): RfcCoiEntity? {
        val rfcEntity = RfcCoiEntity()
        val auth = this.commonDaoServices.loggedInUserAuthentication()
        this.rfcCoiRepository.findByRfcNumber(rfc.rfcNumber!!)?.let {
            return null
        } ?: run {
            val coi = this.cocRepo.findByUcrNumberAndCocType(rfc.ucrNumber!!, "COI")
            rfcEntity.rfcNumber = rfc.rfcNumber
            rfcEntity.coiId = coi?.id
            rfcEntity.idfNumber = rfc.idfNumber
            rfcEntity.ucrNumber = rfc.ucrNumber
            rfcEntity.rfcDate = rfc.rfcDate
            rfcEntity.countryOfDestination = rfc.countryOfDestination
            rfcEntity.applicationType = rfc.applicationType
            rfcEntity.solReference = rfc.solReference
            rfcEntity.sorReference = rfc.sorReference
            rfcEntity.importerName = rfc.importerName
            rfcEntity.importerAddress1 = rfc.importerAddress1
            rfcEntity.importerAddress2 = rfc.importerAddress2
            rfcEntity.importerCity = rfc.importerCity
            rfcEntity.importerFaxNumber = rfc.importerFaxNumber
            rfcEntity.importerPin = rfc.importerPin
            rfcEntity.importerZipcode = rfc.importerZipCode
            rfcEntity.importerTelephoneNumber = rfc.importerTelephoneNumber
            rfcEntity.importerEmail = rfc.importerEmail
            rfcEntity.exporterName = rfc.exporterName
            rfcEntity.exporterPin = rfc.exporterPin
            rfcEntity.exporterCity = rfc.exporterCity
            rfcEntity.exporterAddress1 = rfc.exporterAddress1
            rfcEntity.exporterAddress2 = rfc.exporterAddress2
            rfcEntity.exporterCountry = rfc.exporterCountry
            rfcEntity.exporterFaxNumber = rfc.exporterFaxNumber
            rfcEntity.exporterTelephoneNumber = rfc.exporterTelephoneNumber
            rfcEntity.exporterZipcode = rfc.exporterZipCode
            rfcEntity.placeOfInspection = rfc.placeOfInspection
            rfcEntity.placeOfInspectionAddress = rfc.placeOfInspectionAddress
            rfcEntity.placeOfInspectionContacts = rfc.placeOfInspectionContacts
            rfcEntity.placeOfInspectionEmail = rfc.placeOfInspectionEmail
            rfcEntity.portOfDischarge = rfc.portOfDischarge
            rfcEntity.portOfLoading = rfc.portOfLoading
            rfcEntity.shipmentMethod = rfc.shipmentMode
            rfcEntity.countryOfSupply = rfc.countryOfSupply
            rfcEntity.partner = user.id
            rfcEntity.route = rfc.route
            rfcEntity.status = s.activeStatus.toLong()
            rfcEntity.goodsCondition = rfcEntity.goodsCondition
            rfcEntity.assemblyState = rfc.assemblyState
            rfcEntity.linkToAttachedDocuments = rfc.linkToAttachedDocuments?.firstOrNull()
            val saved = this.rfcCoiRepository.save(rfcEntity)
            rfc.items?.let { items ->
                for (item in items) {
                    val rfcItem = RfcCoiItemsEntity()
                    rfcItem.rfcId = saved.id
                    rfcItem.declaredHsCode = item.declaredHsCode
                    rfcItem.itemQuantity = item.itemQuantity
                    rfcItem.productDescription = item.productDescription
                    rfcItem.ownerName = item.ownerName
                    rfcItem.ownerPin = item.ownerPin
                    rfcItem.createdBy = auth.name
                    rfcItem.createdOn = Timestamp.from(Instant.now())
                    rfcItem.modifiedBy = auth.name
                    rfcItem.modifiedOn = Timestamp.from(Instant.now())
                    this.rfcCoiItemRepository.save(rfcItem)
                }
            }
        }
        return rfcEntity
    }

    fun foreignIdfData(rfc: IdfEntityForm, s: ServiceMapsEntity, user: PvocPartnersEntity): IdfsEntity? {
        val rfcEntity = IdfsEntity()
        val auth = this.commonDaoServices.loggedInUserAuthentication()
        this.idfsRepository.findFirstByUcr(rfc.ucrNumber!!)?.let {
            return null
        } ?: run {
            rfcEntity.idfNumber = rfc.idfNumber
            rfcEntity.ucr = rfc.ucrNumber
            rfcEntity.importerName = rfc.importerName
            rfcEntity.importerAddress = rfc.importerAddress
            rfcEntity.importerFax = rfc.importerFaxNumber
            rfcEntity.importerTelephoneNumber = rfc.importerTelephoneNumber
            rfcEntity.importerEmail = rfc.importerEmail
            rfcEntity.importerContactName = rfc.importerContactName
            rfcEntity.sellerName = rfc.exporterName
            rfcEntity.sellerAddress = rfc.exporterAddress
            rfcEntity.sellerFax = rfc.exporterFaxNumber
            rfcEntity.sellerTelephoneNumber = rfc.exporterTelephoneNumber
            rfcEntity.sellerContactName = rfc.exporterContactName
            rfcEntity.sellerEmail = rfc.exporterEmail
            rfcEntity.countryOfSupply = rfc.countryOfSupply
            rfcEntity.portOfDischarge = rfc.portOfDischarge
            rfcEntity.portOfCustomsClearance = rfc.portOfCustomsClearance
            rfcEntity.modeOfTransport = rfc.modeOfTransport
            rfcEntity.countryOfSupply = rfc.countryOfSupply
            rfcEntity.comesa = rfc.comesa
            rfcEntity.invoiceNumber = rfcEntity.invoiceNumber
            rfcEntity.invoiceDate = rfc.invoiceDate
            rfcEntity.currency = rfc.currency
            rfcEntity.exchangeRate = rfc.exchangeRate ?: 0.0
            rfcEntity.fobValue = rfc.fobValue ?: 0.0
            rfcEntity.freight = rfc.freight ?: 0.0
            rfcEntity.insurance = rfc.insurance ?: 0.0
            rfcEntity.otherCharges = rfc.otherChargers ?: 0.0
            rfcEntity.usedStatus = when ("YES".equals(rfc.usedStatus, true)) {
                true -> 1
                else -> 0
            }
            rfcEntity.total = rfc.total ?: 0.0
            rfcEntity.partner = user.id

            rfcEntity.status = s.activeStatus

            val saved = this.idfsRepository.save(rfcEntity)
            rfc.items?.let { items ->
                for (item in items) {
                    val rfcItem = IdfItemsEntity()
                    rfcItem.idfId = saved.id
                    rfcItem.itemDescription = item.itemDescription
                    rfcItem.quantity = item.quantity ?: 0
                    rfcItem.hsCode = item.hsCode
                    rfcItem.unitOfMeasure = item.unitOfMeasure
                    rfcItem.newUsed = item.newUsed
                    rfcItem.applicableStandard = item.applicableStandard
                    rfcItem.itemCost = item.itemCost ?: 0
                    rfcItem.createdBy = auth.name
                    rfcItem.createdOn = Timestamp.from(Instant.now())
                    rfcItem.modifiedBy = auth.name
                    rfcItem.modifiedOn = Timestamp.from(Instant.now())
                    this.idfsItemRepository.save(rfcItem)
                }
            }
        }
        return rfcEntity
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
        risk.status = s.activeStatus.toLong()
        risk.createdBy = auth.name
        risk.createdOn = Timestamp.from(Instant.now())
        risk.modifiedBy = auth.name
        risk.modifiedOn = Timestamp.from(Instant.now())
        return this.riskProfileRepository.save(risk)
    }
}