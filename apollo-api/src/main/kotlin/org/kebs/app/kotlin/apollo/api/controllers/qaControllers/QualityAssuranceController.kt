package org.kebs.app.kotlin.apollo.api.controllers.qaControllers


import org.kebs.app.kotlin.apollo.api.notifications.Notifications
import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.QADaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.QualityAssuranceDaoServices
import org.kebs.app.kotlin.apollo.common.dto.FmarkEntityDto
import org.kebs.app.kotlin.apollo.common.dto.qa.NewBatchInvoiceDto
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.kebs.app.kotlin.apollo.common.exceptions.ServiceMapNotFoundException
import org.kebs.app.kotlin.apollo.common.utils.generateRandomText
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.model.di.ConsignmentDocumentDetailsEntity
import org.kebs.app.kotlin.apollo.store.model.qa.*
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.function.paramOrNull
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import javax.servlet.http.HttpServletResponse


@Controller
@RequestMapping("/api/qa")
class QualityAssuranceController(
    private val applicationMapProperties: ApplicationMapProperties,
    private val qaDaoServices: QADaoServices,
    private val notifications: Notifications,
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
        sm.message = "You have Successful Filled STA 1 , Complete your application"

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
        val permitDetails = qaDaoServices.findPermitBYID(
            fmarkEntityDto.smarkPermitID ?: throw ExpectedDataNotFound("Smark Permit id not found")
        )

        result = qaDaoServices.permitGenerateFmark(map, loggedInUser, permitDetails)

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

        //Add Permit ID THAT was Fetched so That it wont create a new record while updating with the methode
        permit.id = permitDetails.id

        //Add the extra permit details from plant attached
        if (permit.attachedPlantId != null) {
            val plantDetails = qaDaoServices.findPlantDetails(permit.attachedPlantId!!)
            val manufacturedDetails = commonDaoServices.findCompanyProfile(
                plantDetails.userId ?: throw ExpectedDataNotFound("MISSING USER ID")
            )
            with(permit) {
                firmName = manufacturedDetails.name
                postalAddress = plantDetails.postalAddress
                telephoneNo = plantDetails.telephone
                email = plantDetails.emailAddress
                physicalAddress = plantDetails.physicalAddress
                faxNo = plantDetails.faxNo
                plotNo = plantDetails.plotNo
                designation = plantDetails.designation
            }
        } else if (permit.sectionId != null) {
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
            //Permit attached plants Details
            permit.attachedPlantId != null -> {
                when (permitDetails.permitType) {
                    applicationMapProperties.mapQAPermitTypeIDDmark -> {
                        qaDaoServices.permitInsertStatus(
                            permitDetails,
                            applicationMapProperties.mapQaStatusPSTA3,
                            loggedInUser
                        )
                    }
                    applicationMapProperties.mapQAPermitTypeIdSmark -> {
                        qaDaoServices.permitInsertStatus(
                            permitDetails,
                            applicationMapProperties.mapQaStatusPSTA10,
                            loggedInUser
                        )
                    }
                }
            }
            //Permit completeness status
            permit.hofQamCompletenessStatus != null -> {
                when (permit.hofQamCompletenessStatus) {
                    map.activeStatus -> {
                        qaDaoServices.permitInsertStatus(
                            permitDetails,
                            applicationMapProperties.mapQaStatusPQAOAssign,
                            loggedInUser
                        )
                    }
                    map.inactiveStatus -> {
                        permitDetails.userTaskId = applicationMapProperties.mapUserTaskNameMANUFACTURE
                        qaDaoServices.permitInsertStatus(
                            permitDetails,
                            applicationMapProperties.mapQaStatusIncompleteAppl,
                            loggedInUser
                        )
                    }
                }
            }
            //Permit assigned officer
            permit.assignOfficerStatus != null -> {
                permitDetails.userTaskId = applicationMapProperties.mapUserTaskNameQAO
                when (permitDetails.permitType) {
                    applicationMapProperties.mapQAPermitTypeIdSmark -> {
                        qaDaoServices.permitInsertStatus(
                            permitDetails,
                            applicationMapProperties.mapQaStatusPFactoryVisitSchedule,
                            loggedInUser
                        )
                    }
                    applicationMapProperties.mapQAPermitTypeIdFmark -> {
                        qaDaoServices.permitInsertStatus(
                            permitDetails,
                            applicationMapProperties.mapQaStatusPFactoryVisitSchedule,
                            loggedInUser
                        )
                    }
                    applicationMapProperties.mapQAPermitTypeIDDmark -> {
                        qaDaoServices.permitInsertStatus(
                            permitDetails,
                            applicationMapProperties.mapQaStatusPfactoryInsForms,
                            loggedInUser
                        )
                    }
                }

            }
            //Permit inspection scheduled status
            permit.inspectionScheduledStatus != null -> {
                if (permitDetails.permitType == applicationMapProperties.mapQAPermitTypeIdSmark) {
                    qaDaoServices.permitInsertStatus(
                        permitDetails,
                        applicationMapProperties.mapQaStatusPfactoryInsForms,
                        loggedInUser
                    )
                }
            }
            //Permit Resubmit application
            permit.resubmitApplicationStatus == map.activeStatus -> {
                with(permit) {
                    resubmitApplicationStatus = null
                }
                qaDaoServices.permitInsertStatus(
                    permitDetails,
                    applicationMapProperties.mapQaStatusResubmitted,
                    loggedInUser
                )
            }
            //Permit pending factory inspection Approval
            permit.factoryInspectionReportApprovedRejectedStatus == map.activeStatus -> {
                qaDaoServices.permitInsertStatus(
                    permitDetails,
                    applicationMapProperties.mapQaStatusPBSNumber,
                    loggedInUser
                )
            }
            //Permit assigned  assessor officer
            permit.assignAssessorStatus == map.activeStatus -> {
                //Send notification to assessor
                val assessor = permitDetails.assessorId?.let { commonDaoServices.findUserByID(it) }
                assessor?.email?.let { qaDaoServices.sendAppointAssessorNotificationEmail(it, permitDetails) }
                permitDetails.userTaskId = applicationMapProperties.mapUserTaskNameASSESSORS
                qaDaoServices.permitInsertStatus(
                    permitDetails,
                    applicationMapProperties.mapQaStatusPFactoryVisitSchedule,
                    loggedInUser
                )
            }
            //Permit assesment scheduled
            permit.assessmentScheduledStatus == map.activeStatus -> {
                //Send manufacturers notification
                val manufacturer = permitDetails.userId?.let { commonDaoServices.findUserByID(it) }
                manufacturer?.email?.let {
                    qaDaoServices.sendScheduledFactoryAssessmentNotificationEmail(
                        it,
                        permitDetails
                    )
                }
                qaDaoServices.permitInsertStatus(
                    permitDetails,
                    applicationMapProperties.mapQaStatusPGenerationAssesmentReport,
                    loggedInUser
                )
            }
            //Permit awarded scheduled
            permit.permitAwardStatus == map.activeStatus -> {
                val issueDate = commonDaoServices.getCurrentDate()
                val permitType = permitDetails.permitType?.let { qaDaoServices.findPermitType(it) }
                val expiryDate = permitType?.numberOfYears?.let { commonDaoServices.addYearsToCurrentDate(it) }


                with(permitDetails) {
                    if (renewalStatus != map.activeStatus) {
                        awardedPermitNumber = "${permitType?.markNumber}${
                            generateRandomText(
                                6,
                                map.secureRandom,
                                map.messageDigestAlgorithm,
                                false
                            )
                        }".toUpperCase()
                    }
                    permitAwardStatus = map.activeStatus
                    dateOfIssue = issueDate
                    dateOfExpiry = expiryDate
                }
                //Generate permit and forward to manufacturer
                KotlinLogging.logger { }.info(":::::: Sending compliance status along with e-permit :::::::")
                qaDaoServices.permitInsertStatus(
                    permitDetails,
                    applicationMapProperties.mapQaStatusPermitAwarded,
                    loggedInUser
                )
            }
            permit.permitAwardStatus == map.inactiveStatus -> {
                //Send defer notification
                KotlinLogging.logger { }.info(":::::: Sending defer notification to assessor/qao :::::::")
//                qaDaoServices.permitInsertStatus(permitDetails,applicationMapProperties.mapQaStatusP,loggedInUser)
            }
            permit.hodApproveAssessmentStatus != null -> {
                //Send manufacturers notification
                //

                var complianceValue: String? = null
                when (permit.hodApproveAssessmentStatus) {
                    map.activeStatus -> {
                        val pacSecList = permitDetails.attachedPlantId?.let {
                            qaDaoServices.findOfficersList(
                                it,
                                permitDetails,
                                map,
                                applicationMapProperties.mapQADesignationIDForPacSecId
                            )
                        }
                        val appointedPacSec = pacSecList?.get(0)

                        with(permitDetails) {
                            hodApproveAssessmentStatus = map.activeStatus
                            hodApproveAssessmentRemarks = permit.hodApproveAssessmentRemarks
                            pacSecId = appointedPacSec?.userId?.id
                            userTaskId = applicationMapProperties.mapUserTaskNamePACSECRETARY
                        }
                        qaDaoServices.permitUpdateDetails(permitDetails, map, loggedInUser)

                        //Send notification to PAC secretary
                        val pacSec = appointedPacSec?.userId?.id?.let { commonDaoServices.findUserByID(it) }
                        pacSec?.email?.let { qaDaoServices.sendPacDmarkAssessmentNotificationEmail(it, permitDetails) }

                        qaDaoServices.permitInsertStatus(
                            permitDetails,
                            applicationMapProperties.mapQaStatusPPACSecretaryAwarding,
                            loggedInUser
                        )

                    }
                    map.inactiveStatus -> {
//                        var reasonValue: String? = null
//                        reasonValue = "REJECTED"
//                        with(permitDetails) {
//                            hodApproveAssessmentStatus = null
//                        }
//                        qaDaoServices.permitInsertStatus(
//                            permitDetails,
//                            applicationMapProperties.mapQaStatusRejectedJustCationReport,
//                            loggedInUser
//                        )
//                        qaDaoServices.justificationReportSendEmail(permitDetails, reasonValue)
                        //                    complianceValue= "NON-COMPLIANT"
                        //                    qaDaoServices.permitInsertStatus(permitDetails,applicationMapProperties.mapQaStatusRe,loggedInUser)
                    }
                }
//                qaDaoServices.sendComplianceStatusAndLabReport(permitDetails, complianceValue ?: throw ExpectedDataNotFound(" "))
            }
            permit.recommendationRemarks != null -> {
                //Send manufacturers notification
//                permitDetails.userTaskId = applicationMapProperties.mapUserTaskNameHOF
                qaDaoServices.permitInsertStatus(
                    permitDetails,
                    applicationMapProperties.mapQaStatusPRecommendationApproval,
                    loggedInUser
                )
                qaDaoServices.sendNotificationForRecommendation(permitDetails)
            }
            permit.recommendationApprovalStatus != null -> {
                //Send notification
                if (permit.recommendationApprovalStatus == map.activeStatus) {
                    with(permit) {
                        pscMemberId = qaDaoServices.assignNextOfficerAfterPayment(
                            permitDetails,
                            map,
                            applicationMapProperties.mapQADesignationIDForPSCId
                        )?.id
                    }
                    //updating of Details in DB
                    permitDetails = qaDaoServices.permitUpdateDetails(
                        commonDaoServices.updateDetails(
                            permit,
                            permitDetails
                        ) as PermitApplicationsEntity, map, loggedInUser
                    ).second
                    qaDaoServices.sendNotificationPSCForAwardingPermit(permitDetails)

                    qaDaoServices.permitInsertStatus(
                        permitDetails,
                        applicationMapProperties.mapQaStatusPPSCMembersAward,
                        loggedInUser
                    )

                } else if (permit.recommendationApprovalStatus == map.inactiveStatus) {
                    permitDetails = qaDaoServices.permitInsertStatus(
                        permitDetails,
                        applicationMapProperties.mapQaStatusPRecommendation,
                        loggedInUser
                    )
                    qaDaoServices.sendNotificationForRecommendationCorrectness(permitDetails)
                }

            }
            permit.pscMemberApprovalStatus != null -> {
                //Send notification
                if (permit.pscMemberApprovalStatus == map.activeStatus) {
                    with(permit) {
                        pcmId = qaDaoServices.assignNextOfficerWithDesignation(
                            permit,
                            map,
                            applicationMapProperties.mapQADesignationIDForPCMId
                        )?.id
                    }
                    //updating of Details in DB
                    permitDetails = qaDaoServices.permitUpdateDetails(
                        commonDaoServices.updateDetails(
                            permit,
                            permitDetails
                        ) as PermitApplicationsEntity, map, loggedInUser
                    ).second
                    qaDaoServices.sendNotificationPCMForAwardingPermit(permitDetails)
                    qaDaoServices.permitInsertStatus(
                        permitDetails,
                        applicationMapProperties.mapQaStatusPPCMAwarding,
                        loggedInUser
                    )


                } else if (permit.pscMemberApprovalStatus == map.inactiveStatus) {
                    qaDaoServices.sendNotificationForDeferredPermitToQaoFromPSC(permitDetails)
                    qaDaoServices.permitInsertStatus(
                        permitDetails,
                        applicationMapProperties.mapQaStatusDeferredPSCMembers,
                        loggedInUser
                    )
                }

            }
            //Approved or rejected SSC
            permit.approvedRejectedScheme != null -> {
                //Send notification
                var reasonValue: String? = null
                when (permit.approvedRejectedScheme) {
                    map.activeStatus -> {
                        reasonValue = "ACCEPTED"
                        qaDaoServices.schemeSendEmail(permitDetails, reasonValue)
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
                        qaDaoServices.schemeSendEmail(permitDetails, reasonValue)
                    }
                }

            }
            permit.justificationReportStatus != null -> {
                //Send notification
                var reasonValue: String? = null
                when (permit.justificationReportStatus) {
                    map.activeStatus -> {
                        reasonValue = "ACCEPTED"
                        qaDaoServices.justificationReportSendEmail(permitDetails, reasonValue)
                        qaDaoServices.permitInsertStatus(
                            permitDetails,
                            applicationMapProperties.mapQaStatusPAssesorAssigning,
                            loggedInUser
                        )
                    }
                    map.inactiveStatus -> {
                        reasonValue = "REJECTED"
                        with(permitDetails) {
                            justificationReportStatus = null
                        }
                        qaDaoServices.permitInsertStatus(
                            permitDetails,
                            applicationMapProperties.mapQaStatusRejectedJustCationReport,
                            loggedInUser
                        )
                        qaDaoServices.justificationReportSendEmail(permitDetails, reasonValue)
                    }
                }

            }
            permit.pcmApprovalStatus != null -> {
                //Send notification
                if (permitDetails.permitType == applicationMapProperties.mapQAPermitTypeIDDmark) {
                    when (permit.pcmApprovalStatus) {
                        map.activeStatus -> {
                            KotlinLogging.logger { }
                                .info(":::::: Sending compliance status along with e-permit :::::::")
                            permitDetails = qaDaoServices.permitUpdateDetails(
                                commonDaoServices.updateDetails(
                                    permit,
                                    permitDetails
                                ) as PermitApplicationsEntity, map, loggedInUser
                            ).second
                            qaDaoServices.pcmGenerateInvoice(
                                map,
                                loggedInUser,
                                permitDetails,
                                permitDetails.permitType ?: throw Exception("ID NOT FOUND")
                            )
                        }
                        map.inactiveStatus -> {
                            with(permitDetails) {
                                resubmitApplicationStatus = map.initStatus
                                sendForPcmReview = null
                                pcmApprovalStatus = null
                            }
                            qaDaoServices.permitInsertStatus(
                                permitDetails,
                                applicationMapProperties.mapQaStatusPendingCorrectionManf,
                                loggedInUser
                            )
                            qaDaoServices.sendNotificationForPermitReviewRejectedFromPCM(permitDetails)
                        }
                    }
                } else {
                    when (permit.pcmApprovalStatus) {
                        map.activeStatus -> {
                            val issueDate = commonDaoServices.getCurrentDate()
                            val permitType = permitDetails.permitType?.let { qaDaoServices.findPermitType(it) }
                            val expiryDate =
                                permitType?.permitAwardYears?.let { commonDaoServices.addYearsToCurrentDate(it.toLong()) }


                            with(permit) {
                                if (renewalStatus != map.activeStatus) {
                                    awardedPermitNumber = "${permitType?.markNumber}${
                                        generateRandomText(
                                            6,
                                            map.secureRandom,
                                            map.messageDigestAlgorithm,
                                            false
                                        )
                                    }".toUpperCase()
                                } else {

                                }
                                permitAwardStatus = map.activeStatus
                                dateOfIssue = issueDate
                                dateOfExpiry = expiryDate
                            }
                            //Generate permit and forward to manufacturer
                            KotlinLogging.logger { }
                                .info(":::::: Sending compliance status along with e-permit :::::::")
                            //updating of Details in DB
                            permitDetails = qaDaoServices.permitUpdateDetails(
                                commonDaoServices.updateDetails(
                                    permit,
                                    permitDetails
                                ) as PermitApplicationsEntity, map, loggedInUser
                            ).second
                            qaDaoServices.permitInsertStatus(
                                permitDetails,
                                applicationMapProperties.mapQaStatusPermitAwarded,
                                loggedInUser
                            )
                            //                    qaDaoServices.sendNotificationPSCForAwardingPermit(permitDetails)

                        }
                        map.inactiveStatus -> {
                            qaDaoServices.sendNotificationForDeferredPermitToQaoFromPCM(permitDetails)
                            qaDaoServices.permitInsertStatus(
                                permitDetails,
                                applicationMapProperties.mapQaStatusDeferredPCM,
                                loggedInUser
                            )
                        }
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
        sm.closeLink = "${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${permitDetails.id}"
        sm.message = "${permit.description}"

        return commonDaoServices.returnValues(result, map, sm)
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
        sm.closeLink = "${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${permitDetails.id}"
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

        var result: ServiceRequestsEntity?

        var myRenewedPermit = qaDaoServices.permitUpdateNewWithSamePermitNumber(permitID, map, loggedInUser)
        val permit = myRenewedPermit.second
        result = myRenewedPermit.first

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.closeLink = "${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${result.varField1}"
        sm.message = "You have Successful Renewed your Permit , Invoice has Been Generated"

        return commonDaoServices.returnValues(result, map, sm)
    }

    @PostMapping("/kebs/invoice/create-batch-invoice/save")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun permitInvoiceBatchAddDetails(
        @ModelAttribute("NewBatchInvoiceDto") NewBatchInvoiceDto: NewBatchInvoiceDto,
        model: Model,
    ): String? {

        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        val result: ServiceRequestsEntity?

        result = qaDaoServices.permitMultipleInvoiceCalculation(map, loggedInUser, NewBatchInvoiceDto)

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
        val permitFound = qaDaoServices.findPermitBYID(permitID)
        var qaInspectionReportRecommendation = qaDaoServices.findQaInspectionReportRecommendationBYPermitID(
            permitFound.id ?: throw Exception("INVALID PERMIT ID FOUND")
        )

        qaInspectionReportRecommendation = commonDaoServices.updateDetails(
            QaInspectionReportRecommendationEntity,
            qaInspectionReportRecommendation
        ) as QaInspectionReportRecommendationEntity

        result = qaDaoServices.inspectionRecommendationUpdate(qaInspectionReportRecommendation, map, loggedInUser)

        if (QaInspectionReportRecommendationEntity.submittedInspectionReportStatus == 1) {
//            permitFound.inspectionReportGenerated=map.activeStatus
            qaDaoServices.permitInsertStatus(
                permitFound,
                applicationMapProperties.mapQaStatusPInspectionReportApproval,
                loggedInUser
            )
        } else if (QaInspectionReportRecommendationEntity.supervisorFilledStatus == 1) {
            permitFound.factoryInspectionReportApprovedRejectedStatus = map.activeStatus
            qaDaoServices.permitInsertStatus(permitFound, applicationMapProperties.mapQaStatusPGenSSC, loggedInUser)
        }

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.closeLink =
            "${applicationMapProperties.baseUrlValue}/qa/inspection/inspection-report-details?permitID=${result.varField1}"
        sm.message = "Inspection Report has Been Generated(DRAFT)"

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
//        @RequestParam("permitID") permitID: Long,
//        @RequestParam("batchID") batchID: Long,
        model: Model,
    ): String? {

        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        val result: ServiceRequestsEntity?

//        val batchDetailsRemover =NewBatchInvoiceDto()
//        batchDetailsRemover.batchID= batchID
//        batchDetailsRemover.permitID= permitID

        result = qaDaoServices.permitMultipleInvoiceRemoveInvoice(map, loggedInUser, batchDetailsRemover)

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

        result = qaDaoServices.permitMultipleInvoiceSubmitInvoice(map, loggedInUser, NewBatchInvoiceDto)

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
        sm.closeLink = "${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${result.varField1}"
        sm.message = "You have Successful resubmitted your Permit for approval"

        return commonDaoServices.returnValues(result, map, sm)
    }

    @PreAuthorize("hasAuthority('QA_OFFICER_MODIFY')")
    @PostMapping("/kebs/lab-results-compliance-status/save")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun complianceStatusSSF(
        @RequestParam("permitID") permitID: Long,
        @ModelAttribute("SampleSubmissionDetails") sampleSubmissionDetails: QaSampleSubmissionEntity,
        model: Model
    ): String? {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val permit = qaDaoServices.findPermitBYID(permitID)

        val result: ServiceRequestsEntity?

        result = qaDaoServices.ssfUpdateDetails(permit, sampleSubmissionDetails, loggedInUser, map).first

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.closeLink = "${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${permit.id}"
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
        }

        result = qaDaoServices.permitUpdateDetails(permit, map, loggedInUser).first

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.closeLink = "${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${result.varField1}"
        sm.message = "You have Successful submitted your Permit for review"

        return commonDaoServices.returnValues(result, map, sm)
    }


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
        val permit = loggedInUser.id?.let { qaDaoServices.findPermitBYUserIDAndId(permitID, it) }
            ?: throw ExpectedDataNotFound("User Id required")


        when (permitViewType) {
            applicationMapProperties.mapPermitRenewMessage -> {
                val sta3 = qaDaoServices.findSTA3WithPermitIDBY(permitID)
                QaSta3Entity.id = sta3.id
                qaDaoServices.sta3Update(
                    commonDaoServices.updateDetails(sta3, QaSta3Entity) as QaSta3Entity,
                    map,
                    loggedInUser
                )
            }
            applicationMapProperties.mapPermitNewMessage -> {
                permit.id?.let { qaDaoServices.sta3NewSave(it, QaSta3Entity, loggedInUser, map) }
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
            commonDaoServices.updateDetails(permit, updatePermit) as PermitApplicationsEntity, map, loggedInUser
        ).first

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.closeLink =
            "${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${permitID}"
        sm.message = "You have Successful Filled STA 3 and has been submitted sucessful , Submit your application"

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
                val sta10 = qaDaoServices.findSTA10WithPermitIDBY(permitID)
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
                permit.id?.let { qaDaoServices.sta10NewSave(it, QaSta10Entity, loggedInUser, map) }
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
            bsNumber = sampleSubmissionDetails.bsNumber
        }
        qaDaoServices.permitInsertStatus(permit, applicationMapProperties.mapQaStatusPLABResults, loggedInUser)

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.closeLink = "${applicationMapProperties.baseUrlValue}/qa/inspection/ssf-details?permitID=${permitID}"
        sm.message = "You have Successful Filled Sample Submission Details"

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
                qaDaoServices.sta10ManufactureProductNewSave(
                    it,
                    QaProductManufacturedEntity,
                    loggedInUser,
                    map
                )
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
        @ModelAttribute("QaRawMaterialEntity") QaRawMaterialEntity: QaRawMaterialEntity,
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
            qaSta10.id?.let { qaDaoServices.sta10MachinePlantNewSave(it, QaMachineryEntity, loggedInUser, map) }
        }
        return "${qaDaoServices.sta10Details}=${qaSta10.permitId}"
    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @PostMapping("/add/new-sta10-manufacturing-process")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun saveNewSta10MachinePlant(
        @RequestParam("closeStatus") closeStatus: Int?,
        @RequestParam("qaSta10ID") qaSta10ID: Long,
        @ModelAttribute("QaManufacturingProcessEntity") QaManufacturingProcessEntity: QaManufacturingProcessEntity,
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
                    permitID,
                    versionNumber,
                    manufactureNonStatus
                )
            }
            map.inactiveStatus -> {
                uploads.ordinaryStatus = ordinaryStatus
                when {
                    cocStatus != null -> {
                        uploads.cocStatus = cocStatus
                        versionNumber = qaDaoServices.findAllUploadedFileBYPermitIDAndCocStatus(
                            permitID,
                            map.activeStatus
                        ).size.toLong().plus(versionNumber)
                        uploadResults = qaDaoServices.saveQaFileUploads(
                            docFile,
                            docFileName,
                            loggedInUser,
                            map,
                            uploads,
                            permitID,
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
                        versionNumber = qaDaoServices.findAllUploadedFileBYPermitIDAndSscStatus(
                            permitID,
                            map.activeStatus
                        ).size.toLong().plus(versionNumber)
                        uploadResults = qaDaoServices.saveQaFileUploads(
                            docFile,
                            docFileName,
                            loggedInUser,
                            map,
                            uploads,
                            permitID,
                            versionNumber,
                            manufactureNonStatus
                        )
                        permitDetails.generateSchemeStatus = map.activeStatus
                        permitDetails.sscId = uploadResults.second.id
                        permitDetails = qaDaoServices.permitUpdateDetails(permitDetails, map, loggedInUser).second
                        qaDaoServices.permitInsertStatus(
                            permitDetails,
                            applicationMapProperties.mapQaStatusPApprSSC,
                            loggedInUser
                        )

                    }
                    assessmentReportStatus != null -> {
                        uploads.assessmentReportStatus = assessmentReportStatus
                        versionNumber = qaDaoServices.findAllUploadedFileBYPermitIDAndAssessmentReportStatus(
                            permitID,
                            map.activeStatus
                        ).size.toLong().plus(versionNumber)
                        uploadResults = qaDaoServices.saveQaFileUploads(
                            docFile,
                            docFileName,
                            loggedInUser,
                            map,
                            uploads,
                            permitID,
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
                        }
                        qaDaoServices.permitUpdateDetails(permitDetails, map, loggedInUser)

                        //Send notification to PAC secretary
                        val hodSec = hodDetails?.id?.let { commonDaoServices.findUserByID(it) }
                        hodSec?.email?.let { qaDaoServices.sendPacDmarkAssessmentNotificationEmail(it, permitDetails) }

//                        permitDetails.generateSchemeStatus = map.activeStatus
//                        permitDetails.sscId = uploadResults.second.id
//                        permitDetails = qaDaoServices.permitUpdateDetails(permitDetails, map, loggedInUser).second
//                        qaDaoServices.permitInsertStatus(
//                            permitDetails,
//                            applicationMapProperties.mapQaStatusPApprSSC,
//                            loggedInUser
//                        )

                    }
                    inspectionReportStatus != null -> {
                        uploads.inspectionReportStatus = inspectionReportStatus
//                        versionNumber = qaDaoServices.findAllUploadedFileBYPermitIDAndSscStatus(permitID, map.activeStatus).size.toLong().plus(versionNumber)
                        uploadResults = qaDaoServices.saveQaFileUploads(
                            docFile,
                            docFileName,
                            loggedInUser,
                            map,
                            uploads,
                            permitID,
                            versionNumber,
                            manufactureNonStatus
                        )
//                        permitDetails.generateSchemeStatus = map.activeStatus
//                        permitDetails.sscId = uploadResults.second.id
                        permitDetails = qaDaoServices.permitUpdateDetails(permitDetails, map, loggedInUser).second
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
                            permitID,
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

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.closeLink = "${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${permitDetails.id}"
        sm.message = "Document Uploaded successful"

        return commonDaoServices.returnValues(result ?: throw Exception("invalid results"), map, sm)
    }

    private fun sendInspectionReport(permitDetails: PermitApplicationsEntity) {
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

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION') or hasAuthority('QA_HOD_READ') or hasAuthority('QA_MANAGER_ASSESSORS_READ') or hasAuthority('QA_HOF_READ') or hasAuthority('QA_OFFICER_MODIFY') or hasAuthority('QA_ASSESSORS_READ')")
    @GetMapping("/factory-assessment")
    fun getFactoryAssesmentReport(response: HttpServletResponse, @RequestParam("permitID") permitID: Long) {
        val permit = qaDaoServices.findPermitBYID(permitID)

        val fileUploaded =
            permit.id?.let { qaDaoServices.findUploadedFileByPermitIdAndDocType(it, "FACTORY_ASSESSMENT_REPORT") }
        val mappedFileClass = fileUploaded?.let { commonDaoServices.mapClass(it) }
        if (mappedFileClass != null) {
            commonDaoServices.downloadFile(response, mappedFileClass)
        }
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

        val permit = loggedInUser.id?.let { qaDaoServices.findPermitBYUserIDAndId(permitID, it) }
            ?: throw ExpectedDataNotFound("Required User ID, check config")
        val permitType = permit.permitType?.let { qaDaoServices.findPermitType(it) }
            ?: throw ExpectedDataNotFound("PermitType Id Not found")

        result = qaDaoServices.permitInvoiceCalculation(map, loggedInUser, permit, permitType)
        with(permit) {
            sendApplication = map.activeStatus
            invoiceGenerated = map.activeStatus
            permitStatus = applicationMapProperties.mapQaStatusPPayment

        }
        result = qaDaoServices.permitUpdateDetails(permit, map, loggedInUser).first

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.closeLink = "${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${permit.id}"
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
        }
        result = qaDaoServices.permitUpdateDetails(permit, map, loggedInUser).first

//        qaDaoServices.permitInsertStatus(
//            permitDetails,
//            applicationMapProperties.mapQaStatusPendingCorrectionManf,
//            loggedInUser
//        )

        //Send notification to HOD for permit approval

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.closeLink =
            "${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${permit.id}"
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
            permitID,
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