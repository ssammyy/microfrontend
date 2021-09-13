package org.kebs.app.kotlin.apollo.api.handlers

import mu.KotlinLogging
import okhttp3.internal.toLongOrDefault
import org.kebs.app.kotlin.apollo.api.payload.ApiResponseModel
import org.kebs.app.kotlin.apollo.api.payload.CdItemDetailsDao
import org.kebs.app.kotlin.apollo.api.payload.ResponseCodes
import org.kebs.app.kotlin.apollo.api.payload.extractPage
import org.kebs.app.kotlin.apollo.api.payload.request.CheckListForm
import org.kebs.app.kotlin.apollo.api.payload.request.MinistryRequestForm
import org.kebs.app.kotlin.apollo.api.payload.request.SsfForm
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DestinationInspectionDaoServices
import org.kebs.app.kotlin.apollo.api.service.ChecklistService
import org.kebs.app.kotlin.apollo.api.service.DestinationInspectionService
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.repo.di.IChecklistCategoryRepository
import org.kebs.app.kotlin.apollo.store.repo.di.IChecklistInspectionTypesRepository
import org.kebs.app.kotlin.apollo.store.repo.di.ILaboratoryRepository
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartHttpServletRequest
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import java.sql.Date

@Component
class ChecklistHandler(
        private val daoServices: DestinationInspectionDaoServices,
        private val diService: DestinationInspectionService,
        private val checlistService: ChecklistService,
        private val iChecklistCategoryRepo: IChecklistCategoryRepository,
        private val iChecklistInspectionTypesRepo: IChecklistInspectionTypesRepository,
        private val iLaboratoryRepo: ILaboratoryRepository,
        private val commonDaoServices: CommonDaoServices,
        private val applicationMapProperties: ApplicationMapProperties
) {
    fun downloadMinistryCheckList(req: ServerRequest): ServerResponse {
        val itemId = req.pathVariable("itemId")
                .toLongOrDefault(0L)
        checlistService.findInspectionMotorVehicleById(itemId)?.let { cdInspectionMotorVehicleItemChecklistEntity ->
            cdInspectionMotorVehicleItemChecklistEntity.ministryReportFile?.let {
                val resource = ByteArrayResource(it)
                return ServerResponse.ok()
                        .header("Content-Disposition", "inline; filename=${cdInspectionMotorVehicleItemChecklistEntity.chassisNo}_inspection_report;")
                        .contentType(MediaType.APPLICATION_PDF)
                        .body(resource)
            } ?: throw ExpectedDataNotFound("Inspection Report file not found")
        }
                ?: throw ExpectedDataNotFound("Motor Vehicle Inspection checklist with ID: $itemId not found")
    }

    fun uploadMinistryCheckList(req: ServerRequest): ServerResponse {
        var response = ApiResponseModel()
        val multipartRequest = (req.servletRequest() as? MultipartHttpServletRequest)
        if (multipartRequest != null) {
            val multipartFile = multipartRequest.getFile("file")
            val comment = req.attribute("comment")
                    .orElse("")
            val itemId = req.pathVariable("itemId")
            response = checlistService.ministryInspectionList(itemId.toLongOrDefault(0L), comment.toString(), multipartFile!!)

        } else {
            response.responseCode = ResponseCodes.INVALID_CODE
            response.message = "Request is not a multipart request"
        }
        return ServerResponse.ok().body(response)
    }

    fun motorVehicleInspection(req: ServerRequest): ServerResponse {
        var response = ApiResponseModel()
        req.pathVariable("itemId").let { itemId ->
            val imId = itemId.toLongOrDefault(0)
            if (imId > 0) {
                response = this.checlistService.motorVehicleInspectionDetails(imId)
            } else {
                response.message = "Invalid item identifier"
                response.responseCode = ResponseCodes.FAILED_CODE
            }
        }
        return ServerResponse.ok().body(response)
    }

    fun consignmentDocumentChecklist(req: ServerRequest): ServerResponse {
        req.pathVariable("cdUuid").let {
            return ServerResponse.ok().body(this.checlistService.inspectionChecklistReportDetails(it))
        }
    }

    fun ministryInspectionRequest(req: ServerRequest): ServerResponse {
        req.pathVariable("itemId").let {
            val form = req.body(MinistryRequestForm::class.java)
            return ServerResponse.ok()
                    .body(this.diService.requestMinistryInspection(it, form.stationId))
        }
    }

    fun ministryInspections(req: ServerRequest): ServerResponse {
        req.pathVariable("inspectionStatus").let { taskId ->
            val status = taskId.toLongOrDefault(0)
            val page = extractPage(req)
            return ServerResponse.ok()
                    .body(this.diService.listMinistryInspection(status >= 1, page))
        }
    }

    fun listAllChecklists(req: ServerRequest): ServerResponse {
        req.pathVariable("itemUuid").let {
            return ServerResponse.ok()
                    .body(this.checlistService.lisAllChecklists(it))
        }
    }

    fun checklistConfigurations(req: ServerRequest): ServerResponse {
        val response = ApiResponseModel()
        response.data = mapOf(
                Pair("categories", iChecklistCategoryRepo.findByStatus(commonDaoServices.activeStatus.toInt())),
                Pair("checkListTypes", iChecklistInspectionTypesRepo.findByStatus(commonDaoServices.activeStatus.toInt())),
                Pair("laboratories", iLaboratoryRepo.findByStatus(commonDaoServices.activeStatus.toInt()))
        )
        response.message = "Success"
        response.responseCode = ResponseCodes.SUCCESS_CODE
        return ServerResponse.ok().body(response)
    }

    fun addScfDetails(req: ServerRequest): ServerResponse{
        return ServerResponse.ok().body("TEST")
    }

    fun addSsfDetails(req: ServerRequest): ServerResponse {
        var response = ApiResponseModel()
        val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
        try {
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val category = req.pathVariable("category")
            req.pathVariable("cdItemID").let { cdItemID ->
                val form = req.body(SsfForm::class.java)
                val ssfDetails=form.ssf()
                ssfDetails.category=category
                when (category) {
                    "engineering" -> {
                        response = this.checlistService.addEngineeringSsf(map,cdItemID.toLongOrDefault(0L), ssfDetails, loggedInUser)
                    }
                    "agrochem" -> {
                        response = this.checlistService.addAgrochemSsf(map,cdItemID.toLongOrDefault(0L), ssfDetails, loggedInUser)
                    }
                    else -> {
                        response.message = "invalid category :" + category
                        response.responseCode = ResponseCodes.FAILED_CODE
                    }
                }
            }
        } catch (ex: Exception) {
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Failed to save SSF details"
        }
        return ServerResponse.ok().body(response)
    }

    fun saveChecklist(req: ServerRequest): ServerResponse {
        val response = ApiResponseModel()
        try {
            val form = req.body(CheckListForm::class.java)

            //Get CD item
            req.pathVariable("cdUuid").let { cdUuid ->
                commonDaoServices.getLoggedInUser()?.let { loggedInUser ->
                    val cdItem = daoServices.findCDWithUuid(cdUuid)
                    //Save the general checklist
                    val generalCheckList = form.generalChecklist()
                    generalCheckList.description = cdItem.description
                    generalCheckList.inspectionDate = Date(java.util.Date().time)
                    generalCheckList.cfs = cdItem.freightStation?.cfsCode
                    generalCheckList.cocNumber = cdItem.cocNumber
                    generalCheckList.idfNumber = cdItem.idfNumber
                    val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
                    val inspectionGeneral = daoServices.saveInspectionGeneralDetails(generalCheckList, cdItem, loggedInUser, map)
                    form.vehicle?.let {

                    }
                    //Save the respective checklist
                    form.agrochem?.let {
                        val agrochemItemInspectionChecklist = form.agrochemChecklist()
                        checlistService.addAgrochemChecklist(map, inspectionGeneral, it.items, agrochemItemInspectionChecklist, loggedInUser)
                    }
                    // Add engineering checklist
                    form.engineering?.let {
                        val engineeringItemInspectionChecklist = form.engineeringChecklist()
                        checlistService.addEngineeringChecklist(map, inspectionGeneral, it.items, engineeringItemInspectionChecklist, loggedInUser)

                    }
                    // Add vehicle checklist
                    form.vehicle?.let {
                        val motorVehicleItemInspectionChecklist = form.vehicleChecklist()
                        checlistService.addVehicleChecklist(map, inspectionGeneral, it.items, motorVehicleItemInspectionChecklist, loggedInUser)
                    }
                    // Add other checklists
                    form.others?.let {
                        val otherItemInspectionChecklist = form.otherChecklist()
                        checlistService.addOtherChecklist(map, inspectionGeneral, it.items, otherItemInspectionChecklist, loggedInUser)
                    }
                }
                response.message = "Success"
                response.responseCode = ResponseCodes.SUCCESS_CODE
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("SAVE CHECKLIST ERR", ex)
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Invalid checklist request"
        }
        return ServerResponse.ok().body(response)
    }
}