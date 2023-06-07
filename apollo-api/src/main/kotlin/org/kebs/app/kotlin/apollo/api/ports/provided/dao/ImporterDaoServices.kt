package org.kebs.app.kotlin.apollo.api.ports.provided.dao

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.utils.generateRandomText
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.model.di.CdItemDetailsEntity
import org.kebs.app.kotlin.apollo.store.model.di.ConsignmentDocumentDetailsEntity
import org.kebs.app.kotlin.apollo.store.model.importer.*
import org.kebs.app.kotlin.apollo.store.repo.*
import org.kebs.app.kotlin.apollo.store.repo.importer.*
import org.kebs.app.kotlin.apollo.store.repo.di.*
import org.kebs.app.kotlin.apollo.store.repo.di.IConsignmentItemsRepository
import org.springframework.beans.factory.annotation.Value

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile


@Service
class ImporterDaoServices(
    private val destinationInspectionRepo: IDestinationInspectionRepository,
    private val iConsignmentItemsRepo: IConsignmentItemsRepository,
    private val serviceMapsRepository: IServiceMapsRepository,
    private val applicationMapProperties: ApplicationMapProperties,
    private val iRfcDocumentDetailsRepo: IRfcDocumentsDetailsRepository,
    private val iCsApprovalApplicationsRepo: ICsApprovalApplicationsRepository,
    private val iCsApprovalApplicationsUploadsRepo: ICsApprovalApplicationsUploadsRepository,
    private val iTemporaryImportApplicationsRepo: ITemporaryImportApplicationsRepository,
    private val iTemporaryImportApplicationsUploadsRepo: ITemporaryImportApplicationsUploadsRepository,
    private val iRfcCoiItemsDetailsRepo: IRfcCoiItemsDetailsRepository,
    private val iImporterDiApplicationsTypesRepo: IImporterDiApplicationsTypesRepository,
    private val iRfcTypesRepo: IRfcTypesTypesRepository,
    private val commonDaoServices: CommonDaoServices,
    private val destinationInspectionDaoServices: DestinationInspectionDaoServices,

    private val iUsersRepo: IUserRepository,
    private val iUserProfilesRepo: IUserProfilesRepository,
    private val idfRepo: IIdfsRepository

) {

    @Value("\${importer.rfc.coc.type.id}")
    lateinit var rfcCOCTypeID: String

    @Value("\${importer.rfc.coi.type.id}")
    lateinit var rfcCOITypeID: String

    @Value("\${importer.rfc.cor.type.id}")
    lateinit var rfcCORTypeID: String

//    @Value("\${importer.di.application.cs.approval.type.id}")
//    lateinit var diApplicationCsApprovalTypeID: String
//
//    @Value("\${importer.di.application.temporary.import.type.id}")
//    lateinit var diApplicationTemporaryImportTypeID: String

    @Value("\${destination.inspection.notification.di.application.submit.uuid}")
    lateinit var diApplicationSubmittedUuid: String

    @Value("\${destination.inspection.notification.di.application.assigned.uuid}")
    lateinit var diApplicationAssignedUuid: String

    @Value("\${destination.inspection.notification.di.application.approved.uuid}")
    lateinit var diApplicationApprovedUuid: String

    @Value("\${destination.inspection.notification.di.application.rejected.uuid}")
    lateinit var diApplicationRejectedUuid: String

    @Value("\${destination.inspection.notification.di.application.issues.uuid}")
    lateinit var diApplicationIssuesUuid: String

    @Value("\${destination.inspection.notification.di.application.awaiting.approval.uuid}")
    lateinit var diApplicationAwaitingApprovalUuid: String

    @Value("\${destination.inspection.designation.inspection.officer.id}")
    lateinit var diDesignationInspectionOfficerId: String
    
    @Value("\${destination.inspection.designation.manager.inspection.officer.id}")
    lateinit var diDesignationManagerInspectionId: String

    final var appId: Int? = null
    private final val systemBy: String = "SYSTEM"


    init {
        appId = applicationMapProperties.mapImporterDetails
    }

    fun findRfcTypeWithID(rfcTypeId: Long): RfcTypesEntity {
        iRfcTypesRepo.findByIdOrNull(rfcTypeId)
                ?.let { rfcTypeEntity ->
                    return rfcTypeEntity
                }
                ?: throw ExpectedDataNotFound("RFC type with ID = $rfcTypeId, does not exist")
    }

    fun findDiApplicationTypes(status: Int): List<ImporterDiApplicationsTypesEntity> {
        iImporterDiApplicationsTypesRepo.findByStatus(status)
                ?.let { diApplicationsTypes ->
                    return diApplicationsTypes
                }
                ?: throw ExpectedDataNotFound("DI type with status = $status, do not exist")
    }

    fun findDiApplicationTypeWithID(diApplicationTypeID: Long): ImporterDiApplicationsTypesEntity {
        iImporterDiApplicationsTypesRepo.findByIdOrNull(diApplicationTypeID)
                ?.let { diApplTypeEntity ->
                    return diApplTypeEntity
                }
                ?: throw ExpectedDataNotFound("DI APPLICATION type with ID = $diApplicationTypeID, does not exist")
    }

    fun findDiApplicationTypeWithUuid(diApplicationTypeUuid: String): ImporterDiApplicationsTypesEntity {
        iImporterDiApplicationsTypesRepo.findByUuid(diApplicationTypeUuid)
                ?.let { diApplTypeEntity ->
                    return diApplTypeEntity
                }
                ?: throw ExpectedDataNotFound("DI APPLICATION type with UUID = $diApplicationTypeUuid, does not exist")
    }

    fun findRfcListWithTypeIDAndImporterID(user: UsersEntity, rfcTypesEntity: RfcTypesEntity, status: Int): List<RfcDocumentsDetailsEntity> {
        iRfcDocumentDetailsRepo.findByImporterIDAndRfcTypeIdAndStatus(user, rfcTypesEntity, status)
                ?.let { rfcList ->
                    return rfcList
                }
                ?: throw ExpectedDataNotFound("RFC with rfcTypeID = ${rfcTypesEntity.id} and userID = ${user.id} and status = ${status}, does not exist")
    }

    fun findCsApprovalApplicationsWithImporterID(user: UsersEntity): List<CsApprovalApplicationsEntity> {
        iCsApprovalApplicationsRepo.findByImporterId(user)
                ?.let { diApplicationList ->
                    return diApplicationList
                }
                ?: throw ExpectedDataNotFound("Logged In user data does not exist")
    }

    fun findTemporaryImportApplicationsWithImporterID(user: UsersEntity): List<TemporaryImportApplicationsEntity> {
        iTemporaryImportApplicationsRepo.findByImporterId(user)
                ?.let { diApplicationList ->
                    return diApplicationList
                }
                ?: throw ExpectedDataNotFound("Logged In user data does not exist")
    }

    fun findListOfAllCSApprovalWithEntryPoint(sectionsEntity: SectionsEntity): List<CsApprovalApplicationsEntity> {
        iCsApprovalApplicationsRepo.findByEntryPointId(sectionsEntity)
                ?.let { CsApprovalListDetails ->
                    return CsApprovalListDetails
                }
                ?: throw ExpectedDataNotFound("CS Approval with entry point ID = ${sectionsEntity.id}, does not exist")

    }

//    fun findAllUsersWithSectionIdA(sectionsEntity: SectionsEntity, status: Int): List<UserProfilesEntity> {
//        iUserProfilesRepo.findBySectionIdAndDesignationIdAndStatus(sectionsEntity, status)
//                ?.let { users ->
//                    return users
//                }
//                ?: throw ExpectedDataNotFound("Users with section ID  = ${sectionsEntity.id} and status = $status, does not Exist")
//    }

    fun findListOfAllTemporaryImportWithEntryPoint(sectionsEntity: SectionsEntity): List<TemporaryImportApplicationsEntity> {
        iTemporaryImportApplicationsRepo.findByEntryPointId(sectionsEntity)
                ?.let { TemporaryImportListDetails ->
                    return TemporaryImportListDetails
                }
                ?: throw ExpectedDataNotFound("Temporary Import with entry point ID = ${sectionsEntity.id}, does not exist")

    }


    fun findRFCDetailsWithID(rfcId: Long): RfcDocumentsDetailsEntity {
        iRfcDocumentDetailsRepo.findByIdOrNull(rfcId)
                ?.let { rfcDetails ->
                    return rfcDetails
                }
                ?: throw ExpectedDataNotFound("RFC with ID = $rfcId, does not exist")

    }

    fun findCSApprovalWithID(csApprovalID: Long): CsApprovalApplicationsEntity {
        iCsApprovalApplicationsRepo.findByIdOrNull(csApprovalID)
                ?.let { csApprovalDetails ->
                    return csApprovalDetails
                }
                ?: throw ExpectedDataNotFound("CS Approval with ID = $csApprovalID, does not exist")

    }

    fun findCSApprovalWithUcrNumber(ucrNumber: String): CsApprovalApplicationsEntity? {
        return iCsApprovalApplicationsRepo.findByUcrNo(ucrNumber)
    }

    fun findCSApprovalWithUuid(csApprovalUuid: String): CsApprovalApplicationsEntity {
        iCsApprovalApplicationsRepo.findByUuid(csApprovalUuid)
                ?.let { csApprovalDetails ->
                    return csApprovalDetails
                }
                ?: throw ExpectedDataNotFound("CS Approval with UUID = $csApprovalUuid, does not exist")

    }

    fun findTemporaryImportsWithID(temporaryImportID: Long): TemporaryImportApplicationsEntity {
        iTemporaryImportApplicationsRepo.findByIdOrNull(temporaryImportID)
                ?.let { temporaryImportDetails ->
                    return temporaryImportDetails
                }
                ?: throw ExpectedDataNotFound("Temporary Import with ID = $temporaryImportID, does not exist")

    }
    
    fun findTemporaryImportsWithUuid(temporaryImportUuid: String): TemporaryImportApplicationsEntity {
        iTemporaryImportApplicationsRepo.findByUuid(temporaryImportUuid)
                ?.let { temporaryImportDetails ->
                    return temporaryImportDetails
                }
                ?: throw ExpectedDataNotFound("Temporary Import with uuid = $temporaryImportUuid, does not exist")

    }

    fun findTemporaryImportsUploadWithID(upLoadID: Long): TemporaryImportApplicationsUploadsEntity {
        iTemporaryImportApplicationsUploadsRepo.findByIdOrNull(upLoadID)
                ?.let { temporaryImportUpLoadIDDetails ->
                    return temporaryImportUpLoadIDDetails
                }
                ?: throw ExpectedDataNotFound("Temporary Import UPLOAD with ID = $upLoadID, does not exist")

    }

    fun findTemporaryImportsUploadsWithTemporaryID(temporaryImport: TemporaryImportApplicationsEntity): List<TemporaryImportApplicationsUploadsEntity> {
        iTemporaryImportApplicationsUploadsRepo.findByTemporaryImportApplicationId(temporaryImport)
                ?.let { temporaryImportUpLoadsDetails ->
                    return temporaryImportUpLoadsDetails
                }
                ?: throw ExpectedDataNotFound("Temporary Import Uploads files for Temporary Import with ID = ${temporaryImport.id}, does not exist")

    }

    fun findCSApprovalUploadWithID(upLoadID: Long): CsApprovalApplicationsUploadsEntity {
        iCsApprovalApplicationsUploadsRepo.findByIdOrNull(upLoadID)
                ?.let { csApprovalUpLoadIDDetails ->
                    return csApprovalUpLoadIDDetails
                }
                ?: throw ExpectedDataNotFound("CS Approval UPLOAD with ID = $upLoadID, does not exist")
    }

    fun findCSApprovalUploadsWithCSApprovalID(csApprovalApplicationsEntity: CsApprovalApplicationsEntity): List<CsApprovalApplicationsUploadsEntity> {
        iCsApprovalApplicationsUploadsRepo.findByCsApprovalApplicationId(csApprovalApplicationsEntity)
                ?.let { csApprovalUpLoadsDetails ->
                    return csApprovalUpLoadsDetails
                }
                ?: throw ExpectedDataNotFound("CS Approval Uploads files for CS Approval with ID = ${csApprovalApplicationsEntity.id}, does not exist")

    }

    fun findRFCCoiItemWithRFCId(rfcId: RfcDocumentsDetailsEntity, status: Int): List<RfcCoiItemsDetailsEntity> {
        iRfcCoiItemsDetailsRepo.findByRfcIdAndStatus(rfcId, status)
                ?.let { rfcCoiItemList ->
                    return rfcCoiItemList
                }
                ?: throw ExpectedDataNotFound("RFC COI Items List for RFC with ID = $rfcId status = ${status}, do not exist")

    }

    fun findRFCCoiItemDetailsWithID(rfcCoiItemId: Long): RfcCoiItemsDetailsEntity {
        iRfcCoiItemsDetailsRepo.findByIdOrNull(rfcCoiItemId)
                ?.let { rfcCoiItemDetails ->
                    return rfcCoiItemDetails
                }
                ?: throw ExpectedDataNotFound("RFC COI Item with ID = $rfcCoiItemId, does not exist")

    }


    fun csApprovalSave(csApprovalApplicationsEntity: CsApprovalApplicationsEntity, user: UsersEntity, s: ServiceMapsEntity, diapplicationType :ImporterDiApplicationsTypesEntity ): CsApprovalApplicationsEntity {
        var applicationDoc = csApprovalApplicationsEntity
        with(applicationDoc) {
            entryPointId = confirmEntryPoint?.let { commonDaoServices.findSectionWIthId(it) }
            sentMdStatus = s.inactiveStatus
            importerId = user
            uuid = commonDaoServices.generateUUIDString()
            //Todo make sure the cd with the ucr number has not been used with any other documents
            cdId = ucrNo?.let { destinationInspectionDaoServices.findCdWithUcrNumberLatest(it)?.id }
            applicationType = diapplicationType
            refNumber = "${applicationType?.mark}${generateRandomText(5, s.secureRandom, s.messageDigestAlgorithm, false)}".toUpperCase()
            transactionDate = commonDaoServices.getCurrentDate()
            status = s.activeStatus
            createdBy = commonDaoServices.concatenateName(user)
            createdOn = commonDaoServices.getTimestamp()
        }
        applicationDoc = iCsApprovalApplicationsRepo.save(applicationDoc)
        KotlinLogging.logger { }.info("CS APPROVAL APPLICATION SAVED WITH ID =${applicationDoc.id}")
        return applicationDoc
    }

    fun temporaryImportSave(temporaryImportApplicationsEntity: TemporaryImportApplicationsEntity, user: UsersEntity, s: ServiceMapsEntity, diApplicationType :ImporterDiApplicationsTypesEntity): TemporaryImportApplicationsEntity {
        var applicationDoc = temporaryImportApplicationsEntity
        with(applicationDoc) {
            entryPointId = confirmEntryPoint?.let { commonDaoServices.findSectionWIthId(it) }
            sentMdStatus = s.inactiveStatus
            importerId = user
            uuid = commonDaoServices.generateUUIDString()
            //Todo make sure the cd with the ucr number has not been used with any other documents
            cdId = ucrNo?.let { destinationInspectionDaoServices.findCdWithUcrNumberLatest(it)?.id }
            applicationType = diApplicationType
            refNumber = "${applicationType?.mark}${generateRandomText(5, s.secureRandom, s.messageDigestAlgorithm, false)}".toUpperCase()
            transactionDate = commonDaoServices.getCurrentDate()
            status = s.activeStatus
            createdBy = commonDaoServices.concatenateName(user)
            createdOn = commonDaoServices.getTimestamp()
        }
        applicationDoc = iTemporaryImportApplicationsRepo.save(applicationDoc)
        KotlinLogging.logger { }.info("TEMPORARY IMPORT APPLICATION SAVED WITH ID =${applicationDoc.id}")
        return applicationDoc
    }

    fun saveTemporaryImportUploads(docFile: MultipartFile, doc: String, user: UsersEntity, map: ServiceMapsEntity,temporaryImportApplicationsEntity: TemporaryImportApplicationsEntity): TemporaryImportApplicationsUploadsEntity {
        var applicationDocUploads = TemporaryImportApplicationsUploadsEntity()
        with(applicationDocUploads) {
            name = commonDaoServices.saveDocuments(docFile)
            fileType = docFile.contentType
            documentType = doc
            document = docFile.bytes
            temporaryImportApplicationId = temporaryImportApplicationsEntity
            transactionDate = commonDaoServices.getCurrentDate()
            status = map.activeStatus
            createdBy = commonDaoServices.concatenateName(user)
            createdOn = commonDaoServices.getTimestamp()

        }
        applicationDocUploads = iTemporaryImportApplicationsUploadsRepo.save(applicationDocUploads)
        KotlinLogging.logger { }.info("TEMPORARY IMPORT APPLICATION FILE ATTACHED SAVED WITH ID =${applicationDocUploads.id} and File Name = ${applicationDocUploads.name}")
        return applicationDocUploads
    }

    fun saveCsApprovalUploads(docFile: MultipartFile, doc: String, user: UsersEntity, map: ServiceMapsEntity, csApprovalApplicationsEntity: CsApprovalApplicationsEntity): CsApprovalApplicationsUploadsEntity {
        var uploads =CsApprovalApplicationsUploadsEntity()
        with(uploads) {
            name = commonDaoServices.saveDocuments(docFile)
            fileType = docFile.contentType
            documentType = doc
            document = docFile.bytes
            csApprovalApplicationId = csApprovalApplicationsEntity
            transactionDate = commonDaoServices.getCurrentDate()
            status = map.activeStatus
            createdBy = commonDaoServices.concatenateName(user)
            createdOn = commonDaoServices.getTimestamp()

        }
        uploads = iCsApprovalApplicationsUploadsRepo.save(uploads)
        KotlinLogging.logger { }.info("CS APPROVAL APPLICATION FILE ATTACHED SAVED WITH ID =${uploads.id} and File Name = ${uploads.name}")
        return uploads
    }

    fun rfcSave(rfcDocumentsDetailsEntity: RfcDocumentsDetailsEntity, rfcType: RfcTypesEntity, user: UsersEntity, s: ServiceMapsEntity): RfcDocumentsDetailsEntity {
        var rfcDoc = rfcDocumentsDetailsEntity
        with(rfcDoc) {
            importerID = user
            rfcTypeId = rfcType
            rfcNumber =  "${rfcType.mark}${generateRandomText(5, s.secureRandom, s.messageDigestAlgorithm, false)}".toUpperCase()
            rfcDate = commonDaoServices.getCurrentDate()
            status = s.activeStatus
            createdBy = commonDaoServices.concatenateName(user)
            createdOn = commonDaoServices.getTimestamp()
        }
        rfcDoc = iRfcDocumentDetailsRepo.save(rfcDoc)
        KotlinLogging.logger { }.info("RFC DETAILS SAVED WITH ID =${rfcDoc.id}")
        return rfcDoc
    }

    fun rfcItemSave(rfcCoiItemsDetailsEntity: RfcCoiItemsDetailsEntity, rfcDocumentsDetailsEntity: RfcDocumentsDetailsEntity, user: UsersEntity, s: ServiceMapsEntity): RfcCoiItemsDetailsEntity {
        var rfcDocItem = rfcCoiItemsDetailsEntity
        with(rfcDocItem) {
            rfcId = rfcDocumentsDetailsEntity
            status = s.activeStatus
            createdBy = commonDaoServices.concatenateName(user)
            createdOn = commonDaoServices.getTimestamp()
        }
        rfcDocItem = iRfcCoiItemsDetailsRepo.save(rfcDocItem)
        KotlinLogging.logger { }.info("RFC ITEM DETAILS SAVED WITH ID =${rfcDocItem.id}")
        return rfcDocItem
    }

    fun rfcUpdatesSave(rfcDocumentsDetailsEntity: RfcDocumentsDetailsEntity, user: UsersEntity, s: ServiceMapsEntity): RfcDocumentsDetailsEntity {
        var rfcDoc = rfcDocumentsDetailsEntity
        with(rfcDoc) {
            completenessStatus = s.activeStatus
            updateBy = commonDaoServices.concatenateName(user)
            updatedOn = commonDaoServices.getTimestamp()
        }
        rfcDoc = iRfcDocumentDetailsRepo.save(rfcDoc)
        KotlinLogging.logger { }.info("RFC DETAILS UPDATED WITH ID =${rfcDoc.id}")
        return rfcDoc
    }

    fun updateCsApproval(csApprovalApplicationsEntity: CsApprovalApplicationsEntity, user: UsersEntity, s: ServiceMapsEntity): CsApprovalApplicationsEntity {
        var updateCSApproval = csApprovalApplicationsEntity
        with(updateCSApproval) {
//            usedStatus = s.activeStatus
            modifiedBy = commonDaoServices.concatenateName(user)
            modifiedOn = commonDaoServices.getTimestamp()
        }
        updateCSApproval = iCsApprovalApplicationsRepo.save(updateCSApproval)
        KotlinLogging.logger { }.info("UPDATED CS APPROVAL WITH ID = ${updateCSApproval.id}")
        return updateCSApproval
    }

    fun updateTemporaryImport(temporaryImportApplicationsEntity: TemporaryImportApplicationsEntity, user: UsersEntity, s: ServiceMapsEntity): TemporaryImportApplicationsEntity {
        var updateTemporaryImport = temporaryImportApplicationsEntity
        with(updateTemporaryImport) {
//            usedStatus = s.activeStatus
            modifiedBy = commonDaoServices.concatenateName(user)
            modifiedOn = commonDaoServices.getTimestamp()
        }
        updateTemporaryImport = iTemporaryImportApplicationsRepo.save(updateTemporaryImport)
        KotlinLogging.logger { }.info("UPDATED TEMPORARY IMPORT WITH ID = ${updateTemporaryImport.id}")
        return updateTemporaryImport
    }

    fun rfcSaveIDFUsed(idfEntity: IdfsEntity, user: UsersEntity, s: ServiceMapsEntity): IdfsEntity {
        with(idfEntity) {
            usedStatus = s.activeStatus
            modifiedBy = commonDaoServices.concatenateName(user)
            modifiedOn = commonDaoServices.getTimestamp()
        }
        KotlinLogging.logger { }.info("IDF DETAILS SAVED idfNumber =${idfEntity.idfNumber} AND UCRNUMBER=${idfEntity.ucr}")
        return idfRepo.save(idfEntity)
    }

    fun findByIdfNoAndUCRNumber(idfNumber: String, ucrNumber: String): IdfsEntity {
        idfRepo.findByIdfNumberAndUcrAndUsedStatus(idfNumber, ucrNumber, commonDaoServices.inActiveStatus.toInt())
                ?.let { idfEntity ->
                    return idfEntity
                }
                ?: throw ExpectedDataNotFound("IDF with the following Details (idfNumber = $idfNumber and UsedStatus = ${commonDaoServices.inActiveStatus.toInt()} , Does not Exist")
    }

    fun findByIdfNoAndUCRNumberAndStatus(idfNumber: String, ucrNumber: String, status: Int): Boolean {
        idfRepo.findByIdfNumberAndUcrAndUsedStatus(idfNumber, ucrNumber, status)
                ?.let {
                    return true
                }
        return false
    }

}