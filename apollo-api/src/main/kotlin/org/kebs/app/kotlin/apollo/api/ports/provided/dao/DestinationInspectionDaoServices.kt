package org.kebs.app.kotlin.apollo.api.ports.provided.dao

import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import okhttp3.internal.toLongOrDefault
import org.json.JSONException
import org.json.JSONObject
import org.json.XML
import org.kebs.app.kotlin.apollo.api.notifications.Notifications
import org.kebs.app.kotlin.apollo.api.ports.provided.emailDTO.*
import org.kebs.app.kotlin.apollo.api.ports.provided.sftp.SftpServiceImpl
import org.kebs.app.kotlin.apollo.api.service.ConsignmentCertificatesIssues
import org.kebs.app.kotlin.apollo.api.service.ConsignmentDocumentStatus
import org.kebs.app.kotlin.apollo.common.dto.MinistryInspectionListResponseDto
import org.kebs.app.kotlin.apollo.common.dto.kesws.receive.DeclarationVerificationMessage
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.exceptions.ServiceMapNotFoundException
import org.kebs.app.kotlin.apollo.common.utils.generateRandomText
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.customdto.*
import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.model.auction.AuctionItemDetails
import org.kebs.app.kotlin.apollo.store.model.auction.AuctionRequests
import org.kebs.app.kotlin.apollo.store.model.di.*
import org.kebs.app.kotlin.apollo.store.model.qa.QaSampleSubmissionEntity
import org.kebs.app.kotlin.apollo.store.repo.*
import org.kebs.app.kotlin.apollo.store.repo.auction.IAuctionRequestsRepository
import org.kebs.app.kotlin.apollo.store.repo.di.*
import org.kebs.app.kotlin.apollo.store.repo.qa.IQaSampleSubmissionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import java.math.BigDecimal
import java.math.RoundingMode
import java.sql.Date
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


// Documents issued after inspections
enum class InspectionIssuedDocuments(val code: String) {
    COC("COC"), NCR("NCR"), COI("COI"), COR("COR"), OTHER("OTHER")
}

enum class PaymentPurpose(val code: String) {
    AUDIT("AUDIT"), CONSIGNMENT("CD"), OTHER("OTHER")
}

// Local document type setting codes (varField1)
enum class CdTypeCodes(val code: String) {
    COC("LOCAL_COC_COI"),
    COR("LOCAL_COR"),
    FOREIGN_COC("FOREIGN_COC_COI"),
    FOREIGN_COR("FOREIGN_COR"),
    TEMPORARY_IMPORTS("TEMPORARY_IMPORT_GOODS"),
    TEMPORARY_IMPORT_VEHICLES("TEMPORARY_IMPORT_VEHICLES"),
    COURIER_GOODS("COURIER_GOODS"),
    AUCTION_GOODS("AUCTION_GOODS"),
    AUCTION_VEHICLE("AUCTION_VEHICLE"),
    PVOC_GOODS("NO_COC_PVOC"),
    PVOC_VEHICLES("NO_COR_PVOC"),
    EXEMPTED_GOODS("EXEMPTED_COC"),
    EXEMPTED_VEHICLES("EXEMPTED_COR"),
    NCR("NCR"),
    OTHER("OTHER")
}


@Service
class DestinationInspectionDaoServices(
        private val applicationMapProperties: ApplicationMapProperties,
        private val commonDaoServices: CommonDaoServices,
        private val SampleSubmissionRepo: IQaSampleSubmissionRepository,
        private val serviceRequestsRepository: IServiceRequestsRepository,
        private val invoiceDaoService: InvoiceDaoService,
        private val feeRangesRepository: InspectionFeeRangesRepository,
        private val notifications: Notifications,
        private val iCocItemRepository: ICocItemRepository,
        private val iUserProfilesRepo: IUserProfilesRepository,
        private val idfsRepo: IIDFDetailsEntityRepository,
        private val manifestRepo: IManifestDetailsEntityRepository,
        private val declarationRepo: IDeclarationDetailsEntityRepository,
        private val iLocalCocTypeRepo: ILocalCocTypesRepository,
        private val iCdContainerRepository: ICdContainerRepository,
        private val cocRepo: ICocsRepository,
        private val usersCfsRepo: IUsersCfsAssignmentsRepository,
        private val iCdInspectionChecklistRepo: ICdInspectionChecklistRepository,
        private val cdTypesRepo: IConsignmentDocumentTypesEntityRepository,
        private val iCdImporterRepo: ICdImporterEntityRepository,
        private val iDemandNoteRepo: IDemandNoteRepository,
        private val iDemandNoteItemRepo: IDemandNoteItemsDetailsRepository,
        private val iCdTransportRepo: ICdTransportEntityRepository,
        private val iCdConsignorRepo: ICdConsignorEntityRepository,
        private val iConsignmentDocumentDetailsRepo: IConsignmentDocumentDetailsRepository,
        private val iBlackListUserTargetRep: IBlackListUserTargetRepository,
        private val iCdStatusTypesDetailsRepo: ICdStatusTypesEntityRepository,
        private val iDIFeeDetailsRepo: IDestinationInspectionFeeRepository,
        private val iDIUploadsRepo: IDiUploadsRepository,
        private val iCdStandardsRepo: ICdStandardsEntityRepository,
        private val iCdStandardsTwoRepo: ICdStandardsTwoEntityRepository,
        private val iSampleCollectRepo: ISampleCollectRepository,
        private val iSampleSubmitRepo: ISampleSubmitRepository,
        private val iSampleSubmissionParamRepo: ICdSampleSubmissionParametersRepository,
        private val iChecklistInspectionTypesRepo: IChecklistInspectionTypesRepository,
        private val iUserRepository: IUserRepository,
        private val iCdItemsRepo: IConsignmentItemsRepository,
        private val iCdItemNonStandardEntityRepository: ICdItemNonStandardEntityRepository,
        private val iCdValuesHeaderLevelRepo: ICdValuesHeaderLevelEntityRepository,
        private val iCdConsigneeRepo: ICdConsigneeEntityRepository,
        private val iCdExporterRepo: ICdExporterEntityRepository,
        private val corsBakRepository: ICorsBakRepository,
        private val idfItemRepo: IIDFItemDetailsEntityRepository,
        private val declarationItemRepo: IDeclarationItemDetailsEntityRepository,
        private val iCdTransactionRepo: ICdTransactionsRepository,
        private val iCountryTypeCodesRepo: ICountryTypeCodesRepository,

        private val iCfsTypeCodesRepository: ICfsTypeCodesRepository,
        private val iCdCfsUserCfsRepository: ICdCfsUserCfsRepository,

        private val iPortsTypeCodesRepository: IPortsTypeCodesRepository,
        private val iCdPortsUserPortsRepository: ICdPortsUserPortsRepository,
        private val iCdInspectionGeneralRepo: ICdInspectionGeneralRepository,
        private val cdMotorVehicleInspectionChecklistRepo: ICdInspectionMotorVehicleChecklistRepository,
        private val iCdInspectionAgrochemItemChecklistRepo: ICdInspectionAgrochemItemChecklistRepository,
        private val auctionRequestRepository: IAuctionRequestsRepository,
        // Demand notes
        private val currencyExchangeRateRepository: ICfgCurrencyExchangeRateRepository,
        //Inspection Checklist Repos
        private val iCdInspectionEngineeringItemChecklistRepo: ICdInspectionEngineeringItemChecklistRepository,
        private val iCdInspectionOtherItemChecklistRepo: ICdInspectionOtherItemChecklistRepository,
        private val iCdInspectionMotorVehicleItemChecklistRepo: ICdInspectionMotorVehicleItemChecklistRepository,
        private val iPvocPartnersCountriesRepository: IPvocPartnersCountriesRepository,

        ) {
    final val DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy")

    @Autowired
    lateinit var sftpService: SftpServiceImpl

    @Value("\${destination.inspection.checklist.name}")
    lateinit var checkListName: String

    @Value("\${destination.inspection.sample.collection.name}")
    lateinit var sampCollectName: String

    @Value("\${destination.inspection.sample.submission.name}")
    lateinit var sampSubmitName: String

    @Value("\${destination.inspection.sample.Submit.Param.Name}")
    lateinit var sampSubmitParamName: String

    @Value("\${destination.inspection.sample.Submit.Add.Param.Details}")
    lateinit var sampSubmitAddParamDetails: String

    @Value("\${destination.inspection.motor-vehicle.details.name}")
    lateinit var motorVehicleInspectionDetailsName: String

    @Value("\${destination.inspection.motor-vehicle.ministry-checklist.name}")
    lateinit var motorVehicleMinistryInspectionChecklistName: String

    @Value("\${destination.inspection.sample.bsNumber.name}")
    lateinit var bsNumber: String

    @Value("\${destination.inspection.sample.labResults.name}")
    lateinit var labResults: String

    @Value("\${destination.inspection.sample.labResults.all.complete.name}")
    lateinit var labResultsAllComplete: String

    @Value("\${destination.inspection.sample.view.name}")
    lateinit var viewPage: String

    @Value("\${destination.inspection.awaiting.payment.status}")
    lateinit var awaitPaymentStatus: String

    @Value("\${destination.inspection.payment.made.status}")
    lateinit var paymentMadeStatus: String

    @Value("\${destination.inspection.rejected.status}")
    lateinit var rejectedStatus: String

    @Value("\${destination.inspection.approved.status}")
    lateinit var approvedStatus: String

    @Value("\${inspection.checklist.type.agrochem.items}")
    lateinit var agrochemItemChecklistType: String

    @Value("\${inspection.checklist.type.engineering.items}")
    lateinit var engineeringItemChecklistType: String

    @Value("\${inspection.checklist.type.other.items}")
    lateinit var otherItemChecklistType: String

    @Value("\${inspection.checklist.type.motor.vehicle.items}")
    lateinit var motorVehicleItemChecklistType: String

    @Value("\${destination.inspection.notification.di.cd.assigned.uuid}")
    lateinit var diCdAssignedUuid: String

    @Value("\${destination.inspection.cd.type.goods.category}")
    lateinit var cdTypeGoodsCategory: String

    @Value("\${destination.inspection.cd.type.vehicles.category}")
    lateinit var cdTypeVehiclesCategory: String

    @Value("\${destination.inspection.cd.status.type.approve.category}")
    lateinit var cdStatusTypeApproveCategory: String

    @Value("\${destination.inspection.cd.status.type.reject.category}")
    lateinit var cdStatusTypeRejectCategory: String

    @Value("\${destination.inspection.cd.status.type.onhold.category}")
    lateinit var cdStatusTypeOnHoldCategory: String

    @Value("\${destination.inspection.cd.status.type.query.category}")
    lateinit var cdStatusTypeQueryCategory: String

    private val createdByValue = "SYSTEM"

    private val cdItemSampleSubmittedViewPageDetails = "redirect:/api/di/inspection/sample-submission?cdItemUuid"

    private val cdItemViewPageDetails = "redirect:/api/di/cd-item-details?cdItemUuid"

    private val cdViewPageDetails = "redirect:/api/di/cd-details?cdUuid"

    fun viewSampleSubmitPage(itemDetails: CdItemDetailsEntity, message: String): String =
            "$cdItemSampleSubmittedViewPageDetails=${itemDetails.uuid}&docType=${message}"

    fun viewCdItemPage(cdItemUuid: String) = "$cdItemViewPageDetails=$cdItemUuid"

    fun viewCdItemSSFPage(cdItemUuid: String) = "$cdItemViewPageDetails=$cdItemUuid"

    fun viewCdPageDetails(cdUuid: String) = "$cdViewPageDetails=$cdUuid"


    fun handleNoCorFromCosWithPvoc(cdDetailsEntity: ConsignmentDocumentDetailsEntity): Boolean {
        //Check if Cos (Country Of Supply) is available
        cdDetailsEntity.cdHeaderOne?.let {
            findCdHeaderOneDetails(it).countryOfSupply?.let { cos ->
                iPvocPartnersCountriesRepository.findByAbbreviationIgnoreCase(cos)
            }?.let {
                cdDetailsEntity.csApprovalStatus = commonDaoServices.inActiveStatus.toInt()
                return true
            }
        }
        return false
    }

    fun linkManifestWithConsignment(manifestNumber: String?, ucrNumber: String?, manifestDocument: Boolean) {
        if (manifestDocument) {
            this.findCdWithUcrNumberLatest(ucrNumber
                    ?: "")?.let { consignmentDocumentDetailsEntity ->
                consignmentDocumentDetailsEntity.manifestNumber = manifestNumber
                this.iConsignmentDocumentDetailsRepo.save(consignmentDocumentDetailsEntity)
            }
        } else {
            this.findCdWithUcrNumberLatest(ucrNumber ?: "")?.let { consignmentDocumentDetailsEntity ->
                this.declarationRepo.findFirstByRefNum(ucrNumber ?: "")?.let { document ->
                    consignmentDocumentDetailsEntity.manifestNumber = document.manifestNumber
                    this.iConsignmentDocumentDetailsRepo.save(consignmentDocumentDetailsEntity)
                    consignmentDocumentDetailsEntity
                } ?: run {
                    consignmentDocumentDetailsEntity.manifestNumber = "NONE"
                    this.iConsignmentDocumentDetailsRepo.save(consignmentDocumentDetailsEntity)
                    consignmentDocumentDetailsEntity
                }
            }
        }
    }

    fun findAllBlackListUsers(status: Int): List<CdBlackListUserTargetTypesEntity> {

        iBlackListUserTargetRep.findAllByStatusOrderByTypeName(status)
                ?.let {
                    return it
                }
                ?: throw Exception("List Of BlackList Users With Status = ${status}, does not Exist")
    }

    fun findAllOlderVersionCDsWithSameUcrNumber(
            ucrNumber: String,
            map: ServiceMapsEntity
    ): List<ConsignmentDocumentDetailsEntity>? {
        return iConsignmentDocumentDetailsRepo.findByUcrNumberAndOldCdStatus(ucrNumber, map.activeStatus)
//            ?.let {
//                return it
//            }
//            ?: throw Exception("List Of ALl , does not Exist")
    }

    fun findLocalCocTypeWithCocTypeCode(cocTypeCode: String): LocalCocTypesEntity {
        iLocalCocTypeRepo.findByCocTypeCode(cocTypeCode)
                ?.let {
                    return it
                }
                ?: throw Exception("Local COC TYPE with type code = ${cocTypeCode}, does not Exist")
    }

    fun findCocByCocNumber(cocNumber: String): CocsEntity? {
        return this.cocRepo.findFirstByCocNumberAndCocNumberIsNotNullOrCoiNumberAndCoiNumberIsNotNull(cocNumber, cocNumber).orElse(null)
    }

    @Transactional
    fun createLocalCoc(
            user: UsersEntity,
            consignmentDocumentDetailsEntity: ConsignmentDocumentDetailsEntity,
            map: ServiceMapsEntity,
            remarks: String,
            routValue: String
    ): CocsEntity {
        var localCoc = CocsEntity()
        consignmentDocumentDetailsEntity.ucrNumber?.let {
            cocRepo.findByUcrNumberAndCocType(it, "COC")
                    ?.let { coc ->
                        return coc
                    }
        }
                ?: kotlin.run {
                    KotlinLogging.logger { }.debug("Starting background task")
                    try {
                        localCoc.cocType = "COC"
                        synchronized(this) {
                            localCoc.cocNumber = this.fillCocDetails(localCoc, consignmentDocumentDetailsEntity, user, routValue)
                            // Add more details
                            with(localCoc) {
                                coiNumber = "UNKNOWN"
                                idfNumber = consignmentDocumentDetailsEntity.ucrNumber?.let { findIdf(it)?.baseDocRefNo }
                                        ?: "UNKOWN"
                                clean = "Y"
                                version = consignmentDocumentDetailsEntity.version ?: 1
                                consignmentDocId = consignmentDocumentDetailsEntity
                                cocType = "COC"
                                documentsType = "L"
                                productCategory = "UNKNOWN"
                                createdBy = commonDaoServices.concatenateName(user)
                                createdOn = commonDaoServices.getTimestamp()
                            }
                            localCoc = cocRepo.save(localCoc)
                        }
                        KotlinLogging.logger { }.info { "localCoc = ${localCoc.id}" }
                        localCocCoiItems(consignmentDocumentDetailsEntity, localCoc, user, map)
                    } catch (e: Exception) {
                        KotlinLogging.logger { }.debug("Threw error from forward express callback", e)
                        throw e
                    }
                    return localCoc
                }

    }

    fun fillCocDetails(localCoc: CocsEntity, consignmentDocumentDetailsEntity: ConsignmentDocumentDetailsEntity, user: UsersEntity, routValue: String): String {
        with(localCoc) {
            ucrNumber = consignmentDocumentDetailsEntity.ucrNumber
            rfcDate = commonDaoServices.getTimestamp()
            cocIssueDate = commonDaoServices.getTimestamp()
            cocRemarks = consignmentDocumentDetailsEntity.localCocOrCorRemarks ?: "NA"
            coiRemarks = consignmentDocumentDetailsEntity.localCoiRemarks ?: "NA"
            issuingOffice = "${consignmentDocumentDetailsEntity.assignedInspectionOfficer?.firstName} ${consignmentDocumentDetailsEntity.assignedInspectionOfficer?.lastName}"

            val cdImporter = consignmentDocumentDetailsEntity.cdImporter?.let { findCDImporterDetails(it) }
            importerName = cdImporter?.name
            importerPin = cdImporter?.pin.orEmpty()
            importerAddress1 = cdImporter?.physicalAddress
            importerAddress2 = cdImporter?.postalAddress ?: "UNKNOWN"
            importerCity = cdImporter?.physicalAddress ?: "UNKNOWN"
            importerCountry = cdImporter?.physicalCountryName ?: "UNKNOWN"
            importerZipCode = "UNKNOWN"
            importerTelephoneNumber = cdImporter?.telephone ?: "UNKNOWN"
            importerFaxNumber = cdImporter?.fax ?: "UNKNOWN"
            importerEmail = cdImporter?.email ?: "UNKNOWN"

            val cdExporter = consignmentDocumentDetailsEntity.cdExporter?.let { findCdExporterDetails(it) }
            exporterName = cdExporter?.name
            exporterPin = cdExporter?.pin
            exporterAddress1 = cdExporter?.physicalAddress
            exporterAddress2 = cdExporter?.postalAddress ?: "UNKNOWN"
            exporterCity = cdExporter?.warehouseLocation ?: "UNKNOWN"
            exporterCountry = cdExporter?.physicalCountryName
            exporterZipCode = "UNKNOWN"
            exporterTelephoneNumber = cdExporter?.telephone ?: "UNKNOWN"
            exporterFaxNumber = cdExporter?.fax ?: "UNKNOWN"
            exporterEmail = cdExporter?.email ?: "UNKNOWN"

            placeOfInspection = consignmentDocumentDetailsEntity.freightStation?.cfsName ?: "UNKNOWN"
            dateOfInspection = commonDaoServices.getTimestamp()

            val cdTransport = consignmentDocumentDetailsEntity.cdTransport?.let { findCdTransportDetails(it) }
            portOfDestination = cdTransport?.portOfArrival
            shipmentMode = cdTransport?.modeOfTransport ?: "UNKNOWN"
            finalInvoiceCurrency = "KES"
            finalInvoiceDate = commonDaoServices.getTimestamp()
            val headerOne = consignmentDocumentDetailsEntity.cdHeaderOne?.let { findCdHeaderOneDetails(it) }
            countryOfSupply = headerOne?.countryOfSupplyName ?: "UNKNOWN"
            shipmentQuantityDelivered = "UNKNOWN"
            shipmentSealNumbers = "UNKNOWN"
            shipmentContainerNumber = "UNKNOWN"
            shipmentGrossWeight = "UNKNOWN"
            route = routValue
            status = 1
            version = consignmentDocumentDetailsEntity.version ?: 1
            consignmentDocId = consignmentDocumentDetailsEntity
            productCategory = "UNKNOWN"
            createdBy = commonDaoServices.concatenateName(user)
            createdOn = commonDaoServices.getTimestamp()
        }
        // Add invoice details
        consignmentDocumentDetailsEntity.id?.let { cdId ->
            this.invoiceDaoService.findDemandNoteCdId(cdId)?.let { itemNote ->
                localCoc.finalInvoiceCurrency = "KES"
                localCoc.finalInvoiceExchangeRate = itemNote.rate?.toDouble() ?: 0.0
                localCoc.finalInvoiceNumber = itemNote.receiptNo
                localCoc.finalInvoiceDate = itemNote.createdOn
                localCoc.finalInvoiceFobValue = itemNote.cfvalue?.toDouble() ?: 0.0
            }
        }
        // Add inspection details
        iCdInspectionGeneralRepo.findFirstByCdDetailsAndCurrentChecklist(consignmentDocumentDetailsEntity, 1)?.let { cdItemDetailsList ->
            localCoc.customsEntryNumber = cdItemDetailsList.customsEntryNumber
            localCoc.clearingAgent = cdItemDetailsList.clearingAgent
            localCoc
        } ?: kotlin.run {
            localCoc.customsEntryNumber = "UNKNOWN"
            localCoc.clearingAgent = "UNKNOWN"
        }
        val generateDate = commonDaoServices.getTimestamp()
        localCoc.cocIssueDate = generateDate
        localCoc.coiIssueDate = generateDate
        val countDoc = countCocs(generateDate, localCoc.cocType ?: "")
        val dataMonth = commonDaoServices.convertDateToString(generateDate.toLocalDateTime(), "yyyyMMdd")
        return "KEBS${localCoc.cocType}\\$dataMonth\\$countDoc"
    }

    fun createLocalNcr(
            user: UsersEntity,
            consignmentDocumentDetailsEntity: ConsignmentDocumentDetailsEntity,
            map: ServiceMapsEntity,
            remarks: String,
            routValue: String
    ): CocsEntity? {
        val ncrItems = listNcrItems(consignmentDocumentDetailsEntity, map)
        if (ncrItems.isEmpty()) {
            return null
        }
        var localNcr = CocsEntity()

        consignmentDocumentDetailsEntity.ucrNumber?.let {
            cocRepo.findByUcrNumberAndCocType(it, "NCR")
                    ?.let { coc ->
                        return coc
                    }
        }
                ?: kotlin.run {
                    KotlinLogging.logger { }.debug("Starting background task")
                    try {
                        synchronized(this) {
                            with(localNcr) {
                                coiNumber = "UNKNOWN"
                                idfNumber = consignmentDocumentDetailsEntity.ucrNumber?.let { findIdf(it)?.baseDocRefNo }
                                        ?: "UNKNOWN"
                                rfiNumber = "UNKNOWN"
                                cocType = "NCR"
                                clean = "N"
                                createdBy = commonDaoServices.concatenateName(user)
                                createdOn = commonDaoServices.getTimestamp()
                            }
                            // Fill details
                            localNcr.cocNumber = this.fillCocDetails(localNcr, consignmentDocumentDetailsEntity, user, routValue)
                            // Save NCR
                            localNcr = cocRepo.save(localNcr)
                        }
                        KotlinLogging.logger { }.info { "localNcr = ${localNcr.id}" }
                        for (item in ncrItems) {
                            generateLocalCocItem(item, localNcr, user, map, item.ownerPin
                                    ?: "NA", item.ownerName ?: "NA")
                        }
                    } catch (e: Exception) {
                        KotlinLogging.logger { }.debug("Threw error from forward express callback")
                        KotlinLogging.logger { }.debug(e.message)
                        KotlinLogging.logger { }.debug(e.toString())
                    }
                    return localNcr
                }

    }

    @Transactional
    fun createLocalCoi(
            user: UsersEntity,
            consignmentDocumentDetailsEntity: ConsignmentDocumentDetailsEntity,
            map: ServiceMapsEntity,
            remarks: String,
            routValue: String
    ): CocsEntity {
        var localCoi = CocsEntity()
        consignmentDocumentDetailsEntity.ucrNumber?.let {
            cocRepo.findByUcrNumberAndCocType(it, "COI")
                    ?.let { coc ->
                        throw Exception("There is an Existing COI with the following UCR No = ${coc.ucrNumber}")
                    }
        }
                ?: kotlin.run {
                    synchronized(this) {
                        localCoi.cocType = "COI"
                        localCoi.coiNumber = this.fillCocDetails(localCoi, consignmentDocumentDetailsEntity, user, routValue)
                        // Add COI details

                        with(localCoi) {
                            cocNumber = "UNKNOWN"
                            idfNumber = consignmentDocumentDetailsEntity.ucrNumber?.let { findIdf(it)?.baseDocRefNo }
                            productCategory = "UNKNOWN"
                            clean = "Y"
                            cocType = "COI"
                            documentsType = "L"
                            createdBy = commonDaoServices.concatenateName(user)
                            createdOn = commonDaoServices.getTimestamp()
                        }
                        localCoi = cocRepo.save(localCoi)
                    }
                    localCocCoiItems(consignmentDocumentDetailsEntity, localCoi, user, map)
                    return localCoi
                }
    }


    fun generateLocalCocItem(
            cdItemDetails: CdItemDetailsEntity,
            localCocEntity: CocsEntity,
            user: UsersEntity,
            map: ServiceMapsEntity,
            ownerPinValues: String,
            ownerNameValues: String
    ): CocItemsEntity {
        var localCocItems = CocItemsEntity()
        with(localCocItems) {
            cocId = localCocEntity.id
            itemId = cdItemDetails.id
            shipmentLineNumber = cdItemDetails.itemNo!!
            shipmentLineHscode = cdItemDetails.itemHsCode
            shipmentLineQuantity = cdItemDetails.quantity ?: BigDecimal.ZERO
            shipmentLineUnitofMeasure = cdItemDetails.unitOfQuantity
            shipmentLineDescription = cdItemDetails.itemDescription ?: cdItemDetails.hsDescription ?: "UNKNOWN"
            shipmentLineVin = "UNKNOWN"
            shipmentLineStickerNumber = "UNKNOWN"
            shipmentLineIcs = "UNKNOWN"
            shipmentLineStandardsReference = "UNKNOWN"
            shipmentLineLicenceReference = "UNKNOWN"
            shipmentLineRegistration = "UNKNOWN"
            shipmentLineBrandName = cdItemDetails.productBrandName ?: "UNKNOWN"
            ownerPin = ownerPinValues
            ownerName = ownerNameValues
            status = map.activeStatus
            createdBy = commonDaoServices.getUserName(user)
            createdOn = commonDaoServices.getTimestamp()
        }
        localCocItems = iCocItemRepository.save(localCocItems)
        KotlinLogging.logger { }.info { "Generated Local coc item WITH id = ${localCocItems.id}" }
        return localCocItems
    }

    fun listNcrItems(
            updatedCDDetails: ConsignmentDocumentDetailsEntity,
            map: ServiceMapsEntity
    ): List<CdItemDetailsEntity> {
        val results = mutableListOf<CdItemDetailsEntity>()
        if (updatedCDDetails.inspectionChecklist == map.activeStatus) { // If checklist was filled, only add compliant items only
            findCDItemsListWithCDID(updatedCDDetails)
                    .forEach { cdItemDetails ->
                        if (!(cdItemDetails.approveStatus == map.activeStatus || cdItemDetails.approveStatus == null || cdItemDetails.approveStatus == map.inactiveStatus)) {
                            results.add(cdItemDetails)
                        }
                    }
        }
        return results
    }

    fun localCocCoiItems(
            updatedCDDetails: ConsignmentDocumentDetailsEntity,
            localCocEntity: CocsEntity,
            user: UsersEntity,
            map: ServiceMapsEntity
    ) {
        if (updatedCDDetails.inspectionChecklist == map.activeStatus) { // If checklist was filled, only add compliant items only
            val cocItems = findCDItemsListWithCDID(updatedCDDetails)
            val itemIds = mutableListOf<Long?>()
            cocItems.forEach { cdItemDetails ->
                if (cdItemDetails.approveStatus == map.activeStatus) {
                    //1. Approved CD Item
                    itemIds.add(cdItemDetails.id)
                    generateLocalCocItem(cdItemDetails, localCocEntity, user, map, cdItemDetails.ownerPin
                            ?: "NA", cdItemDetails.ownerName ?: "NA")
                } else if (cdItemDetails.checklistStatus != map.activeStatus) {
                    //2.  Checklist was not filled(Pre-Approved)
                    itemIds.add(cdItemDetails.id)
                    generateLocalCocItem(cdItemDetails, localCocEntity, user, map, cdItemDetails.ownerPin
                            ?: "NA", cdItemDetails.ownerName ?: "NA")
                }
            }
            // Prevent empty COC/COI generation
            if (itemIds.isEmpty()) {
                throw ExpectedDataNotFound("Compliance Certificate can't be issues when there is zero compliant items")
            }
        } else {
            val cocItems = findCDItemsListWithCDID(updatedCDDetails)
            cocItems.forEach { cdItemDetails ->
                generateLocalCocItem(cdItemDetails, localCocEntity, user, map, cdItemDetails.ownerPin
                        ?: "NA", cdItemDetails.ownerName ?: "NA")
            }
        }

    }


    fun sendLocalCoc(cocsEntity: CocsEntity) {
        val coc: CustomCocXmlDto = cocsEntity.toCocXmlRecordRefl()
        //COC ITEM
        val listItems = mutableListOf<CocDetails>()
        iCocItemRepository.findByCocId(cocsEntity.id)?.forEach { cocItem ->
            cocItem.toCocItemDetailsXmlRecordRefl(cocsEntity.cocNumber ?: "NA").let { cocItemXml ->
                listItems.add(cocItemXml)
            }
        }
        if (!listItems.isEmpty()) {
            coc.cocDetals = listItems
        }
        coc.version = (cocsEntity.version ?: 1).toString()
        val cocFinalDto = COCXmlDTO()
        cocFinalDto.coc = coc
        // Create file name from UCR number
        val fileName = when (cocsEntity.cocNumber) {
            null -> {
                cocsEntity.consignmentDocId?.let {
                    commonDaoServices.createKesWsFileName(
                            applicationMapProperties.mapKeswsCocDoctype,
                            it.ucrNumber ?: "", coc.version ?: "1"
                    )
                } ?: throw ExpectedDataNotFound("Invalid Local UCR NUmber")
            }
            else -> {
                commonDaoServices.createKesWsFileName(
                        applicationMapProperties.mapKeswsCocDoctype,
                        cocsEntity.ucrNumber ?: "",
                        coc.version ?: "1"
                )
            }
        }
        val xmlFile = commonDaoServices.serializeToXml(fileName, cocFinalDto)
        sftpService.uploadFile(xmlFile, "COC")
    }

    fun sendLocalCoi(coiEntity: CocsEntity) {
        val coi: CustomCoiXmlDto = coiEntity.toCoiXmlRecordRefl()
        //COC ITEM
        val coiFinalDto = COIXmlDTO()
        val itemList = mutableListOf<CoiDetails>()
        iCocItemRepository.findByCocId(coiEntity.id)?.forEach { coiItem ->
            itemList.add(coiItem.toCoiItemDetailsXmlRecordRefl(coiEntity.coiNumber ?: ""))
        }
        coi.coiItems = itemList
        coi.version = (coiEntity.version ?: 1).toString()
        coiFinalDto.coi = coi
        val fileName = coiEntity.ucrNumber?.let {
            commonDaoServices.createKesWsFileName(
                    applicationMapProperties.mapKeswsCoiDoctype,
                    it, coiEntity.version?.toString() ?: "1"
            )
        } ?: throw ExpectedDataNotFound("Consignment document UCR number was not found")

        val xmlFile = commonDaoServices.serializeToXml(fileName, coiFinalDto)

        sftpService.uploadFile(xmlFile, "COI")
    }

    fun countCor(date: Timestamp): Long {
        val yearG = commonDaoServices.convertDateToString(date.toLocalDateTime(), "yyyy").toLongOrDefault(1990)
        return corsBakRepository.countAllByYearGenerate(yearG)
    }

    fun countCocs(date: Timestamp, documentType: String): Long {
        val yearG = commonDaoServices.convertDateToString(date.toLocalDateTime(), "yyyy").toLongOrDefault(1990)
        return cocRepo.countAllByYearGenerate(yearG, documentType)
    }

    fun generateCor(
            cdEntity: ConsignmentDocumentDetailsEntity,
            s: ServiceMapsEntity,
            user: UsersEntity,
    ): CorsBakEntity {
        var localCor = CorsBakEntity()
        //Get CD Item by cd doc id
        var mvirFound = false
        var ministryReportRef: String? = "NEW"
        var ministryName: String? = "NAIROBI"
        var ministryReporDate: Timestamp? = null
        iCdInspectionGeneralRepo.findFirstByCdDetailsAndCurrentChecklist(cdEntity, 1)?.let { cdItemDetailsList ->
            this.cdMotorVehicleInspectionChecklistRepo.findByInspectionGeneral(cdItemDetailsList)?.let { mvInspectionEntity ->
                this.iCdInspectionMotorVehicleItemChecklistRepo.findFirstByInspection(mvInspectionEntity)?.let { cdMvInspectionEntity ->
                    ministryReportRef = cdMvInspectionEntity.ministryReportReference
                    ministryReporDate = cdMvInspectionEntity.ministryReportDate
                    ministryName = cdMvInspectionEntity.ministryStationId?.stationName
                    // Add item details
                    cdMvInspectionEntity.itemId?.let { item ->
                        localCor.varField1 = item.id?.toString()
                        findCdItemNonStandardByItemID(item)?.let { nonStandard ->
                            localCor.chasisNumber = nonStandard.chassisNo.toString()
                            localCor.model = nonStandard.vehicleModel ?: "UNKNOWN"
                            localCor.make = nonStandard.vehicleMake ?: "UNKNOWN"
                            localCor.yearOfFirstRegistration = nonStandard.vehicleYear ?: "UNKNOWN"
                            localCor.fuelType = "UNKNOWN"
                            localCor.bodyColor = "UNKNOWN"
                        } ?: throw ExpectedDataNotFound("No Item Non standard details found, chassis number not found")
                    } ?: throw ExpectedDataNotFound("No Item attached to this MVIR")
                    mvirFound = true
                    with(localCor) {
                        inspectionCenter = cdItemDetailsList.cdDetails?.freightStation?.cfsName ?: "UNKNOWN"
                        engineNumber = "UNKNOWN"
                        typeOfBody = "UNKNOWN"
                        engineCapacity = cdMvInspectionEntity.engineNoCapacity
                        yearOfManufacture = cdMvInspectionEntity.manufactureDate.toString()
                        inspectionMileage = cdMvInspectionEntity.odemetreReading ?: "UNKNOWN"
                        inspectionRemarks = cdMvInspectionEntity.remarks
                        previousCountryOfRegistration = "UNKNOWN"
                        tareWeight = (cdMvInspectionEntity.itemId?.itemNetWeight?.toBigDecimal()
                                ?: BigDecimal.ZERO).toDouble()
                        grossWeight = (cdMvInspectionEntity.itemId?.itemGrossWeight?.toBigDecimal()
                                ?: BigDecimal.ZERO).toDouble()
                        typeOfVehicle = cdMvInspectionEntity.makeVehicle
                        bodyColor = cdMvInspectionEntity.colour
                        customsIeNo = cdItemDetailsList.customsEntryNumber
                        transmission = cdMvInspectionEntity.transmissionAutoManual
                        version = cdItemDetailsList.cdDetails?.version ?: 1
                        inspectionOfficer = cdItemDetailsList.cdDetails?.assignedInspectionOfficer?.let {
                            "${it.firstName} ${it.lastName}"
                        } ?: ""
                        inspectionRemarks = mvInspectionEntity.remarks ?: "No Remarks"
                    }
                } ?: throw ExpectedDataNotFound("ITEM MVR INSPECTION CHECKLIST")
            }
        }
        // Approval without inspection checklist
        if (!mvirFound) {
            this.findVehicleFromCd(cdEntity)?.let { vehicle ->
                this.findCdItemNonStandardByItemID(vehicle)?.let { nonStandardEntity ->
                    localCor.make = nonStandardEntity.vehicleMake ?: "UNKNOWN"
                    localCor.model = nonStandardEntity.vehicleModel ?: "UNKNOWN"
                    localCor.typeOfVehicle = nonStandardEntity.vehicleMake ?: "UNKNOWN"
                    localCor.yearOfManufacture = nonStandardEntity.vehicleYear ?: "UNKNOWN"
                    localCor.chasisNumber = nonStandardEntity.chassisNo ?: "UNKNOWN"
                    localCor
                } ?: throw ExpectedDataNotFound("No Item Non standard details found, chassis number not found")
                with(localCor) {
                    inspectionCenter = cdEntity.freightStation?.cfsName ?: "UNKNOWN"
                    inspectionMileage = "UNKNOWN"
                    inspectionRemarks = "NONE"
                    engineCapacity = "-1"
                    customsIeNo = "UNKNOWN"
                    transmission = "UNKNOWN"
                    bodyColor = "UNKNOWN"
                    typeOfBody = "UNKNOWN"
                    fuelType = "UNKNOWN"
                    engineNumber = "UNKNOWN"
                    previousCountryOfRegistration = "UNKNOWN"
                    yearOfFirstRegistration = "UNKNOWN"
                    yearOfManufacture = "UNKNOWN"
                    tareWeight = BigDecimal.ZERO.toDouble()
                    grossWeight = BigDecimal.ZERO.toDouble()
                    version = vehicle.cdDocId?.version ?: 1
                    inspectionOfficer = "${user.firstName} ${user.lastName}"
                    inspectionRemarks = "NA"
                }
            } ?: throw ExpectedDataNotFound("No vehicle found in this consignment, chassis number not found")
        }
        val ministryReporDateStr = commonDaoServices.convertDateToString(ministryReporDate?.toLocalDateTime()
                ?: LocalDateTime.now(), "dd-MMMM-YYY")
        // TODO: For new vehicles or vehicles withought ministry inspection
        val overallRemarks = "The Motor Vehicle was presented for inspection at MINISTRY OF TRANSPORT INSPECTION CENTER ($ministryName) and an inspection report No. $ministryReportRef dated $ministryReporDateStr was issued by CHIEF MECHANICAL ENGINEER.\n\n" +
                "The above Motor Vehicle is therefore found to comply with the requirements of KS 1515:2000."
        // Fill checklist details
        val issueDate = commonDaoServices.getTimestamp()
        synchronized(this) {
            val corCount = countCor(issueDate)
            val corDateMonth = commonDaoServices.convertDateToString(issueDate.toLocalDateTime(), "yyyyMMdd")
            with(localCor) {
                corNumber = "KEBSCOR\\$corDateMonth\\$corCount"
                corIssueDate = issueDate
                val cdHeaderOne = cdEntity.cdHeaderOne?.let { findCdHeaderOneDetails(it) }
                countryOfSupply = cdHeaderOne?.countryOfSupply
                val cdExporter = cdEntity.cdExporter?.let { findCdExporterDetails(it) }
                exporterName = cdExporter?.name
                exporterAddress1 = cdExporter?.physicalAddress
                exporterAddress2 = cdExporter?.postalAddress
                exporterEmail = cdExporter?.email
                applicationBookingDate = Timestamp.valueOf(cdEntity.inspectionDate?.toLocalDate()?.atStartOfDay()
                        ?: LocalDateTime.now())
                inspectionDate = commonDaoServices.getTimestamp()
                inspectionFee = 0.0
                unitsOfMileage = "KM"
                inspectionStatement = overallRemarks
                previousRegistrationNumber = "UNKNOWN"
                approvalStatus = cdEntity.compliantStatus.toString()
                ucrNumber = cdEntity.ucrNumber
                inspectionFeeCurrency = "KES"
                inspectionFeeExchangeRate = 0.0
                inspectionFeePaymentDate = commonDaoServices.getTimestamp()
                consignmentDocId = cdEntity
                status = s.activeStatus
                createdBy = commonDaoServices.getUserName(user)
                createdOn = commonDaoServices.getTimestamp()
            }
            // Add invoice details
            cdEntity.id?.let { cdId ->
                this.invoiceDaoService.findDemandNoteCdId(cdId)?.let { itemNote ->
                    localCor.inspectionFeeCurrency = "KES"
                    localCor.inspectionFee = itemNote.totalAmount?.toDouble() ?: 0.0
                    localCor.inspectionFeePaymentDate = itemNote.createdOn
                    localCor.inspectionFeeExchangeRate = itemNote.rate?.toDouble() ?: 0.0
                }
            }
            // Save local COR
            KotlinLogging.logger { }.info("COR: $localCor")
            localCor = corsBakRepository.save(localCor)
        }
        KotlinLogging.logger { }.info { "Generated Local CoR WITH id = ${localCor.id}" }
        return localCor
    }

    fun saveCorDetails(corsBakEntity: CorsBakEntity): CorsBakEntity {
        return corsBakRepository.save(corsBakEntity)
    }

    fun mapDataInJsonOrNull(dataFetched: JSONObject?, valueToFind: String): String? {
        return dataFetched?.let {
            var myReturnValue: String? = null
            if (it.has(valueToFind)) {
                it.get(valueToFind)
                        ?.let { myValue ->
                            myReturnValue = myValue.toString()
                        }
            }
            return myReturnValue
        }
    }

    fun findCD(cdId: Long): ConsignmentDocumentDetailsEntity {

        iConsignmentDocumentDetailsRepo.findByIdOrNull(cdId)
                ?.let { cdDetailsEntity ->
                    return cdDetailsEntity
                }
                ?: throw Exception("CD Details with the following ID = ${cdId}, does not Exist")
    }

    fun findCDWithUuid(uuid: String): ConsignmentDocumentDetailsEntity {
        iConsignmentDocumentDetailsRepo.findByUuid(uuid)
                ?.let { cdDetailsEntity ->
                    return cdDetailsEntity
                }
                ?: throw Exception("CD Details with the following uuid = ${uuid}, does not Exist")
    }

    fun findCDWithUuids(uuids: Iterable<String>): List<ConsignmentDocumentDetailsEntity> {
        return iConsignmentDocumentDetailsRepo.findByUuidIn(uuids)
    }

    fun findCORById(cdId: Long): CorsBakEntity? {
        return corsBakRepository.findByIdOrNull(cdId)
    }

    fun findCORByCdId(cdId: ConsignmentDocumentDetailsEntity?): CorsBakEntity? {
        return corsBakRepository.findByConsignmentDocId(cdId)
    }

    fun findCORByCorNumber(corNumber: String): CorsBakEntity? {
        return corsBakRepository.findByCorNumber(corNumber)
    }

    fun findCORByChassisNumber(chassisNo: String): CorsBakEntity {
        corsBakRepository.findByChasisNumber(chassisNo)
                ?.let { corEntity ->
                    return corEntity
                }
                ?: throw Exception("CoR Entity with the following chassis number = ${chassisNo}, does not Exist")
    }

    fun findCdManualAssignable(status: Int): List<CdStatusTypesEntity> {
        return iCdStatusTypesDetailsRepo.findByStatusAndApplicationStatus(status, 0)
    }

    fun findCdStatusValueList(status: Int): List<CdStatusTypesEntity> {
        iCdStatusTypesDetailsRepo.findByStatus(status)
                ?.let { cdStatusDetails ->
                    return cdStatusDetails
                }
                ?: throw Exception("cd Status Details with status = ${status}, do not Exist")
    }


    fun findCdStatusValue(statusID: Long): CdStatusTypesEntity {
        iCdStatusTypesDetailsRepo.findByIdOrNull(statusID)
                ?.let { cdStatusDetails ->
                    return cdStatusDetails
                }
                ?: throw Exception("Status Details with id = ${statusID}, does not Exist")
    }

    fun findCdStatusCategory(status: String): CdStatusTypesEntity {
        iCdStatusTypesDetailsRepo.findByCategoryAndStatus(status, 1)
                ?.let { cdStatusDetails ->
                    return cdStatusDetails
                }
                ?: throw Exception("Status Details with id = ${status}, does not Exist")
    }


    fun findCocItemList(cocId: Long): List<CocItemsEntity> {
        iCocItemRepository.findByCocId(cocId)
                ?.let { cocItemDetails ->
                    return cocItemDetails
                }
                ?: throw Exception("COC ITEM(s) Details with COC ID = ${cocId}, do not Exist")
    }

    fun findDemandNoteWithCDID(cdId: Long): CdDemandNoteEntity? {
        return iDemandNoteRepo.findFirstByCdId(cdId)
    }

    fun findIdfItemList(idf: IDFDetailsEntity): List<IDFItemDetailsEntity>? {
        return idfItemRepo.findAllByIdfDetails(idf)
//                ?.let { idfDetails ->
//                    return idfDetails
//                }
//                ?: throw Exception("IDF ITEM(s) Details with IDF ID = ${idfId}, do not Exist")
    }

    fun findDeclarationItemList(declarationDetails: DeclarationDetailsEntity): List<DeclarationItemDetailsEntity>? {
        return declarationItemRepo.findAllByDeclarationDetailsId(declarationDetails)
//                ?.let { idfDetails ->
//                    return idfDetails
//                }
//                ?: throw Exception("IDF ITEM(s) Details with IDF ID = ${idfId}, do not Exist")
    }

    fun sendDemandNotGeneratedToKWIS(demandNoteEntity: CdDemandNoteEntity?, version: String) {
        demandNoteEntity?.let {
            val mpesaDetails = invoiceDaoService.findPaymentMethodtype(applicationMapProperties.mapMpesaDetails)
            val bank1Details = invoiceDaoService.findPaymentMethodtype(applicationMapProperties.mapBankOneDetails)
            val bank2Details = invoiceDaoService.findPaymentMethodtype(applicationMapProperties.mapBankTwoDetails)
            val bank3Details = invoiceDaoService.findPaymentMethodtype(applicationMapProperties.mapBankThreeDetails)

            val demandNote: CustomDemandNoteXmlDto = it.toCdDemandNoteXmlRecordRefl()
            demandNote.demandNoteNumber = it.postingReference
            demandNote.transactionNumber = it.cdRefNo
            demandNote.nameImporter = it.nameImporter
            demandNote.address = it.address
            demandNote.currency = it.currency ?: "KES"
            demandNote.telephone = it.telephone
            demandNote.amountPayable = it.totalAmount ?: BigDecimal.ZERO
            demandNote.cfvalue = it.cfvalue ?: BigDecimal.ZERO
            demandNote.id = it.id
            demandNote.receiptNo = it.receiptNo ?: "UNKNOWN"
            demandNote.entryAblNumber = it.entryAblNumber ?: "UNKNOWN"
            demandNote.totalAmount = it.totalAmount
            demandNote.entryAblNumber = it.entryAblNumber
            demandNote.transactionType = "PAYMENT"
            demandNote.dateGenerated = demandNote.convertTimestampToKeswsValidDate(it.dateGenerated
                    ?: Date(java.util.Date().time))
            demandNote.rate = it.rate
            demandNote.product = it.product
            demandNote.paymentInstruction1 = PaymenInstruction1(bank1Details)
            demandNote.paymentInstruction2 = PaymenInstruction2(bank2Details)
            demandNote.paymentInstruction3 = PaymenInstruction3(bank3Details)
            mpesaDetails.mpesaAccNo = demandNoteEntity.postingReference
            demandNote.paymentInstructionMpesa = PaymenInstructionMpesa(mpesaDetails)
            demandNote.paymentInstructionOther = PaymenInstructionOther(mpesaDetails)
            demandNote.version = demandNote.version ?: 1

            val demandNoteFinalDto = DemandNoteXmlDTO()
            demandNoteFinalDto.customDemandNote = demandNote

            val fileName = it.demandNoteNumber?.let {
                commonDaoServices.createKesWsFileName(applicationMapProperties.mapKeswsDemandNoteDoctype, it, version)
            } ?: throw ExpectedDataNotFound("Demand note number not found")
            KotlinLogging.logger { }.debug("DEMAND NOTE FILE NAME: $fileName")
            val xmlFile = commonDaoServices.serializeToXml(fileName, demandNoteFinalDto)

            sftpService.uploadFile(xmlFile, "DEMAND_NOTE")

        } ?: throw  ExpectedDataNotFound("Demand note not found on the server")
    }


    fun sendDemandNotePayedStatusToKWIS(demandNote: CdDemandNoteEntity, amount: BigDecimal?, version: String) {
        val customDemandNotePay = CustomDemandNotePayXmlDto(demandNote)
        val demandNotePay = DemandNotePayXmlDTO()
        customDemandNotePay.amountPaid = amount
        demandNotePay.customDemandNotePay = customDemandNotePay

        val fileName = customDemandNotePay.demandNoteNumber?.let {
            commonDaoServices.createKesWsFileName(applicationMapProperties.mapKeswsDemandNotePayDoctype, it, version)
        } ?: throw ExpectedDataNotFound("Demand note number not found on the demand note")

        val xmlFile = commonDaoServices.serializeToXml(fileName, demandNotePay)

        sftpService.uploadFile(xmlFile, "DEMAND_NOTE_PAYMENT")

    }

    fun demandNoteCalculation(
            demandNoteItem: CdDemandNoteItemsDetailsEntity,
            diInspectionFeeId: DestinationInspectionFeeEntity,
            currencyCode: String,
            documentType: String,
            itemId: Long?
    ) {
        val cfiValue = demandNoteItem.cfvalue ?: BigDecimal.ZERO
        var minimumUsd: BigDecimal? = null
        var maximumUsd: BigDecimal? = null
        var minimumKes: BigDecimal = BigDecimal.ZERO
        var maximumKes: BigDecimal = BigDecimal.ZERO
        var fixedAmount: BigDecimal = BigDecimal.ZERO
        var rate: BigDecimal? = null
        var fee: InspectionFeeRanges? = null
        //1. Handle ranges in the fee depending on amounts
        var feeType = diInspectionFeeId.rateType
        if ("RANGE".equals(diInspectionFeeId.rateType)) {
            var documentType = "LOCAL"
            when (documentType) {
                "L" -> {
                    documentType = "LOCAL"
                }
                "F" -> {
                    documentType = "PVOC"
                }
                "A" -> {
                    documentType = "AUCTION"
                }
            }
            KotlinLogging.logger { }.info("${documentType} CFI DOCUMENT TYPE = $currencyCode-$cfiValue")
            val feeRange = this.feeRangesRepository.findByInspectionFeeAndMinimumKshGreaterThanEqualAndMaximumKshLessThanEqual(diInspectionFeeId.id, cfiValue, documentType)
            if (feeRange.isEmpty()) {
                throw Exception("Item details with Id = ${itemId}, does not Have any range For payment Fee Id Selected ${cfiValue}")
            } else {
                fee = feeRange.get(0)
            }
            minimumUsd = fee.minimumUsd?.let { convertAmount(it, "USD") }
            maximumUsd = fee.maximumUsd?.let { convertAmount(it, "USD") }
            minimumKes = fee.minimumKsh ?: BigDecimal.ZERO
            maximumKes = fee.maximumKsh ?: BigDecimal.ZERO
            fixedAmount = fee.fixedAmount ?: BigDecimal.ZERO
            feeType = fee.rateType
            rate = fee.rate
        } else {
            minimumUsd = diInspectionFeeId.minimumUsd?.let { convertAmount(it.toBigDecimal(), "USD") }
            maximumUsd = diInspectionFeeId.higher?.let { convertAmount(it.toBigDecimal(), "USD") }
            minimumKes = diInspectionFeeId.minimumKsh?.toBigDecimal() ?: BigDecimal.ZERO
            maximumKes = diInspectionFeeId.maximumKsh?.toBigDecimal() ?: BigDecimal.ZERO
            fixedAmount = diInspectionFeeId.amountKsh?.toBigDecimal() ?: BigDecimal.ZERO
            rate = diInspectionFeeId.rate
            feeType = diInspectionFeeId.rateType
        }
        val percentage = 100
        var amount: BigDecimal? = null
        KotlinLogging.logger { }.info("$feeType CFI AMOUNT BEFORE CALCULATION = $currencyCode-$cfiValue")
        //2. Calculate based on the ranges provided
        when (feeType) {
            "PERCENTAGE" -> {
                amount = cfiValue.multiply(rate).divide(percentage.toBigDecimal())
                demandNoteItem.amountPayable = BigDecimal(amount?.toDouble() ?: 0.0)
                KotlinLogging.logger { }.info("$feeType MY AMOUNT BEFORE CALCULATION = $currencyCode-$amount")
                //3.  APPLY MAX AND MIN VALUES Prefer USD setting to KES setting
                amount?.let {
                    when (minimumUsd) {
                        null -> {
                            if (it < minimumKes && minimumKes > BigDecimal.ZERO) {
                                amount = minimumKes
                            } else if (it > maximumKes && maximumKes < BigDecimal.ZERO) {
                                amount = maximumKes
                            }
                        }
                        else -> {
                            if (it < minimumUsd && minimumUsd > BigDecimal.ZERO) {
                                amount = minimumUsd
                            } else if (maximumUsd != null && it > maximumUsd && maximumUsd > BigDecimal.ZERO) {
                                amount = maximumUsd
                            }
                        }
                    }
                }
                demandNoteItem.adjustedAmount = amount
                KotlinLogging.logger { }.info("$feeType MY AMOUNT AFTER CALCULATION = $amount")
            }
            "FIXED" -> {
                amount = fixedAmount
                KotlinLogging.logger { }.info("FIXED AMOUNT BEFORE CALCULATION = $fixedAmount")
                demandNoteItem.adjustedAmount = fixedAmount
                demandNoteItem.amountPayable = fixedAmount
            }
            "MANUAL" -> {
                // Not-Applicable to items
                amount = BigDecimal.ZERO
                demandNoteItem.adjustedAmount = amount
                demandNoteItem.amountPayable = amount
                KotlinLogging.logger { }.info("MANUAL AMOUNT BEFORE CALCULATION = $amount")
            }
            else -> {
                amount = BigDecimal.ZERO
                demandNoteItem.adjustedAmount = amount
                demandNoteItem.amountPayable = amount
            }
        }
    }

    fun convertAmount(amount: BigDecimal?, currencyCode: String): BigDecimal {
        return currencyExchangeRateRepository.findFirstByCurrencyCodeAndApplicableDateOrderByApplicableDateDesc(currencyCode, DATE_FORMAT.format(LocalDate.now()))?.let { exchangeRateEntity ->
            return amount?.times(exchangeRateEntity.exchangeRate
                    ?: BigDecimal.ZERO) ?: BigDecimal.ZERO
        } ?: run {
            currencyExchangeRateRepository.findFirstByCurrencyCodeAndCurrentRateAndStatus(currencyCode, 1, 1)?.let { exchangeRateEntity ->
                return amount?.times(exchangeRateEntity.exchangeRate
                        ?: BigDecimal.ZERO) ?: BigDecimal.ZERO
            } ?: run {
                throw ExpectedDataNotFound("Conversion rate for currency ${currencyCode} not found")
            }
        }
    }

    fun convertAmount(amount: BigDecimal?, currencyCode: String, demandNoteItem: CdDemandNoteItemsDetailsEntity?) {
        currencyExchangeRateRepository.findFirstByCurrencyCodeAndApplicableDateOrderByApplicableDateDesc(currencyCode, DATE_FORMAT.format(LocalDate.now()))?.let { exchangeRateEntity ->
            demandNoteItem?.exchangeRateId = exchangeRateEntity.id
            demandNoteItem?.cfvalue = amount?.times(exchangeRateEntity.exchangeRate
                    ?: BigDecimal.ZERO) ?: BigDecimal.ZERO
        } ?: run {
            KotlinLogging.logger { }.warn("Exchange rate for today not found, using the last known exchange rate")
            currencyExchangeRateRepository.findFirstByCurrencyCodeAndCurrentRateAndStatus(currencyCode, 1, 1)?.let { exchangeRateEntity ->
                demandNoteItem?.cfvalue = amount?.times(exchangeRateEntity.exchangeRate
                        ?: BigDecimal.ZERO) ?: BigDecimal.ZERO
                demandNoteItem?.exchangeRateId = exchangeRateEntity.id
            } ?: run {
                demandNoteItem?.cfvalue = amount ?: BigDecimal.ZERO
                throw ExpectedDataNotFound("Conversion rate for currency ${currencyCode} not found")
            }
        }
    }

    fun addAuctionItemDetailsToDemandNote(
            itemDetails: AuctionItemDetails, demandNote: CdDemandNoteEntity, map: ServiceMapsEntity,
            presentment: Boolean,
            user: UsersEntity
    ) {
        val fee = itemDetails.paymentFeeIdSelected
                ?: throw Exception("Item details with Id = ${itemDetails.id}, does not Have any Details For payment Fee Id Selected ")
        var demandNoteItem = iDemandNoteItemRepo.findByItemIdAndDemandNoteId(itemDetails.id, demandNote.id)
        if (demandNoteItem == null) {
            demandNoteItem = CdDemandNoteItemsDetailsEntity()
        }
        // Apply currency conversion rates for today
        when (itemDetails.currency?.toUpperCase()) {
            "KES" -> {
                demandNoteItem.cfvalue = itemDetails.totalPrice
            }
            else -> {
                convertAmount(itemDetails.totalPrice, "${itemDetails.currency}", demandNoteItem)
                KotlinLogging.logger { }.warn("Exchange Rate for ${itemDetails.currency}:${itemDetails.totalPrice} => ${demandNoteItem.cfvalue}")
            }
        }
        // Add extra data
        with(demandNoteItem) {
            itemId = itemDetails.id
            varField1 = itemDetails.quantity.toString()
            demandNoteId = demandNote.id
            product = itemDetails.itemName
            rate = fee.rate?.toString()
            rateType = fee.rateType
            feeName = fee.name ?: fee.description
            // Demand note Calculation Details
            status = map.activeStatus
            createdOn = commonDaoServices.getTimestamp()
            createdBy = commonDaoServices.getUserName(user)
        }
        // Apply fee type and adjustments
        demandNoteCalculation(demandNoteItem, fee, "KES", "A", itemDetails.id)
        // Skip saving for presentment
        if (!presentment) {
            iDemandNoteItemRepo.save(demandNoteItem)
            return
        }
        // Add this for presentment purposes
        demandNote.totalAmount = demandNote.totalAmount?.plus(demandNoteItem.adjustedAmount ?: BigDecimal.ZERO)
        demandNote.amountPayable = demandNote.amountPayable?.plus(demandNoteItem.amountPayable ?: BigDecimal.ZERO)
    }

    fun addItemDetailsToDemandNote(
            itemDetails: CdItemDetailsEntity, demandNote: CdDemandNoteEntity, map: ServiceMapsEntity,
            presentment: Boolean,
            user: UsersEntity
    ) {
        val fee = itemDetails.paymentFeeIdSelected
                ?: throw Exception("Item details with Id = ${itemDetails.id}, does not Have any Details For payment Fee Id Selected ")
        var demandNoteItem = iDemandNoteItemRepo.findByItemIdAndDemandNoteId(itemDetails.id, demandNote.id)
        if (demandNoteItem == null) {
            demandNoteItem = CdDemandNoteItemsDetailsEntity()
        }
        // Apply currency conversion rates for today
        when (itemDetails.foreignCurrencyCode?.toUpperCase()) {
            "KES" -> {
                demandNoteItem.cfvalue = itemDetails.totalPriceNcy
            }
            else -> {
                convertAmount(itemDetails.totalPriceNcy, "${itemDetails.foreignCurrencyCode}", demandNoteItem)
                KotlinLogging.logger { }.warn("Exchange Rate for ${itemDetails.foreignCurrencyCode}:${itemDetails.totalPriceNcy} => ${demandNoteItem.cfvalue}")
            }
        }
        // Add extra data
        with(demandNoteItem) {
            itemId = itemDetails.id
            varField1 = itemDetails.quantity.toString()
            demandNoteId = demandNote.id
            product = itemDetails.itemDescription ?: itemDetails.hsDescription ?: itemDetails.productTechnicalName
            rate = fee.rate?.toString()
            rateType = fee.rateType
            feeName = fee.name ?: fee.description
            // Demand note Calculation Details
            status = map.activeStatus
            createdOn = commonDaoServices.getTimestamp()
            createdBy = commonDaoServices.getUserName(user)
        }
        // Apply fee type and adjustments
        demandNoteCalculation(demandNoteItem, fee, "KES", itemDetails.cdDocId?.cdStandardsTwo?.cocType
                ?: "L", itemDetails.id)
        // Skip saving for presentment
        if (!presentment) {
            iDemandNoteItemRepo.save(demandNoteItem)
            return
        }
        // Add this for presentment purposes
        demandNote.totalAmount = demandNote.totalAmount?.plus(demandNoteItem.adjustedAmount ?: BigDecimal.ZERO)
        demandNote.amountPayable = demandNote.amountPayable?.plus(demandNoteItem.amountPayable ?: BigDecimal.ZERO)
    }

    fun calculateTotalAmountDemandNote(
            demandNote: CdDemandNoteEntity,
            map: ServiceMapsEntity,
            user: UsersEntity,
            presentment: Boolean,
    ): CdDemandNoteEntity {
        demandNote.amountPayable = BigDecimal.ZERO
        demandNote.totalAmount = BigDecimal.ZERO
        demandNote.cfvalue = BigDecimal.ZERO
        iDemandNoteItemRepo.findByDemandNoteId(demandNote.id!!).forEach { demandNoteItem ->
            demandNote.amountPayable = demandNote.amountPayable?.plus(demandNoteItem.adjustedAmount
                    ?: BigDecimal.ZERO)
            demandNote.totalAmount = demandNote.totalAmount?.plus(demandNoteItem.amountPayable
                    ?: BigDecimal.ZERO)
            demandNote.cfvalue = demandNote.cfvalue?.plus(demandNoteItem.cfvalue
                    ?: BigDecimal.ZERO)

        }

        demandNote.cfvalue = demandNote.cfvalue?.setScale(2, RoundingMode.HALF_UP)
        demandNote.totalAmount = demandNote.totalAmount?.setScale(2, RoundingMode.HALF_UP)
        demandNote.amountPayable = demandNote.amountPayable?.setScale(2, RoundingMode.HALF_UP)

        if (presentment) {
            return demandNote
        }
        return upDateDemandNoteWithUser(demandNote, user)
    }

    /**
     * Check for new demand note, approved demand note and payment status
     *
     * @param cdId Consignment ID
     */
    fun demandNotePaid(cdId: Long): CdDemandNoteEntity? {
        val noteEntity = iDemandNoteRepo.findAllByCdIdAndStatusIn(cdId, listOf(-1, 0, 1, 10))
        when {
            noteEntity.isEmpty() -> {
                KotlinLogging.logger { }.info("No Demand note")
                return null
            }
            else -> {
                noteEntity.forEach {
                    KotlinLogging.logger { }.info("Demand note: ${it.paymentStatus}")
                    if (it.paymentStatus == 1) {
                        return it
                    }
                }
                return null
            }
        }
    }

    @Transactional
    fun generateDemandNoteWithItemList(
            itemList: List<CdItemDetailsEntity>,
            map: ServiceMapsEntity,
            consignmentDocument: ConsignmentDocumentDetailsEntity,
            presentment: Boolean,
            amount: Double,
            user: UsersEntity
    ): CdDemandNoteEntity {
        return iDemandNoteRepo.findFirstByCdIdAndStatusIn(consignmentDocument.id!!, listOf(map.workingStatus))
                ?.let { demandNote ->
                    var demandNoteDetails = demandNote
                    // Remove all items for update
                    val demandNoteItems = iDemandNoteItemRepo.findByDemandNoteId(demandNote.id!!)
                    for (itm in demandNoteItems) {
                        iDemandNoteItemRepo.delete(itm)
                    }
                    //Call Function to add Item Details To be attached To The Demand note
                    demandNote.totalAmount = BigDecimal.ZERO
                    demandNote.amountPayable = BigDecimal.ZERO
                    itemList.forEach {
                        addItemDetailsToDemandNote(it, demandNoteDetails, map, presentment, user)
                        it.dnoteStatus = map.activeStatus
                        if (!presentment) {
                            demandNoteDetails = demandNoteUpDatingCDAndItem(it, user, demandNoteDetails)
                        }
                    }
                    // Foreign CoR without Items
                    if (itemList.isEmpty()) {
                        demandNoteDetails.totalAmount = amount.toBigDecimal()
                        demandNoteDetails.amountPayable = amount.toBigDecimal()
                    }
                    //Calculate the total Amount for Items In one Cd To be paid For
                    if (!presentment) {
                        demandNoteDetails = calculateTotalAmountDemandNote(demandNoteDetails, map, user, presentment)
                    }
                    demandNoteDetails
                }
                ?: kotlin.run {
                    var demandNote = CdDemandNoteEntity()
                    with(demandNote) {
                        cdId = consignmentDocument.id
                        val cdImporter = consignmentDocument.cdImporter?.let { findCDImporterDetails(it) }
                        nameImporter = cdImporter?.name
                        address = cdImporter?.physicalAddress
                        telephone = cdImporter?.telephone
                        cdRefNo = consignmentDocument.cdStandard?.applicationRefNo
                        //todo: Entry Number
                        entryAblNumber = consignmentDocument.cdStandard?.declarationNumber ?: "UNKNOWN"
                        totalAmount = BigDecimal.ZERO
                        amountPayable = BigDecimal.ZERO
                        receiptNo = "NOT PAID"
                        product = consignmentDocument.ucrNumber ?: "UNKNOWN"
                        rate = "UNKNOWN"
                        ucrNumber = consignmentDocument.ucrNumber
                        cfvalue = BigDecimal.ZERO
                        //Generate Demand note number
                        demandNoteNumber =
                                "KIMS${consignmentDocument.cdType?.demandNotePrefix}${
                                    generateRandomText(
                                            5,
                                            map.secureRandom,
                                            map.messageDigestAlgorithm,
                                            true
                                    )
                                }".toUpperCase()
                        paymentStatus = map.inactiveStatus
                        paymentPurpose = PaymentPurpose.CONSIGNMENT.code
                        dateGenerated = commonDaoServices.getCurrentDate()
                        generatedBy = commonDaoServices.concatenateName(user)
                        status = map.workingStatus
                        createdOn = commonDaoServices.getTimestamp()
                        createdBy = commonDaoServices.getUserName(user)
                    }
                    if (!presentment) {
                        demandNote = iDemandNoteRepo.save(demandNote)
                    }
                    //Call Function to add Item Details To be attached To The Demand note
                    itemList.forEach {
                        addItemDetailsToDemandNote(it, demandNote, map, presentment, user)
                        //Calculate the total Amount for Items In one Cd Tobe paid For
                        it.dnoteStatus = map.activeStatus
                        if (!presentment) {
                            demandNoteUpDatingCDAndItem(it, user, demandNote)
                        }
                    }
                    if (!presentment) {
                        demandNote = calculateTotalAmountDemandNote(demandNote, map, user, presentment)
                    }
                    // Foreign CoR/CoC without Items
                    if (itemList.isEmpty()) {
                        demandNote.totalAmount = amount.toBigDecimal()
                        demandNote.amountPayable = amount.toBigDecimal()
                    }
                    return demandNote

                }
    }

    @Transactional
    fun generateAuctionDemandNoteWithItemList(
            itemList: List<AuctionItemDetails>,
            map: ServiceMapsEntity,
            auctionRequest: AuctionRequests,
            presentment: Boolean,
            amount: Double,
            user: UsersEntity
    ): CdDemandNoteEntity {
        return iDemandNoteRepo.findFirstByUcrNumberAndPaymentStatusIn(auctionRequest.auctionLotNo!!, listOf(map.workingStatus))
                ?.let { demandNote ->
                    var demandNoteDetails = demandNote
                    // Remove all items for update
                    val demandNoteItems = iDemandNoteItemRepo.findByDemandNoteId(demandNote.id!!)
                    for (itm in demandNoteItems) {
                        iDemandNoteItemRepo.delete(itm)
                    }
                    //Call Function to add Item Details To be attached To The Demand note
                    demandNote.totalAmount = BigDecimal.ZERO
                    demandNote.amountPayable = BigDecimal.ZERO
                    itemList.forEach {
                        addAuctionItemDetailsToDemandNote(it, demandNoteDetails, map, presentment, user)
                    }
                    // Foreign CoR without Items
                    if (itemList.isEmpty()) {
                        demandNoteDetails.totalAmount = amount.toBigDecimal()
                        demandNoteDetails.amountPayable = amount.toBigDecimal()
                    }
                    //Calculate the total Amount for Items In one Cd To be paid For
                    if (!presentment) {
                        demandNoteDetails = calculateTotalAmountDemandNote(demandNoteDetails, map, user, presentment)
                    }
                    demandNoteDetails
                }
                ?: kotlin.run {
                    var demandNote = CdDemandNoteEntity()
                    with(demandNote) {
                        cdId = auctionRequest.id
                        paymentPurpose = PaymentPurpose.AUDIT.code

                        nameImporter = auctionRequest.importerName
                        address = auctionRequest.location
                        telephone = auctionRequest.importerPhone
                        cdRefNo = auctionRequest.auctionLotNo
                        //todo: Entry Number
                        entryAblNumber = "UNKNOWN"
                        totalAmount = BigDecimal.ZERO
                        amountPayable = BigDecimal.ZERO
                        receiptNo = "NOT PAID"
                        product = "UNKNOWN"
                        rate = "UNKNOWN"
                        ucrNumber = auctionRequest.auctionLotNo
                        cfvalue = BigDecimal.ZERO
                        //Generate Demand note number
                        demandNoteNumber = "KIMSAR${
                            generateRandomText(
                                    5,
                                    map.secureRandom,
                                    map.messageDigestAlgorithm,
                                    true
                            )
                        }".toUpperCase()
                        paymentStatus = map.inactiveStatus
                        dateGenerated = commonDaoServices.getCurrentDate()
                        generatedBy = commonDaoServices.concatenateName(user)
                        status = map.workingStatus
                        createdOn = commonDaoServices.getTimestamp()
                        createdBy = commonDaoServices.getUserName(user)
                    }
                    if (!presentment) {
                        demandNote = iDemandNoteRepo.save(demandNote)
                    }
                    //Call Function to add Item Details To be attached To The Demand note
                    itemList.forEach {
                        addAuctionItemDetailsToDemandNote(it, demandNote, map, presentment, user)
                        //Calculate the total Amount for Items In one Cd Tobe paid For
                    }
                    if (!presentment) {
                        demandNote = calculateTotalAmountDemandNote(demandNote, map, user, presentment)
                    }
                    // Foreign CoR/CoC without Items
                    if (itemList.isEmpty()) {
                        demandNote.totalAmount = amount.toBigDecimal()
                        demandNote.amountPayable = amount.toBigDecimal()
                    }
                    return demandNote

                }
    }

    fun demandNoteUpDatingCDAndItem(
            itemDetails: CdItemDetailsEntity,
            user: UsersEntity,
            demandNote: CdDemandNoteEntity
    ): CdDemandNoteEntity {
        (updateCdItemDetailsInDB(itemDetails, user).cdDocId
                ?.let { cdDetails ->
                    updateCDStatus(cdDetails, ConsignmentDocumentStatus.PAYMENT_REQUEST)
                    updateCdDetailsInDB(cdDetails, user)
                            .let {
                                return demandNote
                            }
                }
                ?: throw Exception("CD details for Item with Id = ${itemDetails.id}, does not Exist"))
    }

    fun demandNotePayment(
            dNote: CdDemandNoteEntity,
            invoiceDetails: StagingPaymentReconciliation,
            map: ServiceMapsEntity
    ): CdDemandNoteEntity {
        var demandNote = dNote
        with(demandNote) {
            receiptNo = invoiceDetails.transactionId
            paymentStatus = map.activeStatus
            modifiedBy = "SYSTEM"
            modifiedOn = commonDaoServices.getTimestamp()
        }

        demandNote = iDemandNoteRepo.save(demandNote)

        val itemDetails = dNote.cdId?.let { findCD(it) }
        if (itemDetails != null) {
            with(itemDetails) {
                paymentMadeStatus = map.activeStatus.toString()
                modifiedBy = "SYSTEM"
                modifiedOn = commonDaoServices.getTimestamp()
            }
            updateCdDetailsInDB(itemDetails, null)
        }

        return demandNote
    }

    fun upDateDemandNoteWithUser(demandNote: CdDemandNoteEntity, user: UsersEntity): CdDemandNoteEntity {
        with(demandNote) {
            modifiedBy = commonDaoServices.getUserName(user)
            modifiedOn = commonDaoServices.getTimestamp()
        }
        return iDemandNoteRepo.save(demandNote)
    }

    fun upDateDemandNote(demandNote: CdDemandNoteEntity): CdDemandNoteEntity {
        KotlinLogging.logger { }.info("Demand Note data: ${demandNote.paymentStatus} -> ${demandNote.status}")
        return iDemandNoteRepo.save(demandNote)
    }


    @Throws(JSONException::class)
    fun convert(json: String?, root: String): String? {
        val jsonObject = JSONObject(json)
        return """<?xml version="1.0" encoding="ISO-8859-15"?> <$root>${XML.toString(jsonObject)}</$root>"""
    }

    fun updateCDStatus(consignment: ConsignmentDocumentDetailsEntity, statusValue: ConsignmentDocumentStatus): ConsignmentDocumentDetailsEntity {
        var updateStatus = consignment
        try {
            val status = findCdStatusCategory(statusValue.code)
            // Add non application status to cd standard
            if (status.applicationStatus != 1) {
                consignment.cdStandard?.let { cdStandard ->
                    with(cdStandard) {
                        approvalStatus = status.typeName
                        statusId = status.id
                        approvalDate = commonDaoServices.getCurrentDate().toString()
                    }
                    val updateCD = iCdStandardsRepo.save(cdStandard)
                    KotlinLogging.logger { }.info { "CD STANDARD UPDATED STATUS TO = ${status.typeName}" }
                }
            } else {
                KotlinLogging.logger { }.info { "CD UPDATED STATUS TO = ${status.typeName}" }
            }

            // Update consignment status as well
            consignment.modifiedOn = commonDaoServices.getTimestamp()
            consignment.approveRejectCdStatusType = status
            updateStatus = this.iConsignmentDocumentDetailsRepo.save(consignment)
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to assign status:  ${statusValue.code}", ex)
        }
        return updateStatus
    }

    fun updateCDStatus(consignment: ConsignmentDocumentDetailsEntity, statusValue: Long): ConsignmentDocumentDetailsEntity {
        var updateStatus = consignment
        try {
            val status = findCdStatusValue(statusValue)
            if (status.applicationStatus != 1) {
                consignment.cdStandard?.let { cdStandard ->
                    with(cdStandard) {
                        approvalStatus = status.typeName
                        statusId = status.id
                        approvalDate = commonDaoServices.getCurrentDate().toString()
                    }
                    val updateCD = iCdStandardsRepo.save(cdStandard)
                    KotlinLogging.logger { }.info { "CD UPDATED STATUS TO = ${updateCD.approvalStatus}" }
                }
            }
            // Update consignment status as well
            consignment.modifiedOn = commonDaoServices.getTimestamp()
            consignment.approveRejectCdStatusType = status
            updateStatus = this.iConsignmentDocumentDetailsRepo.save(consignment)
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to assign status: $statusValue", ex)
        }
        return updateStatus
    }

    fun updateItemCdStatus(item: CdItemDetailsEntity, statusValue: Long): CdItemDetailsEntity {
        with(item) {
            itemStatus = findCdStatusValue(statusValue).typeName
        }
        return item
    }

    fun listDIFee(): List<DestinationInspectionFeeEntity> {
        return iDIFeeDetailsRepo.findByStatus(1)
    }

    fun findDIFee(feeId: Long): DestinationInspectionFeeEntity {
        iDIFeeDetailsRepo.findByIdOrNull(feeId)
                ?.let { diFeeDetails ->
                    return diFeeDetails
                }
                ?: throw Exception("DI fee Details with ID = ${feeId}, does not Exist")
    }

    fun findDemandNoteWithID(cdID: Long): CdDemandNoteEntity? {
        return iDemandNoteRepo.findByIdOrNull(cdID)
    }

    fun findDemandNoteItemByID(itemId: Long): CdDemandNoteItemsDetailsEntity? {
        val items = iDemandNoteItemRepo.findByItemId(itemId)
        for (item in items) {
            val dnn = iDemandNoteRepo.findByIdOrNull(item.demandNoteId)?.let { dn -> dn.status == 10 || dn.status == 1 }
            if (dnn == true) {
                return item
            }
        }
        return null
    }

    fun findDemandNoteItemDetails(demandNoteID: Long): List<CdDemandNoteItemsDetailsEntity> {
        return iDemandNoteItemRepo.findByDemandNoteId(demandNoteID)
//                ?.let { demandNoteDetails ->
//                    return demandNoteDetails
//                }
//                ?: throw Exception("Demand Note Details with [Item ID = ${item.id}], does not Exist")
    }

    fun findDemandNoteWithPaymentStatus(cdId: Long, status: Int): CdDemandNoteEntity? {
        return iDemandNoteRepo.findFirstByCdIdAndPaymentStatusAndPaymentPurpose(cdId, status, PaymentPurpose.CONSIGNMENT.code)
    }

    fun findDemandNoteByBatchID(invoiceBatchId: Long): List<CdDemandNoteEntity> {
        iDemandNoteRepo.findByInvoiceBatchNumberId(invoiceBatchId)
                ?.let { demandNoteDetails ->
                    return demandNoteDetails
                }
                ?: throw Exception("Demand Note Details with [Invoice Batch ID = ${invoiceBatchId}], do not exist")
    }

    fun generateDemandNoteNumber(): String {
        val prefix = "DN-"
        val number = (0..9).shuffled().take(6).joinToString("")
        val demandNoteNumber = prefix.plus(number)
        val demandNote = iDemandNoteRepo.findByDemandNoteNumber(demandNoteNumber)
        if (demandNote != null) {
            generateDemandNoteNumber()
        }
        return demandNoteNumber
    }

    fun findCDItemsListWithCDID(consignmentDocumentDetailsEntity: ConsignmentDocumentDetailsEntity): List<CdItemDetailsEntity> {
        iCdItemsRepo.findByCdDocId(consignmentDocumentDetailsEntity)
                ?.let { cdItemsDetailsList ->
                    return cdItemsDetailsList
                }
                ?: throw Exception("CD Item Details with the following CD ID= ${consignmentDocumentDetailsEntity.id}, do not Exist")
    }

    fun findCDItemsListWithCDIDAndDemandNoteStatus(
            consignmentDocumentDetailsEntity: ConsignmentDocumentDetailsEntity,
            map: ServiceMapsEntity
    ): List<CdItemDetailsEntity> {
        iCdItemsRepo.findByCdDocIdAndDnoteStatus(consignmentDocumentDetailsEntity, map.activeStatus)
                ?.let { cdItemsDetailsList ->
                    return cdItemsDetailsList
                }
                ?: throw Exception("CD Item Details with the following CD ID= ${consignmentDocumentDetailsEntity.id} and  demand note status = ${map.activeStatus}, do not Exist")
    }

    fun saveCheckListDetails(
            checklistEntity: CdInspectionChecklistEntity,
            user: UsersEntity,
            map: ServiceMapsEntity
    ): CdInspectionChecklistEntity {
        var checklist = checklistEntity
        with(checklist) {
            status = map.activeStatus
            createdBy = commonDaoServices.getUserName(user)
            createdOn = commonDaoServices.getTimestamp()
        }
        checklist = iCdInspectionChecklistRepo.save(checklist)

        KotlinLogging.logger { }.info { "CheckList Details saved ID = ${checklist.id}" }
        return checklist
    }

    fun saveInspectionGeneralDetails(
            inspectionGeneralEntity: CdInspectionGeneralEntity, cDetails: ConsignmentDocumentDetailsEntity,
            user: UsersEntity, map: ServiceMapsEntity
    ): CdInspectionGeneralEntity {
        // Detach previous checklist on new checklist filled
        iCdInspectionGeneralRepo.findFirstByCdDetailsAndCurrentChecklist(cDetails, 1)?.let { prevChecklist ->
            prevChecklist.currentChecklist = 0
            prevChecklist.varField4 = commonDaoServices.getTimestamp().toString()
            iCdInspectionGeneralRepo.save(prevChecklist)
        }
        // Get existing one or create a new one
        with(inspectionGeneralEntity) {
            cdDetails = cDetails
            checklistVersion = iCdInspectionGeneralRepo.countByCdDetails(cDetails) + 1
            currentChecklist = 1
            status = map.activeStatus
            createdBy = commonDaoServices.getUserName(user)
            createdOn = commonDaoServices.getTimestamp()
        }
        val updatedInspectionGeneralEntity = iCdInspectionGeneralRepo.save(inspectionGeneralEntity)

        KotlinLogging.logger { }
                .info { "Inspection General CheckList Details saved ID = ${updatedInspectionGeneralEntity.id}" }
        return updatedInspectionGeneralEntity
    }

    fun saveInspectionAgrochemItemChecklist(
            inspectionAgrochemItemChecklistEntity: CdInspectionAgrochemItemChecklistEntity,
            inspectionGeneralEntity: CdInspectionGeneralEntity,
            user: UsersEntity, map: ServiceMapsEntity
    ): CdInspectionAgrochemItemChecklistEntity {
        var inspectionAgrochemItemChecklist = inspectionAgrochemItemChecklistEntity
        with(inspectionAgrochemItemChecklist) {
            status = map.activeStatus
            createdBy = commonDaoServices.getUserName(user)
            createdOn = commonDaoServices.getTimestamp()
        }
        inspectionAgrochemItemChecklist = iCdInspectionAgrochemItemChecklistRepo.save(inspectionAgrochemItemChecklist)

        KotlinLogging.logger { }
                .info { "Inspection Agrochem CheckList Details saved ID = ${inspectionAgrochemItemChecklist.id}" }
        return inspectionAgrochemItemChecklist
    }

    fun saveInspectionEngineeringItemChecklist(
            inspectionEngineeringItemChecklistEntity: CdInspectionEngineeringItemChecklistEntity,
            inspectionGeneralEntity: CdInspectionGeneralEntity,
            user: UsersEntity, map: ServiceMapsEntity
    ): CdInspectionEngineeringItemChecklistEntity {
        var inspectionEngineeringItemChecklist = inspectionEngineeringItemChecklistEntity
        with(inspectionEngineeringItemChecklist) {
            status = map.activeStatus
            createdBy = commonDaoServices.getUserName(user)
            createdOn = commonDaoServices.getTimestamp()
        }
        inspectionEngineeringItemChecklist =
                iCdInspectionEngineeringItemChecklistRepo.save(inspectionEngineeringItemChecklist)

        KotlinLogging.logger { }
                .info { "Inspection Engineering CheckList Details saved ID = ${inspectionEngineeringItemChecklist.id}" }
        return inspectionEngineeringItemChecklist
    }

    fun saveInspectionOtherItemChecklist(
            inspectionOtherItemChecklistEntity: CdInspectionOtherItemChecklistEntity,
            inspectionGeneralEntity: CdInspectionGeneralEntity,
            user: UsersEntity, map: ServiceMapsEntity
    ): CdInspectionOtherItemChecklistEntity {
        var inspectionOtherItemChecklist = inspectionOtherItemChecklistEntity
        with(inspectionOtherItemChecklist) {
            status = map.activeStatus
            createdBy = commonDaoServices.getUserName(user)
            createdOn = commonDaoServices.getTimestamp()
        }
        inspectionOtherItemChecklist = iCdInspectionOtherItemChecklistRepo.save(inspectionOtherItemChecklist)

        KotlinLogging.logger { }
                .info { "Inspection Engineering CheckList Details saved ID = ${inspectionOtherItemChecklist.id}" }
        return inspectionOtherItemChecklist
    }

    fun saveInspectionMotorVehicleItemChecklist(
            inspectionMotorVehicleItemChecklistEntity: CdInspectionMotorVehicleItemChecklistEntity,
            inspectionGeneralEntity: CdInspectionGeneralEntity,
            user: UsersEntity, map: ServiceMapsEntity
    ): CdInspectionMotorVehicleItemChecklistEntity {
        var inspectionMotorVehicleItemChecklist = inspectionMotorVehicleItemChecklistEntity
        with(inspectionMotorVehicleItemChecklist) {
            status = map.activeStatus
            createdBy = commonDaoServices.getUserName(user)
            createdOn = commonDaoServices.getTimestamp()
        }
        inspectionMotorVehicleItemChecklist =
                iCdInspectionMotorVehicleItemChecklistRepo.save(inspectionMotorVehicleItemChecklist)

        KotlinLogging.logger { }
                .info { "Inspection Motor Vehicle CheckList Details saved ID = ${inspectionMotorVehicleItemChecklist.id}" }
        return inspectionMotorVehicleItemChecklist
    }

    fun findChecklistInspectionTypeById(checkListId: Long): CdChecklistTypesEntity =
            iChecklistInspectionTypesRepo.findByIdOrNull(checkListId)
                    ?.let {
                        return it
                    }
                    ?: throw Exception("No inspection type with the following ID= $checkListId")

    fun saveSampleCollectDetails(
            sampleCollectEntity: CdSampleCollectionEntity,
            user: UsersEntity,
            map: ServiceMapsEntity
    ): CdSampleCollectionEntity {
        var sampleCollect = sampleCollectEntity
        with(sampleCollect) {
            slNo = generateRandomText(5, map.secureRandom, map.messageDigestAlgorithm, true)
            status = map.activeStatus
            createdBy = commonDaoServices.getUserName(user)
            createdOn = commonDaoServices.getTimestamp()
        }
        sampleCollect = iSampleCollectRepo.save(sampleCollect)

        KotlinLogging.logger { }.info { "Sample Collect Details saved ID = ${sampleCollect.id}" }
        return sampleCollect
    }

    fun saveSampleSubmitDetails(
            sampleSubmitEntity: CdSampleSubmissionItemsEntity,
            user: UsersEntity,
            map: ServiceMapsEntity
    ): CdSampleSubmissionItemsEntity {
        var sampleSubmit = sampleSubmitEntity
        with(sampleSubmit) {
            sampleRefNumber =
                    "SSF#${generateRandomText(5, map.secureRandom, map.messageDigestAlgorithm, true).toUpperCase()}"
            status = map.activeStatus
            createdBy = commonDaoServices.getUserName(user)
            createdOn = commonDaoServices.getTimestamp()
        }
        sampleSubmit = iSampleSubmitRepo.save(sampleSubmit)

        KotlinLogging.logger { }.info { "Sample Submit Details saved ID = ${sampleSubmit.id}" }
        return sampleSubmit
    }

    fun updateSampleSubmitDetails(
            sampleSubmitEntity: CdSampleSubmissionItemsEntity,
            user: UsersEntity,
            map: ServiceMapsEntity
    ): CdSampleSubmissionItemsEntity {
        var sampleSubmit = sampleSubmitEntity
        with(sampleSubmit) {
            modifiedBy = commonDaoServices.getUserName(user)
            modifiedOn = commonDaoServices.getTimestamp()
        }
        sampleSubmit = iSampleSubmitRepo.save(sampleSubmit)

        KotlinLogging.logger { }.info { "Sample Submit Details Update with ID = ${sampleSubmit.id}" }
        return sampleSubmit
    }

    fun saveSampleSubmitParamDetails(
            sampleSubmitEntity: CdSampleSubmissionItemsEntity,
            laboratoryEntity: CdLaboratoryEntity,
            sampleSubmitParamEntity: CdSampleSubmissionParamatersEntity,
            user: UsersEntity,
            map: ServiceMapsEntity
    ): CdSampleSubmissionParamatersEntity {
        var sampleSubmitParam = sampleSubmitParamEntity
        with(sampleSubmitParam) {
            laboratoryId = laboratoryEntity
            sampleSubmissionId = sampleSubmitEntity
            transactionDate = commonDaoServices.getCurrentDate()
            status = map.activeStatus
            createdBy = commonDaoServices.getUserName(user)
            createdOn = commonDaoServices.getTimestamp()
        }
        sampleSubmitParam = iSampleSubmissionParamRepo.save(sampleSubmitParam)

        KotlinLogging.logger { }.info { "Sample Submit Param Details saved ID = ${sampleSubmitParam.id}" }
        return sampleSubmitParam
    }

    fun getInspectionDate(itemDetails: CdItemDetailsEntity): Date {
        itemDetails.cdDocId?.inspectionDate?.let {
            return it
        } ?: throw Exception("No inspection date on CD Item Details with the following ID= ${itemDetails.id}")
    }

    fun findCdCountryByCountryCode(countryCodeValue: String): CountryTypeCodesEntity? {
        return iCountryTypeCodesRepo.findByCountryCode(countryCodeValue)
    }

    fun findCfsCd(cfsCodeValue: String): CfsTypeCodesEntity? {
        return iCfsTypeCodesRepository.findByCfsCode(cfsCodeValue)
    }

    fun findCfsID(id: Long): CfsTypeCodesEntity {
        iCfsTypeCodesRepository.findByIdOrNull(id)?.let {
            return it
        }
                ?: throw Exception("No CFS with ID = $id")
    }

    fun findCfsUserFromCdCfs(cfsCdID: Long): CdCfsUserCfsEntity? {
        return iCdCfsUserCfsRepository.findByCdCfs(cfsCdID)
    }

    fun findPortCd(portCodeValue: String): PortsTypeCodesEntity? {
        return iPortsTypeCodesRepository.findByPortCode(portCodeValue)
    }

    fun findPortUserFromPortCd(portCdID: Long): CdPortsUserPortsEntity? {
        return iCdPortsUserPortsRepository.findByCdPorts(portCdID)
    }

    fun findCdWithUcrNumber(ucrNumber: String): ConsignmentDocumentDetailsEntity? =
        iConsignmentDocumentDetailsRepo.findByUcrNumber(ucrNumber)
            ?.let {
                return it
            }

    fun findCdWithUcrNumberExcept(ucrNumber: String, id: Long?) =
        iConsignmentDocumentDetailsRepo.findByUcrNumberAndIdNot(ucrNumber, id)
    fun findCdWithUcrNumberAndCdRef(ucrNumber: String, version: Long,cdRef: String?) =
        iConsignmentDocumentDetailsRepo.findFirsByUcrNumberAndVersionAndCdStandard_ApplicationRefNoOrderByVersionAsc(ucrNumber,version, cdRef)


    fun countCdWithUcrNumberAndVersion(ucrNumber: String, version: Long): Long =
        iConsignmentDocumentDetailsRepo.countByUcrNumberAndVersion(ucrNumber, version)

    fun countCdWithUcrNumberAndVersionAndStatusNotIn(ucrNumber: String, version: Long, statuses: List<Int>): Long =
        iConsignmentDocumentDetailsRepo.countByUcrNumberAndVersionAndStatusNotIn(ucrNumber, version, statuses)

    fun findCdWithUcrNumberAndVersion(ucrNumber: String, version: Long): ConsignmentDocumentDetailsEntity? =
        iConsignmentDocumentDetailsRepo.findByUcrNumberAndVersion(ucrNumber, version)

    fun findCdWithUcrNumberAndVersion(
        ucrNumber: String,
        version: Long,
        statuses: List<Int>
    ): ConsignmentDocumentDetailsEntity? =
        iConsignmentDocumentDetailsRepo.findByUcrNumberAndVersionAndStatusNotIn(ucrNumber, version, statuses)

    fun findCdWithUcrNumberLatest(ucrNumber: String): ConsignmentDocumentDetailsEntity? =
        iConsignmentDocumentDetailsRepo.findTopByUcrNumberOrderByIdDesc(ucrNumber)
            ?.let {
                return it
            }

    fun findCdWithUcrNumberLatestAndStatusNotIn(
        ucrNumber: String,
        statuses: List<Int>
    ): ConsignmentDocumentDetailsEntity? =
        iConsignmentDocumentDetailsRepo.findTopByUcrNumberAndStatusNotInOrderByIdDesc(ucrNumber, statuses)
            ?.let {
                return it
            }

    fun createCDTransactionLog(
        map: ServiceMapsEntity,
        user: UsersEntity,
        CDID: Long,
        transRemarks: String,
        transProcess: String,
        saveLog: Boolean = false,
    ): CdTransactionsEntity {
        var cdTransactionLog = CdTransactionsEntity()
        with(cdTransactionLog) {
            cdId = CDID
            remarks = transRemarks
            processStage = transProcess
            transactionDate = commonDaoServices.getCurrentDate()
            status = map.activeStatus
            createdBy = commonDaoServices.concatenateName(user)
            createdOn = commonDaoServices.getTimestamp()
        }
        // Skip saving transaction log, its not necessary
        if (saveLog) {
            cdTransactionLog = iCdTransactionRepo.save(cdTransactionLog)
        }
        return cdTransactionLog
    }


    fun saveUploads(
            uploads: DiUploadsEntity,
            docFile: MultipartFile,
            doc: String,
            user: UsersEntity,
            map: ServiceMapsEntity,
            itemDetails: CdItemDetailsEntity?,
            consignmentDocumentDetailsEntity: ConsignmentDocumentDetailsEntity?
    ): DiUploadsEntity {

        with(uploads) {
            name = commonDaoServices.saveDocuments(docFile)
            fileType = docFile.contentType
            documentType = doc
            fileSize = docFile.size
            document = docFile.bytes
            itemId = itemDetails
            cdId = consignmentDocumentDetailsEntity
            transactionDate = commonDaoServices.getCurrentDate()
            status = map.activeStatus
            createdBy = commonDaoServices.getUserName(user)
            createdOn = commonDaoServices.getTimestamp()

        }

        return iDIUploadsRepo.save(uploads)
    }

    fun saveConsignmentAttachment(
            uploads: DiUploadsEntity,
            docFile: MultipartFile,
            doc: String,
            user: UsersEntity,
            map: ServiceMapsEntity,
            consignmentDocumentDetailsEntity: ConsignmentDocumentDetailsEntity?
    ): DiUploadsEntity {
        with(uploads) {
            name = commonDaoServices.saveDocuments(docFile)
            fileType = docFile.contentType
            documentType = doc
            fileSize = docFile.size
            document = docFile.bytes
            cdId = consignmentDocumentDetailsEntity
            transactionDate = commonDaoServices.getCurrentDate()
            status = map.activeStatus
            createdBy = commonDaoServices.getUserName(user)
            createdOn = commonDaoServices.getTimestamp()

        }
        return iDIUploadsRepo.save(uploads)
    }

    fun findDiUploadById(uploadId: Long): DiUploadsEntity {
        iDIUploadsRepo.findByIdOrNull(uploadId)
                ?.let { diUpload ->
                    return diUpload
                }
                ?: throw Exception("Attachment with the following ID= ${uploadId}, do not Exist")
    }

    fun findAllAttachmentsByCd(cd: ConsignmentDocumentDetailsEntity): List<DiUploadsEntity>? {
        return iDIUploadsRepo.findAllByCdId(cd)
    }

    fun findItemWithItemID(cdItemId: Long): CdItemDetailsEntity {
        iCdItemsRepo.findByIdOrNull(cdItemId)
                ?.let { cdItemDetails ->
                    return cdItemDetails
                }
                ?: throw Exception("CD Item Details with the following ID= ${cdItemId}, do not Exist")
    }

    fun findItemWithItemIDAndDocument(cd: ConsignmentDocumentDetailsEntity, cdItemId: Long?): CdItemDetailsEntity {
        return iCdItemsRepo.findByCdDocIdAndId(cd, cdItemId)
    }

    fun findItemWithUuid(uuid: String): CdItemDetailsEntity {
        iCdItemsRepo.findByUuid(uuid)
                ?.let { cdItemDetails ->
                    return cdItemDetails
                }
                ?: throw Exception("CD Item Details with the following uuid= ${uuid}, do not Exist")
    }

    fun findCDImporterDetails(importerDetailsID: Long): CdImporterDetailsEntity {
        iCdImporterRepo.findByIdOrNull(importerDetailsID)
                ?.let { it ->
                    return it
                }
                ?: throw Exception("Importer Details with ID= ${importerDetailsID}, does not Exist")
    }

    fun findCdStandardsTWODetails(standardsTwoDetailsID: Long): CdStandardsTwoEntity {
        iCdStandardsTwoRepo.findByIdOrNull(standardsTwoDetailsID)
                ?.let { it ->
                    return it
                }
                ?: throw Exception("Standards Two Details with ID= ${standardsTwoDetailsID}, does not Exist")
    }

    fun findCdConsigneeDetails(consigneeDetailsID: Long): CdConsigneeDetailsEntity {
        iCdConsigneeRepo.findByIdOrNull(consigneeDetailsID)
                ?.let { it ->
                    return it
                }
                ?: throw Exception("Consignee Details with ID= ${consigneeDetailsID}, does not Exist")
    }

    fun findCdExporterDetails(exporterDetailsID: Long): CdExporterDetailsEntity {
        iCdExporterRepo.findByIdOrNull(exporterDetailsID)
                ?.let { it ->
                    return it
                }
                ?: throw Exception("Exporter Details with ID= ${exporterDetailsID}, does not Exist")
    }

    fun findCdConsignorDetails(consignorDetailsID: Long): CdConsignorDetailsEntity {
        iCdConsignorRepo.findByIdOrNull(consignorDetailsID)
                ?.let { it ->
                    return it
                }
                ?: throw Exception("Consignor Details with ID= ${consignorDetailsID}, does not Exist")
    }

    fun findCdTransportDetails(transportDetailsID: Long): CdTransportDetailsEntity {
        iCdTransportRepo.findByIdOrNull(transportDetailsID)
                ?.let { it ->
                    return it
                }
                ?: throw Exception("Transport Details with ID= ${transportDetailsID}, does not Exist")
    }

    fun findCdHeaderOneDetails(headerOneDetailsID: Long): CdValuesHeaderLevelEntity {
        iCdValuesHeaderLevelRepo.findByIdOrNull(headerOneDetailsID)
                ?.let { it ->
                    return it
                }
                ?: throw Exception("Header One Details with ID= ${headerOneDetailsID}, does not Exist")
    }

    fun findCdItemNonStandardByItemID(cdItemDetails: CdItemDetailsEntity): CdItemNonStandardEntity? {
        return iCdItemNonStandardEntityRepository.findByCdItemDetailsId(cdItemDetails)
    }

    fun findIdf(ucrNumber: String): IDFDetailsEntity? {
        return idfsRepo.findFirstByUcrNo(ucrNumber)
//                ?.let { idfsEntity ->
//                    return idfsEntity
//                }
//                ?: throw Exception("IDF Details with the following UCR NUMBER = ${ucrNumber}, does not Exist")
    }

    fun findManifest(billCode: String): ManifestDetailsEntity? {
        return manifestRepo.findFirstByTdBillCode(billCode)
//                ?.let { idfsEntity ->
//                    return idfsEntity
//                }
//                ?: throw Exception("IDF Details with the following UCR NUMBER = ${ucrNumber}, does not Exist")
    }

    fun findByManifestNumber(manifestNo: String): ManifestDetailsEntity? {
        return manifestRepo.findByManifestNumber(manifestNo)
    }

    fun findDeclaration(ucrNumber: String): DeclarationDetailsEntity? {
        return declarationRepo.findFirstByRefNum(ucrNumber)
//                ?.let { idfsEntity ->
//                    return idfsEntity
//                }
//                ?: throw Exception("IDF Details with the following UCR NUMBER = ${ucrNumber}, does not Exist")
    }

    fun findDeclarationByDclRefNum(dclRefNum: String): DeclarationDetailsEntity? {
        return declarationRepo.findByDeclarationRefNo(dclRefNum)
    }

    fun findSampleSubmittedItemID(cdItemID: Long): QaSampleSubmissionEntity? {
        return SampleSubmissionRepo.findByCdItemId(cdItemID)
    }

    fun findSampleSubmittedBYCdItemID(cdItemID: Long): QaSampleSubmissionEntity {
        SampleSubmissionRepo.findByCdItemId(cdItemID)?.let {
            return it
        } ?: throw ExpectedDataNotFound("No sample submission found with the following [cdItemID=$cdItemID]")
    }

    fun ssfSave(
            cdItemDetails: CdItemDetailsEntity,
            ssfDetails: QaSampleSubmissionEntity,
            user: UsersEntity,
            map: ServiceMapsEntity
    ): Pair<ServiceRequestsEntity, QaSampleSubmissionEntity> {

        var sr = commonDaoServices.createServiceRequest(map)
        var saveSSF = ssfDetails
        try {

            with(saveSSF) {
                cdItemId = cdItemDetails.id
                status = map.activeStatus
                labResultsStatus = map.inactiveStatus
                createdBy = commonDaoServices.concatenateName(user)
                createdOn = commonDaoServices.getTimestamp()
            }


            saveSSF = SampleSubmissionRepo.save(saveSSF)

            sr.payload = "New SSF Saved [BRAND name${saveSSF.brandName} and ${saveSSF.id}]"
            sr.names = "${saveSSF.brandName}"
            sr.varField1 = cdItemDetails.id.toString()

            sr.responseStatus = sr.serviceMapsId?.successStatusCode
            sr.responseMessage = "Success ${sr.payload}"
            sr.status = map.successStatus
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
        return Pair(sr, saveSSF)
    }

    fun ssfUpdateDetails(
            cdItemDetails: CdItemDetailsEntity,
            ssfDetails: QaSampleSubmissionEntity,
            user: UsersEntity,
            map: ServiceMapsEntity
    ): Pair<ServiceRequestsEntity, QaSampleSubmissionEntity> {

        var sr = commonDaoServices.createServiceRequest(map)
        var saveSSF = findSampleSubmittedBYCdItemID(cdItemDetails.id ?: throw Exception("MISSING ITEM ID"))
        try {

            with(saveSSF) {
                resultsAnalysis = ssfDetails.resultsAnalysis
                modifiedBy = commonDaoServices.concatenateName(user)
                modifiedOn = commonDaoServices.getTimestamp()
            }

            saveSSF = SampleSubmissionRepo.save(saveSSF)

            sr.payload = "New SSF Saved [BRAND name${saveSSF.brandName} and ${saveSSF.id}]"
            sr.names = "${saveSSF.brandName}"
            sr.varField1 = cdItemDetails.id.toString()

            sr.responseStatus = sr.serviceMapsId?.successStatusCode
            sr.responseMessage = "Success ${sr.payload}"
            sr.status = map.successStatus
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
        return Pair(sr, saveSSF)
    }

    fun checkHasVehicle(cd: ConsignmentDocumentDetailsEntity): String? {
        try {
            var item = iCdItemsRepo.findFirstByCdDocIdAndChassisNumberIsNotNull(cd)
            if (item.isPresent) {
                return item.get().chassisNumber
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Not Vehicle with", ex)
        }
        return null
    }

    fun findVehicleFromCd(cd: ConsignmentDocumentDetailsEntity): CdItemDetailsEntity? {
        try {
            var item = iCdItemsRepo.findFirstByCdDocIdAndChassisNumberIsNotNull(cd)
            if (item.isPresent) {
                return item.get()
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Not Vehicle with", ex)
        }
        return null
    }

    fun updateConsignmentCorDetails(cdDetailsEntity: ConsignmentDocumentDetailsEntity, cocType: String?, documentCode: String, corsBakEntity: CorsBakEntity?) {
        var documentType = CdTypeCodes.COR.code
        var isCosGood = false
        corsBakEntity?.let {
            cdDetailsEntity.docTypeId = it.id
            it.id
        } ?: run {
            isCosGood = this.handleNoCorFromCosWithPvoc(cdDetailsEntity)
            null
        }
        // COC Type
        when (cocType) {
            "F" -> {
                documentType = CdTypeCodes.FOREIGN_COR.code
            }
            "L" -> {
                if (isCosGood) {
                    documentType = CdTypeCodes.PVOC_VEHICLES.code
                } else {
                    documentType = CdTypeCodes.COR.code
                }
            }
        }
        // Set CD type
        cdDetailsEntity.cdType = findCdTypeDetailsWithName(documentType)
    }

    fun updateConsignmentCocDetails(cdDetailsEntity: ConsignmentDocumentDetailsEntity, localCocType: String?, documentCode: String, cocEntity: CocsEntity?) {
        var documentType = CdTypeCodes.COC.code
        cocEntity?.let {
            cdDetailsEntity.docTypeId = it.id
        }

        // PVOC countries
        var isCosGood = false
        cocEntity?.let {
            cdDetailsEntity.docTypeId = it.id
            it.id
        } ?: run {
            isCosGood = this.handleNoCorFromCosWithPvoc(cdDetailsEntity)
            null
        }
        // Document types
        when (localCocType) {
            "F" -> {
                documentType = CdTypeCodes.FOREIGN_COC.code
            }
            "L" -> {
                when (cocEntity?.clean ?: "") {
                    "N" -> {
                        documentType = CdTypeCodes.NCR.code
                    }
                    else -> {
                        if (isCosGood) {
                            documentType = CdTypeCodes.PVOC_GOODS.code
                        } else {
                            documentType = CdTypeCodes.COC.code
                        }
                    }
                }
            }
        }
        // Set CD type
        cdDetailsEntity.cdType = findCdTypeDetailsWithName(documentType)
    }

    fun updateCDDetailsWithCdType(
            documentCode: String,
            cdDetailsEntity: ConsignmentDocumentDetailsEntity
    ): ConsignmentDocumentDetailsEntity {
        val ucrNumber = cdDetailsEntity.ucrNumber ?: ""
        val chassisNumber = checkHasVehicle(cdDetailsEntity)
        // Add Local COC Type
        cdDetailsEntity.cdStandardsTwo?.localCocType
                ?.let {
                    cdDetailsEntity.cdCocLocalTypeId = findLocalCocTypeWithCocTypeCode(it).id
                }
        KotlinLogging.logger { }.info("Map CD Type with Chassis Number: $chassisNumber")
        //  Add CD Type to Document
        when (documentCode) {
            "TIMP" -> {
                // Temporary imports
                if (StringUtils.hasLength(chassisNumber)) {
                    cdDetailsEntity.cdType = findCdTypeDetailsWithName(CdTypeCodes.TEMPORARY_IMPORTS.code)
                } else {
                    cdDetailsEntity.cdType = findCdTypeDetailsWithName(CdTypeCodes.TEMPORARY_IMPORT_VEHICLES.code)
                }
            }
            "AG" -> {
                // Auction Goods
                if (StringUtils.hasLength(chassisNumber)) {
                    cdDetailsEntity.cdType = findCdTypeDetailsWithName(CdTypeCodes.AUCTION_VEHICLE.code)
                } else {
                    cdDetailsEntity.cdType = findCdTypeDetailsWithName(CdTypeCodes.AUCTION_GOODS.code)
                }
            }
            "DG", "DMSG", "UPERR", "EXE", "MWG", "EAC", "RIMP" -> {
                // goods Exempted from COC
                if (StringUtils.hasLength(chassisNumber)) {
                    cdDetailsEntity.cdType = findCdTypeDetailsWithName(CdTypeCodes.EXEMPTED_VEHICLES.code)
                } else {
                    cdDetailsEntity.cdType = findCdTypeDetailsWithName(CdTypeCodes.EXEMPTED_GOODS.code)
                }
            }
            "CGD" -> {
                cdDetailsEntity.cdType = findCdTypeDetailsWithName(CdTypeCodes.COURIER_GOODS.code)
            }
            else ->
                when {
                    StringUtils.hasLength(chassisNumber) -> {
                        KotlinLogging.logger { }.info("Map COR")
                        // COR, NO_COR_PVOC or NO_COR Goods
                        this.updateConsignmentCorDetails(cdDetailsEntity, cdDetailsEntity.cdStandardsTwo?.cocType, documentCode, corsBakRepository.findByChasisNumber(chassisNumber!!))
                    }
                    else -> {
                        KotlinLogging.logger { }.info("Map COC")
                        // COC, NCR or NO_COC Goods
                        this.updateConsignmentCocDetails(cdDetailsEntity, cdDetailsEntity.cdStandardsTwo?.cocType, documentCode, findCocByUcrNumber(ucrNumber))
                    }
                }
        }
        return iConsignmentDocumentDetailsRepo.save(cdDetailsEntity)
    }

    fun timeStampToJavaSql(ts: Timestamp?): java.sql.Date {
        if (ts != null) {
            return java.sql.Date.valueOf(ts.toLocalDateTime().toLocalDate())
        }
        return java.sql.Date.valueOf(LocalDate.now())
    }


    fun updateCDDetailsWithForeignDocuments(cdDetailsEntity: ConsignmentDocumentDetailsEntity): ConsignmentDocumentDetailsEntity {
        KotlinLogging.logger { }.info("Map Foreign Documents to consignment: ${cdDetailsEntity.ucrNumber}")
        // Attach COC
        this.cocRepo.findByUcrNumberAndCocType(cdDetailsEntity.ucrNumber
                ?: "", ConsignmentCertificatesIssues.COC.nameDesc)?.let { coc ->
            KotlinLogging.logger { }.info("Map Foreign COC Documents to consignment: ${cdDetailsEntity.ucrNumber}")
            cdDetailsEntity.cocNumber = coc.cocNumber
            cdDetailsEntity.localCocOrCorStatus = 1
            cdDetailsEntity.localCocOrCorDate = timeStampToJavaSql(coc.cocIssueDate)
            cdDetailsEntity.localCocOrCorRemarks = "Foreign COC document"
            coc.consignmentDocId = cdDetailsEntity
            cocRepo.save(coc)
        }
        // Attach NCR
        this.cocRepo.findByUcrNumberAndCocType(cdDetailsEntity.ucrNumber
                ?: "", ConsignmentCertificatesIssues.NCR.nameDesc)?.let { ncr ->
            KotlinLogging.logger { }.info("Map Foreign NCR Documents to consignment: ${cdDetailsEntity.ucrNumber}")
            cdDetailsEntity.ncrNumber = ncr.cocNumber
            cdDetailsEntity.localCocOrCorStatus = 1
            cdDetailsEntity.localCocOrCorDate = timeStampToJavaSql(ncr.cocIssueDate)
            cdDetailsEntity.localCocOrCorRemarks = "Foreign COC/NCR document"
            ncr.consignmentDocId = cdDetailsEntity
            cocRepo.save(ncr)
        }
        // Attach COI
        this.cocRepo.findByUcrNumberAndCocType(cdDetailsEntity.ucrNumber
                ?: "", ConsignmentCertificatesIssues.COI.nameDesc)?.let { coi ->
            KotlinLogging.logger { }.info("Map Foreign COI Documents to consignment: ${cdDetailsEntity.ucrNumber}")
            cdDetailsEntity.localCoi = 1
            cdDetailsEntity.localCocOrCorStatus = 0
            cdDetailsEntity.cocNumber = coi.coiNumber
            cdDetailsEntity.localCocOrCorDate = timeStampToJavaSql(coi.cocIssueDate)
            cdDetailsEntity.localCoiRemarks = "Foreign COC document"
            coi.consignmentDocId = cdDetailsEntity
            cocRepo.save(coi)
        }
        // Attach COR
        findVehicleFromCd(cdDetailsEntity)?.let { vehicle ->
            this.corsBakRepository.findByChasisNumber(vehicle.chassisNumber ?: "")?.let { cor ->
                KotlinLogging.logger { }.info("Map Foreign COR Documents to consignment: ${cdDetailsEntity.ucrNumber}")
                cdDetailsEntity.localCocOrCorStatus = 1
                cdDetailsEntity.corNumber = cor.corNumber
                cdDetailsEntity.localCocOrCorDate = timeStampToJavaSql(cor.corIssueDate)
                cdDetailsEntity.localCocOrCorRemarks = "Foreign COR document"
                // Update COR link
                cor.consignmentDocId = cdDetailsEntity
                corsBakRepository.save(cor)
            }
        }
        return iConsignmentDocumentDetailsRepo.save(cdDetailsEntity)

    }

    fun findCOC(ucrNumber: String, docType: String): CocsEntity {
        cocRepo.findByUcrNumberAndCocType(ucrNumber, docType.toUpperCase())
                ?.let { cocEntity ->
                    return cocEntity
                }
                ?: throw Exception(docType.toUpperCase() + " Details with the following UCR NUMBER = ${ucrNumber}, does not Exist")
    }

    fun findCOCById(cocId: Long): CocsEntity? {
        return cocRepo.findByIdOrNull(cocId)
    }

    fun findCocByUcrNumber(ucrNumber: String): CocsEntity? {
        return cocRepo.findByUcrNumberAndCocType(ucrNumber, "COC")
    }

    fun findCoiByUcrNumber(ucrNumber: String): CocsEntity? {
        return cocRepo.findByUcrNumberAndCocType(ucrNumber, "COI")
    }

    fun findCdTypeDetails(cdTypeID: Long): ConsignmentDocumentTypesEntity {
        cdTypesRepo.findByIdOrNull(cdTypeID)
                ?.let { cdTypeDetails ->
                    return cdTypeDetails
                }
                ?: throw Exception("CD Type Details with the following ID = ${cdTypeID}, does not Exist")
    }

    fun findCdTypeDetailsWithUuid(uuid: String): ConsignmentDocumentTypesEntity {
        cdTypesRepo.findByUuid(uuid)
                ?.let { cdTypeDetails ->
                    return cdTypeDetails
                }
                ?: throw Exception("CD Type Details with the following uuid = ${uuid}, does not Exist")
    }

    fun findCdTypeDetailsWithName(name: String): ConsignmentDocumentTypesEntity {
        cdTypesRepo.findFirstByVarField1(name)
                ?.let { cdTypeDetails ->
                    return cdTypeDetails
                }
                ?: throw Exception("CD Type Details with the following name = ${name}, does not Exist")
    }

    fun findAllAvailableCdWithPortOfEntry(
            cfs: List<UsersCfsAssignmentsEntity>,
            cdType: ConsignmentDocumentTypesEntity?,
            statuses: List<Int?>,
            page: PageRequest
    ): Page<ConsignmentDocumentDetailsEntity> {
        val cfsIds = mutableListOf<Long>()
        cfs.forEach {
            it.cfsId?.let { it1 -> cfsIds.add(it1) }
        }
        return cdType?.let {
            iConsignmentDocumentDetailsRepo.findByFreightStation_IdInAndCdTypeAndAssignedInspectionOfficerIsNullAndOldCdStatusIsNullAndStatusIn(
                    cfsIds,
                    cdType,
                    statuses,
                    page
            )
        } ?: run {
            iConsignmentDocumentDetailsRepo.findByFreightStation_IdInAndAssignedInspectionOfficerIsNullAndOldCdStatusIsNullAndStatusIn(
                    cfsIds,
                    statuses,
                    page
            )
        }

    }


    fun findAllOngoingCdWithFreightStationID(
            cfsEntity: List<UsersCfsAssignmentsEntity>,
            cdType: ConsignmentDocumentTypesEntity?,
            statuses: List<Int?>,
            page: PageRequest
    ): Page<ConsignmentDocumentDetailsEntity> {
        val cfsIds = mutableListOf<Long>()
        cfsEntity.forEach {
            it.cfsId?.let { it1 -> cfsIds.add(it1) }
        }
        return cdType?.let {
            return iConsignmentDocumentDetailsRepo.findByFreightStation_IdInAndCdTypeAndUcrNumberIsNotNullAndOldCdStatusIsNullAndCompliantStatusIsNullAndApproveRejectCdStatusIsNull(
                    cfsIds,
                    cdType,
                    page)
        } ?: run {
            return iConsignmentDocumentDetailsRepo.findByFreightStation_IdInAndUcrNumberIsNotNullAndOldCdStatusIsNullAndCompliantStatusIsNullAndApproveRejectCdStatusIsNull(
                    cfsIds, page)
        }

    }


    fun findAllCdWithAssignedIoID(
            usersEntity: UsersEntity,
            cdType: ConsignmentDocumentTypesEntity?,
            statuses: List<Int?>,
            page: PageRequest
    ): Page<ConsignmentDocumentDetailsEntity> {
        return cdType?.let {
            iConsignmentDocumentDetailsRepo.findAllByAssignedInspectionOfficerAndCdTypeAndUcrNumberIsNotNullAndOldCdStatusIsNullAndStatusIn(
                    usersEntity,
                    it,
                    statuses,
                    page
            )
        } ?: run {
            iConsignmentDocumentDetailsRepo.findAllByAssignedInspectionOfficerAndUcrNumberIsNotNullAndOldCdStatusIsNullAndStatusIn(
                    usersEntity,
                    statuses,
                    page
            )
        }
    }

    fun findAllCompleteCdWithAssignedIoID(
            usersEntity: UsersEntity,
            cdType: ConsignmentDocumentTypesEntity?,
            statuses: List<Int>,
            page: PageRequest
    ): Page<ConsignmentDocumentDetailsEntity> {
        return cdType?.let {
            iConsignmentDocumentDetailsRepo.findAllByAssignedInspectionOfficerAndCdTypeAndUcrNumberIsNotNullAndOldCdStatusIsNullAndStatusIn(
                    usersEntity,
                    it,
                    statuses,
                    page
            )
        } ?: run {
            iConsignmentDocumentDetailsRepo.findAllByAssignedInspectionOfficerAndUcrNumberIsNotNullAndOldCdStatusIsNullAndStatusIn(
                    usersEntity,
                    statuses,
                    page
            )
        }
    }

    fun findAllCompleteCdWithAssigner(
            usersEntity: UsersEntity,
            cdType: ConsignmentDocumentTypesEntity?,
            statuses: List<Int>,
            page: PageRequest
    ): Page<ConsignmentDocumentDetailsEntity> {
        return cdType?.let {
            iConsignmentDocumentDetailsRepo.findAllByAssignerAndCdTypeAndUcrNumberIsNotNullAndOldCdStatusIsNullAndStatusIn(
                    usersEntity,
                    it,
                    statuses,
                    page
            )
        } ?: run {
            iConsignmentDocumentDetailsRepo.findAllByAssignerAndUcrNumberIsNotNullAndOldCdStatusIsNullAndStatusIn(
                    usersEntity,
                    statuses,
                    page
            )
        }
    }

    fun findAllInspectionsCdWithAssigner(
            usersEntity: UsersEntity,
            cdType: ConsignmentDocumentTypesEntity?,
            statuses: List<Int>,
            page: PageRequest
    ): Page<ConsignmentDocumentDetailsEntity> {
        return cdType?.let {
            iConsignmentDocumentDetailsRepo.findAllByAssignerAndCdTypeAndUcrNumberIsNotNullAndOldCdStatusIsNullAndStatusIn(
                    usersEntity,
                    it,
                    statuses,
                    page
            )
        } ?: run {
            iConsignmentDocumentDetailsRepo.findAllByAssignerAndUcrNumberIsNotNullAndOldCdStatusIsNullAndStatusIn(
                    usersEntity,
                    statuses,
                    page
            )
        }
    }


    fun findUserById(officerId: Long?): Optional<UsersEntity> {
        if (officerId != null) {
            return iUserRepository.findById(officerId)
        }
        return Optional.empty()
    }

    fun findOfficersList(consignmentDocumentDetailsEntity: ConsignmentDocumentDetailsEntity): List<UserProfilesEntity> {
        consignmentDocumentDetailsEntity.freightStation
                ?.let { subSectionsLevel2Entity ->
                    iUserProfilesRepo.findBySubSectionL2IdAndStatus(
                            commonDaoServices.findSectionLevel2WIthId(
                                    subSectionsLevel2Entity.id
                            ), commonDaoServices.activeStatus.toInt()
                    )
                            ?.let { userProfileList ->
                                return userProfileList
                            }
                            ?: throw ServiceMapNotFoundException("Users with Freight Station with ID = $subSectionsLevel2Entity and Status = ${commonDaoServices.activeStatus}, Does not exist")
                }
                ?: throw ServiceMapNotFoundException("Freight Station details on consignment with ID = ${consignmentDocumentDetailsEntity.id}, is Empty")
    }


    fun findOfficersList(freightStation: CfsTypeCodesEntity?, designationId: Long): List<UserProfilesEntity> {
        freightStation?.let { fs ->

            val profilesAssignment = findByCFSId(fs.id)
            val userProfiles = mutableListOf<Long>()
            profilesAssignment.forEach { p ->
                userProfiles.add(p.userProfileId!!)
            }
            return iUserProfilesRepo.findByIdInAndDesignationId_IdAndStatus(userProfiles, designationId, 1)
        } ?: throw ServiceMapNotFoundException("Freight Station details on consignment with ID = null, is Empty")
    }

    fun findOfficersInMyCfsList(user: UserProfilesEntity, designationId: Long): List<UserProfilesEntity> {

        val profilesAssignment = findAllCFSUserList(user.id!!)
        KotlinLogging.logger { }.info("USR CFS COUNT: ${profilesAssignment.size}-${user.id}")
        // CFS in my profile
        val cfsIds = mutableListOf<Long>()
        profilesAssignment.forEach { p ->
            cfsIds.add(p.cfsId!!)
        }
        // Profiles in my CFS
        val profilIds = mutableListOf<Long>()
        usersCfsRepo.findAllByCfsIdInAndStatus(cfsIds.distinct(), 1).forEach {
            profilIds.add(it.userProfileId!!)
        }
        // Users
        val userProfiles = iUserProfilesRepo.findByIdInAndDesignationId_IdAndStatus(profilIds.distinct(), designationId, 1)
        KotlinLogging.logger { }.info("USR COUNT: ${userProfiles.size}")
        return userProfiles
    }

    fun convertEntityToJsonObject(entityToConvert: Any): Any {
        return JSONObject(entityToConvert)
    }


    fun removeKeyAndUpdateValueJsonObject(jsonObject: JSONObject, keyRemove: String, valueUpdate: Any): JSONObject {
        with(jsonObject) {
            remove(keyRemove)
            put(keyRemove, valueUpdate)
        }
        return jsonObject
    }


    fun updateCdItemDetailsInDB(updateCDItem: CdItemDetailsEntity, user: UsersEntity?): CdItemDetailsEntity {
        user?.let {
            updateCDItem.lastModifiedBy = commonDaoServices.getUserName(it)
        }
        updateCDItem.lastModifiedOn = commonDaoServices.getTimestamp()
        KotlinLogging.logger { }.debug("MY UPDATED ITEM ID =  ${updateCDItem.id}")
        return iCdItemsRepo.save(updateCDItem)
    }

    fun updateCdItemSampleParamDetailsInDB(
            sampleSubmitParamEntity: CdSampleSubmissionParamatersEntity,
            user: UsersEntity
    ): CdSampleSubmissionParamatersEntity {
        with(sampleSubmitParamEntity) {
            modifiedBy = commonDaoServices.getUserName(user)
            modifiedOn = commonDaoServices.getTimestamp()
        }
        KotlinLogging.logger { }
                .info { "MY UPDATED sample SubmitParam Entity with ID =  ${sampleSubmitParamEntity.id}" }
        return iSampleSubmissionParamRepo.save(sampleSubmitParamEntity)
    }

    fun findAllCFSUserCodes(userProfileID: Long): List<String> {
        return usersCfsRepo.findAllUserCfsCodes(userProfileID)
    }

    fun countCFSUserCodes(userProfileID: Long, cfsId: Long): Long {
        return usersCfsRepo.countUserCfsCodes(userProfileID, cfsId)
    }

    fun findAllCFSUserList(userProfileID: Long): List<UsersCfsAssignmentsEntity> {
        usersCfsRepo.findByUserProfileId(userProfileID)
                ?.let {
                    return it
                }
                ?: throw ServiceMapNotFoundException("NO USER CFS FOUND WITH PROFILE ID = ${userProfileID}")
    }

    fun findByCFSId(cfsId: Long): List<UsersCfsAssignmentsEntity> {
        usersCfsRepo.findAllByCfsId(cfsId)
                ?.let {
                    return it
                }
                ?: throw ServiceMapNotFoundException("NO USER CFS FOUND WITH PROFILE ID = ${cfsId}")
    }

    fun findSavedChecklist(itemId: Long): CdInspectionChecklistEntity {
        iCdInspectionChecklistRepo.findByItemId(itemId)
                ?.let { checklist ->
                    return checklist
                }
                ?: throw ServiceMapNotFoundException("CheckList for ITEM with ID = ${itemId}, Does not exist")
    }


    fun findSavedSampleCollection(itemId: Long): CdSampleCollectionEntity {
        iSampleCollectRepo.findByItemId(itemId)
                ?.let { sampleCollect ->
                    return sampleCollect
                }
                ?: throw ServiceMapNotFoundException("Sample Collection for ITEM with ID = ${itemId}, Does not exist")
    }

    fun findSavedSampleSubmission(itemId: Long): CdSampleSubmissionItemsEntity {
        iSampleSubmitRepo.findByItemId(itemId)
                ?.let { sampleSubmitted ->
                    return sampleSubmitted
                }
                ?: throw ServiceMapNotFoundException("Sample Submitted for ITEM with ID = ${itemId}, Does not exist")
    }

    fun findSavedSampleSubmissionById(sampleSubmissionId: Long): CdSampleSubmissionItemsEntity {
        iSampleSubmitRepo.findByIdOrNull(sampleSubmissionId)
                ?.let { sampleSubmitted ->
                    return sampleSubmitted
                }
                ?: throw ServiceMapNotFoundException("Sample Submitted with ID = ${sampleSubmissionId}, Does not exist")
    }

    fun sendSamplesToRespectiveLabs(
            sampleSubmitEntity: CdSampleSubmissionItemsEntity,
            remarksValue: String,
            user: UsersEntity,
            map: ServiceMapsEntity,
            sampleParams: List<CdSampleSubmissionParamatersEntity>
    ): CdSampleSubmissionItemsEntity {
        //Todo: function for sending To LIMS to give Back BS Number
        val mySampleSubmittedToLab = sendBSNumber(sampleSubmitEntity, user, map)

        sampleParams.forEach { sampleParam ->
//            var mySampleParam = sampleParam
            with(sampleParam) {
                remarks = remarksValue
            }

            updateCdItemSampleParamDetailsInDB(sampleParam, user)
            //Todo: function for sending To Respective labs
            //Simulation Of Getting Lab results
//            //Todo: Receiving Lab Results
//            mySampleParam = simulateLabReportResults(mySampleParam, map)

//            mySampleParam = updateCdItemSampleParamDetailsInDB(mySampleParam, user)
//            updateCdItemSampleParamDetailsInDB(mySampleParam, user)
        }

        return mySampleSubmittedToLab
    }


    fun sendLabEmail(recipientEmail: String, paramatersEntity: CdSampleSubmissionParamatersEntity): Boolean {
        val subject = "LAB RESULTS"
        val messageBody =
                "Please Find The attached Lab Results of the samples collected and submitted for testing  \n" +
                        "\n " +
                        "https://localhost:8006/api/di/item/sample-Submit-param/bs-number?cdSampleSubmitID=${paramatersEntity.sampleSubmissionId?.id}&docType=${sampSubmitName}&itemID=${paramatersEntity.sampleSubmissionId?.itemId}&message=${labResults}"
        notifications.sendEmail(recipientEmail, subject, messageBody)
        return true
    }


    fun addCDByCFSToList() {

    }

    fun sendBSNumber(
            sampleSubmitEntity: CdSampleSubmissionItemsEntity,
            user: UsersEntity,
            map: ServiceMapsEntity
    ): CdSampleSubmissionItemsEntity {
        with(sampleSubmitEntity) {
            bsNumber = generatedBsNumberUpdate(map)
        }
//        val sampleWithBsNumber = updateSampleSubmitDetails(sampleSubmitEntity, user, map)
        return updateSampleSubmitDetails(sampleSubmitEntity, user, map)

//        commonDaoServices.sendEmailWithUserEntity(hod, applicationMapProperties.mapMsComplaintSubmittedHodNotification, complaint, map, sr)
//        mapDIBsNumberNotification
//        val subject = "LAB BS NUMBER"
//        val messageBody = "Please attach the BS NUMBER below for sample with parameter with ID = ${paramatersEntity.id} \n" +
//                "\n" +
//                " BS NUMBER = ${"BS${generateRandomText(5, s.secureRandom, s.messageDigestAlgorithm, false)}".toUpperCase()}.................  \n" +
//                "\n" +
//                " https://localhost:8006/api/di/item/sample-Submit-param/bs-number?cdSampleSubmitID=${paramatersEntity.sampleSubmissionId?.id}&docType=${sampSubmitName}&itemID=${paramatersEntity.sampleSubmissionId?.itemId}&message=${bsNumber}"
//        notifications.sendEmail(recipientEmail, subject, messageBody)
    }

    fun generatedBsNumberUpdate(map: ServiceMapsEntity): String {
        return "BS#${generateRandomText(5, map.secureRandom, map.messageDigestAlgorithm, false).toUpperCase()}"
    }

    fun bsNumberLabDTOResultsEmailCompose(
            sampleSubmittedToLab: CdSampleSubmissionItemsEntity,
            updatedItemDetails: CdItemDetailsEntity,
            docType: String
    ): BSNumberLabDTO {
        val bsNumberLabDto = BSNumberLabDTO()
        with(bsNumberLabDto) {
            bsNumber = sampleSubmittedToLab.bsNumber
            sampleRefNumber = sampleSubmittedToLab.sampleRefNumber
            docTypeDetail = docType
            date = sampleSubmittedToLab.modifiedOn
            baseUrl = applicationMapProperties.baseUrlValue
            uuid = updatedItemDetails.uuid
            fullName =
                    updatedItemDetails.cdDocId?.assignedInspectionOfficer?.let { commonDaoServices.concatenateName(it) }
        }
        return bsNumberLabDto
    }

    fun inspectionReportApprovalDTOEmailCompose(
            itemDetails: CdItemDetailsEntity,
            generalReportInspection: CdInspectionGeneralEntity
    ): InspectionReportApprovalDTO {
        val inspectionReportApprovalDto = InspectionReportApprovalDTO()
        with(inspectionReportApprovalDto) {
            refNumber = generalReportInspection.inspectionReportRefNumber
            baseUrl = applicationMapProperties.baseUrlValue
            uuid = itemDetails.uuid
            fullName = itemDetails.cdDocId?.assigner?.let { commonDaoServices.concatenateName(it) }
        }

        return inspectionReportApprovalDto
    }

    fun inspectionReportApprovedDTOEmailCompose(
            itemDetails: CdItemDetailsEntity,
            generalReportInspection: CdInspectionGeneralEntity
    ): InspectionReportApprovedDTO {
        val inspectionReportApprovedDto = InspectionReportApprovedDTO()
        with(inspectionReportApprovedDto) {
            refNumber = generalReportInspection.inspectionReportRefNumber
            commentRemarks = generalReportInspection.inspectionReportApprovalComments
            date = generalReportInspection.inspectionReportApprovalDate
            baseUrl = applicationMapProperties.baseUrlValue
            uuid = itemDetails.uuid
            fullName = itemDetails.cdDocId?.assignedInspectionOfficer?.let { commonDaoServices.concatenateName(it) }
        }

        return inspectionReportApprovedDto
    }

    fun inspectionReportDisApprovedDTOEmailCompose(
            itemDetails: CdItemDetailsEntity,
            generalReportInspection: CdInspectionGeneralEntity
    ): InspectionReportDisApprovedDTO {
        val inspectionReportDisApprovedDto = InspectionReportDisApprovedDTO()
        with(inspectionReportDisApprovedDto) {
            refNumber = generalReportInspection.inspectionReportRefNumber
            commentRemarks = generalReportInspection.inspectionReportDisapprovalComments
            date = generalReportInspection.inspectionReportDisapprovalDate
            baseUrl = applicationMapProperties.baseUrlValue
            uuid = itemDetails.uuid
            fullName = itemDetails.cdDocId?.assignedInspectionOfficer?.let { commonDaoServices.concatenateName(it) }
        }

        return inspectionReportDisApprovedDto
    }

    fun sampleResultsLabDTOResultsEmailCompose(
            sampleSubmitParam: CdSampleSubmissionParamatersEntity,
            sampleSubmitted: CdSampleSubmissionItemsEntity,
            updatedItemDetails: CdItemDetailsEntity,
            docType: String
    ): SampleResultsLabDTO {
        val sampleResultsLabDTO = SampleResultsLabDTO()
        with(sampleResultsLabDTO) {
            id = sampleSubmitParam.id
            parameterName = sampleSubmitParam.parameterName
            labName = sampleSubmitParam.laboratoryId?.labName
            sampleRefNumber = sampleSubmitted.sampleRefNumber
            docTypeDetail = docType
            date = sampleSubmitted.modifiedOn
            baseUrl = applicationMapProperties.baseUrlValue
            uuid = updatedItemDetails.uuid
            fullName =
                    updatedItemDetails.cdDocId?.assignedInspectionOfficer?.let { commonDaoServices.concatenateName(it) }
        }
        return sampleResultsLabDTO
    }

    fun simulateLabReportSendingResults(
            sampleSubmitParameters: List<CdSampleSubmissionParamatersEntity>,
            map: ServiceMapsEntity,
            user: UsersEntity,
            sampleSubmitted: CdSampleSubmissionItemsEntity
    ): Boolean {
        sampleSubmitParameters.forEach { sampleParam ->
            //Update Lab With Simulated Results
            var mySampleParam = simulateLabReportResults(sampleParam, map)

            mySampleParam = updateCdItemSampleParamDetailsInDB(mySampleParam, user)
            //Methode For sending Email For Lab Results
            simulateEmailLabResults(mySampleParam, map, user)
        }

        return true
    }

    fun checkIfResultsCompleted(
            sampleSubmitParameters: List<CdSampleSubmissionParamatersEntity>,
            map: ServiceMapsEntity
    ): Int {
        var resultsNumber = 0
        val sampleParamNumber = sampleSubmitParameters.size
        sampleSubmitParameters.forEach { sampleParam ->
            if (sampleParam.labResultCompleteStatus == map.activeStatus) {
                resultsNumber++
            }
        }

        return when (resultsNumber) {
            sampleParamNumber -> {
                map.activeStatus
            }
            else -> {
                map.inactiveStatus
            }
        }

    }

    fun simulateEmailLabResults(
            sampleParam: CdSampleSubmissionParamatersEntity,
            map: ServiceMapsEntity,
            user: UsersEntity
    ) {
        val payload =
                "Updated details with Lab Results [${this::simulateEmailLabResults.name} with Item ID =[${sampleParam.id}]]"
        val sr = commonDaoServices.mapServiceRequestForSuccess(map, payload, user)
        //Creation Of Email DTO for sending Lab results
        val item = sampleParam.sampleSubmissionId?.itemId?.let { findItemWithItemID(it) }
        if (item != null) {
            val sampleResultsLabDTO = sampleParam.sampleSubmissionId?.let {
                sampleResultsLabDTOResultsEmailCompose(sampleParam, it, item, labResults)
            }
            item.cdDocId?.assignedInspectionOfficer?.let {
                if (sampleResultsLabDTO != null) {
                    // commonDaoServices.sendEmailWithUserEntity(it, applicationMapProperties.mapDILabResultsNotification, sampleResultsLabDTO, map, sr)
                }
            }
        }


    }

    fun simulateLabReportResults(
            sampleSubmitParamEntity: CdSampleSubmissionParamatersEntity,
            s: ServiceMapsEntity
    ): CdSampleSubmissionParamatersEntity {
        with(sampleSubmitParamEntity) {
            lod = "test lod"
            reportUid = commonDaoServices.generateUUIDString()
            results = 13.5.toString()
            testMethodNo = "KS 02-17"
            requirements = "Test 15Max"
            labResultStatus = s.activeStatus
            labResultCompleteStatus = s.inactiveStatus
        }
        return sampleSubmitParamEntity
    }

    fun findListSampleSubmissionParameter(sampleSubmission: CdSampleSubmissionItemsEntity): List<CdSampleSubmissionParamatersEntity> {
        iSampleSubmissionParamRepo.findBySampleSubmissionId(sampleSubmission)
                ?.let { sampleSubmittedParameter ->
                    return sampleSubmittedParameter
                }
                ?: throw ServiceMapNotFoundException("Parameters with for ITEM with ID = ${sampleSubmission.id}, Does not exist")
    }

    fun findSavedSampleSubmissionParameter(paramId: Long): CdSampleSubmissionParamatersEntity {
        iSampleSubmissionParamRepo.findByIdOrNull(paramId)
                ?.let { sampleSubmittedParameter ->
                    return sampleSubmittedParameter
                }
                ?: throw ServiceMapNotFoundException("Parameter with ID = ${paramId}, Does not exist")
    }

    fun sendMotorVehicleInspectionRequestEmail(recipientEmail: String, paramatersEntity: CdItemDetailsEntity): Boolean {
        val subject = "Motor Vehicle Inspection Request"
        val messageBody = "Please Find The attached motor vehicle details submitted for inspection  \n" +
                "\n " +
                "https://localhost:8006/api/di/ministry-submission/inspect?itemID=${paramatersEntity.id}"
        notifications.sendEmail(recipientEmail, subject, messageBody)
        return true
    }

    fun sendMinistryInspectionReportSubmittedEmail(
            recipientEmail: String,
            paramatersEntity: CdItemDetailsEntity
    ): Boolean {
        val subject = "Motor Vehicle Inspection Report"
        val messageBody =
                "Motor vehicle inspection report has been submitted from the ministry. Click the link to view it:  \n" +
                        "\n " +
                        "https://localhost:8006/api/di/cd-item-details?cdItemUuid=${paramatersEntity.id}"
        notifications.sendEmail(recipientEmail, subject, messageBody)
        return true
    }

    fun getVersionCount(ucrNumber: String): Long {
        return iConsignmentDocumentDetailsRepo.countByUcrNumber(ucrNumber)
    }

    fun updateCdDetailsInDB(
            updateCD: ConsignmentDocumentDetailsEntity,
            user: UsersEntity?
    ): ConsignmentDocumentDetailsEntity {
        if (user != null) {
            updateCD.apply {
                modifiedBy = commonDaoServices.getUserName(user)
                modifiedOn = commonDaoServices.getTimestamp()
            }
        } else {
            updateCD.apply {
                modifiedOn = commonDaoServices.getTimestamp()
            }
        }
        KotlinLogging.logger { }.info { "MY UPDATED CD ID =  ${updateCD.id}" }
        return iConsignmentDocumentDetailsRepo.save(updateCD)
    }

    fun updateCDDetails(
            cdDetails: ConsignmentDocumentDetailsEntity,
            cdId: Long,
            user: UsersEntity,
            s: ServiceMapsEntity
    ): ConsignmentDocumentDetailsEntity {
        // Getting an Object with fields that user Has Updated that are needed to be updated to the database
        JSONObject(ObjectMapper().writeValueAsString(cdDetails))
                .let { addValues ->
                    // Creating of a json object that can be user to map the details from Database with the updated fields from user
                    JSONObject(ObjectMapper().writeValueAsString(findCD(cdId)))
                            .let { JCD ->
                                // Looping each field of the updated Entity to be updated
                                for (key in addValues.keys()) {
                                    key.let { keyStr ->
                                        // Checks if the field with the following Key is null or not Null (meaning it is the field that is updated)
                                        when {
                                            addValues.isNull(keyStr) -> {
                                                KotlinLogging.logger { }
                                                        .info { "MY null values key: $keyStr value: ${addValues.get(keyStr)}" }
                                            }
//                                            keyStr.equals("confirmAssignedUserId").and(addValues.get(keyStr) != null) -> {
//                                                KotlinLogging.logger { }.info { "My values key: $keyStr value: ${addValues.get(keyStr)}" }
//
//                                                removeKeyAndUpdateValueJsonObject(JCD,"assignedInspectionOfficer", convertEntityToJsonObject(commonDaoServices.findUserByID(addValues.getLong(keyStr))))
//                                                KotlinLogging.logger { }.info { "My values key: $keyStr value: ${addValues.get(keyStr)}" }
//                                            }
                                            // Field with Key Have values so we will update the  database value with the updated one
                                            else -> {
                                                removeKeyAndUpdateValueJsonObject(JCD, keyStr, addValues.get(keyStr))
                                                KotlinLogging.logger { }
                                                        .info { "My values key: $keyStr value: ${addValues.get(keyStr)}" }
                                            }
                                        }
                                    }
                                }
                                // Change the JCD to an Entity to be saved
                                ObjectMapper().readValue(JCD.toString(), ConsignmentDocumentDetailsEntity::class.java)
                                        .let { updateCD ->
                                            return updateCdDetailsInDB(updateCD, user)
                                        }

                            }
                }
    }

    fun checkIfChecklistUndergoesSampling(
            sampled: String,
            compliant: String,
            item: CdItemDetailsEntity,
            map: ServiceMapsEntity
    ): CdItemDetailsEntity? {
        var updateItem = item
        updateItem.checklistStatus = map.activeStatus
        // Mark sampled or not sampled
        when (sampled) {
            "YES" -> {
                updateItem.sampledStatus = map.activeStatus
                updateItem.varField10 = "LAB RESULT PENDING"
                updateItem.allTestReportStatus = map.inactiveStatus
            }
            "NO" -> {
                updateItem.allTestReportStatus = map.activeStatus
                updateItem = updateItemNoSampling(item, map)
            }
        }
        // Mark compliant or non-compliant
        when (compliant) {
            "COMPLIANT" -> {
                updateItem.approveStatus = map.activeStatus
                updateItem.approveDate = Date(Date().time)
            }
            else -> {
                updateItem.approveStatus = map.invalidStatus
                updateItem.approveDate = Date(Date().time)
            }
        }
        // Update CD Item
        this.iCdItemsRepo.save(updateItem)
        return updateItem
    }

    fun updateItemNoSampling(item: CdItemDetailsEntity, map: ServiceMapsEntity): CdItemDetailsEntity {
        item.sampledStatus = map.inactiveStatus
        item.sampledCollectedStatus = map.inactiveStatus
        item.sampleBsNumberStatus = map.inactiveStatus
        item.sampleSubmissionStatus = map.inactiveStatus

        return item
    }


    fun updateCDItemDetails(
            cdItemDetails: CdItemDetailsEntity,
            cdItemId: Long,
            user: UsersEntity,
            s: ServiceMapsEntity
    ): CdItemDetailsEntity {
        // Getting an Object with fields that user Has Updated that are needed to be updated to the database
        JSONObject(ObjectMapper().writeValueAsString(cdItemDetails))
                .let { addValues ->
                    // Creating of a json object that can be user to map the details from Database with the updated fields from user
                    JSONObject(ObjectMapper().writeValueAsString(findItemWithItemID(cdItemId)))
                            .let { JCD ->
                                // Looping each field of the updated Entity to be updated
                                for (key in addValues.keys()) {
                                    key.let { keyStr ->
                                        // Checks if the field with the following Key is null or not Null (meaning it is the field that is updated)
                                        when {
                                            addValues.isNull(keyStr) -> {
                                                KotlinLogging.logger { }
                                                        .info { "MY null values key: $keyStr value: ${addValues.get(keyStr)}" }
                                            }
//                                            keyStr.equals("confirmAssignedUserId").and(addValues.get(keyStr) != null) -> {
//                                                KotlinLogging.logger { }.info { "My values key: $keyStr value: ${addValues.get(keyStr)}" }
//
//                                                removeKeyAndUpdateValueJsonObject(JCD,"assignedInspectionOfficer", convertEntityToJsonObject(commonDaoServices.findUserByID(addValues.getLong(keyStr))))
//                                                KotlinLogging.logger { }.info { "My values key: $keyStr value: ${addValues.get(keyStr)}" }
//                                            }
                                            // Field with Key Have values so we will update the  database value with the updated one
                                            else -> {
                                                removeKeyAndUpdateValueJsonObject(JCD, keyStr, addValues.get(keyStr))
                                                KotlinLogging.logger { }
                                                        .info { "My values key: $keyStr value: ${addValues.get(keyStr)}" }
                                            }
                                        }
                                    }
                                }
                                // Change the JCD to an Entity to be saved
                                ObjectMapper().readValue(JCD.toString(), CdItemDetailsEntity::class.java)
                                        .let { updateCDItem ->

                                            return updateCdItemDetailsInDB(updateCDItem, user)
                                        }

                            }
                }
    }


    fun updateCdInspectionMotorVehicleItemChecklistInDB(
            cdInspectionMotorVehicleItemChecklistEntity: CdInspectionMotorVehicleItemChecklistEntity,
            user: UsersEntity?
    ): CdInspectionMotorVehicleItemChecklistEntity {
        if (user != null) {
            with(cdInspectionMotorVehicleItemChecklistEntity) {
                modifiedBy = commonDaoServices.getUserName(user)
                modifiedOn = commonDaoServices.getTimestamp()
            }
        }
        KotlinLogging.logger { }
                .info { "CdInspectionMotorVehicleItemChecklistEntity UPDATED ITEM ID =  ${cdInspectionMotorVehicleItemChecklistEntity.id}" }
        return iCdInspectionMotorVehicleItemChecklistRepo.save(cdInspectionMotorVehicleItemChecklistEntity)
    }


    fun findAllDemandNotesWithSwPending(paymentStatus: Int): List<CdDemandNoteEntity> {
        return iDemandNoteRepo.findAllByPaymentStatusAndSwStatusInAndPaymentPurpose(paymentStatus, listOf(0, null), PaymentPurpose.CONSIGNMENT.code)
    }

    //Send CD status to KeSWS
    fun submitCDStatusToKesWS(pgaRemarks: String, status: String, version: String, cdDetails: ConsignmentDocumentDetailsEntity) {
        val current = LocalDateTime.now()
        val expiryDate = current.plusYears(1)

        val formatter = DateTimeFormatter.ofPattern("yyyyMMddhhmmss")
        val formatted = current.format(formatter)
        val formattedExipry = expiryDate.format(formatter)
        val messageDate = formatted

        cdDetails.cdStandard?.applicationRefNo?.let { cdNumber ->
            KotlinLogging.logger { }.info { "::::::::::::::::::: CD Ref Number = $cdNumber ::::::::::::::" }
            val docHeader = DocumentHeader(cdNumber, messageDate, version)
            val docDetails = DocumentDetails(cdNumber, formattedExipry, messageDate, status, pgaRemarks, applicationMapProperties.mapKeswsCheckingOfficer)
            docDetails.verNo = version
            val cdApprovalResponseDTO = CDApprovalResponseDTO()
            cdApprovalResponseDTO.documentHeader = docHeader
            cdApprovalResponseDTO.documentDetails = docDetails

            val fileName =
                    commonDaoServices.createKesWsFileName(applicationMapProperties.mapKeswsCdApprovalDoctype, cdNumber, version)
            val xmlFile = fileName.let { commonDaoServices.serializeToXml(it, cdApprovalResponseDTO) }
            xmlFile.let { it1 -> sftpService.uploadFile(it1) }
        }
    }

    //Send On hold/verification request
    fun submitOnHoldStatusToKesWS(declarationRefNo: String, version: Int) {
        KotlinLogging.logger { }.info { "::::::::::::::::::: Sending verification request for declaration no = $declarationRefNo ::::::::::::::" }

        val cdVerificationRequestDataSAD = CdVerificationRequestDataSAD(declarationRefNo)
        val cdVerificationRequestDataIn = CdVerificationRequestDataIn()
        cdVerificationRequestDataIn.cdVerificationRequestDataSAD = cdVerificationRequestDataSAD
        val cdVerificationRequestData = CdVerificationRequestData()
        cdVerificationRequestData.cdVerificationRequestDataIn = cdVerificationRequestDataIn
        val cdVerificationRequestHeader = CdVerificationRequestHeader(version)

        val cdVerificationRequestXmlDTO = CDVerificationRequestXmlDTO()
        cdVerificationRequestXmlDTO.cdVerificationRequestHeader = cdVerificationRequestHeader
        cdVerificationRequestXmlDTO.cdVerificationRequestData = cdVerificationRequestData

        val fileName = commonDaoServices.createKesWsFileName(applicationMapProperties.mapKeswsOnHoldDoctype, declarationRefNo, version.toString())

        val xmlFile = fileName.let { commonDaoServices.serializeToXml(fileName, cdVerificationRequestXmlDTO) }

        xmlFile.let { it1 -> sftpService.uploadFile(it1) }
    }

    //Send CD status to KeSWS
    fun submitCoRToKesWS(corsBakEntity: CorsBakEntity) {
        val cor: CustomCorXmlDto = corsBakEntity.toCorXmlRecordRefl()
        val corDto = CORXmlDTO()
        cor.version = (corsBakEntity.version ?: 1).toString()
        corDto.cor = cor
        val fileName = corsBakEntity.chasisNumber?.let {
            commonDaoServices.createKesWsFileName(
                    applicationMapProperties.mapKeswsCorDoctype,
                    it, corsBakEntity.version?.toString() ?: "1"
            )
        } ?: throw ExpectedDataNotFound("Invalid chassis number")
        val xmlFile = commonDaoServices.serializeToXml(fileName, corDto)
        sftpService.uploadFile(xmlFile, "COR")
    }

    /*
    Update CD After receiving verification schedule from KeSWS/KRA
     */
    fun updateCdVerificationSchedule(declarationVerificationDocumentMessage: DeclarationVerificationMessage) {
        KotlinLogging.logger { }.info { "::::::::::::::::::: Updating verification schedule ::::::::::::::" }
        //Find the declaration
        val declaration = declarationVerificationDocumentMessage.data?.dataIn?.sad?.sadId?.let {
            this.findDeclarationByDclRefNum(
                    it
            )
        }
        //Find the CD
        declaration?.refNum?.let { ucr ->
            findCdWithUcrNumber(ucr)?.let { cdDetails ->
                KotlinLogging.logger { }.info { "::: cdDetails: ${cdDetails.id} ::::::::" }
                val inspectionDateReceived = declarationVerificationDocumentMessage.data?.dataIn?.sad?.verificationDateTime?.let {
                    commonDaoServices.convertISO8601DateToTimestamp(
                            it
                    )
                }
                KotlinLogging.logger { }.info { "::: inspectionDateReceived: ${inspectionDateReceived} ::::::::" }
                with(cdDetails) {
                    inspectionDate = inspectionDateReceived?.let { Date(it.time) }
                    inspectionDateSetStatus = commonDaoServices.activeStatus.toInt()
                }
                iConsignmentDocumentDetailsRepo.save(cdDetails)
                updateCDStatus(cdDetails, ConsignmentDocumentStatus.KRA_VERIFICATION)
            }
                    ?: KotlinLogging.logger { }.info { "Consignment document for declaration: ${declarationVerificationDocumentMessage.data?.dataIn?.sad?.sadId} not found" }
        }
    }

    fun sendLocalCocReportEmail(recipientEmail: String, filePath: String): Boolean {
        val subject = "Local COC Certificate"
        val messageBody =
                "Hello,  \n" +
                        "\n " +
                        "Find attached the Local COC Certificate."
        var emailAddress = recipientEmail
        if (!applicationMapProperties.defaultTestEmailAddres.isNullOrEmpty()) {
            emailAddress = applicationMapProperties.defaultTestEmailAddres.orEmpty()
        }
        notifications.sendEmail(emailAddress, subject, messageBody, filePath)
        return true
    }

    fun sendLocalCorReportEmail(recipientEmail: String, filePath: String): Boolean {
        val subject = "Local COR Certificate"
        val messageBody =
                "Hello,  \n" +
                        "\n " +
                        "Find attached the Local COR Certificate."
        var emailAddress = recipientEmail
        if (!applicationMapProperties.defaultTestEmailAddres.isNullOrEmpty()) {
            emailAddress = applicationMapProperties.defaultTestEmailAddres.orEmpty()
        }
        notifications.sendEmail(emailAddress, subject, messageBody, filePath)
        return true
    }

    fun convertCdItemDetailsToMinistryInspectionListResponseDto(cdItemDetails: CdItemDetailsEntity, completed: Boolean): MinistryInspectionListResponseDto {
        val ministryInspectionItem = MinistryInspectionListResponseDto()
        ministryInspectionItem.cdId = cdItemDetails.cdDocId?.id!!
        ministryInspectionItem.ministryInspectionComplete = completed
        ministryInspectionItem.cdUcr = cdItemDetails.cdDocId?.ucrNumber
        ministryInspectionItem.cdItemDetailsId = cdItemDetails.id
        this.findCdItemNonStandardByItemID(cdItemDetails)?.let { cdItemNonStandard ->
            ministryInspectionItem.chassis = cdItemNonStandard.chassisNo
            ministryInspectionItem.used = cdItemNonStandard.usedIndicator ?: "N/A"
            ministryInspectionItem.year = cdItemNonStandard.vehicleYear ?: "N/A"
            ministryInspectionItem.model = cdItemNonStandard.vehicleModel ?: "N/A"
            ministryInspectionItem.make = cdItemNonStandard.vehicleMake ?: "N/A"
        }
        return ministryInspectionItem
    }

    private fun formatDate(date: String, endOfDay: Boolean): Timestamp {
        var dt = LocalDate.now()
        if (!date.trim().isEmpty()) {
            val parsedDate = LocalDate.from(DATE_FORMAT.parse(date))
            dt = parsedDate
        }
        if (endOfDay) {
            return Timestamp.valueOf(dt.plusDays(1).atStartOfDay())
        }
        return Timestamp.valueOf(dt.atStartOfDay())
    }

    fun listExchangeRates(date: String): List<CurrencyExchangeRates> {
        val dt = formatDate(date, false)
        return this.currencyExchangeRateRepository.findByApplicableDateAndStatus(DATE_FORMAT.format(dt.toLocalDateTime()), 1)
    }

    fun listExchangeRatesRange(date: String, endDate: String): List<CurrencyExchangeRates> {
        return this.currencyExchangeRateRepository.findAllByCreatedOnBetween(formatDate(date, false), formatDate(endDate, true))
    }

    fun listCurrentExchangeRates(status: Int): List<CurrencyExchangeRates> {
        return this.currencyExchangeRateRepository.findAllByCurrentRateAndStatus(status, 1)
    }

    fun updateIdfNumber(ucrNumber: String, baseDocRefNo: String, version: String?) {
        val v = version?.toLongOrDefault(1) ?: 0
        this.findCdWithUcrNumberAndVersion(ucrNumber, v)?.let { cdDetails ->
            cdDetails.idfNumber = baseDocRefNo
            cdDetails.modifiedOn = commonDaoServices.getTimestamp()
            iConsignmentDocumentDetailsRepo.save(cdDetails)
        }
    }


}
