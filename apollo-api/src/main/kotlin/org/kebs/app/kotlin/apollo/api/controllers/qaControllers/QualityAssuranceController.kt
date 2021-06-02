package org.kebs.app.kotlin.apollo.api.controllers.qaControllers


import org.kebs.app.kotlin.apollo.api.notifications.Notifications
import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.QADaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.QualityAssuranceDaoServices
import org.kebs.app.kotlin.apollo.common.dto.FmarkEntityDto
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
        val permitDetails = qaDaoServices.findPermitBYID(fmarkEntityDto.smarkPermitID?: throw ExpectedDataNotFound("Smark Permit id not found"))

        result = qaDaoServices.permitGenerateFmark(map,loggedInUser,permitDetails)

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
        @RequestParam( "permitID") permitID: Long,
        @RequestParam( "status") status: Int?,
        @RequestParam( "schemeID") schemeID: Long?,
        model: Model)
    : String? {
        var result: ServiceRequestsEntity?
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        //Find Permit with permit ID
        val permitDetails = qaDaoServices.findPermitBYID(permitID)
        val sm = CommonDaoServices.MessageSuccessFailDTO()


        if (schemeID!=null && status!=null){
            QaSchemeForSupervisionEntity.status= status
            QaSchemeForSupervisionEntity.acceptedRejectedStatus= null
            result = qaDaoServices.schemeSupervisionUpdateSave(schemeID, QaSchemeForSupervisionEntity, loggedInUser,map)
            sm.message = "You have Successful UPDATED SSC"
        }else{
            result = qaDaoServices.newSchemeSupervisionSave(permitDetails, QaSchemeForSupervisionEntity, loggedInUser,map)
            //Permit SCC approval status
            qaDaoServices.permitInsertStatus(permitDetails,applicationMapProperties.mapQaStatusPApprSSC,loggedInUser)
            sm.message = "You have Successful Filled SSC"
        }


        //Set scheme as generated and update results
        permitDetails.generateSchemeStatus = map.activeStatus

        result = qaDaoServices.permitUpdateDetails(permitDetails,map, loggedInUser).first

        sm.closeLink = "${applicationMapProperties.baseUrlValue}/qa/scheme-of-supervision?permitID=${permitDetails.id}"


        return commonDaoServices.returnValues(result, map, sm)
    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @PostMapping("/apply/update-scheme-of-supervision")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun updateSchemeOfSupervision(
        @ModelAttribute("schemeFound") schemeFound: QaSchemeForSupervisionEntity,
        @RequestParam( "schemeID") schemeID: Long,
        model: Model)
    : String? {
        val result: ServiceRequestsEntity?
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        result = qaDaoServices.schemeSupervisionUpdateSave(schemeID, schemeFound, loggedInUser,map)
        val permitDetails = qaDaoServices.findPermitBYID(result.varField1?.toLong() ?: throw ExpectedDataNotFound("MISSING PERMIT ID"))
        when (schemeFound.acceptedRejectedStatus) {
            map.activeStatus -> {
                qaDaoServices.permitInsertStatus(permitDetails,applicationMapProperties.mapQaStatusPInspReport,loggedInUser)
            }
            map.inactiveStatus -> {
                qaDaoServices.permitInsertStatus(permitDetails,applicationMapProperties.mapQaStatusSSCRejected,loggedInUser)
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
        @RequestParam( "permitID") permitID: Long,
        model: Model)
    : String? {
        val result: ServiceRequestsEntity?
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        result = qaDaoServices.permitRequests(permitRequest, permitID, loggedInUser,map).first
        val permitDetails = qaDaoServices.findPermitBYID(result.varField1?.toLong() ?: throw ExpectedDataNotFound("MISSING PERMIT ID"))

        qaDaoServices.permitInsertStatus(permitDetails,applicationMapProperties.mapQaStatusPRequestApproval,loggedInUser)


        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.closeLink = "${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${result.varField1}"
        sm.message = "Your Request has been Successful Submitted"

        return commonDaoServices.returnValues(result, map, sm)
    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION') or hasAuthority('QA_MANAGER_ASSESSORS_MODIFY') or hasAuthority('QA_HOF_MODIFY') " +
            "or hasAuthority('QA_HOD_MODIFY') or hasAuthority('QA_OFFICER_MODIFY') or hasAuthority('QA_PAC_SECRETARY_MODIFY') or hasAuthority('QA_PSC_MEMBERS_MODIFY') or hasAuthority('QA_PCM_MODIFY') or hasAuthority('QA_ASSESSORS_MODIFY')")
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

        //Add Permit ID THAT was Fetched so That it wont create a new record while updating with the methode
        permit.id = permitDetails.id

        //Add the extra permit details from plant attached
        if( permit.attachedPlantId != null){
            val plantDetails = qaDaoServices.findPlantDetails(permit.attachedPlantId!!)
            val manufacturedDetails = commonDaoServices.findCompanyProfile(plantDetails.userId ?: throw ExpectedDataNotFound("MISSING USER ID"))
            with(permit){
                firmName = manufacturedDetails.name
                postalAddress = plantDetails.postalAddress
                telephoneNo = plantDetails.telephone
                email = plantDetails.emailAddress
                physicalAddress = plantDetails.physicalAddress
                faxNo = plantDetails.faxNo
                plotNo = plantDetails.plotNo
                designation = plantDetails.designation
            }
        }

        //updating of Details in DB
        val updateResults = qaDaoServices.permitUpdateDetails(commonDaoServices.updateDetails(permit, permitDetails) as PermitApplicationsEntity, map, loggedInUser)

        result = updateResults.first

        permitDetails = updateResults.second

        when {
            //Permit attached plants Details
            permit.attachedPlantId != null -> {
                when (permitDetails.permitType) {
                    applicationMapProperties.mapQAPermitTypeIDDmark -> {
                        qaDaoServices.permitInsertStatus(permitDetails,applicationMapProperties.mapQaStatusPSTA3,loggedInUser)
                    }
                    applicationMapProperties.mapQAPermitTypeIdSmark -> {
                        qaDaoServices.permitInsertStatus(permitDetails,applicationMapProperties.mapQaStatusPSTA10,loggedInUser)
                    }
                }
            }
            //Permit completeness status
            permit.hofQamCompletenessStatus != null -> {
                when (permit.hofQamCompletenessStatus) {
                    map.activeStatus -> {
                        qaDaoServices.permitInsertStatus(permitDetails,applicationMapProperties.mapQaStatusPQAOAssign,loggedInUser)
                    }
                    map.inactiveStatus -> {
                        qaDaoServices.permitInsertStatus(permitDetails,applicationMapProperties.mapQaStatusIncompleteAppl,loggedInUser)
                    }
                }
            }
            //Permit inspection scheduled status
            permit.assignOfficerStatus != null -> {
                when (permitDetails.permitType) {
                    applicationMapProperties.mapQAPermitTypeIdSmark -> {
                        qaDaoServices.permitInsertStatus(permitDetails,applicationMapProperties.mapQaStatusPFactoryVisitSchedule,loggedInUser)
                    }
                    applicationMapProperties.mapQAPermitTypeIdFmark -> {
                        qaDaoServices.permitInsertStatus(permitDetails,applicationMapProperties.mapQaStatusPFactoryVisitSchedule,loggedInUser)
                    }
                    applicationMapProperties.mapQAPermitTypeIDDmark -> {
                        qaDaoServices.permitInsertStatus(permitDetails,applicationMapProperties.mapQaStatusPfactoryInsForms,loggedInUser)
                    }
                }

            }
            //Permit inspection scheduled status
            permit.inspectionScheduledStatus != null -> {
                if (permitDetails.permitType == applicationMapProperties.mapQAPermitTypeIdSmark){
                    qaDaoServices.permitInsertStatus(permitDetails,applicationMapProperties.mapQaStatusPGenSSC,loggedInUser)
                }
            }
            //Permit pending factory inspection Approval
            permit.factoryInspectionReportApprovedRejectedStatus == map.activeStatus -> {
                qaDaoServices.permitInsertStatus(permitDetails,applicationMapProperties.mapQaStatusPBSNumber,loggedInUser)
            }
            permit.assignAssessorStatus == map.activeStatus -> {
                //Send notification to assessor
                val assessor = permitDetails.assessorId?.let { commonDaoServices.findUserByID(it) }
                assessor?.email?.let { qaDaoServices.sendAppointAssessorNotificationEmail(it, permitDetails) }
                qaDaoServices.permitInsertStatus(permitDetails,applicationMapProperties.mapQaStatusPFactoryVisitSchedule,loggedInUser)
            }
            permit.assessmentScheduledStatus == map.activeStatus -> {
                //Send manufacturers notification
                val manufacturer = permitDetails.userId?.let { commonDaoServices.findUserByID(it) }
                manufacturer?.email?.let { qaDaoServices.sendScheduledFactoryAssessmentNotificationEmail(it, permitDetails) }
                qaDaoServices.permitInsertStatus(permitDetails,applicationMapProperties.mapQaStatusPGenerationAssesmentReport,loggedInUser)
            }
            permit.permitAwardStatus == map.activeStatus -> {
                val issueDate = commonDaoServices.getCurrentDate()
                val permitType = permitDetails.permitType?.let { qaDaoServices.findPermitType(it) }
                val expiryDate = permitType?.permitAwardYears?.let { commonDaoServices.addYearsToCurrentDate(it.toLong()) }


                with(permitDetails) {
                    awardedPermitNumber = "${permitType?.markNumber}${generateRandomText(6, map.secureRandom, map.messageDigestAlgorithm, false)}".toUpperCase()
                    permitAwardStatus= map.activeStatus
                    dateOfIssue = issueDate
                    dateOfExpiry = expiryDate
                }
                //Generate permit and forward to manufacturer
                KotlinLogging.logger { }.info(":::::: Sending compliance status along with e-permit :::::::")
                qaDaoServices.permitInsertStatus(permitDetails,applicationMapProperties.mapQaStatusPermitAwarded,loggedInUser)
            }
            permit.permitAwardStatus == map.inactiveStatus -> {
                //Send defer notification
                KotlinLogging.logger { }.info(":::::: Sending defer notification to assessor/qao :::::::")
//                qaDaoServices.permitInsertStatus(permitDetails,applicationMapProperties.mapQaStatusP,loggedInUser)
            }
            permit.hodApproveAssessmentStatus != null -> {
                //Send manufacturers notification
                //

                var complianceValue: String?= null
                if (permit.hodApproveAssessmentStatus==map.activeStatus){
                    val pacSecList =  qaDaoServices.findOfficersList(permitDetails, map, applicationMapProperties.mapQADesignationIDForPacSecId)
                    val appointedPacSec = pacSecList[0]

                    with(permitDetails) {
                        hodApproveAssessmentStatus = map.activeStatus
                        hodApproveAssessmentRemarks = permit.hodApproveAssessmentRemarks
                        pacSecId = appointedPacSec.userId?.id
                    }
                    qaDaoServices.permitUpdateDetails(permitDetails, map, loggedInUser)

                    //Send notification to PAC secretary
                    val pacSec = appointedPacSec.userId?.id?.let { commonDaoServices.findUserByID(it) }
                    pacSec?.email?.let { qaDaoServices.sendPacDmarkAssessmentNotificationEmail(it, permitDetails) }

                    qaDaoServices.permitInsertStatus(permitDetails,applicationMapProperties.mapQaStatusPPACSecretaryAwarding,loggedInUser)

                }else if (permit.hodApproveAssessmentStatus==map.inactiveStatus){
//                    complianceValue= "NON-COMPLIANT"
//                    qaDaoServices.permitInsertStatus(permitDetails,applicationMapProperties.mapQaStatusRe,loggedInUser)
                }
//                qaDaoServices.sendComplianceStatusAndLabReport(permitDetails, complianceValue ?: throw ExpectedDataNotFound(" "))
            }

            permit.recommendationRemarks != null -> {
                //Send manufacturers notification
                qaDaoServices.permitInsertStatus(permitDetails,applicationMapProperties.mapQaStatusPRecommendationApproval,loggedInUser)
                qaDaoServices.sendNotificationForRecommendation(permitDetails)
            }
            permit.recommendationApprovalStatus != null -> {
                //Send notification
                if (permit.recommendationApprovalStatus ==map.activeStatus){
                    with(permit){
                        pscMemberId= qaDaoServices.assignNextOfficerAfterPayment(permitDetails, map, applicationMapProperties.mapQADesignationIDForPSCId)?.id
                    }
                    //updating of Details in DB
                    permitDetails = qaDaoServices.permitUpdateDetails(commonDaoServices.updateDetails(permit, permitDetails) as PermitApplicationsEntity, map, loggedInUser).second
                    qaDaoServices.sendNotificationPSCForAwardingPermit(permitDetails)

                    qaDaoServices.permitInsertStatus(permitDetails,applicationMapProperties.mapQaStatusPPSCMembersAward,loggedInUser)

                }else if (permit.recommendationApprovalStatus ==map.inactiveStatus){
                    permitDetails = qaDaoServices.permitInsertStatus(permitDetails,applicationMapProperties.mapQaStatusPRecommendation,loggedInUser)
                    qaDaoServices.sendNotificationForRecommendationCorrectness(permitDetails)
                }

            }
            permit.pscMemberApprovalStatus != null -> {
                //Send notification
                if (permit.pscMemberApprovalStatus ==map.activeStatus){
                    with(permit){
                        pcmId= qaDaoServices.assignNextOfficerAfterPayment(permitDetails, map, applicationMapProperties.mapQADesignationIDForPCMId)?.id
                    }
                    //updating of Details in DB
                    permitDetails = qaDaoServices.permitUpdateDetails(commonDaoServices.updateDetails(permit, permitDetails) as PermitApplicationsEntity, map, loggedInUser).second
                    qaDaoServices.sendNotificationPCMForAwardingPermit(permitDetails)
                    qaDaoServices.permitInsertStatus(permitDetails,applicationMapProperties.mapQaStatusPPCMAwarding,loggedInUser)


                }else if (permit.pscMemberApprovalStatus ==map.inactiveStatus){
                    qaDaoServices.sendNotificationForDeferredPermitToQaoFromPSC(permitDetails)
                    qaDaoServices.permitInsertStatus(permitDetails,applicationMapProperties.mapQaStatusDeferredPSCMembers,loggedInUser)
                }

            }

            permit.pcmApprovalStatus != null -> {
                //Send notification
                if(permitDetails.permitType == applicationMapProperties.mapQAPermitTypeIDDmark){
                    when (permit.pcmApprovalStatus) {
                        map.activeStatus -> {
                            KotlinLogging.logger { }.info(":::::: Sending compliance status along with e-permit :::::::")
                            permitDetails = qaDaoServices.permitUpdateDetails(commonDaoServices.updateDetails(permit, permitDetails) as PermitApplicationsEntity, map, loggedInUser).second
                            qaDaoServices.pcmGenerateInvoice(map,loggedInUser,permitDetails,permitDetails.permitType?: throw Exception("ID NOT FOUND"))
                        }
                        map.inactiveStatus -> {
                            qaDaoServices.sendNotificationForPermitReviewRejectedFromPCM(permitDetails)
                        }
                    }
                }else{
                    when (permit.pcmApprovalStatus) {
                        map.activeStatus -> {
                            val issueDate = commonDaoServices.getCurrentDate()
                            val permitType = permitDetails.permitType?.let { qaDaoServices.findPermitType(it) }
                            val expiryDate = permitType?.permitAwardYears?.let { commonDaoServices.addYearsToCurrentDate(it.toLong()) }


                            with(permit) {
                                awardedPermitNumber = "${permitType?.markNumber}${generateRandomText(6, map.secureRandom, map.messageDigestAlgorithm, false)}".toUpperCase()
                                permitAwardStatus= map.activeStatus
                                dateOfIssue = issueDate
                                dateOfExpiry = expiryDate
                            }
                            //Generate permit and forward to manufacturer
                            KotlinLogging.logger { }.info(":::::: Sending compliance status along with e-permit :::::::")
                            //updating of Details in DB
                            permitDetails = qaDaoServices.permitUpdateDetails(commonDaoServices.updateDetails(permit, permitDetails) as PermitApplicationsEntity, map, loggedInUser).second
                            qaDaoServices.permitInsertStatus(permitDetails,applicationMapProperties.mapQaStatusPermitAwarded,loggedInUser)
                            //                    qaDaoServices.sendNotificationPSCForAwardingPermit(permitDetails)

                        }
                        map.inactiveStatus -> {
                            qaDaoServices.sendNotificationForDeferredPermitToQaoFromPCM(permitDetails)
                            qaDaoServices.permitInsertStatus(permitDetails,applicationMapProperties.mapQaStatusDeferredPCM,loggedInUser)
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

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION') or hasAuthority('QA_MANAGER_ASSESSORS_MODIFY') or hasAuthority('QA_HOF_MODIFY') " +
            "or hasAuthority('QA_HOD_MODIFY') or hasAuthority('QA_OFFICER_MODIFY') or hasAuthority('QA_PAC_SECRETARY_MODIFY') or hasAuthority('QA_PSC_MEMBERS_MODIFY') or hasAuthority('QA_PCM_MODIFY') or hasAuthority('QA_ASSESSORS_MODIFY')")
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
        val updateResults = qaDaoServices.permitUpdateDetails(commonDaoServices.updateDetails(permit, permitDetails) as PermitApplicationsEntity, map, loggedInUser)

        result = updateResults.first

        permitDetails = updateResults.second

        when {

            permit.recommendationRemarks != null -> {
                with(permitDetails){
                    recommendationApprovalStatus = null
                    recommendationApprovalRemarks = null
                }
                qaDaoServices.permitInsertStatus(permitDetails,applicationMapProperties.mapQaStatusPRecommendationApproval,loggedInUser)
                qaDaoServices.sendNotificationForRecommendation(permitDetails)
            }

        }

        //updating of Details in DB
        result = qaDaoServices.permitUpdateDetails(commonDaoServices.updateDetails(permit, permitDetails) as PermitApplicationsEntity, map, loggedInUser).first


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
        sm.message = "Plant with the following building Name = ${manufacturePlantDetails.buildingName} was added successfully"

        return commonDaoServices.returnValues(result, map, sm)
    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @GetMapping("/kebs/renew/permit-details/save")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun permitRenewDetails(
        @RequestParam( "permitID") permitID: Long,
        model: Model,
    ): String? {

        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        var result: ServiceRequestsEntity?

        var myRenewedPermit = qaDaoServices.permitUpdateNewWithSamePermitNumber(permitID,map, loggedInUser)
        val permit = myRenewedPermit.second
        result = myRenewedPermit.first
//        if (permit.permitType==applicationMapProperties.mapQAPermitTypeIdSmark ){
//
//        }
//            result = qaDaoServices.permitInvoiceCalculation(map, loggedInUser, permit, qaDaoServices.findPermitType(permit.permitType!!))
//        with(permit) {
//            sendApplication = map.activeStatus
//            invoiceGenerated = map.activeStatus
//            permitStatus = applicationMapProperties.mapQaStatusPPayment
//
//        }
//        result = qaDaoServices.permitUpdateDetails(permit, map, loggedInUser).first


        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.closeLink = "${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${result.varField1}"
        sm.message = "You have Successful Renewed your Permit , Invoice has Been Generated"

        return commonDaoServices.returnValues(result, map, sm)
    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @GetMapping("/kebs/resubmit/permit-details/save")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun permitReSubmitDetails(
        @RequestParam( "permitID") permitID: Long,
        model: Model,
    ): String? {

        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val permit = loggedInUser.id?.let { qaDaoServices.findPermitBYUserIDAndId(permitID, it) } ?: throw ExpectedDataNotFound("User Id required")

        val result: ServiceRequestsEntity?

        when {
            permit.sendForPcmReview == map.activeStatus &&  permit.pcmApprovalStatus == map.inactiveStatus -> {
                with(permit){
                    resubmitApplicationStatus = map.activeStatus
                    pcmApprovalStatus = null
                    permitStatus = applicationMapProperties.mapQaStatusResubmitted
                }
            }
            else -> {
                with(permit){
                    resubmitApplicationStatus = map.activeStatus
                    hofQamCompletenessStatus = null
                    permitStatus = applicationMapProperties.mapQaStatusResubmitted
                }
            }
        }


        result = qaDaoServices.permitUpdateDetails(permit,map,loggedInUser).first

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

        result = qaDaoServices.ssfUpdateDetails(permit,sampleSubmissionDetails,loggedInUser,map).first

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

        result = qaDaoServices.requestUpdateDetails(permit,requestDetails,loggedInUser,map).first

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.closeLink = "${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${permit.id}"
        sm.message = "Your request has been submitted Successful Details"

        return commonDaoServices.returnValues(result, map, sm)
    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @GetMapping("/new-permit-submit-review")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun permitDmarkSubmitForReviewDetails(
        @RequestParam( "permitID") permitID: Long,
        model: Model
    ): String? {

        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val permit = loggedInUser.id?.let { qaDaoServices.findPermitBYUserIDAndId(permitID, it) } ?: throw ExpectedDataNotFound("User Id required")

        val result: ServiceRequestsEntity?

        with(permit){
            sendForPcmReview = map.activeStatus
            pcmId= qaDaoServices.assignNextOfficerAfterPayment(permit, map, applicationMapProperties.mapQADesignationIDForPCMId)?.id
            permitStatus = applicationMapProperties.mapQaStatusPPCMAwarding
        }

        result = qaDaoServices.permitUpdateDetails(permit,map,loggedInUser).first

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
        @RequestParam("myRenewPermit") myRenewPermit: String?,
        @ModelAttribute("QaSta3Entity") QaSta3Entity: QaSta3Entity,
        model: Model
    ): String? {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val permit = loggedInUser.id?.let { qaDaoServices.findPermitBYUserIDAndId(permitID, it) }
            ?: throw ExpectedDataNotFound("User Id required")


        when (myRenewPermit) {
            applicationMapProperties.mapPermitRenewMessage -> {
                val sta3 = qaDaoServices.findSTA3WithPermitIDBY(permitID)
                qaDaoServices.sta3Update(
                    commonDaoServices.updateDetails(QaSta3Entity, sta3) as QaSta3Entity,
                    map,
                    loggedInUser
                )
            }
            else -> {
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
            commonDaoServices.updateDetails(
                permit,
                updatePermit
            ) as PermitApplicationsEntity, map, loggedInUser
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
        @RequestParam("myRenewPermit") myRenewPermit: String?,
        @ModelAttribute("QaSta10Entity") QaSta10Entity: QaSta10Entity,
        model: Model
    ): String? {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val permit = loggedInUser.id?.let { qaDaoServices.findPermitBYUserIDAndId(permitID, it) }
            ?: throw ExpectedDataNotFound("User Id required")
        if (myRenewPermit == applicationMapProperties.mapPermitRenewMessage) {
            val sta10 = qaDaoServices.findSTA10WithPermitIDBY(permitID)
            qaDaoServices.sta10Update(
                commonDaoServices.updateDetails(QaSta10Entity, sta10) as QaSta10Entity,
                map,
                loggedInUser
            )
        } else {
            permit.id?.let { qaDaoServices.sta10NewSave(it, QaSta10Entity, loggedInUser, map) }
        }

        val result: ServiceRequestsEntity?

        val updatePermit = PermitApplicationsEntity()
        with(updatePermit) {
            id = permit.id
            sta10FilledStatus = map.inactiveStatus
            permitStatus = applicationMapProperties.mapQaStatusPSTA10Completion
        }
        //updating of Details in DB
        result = qaDaoServices.permitUpdateDetails(commonDaoServices.updateDetails(permit, updatePermit) as PermitApplicationsEntity, map,loggedInUser).first

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
        result = qaDaoServices.ssfSave(permit,sampleSubmissionDetails,loggedInUser,map).first
        with(permit){
            bsNumber = sampleSubmissionDetails.bsNumber
        }
        qaDaoServices.permitInsertStatus(permit,applicationMapProperties.mapQaStatusPLABResults,loggedInUser)

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.closeLink = "${applicationMapProperties.baseUrlValue}/qa/ssf-details?permitID=${permitID}"
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
        val permit = foundSta10Entity.permitId?.let { qaDaoServices.findPermitBYID(it) } ?: throw ExpectedDataNotFound("PERMIT ID ON STA10  with [id=${sta10ID}] is NULL")

        val result: ServiceRequestsEntity?

        val updatePermit = PermitApplicationsEntity()
        with(updatePermit) {
            id = permit.id
            sta10FilledOfficerStatus = map.activeStatus
        }
        //updating of Details in DB
        result = qaDaoServices.permitUpdateDetails(commonDaoServices.updateDetails(permit, updatePermit) as PermitApplicationsEntity, map,loggedInUser).first


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
        if (closeStatus!=null){
            qaSta10.closedProduction = map.activeStatus
            qaDaoServices.sta10Update(qaSta10, map, loggedInUser)
        }else{
            qaSta10.id?.let { qaDaoServices.sta10ManufactureProductNewSave(it, QaProductManufacturedEntity, loggedInUser, map) }
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
        if (closeStatus!=null){
            qaSta10.closedRawMaterials = map.activeStatus
            qaDaoServices.sta10Update(qaSta10, map, loggedInUser)
        }else {
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
        if (closeStatus!=null){
            qaSta10.closedMachineryPlants = map.activeStatus
            qaDaoServices.sta10Update(qaSta10, map, loggedInUser)
        }else {
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
        if (closeStatus!=null){
            qaSta10.closedManufacturingProcesses = map.activeStatus
            qaDaoServices.sta10Update(qaSta10, map, loggedInUser)
        }else {
            qaSta10.id?.let { qaDaoServices.sta10ManufacturingProcessNewSave(it, QaManufacturingProcessEntity, loggedInUser, map) }
        }
        return "${qaDaoServices.sta10Details}=${qaSta10.permitId}"
    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION') or hasAuthority('QA_OFFICER_MODIFY') or hasAuthority('QA_HOD_MODIFY') or hasAuthority('QA_MANAGER_ASSESSORS_MODIFY')" +
            " or hasAuthority('QA_HOF_MODIFY') or hasAuthority('QA_ASSESSORS_MODIFY') or hasAuthority('QA_PAC_SECRETARY_MODIFY') or hasAuthority('QA_PSC_MEMBERS_MODIFY') or hasAuthority('QA_PCM_MODIFY')")
    @PostMapping("/kebs/add/new-upload")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun uploadFilesQA(
        @RequestParam("permitID") permitID: Long,
        @RequestParam("manufactureNonStatus") manufactureNonStatus: Int,
        @RequestParam("inspectionReportStatus") inspectionReportStatus: Int?,
        @RequestParam("scfStatus") scfStatus: Int?,
        @RequestParam("ssfStatus") ssfStatus: Int?,
        @RequestParam("labResultsStatus") labResultsStatus: Int?,
        @RequestParam("docFileName") docFileName: String,
        @RequestParam("doc_file") docFile: MultipartFile,
        model: Model
    )
            : String? {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        var permitDetails =  qaDaoServices.findPermitBYID(permitID)

        val result: ServiceRequestsEntity?
        val uploadResults = qaDaoServices.saveQaFileUploads(docFile, docFileName, loggedInUser, map, permitID, manufactureNonStatus)

        result = uploadResults.first

        when {
            inspectionReportStatus!= null -> {
                permitDetails.inspectionReportId = uploadResults.second.id
                permitDetails = qaDaoServices.permitUpdateDetails(permitDetails,map,loggedInUser).second
                qaDaoServices.permitInsertStatus(permitDetails,applicationMapProperties.mapQaStatusPInspectionReportApproval,loggedInUser)
                sendInspectionReport(permitDetails)
            }
            scfStatus!= null -> {
                permitDetails.scfId = uploadResults.second.id
                permitDetails = qaDaoServices.permitUpdateDetails(permitDetails,map,loggedInUser).second
                qaDaoServices.permitInsertStatus(permitDetails,applicationMapProperties.mapQaStatusPSSF,loggedInUser)

            }
            ssfStatus!= null -> {
                permitDetails.ssfId = uploadResults.second.id
                permitDetails = qaDaoServices.permitUpdateDetails(permitDetails,map,loggedInUser).second
                qaDaoServices.permitInsertStatus(permitDetails,applicationMapProperties.mapQaStatusPBSNumber,loggedInUser)

            }
            labResultsStatus!= null -> {
                permitDetails.testReportId = uploadResults.second.id
                permitDetails = qaDaoServices.permitUpdateDetails(permitDetails,map,loggedInUser).second

            }
        }

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.closeLink = "${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${permitDetails.id}"
        sm.message = "Document Uploaded successful"

        return commonDaoServices.returnValues(result, map, sm)
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

        val fileUploaded = permit.id?.let { qaDaoServices.findUploadedFileByPermitIdAndDocType(it, "FACTORY_ASSESSMENT_REPORT") }
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

        val permit = loggedInUser.id?.let { qaDaoServices.findPermitBYUserIDAndId(permitID, it) } ?: throw ExpectedDataNotFound("Required User ID, check config")
        val permitType = permit.permitType?.let { qaDaoServices.findPermitType(it) } ?: throw ExpectedDataNotFound("PermitType Id Not found")

        result = qaDaoServices.permitInvoiceCalculation(map, loggedInUser, permit, permitType)
        with(permit) {
            sendApplication = map.activeStatus
            invoiceGenerated = map.activeStatus
            permitStatus = applicationMapProperties.mapQaStatusPPayment

        }
        result = qaDaoServices.permitUpdateDetails(permit, map, loggedInUser).first

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.closeLink = "${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${permit.id}"
        sm.message = "You have successful Submitted Your Application, an invoice has been generated, check Your permit detail and pay for the Invoice"

        return commonDaoServices.returnValues(result, map, sm)
    }

    @PreAuthorize("hasAuthority('PERMIT_APPLICATION')")
    @GetMapping("/kebs/mpesa-stk-push")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun payPermitWithMpesa(
        @RequestParam("permitID") permitID: Long,
        @RequestParam("phoneNumber") phoneNumber: String,
        model: Model
    )
            : String? {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()

        val result: ServiceRequestsEntity?

        val permit = loggedInUser.id?.let { qaDaoServices.findPermitBYUserIDAndId(permitID, it) }
            ?: throw ExpectedDataNotFound("Required User ID, check config")
        val invoiceEntity = qaDaoServices.findPermitInvoiceByPermitID(permitID, loggedInUser.id!!)
        result = qaDaoServices.permitInvoiceSTKPush(map, loggedInUser, phoneNumber, invoiceEntity)

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.closeLink =
            "${applicationMapProperties.baseUrlValue}/qa/invoice-details?permitID=${permit.id}"
        sm.message =
            "Check You phone for an STK Push,If You can't see the push either pay with Bank or Normal Mpesa service"

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
        }
        result = qaDaoServices.permitUpdateDetails(permit, map, loggedInUser).first

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
        model: Model): String? {
        val map = commonDaoServices.serviceMapDetails(appId)
        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val permitDetails = qaDaoServices.findPermitBYID(permitID)

        var result: ServiceRequestsEntity?

        result = qaDaoServices.saveQaFileUploads(docFile, docFileName, loggedInUser, map, permitID, null).first

        val hodDetails =  qaDaoServices.assignNextOfficerAfterPayment(permitDetails, map, applicationMapProperties.mapQADesignationIDForHODId)


        with(permitDetails) {
            assessmentScheduledStatus = map.successStatus
            assessmentReportRemarks = assessmentRecommendations
            hodId = hodDetails?.id
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
        sm.closeLink =
            "${applicationMapProperties.baseUrlValue}/qa/permit-details?permitID=${permitDetails.id}"
        sm.message = "Factory Assessment report successfully uploaded"

        return commonDaoServices.returnValues(result, map, sm)
    }
}