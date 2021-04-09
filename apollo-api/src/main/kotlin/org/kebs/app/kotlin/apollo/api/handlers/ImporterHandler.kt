///*
// *
// *  *
// *  *
// *  *  *    Copyright (c) ${YEAR}.   BSK Global Technologies
// *  *  *
// *  *  *    Licensed under the Apache License, Version 2.0 (the "License");
// *  *  *    you may not use this file except in compliance with the License.
// *  *  *    You may obtain a copy of the License at
// *  *  *
// *  *  *       http://www.apache.org/licenses/LICENSE-2.0
// *  *  *
// *  *  *    Unless required by applicable law or agreed to in writing, software
// *  *  *    distributed under the License is distributed on an "AS IS" BASIS,
// *  *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// *  *  *   See the License for the specific language governing permissions and
// *  *  *   limitations under the License.
// *  *
// *
// */
//
package org.kebs.app.kotlin.apollo.api.handlers

import liquibase.pro.packaged.e
import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.ports.provided.createUserAlert
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DestinationInspectionDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.ImporterDaoServices
import org.kebs.app.kotlin.apollo.common.exceptions.*
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.model.di.ConsignmentDocumentDetailsEntity
import org.kebs.app.kotlin.apollo.store.model.importer.*
import org.kebs.app.kotlin.apollo.store.repo.importer.*
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.ServerResponse.ok
import org.springframework.web.servlet.function.paramOrNull


@Component
class ImporterHandler(
        private val applicationMapProperties: ApplicationMapProperties,
        private val daoServices: ImporterDaoServices,
        private val diDaoServices: DestinationInspectionDaoServices,
        private val commonDaoServices: CommonDaoServices,
        private val iRfcTypesRepo: IRfcTypesTypesRepository,
) {
    final val appId: Int = applicationMapProperties.mapImporterDetails

    private final val importerHomePage = "importer/importer-home"
    private final val rfcPageList = "importer/rfc-list"
    private final val diApplicationPageList = "importer/di-application-list"
    private final val diApplicationNewPage = "importer/new-di-application-details"
    private final val diApplicationDetailsPage = "importer/di-application-details"
    private final val rfcNewPage = "importer/new-rfc-details"
    private final val rfcPageDetails = "importer/rfc-detail"
    private final val rfcPageDetailsRedirect = "redirect:/api/importer/rfc-detail?rfcID"


    fun home(req: ServerRequest): ServerResponse =
            try {
                val map = commonDaoServices.serviceMapDetails(appId)
                req.attributes()["rfcTypes"] = iRfcTypesRepo.findByStatus(map.activeStatus)
                req.attributes()["diApplicationsTypes"] = daoServices.findDiApplicationTypes(map.activeStatus)
                req.attributes()["map"] = map
                ok().render(importerHomePage, req.attributes())
            } catch (e: Exception) {
                createUserAlert(req, e)
            }

    fun newRfc(req: ServerRequest): ServerResponse =
            try {
                val map = commonDaoServices.serviceMapDetails(appId)
                req.paramOrNull("rfcTypeID")
                        ?.let { rfcTypeID ->
                            daoServices.findRfcTypeWithID(rfcTypeID.toLong())
                                    .let { rfcType ->
                                        req.attributes().putAll(loadRfcUIComponents())
                                        req.attributes()["rfcType"] = rfcType
                                        req.attributes()["map"] = map
                                        return ok().render(rfcNewPage, req.attributes())
                                    }
                        }
                        ?: throw ExpectedDataNotFound("Missing rfcTypeID ID = ${req.paramOrNull("rfcTypeID")}, recheck configuration")
            } catch (e: Exception) {
                createUserAlert(req, e)
            }


    fun newDiApplication(req: ServerRequest): ServerResponse =
            try {
                val map = commonDaoServices.serviceMapDetails(appId)
                req.paramOrNull("diApplicationTypeUuid")
                        ?.let { diApplicationTypeUuid ->
                            daoServices.findDiApplicationTypeWithUuid(diApplicationTypeUuid)
                                    .let { diApplicationType ->
                                        req.attributes().putAll(loadDiApplicationCommonUIComponents(map))
                                        req.attributes().putAll(loadCommonUIComponents())
                                        req.attributes()["diApplicationType"] = diApplicationType
                                        req.attributes()["map"] = map
                                        return ok().render(diApplicationNewPage, req.attributes())
                                    }
                        }
                        ?: throw ExpectedDataNotFound("Missing diApplicationTypeUuid with ID = ${req.paramOrNull("diApplicationTypeUuid")}, recheck configuration")

            } catch (e: Exception) {
                createUserAlert(req, e)
            }


    fun rfcList(req: ServerRequest): ServerResponse =
            try {
                val map = commonDaoServices.serviceMapDetails(appId)
                val usersEntity = commonDaoServices.loggedInUserDetails()
                req.paramOrNull("rfcTypeID")
                        ?.let { rfcTypeID ->
                            daoServices.findRfcTypeWithID(rfcTypeID.toLong())
                                    .let { rfcType ->
                                        req.attributes().putAll(loadRfcCommonUIComponents())
                                        req.attributes()["rfcType"] = rfcType
                                        req.attributes()["rfcLists"] = daoServices.findRfcListWithTypeIDAndImporterID(usersEntity, rfcType, map.activeStatus)
                                        req.attributes()["map"] = map
                                        return ok().render(rfcPageList, req.attributes())
                                    }

                        }
                        ?: throw ExpectedDataNotFound("Missing rfcTypeID ID = ${req.paramOrNull("rfcTypeID")}, recheck configuration")
            } catch (e: Exception) {
                createUserAlert(req, e)
            }


    fun rfcDetails(req: ServerRequest): ServerResponse =
            try {
                val map = commonDaoServices.serviceMapDetails(appId)
                req.paramOrNull("rfcID")
                        ?.let { rfcID ->
                            daoServices.findRFCDetailsWithID(rfcID.toLong())
                                    .let { rfcDetails ->
                                        when (rfcDetails.rfcTypeId?.id) {
                                            daoServices.rfcCOITypeID.toLong() -> {
                                                req.attributes()["rfcItems"] = daoServices.findRFCCoiItemWithRFCId(rfcDetails, map.activeStatus)
                                            }
                                        }
                                        req.attributes().putAll(loadRfcUIComponents())
                                        req.attributes()["rfcDetails"] = rfcDetails
                                        req.attributes()["map"] = map

                                        return ok().render(rfcPageDetails, req.attributes())
                                    }
                        }
                        ?: throw ExpectedDataNotFound("Missing RFC ID = ${req.paramOrNull("rfcID")}, recheck configuration")

            } catch (e: Exception) {
                createUserAlert(req, e)
            }


    fun finishedAddingItem(req: ServerRequest): ServerResponse =
            try {
                val map = commonDaoServices.serviceMapDetails(appId)
                val usersEntity = commonDaoServices.loggedInUserDetails()
                req.paramOrNull("rfcID")
                        ?.let { rfcID ->
                            daoServices.rfcUpdatesSave(daoServices.findRFCDetailsWithID(rfcID.toLong()), usersEntity, map)
                            return ok().render("$rfcPageDetailsRedirect=$rfcID")
                        }
                        ?: throw ExpectedDataNotFound("Missing RFC ID = ${req.paramOrNull("rfcID")}, recheck configuration")

            } catch (e: Exception) {
                createUserAlert(req, e)
            }


    fun diApplicationList(req: ServerRequest): ServerResponse =
            try {
                val map = commonDaoServices.serviceMapDetails(appId)
                val usersEntity = commonDaoServices.loggedInUserDetails()
                val auth = commonDaoServices.loggedInUserAuthentication()
                req.paramOrNull("diApplicationTypeUuid")
                        ?.let { diApplicationTypeUuid ->
                            daoServices.findDiApplicationTypeWithUuid(diApplicationTypeUuid)
                                    .let { diApplicationType ->
                                        req.attributes().putAll(loadDiApplicationCommonUIComponents(map))
                                        req.attributes().putAll(loadCommonUIComponents())
                                        req.attributes()["diApplicationType"] = diApplicationType
                                        when {
                                            auth.authorities.stream().anyMatch { authority -> authority.authority == "IMPORTER" } -> {
                                                when (diApplicationType.uuid) {
                                                    applicationMapProperties.mapDIApplicationCSApprovalTypeUuid -> {
                                                        req.attributes()["diApplicationLists"] = daoServices.findCsApprovalApplicationsWithImporterID(usersEntity)
                                                    }
                                                    applicationMapProperties.mapDIApplicationTemporaryImportTypeUuid -> {
                                                        req.attributes()["diApplicationLists"] = daoServices.findTemporaryImportApplicationsWithImporterID(usersEntity)
                                                    }
                                                }

                                                req.attributes()["map"] = map
                                                return ok().render(diApplicationPageList, req.attributes())

                                            }
                                            auth.authorities.stream().anyMatch { authority -> authority.authority == "DI_MANAGER_INSPECTION_READ" } -> {
                                                when (diApplicationType.uuid) {
                                                    applicationMapProperties.mapDIApplicationCSApprovalTypeUuid -> {
                                                        req.attributes()["diApplicationLists"] = commonDaoServices.findUserProfileByUserID(usersEntity, map.activeStatus).sectionId?.let { daoServices.findListOfAllCSApprovalWithEntryPoint(it) }
                                                    }
                                                    applicationMapProperties.mapDIApplicationTemporaryImportTypeUuid -> {
                                                        req.attributes()["diApplicationLists"] = commonDaoServices.findUserProfileByUserID(usersEntity, map.activeStatus).sectionId?.let { daoServices.findListOfAllTemporaryImportWithEntryPoint(it) }
                                                    }
                                                }

                                                req.attributes()["map"] = map
                                                return ok().render(diApplicationPageList, req.attributes())

                                            }
                                            else -> throw SupervisorNotFoundException("can't access this page Due to Invalid authority")
                                        }
                                    }

                        }
                        ?: throw ExpectedDataNotFound("Missing diApplicationTypeID ID = ${req.paramOrNull("diApplicationTypeID")}, recheck configuration")

            } catch (e: Exception) {
                createUserAlert(req, e)
            }


    fun diApplicationDetails(req: ServerRequest): ServerResponse =
            try {
                val map = commonDaoServices.serviceMapDetails(appId)
                req.paramOrNull("diApplicationTypeUuid")
                        ?.let { diApplicationTypeUuid ->
                            req.paramOrNull("diApplicationUuid")
                                    ?.let { diApplicationUuid ->
                                        val usersEntity = commonDaoServices.loggedInUserDetails()
//                                                req.paramOrNull("message")
//                                                        ?.let { message ->
                                        daoServices.findDiApplicationTypeWithUuid(diApplicationTypeUuid)
                                                .let { diApplicationType ->
                                                    var fileParameters: Any? = null
                                                    var diApplicationDetails: Any? = null
                                                    var cdDetails: ConsignmentDocumentDetailsEntity? = null
                                                    when (diApplicationType.uuid) {
                                                        applicationMapProperties.mapDIApplicationCSApprovalTypeUuid -> {
                                                            daoServices.findCSApprovalWithUuid(diApplicationUuid).let { csApprovalApplicationsEntity ->
                                                                diApplicationDetails = csApprovalApplicationsEntity
                                                                cdDetails = csApprovalApplicationsEntity.cdId?.let { diDaoServices.findCD(it) }
                                                                fileParameters = daoServices.findCSApprovalUploadsWithCSApprovalID(csApprovalApplicationsEntity)
                                                            }
                                                        }
                                                        applicationMapProperties.mapDIApplicationTemporaryImportTypeUuid -> {
                                                            daoServices.findTemporaryImportsWithUuid(diApplicationUuid).let { temporaryImportApplicationsEntity ->
                                                                diApplicationDetails = temporaryImportApplicationsEntity
                                                                cdDetails = temporaryImportApplicationsEntity.cdId?.let { diDaoServices.findCD(it) }
                                                                fileParameters = daoServices.findTemporaryImportsUploadsWithTemporaryID(temporaryImportApplicationsEntity)
                                                            }
                                                        }
                                                    }

                                                    req.attributes().putAll(loadDiApplicationCommonUIComponents(map))
                                                    req.attributes().putAll(commonDaoServices.loadCommonUIComponents(map))
                                                    //Load inspection Officers using the following roles
                                                    SecurityContextHolder.getContext().authentication
                                                            ?.let { auth ->
                                                                when {
                                                                    auth.authorities.stream().anyMatch { authority -> authority.authority == "DI_MANAGER_INSPECTION_READ" } -> {
                                                                        req.attributes()["officers"] = commonDaoServices.findUserProfileByUserID(usersEntity, map.activeStatus).sectionId?.let { commonDaoServices.findAllUsersWithSectionIdAndDesignation(it, commonDaoServices.findDesignationByID(daoServices.diDesignationInspectionOfficerId.toLong()), map.activeStatus) }
                                                                    }
                                                                }
                                                            }
                                                            ?: throw Exception("Missing [username=${SecurityContextHolder.getContext().authentication?.name}, does not exist")

                                                    req.attributes()["diApplicationType"] = diApplicationType
                                                    req.attributes()["diApplicationDetails"] = diApplicationDetails
                                                    req.attributes()["fileParameters"] = fileParameters
                                                    req.attributes()["cdUuid"] = cdDetails?.uuid
                                                    req.attributes()["map"] = map
                                                    ok().render(diApplicationDetailsPage, req.attributes())
                                                }
//                                                        }
//                                                        ?: throw ExpectedDataNotFound("Required message value, check config")
                                    }
                                    ?: throw ExpectedDataNotFound("Missing diApplication UUid = ${req.paramOrNull("diApplicationUuid")}, recheck configuration")
                        }
                        ?: throw ExpectedDataNotFound("Missing diApplicationTypeID Uuid = ${req.paramOrNull("diApplicationTypeUuid")}, recheck configuration")

            } catch (e: Exception) {
                createUserAlert(req, e)
            }

    fun diCsApprovalDetails(req: ServerRequest): ServerResponse =
            try {
                val map = commonDaoServices.serviceMapDetails(appId)
                req.paramOrNull("ucrNumber")?.let { ucrNumber ->
                    val usersEntity = commonDaoServices.loggedInUserDetails()
                    var fileParameters: Any? = null
                    var diApplicationDetails: Any? = null
                    var cdDetails: ConsignmentDocumentDetailsEntity? = null
                    daoServices.findCSApprovalWithUcrNumber(ucrNumber)?.let { csApprovalApplicationsEntity ->
                        diApplicationDetails = csApprovalApplicationsEntity
                        cdDetails = csApprovalApplicationsEntity.cdId?.let { diDaoServices.findCD(it) }
                        fileParameters = daoServices.findCSApprovalUploadsWithCSApprovalID(csApprovalApplicationsEntity)
                    }

                    req.attributes().putAll(loadDiApplicationCommonUIComponents(map))
                    req.attributes().putAll(commonDaoServices.loadCommonUIComponents(map))

                    req.attributes()["diApplicationType"] = daoServices.findDiApplicationTypeWithID(applicationMapProperties.mapDIApplicationCSApprovalTypeUuid.toLong())
                    req.attributes()["diApplicationDetails"] = diApplicationDetails
                    req.attributes()["fileParameters"] = fileParameters
                    req.attributes()["cdUuid"] = cdDetails?.uuid
                    req.attributes()["map"] = map
                    ok().render(diApplicationDetailsPage, req.attributes())
//                                                        }
//                                                        ?: throw ExpectedDataNotFound("Required message value, check config")
                }
                        ?: throw ExpectedDataNotFound("Missing diApplication UUid = ${req.paramOrNull("diApplicationUuid")}, recheck configuration")

            } catch (e: Exception) {
                createUserAlert(req, e)
            }


    private fun loadRfcCommonUIComponents(): MutableMap<String, Any> {
        return mutableMapOf(
                Pair("rfcCOCTypeID", daoServices.rfcCOCTypeID.toLong()),
                Pair("rfcCOITypeID", daoServices.rfcCOITypeID.toLong()),
                Pair("rfcCORTypeID", daoServices.rfcCORTypeID.toLong())
        )
    }

    private fun loadCommonUIComponents(): MutableMap<String, Any> {
        return mutableMapOf(
                Pair("viewPage", commonDaoServices.viewPage)
        )
    }

    private fun loadDiApplicationCommonUIComponents(s: ServiceMapsEntity): MutableMap<String, Any> {
        return mutableMapOf(
                Pair("diApplicationCsApprovalTypeUuid", applicationMapProperties.mapDIApplicationCSApprovalTypeUuid),
                Pair("diApplicationTemporaryImportTypeUuid", applicationMapProperties.mapDIApplicationTemporaryImportTypeUuid),
                Pair("entryPointDetails", commonDaoServices.findAllSectionsListWithDivision(commonDaoServices.findDivisionWIthId(commonDaoServices.pointOfEntries.toLong()), s.activeStatus)),
                Pair("csApprovalEntity", CsApprovalApplicationsEntity()),
                Pair("csApprovalUploadEntity", CsApprovalApplicationsUploadsEntity()),
                Pair("temporaryImportEntity", TemporaryImportApplicationsEntity()),
                // Todo  : method for users to be assigned to that application
//                Pair("officers", commonDaoServices.findAllUsersWithSectionId()),
                Pair("temporaryImportsUploadEntity", TemporaryImportApplicationsUploadsEntity())
        )
    }

    private fun loadRfcUIComponents(): MutableMap<String, Any> {

        return mutableMapOf(
                Pair("country", commonDaoServices.findCountryList()),
                Pair("rfcCOCTypeID", daoServices.rfcCOCTypeID.toLong()),
                Pair("rfcCOITypeID", daoServices.rfcCOITypeID.toLong()),
                Pair("rfcCORTypeID", daoServices.rfcCORTypeID.toLong()),
                Pair("rfcDocumentDetailsEntity", RfcDocumentsDetailsEntity()),
                Pair("rfcCoiItemEntity", RfcCoiItemsDetailsEntity())
        )
    }


}
