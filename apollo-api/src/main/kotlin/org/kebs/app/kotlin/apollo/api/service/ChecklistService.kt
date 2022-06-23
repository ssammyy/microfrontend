package org.kebs.app.kotlin.apollo.api.service;

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.notifications.Notifications
import org.kebs.app.kotlin.apollo.api.payload.*
import org.kebs.app.kotlin.apollo.api.payload.request.CheckListForm
import org.kebs.app.kotlin.apollo.api.payload.request.SsfResultForm
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.DestinationInspectionBpmn
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DestinationInspectionDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.QADaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.ReportsDaoService
import org.kebs.app.kotlin.apollo.api.ports.provided.lims.LimsServices
import org.kebs.app.kotlin.apollo.common.dto.MinistryInspectionListResponseDto
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.utils.generateRandomText
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.ServiceMapsEntity
import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import org.kebs.app.kotlin.apollo.store.model.di.*
import org.kebs.app.kotlin.apollo.store.model.qa.QaSampleCollectionEntity
import org.kebs.app.kotlin.apollo.store.model.qa.QaSampleSubmissionEntity
import org.kebs.app.kotlin.apollo.store.model.qa.QaSampleSubmittedPdfListDetailsEntity
import org.kebs.app.kotlin.apollo.store.repo.di.*
import org.kebs.app.kotlin.apollo.store.repo.qa.IQaSampleCollectionRepository
import org.kebs.app.kotlin.apollo.store.repo.qa.IQaSampleLabTestParametersRepository
import org.kebs.app.kotlin.apollo.store.repo.qa.IQaSampleLabTestResultsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.util.*

enum class ChecklistType {
    AGROCHEM, ENGINEERING, VEHICLE, OTHER, NONE
}

@Service("checklistService")
class ChecklistService(
        private val inspectionGeneralRepo: ICdInspectionGeneralRepository,
        private val applicationMapProperties: ApplicationMapProperties,
        private val engineeringChecklistRepository: ICdInspectionEngineeringChecklistRepository,
        private val agrochemChecklistRepository: ICdInspectionAgrochemChecklistRepository,
        private val agrochemItemChecklistRepository: ICdInspectionAgrochemItemChecklistRepository,
        private val engineeringItemChecklistRepository: ICdInspectionEngineeringItemChecklistRepository,
        private val motorVehicleItemChecklistRepository: ICdInspectionMotorVehicleItemChecklistRepository,
        private val motorVehicleChecklistRepository: ICdInspectionMotorVehicleChecklistRepository,
        private val otherItemChecklistRepository: ICdInspectionOtherItemChecklistRepository,
        private val otherChecklistRepository: ICdInspectionOtherChecklistRepository,
        private val iChecklistInspectionTypesRepo: IChecklistInspectionTypesRepository,
        private val iCdItemsRepo: IConsignmentItemsRepository,
        private val commonDaoServices: CommonDaoServices,
        private val bpmn: DestinationInspectionBpmn,
        private val reportsDaoService: ReportsDaoService,
        private val notifications: Notifications,
        private val sampleParametersRepository: IQaSampleLabTestParametersRepository,
        private val sampleResultsRepository: IQaSampleLabTestResultsRepository,
        private val ministryStationRepo: IMinistryStationEntityRepository,
        private val ministryStationRepository: IMinistryStationEntityRepository,
        private val qaISampleCollectRepository: IQaSampleCollectionRepository,
        private val consignmentAuditService: ConsignmentDocumentAuditService,
        private val daoServices: DestinationInspectionDaoServices,
) {
    @Lazy
    @Autowired
    lateinit var qaDaoServices: QADaoServices

    @Lazy
    @Autowired
    lateinit var limsServices: LimsServices

    @Transactional
    fun saveChecklist(form: CheckListForm, cdItem: ConsignmentDocumentDetailsEntity, loggedInUser: UsersEntity, map: ServiceMapsEntity): ApiResponseModel {
        val response = ApiResponseModel()
        //Save the general checklist
        val generalCheckList = form.generalChecklist()
        generalCheckList.ucrNumber = cdItem.ucrNumber
        generalCheckList.description = cdItem.description
        daoServices.findCDImporterDetails(cdItem.cdImporter ?: 0).let { importer ->
            generalCheckList.importersName = importer.name
        }
        // Manifest details
        cdItem.manifestNumber?.let { man ->
            daoServices.findManifest(man)?.let {
                generalCheckList.declarationNumber = it.manifestNumber
                generalCheckList.declarationRepresentative = it.receiver.orEmpty()
            }
        }
        generalCheckList.currentChecklist = 1
        generalCheckList.inspectionDate = Date(java.util.Date().time)
        generalCheckList.inspectionOfficer = "${loggedInUser.firstName} ${loggedInUser.lastName}".trim()
        generalCheckList.supervisorName = "${cdItem.assigner?.firstName} ${cdItem.assigner?.lastName}".trim()
        generalCheckList.cfs = cdItem.freightStation?.cfsName
        val inspectionGeneral = daoServices.saveInspectionGeneralDetails(generalCheckList, cdItem, loggedInUser, map)
        if (form.agrochem == null && form.engineering == null && form.vehicle == null && form.others == null) {
            response.responseCode = ResponseCodes.FAILED_CODE
            response.message = "Validation failed, please select and fill at least one checklist"
            return response
        }
        //Save the respective checklist
        form.agrochem?.let {
            val agrochemItemInspectionChecklist = form.agrochemChecklist()
            addAgrochemChecklist(map, inspectionGeneral, form.agrochemChecklistItems(), agrochemItemInspectionChecklist, loggedInUser)
        }
        // Add engineering checklist
        form.engineering?.let {
            val engineeringItemInspectionChecklist = form.engineeringChecklist()
            addEngineeringChecklist(map, inspectionGeneral, form.engineeringChecklistItems(), engineeringItemInspectionChecklist, loggedInUser)

        }
        // Add vehicle checklist
        form.vehicle?.let {
            val motorVehicleItemInspectionChecklist = form.vehicleChecklist()
            addVehicleChecklist(map, inspectionGeneral, form.vehicleChecklistItems(), motorVehicleItemInspectionChecklist, loggedInUser)
        }
        // Add other checklists
        form.others?.let {
            val otherItemInspectionChecklist = form.otherChecklist()
            addOtherChecklist(map, inspectionGeneral, form.otherChecklistItems(), otherItemInspectionChecklist, loggedInUser)
        }
        response.message = "Checklist submitted successfully"
        response.responseCode = ResponseCodes.SUCCESS_CODE
        return response
    }

    fun listLabPdfFiles(ssfId: Long, loggedInUser: UsersEntity): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
            val sample = this.qaDaoServices.findSampleSubmittedBYID(ssfId)
            var savedPDFFiles = qaDaoServices.findSampleSubmittedListPdfBYSSFid(ssfId)
            if (savedPDFFiles.isNullOrEmpty() && sample.labResultsStatus == map.activeStatus) {
                savedPDFFiles = mutableListOf<QaSampleSubmittedPdfListDetailsEntity>()
                sample.bsNumber?.let { bsNumber ->
                    limsServices.checkPDFFiles(bsNumber)?.let { files ->
                        for (index in files.indices) {
                            val qaFile = QaSampleSubmittedPdfListDetailsEntity()
                            qaFile.pdfName = files[index]
                            qaFile.description = "File ${index}"
                            qaFile.sffId = sample.id
                            qaFile.status = map.activeStatus
                            savedPDFFiles.add(qaDaoServices.saveSampleSubmittedPdf(qaFile, loggedInUser))
                        }
                    }
                }
            }
            response.data = LimsPdfFilesDto.fromList(savedPDFFiles)
            response.message = "Success"
            response.responseCode = ResponseCodes.SUCCESS_CODE
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("FAILED TO LOAD FILES", ex)
        }
        return response
    }

    fun updateItemComplianceStatus(document: ConsignmentDocumentDetailsEntity, cdItemID: Long, remarks: String, compliant: String): ApiResponseModel {
        val response = ApiResponseModel()
        inspectionGeneralRepo.findFirstByCdDetailsAndCurrentChecklist(document, 1)?.let { inspectionGeneral ->
            val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
            val itemDetails = daoServices.findItemWithItemID(cdItemID)
            itemDetails.checkListTypeId?.let { checklistType ->
                itemDetails.approveDate = Date(Date().time)
                itemDetails.approveReason = remarks
                itemDetails.status = map.activeStatus
                if (compliant == "YES") {
                    itemDetails.approveStatus = map.activeStatus
                } else {
                    itemDetails.approveStatus = map.invalidStatus
                }
                // Handle Not found
                response.responseCode = ResponseCodes.NOT_FOUND
                response.message = "Checklist item not found: " + checklistType.varField1
                // Checklist update
                when (checklistType.typeName) {
                    ChecklistType.AGROCHEM.name -> {
                        agrochemItemChecklistRepository.findByInspection_InspectionGeneralAndItemId_Id(inspectionGeneral, cdItemID)?.let { checkList ->
                            checkList.compliant = compliant
                            checkList.status = map.activeStatus
                            agrochemItemChecklistRepository.save(checkList)
                            iCdItemsRepo.save(itemDetails)
                            response.responseCode = ResponseCodes.SUCCESS_CODE
                            response.message = "Checklist compliance update successfully"
                            response
                        }
                    }
                    ChecklistType.ENGINEERING.name -> {
                        engineeringItemChecklistRepository.findByInspection_InspectionGeneralAndItemId_Id(inspectionGeneral, cdItemID)?.let { checkList ->
                            checkList.compliant = compliant
                            checkList.status = map.activeStatus
                            engineeringItemChecklistRepository.save(checkList)
                            iCdItemsRepo.save(itemDetails)
                            response.responseCode = ResponseCodes.SUCCESS_CODE
                            response.message = "Success"
                            response
                        }
                    }
                    ChecklistType.OTHER.name -> {
                        otherItemChecklistRepository.findByInspection_InspectionGeneralAndItemId_Id(inspectionGeneral, cdItemID)?.let { checkList ->
                            checkList.compliant = compliant
                            checkList.status = map.activeStatus
                            otherItemChecklistRepository.save(checkList)
                            iCdItemsRepo.save(itemDetails)
                            response.responseCode = ResponseCodes.SUCCESS_CODE
                            response.message = "Success"
                            response
                        }
                    }
                    else -> {
                        response.responseCode = ResponseCodes.FAILED_CODE
                        response.message = "Invalid checklist type: ${checklistType.typeName}"
                    }
                }
                response
            } ?: run {
                response.message = "Checklist not filled for this item"
                response.responseCode = ResponseCodes.NOT_FOUND
                response
            }
        } ?: run {
            response.responseCode = ResponseCodes.NOT_FOUND
            response.message = "Inspection details for CD not found"
            response
        }
        return response
    }

    fun addEngineeringSsf(map: ServiceMapsEntity, cdItemID: Long, sampleSubmissionDetails: QaSampleSubmissionEntity, loggedInUser: UsersEntity): ApiResponseModel {
        var response = ApiResponseModel()
        val enginerringItem = engineeringItemChecklistRepository.findByIdOrNull(cdItemID)
        enginerringItem?.let {
            engineeringItemChecklistRepository.save(it)
            response = saveSsfDetails(sampleSubmissionDetails, cdItemID, map, loggedInUser)
        } ?: run {
            response.message = "Invalid checklist identifier"
            response.responseCode = ResponseCodes.NOT_FOUND
            response
        }
        return response
    }

    fun addAgrochemSsf(map: ServiceMapsEntity, cdItemID: Long, sampleSubmissionDetails: QaSampleSubmissionEntity, loggedInUser: UsersEntity): ApiResponseModel {
        var response = ApiResponseModel()
        val enginerringItem = agrochemItemChecklistRepository.findByIdOrNull(cdItemID)
        enginerringItem?.let {
            response = saveSsfDetails(sampleSubmissionDetails, cdItemID, map, loggedInUser)
            response
        } ?: run {
            response.message = "Invalid checklist identifier"
            response.responseCode = ResponseCodes.NOT_FOUND
            response
        }
        return response
    }

    @Transactional
    fun addEngineeringChecklist(map: ServiceMapsEntity, general: CdInspectionGeneralEntity, itemList: List<CdInspectionEngineeringItemChecklistEntity>?, engineeringChecklist: CdInspectionEngineeringChecklist, loggedInUser: UsersEntity) {
        var engineering: CdInspectionEngineeringChecklist? = engineeringChecklistRepository.findByInspectionGeneral(general)
        if (engineering == null) {
            engineering = engineeringChecklist
            val checkListType = this.iChecklistInspectionTypesRepo.findByTypeName(ChecklistType.ENGINEERING.name)
            engineering.inspectionChecklistType = checkListType
            engineering.inspectionGeneral = general
            engineering.createdBy = loggedInUser.userName
            engineering.createdOn = Timestamp.from(Instant.now())
        } else {
            engineering.inspectionGeneral = general
            // Update details
            engineering.description = engineeringChecklist.description
        }
        this.engineeringChecklistRepository.save(engineering)
        itemList?.let { items ->
            for (itm in items) {
                val itemDetails = iCdItemsRepo.findById(itm.itemIdTmp!!)
                if (itemDetails.isPresent) {
                    val detail = itemDetails.get()
                    var checklistItem: CdInspectionEngineeringItemChecklistEntity? = this.engineeringItemChecklistRepository.findByInspectionAndItemId_Id(engineering, itm.itemIdTmp)
                    if (checklistItem == null) {
                        checklistItem = itm
                        checklistItem.createdBy = loggedInUser.userName
                        checklistItem.createdOn = Timestamp.from(Instant.now())
                    } else {
                        checklistItem.modifiedBy = loggedInUser.userName
                        checklistItem.modifiedOn = Timestamp.from(Instant.now())
                    }
                    checklistItem.itemId = detail
                    // Details
                    checklistItem.sampled = itm.sampled
                    if ("YES".equals(itm.sampled)) {
                        checklistItem.sampleUpdated = map.initStatus
                    } else {
                        checklistItem.sampleUpdated = map.activeStatus
                    }
                    checklistItem.quantityDeclared = detail.quantity.toString()
                    checklistItem.productDescription = detail.itemDescription
                    checklistItem.itemId = itm.itemId
                    checklistItem.sizeClassCapacity = itm.sizeClassCapacity
                    checklistItem.description = detail.hsDescription
                    checklistItem.brand = itm.brand ?: detail.productBrandName
                    checklistItem.serialNumber = itm.serialNumber
                    checklistItem.compliant = itm.compliant
                    checklistItem.inspection = engineering

                    this.engineeringItemChecklistRepository.save(checklistItem)
                    // Update checklist update
                    detail.checkListTypeId = engineering.inspectionChecklistType
                    daoServices.checkIfChecklistUndergoesSampling(
                            checklistItem.sampled ?: "NO",
                            checklistItem.compliant ?: "NON-COMPLIANT",
                            detail,
                            map
                    )

                }
            }
        }
    }

    @Transactional
    fun addAgrochemChecklist(map: ServiceMapsEntity, general: CdInspectionGeneralEntity, itemList: List<CdInspectionAgrochemItemChecklistEntity>?, engineeringChecklist: CdInspectionAgrochemChecklist, loggedInUser: UsersEntity) {
        var agrochemChecklist: CdInspectionAgrochemChecklist? = agrochemChecklistRepository.findByInspectionGeneral(general)
        if (agrochemChecklist == null) {
            agrochemChecklist = engineeringChecklist
            val checkListType = this.iChecklistInspectionTypesRepo.findByTypeName(ChecklistType.AGROCHEM.name)
            agrochemChecklist.inspectionChecklistType = checkListType
            agrochemChecklist.inspectionGeneral = general
            agrochemChecklist.createdBy = loggedInUser.userName
            agrochemChecklist.createdOn = Timestamp.from(Instant.now())
        } else {
            // Update details
            agrochemChecklist.description = engineeringChecklist.description
        }
        this.agrochemChecklistRepository.save(agrochemChecklist)
        itemList?.let { items ->
            for (itm in items) {
                val itemDetails = iCdItemsRepo.findById(itm.itemIdTmp!!)
                if (itemDetails.isPresent) {
                    val detail = itemDetails.get()
                    var checklistItem: CdInspectionAgrochemItemChecklistEntity? = this.agrochemItemChecklistRepository.findByInspectionAndItemId_Id(agrochemChecklist, itm.itemIdTmp)
                    if (checklistItem == null) {
                        checklistItem = itm
                        checklistItem.itemId = detail
                        checklistItem.createdBy = loggedInUser.userName
                        checklistItem.createdOn = Timestamp.from(Instant.now())
                    } else {
                        checklistItem.modifiedBy = loggedInUser.userName
                        checklistItem.modifiedOn = Timestamp.from(Instant.now())
                    }
                    // Details
                    checklistItem.sampled = itm.sampled
                    if ("YES".equals(itm.sampled)) {
                        checklistItem.sampleUpdated = map.initStatus
                    } else {
                        checklistItem.sampleUpdated = map.activeStatus
                    }
                    checklistItem.productDescription = itm.productDescription ?: detail.itemDescription
                    checklistItem.quantityDeclared = itm.quantityDeclared ?: detail.quantity.toString()
                    checklistItem.itemId = itm.itemId
                    checklistItem.description = detail.hsDescription
                    checklistItem.brand = itm.brand ?: detail.productBrandName
                    checklistItem.serialNumber = itm.serialNumber
                    checklistItem.compliant = itm.compliant
                    checklistItem.inspection = agrochemChecklist
                    this.agrochemItemChecklistRepository.save(checklistItem)
                    // Mark item as sampled
                    detail.checkListTypeId = agrochemChecklist.inspectionChecklistType
                    daoServices.checkIfChecklistUndergoesSampling(
                            checklistItem.sampled ?: "NO",
                            checklistItem.compliant ?: "NON-COMPLIANT",
                            detail,
                            map
                    )
                }
            }
        }
    }

    /**
     * Check lab result status:
     * 1. if result, then check if we have all result for all lab records and clear wait
     * 2. else Check if we added any lab result request during checklist saving
     */
    fun updateConsignmentSampledStatus(cdItemDetails: ConsignmentDocumentDetailsEntity, result: Boolean, isVehicle: Boolean, map: ServiceMapsEntity): Boolean {
        var response = false
        try {
            if (result) {
                val items = iCdItemsRepo.findByCdDocIdAndSampledStatus(cdItemDetails, map.activeStatus)
                var allItemsResult = true
                for (itm in items) {
                    if (itm.allTestReportStatus != 1) {
                        allItemsResult = false
                        break
                    }
                }
                // All lab results have been received
                if (allItemsResult) {
                    cdItemDetails.status = ConsignmentApprovalStatus.UNDER_INSPECTION.code
                    cdItemDetails.varField10 = "Lab result received"
                    daoServices.updateCDStatus(cdItemDetails, ConsignmentDocumentStatus.LAB_RESULT_RESULT)
                }
            } else {
                if (isVehicle) {
                    val items = iCdItemsRepo.findByCdDocIdAndMinistrySubmissionStatus(cdItemDetails, map.activeStatus)
                    if (items.isEmpty()) {
                        cdItemDetails.status = ConsignmentApprovalStatus.UNDER_INSPECTION.code
                        cdItemDetails.varField10 = "CHECKLIST FILLED, AWAITING COMPLIANCE STATUS"
                        daoServices.updateCDStatus(cdItemDetails, ConsignmentDocumentStatus.CHECKLIST_FILLED)
                    } else {
                        cdItemDetails.varField10 = "Waiting for ministry report"
                        cdItemDetails.status = ConsignmentApprovalStatus.WAITING.code
                        daoServices.updateCDStatus(cdItemDetails, ConsignmentDocumentStatus.MINISTRY_REQUEST)
                        response = true
                    }
                } else {
                    val items = iCdItemsRepo.findByCdDocIdAndSampledStatus(cdItemDetails, map.activeStatus)
                    if (items.isEmpty()) {
                        cdItemDetails.status = ConsignmentApprovalStatus.UNDER_INSPECTION.code
                        cdItemDetails.varField10 = "CHECKLIST FILLED, AWAITING COMPLIANCE STATUS"
                        daoServices.updateCDStatus(cdItemDetails, ConsignmentDocumentStatus.CHECKLIST_FILLED)
                    } else {
                        cdItemDetails.status = ConsignmentApprovalStatus.WAITING.code
                        cdItemDetails.varField10 = "Waiting for lab result"
                        daoServices.updateCDStatus(cdItemDetails, ConsignmentDocumentStatus.LAB_REQUEST)
                        response = true
                    }
                }
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to update lab result")
        }
        return response
    }


    @Transactional
    fun addVehicleChecklist(map: ServiceMapsEntity, general: CdInspectionGeneralEntity, itemList: List<CdInspectionMotorVehicleItemChecklistEntity>?, engineeringChecklist: CdInspectionMotorVehicleChecklist, loggedInUser: UsersEntity) {
        var vehicleChecklist: CdInspectionMotorVehicleChecklist? = motorVehicleChecklistRepository.findByInspectionGeneral(general)
        if (vehicleChecklist == null) {
            vehicleChecklist = engineeringChecklist
            val checkListType = this.iChecklistInspectionTypesRepo.findByTypeName(ChecklistType.VEHICLE.name)
            vehicleChecklist.inspectionChecklistType = checkListType
            vehicleChecklist.inspectionGeneral = general
            vehicleChecklist.createdBy = loggedInUser.userName
            vehicleChecklist.createdOn = Timestamp.from(Instant.now())
        } else {
            // Update details
            vehicleChecklist.modifiedBy = loggedInUser.userName
            vehicleChecklist.modifiedOn = Timestamp.from(Instant.now())
            vehicleChecklist.description = engineeringChecklist.description
        }
        this.motorVehicleChecklistRepository.save(vehicleChecklist)
        itemList?.let { items ->
            for (itm in items) {
                val itemDetails = iCdItemsRepo.findById(itm.temporalItemId!!)
                if (itemDetails.isPresent) {
                    val detail = itemDetails.get()
                    var checklistItem: CdInspectionMotorVehicleItemChecklistEntity? = this.motorVehicleItemChecklistRepository.findByInspectionAndItemId(vehicleChecklist, itm.itemId)
                    if (checklistItem == null) {
                        checklistItem = itm
                        checklistItem.createdBy = loggedInUser.userName
                        checklistItem.createdOn = Timestamp.from(Instant.now())
                    } else {
                        checklistItem.modifiedBy = loggedInUser.userName
                        checklistItem.modifiedOn = Timestamp.from(Instant.now())
                    }
                    // Add chassis number
                    daoServices.findCdItemNonStandardByItemID(detail)?.let {
                        checklistItem.chassisNo = it.chassisNo
                        checklistItem.yearOfManufacture = it.vehicleYear
                    }
                    // Update details
                    checklistItem.sampled = itm.sampled
                    if ("YES".equals(itm.sampled)) {
                        checklistItem.ministryReportSubmitStatus = map.workingStatus
                        checklistItem.sampleUpdated = map.workingStatus
                    } else {
                        checklistItem.ministryReportSubmitStatus = map.inactiveStatus
                        checklistItem.sampleUpdated = map.inactiveStatus
                    }
                    checklistItem.itemId = detail
                    checklistItem.description = detail.itemDescription ?: detail.hsDescription
                    checklistItem.serialNumber = itm.serialNumber
                    checklistItem.compliant = itm.compliant
                    checklistItem.inspection = vehicleChecklist
                    detail.checkListTypeId = vehicleChecklist.inspectionChecklistType
                    // Submit to ministry process
                    daoServices.updateCdItemDetailsInDB(detail, null)
                    if (checklistItem.ministryReportSubmitStatus == map.workingStatus) {
                        val ministryStation = this.ministryStationRepository.findById(checklistItem.stationId)
                        if (ministryStation.isPresent) {
                            checklistItem.ministryStationId = ministryStation.get()
                            val saved = this.motorVehicleItemChecklistRepository.save(checklistItem)
                            //Submit ministry inspection
                            this.bpmn.startMinistryInspection(saved, detail)
                        }
                        // FIXME: Handle invalid selection here
                    } else {
                        this.motorVehicleItemChecklistRepository.save(checklistItem)
                    }
                }
            }
        }
    }

    fun lisAllChecklists(cdItemUuid: String): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val cdItemDetails = daoServices.findCDWithUuid(cdItemUuid)
            response.data = InspectionGeneralDetailsDto.fromList(this.inspectionGeneralRepo.findAllByCdDetails(cdItemDetails))
            response.responseCode = ResponseCodes.SUCCESS_CODE
            response.message = "Success"
        } catch (ex: Exception) {
            response.message = "Invalid inspection item"
            response.responseCode = ResponseCodes.FAILED_CODE
        }
        return response
    }

    @Transactional
    fun addOtherChecklist(map: ServiceMapsEntity, general: CdInspectionGeneralEntity, itemList: List<CdInspectionOtherItemChecklistEntity>?, requestChecklist: CdInspectionOtherChecklist, loggedInUser: UsersEntity) {
        var otherChecklist: CdInspectionOtherChecklist? = otherChecklistRepository.findByInspectionGeneral(general)
        if (otherChecklist == null) {
            otherChecklist = requestChecklist
            val checkListType = this.iChecklistInspectionTypesRepo.findByTypeName(ChecklistType.OTHER.name)
            otherChecklist.createdBy = loggedInUser.userName
            otherChecklist.createdOn = Timestamp.from(Instant.now())
            otherChecklist.inspectionChecklistType = checkListType
            otherChecklist.inspectionGeneral = general
            otherChecklist.status = map.activeStatus
        } else {
            // Update details
            otherChecklist.description = requestChecklist.description
        }
        this.otherChecklistRepository.save(otherChecklist)
        itemList?.let { items ->
            for (itm in items) {
                val itemDetails = iCdItemsRepo.findById(itm.itemIdTmp!!)
                if (itemDetails.isPresent) {
                    val detail = itemDetails.get()
                    var checklistItem: CdInspectionOtherItemChecklistEntity? = this.otherItemChecklistRepository.findByInspectionAndItemId_Id(otherChecklist, itm.itemIdTmp)
                    if (checklistItem == null) {
                        checklistItem = itm
                        checklistItem.createdBy = loggedInUser.userName
                        checklistItem.createdOn = Timestamp.from(Instant.now())
                    } else {
                        checklistItem.modifiedBy = loggedInUser.userName
                        checklistItem.modifiedOn = Timestamp.from(Instant.now())
                    }
                    checklistItem.itemId = detail
                    checklistItem.sampled = itm.sampled
                    checklistItem.category = itm.category
                    if ("YES".equals(itm.sampled)) {
                        checklistItem.sampleUpdated = map.activeStatus
                    } else {
                        checklistItem.sampleUpdated = 2
                    }
                    checklistItem.quantityDeclared = detail.quantity.toString()
                    checklistItem.itemId = itm.itemId
                    checklistItem.description = detail.hsDescription
                    checklistItem.serialNumber = itm.serialNumber
                    checklistItem.compliant = itm.compliant
                    checklistItem.brand = itm.brand ?: detail.productBrandName
                    checklistItem.status = map.activeStatus
                    checklistItem.inspection = otherChecklist
                    this.otherItemChecklistRepository.save(checklistItem)
                    // Update sampling status
                    detail.checkListTypeId = otherChecklist.inspectionChecklistType
                    daoServices.checkIfChecklistUndergoesSampling(
                            checklistItem.sampled ?: "NO",
                            checklistItem.compliant ?: "NO",
                            detail,
                            map
                    )
                }
            }
        }
    }

    fun ministryInspectionList(inspectionChecklistId: Long, comment: String?, referenceNumber: String?, docFile: MultipartFile): ApiResponseModel {
        val response = ApiResponseModel()
        val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        KotlinLogging.logger { }.info("mvInspectionChecklistId = $inspectionChecklistId")
        motorVehicleItemChecklistRepository.findByIdOrNull(inspectionChecklistId)
                ?.let { inspectionMotorVehicle ->
                    inspectionMotorVehicle.ministryReportFile = docFile.bytes
                    inspectionMotorVehicle.ministryReportReinspectionRemarks = comment
                    inspectionMotorVehicle.ministryReportReference = referenceNumber
                    inspectionMotorVehicle.ministryReportDate = commonDaoServices.getTimestamp()
                    inspectionMotorVehicle.ministryReportSubmitStatus = map.activeStatus
                    daoServices.updateCdInspectionMotorVehicleItemChecklistInDB(
                            inspectionMotorVehicle,
                            loggedInUser
                    ).let { cdInspectionMVChecklist ->
                        cdInspectionMVChecklist.inspection?.inspectionGeneral?.cdDetails?.let { cdetails ->
                            //Update status
                            cdetails.varField10 = "Ministry report received"
                            cdetails.status = ConsignmentApprovalStatus.UNDER_INSPECTION.code
                            daoServices.updateCDStatus(cdetails, ConsignmentDocumentStatus.MINISTRY_UPLOAD)
                            this.consignmentAuditService.addHistoryRecord(cdetails.id, cdetails.ucrNumber, comment, "MINISTRY", "Ministry Inspection Report submitted")
                        }
                    }
                    // Save submission status
                    inspectionMotorVehicle.itemId?.let { cdItem ->
                        cdItem.ministrySubmissionStatus = map.activeStatus
                        this.iCdItemsRepo.save(cdItem)
                    }

                    response.message = "Report Submitted Successfully"
                    response.responseCode = ResponseCodes.SUCCESS_CODE
                    response
                } ?: run {
            response.message = "No Motor Vehicle Inspection Checklist Found"
            response.responseCode = ResponseCodes.NOT_FOUND
            response
        }

        return response
    }


    fun findInspectionMotorVehicleById(itemId: Long): CdInspectionMotorVehicleItemChecklistEntity? {
        return this.motorVehicleItemChecklistRepository.findByIdOrNull(itemId)
    }


    fun consignmentChecklistSampled(cdUuid: String): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val cdItemDetails = daoServices.findCDWithUuid(cdUuid)
            val items = iCdItemsRepo.findByCdDocIdAndSampledStatus(cdItemDetails, 1)
            response.data = CdItemDetailsDao.fromList(items)
            response.responseCode = ResponseCodes.SUCCESS_CODE
            response.message = "Success"
        } catch (ex: Exception) {
            response.message = "Failed to fetch samples"
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
        }
        return response
    }

    fun inspectionChecklistReportDetails(cdItemUuid: String, checklistId: String?): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val cdItemDetails = daoServices.findCDWithUuid(cdItemUuid)
            val checklistDetails = when (checklistId) {
                null -> inspectionGeneralRepo.findFirstByCdDetailsAndCurrentChecklist(cdItemDetails, 1)
                else -> inspectionGeneralRepo.findFirstByIdAndCdDetails(checklistId.toLong(), cdItemDetails)
            }
            checklistDetails?.let { generalChecklist ->
                //Get checklist type details
                val dataMap = mutableMapOf<String, Any?>()
                // Agrochem checklist details
                agrochemChecklistRepository.findByInspectionGeneral(generalChecklist)?.let {
                    val agrochem = InspectionAgrochemDetailsDto.fromEntity(it)
                    agrochem.items = InspectionAgrochemItemDto.fromList(agrochemItemChecklistRepository.findByInspection(it))
                    dataMap.put("agrochem_checklist", agrochem)
                }
                // Motor vehicle details
                motorVehicleChecklistRepository.findByInspectionGeneral(generalChecklist)?.let {
                    val vehicle = InspectionMotorVehicleDetailsDto.fromEntity(it)
                    vehicle.items = InspectionMotorVehicleItemDto.fromList(motorVehicleItemChecklistRepository.findByInspection(it))
                    dataMap.put("vehicle_checklist", vehicle)
                }
                // Engineering items
                engineeringChecklistRepository.findByInspectionGeneral(generalChecklist)?.let {
                    val checklist = InspectionEngineeringDetailsDto.fromEntity(it)
                    checklist.items = InspectionEngineeringItemDto.fromList(engineeringItemChecklistRepository.findByInspection(it))
                    dataMap.put("engineering_checklist", checklist)
                }
                // Other checklist items
                otherChecklistRepository.findByInspectionGeneral(generalChecklist)?.let {
                    val other = InspectionOtherDetailsDto.fromEntity(it)
                    other.items = InspectionOtherItemDto.fromList(otherItemChecklistRepository.findByInspection(it))
                    dataMap.put("other_checklist", other)
                }
                dataMap.put("details", InspectionGeneralDetailsDto.fromEntity(generalChecklist))
                generalChecklist.cdDetails?.let {
                    dataMap.put("cd_details", ConsignmentDocumentDao.fromEntity(it))
                    it.cdStandard?.let { standard ->
                        dataMap.put("cd_standards", CdStandardsEntityDao.fromEntity(standard))
                    }
                }

                response.data = dataMap
                response.message = "Success"
                response.responseCode = ResponseCodes.SUCCESS_CODE
                response
            } ?: run {
                response.message = "Inspection not found for document"
                response.responseCode = ResponseCodes.FAILED_CODE
                response
            }

        } catch (e: Exception) {
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Checklist not found"
        }
        return response
    }

    fun motorVehicleInspectionDetails(inspectionId: Long): ApiResponseModel {
        //Get the CD Item
        val response = ApiResponseModel()
        try {
            this.motorVehicleItemChecklistRepository.findByIdOrNull(inspectionId)?.let { mvInpsection ->
                val dataMap = mutableMapOf<String, Any?>()
                mvInpsection.itemId?.let { cdItemDetails ->
                    cdItemDetails.cdDocId?.let { cdDetails ->
                        dataMap["cd_details"] = ConsignmentDocumentDao.fromEntity(cdDetails)
                        dataMap["feePaid"] = "NO"
                        daoServices.findDemandNoteWithPaymentStatus(cdDetails.id ?: 0, 1)?.let { cdDemandNoteEntity ->
                            dataMap["receiptNumber"] = cdDemandNoteEntity.receiptNo
                            dataMap["feePaid"] = "YES"
                        }
                    }
                    //Get inspection checklist details
                    mvInpsection.inspection?.inspectionGeneral?.let { generalInspection ->
                        dataMap["inspectionGeneralEntity"] = CdInspectionGeneralDao.fromEntity(generalInspection)
                    }
                    //Get the motor vehicle checklist
                    mvInpsection.inspection?.let {
                        dataMap["checklist"] = InspectionMotorVehicleDetailsDto.fromEntity(it)
                    }
                    dataMap["mvInspectionChecklist"] = CdInspectionMotorVehicleItemChecklistDao.fromEntity(mvInpsection)
                }
                response.data = dataMap
                response.responseCode = ResponseCodes.SUCCESS_CODE
                response.message = "Success"
                response
            } ?: run {
                response.responseCode = ResponseCodes.SUCCESS_CODE
                response.message = "Success"
                //Check for flash attributes
                response
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error { ex }
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Request failed, please try again"
        }
        return response
    }

    fun mvirRportData(mvInspectionId: Long): HashMap<String, Any> {
        val data = hashMapOf<String, Any>()
        val optional = this.motorVehicleItemChecklistRepository.findById(mvInspectionId)
        if (optional.isPresent) {
            val mvInspection = optional.get()
            data["imagePath"] = commonDaoServices.resolveAbsoluteFilePath(applicationMapProperties.mapKebsLogoPath)
            mvInspection.inspection?.inspectionGeneral?.let { general ->
                data["customsIeNo"] = general.customsEntryNumber.orEmpty()
                data["clearingAgent"] = general.clearingAgent.orEmpty()
                data["officerName"] = general.inspectionOfficer.orEmpty()
                data["declarationNumber"] = general.declarationNumber.orEmpty()
                data["declarationRepresentative"] = general.declarationRepresentative.orEmpty()
            } ?: run {
                data["customsIeNo"] = "NA"
                data["clearingAgent"] = "NA"
                data["officerName"] = "NA"
                data["declarationRepresentative"] = "N/A"
                data["declarationNumber"] = "N/A"
            }

            data["mvirNum"] = mvInspection.serialNumber.toString()
            data["inspectionRegion"] = mvInspection.ministryStationId?.stationName.orEmpty()
            data["reportReference"] = mvInspection.ministryReportReference.orEmpty()
            data["makeVehicle"] = mvInspection.makeVehicle ?: ""
            data["engineNumber"] = mvInspection.engineNoCapacity ?: ""
            data["modeOfRelease"] = ""
            data["signedBy"] = ""
            data["chassisNo"] = mvInspection.chassisNo ?: ""
            data["yearOfManufacture"] = mvInspection.yearOfManufacture ?: "N/A"
            data["remarks"] = mvInspection.remarks ?: ""
            data["genDate"] = SimpleDateFormat("dd/MM/yyyy HH:mm").format(mvInspection.createdOn)
            data["dutyStation"] = mvInspection.ministryStationId?.stationName ?: "N/A"

            mvInspection.itemId?.cdDocId?.let { cdDetaisl ->
                data["trackingNum"] = cdDetaisl.ucrNumber.orEmpty()
                data["entryPoint"] = cdDetaisl.freightStation?.cfsName.orEmpty()
                cdDetaisl.cdImporter?.let { importerId ->
                    val cdImporterDetails = daoServices.findCDImporterDetails(importerId)
                    data["emailAddress"] = cdImporterDetails.email.orEmpty()
                    data["idfNumber"] = cdDetaisl.idfNumber.orEmpty()
                    data["name"] = cdImporterDetails.name.orEmpty()
                    data["importerAddress"] = cdImporterDetails.email.orEmpty()
                    data.put("importerName", cdImporterDetails.name.orEmpty())
                }
            }
            data.put("mvir", mvInspection)
        } else {
            throw ExpectedDataNotFound("Inspection ID does not exist")
        }
        return data

    }

    fun sendEmailToImporter(mvInspectionId: Long, cdItemId: Long): Boolean {
        try {
            val data = mvirRportData(mvInspectionId)
            var importerEmail: String? = data["emailAddress"] as String
            if (StringUtils.hasLength(applicationMapProperties.defaultTestEmailAddres)) {
                importerEmail = applicationMapProperties.defaultTestEmailAddres
            }
            if (importerEmail == null) {
                KotlinLogging.logger { }.warn("MVIR ${cdItemId} Importer  does not have a valid email address")
                return false
            }
            val serialNumber = data["mvirNum"] as String
            val fileName = "/tmp/MVIR-${serialNumber}.pdf"
            reportsDaoService.generateEmailPDFReportWithNoDataSource(fileName, data, "classpath:reports/motorVehicleInspectionReport.jrxml")

            val subject = "Motor Vehicle Inspection Request"
            val messageBody = "Please Find The attached motor vehicle details submitted for inspection  \n" +
                    "\n "
            // Deliver email
            notifications.sendEmail(importerEmail, subject, messageBody, fileName)
            return true
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Error sending email", ex)
        }
        return false
    }

    @Transactional
    fun saveSsfDetails(sampleSubmissionDetails: QaSampleSubmissionEntity, itemId: Long, map: ServiceMapsEntity, loggedInUser: UsersEntity): ApiResponseModel {
        val response = ApiResponseModel()
        val cdItemOptional = iCdItemsRepo.findById(itemId)
        if (cdItemOptional.isPresent) {
            var cdItem = cdItemOptional.get()
            sampleSubmissionDetails.brandName = cdItem.productBrandName
            sampleSubmissionDetails.productDescription = cdItem.itemDescription ?: cdItem.hsDescription
            //updating of Details in DB
            if (cdItem.sampledCollectedStatus == map.activeStatus) {
                with(sampleSubmissionDetails) {
                    createdBy = loggedInUser.userName
                    createdOn = Timestamp.from(Instant.now())
                    status = map.activeStatus
                    cdItemId = cdItem.id
                }
                val serviceRequestsEntity = daoServices.ssfSave(cdItem, sampleSubmissionDetails, loggedInUser, map).first
                with(cdItem) {
                    sampleBsNumberStatus = map.initStatus
                    sampleSubmissionStatus = map.activeStatus
                    ssfId = serviceRequestsEntity.id
                }

                cdItem = daoServices.updateCdItemDetailsInDB(cdItem, loggedInUser)
                response.data = CdItemDetailsDao.fromEntity(cdItem)
                response.responseCode = ResponseCodes.SUCCESS_CODE
                response.message = "You have Successful Filled Sample Submission Details"
            } else {
                response.responseCode = ResponseCodes.FAILED_CODE
                response.message = "Please fill SCF form first"
            }
        } else {
            response.responseCode = ResponseCodes.FAILED_CODE
            response.message = "Invalid item identifier configured"
        }
        return response
    }

    fun checklistDetails(map: HashMap<String, Any>, itemDetails: CdItemDetailsEntity) {
        // Checklist details
        itemDetails.cdDocId?.let { cdDetails ->
            map["cocNum"] = cdDetails.cocNumber.orEmpty()
            map["IdfNumber"] = cdDetails.idfNumber.orEmpty()
            map["contentDeclared"] = when {
                cdDetails.idfNumber.isNullOrEmpty() -> "NO"
                else -> "YES"
            }
            map["ucrNumber"] = cdDetails.ucrNumber.orEmpty()
            map["dutyStation"] = cdDetails.freightStation?.cfsName.orEmpty()
            map["officerName"] = "${cdDetails.assignedInspectionOfficer?.firstName} ${cdDetails.assignedInspectionOfficer?.lastName}"

            val consgnorDetails = cdDetails.cdConsignor?.let { it3 -> daoServices.findCdConsignorDetails(it3) }
            consgnorDetails?.let { consignor ->
                map["supplierName"] = consignor.name.toString()
                map["supplierAddress"] = consignor.physicalAddress ?: consignor.postalAddress.toString()
                map["importerTelNo"] = consignor.telephone.orEmpty()
                map["importerFaxNo"] = consignor.fax.orEmpty()
                map["importerEmail"] = consignor.email.orEmpty()
                map["contactName"] = consignor.name.orEmpty()
                map["contactFullAddress"] = consignor.postalAddress ?: consignor.physicalAddress.orEmpty()
                map["contactPhone"] = consignor.telephone.orEmpty()
            }
            val importerDetailsEntity = cdDetails.cdImporter?.let { it1 -> this.daoServices.findCDImporterDetails(it1) }
            importerDetailsEntity?.let { importer ->
                map["importerName"] = importer.name.orEmpty()
                map["importerAddress"] = importer.physicalAddress ?: importer.postalAddress.orEmpty()
                map["importerFaxNo"] = importer.fax.orEmpty()
                map["importerTelNo"] = importer.telephone.orEmpty()
                map["importerPhysicalLocation"] = importer.physicalAddress ?: importer.postalAddress.orEmpty()
            }
        }
        map["nonComplianceDetails"] = "NA"
        map["countryOfOrigin"] = itemDetails.countryOfOrginDesc.orEmpty()
        map["containerNos"] = itemDetails.marksAndContainers.orEmpty()
        getItemChecklist(itemDetails)?.let { (checklistType, checklist) ->
            when (checklistType) {
                ChecklistType.AGROCHEM.name -> {
                    val data = checklist as CdInspectionAgrochemItemChecklistEntity
                    map["expiryDate"] = data.dateExpiry?.let { commonDaoServices.convertDateToString(it, "dd/MM/yyyy") }
                            ?: ""
                    map["packaging"] = ""
                    map["conditionOfProduct"] = data.appearance.orEmpty()
                    map["customsIENo"] = data.inspection?.inspectionGeneral?.customsEntryNumber ?: ""
                    map["manufactureDate"] = data.dateMfgPackaging?.let { commonDaoServices.convertDateToString(it, "dd/MM/yyyy") }
                            ?: ""
                    map["quantityDeclared"] = data.quantityDeclared.orEmpty()
                }
                ChecklistType.ENGINEERING.name -> {
                    val data = checklist as CdInspectionEngineeringItemChecklistEntity
                    map["expiryDate"] = ""
                    map["manufactureDate"] = ""
                    map["packaging"] = ""
                    map["conditionOfProduct"] = ""
                    map["batchNo"] = data.batchNoModelTypeRef.orEmpty()
                    map["quantityDeclared"] = data.quantityDeclared.orEmpty()
                }
                ChecklistType.OTHER.name -> {
                    val data = checklist as CdInspectionOtherItemChecklistEntity
                    map["expiryDate"] = ""
                    map["packaging"] = data.packagingLabelling.orEmpty()
                    map["manufactureDate"] = ""
                    map["conditionOfProduct"] = data.physicalCondition.orEmpty()
                    map["quantityDeclared"] = data.quantityDeclared.orEmpty()
                }
                ChecklistType.VEHICLE.name -> {
                    val data = checklist as CdInspectionMotorVehicleItemChecklistEntity
                    map["expiryDate"] = ""
                    map["packaging"] = ""
                    map["manufactureDate"] = data.manufactureDate?.let { commonDaoServices.convertDateToString(it, "dd/MM/yyyy") }
                            ?: ""
                    map["quantityDeclared"] = ""
                }
            }
        }
    }

    fun getSsfDetails(itemId: Long): HashMap<String, Any> {
        val map = hashMapOf<String, Any>()
        return this.daoServices.findSampleSubmittedItemID(itemId)?.let { inspectionGeneral ->

            map["id"] = inspectionGeneral.id.toString()
            map["ssfNum"] = inspectionGeneral.ssfNo.orEmpty()
            map["ssfRefNo"] = inspectionGeneral.bsNumber.orEmpty()
            map["product"] = inspectionGeneral.productDescription.orEmpty()
            map["genDate"] = commonDaoServices.convertDateToString(inspectionGeneral.createdOn?.toLocalDateTime()
                    ?: LocalDateTime.now(), "dd/MM/yyy")
            map["disposalMode"] = inspectionGeneral.returnOrDispose.orEmpty()
            map["testParameters"] = inspectionGeneral.testParameters.orEmpty()
            map["laboratoryName"] = inspectionGeneral.laboratoryName.orEmpty()
            map["ssfDescription"] = inspectionGeneral.description.orEmpty()
            map["conditionOfSample"] = inspectionGeneral.conditionOfSample.orEmpty()
            val itemDetails = this.daoServices.findItemWithItemID(inspectionGeneral.cdItemId!!)
            itemDetails.cdDocId?.let {
                map["cocNum"] = it.cocNumber.orEmpty()
                map["ucrNumber"] = it.ucrNumber.orEmpty()
                map["dutyStation"] = it.freightStation?.cfsName.orEmpty()
                map["officerName"] = "${it.assignedInspectionOfficer?.firstName} ${it.assignedInspectionOfficer?.lastName}"
            }
            this.checklistDetails(map, itemDetails)
            // Payment details for this item
            this.daoServices.findDemandNoteItemByID(itemId)?.let { itemNote ->
                map["amount"] = itemNote.amountPayable.toString()
                this.daoServices.findDemandNoteWithID(itemNote.demandNoteId ?: -2)?.let { demandNote ->
                    map["receiptNo"] = demandNote.receiptNo ?: "N/A"
                    map["invoiceNo"] = demandNote.demandNoteNumber ?: "N/A"
                    map["paid"] = when (demandNote.paymentStatus) {
                        1 -> "YES"
                        else -> "NO"
                    }
                    map
                } ?: run {
                    map["receiptNo"] = "N/A"
                    map["invoiceNo"] = "N/A"
                    map["paid"] = "N/A"
                    map
                }
            } ?: run {
                map["amount"] = "0.00"
                map["receiptNo"] = "N/A"
                map["invoiceNo"] = "N/A"
                map["paid"] = "N/A"
                map
            }
            this.qaISampleCollectRepository.findByItemId(itemId)?.let { scf ->
                map["sampleSize"] = scf.sampleSize.toString()
                map["scfNo"] = scf.scfNo.orEmpty()
                map["batchSize"] = scf.batchSize.orEmpty()
                map["batchNo"] = scf.batchNo.orEmpty()
                map["tradeMark"] = scf.brandName.orEmpty()
                map["transmissionResult"] = scf.transmissionResult.orEmpty()
                map["referenceStandard"] = scf.referenceStandard.orEmpty()
                map["purposeOfTest"] = scf.reasonForCollectingSample.orEmpty()
                map["product"] = scf.nameOfProduct.orEmpty()
                map["importerName"] = scf.nameOfManufacture.orEmpty()
                map["importerAddress"] = scf.addressOfManufacture.orEmpty()
            }
            map
        } ?: throw ExpectedDataNotFound("SSF details not found")
    }

    fun getItemSampleAndLabResults(itemUuid: String): ApiResponseModel {
        val response = ApiResponseModel()
        this.iCdItemsRepo.findByUuid(itemUuid)?.let {
            val responseData = hashMapOf<String, Any?>()
            responseData["cd_uuid"] = it.cdDocId?.uuid
            responseData["item_details"] = CdItemDetailsDao.fromEntity(it)
            // Sample Collection
            this.qaISampleCollectRepository.findByItemId(it.id!!)?.let { sampleCollected ->
                responseData["scf_details"] = sampleCollected
            }
            // Sample Submitted
            this.daoServices.findSampleSubmittedItemID(it.id!!)?.let { sampleSubmitted ->
                responseData["ssf_details"] = sampleSubmitted
                // Lab Results
                sampleSubmitted.bsNumber?.let { bsNo ->
                    responseData["lab_parameters"] = sampleParametersRepository.findByOrderId(bsNo)
                    responseData["lab_results"] = sampleResultsRepository.findByOrderId(bsNo)
                }
            }
            response.data = responseData
            response.message = "Success"
            response.responseCode = ResponseCodes.SUCCESS_CODE
            response
        } ?: run {
            response.message = "Invalid Item Reference Number"
            response.responseCode = ResponseCodes.NOT_FOUND
            response
        }
        return response
    }

    fun getScfDetails(itemId: Long): HashMap<String, Any> {
        val map = hashMapOf<String, Any>()
        return this.qaISampleCollectRepository.findByItemId(itemId)?.let { inspectionGeneral ->
            map["id"] = inspectionGeneral.id.toString()
            map["slNum"] = inspectionGeneral.batchNo.orEmpty()
            map["product"] = inspectionGeneral.nameOfProduct.orEmpty()
            map["genDate"] = inspectionGeneral.createdOn?.let { commonDaoServices.convertDateToString(it.toLocalDateTime(), "dd-MM-yyyy") }
                    ?: ""
            map["samplingMethod"] = inspectionGeneral.samplingMethod.orEmpty()
            map["sampleSize"] = inspectionGeneral.sampleSize.toString()
            map["batchSize"] = inspectionGeneral.batchSize.toString()
            map["batchNo"] = inspectionGeneral.batchNo.orEmpty()
            map["scfNo"] = inspectionGeneral.scfNo.orEmpty()
            map["officerDesignation"] = "IO"
            map["expiryDate"] = inspectionGeneral.expiryDate.orEmpty()
            map["modeOfRelease"] = inspectionGeneral.modeOfRelease.orEmpty()
            map["manufacturerName"] = inspectionGeneral.nameOfManufacture.orEmpty()
            map["remarks"] = inspectionGeneral.anyRemarks.orEmpty()
            map["witnessName"] = inspectionGeneral.nameOfWitness.orEmpty()
            map["witnessDesignation"] = inspectionGeneral.witnessDesignation.orEmpty()
            map["witnessDate"] = inspectionGeneral.witnessDate?.let { commonDaoServices.convertDateToString(it, "dd-MM-yyyy") }
                    ?: ""
            map["labelDetails"] = inspectionGeneral.labelDetails.orEmpty()
            map["referenceStandard"] = inspectionGeneral.referenceStandard.orEmpty()
            map["purposeOfTest"] = inspectionGeneral.reasonForCollectingSample.orEmpty()
            val itemDetails = this.daoServices.findItemWithItemID(inspectionGeneral.itemId!!)
            map["countryOfOrigin"] = itemDetails.countryOfOrginDesc.orEmpty()
            map["nonComplianceDetails"] = itemDetails.rejectReason.orEmpty()
            this.checklistDetails(map, itemDetails)
            map
        } ?: throw ExpectedDataNotFound("SCF details not found")
    }

    @Transactional
    fun saveScfDetails(sampleCollectionForm: QaSampleCollectionEntity, itemId: Long, loggedInUser: UsersEntity): ApiResponseModel {
        val response = ApiResponseModel()
        var collectionEntity = qaISampleCollectRepository.findByItemId(itemId)
        if (collectionEntity == null) {
            collectionEntity = sampleCollectionForm
            collectionEntity.modifiedOn = Timestamp.from(Instant.now())
            collectionEntity.nameOfOfficer = "${loggedInUser.firstName} ${loggedInUser.lastName}"
            collectionEntity.officerDesignation = "IO"
            collectionEntity.witnessDate = Date(java.util.Date().time)
            collectionEntity.scfNo = "SCF" + SimpleDateFormat("yyyyMMddHH").format(java.util.Date()) + generateRandomText(5).toUpperCase()
            sampleCollectionForm.createdBy = loggedInUser.createdBy
            sampleCollectionForm.createdOn = Timestamp.from(Instant.now())
        } else {
            sampleCollectionForm.modifiedBy = loggedInUser.userName
            sampleCollectionForm.modifiedOn = Timestamp.from(Instant.now())
            collectionEntity.anyRemarks = sampleCollectionForm.anyRemarks
            collectionEntity.nameOfManufacture = sampleCollectionForm.nameOfManufacture
            collectionEntity.addressOfManufacture = sampleCollectionForm.addressOfManufacture
            collectionEntity.brandName = sampleCollectionForm.brandName
            collectionEntity.batchNo = sampleCollectionForm.batchNo
            collectionEntity.permitId = sampleCollectionForm.permitId
            collectionEntity.modeOfRelease = sampleCollectionForm.modeOfRelease
            collectionEntity.expiryDate = sampleCollectionForm.expiryDate
            collectionEntity.labelDetails = sampleCollectionForm.labelDetails
            collectionEntity.batchSize = sampleCollectionForm.batchSize
            collectionEntity.sampleSize = sampleCollectionForm.sampleSize
            collectionEntity.samplingMethod = sampleCollectionForm.samplingMethod
            collectionEntity.reasonForCollectingSample = sampleCollectionForm.reasonForCollectingSample
        }
        val cdItemOptional = iCdItemsRepo.findById(itemId)
        if (cdItemOptional.isPresent) {
            val cdItem = cdItemOptional.get()
            val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
            if (cdItem.sampledStatus == map.activeStatus) {
                // Update SCF product details
                collectionEntity.nameOfProduct = cdItem.hsDescription ?: cdItem.productTechnicalName ?: "UNKNOWN"
                val entity = this.qaISampleCollectRepository.save(collectionEntity)
                // Manufacturer details
                collectionEntity.brandName = cdItem.productBrandName ?: ""
                cdItem.cdDocId?.cdImporter?.let {
                    val importerDetails = daoServices.findCDImporterDetails(it)
                    collectionEntity.addressOfManufacture = importerDetails.physicalAddress
                            ?: importerDetails.postalAddress
                    collectionEntity.nameOfManufacture = importerDetails.name
                    importerDetails
                }
                //Update CD item SCF status
                cdItem.sampledCollectedStatus = map.activeStatus
                cdItem.sampleSubmissionStatus = map.initStatus
                collectionEntity.itemId = cdItem.id
                cdItem.scfId = entity.id
                iCdItemsRepo.save(cdItem)
                //
                response.responseCode = ResponseCodes.SUCCESS_CODE
                response.message = "Sample Collection Form received"
            } else {
                response.responseCode = ResponseCodes.FAILED_CODE
                response.message = "Item was not marked as sampled"
            }
        } else {
            response.message = "Invalid item details submitted"
            response.responseCode = ResponseCodes.FAILED_CODE
        }
        return response
    }

    @Transactional
    fun updateSsfResult(form: SsfResultForm, item: CdItemDetailsEntity, map: ServiceMapsEntity, loggedInUser: UsersEntity): ApiResponseModel {
        val response = ApiResponseModel()
        val sample = daoServices.findSampleSubmittedBYCdItemID(item.id!!)
        if (item.ssfId != null) {
            if (item.sampleBsNumberStatus != map.activeStatus) {
                if (limsServices.checkBsNumberExistOnLims(form.bsNumber ?: "")) {
                    item.sampleBsNumberStatus = map.activeStatus
                    iCdItemsRepo.save(item)
                    sample.bsNumber = form.bsNumber
                    sample.ssfNo = "SSF" + SimpleDateFormat("yyyyMMddHH").format(java.util.Date()) + generateRandomText(5).toUpperCase()
                    sample.labResultsStatus = map.inactiveStatus
                    sample.ssfSubmissionDate = Date(Date().time)
                    form.submissionDate?.let {
                        try {
                            sample.ssfSubmissionDate = Date.valueOf(it)
                        } catch (ex: Exception) {
                            sample.ssfSubmissionDate = Date(Date().time)
                        }
                    }
                    sample.complianceRemarks = form.remarks
                    daoServices.ssfUpdateDetails(item, sample, loggedInUser, map)
                    response.message = "Sample BS number and SSF NO updated"
                    response.responseCode = ResponseCodes.SUCCESS_CODE
                } else {
                    response.message = "BS number not found on LIMS, please enter a correct BS number"
                    response.responseCode = ResponseCodes.FAILED_CODE
                }
            } else {
                response.message = "Sample BS number already submitted"
                response.responseCode = ResponseCodes.FAILED_CODE
            }
        } else {
            response.message = "Sampling has not been requested for this item"
            response.responseCode = ResponseCodes.FAILED_CODE
        }
        return response
    }

    // Assign Ministry user
    fun assignMinistryUser(mvInspectionId: Long, stationId: Long): HashMap<String, Any?> {
        val data = hashMapOf<String, Any?>()
        val optional = this.motorVehicleItemChecklistRepository.findById(mvInspectionId)
        if (optional.isPresent) {
            val ministryInspection = optional.get()
            commonDaoServices.findAllUsersWithMinistryUserType()?.let { ministryUsers ->
                if (ministryUsers.isNotEmpty()) {
                    ministryUsers[Random().nextInt(ministryUsers.size)].let {
                        ministryInspection.assignedUser = it
                        data["ministryUser"] = it.userName
                        data["userId"] = it.id
                        this.motorVehicleItemChecklistRepository.save(ministryInspection)
                    }
                } else {
                    throw ExpectedDataNotFound("No ministry user available, please add ministry user and try again")
                }
            }
        }
        return data
    }

    fun requestMinistryInspection(mvInspectionId: Long, stationId: Long): ApiResponseModel {
        val response = ApiResponseModel()
        val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        val optional = this.motorVehicleItemChecklistRepository.findById(mvInspectionId)
        if (optional.isPresent) {
            val motorVehicleInspection = optional.get()
            if (motorVehicleInspection.ministryReportSubmitStatus == map.activeStatus) {
                motorVehicleInspection.itemId?.let { cdItemDetails ->
                    cdItemDetails.ministrySubmissionStatus = map.activeStatus
                    val ministryStation = this.ministryStationRepo.findById(stationId)
                    if (ministryStation.isPresent) {
                        motorVehicleInspection.ministryStationId = ministryStation.get()
                        motorVehicleInspection.ministryReportSubmitStatus = map.activeStatus
                        this.bpmn.startMinistryInspection(motorVehicleInspection, cdItemDetails)
                        // Update
                        try {
                            daoServices.updateCDItemDetails(cdItemDetails, cdItemDetails.id!!, loggedInUser, map)
                            this.motorVehicleItemChecklistRepository.save(motorVehicleInspection)
                            cdItemDetails.consigmentId?.let {
                                val cdDetails = daoServices.findCD(it)
                                cdDetails.status = ConsignmentApprovalStatus.WAITING.code
                                cdDetails.varField10 = "Waiting for ministry inspection report"
                                daoServices.updateCDStatus(cdDetails, ConsignmentDocumentStatus.MINISTRY_REQUEST)
                            }
                        } catch (ex: Exception) {
                            KotlinLogging.logger { }.error("Failed to update consignment", ex)
                        }
                        response.data = InspectionMotorVehicleItemDto.fromEntity(motorVehicleInspection)
                        response.message = "Data Submitted Successfully"
                        response.responseCode = ResponseCodes.SUCCESS_CODE
                    } else {
                        response.message = "Invalid ministry station request"
                        response.responseCode = ResponseCodes.NOT_FOUND
                    }
                }
            } else {
                response.message = "Ministry station report already submitted"
                response.responseCode = ResponseCodes.FAILED_CODE
            }
        } else {
            response.message = "Inspection request does not exist"
            response.responseCode = ResponseCodes.NOT_FOUND
        }
        return response
    }

    fun motorVehicleInspectionReportDetails(mvInspectionId: Long): HashMap<String, Any> {
        val map = hashMapOf<String, Any>()
        motorVehicleItemChecklistRepository.findByIdOrNull(mvInspectionId)?.let { mvInspectionChecklist ->
            val inspectionGeneral = mvInspectionChecklist.inspection?.inspectionGeneral
            map["inspectionType"] = inspectionGeneral?.inspection.orEmpty()
            map["productCategory"] = "Motor Vehicle inspection"
            map["Date"] = inspectionGeneral?.inspectionDate?.toString() ?: ""
            map["issuedOn"] = commonDaoServices.convertDateToString(inspectionGeneral?.inspectionDate, "dd-MMMM-yyyy")
            map["ImportersName"] = inspectionGeneral?.importersName.orEmpty()
            map["ClearingAgent"] = inspectionGeneral?.clearingAgent.orEmpty()
            map["CustomsEntryNo"] = inspectionGeneral?.customsEntryNumber.orEmpty()
            map["inspectionOfficer"] = inspectionGeneral?.inspectionOfficer.orEmpty()
            map["supervisorName"] = inspectionGeneral?.supervisorName.orEmpty()
            map["FeePaid"] = "No"
            map["ReceiptNo"] = ""
            inspectionGeneral?.cdDetails?.let {
                map["EntryPoint"] = it.freightStation?.cfsName.orEmpty()
                map["Cfs"] = it.freightStation?.cfsCode.orEmpty()
                map["IdfNo"] = it.idfNumber.orEmpty()
                map["CocNo"] = it.corNumber.orEmpty()
                daoServices.findDemandNoteWithPaymentStatus(it.id!!, 1)?.let { demandNote ->
                    map["FeePaid"] = "Yes"
                    map["ReceiptNo"] = demandNote.demandNoteNumber.orEmpty()
                    map
                }
            }
            map["ministryRepresentative"] = ""
            map["signatures"] = ""
            mvInspectionChecklist.assignedUser?.let { user ->
                map["ministryRepresentative"] = "${user.firstName} ${user.lastName}".trim()
            }
            mvInspectionChecklist.ministryStationId?.let { ministry ->
                map["ministryStation"] = ministry.stationName.orEmpty()
            } ?: {
                map["ministryStation"] = "N/A"
            }
            map["UcrNo"] = inspectionGeneral?.ucrNumber.orEmpty()
            map["SerialNo"] = mvInspectionChecklist.serialNumber.orEmpty()
            map["VehicleMake"] = mvInspectionChecklist.makeVehicle.toString()
            map["ChassisNo"] = mvInspectionChecklist.chassisNo.toString()
            map["EngineCapacity"] = mvInspectionChecklist.engineNoCapacity.toString()
            map["ManufacturerDate"] = mvInspectionChecklist.manufactureDate.toString()
            map["RegistrationDate"] = mvInspectionChecklist.registrationDate.toString()
            map["OdometreReading"] = mvInspectionChecklist.odemetreReading.toString()
            map["Drive"] = mvInspectionChecklist.driveRhdLhd.toString()
            map["Transmission"] = mvInspectionChecklist.transmissionAutoManual.toString()
            map["Colour"] = mvInspectionChecklist.colour.toString()
            map["OverallAppearance"] = mvInspectionChecklist.overallAppearance.toString()
            map["OverallAppearance"] = mvInspectionChecklist.overallAppearance.toString()
            map["Remarks"] = mvInspectionChecklist.remarks.toString()
            map["OverallRemarks"] = inspectionGeneral?.overallRemarks.orEmpty()

        } ?: throw ExpectedDataNotFound("Motor Vehicle Inspection Checklist does not exist")
        return map
    }

    fun listMinistryInspection(statuses: Int, page: PageRequest): ApiResponseModel {
        val requests: Page<CdInspectionMotorVehicleItemChecklistEntity>
        val ministryInspectionItems: MutableList<MinistryInspectionListResponseDto> = ArrayList()
        val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
        val response = ApiResponseModel()
        if (statuses > 0) {
            KotlinLogging.logger { }.info("Completed ministry inspections")
            requests = motorVehicleItemChecklistRepository.findByMinistryReportSubmitStatusInAndSampled(listOf(map.activeStatus), "YES", page)
        } else {
            KotlinLogging.logger { }.info("On Going ministry inspections")
            requests = motorVehicleItemChecklistRepository.findByMinistryReportSubmitStatusInAndSampled(listOf(map.initStatus, map.workingStatus, map.testStatus), "YES", page)
        }
        // Get inspection items
        requests.toList().forEach {
            it.itemId?.let { itemDetails ->
                val d = this.daoServices.convertCdItemDetailsToMinistryInspectionListResponseDto(itemDetails, map.activeStatus == it.ministryReportSubmitStatus)
                d.inspectionId = it.id
                d.remarks = it.remarks
                ministryInspectionItems.add(d)
            }
        }
        response.pageNo = requests.number
        response.totalPages = requests.totalPages
        response.data = ministryInspectionItems
        response.extras = daoServices.motorVehicleMinistryInspectionChecklistName
        response.responseCode = ResponseCodes.SUCCESS_CODE
        response.message = "Success"

        return response
    }

    fun loadGeneralInspection(inspectionGeneral: CdInspectionGeneralEntity?): Map<out String, Any> {
        val map = hashMapOf<String, Any>()
        map["inspection"] = inspectionGeneral?.inspection.toString()
        val date = commonDaoServices.convertDateToString(inspectionGeneral?.createdOn?.toLocalDateTime()
                ?: LocalDateTime.now(), "dd-MMMM-yyyy")
        map["csName"] = ""
        map["csDate"] = date
        map["oicDate"] = date
        map["oicName"] = ""
        map["createdOn"] = date
        map["overallRemarks"] = inspectionGeneral?.overallRemarks ?: ""
        map["inspectionDate"] = inspectionGeneral?.inspectionDate.toString()
        map["importersName"] = inspectionGeneral?.importersName.toString()
        map["clearingAgent"] = inspectionGeneral?.clearingAgent.toString()
        map["customsEntryNumber"] = inspectionGeneral?.customsEntryNumber.toString()

        inspectionGeneral?.cdDetails?.let {
            map["idfNumber"] = it.idfNumber ?: ""
            map["cocNumber"] = it.cocNumber ?: ""
            map["ucrNumber"] = it.ucrNumber ?: ""
            map["inspectionOfficer"] = "${it.assignedInspectionOfficer?.firstName.orEmpty()} ${it.assignedInspectionOfficer?.lastName.orEmpty()}".trim()
            map["supervisor"] = "${it.assigner?.firstName.orEmpty()} ${it.assigner?.lastName.orEmpty()}".trim()
            map["receiptNumber"] = "NA"
            map["feePaid"] = "0.0"

            daoServices.findDemandNoteWithPaymentStatus(it.id!!, 1)?.let { dn ->
                map["receiptNumber"] = dn.demandNoteNumber.toString()
                map["feePaid"] = dn.amountPaid?.toString() ?: "0.0"
            }
            map["entryPoint"] = it.freightStation?.cfsName ?: "NA"
            map["cfs"] = it.freightStation?.cfsCode ?: "NA"
            map["cdId"] = it.id ?: 0L
            it.cdImporter?.let { importterId ->
                this.daoServices.findCDImporterDetails(importterId).let { importer ->
                    map["importersName"] = importer.name ?: inspectionGeneral.importersName.toString()
                }
            }
        }
        return map
    }

    fun getAgrochemChecklistDetails(inspectionId: Long): Map<out String, Any> {
        KotlinLogging.logger { }.info("Agrochem checklist details")
        val map = hashMapOf<String, Any>()
        agrochemChecklistRepository.findByIdOrNull(inspectionId)?.let { mvInspectionChecklist ->
            KotlinLogging.logger { }.info("Agrochem checklist details Found")
            val inspectionGeneral = mvInspectionChecklist.inspectionGeneral
            map["inspection"] = inspectionGeneral?.inspection.toString()
            map["documentCategory"] = mvInspectionChecklist.inspectionChecklistType?.typeName
                    ?: "Agrochemical checklist"
            map["itemType"] = "AGROCHEMICAL"
            map["cfs"] = inspectionGeneral?.cfs.toString()
            map["inspectionDate"] = inspectionGeneral?.inspectionDate.toString()
            map["inspectionOfficer"] = inspectionGeneral?.inspectionOfficer.orEmpty()
            map["importersName"] = inspectionGeneral?.importersName.toString()
            map["clearingAgent"] = inspectionGeneral?.clearingAgent.toString()
            map["customsEntryNumber"] = inspectionGeneral?.customsEntryNumber.toString()

            inspectionGeneral?.cdDetails?.let {
                map["idfNumber"] = it.idfNumber ?: ""
                map["cocNumber"] = it.cocNumber ?: ""
                map["ucrNumber"] = it.ucrNumber ?: ""
                map["receiptNumber"] = "NA"
                map["feePaid"] = "0.0"
                daoServices.findDemandNoteWithPaymentStatus(it.id!!, 1)?.let {
                    map["receiptNumber"] = it.demandNoteNumber.toString()
                    map["feePaid"] = it.amountPayable.toString()
                }
                map["entryPoint"] = it.freightStation?.cfsName ?: "NA"
                map["cfs"] = it.freightStation?.cfsCode ?: "NA"
                map["cdId"] = it.id ?: 0L
                it.cdImporter?.let { importterId ->
                    this.daoServices.findCDImporterDetails(importterId).let { importer ->
                        map["importersName"] = importer.name ?: inspectionGeneral.importersName.toString()
                    }
                }
            }
            map["id"] = mvInspectionChecklist.id ?: 0L
            map["serialNumber"] = mvInspectionChecklist.serialNumber.toString()
            val items = InspectionAgrochemItemDto.fromList(agrochemItemChecklistRepository.findByInspection(mvInspectionChecklist))
            map["items"] = items
            KotlinLogging.logger { }.info("Number of items: ${items.size}")
            map["itemNumber"] = items.size
            map["remarks"] = mvInspectionChecklist.remarks.orEmpty()
            map["overallRemarks"] = inspectionGeneral?.overallRemarks.orEmpty()
            map
        } ?: throw ExpectedDataNotFound("Agrochemical Inspection Checklist does not exist")
        return map
    }

    fun getEngineeringChecklistDetails(inspectionId: Long): Map<out String, Any> {
        KotlinLogging.logger { }.info("Engineering checklist details")
        val map = hashMapOf<String, Any>()
        engineeringChecklistRepository.findByIdOrNull(inspectionId)?.let { mvInspectionChecklist ->
            KotlinLogging.logger { }.info("Engineering checklist details Found")
            val inspectionGeneral = mvInspectionChecklist.inspectionGeneral
            map.putAll(loadGeneralInspection(inspectionGeneral))
            map["documentCategory"] = mvInspectionChecklist.inspectionChecklistType?.typeName
                    ?: "Engineering checklist"
            map["itemType"] = "ENGINEERING"
            map["id"] = mvInspectionChecklist.id ?: 0L
            map["serialNumber"] = mvInspectionChecklist.serialNumber.toString()
            map["items"] = InspectionEngineeringItemDto.fromList(engineeringItemChecklistRepository.findByInspection(mvInspectionChecklist))
            map["remarks"] = mvInspectionChecklist.remarks.orEmpty()
            map["overallRemarks"] = mvInspectionChecklist.remarks.orEmpty()
            map
        } ?: throw ExpectedDataNotFound("Engineering Inspection Checklist does not exist")
        return map
    }

    fun getOtherChecklistDetails(inspectionId: Long): Map<out String, Any> {
        KotlinLogging.logger { }.info("Other checklist details")
        val map = hashMapOf<String, Any>()
        otherChecklistRepository.findByIdOrNull(inspectionId)?.let { mvInspectionChecklist ->
            KotlinLogging.logger { }.info("Other checklist details Found")
            map.putAll(loadGeneralInspection(mvInspectionChecklist.inspectionGeneral))
            map["documentCategory"] = mvInspectionChecklist.inspectionChecklistType?.typeName ?: "Other Checklist"
            map["itemType"] = "OTHER ITEMS"
            map["id"] = mvInspectionChecklist.id ?: 0L
            map["serialNumber"] = mvInspectionChecklist.serialNumber.toString()
            map["items"] = InspectionOtherItemDto.fromList(otherItemChecklistRepository.findByInspection(mvInspectionChecklist))
            map["remarks"] = mvInspectionChecklist.remarks.toString()
            map["overallRemarks"] = mvInspectionChecklist.remarks.toString()
            map
        } ?: throw ExpectedDataNotFound("Other Inspection Checklist does not exist")
        return map
    }

    fun getVehicleChecklistDetails(inspectionId: Long): Map<out String, Any> {
        KotlinLogging.logger { }.info("Vehicle checklist details")
        val map = hashMapOf<String, Any>()
        motorVehicleChecklistRepository.findByIdOrNull(inspectionId)?.let { mvInspectionChecklist ->
            KotlinLogging.logger { }.info("Motor Vehicle checklist details Found")
            map.putAll(loadGeneralInspection(mvInspectionChecklist.inspectionGeneral))
            map["documentCategory"] = mvInspectionChecklist.inspectionChecklistType?.typeName ?: "Vehicle Checklist"
            map["id"] = mvInspectionChecklist.id ?: 0L
            map["itemType"] = "VEHICLE"
            map["ministryRepresentative"] = ""
            map["serialNumber"] = mvInspectionChecklist.serialNumber.orEmpty()
            map["items"] = InspectionMotorVehicleItemDto.fromList(motorVehicleItemChecklistRepository.findByInspection(mvInspectionChecklist))
            map["remarks"] = mvInspectionChecklist.remarks.orEmpty()
            map["overallRemarks"] = mvInspectionChecklist.remarks.orEmpty()
            map
        } ?: throw ExpectedDataNotFound("Other Inspection Checklist does not exist")
        return map
    }


    fun getAllChecklistDetails(cdUuid: String): Map<out String, Any> {
        val document = this.daoServices.findCDWithUuid(cdUuid)
        KotlinLogging.logger { }.info("All checklist details")
        val map = hashMapOf<String, Any>()
        inspectionGeneralRepo.findFirstByCdDetailsAndCurrentChecklist(document, 1)?.let { inspectionGeneral ->
            KotlinLogging.logger { }.info("All checklist details Found")
            map.putAll(loadGeneralInspection(inspectionGeneral))
            map["documentCategory"] = "All Checklists"
            map["hasOthers"] = false
            map["hasAgrochemical"] = false
            map["hasVehicles"] = false
            map["hasEngineering"] = false
            val dataSources = hashMapOf<String, List<Any>>()
            // AgroChecmical
            agrochemChecklistRepository.findByInspectionGeneral(inspectionGeneral)?.let {
                val items = InspectionAgrochemItemDto.fromList(agrochemItemChecklistRepository.findByInspection(it))
                dataSources["agrochemicalItemsDatasource"] = items
                map["hasAgrochemical"] = items.isNotEmpty()
            }
            //Engineering
            engineeringChecklistRepository.findByInspectionGeneral(inspectionGeneral)?.let {
                val items = InspectionEngineeringItemDto.fromList(engineeringItemChecklistRepository.findByInspection(it))
                dataSources["engineeringItemsDatasource"] = items
                map["hasEngineering"] = items.isNotEmpty()
            }
            // Vehicle
            motorVehicleChecklistRepository.findByInspectionGeneral(inspectionGeneral)?.let {
                val items = InspectionMotorVehicleItemDto.fromList(motorVehicleItemChecklistRepository.findByInspection(it))
                dataSources["motorVehicleItemsDatasource"] = items
                map["hasVehicles"] = items.isNotEmpty()
            }
            // Other
            otherChecklistRepository.findByInspectionGeneral(inspectionGeneral)?.let {
                val items = InspectionOtherItemDto.fromList(otherItemChecklistRepository.findByInspection(it))
                dataSources["otherItemsDatasource"] = items
                map["hasOthers"] = items.isNotEmpty()
            }
            KotlinLogging.logger { }.info("Date: $map")
            map["dataSources"] = dataSources
            map
        } ?: throw ExpectedDataNotFound("Other Inspection Checklist does not exist")
        return map
    }

    fun getItemChecklist(itemDetails: CdItemDetailsEntity): Pair<String, Any>? {
        var resultData: Pair<String, Any>? = null
        inspectionGeneralRepo.findFirstByCdDetailsAndCurrentChecklist(itemDetails.cdDocId!!, 1)?.let { inspectionGeneral ->
            itemDetails.checkListTypeId?.let { checklistType ->
                when (checklistType.typeName) {
                    ChecklistType.AGROCHEM.name -> {
                        agrochemItemChecklistRepository.findByInspection_InspectionGeneralAndItemId_Id(inspectionGeneral, itemDetails.id)?.let { checkList ->
                            resultData = Pair(ChecklistType.AGROCHEM.name, checkList)
                            resultData
                        }
                    }
                    ChecklistType.ENGINEERING.name -> {
                        engineeringItemChecklistRepository.findByInspection_InspectionGeneralAndItemId_Id(inspectionGeneral, itemDetails.id)?.let { checkList ->
                            resultData = Pair(ChecklistType.ENGINEERING.name, checkList)
                            resultData
                        }
                    }
                    ChecklistType.VEHICLE.name -> {
                        motorVehicleItemChecklistRepository.findByInspection_InspectionGeneralAndItemId(inspectionGeneral, itemDetails)?.let { checkList ->
                            resultData = Pair(ChecklistType.VEHICLE.name, checkList)
                            resultData
                        }
                    }
                    ChecklistType.OTHER.name -> {
                        otherItemChecklistRepository.findByInspection_InspectionGeneralAndItemId_Id(inspectionGeneral, itemDetails.id!!)?.let { checkList ->
                            resultData = Pair(ChecklistType.OTHER.name, checkList)
                            resultData
                        }
                    }

                    else -> {
                    }
                }
            }
        }
        return resultData
    }
}
