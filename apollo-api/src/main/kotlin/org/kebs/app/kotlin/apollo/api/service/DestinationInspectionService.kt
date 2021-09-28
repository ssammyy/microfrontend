package org.kebs.app.kotlin.apollo.api.service

import mu.KotlinLogging
import okhttp3.internal.toLongOrDefault
import org.kebs.app.kotlin.apollo.api.payload.*
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.DestinationInspectionBpmn
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.*
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.exceptions.InvalidValueException
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.CocItemsEntity
import org.kebs.app.kotlin.apollo.store.model.CocsEntity
import org.kebs.app.kotlin.apollo.store.model.ServiceMapsEntity
import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import org.kebs.app.kotlin.apollo.store.model.di.*
import org.kebs.app.kotlin.apollo.store.repo.*
import org.kebs.app.kotlin.apollo.store.repo.di.*
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream
import java.io.InputStreamReader
import java.io.Reader
import java.sql.Date
import java.sql.Timestamp
import java.time.Instant
import java.util.*


@Service("diService")
class DestinationInspectionService(
        private val ministryStationRepo: IMinistryStationEntityRepository,
        private val applicationMapProperties: ApplicationMapProperties,
        private val riskProfileDaoService: RiskProfileDaoService,
        private val commonDaoServices: CommonDaoServices,
        private val service: DaoService,
        private val iDIUploadsRepo: IDiUploadsRepository,
        private val cocsRepository: ICocsRepository,
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
        private val userEntityRepository: IUserRepository
) {
    @Value("\${destination.inspection.cd.type.cor}")
    lateinit var corCdType: String

    fun markCompliant(cdUuid: String, compliantStatus: Int, supervisor: String, remarks: String, taskApproved: Boolean): Boolean {
        if (taskApproved) {
            val consignmentDocument = this.daoServices.findCDWithUuid(cdUuid)
            consignmentDocument.compliantStatus = compliantStatus
            consignmentDocument.compliantDate = commonDaoServices.getCurrentDate()
            consignmentDocument.compliantRemarks = remarks
            val loggedInUser = commonDaoServices.findUserByUserName(supervisor)
            daoServices.updateCdDetailsInDB(consignmentDocument, loggedInUser)
            cdAuditService.addHistoryRecord(consignmentDocument.id, consignmentDocument.ucrNumber, remarks, "KEBS_APPROVE_COMPLIANCE", "Approve compliance request", supervisor)
        }
        return false
    }

    fun rejectComplianceRequests(cdUuid: String, supervisor: String, remarks: String): Boolean {
        val consignmentDocument = this.daoServices.findCDWithUuid(cdUuid)
        val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
        consignmentDocument.compliantStatus = map.inactiveStatus
        daoServices.updateCdDetailsInDB(consignmentDocument, null)
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
            } else {
                cdStatusType = daoServices.findCdStatusCategory("REJECT")
                consignmentDocument.varField10 = "Consignment Document Rejected on Non-Compliance"
            }
            consignmentDocument.approveRejectCdStatus = map.activeStatus
            consignmentDocument.approveRejectCdStatusType = cdStatusType
            consignmentDocument.approveRejectCdRemarks = remarks
            consignmentDocument.approveRejectCdDate = Date(Date().time)
            // Update consignment
            daoServices.updateCdDetailsInDB(consignmentDocument, commonDaoServices.findUserByUserName(supervisor))
            // Update SW status
            cdStatusType.statusCode?.let {
                daoServices.submitCDStatusToKesWS(remarks, it, consignmentDocument.version.toString(), consignmentDocument)
                consignmentDocument.cdStandard?.let { cdStd ->
                    consignmentDocument.approveRejectCdStatusType?.id?.let { it2 ->
                        daoServices.updateCDStatus(cdStd, it2)
                    }
                }
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("FAILED TO COMPLIANCE REJECTION STATUS", ex)
        }
        return false
    }

    //    fun createVersion(consignmentDocumentDetailsEntity: ConsignmentDocumentDetailsEntity): Boolean{
//        var consignmentDetailsCopy=ConsignmentDocumentDetailsEntity()
//        ModelMapper().map(consignmentDocumentDetailsEntity,consignmentDetailsCopy)
//        consignmentDetailsCopy.id=null
//
//        return true
//    }
    fun updateStatus(cdUuid: String, cdStatusTypeId: Long, supervisor: String, remarks: String): Boolean {
        try {
            val consignmentDocument = this.daoServices.findCDWithUuid(cdUuid)
            val cdStatusType = daoServices.findCdStatusValue(cdStatusTypeId)
            consignmentDocument.approveRejectCdStatusType = cdStatusType
            consignmentDocument.approveRejectCdDate = Date(Date().time)
            consignmentDocument.varField10 = "UPDATE LOCAL STATUS TO SW"
            daoServices.updateCdDetailsInDB(consignmentDocument, commonDaoServices.findUserByUserName(supervisor))
            // Update Local status
            if (cdStatusType.category == "APPROVE" || cdStatusType.category == "REJECT") {
                consignmentDocument.cdStandard?.let { cdStd ->
                    daoServices.updateCDStatus(cdStd, cdStatusTypeId)
                }
            } else if (cdStatusType.category == "AMENDMENT") {
                consignmentDocument.cdStandard?.let { cdStd ->
                    daoServices.updateCDStatus(cdStd, cdStatusTypeId)
                }
            } else if (cdStatusType.category === "QUERY") {
                consignmentDocument.cdStandard?.let { cdStd ->
                    daoServices.updateCDStatus(cdStd, cdStatusTypeId)
                }
            } else {
                consignmentDocument.cdStandard?.let { cdStd ->
                    daoServices.updateCDStatus(cdStd, cdStatusTypeId)
                }
            }
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("FAILED TO UPDATE STATUS", ex)
        }
        return false
    }

    fun updateBpmCompletionStatus(cdUuid: String, supervisor: String, remarks: String): Boolean {
        try {
            val consignmentDocument = this.daoServices.findCDWithUuid(cdUuid)
            consignmentDocument.varField10 = "SEND COMPLIANCE STATUS TO SW"
            val usersEntity = commonDaoServices.findUserByUserName(supervisor)
            daoServices.updateCdDetailsInDB(consignmentDocument, usersEntity)
            // Update SW
            consignmentDocument.approveRejectCdStatusType?.statusCode?.let {
                daoServices.submitCDStatusToKesWS(remarks, it, consignmentDocument.version.toString(), consignmentDocument)
            }
            // Local Status
            consignmentDocument.cdStandard?.let { cdStd ->
                consignmentDocument.approveRejectCdStatusType?.id?.let { it2 ->
                    daoServices.updateCDStatus(cdStd, it2)
                }
            }
            consignmentDocument.varField10 = "SW COMPLIANCE UPDATED"
            daoServices.updateCdDetailsInDB(consignmentDocument, usersEntity)
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("FAILED TO UPDATE ON SW", ex)
        }
        return false
    }

    fun assignInspectionOfficer(cdUuid: String, officerId: Long, supervisor: String, remarks: String) {
        try {
            val consignmentDocument = this.daoServices.findCDWithUuid(cdUuid)

            val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
            val loggedInUser = commonDaoServices.findUserByUserName(supervisor)
            val officer = daoServices.findUserById(officerId)
            KotlinLogging.logger { }.info("ASSIGN to ${officer.get().userName}")
            with(consignmentDocument) {
                assignedRemarks = remarks
                assignedDate = Date(Date().time)
                assignedStatus = map.activeStatus
                assigner = loggedInUser
                varField10 = "Assigned IO"
                assignedInspectionOfficer = officer.get()
            }

            this.daoServices.updateCdDetailsInDB(consignmentDocument, loggedInUser)
            // Update CD status
            consignmentDocument.cdStandard?.let { cdStd ->
                daoServices.updateCDStatus(cdStd, applicationMapProperties.mapDIStatusTypeAssignIoId)
            }
            cdAuditService.addHistoryRecord(consignmentDocument.id, consignmentDocument.ucrNumber, remarks, "KEBS_ASSIGN_IO", "Assign inspection officer to consignment", username = supervisor)
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("ASSIGNMENT FAILED", ex)
        }
    }

    fun reassignInspectionOfficer(cdUuid: String, officerId: Long, supervisor: String, remarks: String) {
        try {
            val consignmentDocument = this.daoServices.findCDWithUuid(cdUuid)
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

            this.daoServices.updateCdDetailsInDB(consignmentDocument, supervisorDetails)
            // Update CD status
            consignmentDocument.cdStandard?.let { cdStd ->
                daoServices.updateCDStatus(cdStd, applicationMapProperties.mapDIStatusTypeReassignIoId)
            }
            cdAuditService.addHistoryRecord(consignmentDocument.id, remarks, "KEBS_REASSIGN_IO", "Re-Assign inspection officer : " + officer.get().userName, supervisor)
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("FAILED TO REASSIGN", ex)
        }
    }

    fun checkForAutoTarget(cdUuid: String): Boolean {
        val consignmentDocument = this.daoServices.findCDWithUuid(cdUuid)
        val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
        //If CD without COR, auto-target
        if (consignmentDocument.cdType?.uuid == daoServices.noCorCdType) {
            with(consignmentDocument) {
                targetStatus = map.activeStatus
                targetReason = "CD without CoR"
                targetApproveStatus = map.activeStatus
                targetApproveDate = Date(Date().time)
            }
            consignmentDocument.assigner?.let { this.daoServices.updateCdDetailsInDB(consignmentDocument, it) }
        }
        return true
    }

    fun completeIOAssignment(cdUuid: String, officerId: Long, supervisor: String): Boolean {
        try {
            val consignmentDocument = this.daoServices.findCDWithUuid(cdUuid)
            consignmentDocument.apply {
                diProcessCompletedOn = Timestamp.from(Instant.now())
                diProcessStatus = 1
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
            val consignmentDocument = this.daoServices.findCDWithUuid(cdUuid)

            val loggedInUser = this.commonDaoServices.findUserByUserName(supervisor)
            daoServices.generateCor(consignmentDocument, map, loggedInUser).let { corDetails ->
                daoServices.submitCoRToKesWS(corDetails)
                consignmentDocument.cdStandard?.let { cdStd ->
                    daoServices.updateCDStatus(
                            cdStd,
                            applicationMapProperties.mapDICdStatusTypeCORGeneratedAndSendID
                    )
                }
                daoServices.saveCorDetails(corDetails)
                val itemId = corDetails.varField1?.toLongOrDefault(0L) ?: 0L
                //Send Cor to manufacturer
                reportsDaoService.generateLocalCoRReport(consignmentDocument, applicationMapProperties.mapReportLocalCorPath, itemId)?.let { file ->
                    with(corDetails) {
                        localCorFile = file.readBytes()
                        localCorFileName = file.name
                        varField10 = "COR Generated"
                    }
                    // Update COR
                    consignmentDocument.compliantStatus = map.activeStatus
                    consignmentDocument.localCocOrCorStatus = map.activeStatus
                    daoServices.updateCdDetailsInDB(consignmentDocument, null)
                    //Send email
                    consignmentDocument.cdImporter?.let {
                        daoServices.findCDImporterDetails(it)
                    }?.let { importer ->
                        importer.email?.let { daoServices.sendLocalCorReportEmail(it, file.path) }
                    }
                }

            }
            KotlinLogging.logger { }.info("COR GENERATION SUCCESS")
            return true
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("COR GENERATIOn FAILED", ex)
        }

        return false
    }

    fun rejectCorGeneration(cdUuid: String, blacklistId: Long, supervisor: String, remarks: String): Boolean {
        KotlinLogging.logger { }.info("UPDATE COR GENERATION REJECTION: ${cdUuid}")
        try {
            val consignmentDocument = this.daoServices.findCDWithUuid(cdUuid)
            consignmentDocument.compliantStatus = 0
            consignmentDocument.localCocOrCorStatus = 0
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
            consignmentDocument.targetStatus = map.invalidStatus
            consignmentDocument.targetApproveRemarks = remarks
            consignmentDocument.targetApproveDate = Date(Date().time)
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
            val consignmentDocument = this.daoServices.findCDWithUuid(cdUuid)
            consignmentDocument.targetStatus = 1
            consignmentDocument.varField10 = "Target approved awaiting inspection"
            consignmentDocument.targetApproveDate = Date(Date().time)
            consignmentDocument.targetApproveRemarks = remarks
            consignmentDocument.targetApproveDate = Date(Date().time)
            if (supervisor == null) {
                this.daoServices.updateCdDetailsInDB(consignmentDocument, null)
            } else {
                this.daoServices.updateCdDetailsInDB(consignmentDocument, this.commonDaoServices.findUserByUserName(supervisor))
            }
            this.cdAuditService.addHistoryRecord(consignmentDocument.id!!, consignmentDocument.ucrNumber, remarks, "APPROVE TARGETING", "Targeting of ${cdUuid} has been rejected by ${supervisor}", supervisor)
            // Submit consignment to Single/Window
            daoServices.submitCDStatusToKesWS("OH", "OH", consignmentDocument.version.toString(), consignmentDocument)
            consignmentDocument.cdStandard?.let { cdStd ->
                daoServices.updateCDStatus(cdStd, applicationMapProperties.mapDIStatusTypeKraVerificationSendId)
            }
            KotlinLogging.logger { }.info("REQUESTED TARGETING SCHEDULE: ${cdUuid}")
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("REJECTION UPDATE STATUS", ex)
        }
        return true
    }

    fun swGenerateCoC(cdUuid: String, remarks: String, supervisor: String): Boolean {
        KotlinLogging.logger { }.info("REQUESTING COC: ${cdUuid}")
        try {
            val consignmentDocument = this.daoServices.findCDWithUuid(cdUuid)
            val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
            val loggedInUser = commonDaoServices.findUserByUserName(supervisor)
            val localCoc = daoServices.createLocalCoc(loggedInUser, consignmentDocument, map, "A")
            consignmentDocument.cdStandard?.let { cdStd ->
                daoServices.updateCDStatus(
                        cdStd,
                        applicationMapProperties.mapDICdStatusTypeCOCGeneratedAndSendID
                )
            }
            consignmentDocument.varField10 = "Local COC Generated"
            consignmentDocument.localCocOrCorStatus = map.activeStatus
            consignmentDocument.compliantStatus = map.activeStatus
            consignmentDocument.cocNumber = localCoc.cocNumber
            daoServices.updateCdDetailsInDB(consignmentDocument, null)

            KotlinLogging.logger { }.info("Local CoC = ${localCoc.id}")
            // Send to SW
            daoServices.sendLocalCoi(localCoc.id)
            //Generate PDF File & send to manufacturer
            reportsDaoService.generateLocalCoCReportWithDataSource(consignmentDocument, applicationMapProperties.mapReportLocalCocPath)?.let { file ->
                consignmentDocument.cdImporter?.let {
                    daoServices.findCDImporterDetails(it)
                }?.let { importer ->
                    importer.email?.let { daoServices.sendLocalCocReportEmail(it, file.path) }
                }
            }
            this.daoServices.updateCdDetailsInDB(consignmentDocument, null)
            KotlinLogging.logger { }.info("GENERATE COC/COI: ${cdUuid}")
            return true
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("REJECTION UPDATE STATUS", ex)
        }
        return false
    }


    fun swGenerateCoI(cdUuid: String, remarks: String, supervisor: String): Boolean {
        KotlinLogging.logger { }.info("REQUESTING COI GENERATION: ${cdUuid}")
        try {
            val consignmentDocument = this.daoServices.findCDWithUuid(cdUuid)
            val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
            val loggedInUser = commonDaoServices.findUserByUserName(supervisor)
            val localCoi = daoServices.createLocalCoi(loggedInUser, consignmentDocument, map, "D")
            consignmentDocument.cdStandard?.let { cdStd ->
                daoServices.updateCDStatus(cdStd, applicationMapProperties.mapDICdStatusTypeCOIGeneratedAndSendID)
            }
            // Update CoI generation
            consignmentDocument.localCoi = 1
            consignmentDocument.compliantStatus = map.activeStatus
            consignmentDocument.localCocOrCorStatus = map.activeStatus
            consignmentDocument.cocNumber = localCoi.cocNumber
            consignmentDocument.varField10 = "Local COI Generated"
            daoServices.updateCdDetailsInDB(consignmentDocument, null)
            // Send to SW
            daoServices.sendLocalCoi(localCoi.id)
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
    fun clearCurrentProcess(cdUuid: String): Boolean {
        try {
            val consignmentDocument = this.daoServices.findCDWithUuid(cdUuid)
            consignmentDocument.diProcessStatus = 0
            consignmentDocument.diProcessCompletedOn = Timestamp.from(Instant.now())
            this.daoServices.updateCdDetailsInDB(consignmentDocument, this.commonDaoServices.getLoggedInUser())
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("UPDATE STATUS", ex)
        }
        return true
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

    fun updateStatus(cdUuid: String, cdStatusTypeId: Long, remarks: String) {
        val consignmentDocument = this.daoServices.findCDWithUuid(cdUuid)
        var cdStatusType: CdStatusTypesEntity?
        KotlinLogging.logger { }.info { "approveRejectCdStatusType = ${cdStatusTypeId}" }
        cdStatusType = cdStatusTypeId.let { daoServices.findCdStatusValue(it) }
        cdStatusType.let { cdStatus ->
            with(consignmentDocument) {
                approveRejectCdStatusType = cdStatus
                approveRejectCdDate = Date(Date().time)
                approveRejectCdRemarks = remarks
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
            map: ServiceMapsEntity
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
                "File upload",
                loggedInUser,
                map,
                null,
                null
        )
        val targetReader: Reader = InputStreamReader(ByteArrayInputStream(docFile.bytes))
        val cocs = service.readCocFileFromController(separator, targetReader)
        val uniqueCoc = cocs.map { it.cocNumber }.distinct()
        uniqueCoc.forEach {
            it
                    ?.let { u ->
                        val coc = cocs.firstOrNull { it.cocNumber == u }
                        cocsRepository.findByUcrNumber(
                                coc?.ucrNumber
                                        ?: throw InvalidValueException("Record with empty UCR Number not allowed")
                        )
                                ?.let {
                                    throw InvalidValueException("CoC with UCR number already exists")
                                }
                                ?: run {
                                    var entity = CocsEntity().apply {
                                        cocNumber = coc.cocNumber
                                        idfNumber = coc.idfNumber
                                        rfiNumber = coc.rfiNumber
                                        ucrNumber = coc.ucrNumber
                                        rfcDate = coc.rfcDate
                                        cocIssueDate = coc.cocIssueDate
                                        clean = coc.clean
                                        shipmentGrossWeight = coc.shipmentGrossWeight ?: "0.0"
                                        importerPin = coc.importerPin ?: "NA"
                                        shipmentQuantityDelivered = coc.shipmentQuantityDelivered ?: "0"
                                        cocRemarks = coc.cocRemarks
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
                                        productCategory = coc.productCategory ?: "UNDEFINED"
                                        productCategory = coc.productCategory ?: "UNDEFINED"
                                        status = 1L
                                        createdBy = loggedInUser.userName
                                        createdOn = Timestamp.from(Instant.now())
                                        partner = loggedInUser.userName
                                        pvocPartner = loggedInUser.id


                                    }
                                    entity = cocsRepository.save(entity)
                                    cocs.filter { dto -> dto.cocNumber == u }.forEach { cocItems ->
                                        val itemEntity = CocItemsEntity().apply {
                                            cocId = entity.id
                                            shipmentLineNumber = cocItems.shipmentLineNumber
                                            shipmentLineHscode = cocItems.shipmentLineHscode ?: "UNDEFINED"
                                            shipmentLineQuantity = cocItems.shipmentLineQuantity.toLong()
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
                                            shipmentLineRegistration = cocItems.shipmentLineRegistration ?: "UNDEFINED"
                                            status = 1
                                            createdBy = loggedInUser.userName
                                            createdOn = Timestamp.from(Instant.now())
                                            cocNumber = entity.cocNumber
                                            shipmentLineBrandName = "UNDEFINED"


                                        }
                                        cocItemsRepository.save(itemEntity)

                                        service.submitCocToKeSWS(entity)

                                    }


                                }

                    }
                    ?: KotlinLogging.logger { }.info("Empty value")
        }
    }

    fun applicationTypes(): ApiResponseModel {
        val response = ApiResponseModel()
        response.responseCode = ResponseCodes.SUCCESS_CODE
        response.message = "Success"
        response.data = this.importerDaoServices.findDiApplicationTypes(1)
        return response
    }


    fun consignmentDocumentDetails(cdUuid: String, isSupervisor: Boolean, isInspectionOfficer: Boolean): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
            var cdDetails = daoServices.findCDWithUuid(cdUuid)
            // When IO requests for details, they are are auto assigned
            if (isInspectionOfficer && cdDetails.assignedInspectionOfficer == null) {
                this.selfAssign(cdDetails)
                // Reload CD on self assign BPM process
                cdDetails = daoServices.findCD(cdDetails.id!!)
            }
            // Load inspection details
            response.data = loadCDDetails(cdDetails, map, isSupervisor, isInspectionOfficer)
            response.responseCode = ResponseCodes.SUCCESS_CODE
            response.message = "Consignment Document"
        } catch (ex: Exception) {
            KotlinLogging.logger { }.error("FAILE TO LOAD DOCUMENT", ex)
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
                if (itemType.equals(corCdType)) {
                    itemType.docTypeId?.let {
                        dataMap.put("doc_type", daoServices.findCORById(it))
                    }
                }
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
        cdDetails.cdTransport?.let {
            dataMap.put("cd_transport", daoServices.findCdTransportDetails(it))
        }
        // Headers
        cdDetails.cdHeaderOne?.let {
            dataMap.put("cd_header_one", daoServices.findCdHeaderOneDetails(it))
        }
        cdDetails.cdStandard?.cdServiceProvider?.let {
            dataMap.put("cd_service_provider", it)
        }
        cdDetails.ucrNumber?.let {
            dataMap.put("old_versions", daoServices.findAllOlderVersionCDsWithSameUcrNumber(it, map)?.let { it1 -> ConsignmentDocumentDao.fromList(it1) })
        }
        dataMap.put("cd_details", ConsignmentDocumentDao.fromEntity(cdDetails))
        dataMap.put("items_cd", CdItemDetailsDao.fromList(daoServices.findCDItemsListWithCDID(cdDetails)))

        // Consignment UI controllers
        try {
            val uiDetails = ConsignmentEnableUI.fromEntity(cdDetails, map, commonDaoServices.loggedInUserAuthentication())
            uiDetails.supervisor = isSupervisor
            uiDetails.inspector = isInspectionOfficer
            try {
                uiDetails.demandNotePaid = daoServices.demandNotePaid(cdDetails.id!!)
            } catch (ex: Exception) {
                uiDetails.demandNotePaid = false
            }
            try {
                cdDetails.ucrNumber?.let {
                    daoServices.findLocalCorByUcrNumber(it)
                    uiDetails.corAvailable = true
                }

            } catch (ignored: Exception) {
                uiDetails.corAvailable = false
            }
            uiDetails.cocAvailable = false
            try {
                cdDetails.ucrNumber?.let {
                    daoServices.findCOC(it)
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

    fun certificateOfConformanceDetails(cdUuid: String): ApiResponseModel {
        val response = ApiResponseModel()
        try {
            val dataMap = mutableMapOf<String, Any?>()
            val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
            val cdDetails = daoServices.findCDWithUuid(cdUuid)
            val cocDetails = cdDetails.ucrNumber?.let { daoServices.findCOC(it) }
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
            val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
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
            val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
            val cdDetails = daoServices.findCDWithUuid(cdUuid)
            val corDetails = cdDetails.ucrNumber?.let { daoServices.findLocalCorByUcrNumber(it) }
            val dataMap = mutableMapOf<String, Any?>()
            dataMap.put("configuration", map)
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
            val map = commonDaoServices.serviceMapDetails(applicationMapProperties.mapImportInspection)
            val cdDetails = daoServices.findCDWithUuid(cdUuid)
            val declarationDetails = cdDetails.ucrNumber?.let { daoServices.findDeclaration(it) }
            val manifestDetails = declarationDetails?.billCode?.let { daoServices.findManifest(it) }
            val dataMap = mutableMapOf<String, Any?>()
            dataMap.put("manifest_details", manifestDetails)
            dataMap.put("cd_details", cdDetails)
            dataMap.put("configuration", map)
            response.data = dataMap
            response.responseCode = ResponseCodes.SUCCESS_CODE
            response.message = "Success"
        } catch (e: Exception) {
            response.responseCode = ResponseCodes.EXCEPTION_STATUS
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
            data["officerId"] = loggedInUser.id
            data["owner"] = loggedInUser.userName
            data["supervisor"] = it.userName
            data["isAutoTargeted"] = cdDetails.cdType?.uuid == daoServices.noCorCdType
            data["cdUuid"] = cdDetails.uuid
            // Start BPM process for assigneing inspection
            this.diBpmn.startAssignmentProcesses(data, cdDetails);
            KotlinLogging.logger { }.info("AUTO ASSIGN COMPLETED: ${cdDetails.uuid}-> Supervisor[${it.userName}]")
        } ?: KotlinLogging.logger { }.info("AUTO ASSIGN FAILED UNABLE TO GET SUPERVISOR for CFS")
    }

    fun getSupervisor(cdDetails: ConsignmentDocumentDetailsEntity): UsersEntity? {
        cdDetails.freightStation?.let {
            val supervisorRoles = this.privilegesRepository.findRoleIdsByRoleName("DI_OFFICER_CHARGE_READ")
            KotlinLogging.logger { }.info("ROLE IDS: ${supervisorRoles}")
            val userIds = cfsTypesEntity.findCfsUserIds(it.id, 1)
            KotlinLogging.logger { }.info("USR IDS: ${userIds}")
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

}
