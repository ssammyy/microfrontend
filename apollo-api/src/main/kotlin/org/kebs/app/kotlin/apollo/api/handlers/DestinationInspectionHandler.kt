/*
 *
 *  *
 *  *
 *  *  *    Copyright (c) ${YEAR}.   BSK Global Technologies
 *  *  *
 *  *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *    you may not use this file except in compliance with the License.
 *  *  *    You may obtain a copy of the License at
 *  *  *
 *  *  *       http://www.apache.org/licenses/LICENSE-2.0
 *  *  *
 *  *  *    Unless required by applicable law or agreed to in writing, software
 *  *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *   See the License for the specific language governing permissions and
 *  *  *   limitations under the License.
 *  *
 *
 */

package org.kebs.app.kotlin.apollo.api.handlers

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.DestinationInspectionBpmn
import org.kebs.app.kotlin.apollo.api.ports.provided.createUserAlert
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.*
import org.kebs.app.kotlin.apollo.common.dto.MinistryInspectionListResponseDto
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.exceptions.SupervisorNotFoundException
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.CdSampleCollectionEntity
import org.kebs.app.kotlin.apollo.store.model.CdSampleSubmissionItemsEntity
import org.kebs.app.kotlin.apollo.store.model.ServiceMapsEntity
import org.kebs.app.kotlin.apollo.store.model.di.*
import org.kebs.app.kotlin.apollo.store.model.qa.QaSampleSubmissionEntity
import org.kebs.app.kotlin.apollo.store.repo.di.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.ServerResponse.ok
import org.springframework.web.servlet.function.paramOrNull
import org.springframework.web.servlet.support.RequestContextUtils
import java.sql.Date


@Component
class DestinationInspectionHandler(
    private val applicationMapProperties: ApplicationMapProperties,
    private val commonDaoServices: CommonDaoServices,
    private val daoServices: DestinationInspectionDaoServices,
    private val qaDaoServices: QADaoServices,
    private val iDemandNoteRepo: IDemandNoteRepository,
    private val riskProfileDaoService: RiskProfileDaoService,
    private val importerDaoServices: ImporterDaoServices,
    private val iChecklistCategoryRepo: IChecklistCategoryRepository,
    private val iChecklistInspectionTypesRepo: IChecklistInspectionTypesRepository,
    private val cdTypesRepo: IConsignmentDocumentTypesEntityRepository,
    private val iLaboratoryRepo: ILaboratoryRepository,
    private val diBpmn: DestinationInspectionBpmn
) {
    @Value("\${destination.inspection.cd.type.cor}")
    lateinit var corCdType: String

    final val appId = applicationMapProperties.mapImportInspection

//    private val destinationInspectionHomePage = "destination-inspection/di-home"
private val destinationInspectionHomePage = "destination-inspection/di-home-new"
    private val destinationInspectionMinistryHomePage = "destination-inspection/ministry-home"
    private val cdPageList = "destination-inspection/cd-documents/consignment-document-list"
    private val cdInvoicePageList = "destination-inspection/cd-documents/consignment-document-invoice-list"
    private val cdCocPage = "destination-inspection/cd-documents/coc-view"
    private val cdCorPage = "destination-inspection/cd-documents/cor-view"
    private val cdCoiPage = "destination-inspection/cd-documents/coi-view"
    private val cdIdfPage = "destination-inspection/cd-documents/idf-view"
    private val cdManifestPage = "destination-inspection/cd-documents/manifest-view"
    private val cdDeclarationPage = "destination-inspection/cd-documents/declaration-view"
    private val cdDetailsView = "destination-inspection/cd-documents/consignment-document-detail"
    private val mvInspectionChecklistDetailsPage =
        "destination-inspection/cd-Inspection-documents/mv-inspection-checklist-detail.html"
    private val mvInspectionDetailsPage = "destination-inspection/cd-Inspection-documents/mv-inspection-details.html"
    private val goodsInspectionDetailsPage = "destination-inspection/cd-Inspection-documents/cd-inspection-report.html"
    private val cdItemDetailsPage = "destination-inspection/cd-documents/consignment-document-item-detail.html"
    private val diSSFDetailsPage = "destination-inspection/cd-Inspection-documents/ssf-details.html"

    //    private final val cdCocDetailsPage = "destination-inspection/cd-documents/consignment-document-item-detail.html"
    private val cdCheckListPage = "destination-inspection/cd-Inspection-documents/cd-inspection-check-list.html"
    private val cdSampleCollectPage = "destination-inspection/cd-Inspection-documents/cd-inspection-sample-collect.html"
    private val cdSampleSubmitPage = "destination-inspection/cd-Inspection-documents/cd-inspection-sample-submit.html"
    private val cdItemViewPageDetails = "redirect:/api/di/cd-item-details?cdItemUuid"

//        @PreAuthorize("hasAuthority('DI_INSPECTION_OFFICER_READ') or hasAuthority('DI_DIRECTOR_READ') or hasAuthority('DI_WETC_CHAIR_READ') " +
//            "or hasAuthority('DI_NSC_MEMBER_READ') or hasAuthority('DI_HOD_READ') or hasAuthority('DI_NSC_SECRETARY_READ') or" +
//            "hasAuthority('DI_CLUSTER_SUPERVISOR_READ') or hasAuthority('DI_WETC_MEMBER_READ') or hasAuthority('DI_OFFICER_CHARGE_READ') " +
//            "or hasAuthority('DI_MANAGER_INSPECTION_READ') or hasAuthority('DI_EXEMPTION_COMMITTEE_CHAIR_READ') or hasAuthority('IMPORTER') ")
//    fun home(req: ServerRequest): ServerResponse =
//        try {
//            val map = commonDaoServices.serviceMapDetails(appId)
//            req.attributes()["cdTypes"] = cdTypesRepo.findByStatus(map.activeStatus)
//                req.attributes()["diApplicationsTypes"] = importerDaoServices.findDiApplicationTypes(map.activeStatus)
//                req.attributes()["map"] = map
//                ok().render(destinationInspectionHomePage, req.attributes())
//            } catch (e: Exception) {
//                createUserAlert(req, e)
//            }

    fun home(req: ServerRequest): ServerResponse =
        try {
            val map = commonDaoServices.serviceMapDetails(appId)
            val auth = commonDaoServices.loggedInUserAuthentication()
            when {
                auth.authorities.stream().anyMatch { authority -> authority.authority == "DI_OFFICER_CHARGE_READ" } -> {
                    val usersEntity = commonDaoServices.findUserByUserName(auth.name)
                    val userProfilesEntity =
                        commonDaoServices.findUserProfileByUserID(usersEntity, map.activeStatus)
                    val allUserCFS = daoServices.findAllCFSUserList(
                        userProfilesEntity.id
                            ?: throw ExpectedDataNotFound("missing USER PROFILE id, check config")
                    )
                    val cdListAutoAssigned = mutableListOf<ConsignmentDocumentDetailsEntity>()

                    allUserCFS.forEach { assignedCfs ->
                        val cfsEntity = daoServices.findCfsID(
                            assignedCfs.cfsId
                                ?: throw ExpectedDataNotFound("missing cfs id, check config")
                        )
                        val allCdFound =
                            daoServices.findAllOngoingCdWithFreightStationID(cfsEntity)
                        cdListAutoAssigned.addAll(allCdFound)
                    }

                    req.attributes()["CDSAutoAssigned"] = cdListAutoAssigned
                    req.attributes()["CDSManualAssign"] =
                        daoServices.findAllCdWithNoFreightStation()

                    val cdListCompleted = mutableListOf<ConsignmentDocumentDetailsEntity>()

                    allUserCFS.forEach { assignedCfs ->
                        val cfsEntity = daoServices.findCfsID(
                            assignedCfs.cfsId
                                ?: throw ExpectedDataNotFound("missing cfs id, check config")
                        )
                        val allCdFound = daoServices.findAllCompleteCdWithFreightStation(cfsEntity)
                        cdListCompleted.addAll(allCdFound)
                    }
                    req.attributes()["CDCompleted"] = cdListCompleted
                    ok().render(destinationInspectionHomePage, req.attributes())
                }

                auth.authorities.stream().anyMatch { authority -> authority.authority == "DI_INSPECTION_OFFICER_READ" } -> {
                    val usersEntity = commonDaoServices.findUserByUserName(auth.name)
                    val userProfilesEntity =
                        commonDaoServices.findUserProfileByUserID(usersEntity, map.activeStatus)
                    val allUserCFS = daoServices.findAllCFSUserList(
                        userProfilesEntity.id
                            ?: throw ExpectedDataNotFound("missing USER PROFILE id, check config")
                    )
                    val cdListAutoAssigned = mutableListOf<ConsignmentDocumentDetailsEntity>()

                    allUserCFS.forEach { assignedCfs ->
                        val cfsEntity = daoServices.findCfsID(
                            assignedCfs.cfsId
                                ?: throw ExpectedDataNotFound("missing cfs id, check config")
                        )
                        val allCdFound = daoServices.findAllCdWithNoAssignedIoID(cfsEntity)
                        if (allCdFound != null) {
                            cdListAutoAssigned.addAll(allCdFound)
                        }
                    }

                    req.attributes()["CDSAutoAssigned"] =
                        daoServices.findAllCdWithAssignedIoID(usersEntity)
                    req.attributes()["CDSManualAssign"] = cdListAutoAssigned
                    req.attributes()["CDCompleted"] =
                        daoServices.findAllCompleteCdWithAssignedIoID(usersEntity)
                    ok().render(destinationInspectionHomePage, req.attributes())
                }
                else -> throw SupervisorNotFoundException("can't access this page Due to Invalid authority")
            }
        } catch (e: Exception) {
            createUserAlert(req, e)
        }

    fun ministryInspectionHome(req: ServerRequest): ServerResponse =
            try {
                val map = commonDaoServices.serviceMapDetails(appId)
                val ministryInspectionItemsOngoing = daoServices.findAllOngoingMinistryInspectionRequests()
                val ministryInspectionItemsComplete = daoServices.findAllCompleteMinistryInspectionRequests()
                val ministryInspectionItemsViewListOngoing: MutableList<MinistryInspectionListResponseDto> = ArrayList()
                val ministryInspectionItemsViewListComplete: MutableList<MinistryInspectionListResponseDto> = ArrayList()

                if (!ministryInspectionItemsOngoing.isNullOrEmpty()) {
                    for (item in ministryInspectionItemsOngoing) {
                        val ministryInspectionItem =  daoServices.convertCdItemDetailsToMinistryInspectionListResponseDto(item)
                        ministryInspectionItemsViewListOngoing.add(ministryInspectionItem)
                    }
                }
                if (!ministryInspectionItemsComplete.isNullOrEmpty()) {
                    for (item in ministryInspectionItemsComplete) {
                        val ministryInspectionItem =  daoServices.convertCdItemDetailsToMinistryInspectionListResponseDto(item)
                        ministryInspectionItemsViewListComplete.add(ministryInspectionItem)
                    }
                }

                req.attributes()["ongoingMinistryInspectionItemsViewList"] = ministryInspectionItemsViewListOngoing
                req.attributes()["completeMinistryInspectionItemsViewList"] = ministryInspectionItemsViewListComplete
                req.attributes()["motorVehicleMinistryInspectionChecklistName"] = daoServices.motorVehicleMinistryInspectionChecklistName
                req.attributes()["map"] = map
                ok().render(destinationInspectionMinistryHomePage, req.attributes())
            } catch (e: Exception) {
                createUserAlert(req, e)
            }

    fun cdList(req: ServerRequest): ServerResponse =
            try {
                val map = commonDaoServices.serviceMapDetails(appId)
                val auth = commonDaoServices.loggedInUserAuthentication()
                req.paramOrNull("cdTypeUuid")
                        ?.let { cdTypeUuid ->
                            val cdType = daoServices.findCdTypeDetailsWithUuid(cdTypeUuid)
                            req.attributes()["cdType"] = cdType
                            when {
                                auth.authorities.stream().anyMatch { authority -> authority.authority == "DI_OFFICER_CHARGE_READ" } -> {
                                    val usersEntity = commonDaoServices.findUserByUserName(auth.name)
                                    val userProfilesEntity =
                                        commonDaoServices.findUserProfileByUserID(usersEntity, map.activeStatus)
                                    val allUserCFS = daoServices.findAllCFSUserList(
                                        userProfilesEntity.id
                                            ?: throw ExpectedDataNotFound("missing USER PROFILE id, check config")
                                    )
                                    val cdListAutoAssigned = mutableListOf<ConsignmentDocumentDetailsEntity>()

                                    allUserCFS.forEach { assignedCfs ->
                                        val cfsEntity = daoServices.findCfsID(
                                            assignedCfs.cfsId
                                                ?: throw ExpectedDataNotFound("missing cfs id, check config")
                                        )
                                        val allCdFound =
                                            daoServices.findAllOngoingCdWithFreightStationID(cfsEntity, cdType)
                                        cdListAutoAssigned.addAll(allCdFound)
                                    }

                                    req.attributes()["CDSAutoAssigned"] = cdListAutoAssigned
                                    req.attributes()["CDSManualAssign"] =
                                        daoServices.findAllCdWithNoFreghitStation(cdType)

                                    val cdListCompleted = mutableListOf<ConsignmentDocumentDetailsEntity>()

                                    allUserCFS.forEach { assignedCfs ->
                                        val cfsEntity = daoServices.findCfsID(
                                            assignedCfs.cfsId
                                                ?: throw ExpectedDataNotFound("missing cfs id, check config")
                                        )
                                        val allCdFound = daoServices.findAllCompleteCdWithFreightStation(cfsEntity)
                                        cdListCompleted.addAll(allCdFound)
                                    }
                                    req.attributes()["CDCompleted"] = cdListCompleted
                                    ok().render(cdPageList, req.attributes())
                                }

                                auth.authorities.stream().anyMatch { authority -> authority.authority == "DI_INSPECTION_OFFICER_READ" } -> {
                                    val usersEntity = commonDaoServices.findUserByUserName(auth.name)
                                    val userProfilesEntity =
                                        commonDaoServices.findUserProfileByUserID(usersEntity, map.activeStatus)
                                    val allUserCFS = daoServices.findAllCFSUserList(
                                        userProfilesEntity.id
                                            ?: throw ExpectedDataNotFound("missing USER PROFILE id, check config")
                                    )
                                    val cdListAutoAssigned = mutableListOf<ConsignmentDocumentDetailsEntity>()

                                    allUserCFS.forEach { assignedCfs ->
                                        val cfsEntity = daoServices.findCfsID(
                                            assignedCfs.cfsId
                                                ?: throw ExpectedDataNotFound("missing cfs id, check config")
                                        )
                                        val allCdFound = daoServices.findAllCdWithNoAssignedIoID(cfsEntity, cdType)
                                        if (allCdFound != null) {
                                            cdListAutoAssigned.addAll(allCdFound)
                                        }
                                    }

                                    req.attributes()["CDSAutoAssigned"] =
                                        daoServices.findAllCdWithAssignedIoID(usersEntity, cdType)
                                    req.attributes()["CDSManualAssign"] = cdListAutoAssigned
                                    req.attributes()["CDCompleted"] =
                                        daoServices.findAllCompleteCdWithAssignedIoID(usersEntity, cdType)
                                    ok().render(cdPageList, req.attributes())
                                }
                                else -> throw SupervisorNotFoundException("can't access this page Due to Invalid authority")
                            }

                        }
                        ?: throw ExpectedDataNotFound("Required cdType diApplicationTypeUuid, check config")

            } catch (e: Exception) {
                createUserAlert(req, e)
            }


    fun cDDetails(req: ServerRequest): ServerResponse =
            try {
                val map = commonDaoServices.serviceMapDetails(appId)
                req.paramOrNull("cdUuid")
                        ?.let { cdUuid ->
                            val cdDetails = daoServices.findCDWithUuid(cdUuid)
                            req.attributes().putAll(loadCommonUIComponents(map))
                            cdDetails.cdType?.let {
                                cdDetails.cdStandardsTwo?.let { it1 ->
                                    cdDetails.cdImporter?.let { it2 ->
                                        cdDetails.cdConsignee?.let { it3 ->
                                            cdDetails.cdExporter?.let { it4 ->
                                                cdDetails.cdConsignor?.let { it5 ->
                                                    cdDetails.cdTransport?.let { it6 ->
                                                        cdDetails.cdHeaderOne?.let { it7 ->
                                                            cdDetails.cdType?.id?.let { it8 ->
                                                                loadCDUIComponents(
                                                                    cdDetails,
                                                                    map,
                                                                    it8,
                                                                    it1.id,
                                                                    it2,
                                                                    it3,
                                                                    it4,
                                                                    it5,
                                                                    it6,
                                                                    it7
                                                                )
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }?.let { req.attributes().putAll(it) }
                            when {
                                cdDetails.freightStation != null -> {
                                    req.attributes()["officers"] = daoServices.findOfficersList(cdDetails)
                                }
                                else -> {
                                    req.attributes()["ports"] = commonDaoServices.findAllSectionsListWithDivision(
                                        commonDaoServices.findDivisionWIthId(commonDaoServices.pointOfEntries.toLong()),
                                        map.activeStatus
                                    )
                                    //                                req.attributes()["cfsStations"] =  commonDaoServices.findAllFreightStationOnPortOfArrival()
                                }
                            }

                            daoServices.findCdItemsConsignmentDetailsOrNull(cdDetails)?.let { cdItemsList ->
                                for (item in cdItemsList) {
                                    daoServices.findInspectionGeneralWithItemDetailsOrNull(item)?.let { inspectionGeneral ->
                                        req.attributes()["inspectionReportApproval"] = inspectionGeneral.inspectionReportApprovalStatus
                                    }
                                }
                            }

                            req.attributes()["availableDemandNote"] =
                                cdDetails.id?.let { iDemandNoteRepo.findByCdId(it) }
                            req.attributes()["blacklists"] = daoServices.findAllBlackListUsers(map.activeStatus)
                            req.attributes()["oldVersionList"] = cdDetails.ucrNumber?.let {
                                daoServices.findAllOlderVersionCDsWithSameUcrNumber(
                                    it,
                                    map
                                )
                            }
                            req.attributes()["cdTypeGoodsCategory"] = daoServices.cdTypeGoodsCategory
                            req.attributes()["cdTypeVehiclesCategory"] = daoServices.cdTypeVehiclesCategory
                            req.attributes()["cdStatusTypeApproveCategory"] = daoServices.cdStatusTypeApproveCategory
                            req.attributes()["cdStatusTypeRejectCategory"] = daoServices.cdStatusTypeRejectCategory
                            req.attributes()["cdStatusTypeOnHoldCategory"] = daoServices.cdStatusTypeOnHoldCategory
                            req.attributes()["cdStatusTypeQuerydCategory"] = daoServices.cdStatusTypeQueryCategory
                            req.attributes()["attachments"] = daoServices.findAllAttachmentsByCd(cdDetails)

                            //Check for flash attributes
                            val request = req.servletRequest()
                            val flashMap = RequestContextUtils.getInputFlashMap(request)
                            if (flashMap != null) {
                                if (flashMap.containsKey("success")) {
                                    val success = flashMap["success"] as String?
                                    req.attributes()["success"] = success
                                    KotlinLogging.logger { }.info { "Success param = $success" }
                                } else if (flashMap.containsKey("error")) {
                                    val error = flashMap["error"] as String?
                                    req.attributes()["error"] = error
                                    KotlinLogging.logger { }.info { "Error param = $error" }
                                }
                            }
                            ok().render(cdDetailsView, req.attributes())
                        }
                        ?: throw ExpectedDataNotFound("Required uuid, check config")
            } catch (e: Exception) {
                createUserAlert(req, e)
            }


    fun cDItemDetails(req: ServerRequest): ServerResponse =
            try {
                val map = commonDaoServices.serviceMapDetails(appId)
                req.paramOrNull("cdItemUuid")
                        ?.let { cdItemUuid ->
                            val cdItemDetails = daoServices.findItemWithUuid(cdItemUuid)
                            req.attributes().putAll(loadCommonUIComponents(map))
                            req.attributes().putAll(loadCDInspectionModuleUIComponents(map))
                            req.attributes().putAll(loadCDItemDetailsUIComponents(cdItemDetails))
//                            cdItemDetails.cdDocId?.cdConsignee?.let {
//                                cdItemDetails.cdDocId?.cdExporter?.let { it1 ->
//                                    cdItemDetails.cdDocId?.cdConsignor?.let { it2 ->
//                                        cdItemDetails.cdDocId?.cdTransport?.let { it3 ->
//                                            cdItemDetails.cdDocId?.cdHeaderOne?.let { it4 ->
//                                                cdItemDetails.cdDocId?.cdImporter?.let { it5 ->
//                                                    cdItemDetails.cdDocId?.cdStandardsTwo?.let { it6 ->
//                                                        cdItemDetails.cdDocId?.cdType?.let { it7 ->
//                                                            loadCDUIComponents(cdItemDetails.cdDocId!!, it7, it6, it5, it, it1, it2, it3, it4)
//                                                        }
//                                                    }
//                                                }
//                                            }
//                                        }
//                                    }
//                                }
//                            }?.let { req.attributes().putAll(it) }
                            req.attributes()["cdType"] = cdItemDetails.cdDocId?.cdType
                            //Get non standard items
                            req.attributes()["itemNonStandard"] = daoServices.findCdItemNonStandardByItemID(cdItemDetails)
                            //Find demand note by item
                            req.attributes()["demandNote"] = daoServices.findDemandNote(cdItemDetails)
                            //Get COR details if available
                            //TODO: Abstract this to different method
                            cdItemDetails.cdDocId?.let { consignmentDocumentDetailsEntity ->
                                consignmentDocumentDetailsEntity.cdType?.let { consignmentDocumentTypesEntity ->
                                    val consignmentDocumentTypesEntityUuid = daoServices.findCdTypeDetails(consignmentDocumentTypesEntity.id)
                                    if (consignmentDocumentTypesEntityUuid.equals(corCdType)) {
                                        consignmentDocumentDetailsEntity.docTypeId?.let {
                                            req.attributes()["cor"] = daoServices.findCORById(it)
                                        }
                                    }
                                    req.attributes()["map"] = map
                                    //Check if motor vehicle inspection exists
                                    daoServices.findMotorVehicleInspectionByCdItem(cdItemDetails)?.let {
                                        KotlinLogging.logger { }.info { "Motor vehicle inspection returned = ${it.id}" }
                                        req.attributes()["motorVehicleInspectionChecklist"] = it
                                    }
                                }
                            }


                            return ok().render(cdItemDetailsPage, req.attributes())

                        }
                        ?: throw ExpectedDataNotFound("Required cdItemUuid, check config")

            } catch (e: Exception) {
                createUserAlert(req, e)
            }

    fun cdInvoiceDetails(req: ServerRequest): ServerResponse =
            try {
                req.paramOrNull("cdUuid")
                        ?.let { cdUuid ->
                            val map = commonDaoServices.serviceMapDetails(appId)
                            val cdDetails = daoServices.findCDWithUuid(cdUuid)
                            val itemListWithDemandNote = daoServices.findCDItemsListWithCDIDAndDemandNoteStatus(cdDetails, map)
                            req.attributes().putAll(loadCommonUIComponents(map))
                            req.attributes()["invoicesNotPaid"] = daoServices.findDemandNoteListFromCdItemList(itemListWithDemandNote, map.inactiveStatus)
//                            req.attributes().putAll(loadCDUIComponents())
                            req.attributes()["map"] = map

                            ok().render(cdInvoicePageList, req.attributes())
                        }
                        ?: throw ExpectedDataNotFound("Required uuid, check config")
            } catch (e: Exception) {
                createUserAlert(req, e)
            }

    fun cdCocDetails(req: ServerRequest): ServerResponse =
            try {
                req.paramOrNull("cdUuid")
                        ?.let { cdUuid ->
                            val map = commonDaoServices.serviceMapDetails(appId)
                            val cdDetails = daoServices.findCDWithUuid(cdUuid)
                            val cocDetails = cdDetails.ucrNumber?.let { daoServices.findCOC(it) }
                            req.attributes().putAll(loadCommonUIComponents(map))
                            req.attributes()["cocDetails"] = cocDetails
                            req.attributes()["cdDetails"] = cdDetails
                            req.attributes()["itemCOC"] = cocDetails?.id?.let { daoServices.findCocItemList(it) }
                            req.attributes()["map"] = map
//                            var pageToView: String = ""
                            val pageToView = when (cocDetails?.route) {
                                applicationMapProperties.mapDICOIRoute -> {
                                    cdCoiPage
                                }
                                else -> {
                                    cdCocPage
                                }
                            }
                            ok().render(pageToView, req.attributes())
                        }
                    ?: throw ExpectedDataNotFound("Required uuid, check config")
            } catch (e: Exception) {
                createUserAlert(req, e)
            }

//    fun cdCoiDetails(req: ServerRequest): ServerResponse =
//            try {
//                req.paramOrNull("cdUuid")
//                        ?.let { cdUuid ->
//                            val map = commonDaoServices.serviceMapDetails(appId)
//                            val cdDetails = daoServices.findCDWithUuid(cdUuid)
//                            val coiDetails = cdDetails.ucrNumber?.let { daoServices.findCOI(it) }
//                            req.attributes().putAll(loadCommonUIComponents(map))
//                            req.attributes()["cocDetails"] = coiDetails
//                            req.attributes()["cdDetails"] = cdDetails
//                            req.attributes()["itemCOC"] = coiDetails?.id?.let { daoServices.findCoiItemList(it) }
//                            req.attributes()["map"] = map
//                            ok().render(cdCocPage, req.attributes())
//                        }
//                        ?: throw ExpectedDataNotFound("Required uuid, check config")
//            } catch (e: Exception) {
//                createUserAlert(req, e)
//            }

    fun cdIdfDetails(req: ServerRequest): ServerResponse =
        try {
            req.paramOrNull("cdUuid")
                ?.let { cdUuid ->
                    val map = commonDaoServices.serviceMapDetails(appId)
                    val cdDetails = daoServices.findCDWithUuid(cdUuid)
                    val idfDetails = cdDetails.ucrNumber?.let { daoServices.findIdf(it) }
                    req.attributes().putAll(loadCommonUIComponents(map))
                    req.attributes()["idfDetails"] = idfDetails
                            req.attributes()["cdDetails"] = cdDetails
                            req.attributes()["itemIdf"] = idfDetails?.let { daoServices.findIdfItemList(it) }
                            req.attributes()["map"] = map
                            ok().render(cdIdfPage, req.attributes())
                        }
                        ?: throw ExpectedDataNotFound("Required uuid, check config")
            } catch (e: Exception) {
                createUserAlert(req, e)
            }

    fun cdCorDetails(req: ServerRequest): ServerResponse =
            try {
                req.paramOrNull("cdUuid")
                        ?.let { cdUuid ->
                            val map = commonDaoServices.serviceMapDetails(appId)
                            val cdDetails = daoServices.findCDWithUuid(cdUuid)
                            val cdItem = daoServices.findCDItemsListWithCDID(cdDetails)[0]
                            val itemNonStandardDetail = daoServices.findCdItemNonStandardByItemID(cdItem)
                            val corDetails = itemNonStandardDetail?.chassisNo?.let { daoServices.findCORByChassisNumber(it) }
                            req.attributes().putAll(loadCommonUIComponents(map))
                            req.attributes()["corDetails"] = corDetails
                            req.attributes()["cdDetails"] = cdDetails
                            req.attributes()["map"] = map
                            ok().render(cdCorPage, req.attributes())
                        }
                    ?: throw ExpectedDataNotFound("Required uuid, check config")
            } catch (e: Exception) {
                createUserAlert(req, e)
            }

    fun cdManifestDetails(req: ServerRequest): ServerResponse =
        try {
            req.paramOrNull("cdUuid")
                ?.let { cdUuid ->
                    val map = commonDaoServices.serviceMapDetails(appId)
                    val cdDetails = daoServices.findCDWithUuid(cdUuid)
                    val declarationDetails = cdDetails.ucrNumber?.let { daoServices.findDeclaration(it) }
                    val manifestDetails = declarationDetails?.billCode?.let { daoServices.findManifest(it) }
                    req.attributes().putAll(loadCommonUIComponents(map))
                    req.attributes()["manifestDetails"] = manifestDetails
                    req.attributes()["cdDetails"] = cdDetails
//                            req.attributes()["itemIdf"] = manifestDetails?.id?.let { daoServices.findIdfItemList(it) }
                    req.attributes()["map"] = map
                    ok().render(cdManifestPage, req.attributes())
                }
                ?: throw ExpectedDataNotFound("Required uuid, check config")
        } catch (e: Exception) {
            createUserAlert(req, e)
        }

    fun cdCustomDeclarationDetails(req: ServerRequest): ServerResponse =
        try {
            req.paramOrNull("cdUuid")
                ?.let { cdUuid ->
                    val map = commonDaoServices.serviceMapDetails(appId)
                    val cdDetails = daoServices.findCDWithUuid(cdUuid)
                    val declarationDetails = cdDetails.ucrNumber?.let { daoServices.findDeclaration(it) }
                    req.attributes().putAll(loadCommonUIComponents(map))
                    req.attributes()["declarationDetails"] = declarationDetails
                    req.attributes()["cdDetails"] = cdDetails
                    req.attributes()["itemDeclaration"] =
                        declarationDetails?.let { daoServices.findDeclarationItemList(it) }
                    req.attributes()["map"] = map
                    ok().render(cdDeclarationPage, req.attributes())
                }
                ?: throw ExpectedDataNotFound("Required uuid, check config")
        } catch (e: Exception) {
            createUserAlert(req, e)
        }

    fun inspectionChecklistReportDetails(req: ServerRequest): ServerResponse =
        try {
            val map = commonDaoServices.serviceMapDetails(appId)
            req.paramOrNull("cdItemUuid")
                ?.let { cdItemUuid ->
                    val cdItemDetails = daoServices.findItemWithUuid(cdItemUuid)
                    val generalChecklist = daoServices.findInspectionGeneralWithItemDetails(cdItemDetails)
                    var inspectionChecklist: Any? = null
                    //Get checklist type details
                            when (generalChecklist.checkListType?.uuid) {
                                daoServices.agrochemItemChecklistType -> {
                                    inspectionChecklist = daoServices.findInspectionAgrochemWithInspectionGeneral(generalChecklist)
                                }
                                daoServices.engineeringItemChecklistType -> {
                                    inspectionChecklist = daoServices.findInspectionEngineeringWithInspectionGeneral(generalChecklist)
                                }
                                daoServices.otherItemChecklistType -> {
                                    inspectionChecklist = daoServices.findInspectionOthersWithInspectionGeneral(generalChecklist)
                                }
                            }

                            req.attributes().putAll(loadCommonUIComponents(map))
                            req.attributes().putAll(loadCheckListTypeUIComponents())
                            req.attributes()["inspectionChecklist"] = inspectionChecklist
                            req.attributes()["generalInspectionReport"] = CdInspectionGeneralEntity()
                            return ok().render(goodsInspectionDetailsPage, req.attributes())

                        }
                        ?: throw ExpectedDataNotFound("Required cdItemUuid, check config")

            } catch (e: Exception) {
                createUserAlert(req, e)
            }

    fun mvInspectionDetails(req: ServerRequest): ServerResponse =
            commonDaoServices.serviceMapDetails(appId)
                    .let { map ->
                        req.paramOrNull("itemId")
                                ?.let { itemId ->
                                    req.paramOrNull("docType")?.let { docType ->
                                        KotlinLogging.logger { }.info { "itemId = $itemId & docType = $docType" }
                                        //Get the CD Item
                                        val cdItemDetails = daoServices.findItemWithItemID(itemId.toLong())
                                        req.attributes()["item"] = cdItemDetails
                                        //Get inspection checklist details
                                        val inspectionGeneralEntity = daoServices.findInspectionGeneralWithItemDetails(cdItemDetails)
                                        //Get the motor vehicle checklist
                                        val motorVehicleItemChecklist = daoServices.findInspectionMotorVehicleWithInspectionGeneral(inspectionGeneralEntity)
                                        req.attributes().putAll(loadCommonUIComponents(map))
                                        req.attributes()["inspectionGeneralEntity"] = inspectionGeneralEntity
                                        req.attributes()["mvInspectionChecklist"] = motorVehicleItemChecklist
                                        req.attributes()["map"] = map

                                        //Check for flash attributes
                                        val request = req.servletRequest()
                                        val flashMap = RequestContextUtils.getInputFlashMap(request)
                                        if (flashMap != null) {
                                            val success = flashMap["success"] as String?
                                            req.attributes()["success"] = success
                                            KotlinLogging.logger { }.info { "Success param = $success" }
                                        }

                                        var myPage = ""
                                        when (docType) {
                                            daoServices.motorVehicleMinistryInspectionChecklistName -> {
                                                myPage = mvInspectionChecklistDetailsPage
                                            }
                                            daoServices.motorVehicleInspectionDetailsName -> {
                                                req.attributes()["motorVehicleItemChecklist"] = daoServices.motorVehicleItemChecklistType
                                                myPage = mvInspectionDetailsPage
                                            }
                                        }
                                        return ok().render(myPage, req.attributes())

                                    } ?: throw ExpectedDataNotFound("Required docType, check config")
                                } ?: throw ExpectedDataNotFound("Required itemId, check config")
                    }

    fun inspectionDetails(req: ServerRequest): ServerResponse =
            try {
                val map = commonDaoServices.serviceMapDetails(appId)
                req.paramOrNull("cdItemUuid")
                        ?.let { cdItemUuid ->
                            req.paramOrNull("docType")
                                    ?.let { docType ->
                                        val cdItemDetails = daoServices.findItemWithUuid(cdItemUuid)
                                        val cdItemID: Long = cdItemDetails.id?.let { commonDaoServices.makeAnyNotBeNull(it) } as Long
                                        req.attributes().putAll(loadCommonUIComponents(map))
                                        req.attributes().putAll(loadCDInspectionModuleUIComponents(map))
                                        req.attributes().putAll(loadCDItemDetailsUIComponents(cdItemDetails))
                                        req.attributes()["itemNonStandard"] = daoServices.findCdItemNonStandardByItemID(cdItemDetails)
                                        req.attributes()["cdTransport"] = cdItemDetails.cdDocId?.cdTransport?.let { daoServices.findCdTransportDetails(it) }
                                        req.attributes()["cdImporter"] = cdItemDetails.cdDocId?.cdImporter?.let {
                                            daoServices.findCDImporterDetails(it)
                                        }
                                        req.attributes()["cdExporter"] = cdItemDetails.cdDocId?.cdExporter?.let {
                                            daoServices.findCdExporterDetails(it)
                                        }
                                        req.attributes()["map"] = map
                                        var myPage = ""
                                        // TODO: 12/15/2020  create a page for Error for initializing myPage
                                        when (docType) {
                                            daoServices.checkListName -> {
                                                req.attributes()["itemInspectionDate"] = daoServices.getInspectionDate(cdItemDetails)
                                                //Get Demand Not Details
                                                when (cdItemDetails.dnoteStatus) {
                                                    map.activeStatus -> {
                                                        val demandNote = daoServices.findDemandNote(cdItemDetails)
                                                        when (demandNote?.paymentStatus) {
                                                            map.activeStatus -> {
                                                                req.attributes()["demandNote"] = demandNote
                                                            }
                                                            map.inactiveStatus -> {
                                                                throw ExpectedDataNotFound("Payment has not been done yet")
                                                            }
                                                        }
                                                    }
                                                }
//                                                req.attributes()["coc"] = cdItemDetails.cdDocId?.ucrNumber?.let { daoServices.findCOC(it) }
                                                req.attributes()["message"] = docType
                                                myPage = cdCheckListPage
                                            }
                                            daoServices.sampCollectName -> {
                                                val filledCheckList = daoServices.findSavedGeneralInspection(cdItemDetails)
                                                var ksEasApplicable: String? = null
                                                var dateExpiry: Date? = null
                                                var dateMfgPackaging: Date? = null
                                                //Get checklist type details
                                                when (filledCheckList.checkListType?.uuid) {
                                                    daoServices.agrochemItemChecklistType -> {
                                                        val agrochem = daoServices.findInspectionAgrochemWithInspectionGeneral(filledCheckList)
                                                        dateExpiry = agrochem.dateExpiry
                                                        dateMfgPackaging = agrochem.dateMfgPackaging
                                                        ksEasApplicable = agrochem.ksEasApplicable
                                                    }
                                                    daoServices.engineeringItemChecklistType -> {
                                                        val engineering = daoServices.findInspectionEngineeringWithInspectionGeneral(filledCheckList)
                                                        ksEasApplicable = engineering.ksEasApplicable
                                                    }
                                                    daoServices.otherItemChecklistType -> {
                                                        val others = daoServices.findInspectionOthersWithInspectionGeneral(filledCheckList)
                                                        ksEasApplicable = others.ksEasApplicable
                                                    }
                                                }
                                                req.attributes()["dateExpiry"] = dateExpiry
                                                req.attributes()["dateMfgPackaging"] = dateMfgPackaging
                                                req.attributes()["ksEasApplicable"] = ksEasApplicable
                                                req.attributes()["message"] = docType
                                                myPage = cdSampleCollectPage
                                            }
                                            daoServices.sampSubmitName -> {
                                                val sampleCollect = daoServices.findSavedSampleCollection(cdItemID)
                                                req.attributes().putAll(sampleDetails(daoServices.findSavedGeneralInspection(cdItemDetails), sampleCollect))
                                                req.attributes().putAll(sampleDatesDetails(sampleCollect))
                                                req.attributes()["message"] = docType
                                                myPage = cdSampleSubmitPage
                                            }
                                            daoServices.sampSubmitAddParamDetails -> {
                                                val sampleCollect = daoServices.findSavedSampleCollection(cdItemID)
                                                val sampleSubmit = daoServices.findSavedSampleSubmission(cdItemID)
                                                val itemParameters = daoServices.findListSampleSubmissionParameter(sampleSubmit)
                                                req.attributes().putAll(sampleDetails(daoServices.findSavedGeneralInspection(cdItemDetails), sampleCollect))
                                                req.attributes().putAll(sampleDatesDetails(sampleCollect))
                                                req.attributes().putAll(loadSampleViewDetailsUIComponents(docType, daoServices.findSavedSampleSubmission(cdItemID), itemParameters))
                                                req.attributes()["sampleParam"] = CdSampleSubmissionParamatersEntity()
                                                myPage = cdSampleSubmitPage
                                            }
                                            daoServices.bsNumber -> {
                                                val sampleCollect = daoServices.findSavedSampleCollection(cdItemID)
                                                val sampleSubmit = daoServices.findSavedSampleSubmission(cdItemID)
                                                val itemParameters = daoServices.findListSampleSubmissionParameter(sampleSubmit)
                                                req.attributes().putAll(sampleDetails(daoServices.findSavedGeneralInspection(cdItemDetails), sampleCollect))
                                                req.attributes().putAll(sampleDatesDetails(sampleCollect))
                                                req.attributes().putAll(loadSampleViewDetailsUIComponents(docType, daoServices.findSavedSampleSubmission(cdItemID), itemParameters))
                                                myPage = cdSampleSubmitPage
                                            }
                                            daoServices.labResults -> {
                                                val sampleCollect = daoServices.findSavedSampleCollection(cdItemID)
                                                val sampleSubmit = daoServices.findSavedSampleSubmission(cdItemID)
                                                val itemParameters = daoServices.findListSampleSubmissionParameter(sampleSubmit)
                                                req.attributes()["allParameterCompleteResults"] = daoServices.checkIfResultsCompleted(itemParameters, map)
                                                req.attributes().putAll(sampleDetails(daoServices.findSavedGeneralInspection(cdItemDetails), sampleCollect))
                                                req.attributes().putAll(sampleDatesDetails(sampleCollect))
                                                req.attributes().putAll(loadSampleViewDetailsUIComponents(docType, daoServices.findSavedSampleSubmission(cdItemID), itemParameters))
                                                myPage = cdSampleSubmitPage
                                            }
                                            daoServices.sampSubmitParamName -> {
                                                val sampleSubmit = daoServices.findSavedSampleSubmission(cdItemID)
                                                req.attributes()["itemParameters"] = daoServices.findListSampleSubmissionParameter(sampleSubmit)
                                                req.attributes()["sampleSubmit"] = sampleSubmit
                                                req.attributes()["message"] = docType
                                                myPage = cdSampleSubmitPage
                                            }
                                        }
                                        return ok().render(myPage, req.attributes())
                                    }
                                    ?: throw ExpectedDataNotFound("Required docType, check config")
                        }
                        ?: throw ExpectedDataNotFound("Required cdItemUuid, check config")

            } catch (e: Exception) {
                createUserAlert(req, e)
            }


    fun getSSfDetails(req: ServerRequest): ServerResponse {
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        val cdItemID = req.paramOrNull("cdItemID")?.toLong() ?: throw ExpectedDataNotFound("Required cd Item ID, check config")
        val item = daoServices.findItemWithItemID(cdItemID)
        val ssfDetails = daoServices.findSampleSubmittedBYCdItemID(item.id?: throw ExpectedDataNotFound("MISSING CD ITEM ID"))

        req.attributes()["ssfDetails"] = ssfDetails
        req.attributes()["LabResultsParameters"] =  qaDaoServices.findSampleLabTestResultsRepoBYBSNumber(ssfDetails.bsNumber?: throw ExpectedDataNotFound("MISSING BS NUMBER"))

        return ok().render(diSSFDetailsPage, req.attributes())

    }

    fun submitMVInspectionRequestToMinistry(req: ServerRequest): ServerResponse =
            try {
                val map = commonDaoServices.serviceMapDetails(appId)
                val loggedInUser = commonDaoServices.loggedInUserDetails()
                req.paramOrNull("cdItemUuid")
                        ?.let { cdItemUuid ->
                            //Get the CD Item entity
                            val cdItemDetails = daoServices.findItemWithUuid(cdItemUuid)
                            val cdItemID: Long = cdItemDetails.id?.let { commonDaoServices.makeAnyNotBeNull(it) } as Long
//                            loggedInUser.email?.let {
//                                daoServices.sendMotorVehicleInspectionRequestEmail(it, cdItemDetails).let {
//                                            cdItemDetails.ministrySubmissionStatus = map.activeStatus
//                                            daoServices.updateCDItemDetails(cdItemDetails, cdItemID, loggedInUser, map)
//                                        }
//                            }
                            //Find users with Ministry Type
                            //TODO: Figure out how to notify a single user, or assign one user in ministry
                            cdItemDetails.ministrySubmissionStatus = map.activeStatus
                            daoServices.updateCDItemDetails(cdItemDetails, cdItemID, loggedInUser, map)

                            val cdDetails = cdItemDetails.cdDocId
                            commonDaoServices.findAllUsersWithMinistryUserType()?.let { ministryUsers ->
                                cdDetails?.id?.let { cdDetailsId ->
                                    ministryUsers.get(0).id?.let {
                                        diBpmn.diMinistryInspectionRequiredComplete(cdDetailsId,
                                            it, true
                                        )
                                    }
                                }
                            }

                            ok().render("$cdItemViewPageDetails=$cdItemUuid")
                        } ?: throw ExpectedDataNotFound("Required cdItemUuid, check config")
            } catch (e: Exception) {
                createUserAlert(req, e)
            }


    fun mvInspectionChecklistDetails(req: ServerRequest): ServerResponse =
            try {
                val map = commonDaoServices.serviceMapDetails(appId)
                req.paramOrNull("cdItemUuid")
                        ?.let { cdItemUuid ->
                            //Get the CD Item
                            val cdItemDetails = daoServices.findItemWithUuid(cdItemUuid)
                            //Get inspection checklist details
                            val inspectionGeneralEntity = daoServices.findInspectionGeneralWithItemDetails(cdItemDetails)
                            //Get the motor vehicle checklist
                            val motorVehicleItemChecklist = daoServices.findInspectionMotorVehicleWithInspectionGeneral(inspectionGeneralEntity)
                            req.attributes().putAll(loadCommonUIComponents(map))
                            req.attributes()["mvInspectionChecklist"] = motorVehicleItemChecklist
                            req.attributes()["map"] = map

                            return ok().render(mvInspectionChecklistDetailsPage, req.attributes())
                        }
                        ?: throw ExpectedDataNotFound("Required itemId, check config")
            } catch (e: Exception) {
                createUserAlert(req, e)
            }


//    fun cdSamplesToLab(req: ServerRequest): ServerResponse =
//            try {
//                val map = commonDaoServices.serviceMapDetails(appId)
//                req.paramOrNull("cdSampleSubmitID")
//                        ?.let { cdSampleSubmitID ->
//                            req.attributes().putAll(loadCommonUIComponents(map))
//                            req.attributes().putAll(loadCDInspectionModuleUIComponents(map))
//                            req.attributes()["map"] = map
//
//                            ok().render(cdCocDetailsPage, req.attributes())
//                        }
//                        ?: throw ExpectedDataNotFound("Required cdID, check config")
//
//            } catch (e: Exception) {
//                createUserAlert(req, e)


//    req.attributes()["feePaid"] = filledCheckList.feePaid
//    req.attributes()["receiptNumber"] = filledCheckList.receiptNumber
//    req.attributes()["ksEasApplicable"] = sampleCollect.referenceStandards
//    req.attributes()["manufactureDate"] = sampleCollect.manufacturingDate
//    req.attributes()["expiryDate"] = sampleCollect.expiryDate
//            }


    private fun sampleDetails(filledCheckList: CdInspectionGeneralEntity, sampleCollect: CdSampleCollectionEntity): MutableMap<String, String?> {
        return mutableMapOf(
                Pair("feePaid", filledCheckList.feePaid),
                Pair("receiptNumber", filledCheckList.receiptNumber),
                Pair("ksEasApplicable", sampleCollect.referenceStandards)
        )
    }

    private fun sampleDatesDetails(sampleCollect: CdSampleCollectionEntity): MutableMap<String, Date?> {
        return mutableMapOf(
                Pair("manufactureDate", sampleCollect.manufacturingDate),
                Pair("expiryDate", sampleCollect.expiryDate)
        )
    }

    private fun loadCDUIComponents(cdDetails: ConsignmentDocumentDetailsEntity,
                                   map: ServiceMapsEntity,
                                   cdTypeID: Long,
                                   cdStandardsTwoID: Long,
                                   cdImporterID: Long,
                                   cdConsigneeID: Long,
                                   cdExporterID: Long,
                                   cdConsignorID: Long,
                                   cdTransportID: Long,
                                   cdHeaderOneID: Long): MutableMap<String, Any> {
        val cdImporter = daoServices.findCDImporterDetails(cdImporterID)
        var riskProfileImporter = false
        cdImporter.pin?.let {
            riskProfileDaoService.findImportersInRiskProfile(it, map.activeStatus).let { riskImporter ->
                when {
                    riskImporter != null -> {
                        riskProfileImporter = true
                    }
                }
            }
        }
        val cdConsignee = daoServices.findCdConsigneeDetails(cdConsigneeID)
        var riskProfileConsignee = false
        cdConsignee.pin?.let {
            riskProfileDaoService.findConsigneeInRiskProfile(it, map.activeStatus).let { riskConsignee ->
                when {
                    riskConsignee != null -> {
                        riskProfileConsignee = true
                    }
                }
            }
        }
        val cdExporter = daoServices.findCdExporterDetails(cdExporterID)
        var riskProfileExporter = false
        cdExporter.pin?.let {
            riskProfileDaoService.findExporterInRiskProfile(it, map.activeStatus).let { riskExporter ->
                when {
                    riskExporter != null -> {
                        riskProfileExporter = true
                    }
                }
            }
        }
        val cdConsignor = daoServices.findCdConsignorDetails(cdConsignorID)
        var riskProfileConsignor = false
        cdConsignor.pin?.let {
            riskProfileDaoService.findConsignorInRiskProfile(it, map.activeStatus).let { riskConsignor ->
                when {
                    riskConsignor != null -> {
                        riskProfileConsignor = true
                    }
                }
            }
        }


        return mutableMapOf(
                Pair("cdDetails", cdDetails),
                Pair("cdType", daoServices.findCdTypeDetails(cdTypeID)),
                Pair("cdStandardsTwo", daoServices.findCdStandardsTWODetails(cdStandardsTwoID)),
                Pair("cdImporter", cdImporter),
                Pair("cdConsignee", cdConsignee),
                Pair("cdExporter", cdExporter),
                Pair("cdConsignor", cdConsignor),
                Pair("riskProfileImporter", riskProfileImporter),
                Pair("riskProfileConsignee", riskProfileConsignee),
                Pair("riskProfileExporter", riskProfileExporter),
                Pair("riskProfileConsignor", riskProfileConsignor),
                Pair("cdTransport", daoServices.findCdTransportDetails(cdTransportID)),
                Pair("cdHeaderOne", daoServices.findCdHeaderOneDetails(cdHeaderOneID)),
                Pair("itemsCD", daoServices.findCDItemsListWithCDID(cdDetails))
        )
    }


    private fun loadSampleViewDetailsUIComponents(docType: String, sampleSubmit: CdSampleSubmissionItemsEntity, itemParameters: List<CdSampleSubmissionParamatersEntity>): MutableMap<String, Any> {
        return mutableMapOf(
                Pair("itemParameters", itemParameters),
                Pair("sampleSubmit", sampleSubmit),
                Pair("message", docType)
        )
    }

//    private fun loadInvoiceViewDetailsUIComponents(itemParameters:List<CdSampleSubmissionParamatersEntity> ): MutableMap<String, Any> {
//        return mutableMapOf(
//                Pair("itemParameters", itemParameters),
//                Pair("sampleSubmit", sampleSubmit),
//                Pair("message", docType)
//        )
//    }

    private fun loadCommonUIComponents(s: ServiceMapsEntity): MutableMap<String, Any> {
        return mutableMapOf(
                Pair("activeStatus", s.activeStatus),
                Pair("inActiveStatus", s.inactiveStatus),
                Pair("initStatus", s.initStatus),
                Pair("currentDate", commonDaoServices.getCurrentDate()),
                Pair("CDStatusTypes", daoServices.findCdStatusValueList(s.activeStatus)),
                Pair("cdTypeGoodsCategory", daoServices.cdTypeGoodsCategory),
                Pair("cdTypeVehiclesCategory", daoServices.cdTypeVehiclesCategory),
                Pair("upLoads", DiUploadsEntity())
        )
    }

    private fun loadCDItemDetailsUIComponents(cdItemDetails: CdItemDetailsEntity): MutableMap<String, Any> {

        return mutableMapOf(
                Pair("item", cdItemDetails)
        )
    }

    private fun loadCDDetailsUIComponents(ucrNumber: String): MutableMap<String, Any> {

        return mutableMapOf(
                Pair("coc", daoServices.findCOC(ucrNumber)),
                Pair("idf", daoServices.findCOC(ucrNumber)),
                Pair("rfc", daoServices.findCOC(ucrNumber)),
                Pair("cor", daoServices.findCOC(ucrNumber))
        )
    }

    private fun loadCheckListTypeUIComponents(): MutableMap<String, Any> {

        return mutableMapOf(
                Pair("agrochemItemChecklistType", daoServices.agrochemItemChecklistType),
                Pair("engineeringItemChecklistType", daoServices.engineeringItemChecklistType),
                Pair("otherItemChecklistType", daoServices.otherItemChecklistType),
                Pair("motorVehicleItemChecklistType", daoServices.motorVehicleItemChecklistType)
        )
    }

    private fun loadCDInspectionModuleUIComponents(map: ServiceMapsEntity): MutableMap<String, Any?> {

        return mutableMapOf(
                Pair("destinationFees", daoServices.findDIFeeList(map.activeStatus)),

                Pair("sampleSubmit", CdSampleSubmissionItemsEntity()),
                Pair("sampleCollect", CdSampleCollectionEntity()),
                Pair("SampleSubmissionDetails", QaSampleSubmissionEntity()),
                Pair("sampleParam", CdSampleSubmissionParamatersEntity()),
                Pair("generalCheckList", CdInspectionGeneralEntity()),
                Pair("agrochemItemInspectionChecklist", CdInspectionAgrochemItemChecklistEntity()),
                Pair("engineeringItemInspectionChecklist", CdInspectionEngineeringItemChecklistEntity()),
                Pair("otherItemInspectionChecklist", CdInspectionOtherItemChecklistEntity()),
                Pair("motorVehicleItemInspectionChecklist", CdInspectionMotorVehicleItemChecklistEntity()),
                Pair("upLoads", DiUploadsEntity()),

                Pair("categories", iChecklistCategoryRepo.findByStatus(commonDaoServices.activeStatus.toInt())),
                Pair("checkListTypes", iChecklistInspectionTypesRepo.findByStatus(commonDaoServices.activeStatus.toInt())),
                Pair("laboratories", iLaboratoryRepo.findByStatus(commonDaoServices.activeStatus.toInt())),

                Pair("sampCollectName", daoServices.sampCollectName),
                Pair("sampSubmitName", daoServices.sampSubmitName),
                Pair("sampSubmitParamName", daoServices.sampSubmitParamName),
                Pair("sampSubmitAddParamDetails", daoServices.sampSubmitAddParamDetails),
                Pair("checkListName", daoServices.checkListName),
                Pair("bsNumber", daoServices.bsNumber),
                Pair("labResults", daoServices.labResults),
                Pair("labResultsAllComplete", daoServices.labResultsAllComplete),
                Pair("viewPage", daoServices.viewPage),
                Pair("motorVehicleItemChecklist", daoServices.motorVehicleItemChecklistType),
                Pair("motorVehicleMinistryInspectionChecklistName", daoServices.motorVehicleMinistryInspectionChecklistName),
                Pair("motorVehicleInspectionDetailsName", daoServices.motorVehicleInspectionDetailsName)


//                Pair("rfcCOCEntity", MainRfcCocEntity()),
//                Pair("rfcCOREntity", MainRfcCorEntity()),
//                Pair("rfcCoiItemEntity", MainRfcCoiItemsEntity())

        )
    }


}
