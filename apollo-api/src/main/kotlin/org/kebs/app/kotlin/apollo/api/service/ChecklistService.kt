package org.kebs.app.kotlin.apollo.api.service;

import mu.KotlinLogging
import okhttp3.internal.toLongOrDefault
import org.kebs.app.kotlin.apollo.api.notifications.Notifications
import org.kebs.app.kotlin.apollo.api.payload.*
import org.kebs.app.kotlin.apollo.api.payload.request.ConsignmentUpdateRequest
import org.kebs.app.kotlin.apollo.api.payload.request.SsfResultForm
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.DestinationInspectionBpmn
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DestinationInspectionDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.ReportsDaoService
import org.kebs.app.kotlin.apollo.common.dto.MinistryInspectionListResponseDto
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.ServiceMapsEntity
import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import org.kebs.app.kotlin.apollo.store.model.di.*
import org.kebs.app.kotlin.apollo.store.model.qa.QaSampleCollectionEntity
import org.kebs.app.kotlin.apollo.store.model.qa.QaSampleSubmissionEntity
import org.kebs.app.kotlin.apollo.store.repo.di.*
import org.kebs.app.kotlin.apollo.store.repo.qa.IQaSampleCollectionRepository
import org.springframework.core.io.ResourceLoader
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

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
        private val ministryStationRepo: IMinistryStationEntityRepository,
        private val ministryStationRepository: IMinistryStationEntityRepository,
        private val qaISampleCollectRepository: IQaSampleCollectionRepository,
        private val consignmentAuditService: ConsignmentDocumentAuditService,
        private val daoServices: DestinationInspectionDaoServices,
        private val resourceLoader: ResourceLoader
) {

    //Get KEBS Logo
    final val checkmarkImageResource = resourceLoader.getResource(applicationMapProperties.mapCheckmarkImagePath)
    val checkmarkImageFile = checkmarkImageResource.file.toString()

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
                    checklistItem.brand = detail.productBrandName
                    checklistItem.serialNumber = itm.serialNumber
                    checklistItem.compliant = itm.compliant
                    checklistItem.inspection = engineering

                    this.engineeringItemChecklistRepository.save(checklistItem)
                    // Update checklist update
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

    @Transactional
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
                    checklistItem.productDescription = detail.itemDescription
                    checklistItem.quantityDeclared = detail.quantity.toString()
                    checklistItem.itemId = itm.itemId
                    checklistItem.description = detail.hsDescription
                    checklistItem.brand = detail.productBrandName
                    checklistItem.serialNumber = itm.serialNumber
                    checklistItem.compliant = itm.compliant
                    checklistItem.inspection = agrochemChecklist
                    this.agrochemItemChecklistRepository.save(checklistItem)
                    // Mark item as sampled
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

    @Transactional
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
                    }
                    // Update details
                    checklistItem.sampled = itm.sampled
                    if ("YES".equals(itm.sampled)) {
                        checklistItem.sampleUpdated = map.initStatus
                        checklistItem.ministryReportSubmitStatus = map.activeStatus
                    } else {
                        checklistItem.sampleUpdated = map.inactiveStatus
                    }
                    checklistItem.itemId = detail
                    checklistItem.description = detail.hsDescription
                    checklistItem.serialNumber = itm.serialNumber
                    checklistItem.compliant = itm.compliant
                    checklistItem.inspection = vehicleChecklist
                    if (checklistItem.ministryReportSubmitStatus == map.activeStatus) {
                        val ministryStation = this.ministryStationRepository.findById(checklistItem.stationId)
                        if (ministryStation.isPresent) {
                            val saved = this.motorVehicleItemChecklistRepository.save(checklistItem)
                            checklistItem.ministryStationId = ministryStation.get()
                            //Submit ministry inspection
                            this.bpmn.startMinistryInspection(saved, detail)
                        } else {
                            // FIXME: Handle invalid selection here
                        }
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
                    checklistItem.itemId=detail
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
                    checklistItem.status = map.activeStatus
                    checklistItem.inspection = otherChecklist
                    this.otherItemChecklistRepository.save(checklistItem)
                    // Update sampling status
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

    fun ministryInspectionList(inspectionChecklistId: Long, comment: String?, docFile: MultipartFile): ApiResponseModel {
        val response = ApiResponseModel()
        val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        KotlinLogging.logger { }.info("mvInspectionChecklistId = $inspectionChecklistId")
        motorVehicleItemChecklistRepository.findByIdOrNull(inspectionChecklistId)
                ?.let { inspectionMotorVehicle ->
                    inspectionMotorVehicle.ministryReportFile = docFile.bytes
                    inspectionMotorVehicle.ministryReportReinspectionRemarks = comment
                    inspectionMotorVehicle.ministryReportSubmitStatus = 2
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

    fun motorVehicleInspectionDetails(inspectionId: Long): ApiResponseModel {
        //Get the CD Item
        val response = ApiResponseModel()
        try {
            this.motorVehicleItemChecklistRepository.findByIdOrNull(inspectionId)?.let { mvInpsection ->
                val dataMap = mutableMapOf<String, Any?>()
                mvInpsection.itemId?.let { cdItemDetails ->
                    cdItemDetails.cdDocId?.let { cdDetails ->
                        dataMap["cd_details"] = ConsignmentDocumentDao.fromEntity(cdDetails)
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

    fun sendEmailToImporter(mvInspectionId: Long, cdItemId: Long): Boolean {
        try {
            val data = hashMapOf<String, Any>()
            val itemDetails = this.daoServices.findItemWithItemID(cdItemId)
            var importerEmail: String? = null
            itemDetails.cdDocId?.let {
                it.cdImporter?.let { importerId ->
                    val cdImporterDetails = daoServices.findCDImporterDetails(importerId)
                    importerEmail = cdImporterDetails.email
                    data.put("emailAddress", cdImporterDetails.email.toString())
                    data.put("name", cdImporterDetails.name.toString())
                    data.put("importerAddress", cdImporterDetails.email.toString())
                    data.put("importerName", cdImporterDetails.name.toString())
                }
            }
            if (importerEmail == null) {
                KotlinLogging.logger { }.warn("${cdItemId} Importer with id ${itemDetails.cdDocId?.cdImporter} does not have a valid email address")
                return false
            }
            val optional = this.motorVehicleItemChecklistRepository.findById(mvInspectionId)
            if (optional.isPresent) {
                val mvInspection = optional.get()
                data.put("imagePath", "")
                data.put("mvirNum", mvInspection.serialNumber.toString())
                data.put("inspectionRegion", mvInspection.ministryStationId?.stationName.toString())
                data.put("genDate", SimpleDateFormat("dd/MM/yyyy HH:mm").format(mvInspection.createdOn))
                data.put("officerName", mvInspection.assignedUser?.firstName.toString())
                data.put("dutyStation", mvInspection.ministryStationId?.stationName.toString())
                val fileName = "/tmp/MVIR-${mvInspection.serialNumber}.pdf"
                val pdfStream = reportsDaoService.generateEmailPDFReportWithNoDataSource(fileName, data, "classpath:reports/motorVehicleInspectionReport.jrxml")
                data.put("mvir", mvInspection)
                val subject = "Motor Vehicle Inspection Request"
                val messageBody = "Please Find The attached motor vehicle details submitted for inspection  \n" +
                        "\n "
                // Deliver email
                notifications.sendEmail(importerEmail!!, subject, messageBody, fileName)
                return true
            }
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

    fun getSsfDetails(itemId: Long): HashMap<String, Any> {
        val map = hashMapOf<String, Any>()
        return this.daoServices.findSampleSubmittedItemID(itemId)?.let { inspectionGeneral ->
            map["CheckMark"] = checkmarkImageFile
            map["id"] = inspectionGeneral.id.toString()
            map["ssfNum"] = inspectionGeneral.ssfNo.toString()
            map["product"] = inspectionGeneral.productDescription.toString()
            map["genDate"] = inspectionGeneral.createdOn.toString()
            val itemDetails = this.daoServices.findItemWithItemID(inspectionGeneral.cdItemId!!)
            itemDetails.cdDocId?.let {
                map["cocNum"] = it.cocNumber.toString()
                map["dutyStation"] = it.freightStation?.cfsName.toString()
                map["officerName"] = "${it.assignedInspectionOfficer?.firstName} ${it.assignedInspectionOfficer?.lastName}"
                val importerDetailsEntity = it.cdImporter?.let { it1 -> this.daoServices.findCDImporterDetails(it1) }
                importerDetailsEntity?.let { importer ->
                    map["importerName"] = importer.name.toString()
                    map["importerAddress"] = importer.physicalAddress ?: importer.postalAddress.toString()
                }
                val consgnorDetails = it.cdConsignor?.let { it3 -> daoServices.findCdConsignorDetails(it3) }
                consgnorDetails?.let { consignor ->
                    map["supplierName"] = consignor.name.toString()
                    map["supplierAddress"] = consignor.physicalAddress ?: consignor.postalAddress.toString()
                }
            }
            map
        } ?: throw ExpectedDataNotFound("SSF details not found")
    }

    fun getScfDetails(itemId: Long): HashMap<String, Any> {
        val map = hashMapOf<String, Any>()
        return this.qaISampleCollectRepository.findByItemId(itemId)?.let { inspectionGeneral ->
            map["CheckMark"] = checkmarkImageFile
            map["id"] = inspectionGeneral.id.toString()
            map["slNum"] = inspectionGeneral.batchNo.toString()
            map["product"] = inspectionGeneral.nameOfProduct.toString()
            map["genDate"] = inspectionGeneral.createdOn.toString()
            val itemDetails = this.daoServices.findItemWithItemID(inspectionGeneral.itemId!!)
            itemDetails.cdDocId?.let {
                map["cocNum"] = it.cocNumber.toString()
                map["dutyStation"] = it.freightStation?.cfsName.toString()
                map["officerName"] = "${it.assignedInspectionOfficer?.firstName} ${it.assignedInspectionOfficer?.lastName}"
                val importerDetailsEntity = it.cdImporter?.let { it1 -> this.daoServices.findCDImporterDetails(it1) }
                importerDetailsEntity?.let { importer ->
                    map["importerName"] = importer.name.toString()
                    map["importerAddress"] = importer.physicalAddress ?: importer.postalAddress.toString()
                }
                val consgnorDetails = it.cdConsignor?.let { it3 -> daoServices.findCdConsignorDetails(it3) }
                consgnorDetails?.let { consignor ->
                    map["supplierName"] = consignor.name.toString()
                    map["supplierAddress"] = consignor.physicalAddress ?: consignor.postalAddress.toString()
                }
            }
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
                item.sampleBsNumberStatus = map.activeStatus
                iCdItemsRepo.save(item)
                sample.bsNumber = form.bsNumber
                sample.ssfNo = form.ssfNo
                sample.permitId = null
                sample.ssfSubmissionDate = Date(java.util.Date().time)
                form.submissionDate?.let {
                    try {
                        sample.ssfSubmissionDate = Date.valueOf(it)
                    } catch (ex: Exception) {
                        sample.ssfSubmissionDate = Date(java.util.Date().time)
                    }
                }
                sample.complianceRemarks = form.remarks
                daoServices.ssfUpdateDetails(item, sample, loggedInUser, map)
                response.message = "Sample BS number and SSF NO updated"
                response.responseCode = ResponseCodes.SUCCESS_CODE
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
                ministryUsers.get(Random().nextInt(ministryUsers.size)).let {
                    ministryInspection.assignedUser = it
                    data.put("ministryUser", it.userName)
                    data.put("userId", it.id)
                    this.motorVehicleItemChecklistRepository.save(ministryInspection)
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
            motorVehicleInspection.itemId?.let { cdItemDetails ->
                cdItemDetails.ministrySubmissionStatus = map.activeStatus
                val ministryStation = this.ministryStationRepo.findById(stationId)
                if (ministryStation.isPresent) {
                    motorVehicleInspection.ministryStationId = ministryStation.get()
                    motorVehicleInspection.ministryReportSubmitStatus = map.activeStatus
                    this.bpmn.startMinistryInspection(motorVehicleInspection, cdItemDetails)
                    // Update
                    daoServices.updateCDItemDetails(cdItemDetails, cdItemDetails.id!!, loggedInUser, map)
                    this.motorVehicleItemChecklistRepository.save(motorVehicleInspection)
                    response.data = InspectionMotorVehicleItemDto.fromEntity(motorVehicleInspection)
                    response.message = "Data Submitted Successfully"
                    response.responseCode = ResponseCodes.SUCCESS_CODE
                } else {
                    response.message = "Invalid ministry station request"
                    response.responseCode = ResponseCodes.NOT_FOUND
                }
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
            map["CheckMark"] = checkmarkImageFile
            when (inspectionGeneral?.inspection) {
                "100% Inspection" -> map["HundredPercentInspectionCheckmark"] = checkmarkImageFile
                "Partial Inspection" -> map["PartialInspectionCheckMark"] = checkmarkImageFile
            }
            map["EntryPoint"] = inspectionGeneral?.entryPoint.toString()
            map["Cfs"] = inspectionGeneral?.cfs.toString()
            map["Date"] = inspectionGeneral?.inspectionDate.toString()
            map["ImportersName"] = inspectionGeneral?.importersName.toString()
            map["ClearingAgent"] = inspectionGeneral?.clearingAgent.toString()
            map["CustomsEntryNo"] = inspectionGeneral?.customsEntryNumber.toString()
            inspectionGeneral?.cdDetails?.let {
                map["IdfNo"] = it.idfNumber.toString()
                map["CocNo"] = it.cocNumber.toString()
                map["ReceiptNo"] = "NA"
            }
            map["UcrNo"] = inspectionGeneral?.ucrNumber.toString()
            map["SerialNo"] = mvInspectionChecklist.serialNumber.toString()
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
            map["Remarks"] = mvInspectionChecklist.remarks.toString()
            map["OverallRemarks"] = inspectionGeneral?.overallRemarks.toString()

        } ?: throw ExpectedDataNotFound("Motor Vehicle Inspection Checklist does not exist")
        return map
    }

    fun listMinistryInspection(statuses: List<Int>, page: PageRequest): ApiResponseModel {
        val requests: Page<CdInspectionMotorVehicleItemChecklistEntity>
        val ministryInspectionItems: MutableList<MinistryInspectionListResponseDto> = ArrayList()
        val response = ApiResponseModel()
        requests = motorVehicleItemChecklistRepository.findByMinistryReportSubmitStatusIn(statuses, page)
        // Get inspection items
        requests.toList().forEach {
            it.itemId?.let { itemDetails ->
                val d = this.daoServices.convertCdItemDetailsToMinistryInspectionListResponseDto(itemDetails)
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
        map["imagePath"] = checkmarkImageFile
        when (inspectionGeneral?.inspection) {
            "FULL" -> map["HundredPercentInspectionCheckmark"] = checkmarkImageFile
            else -> map["PartialInspectionCheckMark"] = checkmarkImageFile
        }
        map["inspection"] = inspectionGeneral?.inspection.toString()
        map["cfs"] = inspectionGeneral?.cfs.toString()
        map["inspectionDate"] = inspectionGeneral?.inspectionDate.toString()
        map["importersName"] = inspectionGeneral?.importersName.toString()
        map["clearingAgent"] = inspectionGeneral?.clearingAgent.toString()
        map["customsEntryNumber"] = inspectionGeneral?.customsEntryNumber.toString()

        inspectionGeneral?.cdDetails?.let {
            map["idfNumber"] = it.idfNumber ?: ""
            map["cocNumber"] = it.cocNumber ?: ""
            map["ucrNumber"] = it.ucrNumber ?: ""
            map["receiptNumber"] = "NA"
            map["feePaid"] = "NO"
            daoServices.findDemandNoteWithPaymentStatus(it.id!!, 1)?.let {
                map["receiptNumber"] = it.demandNoteNumber.toString()
                map["feePaid"] = "YES"
            }
            map["entryPoint"] = it.freightStation?.cfsName ?: "NA"
            map["cfs"] = it.freightStation?.cfsCode ?: "NA"
            map["cdId"] = it.id ?: 0L
            it.cdImporter?.let { importterId ->
                this.daoServices.findCDImporterDetails(importterId)?.let { importer ->
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
            map["imagePath"] = checkmarkImageFile
            when (inspectionGeneral?.inspection) {
                "FULL" -> map["HundredPercentInspectionCheckmark"] = checkmarkImageFile
                else -> map["PartialInspectionCheckMark"] = checkmarkImageFile
            }
            map["inspection"] = inspectionGeneral?.inspection.toString()
            map["documentCategory"] = mvInspectionChecklist.inspectionChecklistType?.description
                    ?: "Agrochemical checklist"
            map["cfs"] = inspectionGeneral?.cfs.toString()
            map["inspectionDate"] = inspectionGeneral?.inspectionDate.toString()
            map["importersName"] = inspectionGeneral?.importersName.toString()
            map["clearingAgent"] = inspectionGeneral?.clearingAgent.toString()
            map["customsEntryNumber"] = inspectionGeneral?.customsEntryNumber.toString()

            inspectionGeneral?.cdDetails?.let {
                map["idfNumber"] = it.idfNumber ?: ""
                map["cocNumber"] = it.cocNumber ?: ""
                map["ucrNumber"] = it.ucrNumber ?: ""
                map["receiptNumber"] = "NA"
                map["feePaid"] = "NO"
                daoServices.findDemandNoteWithPaymentStatus(it.id!!, 1)?.let {
                    map["receiptNumber"] = it.demandNoteNumber.toString()
                    map["feePaid"] = "YES"
                }
                map["entryPoint"] = it.freightStation?.cfsName ?: "NA"
                map["cfs"] = it.freightStation?.cfsCode ?: "NA"
                map["cdId"] = it.id ?: 0L
                it.cdImporter?.let { importterId ->
                    this.daoServices.findCDImporterDetails(importterId)?.let { importer ->
                        map["importersName"] = importer.name ?: inspectionGeneral.importersName.toString()
                    }
                }
            }
            map["id"] = mvInspectionChecklist.id ?: 0L
            map["serialNumber"] = mvInspectionChecklist.serialNumber.toString()
            map["items"] = InspectionAgrochemItemDto.fromList(agrochemItemChecklistRepository.findByInspection(mvInspectionChecklist))
            map["remarks"] = mvInspectionChecklist.remarks.toString()
            map["overallRemarks"] = mvInspectionChecklist.remarks.toString()
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
            map["documentCategory"] = mvInspectionChecklist.inspectionChecklistType?.description?:"Engineering checklist"
            map["id"] = mvInspectionChecklist.id ?: 0L
            map["serialNumber"] = mvInspectionChecklist.serialNumber.toString()
            map["items"] = InspectionEngineeringItemDto.fromList(engineeringItemChecklistRepository.findByInspection(mvInspectionChecklist))
            map["remarks"] = mvInspectionChecklist.remarks.toString()
            map["overallRemarks"] = mvInspectionChecklist.remarks.toString()
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
            map["documentCategory"] = mvInspectionChecklist.inspectionChecklistType?.description?:"Other Checklist"
            map["id"] = mvInspectionChecklist.id ?: 0L
            map["serialNumber"] = mvInspectionChecklist.serialNumber.toString()
            map["items"] = InspectionOtherItemDto.fromList(otherItemChecklistRepository.findByInspection(mvInspectionChecklist))
            map["remarks"] = mvInspectionChecklist.remarks.toString()
            map["overallRemarks"] = mvInspectionChecklist.remarks.toString()
            map
        } ?: throw ExpectedDataNotFound("Other Inspection Checklist does not exist")
        return map
    }

    fun getVehicleChecklistDetails(inspectionId: Long): Map<out String, Any>{
        KotlinLogging.logger { }.info("Vehicle checklist details")
        val map = hashMapOf<String, Any>()
        motorVehicleChecklistRepository.findByIdOrNull(inspectionId)?.let { mvInspectionChecklist ->
            KotlinLogging.logger { }.info("Motor Vehicle checklist details Found")
            map.putAll(loadGeneralInspection(mvInspectionChecklist.inspectionGeneral))
            map["documentCategory"] = mvInspectionChecklist.inspectionChecklistType?.description?:"Other Checklist"
            map["id"] = mvInspectionChecklist.id ?: 0L
            map["serialNumber"] = mvInspectionChecklist.serialNumber.toString()
            map["items"] = InspectionMotorVehicleItemDto.fromList(motorVehicleItemChecklistRepository.findByInspection(mvInspectionChecklist))
            map["remarks"] = mvInspectionChecklist.remarks.toString()
            map["overallRemarks"] = mvInspectionChecklist.remarks.toString()
            map
        } ?: throw ExpectedDataNotFound("Other Inspection Checklist does not exist")
        return map
    }


    fun getAllChecklistDetails(cdUuid: String): Map<out String, Any> {
        val document = this.daoServices.findCDWithUuid(cdUuid)
        KotlinLogging.logger { }.info("All checklist details")
        val map = hashMapOf<String, Any>()
        inspectionGeneralRepo.findFirstByCdDetails(document)?.let { inspectionGeneral ->
            KotlinLogging.logger { }.info("All checklist details Found")
            map.putAll(loadGeneralInspection(inspectionGeneral))
            map["documentCategory"] = "All Checklists"
            // AgroChecmical
            agrochemChecklistRepository.findByInspectionGeneral(inspectionGeneral)?.let {
                map["agrochemicalItems"] = InspectionAgrochemItemDto.fromList(agrochemItemChecklistRepository.findByInspection(it))
            }
            //Engineering
            engineeringChecklistRepository.findByInspectionGeneral(inspectionGeneral)?.let {
                map["engineeringItems"] = InspectionEngineeringItemDto.fromList(engineeringItemChecklistRepository.findByInspection(it))
            }
            // Vehicle
            motorVehicleChecklistRepository.findByInspectionGeneral(inspectionGeneral)?.let {
                map["motorVehicleItems"] = InspectionMotorVehicleItemDto.fromList(motorVehicleItemChecklistRepository.findByInspection(it))
            }
            // Other
            otherChecklistRepository.findByInspectionGeneral(inspectionGeneral)?.let {
                map["otherItems"] = InspectionOtherItemDto.fromList(otherItemChecklistRepository.findByInspection(it))
            }
            map
        } ?: throw ExpectedDataNotFound("Other Inspection Checklist does not exist")
        return map
    }
}
