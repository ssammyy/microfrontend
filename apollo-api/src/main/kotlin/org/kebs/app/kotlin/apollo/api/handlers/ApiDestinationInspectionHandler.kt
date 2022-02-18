package org.kebs.app.kotlin.apollo.api.handlers

import mu.KotlinLogging
import okhttp3.internal.toLongOrDefault
import org.kebs.app.kotlin.apollo.api.payload.*
import org.kebs.app.kotlin.apollo.api.payload.request.SearchForms
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DestinationInspectionDaoServices
import org.kebs.app.kotlin.apollo.api.service.ConsignmentApprovalStatus
import org.kebs.app.kotlin.apollo.api.service.ConsignmentDocumentAuditService
import org.kebs.app.kotlin.apollo.api.service.DestinationInspectionService
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.ServiceMapsEntity
import org.kebs.app.kotlin.apollo.store.model.di.ConsignmentDocumentDetailsEntity
import org.kebs.app.kotlin.apollo.store.model.di.ConsignmentDocumentTypesEntity
import org.kebs.app.kotlin.apollo.store.model.di.DiUploadsEntity
import org.kebs.app.kotlin.apollo.store.repo.IUserRoleAssignmentsRepository
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartHttpServletRequest
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.paramOrNull

@Service
class ApiDestinationInspectionHandler(
        private val destinationInspectionService: DestinationInspectionService,
        private val daoServices: DestinationInspectionDaoServices,
        private val commonDaoServices: CommonDaoServices,
        private val roleAssignmentsRepository: IUserRoleAssignmentsRepository,
        private val consignmentAuditService: ConsignmentDocumentAuditService,
        private val applicationMapProperties: ApplicationMapProperties
) {
    fun consignmentDocumentSupervisorTasks(req: ServerRequest): ServerResponse {
        val cdUuid = req.pathVariable("cdUuid")
        return ServerResponse.ok().body(this.destinationInspectionService.consignmentDocumentTasks(cdUuid))
    }

    fun listBlackListedUser(req: ServerRequest): ServerResponse {
        val response = ApiResponseModel()
        try {
            // Load active blacklisted users
            commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
                    .let { map ->
                        response.data = daoServices.findAllBlackListUsers(map.activeStatus)
                    }
            response.message = "Success"
            response.responseCode = ResponseCodes.SUCCESS_CODE
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error { ex }
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Failed to list blacklisted users"
        }
        return ServerResponse.ok().body(response)
    }

    fun listPortFreightStations(req: ServerRequest): ServerResponse {
        val response = ApiResponseModel()
        try {
            // Load active ports
            val portId = req.pathVariable("portId").toLongOrDefault(0L)
            val section = commonDaoServices.findSectionWIthId(portId)

            commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
                    .let { map ->
                        response.data = SectionL2EntityDao.fromList(commonDaoServices.findAllFreightStationOnPortOfArrival(section, map.activeStatus))
                    }
            response.message = "Success"
            response.responseCode = ResponseCodes.SUCCESS_CODE
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error { ex }
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Failed to list ports"
        }
        return ServerResponse.ok().body(response)
    }

    fun loadPortOfArrival(req: ServerRequest): ServerResponse {
        val response = ApiResponseModel()
        try {
            // Load active ports
            commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
                    .let { map ->
                        val ports = commonDaoServices.findAllSectionsListWithDivision(
                                commonDaoServices.findDivisionWIthId(commonDaoServices.pointOfEntries.toLong()),
                                map.activeStatus
                        )
                        response.data = SectionEntityDao.fromList(ports)
                    }
            response.message = "Success"
            response.responseCode = ResponseCodes.SUCCESS_CODE
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error { ex }
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Failed to list ports"
        }
        return ServerResponse.ok().body(response)
    }

    fun loadCommonUIComponents(req: ServerRequest): ServerResponse {
        val s: ServiceMapsEntity = this.commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
        val response = ApiResponseModel()
        response.data = mutableMapOf(
                Pair("activeStatus", s.activeStatus),
                Pair("inActiveStatus", s.inactiveStatus),
                Pair("initStatus", s.initStatus),
                Pair("currentDate", commonDaoServices.getCurrentDate()),
                Pair("CDStatusTypes", daoServices.findCdManualAssignable(s.activeStatus)),
                Pair("cdTypeGoodsCategory", daoServices.cdTypeGoodsCategory),
                Pair("cdTypeVehiclesCategory", daoServices.cdTypeVehiclesCategory)
        )
        response.responseCode = ResponseCodes.SUCCESS_CODE
        response.message = "Success"
        return ServerResponse.ok()
                .body(response)
    }

    fun uploadForeignConsignmentDocument(req: ServerRequest): ServerResponse {
        val multipartRequest = (req.servletRequest() as? MultipartHttpServletRequest)
                ?: throw ResponseStatusException(
                        HttpStatus.NOT_ACCEPTABLE,
                        "Request is not a multipart request"
                )
        val multipartFile = multipartRequest.getFile("file")
        val fileType = multipartRequest.getParameter("file_type")
        val docType = multipartRequest.getParameter("docType")
        val partnerId = multipartRequest.getParameter("partnerId").toLongOrNull()
        val response = ApiResponseModel()
        if (multipartFile != null) {
            commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
                    .let { map ->
                        commonDaoServices.loggedInUserDetails()
                                .let { loggedInUser ->
                                    try {
                                        val uploads = DiUploadsEntity()
                                        uploads.documentType = docType.toUpperCase()
                                        uploads.fileType = fileType
                                        this.destinationInspectionService.saveUploadedCsvFileAndSendToKeSWS(multipartFile, uploads, loggedInUser, map, partnerId)
                                        response.data = fileType
                                        response.message = "${uploads.documentType?.toUpperCase()} successfully uploaded"
                                        response.responseCode = ResponseCodes.SUCCESS_CODE
                                    } catch (e: ExpectedDataNotFound) {
                                        KotlinLogging.logger { }.error("FAILED TO UPLOAD COCs", e)
                                        response.responseCode = ResponseCodes.FAILED_CODE
                                        response.message = e.localizedMessage
                                    } catch (e: Exception) {
                                        KotlinLogging.logger { }.error("FAILED TO UPLOAD COCs", e)
                                        response.responseCode = ResponseCodes.FAILED_CODE
                                        response.message = "Upload failed, please check document format and try again"
                                    }
                                }
                    }
        } else {
            response.message = "Please select upload file"
            response.responseCode = ResponseCodes.FAILED_CODE
        }
        return ServerResponse.ok().body(response)
    }


    fun listConsignmentDocumentTypes(req: ServerRequest): ServerResponse {
        return ServerResponse.ok()
                .body(this.destinationInspectionService.loadCDTypes())
    }

    fun listMinistryStations(req: ServerRequest): ServerResponse {
        return ServerResponse.ok()
                .body(this.destinationInspectionService.loadMinistryStations())
    }

    fun listApplicationTypes(req: ServerRequest): ServerResponse {
        return ServerResponse.ok()
                .body(this.destinationInspectionService.applicationTypes())
    }


    fun consignmentDocumentDetails(req: ServerRequest): ServerResponse {
        try {
            val auth = commonDaoServices.loggedInUserAuthentication()
            val usersEntity = commonDaoServices.findUserByUserName(auth.name)
            val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
            val supervisorCount = this.roleAssignmentsRepository.checkUserHasRole("DI_Officer_Charge", map.activeStatus, usersEntity.id!!)
            val inspectorCount = this.roleAssignmentsRepository.checkUserHasRole("DI_Inspection_Officers", map.activeStatus, usersEntity.id!!)
            KotlinLogging.logger { }.info("S=${supervisorCount},I=${inspectorCount}")
            req.pathVariable("coUuid").let {
                return ServerResponse.ok().body(this.destinationInspectionService.consignmentDocumentDetails(it, supervisorCount > 0, inspectorCount > 0))
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("ERRO", ex)
        }
        return ServerResponse.badRequest().body("INVALID")
    }

    fun uploadConsignmentDocumentAttachment(req: ServerRequest): ServerResponse {
        val multipartRequest = (req.servletRequest() as? MultipartHttpServletRequest)
        val response = ApiResponseModel()
        try {
            if (multipartRequest != null) {
                val docFile = multipartRequest.getFile("file")
                val comment = multipartRequest.getParameter("description")
                if (docFile != null) {
                    req.pathVariable("cdUuid")
                            .let { cdUuid ->
                                commonDaoServices.loggedInUserDetails()
                                        .let { loggedInUser ->
                                            daoServices.findCDWithUuid(cdUuid)
                                                    .let { cdDetails ->

                                                        daoServices.saveConsignmentAttachment(
                                                                DiUploadsEntity()
                                                                        .apply {
                                                                            description = comment?.toString()
                                                                        },
                                                                docFile,
                                                                "consignment_doc",
                                                                loggedInUser,
                                                                commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection),
                                                                cdDetails
                                                        )
                                                        response.message = "Attachment successfully uploaded"
                                                        response.responseCode = ResponseCodes.SUCCESS_CODE

                                                    }
                                        }
                            }
                } else {
                    response.message = "Please select a file to upload."
                    response.responseCode = ResponseCodes.EXCEPTION_STATUS
                }
            } else {
                response.responseCode = ResponseCodes.FAILED_CODE
                response.message = "Expected file upload request"
            }
        } catch (ex: Exception) {
            response.message = "Please select a file and comment"
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
        }

        return ServerResponse.ok()
                .body(response)
    }

    fun deleteConsignmentDocumentAttachment(req: ServerRequest): ServerResponse {
        req.pathVariable("attachmentId").let {
            return ServerResponse.ok().body(this.destinationInspectionService.deleteConsignmentDocumentAttachments(it.toLongOrDefault(0L)))
        }
    }

    fun consignmentDocumentAttachments(req: ServerRequest): ServerResponse {
        req.pathVariable("coUuid").let {
            return ServerResponse.ok().body(this.destinationInspectionService.consignmentDocumentAttachments(it))
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

    @PreAuthorize("hasAuthority('DI_INSPECTION_OFFICER_READ') || hasAuthority('DI_OFFICER_CHARGE_READ')")
    fun assignedConsignmentDocuments(req: ServerRequest): ServerResponse {
        val auth = commonDaoServices.loggedInUserAuthentication()
        var response = ApiResponseModel()
        try {
            req.paramOrNull("cdTypeUuid").let { cdTypeUuid ->
                var cdType: ConsignmentDocumentTypesEntity? = null
                if (cdTypeUuid != null) {
                    cdType = daoServices.findCdTypeDetailsWithUuid(cdTypeUuid)
                }
                val usersEntity = commonDaoServices.findUserByUserName(auth.name)
                val activeDocument = req.paramOrNull("active") ?: "1"
                // Query supervisor assigned or inspection officer assigned
                val page = extractPage(req)
                var data: Page<ConsignmentDocumentDetailsEntity>? = null
                when {
                    auth.authorities.stream().anyMatch { authority -> authority.authority == "DI_OFFICER_CHARGE_READ" } -> {
                        val personalTasks = req.paramOrNull("personal")
                        var personal = true
                        personalTasks?.let {
                            personal = it.toBoolean()
                        }
                        val supervisorCategory = req.paramOrNull("category")
                        if ("1".equals(activeDocument, false)) {
                            response = destinationInspectionService.findDocumentsWithActions(usersEntity, supervisorCategory, personal, page)
                        } else {
                            data = daoServices.findAllInspectionsCdWithAssigner(usersEntity, cdType, arrayListOf(ConsignmentApprovalStatus.QUERIED.code, ConsignmentApprovalStatus.UNDER_INSPECTION.code), page)
                            // Add data to response
                            response.data = ConsignmentDocumentDao.fromList(data.toList())
                            response.pageNo = data.number
                            response.totalPages = data.totalPages
                            response.totalCount = data.totalElements
                            response.message = "Assigned CD"
                            response.responseCode = ResponseCodes.SUCCESS_CODE
                        }
                    }
                    auth.authorities.stream().anyMatch { authority -> authority.authority == "DI_INSPECTION_OFFICER_READ" } -> {
                        data = if ("1".equals(activeDocument, false)) {
                            daoServices.findAllCdWithAssignedIoID(usersEntity, cdType, arrayListOf(ConsignmentApprovalStatus.UNDER_INSPECTION.code), page)
                        } else {
                            daoServices.findAllCdWithAssignedIoID(usersEntity, cdType, arrayListOf(ConsignmentApprovalStatus.WAITING.code, ConsignmentApprovalStatus.QUERIED.code), page)
                        }
                        // Add data to response
                        response.data = ConsignmentDocumentDao.fromList(data.toList())
                        response.pageNo = data.number
                        response.totalPages = data.totalPages
                        response.totalCount = data.totalElements
                        response.message = "Assigned CD"
                        response.responseCode = ResponseCodes.SUCCESS_CODE
                    }
                    else -> {
                        response.message = "Unauthorized access"
                        response.responseCode = ResponseCodes.FAILED_CODE
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

    @PreAuthorize("hasAuthority('DI_INSPECTION_OFFICER_READ') || hasAuthority('DI_OFFICER_CHARGE_READ')")
    fun ongoingAssignedConsignmentDocuments(req: ServerRequest): ServerResponse {
        val auth = commonDaoServices.loggedInUserAuthentication()
        val response = ApiResponseModel()
        val page = extractPage(req)
        req.paramOrNull("cdTypeUuid").let { cdTypeUuid ->
            try {
                var cdType: ConsignmentDocumentTypesEntity? = null
                if (cdTypeUuid != null) {
                    cdType = daoServices.findCdTypeDetailsWithUuid(cdTypeUuid)
                }
                val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
                val usersEntity = commonDaoServices.findUserByUserName(auth.name)
                val userProfilesEntity = commonDaoServices.findUserProfileByUserID(usersEntity, map.activeStatus)
                val allUserCFS = daoServices.findAllCFSUserList(userProfilesEntity.id!!)

                val pp = daoServices.findAllOngoingCdWithFreightStationID(allUserCFS, cdType, listOf(ConsignmentApprovalStatus.UNDER_INSPECTION.code, ConsignmentApprovalStatus.WAITING.code, ConsignmentApprovalStatus.QUERIED.code), page)
                response.data = ConsignmentDocumentDao.fromList(pp.toList())
                response.pageNo = pp.number
                response.totalPages = pp.totalPages
                response.totalCount = pp.totalElements
                response.message = "Un assigned CD"
                response.responseCode = ResponseCodes.SUCCESS_CODE
            } catch (ex: Exception) {
                KotlinLogging.logger { }.error("MANUAL ASSIGNED:", ex)
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
                val statuses = arrayListOf(ConsignmentApprovalStatus.APPROVED.code, ConsignmentApprovalStatus.REJECTED.code, ConsignmentApprovalStatus.REJECTED_AMEND.code)
                when {
                    auth.authorities.stream().anyMatch { authority -> authority.authority == "DI_OFFICER_CHARGE_READ" } -> {
                        val pp = daoServices.findAllCompleteCdWithAssigner(usersEntity, cdType, statuses, page)
                        response.data = ConsignmentDocumentDao.fromList(pp.toList())
                        response.totalPages = pp.totalPages
                        response.pageNo = pp.number
                        response.totalCount = pp.totalElements
                        response.responseCode = ResponseCodes.SUCCESS_CODE
                        response.message = "Success"
                    }
                    auth.authorities.stream().anyMatch { authority -> authority.authority == "DI_INSPECTION_OFFICER_READ" } -> {
                        val pp = daoServices.findAllCompleteCdWithAssignedIoID(usersEntity, cdType, statuses, page)
                        response.data = ConsignmentDocumentDao.fromList(pp.toList())
                        response.pageNo = pp.number
                        response.totalPages = pp.totalPages
                        response.totalCount = pp.totalElements
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

    @PreAuthorize("hasAuthority('DI_OFFICER_CHARGE_READ')|| hasAuthority('DI_INSPECTION_OFFICER_READ')")
    fun availableConsignmentDocuments(req: ServerRequest): ServerResponse {
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
                val allUserCFS = daoServices.findAllCFSUserList(userProfilesEntity.id!!)
                val statuses = listOf(ConsignmentApprovalStatus.NEW.code, -1, null)
                // Find documents
                val pp = daoServices.findAllAvailableCdWithPortOfEntry(allUserCFS, cdType, statuses, page)
                response.data = ConsignmentDocumentDao.fromList(pp.toList())
                response.totalPages = pp.totalPages
                response.pageNo = pp.number
                response.totalCount = pp.totalElements
                response.message = "Success"
                response.responseCode = ResponseCodes.SUCCESS_CODE

            } catch (ex: Exception) {
                KotlinLogging.logger { }.error("FAILED TO LOAD", ex)
                response.message = "Consignment type not found"
                response.responseCode = ResponseCodes.NOT_FOUND
            }
        }
        return ServerResponse.ok().body(response)
    }

    fun certificateOfConformance(req: ServerRequest): ServerResponse {
        req.pathVariable("coUuid").let {
            return ServerResponse.ok().body(this.destinationInspectionService.certificateOfConformanceDetails(it, req.pathVariable("docType")))
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

    fun consignmentDocumentHistory(req: ServerRequest): ServerResponse {
        req.pathVariable("cdId").let {
            return ServerResponse.ok().body(this.consignmentAuditService.listDocumentHistory(it.toLongOrDefault(0L)))
        }
    }

    fun consignmentDocumentCustomsDeclaration(req: ServerRequest): ServerResponse {
        req.pathVariable("coUuid").let {
            return ServerResponse.ok().body(this.destinationInspectionService.cdCustomDeclarationDetails(it))
        }
    }

    fun consignmentDocumentSSFLabDetails(req: ServerRequest): ServerResponse {
        req.pathVariable("cdItemUuid").let {
            return ServerResponse.ok().body(this.destinationInspectionService.getSSfDetails(it.toLongOrDefault(0)))
        }
    }

    fun searchConsignmentDocuments(req: ServerRequest): ServerResponse {
        var respose = ApiResponseModel()
        try {
            val auth = commonDaoServices.loggedInUserAuthentication()
            val form = req.body(SearchForms::class.java)
            when {
                auth.authorities.stream().anyMatch { authority -> authority.authority == "DI_OFFICER_CHARGE_READ" } -> {
                    respose = this.destinationInspectionService.searchConsignmentDocuments(form, true, false)
                }
                else -> {
                    respose = this.destinationInspectionService.searchConsignmentDocuments(form, false, true)
                }
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("SEARCH FAILED", ex)
            respose.responseCode = ResponseCodes.EXCEPTION_STATUS
            respose.message = "Search failed"
        }
        return ServerResponse.ok().body(respose)
    }

}