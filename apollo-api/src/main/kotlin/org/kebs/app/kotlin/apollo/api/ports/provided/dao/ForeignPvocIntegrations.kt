package org.kebs.app.kotlin.apollo.api.ports.provided.dao

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.payload.request.*
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.model.pvc.PvocPartnersEntity
import org.kebs.app.kotlin.apollo.store.repo.*
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.sql.Timestamp
import java.time.Instant
import javax.transaction.Transactional

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
                            rfiNumber = coc.rfiNumber ?: "UNKNOWN"
                            ucrNumber = coc.ucrNumber
                            rfcDate = commonDaoServices.getTimestamp()
                            shipmentQuantityDelivered = "UNKNOWN"
                            cocIssueDate = commonDaoServices.getTimestamp()
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
                            finalInvoiceCurrency = coc.finalInvoiceCurrency ?: "KES"
                            finalInvoiceDate = coc.finalInvoiceDate ?: commonDaoServices.getTimestamp()
                            shipmentSealNumbers = coc.shipmentSealNumbers ?: "UNKNOWN"
                            shipmentContainerNumber = coc.shipmentContainerNumber ?: "UNKNOWN"
                            shipmentGrossWeight = coc.shipmentGrossWeight?.toString() ?: "UNKNOWN"
                            cocRemarks = coc.cocRemarks ?: "UNKNOWN"
                            route = coc.route ?: "Z"
                            partner = user.id
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

    fun foreignNcr(
            user: PvocPartnersEntity,
            ncr: NcrEntityForm,
            map: ServiceMapsEntity,
    ): CocsEntity? {
        ncr.ucrNumber?.let { ucrNumber ->
            cocRepo.findByUcrNumberAndCocType(ucrNumber, "NCR")
                    ?.let { coc ->
                        return null
                    }
        }
                ?: kotlin.run {
                    var foreignNcr = CocsEntity()
                    KotlinLogging.logger { }.debug("Starting background task")
                    try {
                        with(foreignNcr) {
                            coiNumber = "UNKNOWN"
                            cocNumber = ncr.ncrNumber?.toUpperCase()
                            idfNumber = ncr.idfNumber ?: "UNKNOWN"
                            rfiNumber = ncr.rfiNumber ?: "UNKNOWN"
                            ucrNumber = ncr.ucrNumber
                            rfcDate = commonDaoServices.getTimestamp()
                            shipmentQuantityDelivered = "UNKNOWN"
                            cocIssueDate = commonDaoServices.getTimestamp()
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
                            finalInvoiceCurrency = ncr.finalInvoiceCurrency ?: "KES"
                            finalInvoiceDate = ncr.finalInvoiceDate ?: commonDaoServices.getTimestamp()
                            shipmentSealNumbers = ncr.shipmentSealNumbers ?: "UNKNOWN"
                            shipmentContainerNumber = ncr.shipmentContainerNumber ?: "UNKNOWN"
                            shipmentGrossWeight = ncr.shipmentGrossWeight?.toString() ?: "UNKNOWN"
                            cocRemarks = ncr.ncrRemarks ?: "UNKNOWN"
                            cocIssueDate = ncr.ncrIssueDate
                            route = ncr.route ?: "Z"
                            partner = user.id
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
                        foreignCocItems(ncr.ncrItems, foreignNcr, map)
                    } catch (e: Exception) {
                        KotlinLogging.logger { }.debug("Threw error from forward express callback")
                        KotlinLogging.logger { }.debug(e.message)
                        KotlinLogging.logger { }.debug(e.toString())
                    }
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

    @Transactional
    fun foreignCoi(
            user: PvocPartnersEntity,
            coi: CoiEntityForm,
            map: ServiceMapsEntity,
    ): CocsEntity? {
        coi.ucrNumber?.let { ucrNumber ->
            cocRepo.findByUcrNumberAndCocType(ucrNumber, "COI")
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
                    rfcDate = commonDaoServices.getTimestamp()
                    shipmentQuantityDelivered = "UNKNOWN"
                    cocIssueDate = commonDaoServices.getTimestamp()
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
                    finalInvoiceCurrency = coi.finalInvoiceCurrency ?: "KES"
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
                    createdBy = commonDaoServices.loggedInUserAuthentication().name
                    createdOn = commonDaoServices.getTimestamp()
                }
                // Add invoice details
                localCoi = cocRepo.save(localCoi)
                KotlinLogging.logger { }.info { "localCoc = ${localCoi.id}" }
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

    @Transactional
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
                corIssueDate = cor.corIssueDate ?: commonDaoServices.getTimestamp()
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
                version = cor.version ?: 1
                approvalStatus = "!"
                ucrNumber = cor.ucrNumber ?: ""
                inspectionFeeCurrency = cor.inspectionFeeCurrency ?: "USD"
                inspectionFee = cor.inspectionFee ?: 0.0
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
            KotlinLogging.logger { }.info { "Save Foreign CoR WITH id = ${localCor.id}" }
        }
        return localCor
    }

    @Transactional
    fun foreignRfcCoi(rfc: RfcCoiEntityForm, s: ServiceMapsEntity, user: PvocPartnersEntity): RfcCoiEntity? {
        val rfcEntity = RfcCoiEntity()
        val auth = this.commonDaoServices.loggedInUserAuthentication()
        this.rfcCoiRepository.findByRfcNumber(rfc.rfcNumber!!)?.let {
            return null
        } ?: run {
            this.cocRepo.findByUcrNumberAndCocType(rfc.ucrNumber!!, "COI")?.let { coi ->
                rfcEntity.rfcNumber = rfc.rfcNumber
                rfcEntity.coiId = coi.id
                rfcEntity.idfNumber = rfc.idfNumber
                rfcEntity.ucrNumber = rfc.ucrNumber
                rfcEntity.rfcDate = rfc.rfcDate
                rfcEntity.countryOfDestination = rfc.countryOfDestination
                rfcEntity.applicationType = rfc.applicationType
                rfcEntity.solReference = rfc.solReference
                rfcEntity.sorReference = rfc.sorReference
                rfcEntity.importerName = rfc.importerName
                rfcEntity.importerCountry = rfc.importerCountry
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
                rfcEntity.exporterEmail = rfc.exporterEmail
                rfcEntity.exporterFaxNumber = rfc.exporterFaxNumber
                rfcEntity.exporterTelephoneNumber = rfc.exporterTelephoneNumber
                rfcEntity.exporterZipcode = rfc.exporterZipCode
                rfcEntity.placeOfInspection = rfc.placeOfInspection
                rfcEntity.placeOfInspectionAddress = rfc.placeOfInspectionAddress
                rfcEntity.placeOfInspectionContacts = rfc.placeOfInspectionContacts
                rfcEntity.placeOfInspectionEmail = rfc.placeOfInspectionEmail
                rfcEntity.portOfDischarge = rfc.portOfDischarge
                rfcEntity.portOfLoading = rfc.portOfLoading
                rfcEntity.shipmentMethod = rfc.shipmentMethod
                rfcEntity.countryOfSupply = rfc.countryOfSupply
                rfcEntity.partner = user.id
                rfcEntity.route = rfc.route
                rfcEntity.status = s.activeStatus.toLong()
                rfcEntity.goodsCondition = rfc.goodsCondition
                rfcEntity.assemblyState = rfc.assemblyState
                rfcEntity.linkToAttachedDocuments = rfc.linkToAttachedDocuments
                val saved = this.rfcCoiRepository.save(rfcEntity)
                rfc.items?.let { items ->
                    for (item in items) {
                        val rfcItem = RfcCoiItemsEntity()
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
                        this.rfcCoiItemRepository.save(rfcItem)
                    }
                } ?: throw ExpectedDataNotFound("COI with ucr number not found")
            }
        }
        return rfcEntity
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
        risk.status = s.activeStatus.toLong()
        risk.createdBy = auth.name
        risk.createdOn = Timestamp.from(Instant.now())
        risk.modifiedBy = auth.name
        risk.modifiedOn = Timestamp.from(Instant.now())
        return this.riskProfileRepository.save(risk)
    }
}
