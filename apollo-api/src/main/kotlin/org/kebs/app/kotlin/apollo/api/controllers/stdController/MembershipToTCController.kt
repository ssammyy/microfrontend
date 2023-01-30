package org.kebs.app.kotlin.apollo.api.controllers.stdController


import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.std.DraftDocumentService
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.std.MembershipToTCService
import org.kebs.app.kotlin.apollo.api.ports.provided.makeAnyNotBeNull
import org.kebs.app.kotlin.apollo.common.dto.std.ID
import org.kebs.app.kotlin.apollo.common.dto.std.ServerResponse
import org.kebs.app.kotlin.apollo.common.dto.std.TaskDetails
import org.kebs.app.kotlin.apollo.store.model.std.*
import org.kebs.app.kotlin.apollo.store.repo.std.MembershipTCRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("api/v1/migration")
class MembershipToTCController(
    val membershipToTCService: MembershipToTCService,
    val draftDocumentService: DraftDocumentService,
    val membershipTCRepository: MembershipTCRepository,
    val commonDaoServices: CommonDaoServices,


    ) {


    //********************************************************** deployment endpoints ********************************************//
    @PostMapping("/anonymous/membershipToTCdeploy")
    fun deployWorkflow(): ServerResponse {
        membershipToTCService.deployProcessDefinition()
        return ServerResponse(HttpStatus.OK, "Successfully deployed server", HttpStatus.OK)
    }

    //***************************************************** submit justification for formation of TC ***************************//
    @PostMapping("/membershipToTC/submitCallForApplication")
    @ResponseBody
    fun submitCallsForTCMembers(@RequestBody callForTCApplication: CallForTCApplication): ServerResponse{
        //return ResponseEntity(standardRequestService.requestForStandard(standardRequest), HttpStatus.OK)
        return ServerResponse(HttpStatus.OK,"Successfully submitted call for applications",membershipToTCService.submitCallsForTCMembers(callForTCApplication))
    }

    @GetMapping("anonymous/membershipToTC/getCallForApplications")
    fun getCallForApplications(): List<TechnicalCommittee> {
        return membershipToTCService.getCallForApplications()
    }


    @PostMapping("/membershipToTC/process")
    @ResponseBody
    fun checkState( @RequestBody id: ID): ServerResponse {
        return ServerResponse(HttpStatus.OK,"Successfully returned process history",membershipToTCService.checkProcessHistory(id))
    }


    @PostMapping("anonymous/membershipToTC/submitTCMemberApplication")
    @ResponseBody
    fun submitTCMemberApplication(@RequestBody membershipTCApplication: MembershipTCApplication): ServerResponse{
        return ServerResponse(HttpStatus.OK,"Successfully application to be TC Member",membershipToTCService.submitTCMemberApplication(membershipTCApplication))
    }

    @GetMapping("/membershipToTC/getApplicationsForReview")
    fun getApplicationsForReview(): List<MembershipTCApplication> {
        return membershipToTCService.getApplicationsForReview()
    }

    @PostMapping("/membershipToTC/decisionOnApplicantRecommendation")
    @ResponseBody
    fun decisionOnApplicantRecommendation(
        @RequestBody membershipTCApplication: MembershipTCApplication,
        @RequestParam("tCApplicationId") tCApplicationId: Long,
    ): ServerResponse {
        return ServerResponse(
            HttpStatus.OK,
            "Decision on applications for membership to TC",
            membershipToTCService.decisionOnApplicantRecommendation(membershipTCApplication, tCApplicationId)
        )
    }

    @GetMapping("/membershipToTC/getRecommendationsFromHOF")
    fun getRecommendationsFromHOF(): List<MembershipTCApplication> {
        return membershipToTCService.getRecommendationsFromHOF()
    }

    @PostMapping("/membershipToTC/completeSPCReview")
    @ResponseBody
    fun completeSPCReview(
        @RequestBody membershipTCApplication: MembershipTCApplication,
        @RequestParam("tCApplicationId") tCApplicationId: Long,
    ) {
        return membershipToTCService.completeSPCReview(membershipTCApplication, tCApplicationId);
    }

    @GetMapping("/membershipToTC/getRecommendationsFromSPC")
    fun getRecommendationsFromSPC(): List<MembershipTCApplication> {
        return membershipToTCService.getRecommendationsFromSPC()
    }

    @PostMapping("/membershipToTC/decisionOnSPCRecommendation")
    @ResponseBody
    fun decisionOnSPCRecommendation(
        @RequestBody membershipTCApplication: MembershipTCApplication,
        @RequestParam("tCApplicationId") tCApplicationId: Long,
        @RequestParam("decision") decision: String
    ) {
        return membershipToTCService.decisionOnSPCRecommendation(membershipTCApplication, tCApplicationId, decision)

    }


    @GetMapping("/membershipToTC/getAcceptedFromSPC")
    fun getAcceptedFromSPC(): List<MembershipTCApplication> {
        return membershipToTCService.getAccepted()
    }

    @GetMapping("/membershipToTC/getRejectedFromSPC")
    fun getRejectedFromSPC(): List<MembershipTCApplication> {
        return membershipToTCService.getRejected()
    }

    @PostMapping("/membershipToTC/approve")
    @ResponseBody
    fun sendApprovalEmail(
        @RequestBody membershipTCApplication: MembershipTCApplication,
        @RequestParam("tCApplicationId") tCApplicationId: Long,
    ) {
        return membershipToTCService.sendEmailToApproved(membershipTCApplication, tCApplicationId)

    }

    @GetMapping("/anonymous/membershipToTC/approve")
    fun getApproval(
        response: ServerResponse,
        @RequestParam("applicationID") applicationID: String
    ): ResponseEntity<String> {
        return membershipToTCService.approveUser(applicationID)

    }

    @GetMapping("/membershipToTC/getApproved")
    fun getApprovedMembers(): List<MembershipTCApplication> {
        return membershipToTCService.getApprovedEmail()
    }

    @PostMapping("/membershipToTC/forwardToHodIct")
    @ResponseBody
    fun forwardToHodIct(
        @RequestBody membershipTCApplication: MembershipTCApplication,
        @RequestParam("tCApplicationId") tCApplicationId: Long,
        @RequestParam("decision") decision: String //scope
    ) {
        return membershipToTCService.decisionOnApprovedHof(membershipTCApplication, tCApplicationId, decision)

    }

    @GetMapping("/membershipToTC/getCredentials")
    fun getMembersToCreateCredentials(): List<MembershipTCApplication> {
        return membershipToTCService.getAllUsersToCreateCredentials()
    }

    @PostMapping("/membershipToTC/createdCredentials")
    @ResponseBody
    fun createdCredentials(
        @RequestBody membershipTCApplication: MembershipTCApplication,
        @RequestParam("tCApplicationId") tCApplicationId: Long
    ) {
        return membershipToTCService.decisionUponCreation(membershipTCApplication, tCApplicationId)

    }

    @GetMapping("/membershipToTC/getAllUsersCreatedCredentials")
    fun getAllUsersCreatedCredentials(): List<MembershipTCApplication> {
        return membershipToTCService.getAllUsersCreatedCredentials()
    }

    @PostMapping("/membershipToTC/induction")
    @ResponseBody
    fun sendInductionEmail(
        @RequestBody membershipTCApplication: MembershipTCApplication,
        @RequestParam("tCApplicationId") tCApplicationId: Long,
    ) {
        return membershipToTCService.sendEmailForInduction(membershipTCApplication, tCApplicationId)

    }

    @GetMapping("/anonymous/membershipToTC/getInduction")
    fun getInduction(
        response: ServerResponse,
        @RequestParam("applicationID") applicationID: String
    ): ResponseEntity<String> {
        return membershipToTCService.approveUserInduction(applicationID)

    }

    @GetMapping("/membershipToTC/getAllUsersApprovedForInduction")
    fun getAllUsersApprovedForInduction(): List<MembershipTCApplication> {
        return membershipToTCService.getAllUsersApprovedForInduction()
    }

    @PostMapping("/membershipToTC/sendEmailForFirstMeeting")
    @ResponseBody
    fun sendEmailForFirstMeeting(
        @RequestBody membershipTCApplication: MembershipTCApplication,
        @RequestParam("tCApplicationId") tCApplicationId: Long,
        @RequestParam("meetingDate") meetingDate: String,

        ) {
        return membershipToTCService.sendEmailForFirstMeeting(membershipTCApplication, tCApplicationId, meetingDate)

    }


    @GetMapping("/membershipToTC/getTCMemberCreationTasks")
    fun getTCMemberCreationTasks(): List<TaskDetails> {
        return membershipToTCService.getTCMemberCreationTasks()
    }

    @PostMapping("/membershipToTC/saveTCMember")
    @ResponseBody
    fun submitTCMemberApplication(@RequestBody technicalCommitteMember: TechnicalCommitteeMember): ServerResponse {
        return ServerResponse(
            HttpStatus.OK,
            "Successfully uploaded TC Member",
            membershipToTCService.saveTCMember(technicalCommitteMember)
        )
    }

    @PostMapping("/anonymous/membershipToTC/file-upload")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun uploadFilesDraftStandard(
        @RequestParam("callForTCApplicationId") callForTCApplicationId: Long,
        @RequestParam("nomineeName") nomineeName: String,
        @RequestParam("docFile") docFile: List<MultipartFile>,
        @RequestParam("type") type: String,

        model: Model
    ): CommonDaoServices.MessageSuccessFailDTO {

        var docDescription: String;

        val application = membershipTCRepository.findByIdOrNull(callForTCApplicationId)
            ?: throw Exception("APPLICATION DOES NOT EXIST")

        docFile.forEach { u ->
            val upload = DatKebsSdStandardsEntity()
            with(upload) {
                sdDocumentId = application.id
                documentTypeDef = type

            }
            docDescription = "$nomineeName CV"

            membershipToTCService.uploadSDFileNotLoggedIn(
                upload,
                u,
                "UPLOADS",
                nomineeName,
                docDescription

            )
        }

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.message = "Document Uploaded successfully"

        return sm
    }

    // View DI SDT Uploaded document
    @GetMapping("/membershipToTC/view/CurriculumVitae")
    fun viewDiJustificationFile(
        response: HttpServletResponse,
        @RequestParam("draftStandardId") diDocumentId: Long,
        @RequestParam("doctype") doctype: String
    ) {
        val fileUploaded = draftDocumentService.findUploadedDIFileBYIdAndByType(diDocumentId, doctype)
        val fileDoc = commonDaoServices.mapClass(fileUploaded)
        response.contentType = "application/pdf"
//                    response.setHeader("Content-Length", pdfReportStream.size().toString())
        response.addHeader("Content-Disposition", "inline; filename=${fileDoc.name}")
        response.outputStream
            .let { responseOutputStream ->
                responseOutputStream.write(fileDoc.document?.let { makeAnyNotBeNull(it) } as ByteArray)
                responseOutputStream.close()
            }

        KotlinLogging.logger { }.info("VIEW FILE SUCCESSFUL")

    }


}
