package org.kebs.app.kotlin.apollo.api.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.payload.*
import org.kebs.app.kotlin.apollo.api.payload.request.SearchForms
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.DestinationInspectionBpmn
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.*
import org.kebs.app.kotlin.apollo.common.dto.CocsItemsEntityDto
import org.kebs.app.kotlin.apollo.common.dto.CorItemsEntityDto
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.exceptions.InvalidValueException
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.events.SearchInitialization
import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.model.di.CdStatusTypesEntity
import org.kebs.app.kotlin.apollo.store.model.di.ConsignmentDocumentDetailsEntity
import org.kebs.app.kotlin.apollo.store.model.di.ConsignmentDocumentTypesEntity
import org.kebs.app.kotlin.apollo.store.model.di.DiUploadsEntity
import org.kebs.app.kotlin.apollo.store.repo.*
import org.kebs.app.kotlin.apollo.store.repo.di.ICfsTypeCodesRepository
import org.kebs.app.kotlin.apollo.store.repo.di.IConsignmentDocumentTypesEntityRepository
import org.kebs.app.kotlin.apollo.store.repo.di.IDiUploadsRepository
import org.kebs.app.kotlin.apollo.store.repo.di.IMinistryStationEntityRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStreamReader
import java.io.Reader
import java.math.BigDecimal
import java.sql.Date
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

enum class ConsignmentCertificatesIssues(val nameDesc: String) {
    COC("COC"), NCR("NCR"), COI("COI"), COR("COR"), OTHERS("OTHER");
}

enum class ConsignmentDocumentStatus(val code: String) {
    NEW_CD("NEW_CD"), REVISED_CD("REVISED_CD"), OLD_CD("OLD_CD"), ON_HOLD("ONHOLD"),
    IO_ASSIGNED("IO_ASSIGNED"), IO_REASSIGNED("IO_REASSIGNED"), IO_SELF_ASSIGN("IO_SELF_ASSIGNED"),
    LAB_REQUEST("LAB_REQUEST"), LAB_RESULT_RESULT("LAB_RESULT"), ITEM_COMPLIANCE("COMPLIANCE"), ITEM_NON_COMPLIANCE("NON_COMPLIANCE"),
    CHECKLIST_FILLED("CHECKLIST_FILLED"), CHECKLIST_COMPLETED("SSF_FILLED"), CHECKLIST_STARTED("CHECKLIST_STARTED"),
    SCF_FILLED("SCF_FILLED"), SSF_FILLED("SSF_FILLED"), BS_NUMBER_FILLED("BS_NUMBER_FILLED"),
    TARGET_REQUEST("TARGET_REQUEST"), TARGET_APPROVED("TARGET_APPROVED"), TARGET_REJECTED("TARGET_REJECTED"), KRA_VERIFICATION_REQUEST("KRA_VERIFICATION_REQUEST"), KRA_VERIFICATION("KRA_VERIFICATION"),
    PAYMENT_REQUEST("PAYMENT_REQUEST"), PAYMENT_APPROVED("PAYMENT_APPROVE"), PAYMENT_REJECTED("PROCESS_REJECT"), PAYMENT_MADE("PAYMENT_MADE"), PARTIAL_PAYMENT_MADE("PARTIAL_PAYMENT_MADE"), PAYMENT_FAILED("PAYMENT_FAILED"),
    COMPLIANCE_REQUEST("COMPLIANCE_REQ"), COMPLIANCE_APPROVED("COMPLIANCE_APPROVE"), COMPLIANCE_REJECTED("COMPLIANCE_REJECTED"),
    BLACKLIST_REQUEST("BLACKLIST_REQUEST"), BLACKLIST_APPROVED("BLACKLIST_APPROVED"), BLACKLIST_REJECTED("BLACKLIST_REJECTED"),
    COC_ISSUED("COC_ISSUED"), COR_ISSUED("COR_ISSUED"), COI_ISSUED("COU_ISSUED"), NCR_ISSUED("NCR_ISSUED"),
    REJ_AMEND_REQUEST("AMENDMENT"), REJ_AMEND_APPROVED("AMENDMENT_APPROVE"), REJ_AMEND_REJECTED("AMEND_REJECTED"),
    QUERY_REQUEST("QUERY_REQUEST"), AMEND_APPROVED("QUERY"), QUERY_REJECTED("QUERY_REJECTED"),
    APPROVE_REQUEST("APPROVE_REQUEST"), APPROVE_APPROVED("APPROVE"), APPROVE_REJECTED("APPROVE_REJECTED"),
    REJECT_REQUEST("REJECT_REQUEST"), REJECT_APPROVED("REJECT"), REJECT_REJECTED("REJECT_REJECTED"),
    MINISTRY_REQUEST("MINISTRY_REQUEST"), MINISTRY_UPLOAD("MINISTRY_UPLOAD")
}

enum class ConsignmentApprovalStatus(val code: Int) {
    NEW(10), UNDER_INSPECTION(0), APPROVED(1), REJECTED(2), REJECTED_AMEND(3), WAITING(4), QUERIED(5);
}

@Service("diService")
class DestinationInspectionService(
        private val ministryStationRepo: IMinistryStationEntityRepository,
        private val applicationMapProperties: ApplicationMapProperties,
        private val riskProfileDaoService: RiskProfileDaoService,
        private val commonDaoServices: CommonDaoServices,
        private val service: DaoService,
        private val iDIUploadsRepo: IDiUploadsRepository,
        private val cocsRepository: ICocsRepository,
        private val corRepository: ICorsBakRepository,
        private val cocItemsRepository: ICocItemsRepository,
        private val daoServices: DestinationInspectionDaoServices,
        private val cdTypesRepo: IConsignmentDocumentTypesEntityRepository,
        private val importerDaoServices: ImporterDaoServices,
        private val cdAuditService: ConsignmentDocumentAuditService,
        private val qaDaoServices: QADaoServices,
        private val cdItemsDetailsRepository: ICdItemDetailsRepo,
        private val diBpmn: DestinationInspectionBpmn,
        private val cfsTypesEntity: ICfsTypeCodesRepository,
        private val reportsDaoService: ReportsDaoService,
        private val privilegesRepository: IUserPrivilegesRepository,
        private val userEntityRepository: IUserRepository,
        private val searchService: SearchInitialization,
) {

    fun findDocumentsWithActions(usersEntity: UsersEntity, category: String?, myTask: Boolean, page: PageRequest): ApiResponseModel {
        return diBpmn.consignmentDocumentWithActions(usersEntity, category, myTask, page)
    }

    fun markCompliant(cdUuid: String, compliantStatus: Int, supervisor: String, remarks: String, taskApproved: Boolean): Boolean {
        if (taskApproved) {
            val consignmentDocument = this.daoServices.findCDWithUuid(cdUuid)
            consignmentDocument.compliantStatus = compliantStatus
            consignmentDocument.status = ConsignmentApprovalStatus.APPROVED.code
            consignmentDocument.compliantDate = commonDaoServices.getCurrentDate()
            consignmentDocument.compliantRemarks = remarks
            val loggedInUser = commonDaoServices.findUserByUserName(supervisor)
            daoServices.updateCdDetailsInDB(consignmentDocument, loggedInUser)
            cdAuditService.addHistoryRecord(consignmentDocument.id, consignmentDocument.ucrNumber, remarks, "KEBS_APPROVE_COMPLIANCE", "Approve compliance request", supervisor)
        }
        return false
    }

    fun rejectComplianceRequests(cdUuid: String, supervisor: String, remarks: String): Boolean {
        var consignmentDocument = this.daoServices.findCDWithUuid(cdUuid)
        val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
        // Move back to officer tasks
        consignmentDocument.status = ConsignmentApprovalStatus.UNDER_INSPECTION.code
        consignmentDocument.compliantStatus = map.inactiveStatus
        consignmentDocument.approveRejectCdStatus = map.initStatus
        consignmentDocument.varField10 = "COMPLIANCE REJECTED"
        // Update and add comments
        consignmentDocument = daoServices.updateCDStatus(consignmentDocument, ConsignmentDocumentStatus.COMPLIANCE_REJECTED)
        cdAuditService.addHistoryRecord(consignmentDocument.id, consignmentDocument.ucrNumber, remarks, "KEBS_REJECT_COMPLIANCE", "Reject compliance request", supervisor)
        return false
    }

    fun swUpdateComplianceStatus(cdUuid: String, compliantStatus: Int, supervisor: String, remarks: String): Boolean {
        // Rejected consignment document after non-compliance status is assigned
        try {
            val consignmentDocument = this.daoServices.findCDWithUuid(cdUuid)
            val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
            // Auto-Reject Approve depending on compliance status
            val cdStatusType: CdStatusTypesEntity?
            if (compliantStatus == 1) {
                cdStatusType = daoServices.findCdStatusCategory("APPROVE")
                consignmentDocument.varField10 = "Consignment Document Approved on Compliance"
                consignmentDocument.approveRejectCdStatus = map.activeStatus
                consignmentDocument.compliantStatus = map.activeStatus
            } else {
                cdStatusType = daoServices.findCdStatusCategory("REJECT")
                consignmentDocument.varField10 = "Consignment Document Rejected on Non-Compliance"
                consignmentDocument.approveRejectCdStatus = map.invalidStatus
                consignmentDocument.compliantStatus = map.invalidStatus
            }
            KotlinLogging.logger { }.info("1: Updating consignment status: ${cdStatusType.typeName}")
            consignmentDocument.approveRejectCdStatusType = cdStatusType
            consignmentDocument.approveRejectCdRemarks = remarks
            consignmentDocument.approveRejectCdDate = Date(Date().time)
            // Update consignment
            daoServices.updateCdDetailsInDB(consignmentDocument, commonDaoServices.findUserByUserName(supervisor))
            // Update SW status
            cdStatusType.statusCode?.let {
                daoServices.submitCDStatusToKesWS(remarks, it, consignmentDocument.version.toString(), consignmentDocument)
                consignmentDocument.approveRejectCdStatusType?.id?.let { it2 ->
                    daoServices.updateCDStatus(consignmentDocument, it2)
                }
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("FAILED TO COMPLIANCE REJECTION STATUS", ex)
        }
        return false
    }

    fun updateStatus(cdUuid: String, cdStatusTypeId: Long, supervisor: String, remarks: String): Boolean {
        try {
            var consignmentDocument = this.daoServices.findCDWithUuid(cdUuid)
            val cdStatusType = daoServices.findCdStatusValue(cdStatusTypeId)
            val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
            // Check if the consignment should be marked as completed
            if (cdStatusType.modificationAllowed != map.activeStatus) {
                consignmentDocument.compliantDate = java.sql.Date(Date().time)
                consignmentDocument.compliantRemarks = remarks
                when (cdStatusType.category?.toUpperCase()) {
                    "APPROVE" -> {
                        consignmentDocument.status = ConsignmentApprovalStatus.APPROVED.code
                        consignmentDocument.compliantStatus = map.activeStatus
                        consignmentDocument.approveRejectCdStatus = map.activeStatus
                        consignmentDocument.varField10 = "Consignment Approved"
                    }
                    "REJECT" -> {
                        consignmentDocument.status = ConsignmentApprovalStatus.REJECTED.code
                        consignmentDocument.approveRejectCdStatus = map.activeStatus
                        consignmentDocument.compliantStatus = map.invalidStatus
                        consignmentDocument.varField10 = "Consignment Rejected"
                    }
                    "QUERY" -> {
                        consignmentDocument.status = ConsignmentApprovalStatus.QUERIED.code
                        consignmentDocument.compliantStatus = map.inactiveStatus
                        consignmentDocument.varField10 = "Waiting for QUERY response"
                    }
                    else -> {
                        consignmentDocument.varField10 = "Consignment ${cdStatusType.category}"
                        consignmentDocument.status = ConsignmentApprovalStatus.REJECTED.code
                        consignmentDocument.compliantStatus = map.inactiveStatus
                    }
                }
            }
            KotlinLogging.logger { }.info("2: Updating consignment status: ${cdStatusType.id} =>${cdStatusType.typeName} =>${consignmentDocument.status}")
            consignmentDocument.approveRejectCdStatusType = cdStatusType
            consignmentDocument.approveRejectCdDate = Date(Date().time)
            consignmentDocument = daoServices.updateCDStatus(consignmentDocument, cdStatusTypeId)
            // Update Local status
            if (cdStatusType.category == "APPROVE" || cdStatusType.category == "REJECT") {
                consignmentDocument = daoServices.updateCDStatus(consignmentDocument, cdStatusTypeId)
                cdAuditService.addHistoryRecord(consignmentDocument.id, consignmentDocument.ucrNumber, remarks, "KEBS DOCUMENT ${cdStatusType.category}", "Consignment ${cdStatusType.category?.toLowerCase()}", username = supervisor)
            } else if (cdStatusType.category == "AMENDMENT") {
                consignmentDocument = daoServices.updateCDStatus(consignmentDocument, cdStatusTypeId)
                cdAuditService.addHistoryRecord(consignmentDocument.id, consignmentDocument.ucrNumber, remarks, "KEBS AMENDMENT REQUEST", "Consignment rejected for amendment", username = supervisor)
            } else if (cdStatusType.category === "QUERY") {
                consignmentDocument = daoServices.updateCDStatus(consignmentDocument, cdStatusTypeId)
                cdAuditService.addHistoryRecord(consignmentDocument.id, consignmentDocument.ucrNumber, remarks, "KEBS QUERY IMPORTER", "Consignment query for importer submitted", username = supervisor)
            } else {
                consignmentDocument = daoServices.updateCDStatus(consignmentDocument, cdStatusTypeId)
                cdAuditService.addHistoryRecord(consignmentDocument.id, consignmentDocument.ucrNumber, remarks, "KEBS DOCUMENT STATUS TO ${cdStatusType.category}", "Consignment document ${cdStatusType.category?.toLowerCase()}", username = supervisor)
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("FAILED TO UPDATE STATUS", ex)
        }
        return false
    }

    fun updateBpmCompletionStatus(cdUuid: String, supervisor: String, remarks: String): Boolean {
        try {
            var consignmentDocument = this.daoServices.findCDWithUuid(cdUuid)
            consignmentDocument.varField10 = "SEND COMPLIANCE STATUS TO SW"
            val usersEntity = commonDaoServices.findUserByUserName(supervisor)
            daoServices.updateCdDetailsInDB(consignmentDocument, usersEntity)
            // Update SW
            consignmentDocument.approveRejectCdStatusType?.statusCode?.let {
                daoServices.submitCDStatusToKesWS(remarks, it, consignmentDocument.version.toString(), consignmentDocument)
            }
            // Local Status
            consignmentDocument.varField10 = "SW COMPLIANCE UPDATED"
            consignmentDocument.approveRejectCdStatusType?.id?.let { it2 ->
                consignmentDocument = daoServices.updateCDStatus(consignmentDocument, it2)
            }
            daoServices.updateCdDetailsInDB(consignmentDocument, usersEntity)
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("FAILED TO UPDATE ON SW", ex)
        }
        return false
    }

    fun assignInspectionOfficer(cdUuid: String, officerId: Long, supervisor: String, remarks: String, selfAssign: Boolean = false) {
        try {
            var consignmentDocument = this.daoServices.findCDWithUuid(cdUuid)
            val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
            val loggedInUser = commonDaoServices.findUserByUserName(supervisor)
            val officer = daoServices.findUserById(officerId)
            KotlinLogging.logger { }.info("${loggedInUser.userName} ASSIGN to ${officer.get().userName}, self $selfAssign")
            consignmentDocument.apply {
                assignedRemarks = remarks
                assignedDate = Date(Date().time)
                assignedStatus = map.activeStatus
                assigner = loggedInUser
                status = ConsignmentApprovalStatus.UNDER_INSPECTION.code
                varField10 = "Assigned IO"
                assignedInspectionOfficer = officer.get()
            }
            if (selfAssign) {
                KotlinLogging.logger { }.info("PICK CONSIGNMENT: ${officer.get().userName}")
                consignmentDocument = this.daoServices.updateCdDetailsInDB(consignmentDocument, officer.get())
                // Update CD status
                daoServices.updateCDStatus(consignmentDocument, ConsignmentDocumentStatus.IO_SELF_ASSIGN)
                cdAuditService.addHistoryRecord(consignmentDocument.id, consignmentDocument.ucrNumber, remarks, "KEBS_SELF_ASSIGNED", "Picked consignment", username = officer.get().userName)
            } else {
                consignmentDocument = this.daoServices.updateCdDetailsInDB(consignmentDocument, loggedInUser)
                // Update CD status
                daoServices.updateCDStatus(consignmentDocument, ConsignmentDocumentStatus.IO_ASSIGNED)
                cdAuditService.addHistoryRecord(consignmentDocument.id, consignmentDocument.ucrNumber, remarks, "KEBS_ASSIGN_IO", "Assign inspection officer to consignment", username = supervisor)
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("ASSIGNMENT FAILED", ex)
        }
    }

    fun reassignInspectionOfficer(cdUuid: String, officerId: Long, supervisor: String, remarks: String) {
        try {
            var consignmentDocument = this.daoServices.findCDWithUuid(cdUuid)
            val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
            val supervisorDetails = commonDaoServices.findUserByUserName(supervisor)
            val officer = daoServices.findUserById(officerId)

            KotlinLogging.logger { }.info("REASSIGN from ${consignmentDocument.assignedInspectionOfficer?.userName} to ${officer.get().userName}")
            consignmentDocument.apply {
                reassignedRemarks = remarks
                assigner = supervisorDetails
                reassignedDate = Date(Date().time)
                reassignedStatus = map.activeStatus
                assignedInspectionOfficer = officer.get()
            }

            consignmentDocument = this.daoServices.updateCdDetailsInDB(consignmentDocument, supervisorDetails)
            // Update CD status
            daoServices.updateCDStatus(consignmentDocument, ConsignmentDocumentStatus.IO_REASSIGNED)
            cdAuditService.addHistoryRecord(consignmentDocument.id, consignmentDocument.ucrNumber, remarks, "KEBS_REASSIGN_IO", "Re-Assign inspection officer : " + officer.get().userName, supervisor)
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("FAILED TO REASSIGN", ex)
        }
    }

    fun checkForAutoTarget(cdUuid: String): Boolean {
        KotlinLogging.logger { }.info("START AUTO TARGET/REJECT PROCESS")
        val consignmentDocument = this.daoServices.findCDWithUuid(cdUuid)
        val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
        //If CD without COR, auto-target
        try {
            consignmentDocument.cdType?.let { cdType ->
                if (cdType.autoTargetStatus == map.activeStatus) {
                    KotlinLogging.logger { }.info("START AUTO TARGET PROCESS")
                    val conditions = cdType.autoTargetCondition?.toUpperCase()?.split(",") ?: listOf()

                    if (conditions.isEmpty() || conditions.contains(consignmentDocument.cdStandardsTwo?.localCocType.orEmpty().toUpperCase())) {
                        with(consignmentDocument) {
                            targetStatus = map.activeStatus
                            targetReason = "Auto Target ${consignmentDocument.cdType?.typeName}"
                            targetApproveStatus = map.activeStatus
                            targetApproveDate = Date(Date().time)
                        }
                        this.daoServices.updateCdDetailsInDB(consignmentDocument, null)
                    } else {
                        KotlinLogging.logger { }.info("Consignment document does not meet condition for auto target")
                    }
                } else if (cdType.autoRejectStatus == map.activeStatus) {
                    KotlinLogging.logger { }.info("START AUTO REJECT PROCESS")
                    // Reject CD due to auto reject
                    val conditions = cdType.autoRejectCondition?.split(",") ?: listOf()
                    if (cdType.autoTargetCondition.isNullOrEmpty() || conditions.contains(consignmentDocument.cdStandardsTwo?.localCocType)) {
                        daoServices.findCdStatusCategory("REJECT").let {
                            this.updateCdStatusOnSw(cdUuid, it.id, "Auto Rejected Consignment ${consignmentDocument.cdType?.typeName}")
                        }
                    } else {
                        KotlinLogging.logger { }.info("Consignment document does not meet condition for auto reject")
                    }
                } else {
                    KotlinLogging.logger { }.info("Consignment document does not have auto reject/target")
                }
            }
            return true
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Check for auto target/reject status failed", ex)
        }
        return false
    }

    fun completeIOAssignment(cdUuid: String, officerId: Long, supervisor: String): Boolean {
        try {
            val consignmentDocument = this.daoServices.findCDWithUuid(cdUuid)
            consignmentDocument.apply {
                diProcessCompletedOn = Timestamp.from(Instant.now())
                diProcessStatus = 0
                varField10 = "IO Assigned"
            }
            commonDaoServices.findUserByUserName(supervisor).let { it1 -> this.daoServices.updateCdDetailsInDB(consignmentDocument, it1) }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("COMPLETE ASSIGNMENT FAILED", ex)
        }
        return true
    }

    fun generateCorForDocument(cdUuid: String, supervisor: String, remarks: String): Boolean {
        try {
            val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
            var consignmentDocument = this.daoServices.findCDWithUuid(cdUuid)

            var loggedInUser = this.commonDaoServices.findUserByUserName(supervisor)
            daoServices.generateCor(consignmentDocument, map, loggedInUser).let { corDetails ->
                // Update CD
                daoServices.updateCDStatus(
                        consignmentDocument,
                        ConsignmentDocumentStatus.COR_ISSUED
                )
                corDetails.varField10 = "COMPLIANCE APPROVED, COR GENERATED"
                daoServices.saveCorDetails(corDetails)
                // Send to KENTRADE
                daoServices.submitCoRToKesWS(corDetails)
                //Send Cor to importer
                consignmentDocument.corNumber = corDetails.corNumber
                consignmentDocument.compliantStatus = map.activeStatus
                consignmentDocument.localCocOrCorStatus = map.activeStatus
                consignmentDocument.status = ConsignmentApprovalStatus.APPROVED.code
                consignmentDocument = this.daoServices.updateCDStatus(consignmentDocument, ConsignmentDocumentStatus.COMPLIANCE_APPROVED)
                //Send email to importer
                consignmentDocument.cdImporter?.let {
                    daoServices.findCDImporterDetails(it)
                }?.let { importer ->
                    val corFile = makeCorFile(corDetails.id)
                    importer.email?.let { daoServices.sendLocalCorReportEmail(it, corFile) }
                }

            }
            KotlinLogging.logger { }.info("COR GENERATION SUCCESS")
            return true
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("COR GENERATION FAILED", ex)
            throw ex
        }
    }

    fun rejectCorGeneration(cdUuid: String, blacklistId: Long, supervisor: String, remarks: String): Boolean {
        KotlinLogging.logger { }.info("UPDATE COR GENERATION REJECTION: ${cdUuid}")
        try {
            val consignmentDocument = this.daoServices.findCDWithUuid(cdUuid)
            consignmentDocument.compliantStatus = 0
            consignmentDocument.localCocOrCorStatus = 0
            // Update CD status
            daoServices.updateCDStatus(consignmentDocument, ConsignmentDocumentStatus.COMPLIANCE_REJECTED)
            daoServices.updateCdDetailsInDB(consignmentDocument, null)
            this.cdAuditService.addHistoryRecord(consignmentDocument.id!!, remarks, "REJECT COR", "COR of ${cdUuid} has been rejected by ${supervisor}", supervisor)
            KotlinLogging.logger { }.info("COR REJECTION UPDATED: ${cdUuid}")
            return true
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("REJECTION UPDATE STATUS", ex)
        }
        return false
    }

    fun rejectUserBlacklisting(cdUuid: String, blacklistId: Long, supervisor: String, remarks: String): Boolean {
        KotlinLogging.logger { }.info("UPDATE BLACKLIST REJECTION: ${cdUuid}")
        try {
            val consignmentDocument = this.daoServices.findCDWithUuid(cdUuid)
            consignmentDocument.blacklistStatus = 0
            consignmentDocument.blacklistApprovedRemarks = remarks
            this.commonDaoServices.getLoggedInUser()?.let { it1 -> this.daoServices.updateCdDetailsInDB(consignmentDocument, it1) }
            this.cdAuditService.addHistoryRecord(consignmentDocument.id!!, remarks, "REJECT BLACKLISTING", "Blacklisting of ${cdUuid} has been rejected by ${supervisor}", supervisor)
            KotlinLogging.logger { }.info("BLACKLIST REJECTION UPDATED: ${cdUuid}")
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("REJECTION UPDATE STATUS", ex)
        }
        return true
    }

    fun approveUserBlacklisting(cdUuid: String, blacklistId: Int, supervisor: String, remarks: String): Boolean {
        KotlinLogging.logger { }.info("UPDATE BLACKLIST APPROVAL: ${cdUuid}")
        try {
            val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
            val consignmentDocument = this.daoServices.findCDWithUuid(cdUuid)
            consignmentDocument.blacklistStatus = 0
            consignmentDocument.blacklistId = blacklistId
            consignmentDocument.blacklistApprovedRemarks = remarks
            val loggedInUser = this.commonDaoServices.findUserByUserName(supervisor)
            this.commonDaoServices.getLoggedInUser()?.let { it1 -> this.daoServices.updateCdDetailsInDB(consignmentDocument, it1) }
            // Start blacklist
            when (consignmentDocument.blacklistId) {
                applicationMapProperties.mapRiskProfileImporter -> {
                    val importerDetailsEntity =
                            consignmentDocument.cdImporter?.let { daoServices.findCDImporterDetails(it) }
                    val allRemarksAdded =
                            """[${remarks}] and [${consignmentDocument.blacklistRemarks}] and [The UCR Number = ${consignmentDocument.ucrNumber}]"""
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
                            "[${remarks}] and [${consignmentDocument.blacklistRemarks}] and [The UCR Number = ${consignmentDocument.ucrNumber}]"
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
            // Log blacklisting
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
            // Add AUDIT
            this.cdAuditService.addHistoryRecord(consignmentDocument.id!!, remarks, "APPROVE BLACKLISTING", "Blacklisting of ${cdUuid} has been approved by ${supervisor}", supervisor)
            KotlinLogging.logger { }.info("BLACKLIST APPROVAL UPDATED: ${cdUuid}")
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("APPROVAL UPDATE STATUS", ex)
        }
        return true
    }

    fun swUpdateBlacklistStatus(cdUuid: String, blacklistId: Long, supervisor: String, remarks: String): Boolean {
        // Send lacklist email
        try {
            val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
            val consignmentDocument = this.daoServices.findCDWithUuid(cdUuid)
            val loggedInUser = this.commonDaoServices.findUserByUserName(supervisor)
            val payload =
                    "BlackList Consignment Document [blacklistStatus= ${1}, blacklistRemarks= ${remarks}]"
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
            return true
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("FAILED TO BLACKLIST USER", ex)
        }
        return false
    }

    fun consignmentChecks(cdUuid: String): MutableMap<String, Any> {
        KotlinLogging.logger { }.info("Consignment Checks: ${cdUuid}")
        val consignmentDocument = this.daoServices.findCDWithUuid(cdUuid)
        val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
        val processProperties = mutableMapOf<String, Any>()
        consignmentDocument.cdType?.let {
            if (it.localCocStatus == map.activeStatus || it.localCocStatus == map.activeStatus) {
                // Update status
                val dd = (consignmentDocument.localCocOrCorStatus == map.activeStatus)
                processProperties.put("localCocGenerated", dd)
                if (dd) {
                    processProperties.put("checkRemarks", "CoC/Cor generated")
                } else {
                    processProperties.put("checkRemarks", "Please generate CoC/Cor First")
                }
            } else {
                // No Local CoC or CoR required
                processProperties.put("localCocGenerated", true)
            }
        }
        return processProperties
    }

    fun rejectTargeting(cdUuid: String, remarks: String, supervisor: String): Boolean {
        KotlinLogging.logger { }.info("UPDATE TARGETING REJECTION: ${cdUuid}")
        try {
            val consignmentDocument = this.daoServices.findCDWithUuid(cdUuid)
            val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
            consignmentDocument.varField9 = null
            consignmentDocument.varField10 = "Targeting rejected"
            consignmentDocument.targetApproveStatus = 0
            consignmentDocument.status = ConsignmentApprovalStatus.UNDER_INSPECTION.code
            consignmentDocument.targetStatus = map.invalidStatus
            consignmentDocument.targetApproveRemarks = remarks
            consignmentDocument.targetApproveDate = Date(Date().time)
            // Update status as rejected
            daoServices.updateCDStatus(consignmentDocument, ConsignmentDocumentStatus.TARGET_REJECTED)
            this.daoServices.updateCdDetailsInDB(consignmentDocument, null)
            this.cdAuditService.addHistoryRecord(consignmentDocument.id!!, consignmentDocument.ucrNumber, remarks, "REJECT TARGETING", "Targting of ${cdUuid} has been rejected by ${supervisor}", supervisor)
            KotlinLogging.logger { }.info("TARGET REJECTION UPDATED: ${cdUuid}")
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("REJECTION UPDATE STATUS", ex)
        }
        return true
    }

    fun rejectCoCGeneration(cdUuid: String, remarks: String, supervisor: String): Boolean {
        KotlinLogging.logger { }.info("UPDATE COC REJECTION: ${cdUuid}")
        try {
            val consignmentDocument = this.daoServices.findCDWithUuid(cdUuid)
            consignmentDocument.varField9 = null
            consignmentDocument.varField10 = null
            this.commonDaoServices.getLoggedInUser()?.let { it1 -> this.daoServices.updateCdDetailsInDB(consignmentDocument, it1) }
            this.cdAuditService.addHistoryRecord(consignmentDocument.id!!, remarks, "REJECT COC GENERATION", "CoC generation for ${cdUuid} has been rejected by ${supervisor}", supervisor)
            KotlinLogging.logger { }.info("COC REJECTION UPDATED: ${cdUuid}")
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("REJECTION UPDATE STATUS", ex)
        }
        return true
    }

    fun swScheduledTargeting(cdUuid: String, remarks: String, supervisor: String?): Boolean {
        KotlinLogging.logger { }.info("REQUESTING TARGETING SCHEDULE: ${cdUuid}")
        try {
            val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
            var consignmentDocument = this.daoServices.findCDWithUuid(cdUuid)
            consignmentDocument.targetStatus = map.activeStatus
            consignmentDocument.status = ConsignmentApprovalStatus.UNDER_INSPECTION.code
            consignmentDocument.varField10 = "Target approved awaiting inspection"
            consignmentDocument.targetApproveDate = Date(Date().time)
            consignmentDocument.targetApproveRemarks = remarks
            consignmentDocument.targetApproveDate = Date(Date().time)
            consignmentDocument = if (supervisor == null) {
                this.daoServices.updateCdDetailsInDB(consignmentDocument, null)
            } else {
                this.daoServices.updateCdDetailsInDB(consignmentDocument, this.commonDaoServices.findUserByUserName(supervisor))
            }
            daoServices.updateCDStatus(consignmentDocument, ConsignmentDocumentStatus.TARGET_APPROVED)
            this.cdAuditService.addHistoryRecord(consignmentDocument.id!!, consignmentDocument.ucrNumber, remarks, "APPROVE TARGETING", "Targeting of ${consignmentDocument.ucrNumber} has been approved by ${supervisor}", supervisor)
            // Submit consignment to Single/Window
            KotlinLogging.logger { }.info("REQUESTED TARGETING SCHEDULE: ${cdUuid}")
            daoServices.submitCDStatusToKesWS("OH", "OH", consignmentDocument.version.toString(), consignmentDocument)
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("REJECTION UPDATE STATUS", ex)
        }
        return true
    }

    @Transactional
    fun swGenerateCoC(cdUuid: String, remarks: String, supervisor: String): Boolean {
        KotlinLogging.logger { }.info("REQUESTING COC: ${cdUuid}")
        try {
            var consignmentDocument = this.daoServices.findCDWithUuid(cdUuid)
            val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
            val loggedInUser = commonDaoServices.findUserByUserName(supervisor)
            val localCoc = daoServices.createLocalCoc(loggedInUser, consignmentDocument, map, remarks, "A")
            consignmentDocument = daoServices.updateCDStatus(
                    consignmentDocument,
                    ConsignmentDocumentStatus.COC_ISSUED
            )
            consignmentDocument.varField10 = "COMPLIANCE APPROVED, COC GENERATED"
            consignmentDocument.localCocOrCorStatus = map.activeStatus
            consignmentDocument.compliantStatus = map.activeStatus
            // Generate NCR if applicable
            daoServices.createLocalNcr(loggedInUser, consignmentDocument, map, remarks, "A")?.let {
                consignmentDocument.ncrNumber = it.cocNumber
                consignmentDocument
            }
            consignmentDocument.cocNumber = localCoc.cocNumber
            consignmentDocument.status = ConsignmentApprovalStatus.APPROVED.code
            consignmentDocument = this.daoServices.updateCDStatus(consignmentDocument, ConsignmentDocumentStatus.COMPLIANCE_APPROVED)

            KotlinLogging.logger { }.info("Local CoC = ${localCoc.id}")
            // Send to single window
            daoServices.sendLocalCoc(localCoc)
            //Generate PDF File & send to importer
            consignmentDocument.cdImporter?.let {
                daoServices.findCDImporterDetails(it)
            }?.let { importer ->
                val fileName = makeCocOrCoiFile(localCoc.id)
                importer.email?.let { daoServices.sendLocalCocReportEmail(it, fileName) }
            }
            this.daoServices.updateCdDetailsInDB(consignmentDocument, null)
            KotlinLogging.logger { }.info("GENERATE COC/COI: ${cdUuid}")
            return true
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("REJECTION UPDATE STATUS", ex)
        }
        return false
    }

    fun makeCocOrCoiFile(cocCoiId: Long): String {
        val data = this.createLocalCocReportMap(cocCoiId)
        data["imagePath"] = commonDaoServices.resolveAbsoluteFilePath(applicationMapProperties.mapKebsLogoPath)

        val items = data["dataSources"] as HashMap<String, List<Any>>

        val pdfStream: ByteArrayOutputStream
        val cocType = data["CoCType"] as String
        val fileName = "/tmp/LOCAL-${cocType.toUpperCase()}-".plus(data["CocNo"] as String).plus(".pdf")
        if ("COI".equals(cocType, true)) {
            pdfStream = reportsDaoService.extractReportMapDataSource(data, "classpath:reports/LocalCoiReport.jrxml", items)
        } else {
            pdfStream = reportsDaoService.extractReportMapDataSource(data, applicationMapProperties.mapReportLocalCocPath, items)
        }
        reportsDaoService.createFileFromBytes(pdfStream, fileName)
        return fileName
    }

    fun makeCorFile(corId: Long): String {
        val data = createLocalCorReportMap(corId)
        data["imagePath"] = commonDaoServices.resolveAbsoluteFilePath(applicationMapProperties.mapKebsLogoPath)
        val corNumber = data["corNumber"] as String
        val fileName = "/tmp/LOCAL-COR-${corNumber}.pdf"
        val pdfStream = reportsDaoService.extractReportEmptyDataSource(data, "classpath:reports/LocalCoRReport.jrxml")
        reportsDaoService.createFileFromBytes(pdfStream, fileName)
        return fileName
    }


    @Transactional
    fun swGenerateCoI(cdUuid: String, remarks: String, supervisor: String): Boolean {
        KotlinLogging.logger { }.info("REQUESTING COI GENERATION: ${cdUuid}")
        try {
            var consignmentDocument = this.daoServices.findCDWithUuid(cdUuid)
            val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
            val loggedInUser = commonDaoServices.findUserByUserName(supervisor)
            val localCoi = daoServices.createLocalCoi(loggedInUser, consignmentDocument, map, remarks, "D")
            consignmentDocument = daoServices.updateCDStatus(consignmentDocument, ConsignmentDocumentStatus.COI_ISSUED)
            // Update CoI generation
            consignmentDocument.localCoi = map.activeStatus
            consignmentDocument.localCoiRemarks = remarks
            consignmentDocument.compliantStatus = map.activeStatus
            consignmentDocument.cocNumber = localCoi.cocNumber
            consignmentDocument.varField10 = "COMPLIANCE APPROVED, COI GENERATED"
            // Generate NCR if applicable
            daoServices.createLocalNcr(loggedInUser, consignmentDocument, map, remarks, "A")?.let {
                consignmentDocument.ncrNumber = it.cocNumber
                consignmentDocument
            }
            // Send coi to importer
            consignmentDocument.cdImporter?.let {
                daoServices.findCDImporterDetails(it)
            }?.let { importer ->
                val fileName = makeCocOrCoiFile(localCoi.id)
                importer.email?.let { daoServices.sendLocalCocReportEmail(it, fileName) }
            }
            consignmentDocument.status = ConsignmentApprovalStatus.APPROVED.code
            consignmentDocument = this.daoServices.updateCDStatus(consignmentDocument, ConsignmentDocumentStatus.COMPLIANCE_APPROVED)
            // Send to SW
            daoServices.sendLocalCoi(localCoi)
            this.commonDaoServices.getLoggedInUser()?.let { it1 -> this.daoServices.updateCdDetailsInDB(consignmentDocument, it1) }
            KotlinLogging.logger { }.info("GENERATED COI: ${cdUuid}")
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("REJECTION UPDATE STATUS", ex)
        }
        return true
    }

    fun clearItemProcess(mvInspectionId: Long, cdItemId: Long): Boolean {
        try {
            val itemDetails = this.daoServices.findItemWithItemID(cdItemId)
            itemDetails.inspectionProcessInstanceId = null
            itemDetails.inspectionProcessStatus = 0
            itemDetails.inspectionProcessCompletedOn = commonDaoServices.getTimestamp()
            itemDetails.varField9 = null
            itemDetails.varField8 = null
            itemDetails.varField7 = null
            this.cdItemsDetailsRepository.save(itemDetails)
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("FAILED to clear process", ex)
        }
        return false
    }

    /**
     * Clear consignment document current process information
     */
    fun clearCurrentProcess(cdUuid: String) {
        try {
            val consignmentDocument = this.daoServices.findCDWithUuid(cdUuid)
            consignmentDocument.diProcessStatus = 0
            consignmentDocument.diProcessInstanceId = null
            consignmentDocument.diProcessCompletedOn = Timestamp.from(Instant.now())
            this.daoServices.updateCdDetailsInDB(consignmentDocument, null)
            KotlinLogging.logger { }.info("CLEARED PROCESS")
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("UPDATE STATUS", ex)
        }
    }

    fun applicationConfigurations(appId: Int): ApiResponseModel {
        val response = ApiResponseModel()
        response.data = this.commonDaoServices.serviceMapDetails(appId)
        response.message = "Success"
        response.responseCode = ResponseCodes.SUCCESS_CODE
        return response
    }

    fun loadCDTypes(): ApiResponseModel {
        val response = ApiResponseModel()
        response.data = this.cdTypesRepo.findByStatus(1)?.let { ConsignmentDocumentTypesEntityDao.fromList(it) }
        response.message = "Success"
        response.responseCode = ResponseCodes.SUCCESS_CODE
        return response
    }

    fun loadMinistryStations(): ApiResponseModel {
        val response = ApiResponseModel()
        response.data = this.ministryStationRepo.findByStatus(1)?.let { MinistryStationEntityDao.fromList(it) }
        response.message = "Success"
        response.responseCode = ResponseCodes.SUCCESS_CODE
        return response
    }

    fun updateCdStatusOnSw(cdUuid: String, cdStatusTypeId: Long, remarks: String) {
        val consignmentDocument = this.daoServices.findCDWithUuid(cdUuid)
        KotlinLogging.logger { }.info { "approveRejectCdStatusType = ${cdStatusTypeId}" }
        val cdStatusType = cdStatusTypeId.let { daoServices.findCdStatusValue(it) }
        cdStatusType.let { cdStatus ->
            with(consignmentDocument) {
                approveRejectCdStatusType = cdStatus
                approveRejectCdDate = Date(Date().time)
                approveRejectCdRemarks = remarks
            }
            //updating of Details in DB
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            consignmentDocument.status = ConsignmentApprovalStatus.UNDER_INSPECTION.code
            val updatedCDDetails = daoServices.updateCdDetailsInDB(consignmentDocument, loggedInUser)
            //Send Approval/Rejection message To Single Window
            consignmentDocument.approveRejectCdRemarks?.let { it1 ->
                cdStatus.statusCode?.let {
                    daoServices.submitCDStatusToKesWS(it1, it, consignmentDocument.version.toString(), consignmentDocument)
                }
                updatedCDDetails.approveRejectCdStatusType?.id?.let { it2 -> daoServices.updateCDStatus(updatedCDDetails, it2) }
            }
            cdAuditService.addHistoryRecord(updatedCDDetails.id, consignmentDocument.ucrNumber, remarks, "KEBS_${cdStatus.category?.toUpperCase()}", "${cdStatus.category?.capitalize()} consignment")
        }
    }

    fun updateBpmCompletionStatus(cdUuid: String) {
        val consignmentDocument = this.daoServices.findCDWithUuid(cdUuid)
//        Update Check CD task in Bpm
        consignmentDocument.assigner?.id?.let { it2 ->
            consignmentDocument.approveRejectCdStatusType?.category?.let { cdDecision ->
                consignmentDocument.id?.let {
                    diBpmn.diCheckCdComplete(it, it2, cdDecision)
                }
            }
        }
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun saveUploadedCsvFileAndSendToKeSWS(
            docFile: MultipartFile,
            upLoads: DiUploadsEntity,
            loggedInUser: UsersEntity,
            map: ServiceMapsEntity,
            partnerId: Long?
    ) {
        val endsWith = docFile.originalFilename?.endsWith(".txt")
        if (!(docFile.contentType == "text/csv" || endsWith == true)) {
            throw InvalidValueException("Incorrect file type received, try again later")
        }
        var separator = ','
        if ("TSV".equals(upLoads.fileType)) {
            KotlinLogging.logger { }.info("TAB SEPARATED DATA")
            separator = '\t'
        } else {
            KotlinLogging.logger { }.info("COMMA SEPARATED DATA")
        }

        daoServices.saveUploads(
                upLoads,
                docFile,
                upLoads.documentType ?: "File Upload",
                loggedInUser,
                map,
                null,
                null
        )
        val targetReader: Reader = InputStreamReader(ByteArrayInputStream(docFile.bytes))
        when (upLoads.documentType) {
            "COC" -> {
                //Generate PDF File &
                val cocs = service.readCocFileFromController(separator, targetReader)
                GlobalScope.launch(Dispatchers.IO) {
                    delay(500L)
                    processCocDocuments(cocs, loggedInUser, partnerId)
                }
            }
            "COR" -> {
                val cors = service.readCorFileFromController(separator, targetReader)
                GlobalScope.launch(Dispatchers.IO) {
                    delay(500L)
                    processCorDocuments(cors, loggedInUser, partnerId)
                }
            }
            else -> {
                throw ExpectedDataNotFound("Invalid document type: ${upLoads.documentType}")
            }
        }
    }

    fun processCocDocuments(cocs: List<CocsItemsEntityDto>, loggedInUser: UsersEntity, partnerId: Long?) {
        val uniqueCoc = cocs.map { it.cocNumber }.distinct()
        uniqueCoc.forEach {
            it
                    ?.let { u ->
                        KotlinLogging.logger {}.info("Unique COC: $u")
                        val coc = cocs.firstOrNull { it.cocNumber == u }
                        if (coc == null) {
                            return
                        }
                        val documentType = when {
                            coc.clean.equals("Y", true) -> "NCR"
                            else -> "COC"
                        }
                        cocsRepository.findByUcrNumberAndCocType(coc.ucrNumber ?: "NA", documentType)
                                ?.let {
                                    KotlinLogging.logger {}.warn("CoC with UCR number already exists: ${coc.ucrNumber}")
                                }
                                ?: run {
                                    var entity = CocsEntity().apply {
                                        cocNumber = coc.cocNumber
                                        idfNumber = coc.idfNumber
                                        rfiNumber = coc.rfiNumber
                                        ucrNumber = coc.ucrNumber
                                        rfcDate = coc.rfcDate
                                        documentsType = "F"
                                        cocIssueDate = coc.cocIssueDate
                                        clean = coc.clean
                                        shipmentGrossWeight = coc.shipmentGrossWeight ?: "0.0"
                                        importerPin = coc.importerPin ?: "NA"
                                        shipmentQuantityDelivered = coc.shipmentQuantityDelivered ?: "0"
                                        cocRemarks = coc.cocRemarks ?: "NA"
                                        issuingOffice = coc.issuingOffice
                                        importerName = coc.importerName
                                        importerPin = coc.importerPin ?: "NA"
                                        importerAddress1 = coc.importerAddress1
                                        importerAddress2 = coc.importerAddress2
                                        importerCity = coc.importerCity
                                        importerCountry = coc.importerCountry
                                        importerZipCode = coc.importerZipCode
                                        importerTelephoneNumber = coc.importerTelephoneNumber
                                        importerFaxNumber = coc.importerFaxNumber
                                        importerEmail = coc.importerEmail
                                        exporterName = coc.exporterName ?: "UNDEFINED"
                                        exporterPin = coc.exporterPin ?: "UNDEFINED"
                                        exporterAddress1 = coc.exporterAddress1 ?: "UNDEFINED"
                                        exporterAddress2 = coc.exporterAddress2 ?: "UNDEFINED"
                                        exporterCity = coc.exporterCity ?: "UNDEFINED"
                                        exporterCountry = coc.exporterCountry ?: "UNDEFINED"
                                        exporterZipCode = coc.exporterZipCode ?: "UNDEFINED"
                                        exporterTelephoneNumber = coc.exporterTelephoneNumber ?: "UNDEFINED"
                                        exporterFaxNumber = coc.exporterFaxNumber ?: "UNDEFINED"
                                        exporterEmail = coc.exporterEmail ?: "UNDEFINED"
                                        placeOfInspection = coc.placeOfInspection ?: "UNDEFINED"
                                        dateOfInspection =
                                                coc.dateOfInspection ?: Timestamp.from(Instant.now())
                                        portOfDestination = coc.portOfDestination ?: "UNDEFINED"
                                        shipmentMode = coc.shipmentMode ?: "UNDEFINED"
                                        countryOfSupply = coc.countryOfSupply ?: "UNDEFINED"
                                        finalInvoiceFobValue = coc.finalInvoiceFobValue
                                        finalInvoiceCurrency = coc.finalInvoiceCurrency
                                        finalInvoiceExchangeRate = coc.finalInvoiceExchangeRate
                                        finalInvoiceCurrency = coc.finalInvoiceCurrency ?: "UNDEFINED"
                                        finalInvoiceDate =
                                                coc.finalInvoiceDate ?: Timestamp.from(Instant.now())
                                        shipmentPartialNumber = coc.shipmentPartialNumber
                                        shipmentSealNumbers = coc.shipmentSealNumbers ?: "UNDEFINED"
                                        route = coc.route ?: "UNDEFINED"
                                        cocType = documentType
                                        productCategory = coc.productCategory ?: "UNDEFINED"
                                        status = 1L
                                        createdBy = loggedInUser.userName
                                        createdOn = Timestamp.from(Instant.now())
                                        partner = partnerId
                                    }

                                    if (coc.placeOfInspection.isNullOrEmpty()) {
                                        entity.placeOfInspection = "UNDEFINED"
                                    } else {
                                        entity.placeOfInspection = coc.placeOfInspection.toString()
                                    }
                                    if (entity.cocRemarks.isNullOrEmpty()) {
                                        entity.cocRemarks = "NA"
                                    }

                                    entity = cocsRepository.save(entity)
                                    cocs.filter { dto -> dto.cocNumber == u }.forEach { cocItems ->
                                        val itemEntity = CocItemsEntity().apply {
                                            cocId = entity.id
                                            shipmentLineNumber = cocItems.shipmentLineNumber
                                            shipmentLineHscode = cocItems.shipmentLineHscode ?: "UNDEFINED"
                                            shipmentLineQuantity = BigDecimal.valueOf(cocItems.shipmentLineQuantity)
                                            shipmentLineUnitofMeasure = cocItems.shipmentLineUnitofMeasure
                                                    ?: "UNDEFINED"
                                            shipmentLineDescription = cocItems.shipmentLineDescription ?: "UNDEFINED"
                                            shipmentLineVin = cocItems.shipmentLineVin ?: "UNDEFINED"
                                            shipmentLineStickerNumber = cocItems.shipmentLineStickerNumber
                                                    ?: "UNDEFINED"
                                            shipmentLineIcs = cocItems.shipmentLineIcs ?: "UNDEFINED"
                                            shipmentLineStandardsReference =
                                                    cocItems.shipmentLineStandardsReference ?: "UNDEFINED"
                                            shipmentLineRegistration = cocItems.shipmentLineRegistration ?: "UNDEFINED"
                                            status = 1
                                            createdBy = loggedInUser.userName
                                            createdOn = Timestamp.from(Instant.now())
                                            shipmentLineBrandName = "UNDEFINED"

                                        }
                                        cocItemsRepository.save(itemEntity)
                                    }
                                    // Send to KENTRADE
                                    KotlinLogging.logger { }.info("SEND FOREIGN COC: ${entity.cocNumber}")
                                    // Check if NCR before sending
                                    daoServices.sendLocalCoc(entity)
                                }

                    }
                    ?: KotlinLogging.logger { }.info("Empty value")
        }
    }

    fun processCorDocuments(cors: List<CorItemsEntityDto>, loggedInUser: UsersEntity, partnerId: Long?) {
        cors.forEach { cor ->
            corRepository.findByChasisNumber(cor.chassisNumber ?: "")
                    ?.let {
                        KotlinLogging.logger {}.info("CoR with chassis number already exists: ${cor.chassisNumber}")
                    }
                    ?: run {
                        var entity = CorsBakEntity().apply {
                            corNumber = cor.corNumber
                            corIssueDate = cor.dateOfIssue
                            countryOfSupply = cor.countryOfSupply
                            inspectionCenter = cor.inspectionCenter
                            applicationBookingDate = cor.applicationBookingDate
                            inspectionDate = cor.inspectionDate
                            make = cor.vehicleMake
                            typeOfVehicle = "UNKNOWN"
                            typeOfBody = "UNKNOWN"
                            bodyColor = "UNKNOWN"
                            customsIeNo = "UNKNOWN"
                            fuelType = "UNKNOWN"
                            transmission = "UNKNOWN"
                            inspectionFee = 0.0
                            inspectionFeeCurrency = "NA"
                            inspectionFeePaymentDate = Timestamp.from(Instant.now())
                            inspectionOfficer = loggedInUser.userName ?: "NA"
                            tareWeight = 0.0
                            grossWeight = 0.0
                            model = cor.vehicleModel
                            chasisNumber = cor.chassisNumber
                            engineNumber = cor.engineNumber
                            engineCapacity = cor.engineCapacity
                            yearOfFirstRegistration = cor.yearOfFirstReg ?: "UNDEFINED"
                            yearOfManufacture = cor.yearOfManufacture
                            inspectionMileage = cor.millage.toString()
                            unitsOfMileage = cor.millageUnit
                            inspectionRemarks = cor.remarks ?: "UNDEFINED"
                            exporterName = "UNDEFINED"
                            exporterAddress1 = "UNDEFINED"
                            exporterAddress2 = "UNDEFINED"
                            exporterEmail = "UNDEFINED"
                            previousCountryOfRegistration = cor.countryOfSupply
                            previousRegistrationNumber = "UNKNOWN"
                            documentsType = "F"
                            status = 1
                            createdBy = loggedInUser.userName
                            createdOn = Timestamp.from(Instant.now())
                            partner = partnerId
                        }
                        if (entity.inspectionRemarks.isNullOrEmpty()) {
                            entity.inspectionRemarks = "NA"
                        }
                        entity = corRepository.save(entity)
                        // Submit to cor
                        KotlinLogging.logger { }.info("SEND FOREIGN COR: ${entity.chasisNumber}")
                        this.daoServices.submitCoRToKesWS(entity)
                    }
        }
    }

    fun applicationTypes(): ApiResponseModel {
        val response = ApiResponseModel()
        response.responseCode = ResponseCodes.SUCCESS_CODE
        response.message = "Success"
        response.data = this.importerDaoServices.findDiApplicationTypes(1)
        return response
    }


    @Transactional
    fun consignmentDocumentDetails(cdUuid: String, isSupervisor: Boolean, isInspectionOfficer: Boolean): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
            var cdDetails = daoServices.findCDWithUuid(cdUuid)
            // When IO requests for details, they are are auto assigned
            if (isInspectionOfficer && cdDetails.assignedInspectionOfficer == null) {
                this.selfAssign(cdDetails)
                // Reload CD on self assign BPM
                response.responseCode = ResponseCodes.RELOAD_PAGE
                response.message = "Consignment Assigned reload details"
            } else {
                // Workaround for consignment that are already received and not linked. Maybe removed in future
                if (!StringUtils.hasLength(cdDetails.manifestNumber)) {
                    this.daoServices.linkManifestWithConsignment(null, cdDetails.ucrNumber, false)
                    cdDetails = daoServices.findCDWithUuid(cdUuid)
                }
                // Load inspection details
                response.data = loadCDDetails(cdDetails, map, isSupervisor, isInspectionOfficer)
                response.responseCode = ResponseCodes.SUCCESS_CODE
                response.message = "Consignment Document"
            }

        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("FAILED TO LOAD DOCUMENT", ex)
            response.data = null
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Record not found"
        }
        return response
    }

    fun deleteConsignmentDocumentAttachments(attachmentId: Long): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val attachment = iDIUploadsRepo.findById(attachmentId)
            if (attachment.isPresent) {
                iDIUploadsRepo.delete(attachment.get());
                response.responseCode = ResponseCodes.SUCCESS_CODE
                response.message = "Record deleted"
            } else {
                response.responseCode = ResponseCodes.NOT_FOUND
                response.message = "Record not found"
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("DELETE ATTACHMENTS", ex)
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Delete failed"
        }
        return response
    }

    fun consignmentDocumentAttachments(cdUuid: String): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val cdDetails = daoServices.findCDWithUuid(cdUuid)
            response.data = daoServices.findAllAttachmentsByCd(cdDetails)?.let { DiUploadsEntityDao.fromList(it) }
            response.responseCode = ResponseCodes.SUCCESS_CODE
            response.message = "Consignment Document"
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("LIST ATTACHMENTS", ex)
            response.data = null
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Record not found"
        }
        return response
    }

    fun consignmentDocumentItemDetails(cdItemUuid: String): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val dataMap = mutableMapOf<String, Any?>()
            val cdItemDetails = daoServices.findItemWithUuid(cdItemUuid)
            dataMap.put("cd_type", cdItemDetails.cdDocId?.cdType?.let { ConsignmentDocumentTypesEntityDao.fromEntity(it) })
            dataMap.put("item_non_standard", daoServices.findCdItemNonStandardByItemID(cdItemDetails)?.let { CdItemNonStandardEntityDto.fromEntity(it) })
            dataMap.put("cd_item", CdItemDetailsDao.fromEntity(cdItemDetails, true))
            // Check Certificate of Roadworthiness(CoR)
            cdItemDetails.cdDocId?.let { itemType ->
                dataMap.put("cd_details", ConsignmentDocumentDao.fromEntity(itemType))
                // Standard
                itemType.cdStandard?.let {
                    dataMap.put("cd_standards", CdStandardsEntityDao.fromEntity(it))
                }
            }

            response.data = dataMap
            response.responseCode = ResponseCodes.SUCCESS_CODE
            response.message = "Success"
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error { ex }
            response.message = "Record not found"
            response.responseCode = ResponseCodes.NOT_FOUND
        }
        return response
    }

    fun consignmentDocumentInvoiceDetails(cdUuid: String): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
            val cdDetails = daoServices.findCDWithUuid(cdUuid)
            val itemListWithDemandNote = daoServices.findCDItemsListWithCDIDAndDemandNoteStatus(cdDetails, map)
            response.data = itemListWithDemandNote
            response.message = "Success"
            response.responseCode = ResponseCodes.SUCCESS_CODE
        } catch (e: Exception) {
            KotlinLogging.logger { }.error { e }
            response.message = "Failed"
            response.responseCode = ResponseCodes.FAILED_CODE
        }
        return response
    }

    private fun loadCDDetails(cdDetails: ConsignmentDocumentDetailsEntity,
                              map: ServiceMapsEntity,
                              isSupervisor: Boolean,
                              isInspectionOfficer: Boolean): MutableMap<String, Any?> {
        val dataMap = mutableMapOf<String, Any?>()
        // Importer details
        try {
            if (cdDetails.cdImporter != null) {
                val cdImporter = daoServices.findCDImporterDetails(cdDetails.cdImporter!!)
                var riskProfileImporter = false
                cdImporter.pin?.let {
                    riskProfileDaoService.findImportersInRiskProfile(it, map.activeStatus).let { riskImporter ->
                        when {
                            riskImporter != null -> {
                                riskProfileImporter = true
                            }
                        }
                    }
                    dataMap.put("cd_importer", cdImporter)
                    dataMap.put("risk_profile_importer", riskProfileImporter)
                }
            } else {
                dataMap.put("risk_profile_importer", false)
                dataMap.put("cd_importer", null)
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error { ex }
            dataMap.put("risk_profile_importer", false)
            dataMap.put("cd_importer", null)
        }
        // Consignee details
        try {
            if (cdDetails.cdConsignee != null) {
                val cdConsignee = cdDetails.cdConsignee?.let { daoServices.findCdConsigneeDetails(it) }
                var riskProfileConsignee = false
                cdConsignee?.pin?.let {
                    riskProfileDaoService.findConsigneeInRiskProfile(it, map.activeStatus).let { riskConsignee ->
                        when {
                            riskConsignee != null -> {
                                riskProfileConsignee = true
                            }
                        }
                    }
                    dataMap.put("cd_consignee", cdConsignee)
                    dataMap.put("risk_profile_consignee", riskProfileConsignee)
                }
            } else {
                dataMap.put("cd_consignee", null)
                dataMap.put("risk_profile_consignee", false)
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error { ex }
        }
        // Exporter details
        try {
            cdDetails.cdExporter?.let { cdExporterID ->
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
                dataMap.put("cd_exporter", cdExporter)
                dataMap.put("risk_profile_exporter", riskProfileExporter)
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error { ex }
            dataMap.put("cd_exporter", null)
            dataMap.put("risk_profile_exporter", false)
        }
        // Consignor ID
        try {
            cdDetails.cdConsignor?.let { consignorId ->
                val cdConsignor = daoServices.findCdConsignorDetails(consignorId)
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
                dataMap.put("cd_consignor", cdConsignor)
                dataMap.put("risk_profile_consignor", riskProfileConsignor)
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error { ex }
            dataMap.put("cd_consignor", null)
            dataMap.put("risk_profile_consignor", false)
        }
        // CdType and CoC details
        cdDetails.cdType?.let { cdType ->
            dataMap.put("cd_type", ConsignmentDocumentTypesEntityDao.fromEntity(cdType))
            if (cdType.localCocStatus == map.activeStatus || cdType.localCorStatus == map.activeStatus) {
                cdDetails.docTypeId?.let { docId ->
                    dataMap.put("doc_type", daoServices.findCORById(docId))
                }
            }
        }

        // Standard
        cdDetails.cdStandardsTwo?.let {
            dataMap.put("cd_standards_two", CdStandardTwoEntityDao.fromEntity(it))
        }
        // Standard
        cdDetails.cdStandard?.let {
            dataMap.put("cd_standard", CdStandardsEntityDao.fromEntity(it))
        }
        // Transporter
        val transportDetails = cdDetails.cdTransport?.let { daoServices.findCdTransportDetails(it) }
        transportDetails?.let {
            dataMap.put("cd_transport", it)
        }
        // Headers
        cdDetails.cdHeaderOne?.let {
            dataMap.put("cd_header_one", daoServices.findCdHeaderOneDetails(it))
        }
        cdDetails.cdStandard?.cdServiceProvider?.let {
            dataMap.put("cd_service_provider", it)
        }
        cdDetails.ucrNumber?.let {
            if (cdDetails.oldCdStatus == map.activeStatus) {
                val otherCds = daoServices.findCdWithUcrNumberExcept(it, cdDetails.id)
                dataMap.put("old_versions", ConsignmentDocumentDao.fromList(otherCds))
            } else {
                daoServices.findAllOlderVersionCDsWithSameUcrNumber(it, map)?.let { it1 ->
                    dataMap.put("old_versions", ConsignmentDocumentDao.fromList(it1))
                }
            }
        }
        dataMap.put("cd_details", ConsignmentDocumentDao.fromEntity(cdDetails))
        dataMap.put("items_cd", CdItemDetailsDao.fromList(daoServices.findCDItemsListWithCDID(cdDetails)))

        // Consignment UI controllers
        try {
            val uiDetails = ConsignmentEnableUI.fromEntity(cdDetails, map, commonDaoServices.loggedInUserAuthentication())
            uiDetails.supervisor = isSupervisor
            uiDetails.inspector = isInspectionOfficer
            daoServices.demandNotePaid(cdDetails.id!!)?.let {
                uiDetails.demandNotePaid = true
                uiDetails
            } ?: run {
                uiDetails.demandNotePaid = !uiDetails.demandNoteRequired
                uiDetails
            }
            try {
                daoServices.findCORByCdId(cdDetails)?.let {
                    uiDetails.corAvailable = true
                }
            } catch (ignored: Exception) {
                uiDetails.corAvailable = false
            }
            uiDetails.cocAvailable = false
            try {
                cdDetails.ucrNumber?.let {
                    daoServices.findCOC(it, "coc")
                    uiDetails.cocAvailable = true
                }

            } catch (ignored: Exception) {
                uiDetails.cocAvailable = false
            }
            dataMap.put("ui", uiDetails)
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("Failed to prepare UI", ex)
        }
        return dataMap
    }

    fun certificateOfConformanceDetails(cdUuid: String, docType: String): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val dataMap = mutableMapOf<String, Any?>()
            val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
            val cdDetails = daoServices.findCDWithUuid(cdUuid)
            val cocDetails = cdDetails.ucrNumber?.let { daoServices.findCOC(it, docType) }
            dataMap.put("configuration", map)
            dataMap.put("certificate_details", cocDetails)
            dataMap.put("consignment_document_details", ConsignmentDocumentDao.fromEntity(cdDetails))
            dataMap.put("item_certificate_of_conformance", cocDetails?.id?.let { daoServices.findCocItemList(it) })
            response.data = dataMap
            response.message = "Success"
            response.responseCode = ResponseCodes.SUCCESS_CODE
        } catch (e: Exception) {
            KotlinLogging.logger { }.error("COC NOT FOUND EROR", e)
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "CoC not found"
        }
        return response
    }

    fun importDeclarationFormDetails(cdUuid: String): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val cdDetails = daoServices.findCDWithUuid(cdUuid)
            val idfDetails = cdDetails.ucrNumber?.let { daoServices.findIdf(it) }
            val dataMap = mutableMapOf<String, Any?>()
            dataMap.put("idf_details", idfDetails)
            dataMap.put("cd_standard", cdDetails.cdStandard?.let { CdStandardsEntityDao.fromEntity(it) })
            dataMap.put("consignment_details", ConsignmentDocumentDao.fromEntity(cdDetails))
            dataMap.put("item_idf", idfDetails?.let { daoServices.findIdfItemList(it) })
            response.data = dataMap
            response.message = "Success"
            response.responseCode = ResponseCodes.SUCCESS_CODE
        } catch (e: Exception) {
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "IDF not found"
        }
        return response
    }

    fun certificateOfRoadWorthinessDetails(cdUuid: String): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val cdDetails = daoServices.findCDWithUuid(cdUuid)
            val corDetails = daoServices.findCORByCdId(cdDetails)
            val dataMap = mutableMapOf<String, Any?>()
            corDetails?.consignmentDocId = null
            dataMap.put("cor_details", corDetails)
            dataMap.put("cd_details", ConsignmentDocumentDao.fromEntity(cdDetails))
            response.data = dataMap
            response.message = "Success"
            response.responseCode = ResponseCodes.SUCCESS_CODE
        } catch (e: Exception) {
            KotlinLogging.logger { }.error("COR NOT FOUND ERROR", e)
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "CoR not found"
        }
        return response
    }

    fun consignmentDocumentManifestDetails(cdUuid: String): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val cdDetails = daoServices.findCDWithUuid(cdUuid)
            daoServices.findByManifestNumber(cdDetails.manifestNumber ?: "")?.let { manifestDetails ->
                val dataMap = mutableMapOf<String, Any?>()
                dataMap["manifest_details"] = manifestDetails
                dataMap["transport_details"] = cdDetails.cdTransport?.let { daoServices.findCdTransportDetails(it) }
                dataMap["consignment_details"] = ConsignmentDocumentDao.fromEntity(cdDetails)
                response.data = dataMap
                response.responseCode = ResponseCodes.SUCCESS_CODE
                response.message = "Success"
            } ?: run {
                response.responseCode = ResponseCodes.FAILED_CODE
                response.message = "Manifest with number '${cdDetails.manifestNumber}' does not exist"
            }
        } catch (e: Exception) {
            response.responseCode = ResponseCodes.NOT_FOUND
            response.message = "CD manifests not found"
        }
        return response
    }

    fun cdCustomDeclarationDetails(cdUuid: String): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
            val cdDetails = daoServices.findCDWithUuid(cdUuid)
            val declarationDetails = cdDetails.ucrNumber?.let { daoServices.findDeclaration(it) }
            val dataMap = mutableMapOf<String, Any?>()
            dataMap.put("declaration_details", declarationDetails)
            dataMap.put("cd_details", ConsignmentDocumentDao.fromEntity(cdDetails))
            dataMap.put("cd_standard", cdDetails.cdStandard)
            dataMap.put("item_declaration", declarationDetails?.let { daoServices.findDeclarationItemList(it) })
            dataMap.put("configuration", map)
            response.data = dataMap
            response.message = "Success"
            response.responseCode = ResponseCodes.SUCCESS_CODE
        } catch (e: Exception) {
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "CD manifests not found"
        }
        return response
    }

    fun getSSfDetails(cdItemId: Long): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val item = daoServices.findItemWithItemID(cdItemId)
            val ssfDetails = daoServices.findSampleSubmittedBYCdItemID(item.id
                    ?: throw ExpectedDataNotFound("MISSING CD ITEM ID"))
            val dataMap = mutableMapOf<String, Any?>()
            dataMap.put("ssf_details", ssfDetails)
            dataMap.put("lab_result_parameters", qaDaoServices.findSampleLabTestResultsRepoBYBSNumber(ssfDetails.bsNumber
                    ?: throw ExpectedDataNotFound("MISSING BS NUMBER")))
            response.data = dataMap
            response.message = "Success"
            response.responseCode = ResponseCodes.SUCCESS_CODE
        } catch (ex: Exception) {
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
            response.message = "Lab results not found"
        }
        return response
    }

    @Transactional
    fun selfAssign(cdDetails: ConsignmentDocumentDetailsEntity, comment: String? = null) {
        KotlinLogging.logger { }.info("START AUTO ASSIGN")
        var addedComment = comment
        if (addedComment == null) {
            addedComment = "Auto Assigned on Open"
        }
        this.getSupervisor(cdDetails)?.let {
            val loggedInUser = commonDaoServices.loggedInUserDetails()
            val data = mutableMapOf<String, Any?>()
            data["remarks"] = addedComment
            data["reassign"] = false
            data["selfAssign"] = true
            data["officerId"] = loggedInUser.id
            data["owner"] = loggedInUser.userName
            data["supervisor"] = it.userName
            data["cdUuid"] = cdDetails.uuid
            // Start BPM process for assigning inspection
            this.diBpmn.startAssignmentProcesses(data, cdDetails)
            // Wait for BPM process to complete
            Thread.sleep(10_000)

            KotlinLogging.logger { }.info("AUTO ASSIGN COMPLETED: ${cdDetails.uuid}-> Supervisor[${it.userName}]")
        } ?: KotlinLogging.logger { }.info("AUTO ASSIGN FAILED UNABLE TO GET SUPERVISOR for CFS")
    }

    fun getSupervisor(cdDetails: ConsignmentDocumentDetailsEntity): UsersEntity? {
        cdDetails.freightStation?.let {
            val supervisorRoles = this.privilegesRepository.findRoleIdsByRoleName("DI_OFFICER_CHARGE_READ")
            KotlinLogging.logger { }.info("ROLE IDS: $supervisorRoles")
            val userIds = cfsTypesEntity.findCfsUserIds(it.id, 1)
            KotlinLogging.logger { }.info("USR IDS: $userIds")
            val supervisors = this.userEntityRepository.findUsersInCfsAndProfiles(supervisorRoles, userIds)
            KotlinLogging.logger { }.info("USR COUNT: ${supervisors.size}")
            var usersEntity: UsersEntity? = null
            if (supervisors.size > 1) {
                usersEntity = supervisors[Random().nextInt(supervisors.size)]
            } else if (supervisors.size == 1) {
                usersEntity = supervisors[0]
            } else {
                return null
            }
            // Assign consignment
            return usersEntity
        }
        return null
    }

    @Transactional
    fun searchConsignmentDocuments(form: SearchForms, isSupervisor: Boolean, isInspectionOfficer: Boolean): ApiResponseModel {
        val response = ApiResponseModel()
        var cdType: ConsignmentDocumentTypesEntity? = null
        form.documentType?.let {
            cdType = daoServices.findCdTypeDetails(it)
        }
        val loggedInUser = commonDaoServices.getLoggedInUser()
        val list: List<ConsignmentDocumentDetailsEntity> = searchService.searchConsignmentDocuments(form.keywords, form.category, cdType, isInspectionOfficer, loggedInUser)
        response.data = ConsignmentDocumentDao.fromList(list)
        response.message = "Success"
        response.totalPages = 1
        response.pageNo = 1
        response.totalCount = 30
        response.responseCode = ResponseCodes.SUCCESS_CODE
        return response
    }

    fun createLocalCorReportMap(corId: Long): HashMap<String, Any> {
        val map = hashMapOf<String, Any>()
        daoServices.findCORById(corId)?.let { corDetails ->

            val importerDetails = corDetails.consignmentDocId?.cdImporter?.let { daoServices.findCDImporterDetails(it) }
            //Importer Details
            map["corNumber"] = corDetails.corNumber ?: ""
            map["ImporterName"] = importerDetails?.name.orEmpty()
            map["ImporterAddress"] = importerDetails?.physicalAddress.orEmpty()
            map["ImporterPhone"] = importerDetails?.telephone.orEmpty()

            //COR Details
            map["CorSerialNo"] = ""
            corDetails.createdOn?.let {
                val issuedOn = LocalDateTime.ofInstant(it.toInstant(), ZoneId.systemDefault())
                map["CorIssueDate"] = commonDaoServices.convertDateToString(issuedOn, "dd/MM/yyyy")
                map["CorExpiryDate"] = commonDaoServices.convertDateToString(issuedOn.plusMonths(3), "dd/MM/yyyy")
            }
            //Vehicle Details
            map["ChassisNumber"] = corDetails.chasisNumber.orEmpty()
            map["EngineNo"] = corDetails.engineNumber.orEmpty()
            map["EngineCap"] = corDetails.engineCapacity.orEmpty()
            map["Yom"] = corDetails.yearOfManufacture.orEmpty()
            map["Yor"] = corDetails.yearOfFirstRegistration.orEmpty()
            map["CustomsIeNo"] = corDetails.customsIeNo.toString()
            map["Cos"] = corDetails.countryOfSupply.orEmpty()
            map["BodyType"] = corDetails.typeOfBody.orEmpty()
            map["Fuel"] = corDetails.fuelType.orEmpty()
            map["TareWeight"] = corDetails.tareWeight.toString()
            map["VehicleType"] = corDetails.typeOfVehicle.orEmpty()
            map["ExporterName"] = corDetails.exporterName.orEmpty()
            map["ExporterAddress"] = corDetails.exporterAddress1.orEmpty()
            map["ExporterEmail"] = corDetails.exporterEmail.orEmpty()
            map["IdfNumber"] = corDetails.consignmentDocId?.idfNumber.toString()
            map["UcrNumber"] = corDetails.ucrNumber.orEmpty()
            map["Make"] = corDetails.make.orEmpty()
            map["Model"] = corDetails.model.orEmpty()
            map["InspectedMileage"] = corDetails.inspectionMileage.orEmpty()
            map["UnitsOfMileage"] = corDetails.unitsOfMileage.orEmpty()
            map["Color"] = corDetails.bodyColor.orEmpty()
            map["AxleNo"] = corDetails.numberOfAxles.toString()
            map["Transmision"] = corDetails.transmission.toString()
            map["OfficerName"] = corDetails.inspectionOfficer
            map["NoOfPassengers"] = corDetails.numberOfPassangers.toString()
            map["PrevRegNo"] = corDetails.previousCountryOfRegistration.orEmpty()
            map["PrevCountryOfReg"] = corDetails.previousCountryOfRegistration.orEmpty()
            map["CustomsIeNo"] = corDetails.customsIeNo ?: "N/A"
            // todo: ADD CHECKLIST COMMENT
//        val inspectionChecklist = findMotorVehicleInspectionByCdItem(cdItem)
            map["InspectionDetails"] = corDetails.inspectionStatement ?: "NA"
            map["InspectionRemarks"] = corDetails.inspectionRemarks ?: "NA"
        } ?: ExpectedDataNotFound("COR DOES NOT EXIST")
        return map
    }


    fun createLocalCocReportMap(cocId: Long): HashMap<String, Any> {
        val map = hashMapOf<String, Any>()
        daoServices.findCOCById(cocId)?.let { localCocEntity ->
            if ("COI".equals(localCocEntity.cocType)) {
                map["CocNo"] = localCocEntity.coiNumber.orEmpty()
            } else {
                map["CocNo"] = localCocEntity.cocNumber.orEmpty()
            }
            map["IssueDate"] = localCocEntity.cocIssueDate.toString()
            map["EntryNo"] = localCocEntity.customsEntryNumber ?: "N/A"
            map["IdfNo"] = localCocEntity.idfNumber.orEmpty()
            map["ImporterName"] = localCocEntity.importerName.orEmpty()
            map["ImporterAddress"] = localCocEntity.importerAddress1.orEmpty()
            map["ImporterPin"] = localCocEntity.importerPin.orEmpty()
            map["ClearingAgent"] = localCocEntity.clearingAgent ?: "N/A"
            map["PortOfEntry"] = localCocEntity.portOfDestination.orEmpty()
            map["UcrNo"] = localCocEntity.ucrNumber.orEmpty()
            map["Status"] = when {
                localCocEntity.status?.toInt() == 1 || localCocEntity.status == null -> "APPROVED"
                else -> "REJECTED"
            }
            map["Remarks"] = localCocEntity.cocRemarks.orEmpty()
            map["CoCType"] = localCocEntity.cocType.orEmpty()
            val cocItems = daoServices.findCocItemList(cocId)
            map["dataSources"] = hashMapOf(Pair("itemDataSource", cocItems))
        } ?: throw ExpectedDataNotFound("Invalid local CocNumber")
        return map
    }

    fun consignmentDocumentTasks(cdUuid: String): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            response.data = this.diBpmn.consignmentDocumentActions(cdUuid)
            response.message = "Cd actions"
            response.responseCode = ResponseCodes.SUCCESS_CODE
        } catch (ex: Exception) {
            response.message = "failed to fetch actions"
            response.responseCode = ResponseCodes.FAILED_CODE
        }
        return response
    }


    /**
     * generate auction goood inspection report (AGIR)
     */
    fun generateAuctionGoodsInspectionReport(cdUuid: String) {
        // TODO: generate auction goood inspection report: AGIR
    }

    /**
     * Send AGIR report
     */
    fun sendAuctionGoodReport(cdUuid: String) {

    }

    fun sendAuctionGoodWitnessReport(cdUuid: String) {

    }
}
