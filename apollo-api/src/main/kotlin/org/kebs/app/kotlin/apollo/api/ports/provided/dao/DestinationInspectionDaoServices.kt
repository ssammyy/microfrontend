package org.kebs.app.kotlin.apollo.api.ports.provided.dao

import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.json.JSONException
import org.json.JSONObject
import org.json.XML
import org.kebs.app.kotlin.apollo.api.notifications.Notifications
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.DestinationInspectionBpmn
import org.kebs.app.kotlin.apollo.api.ports.provided.emailDTO.*
import org.kebs.app.kotlin.apollo.api.ports.provided.sftp.SftpServiceImpl
import org.kebs.app.kotlin.apollo.common.dto.MinistryInspectionListResponseDto
import org.kebs.app.kotlin.apollo.common.dto.kesws.receive.DeclarationVerificationMessage
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.exceptions.InvalidInputException
import org.kebs.app.kotlin.apollo.common.exceptions.ServiceMapNotFoundException
import org.kebs.app.kotlin.apollo.common.utils.generateRandomText
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.customdto.*
import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.model.di.*
import org.kebs.app.kotlin.apollo.store.model.qa.QaSampleSubmissionEntity
import org.kebs.app.kotlin.apollo.store.repo.*
import org.kebs.app.kotlin.apollo.store.repo.di.*
import org.kebs.app.kotlin.apollo.store.repo.qa.IQaSampleSubmissionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.math.BigDecimal
import java.sql.Date
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Service
class DestinationInspectionDaoServices(
    private val applicationMapProperties: ApplicationMapProperties,
    private val commonDaoServices: CommonDaoServices,
    private val SampleSubmissionRepo: IQaSampleSubmissionRepository,
    private val serviceRequestsRepository: IServiceRequestsRepository,
    private val invoiceDaoService: InvoiceDaoService,
    private val notifications: Notifications,
    private val iCocItemRepository: ICocItemRepository,
    private val iUserProfilesRepo: IUserProfilesRepository,
    private val iSubSectionsLevel2Repo: ISubSectionsLevel2Repository,
    private val idfsRepo: IIDFDetailsEntityRepository,
    private val manifestRepo: IManifestDetailsEntityRepository,
    private val declarationRepo: IDeclarationDetailsEntityRepository,
    private val iLocalCocTypeRepo: ILocalCocTypesRepository,
    private val cocRepo: ICocsRepository,
    private val coisRep: ICoisRepository,
    private val usersCfsRepo: IUsersCfsAssignmentsRepository,
    private val iCdInspectionChecklistRepo: ICdInspectionChecklistRepository,
    private val cdTypesRepo: IConsignmentDocumentTypesEntityRepository,
    private val iCdImporterRepo: ICdImporterEntityRepository,
    private val iDemandNoteRepo: IDemandNoteRepository,
    private val iDemandNoteItemRepo: IDemandNoteItemsDetailsRepository,
    private val iCfgMoneyTypeCodesRepo: ICfgMoneyTypeCodesRepository,
    private val iCdTransportRepo: ICdTransportEntityRepository,
    private val iCdConsignorRepo: ICdConsignorEntityRepository,
    private val iCdServiceProviderRepo: ICdServiceProviderEntityRepository,
    private val iConsignmentDocumentDetailsRepo: IConsignmentDocumentDetailsRepository,
    private val iCocTypesRepo: ICocTypesEntityRepository,
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
    private val iLocalCocRepo: ILocalCocEntityRepository,

    private val iLocalCorRepo: ILocalCorEntityRepository,
    private val iLocalCocItemRepo: ILocalCocItemsEntityRepository,
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
    private val iCdInspectionAgrochemItemChecklistRepo: ICdInspectionAgrochemItemChecklistRepository,

    //Inspection Checklist Repos
    private val iCdInspectionEngineeringItemChecklistRepo: ICdInspectionEngineeringItemChecklistRepository,
    private val iCdInspectionOtherItemChecklistRepo: ICdInspectionOtherItemChecklistRepository,
    private val iCdInspectionMotorVehicleItemChecklistRepo: ICdInspectionMotorVehicleItemChecklistRepository,
    private val iPvocPartnersCountriesRepository: IPvocPartnersCountriesRepository,
    private val diBpmn: DestinationInspectionBpmn

) {

    @Autowired
    lateinit var sftpService: SftpServiceImpl

    @Value("\${destination.inspection.cd.type.coc}")
    lateinit var cocCdType: String

    @Value("\${destination.inspection.cd.type.exempted}")
    lateinit var exemptedCdType: String

    @Value("\${destination.inspection.cd.type.cor}")
    lateinit var corCdType: String

    @Value("\${destination.inspection.cd.type.no.cor}")
    lateinit var noCorCdType: String

    @Value("\${destination.inspection.cd.type.ncr}")
    lateinit var ncrCdType: String

    @Value("\${destination.inspection.cd.type.temporary.imports}")
    lateinit var temporaryImportCdType: String

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


    fun updateCdDetailsWIthCor(cdDetails: ConsignmentDocumentDetailsEntity, chasisNumber: String?) {
        chasisNumber?.let {
            corsBakRepository.findByChasisNumber(it).let { corsBakEntity ->
                if (corsBakEntity != null) {
                    updateCDDetailsWithCORData(corsBakEntity, cdDetails)
                } else {
                    this.handleNoCorFromCosWithPvoc(cdDetails)
                    //Check if No CoR from CoS with PVoC
                    with(cdDetails) {
                        cdType = findCdTypeDetailsWithUuid(noCorCdType)
                    }
                    iConsignmentDocumentDetailsRepo.save(cdDetails)
                }
            }
        }
    }

    fun handleNoCorFromCosWithPvoc(cdDetailsEntity: ConsignmentDocumentDetailsEntity) {
        //Check if Cos is available
        cdDetailsEntity.cdHeaderOne?.let {
            findCdHeaderOneDetails(it).countryOfSupply?.let { cos ->
                iPvocPartnersCountriesRepository.findByAbbreviationIgnoreCase(cos)
            }?.let {
                cdDetailsEntity.csApprovalStatus = commonDaoServices.inActiveStatus.toInt()
            }
        }
    }

    fun findCocType(cocTypeID: Long): CocTypesEntity {
        iCocTypesRepo.findByIdOrNull(cocTypeID)
            ?.let {
                return it
            }
            ?: throw Exception("COC TYPE WIth id = ${cocTypeID}, does not Exist")
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

    fun findCocTypeWithTypeName(cocType: String): CocTypesEntity {
        iCocTypesRepo.findByTypeName(cocType)
            ?.let {
                return it
            }
            ?: throw Exception("COC TYPE with type name = ${cocType}, does not Exist")
    }

    fun findLocalCocTypeWithCocTypeCode(cocTypeCode: String): LocalCocTypesEntity {
        iLocalCocTypeRepo.findByCocTypeCode(cocTypeCode)
            ?.let {
                return it
            }
            ?: throw Exception("Local COC TYPE with type code = ${cocTypeCode}, does not Exist")
    }

    fun findALlLocalCocTypeDetails(status: Int): List<LocalCocTypesEntity> {
        iLocalCocTypeRepo.findByStatus(status)
            ?.let {
                return it
            }
            ?: throw Exception("All Local COC TYPE with status = ${status}, does not Exist")
    }


    fun extractCDTablesDetailsFromJson(dataFetched: JSONObject?, objectTitle: String): JSONObject? {
        return dataFetched?.let {
            try {
                dataFetched.getJSONObject(objectTitle)
            } catch (_: Exception) {
                null
            }
        }
    }

    fun convertStringToBigDecimal(priceValue: String): BigDecimal {
        val result = BigDecimal(priceValue.replace(",", ""))
        println(result)
        return result
    }

    fun findInspectionGeneralWithItemDetailsOrNull(cdItemDetails: CdItemDetailsEntity): CdInspectionGeneralEntity? {
        return iCdInspectionGeneralRepo.findFirstByCdItemDetails(cdItemDetails)
    }

    fun findCdItemsConsignmentDetailsOrNull(consignmentDocumentDetailsEntity: ConsignmentDocumentDetailsEntity): List<CdItemDetailsEntity>? {
        return iCdItemsRepo.findByCdDocId(consignmentDocumentDetailsEntity)
    }

//    // save Applicant Details
//    fun applicantCDDetails(cdApplicantDetailsEntity: CdApplicantDetailsEntity): CdApplicantDetailsEntity {
//        var applicantDetails = cdApplicantDetailsEntity
//        with(applicantDetails) {
//            createdBy = createdByValue
//            createdOn = commonDaoServices.getTimestamp()
//        }
//        applicantDetails = iCdApplicantRepo.save(applicantDetails)
//        KotlinLogging.logger { }.info { "Applicant Details saved ID = ${applicantDetails.id}" }
//
//        return applicantDetails
//    }

//    // save ApplicationStatus Details
//    fun applicationStatusCDDetails(cdApplicationStatusEntity: CdApplicationStatusEntity): CdApplicationStatusEntity {
//        var applicationStatusDetails = cdApplicationStatusEntity
//        with(applicationStatusDetails) {
//            createdBy = createdByValue
//            createdOn = commonDaoServices.getTimestamp()
//        }
//        applicationStatusDetails = iCdApplicationStatusRepo.save(applicationStatusDetails)
//        KotlinLogging.logger { }.info { "Application Status Details saved ID = ${applicationStatusDetails.id}" }
//
//        return applicationStatusDetails
//    }
//Todo : after confermation

fun createLocalCoc(
    user: UsersEntity,
    consignmentDocumentDetailsEntity: ConsignmentDocumentDetailsEntity,
    map: ServiceMapsEntity,
    routValue: String
): CocsEntity {
    var localCoc = CocsEntity()
    consignmentDocumentDetailsEntity.ucrNumber?.let {
        cocRepo.findByUcrNumber(it)
            ?.let { coc ->
                throw Exception("There is an Existing COC with the following UCR No = ${coc.ucrNumber}")
            }
    }
        ?: kotlin.run {
//            GlobalScope.launch(Dispatchers.IO) {
                KotlinLogging.logger { }.debug("Starting background task")
                try {
                    with(localCoc) {
                        coiNumber = "UNKNOWN"
                        cocNumber =
                            "KEBSCOC${
                                generateRandomText(
                                    5,
                                    map.secureRandom,
                                    map.messageDigestAlgorithm,
                                    true
                                )
                            }".toUpperCase()
                        idfNumber = consignmentDocumentDetailsEntity.ucrNumber?.let { findIdf(it)?.baseDocRefNo } ?: "UNKOWN"
                        rfiNumber = "UNKNOWN"
                        ucrNumber = consignmentDocumentDetailsEntity.ucrNumber
                        rfcDate = commonDaoServices.getTimestamp()
                        shipmentQuantityDelivered = "UNKNOWN"
                        cocIssueDate = commonDaoServices.getTimestamp()
                        coiIssueDate = commonDaoServices.getTimestamp()
                        clean = "Y"

                        cocRemarks = "UNKNOWN"
                        coiRemarks = "UNKNOWN"
                        issuingOffice = "UNKNOWN"

                        val cdImporter = consignmentDocumentDetailsEntity.cdImporter?.let { findCDImporterDetails(it) }
                        importerName = cdImporter?.name
                        importerPin = cdImporter?.pin
                        importerAddress1 = cdImporter?.physicalAddress
                        importerAddress2 = "UNKNOWN"
                        importerCity = "UNKNOWN"
                        importerCountry = cdImporter?.physicalCountryName
                        importerZipCode = "UNKNOWN"
                        importerTelephoneNumber = cdImporter?.telephone
                        importerFaxNumber = cdImporter?.fax
                        importerEmail = cdImporter?.email

                        val cdExporter = consignmentDocumentDetailsEntity.cdExporter?.let { findCdExporterDetails(it) }
                        exporterName = cdExporter?.name
                        exporterPin = cdExporter?.pin
                        exporterAddress1 = cdExporter?.physicalAddress
                        exporterAddress2 = "UNKNOWN"
                        exporterCity = "UNKNOWN"
                        exporterCountry = cdExporter?.physicalCountryName
                        exporterZipCode = "UNKNOWN"
                        exporterTelephoneNumber = cdExporter?.telephone
                        exporterFaxNumber = cdExporter?.fax
                        exporterEmail = cdExporter?.email

                        placeOfInspection = "UNKNOWN"
                        dateOfInspection = commonDaoServices.getTimestamp()

                        val cdTransport = consignmentDocumentDetailsEntity.cdTransport?.let { findCdTransportDetails(it) }
                        portOfDestination = cdTransport?.portOfArrival
                        shipmentMode = "UNKNOWN"
                        countryOfSupply = "UNKNOWN"
                        finalInvoiceCurrency = "KES"
                        finalInvoiceDate = commonDaoServices.getTimestamp()
                        shipmentSealNumbers = "UNKNOWN"
                        shipmentContainerNumber = "UNKNOWN"
                        shipmentGrossWeight = "UNKNOWN"
//                        shipmentGrossWeightUnit = "UNKOWN"
                        route = routValue
                        cocType = "L"
                        productCategory = "UNKNOWN"
                        partner = "UNKNOWN"
                        createdBy = commonDaoServices.concatenateName(user)
                        createdOn = commonDaoServices.getTimestamp()
                    }

                    localCoc = cocRepo.save(localCoc)
                    KotlinLogging.logger { }.info { "localCoc = ${localCoc.id}" }
                    localCocItems(consignmentDocumentDetailsEntity, localCoc, user, map)
                    sendLocalCoc(localCoc.id)
                } catch (e: Exception) {
                    KotlinLogging.logger { }.debug("Threw error from forward express callback")
                    KotlinLogging.logger { }.debug(e.message)
                    KotlinLogging.logger { }.debug(e.toString())
                }
//            }
            return localCoc
        }

}


    fun createLocalCoi(
        user: UsersEntity,
        consignmentDocumentDetailsEntity: ConsignmentDocumentDetailsEntity,
        map: ServiceMapsEntity,
        routValue: String
    ): CocsEntity {
        val coc = CocsEntity()
        consignmentDocumentDetailsEntity.ucrNumber?.let {
            cocRepo.findByUcrNumber(it)
                ?.let { coc ->
                    throw Exception("There is an Existing COI with the following UCR No = ${coc.ucrNumber}")
                }
        }
            ?: kotlin.run {

                with(coc) {
                    cocNumber = "UNKNOWN"
                    coiNumber =
                        "KEBSCOI${
                            generateRandomText(
                                5,
                                map.secureRandom,
                                map.messageDigestAlgorithm,
                                true
                            )
                        }".toUpperCase()
                    idfNumber = consignmentDocumentDetailsEntity.ucrNumber?.let { findIdf(it)?.baseDocRefNo }
                    rfiNumber = "UNKNOWN"
                    ucrNumber = consignmentDocumentDetailsEntity.ucrNumber
                    rfcDate = commonDaoServices.getTimestamp()
                    shipmentQuantityDelivered = "UNKNOWN"
                    cocIssueDate = commonDaoServices.getTimestamp()
                    coiIssueDate = commonDaoServices.getTimestamp()
                    clean = "Y"
                    cocRemarks = "UNKNOWN"
                    coiRemarks = "UNKNOWN"
                    issuingOffice = "UNKNOWN"

                    val cdImporter = consignmentDocumentDetailsEntity.cdImporter?.let { findCDImporterDetails(it) }
                    importerName = cdImporter?.name
                    importerPin = cdImporter?.pin
                    importerAddress1 = cdImporter?.physicalAddress
                    importerAddress2 = "UNKNOWN"
                    importerCity = "UNKNOWN"
                    importerCountry = cdImporter?.physicalCountryName
                    importerZipCode = "UNKNOWN"
                    importerTelephoneNumber = cdImporter?.telephone
                    importerFaxNumber = cdImporter?.fax
                    importerEmail = cdImporter?.email

                    val cdExporter = consignmentDocumentDetailsEntity.cdExporter?.let { findCdExporterDetails(it) }
                    exporterName = cdExporter?.name
                    exporterPin = cdExporter?.pin
                    exporterAddress1 = cdExporter?.physicalAddress
                    exporterAddress2 = "UNKNOWN"
                    exporterCity = "UNKNOWN"
                    exporterCountry = cdExporter?.physicalCountryName
                    exporterZipCode = "UNKNOWN"
                    exporterTelephoneNumber = cdExporter?.telephone
                    exporterFaxNumber = cdExporter?.fax
                    exporterEmail = cdExporter?.email

                    placeOfInspection = "UNKNOWN"
                    dateOfInspection = commonDaoServices.getTimestamp()

                    val cdTransport = consignmentDocumentDetailsEntity.cdTransport?.let { findCdTransportDetails(it) }
                    portOfDestination = cdTransport?.portOfArrival
                    shipmentMode = "UNKNOWN"
                    countryOfSupply = "UNKNOWN"
                    finalInvoiceCurrency = "KES"
                    finalInvoiceDate = commonDaoServices.getTimestamp()
                    shipmentSealNumbers = "UNKNOWN"
                    shipmentContainerNumber = "UNKNOWN"
                    shipmentGrossWeight = "UNKNOWN"
//                    shipmentGrossWeightUnit = "UNKNOWN"
                    route = routValue
                    cocType = "L"
                    productCategory = "UNKNOWN"
                    partner = "UNKNOWN"
                    createdBy = commonDaoServices.concatenateName(user)
                    createdOn = commonDaoServices.getTimestamp()
                }
                return cocRepo.save(coc)
            }


    }



//    fun generateLocalCoc(cdLocalCocEntity: CdLocalCocEntity, consignmentDocumentDetailsEntity: ConsignmentDocumentDetailsEntity, user: UsersEntity, map: ServiceMapsEntity): CdLocalCocEntity {
//        var localCoc = cdLocalCocEntity
//        with(localCoc) {
//            cocNo = "KEBS/COC/${generateRandomText(5, map.secureRandom, map.messageDigestAlgorithm, true)}".toUpperCase()
//            cocStatus = findCdStatusValue(approvedStatus.toLong()).typeName
//            cocType = "LOCAL COC 0"
//            issueDate = commonDaoServices.getCurrentDate()
//            entryNo = "ENTRY NUMBER"
//            idfNo = consignmentDocumentDetailsEntity.idfNumber
//            val cdImporter = consignmentDocumentDetailsEntity.cdImporter?.let { findCDImporterDetails(it) }
//            importerName = cdImporter?.name
//            importerAddress = cdImporter?.physicalAddress
//            importerPin = cdImporter?.pin
//            clearAgent = "CLEARING AGENT"
//            val cdTransport = consignmentDocumentDetailsEntity.cdTransport?.let { findCdTransportDetails(it) }
//            portEntry = cdTransport?.portOfArrivalDesc
//            remarks = null
//            ucrNumber = consignmentDocumentDetailsEntity.ucrNumber
//            status = map.activeStatus
//            createdBy = commonDaoServices.getUserName(user)
//            createdOn = commonDaoServices.getTimestamp()
//        }
//        localCoc = iLocalCocRepo.save(localCoc)
//        KotlinLogging.logger { }.info { "Generated Local coc WITH id = ${localCoc.id}" }
//        return localCoc
//    }


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
            shipmentLineNumber = cdItemDetails.itemNo!!
            shipmentLineHscode = cdItemDetails.itemHsCode
            shipmentLineQuantity = cdItemDetails.quantity?.toLong()!!
            shipmentLineUnitofMeasure = cdItemDetails.unitOfQuantity
            shipmentLineDescription = cdItemDetails.itemDescription
            shipmentLineVin = "UNKNOWN"
            shipmentLineStickerNumber = "UNKNOWN"
            shipmentLineIcs = "UNKNOWN"
            shipmentLineStandardsReference = "UNKNOWN"
            shipmentLineLicenceReference = "UNKNOWN"
            shipmentLineRegistration = "UNKNOWN"
            ownerPin = ownerPinValues
            ownerName = ownerNameValues
            status = map.activeStatus
            cocNumber = localCocEntity.cocNumber
            coiNumber = localCocEntity.coiNumber
            shipmentLineBrandName = "UNKNOWN"
            createdBy = commonDaoServices.getUserName(user)
            createdOn = commonDaoServices.getTimestamp()
        }
        localCocItems = iCocItemRepository.save(localCocItems)
        KotlinLogging.logger { }.info { "Generated Local coc item WITH id = ${localCocItems.id}" }
        return localCocItems
    }


    fun localCocItems(
        updatedCDDetails: ConsignmentDocumentDetailsEntity,
        localCocEntity: CocsEntity,
        user: UsersEntity,
        map: ServiceMapsEntity
    ) {

        findCDItemsListWithCDID(updatedCDDetails)
            .forEach { cdItemDetails ->
//                                if (cdItemDetails.approveStatus == map.activeStatus) {
                generateLocalCocItem(cdItemDetails, localCocEntity, user, map, "NA", "NA")
//                                }
            }


    }


    fun localCoiItems(
        updatedCDDetails: ConsignmentDocumentDetailsEntity,
        localCocEntity: CocsEntity,
        user: UsersEntity,
        map: ServiceMapsEntity
    ) {

        findCDItemsListWithCDID(updatedCDDetails)
            .forEach { cdItemDetails ->
                val ownerPinValues =
                    cdItemDetails.ownerPin ?: throw Exception("Add OWNER PIN for ITEM number = ${cdItemDetails.itemNo}")
                val ownerNameValues = cdItemDetails.ownerName
                    ?: throw Exception("Add OWNER NAME for ITEM number = ${cdItemDetails.itemNo}")
                generateLocalCocItem(cdItemDetails, localCocEntity, user, map, ownerPinValues, ownerNameValues)

            }


    }


    fun sendLocalCoc(cocId: Long) {

        val cocsEntity: CocsEntity = cocRepo.findById(cocId).get()

        cocsEntity.let {
            val coc: CustomCocXmlDto = it.toCocXmlRecordRefl()
            //COC ITEM
            val cocItem = iCocItemRepository.findByCocId(cocsEntity.id)?.get(0)
            cocItem?.toCocItemDetailsXmlRecordRefl().let {
                coc.cocDetals = it
                val cocFinalDto = COCXmlDTO()
                cocFinalDto.coc = coc

                val fileName = cocFinalDto.coc?.ucrNumber?.let {
                    commonDaoServices.createKesWsFileName(
                        applicationMapProperties.mapKeswsCocDoctype,
                        it
                    )
                }

                val xmlFile = fileName?.let { commonDaoServices.serializeToXml(it, cocFinalDto) }

                xmlFile?.let { it1 -> sftpService.uploadFile(it1) }

            }

        }
    }

    fun sendLocalCoi(cocId: Long) {

        val cocsEntity: CocsEntity = cocRepo.findById(cocId).get()

        cocsEntity.let {
            val coi: CustomCoiXmlDto = it.toCoiXmlRecordRefl()
//            val coc: CustomCocXmlDto = it.toCocXmlRecordRefl()
            //COC ITEM
            val cocItem = iCocItemRepository.findByCocId(cocsEntity.id)?.get(0)
            cocItem?.toCocItemDetailsXmlRecordRefl().let {
                val coiFinalDto = COIXmlDTO()
                coiFinalDto.coi = coi

                val fileName = coiFinalDto.coi?.ucrNumber?.let {
                    commonDaoServices.createKesWsFileName(
                        applicationMapProperties.mapKeswsCocDoctype,
                        it
                    )
                }

                val xmlFile = fileName?.let { commonDaoServices.serializeToXml(it, coiFinalDto) }

                xmlFile?.let { it1 -> sftpService.uploadFile(it1) }

            }

        }
    }

    //TODO: Figure out where to get the null details
    fun generateLocalCor(
        cdLocalCorEntity: CdLocalCorEntity, cdEntity: ConsignmentDocumentDetailsEntity, s: ServiceMapsEntity,
        user: UsersEntity,
    ): CdLocalCorEntity {
        var localCor = cdLocalCorEntity
        //Get CD Item by cd doc id
        iCdItemsRepo.findByCdDocId(cdEntity)?.let { cdItemDetailsList ->
            this.findMotorVehicleInspectionByCdItem(cdItemDetailsList.get(0))?.let { cdMvInspectionEntity ->
                with(localCor) {
                    corSerialNo =
                        "KEBS/COR-NBI/${generateRandomText(5, s.secureRandom, s.messageDigestAlgorithm, false)}"
                    corIssueDate = commonDaoServices.getCurrentDate()
                    corExpiryDate = commonDaoServices.addMonthsToCurrentDate(3)

                    val cdImporter = cdEntity.cdImporter?.let { findCDImporterDetails(it) }
                    importerName = cdImporter?.name
                    importerAddress = cdImporter?.physicalAddress
                    importerPhone = cdImporter?.telephone

                    val cdExporter = cdEntity.cdExporter?.let { findCdExporterDetails(it) }
                    exporterName = cdExporter?.name
                    exporterAddress = cdExporter?.physicalAddress
                    exporterEmail = cdExporter?.email
                    chassisNo = cdMvInspectionEntity.chassisNo
                    engineNoModel = cdMvInspectionEntity.engineNoCapacity
                    engineCapacity = cdMvInspectionEntity.engineNoCapacity
                    yearOfManufacture = cdMvInspectionEntity.manufactureDate?.toString()
                    yearOfFirstRegistration = cdMvInspectionEntity.registrationDate?.toString()
                    customsIeNo = null
                    val cdHeaderOne = cdEntity.cdHeaderOne?.let { findCdHeaderOneDetails(it) }
                    countryOfSupply = cdHeaderOne?.countryOfSupply
                    bodyType = null
                    fuel = null
                    tareWeight = null
                    typeOfVehicle = null
                    cdEntity.ucrNumber?.let {
                        idfNo = findIdf(it)?.baseDocRefNo
                    }
                    ucrNumber = cdEntity.ucrNumber
                    make = cdMvInspectionEntity.makeVehicle
                    model = null
                    inspectionMileage = cdMvInspectionEntity.odemetreReading
                    unitsOfMileage = null
                    color = cdMvInspectionEntity.colour
                    axleNo = null
                    transmission = cdMvInspectionEntity.transmissionAutoManual
                    noOfPassengers = null
                    prevRegNo = null
                    prevCountryOfReg = null
                    inspectionDetails = cdMvInspectionEntity.remarks
                    status = s.activeStatus
                    createdBy = commonDaoServices.getUserName(user)
                    createdOn = commonDaoServices.getTimestamp()
                }
                localCor = iLocalCorRepo.save(localCor)
                KotlinLogging.logger { }.info { "Generated Local CoR WITH id = ${localCor.id}" }
            }
        }
        return localCor
    }

    fun generateCor(
        cdEntity: ConsignmentDocumentDetailsEntity,
        s: ServiceMapsEntity,
        user: UsersEntity,
    ): CorsBakEntity {
        var localCor = CorsBakEntity()
        //Get CD Item by cd doc id
        iCdItemsRepo.findByCdDocId(cdEntity)?.let { cdItemDetailsList ->
            this.findMotorVehicleInspectionByCdItem(cdItemDetailsList.get(0))?.let { cdMvInspectionEntity ->
                with(localCor) {
                    corNumber = "COR${generateRandomText(5, s.secureRandom, s.messageDigestAlgorithm, true)}"
                    corIssueDate = commonDaoServices.getTimestamp()
                    val cdHeaderOne = cdEntity.cdHeaderOne?.let { findCdHeaderOneDetails(it) }
                    countryOfSupply = cdHeaderOne?.countryOfSupply
                    inspectionCenter = "test"
                    val cdExporter = cdEntity.cdExporter?.let { findCdExporterDetails(it) }
                    exporterName = cdExporter?.name
                    exporterAddress1 = cdExporter?.physicalAddress
                    exporterAddress2 = cdExporter?.postalAddress
                    exporterEmail = cdExporter?.email
                    applicationBookingDate = commonDaoServices.getTimestamp()
                    inspectionDate = commonDaoServices.getTimestamp()
                    make = cdMvInspectionEntity.makeVehicle
                    model = "test"
                    chasisNumber = cdMvInspectionEntity.chassisNo
                    engineNumber = cdMvInspectionEntity.engineNoCapacity
                    engineCapacity = cdMvInspectionEntity.engineNoCapacity
                    yearOfManufacture = cdMvInspectionEntity.manufactureDate.toString()
                    yearOfFirstRegistration = "test"
                    inspectionMileage = cdMvInspectionEntity.odemetreReading
                    unitsOfMileage = "KM"
                    inspectionRemarks = cdMvInspectionEntity.remarks
                    previousRegistrationNumber = "test"
                    previousCountryOfRegistration = "test"
                    tareWeight = 0
                    loadCapacity = 0
                    grossWeight = 0
                    numberOfAxles = 0
                    typeOfVehicle = cdMvInspectionEntity.makeVehicle
                    numberOfPassangers = 0
                    typeOfBody = "test"
                    bodyColor = cdMvInspectionEntity.colour
                    fuelType = "test"
                    inspectionFee = 0
                    approvalStatus = cdEntity.approveRejectCdStatus.toString()
                    ucrNumber = cdEntity.ucrNumber
                    inspectionFeeCurrency = "USD"
                    partner = "test"
                    inspectionFeeExchangeRate = 0
                    inspectionFeePaymentDate = commonDaoServices.getTimestamp()
                    inspectionRemarks = "test"
                    status = s.activeStatus
                    createdBy = commonDaoServices.getUserName(user)
                    createdOn = commonDaoServices.getTimestamp()
                }
                localCor = saveCorDetails(localCor)
                KotlinLogging.logger { }.info { "Generated Local CoR WITH id = ${localCor.id}" }
            }
        }
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

    fun findCORById(cdId: Long): CorsBakEntity? {
        return corsBakRepository.findByIdOrNull(cdId)
    }

    fun findCORByChassisNumber(chassisNo: String): CorsBakEntity {
        corsBakRepository.findByChasisNumber(chassisNo)
            ?.let { corEntity ->
                return corEntity
            }
            ?: throw Exception("CoR Entity with the following chassis number = ${chassisNo}, does not Exist")
    }

    fun findCdStatusValueList(status: Int): List<CdStatusTypesEntity> {
        iCdStatusTypesDetailsRepo.findByStatus(status)
            ?.let { cdStatusDetails ->
                return cdStatusDetails
            }
            ?: throw Exception("cd Status Details with status = ${status}, do not Exist")
    }

    fun findDemandNoteListFromCdItemList(
        cdItemList: List<CdItemDetailsEntity>,
        status: Int
    ): MutableList<CdDemandNoteEntity> {
        val demandNotes: MutableList<CdDemandNoteEntity> = mutableListOf()
        cdItemList.forEach { item ->
            val demandNote = findDemandNoteWithPaymentStatus(item, status)
            if (demandNote != null) {
                demandNotes.add(demandNote)
            }
        }
        return demandNotes
    }

    fun findLocalCocItemsList(localCocId: Long): List<CdLocalCocItemsEntity> {
        iLocalCocItemRepo.findByLocalCocId(localCocId)
            ?.let { localCocItemDetails ->
                return localCocItemDetails
            }
            ?: throw Exception("Local COC with ID = ${localCocId}, do not Exist")
    }

    fun findCdStatusValue(statusID: Long): CdStatusTypesEntity {
        iCdStatusTypesDetailsRepo.findByIdOrNull(statusID)
            ?.let { cdStatusDetails ->
                return cdStatusDetails
            }
            ?: throw Exception("Status Details with id = ${statusID}, does not Exist")
    }

    fun findDIFeeList(status: Int): List<DestinationInspectionFeeEntity>? {
        iDIFeeDetailsRepo.findByStatus(status)
            ?.let { diFeeDetails ->
                return diFeeDetails
            }
            ?: throw Exception("DI fee Details with status = ${status}, do not Exist")
    }

    fun findCocItemList(cocId: Long): List<CocItemsEntity> {
        iCocItemRepository.findByCocId(cocId)
            ?.let { cocItemDetails ->
                return cocItemDetails
            }
            ?: throw Exception("COC ITEM(s) Details with COC ID = ${cocId}, do not Exist")
    }

    fun findDemandNoteWithCDID(cdId: Long): CdDemandNoteEntity {
        iDemandNoteRepo.findByCdId(cdId)
            ?.let { demandNote ->
                return demandNote
            }
            ?: throw Exception("Demand Note Details with CD ID = ${cdId}, do not Exist")
    }

    fun findCoiItemList(coiId: Long): List<CocItemsEntity> {
        iCocItemRepository.findByCocId(coiId)
            ?.let { cocItemDetails ->
                return cocItemDetails
            }
            ?: throw Exception("COI ITEM(s) Details with COI ID = ${coiId}, do not Exist")
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

    fun sendDemandNotGeneratedToKWIS(demandNoteId: Long) {
        val demandNoteEntity: CdDemandNoteEntity = iDemandNoteRepo.findById(demandNoteId).get()
        demandNoteEntity.let {
            val mpesaDetails = invoiceDaoService.findPaymentMethodtype(applicationMapProperties.mapMpesaDetails)
            val bank1Details = invoiceDaoService.findPaymentMethodtype(applicationMapProperties.mapBankOneDetails)
            val bank2Details = invoiceDaoService.findPaymentMethodtype(applicationMapProperties.mapBankTwoDetails)
            val bank3Details = invoiceDaoService.findPaymentMethodtype(applicationMapProperties.mapBankThreeDetails)

            val demandNote: CustomDemandNoteXmlDto = it.toCdDemandNoteXmlRecordRefl()
            demandNote.paymentInstruction1 = PaymenInstruction1(bank1Details)
            demandNote.paymentInstruction2 = PaymenInstruction2(bank2Details)
            demandNote.paymentInstruction3 = PaymenInstruction3(bank3Details)
            demandNote.paymentInstructionMpesa = PaymenInstructionMpesa(mpesaDetails)
            demandNote.paymentInstructionOther = PaymenInstructionOther(mpesaDetails)

            val demandNoteFinalDto = DemandNoteXmlDTO()
            demandNoteFinalDto.customDemandNote = demandNote

            val fileName = demandNoteFinalDto.customDemandNote?.demandNoteNumber?.let {
                commonDaoServices.createKesWsFileName(applicationMapProperties.mapKeswsDemandNoteDoctype, it)
            }

            val xmlFile = fileName?.let { commonDaoServices.serializeToXml(it, demandNoteFinalDto) }

            xmlFile?.let { it1 -> sftpService.uploadFile(it1) }

        }
    }


    fun sendDemandNotePayedStatusToKWIS(demandNoteId: Long) {
        iDemandNoteRepo.findByIdOrNull(demandNoteId)
            ?.let { demandNote ->
                val customDemandNotePay = CustomDemandNotePayXmlDto(demandNote)
                val demandNotePay = DemandNotePayXmlDTO()
                demandNotePay.customDemandNotePay = customDemandNotePay

                val fileName = customDemandNotePay.demandNoteNumber?.let {
                    commonDaoServices.createKesWsFileName(applicationMapProperties.mapKeswsDemandNotePayDoctype, it)
                }

                val xmlFile = fileName?.let { commonDaoServices.serializeToXml(fileName, demandNotePay) }

                xmlFile?.let { it1 -> sftpService.uploadFile(it1) }
            }
            ?: throw ExpectedDataNotFound("Demand note WIth ID= ${demandNoteId}, does not Exist")

    }

    fun demandNoteCalculation(
        cfiValue: BigDecimal,
        diInspectionFeeId: DestinationInspectionFeeEntity,
        itemDetails: CdItemDetailsEntity
    ): BigDecimal? {
        val percentage = 100
        var amount = (diInspectionFeeId.rate?.toBigDecimal())?.multiply(cfiValue)?.divide(percentage.toBigDecimal())

        KotlinLogging.logger { }.info { "MY AMOUNT BEFORE CALCULATION = $amount" }

        val currencyValues = itemDetails.foreignCurrencyCode?.let { iCfgMoneyTypeCodesRepo.findByTypeCode(it) }
//        var amountInUSD = itemDetails.foreignCurrencyCode?.let { iCfgMoneyTypeCodesRepo.findByTypeCode(it) }

        when (amount) {
            null -> {
                amount = diInspectionFeeId.amountKsh?.toBigDecimal()
            }
        }

        when {
            diInspectionFeeId.minimumUsd != null -> {
                val minimumValue = (currencyValues?.typeCodeValue)?.toBigDecimal()
                    ?.multiply(diInspectionFeeId.minimumUsd?.toBigDecimal())
                when {
                    minimumValue != null -> {
                        when {
                            amount != null -> {
                                amount = when {
                                    amount < minimumValue -> {
                                        minimumValue
                                    }
                                    else -> {
                                        amount
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        when {
            diInspectionFeeId.higher != null -> {
                val higherValue =
                    (currencyValues?.typeCodeValue)?.toBigDecimal()?.multiply(diInspectionFeeId.higher?.toBigDecimal())
                when {
                    higherValue != null -> {
                        when {
                            amount != null -> {
                                amount = when {
                                    amount > higherValue -> {
                                        amount
                                    }
                                    amount < higherValue -> {
                                        higherValue
                                    }
                                    else -> {
                                        amount
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }


        when {
            diInspectionFeeId.minimumKsh != null -> {
                val minimumValueKSH = diInspectionFeeId.minimumKsh?.toBigDecimal()
                when {
                    minimumValueKSH != null -> {
                        when {
                            amount != null -> {
                                amount = when {
                                    amount < minimumValueKSH -> {
                                        minimumValueKSH
                                    }
                                    else -> {
                                        amount
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return amount
    }

    fun addItemDetailsToDemandNote(
        itemDetails: CdItemDetailsEntity, demandNote: CdDemandNoteEntity, map: ServiceMapsEntity,
        user: UsersEntity
    ) {
        val fee = itemDetails.paymentFeeIdSelected
            ?: throw Exception("Item details with Id = ${itemDetails.id}, does not Have any Details For payment Fee Id Selected ")
        val demandNoteItem = CdDemandNoteItemsDetailsEntity()
        with(demandNoteItem) {
            itemId = itemDetails.id
            demandNoteId = demandNote.id
            product = itemDetails.itemDescription
            cfvalue = itemDetails.totalPriceNcy
            rate = fee.rate
            // Demand note Calculation Details
            amountPayable = cfvalue?.let { demandNoteCalculation(it, fee, itemDetails) }
                ?: throw Exception("Item details with Id = ${itemDetails.id}, does not Have any Details For total Price Ncy")
            status = map.activeStatus
            createdOn = commonDaoServices.getTimestamp()
            createdBy = commonDaoServices.getUserName(user)
        }
        iDemandNoteItemRepo.save(demandNoteItem)
    }

    fun calculateTotalAmountDemandNote(
        demandNote: CdDemandNoteEntity,
        map: ServiceMapsEntity,
        user: UsersEntity
    ): CdDemandNoteEntity {
        /* todo: Discuss with KEN on how to go with the bigDecimal not to be null when initializing */
        var totalAmountPayable: BigDecimal = 0.00.toBigDecimal()
        val demandNoteID =
            demandNote.id ?: throw Exception("Demand Note details with Id = ${demandNote.id}, does not Exist")
        iDemandNoteItemRepo.findByDemandNoteId(demandNoteID)
            ?.forEach { demandNoteItem ->
                totalAmountPayable = demandNoteItem.amountPayable?.plus(totalAmountPayable)!!
            }
        //Add the total amount To demandNote Details
        with(demandNote) {
            totalAmount = totalAmountPayable
        }
        //Update Demand Note Details
        return upDateDemandNoteWithUser(demandNote, user)
    }


    fun generateDemandNoteWithItemList(
        itemDetails: CdItemDetailsEntity,
        map: ServiceMapsEntity,
        user: UsersEntity
    ): CdDemandNoteEntity {

        /* todo: Discuss with KEN on how the function works */
        val itemCdId = itemDetails.cdDocId?.id
            ?: throw Exception("Item details with Id = ${itemDetails.id}, does not Have any Details For CD attached ")
        iDemandNoteRepo.findByCdId(itemCdId)
            ?.let { demandNote ->
                var demandNoteDetails = demandNote
                //Call Function to add Item Details To be attached To The Demand note
                addItemDetailsToDemandNote(itemDetails, demandNoteDetails, map, user)
                //Calculate the total Amount for Items In one Cd Tobe paid For
                demandNoteDetails = calculateTotalAmountDemandNote(demandNoteDetails, map, user)
                itemDetails.dnoteStatus = map.activeStatus
                return demandNoteUpDatingCDAndItem(itemDetails, user, demandNoteDetails)
            }
            ?: kotlin.run {
                var demandNote = CdDemandNoteEntity()
                with(demandNote) {
                    cdId = itemDetails.cdDocId?.id
                    val cdImporter = itemDetails.cdDocId?.cdImporter?.let { findCDImporterDetails(it) }
                    nameImporter = cdImporter?.name
                    address = cdImporter?.physicalAddress
                    telephone = cdImporter?.telephone
                    cdRefNo = itemDetails.cdDocId?.cdStandard?.applicationRefNo
                    //todo: Entry Number
                    entryAblNumber = "UNKNOWN"
                    amountPayable = 0.00.toBigDecimal()
                    receiptNo = "NOT PAID"
                    product = "UNKNOWN"
                    rate = "UNKNOWN"
                    cfvalue = 0.00.toBigDecimal()


                    //Generate Demand note number
                    demandNoteNumber =
                        "KIMS${itemDetails.cdDocId?.cdType?.demandNotePrefix}${
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
                    status = map.activeStatus
                    createdOn = commonDaoServices.getTimestamp()
                    createdBy = commonDaoServices.getUserName(user)
                }
                demandNote = iDemandNoteRepo.save(demandNote)
                //Call Function to add Item Details To be attached To The Demand note
                addItemDetailsToDemandNote(itemDetails, demandNote, map, user)
                //Calculate the total Amount for Items In one Cd Tobe paid For
                demandNote = calculateTotalAmountDemandNote(demandNote, map, user)
                itemDetails.dnoteStatus = map.activeStatus
                return demandNoteUpDatingCDAndItem(itemDetails, user, demandNote)

            }

    }


    fun demandNoteUpDatingCDAndItem(
        itemDetails: CdItemDetailsEntity,
        user: UsersEntity,
        demandNote: CdDemandNoteEntity
    ): CdDemandNoteEntity {
        (updateCdItemDetailsInDB(itemDetails, user).cdDocId
            ?.let { cdDetails ->
                cdDetails.cdStandard?.let { updateCDStatus(it, awaitPaymentStatus.toLong()) }
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

        var itemDetails = dNote.itemId?.id?.let { findItemWithItemID(it) }
        if (itemDetails != null) {
            itemDetails = updateItemCdStatus(itemDetails, paymentMadeStatus.toLong())
            with(itemDetails) {
                paymentMadeStatus = map.activeStatus
                lastModifiedBy = "SYSTEM"
                lastModifiedOn = commonDaoServices.getTimestamp()
            }
            iCdItemsRepo.save(itemDetails)
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
        return iDemandNoteRepo.save(demandNote)
    }


    @Throws(JSONException::class)
    fun convert(json: String?, root: String): String? {
        val jsonObject = JSONObject(json)
        return """<?xml version="1.0" encoding="ISO-8859-15"?> <$root>${XML.toString(jsonObject)}</$root>"""
    }

    fun updateCDStatus(cdStandard: CdStandardsEntity, statusValue: Long): Boolean {
        var updateCD = cdStandard
        with(updateCD) {
            approvalStatus = findCdStatusValue(statusValue).typeName
            approvalDate = commonDaoServices.getCurrentDate().toString()
        }
        updateCD = iCdStandardsRepo.save(updateCD)
        KotlinLogging.logger { }.info { "CD UPDATED STATUS TO = ${updateCD.approvalStatus}" }

        return true
    }

    fun updateItemCdStatus(item: CdItemDetailsEntity, statusValue: Long): CdItemDetailsEntity {
        with(item) {
            itemStatus = findCdStatusValue(statusValue).typeName
        }
        return item
    }

    fun findDIFee(feeId: Long): DestinationInspectionFeeEntity {
        iDIFeeDetailsRepo.findByIdOrNull(feeId)
            ?.let { diFeeDetails ->
                return diFeeDetails
            }
            ?: throw Exception("DI fee Details with ID = ${feeId}, does not Exist")
    }

    fun findDemandNote(item: CdItemDetailsEntity): CdDemandNoteEntity? {
        return iDemandNoteRepo.findByItemId(item)
//                ?.let { demandNoteDetails ->
//                    return demandNoteDetails
//                }
//                ?: throw Exception("Demand Note Details with [Item ID = ${item.id}], does not Exist")
    }

    fun findDemandNoteWithCdID(cdID: Long): CdDemandNoteEntity? {
        return iDemandNoteRepo.findByCdId(cdID)
//                ?.let { demandNoteDetails ->
//                    return demandNoteDetails
//                }
//                ?: throw Exception("Demand Note Details with [Item ID = ${item.id}], does not Exist")
    }

    fun findDemandNoteItemDetails(demandNoteID: Long): List<CdDemandNoteItemsDetailsEntity>? {
        return iDemandNoteItemRepo.findByDemandNoteId(demandNoteID)
//                ?.let { demandNoteDetails ->
//                    return demandNoteDetails
//                }
//                ?: throw Exception("Demand Note Details with [Item ID = ${item.id}], does not Exist")
    }

    fun findDemandNoteWithPaymentStatus(item: CdItemDetailsEntity, status: Int): CdDemandNoteEntity? {
        iDemandNoteRepo.findByItemIdAndPaymentStatus(item, status)
            ?.let { demandNoteDetails ->
                return demandNoteDetails
            }
            ?: throw Exception("Demand Note Details with [Item ID = ${item.id} and Status ${status}], does not Exist")
    }

//    fun findDemandNoteByCDID(invoiceBatchId:Long): List<CdDemandNoteEntity> {
//        iDemandNoteRepo.findByInvoiceBatchNumberId(invoiceBatchId)
//                ?.let { demandNoteDetails ->
//                    return demandNoteDetails
//                }
//                ?: throw Exception("Demand Note Details with [Invoice Batch ID = ${invoiceBatchId}], do not exist")
//    }

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
        inspectionGeneralEntity: CdInspectionGeneralEntity, itemDetails: CdItemDetailsEntity,
        user: UsersEntity, map: ServiceMapsEntity
    ): CdInspectionGeneralEntity {
        var inspectionGeneralChecklist = inspectionGeneralEntity
        with(inspectionGeneralChecklist) {
            checkListType = inspectionGeneralChecklist.confirmItemType?.let { findChecklistInspectionTypeById(it) }
            cdItemDetails = itemDetails
            status = map.activeStatus
            createdBy = commonDaoServices.getUserName(user)
            createdOn = commonDaoServices.getTimestamp()
        }
        inspectionGeneralChecklist = iCdInspectionGeneralRepo.save(inspectionGeneralChecklist)

        KotlinLogging.logger { }
            .info { "Inspection General CheckList Details saved ID = ${inspectionGeneralChecklist.id}" }
        return inspectionGeneralChecklist
    }

    fun saveInspectionAgrochemItemChecklist(
        inspectionAgrochemItemChecklistEntity: CdInspectionAgrochemItemChecklistEntity,
        inspectionGeneralEntity: CdInspectionGeneralEntity,
        user: UsersEntity, map: ServiceMapsEntity
    ): CdInspectionAgrochemItemChecklistEntity {
        var inspectionAgrochemItemChecklist = inspectionAgrochemItemChecklistEntity
        with(inspectionAgrochemItemChecklist) {
            inspectionGeneral = inspectionGeneralEntity
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
            inspectionGeneral = inspectionGeneralEntity
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
            inspectionGeneral = inspectionGeneralEntity
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
            inspectionGeneral = inspectionGeneralEntity
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

    fun findCdWithUcrNumberLatest(ucrNumber: String): ConsignmentDocumentDetailsEntity? =
        iConsignmentDocumentDetailsRepo.findTopByUcrNumberOrderByIdDesc(ucrNumber)
            ?.let {
                return it
            }

    fun createCDTransactionLog(
        map: ServiceMapsEntity,
        user: UsersEntity,
        CDID: Long,
        transRemarks: String,
        transProcess: String
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

        cdTransactionLog = iCdTransactionRepo.save(cdTransactionLog)

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


    fun findItemWithItemID(cdItemId: Long): CdItemDetailsEntity {
        iCdItemsRepo.findByIdOrNull(cdItemId)
            ?.let { cdItemDetails ->
                return cdItemDetails
            }
            ?: throw Exception("CD Item Details with the following ID= ${cdItemId}, do not Exist")
    }

    fun findItemWithUuid(uuid: String): CdItemDetailsEntity {
        iCdItemsRepo.findByUuid(uuid)
            ?.let { cdItemDetails ->
                return cdItemDetails
            }
            ?: throw Exception("CD Item Details with the following uuid= ${uuid}, do not Exist")
    }

    fun findInspectionGeneralWithItemDetails(cdItemDetails: CdItemDetailsEntity): CdInspectionGeneralEntity {
        iCdInspectionGeneralRepo.findFirstByCdItemDetails(cdItemDetails)
            ?.let { it ->
                return it
            }
            ?: throw Exception("Inspection General Checklist with CD Item ID= ${cdItemDetails.id}, do not Exist")
    }

    fun findInspectionMotorVehicleWithInspectionGeneral(cdInspectionGeneralEntity: CdInspectionGeneralEntity):
            CdInspectionMotorVehicleItemChecklistEntity? {
        return iCdInspectionMotorVehicleItemChecklistRepo.findByInspectionGeneral(cdInspectionGeneralEntity)
//                ?.let { it ->
//                    return it
//                }
//                ?: throw Exception("Motor Vehicle Inspection Checklist with General Checklist ID= ${cdInspectionGeneralEntity.id}, do not Exist")
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

    fun findInspectionAgrochemWithInspectionGeneral(cdInspectionGeneralEntity: CdInspectionGeneralEntity): CdInspectionAgrochemItemChecklistEntity {
        iCdInspectionAgrochemItemChecklistRepo.findByInspectionGeneral(cdInspectionGeneralEntity)
            ?.let { it ->
                return it
            }
            ?: throw Exception("Agrochem Inspection Checklist with General Checklist ID= ${cdInspectionGeneralEntity.id}, do not Exist")
    }

    fun findInspectionOthersWithInspectionGeneral(cdInspectionGeneralEntity: CdInspectionGeneralEntity): CdInspectionOtherItemChecklistEntity {
        iCdInspectionOtherItemChecklistRepo.findByInspectionGeneral(cdInspectionGeneralEntity)
            ?.let { it ->
                return it
            }
            ?: throw Exception("Other Inspection Checklist with General Checklist ID= ${cdInspectionGeneralEntity.id}, do not Exist")
    }

    fun findInspectionEngineeringWithInspectionGeneral(cdInspectionGeneralEntity: CdInspectionGeneralEntity): CdInspectionEngineeringItemChecklistEntity {
        iCdInspectionEngineeringItemChecklistRepo.findByInspectionGeneral(cdInspectionGeneralEntity)
            ?.let { it ->
                return it
            }
            ?: throw Exception("Engineering Inspection Checklist with General Checklist ID= ${cdInspectionGeneralEntity.id}, do not Exist")
    }

    fun findInspectionMotorVehicleById(id: Long): CdInspectionMotorVehicleItemChecklistEntity? {
        return iCdInspectionMotorVehicleItemChecklistRepo.findByIdOrNull(id)
    }

    fun findInspectionGeneralById(id: Long): CdInspectionGeneralEntity? {
        return iCdInspectionGeneralRepo.findByIdOrNull(id)
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

    fun updateCDDetailsWithCOCData(
        cocEntity: CocsEntity,
        cdDetailsEntity: ConsignmentDocumentDetailsEntity
    ): ConsignmentDocumentDetailsEntity {
        if (cocEntity.clean.equals("Y")) {
            with(cdDetailsEntity) {
                cdType = findCdTypeDetailsWithUuid(cocCdType)
                docTypeId = cocEntity.id
                cocNumber = cocEntity.cocNumber

            }
        } else if (cocEntity.clean.equals("N")) {
            with(cdDetailsEntity) {
                cdType = findCdTypeDetailsWithUuid(ncrCdType)
                docTypeId = cocEntity.id
                cocNumber = cocEntity.cocNumber
            }
            cdDetailsEntity.id?.let { automaticRejectCDNcr(it) }
        }
        return iConsignmentDocumentDetailsRepo.save(cdDetailsEntity)

    }

    fun updateCDDetailsWithCORData(
        corsBakEntity: CorsBakEntity,
        cdDetailsEntity: ConsignmentDocumentDetailsEntity
    ): ConsignmentDocumentDetailsEntity {
        KotlinLogging.logger { }.info { "CorsBakEntity = $corsBakEntity.id" }
        with(cdDetailsEntity) {
            cdType = findCdTypeDetailsWithUuid(corCdType)
            docTypeId = corsBakEntity.id
        }
        return iConsignmentDocumentDetailsRepo.save(cdDetailsEntity)
    }

    fun automaticRejectCDNcr(cdId: Long): Boolean {
        findCDItemsListWithCDID(findCD(cdId))
            .forEach { cdItemDetails ->
                with(cdItemDetails) {
                    rejectDate = commonDaoServices.getCurrentDate()
                    rejectStatus = commonDaoServices.activeStatus.toInt()
                    rejectReason = "Goods with NCR to be flagged and automatically rejected on the system"
//                        val myStatus = findCdStatusValue(rejectedStatus.toLong()).typeName
//                        if (myStatus == null) {
//                        } else {
//                            rejectedStatus = myStatus
//                        }
                    lastModifiedBy = "SYSTEM"
                    lastModifiedOn = commonDaoServices.getTimestamp()
                }
                cdItemDetails.cdDocId?.let {
                    it.cdStandard?.let { it1 ->
                        updateCDStatus(
                            it1,
                            rejectedStatus.toLong()
                        )
                    }
                }
                iCdItemsRepo.save(cdItemDetails)
                KotlinLogging.logger { }.info { "MY SYSTEM REJECTION  ${cdItemDetails.id}" }
            }
        return true
    }

    fun findCOC(ucrNumber: String): CocsEntity {
        cocRepo.findByUcrNumber(ucrNumber)
            ?.let { cocEntity ->
                return cocEntity
            }
            ?: throw Exception("COC Details with the following UCR NUMBER = ${ucrNumber}, does not Exist")
    }

    fun findCOCById(cocId: Long): CocsEntity? {
        return cocRepo.findByIdOrNull(cocId)
    }

    fun findCorById(corId: Long): CorsBakEntity? {
        return corsBakRepository.findByIdOrNull(corId)
    }

    fun findCocByUcrNumber(ucrNumber: String): CocsEntity? {
        return cocRepo.findByUcrNumber(ucrNumber)
    }

    fun findCOI(ucrNumber: String): CocsEntity {
        cocRepo.findByUcrNumber(ucrNumber)
            ?.let { coiEntity ->
                return coiEntity
            }
            ?: throw Exception("COI Details with the following UCR NUMBER = ${ucrNumber}, does not Exist")
    }

    fun saveCoc(cocDetails: CocsEntity): CocsEntity {
        return cocRepo.save(cocDetails)
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

    fun findAllOngoingCdWithPortOfEntry(
        sectionsEntity: SectionsEntity,
        cdType: ConsignmentDocumentTypesEntity
    ): List<ConsignmentDocumentDetailsEntity> {
        iConsignmentDocumentDetailsRepo.findByPortOfArrivalAndCdTypeAndUcrNumberIsNotNullAndOldCdStatusIsNullAndApproveRejectCdStatusIsNull(
            sectionsEntity.id,
            cdType.id
        )
            ?.let {
                return it
            }
            ?: throw Exception("COC List with the following Port arrival = ${sectionsEntity.section} and CD Type = ${cdType.typeName}, does not Exist")

    }

    fun findAllOngoingCdWithFreightStationID(
        cfsEntity: CfsTypeCodesEntity,
        cdType: ConsignmentDocumentTypesEntity
    ): List<ConsignmentDocumentDetailsEntity> {
        iConsignmentDocumentDetailsRepo.findByFreightStationAndCdTypeAndUcrNumberIsNotNullAndOldCdStatusIsNullAndApproveRejectCdStatusIsNull(
            cfsEntity.id,
            cdType
        )?.let {
            return it
        }
            ?: throw Exception("COC List with the following  Freight STATION = ${cfsEntity.cfsName} and CD Type = ${cdType.typeName}, does not Exist")

    }

    fun findAllOngoingCdWithFreightStationID(cfsEntity: CfsTypeCodesEntity): List<ConsignmentDocumentDetailsEntity> {
        iConsignmentDocumentDetailsRepo.findByFreightStationAndUcrNumberIsNotNullAndOldCdStatusIsNullAndApproveRejectCdStatusIsNull(
            cfsEntity.id
        )?.let {
            return it
        }
            ?: throw Exception("COC List with the following  Freight STATION = ${cfsEntity.cfsName}, does not Exist")
    }

    fun findAllOngoingCdWithPortOfEntry(
        sectionsEntity: SectionsEntity
    ): List<ConsignmentDocumentDetailsEntity> {
        iConsignmentDocumentDetailsRepo.findByPortOfArrivalAndUcrNumberIsNotNullAndOldCdStatusIsNullAndApproveRejectCdStatusIsNull(
            sectionsEntity.id
        )
            ?.let {
                return it
            }
            ?: throw Exception("COC List with the following Port arrival = ${sectionsEntity.section}, does not Exist")

    }

    fun findAllCompleteCdWithPortOfEntry(
        sectionsEntity: SectionsEntity,
        cdType: ConsignmentDocumentTypesEntity
    ): List<ConsignmentDocumentDetailsEntity> {
        iConsignmentDocumentDetailsRepo.findByPortOfArrivalAndCdTypeAndUcrNumberIsNotNullAndOldCdStatusIsNullAndApproveRejectCdStatusIsNotNull(
            sectionsEntity.id,
            cdType.id
        )
            ?.let {
                return it
            }
            ?: throw Exception("COC List with the following Port arrival = ${sectionsEntity.section} and CD Type = ${cdType.typeName}, does not Exist")

    }

    fun findAllCompleteCdWithPortOfEntry(
        sectionsEntity: SectionsEntity
    ): List<ConsignmentDocumentDetailsEntity> {
        iConsignmentDocumentDetailsRepo.findByPortOfArrivalAndUcrNumberIsNotNullAndOldCdStatusIsNullAndApproveRejectCdStatusIsNotNull(
            sectionsEntity.id
        )
            ?.let {
                return it
            }
            ?: throw Exception("COC List with the following Port arrival = ${sectionsEntity.section}, does not Exist")

    }

    fun findAllCompleteCdWithFreightStation(
        cfsEntity: CfsTypeCodesEntity
    ): List<ConsignmentDocumentDetailsEntity> {
        iConsignmentDocumentDetailsRepo.findByFreightStationAndUcrNumberIsNotNullAndOldCdStatusIsNullAndApproveRejectCdStatusIsNotNull(
            cfsEntity.id
        )?.let {
            return it
        }
            ?: throw Exception("COC List with the following Freight Station = ${cfsEntity.cfsName}, does not Exist")

    }

    fun findAllCdWithNoPortOfEntry(cdType: ConsignmentDocumentTypesEntity): List<ConsignmentDocumentDetailsEntity>? {
        return iConsignmentDocumentDetailsRepo.findByPortOfArrivalIsNullAndCdTypeAndUcrNumberIsNotNullAndOldCdStatusIsNull(
            cdType.id
        )
    }

    fun findAllCdWithNoFreghitStation(cdType: ConsignmentDocumentTypesEntity): List<ConsignmentDocumentDetailsEntity>? {
        return iConsignmentDocumentDetailsRepo.findByFreightStationIsNullAndCdTypeAndUcrNumberIsNotNullAndOldCdStatusIsNull(
            cdType
        )
    }

    fun findAllCdWithNoFreightStation(): List<ConsignmentDocumentDetailsEntity>? {
        return iConsignmentDocumentDetailsRepo.findByFreightStationIsNullAndUcrNumberIsNotNullAndOldCdStatusIsNull()
    }

    fun findAllCdWithNoPortOfEntry(): List<ConsignmentDocumentDetailsEntity>? {
        return iConsignmentDocumentDetailsRepo.findByPortOfArrivalIsNullAndUcrNumberIsNotNullAndOldCdStatusIsNull()
    }

    fun findAllCdWithAssignedIoID(
        usersEntity: UsersEntity,
        cdType: ConsignmentDocumentTypesEntity
    ): List<ConsignmentDocumentDetailsEntity> {
        iConsignmentDocumentDetailsRepo.findAllByAssignedInspectionOfficerAndCdTypeAndUcrNumberIsNotNullAndOldCdStatusIsNullAndApproveRejectCdStatusIsNull(
            usersEntity,
            cdType
        )
            ?.let {
                return it
            }
            ?: throw Exception("Assigned Inspection Officer with ID = ${usersEntity.id}, does not Exist")
    }

    fun findAllCdWithAssignedIoID(
        usersEntity: UsersEntity
    ): List<ConsignmentDocumentDetailsEntity> {
        iConsignmentDocumentDetailsRepo.findAllByAssignedInspectionOfficerAndUcrNumberIsNotNullAndOldCdStatusIsNullAndApproveRejectCdStatusIsNull(
            usersEntity
        )
            ?.let {
                return it
            }
            ?: throw Exception("Assigned Inspection Officer with ID = ${usersEntity.id}, does not Exist")
    }

    fun findAllCompleteCdWithAssignedIoID(
        usersEntity: UsersEntity,
        cdType: ConsignmentDocumentTypesEntity
    ): List<ConsignmentDocumentDetailsEntity> {
        iConsignmentDocumentDetailsRepo.findAllByAssignedInspectionOfficerAndCdTypeAndUcrNumberIsNotNullAndOldCdStatusIsNullAndApproveRejectCdStatusIsNotNull(
            usersEntity,
            cdType
        )
            ?.let {
                return it
            }
            ?: throw Exception("Assigned Inspection Officer with ID = ${usersEntity.id}, does not Exist")
    }

    fun findAllCompleteCdWithAssignedIoID(
        usersEntity: UsersEntity
    ): List<ConsignmentDocumentDetailsEntity> {
        iConsignmentDocumentDetailsRepo.findAllByAssignedInspectionOfficerAndUcrNumberIsNotNullAndOldCdStatusIsNullAndApproveRejectCdStatusIsNotNull(
            usersEntity
        )
            ?.let {
                return it
            }
            ?: throw Exception("Assigned Inspection Officer with ID = ${usersEntity.id}, does not Exist")
    }


    fun findAllCdWithNoAssignedIoID(
        cfsEntity: CfsTypeCodesEntity,
        cdType: ConsignmentDocumentTypesEntity
    ): List<ConsignmentDocumentDetailsEntity>? {
        return iConsignmentDocumentDetailsRepo.findByFreightStationAndAssignedInspectionOfficerIsNullAndCdTypeAndUcrNumberIsNotNullAndOldCdStatusIsNull(
            cfsEntity.id,
            cdType
        )
    }

    fun findAllCdWithNoAssignedIoID(cfsEntity: CfsTypeCodesEntity): List<ConsignmentDocumentDetailsEntity>? {
        return iConsignmentDocumentDetailsRepo.findByFreightStationAndAssignedInspectionOfficerIsNullAndUcrNumberIsNotNullAndOldCdStatusIsNull(
            cfsEntity.id
        )
    }

    fun findAllCdWithNoAssignedIoID(
        subSectionsLevel2Entity: SubSectionsLevel2Entity
    ): List<ConsignmentDocumentDetailsEntity>? {
        return iConsignmentDocumentDetailsRepo.findByFreightStationAndAssignedInspectionOfficerIsNullAndUcrNumberIsNotNullAndOldCdStatusIsNull(
            subSectionsLevel2Entity.id
        )
    }

    fun addFreightStation(freightStation: String, status: Int): SubSectionsLevel2Entity =
        iSubSectionsLevel2Repo.findBySubSectionAndStatus(freightStation, status)
            ?.let { subSectionsLevel2Entity ->
                return subSectionsLevel2Entity
            }
            ?: throw Exception("The freight station with name = $freightStation and status = ${status}, does not Exist")


    fun findFreightStation(freightStationID: Long): SubSectionsLevel2Entity =
        iSubSectionsLevel2Repo.findByIdOrNull(freightStationID)
            ?.let { subSectionsLevel2Entity ->
                return subSectionsLevel2Entity
            }
            ?: throw Exception("The freight station with ID = $freightStationID, does not Exist")

    fun addPortOfArrival(freightStation: SubSectionsLevel2Entity): SectionsEntity {
        freightStation.sectionId
            ?.let { sectionsEntity ->
                return sectionsEntity
            }
            ?: throw Exception("The Port of arrival for freight Station with name = ${freightStation.subSection}, does not Exist")
    }

//    fun findPortOfArrival(freightStation: SubSectionsLevel2Entity): SectionsEntity {
//        freightStation.sectionId
//                ?.let { sectionsEntity ->
//                    return sectionsEntity
//                }
//                ?: throw Exception("The Port of arrival for freight Station with name = ${freightStation.subSection}, does not Exist")
//    }


    fun findOfficersList(consignmentDocumentDetailsEntity: ConsignmentDocumentDetailsEntity): List<UserProfilesEntity> {
        consignmentDocumentDetailsEntity.freightStation
            ?.let { subSectionsLevel2Entity ->
                iUserProfilesRepo.findBySubSectionL2IdAndStatus(
                    commonDaoServices.findSectionLevel2WIthId(
                        subSectionsLevel2Entity
                    ), commonDaoServices.activeStatus.toInt()
                )
                    ?.let { userProfileList ->
                        return userProfileList
                    }
                    ?: throw ServiceMapNotFoundException("Users with Freight Station with ID = $subSectionsLevel2Entity and Status = ${commonDaoServices.activeStatus}, Does not exist")
            }
            ?: throw ServiceMapNotFoundException("Freight Station details on consignment with ID = ${consignmentDocumentDetailsEntity.id}, is Empty")
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

//    fun loopAllItemsInCDToBeTargeted(
//        cdId: Long,
//        cdDetails: ConsignmentDocumentDetailsEntity,
//        s: ServiceMapsEntity,
//        user: UsersEntity
//    ): Boolean {
//        findCDItemsListWithCDID(findCD(cdId))
//            .forEach { cdItemDetails ->
//                with(cdItemDetails) {
//                    targetApproveDate = commonDaoServices.getCurrentDate()
//                    targetApproveRemarks = cdDetails.blacklistApprovedRemarks
//                    targetApproveStatus = s.activeStatus
//                    targetStatus = s.activeStatus
//                    targetReason = cdDetails.blacklistApprovedRemarks
//                    targetDate = commonDaoServices.getCurrentDate()
//                    inspectionNotificationDate = commonDaoServices.getCurrentDate()
//                    inspectionNotificationStatus = s.activeStatus
//                }
//
//                notifyKRATargetedItem(updateCdItemDetailsInDB(cdItemDetails, user), s, user)
//
//
//            }
//        return true
//    }


    fun updateCdItemDetailsInDB(updateCDItem: CdItemDetailsEntity, user: UsersEntity): CdItemDetailsEntity {
        with(updateCDItem) {
            lastModifiedBy = commonDaoServices.getUserName(user)
            lastModifiedOn = commonDaoServices.getTimestamp()
        }
        KotlinLogging.logger { }.info { "MY UPDATED ITEM ID =  ${updateCDItem.id}" }
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


    fun findAllCFSUserList(userProfileID: Long): List<UsersCfsAssignmentsEntity> {
        usersCfsRepo.findByUserProfileId(userProfileID)
            ?.let {
                return it
            }
            ?: throw ServiceMapNotFoundException("NO USER CFS FOUND WITH PROFILE ID = ${userProfileID}")
    }

    fun findSavedChecklist(itemId: Long): CdInspectionChecklistEntity {
        iCdInspectionChecklistRepo.findByItemId(itemId)
            ?.let { checklist ->
                return checklist
            }
            ?: throw ServiceMapNotFoundException("CheckList for ITEM with ID = ${itemId}, Does not exist")
    }

    fun findSavedGeneralInspection(itemId: CdItemDetailsEntity): CdInspectionGeneralEntity {
        iCdInspectionGeneralRepo.findFirstByCdItemDetails(itemId)
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

    fun findSavedSampleSubmissionParameterList(paramId: Long): List<CdSampleSubmissionParamatersEntity> {
        iSampleSubmissionParamRepo.findByIdAndStatus(paramId, commonDaoServices.activeStatus.toInt())
            ?.let { sampleSubmittedParameter ->
                return sampleSubmittedParameter
            }
            ?: throw ServiceMapNotFoundException("Parameter with ID = ${paramId}, Does not exist")
    }

//    fun notifyKRATargetedItem(CDItem: CdItemDetailsEntity, map: ServiceMapsEntity, user: UsersEntity): Boolean {
//        // TODO: Logic for notifying KRA for inspection
//        var item = CDItem
//        with(item) {
//            inspectionRemarks = "Agreed on the item to be inspected "
//            inspectionDate = commonDaoServices.getCalculatedDate(7)
//            inspectionDateSetStatus = map.activeStatus
//        }
//        item = updateCdItemDetailsInDB(item, user)
//
////        val subject = "INSPECTION SCHEDULED"
////        val messageBody = "We have scheduled the inspection Date for CD with UCR NUMBER = ${item.cdDocId?.ucrNumber} and ITEM with number =${item.itemNo} , to the following Date......${item.inspectionDate} \n" +
////                "\n " +
////                "https://localhost:8006/api/di/cd-item-details?cdItemUuid=${item.id}"
////        item.cdDocId?.assignedInspectionOfficer?.email?.let { notifications.sendEmail(it, subject, messageBody) }
//        return true
//    }

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

    fun updateCdDetailsInDB(
        updateCD: ConsignmentDocumentDetailsEntity,
        user: UsersEntity
    ): ConsignmentDocumentDetailsEntity {
        with(updateCD) {
            modifiedBy = commonDaoServices.getUserName(user)
            modifiedOn = commonDaoServices.getTimestamp()
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


    fun updateCDSampleParamsDetails(
        sampleSubmitParamEntity: CdSampleSubmissionParamatersEntity,
        paramID: Long,
        user: UsersEntity,
        s: ServiceMapsEntity
    ): CdSampleSubmissionParamatersEntity {
        // Getting an Object with fields that user Has Updated that are needed to be updated to the database
        JSONObject(ObjectMapper().writeValueAsString(sampleSubmitParamEntity))
            .let { addValues ->
                // Creating of a json object that can be user to map the details from Database with the updated fields from user
                JSONObject(ObjectMapper().writeValueAsString(findSavedSampleSubmissionParameter(paramID)))
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
                        ObjectMapper().readValue(JCD.toString(), CdSampleSubmissionParamatersEntity::class.java)
                            .let { updateParam ->
                                return updateCdItemSampleParamDetailsInDB(updateParam, user)
                            }

                    }
            }
    }

    fun makeItemNotBeNull(itemId: Long, itemDetails: CdItemDetailsEntity): CdItemDetailsEntity {
        with(itemDetails) {
            this.id = itemId
        }
        return itemDetails
    }

    fun checkIfChecklistUndergoesSampling(
        sampled: String,
        item: CdItemDetailsEntity,
        map: ServiceMapsEntity
    ): CdItemDetailsEntity? {
        var updateItem = item
        when (sampled) {
            "YES" -> {
                updateItem.sampledStatus = map.activeStatus
            }
            "NO" -> {
                updateItem = updateItemNoSampling(item, map)
            }
        }
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

    fun updateDemandNotePayment(demandNoteNumber: String, amount: BigDecimal, user: UsersEntity) =//Get the demandNote
        iDemandNoteRepo.findByDemandNoteNumber(demandNoteNumber)
            ?.let { demandNote ->
                demandNote.amountPayable?.let {
                    //Amount paid is less than amount payable
                    if (it < amount) {
                        throw InvalidInputException("Amount paid is less than payable amount of: $it")
                    }
                    demandNote.paymentStatus = 1
                    upDateDemandNoteWithUser(demandNote, user)
                    //Get the consignment document
                    demandNote.itemId?.cdDocId?.cdStandard?.applicationDate?.let {
                        //TODO: Update CD status to previous one
                    }
                }
            } ?: throw InvalidInputException("Demand note with Number = ${demandNoteNumber}, Does not exist")

    fun findAllMinistryInspectionRequests(status: Int): List<CdItemDetailsEntity> {
        iCdItemsRepo.findByMinistrySubmissionStatus(status)
            ?.let { ministryInspectionItems ->
                return ministryInspectionItems
            }
            ?: throw ExpectedDataNotFound("Ministry Inspection requests with status = $status, do not exist")
    }

    fun findAllOngoingMinistryInspectionRequests(): List<CdItemDetailsEntity> {
            iCdItemsRepo.findOngoingMinistrySubmissions()
                ?.let { ministryInspectionItems ->
                    return ministryInspectionItems
                }
                ?: throw ExpectedDataNotFound("Ongoing Ministry Inspection requests, do not exist")
    }
    fun findAllCompleteMinistryInspectionRequests(): List<CdItemDetailsEntity> {
        iCdItemsRepo.findCompletedMinistrySubmissions()
            ?.let { ministryInspectionItems ->
                return ministryInspectionItems
            }
            ?: throw ExpectedDataNotFound("Complete Ministry Inspection requests, do not exist")
    }

    fun updateCdInspectionMotorVehicleItemChecklistInDB(
        cdInspectionMotorVehicleItemChecklistEntity: CdInspectionMotorVehicleItemChecklistEntity,
        user: UsersEntity
    ): CdInspectionMotorVehicleItemChecklistEntity {
        with(cdInspectionMotorVehicleItemChecklistEntity) {
            modifiedBy = commonDaoServices.getUserName(user)
            modifiedOn = commonDaoServices.getTimestamp()
        }
        KotlinLogging.logger { }
            .info { "CdInspectionMotorVehicleItemChecklistEntity UPDATED ITEM ID =  ${cdInspectionMotorVehicleItemChecklistEntity.id}" }
        return iCdInspectionMotorVehicleItemChecklistRepo.save(cdInspectionMotorVehicleItemChecklistEntity)
    }

    fun updateCdInspectionGeneralChecklistInDB(
        cdInspectionGeneralEntity: CdInspectionGeneralEntity,
        user: UsersEntity
    ): CdInspectionGeneralEntity {
        with(cdInspectionGeneralEntity) {
            modifiedBy = commonDaoServices.getUserName(user)
            modifiedOn = commonDaoServices.getTimestamp()
        }
        KotlinLogging.logger { }.info { "CdInspectionGeneralEntity UPDATED with ID =  ${cdInspectionGeneralEntity.id}" }
        return iCdInspectionGeneralRepo.save(cdInspectionGeneralEntity)
    }

    fun findMotorVehicleInspectionByCdItem(cdItem: CdItemDetailsEntity): CdInspectionMotorVehicleItemChecklistEntity? {
        iCdInspectionGeneralRepo.findFirstByCdItemDetails(cdItem)?.let { inspectionGeneral ->
            return findInspectionMotorVehicleWithInspectionGeneral(inspectionGeneral)
        }
        return null
    }

    fun findLocalCorByUcrNumber(ucrNumber: String): CdLocalCorEntity {
        iLocalCorRepo.findByUcrNumber(ucrNumber)
            ?.let { cocEntity ->
                return cocEntity
            }
            ?: throw Exception("Local Cor Details with the following UCR NUMBER = ${ucrNumber}, does not Exist")
    }

    fun createLocalCorReportMap(cdDetails: ConsignmentDocumentDetailsEntity): HashMap<String, Any> {
        val map = hashMapOf<String, Any>()
        val importerDetails = cdDetails.cdImporter?.let { findCDImporterDetails(it) }
        //Importer Details
        map["ImporterName"] = importerDetails?.name.orEmpty()
        map["ImporterAddress"] = importerDetails?.physicalAddress.orEmpty()
        map["ImporterPhone"] = importerDetails?.telephone.orEmpty()

        //COR Details
        map["CorSerialNo"] = ""
        map["CorIssueDate"] = commonDaoServices.convertDateToString(commonDaoServices.getCurrentDate(), "mm/dd/yyyy")
        map["CorExpiryDate"] = commonDaoServices.convertDateToString(commonDaoServices.addMonthsToCurrentDate(3), "mm/dd/yyyy")

        val cdItem = findCDItemsListWithCDID(cdDetails)[0]
        val itemNonStandardDetail = findCdItemNonStandardByItemID(cdItem)
        val corDetails = itemNonStandardDetail?.chassisNo?.let { findCORByChassisNumber(it) }

        //Vehicle Details
        map["ChassisNumber"] = corDetails?.chasisNumber.orEmpty()
        map["EngineNo"] = corDetails?.engineNumber.orEmpty()
        map["EngineCap"] = corDetails?.engineCapacity.orEmpty()
        map["Yom"] = corDetails?.yearOfManufacture.orEmpty()
        map["Yor"] = corDetails?.yearOfFirstRegistration.orEmpty()
        map["CustomsIeNo"] = ""
        map["Cos"] = corDetails?.countryOfSupply.orEmpty()
        map["BodyType"] = corDetails?.typeOfBody.orEmpty()
        map["Fuel"] = corDetails?.fuelType.orEmpty()
        map["TareWeight"] = corDetails?.tareWeight.toString()
        map["VehicleType"] = corDetails?.typeOfVehicle.orEmpty()
        map["ExporterName"] = corDetails?.exporterName.orEmpty()
        map["ExporterAddress"] = corDetails?.exporterAddress1.orEmpty()
        map["ExporterEmail"] = corDetails?.exporterEmail.orEmpty()
        map["IdfNumber"] = ""
        map["UcrNumber"] = corDetails?.ucrNumber.orEmpty()
        map["Make"] = corDetails?.make.orEmpty()
        map["Model"] = corDetails?.model.orEmpty()
        map["InspectedMileage"] = corDetails?.inspectionMileage.orEmpty()
        map["UnitsOfMileage"] = corDetails?.unitsOfMileage.orEmpty()
        map["Color"] = corDetails?.bodyColor.orEmpty()
        map["AxleNo"] = corDetails?.numberOfAxles.toString()
        map["Transmision"] = ""
        map["NoOfPassengers"] = corDetails?.numberOfPassangers.toString()
        map["PrevRegNo"] = corDetails?.previousCountryOfRegistration.orEmpty()
        map["PrevCountryOfReg"] = corDetails?.previousCountryOfRegistration.orEmpty()

        val inspectionChecklist = findMotorVehicleInspectionByCdItem(cdItem)
        map["InspectionDetails"] = inspectionChecklist?.remarks.orEmpty()

        return map
    }

    fun createLocalCocReportMap(localCocEntity: CocsEntity): HashMap<String, Any> {
        val map = hashMapOf<String, Any>()

        map["CocNo"] = localCocEntity.cocNumber.orEmpty()
        map["IssueDate"] = localCocEntity.cocIssueDate.toString()
        map["EntryNo"] = ""
        map["IdfNo"] = ""
        map["ImporterName"] = localCocEntity.importerName.orEmpty()
        map["ImporterAddress"] = localCocEntity.importerAddress1.orEmpty()
        map["ImporterPin"] = localCocEntity.importerPin.orEmpty()
        map["ClearingAgent"] = ""
        map["PortOfEntry"] = localCocEntity.portOfDestination.orEmpty()
        map["UcrNo"] = localCocEntity.ucrNumber.orEmpty()
        map["Status"] = localCocEntity.status.toString()
        map["Remarks"] = localCocEntity.cocRemarks.toString()
        map["CoCType"] = localCocEntity.cocType.toString()

        return map
    }

    //Start relevant BPMN process
    fun startDiBpmProcessByCdType(consignmentDoc: ConsignmentDocumentDetailsEntity) {
        consignmentDoc.cdType?.let {
            it.uuid?.let { cdTypeUuid ->
                when (cdTypeUuid) {
                    corCdType -> consignmentDoc.id?.let { it1 ->
                        consignmentDoc.assignedInspectionOfficer?.id?.let { it2 ->
                            diBpmn.startImportedVehiclesWithCorProcess(
                                it1,
                                it2
                            )
                        }
                    }
                    else -> {
                        KotlinLogging.logger { }.error("CD type uuid not found for CD: $consignmentDoc.id")
                    }
                }
            }
        }
    }

    //BPM: Assign officer
    fun assignIOBpmTask(consignmentDoc: ConsignmentDocumentDetailsEntity) {
        //Check if any item in consignment has been targeted
        var supervisorTargetStatus = 0
//        iCdItemsRepo.findByCdDocId(consignmentDoc)?.let { cdItemDetailsList ->
//            val itemsTargeted =
//                cdItemDetailsList.filter { item -> item.targetStatus == commonDaoServices.activeStatus.toInt() }
//            KotlinLogging.logger { }.info("No of targeted items: ${itemsTargeted.size}")
//            if (itemsTargeted.isNotEmpty()) {
//                supervisorTargetStatus = 1
//            }
//        }
        // Assign IO complete task
        consignmentDoc.id?.let { it1 ->
            consignmentDoc.assignedInspectionOfficer?.id?.let { it2 ->
                diBpmn.diAssignIOComplete(
                    it1,
                    it2,
                    supervisorTargetStatus
                )
            }
        }
    }

    //Update Inspection Notification status & date after KRA submission
//    fun updateInspectionNotificationSent(cdItemId: Long) {
//        iCdItemsRepo.findByIdOrNull(cdItemId)?.let { cdItem ->
//            with(cdItem) {
//                inspectionNotificationDate = commonDaoServices.getCurrentDate()
//                inspectionNotificationStatus = commonDaoServices.activeStatus.toInt()
//            }
//            iCdItemsRepo.save(cdItem)
//        }
//    }

    //Update Inspection schedule received from KRA
//    fun updateInspectionScheduleReceived(cdItem: CdItemDetailsEntity, receivedInspectionDate: Date) {
//        with(cdItem) {
//            inspectionDate = receivedInspectionDate
//            inspectionDateSetStatus = commonDaoServices.activeStatus.toInt()
//        }
//        iCdItemsRepo.save(cdItem)
//    }

    //Find all demand notes by payment status
    fun findAllDemandNotesWithPaidStatus(paymentStatus: Int): List<CdDemandNoteEntity>? {
        return iDemandNoteRepo.findAllByPaymentStatus(paymentStatus)
    }

    //Trigger demand note paid bpm task
    fun triggerDemandNotePaidBpmTask(demandNote: CdDemandNoteEntity): Boolean {
        //Get Item from Demand Note
        demandNote.itemId?.let { cdItemDetailsEntity ->
            //Get the CD details entity
            cdItemDetailsEntity.cdDocId?.let { consignmentDocumentDetailsEntity ->
                consignmentDocumentDetailsEntity.id?.let {
                    consignmentDocumentDetailsEntity.assignedInspectionOfficer?.id?.let { it1 ->
                        return diBpmn.diReceivePaymentConfirmation(
                            it,
                            it1
                        )
                    }
                }
            }
        }
        return false
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
            val cdApprovalResponseDTO = CDApprovalResponseDTO()
            cdApprovalResponseDTO.documentHeader = docHeader
            cdApprovalResponseDTO.documentDetails = docDetails

            val fileName =
                commonDaoServices.createKesWsFileName(applicationMapProperties.mapKeswsCdApprovalDoctype, cdNumber)
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

        val fileName = commonDaoServices.createKesWsFileName(applicationMapProperties.mapKeswsOnHoldDoctype, declarationRefNo)

        val xmlFile = fileName.let { commonDaoServices.serializeToXml(fileName, cdVerificationRequestXmlDTO) }

        xmlFile.let { it1 -> sftpService.uploadFile(it1) }
    }

    //Send CD status to KeSWS
    fun submitCoRToKesWS(corsBakEntity: CorsBakEntity) {
        val cor: CustomCorXmlDto = corsBakEntity.toCorXmlRecordRefl()
        val corDto = CORXmlDTO()
        corDto.cor = cor
        val fileName = corDto.cor?.chasisNumber?.let {
            commonDaoServices.createKesWsFileName(
                applicationMapProperties.mapKeswsCorDoctype,
                it
            )
        }
        val xmlFile = fileName?.let { commonDaoServices.serializeToXml(it, corDto) }
        xmlFile?.let { it1 -> sftpService.uploadFile(it1) }
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

                cdDetails.cdStandard?.let { cdStd ->
                    updateCDStatus(cdStd, applicationMapProperties.mapDIStatusTypeKraVerificationApprovedId)
                }

            } ?: KotlinLogging.logger { }.info { "Consignment document for declaration: ${declarationVerificationDocumentMessage.data?.dataIn?.sad?.sadId} not found" }
        }
    }

    fun sendLocalCocReportEmail(recipientEmail: String, filePath: String): Boolean {
        val subject = "Local COC Certificate"
        val messageBody =
            "Hello,  \n" +
                    "\n " +
                    "Find attached the Local COC Certificate."
        notifications.sendEmail(recipientEmail, subject, messageBody, filePath)
        return true
    }

    fun sendLocalCorReportEmail(recipientEmail: String, filePath: String): Boolean {
        val subject = "Local COR Certificate"
        val messageBody =
            "Hello,  \n" +
                    "\n " +
                    "Find attached the Local COR Certificate."
        notifications.sendEmail(recipientEmail, subject, messageBody, filePath)
        return true
    }

    fun convertCdItemDetailsToMinistryInspectionListResponseDto(cdItemDetails: CdItemDetailsEntity): MinistryInspectionListResponseDto {
        var ministryInspectionItem  = MinistryInspectionListResponseDto()
        ministryInspectionItem.cdId = cdItemDetails.cdDocId?.id!!
        ministryInspectionItem.cdUcr = cdItemDetails.cdDocId?.ucrNumber
        ministryInspectionItem.cdItemDetailsId = cdItemDetails.id
        this.findCdItemNonStandardByItemID(cdItemDetails)?.let { cdItemNonStandard ->
            ministryInspectionItem.chassis = cdItemNonStandard.chassisNo
            ministryInspectionItem.used = cdItemNonStandard.usedIndicator
            ministryInspectionItem.year = cdItemNonStandard.vehicleYear
            ministryInspectionItem.model = cdItemNonStandard.vehicleModel
            ministryInspectionItem.make = cdItemNonStandard.vehicleMake
        }
        return ministryInspectionItem
    }
}
