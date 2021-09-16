package org.kebs.app.kotlin.apollo.api.service;

import mu.KotlinLogging
import okhttp3.internal.toLongOrDefault
import org.kebs.app.kotlin.apollo.api.payload.*
import org.kebs.app.kotlin.apollo.api.payload.request.ConsignmentUpdateRequest
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DestinationInspectionDaoServices
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.ServiceMapsEntity
import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import org.kebs.app.kotlin.apollo.store.model.di.*
import org.kebs.app.kotlin.apollo.store.model.qa.QaSampleCollectionEntity
import org.kebs.app.kotlin.apollo.store.model.qa.QaSampleSubmissionEntity
import org.kebs.app.kotlin.apollo.store.repo.di.*
import org.kebs.app.kotlin.apollo.store.repo.qa.IQaSampleCollectionRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import java.sql.Timestamp
import java.time.Instant

@Service
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
        private val qaISampleCollectRepository: IQaSampleCollectionRepository,
        private val consignmentAuditService: ConsignmentDocumentAuditService,
        private val daoServices: DestinationInspectionDaoServices,
) {

    fun addEngineeringSsf(map: ServiceMapsEntity, cdItemID: Long, sampleSubmissionDetails: QaSampleSubmissionEntity, loggedInUser: UsersEntity): ApiResponseModel {
        var response = ApiResponseModel()
        val enginerringItem = engineeringItemChecklistRepository.findByIdOrNull(cdItemID)
        enginerringItem?.let {
            engineeringItemChecklistRepository.save(it)
            response = saveSsfDetails(sampleSubmissionDetails, enginerringItem.itemId!!, map, loggedInUser)
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
            response = saveSsfDetails(sampleSubmissionDetails, enginerringItem.itemId!!, map, loggedInUser)
            response
        } ?: run {
            response.message = "Invalid checklist identifier"
            response.responseCode = ResponseCodes.NOT_FOUND
            response
        }
        return response
    }

    fun addEngineeringChecklist(map: ServiceMapsEntity, general: CdInspectionGeneralEntity, itemList: List<CdInspectionEngineeringItemChecklistEntity>?, engineeringChecklist: CdInspectionEngineeringChecklist, loggedInUser: UsersEntity) {
        var engineering: CdInspectionEngineeringChecklist? = engineeringChecklistRepository.findByInspectionGeneral(general)
        if (engineering == null) {
            engineering = engineeringChecklist
            val checkListType = this.iChecklistInspectionTypesRepo.findByTypeName(daoServices.engineeringItemChecklistType)
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
                val itemDetails = iCdItemsRepo.findById(itm.itemId!!)
                if (itemDetails.isPresent) {
                    val detail = itemDetails.get()
                    var checklistItem: CdInspectionEngineeringItemChecklistEntity? = this.engineeringItemChecklistRepository.findByInspectionAndItemId(engineering, itm.itemId)
                    if (checklistItem == null) {
                        checklistItem = itm
                        checklistItem.createdBy = loggedInUser.userName
                        checklistItem.createdOn = Timestamp.from(Instant.now())
                    } else {
                        checklistItem.modifiedBy = loggedInUser.userName
                        checklistItem.modifiedOn = Timestamp.from(Instant.now())
                    }
                    // Details
                    checklistItem.sampled = itm.sampled
                    if ("YES".equals(itm.sampled)) {
                        checklistItem.sampleUpdated = 0
                    } else {
                        checklistItem.sampleUpdated = 2
                    }
                    checklistItem.itemId = itm.itemId
                    checklistItem.sizeClassCapacity = detail.productClassCode
                    checklistItem.description = detail.hsDescription
                    checklistItem.brand = detail.productBrandName
                    checklistItem.serialNumber = itm.serialNumber
                    checklistItem.compliant = itm.compliant
                    checklistItem.inspection = engineering

                    this.engineeringItemChecklistRepository.save(checklistItem)
                    // Update checklist update
                    daoServices.checkIfChecklistUndergoesSampling(
                            checklistItem.sampled!!,
                            detail,
                            map
                    )
                }
            }
        }
    }

    fun addAgrochemChecklist(map: ServiceMapsEntity, general: CdInspectionGeneralEntity, itemList: List<CdInspectionAgrochemItemChecklistEntity>?, engineeringChecklist: CdInspectionAgrochemChecklist, loggedInUser: UsersEntity) {
        var agrochemChecklist: CdInspectionAgrochemChecklist? = agrochemChecklistRepository.findByInspectionGeneral(general)
        if (agrochemChecklist == null) {
            agrochemChecklist = engineeringChecklist
            val checkListType = this.iChecklistInspectionTypesRepo.findByTypeName(daoServices.agrochemItemChecklistType)
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
                val itemDetails = iCdItemsRepo.findById(itm.itemId!!)
                if (itemDetails.isPresent) {
                    val detail = itemDetails.get()
                    var checklistItem: CdInspectionAgrochemItemChecklistEntity? = this.agrochemItemChecklistRepository.findByInspectionAndItemId(agrochemChecklist, itm.itemId)
                    if (checklistItem == null) {
                        checklistItem = itm
                        checklistItem.createdBy = loggedInUser.userName
                        checklistItem.createdOn = Timestamp.from(Instant.now())
                    } else {
                        checklistItem.modifiedBy = loggedInUser.userName
                        checklistItem.modifiedOn = Timestamp.from(Instant.now())
                    }
                    // Details
                    checklistItem.sampled = itm.sampled
                    if ("YES".equals(itm.sampled)) {
                        checklistItem.sampleUpdated = 0
                    } else {
                        checklistItem.sampleUpdated = 2
                    }
                    checklistItem.itemId = itm.itemId
                    checklistItem.description = detail.hsDescription
                    checklistItem.brand = detail.productBrandName
                    checklistItem.serialNumber = itm.serialNumber
                    checklistItem.compliant = itm.compliant
                    checklistItem.inspection = agrochemChecklist
                    this.agrochemItemChecklistRepository.save(checklistItem)
                    // Mark item as sampled
                    daoServices.checkIfChecklistUndergoesSampling(
                            checklistItem.sampled!!,
                            detail,
                            map
                    )
                }
            }
        }
    }

    fun addVehicleChecklist(map: ServiceMapsEntity, general: CdInspectionGeneralEntity, itemList: List<CdInspectionMotorVehicleItemChecklistEntity>?, engineeringChecklist: CdInspectionMotorVehicleChecklist, loggedInUser: UsersEntity) {
        var vehicleChecklist: CdInspectionMotorVehicleChecklist? = motorVehicleChecklistRepository.findByInspectionGeneral(general)
        if (vehicleChecklist == null) {
            vehicleChecklist = engineeringChecklist
            val checkListType = this.iChecklistInspectionTypesRepo.findByTypeName(daoServices.motorVehicleItemChecklistType)
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
                val itemDetails = iCdItemsRepo.findById(itm.itemId!!)
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
                    // Update details
                    checklistItem.sampled = itm.sampled
                    if ("YES".equals(itm.sampled)) {
                        checklistItem.sampleUpdated = 0
                    } else {
                        checklistItem.sampleUpdated = 2
                    }

                    checklistItem.itemId = itm.itemId
                    checklistItem.description = detail.hsDescription
                    checklistItem.serialNumber = itm.serialNumber
                    checklistItem.compliant = itm.compliant
                    checklistItem.inspection = vehicleChecklist
                    this.motorVehicleItemChecklistRepository.save(checklistItem)
                    // Update Item
                    daoServices.checkIfChecklistUndergoesSampling(
                            checklistItem.sampled!!,
                            detail,
                            map
                    )
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

    fun addOtherChecklist(map: ServiceMapsEntity, general: CdInspectionGeneralEntity, itemList: List<CdInspectionOtherItemChecklistEntity>?, requestChecklist: CdInspectionOtherChecklist, loggedInUser: UsersEntity) {
        var otherChecklist: CdInspectionOtherChecklist? = otherChecklistRepository.findByInspectionGeneral(general)
        if (otherChecklist == null) {
            otherChecklist = requestChecklist
            val checkListType = this.iChecklistInspectionTypesRepo.findByTypeName(daoServices.otherItemChecklistType)
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
                val itemDetails = iCdItemsRepo.findById(itm.itemId!!)
                if (itemDetails.isPresent) {
                    val detail = itemDetails.get()
                    var checklistItem: CdInspectionOtherItemChecklistEntity? = this.otherItemChecklistRepository.findByInspectionAndItemId(otherChecklist, itm.itemId)
                    if (checklistItem == null) {
                        checklistItem = itm
                        checklistItem.createdBy = loggedInUser.userName
                        checklistItem.createdOn = Timestamp.from(Instant.now())
                    } else {
                        checklistItem.modifiedBy = loggedInUser.userName
                        checklistItem.modifiedOn = Timestamp.from(Instant.now())
                    }
                    checklistItem.sampled = itm.sampled
                    checklistItem.category = itm.category
                    if ("YES".equals(itm.sampled)) {
                        checklistItem.sampleUpdated = map.activeStatus
                    } else {
                        checklistItem.sampleUpdated = 2
                    }
                    checklistItem.itemId = itm.itemId
                    checklistItem.description = detail.hsDescription
                    checklistItem.serialNumber = itm.serialNumber
                    checklistItem.compliant = itm.compliant
                    checklistItem.status = map.activeStatus
                    checklistItem.inspection = otherChecklist
                    this.otherItemChecklistRepository.save(checklistItem)
                    // Update sampling status
                    daoServices.checkIfChecklistUndergoesSampling(
                            checklistItem.sampled!!,
                            detail,
                            map
                    )
                }
            }
        }
    }

    fun ministryInspectionList(inspectionChecklistId: Long, comment: String?, docFile: MultipartFile): ApiResponseModel {
        val response = ApiResponseModel()
        val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        KotlinLogging.logger { }.info("mvInspectionChecklistId = $inspectionChecklistId")
        motorVehicleItemChecklistRepository.findByIdOrNull(inspectionChecklistId)
                ?.let { inspectionMotorVehicle ->
                    inspectionMotorVehicle.ministryReportFile = docFile.bytes
                    inspectionMotorVehicle.ministryReportReinspectionRemarks = comment
                    inspectionMotorVehicle.ministryReportSubmitStatus = map.activeStatus
                    daoServices.updateCdInspectionMotorVehicleItemChecklistInDB(
                            inspectionMotorVehicle,
                            loggedInUser
                    ).let { cdInspectionMVChecklist ->
                        cdInspectionMVChecklist.inspection?.inspectionGeneral?.cdDetails?.let { cdetails ->
                            //Update status
                            cdetails.cdStandard?.let { cdStd ->
                                daoServices.updateCDStatus(cdStd, applicationMapProperties.mapDIStatusTypeMinistryInspectionUploadedId)
                            }
                            this.consignmentAuditService.addHistoryRecord(cdetails.id, cdetails.ucrNumber, comment, "MINISTRY", "Ministry Inspection Report submitted")
                        }
                    }
                    // Save submission status
                    this.iCdItemsRepo.findByIdOrNull(inspectionMotorVehicle.itemId)?.let { cdItem ->
                        cdItem.ministrySubmissionStatus = 1
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

    fun updateMotorVehicleComplianceStatus(req: ServerRequest): ServerResponse {
        val response = ApiResponseModel()
        val form = req.body(ConsignmentUpdateRequest::class.java)
        req.pathVariable("inspectionChecklistId").let { inspectionChecklistId ->
            val inspectionChecklistId = inspectionChecklistId.toLongOrDefault(0L)
            commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
                    .let {
                        KotlinLogging.logger { }.info { "generalInspectionChecklistId = $inspectionChecklistId" }
                        this.motorVehicleItemChecklistRepository.findByIdOrNull(inspectionChecklistId)?.let { cdInspectionGeneralEntity ->
                            cdInspectionGeneralEntity.inspection?.let { consignmentDocumentDetailsEntity ->
//                                        cdInspectionGeneralEntity.compliantStatus = form.compliantStatus
//                                        cdInspectionGeneralEntity.compliantDate = commonDaoServices.getCurrentDate()
//                                        cdInspectionGeneralEntity.compliantRemarks = form.remarks
//                                        val loggedInUser = commonDaoServices.loggedInUserDetails()
//                                        daoServices.updateCdDetailsInDB(consignmentDocumentDetailsEntity, loggedInUser).let {
//                                            //TODO: Send notification to the supervisor
//                                        }
//                                        consignmentAuditService.addHistoryRecord(cdInspectionGeneralEntity.id, form.remarks, "KEBS_COMPLIANCE", "Send Certificate of Inspection")
//                                        response.data = ConsignmentDocumentDao.fromEntity(consignmentDocumentDetailsEntity)
                                response.responseCode = ResponseCodes.SUCCESS_CODE
                                response.message = "Success"
                                response
                            } ?: run {
                                response.message = "No Consignment Document Found"
                                response.responseCode = ResponseCodes.NOT_FOUND
                                response
                            }

                        } ?: run {
                            response.message = "No General Inspection Checklist Found"
                            response.responseCode = ResponseCodes.NOT_FOUND
                            response
                        }
                    }
        }
        return ServerResponse.ok().body(response)
    }

    fun findInspectionMotorVehicleById(itemId: Long): CdInspectionMotorVehicleItemChecklistEntity? {
        return this.motorVehicleItemChecklistRepository.findByIdOrNull(itemId)
    }

    fun findInspectionGeneralById(inspectionId: Long): Any {
        TODO("Not yet implemented")
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

    fun inspectionChecklistReportDetails(cdItemUuid: String): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val cdItemDetails = daoServices.findCDWithUuid(cdItemUuid)
            inspectionGeneralRepo.findFirstByCdDetails(cdItemDetails)?.let { generalChecklist ->
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

    fun motorVehicleInspectionDetails(itemId: Long): ApiResponseModel {
        //Get the CD Item
        val response = ApiResponseModel()
        try {
            val cdItemDetails = daoServices.findCD(itemId)
            val dataMap = mutableMapOf<String, Any?>()
            dataMap["cd_details"] = ConsignmentDocumentDao.fromEntity(cdItemDetails)
            //Get inspection checklist details
            inspectionGeneralRepo.findFirstByCdDetails(cdItemDetails)?.let { inspectionGeneralEntity ->
                //Get the motor vehicle checklist
                dataMap["inspectionGeneralEntity"] = CdInspectionGeneralDao.fromEntity(inspectionGeneralEntity)

                motorVehicleChecklistRepository.findByInspectionGeneral(inspectionGeneralEntity)
                        ?.let {
                            dataMap["checklist"] = it
                            dataMap["checklist_items"] = CdInspectionMotorVehicleItemChecklistDao.fromList(this.motorVehicleItemChecklistRepository.findAllByInspection(it))
                        } ?: run {
                    dataMap["mvInspectionChecklist"] = null
                }

                //Check for flash attributes
                response.data = dataMap
                response.responseCode = ResponseCodes.SUCCESS_CODE
                response.message = "Success"
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error { ex }
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Request failed, please try again"
        }
        return response
    }

    fun saveSsfDetails(sampleSubmissionDetails: QaSampleSubmissionEntity, itemId: Long, map: ServiceMapsEntity, loggedInUser: UsersEntity): ApiResponseModel {
        val response = ApiResponseModel()
        val cdItemOptional = iCdItemsRepo.findById(itemId)
        if (cdItemOptional.isPresent) {
            var cdItem = cdItemOptional.get()
            //updating of Details in DB
            if (cdItem.sampledCollectedStatus == map.activeStatus) {
                val serviceRequestsEntity = daoServices.ssfSave(cdItem, sampleSubmissionDetails, loggedInUser, map).first
                with(cdItem) {
                    sampleBsNumberStatus = map.activeStatus
                }
                cdItem.ssfId = serviceRequestsEntity.id
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

    fun saveScfDetails(sampleCollectionForm: QaSampleCollectionEntity, itemId: Long, loggedInUser: UsersEntity): ApiResponseModel {
        val response = ApiResponseModel()
        var collectionEntity = qaISampleCollectRepository.findByPermitId(sampleCollectionForm.permitId!!)
        if (collectionEntity == null) {
            collectionEntity = sampleCollectionForm
            collectionEntity.modifiedOn = Timestamp.from(Instant.now())
            collectionEntity.nameOfOfficer = "${loggedInUser.firstName} ${loggedInUser.lastName}"
            collectionEntity.officerDesignation = "IO"
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
                //Update CD item SCF status
                cdItem.sampledCollectedStatus = map.activeStatus
                cdItem.sampleSubmissionStatus = map.initStatus

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
}
