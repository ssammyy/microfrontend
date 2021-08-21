package org.kebs.app.kotlin.apollo.api.handlers

import mu.KotlinLogging
import okhttp3.internal.toLongOrDefault
import org.kebs.app.kotlin.apollo.api.payload.ApiResponseModel
import org.kebs.app.kotlin.apollo.api.payload.ConsignmentDocumentDao
import org.kebs.app.kotlin.apollo.api.payload.ResponseCodes
import org.kebs.app.kotlin.apollo.api.payload.extractPage
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DestinationInspectionDaoServices
import org.kebs.app.kotlin.apollo.api.service.DestinationInspectionService
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.di.ConsignmentDocumentTypesEntity
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartHttpServletRequest
import org.springframework.web.server.ResponseStatusException
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

    fun uploadForeignConsignmentDocument(req: ServerRequest): ServerResponse {
        val multipartRequest = (req.servletRequest() as? MultipartHttpServletRequest)
                ?: throw ResponseStatusException(
                        HttpStatus.NOT_ACCEPTABLE,
                        "Request is not a multipart request"
                )
        val multipartFile = multipartRequest.getFile("file")
        // TODO: implement upload here
        val fileType = multipartRequest.getAttribute("file_type")
        val response = ApiResponseModel()
        response.data = fileType
        response.message = "Not Implemented"
        response.responseCode = ResponseCodes.NOT_IMPLEMENTED
        return ServerResponse.ok().body(response)
    }

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
            val page = extractPage(req)
            return ServerResponse.ok()
                    .body(this.destinationInspectionService.listMinistryInspection(status >= 1, page))
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

    fun consignmentDocumentConfiguration(req: ServerRequest): ServerResponse {
        val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
        val response = ApiResponseModel()
        response.data = map
        response.responseCode = ResponseCodes.SUCCESS_CODE
        response.message = "Success"
        return ServerResponse.ok().body(response)
    }

    @PreAuthorize("hasAuthority('DI_INSPECTION_OFFICER_READ')")
    fun assignedConsignmentDocuments(req: ServerRequest): ServerResponse {
        val auth = commonDaoServices.loggedInUserAuthentication()
        val response = ApiResponseModel()
        try {
            req.paramOrNull("cdTypeUuid").let { cdTypeUuid ->
                var cdType: ConsignmentDocumentTypesEntity? = null
                if (cdTypeUuid != null) {
                    cdType = daoServices.findCdTypeDetailsWithUuid(cdTypeUuid)
                }
                val usersEntity = commonDaoServices.findUserByUserName(auth.name)
                val data = daoServices.findAllCdWithAssignedIoID(usersEntity, cdType, extractPage(req))
                response.data = ConsignmentDocumentDao.fromList(data.toList())
                response.pageNo = data.number
                response.totalPages = data.totalPages
                response.message = "Assigned CD"
                response.responseCode = ResponseCodes.SUCCESS_CODE
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error { ex }
            response.message = "Consignment type not found"
            response.responseCode = ResponseCodes.NOT_FOUND
        }
        return ServerResponse.ok().body(response)
    }

    @PreAuthorize("hasAuthority('DI_INSPECTION_OFFICER_READ') || hasAuthority('DI_OFFICER_CHARGE_READ')")
    fun manualAssignedConsignmentDocuments(req: ServerRequest): ServerResponse {
        val auth = commonDaoServices.loggedInUserAuthentication()
        val response = ApiResponseModel()
        val page = extractPage(req)
        req.paramOrNull("cdTypeUuid").let { cdTypeUuid ->
            try {
                var cdType: ConsignmentDocumentTypesEntity? = null
                if (cdTypeUuid != null) {
                    cdType = daoServices.findCdTypeDetailsWithUuid(cdTypeUuid)
                }
                when {
                    auth.authorities.stream().anyMatch { authority -> authority.authority == "DI_OFFICER_CHARGE_READ" } -> {
                        val pp = daoServices.findAllCdWithNoPortOfEntry(cdType, page)
                        response.data = ConsignmentDocumentDao.fromList(pp.toList())
                        response.totalPages = pp.totalPages
                        response.pageNo = pp.number
                        response.responseCode = ResponseCodes.SUCCESS_CODE
                        response.message = "Success"
                    }
                    auth.authorities.stream().anyMatch { authority -> authority.authority == "DI_INSPECTION_OFFICER_READ" } -> {
                        val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
                        val usersEntity = commonDaoServices.findUserByUserName(auth.name)
                        val userProfilesEntity = commonDaoServices.findUserProfileByUserID(usersEntity, map.activeStatus)
                        userProfilesEntity.subSectionL2Id?.let { section ->
                            val pp = daoServices.findAllCdWithNoAssignedIoID(section, cdType, page)
                            response.data = ConsignmentDocumentDao.fromList(pp.toList())
                            response.pageNo = pp.number
                            response.totalPages = pp.totalPages
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
                response.message = "Consignment type not found"
                response.responseCode = ResponseCodes.NOT_FOUND
            }
        }
        return ServerResponse.ok().body(response)
    }

    @PreAuthorize("hasAuthority('DI_INSPECTION_OFFICER_READ')|| hasAuthority('DI_OFFICER_CHARGE_READ')")
    fun completedConsignmentDocuments(req: ServerRequest): ServerResponse {
        val auth = commonDaoServices.loggedInUserAuthentication()
        val response = ApiResponseModel()
        val page = extractPage(req)
        try {
            req.paramOrNull("cdTypeUuid").let { cdTypeUuid ->
                var cdType: ConsignmentDocumentTypesEntity? = null
                if (cdTypeUuid != null) {
                    cdType = daoServices.findCdTypeDetailsWithUuid(cdTypeUuid)
                }
                val usersEntity = commonDaoServices.findUserByUserName(auth.name)
                when {
                    auth.authorities.stream().anyMatch { authority -> authority.authority == "DI_OFFICER_CHARGE_READ" } -> {
                        val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
                        val userProfilesEntity = commonDaoServices.findUserProfileByUserID(usersEntity, map.activeStatus)
                        userProfilesEntity.sectionId?.let { section ->
                            val pp = daoServices.findAllCompleteCdWithPortOfEntry(section, cdType, page)
                            response.data = ConsignmentDocumentDao.fromList(pp.toList())
                            response.totalPages = pp.totalPages
                            response.pageNo = pp.number
                            response.responseCode = ResponseCodes.SUCCESS_CODE
                            response.message = "Success"
                        } ?: run {
                            response.responseCode = ResponseCodes.NOT_FOUND
                            response.message = "User profile not allowed"
                        }
                    }
                    auth.authorities.stream().anyMatch { authority -> authority.authority == "DI_INSPECTION_OFFICER_READ" } -> {
                        val pp = daoServices.findAllCompleteCdWithAssignedIoID(usersEntity, cdType, page)
                        response.data = ConsignmentDocumentDao.fromList(pp.toList())
                        response.pageNo = pp.number
                        response.totalPages = pp.totalPages
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
    fun outgoingConsignmentDocuments(req: ServerRequest): ServerResponse {
        val auth = commonDaoServices.loggedInUserAuthentication()
        val response = ApiResponseModel()
        val page = extractPage(req)
        req.paramOrNull("cdTypeUuid").let { cdTypeUuid ->
            try {
                var cdType: ConsignmentDocumentTypesEntity? = null
                cdTypeUuid?.let { cdTypeUuidTmp ->
                    cdType = daoServices.findCdTypeDetailsWithUuid(cdTypeUuidTmp)
                }
                val usersEntity = commonDaoServices.findUserByUserName(auth.name)
                val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
                val userProfilesEntity = commonDaoServices.findUserProfileByUserID(usersEntity, map.activeStatus)
                when {
                    auth.authorities.stream().anyMatch { authority -> authority.authority == "DI_OFFICER_CHARGE_READ" } -> {
                        userProfilesEntity.sectionId?.let { section ->
                            val pp = daoServices.findAllOngoingCdWithPortOfEntry(section, cdType, page)
                            response.data = ConsignmentDocumentDao.fromList(pp.toList())
                            response.totalPages = pp.totalPages
                            response.pageNo = pp.number
                            response.message = "Success"
                            response.responseCode = ResponseCodes.SUCCESS_CODE
                        } ?: run {
                            response.message = "Assigned CD not applicable to this profile"
                            response.responseCode = ResponseCodes.NOT_FOUND
                        }
                    }
                    auth.authorities.stream().anyMatch { authority -> authority.authority == "DI_INSPECTION_OFFICER_READ" } -> {
                        userProfilesEntity.sectionId?.let { section ->
                            val pp = daoServices.findAllOngoingCdWithPortOfEntry(section, cdType, page)
                            response.data = ConsignmentDocumentDao.fromList(pp.toList())
                            response.totalPages = pp.totalPages
                            response.pageNo = pp.number
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
                response.message = "Consignment type not found"
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

    fun motorVehicleInspection(req: ServerRequest): ServerResponse {
        var response = ApiResponseModel()
        req.pathVariable("itemId").let { itemId ->
            val imId = itemId.toLongOrDefault(0)
            if (imId > 0) {
                response = this.destinationInspectionService.motorVehicleInspectionDetails(imId)
            } else {
                response.message = "Invalid item identifier"
                response.responseCode = ResponseCodes.FAILED_CODE
            }
        }
        return ServerResponse.ok().body(response)
    }
}