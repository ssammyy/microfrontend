package org.kebs.app.kotlin.apollo.api.handlers

import mu.KotlinLogging
import okhttp3.internal.toLongOrDefault
import org.kebs.app.kotlin.apollo.api.payload.ApiResponseModel
import org.kebs.app.kotlin.apollo.api.payload.ConsignmentDocumentDao
import org.kebs.app.kotlin.apollo.api.payload.ResponseCodes
import org.kebs.app.kotlin.apollo.api.payload.request.ConsignmentUpdateRequest
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.DestinationInspectionBpmn
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.*
import org.kebs.app.kotlin.apollo.api.service.ConsignmentDocumentAuditService
import org.kebs.app.kotlin.apollo.api.service.DestinationInspectionService
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.di.CdStatusTypesEntity
import org.kebs.app.kotlin.apollo.store.model.di.DeclarationDetailsEntity
import org.springframework.stereotype.Service
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import java.sql.Date

@Service
class DestinationInspectionActionsHandler(
        private val daoServices: DestinationInspectionDaoServices,
        private val commonDaoServices: CommonDaoServices,
        private val consignmentAuditService: ConsignmentDocumentAuditService,
        private val applicationMapProperties: ApplicationMapProperties,
        private val diBpmn: DestinationInspectionBpmn,
        private val diService: DestinationInspectionService
) {
    fun blacklistConsignment(req: ServerRequest): ServerResponse {
        val response = ApiResponseModel()
        try {
            val cdUuid = req.pathVariable("cdUuid")
            val consignmentDocument = daoServices.findCDWithUuid(cdUuid)
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val form = req.body(ConsignmentUpdateRequest::class.java)
            val data = mutableMapOf<String, Any?>()
            data.put("remarks", form.remarks)
            data.put("owner", loggedInUser.userName)
            data.put("blacklistId", form.blacklistId)
            data.put("cdUuid", cdUuid)
            data.put("supervisor", consignmentDocument.assigner?.userName)
            // Start blacklisting process
            this.diBpmn.startBlacklist(data, consignmentDocument)
            response.responseCode = ResponseCodes.SUCCESS_CODE
            response.message = "Success"
        } catch (ex: Exception) {
            response.message = ex.localizedMessage
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
        }

        return ServerResponse.ok().body(response)
    }

    fun generateLocalCoc(req: ServerRequest): ServerResponse {
        val response = ApiResponseModel()
        try {
            val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
            val cdUuid = req.pathVariable("cdUuid")
            val form = req.body(ConsignmentUpdateRequest::class.java)
            val consignmentDocument = daoServices.findCDWithUuid(cdUuid)
            if (consignmentDocument.cdType?.localCocStatus != map.activeStatus) {
                val data = mutableMapOf<String, Any?>()
                data.put("remarks", form.remarks)
                data.put("supervisor", consignmentDocument.assigner?.userName)
                data.put("cdUuid", cdUuid)
                this.diBpmn.startGenerateCoC(data, consignmentDocument)
                response.message = "Request submitted for approval"
                response.responseCode = ResponseCodes.SUCCESS_CODE
            } else {
                response.responseCode = ResponseCodes.FAILED_CODE
                response.message = "Local COC is not approved"
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("GENERATE LOCAL COC", ex)
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Failed to submit Local CoC request, please try again later"
        }
        return ServerResponse.ok().body(response)
    }

    fun generateLocalCor(req: ServerRequest): ServerResponse {
        val response = ApiResponseModel()
        try {
            val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
            val cdUuid = req.pathVariable("cdUuid")
            val consignmentDocument = daoServices.findCDWithUuid(cdUuid)
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val form = req.body(ConsignmentUpdateRequest::class.java)

            if (consignmentDocument.cdType?.localCorStatus == map.activeStatus) {
                val data = mutableMapOf<String, Any?>()
                data.put("remarks", form.remarks)
                data.put("cdUuid", cdUuid)
                data.put("owner", loggedInUser.userName)
                data.put("supervisor", consignmentDocument.assigner?.userName)
                this.diBpmn.startGenerateCor(data, consignmentDocument)
                response.responseCode=ResponseCodes.SUCCESS_CODE
                response.message="Local CoR request received"
            } else {
                response.responseCode = ResponseCodes.FAILED_CODE
                response.message = "Local COR is not yet approved"
            }
        }catch (ex: Exception) {
            KotlinLogging.logger {  }.error("COR REQUEST FAILED",ex)
            response.responseCode=ResponseCodes.EXCEPTION_STATUS
            response.message="Local COR request failed, please try again later"
        }

        return ServerResponse.ok().body(response)
    }

    fun updateMotorVehicleComplianceStatus(req: ServerRequest): ServerResponse {
        val response = ApiResponseModel()
        val form = req.body(ConsignmentUpdateRequest::class.java)
        req.pathVariable("inspectionChecklistId").let { inspectionChecklistId ->
            val generalInspectionChecklistId = inspectionChecklistId.toLongOrDefault(0L)
            commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
                    .let {
                        KotlinLogging.logger { }.info { "generalInspectionChecklistId = $generalInspectionChecklistId" }
                        daoServices.findInspectionGeneralById(generalInspectionChecklistId)
                                ?.let { cdInspectionGeneralEntity ->
                                    cdInspectionGeneralEntity.cdItemDetails?.cdDocId?.let { consignmentDocumentDetailsEntity ->
                                        consignmentDocumentDetailsEntity.compliantStatus = form.compliantStatus
                                        consignmentDocumentDetailsEntity.compliantDate = commonDaoServices.getCurrentDate()
                                        consignmentDocumentDetailsEntity.compliantRemarks = form.remarks
                                        val loggedInUser = commonDaoServices.loggedInUserDetails()
                                        daoServices.updateCdDetailsInDB(consignmentDocumentDetailsEntity, loggedInUser).let {
                                            //TODO: Send notification to the supervisor
                                        }
                                        consignmentAuditService.addHistoryRecord(cdInspectionGeneralEntity.id, form.remarks, "KEBS_COMPLIANCE", "Send Certificate of Inspection")
                                        response.data = ConsignmentDocumentDao.fromEntity(consignmentDocumentDetailsEntity)
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

    // Send COI
    fun sendCertificateOfInspection(req: ServerRequest): ServerResponse {
        val response = ApiResponseModel()
        try {
            req.pathVariable("cdUuid").let { cdUuid ->
                val form = req.body(ConsignmentUpdateRequest::class.java)
                val consignmentDocument = this.daoServices.findCDWithUuid(cdUuid)
                val loggedInUser = commonDaoServices.loggedInUserDetails()
                commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
                        .let { map ->
                            KotlinLogging.logger { }.info { "approveRejectCdStatusType = ${form.sendCoiStatus}" }
                            // Update consignment details
                            with(consignmentDocument) {
                                sendCoiStatus = form.sendCoiStatus
                                sendCoiRemarks = form.remarks
                                localCocOrCorDate = Date(java.util.Date().time)
                            }
                            //updating of Details in DB
                            val updatedCDDetails = daoServices.updateCdDetailsInDB(consignmentDocument, loggedInUser)
                            //Send Coi message To Single Window
                            val localCoi = updatedCDDetails.ucrNumber?.let { daoServices.findCOC(it) }
                            if (localCoi != null) {
                                daoServices.localCoiItems(updatedCDDetails, localCoi, loggedInUser, map)
                                daoServices.sendLocalCoi(localCoi.id)
                                updatedCDDetails.cdStandard?.let { cdStd ->
                                    daoServices.updateCDStatus(cdStd, applicationMapProperties.mapDICdStatusTypeCOIGeneratedAndSendID)
                                }
                            }
                            consignmentAuditService.addHistoryRecord(updatedCDDetails.id, form.remarks, "KEBS_COI", "Send Certificate of Inspection")
                            response.data = ConsignmentDocumentDao.fromEntity(consignmentDocument)
                            response.responseCode = ResponseCodes.SUCCESS_CODE
                            response.message = "Success"
                        }
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error { ex }
            response.message = ex.localizedMessage
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
        }
        return ServerResponse.ok().body(response)
    }

    fun approveRejectConsignment(req: ServerRequest): ServerResponse {
        var response = ApiResponseModel()
        try {
            req.pathVariable("cdUuid").let { cdUuid ->
                val form = req.body(ConsignmentUpdateRequest::class.java)
                response = this.diBpmn.startApprovalConsignment(cdUuid, form)
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("PROCESS ERROR", ex)
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = ex.localizedMessage
        }
        return ServerResponse.ok().body(response)
    }

    fun assignPort(req: ServerRequest): ServerResponse {
        val response = ApiResponseModel()
        try {
            req.pathVariable("cdUuid").let { cdUuid ->
                val form = req.body(ConsignmentUpdateRequest::class.java)
                val consignmentDocument = this.daoServices.findCDWithUuid(cdUuid)
                val processStages = commonDaoServices.findProcesses(applicationMapProperties.mapImportInspection)
                if (form.freightStation != null && form.portOfArrival != null) {
                    val freignStations = daoServices.findCfsID(form.freightStation!!)
                    with(consignmentDocument) {
                        freightStation = freignStations
                        portOfArrival = form.portOfArrival
                        assignPortRemarks = form.remarks
                    }
                    val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
                    val loggedInUser = commonDaoServices.loggedInUserDetails()
                    // update consignment
                    daoServices.updateCdDetailsInDB(consignmentDocument, loggedInUser)
                    consignmentDocument.assignPortRemarks?.let {
                        processStages.process1?.let { it1 ->
                            daoServices.createCDTransactionLog(map, loggedInUser, consignmentDocument.id!!, it, it1)
                        }
                    }
                    consignmentAuditService.addHistoryRecord(consignmentDocument.id, form.remarks, "KEBS_ASSIGN_PORT", "Assign Port To consignment")
                    response.message = "Port assigned"
                    response.responseCode = ResponseCodes.SUCCESS_CODE
                } else {
                    response.message = "Please provide the freight station and port of arival"
                    response.responseCode = ResponseCodes.FAILED_CODE
                }
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error { ex }
            response.message = ex.localizedMessage
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
        }
        return ServerResponse.ok().body(response)
    }


    fun targetConsignment(req: ServerRequest): ServerResponse {
        val response = ApiResponseModel()
        try {
            req.pathVariable("cdUuid").let { cdUuid ->
                val form = req.body(ConsignmentUpdateRequest::class.java)
                val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
                val consignmentDocument = this.daoServices.findCDWithUuid(cdUuid)
                if (consignmentDocument.targetStatus != 1) {
                    val data = mutableMapOf<String, Any?>()
                    data["targetStatus"] = map.activeStatus
                    data["remarks"] = form.remarks
                    data["cdUuid"] = cdUuid
                    val loggedInUser = commonDaoServices.loggedInUserDetails()
                    // Supervisor can request targeting
                    data["isSupervisor"]=(loggedInUser.userName==consignmentDocument.assigner?.userName)
                    // update consignment
                    data["supervisor"] = consignmentDocument.assigner?.userName
                    daoServices.updateCdDetailsInDB(consignmentDocument, loggedInUser)
                    // Submit Targeting to BPM
                    this.diBpmn.startTargetConsignment(data, consignmentDocument);
                    consignmentAuditService.addHistoryRecord(consignmentDocument.id, form.remarks, "KEBS_TARGET", "Target consignment")
                    response.message = "Target request submitted"
                    response.responseCode = ResponseCodes.SUCCESS_CODE
                } else {
                    response.message = "Consignment targeting has already been sent, please await approval"
                    response.responseCode = ResponseCodes.FAILED_CODE
                }
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error { ex }
            response.message = ex.localizedMessage
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
        }
        return ServerResponse.ok().body(response)
    }

    fun supervisorTasks(req: ServerRequest): ServerResponse {
        return ServerResponse.ok()
                .body(this.diBpmn.listUserTasks())
    }

    // Supervisor approve targeted consignment
    fun approveRejectTask(req: ServerRequest): ServerResponse {
        try {
            val form = req.body(ConsignmentUpdateRequest::class.java)
            val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
            val data = mutableMapOf<String, Any?>()
            data.put("remarks", form.remarks)
            data.put("targetingApproved", form.approvalStatus == map.activeStatus)
            data.put("taskApproved", form.approvalStatus == map.activeStatus)
            val cdUuid = req.pathVariable("cdUuid")
            val consignmentDocument = this.daoServices.findCDWithUuid(cdUuid)
            req.pathVariable("taskId").let {
                return ServerResponse.ok()
                        .body(this.diBpmn.consignmentDocumentProcessUpdate(it, data, consignmentDocument))
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("PROCESS UPDATE FAILED", ex)
            val response = ApiResponseModel()
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Request failed, please try again later"
            return ServerResponse.ok()
                    .body(response)
        }
    }

    // Targeting approval
    fun approveTargeting(req: ServerRequest, supervisor: Boolean): ServerResponse {
        val response = ApiResponseModel()
        try {
            req.pathVariable("cdUuid").let { cdUuid ->
                val form = req.body(ConsignmentUpdateRequest::class.java)
                val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
                val consignmentDocument = this.daoServices.findCDWithUuid(cdUuid)
                if (consignmentDocument.targetStatus == map.activeStatus || supervisor) {
                    with(consignmentDocument) {
                        targetApproveStatus = form.targetApproveStatus
                        targetApproveRemarks = form.remarks
                        targetApproveDate = Date(java.util.Date().time)
                    }
                    val loggedInUser = commonDaoServices.loggedInUserDetails()
                    // Supervisor status
                    if (supervisor) {
                        consignmentDocument.targetStatus = map.activeStatus
                        consignmentDocument.targetReason = form.remarks
                        consignmentDocument.targetDate = Date(java.util.Date().time)
                    }
                    // Inspection status
                    if (form.inspectionNotificationStatus == map.activeStatus) {
                        consignmentDocument.inspectionNotificationStatus = map.activeStatus
                        consignmentDocument.inspectionNotificationDate = Date(java.util.Date().time)
                    }
                    // update consignment
                    daoServices.updateCdDetailsInDB(consignmentDocument, loggedInUser)
                    val declaration: DeclarationDetailsEntity? = consignmentDocument.ucrNumber?.let { daoServices.findDeclaration(it) }
                    if (declaration != null && form.inspectionNotificationStatus == map.activeStatus) {
                        // Submit consignment to Single/Window
                        daoServices.submitCDStatusToKesWS("OH", "OH", consignmentDocument.version.toString(), consignmentDocument)
                        consignmentDocument.cdStandard?.let { cdStd ->
                            daoServices.updateCDStatus(cdStd, applicationMapProperties.mapDIStatusTypeKraVerificationSendId)
                        }
                        if (supervisor) {
                            response.message = "Target request submitted"
                            consignmentAuditService.addHistoryRecord(consignmentDocument.id, form.remarks, "KEBS_SUPERVISOR_TARGET", "Supervisor targeted consignment")
                        } else {
                            response.message = "Target approved successful"
                            consignmentAuditService.addHistoryRecord(consignmentDocument.id, form.remarks, "KEBS_APPROVE_TARGET", "Approve target consignment")
                        }
                        response.responseCode = ResponseCodes.SUCCESS_CODE
                    } else {
                        response.message = "Could not send verification request. Declaration unavailable"
                        response.responseCode = ResponseCodes.FAILED_CODE
                    }

                } else {
                    response.message = "Consignment targeting has already been sent, please await approval"
                    response.responseCode = ResponseCodes.FAILED_CODE
                }
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error { ex }
            response.message = ex.localizedMessage
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
        }
        return ServerResponse.ok().body(response)
    }

    fun pickConsignmentInspectionOfficer(req: ServerRequest): ServerResponse {
        val response = ApiResponseModel()
        try {
            req.pathVariable("cdUuid").let { cdUuid ->
                val form = req.body(ConsignmentUpdateRequest::class.java)
                val consignmentDocument = this.daoServices.findCDWithUuid(cdUuid)

                val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
                val processStages = commonDaoServices.findProcesses(applicationMapProperties.mapImportInspection)
                val loggedInUser = commonDaoServices.loggedInUserDetails()
                // Assign consignment to this user
                with(consignmentDocument) {
                    assignedRemarks = form.remarks
                    assignedDate = Date(java.util.Date().time)
                    assignedStatus = map.activeStatus
                    assignedInspectionOfficer = loggedInUser
                }
                this.daoServices.updateCdDetailsInDB(consignmentDocument, loggedInUser)
                // Transaction Log
                consignmentDocument.assignedRemarks?.let {
                    processStages.process2?.let { it1 ->
                        daoServices.createCDTransactionLog(map, loggedInUser, consignmentDocument.id!!, it, it1)
                    }
                }
                consignmentDocument.cdStandard?.let { cdStd ->
                    daoServices.updateCDStatus(cdStd, applicationMapProperties.mapDIStatusTypeAssignIoId)
                }
                //Start the relevant BPM
                diBpmn.startDiBpmProcessByCdType(consignmentDocument)
                diBpmn.assignIOBpmTask(consignmentDocument)
                consignmentAuditService.addHistoryRecord(consignmentDocument.id, form.remarks, "KEBS_MANUAL_ASSIGN_IO", "Manual pick consignment")
                response.responseCode = ResponseCodes.SUCCESS_CODE
                response.message = "Inspection officer assigned"
            }
        } catch (ex: Exception) {
            response.message = ex.localizedMessage
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
        }
        return ServerResponse.ok().body(response)
    }

    fun assignInspectionOfficer(req: ServerRequest): ServerResponse {
        val response = ApiResponseModel()
        try {
            req.pathVariable("cdUuid").let { cdUuid ->
                val form = req.body(ConsignmentUpdateRequest::class.java)
                val officer = daoServices.findUserById(form.officerId)
                if (officer.isPresent) {
                    val consignmentDocument = this.daoServices.findCDWithUuid(cdUuid)

                    val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
                    val processStages = commonDaoServices.findProcesses(applicationMapProperties.mapImportInspection)
                    val loggedInUser = commonDaoServices.loggedInUserDetails()
                    if (form.reassign) {
                        with(consignmentDocument) {
                            reassignedRemarks = form.remarks
                            reassignedDate = Date(java.util.Date().time)
                            reassignedStatus = map.activeStatus
                            assignedInspectionOfficer = officer.get()
                        }
                    } else {
                        with(consignmentDocument) {
                            assignedRemarks = form.remarks
                            assignedDate = Date(java.util.Date().time)
                            assignedStatus = map.activeStatus
                            assignedInspectionOfficer = officer.get()
                        }
                    }
                    this.daoServices.updateCdDetailsInDB(consignmentDocument, loggedInUser)
                    consignmentDocument.assignedRemarks?.let {
                        processStages.process2?.let { it1 ->
                            daoServices.createCDTransactionLog(map, loggedInUser, consignmentDocument.id!!, it, it1)
                        }
                    }
                    consignmentDocument.cdStandard?.let { cdStd ->
                        daoServices.updateCDStatus(cdStd, applicationMapProperties.mapDIStatusTypeAssignIoId)
                    }
                    //Start the relevant BPM
                    diBpmn.startDiBpmProcessByCdType(consignmentDocument)
                    diBpmn.assignIOBpmTask(consignmentDocument)
                    if (form.reassign) {
                        consignmentAuditService.addHistoryRecord(consignmentDocument.id, form.remarks, "KEBS_REASSIGN_IO", "Re-Assign inspection officer to consignment")
                    } else {
                        consignmentAuditService.addHistoryRecord(consignmentDocument.id, form.remarks, "KEBS_ASSIGN_IO", "Assign inspection officer to consignment")
                    }
                    response.responseCode = ResponseCodes.SUCCESS_CODE
                    response.message = "Inspection officer assigned"
                } else {
                    response.message = "Please select inspection officer"
                    response.responseCode = ResponseCodes.NOT_FOUND
                }
            }
        } catch (ex: Exception) {
            response.message = ex.localizedMessage
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
        }
        return ServerResponse.ok().body(response)
    }

}