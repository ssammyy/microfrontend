package org.kebs.app.kotlin.apollo.api.handlers

import mu.KotlinLogging
import okhttp3.internal.toLongOrDefault
import org.kebs.app.kotlin.apollo.api.payload.ApiResponseModel
import org.kebs.app.kotlin.apollo.api.payload.ResponseCodes
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DestinationInspectionDaoServices
import org.kebs.app.kotlin.apollo.api.service.DestinationInspectionService
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.paramOrNull

@Component
class ApiDestinationInspectionHandler(
        private val destinationInspectionService: DestinationInspectionService,
        private val daoServices: DestinationInspectionDaoServices,
        private val commonDaoServices: CommonDaoServices,
        private val applicationMapProperties: ApplicationMapProperties
) {

    fun listConsignmentDocumentTypes(req: ServerRequest): ServerResponse {
        return ServerResponse.ok()
                .body(this.destinationInspectionService.loadCDTypes())
    }

    fun listApplicationTypes(req: ServerRequest): ServerResponse {
        return ServerResponse.ok()
                .body(this.destinationInspectionService.applicationTypes())
    }

    fun ministryInspections(req: ServerRequest): ServerResponse {
        req.pathVariable("inspectionStatus").let { taskId ->
            val status = taskId.toLongOrDefault(0)
            return ServerResponse.ok()
                    .body(this.destinationInspectionService.listMinistryInspection(status >= 1))
        }
    }

    fun consignmentDocumentDetails(req: ServerRequest): ServerResponse {
        req.pathVariable("coUuid").let {
            return ServerResponse.ok().body(this.destinationInspectionService.consignmentDocumentDetails(it))
        }
    }

    fun consignmentDocumentItemDetails(req: ServerRequest): ServerResponse {
        req.pathVariable("coItemUuid").let {
            return ServerResponse.ok().body(this.destinationInspectionService.consignmentDocumentItemDetails(it))
        }
    }

    fun consignmentDocumentInvoices(req: ServerRequest): ServerResponse {
        req.pathVariable("coUuid").let {
            return ServerResponse.ok().body(this.destinationInspectionService.consignmentDocumentInvoiceDetails(it))
        }
    }

    @PreAuthorize("hasAuthority('DI_INSPECTION_OFFICER_READ')")
    fun assignedConsignmentDocuments(req: ServerRequest): ServerResponse {
        val auth = commonDaoServices.loggedInUserAuthentication()
        val response = ApiResponseModel()
        try {
            req.pathVariable("cdTypeUuid").let { cdTypeUuid ->
                val cdType = daoServices.findCdTypeDetailsWithUuid(cdTypeUuid)
                val usersEntity = commonDaoServices.findUserByUserName(auth.name)
                response.data = daoServices.findAllCdWithAssignedIoID(usersEntity, cdType)
                response.message = "Assigned CD"
                response.responseCode = ResponseCodes.SUCCESS_CODE
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error { ex }
            response.message = "Condignment type not found"
            response.responseCode = ResponseCodes.NOT_FOUND
        }
        return ServerResponse.ok().body(response)
    }

    @PreAuthorize("hasAuthority('DI_INSPECTION_OFFICER_READ') || hasAuthority('DI_OFFICER_CHARGE_READ')")
    fun manualAssignedConsignmentDocuments(req: ServerRequest): ServerResponse {
        val auth = commonDaoServices.loggedInUserAuthentication()
        val response = ApiResponseModel()
        req.pathVariable("cdTypeUuid").let { cdTypeUuid ->
            try {
                val cdType = daoServices.findCdTypeDetailsWithUuid(cdTypeUuid)
                when {
                    auth.authorities.stream().anyMatch { authority -> authority.authority == "DI_OFFICER_CHARGE_READ" } -> {
                        response.data = daoServices.findAllCdWithNoPortOfEntry(cdType)
                        response.responseCode = ResponseCodes.SUCCESS_CODE
                        response.message = "Success"
                    }
                    auth.authorities.stream().anyMatch { authority -> authority.authority == "DI_INSPECTION_OFFICER_READ" } -> {
                        val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
                        val usersEntity = commonDaoServices.findUserByUserName(auth.name)
                        val userProfilesEntity = commonDaoServices.findUserProfileByUserID(usersEntity, map.activeStatus)
                        userProfilesEntity.subSectionL2Id?.let { section ->
                            response.data = daoServices.findAllCdWithNoAssignedIoID(section, cdType)
                            response.message = "Manual Assigned CD"
                            response.responseCode = ResponseCodes.SUCCESS_CODE
                        } ?: run {
                            response.message = "Manual Assigned CD not applicable to this profile"
                            response.responseCode = ResponseCodes.NOT_FOUND
                        }
                    }
                }
            } catch (ex: Exception) {
                KotlinLogging.logger { }.error { ex }
                response.message = "Condignment type not found"
                response.responseCode = ResponseCodes.NOT_FOUND
            }
        }
        return ServerResponse.ok().body(response)
    }

    @PreAuthorize("hasAuthority('DI_INSPECTION_OFFICER_READ')|| hasAuthority('DI_OFFICER_CHARGE_READ')")
    fun completedConsignmentDocuments(req: ServerRequest): ServerResponse {
        val auth = commonDaoServices.loggedInUserAuthentication()
        val response = ApiResponseModel()
        try {
            req.pathVariable("cdTypeUuid").let { cdTypeUuid ->
                val cdType = daoServices.findCdTypeDetailsWithUuid(cdTypeUuid)
                val usersEntity = commonDaoServices.findUserByUserName(auth.name)
                when {
                    auth.authorities.stream().anyMatch { authority -> authority.authority == "DI_OFFICER_CHARGE_READ" } -> {
                        val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
                        val userProfilesEntity = commonDaoServices.findUserProfileByUserID(usersEntity, map.activeStatus)
                        userProfilesEntity.sectionId?.let { section ->
                            response.data = daoServices.findAllCompleteCdWithPortOfEntry(section, cdType)
                            response.responseCode = ResponseCodes.SUCCESS_CODE
                            response.message = "Success"
                        } ?: run {
                            response.responseCode = ResponseCodes.NOT_FOUND
                            response.message = "User profile not allowed"
                        }
                    }
                    auth.authorities.stream().anyMatch { authority -> authority.authority == "DI_INSPECTION_OFFICER_READ" } -> {
                        response.data = daoServices.findAllCompleteCdWithAssignedIoID(usersEntity, cdType)
                        response.message = "Completed CD"
                        response.responseCode = ResponseCodes.SUCCESS_CODE
                    }
                }
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error { ex }
            response.message = "Consignment type not found"
            response.responseCode = ResponseCodes.NOT_FOUND
        }
        return ServerResponse.ok().body(response)
    }

    @PreAuthorize("hasAuthority('DI_OFFICER_CHARGE_READ')|| hasAuthority('DI_OFFICER_CHARGE_READ')")
    fun autoAssignedConsignmentDocuments(req: ServerRequest): ServerResponse {
        val auth = commonDaoServices.loggedInUserAuthentication()
        val response = ApiResponseModel()
        req.pathVariable("cdTypeUuid").let { cdTypeUuid ->
            try {
                val cdType = daoServices.findCdTypeDetailsWithUuid(cdTypeUuid)
                val usersEntity = commonDaoServices.findUserByUserName(auth.name)
                val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
                val userProfilesEntity = commonDaoServices.findUserProfileByUserID(usersEntity, map.activeStatus)
                when {
                    auth.authorities.stream().anyMatch { authority -> authority.authority == "DI_OFFICER_CHARGE_READ" } -> {
                        userProfilesEntity.sectionId?.let { section ->
                            response.data = daoServices.findAllOngoingCdWithPortOfEntry(section, cdType)
                            response.message = "Success"
                            response.responseCode = ResponseCodes.SUCCESS_CODE
                        } ?: run {
                            response.message = "Assigned CD not applicable to this profile"
                            response.responseCode = ResponseCodes.NOT_FOUND
                        }
                    }
                    auth.authorities.stream().anyMatch { authority -> authority.authority == "DI_INSPECTION_OFFICER_READ" } -> {
                        userProfilesEntity.sectionId?.let { section ->
                            response.data = daoServices.findAllOngoingCdWithPortOfEntry(section, cdType)
                            response.message = "Auto Assigned CD at section"
                            response.responseCode = ResponseCodes.SUCCESS_CODE
                        } ?: run {
                            response.message = "Assigned CD not applicable to this profile"
                            response.responseCode = ResponseCodes.NOT_FOUND
                        }
                    }
                }
            } catch (ex: Exception) {
                KotlinLogging.logger { }.error { ex }
                response.message = "Condignment type not found"
                response.responseCode = ResponseCodes.NOT_FOUND
            }
        }
        return ServerResponse.ok().body(response)
    }

    fun certificateOfConformance(req: ServerRequest): ServerResponse {
        req.pathVariable("coUuid").let {
            return ServerResponse.ok().body(this.destinationInspectionService.certificateOfConformanceDetails(it))
        }
    }

    fun importDeclarationFormDetails(req: ServerRequest): ServerResponse {
        req.pathVariable("coUuid").let {
            return ServerResponse.ok().body(this.destinationInspectionService.importDeclarationFormDetails(it))
        }
    }

    fun certificateOfRoadWorthines(req: ServerRequest): ServerResponse {
        req.pathVariable("coUuid").let {
            return ServerResponse.ok().body(this.destinationInspectionService.certificateOfRoadWorthinessDetails(it))
        }
    }

    fun consignmentDocumentManifest(req: ServerRequest): ServerResponse {
        req.pathVariable("coUuid").let {
            return ServerResponse.ok().body(this.destinationInspectionService.consignmentDocumentManifestDetails(it))
        }
    }

    fun consignmentDocumentCustomsDeclaration(req: ServerRequest): ServerResponse {
        req.pathVariable("coUuid").let {
            return ServerResponse.ok().body(this.destinationInspectionService.cdCustomDeclarationDetails(it))
        }
    }

    fun consignmentDocumentChecklist(req: ServerRequest): ServerResponse {
        req.pathVariable("cdItemUuid").let {
            return ServerResponse.ok().body(this.destinationInspectionService.inspectionChecklistReportDetails(it))
        }
    }

    fun consignmentDocumentSSFLabDetails(req: ServerRequest): ServerResponse {
        req.pathVariable("cdItemUuid").let {
            return ServerResponse.ok().body(this.destinationInspectionService.getSSfDetails(it.toLongOrDefault(0)))
        }
    }

}