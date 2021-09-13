package org.kebs.app.kotlin.apollo.api.service;

import mu.KotlinLogging
import okhttp3.internal.toLongOrDefault
import org.kebs.app.kotlin.apollo.api.payload.*
import org.kebs.app.kotlin.apollo.api.payload.request.CheckListItems
import org.kebs.app.kotlin.apollo.api.payload.request.ConsignmentUpdateRequest
import org.kebs.app.kotlin.apollo.api.payload.request.EngineeringChecklist
import org.kebs.app.kotlin.apollo.api.payload.request.SsfForm
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DestinationInspectionDaoServices
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.ServiceMapsEntity
import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import org.kebs.app.kotlin.apollo.store.model.di.*
import org.kebs.app.kotlin.apollo.store.model.qa.QaSampleSubmissionEntity
import org.kebs.app.kotlin.apollo.store.repo.di.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import java.sql.Date
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
        private val consignmentAuditService: ConsignmentDocumentAuditService,
        private val daoServices: DestinationInspectionDaoServices,
) {

    fun addEngineeringSsf(map: ServiceMapsEntity, cdItemID: Long, sampleSubmissionDetails: QaSampleSubmissionEntity, loggedInUser: UsersEntity): ApiResponseModel {
        val response = ApiResponseModel()
        val enginerringItem = engineeringItemChecklistRepository.findByIdOrNull(cdItemID)
        enginerringItem?.let {
            val cdItemOptional = iCdItemsRepo.findById(it.itemId!!)
            if (cdItemOptional.isPresent) {
                var cdItem = cdItemOptional.get()
                //updating of Details in DB
                val serviceRequestsEntity = daoServices.ssfSave(cdItem, sampleSubmissionDetails, loggedInUser, map).first
                with(cdItem) {
                    sampleBsNumberStatus = map.activeStatus
                }
                it.ssfId = serviceRequestsEntity.id
                engineeringItemChecklistRepository.save(it)
                cdItem = daoServices.updateCdItemDetailsInDB(cdItem, loggedInUser)
                response.data = CdItemDetailsDao.fromEntity(cdItem)
                response.responseCode = ResponseCodes.SUCCESS_CODE
                response.message = "You have Successful Filled Sample Submission Details"
            } else {
                response.responseCode = ResponseCodes.FAILED_CODE
                response.message = "Invalid item identifier configured"
            }
            response
        } ?: run {
            response.message = "Invalid checklist identifier"
            response.responseCode = ResponseCodes.NOT_FOUND
            response
        }
        return response
    }

    fun addAgrochemSsf(map: ServiceMapsEntity, cdItemID: Long, sampleSubmissionDetails: QaSampleSubmissionEntity, loggedInUser: UsersEntity): ApiResponseModel {
        val response = ApiResponseModel()
        val enginerringItem = agrochemItemChecklistRepository.findByIdOrNull(cdItemID)
        enginerringItem?.let {
            val cdItemOptional = iCdItemsRepo.findById(it.itemId!!)
            if (cdItemOptional.isPresent) {
                var cdItem = cdItemOptional.get()
                //updating of Details in DB
                val serviceRequestsEntity = daoServices.ssfSave(cdItem, sampleSubmissionDetails, loggedInUser, map).first
                with(cdItem) {
                    sampleBsNumberStatus = map.activeStatus
                }
                it.ssfId = serviceRequestsEntity.id
                agrochemItemChecklistRepository.save(it)
                cdItem = daoServices.updateCdItemDetailsInDB(cdItem, loggedInUser)
                response.data = CdItemDetailsDao.fromEntity(cdItem)
                response.responseCode = ResponseCodes.SUCCESS_CODE
                response.message = "You have Successful Filled Sample Submission Details"
            } else {
                response.responseCode = ResponseCodes.FAILED_CODE
                response.message = "Invalid item identifier configured"
            }
            response
        } ?: run {
            response.message = "Invalid checklist identifier"
            response.responseCode = ResponseCodes.NOT_FOUND
            response
        }
        return response
    }

    fun addEngineeringChecklist(map: ServiceMapsEntity, general: CdInspectionGeneralEntity, itemList: List<CheckListItems>?, engineeringChecklist: CdInspectionEngineeringChecklist, loggedInUser: UsersEntity) {
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
                    val checklistItem: CdInspectionEngineeringItemChecklistEntity = this.engineeringItemChecklistRepository.findByInspectionAndItemId(engineering, itm.itemId)
                            ?: CdInspectionEngineeringItemChecklistEntity()
                    checklistItem.sampled = itm.sampled
                    if ("YES".equals(itm.sampled)) {
                        checklistItem.sampleUpdated = 0
                    } else {
                        checklistItem.sampleUpdated = 2
                    }
                    daoServices.checkIfChecklistUndergoesSampling(
                            checklistItem.sampled!!,
                            detail,
                            map
                    )
                    checklistItem.itemId = itm.itemId
                    checklistItem.sizeClassCapacity = detail.productClassCode
                    checklistItem.description = detail.hsDescription
                    checklistItem.brand = detail.productBrandName
                    checklistItem.serialNumber = itm.serialNumber
                    checklistItem.compliant = itm.compliant
                    checklistItem.inspection = engineering
                    checklistItem.createdBy = loggedInUser.userName
                    checklistItem.createdOn = Timestamp.from(Instant.now())
                    this.engineeringItemChecklistRepository.save(checklistItem)
                }
            }
        }
    }

    fun addAgrochemChecklist(map: ServiceMapsEntity, general: CdInspectionGeneralEntity, itemList: List<CheckListItems>?, engineeringChecklist: CdInspectionAgrochemChecklist, loggedInUser: UsersEntity) {
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
                    val checklistItem: CdInspectionAgrochemItemChecklistEntity = this.agrochemItemChecklistRepository.findByInspectionAndItemId(agrochemChecklist, itm.itemId)
                            ?: CdInspectionAgrochemItemChecklistEntity()
                    checklistItem.sampled = itm.sampled
                    if ("YES".equals(itm.sampled)) {
                        checklistItem.sampleUpdated = 0
                    } else {
                        checklistItem.sampleUpdated = 2
                    }
                    daoServices.checkIfChecklistUndergoesSampling(
                            checklistItem.sampled!!,
                            detail,
                            map
                    )
                    checklistItem.itemId = itm.itemId
                    checklistItem.description = detail.hsDescription
                    checklistItem.brand = detail.productBrandName
                    checklistItem.serialNumber = itm.serialNumber
                    checklistItem.compliant = itm.compliant
                    checklistItem.inspection = agrochemChecklist
                    checklistItem.createdBy = loggedInUser.userName
                    checklistItem.createdOn = Timestamp.from(Instant.now())
                    this.agrochemItemChecklistRepository.save(checklistItem)
                }
            }
        }
    }

    fun addVehicleChecklist(map: ServiceMapsEntity, general: CdInspectionGeneralEntity, itemList: List<CheckListItems>?, engineeringChecklist: CdInspectionMotorVehicleChecklist, loggedInUser: UsersEntity) {
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
            vehicleChecklist.description = engineeringChecklist.description
        }
        this.motorVehicleChecklistRepository.save(vehicleChecklist)
        itemList?.let { items ->
            for (itm in items) {
                val itemDetails = iCdItemsRepo.findById(itm.itemId!!)
                if (itemDetails.isPresent) {
                    val detail = itemDetails.get()
                    val checklistItem: CdInspectionMotorVehicleItemChecklistEntity = this.motorVehicleItemChecklistRepository.findByInspectionAndItemId(vehicleChecklist, itm.itemId)
                            ?: CdInspectionMotorVehicleItemChecklistEntity()
                    checklistItem.sampled = itm.sampled
                    if ("YES".equals(itm.sampled)) {
                        checklistItem.sampleUpdated = 0
                    } else {
                        checklistItem.sampleUpdated = 2
                    }
                    daoServices.checkIfChecklistUndergoesSampling(
                            checklistItem.sampled!!,
                            detail,
                            map
                    )
                    checklistItem.itemId = itm.itemId
                    checklistItem.description = detail.hsDescription
                    checklistItem.serialNumber = itm.serialNumber
                    checklistItem.compliant = itm.compliant
                    checklistItem.inspection = vehicleChecklist
                    checklistItem.createdBy = loggedInUser.userName
                    checklistItem.createdOn = Timestamp.from(Instant.now())
                    this.motorVehicleItemChecklistRepository.save(checklistItem)
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

    fun addOtherChecklist(map: ServiceMapsEntity, general: CdInspectionGeneralEntity, itemList: List<CheckListItems>?, requestChecklist: CdInspectionOtherChecklist, loggedInUser: UsersEntity) {
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
                    val checklistItem: CdInspectionOtherItemChecklistEntity = this.otherItemChecklistRepository.findByInspectionAndItemId(otherChecklist, itm.itemId)
                            ?: CdInspectionOtherItemChecklistEntity()
                    checklistItem.sampled = itm.sampled
                    checklistItem.category = itm.category
                    if ("YES".equals(itm.sampled)) {
                        checklistItem.sampleUpdated = 0
                    } else {
                        checklistItem.sampleUpdated = 2
                    }
                    // Update sampling status
                    daoServices.checkIfChecklistUndergoesSampling(
                            checklistItem.sampled!!,
                            detail,
                            map
                    )
                    checklistItem.itemId = itm.itemId
                    checklistItem.description = detail.hsDescription
                    checklistItem.serialNumber = itm.serialNumber
                    checklistItem.compliant = itm.compliant
                    checklistItem.status = map.activeStatus
                    checklistItem.inspection = otherChecklist
                    checklistItem.createdBy = loggedInUser.userName
                    checklistItem.createdOn = Timestamp.from(Instant.now())
                    this.otherItemChecklistRepository.save(checklistItem)
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
                            this.consignmentAuditService.addHistoryRecord(cdetails.id,cdetails.ucrNumber ,comment, "MINISTRY", "Ministry Inspection Report submitted")
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

    fun inspectionChecklistReportDetails(cdItemUuid: String): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
            val cdItemDetails = daoServices.findCDWithUuid(cdItemUuid)
            inspectionGeneralRepo.findFirstByCdDetails(cdItemDetails)?.let { generalChecklist ->
                //Get checklist type details
                val dataMap = mutableMapOf<String, Any?>()
                // Agrochem checklist details
                agrochemChecklistRepository.findByInspectionGeneral(generalChecklist)?.let {
                    val agrochem=InspectionAgrochemDetailsDto.fromEntity(it)
                    agrochem.items=InspectionAgrochemItemDto.fromList(agrochemItemChecklistRepository.findByInspection(it))
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
}
