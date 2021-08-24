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
        private val reportsDaoService: ReportsDaoService,
        private val riskProfileDaoService: RiskProfileDaoService
) {
    fun approveBlacklistConsignment(req: ServerRequest): ServerResponse {
        val response = ApiResponseModel()
        try {
            val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
            val cdUuid = req.pathVariable("cdUuid")
            val consignmentDocument = daoServices.findCDWithUuid(cdUuid)
            val form = req.body(ConsignmentUpdateRequest::class.java)
            with(consignmentDocument) {
                blacklistApprovedRemarks = form.remarks
                blacklistId = form.blacklistId
                blacklistApprovedDate = Date(java.util.Date().time)
                blacklistApprovedStatus = form.blacklistStatus
            }
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            daoServices.updateCdDetailsInDB(consignmentDocument, loggedInUser)


            when (consignmentDocument.blacklistId) {
                applicationMapProperties.mapRiskProfileImporter -> {
                    val importerDetailsEntity =
                            consignmentDocument.cdImporter?.let { daoServices.findCDImporterDetails(it) }
                    val allRemarksAdded =
                            """[${form.remarks}] and [${consignmentDocument.blacklistRemarks}] and [The UCR Number = ${consignmentDocument.ucrNumber}]"""
                    importerDetailsEntity?.let {
                        riskProfileDaoService.addImporterToRiskProfile(
                                it,
                                allRemarksAdded,
                                loggedInUser
                        )
                    }
                }
                applicationMapProperties.mapRiskProfileExporter -> {
                    val exporterDetailsEntity =
                            consignmentDocument.cdExporter?.let { daoServices.findCdExporterDetails(it) }
                    val allRemarksAdded =
                            "[${form.remarks}] and [${consignmentDocument.blacklistRemarks}] and [The UCR Number = ${consignmentDocument.ucrNumber}]"
                    exporterDetailsEntity?.let {
                        riskProfileDaoService.addExporterToRiskProfile(
                                it,
                                allRemarksAdded,
                                loggedInUser
                        )
                    }
                }
                applicationMapProperties.mapRiskProfileConsignee -> {
                    val consigneeDetailsEntity =
                            consignmentDocument.cdConsignee?.let { daoServices.findCdConsigneeDetails(it) }
                    val allRemarksAdded =
                            "[${consignmentDocument.blacklistApprovedRemarks}] and [${consignmentDocument.blacklistRemarks}] and [The UCR Number = ${consignmentDocument.ucrNumber}]"
                    consigneeDetailsEntity?.let {
                        riskProfileDaoService.addConsigneeToRiskProfile(
                                it,
                                allRemarksAdded,
                                loggedInUser
                        )
                    }
                }
                applicationMapProperties.mapRiskProfileConsignor -> {
                    val consignorDetailsEntity =
                            consignmentDocument.cdConsignor?.let { daoServices.findCdConsignorDetails(it) }
                    val allRemarksAdded =
                            "[${consignmentDocument.blacklistApprovedRemarks}] and [${consignmentDocument.blacklistRemarks}] and [The UCR Number = ${consignmentDocument.ucrNumber}]"
                    consignorDetailsEntity?.let {
                        riskProfileDaoService.addConsignorToRiskProfile(
                                it,
                                allRemarksAdded,
                                loggedInUser
                        )
                    }
                }
            }
            val processStages = commonDaoServices.findProcesses(applicationMapProperties.mapImportInspection)
            consignmentDocument.blacklistApprovedRemarks?.let {
                processStages.process1?.let { it1 ->
                    daoServices.createCDTransactionLog(
                            map,
                            loggedInUser,
                            consignmentDocument.id!!,
                            it,
                            it1
                    )
                }
            }
            response.responseCode = ResponseCodes.SUCCESS_CODE
            response.message = "Success"
        } catch (ex: Exception) {
            response.message = ex.localizedMessage
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
        }
        return ServerResponse.ok().body(response)
    }

    fun blacklistConsignment(req: ServerRequest): ServerResponse {
        val response = ApiResponseModel()
        try {
            val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
            val cdUuid = req.pathVariable("cdUuid")
            val consignmentDocument = daoServices.findCDWithUuid(cdUuid)
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val form = req.body(ConsignmentUpdateRequest::class.java)
            // Update document
            with(consignmentDocument) {
                blacklistRemarks = form.remarks
                blacklistId = form.blacklistId
                blacklistStatus = form.blacklistStatus
            }
            this.daoServices.updateCdDetailsInDB(consignmentDocument, loggedInUser)
            // Process blacklist request
            val payload =
                    "BlackList Consignment Document [blacklistStatus= ${form.blacklistStatus}, blacklistRemarks= ${form.remarks}]"
            val sr = commonDaoServices.mapServiceRequestForSuccess(map, payload, loggedInUser)
            consignmentDocument.assigner?.let {
                commonDaoServices.sendEmailWithUserEntity(
                        it,
                        daoServices.diCdAssignedUuid,
                        consignmentDocument,
                        map,
                        sr
                )
            }
            val processStages = commonDaoServices.findProcesses(applicationMapProperties.mapImportInspection)
            consignmentDocument.assignedRemarks?.let {
                processStages.process2?.let { it1 ->
                    daoServices.createCDTransactionLog(
                            map,
                            loggedInUser,
                            consignmentDocument.id!!,
                            it,
                            it1
                    )
                }
            }
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
        val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
        val cdUuid = req.pathVariable("cdUuid")
        val consignmentDocument = daoServices.findCDWithUuid(cdUuid)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        if (consignmentDocument.cdType?.localCocStatus == map.activeStatus) {
            when (consignmentDocument.localCoi) {
                //Todo : Ask Fred on where to get the routValue
                map.activeStatus -> {
                    val localCoi = daoServices.createLocalCoi(loggedInUser, consignmentDocument, map, "D")
                    response.responseCode = ResponseCodes.SUCCESS_CODE
                    response.message = "Local COC generated"
                }
                else -> {
                    val localCoc = daoServices.createLocalCoc(loggedInUser, consignmentDocument, map, "A")
                    consignmentDocument.cdStandard?.let { cdStd ->
                        daoServices.updateCDStatus(
                                cdStd,
                                applicationMapProperties.mapDICdStatusTypeCOCGeneratedAndSendID
                        )
                    }
                    KotlinLogging.logger { }.info { "localCoc = ${localCoc.id}" }
                    //Generate PDF File & send to manufacturer
                    reportsDaoService.generateLocalCoCReportWithDataSource(consignmentDocument, applicationMapProperties.mapReportLocalCocPath)?.let { file ->
                        consignmentDocument.cdImporter?.let {
                            daoServices.findCDImporterDetails(it)
                        }?.let { importer ->
                            importer.email?.let { daoServices.sendLocalCocReportEmail(it, file.path) }
                        }
                    }
                    response.responseCode = ResponseCodes.SUCCESS_CODE
                    response.message = "Local COC generated"
                }
            }
        } else {
            response.responseCode = ResponseCodes.FAILED_CODE
            response.message = "Local COC is not approved"
        }
        return ServerResponse.ok().body(response)
    }

    fun generateLocalCor(req: ServerRequest): ServerResponse {
        val response = ApiResponseModel()
        val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
        val cdUuid = req.pathVariable("cdUuid")
        val consignmentDocument = daoServices.findCDWithUuid(cdUuid)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        if (consignmentDocument.cdType?.localCorStatus == map.activeStatus) {
            daoServices.generateCor(consignmentDocument, map, loggedInUser).let { corDetails ->
                daoServices.submitCoRToKesWS(corDetails)
                consignmentDocument.cdStandard?.let { cdStd ->
                    daoServices.updateCDStatus(
                            cdStd,
                            applicationMapProperties.mapDICdStatusTypeCORGeneratedAndSendID
                    )
                }
                //Send Cor to manufacturer
                reportsDaoService.generateLocalCoRReport(consignmentDocument, applicationMapProperties.mapReportLocalCorPath)?.let { file ->
                    with(corDetails) {
                        localCorFile = file.readBytes()
                        localCorFileName = file.name
                    }
                    daoServices.saveCorDetails(corDetails)
                    //Send email
                    consignmentDocument.cdImporter?.let {
                        daoServices.findCDImporterDetails(it)
                    }?.let { importer ->
                        importer.email?.let { daoServices.sendLocalCorReportEmail(it, file.path) }
                    }
                }
                response.responseCode = ResponseCodes.SUCCESS_CODE
                response.message = "Local COR generated"
            }
        } else {
            response.responseCode = ResponseCodes.FAILED_CODE
            response.message = "Local COR is not yet approved"
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
        val response = ApiResponseModel()
        try {
            req.pathVariable("cdUuid").let { cdUuid ->
                val form = req.body(ConsignmentUpdateRequest::class.java)
                val consignmentDocument = this.daoServices.findCDWithUuid(cdUuid)
                var cdStatusType: CdStatusTypesEntity?

                commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
                        .let { map ->
                            KotlinLogging.logger { }.info { "approveRejectCdStatusType = ${form.cdStatusTypeId}" }
                            cdStatusType = form.cdStatusTypeId?.let { daoServices.findCdStatusValue(it) }
                            cdStatusType?.let { cdStatus ->
                                with(consignmentDocument) {
                                    approveRejectCdStatusType = cdStatus
                                    approveRejectCdDate = Date(java.util.Date().time)
                                    approveRejectCdRemarks = form.remarks
                                }
                                //updating of Details in DB
                                val loggedInUser = commonDaoServices.loggedInUserDetails()
                                val updatedCDDetails = daoServices.updateCdDetailsInDB(consignmentDocument, loggedInUser)
                                //Send Approval/Rejection message To Single Window
                                consignmentDocument.approveRejectCdRemarks?.let { it1 ->
                                    cdStatus.statusCode?.let {
                                        daoServices.submitCDStatusToKesWS(it1, it, consignmentDocument.version.toString(), consignmentDocument)
                                    }
                                    updatedCDDetails.cdStandard?.let { cdStd ->
                                        updatedCDDetails.approveRejectCdStatusType?.id?.let { it2 -> daoServices.updateCDStatus(cdStd, it2) }
                                    }
                                }
                                //Update Check CD task in Bpm
                                updatedCDDetails.assigner?.id?.let { it2 ->
                                    updatedCDDetails.approveRejectCdStatusType?.category?.let { cdDecision ->
                                        updatedCDDetails.id?.let {
                                            diBpmn.diCheckCdComplete(it, it2, cdDecision)
                                        }
                                    }
                                }
                                consignmentAuditService.addHistoryRecord(updatedCDDetails.id, form.remarks, "KEBS_${cdStatus.category?.toUpperCase()}", "${cdStatus.category?.capitalize()} consignment")
                            }

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

    fun assignPort(req: ServerRequest): ServerResponse {
        val response = ApiResponseModel()
        try {
            req.pathVariable("cdUuid").let { cdUuid ->
                val form = req.body(ConsignmentUpdateRequest::class.java)
                val consignmentDocument = this.daoServices.findCDWithUuid(cdUuid)
                val processStages = commonDaoServices.findProcesses(applicationMapProperties.mapImportInspection)
                if (form.freightStation != null && form.portOfArrival != null) {

                    with(consignmentDocument) {
                        freightStation = form.freightStation
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
                    with(consignmentDocument) {
                        targetStatus = map.activeStatus
                        targetReason = form.remarks
                        targetDate = Date(java.util.Date().time)
                    }
                    val loggedInUser = commonDaoServices.loggedInUserDetails()
                    // update consignment
                    daoServices.updateCdDetailsInDB(consignmentDocument, loggedInUser)
                    // TODO: what to do after status update
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

    // Targeting of consignment by supervisor, no need for approval
    fun supervisorTargetConsignment(req: ServerRequest): ServerResponse {
        return approveTargeting(req, true)
    }

    // Supervisor approve targeted consignment
    fun approveTargetConsignment(req: ServerRequest): ServerResponse {
        return approveTargeting(req, false)
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
                    if(form.inspectionNotificationStatus==map.activeStatus){
                        consignmentDocument.inspectionNotificationStatus=map.activeStatus
                        consignmentDocument.inspectionNotificationDate=Date(java.util.Date().time)
                    }
                    // update consignment
                    daoServices.updateCdDetailsInDB(consignmentDocument, loggedInUser)
                    val declaration: DeclarationDetailsEntity? = consignmentDocument.ucrNumber?.let { daoServices.findDeclaration(it) }
                    if (declaration != null && form.inspectionNotificationStatus==map.activeStatus) {
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
                daoServices.startDiBpmProcessByCdType(consignmentDocument)
                daoServices.assignIOBpmTask(consignmentDocument)
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
                    daoServices.startDiBpmProcessByCdType(consignmentDocument)
                    daoServices.assignIOBpmTask(consignmentDocument)
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