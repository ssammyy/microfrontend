package org.kebs.app.kotlin.apollo.api.handlers

import mu.KotlinLogging
import okhttp3.internal.toLongOrDefault
import org.kebs.app.kotlin.apollo.api.payload.ApiResponseModel
import org.kebs.app.kotlin.apollo.api.payload.ResponseCodes
import org.kebs.app.kotlin.apollo.api.payload.extractPage
import org.kebs.app.kotlin.apollo.api.payload.request.*
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DestinationInspectionDaoServices
import org.kebs.app.kotlin.apollo.api.service.ChecklistService
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.repo.di.IChecklistCategoryRepository
import org.kebs.app.kotlin.apollo.store.repo.di.IChecklistInspectionTypesRepository
import org.kebs.app.kotlin.apollo.store.repo.di.ILaboratoryRepository
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartHttpServletRequest
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import java.sql.Date
import java.sql.Timestamp
import java.time.Instant

@Component
class ChecklistHandler(
        private val daoServices: DestinationInspectionDaoServices,
        private val checlistService: ChecklistService,
        private val iChecklistCategoryRepo: IChecklistCategoryRepository,
        private val iChecklistInspectionTypesRepo: IChecklistInspectionTypesRepository,
        private val iLaboratoryRepo: ILaboratoryRepository,
        private val commonDaoServices: CommonDaoServices,
        private val applicationMapProperties: ApplicationMapProperties
) {
    fun ssfPdfFilesResults(req: ServerRequest): ServerResponse {
        val ssfId = req.pathVariable("ssfId")
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        return ServerResponse.ok().body(checlistService.listLabPdfFiles(ssfId.toLongOrDefault(0L),loggedInUser))
    }
    fun approveRejectSampledItem(req: ServerRequest): ServerResponse {
        var response = ApiResponseModel()
        try {
            req.pathVariable("cdUuid").let { cdUuid ->
                val form = req.body(ConsignmentUpdateRequest::class.java)
                val document = daoServices.findCDWithUuid(cdUuid)
                if (document.approveRejectCdStatusType == null || document.approveRejectCdStatusType?.modificationAllowed == 1) {
                    val itemId = req.pathVariable("cdItemId").toLongOrDefault(0L)
                    form.compliant?.let {
                        response = checlistService.updateItemComplianceStatus(document, itemId, form.remarks.orEmpty(), it)
                        response
                    } ?: run {
                        response.message = "Compliance status is required: ${document.approveRejectCdStatusType?.typeName}"
                        response.responseCode = ResponseCodes.FAILED_CODE
                        response
                    }
                } else {
                    response.message = "Document modification is not allowed"
                    response.responseCode = ResponseCodes.FAILED_CODE
                }
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("PROCESS ERROR", ex)
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = ex.localizedMessage
        }
        return ServerResponse.ok().body(response)
    }

    fun loadLabResult(req: ServerRequest): ServerResponse {
        req.pathVariable("cdItemID").let {
            return ServerResponse.ok().body(checlistService.getItemSampleAndLabResults(it))
        }
    }

    fun uploadMinistryCheckList(req: ServerRequest): ServerResponse {
        var response = ApiResponseModel()
        val multipartRequest = (req.servletRequest() as? MultipartHttpServletRequest)
        if (multipartRequest != null) {
            multipartRequest.getFile("file")?.let { multipartFile ->
                val comment = req.attribute("comment")
                        .orElse("")
                val itemId = req.pathVariable("inspectionId")
                response = checlistService.ministryInspectionList(itemId.toLongOrDefault(0L), comment.toString(), multipartFile)
                response
            } ?: run {
                response.responseCode = ResponseCodes.FAILED_CODE
                response.message = "Please select file to upload"
                response
            }
        } else {
            response.responseCode = ResponseCodes.INVALID_CODE
            response.message = "Request is not a multipart request"
        }
        return ServerResponse.ok().body(response)
    }

    fun motorVehicleInspection(req: ServerRequest): ServerResponse {
        var response = ApiResponseModel()
        req.pathVariable("inspectionId").let { inspectionId ->
            val imId = inspectionId.toLongOrDefault(0)
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

    fun consignmentDocumentChecklistSampled(req: ServerRequest): ServerResponse {
        req.pathVariable("cdUuid").let {
            return ServerResponse.ok().body(this.checlistService.consignmentChecklistSampled(it))
        }
    }

    fun ministryInspectionRequest(req: ServerRequest): ServerResponse {
        req.pathVariable("inspectionId").let {
            val form = req.body(MinistryRequestForm::class.java)
            return ServerResponse.ok()
                    .body(this.checlistService.requestMinistryInspection(it.toLongOrDefault(0L), form.stationId))
        }
    }

    fun ministryInspections(req: ServerRequest): ServerResponse {
        req.pathVariable("inspectionStatus").let { taskId ->
            val status = taskId.toInt()
            val page = extractPage(req)
            return ServerResponse.ok()
                    .body(this.checlistService.listMinistryInspection(listOf(status), page))
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

    fun addChecklistScfDetails(req: ServerRequest): ServerResponse {
        return ServerResponse.ok().body("OK")
    }

    fun addScfDetails(req: ServerRequest): ServerResponse {
        var response = ApiResponseModel()
        try {
            req.pathVariable("cdItemID").let {
                val item = daoServices.findItemWithUuid(it)
                val form = req.body(ScfForm::class.java)
                val loggedInUser = commonDaoServices.loggedInUserDetails()
                val sampleCollectionForm = form.scf()
                sampleCollectionForm.createdBy = loggedInUser.createdBy
                sampleCollectionForm.modifiedBy = loggedInUser.userName
                sampleCollectionForm.createdOn = Timestamp.from(Instant.now())
                response = this.checlistService.saveScfDetails(sampleCollectionForm, item.id!!, loggedInUser)
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("FAILED TO CREATED SCF", ex)
            response.responseCode = ResponseCodes.FAILED_CODE
            response.message = "SCF submission failed"
        }
        return ServerResponse.ok().body(response)
    }

    fun updateScfDetails(req: ServerRequest): ServerResponse {
        var response = ApiResponseModel()
        try {
            val form = req.body(ScfForm::class.java)
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val sampleCollectionForm = form.scf()
            response = this.checlistService.saveScfDetails(sampleCollectionForm, form.itemId, loggedInUser)
        } catch (ex: Exception) {
            response.responseCode = ResponseCodes.FAILED_CODE
            response.message = "SCF submission failed"
        }
        return ServerResponse.ok().body(response)
    }

    fun completeSsfDetails(req: ServerRequest): ServerResponse {
        val form = req.body(SsfForm::class.java)
        // Update sample
        return ServerResponse.ok().body("OK")
    }

    fun addChecklistSsfDetails(req: ServerRequest): ServerResponse {
        var response = ApiResponseModel()
        val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
        try {
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val category = req.pathVariable("category")
            req.pathVariable("cdItemID").let { cdItemID ->
                val form = req.body(SsfForm::class.java)
                val ssfDetails = form.ssf()
                ssfDetails.category = category
                when (category) {
                    "engineering" -> {
                        response = this.checlistService.addEngineeringSsf(map, cdItemID.toLongOrDefault(0L), ssfDetails, loggedInUser)
                    }
                    "agrochem" -> {
                        response = this.checlistService.addAgrochemSsf(map, cdItemID.toLongOrDefault(0L), ssfDetails, loggedInUser)
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

    fun addSsfDetails(req: ServerRequest): ServerResponse {
        var response = ApiResponseModel()
        val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
        try {
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            req.pathVariable("cdItemID").let { cdItemID ->
                val item = daoServices.findItemWithUuid(cdItemID)
                val form = req.body(SsfForm::class.java)
                val ssfDetails = form.ssf()
                response = this.checlistService.saveSsfDetails(ssfDetails, item.id!!, map, loggedInUser)
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to submit SSF", ex)
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Failed to save SSF details"
        }
        return ServerResponse.ok().body(response)
    }


    fun updateSsfResults(req: ServerRequest): ServerResponse {
        var response = ApiResponseModel()
        val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
        try {
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            req.pathVariable("cdItemID").let { cdItemID ->
                val item = daoServices.findItemWithUuid(cdItemID)
                val form = req.body(SsfResultForm::class.java)
                response = this.checlistService.updateSsfResult(form, item, map, loggedInUser)
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to update BS number", ex)
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "BS number already in use, please enter another BS number"
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
                    generalCheckList.cfs = cdItem.freightStation?.cfsName
                    val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
                    val inspectionGeneral = daoServices.saveInspectionGeneralDetails(generalCheckList, cdItem, loggedInUser, map)
                    if (form.agrochem == null && form.engineering == null && form.vehicle == null && form.others == null) {
                        response.responseCode = ResponseCodes.FAILED_CODE
                        response.message = "Validation failed, please select and fill at least one checklist"
                        return ServerResponse.ok().body(response)
                    }
                    //Save the respective checklist
                    form.agrochem?.let {
                        val agrochemItemInspectionChecklist = form.agrochemChecklist()
                        checlistService.addAgrochemChecklist(map, inspectionGeneral, form.agrochemChecklistItems(), agrochemItemInspectionChecklist, loggedInUser)
                    }
                    // Add engineering checklist
                    form.engineering?.let {
                        val engineeringItemInspectionChecklist = form.engineeringChecklist()
                        checlistService.addEngineeringChecklist(map, inspectionGeneral, form.engineeringChecklistItems(), engineeringItemInspectionChecklist, loggedInUser)

                    }
                    // Add vehicle checklist
                    form.vehicle?.let {
                        val motorVehicleItemInspectionChecklist = form.vehicleChecklist()
                        checlistService.addVehicleChecklist(map, inspectionGeneral, form.vehicleChecklistItems(), motorVehicleItemInspectionChecklist, loggedInUser)
                    }
                    // Add other checklists
                    form.others?.let {
                        val otherItemInspectionChecklist = form.otherChecklist()
                        checlistService.addOtherChecklist(map, inspectionGeneral, form.otherChecklistItems(), otherItemInspectionChecklist, loggedInUser)
                    }
                    cdItem.inspectionChecklist = map.activeStatus
                    cdItem.varField10 = "CHECKLIST FILLED, AWAITING COMPLIANCE STATUS"
                    daoServices.updateCdDetailsInDB(cdItem, loggedInUser)
                }
                response.message = "Checklist submitted successfully"
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