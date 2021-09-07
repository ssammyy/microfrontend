package org.kebs.app.kotlin.apollo.api.handlers

import mu.KotlinLogging
import okhttp3.internal.toLongOrDefault
import org.kebs.app.kotlin.apollo.api.payload.ApiResponseModel
import org.kebs.app.kotlin.apollo.api.payload.ResponseCodes
import org.kebs.app.kotlin.apollo.api.payload.request.CheckListForm
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DestinationInspectionDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.ReportsDaoService
import org.kebs.app.kotlin.apollo.api.service.DestinationInspectionService
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.di.CdItemDetailsEntity
import org.kebs.app.kotlin.apollo.store.repo.di.IChecklistCategoryRepository
import org.kebs.app.kotlin.apollo.store.repo.di.IChecklistInspectionTypesRepository
import org.kebs.app.kotlin.apollo.store.repo.di.ILaboratoryRepository
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import java.sql.Date

@Component
class ChecklistHandler(
        private val daoServices: DestinationInspectionDaoServices,
        private val diService: DestinationInspectionService,
        private val reportsDaoService: ReportsDaoService,
        private val iChecklistCategoryRepo: IChecklistCategoryRepository,
        private val iChecklistInspectionTypesRepo: IChecklistInspectionTypesRepository,
        private val iLaboratoryRepo: ILaboratoryRepository,
        private val commonDaoServices: CommonDaoServices,
        private val applicationMapProperties: ApplicationMapProperties
) {



    fun listAllChecklists(req: ServerRequest): ServerResponse {
        req.pathVariable("itemUuid").let {
            return ServerResponse.ok()
                    .body(this.diService.lisAllChecklists(it))
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

    fun saveChecklist(req: ServerRequest): ServerResponse {
        val response = ApiResponseModel()
        try {
            val form = req.body(CheckListForm::class.java)

            //Get CD item
            req.pathVariable("cdItemUuid").let { cdItemUuid ->
                commonDaoServices.getLoggedInUser()?.let { loggedInUser ->
                    val cdItem = daoServices.findItemWithUuid(cdItemUuid)
                    var cdUpdateItem: CdItemDetailsEntity? = cdItem
                    //Save the general checklist
                    val checkListType = this.iChecklistInspectionTypesRepo.findByTypeName(form.confirmItemType!!)
                    val generalCheckList = form.generalChecklist()
                    generalCheckList.checkListType = checkListType
                    generalCheckList.description = cdItem.itemDescription
                    generalCheckList.inspectionDate = Date(java.util.Date().time)
                    cdItem.cdDocId?.let {
                        generalCheckList.cfs = it.freightStation?.cfsCode
                        generalCheckList.cocNumber = it.cocNumber
                        generalCheckList.idfNumber = it.idfNumber
                    }
                    val map=commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
                    val inspectionGeneral = daoServices.saveInspectionGeneralDetails(generalCheckList, cdItem, loggedInUser, map)
                    //Save the respective checklist
                    when (inspectionGeneral.checkListType?.uuid) {
                        daoServices.agrochemItemChecklistType -> {
                            val agrochemItemInspectionChecklist = form.agrochemChecklist()
                            agrochemItemInspectionChecklist.quantityDeclared = cdItem.quantity?.toString()
                            agrochemItemInspectionChecklist.description = cdItem.itemDescription
                            daoServices.saveInspectionAgrochemItemChecklist(
                                    agrochemItemInspectionChecklist,
                                    inspectionGeneral,
                                    loggedInUser,
                                    map
                            )
                            cdUpdateItem=agrochemItemInspectionChecklist.sampled?.let {
                                daoServices.checkIfChecklistUndergoesSampling(
                                        it,
                                        cdItem,
                                        map
                                )
                            }
                        }
                        daoServices.engineeringItemChecklistType -> {
                            val engineeringItemInspectionChecklist = form.engineeringChecklist()
                            daoServices.saveInspectionEngineeringItemChecklist(
                                    engineeringItemInspectionChecklist,
                                    inspectionGeneral,
                                    loggedInUser,
                                    map
                            )
                            cdUpdateItem = engineeringItemInspectionChecklist.sampled?.let {
                                daoServices.checkIfChecklistUndergoesSampling(
                                        it,
                                        cdItem,
                                        map
                                )
                            }
                        }
                        daoServices.otherItemChecklistType -> {
                            val otherItemInspectionChecklist = form.otherChecklist()
                            daoServices.saveInspectionOtherItemChecklist(
                                    otherItemInspectionChecklist,
                                    inspectionGeneral,
                                    loggedInUser,
                                    map
                            )
                            cdUpdateItem = otherItemInspectionChecklist.sampled?.let {
                                daoServices.checkIfChecklistUndergoesSampling(
                                        it,
                                        cdItem,
                                        map
                                )
                            }
                        }
                        daoServices.motorVehicleItemChecklistType -> {
                            val motorVehicleItemInspectionChecklist = form.vehicleChecklist()
                            daoServices.saveInspectionMotorVehicleItemChecklist(
                                    motorVehicleItemInspectionChecklist,
                                    inspectionGeneral,
                                    loggedInUser,
                                    map
                            )
                            cdUpdateItem = daoServices.updateItemNoSampling(cdItem, map)
                        }
                    }
                    //Save CD item details
                    val cdItemID: Long = cdItem.id?.let { commonDaoServices.makeAnyNotBeNull(it) } as Long
                    cdUpdateItem?.id = cdItemID
                    cdUpdateItem?.checkListTypeId = generalCheckList.checkListType
                    when {
                        cdUpdateItem != null -> {
                            daoServices.updateCDItemDetails(cdUpdateItem, cdItemID, loggedInUser, map)
                        }
                    }
                    //BPM: Update fill inspection details workflow
                    val cdDetails = cdItem.cdDocId
                    cdDetails?.cdStandard?.let { cdStd ->
                        daoServices.updateCDStatus(cdStd, applicationMapProperties.mapDIStatusTypeInspectionChecklistId)
                    }

                }
            }
            response.message = "Success"
            response.responseCode = ResponseCodes.SUCCESS_CODE
            return ServerResponse.ok().body(response)
        } catch (ex: Exception) {
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Invalid checklist request"
        }
        return ServerResponse.ok().body(response)
    }
}