package org.kebs.app.kotlin.apollo.api.handlers

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.payload.ApiResponseModel
import org.kebs.app.kotlin.apollo.api.payload.ConsignmentDocumentDao
import org.kebs.app.kotlin.apollo.api.payload.ResponseCodes
import org.kebs.app.kotlin.apollo.api.payload.request.ConsignmentUpdateRequest
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.DestinationInspectionBpmn
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.DestinationInspectionDaoServices
import org.kebs.app.kotlin.apollo.api.service.ConsignmentDocumentAuditService
import org.kebs.app.kotlin.apollo.api.service.DestinationInspectionService
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
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
            if (diBpmn.modifyDisabled(consignmentDocument, response)) {
                KotlinLogging.logger { }.error("FAILED to APPROVE IO, MODIFICATION disabled")
                return ServerResponse.ok().body(response)
            }
            // Blacklist
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
            response.message = "Black listing request received, please await approval"
        } catch (ex: Exception) {
            response.message = ex.localizedMessage
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
        }

        return ServerResponse.ok().body(response)
    }

    fun markConsignmentCompliant(req: ServerRequest): ServerResponse {
        val response = ApiResponseModel()
        try {
            val cdUuid = req.pathVariable("cdUuid")
            val consignmentDocument = daoServices.findCDWithUuid(cdUuid)
            if (diBpmn.modifyDisabled(consignmentDocument, response)) {
                KotlinLogging.logger { }.error("FAILED to MARK COMPLIANT, MODIFICATION disabled")
            } else {
                val loggedInUser = commonDaoServices.loggedInUserDetails()
                val form = req.body(ConsignmentUpdateRequest::class.java)
                val data = mutableMapOf<String, Any?>()
                data.put("remarks", form.remarks)
                data.put("owner", loggedInUser.userName)
                data.put("documentType", form.documentType)
                data.put("cdUuid", cdUuid)
                data.put("compliantStatus", form.compliantStatus)
                data.put("supervisor", consignmentDocument.assigner?.userName)
                // Start blacklisting process
                val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
                this.diBpmn.startCompliantProcess(map, data, consignmentDocument)
                response.responseCode = ResponseCodes.SUCCESS_CODE
                response.message = "Compliant request received, please await approval"
            }
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
            if (diBpmn.modifyDisabled(consignmentDocument, response)) {
                KotlinLogging.logger { }.error("FAILED to GENERATE LOCAL COC, MODIFICATION disabled")
                return ServerResponse.ok().body(response)
            }
            if (consignmentDocument.cdType?.localCocStatus != map.activeStatus) {
                val data = mutableMapOf<String, Any?>()
                data.put("remarks", form.remarks)
                data.put("supervisor", consignmentDocument.assigner?.userName)
                data.put("cdUuid", cdUuid)
                this.diBpmn.startGenerateCoC(map, data, consignmentDocument)
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
            if (diBpmn.modifyDisabled(consignmentDocument, response)) {
                KotlinLogging.logger { }.error("FAILED to GENERATE LOCAL COR, MODIFICATION disabled")
                return ServerResponse.ok().body(response)
            }
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val form = req.body(ConsignmentUpdateRequest::class.java)

            if (consignmentDocument.cdType?.localCorStatus == map.activeStatus) {
                val data = mutableMapOf<String, Any?>()
                data.put("remarks", form.remarks)
                data.put("cdUuid", cdUuid)
                data.put("owner", loggedInUser.userName)
                data.put("supervisor", consignmentDocument.assigner?.userName)
                this.diBpmn.startGenerateCor(map, data, consignmentDocument)
                response.responseCode = ResponseCodes.SUCCESS_CODE
                response.message = "Local CoR request received"
            } else {
                response.responseCode = ResponseCodes.FAILED_CODE
                response.message = "Local COR is not yet approved"
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("COR REQUEST FAILED", ex)
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Local COR request failed, please try again later"
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
                            val localCoi = updatedCDDetails.ucrNumber?.let { daoServices.findCOC(it,"coi") }
                            if (localCoi != null) {
                                daoServices.localCocCoiItems(updatedCDDetails, localCoi, loggedInUser, map)
                                daoServices.sendLocalCoi(localCoi.id)
                                updatedCDDetails.cdStandard?.let { cdStd ->
                                    daoServices.updateCDStatus(cdStd, applicationMapProperties.mapDICdStatusTypeCOIGeneratedAndSendID)
                                }
                            }
                            consignmentAuditService.addHistoryRecord(updatedCDDetails.id, updatedCDDetails.ucrNumber, form.remarks, "KEBS_COI", "Send Certificate of Inspection")
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
                    consignmentAuditService.addHistoryRecord(consignmentDocument.id, consignmentDocument.ucrNumber, form.remarks, "KEBS_ASSIGN_PORT", "Assign Port To consignment")
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
        return targetConsignment(req, false)
    }

    fun supervisorTargetConsignment(req: ServerRequest): ServerResponse {
        return targetConsignment(req, true)
    }

    fun targetConsignment(req: ServerRequest, isSupervisor: Boolean): ServerResponse {
        val response = ApiResponseModel()
        try {
            req.pathVariable("cdUuid").let { cdUuid ->
                val form = req.body(ConsignmentUpdateRequest::class.java)
                val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
                val consignmentDocument = this.daoServices.findCDWithUuid(cdUuid)
                if (diBpmn.modifyDisabled(consignmentDocument, response)) {
                    KotlinLogging.logger { }.error("FAILED to TARGET CD, MODIFICATION disabled")
                } else {
                    if (consignmentDocument.targetStatus != 1) {
                        val data = mutableMapOf<String, Any?>()
                        data["targetStatus"] = map.activeStatus
                        data["remarks"] = form.remarks
                        data["cdUuid"] = cdUuid
                        val loggedInUser = commonDaoServices.loggedInUserDetails()
                        // Supervisor can request targeting
                        data["isSupervisor"] = isSupervisor
                        // update consignment
                        data["supervisor"] = consignmentDocument.assigner?.userName
                        daoServices.updateCdDetailsInDB(consignmentDocument, loggedInUser)
                        // Submit Targeting to BPM
                        this.diBpmn.startTargetConsignment(map, data, consignmentDocument);
                        response.message = "Target request submitted"
                        response.responseCode = ResponseCodes.SUCCESS_CODE
                    } else {
                        response.message = "Consignment targeting has already been sent, please await approval"
                        response.responseCode = ResponseCodes.FAILED_CODE
                    }
                }
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error { ex }
            response.message = ex.localizedMessage
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
        }
        return ServerResponse.ok().body(response)
    }
    fun deleteSupervisorTasks(req: ServerRequest): ServerResponse {
        return ServerResponse.ok().body(this.diBpmn.deleteTask(req.pathVariable("taskId")))
    }
    fun supervisorTasks(req: ServerRequest): ServerResponse {
        return ServerResponse.ok()
                .body(this.diBpmn.listUserTasks())
    }

    // Supervisor approve targeted consignment
    fun approveRejectTask(req: ServerRequest): ServerResponse {
        val response = ApiResponseModel()
        try {
            val form = req.body(ConsignmentUpdateRequest::class.java)
            val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
            val data = mutableMapOf<String, Any?>()
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            data.put("remarks", form.remarks)
            data.put("supervisor", loggedInUser.userName)
            data.put("targetingApproved", form.approvalStatus == map.activeStatus)
            data.put("taskApproved", form.approvalStatus == map.activeStatus)
            val cdUuid = req.pathVariable("cdUuid")
            val consignmentDocument = this.daoServices.findCDWithUuid(cdUuid)
            if (diBpmn.modifyDisabled(consignmentDocument, response)) {
                KotlinLogging.logger { }.error("FAILED to APPROVE IO, MODIFICATION disabled")
            } else {
                req.pathVariable("taskId").let {
                    return ServerResponse.ok()
                            .body(this.diBpmn.consignmentDocumentProcessUpdate(it, data, consignmentDocument))
                }
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("PROCESS UPDATE FAILED", ex)
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Request failed, please try again later"
        }
        return ServerResponse.ok()
                .body(response)
    }

    fun pickConsignmentInspectionOfficer(req: ServerRequest): ServerResponse {
        val response = ApiResponseModel()
        try {
            req.pathVariable("cdUuid").let { cdUuid ->
                val form = req.body(ConsignmentUpdateRequest::class.java)
                val consignmentDocument = this.daoServices.findCDWithUuid(cdUuid)
                //Start the relevant BPM
                if (diBpmn.modifyDisabled(consignmentDocument, response)) {
                    KotlinLogging.logger { }.error("FAILED to PICK FOR IO, MODIFICATION disabled")
                } else {
                    diService.selfAssign(consignmentDocument, form.remarks)
                    consignmentAuditService.addHistoryRecord(consignmentDocument.id, consignmentDocument.ucrNumber, form.remarks, "KEBS_MANUAL_ASSIGN_IO", "Manual pick consignment")
                    response.responseCode = ResponseCodes.SUCCESS_CODE
                    response.message = "Inspection officer assigned"
                }
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
                    if (diBpmn.modifyDisabled(consignmentDocument, response)) {
                        KotlinLogging.logger { }.error("FAILED to Assign IO, MODIFICATION disabled")
                    } else {
                        val loggedInUser = commonDaoServices.loggedInUserDetails()
                        val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
                        val data = mutableMapOf<String, Any?>()
                        data["remarks"] = form.remarks
                        data["reassign"] = form.reassign
                        data["officerId"] = form.officerId
                        data["owner"] = officer.get().userName
                        data["supervisor"] = loggedInUser.userName
                        data["isAutoRejected"] = consignmentDocument.cdType?.autoRejectStatus == map.activeStatus
                        data["isAutoTargeted"] = consignmentDocument.cdType?.autoTargetStatus == map.activeStatus
                        data["cdUuid"] = cdUuid
                        // Start BPM process
                        this.diBpmn.startAssignmentProcesses(data, consignmentDocument);
                        // Prepare response
                        response.responseCode = ResponseCodes.SUCCESS_CODE
                        response.message = "Inspection officer assigned"
                    }
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