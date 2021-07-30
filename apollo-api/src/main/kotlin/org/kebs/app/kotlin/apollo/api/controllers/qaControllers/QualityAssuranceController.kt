package org.kebs.app.kotlin.apollo.api.controllers.qaControllers


import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.notifications.Notifications
import org.kebs.app.kotlin.apollo.api.ports.provided.bpmn.QualityAssuranceBpmn
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.QADaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.lims.LimsServices
import org.kebs.app.kotlin.apollo.common.dto.FmarkEntityDto
import org.kebs.app.kotlin.apollo.common.dto.qa.NewBatchInvoiceDto
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.utils.generateRandomText
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.ServiceMapsEntity
import org.kebs.app.kotlin.apollo.store.model.ServiceRequestsEntity
import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import org.kebs.app.kotlin.apollo.store.model.qa.*
import org.kebs.app.kotlin.apollo.store.repo.qa.IQaSampleSubmissionRepository
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import javax.servlet.http.HttpServletResponse


@Controller
@RequestMapping("/api/qa")
class QualityAssuranceController(
    private val applicationMapProperties: ApplicationMapProperties,
    private val qaDaoServices: QADaoServices,
    private val qualityAssuranceBpmn: QualityAssuranceBpmn,
    private val SampleSubmissionRepo: IQaSampleSubmissionRepository,
    private val notifications: Notifications,
    private val limsServices: LimsServices,
    private val commonDaoServices: CommonDaoServices,
) {

    final val appId: Int = applicationMapProperties.mapQualityAssurance

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @PostMapping("/apply/new-permit")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun saveNewPermit(
        @ModelAttribute("permit") permit: PermitApplicationsEntity,
        @RequestParam("permitTypeID") permitTypeID: Long,
        model: Model
    ): String? {
        val result: ServiceRequestsEntity?
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val permitType = qaDaoServices.findPermitType(permitTypeID)

        result = qaDaoServices.permitSave(permit, permitType, loggedInUser, map).first

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.closeLink = "${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${result.varField1}"
        sm.message = "You have Successful Filled STA 1, Complete your application"

        return commonDaoServices.returnValues(result, map, sm)
    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @PostMapping("/kebs/apply/new-fmark-permit")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun saveNewFmarkPermit(
        @ModelAttribute("fmarkEntityDto") fmarkEntityDto: FmarkEntityDto,
        model: Model
    ): String? {
        val result: ServiceRequestsEntity?
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val auth = commonDaoServices.loggedInUserAuthentication()
        val permitDetails = qaDaoServices.findPermitBYID(
            fmarkEntityDto.smarkPermitID ?: throw ExpectedDataNotFound("Smark Permit id not found")
        )

        result = qaDaoServices.permitGenerateFmark(map, loggedInUser, permitDetails, auth).first

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.closeLink = "${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${result.varField1}"
        sm.message = "You have Successful Generated FMARK application, Proceed to Submit Your application"

        return commonDaoServices.returnValues(result, map, sm)
    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION') or hasAuthority('QA_MANAGER_ASSESSORS_READ') or hasAuthority('QA_HOF_READ') or hasAuthority('QA_HOD_READ') or hasAuthority('QA_OFFICER_MODIFY') or hasAuthority('QA_ASSESSORS_MODIFY') or hasAuthority('QA_PSC_MEMBERS_READ') or hasAuthority('QA_PCM_READ')")
    @PostMapping("/apply/new-scheme-of-supervision")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun saveNewSchemeOfSupervision(
        @ModelAttribute("QaSchemeForSupervisionEntity") QaSchemeForSupervisionEntity: QaSchemeForSupervisionEntity,
        @RequestParam("permitID") permitID: Long,
        @RequestParam("status") status: Int?,
        @RequestParam("schemeID") schemeID: Long?,
        model: Model
    ): String? {
        var result: ServiceRequestsEntity?
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        //Find Permit with permit ID
        val permitDetails = qaDaoServices.findPermitBYID(permitID)
        val sm = CommonDaoServices.MessageSuccessFailDTO()


        if (schemeID != null && status != null) {
            QaSchemeForSupervisionEntity.status = status
            QaSchemeForSupervisionEntity.acceptedRejectedStatus = null
            result =
                qaDaoServices.schemeSupervisionUpdateSave(schemeID, QaSchemeForSupervisionEntity, loggedInUser, map)
            sm.message = "You have Successful UPDATED SSC"
        } else {
            result =
                qaDaoServices.newSchemeSupervisionSave(permitDetails, QaSchemeForSupervisionEntity, loggedInUser, map)
            //Permit SCC approval status
            qaDaoServices.permitInsertStatus(permitDetails, applicationMapProperties.mapQaStatusPApprSSC, loggedInUser)
            sm.message = "You have Successful Filled SSC"
        }


        //Set scheme as generated and update results
        permitDetails.generateSchemeStatus = map.activeStatus

        result = qaDaoServices.permitUpdateDetails(permitDetails, map, loggedInUser).first

        sm.closeLink = "${applicationMapProperties.baseUrlValue}/qa/scheme-of-supervision?permitID=${permitDetails.id}"


        return commonDaoServices.returnValues(result, map, sm)
    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @PostMapping("/apply/update-scheme-of-supervision")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun updateSchemeOfSupervision(
        @ModelAttribute("schemeFound") schemeFound: QaSchemeForSupervisionEntity,
        @RequestParam("schemeID") schemeID: Long,
        model: Model
    ): String? {
        val result: ServiceRequestsEntity?
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        result = qaDaoServices.schemeSupervisionUpdateSave(schemeID, schemeFound, loggedInUser, map)
        val permitDetails =
            qaDaoServices.findPermitBYID(result.varField1?.toLong() ?: throw ExpectedDataNotFound("MISSING PERMIT ID"))
        when (schemeFound.acceptedRejectedStatus) {
            map.activeStatus -> {
                qaDaoServices.permitInsertStatus(
                    permitDetails,
                    applicationMapProperties.mapQaStatusPInspReport,
                    loggedInUser
                )
            }
            map.inactiveStatus -> {
                qaDaoServices.permitInsertStatus(
                    permitDetails,
                    applicationMapProperties.mapQaStatusSSCRejected,
                    loggedInUser
                )
            }
        }

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.closeLink = "${applicationMapProperties.baseUrlValue}/qa/scheme-of-supervision?permitID=${result.varField1}"
        sm.message = "You have Successful UPDATED SSC"

        return commonDaoServices.returnValues(result, map, sm)
    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @PostMapping("/apply/update-permit-request")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun permitRequestUpdates(
        @ModelAttribute("permitRequest") permitRequest: PermitUpdateDetailsRequestsEntity,
        @RequestParam("permitID") permitID: Long,
        model: Model
    ): String? {
        val result: ServiceRequestsEntity?
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        result = qaDaoServices.permitRequests(permitRequest, permitID, loggedInUser, map).first
        val permitDetails =
            qaDaoServices.findPermitBYID(result.varField1?.toLong() ?: throw ExpectedDataNotFound("MISSING PERMIT ID"))

        qaDaoServices.permitInsertStatus(
            permitDetails,
            applicationMapProperties.mapQaStatusPRequestApproval,
            loggedInUser
        )


        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.closeLink = "${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${result.varField1}"
        sm.message = "Your Request has been Successful Submitted"

        return commonDaoServices.returnValues(result, map, sm)
    }

    @PreAuthorize(
        "hasAuthority('PERMIT_APPLICATION') or hasAuthority('QA_MANAGER_ASSESSORS_MODIFY') or hasAuthority('QA_HOF_MODIFY') " +
                "or hasAuthority('QA_HOD_MODIFY') or hasAuthority('QA_OFFICER_MODIFY') or hasAuthority('QA_PAC_SECRETARY_MODIFY') or hasAuthority('QA_PSC_MEMBERS_MODIFY') or hasAuthority('QA_PCM_MODIFY') or hasAuthority('QA_ASSESSORS_MODIFY')"
    )
    @PostMapping("/apply/update-permit")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun updatePermitDetails(
        @ModelAttribute("permit") permit: PermitApplicationsEntity,
        @RequestParam("permitID") permitID: Long,
        model: Model
    ): String? {

        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        var result: ServiceRequestsEntity?


        //Find Permit with permit ID
        var permitDetails = qaDaoServices.findPermitBYID(permitID)
        val permitType =
            qaDaoServices.findPermitType(permitDetails.permitType ?: throw Exception("MISSING PERMIT TYPE ID"))

        var returnDetails: Pair<PermitApplicationsEntity, String> =
            assigndefalutDetailsDetails(map, permitDetails, loggedInUser)

        //Add Permit ID THAT was Fetched so That it wont create a new record while updating with the methode
        permit.id = permitDetails.id

        if (permit.sectionId != null) {
            with(permit) {
                divisionId = commonDaoServices.findSectionWIthId(
                    sectionId ?: throw Exception("SECTION ID IS MISSING")
                ).divisionId?.id
            }
        } else if (permit.qaoId != null && permit.assignOfficerStatus == map.activeStatus) {
            with(permit) {
                factoryVisit = commonDaoServices.getCalculatedDate(
                    permitType.factoryVisitDate ?: throw Exception("MISSING FACTORY INSPECTION DATE")
                )
            }
        }

        //updating of Details in DB
        val updateResults = qaDaoServices.permitUpdateDetails(
            commonDaoServices.updateDetails(
                permit,
                permitDetails
            ) as PermitApplicationsEntity, map, loggedInUser
        )

        result = updateResults.first

        permitDetails = updateResults.second

        when {
            //PCM REVIEW ACTIONS TO BE DONE
            permit.pcmReviewApprovalStatus != null -> {
                returnDetails = permitApplicationsPCMReviewActions(permitDetails, permit, map, loggedInUser)
                permitDetails = returnDetails.first
            }
            //Permit completeness status
            permit.hofQamCompletenessStatus != null -> {
                returnDetails = hodQamCompletenessCheck(permit, map, permitDetails, loggedInUser)
                permitDetails = returnDetails.first

            }
            //Permit assigned officer
            permit.assignOfficerStatus != null -> {
                returnDetails = assignQAOfficer(permitDetails, permitType, loggedInUser)
                permitDetails = returnDetails.first
            }
            //Add standard Details
            permit.productStandard != null -> {
                returnDetails = addedStandardsDetails(map, permitDetails, loggedInUser)
                permitDetails = returnDetails.first
            }
            //Permit factory Inspection scheduled
            permit.inspectionScheduledStatus != null -> {
                returnDetails = factoryInspection(permitDetails, loggedInUser)
                permitDetails = returnDetails.first
            }
            //ADD Recommendation
            permit.recommendationRemarks != null -> {
                returnDetails = addRecommendation(permitDetails, loggedInUser)
                permitDetails = returnDetails.first
            }
            //APPROVE RECOMMENDATION
            permit.recommendationApprovalStatus != null -> {
                returnDetails = approveRecommendationPermit(permit, map, permitDetails, loggedInUser)
                permitDetails = returnDetails.first
            }
            //Permit awarded PSC review
            permit.pscMemberApprovalStatus != null -> {
                returnDetails = permitPSCApprovalProcess(permit, map, permitDetails, loggedInUser)
                permitDetails = returnDetails.first
            }
            //PCM APPROVE PERMIT
            permit.pcmApprovalStatus != null -> {
                //PCM ACTIONS TO BE DONE
                returnDetails = permitApplicationsPCMApprovalActions(permitDetails, permit, map, loggedInUser)
                permitDetails = returnDetails.first
            }
            //JUSTIFICATION REPORT APPROVE
            permit.justificationReportStatus != null -> {
                returnDetails = permitApproveJustificationReport(permit, map, permitDetails, loggedInUser)
                permitDetails = returnDetails.first
            }
            //PERMIT ASSIGNED  ASSESSOR OFFICER
            permit.assignAssessorStatus != null -> {
                //Send notification to assessor
                returnDetails = permitAssignAssessor(permitDetails, loggedInUser)
                permitDetails = returnDetails.first
            }
            //PERMIT ASSESMENT SCHEDULED
            permit.assessmentScheduledStatus != null -> {
                //Send manufacturers notification
                returnDetails = assessmentDateScheduled(permitDetails, loggedInUser)
                permitDetails = returnDetails.first
            }
            //PERMIT AWARD DMARK
            permit.pacDecisionStatus != null -> {
                returnDetails = pacApprovePermit(permit, map, permitDetails, loggedInUser)
                permitDetails = returnDetails.first
            }
            //PERMIT APPROVE ASSESSMENT REPORT
            permit.hodApproveAssessmentStatus != null -> {
                returnDetails = approveAssesmentReport(permit, map, permitDetails, loggedInUser)
                permitDetails = returnDetails.first
            }

//            //Permit Resubmit application
//            permit.resubmitApplicationStatus == map.activeStatus -> {
//                with(permit) {
//                    resubmitApplicationStatus = null
//                }
//                qaDaoServices.permitInsertStatus(
//                    permitDetails,
//                    applicationMapProperties.mapQaStatusResubmitted,
//                    loggedInUser
//                )
//            }
            //Permit pending factory inspection Approval
            permit.factoryInspectionReportApprovedRejectedStatus == map.activeStatus -> {
                qaDaoServices.permitInsertStatus(
                    permitDetails,
                    applicationMapProperties.mapQaStatusPBSNumber,
                    loggedInUser
                )
            }


            //Permit awarded scheduled




            //Approved or rejected SSC
            permit.approvedRejectedScheme != null -> {
                //Send notification
                var reasonValue: String? = null
                when (permit.approvedRejectedScheme) {
                    map.activeStatus -> {
                        reasonValue = "ACCEPTED"
                        qaDaoServices.schemeSendEmail(permitDetails, loggedInUser, reasonValue)
                        qaDaoServices.permitInsertStatus(
                            permitDetails,
                            applicationMapProperties.mapQaStatusPSSF,
                            loggedInUser
                        )
                    }
                    map.inactiveStatus -> {
                        reasonValue = "REJECTED"
                        with(permitDetails) {
                            approvedRejectedScheme = null
                            generateSchemeStatus = null
                        }
                        qaDaoServices.permitInsertStatus(
                            permitDetails,
                            applicationMapProperties.mapQaStatusSSCRejected,
                            loggedInUser
                        )
                        qaDaoServices.schemeSendEmail(permitDetails, loggedInUser, reasonValue)
                    }
                }

            }


        }

        //updating of Details in DB
        result = qaDaoServices.permitUpdateDetails(
            commonDaoServices.updateDetails(
                permit,
                permitDetails
            ) as PermitApplicationsEntity, map, loggedInUser
        ).first


        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.closeLink = returnDetails.second
        sm.message = "${permit.description}"

        return commonDaoServices.returnValues(result, map, sm)
    }

    fun approveAssesmentReport(
        permit: PermitApplicationsEntity,
        map: ServiceMapsEntity,
        permitDetails: PermitApplicationsEntity,
        loggedInUser: UsersEntity
    ): Pair<PermitApplicationsEntity, String> {
        var complianceValue: String? = null
        var permitDetailsDB = permitDetails
        when (permit.hodApproveAssessmentStatus) {
            map.activeStatus -> {
                val appointedPacSec = qaDaoServices.assignNextOfficerWithDesignation(
                    permitDetailsDB,
                    map,
                    applicationMapProperties.mapQADesignationIDForPacSecId
                )

                with(permitDetailsDB) {
                    hodApproveAssessmentStatus = map.activeStatus
                    hodApproveAssessmentRemarks = permit.hodApproveAssessmentRemarks
                    pacSecId = appointedPacSec?.id
                    userTaskId = applicationMapProperties.mapUserTaskNamePACSECRETARY
                }
                qaDaoServices.permitUpdateDetails(permitDetailsDB, map, loggedInUser)

                //Send notification to PAC secretary
                val pacSec = appointedPacSec?.id?.let { commonDaoServices.findUserByID(it) }
                pacSec?.email?.let { qaDaoServices.sendPacDmarkAssessmentNotificationEmail(it, permitDetailsDB) }

                qaDaoServices.permitInsertStatus(
                    permitDetailsDB,
                    applicationMapProperties.mapQaStatusPPACSecretaryAwarding,
                    loggedInUser
                )

            }
            map.inactiveStatus -> {
                complianceValue = "REJECTED"
                with(permitDetailsDB) {
                    assessmentScheduledStatus = null
                    hodApproveAssessmentStatus = null
//                    permitAwardStatus = null
                }

                permitDetailsDB = qaDaoServices.permitInsertStatus(
                    permitDetailsDB,
                    applicationMapProperties.mapQaStatusPermitAwarded,
                    loggedInUser
                )

                qaDaoServices.sendAssessmentReportRejection(
                    permitDetailsDB,
                    complianceValue,
                    permitDetailsDB.hodApproveAssessmentRemarks
                        ?: throw ExpectedDataNotFound("MISSING COMPLIANCE REMARKS"),
                )

            }
        }

        val closeLink =
            "${applicationMapProperties.baseUrlValue}/qa/permits-list?permitTypeID=${permitDetailsDB.permitType}"
        return Pair(permitDetailsDB, closeLink)
    }

    fun pacApprovePermit(
        permit: PermitApplicationsEntity,
        map: ServiceMapsEntity,
        permitDetails: PermitApplicationsEntity,
        loggedInUser: UsersEntity
    ): Pair<PermitApplicationsEntity, String> {
        var permitDetailsDB = permitDetails
        when (permit.pacDecisionStatus) {
            map.activeStatus -> {
                with(permitDetailsDB) {

                    userTaskId = applicationMapProperties.mapUserTaskNamePCM
                    pcmId = qaDaoServices.assignNextOfficerWithDesignation(
                        permit,
                        map,
                        applicationMapProperties.mapQADesignationIDForPCMId
                    )?.id
                }
                permitDetailsDB = qaDaoServices.permitInsertStatus(
                    permitDetails,
                    applicationMapProperties.mapQaStatusPPCMAwarding,
                    loggedInUser
                )
                qaDaoServices.sendNotificationPCMForAwardingPermit(permitDetailsDB)
            }
            map.inactiveStatus -> {
                with(permitDetailsDB) {
                    assessmentScheduledStatus = null
//                    permitAwardStatus = null
                    userTaskId = applicationMapProperties.mapUserTaskNameASSESSORS
                }

                permitDetailsDB = qaDaoServices.permitInsertStatus(
                    permitDetails,
                    applicationMapProperties.mapQaStatusDeferredByPACSecretary,
                    loggedInUser
                )
                qaDaoServices.sendNotificationForDeferredPermitToQaoFromPSC(permitDetailsDB)
                qaDaoServices.sendNotificationForDeferredPermitToAssessorFromPCM(permitDetailsDB)
            }
        }

        val closeLink =
            "${applicationMapProperties.baseUrlValue}/qa/permits-list?permitTypeID=${permitDetailsDB.permitType}"
        return Pair(permitDetailsDB, closeLink)
    }

    fun assessmentDateScheduled(
        permitDetails: PermitApplicationsEntity,
        loggedInUser: UsersEntity
    ): Pair<PermitApplicationsEntity, String> {
        var permitDetailsDB = permitDetails
        permitDetailsDB = qaDaoServices.permitInsertStatus(
            permitDetailsDB,
            applicationMapProperties.mapQaStatusPGenerationAssesmentReport,
            loggedInUser
        )
        val manufacturer = permitDetailsDB.userId?.let { commonDaoServices.findUserByID(it) }
        manufacturer?.email?.let {
            qaDaoServices.sendScheduledFactoryAssessmentNotificationEmail(
                it,
                permitDetailsDB
            )
        }


        val closeLink = "${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${permitDetailsDB.id}"
        return Pair(permitDetailsDB, closeLink)
    }

    fun permitAssignAssessor(
        permitDetailsDB: PermitApplicationsEntity,
        loggedInUser: UsersEntity
    ): Pair<PermitApplicationsEntity, String> {
        val assessor = permitDetailsDB.assessorId?.let { commonDaoServices.findUserByID(it) }
        assessor?.email?.let { qaDaoServices.sendAppointAssessorNotificationEmail(it, permitDetailsDB) }
        val Leadassessor = permitDetailsDB.leadAssessorId?.let { commonDaoServices.findUserByID(it) }
        Leadassessor?.email?.let { qaDaoServices.sendAppointAssessorNotificationEmail(it, permitDetailsDB) }
        permitDetailsDB.userTaskId = applicationMapProperties.mapUserTaskNameASSESSORS
        qaDaoServices.permitInsertStatus(
            permitDetailsDB,
            applicationMapProperties.mapQaStatusPFactoryVisitSchedule,
            loggedInUser
        )
        val closeLink =
            "${applicationMapProperties.baseUrlValue}/qa/permits-list?permitTypeID=${permitDetailsDB.permitType}"
        return Pair(permitDetailsDB, closeLink)
    }

    fun permitApproveJustificationReport(
        permit: PermitApplicationsEntity,
        map: ServiceMapsEntity,
        permitDetails: PermitApplicationsEntity,
        loggedInUser: UsersEntity
    ): Pair<PermitApplicationsEntity, String> {
        var reasonValue: String? = null
        var permitDetailsDB = permitDetails
        when (permit.justificationReportStatus) {
            map.activeStatus -> {
                reasonValue = "ACCEPTED"
//                qaDaoServices.justificationReportSendEmail(permitDetails, reasonValue)
                permitDetailsDB = qaDaoServices.permitInsertStatus(
                    permitDetails,
                    applicationMapProperties.mapQaStatusPAssesorAssigning,
                    loggedInUser
                )
            }
            map.inactiveStatus -> {
                reasonValue = "REJECTED"
                with(permitDetails) {
                    justificationReportStatus = 0
                    userTaskId = applicationMapProperties.mapUserTaskNameQAO
                }
                permitDetailsDB = qaDaoServices.permitInsertStatus(
                    permitDetails,
                    applicationMapProperties.mapQaStatusRejectedJustCationReport,
                    loggedInUser
                )
                qaDaoServices.justificationReportSendEmail(permitDetails, reasonValue)
            }
        }

        val closeLink = "${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${permitDetailsDB.id}"
        return Pair(permitDetailsDB, closeLink)
    }

    fun permitPSCApprovalProcess(
        permit: PermitApplicationsEntity,
        map: ServiceMapsEntity,
        permitDetails: PermitApplicationsEntity,
        loggedInUser: UsersEntity
    ): Pair<PermitApplicationsEntity, String> {
        var permitDetailsDB = permitDetails
        when (permit.pscMemberApprovalStatus) {
            map.activeStatus -> {
                with(permit) {
                    userTaskId = applicationMapProperties.mapUserTaskNamePCM
                    pcmId = qaDaoServices.assignNextOfficerWithDesignation(
                        permit,
                        map,
                        applicationMapProperties.mapQADesignationIDForPCMId
                    )?.id
                }
                //updating of Details in DB
                permitDetailsDB = qaDaoServices.permitUpdateDetails(
                    commonDaoServices.updateDetails(
                        permit,
                        permitDetailsDB
                    ) as PermitApplicationsEntity, map, loggedInUser
                ).second

                qaDaoServices.permitInsertStatus(
                    permitDetailsDB,
                    applicationMapProperties.mapQaStatusPPCMAwarding,
                    loggedInUser
                )
                qaDaoServices.sendNotificationPCMForAwardingPermit(permitDetailsDB)

            }
            map.inactiveStatus -> {
                with(permitDetailsDB) {
                    compliantStatus = 0
                    recommendationApprovalStatus = 0
                }

                permitDetailsDB.userTaskId = applicationMapProperties.mapUserTaskNameQAO
                qaDaoServices.permitInsertStatus(
                    permitDetailsDB,
                    applicationMapProperties.mapQaStatusDeferredPSCMembers,
                    loggedInUser
                )
                qaDaoServices.sendNotificationForDeferredPermitToQaoFromPSC(permitDetailsDB)

            }
        }
        val closeLink =
            "${applicationMapProperties.baseUrlValue}/qa/permits-list?permitTypeID=${permitDetailsDB.permitType}"
        return Pair(permitDetailsDB, closeLink)
    }

    fun approveRecommendationPermit(
        permit: PermitApplicationsEntity,
        map: ServiceMapsEntity,
        permitDetails: PermitApplicationsEntity,
        loggedInUser: UsersEntity
    ): Pair<PermitApplicationsEntity, String> {
        var permitDetailsDB = permitDetails
        when (permit.recommendationApprovalStatus) {
            map.activeStatus -> {
                with(permit) {
                    pscMemberId = qaDaoServices.assignNextOfficerAfterPayment(
                        permitDetailsDB,
                        map,
                        applicationMapProperties.mapQADesignationIDForPSCId
                    )?.id
                    userTaskId = applicationMapProperties.mapUserTaskNamePSC
                }
                //updating of Details in DB
                permitDetailsDB = qaDaoServices.permitUpdateDetails(
                    commonDaoServices.updateDetails(
                        permit,
                        permitDetailsDB
                    ) as PermitApplicationsEntity, map, loggedInUser
                ).second

                permitDetailsDB = qaDaoServices.permitInsertStatus(
                    permitDetailsDB,
                    applicationMapProperties.mapQaStatusPPSCMembersAward,
                    loggedInUser
                )
                qaDaoServices.sendNotificationPSCForAwardingPermit(permitDetailsDB)

            }
            map.inactiveStatus -> {
                permitDetailsDB.userTaskId = applicationMapProperties.mapUserTaskNameQAO
                permitDetailsDB = qaDaoServices.permitInsertStatus(
                    permitDetailsDB,
                    applicationMapProperties.mapQaStatusDeferredRecommendationQAM,
                    loggedInUser
                )
                qaDaoServices.sendNotificationForRecommendationCorrectness(permitDetailsDB)
            }
        }
        val closeLink =
            "${applicationMapProperties.baseUrlValue}/qa/permits-list?permitTypeID=${permitDetailsDB.permitType}"
        return Pair(permitDetailsDB, closeLink)
    }

    fun addRecommendation(
        permitDetails: PermitApplicationsEntity,
        loggedInUser: UsersEntity
    ): Pair<PermitApplicationsEntity, String> {
        var permitDetailsDB = permitDetails
        when (permitDetailsDB.permitType) {
            applicationMapProperties.mapQAPermitTypeIDDmark -> {
                permitDetailsDB.userTaskId = applicationMapProperties.mapUserTaskNameHOD
            }
            else -> {
                permitDetailsDB.userTaskId = applicationMapProperties.mapUserTaskNameQAM
            }
        }
        permitDetailsDB.recommendationApprovalStatus = null
        permitDetailsDB = qaDaoServices.permitInsertStatus(
            permitDetailsDB,
            applicationMapProperties.mapQaStatusPRecommendationApproval,
            loggedInUser
        )
        qaDaoServices.sendNotificationForRecommendation(permitDetails)

        val closeLink =
            "${applicationMapProperties.baseUrlValue}/qa/permits-list?permitTypeID=${permitDetailsDB.permitType}"
        return Pair(permitDetailsDB, closeLink)
    }

    fun factoryInspection(
        permitDetailsFromDB: PermitApplicationsEntity,
        loggedInUser: UsersEntity
    ): Pair<PermitApplicationsEntity, String> {
        var permitDetailsDB = permitDetailsFromDB
        permitDetailsDB = qaDaoServices.permitInsertStatus(
            permitDetailsDB,
            applicationMapProperties.mapQaStatusPfactoryInsForms,
            loggedInUser
        )
        val closeLink = "${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${permitDetailsDB.id}"
        return Pair(permitDetailsDB, closeLink)

    }

    fun assignQAOfficer(
        permitDetailsFromDB: PermitApplicationsEntity,
        permitType: PermitTypesEntity,
        loggedInUser: UsersEntity
    ): Pair<PermitApplicationsEntity, String> {
        var permitDetailsDB = permitDetailsFromDB
        val userDetails =
            commonDaoServices.findUserByID(permitDetailsDB.qaoId ?: throw ExpectedDataNotFound("MISSING QAO ID"))
        permitDetailsDB.userTaskId = applicationMapProperties.mapUserTaskNameQAO
        permitDetailsDB.factoryVisit = commonDaoServices.getCalculatedDate(
            permitType.factoryVisitDate ?: throw Exception("MISSING FACTORY INSPECTION DATE")
        )
        when (permitDetailsDB.permitType) {
            applicationMapProperties.mapQAPermitTypeIdSmark -> {
                permitDetailsDB = qaDaoServices.permitInsertStatus(
                    permitDetailsDB,
                    applicationMapProperties.mapQaStatusPFactoryVisitSchedule,
                    loggedInUser
                )
            }
            applicationMapProperties.mapQAPermitTypeIdFmark -> {
                permitDetailsDB = qaDaoServices.permitInsertStatus(
                    permitDetailsDB,
                    applicationMapProperties.mapQaStatusPFactoryVisitSchedule,
                    loggedInUser
                )
            }
            applicationMapProperties.mapQAPermitTypeIDDmark -> {
                permitDetailsDB = qaDaoServices.permitInsertStatus(
                    permitDetailsDB,
                    applicationMapProperties.mapQaStatusPfactoryInsForms,
                    loggedInUser
                )
            }
        }

        qaDaoServices.sendEmailWithTaskDetails(
            userDetails.email ?: throw ExpectedDataNotFound("MISSING QAO EMAIL"),
            permitDetailsDB.permitRefNumber ?: throw ExpectedDataNotFound("MISSING PERMIT REF NUMBER")
        )

        val closeLink =
            "${applicationMapProperties.baseUrlValue}/qa/permits-list?permitTypeID=${permitDetailsDB.permitType}"
        return Pair(permitDetailsDB, closeLink)
    }

    fun hodQamCompletenessCheck(
        permitFromInterface: PermitApplicationsEntity,
        map: ServiceMapsEntity,
        permitDetailsFromDB: PermitApplicationsEntity,
        loggedInUser: UsersEntity
    ): Pair<PermitApplicationsEntity, String> {
        var permitDetailsDB = permitDetailsFromDB
        var closeLink = "${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${permitDetailsDB.id}"
        when (permitFromInterface.hofQamCompletenessStatus) {
            map.activeStatus -> {
                permitDetailsDB = qaDaoServices.permitInsertStatus(
                    permitDetailsFromDB,
                    applicationMapProperties.mapQaStatusPQAOAssign,
                    loggedInUser
                )
            }
            map.inactiveStatus -> {
                with(permitDetailsDB) {
                    resubmitApplicationStatus = map.activeStatus
                    userTaskId = applicationMapProperties.mapUserTaskNameMANUFACTURE
                }
                permitDetailsDB = qaDaoServices.permitInsertStatus(
                    permitDetailsFromDB,
                    applicationMapProperties.mapQaStatusIncompleteAppl,
                    loggedInUser
                )
                closeLink =
                    "${applicationMapProperties.baseUrlValue}/qa/permits-list?permitTypeID=${permitDetailsDB.permitType}"
                qaDaoServices.sendNotificationForPermitReviewRejected(
                    permitDetailsDB,
                    permitFromInterface.hofQamCompletenessRemarks ?: throw Exception("MISSING REMARKS")
                )
            }
        }


        return Pair(permitDetailsDB, closeLink)
    }

    fun addedStandardsDetails(
        map: ServiceMapsEntity,
        permitDetailsFromDB: PermitApplicationsEntity,
        loggedInUser: UsersEntity
    ): Pair<PermitApplicationsEntity, String> {
        val permitDetailsDB = permitDetailsFromDB

        val closeLink = "${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${permitDetailsDB.id}"
        return Pair(permitDetailsDB, closeLink)
    }

    fun assigndefalutDetailsDetails(
        map: ServiceMapsEntity,
        permitDetailsFromDB: PermitApplicationsEntity,
        loggedInUser: UsersEntity
    ): Pair<PermitApplicationsEntity, String> {
        val permitDetailsDB = permitDetailsFromDB

        val closeLink = "${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${permitDetailsDB.id}"
        return Pair(permitDetailsDB, closeLink)
    }


    fun permitApplicationsPCMApprovalActions(
        permitDetailsFromDB: PermitApplicationsEntity,
        permitFromInterface: PermitApplicationsEntity,
        map: ServiceMapsEntity,
        loggedInUser: UsersEntity
    ): Pair<PermitApplicationsEntity, String> {
        var permitDetailsDB = permitDetailsFromDB

        when (permitFromInterface.pcmApprovalStatus) {
            map.activeStatus -> {
                //TODO: CHANGE THE DATE OF EXPIRY IF RENEWAL
                val issueDate = commonDaoServices.getCurrentDate()
                val permitType = permitDetailsDB.permitType?.let { qaDaoServices.findPermitType(it) }
                val expiryDate = permitType?.numberOfYears?.let { commonDaoServices.addYearsToCurrentDate(it) }

                with(permitFromInterface) {
                    awardedPermitNumber = if (permitDetailsFromDB.renewalStatus != map.activeStatus) {
                        "${permitType?.markNumber}${
                            generateRandomText(
                                6,
                                map.secureRandom,
                                map.messageDigestAlgorithm,
                                false
                            )
                        }".toUpperCase()
                    } else {
                        permitDetailsFromDB.awardedPermitNumber
                    }
                    userTaskId = null
                    permitAwardStatus = map.activeStatus
                    dateOfIssue = issueDate
                    dateOfExpiry = expiryDate
                }
                //Generate permit and forward to manufacturer
                KotlinLogging.logger { }.info(":::::: Sending compliance status along with e-permit :::::::")
                //updating of Details in DB
                permitDetailsDB = qaDaoServices.permitUpdateDetails(
                    commonDaoServices.updateDetails(
                        permitFromInterface,
                        permitDetailsDB
                    ) as PermitApplicationsEntity, map, loggedInUser
                ).second

                permitDetailsDB.userTaskId = null
                qaDaoServices.permitInsertStatus(
                    permitDetailsDB,
                    applicationMapProperties.mapQaStatusPermitAwarded,
                    loggedInUser
                )

                val permitID = permitDetailsDB.id ?: throw ExpectedDataNotFound("MISSING Permit ID, check config")
                val fileDetails = qaDaoServices.getFileCertificateIssuedPDFForm(permitID)

                qaDaoServices.sendNotificationForAwardedPermitToManufacture(permitDetailsDB, fileDetails.path)

            }
            map.inactiveStatus -> {
                with(permitDetailsDB) {
                    userTaskId = applicationMapProperties.mapUserTaskNameQAO
                    compliantStatus = 0
                    recommendationApprovalStatus = 0
                }
                permitDetailsDB = qaDaoServices.permitInsertStatus(
                    permitDetailsDB,
                    applicationMapProperties.mapQaStatusDeferredPCM,
                    loggedInUser
                )
                qaDaoServices.sendNotificationForDeferredPermitToQaoFromPCM(permitDetailsDB)

            }
        }

        val closeLink =
            "${applicationMapProperties.baseUrlValue}/qa/permits-list?permitTypeID=${permitDetailsDB.permitType}"
        return Pair(permitDetailsDB, closeLink)
    }

    fun permitApplicationsPCMReviewActions(
        permitDetailsFromDB: PermitApplicationsEntity,
        permitFromInterface: PermitApplicationsEntity,
        map: ServiceMapsEntity,
        loggedInUser: UsersEntity
    ): Pair<PermitApplicationsEntity, String> {
        var permitDetailsDB = permitDetailsFromDB
//        when {
//            permitDetailsDB.permitType == applicationMapProperties.mapQAPermitTypeIDDmark && permitFromInterface.sendForPcmReview == map.activeStatus -> {
        when (permitFromInterface.pcmReviewApprovalStatus) {
            map.activeStatus -> {
                KotlinLogging.logger { }.info(":::::: Sending compliance status along with e-permit :::::::")
                permitDetailsDB.userTaskId = applicationMapProperties.mapUserTaskNameMANUFACTURE
                permitDetailsDB = qaDaoServices.permitUpdateDetails(
                    commonDaoServices.updateDetails(
                        permitFromInterface,
                        permitDetailsDB
                    ) as PermitApplicationsEntity, map, loggedInUser
                ).second
                qaDaoServices.pcmGenerateInvoice(
                    map,
                    loggedInUser,
                    permitDetailsDB,
                    permitDetailsDB.permitType ?: throw Exception("ID NOT FOUND")
                )

                val permitUser = commonDaoServices.findUserByID(
                    permitDetailsDB.userId ?: throw ExpectedDataNotFound("Permit USER Id Not found")
                )
                val pair = qaDaoServices.consolidateInvoiceAndSendMail(
                    permitDetailsDB.id ?: throw ExpectedDataNotFound("MISSING PERMIT ID"), map, permitUser
                )
                var batchInvoice = pair.first
                permitDetailsDB = pair.second

//                qualityAssuranceBpmn.qaDmARCheckApplicationComplete(
//                    permitDetailsDB.id ?: throw Exception("MISSING PERMIT ID"),
//                    permitDetailsDB.userId ?: throw Exception("MISSING USER ID"),
//                    true
//                )
                //Application complete and successful
                qualityAssuranceBpmn.qaDmCheckApplicationComplete(permitDetailsDB.id ?: throw Exception("MISSING PERMIT ID"), true)

            }
            map.inactiveStatus -> {

                qaDaoServices.permitInsertStatus(
                    permitDetailsDB,
                    applicationMapProperties.mapQaStatusPendingCorrectionManf,
                    loggedInUser
                )
                //Rejected Permit creates a new version
//                        permitDetailsDB = qaDaoServices.permitRejectedVersionCreation(
//                            permitDetailsDB.id ?: throw ExpectedDataNotFound("MISSING PERMIT ID"), map, loggedInUser
//                        ).second

                with(permitDetailsDB) {
                    resubmitApplicationStatus = map.activeStatus
                    userTaskId = applicationMapProperties.mapUserTaskNameMANUFACTURE
                }

                qaDaoServices.sendNotificationForPermitReviewRejectedFromPCM(permitDetailsDB)

//                qualityAssuranceBpmn.qaDmARCheckApplicationComplete(
//                    permitDetailsDB.id ?: throw Exception("MISSING PERMIT ID"),
//                    permitDetailsDB.userId ?: throw Exception("MISSING USER ID"),
//                    false
//                )

                //Application complete and successful
                qualityAssuranceBpmn.qaDmCheckApplicationComplete(permitDetailsDB.id ?: throw Exception("MISSING PERMIT ID"), false)
            }
        }
        val closeLink =
            "${applicationMapProperties.baseUrlValue}/qa/permits-list?permitTypeID=${permitDetailsDB.permitType}"
        return Pair(permitDetailsDB, closeLink)
    }

    @PreAuthorize(
        "hasAuthority('PERMIT_APPLICATION') or hasAuthority('QA_MANAGER_ASSESSORS_MODIFY') or hasAuthority('QA_HOF_MODIFY') " +
                "or hasAuthority('QA_HOD_MODIFY') or hasAuthority('QA_OFFICER_MODIFY') or hasAuthority('QA_PAC_SECRETARY_MODIFY') or hasAuthority('QA_PSC_MEMBERS_MODIFY') or hasAuthority('QA_PCM_MODIFY') or hasAuthority('QA_ASSESSORS_MODIFY')"
    )
    @PostMapping("/apply/update-permit-resubmit")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun updateResubmitPermitDetails(
        @ModelAttribute("permit") permit: PermitApplicationsEntity,
        @RequestParam("permitID") permitID: Long,
        model: Model
    ): String? {

        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        var result: ServiceRequestsEntity?

        //Find Permit with permit ID
        var permitDetails = qaDaoServices.findPermitBYID(permitID)

        //Add Permit ID THAT was Fetched so That it wont create a new record while updating with the methode
        permit.id = permitDetails.id

        //updating of Details in DB
        val updateResults = qaDaoServices.permitUpdateDetails(
            commonDaoServices.updateDetails(
                permit,
                permitDetails
            ) as PermitApplicationsEntity, map, loggedInUser
        )

        result = updateResults.first

        permitDetails = updateResults.second

        when {

            permit.recommendationRemarks != null -> {
                with(permitDetails) {
                    recommendationApprovalStatus = null
                    recommendationApprovalRemarks = null
                }
                qaDaoServices.permitInsertStatus(
                    permitDetails,
                    applicationMapProperties.mapQaStatusPRecommendationApproval,
                    loggedInUser
                )
                qaDaoServices.sendNotificationForRecommendation(permitDetails)
            }

        }

        //updating of Details in DB
        result = qaDaoServices.permitUpdateDetails(
            commonDaoServices.updateDetails(
                permit,
                permitDetails
            ) as PermitApplicationsEntity, map, loggedInUser
        ).first


        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.closeLink =
            "${applicationMapProperties.baseUrlValue}/qa/permits-list?permitTypeID=${permitDetails.permitType}"
        sm.message = "${permit.description}"

        return commonDaoServices.returnValues(result, map, sm)
    }


    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/kebs/add/plant-details/save")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun addManufacturePlantDetails(
        model: Model,
        @ModelAttribute("manufacturePlantDetails") manufacturePlantDetails: ManufacturePlantDetailsEntity,
        results: BindingResult,
        redirectAttributes: RedirectAttributes
    ): String? {

        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        val result: ServiceRequestsEntity?

        result = qaDaoServices.addPlantDetailsManufacture(manufacturePlantDetails, map, loggedInUser)

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.closeLink = "${applicationMapProperties.baseUrlValue}/user/user-profile"
        sm.message =
            "Plant with the following building Name = ${manufacturePlantDetails.buildingName} was added successfully"

        return commonDaoServices.returnValues(result, map, sm)
    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @GetMapping("/kebs/renew/permit-details/save")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun permitRenewDetails(
        @RequestParam("permitID") permitID: Long,
        model: Model,
    ): String? {

        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        val result: ServiceRequestsEntity?

        val myRenewedPermit = qaDaoServices.permitUpdateNewWithSamePermitNumber(permitID, map, loggedInUser)
        val permit = myRenewedPermit.second
        result = myRenewedPermit.first

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.closeLink = "${applicationMapProperties.baseUrlValue}/qa/permits-list?permitTypeID=${permit.permitType}"
        sm.message = "You have Successful Renewed your Permit , Invoice has Been Generated"

        return commonDaoServices.returnValues(result, map, sm)
    }

    @PostMapping("/kebs/invoice/create-batch-invoice/save")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun permitInvoiceBatchAddDetails(
        @ModelAttribute("NewBatchInvoiceDto") NewBatchInvoiceDto: NewBatchInvoiceDto,
        @RequestParam("permitID") permitID: Long,
        model: Model,
    ): String? {

        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        val result: ServiceRequestsEntity?

        result = qaDaoServices.permitMultipleInvoiceCalculation(map, loggedInUser, NewBatchInvoiceDto).first

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.closeLink = "${applicationMapProperties.baseUrlValue}/qa/invoice/batch-details?batchID=${result.varField1}"
        sm.message = "Batch Invoice has Been Generated"

        return commonDaoServices.returnValues(result, map, sm)
    }

    @PostMapping("/kebs/add/new-qpsms-details/save")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun permitAddInspectionReportDetails(
        @ModelAttribute("QaInspectionTechnicalEntity") QaInspectionTechnicalEntity: QaInspectionTechnicalEntity,
        @RequestParam("permitID") permitID: Long,
        model: Model,
    ): String? {

        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        val result: ServiceRequestsEntity?

        result = qaDaoServices.permitAddNewInspectionReportDetailsTechnical(
            map,
            loggedInUser,
            permitID,
            QaInspectionTechnicalEntity
        )

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.closeLink =
            "${applicationMapProperties.baseUrlValue}/qa/inspection/inspection-report-details?permitID=${result.varField1}"
        sm.message = "Inspection Report has Been Generated(DRAFT)"

        return commonDaoServices.returnValues(result, map, sm)
    }

    @PostMapping("/kebs/add/new-recommendation-details/save")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun permitAddInspectionReportRecommendationDetails(
        @ModelAttribute("QaInspectionReportRecommendationEntity") QaInspectionReportRecommendationEntity: QaInspectionReportRecommendationEntity,
        @RequestParam("permitID") permitID: Long,
        model: Model,
    ): String? {

        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        val result: ServiceRequestsEntity?
        var permitFound = qaDaoServices.findPermitBYID(permitID)

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.closeLink = "${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${permitFound.id}"
        sm.message = "Inspection Report has Been updated successfully"

        var qaInspectionReportRecommendation = qaDaoServices.findQaInspectionReportRecommendationBYPermitRefNumber(
            permitFound.permitRefNumber ?: throw Exception("INVALID PERMIT REF NUMBER FOUND")
        )

        qaInspectionReportRecommendation = commonDaoServices.updateDetails(
            QaInspectionReportRecommendationEntity,
            qaInspectionReportRecommendation
        ) as QaInspectionReportRecommendationEntity

        result = qaDaoServices.inspectionRecommendationUpdate(qaInspectionReportRecommendation, map, loggedInUser)
        var userAssigned: UsersEntity
        when {
            //Todo: Create jasper inspection report

            QaInspectionReportRecommendationEntity.submittedInspectionReportStatus == 1 -> {
                permitFound.inspectionReportGenerated = 1
                when (permitFound.permitType) {
                    applicationMapProperties.mapQAPermitTypeIDDmark -> {
                        permitFound.userTaskId = applicationMapProperties.mapUserTaskNameHOD
                        userAssigned = commonDaoServices.findUserByID(
                            permitFound.hodId ?: throw ExpectedDataNotFound("MISSING HOD ID")
                        )
                    }
                    else -> {
                        permitFound.userTaskId = applicationMapProperties.mapUserTaskNameQAM
                        userAssigned = commonDaoServices.findUserByID(
                            permitFound.qamId ?: throw ExpectedDataNotFound("MISSING QAM ID")
                        )
                    }
                }
                permitFound = qaDaoServices.permitInsertStatus(
                    permitFound,
                    applicationMapProperties.mapQaStatusPInspectionReportApproval,
                    loggedInUser
                )
                sm.closeLink = "${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${permitFound.id}"
                qaDaoServices.sendEmailWithTaskDetails(
                    userAssigned.email ?: throw ExpectedDataNotFound("Missing Email address"),
                    permitFound.permitRefNumber ?: throw ExpectedDataNotFound("MISSING PERMIT REF NUMBER")
                )
                sendInspectionReport(permitFound)
            }
            QaInspectionReportRecommendationEntity.supervisorFilledStatus == 1 -> {
                permitFound.userTaskId = applicationMapProperties.mapUserTaskNameQAO
                when (QaInspectionReportRecommendationEntity.approvedRejectedStatus) {
                    1 -> {
                        permitFound.factoryInspectionReportApprovedRejectedStatus = map.activeStatus
                        permitFound = qaDaoServices.permitInsertStatus(
                            permitFound,
                            applicationMapProperties.mapQaStatusPGenSSC,
                            loggedInUser
                        )
                    }
                    0 -> {
                        with(qaInspectionReportRecommendation) {
                            supervisorFilledStatus = null
                            submittedInspectionReportStatus = null
                        }
                        qaDaoServices.inspectionRecommendationUpdate(
                            qaInspectionReportRecommendation,
                            map,
                            loggedInUser
                        )
                        with(permitFound) {
                            //                        inspectionReportGenerated = map.inactiveStatus
                            factoryInspectionReportApprovedRejectedStatus = map.inactiveStatus
                        }
                        permitFound = qaDaoServices.permitInsertStatus(
                            permitFound,
                            applicationMapProperties.mapQaStatusPendingCorrectionManf,
                            loggedInUser
                        )
                    }
                }
                userAssigned =
                    commonDaoServices.findUserByID(permitFound.qaoId ?: throw ExpectedDataNotFound("MISSING QAO ID"))
                //Todo: CHECK IF SSC to be generated
                sm.closeLink =
                    "${applicationMapProperties.baseUrlValue}/qa/permits-list?permitTypeID=${permitFound.permitType}"
                qaDaoServices.sendEmailWithTaskDetails(
                    userAssigned.email ?: throw ExpectedDataNotFound("Missing Email address"),
                    permitFound.permitRefNumber ?: throw ExpectedDataNotFound("MISSING PERMIT REF NUMBER")
                )
            }
        }



        return commonDaoServices.returnValues(result, map, sm)
    }

    @PostMapping("/kebs/add/new-haccp-implementation/save")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun permitAddInspectionReportHACCPImplimantationDetails(
        @ModelAttribute("QaInspectionHaccpImplementationEntity") QaInspectionHaccpImplementationEntity: QaInspectionHaccpImplementationEntity,
        @RequestParam("permitID") permitID: Long,
        model: Model,
    ): String? {

        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        val result: ServiceRequestsEntity?

        result = qaDaoServices.permitAddNewInspectionReportDetailsHaccp(
            map,
            loggedInUser,
            permitID,
            QaInspectionHaccpImplementationEntity
        )

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.closeLink =
            "${applicationMapProperties.baseUrlValue}/qa/inspection/inspection-report-details?permitID=${result.varField1}"
        sm.message = "Inspection Report Details have Been added Successful"

        return commonDaoServices.returnValues(result, map, sm)
    }

    @PostMapping("/kebs/add/new-opc-details/save")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun permitAddInspectionReportOPCDetails(
        @ModelAttribute("QaInspectionOpcEntity") QaInspectionOpcEntity: QaInspectionOpcEntity,
        @RequestParam("permitID") permitID: Long,
        model: Model,
    ): String? {

        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        val result: ServiceRequestsEntity?

        result =
            qaDaoServices.permitAddNewInspectionReportDetailsOPC(map, loggedInUser, permitID, QaInspectionOpcEntity)

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.closeLink =
            "${applicationMapProperties.baseUrlValue}/qa/inspection/inspection-report-details?permitID=${result.varField1}"
        sm.message = "Inspection Report Details have Been added Successful"

        return commonDaoServices.returnValues(result, map, sm)
    }

    @PostMapping("/kebs/invoice/remove-invoice-detail/save")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun permitInvoiceBatchRemoveDetails(
        @ModelAttribute("NewBatchInvoiceDto") batchDetailsRemover: NewBatchInvoiceDto,
        @RequestParam("permitID") permitID: Long,
//        @RequestParam("batchID") batchID: Long,
        model: Model,
    ): String? {

        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        val result: ServiceRequestsEntity?

//        val batchDetailsRemover =NewBatchInvoiceDto()
//        batchDetailsRemover.batchID= batchID
//        batchDetailsRemover.permitID= permitID

        result =
            qaDaoServices.permitMultipleInvoiceRemoveInvoice(map, loggedInUser, batchDetailsRemover).first

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.closeLink = "${applicationMapProperties.baseUrlValue}/qa/invoice/batch-details?batchID=${result.varField1}"
        sm.message = "Batch Invoice has Been Updated"

        return commonDaoServices.returnValues(result, map, sm)
    }


    @PostMapping("/kebs/invoice/submit-invoice-detail/save")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun permitInvoiceBatchSubmitDetails(
        @ModelAttribute("NewBatchInvoiceDto") NewBatchInvoiceDto: NewBatchInvoiceDto,
        model: Model,
    ): String? {

        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        val result: ServiceRequestsEntity?

        result = qaDaoServices.permitMultipleInvoiceSubmitInvoice(map, loggedInUser, NewBatchInvoiceDto).first

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.closeLink = "${applicationMapProperties.baseUrlValue}/qa/invoice/batch-details?batchID=${result.varField1}"
        sm.message = "Batch Invoice Submitted Successful"

        return commonDaoServices.returnValues(result, map, sm)
    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @GetMapping("/kebs/resubmit/permit-details/save")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun permitReSubmitDetails(
        @RequestParam("permitID") permitID: Long,
        model: Model,
    ): String? {

        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val permit = loggedInUser.id?.let { qaDaoServices.findPermitBYUserIDAndId(permitID, it) }
            ?: throw ExpectedDataNotFound("User Id required")

        val result: ServiceRequestsEntity?

        when {
            permit.sendForPcmReview == map.activeStatus && permit.pcmApprovalStatus == map.inactiveStatus -> {
                with(permit) {
                    resubmitApplicationStatus = null
                    permitStatus = applicationMapProperties.mapQaStatusResubmitted
                }
            }
            else -> {
                with(permit) {
                    resubmitApplicationStatus = map.activeStatus
                    hofQamCompletenessStatus = null
                    permitStatus = applicationMapProperties.mapQaStatusResubmitted
                }
            }
        }


        result = qaDaoServices.permitUpdateDetails(permit, map, loggedInUser).first

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.closeLink = "${applicationMapProperties.baseUrlValue}/qa/permits-list?permitTypeID=${permit.permitType}"
        sm.message = "You have Successful resubmitted your Permit for approval"

        return commonDaoServices.returnValues(result, map, sm)
    }

    @PreAuthorize("hasAuthority('QA_OFFICER_MODIFY')")
    @PostMapping("/kebs/lab-results-compliance-status/save")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun complianceStatusSSF(
        @RequestParam("ssfID") ssfID: Long,
        @ModelAttribute("SampleSubmissionDetails") sampleSubmissionDetails: QaSampleSubmissionEntity,
        model: Model
    ): String? {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        val result: ServiceRequestsEntity?

        result = qaDaoServices.ssfUpdateDetails(ssfID, sampleSubmissionDetails, loggedInUser, map).first

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.closeLink = "${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${result.varField1}"
        sm.message = "You have Successful Filled Sample Submission Details"

        return commonDaoServices.returnValues(result, map, sm)
    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION') or hasAuthority('QA_MANAGER_ASSESSORS_MODIFY') or hasAuthority('QA_HOF_MODIFY') or hasAuthority('QA_HOD_MODIFY') or hasAuthority('QA_OFFICER_MODIFY') or hasAuthority('QA_PAC_SECRETARY_MODIFY') or hasAuthority('QA_PSC_MEMBERS_MODIFY') or hasAuthority('QA_PCM_MODIFY') or hasAuthority('QA_ASSESSORS_MODIFY')")
    @PostMapping("/kebs/request-update-details")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun permitRequestForUpdates(
        @RequestParam("permitID") permitID: Long,
        @ModelAttribute("requestDetails") requestDetails: PermitUpdateDetailsRequestsEntity,
        model: Model
    ): String? {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val permit = qaDaoServices.findPermitBYID(permitID)

        val result: ServiceRequestsEntity?

        result = qaDaoServices.permitRequests(requestDetails, permitID, loggedInUser, map).first

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.closeLink = "${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${permit.id}"
        sm.message = "Your request has been submitted Successful Details"

        return commonDaoServices.returnValues(result, map, sm)
    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION') or hasAuthority('QA_MANAGER_ASSESSORS_MODIFY') or hasAuthority('QA_HOF_MODIFY') or hasAuthority('QA_HOD_MODIFY') or hasAuthority('QA_OFFICER_MODIFY') or hasAuthority('QA_PAC_SECRETARY_MODIFY') or hasAuthority('QA_PSC_MEMBERS_MODIFY') or hasAuthority('QA_PCM_MODIFY') or hasAuthority('QA_ASSESSORS_MODIFY')")
    @PostMapping("/kebs/update-request-details")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun permitRequestUpdates(
        @RequestParam("requestID") requestID: Long,
        @ModelAttribute("requestDetails") requestDetails: PermitUpdateDetailsRequestsEntity,
        model: Model
    ): String? {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        val result: ServiceRequestsEntity?

        val myResults = qaDaoServices.requestUpdateDetails(requestID, requestDetails, loggedInUser, map)

        result = myResults.first

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.closeLink =
            "${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${myResults.second.permitId}"
        sm.message = "Your request has been submitted Successful Details"

        return commonDaoServices.returnValues(result, map, sm)
    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @GetMapping("/kebs/new-permit-submit-review")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun permitDmarkSubmitForReviewDetails(
        @RequestParam("permitID") permitID: Long,
        model: Model
    ): String? {

        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val permit = loggedInUser.id?.let { qaDaoServices.findPermitBYUserIDAndId(permitID, it) }
            ?: throw ExpectedDataNotFound("User Id required")

        val result: ServiceRequestsEntity?

        with(permit) {
            sendForPcmReview = map.activeStatus
            pcmId = qaDaoServices.assignNextOfficerWithDesignation(
                permit,
                map,
                applicationMapProperties.mapQADesignationIDForPCMId
            )?.id
            permitStatus = applicationMapProperties.mapQaStatusPPCMAwarding
            userTaskId = applicationMapProperties.mapUserTaskNamePCM
        }

        result = qaDaoServices.permitUpdateDetails(permit, map, loggedInUser).first

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.closeLink = "${applicationMapProperties.baseUrlValue}/qa/permits-list?permitTypeID=${permit.permitType}"
        sm.message = "You have Successful submitted your Permit for review"

        return commonDaoServices.returnValues(result, map, sm)
    }

//    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
//    @GetMapping("/kebs/new-permit-submit-review-")
//    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
//    fun permitDmarkPCMForeginReviewDetails(
//        @RequestParam("permitID") permitID: Long,
//        model: Model
//    ): String? {
//
//        val map = commonDaoServices.serviceMapDetails(appId)
//        val loggedInUser = commonDaoServices.loggedInUserDetails()
//        val permit = loggedInUser.id?.let { qaDaoServices.findPermitBYUserIDAndId(permitID, it) }
//            ?: throw ExpectedDataNotFound("User Id required")
//
//        val result: ServiceRequestsEntity?
//
//        with(permit) {
//            sendForPcmReview = map.activeStatus
//            pcmId = qaDaoServices.assignNextOfficerWithDesignation(
//                permit,
//                map,
//                applicationMapProperties.mapQADesignationIDForPCMId
//            )?.id
//            permitStatus = applicationMapProperties.mapQaStatusPPCMAwarding
//            userTaskId = applicationMapProperties.mapUserTaskNamePCM
//        }
//
//        result = qaDaoServices.permitUpdateDetails(permit, map, loggedInUser).first
//
//        val sm = CommonDaoServices.MessageSuccessFailDTO()
//        sm.closeLink = "${applicationMapProperties.baseUrlValue}/qa/permits-list?permitTypeID=${permit.permitType}"
//        sm.message = "You have Successful submitted your Permit for review"
//
//        return commonDaoServices.returnValues(result, map, sm)
//    }


    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @PostMapping("/apply/new-sta3")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun saveNewSta3(
        @RequestParam("permitID") permitID: Long,
        @RequestParam("permitViewType") permitViewType: String,
        @ModelAttribute("QaSta3Entity") QaSta3Entity: QaSta3Entity,
        model: Model
    ): String? {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val permit = loggedInUser.id?.let { qaDaoServices.findPermitBYUserIDAndId(permitID, it) } ?: throw ExpectedDataNotFound("User Id required")


        when (permitViewType) {
            applicationMapProperties.mapPermitRenewMessage -> {
                val sta3 = qaDaoServices.findSTA3WithPermitIDAndRefNumber(
                    permit.permitRefNumber ?: throw Exception("INVALID PERMIT REF NUMBER"), permitID
                )
                QaSta3Entity.id = sta3.id
                qaDaoServices.sta3Update(
                    commonDaoServices.updateDetails(sta3, QaSta3Entity) as QaSta3Entity,
                    map,
                    loggedInUser
                )
            }
            applicationMapProperties.mapPermitNewMessage -> {
                qaDaoServices.sta3NewSave(
                    permitID,
                    permit.permitRefNumber ?: throw Exception("INVALID PERMIT REF NUMBER"),
                    QaSta3Entity,
                    loggedInUser,
                    map
                )
            }
        }
        val result: ServiceRequestsEntity?

        val updatePermit = PermitApplicationsEntity()
        with(updatePermit) {
            id = permit.id
            sta3FilledStatus = map.activeStatus
            permitStatus = applicationMapProperties.mapQaStatusPSubmission
        }
        //updating of Details in DB
        result = qaDaoServices.permitUpdateDetails(
            commonDaoServices.updateDetails(updatePermit, permit) as PermitApplicationsEntity, map, loggedInUser
        ).first

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.closeLink =
            "${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${permitID}"
        sm.message = "You have Successful Filled STA 3 and has been submitted successful , Submit your application"

        return commonDaoServices.returnValues(result, map, sm)
    }


    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @PostMapping("/apply/new-sta10")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun saveNewSta10(
        @RequestParam("permitID") permitID: Long,
        @RequestParam("permitViewType") permitViewType: String,
        @ModelAttribute("QaSta10Entity") QaSta10Entity: QaSta10Entity,
        model: Model
    ): String? {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val permit = loggedInUser.id?.let { qaDaoServices.findPermitBYUserIDAndId(permitID, it) }
            ?: throw ExpectedDataNotFound("User Id required")
        when (permitViewType) {
            applicationMapProperties.mapPermitRenewMessage -> {
                val sta10 = qaDaoServices.findSTA10WithPermitRefNumberANdPermitID(
                    permit.permitRefNumber ?: throw Exception("INVALID PERMIT REF NUMBER"), permitID
                )
                with(QaSta10Entity){
                    id = sta10.id
                    closedProduction=map.inactiveStatus
                    closedRawMaterials=map.inactiveStatus
                    closedMachineryPlants=map.inactiveStatus
                    closedManufacturingProcesses=map.inactiveStatus
                }
                qaDaoServices.sta10Update(commonDaoServices.updateDetails(QaSta10Entity, sta10) as QaSta10Entity, map, loggedInUser)
            }
            applicationMapProperties.mapPermitNewMessage -> {
              qaDaoServices.sta10NewSave(permit, QaSta10Entity, loggedInUser, map)
            }
        }

        val result: ServiceRequestsEntity?

        with(permit) {
            id = permit.id
            sta10FilledStatus = map.inactiveStatus
            permitStatus = applicationMapProperties.mapQaStatusPSTA10Completion
        }
        //updating of Details in DB

        result = qaDaoServices.permitUpdateDetails(permit, map, loggedInUser).first

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.closeLink =
            "${applicationMapProperties.baseUrlValue}/qa/view-sta10?permitID=${permitID}"
        sm.message = "You have Successful Filled Some part of STA 10, Processed To finish the Rest and submit"

        return commonDaoServices.returnValues(result, map, sm)
    }

    @PreAuthorize("hasAuthority('QA_OFFICER_MODIFY')")
    @PostMapping("/kebs/ssf-details-uploads")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun saveNewSSF(
        @RequestParam("permitID") permitID: Long,
        @ModelAttribute("SampleSubmissionDetails") sampleSubmissionDetails: QaSampleSubmissionEntity,
        model: Model
    ): String? {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val permit = qaDaoServices.findPermitBYID(permitID)

        val result: ServiceRequestsEntity?

        //updating of Details in DB
        result = qaDaoServices.ssfSave(permit, sampleSubmissionDetails, loggedInUser, map).first
        with(permit) {
            ssfCompletedStatus = map.activeStatus
        }
        qaDaoServices.permitInsertStatus(permit, applicationMapProperties.mapQaStatusPLABResults, loggedInUser)

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.closeLink = "${applicationMapProperties.baseUrlValue}/qa/inspection/ssf-list?permitID=${permitID}"
        sm.message = "You have Successful added a BS Number"

        return commonDaoServices.returnValues(result, map, sm)
    }


    @PreAuthorize("hasAuthority('QA_MANAGER_ASSESSORS_MODIFY') or hasAuthority('QA_OFFICER_MODIFY')")
    @PostMapping("/apply/new-sta10-officer")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun saveNewSta10Officer(
        @RequestParam("sta10ID") sta10ID: Long,
        @ModelAttribute("QaSta10Entity") QaSta10Entity: QaSta10Entity,
        model: Model
    ): String? {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        var foundSta10Entity = qaDaoServices.findSta10BYID(sta10ID)
        foundSta10Entity = qaDaoServices.sta10OfficerNewSave(
            commonDaoServices.updateDetails(foundSta10Entity, QaSta10Entity) as QaSta10Entity, map, loggedInUser
        )
        val permit = foundSta10Entity.permitId?.let { qaDaoServices.findPermitBYID(it) }
            ?: throw ExpectedDataNotFound("PERMIT ID ON STA10  with [id=${sta10ID}] is NULL")

        val result: ServiceRequestsEntity?

        val updatePermit = PermitApplicationsEntity()
        with(updatePermit) {
            id = permit.id
            sta10FilledOfficerStatus = map.activeStatus
        }
        //updating of Details in DB
        result = qaDaoServices.permitUpdateDetails(
            commonDaoServices.updateDetails(
                permit,
                updatePermit
            ) as PermitApplicationsEntity, map, loggedInUser
        ).first


        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.closeLink = "${applicationMapProperties.baseUrlValue}/qa/view-sta10?permitID=${permit.id}"
        sm.message = "You have Successful Filled STA 10 Official Part"

        return commonDaoServices.returnValues(result, map, sm)
    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @PostMapping("/add/new-sta10-product-manufactured")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun saveNewSta10ProductManufactured(
        @RequestParam("closeStatus") closeStatus: Int?,
        @RequestParam("qaSta10ID") qaSta10ID: Long,
        @ModelAttribute("QaProductManufacturedEntity") QaProductManufacturedEntity: QaProductManufacturedEntity,
        model: Model,
        result: BindingResult
    ): String? {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val qaSta10 = qaDaoServices.findSta10BYID(qaSta10ID)
        if (closeStatus != null) {
            qaSta10.closedProduction = map.activeStatus
            qaDaoServices.sta10Update(qaSta10, map, loggedInUser)
        } else {
            qaSta10.id?.let {
//                qaDaoServices.sta10ManufactureProductNewSave(
//                    it,
//                    QaProductManufacturedEntity,
//                    loggedInUser,
//                    map
//                )
            }
        }


        return "${qaDaoServices.sta10Details}=${qaSta10.permitId}"
    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @PostMapping("/add/new-sta10-raw-materials")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun saveNewSta10RawMaterials(
        @RequestParam("closeStatus") closeStatus: Int?,
        @RequestParam("qaSta10ID") qaSta10ID: Long,
        @ModelAttribute("QaRawMaterialEntity") QaRawMaterialEntity: List<QaRawMaterialEntity>,
        model: Model,
        result: BindingResult
    ): String? {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val qaSta10 = qaDaoServices.findSta10BYID(qaSta10ID)
        if (closeStatus != null) {
            qaSta10.closedRawMaterials = map.activeStatus
            qaDaoServices.sta10Update(qaSta10, map, loggedInUser)
        } else {
            qaSta10.id?.let { qaDaoServices.sta10RawMaterialsNewSave(it, QaRawMaterialEntity, loggedInUser, map) }
        }
        return "${qaDaoServices.sta10Details}=${qaSta10.permitId}"
    }


    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @PostMapping("/add/new-sta10-machine-plant")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun saveNewSta10MachinePlant(
        @RequestParam("closeStatus") closeStatus: Int?,
        @RequestParam("qaSta10ID") qaSta10ID: Long,
        @ModelAttribute("QaMachineryEntity") QaMachineryEntity: QaMachineryEntity,
        model: Model,
        result: BindingResult
    ): String? {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val qaSta10 = qaDaoServices.findSta10BYID(qaSta10ID)
        if (closeStatus != null) {
            qaSta10.closedMachineryPlants = map.activeStatus
            qaDaoServices.sta10Update(qaSta10, map, loggedInUser)
        } else {
//            qaSta10.id?.let { qaDaoServices.sta10MachinePlantNewSave(it, QaMachineryEntity, loggedInUser, map) }
        }
        return "${qaDaoServices.sta10Details}=${qaSta10.permitId}"
    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @PostMapping("/add/new-sta10-manufacturing-process")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun saveNewSta10MachinePlant(
        @RequestParam("closeStatus") closeStatus: Int?,
        @RequestParam("qaSta10ID") qaSta10ID: Long,
        @ModelAttribute("QaManufacturingProcessEntity") QaManufacturingProcessEntity: List<QaManufacturingProcessEntity>,
        model: Model,
        result: BindingResult
    ): String? {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val qaSta10 = qaDaoServices.findSta10BYID(qaSta10ID)
        if (closeStatus != null) {
            qaSta10.closedManufacturingProcesses = map.activeStatus
            qaDaoServices.sta10Update(qaSta10, map, loggedInUser)
        } else {
            qaSta10.id?.let {
                qaDaoServices.sta10ManufacturingProcessNewSave(
                    it,
                    QaManufacturingProcessEntity,
                    loggedInUser,
                    map
                )
            }
        }
        return "${qaDaoServices.sta10Details}=${qaSta10.permitId}"
    }

    @PreAuthorize(
        "hasAuthority('PERMIT_APPLICATION') or hasAuthority('QA_OFFICER_MODIFY') or hasAuthority('QA_HOD_MODIFY') or hasAuthority('QA_MANAGER_ASSESSORS_MODIFY')" +
                " or hasAuthority('QA_HOF_MODIFY') or hasAuthority('QA_ASSESSORS_MODIFY') or hasAuthority('QA_PAC_SECRETARY_MODIFY') or hasAuthority('QA_PSC_MEMBERS_MODIFY') or hasAuthority('QA_PCM_MODIFY')"
    )
    @PostMapping("/kebs/add/new-upload")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun uploadFilesQA(
        @RequestParam("permitID") permitID: Long,
        @RequestParam("manufactureNonStatus") manufactureNonStatus: Int,
        @RequestParam("ordinaryStatus") ordinaryStatus: Int,
        @RequestParam("inspectionReportStatus") inspectionReportStatus: Int?,
        @RequestParam("sta10Status") sta10Status: Int?,
        @RequestParam("sscUploadStatus") sscUploadStatus: Int?,
        @RequestParam("scfStatus") scfStatus: Int?,
        @RequestParam("ssfStatus") ssfStatus: Int?,
        @RequestParam("cocStatus") cocStatus: Int?,
        @RequestParam("assessmentReportStatus") assessmentReportStatus: Int?,
        @RequestParam("labResultsStatus") labResultsStatus: Int?,
        @RequestParam("docFileName") docFileName: String,
        @RequestParam("doc_file") docFile: MultipartFile,
        @RequestParam("assessment_recommendations") assessmentRecommendations: String?,
        model: Model
    ): String? {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        var permitDetails = qaDaoServices.findPermitBYID(permitID)

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.closeLink = "${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${permitDetails.id}"
        sm.message = "Document Uploaded successful"

        val result: ServiceRequestsEntity?
        val uploads = QaUploadsEntity()
        var versionNumber: Long = 1
        var uploadResults: Pair<ServiceRequestsEntity, QaUploadsEntity>? = null

        when (ordinaryStatus) {
            map.activeStatus -> {
                uploads.ordinaryStatus = ordinaryStatus
                uploadResults = qaDaoServices.saveQaFileUploads(
                    docFile,
                    docFileName,
                    loggedInUser,
                    map,
                    uploads,
                    permitDetails.permitRefNumber?: throw Exception("INVALID PERMIT REF NUMBER"),
                    versionNumber,
                    manufactureNonStatus
                )
            }
            map.inactiveStatus -> {
                uploads.ordinaryStatus = ordinaryStatus
                when {
                    cocStatus != null -> {
                        uploads.cocStatus = cocStatus
                        versionNumber = qaDaoServices.findAllUploadedFileBYPermitRefNumberAndCocStatus(
                            permitDetails.permitRefNumber?: throw Exception("INVALID PERMIT REF NUMBER"),
                            map.activeStatus
                        ).size.toLong().plus(versionNumber)
                        uploadResults = qaDaoServices.saveQaFileUploads(
                            docFile,
                            docFileName,
                            loggedInUser,
                            map,
                            uploads,
                            permitDetails.permitRefNumber?: throw Exception("INVALID PERMIT REF NUMBER"),
                            versionNumber,
                            manufactureNonStatus
                        )
                        permitDetails.cocId = uploadResults.second.id
                        permitDetails = qaDaoServices.permitUpdateDetails(permitDetails, map, loggedInUser).second
                        qaDaoServices.permitInsertStatus(
                            permitDetails,
                            applicationMapProperties.mapQaStatusCocUploaded,
                            loggedInUser
                        )

                    }
                    sscUploadStatus != null -> {
                        uploads.sscStatus = sscUploadStatus
                        uploadResults = uploadedSCC(
                            versionNumber,
                            permitDetails,
                            map,
                            uploadResults,
                            docFile,
                            docFileName,
                            loggedInUser,
                            uploads,
                            manufactureNonStatus
                        )

                    }
                    assessmentReportStatus != null -> {
                        uploads.assessmentReportStatus = assessmentReportStatus
                        versionNumber = qaDaoServices.findAllUploadedFileBYPermitRefNumberAndAssessmentReportStatus(
                            permitDetails.permitRefNumber?: throw Exception("INVALID PERMIT REF NUMBER"),
                            map.activeStatus
                        ).size.toLong().plus(versionNumber)
                        uploadResults = qaDaoServices.saveQaFileUploads(
                            docFile,
                            docFileName,
                            loggedInUser,
                            map,
                            uploads,
                            permitDetails.permitRefNumber?: throw Exception("INVALID PERMIT REF NUMBER"),
                            versionNumber,
                            manufactureNonStatus
                        )

                        val hodDetails = qaDaoServices.assignNextOfficerAfterPayment(
                            permitDetails,
                            map,
                            applicationMapProperties.mapQADesignationIDForHODId
                        )


                        with(permitDetails) {
                            assessmentScheduledStatus = map.successStatus
                            assessmentReportRemarks = assessmentRecommendations
                            hodId = hodDetails?.id
                            permitStatus = applicationMapProperties.mapQaStatusPApprovalAssesmentReport
                            userTaskId = applicationMapProperties.mapUserTaskNameHOD
                        }
                        permitDetails = qaDaoServices.permitUpdateDetails(permitDetails, map, loggedInUser).second

                        //Send notification to HOD secretary
                        val hodSec = hodDetails?.id?.let { commonDaoServices.findUserByID(it) }
                        hodSec?.email?.let { qaDaoServices.sendPacDmarkAssessmentNotificationEmail(it, permitDetails) }
                        sm.closeLink =
                            "${applicationMapProperties.baseUrlValue}/qa/permits-list?permitTypeID=${permitDetails.permitType}"


                    }
                    inspectionReportStatus != null -> {
                        val pair = uloadsForInspectionReport(
                            uploads,
                            inspectionReportStatus,
                            uploadResults,
                            docFile,
                            docFileName,
                            loggedInUser,
                            map,
                            permitDetails,
                            versionNumber,
                            manufactureNonStatus
                        )
                        permitDetails = pair.first
                        uploadResults = pair.second
                        sm.closeLink =
                            "${applicationMapProperties.baseUrlValue}/qa/inspection/inspection-report-details?permitID=${permitDetails.id}"
//                        qaDaoServices.permitInsertStatus(permitDetails, applicationMapProperties.mapQaStatusPApprSSC, loggedInUser)

                    }
                    sta10Status != null -> {
                        uploads.sta10Status = sta10Status
//                        versionNumber = qaDaoServices.findAllUploadedFileBYPermitIDAndSscStatus(permitID, map.activeStatus).size.toLong().plus(versionNumber)
                        uploadResults = qaDaoServices.saveQaFileUploads(
                            docFile,
                            docFileName,
                            loggedInUser,
                            map,
                            uploads,
                            permitDetails.permitRefNumber?: throw Exception("INVALID PERMIT REF NUMBER"),
                            versionNumber,
                            manufactureNonStatus
                        )
//                        permitDetails.generateSchemeStatus = map.activeStatus
//                        permitDetails.sscId = uploadResults.second.id
                        permitDetails = qaDaoServices.permitUpdateDetails(permitDetails, map, loggedInUser).second
//                        qaDaoServices.permitInsertStatus(permitDetails, applicationMapProperties.mapQaStatusPApprSSC, loggedInUser)

                    }

                }
            }
        }

        result = uploadResults?.first


        return commonDaoServices.returnValues(result ?: throw Exception("invalid results"), map, sm)
    }

    fun uploadedSCC(
        versionNumber: Long,
        permitDetails: PermitApplicationsEntity,
        map: ServiceMapsEntity,
        uploadResults: Pair<ServiceRequestsEntity, QaUploadsEntity>?,
        docFile: MultipartFile,
        docFileName: String,
        loggedInUser: UsersEntity,
        uploads: QaUploadsEntity,
        manufactureNonStatus: Int
    ): Pair<ServiceRequestsEntity, QaUploadsEntity>? {
        var versionNumber1 = versionNumber
        var permitDetails1 = permitDetails
        var uploadResults1 = uploadResults
        versionNumber1 = qaDaoServices.findAllUploadedFileBYPermitRefNumberAndSscStatus(
            permitDetails1.permitRefNumber ?: throw Exception("INVALID PERMIT REF NUMBER"),
            map.activeStatus
        ).size.toLong().plus(versionNumber1)
        uploadResults1 = qaDaoServices.saveQaFileUploads(
            docFile,
            docFileName,
            loggedInUser,
            map,
            uploads,
            permitDetails1.permitRefNumber ?: throw Exception("INVALID PERMIT REF NUMBER"),
            versionNumber1,
            manufactureNonStatus
        )
        permitDetails1.generateSchemeStatus = map.activeStatus
        permitDetails1.sscId = uploadResults1.second.id
        permitDetails1 = qaDaoServices.permitUpdateDetails(permitDetails1, map, loggedInUser).second
        qaDaoServices.permitInsertStatus(
            permitDetails1,
            applicationMapProperties.mapQaStatusPApprSSC,
            loggedInUser
        )
        qaDaoServices.sendEmailWithSSC(
            commonDaoServices.findUserByID(
                permitDetails1.userId ?: throw ExpectedDataNotFound("MISSING USER ID")
            ).email ?: throw ExpectedDataNotFound("MISSING USER ID"),
            commonDaoServices.convertMultipartFileToFile(docFile).path,
            permitDetails1.permitRefNumber ?: throw ExpectedDataNotFound("MISSING PERMIT REF NUMBER")
        )

//        qaDaoServices.sendEmailWithSSCAttached(commonDaoServices.findUserByID(
//            permitDetails1.userId ?: throw ExpectedDataNotFound("MISSING USER ID")
//        ).email ?: throw ExpectedDataNotFound("MISSING USER ID"),docFile.bytes.toString(), permitDetails1.permitRefNumber ?: throw ExpectedDataNotFound("MISSING PERMIT REF NUMBER"))

        return uploadResults1
    }

    fun uloadsForInspectionReport(
        uploads: QaUploadsEntity,
        inspectionReportStatus: Int?,
        uploadResults: Pair<ServiceRequestsEntity, QaUploadsEntity>?,
        docFile: MultipartFile,
        docFileName: String,
        loggedInUser: UsersEntity,
        map: ServiceMapsEntity,
        permitDetails: PermitApplicationsEntity,
        versionNumber: Long,
        manufactureNonStatus: Int
    ): Pair<PermitApplicationsEntity, Pair<ServiceRequestsEntity, QaUploadsEntity>?> {
        var uploadResults1 = uploadResults
        var permitDetails1 = permitDetails
        uploads.inspectionReportStatus = inspectionReportStatus
        //                        versionNumber = qaDaoServices.findAllUploadedFileBYPermitIDAndSscStatus(permitID, map.activeStatus).size.toLong().plus(versionNumber)
        uploadResults1 = qaDaoServices.saveQaFileUploads(
            docFile,
            docFileName,
            loggedInUser,
            map,
            uploads,
            permitDetails1.permitRefNumber ?: throw Exception("INVALID PERMIT REF NUMBER"),
            versionNumber,
            manufactureNonStatus
        )
        //                        permitDetails.generateSchemeStatus = map.activeStatus
        //                        permitDetails.sscId = uploadResults.second.id
        permitDetails1 = qaDaoServices.permitUpdateDetails(permitDetails1, map, loggedInUser).second
        return Pair(permitDetails1, uploadResults1)
    }

    fun sendInspectionReport(permitDetails: PermitApplicationsEntity) {
        //todo: for now lets work with this i will change it
        var userPermit: UsersEntity? = null
        if (permitDetails.permitType == applicationMapProperties.mapQAPermitTypeIDDmark) {
            userPermit = permitDetails.hodId?.let { commonDaoServices.findUserByID(it) }
        } else if (permitDetails.permitType == applicationMapProperties.mapQAPermitTypeIdSmark) {
            userPermit = permitDetails.qamId?.let { commonDaoServices.findUserByID(it) }
        }

        val subject = "FACTORY INSPECTION REPORT"
        val messageBody = "Dear ${userPermit?.let { commonDaoServices.concatenateName(it) }}: \n" +
                "\n " +
                "Factory Inspection Report has been sent for approval :" +
                "\n " +
                "${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${permitDetails.id}"

        userPermit?.email?.let { notifications.sendEmail(it, subject, messageBody) }
    }

    @GetMapping("/kebs/view/attached")
    fun downloadFileDocument(
        response: HttpServletResponse,
        @RequestParam("fileID") fileID: Long
    ) {
        val fileUploaded = qaDaoServices.findUploadedFileBYId(fileID)
        val mappedFileClass = commonDaoServices.mapClass(fileUploaded)
        commonDaoServices.downloadFile(response, mappedFileClass)
    }


    @GetMapping("/kebs/view/attached-lab-pdf")
    fun downloadFileLabResultsDocument(
        response: HttpServletResponse,
        @RequestParam("fileName") fileName: String,
        @RequestParam("bsNumber") bsNumber: String
    ) {
        val file = limsServices.mainFunctionLimsGetPDF(bsNumber, fileName)
        //            val targetFile = File(Files.createTempDir(), fileName)
//            targetFile.deleteOnExit()
        response.contentType = commonDaoServices.getFileTypeByMimetypesFileTypeMap(file.name)
//                    response.setHeader("Content-Length", pdfReportStream.size().toString())
        response.addHeader("Content-Disposition", "inline; filename=${file.name}")
        response.outputStream
            .let { responseOutputStream ->
                responseOutputStream.write(file.readBytes())
                responseOutputStream.close()
            }

        KotlinLogging.logger { }.info("VIEW FILE SUCCESSFUL")

    }

    @GetMapping("/kebs/selected/attached-lab-pdf")
    fun selectedFileLabResultsDocument(
        response: HttpServletResponse,
        @RequestParam("fileName") fileName: String,
        @RequestParam("bsNumber") bsNumber: String,
        @RequestParam("ssfID") ssfID: Long
    ): String? {
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val map = commonDaoServices.serviceMapDetails(appId)
        var result: ServiceRequestsEntity?
        var myResults: Pair<ServiceRequestsEntity, QaSampleSubmissionEntity>? = null
        val fileContent = limsServices.mainFunctionLimsGetPDF(bsNumber, fileName)
        if (fileContent != null) {
            myResults = qaDaoServices.ssfSavePDFSelectedDetails(fileContent, ssfID, map, loggedInUser)
            result = myResults.first
            KotlinLogging.logger { }.info("SAVE FILE SUCCESSFUL")
        } else {
            throw ExpectedDataNotFound("NO PDF FOUND WITH THE FOLLOWING NAME $fileName for BS Number $bsNumber")
        }

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.closeLink = "${applicationMapProperties.baseUrlValue}/qa/inspection/ssf-details?ssfID=${myResults.second.id}"
        sm.message = "You have successful Saved The Lab Results That Will be sent To manufacture"

        return commonDaoServices.returnValues(result, map, sm)

    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION') or hasAuthority('QA_HOD_READ') or hasAuthority('QA_MANAGER_ASSESSORS_READ') or hasAuthority('QA_HOF_READ') or hasAuthority('QA_OFFICER_MODIFY') or hasAuthority('QA_ASSESSORS_READ')")
    @GetMapping("/factory-assessment")
    fun getFactoryAssesmentReport(response: HttpServletResponse, @RequestParam("permitID") permitID: Long) {
        val permit = qaDaoServices.findPermitBYID(permitID)

        val fileUploaded = qaDaoServices.findUploadedFileByPermitRefNumberAndDocType(
            permit.permitRefNumber ?: throw Exception("INVALID PERMIT REF NUMBER"), "FACTORY_ASSESSMENT_REPORT"
        )
        val mappedFileClass = fileUploaded.let { commonDaoServices.mapClass(it) }
        commonDaoServices.downloadFile(response, mappedFileClass)
    }


    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @GetMapping("/new-permit-submit")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun submitPermit(
        @RequestParam("permitID") permitID: Long,
        model: Model
    ): String? {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        var result: ServiceRequestsEntity?

        val permit = loggedInUser.id?.let { qaDaoServices.findPermitBYUserIDAndId(permitID, it) } ?: throw ExpectedDataNotFound("Required User ID, check config")
        val permitType = permit.permitType?.let { qaDaoServices.findPermitType(it) } ?: throw ExpectedDataNotFound("PermitType Id Not found")

        result = qaDaoServices.permitInvoiceCalculation(map, loggedInUser, permit, permitType).first
        with(permit) {
            sendApplication = map.activeStatus
            invoiceGenerated = map.activeStatus
            permitStatus = applicationMapProperties.mapQaStatusPPayment

        }
        result = qaDaoServices.permitUpdateDetails(permit, map, loggedInUser).first

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.closeLink = "${applicationMapProperties.baseUrlValue}/qa/permits-list?permitTypeID=${permit.permitType}"
        sm.message =
            "You have successful Submitted Your Application, an invoice has been generated, check Your permit detail and pay for the Invoice"

        return commonDaoServices.returnValues(result, map, sm)
    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @PostMapping("/kebs/mpesa-stk-push")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun payPermitWithMpesa(
        @RequestParam("batchInvoiceID") batchInvoiceID: Long,
        @RequestParam("phoneNumber") phoneNumber: String,
        model: Model
    ): String? {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        val result: ServiceRequestsEntity?

        val invoiceEntity = qaDaoServices.findBatchInvoicesWithID(batchInvoiceID)
        result = qaDaoServices.permitInvoiceSTKPush(map, loggedInUser, phoneNumber, invoiceEntity)

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.closeLink = "${applicationMapProperties.baseUrlValue}/qa/invoice/batch-details?batchID=${invoiceEntity.id}"
        sm.message =
            "Check You phone for an STK Push,If You can't see the push either pay with Bank or Normal MPesa service"

        return commonDaoServices.returnValues(result, map, sm)
    }

    @PreAuthorize("hasAuthority('QA_OFFICER_MODIFY')")
    @GetMapping("/justification-submit")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun submitJustification(@RequestParam("permitID") permitID: Long, model: Model): String? {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        var result: ServiceRequestsEntity?

        val permit = qaDaoServices.findPermitBYID(permitID)

        with(permit) {
            justificationReportStatus = map.initStatus
            permitStatus = applicationMapProperties.mapQaStatusPApprovalustCationReport
            userTaskId = applicationMapProperties.mapUserTaskNameHOD
        }
        val myResults = qaDaoServices.permitUpdateDetails(permit, map, loggedInUser)
        result = myResults.first

        qaDaoServices.sendNotificationForJustification(myResults.second)

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.closeLink = "${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${permit.id}"
        sm.message = "Justification report successfully submitted for Approval"

        return commonDaoServices.returnValues(result, map, sm)
    }

    @PreAuthorize("hasAuthority('QA_ASSESSORS_MODIFY')")
    @PostMapping("/factory-assessment-report")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun uploadFactoryAssessment(
        @RequestParam("permitID") permitID: Long,
        @RequestParam("docFileName") docFileName: String,
        @RequestParam("doc_file") docFile: MultipartFile,
        @RequestParam("assessment_recommendations") assessmentRecommendations: String,
        model: Model
    ): String? {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val permitDetails = qaDaoServices.findPermitBYID(permitID)

        var result: ServiceRequestsEntity?
        val versionNumber: Long = 1
        var uploads = QaUploadsEntity()
        uploads.ordinaryStatus = map.inactiveStatus
        result = qaDaoServices.saveQaFileUploads(
            docFile,
            docFileName,
            loggedInUser,
            map,
            uploads,
            permitDetails.permitRefNumber?: throw Exception("INVALID PERMIT REF NUMBER"),
            versionNumber,
            null
        ).first

        val hodDetails = qaDaoServices.assignNextOfficerAfterPayment(
            permitDetails,
            map,
            applicationMapProperties.mapQADesignationIDForHODId
        )


        with(permitDetails) {
            assessmentScheduledStatus = map.successStatus
            assessmentReportRemarks = assessmentRecommendations
            hodId = hodDetails?.id
            permitStatus = applicationMapProperties.mapQaStatusPApprovalAssesmentReport
        }
        qaDaoServices.permitUpdateDetails(permitDetails, map, loggedInUser)

        //Send notification to PAC secretary
        val hodSec = hodDetails?.id?.let { commonDaoServices.findUserByID(it) }
        hodSec?.email?.let { qaDaoServices.sendPacDmarkAssessmentNotificationEmail(it, permitDetails) }
//
//        val pacSecList =  qaDaoServices.findOfficersList(permitDetails, map, applicationMapProperties.mapQADesignationIDForPacSecId)
//        val appointedPacSec = pacSecList[0]
//
//        with(permitDetails) {
//            assessmentScheduledStatus = map.successStatus
//            assessmentReportRemarks = assessmentRecommendations
//            pacSecId = appointedPacSec.userId?.id
//        }
//        qaDaoServices.permitUpdateDetails(permitDetails, map, loggedInUser)
//
//        //Send notification to PAC secretary
//        val pacSec = appointedPacSec.userId?.id?.let { commonDaoServices.findUserByID(it) }
//        pacSec?.email?.let { qaDaoServices.sendPacDmarkAssessmentNotificationEmail(it, permitDetails) }

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.closeLink = "${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${permitDetails.id}"
        sm.message = "Factory Assessment report successfully uploaded"

        return commonDaoServices.returnValues(result, map, sm)
    }
}