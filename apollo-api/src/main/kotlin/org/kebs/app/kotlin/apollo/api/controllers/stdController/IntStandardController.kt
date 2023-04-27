package org.kebs.app.kotlin.apollo.api.controllers.stdController

import com.google.gson.Gson
import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.std.*
import org.kebs.app.kotlin.apollo.api.ports.provided.makeAnyNotBeNull
import org.kebs.app.kotlin.apollo.common.dto.std.*
import org.kebs.app.kotlin.apollo.store.model.std.*
import org.kebs.app.kotlin.apollo.store.repo.std.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import javax.servlet.http.HttpServletResponse

@RestController
//@CrossOrigin(origins = ["http://localhost:4200"])
@RequestMapping("api/v1/migration")
class IntStandardController(
    val internationalStandardService: IntStandardService,
    private val isAdoptionProposalRepository: ISAdoptionProposalRepository,
    private val isAdoptionJustificationRepository: ISAdoptionJustificationRepository,
    private val isStandardUploadsRepository: ISStandardUploadsRepository,
    private val isUploadStandardRepository: ISUploadStandardRepository,
    private val sdisGazetteNoticeUploadsRepository: SDISGazetteNoticeUploadsRepository,
    private val isGazetteNoticeRepository: ISGazetteNoticeRepository,
    private val commonDaoServices: CommonDaoServices,
    private val comStdDraftRepository: ComStdDraftRepository,
    ) {
    //********************************************************** deployment endpoints **********************************************************
    @PostMapping("/international_standard/deploy")
    fun deployWorkflow(): ServerResponse {
        internationalStandardService.deployProcessDefinition()
        return ServerResponse(HttpStatus.OK,"Successfully deployed server", HttpStatus.OK)
    }

//    //********************************************************** deployment endpoints **********************************************************
//    @PostMapping("/startProcessInstance")
//    fun startProcess(): ServerResponse {
//        internationalStandardService.startProcessInstance()
//        return ServerResponse(HttpStatus.OK,"Successfully Started server", HttpStatus.OK)
//    }
//    *****************************************Find Stake Holders***********************************

    @GetMapping("/international_standard/getEditorDetails")
    @ResponseBody
    fun getEditorDetails(): List<UserDetailHolder> {
        return internationalStandardService.getEditorDetails()
    }

    @GetMapping("/international_standard/getDraughtsManDetails")
    @ResponseBody
    fun getDraughtsManDetails(): List<UserDetailHolder> {
        return internationalStandardService.getDraughtsManDetails()
    }

    @GetMapping("/international_standard/getProofReaderDetails")
    @ResponseBody
    fun getProofReaderDetails(): List<UserDetailHolder> {
        return internationalStandardService.getProofReaderDetails()
    }

    @GetMapping("/international_standard/findStandardStakeholders")
    @ResponseBody
    fun findStandardStakeholders(): List<UserDetailHolder>? {
        return internationalStandardService.findStandardStakeholders()
    }

    //Get KNW Committee
    //@PreAuthorize("hasAuthority('TC_SEC_SD_READ') or hasAuthority('KNW_SEC_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/international_standard/getIntStandardProposals")
    @ResponseBody
    fun getIntStandardProposals(): MutableList<StandardRequest>
    {
        return internationalStandardService.getIntStandardProposals()
    }


    //********************************************************** process upload Justification **********************************************************
    //@PreAuthorize("hasAuthority('TC_SEC_SD_MODIFY') or hasAuthority('KNW_SEC_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/international_standard/prepareAdoptionProposal")
    @ResponseBody
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun prepareAdoptionProposal(@RequestBody iSAdoptionProposalDto: ISAdoptionProposalDto): ServerResponse{

        return ServerResponse(HttpStatus.OK,"Successfully uploaded Adoption proposal",internationalStandardService.prepareAdoptionProposal(iSAdoptionProposalDto))
    }

    @PostMapping("/international_standard/draft-file-upload")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun uploadDraftFiles(
        @RequestParam("comStdDraftID") comStdDraftID: Long,
        @RequestParam("docFile") docFile: List<MultipartFile>,
        model: Model
    ): CommonDaoServices.MessageSuccessFailDTO {

        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val comDraft = comStdDraftRepository.findByIdOrNull(comStdDraftID)?: throw Exception("DRAFT DOCUMENT ID DOES NOT EXIST")

        docFile.forEach { u ->
            val upload = ComStandardDraftUploads()
            with(upload) {
                comDraftDocumentId = comDraft.id

            }
            internationalStandardService.uploadDrFile(
                upload,
                u,
                "UPLOADS",
                loggedInUser,
                "INTERNATIONAL STANDARD DRAFT"
            )
        }

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.message = "Document Uploaded successfully"

        return sm
    }

    @PostMapping("/international_standard/file-upload")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun uploadFiles(
        @RequestParam("isProposalID") isProposalID: Long,
        @RequestParam("docFile") docFile: List<MultipartFile>,
        model: Model
    ): CommonDaoServices.MessageSuccessFailDTO {

        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val iSAdoptionProposal = isAdoptionProposalRepository.findByIdOrNull(isProposalID)?: throw Exception("IS PROPOSAL DOCUMENT ID DOES NOT EXIST")

        docFile.forEach { u ->
            val upload = SdIsDocumentUploads()
            with(upload) {
                isDocumentId = iSAdoptionProposal.id

            }
            internationalStandardService.uploadISFile(
                upload,
                u,
                "UPLOADS",
                loggedInUser,
                "IS Proposal"
            )
        }

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.message = "Document Uploaded successfully"

        return sm
    }

    //@PreAuthorize("hasAuthority('TC_SEC_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/international_standard/getProposal")
    @ResponseBody
    fun getProposal(): MutableList<ProposalDetails>
    {
        return internationalStandardService.getProposal()
    }

    @GetMapping("/anonymous/international_standard/getProposals")
    @ResponseBody
    fun getProposals(@RequestParam("proposalId") proposalId: Long): MutableList<ProposalDetails>
    {
        return internationalStandardService.getProposals(proposalId)
    }

    @GetMapping("/international_standard/getSessionProposals")
    @ResponseBody
    fun getSessionProposals(): MutableList<ProposalDetails>?
    {
        return internationalStandardService.getSessionProposals()
    }


    //********************************************************** Submit Comments **********************************************************
   //view Proposal Document
    @GetMapping("/international_standard/view/proposal")
    fun viewProposalFile(
        response: HttpServletResponse,
        @RequestParam("isDocumentId") isDocumentId: Long
    ) {
        val fileUploaded = internationalStandardService.findUploadedFileBYId(isDocumentId)
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

    //@PreAuthorize("hasAuthority('STAKEHOLDERS_SD_MODIFY')")
    @PostMapping("/international_standard/submitProposalComments")
    @ResponseBody
    fun submitProposalComments(@RequestBody iSAdoptionProposalComments: ISPropComments  ): ServerResponse{
        val isAdoptionComments= ISAdoptionComments().apply {
            adoption_proposal_comment=iSAdoptionProposalComments.comment
            proposalID=iSAdoptionProposalComments.proposalID
            commentTitle=iSAdoptionProposalComments.commentTitle
            commentDocumentType=iSAdoptionProposalComments.commentDocumentType
            comNameOfOrganization=iSAdoptionProposalComments.nameOfOrganization
            comClause=iSAdoptionProposalComments.clause
            scope=iSAdoptionProposalComments.scope
            dateOfApplication=iSAdoptionProposalComments.preparedDate
            comParagraph=iSAdoptionProposalComments.paragraph
            observation=iSAdoptionProposalComments.observation
            typeOfComment=iSAdoptionProposalComments.typeOfComment
            proposedChange=iSAdoptionProposalComments.proposedChange
            recommendations=iSAdoptionProposalComments.recommendations
            nameOfRespondent=iSAdoptionProposalComments.nameOfRespondent
            positionOfRespondent=iSAdoptionProposalComments.positionOfRespondent
            nameOfOrganization=iSAdoptionProposalComments.nameOfOrganization
        }

        return ServerResponse(HttpStatus.OK,"Comment Has been submitted",internationalStandardService.submitAPComments(isAdoptionComments))
    }

    //@PreAuthorize("hasAuthority('STAKEHOLDERS_SD_MODIFY')")
    @PostMapping("/anonymous/international_standard/submitAPComments")
    @ResponseBody
    fun submitAPComments(@RequestBody iSAdoptionProposalComments: ISPropComments  ): ServerResponse{
        val isAdoptionComments= ISAdoptionComments().apply {
            adoption_proposal_comment=iSAdoptionProposalComments.comment
            proposalID=iSAdoptionProposalComments.proposalID
            commentTitle=iSAdoptionProposalComments.commentTitle
            commentDocumentType=iSAdoptionProposalComments.commentDocumentType
            comNameOfOrganization=iSAdoptionProposalComments.nameOfOrganization
            comClause=iSAdoptionProposalComments.clause
            scope=iSAdoptionProposalComments.scope
            dateOfApplication=iSAdoptionProposalComments.preparedDate
            comParagraph=iSAdoptionProposalComments.paragraph
            observation=iSAdoptionProposalComments.observation
            typeOfComment=iSAdoptionProposalComments.typeOfComment
            proposedChange=iSAdoptionProposalComments.proposedChange
            recommendations=iSAdoptionProposalComments.recommendations
            nameOfRespondent=iSAdoptionProposalComments.nameOfRespondent
            positionOfRespondent=iSAdoptionProposalComments.positionOfRespondent
            nameOfOrganization=iSAdoptionProposalComments.nameOfOrganization
        }

        return ServerResponse(HttpStatus.OK,"Comment Has been submitted",internationalStandardService.submitAPComments(isAdoptionComments))
    }

    @PostMapping("/anonymous/international_standard/submitDraftComments")
    fun submitDraftComments(@RequestBody intDraftCommentDto: ProposalCommentsDto
    ) : ServerResponse
    {

        return ServerResponse(HttpStatus.OK,"Comment Updated",internationalStandardService.submitDraftComments(intDraftCommentDto))

    }

    @PostMapping("/international_standard/submitDraftComment")
    fun submitDraftComment(@RequestBody intDraftCommentDto: ProposalCommentsDto
    ) : ServerResponse
    {

        return ServerResponse(HttpStatus.OK,"Comment Updated",internationalStandardService.submitDraftComment(intDraftCommentDto))

    }


    @GetMapping("/international_standard/getAllComment")
    fun getAllComment(@RequestParam("proposalId") proposalId: Long):MutableIterable<ISAdoptionComments>?
    {
        return internationalStandardService.getAllComments(proposalId)
    }

    //@PreAuthorize("hasAuthority('TC_SEC_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/international_standard/getAllComments")
    fun getAllComments(@RequestParam("proposalId") proposalId: Long):MutableIterable<ISProposalComments>?
    {
        return internationalStandardService.getProposalComments(proposalId)
    }

   // @PreAuthorize("hasAuthority('TC_SEC_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/international_standard/getProposalComments")
    fun getProposalComments(@RequestParam("proposalId") proposalId: Long):MutableIterable<ISProposalComments>?
    {
        return internationalStandardService.getProposalComments(proposalId)
    }


//    @PreAuthorize("hasAuthority('TC_SEC_SD_READ') or hasAuthority('SPC_SEC_SD_READ')" +
//            " or hasAuthority('SAC_SEC_SD_READ') or hasAuthority('HOP_SD_READ') or hasAuthority('EDITOR_SD_READ') or hasAuthority('PROOFREADER_SD_READ') " +
//            " or hasAuthority('HO_SIC_SD_READ')  or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN') or hasAuthority('DRAUGHTSMAN_SD_READ')  ")
    @GetMapping("/international_standard/getUserTasks")
    fun getUserTasks():List<InternationalStandardTasks>
    {
        return internationalStandardService.getUserTasks()
    }


    //decision on Adoption Proposal
    //@PreAuthorize("hasAuthority('TC_SEC_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/international_standard/decisionOnProposal")
    fun decisionOnProposal(@RequestBody comStdDraftDecisionDto: IntStdDraftDecisionDto
    ) : ServerResponse
    {
        val comStdDraft= ComStdDraft().apply {
            id=comStdDraftDecisionDto.draftId
            accentTo=comStdDraftDecisionDto.accentTo
        }

        val companyStandardRemarks= CompanyStandardRemarks().apply {
            requestId=comStdDraftDecisionDto.proposalId
            remarks=comStdDraftDecisionDto.comments
        }

//        val gson = Gson()
//        KotlinLogging.logger { }.info { "WORKSHOP DRAFT DECISION" + gson.toJson(iSDecisions) }

        return ServerResponse(HttpStatus.OK,"Decision",internationalStandardService.decisionOnProposal(comStdDraft,companyStandardRemarks))

    }

    @GetMapping("/international_standard/getDraftComments")
    fun getDraftComments(@RequestParam("requestId") requestId: Long):MutableIterable<CompanyStandardRemarks>?
    {
        return internationalStandardService.getDraftComments(requestId)
    }

    //@PreAuthorize("hasAuthority('TC_SEC_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/international_standard/getApprovedProposals")
    @ResponseBody
    fun getApprovedProposals(): MutableList<ProposalDetails>
    {
        return internationalStandardService.getApprovedProposals()
    }

    //********************************************************** process upload Justification **********************************************************
    //@PreAuthorize("hasAuthority('TC_SEC_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/international_standard/prepareJustification")
    @ResponseBody
    fun prepareJustification(@RequestBody isProposalJustification: ISProposalJustification): ServerResponse{
        return ServerResponse(HttpStatus.OK,"Successfully uploaded Justification",internationalStandardService.prepareJustification(isProposalJustification))
    }

    @PostMapping("/international_standard/editJustification")
    @ResponseBody
    fun editJustification(@RequestBody isProposalJustification: ISProposalJustification): ServerResponse{
        return ServerResponse(HttpStatus.OK,"Successfully uploaded Justification",internationalStandardService.editJustification(isProposalJustification))
    }



    //@PreAuthorize("hasAuthority('SPC_SEC_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/international_standard/getJustification")
    @ResponseBody
    fun getJustification(): MutableList<ProposalDetails>
    {
        return internationalStandardService.getJustification()
    }


    // @PreAuthorize("hasAuthority('SPC_SEC_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/international_standard/getJustificationStatus")
    @ResponseBody
    fun getJustificationStatus(@RequestParam("draftId") draftId: Long): JustificationStatus
    {
        return internationalStandardService.getJustificationStatus(draftId)
    }

   // @PreAuthorize("hasAuthority('SPC_SEC_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/international_standard/getISJustification")
    @ResponseBody
    fun getISJustification(@RequestParam("draftId") draftId: Long): MutableList<ISAdoptionJustification>
    {
        return internationalStandardService.getISJustification(draftId)
    }

    //decision on Adoption Proposal
    //@PreAuthorize("hasAuthority('SPC_SEC_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/international_standard/decisionOnJustification")
    fun decisionOnJustification(@RequestBody comStdDraftDecisionDto: IntStdDraftDecisionDto
    ) : ServerResponse
    {
        val comStdDraft= ComStdDraft().apply {
            id=comStdDraftDecisionDto.draftId
            accentTo=comStdDraftDecisionDto.accentTo
        }

        val companyStandardRemarks= CompanyStandardRemarks().apply {
            requestId=comStdDraftDecisionDto.proposalId
            remarks=comStdDraftDecisionDto.comments
        }

//        val gson = Gson()
//        KotlinLogging.logger { }.info { "WORKSHOP DRAFT DECISION" + gson.toJson(iSDecisions) }

        return ServerResponse(HttpStatus.OK,"Decision",internationalStandardService.decisionOnJustification(comStdDraft,companyStandardRemarks))

    }

    //@PreAuthorize("hasAuthority('TC_SEC_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/international_standard/getApprovedJustification")
    @ResponseBody
    fun getApprovedJustification(): MutableList<ProposalDetails>
    {
        return internationalStandardService.getApprovedJustification()
    }

   // @PreAuthorize("hasAuthority('EDITOR_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/international_standard/submitDraftForEditing")
    @ResponseBody
    fun submitDraftForEditing(@RequestBody isDraftDto: CSDraftDto): ServerResponse
    {

        return ServerResponse(HttpStatus.OK,"Successfully Edited Draft",internationalStandardService.submitDraftForEditing(isDraftDto))
    }


    //
    @PostMapping("/international_standard/js-file-upload")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun uploadIJSFiles(
        @RequestParam("isJustificationID") isJustificationID: Long,
        @RequestParam("docFile") docFile: List<MultipartFile>,
        model: Model
    ): CommonDaoServices.MessageSuccessFailDTO {

        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val iSAdoptionJustification = isAdoptionJustificationRepository.findByIdOrNull(isJustificationID)?: throw Exception("IS JUSTIFICATION DOCUMENT ID DOES NOT EXIST")

        docFile.forEach { u ->
            val upload = ISJustificationUploads()
            with(upload) {
                isJSDocumentId = iSAdoptionJustification.id

            }
            internationalStandardService.uploadISJFile(
                upload,
                u,
                "UPLOADS",
                loggedInUser,
                "IS Justification"
            )
        }

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.message = "Document Uploaded successfully"

        return sm
    }


    //view IS Justification Document
    @GetMapping("/international_standard/view/justification")
    fun viewJustificationFile(
        response: HttpServletResponse,
        @RequestParam("isJSDocumentId") isJSDocumentId: Long
    ) {
        val fileUploaded = internationalStandardService.findUploadedJSFileBYId(isJSDocumentId)
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

    //decision on Adoption Proposal
//    @PreAuthorize("hasAuthority('SPC_SEC_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
//    @PostMapping("/international_standard/decisionOnJustification")
//    fun decisionOnJustification(@RequestBody iSJustificationDecisions: ISJustificationDecisions
//    ) : ServerResponse
//    {
//        val iSAdoptionJustification= ISAdoptionJustification().apply {
//            accentTo=iSJustificationDecisions.accentTo
//            id=iSJustificationDecisions.justificationId
//        }
//        val internationalStandardRemarks= InternationalStandardRemarks().apply {
//            proposalId=iSJustificationDecisions.proposalId
//            remarks=iSJustificationDecisions.comments
//        }
//
//        return ServerResponse(HttpStatus.OK,"Decision",internationalStandardService.decisionOnJustification(iSAdoptionJustification,internationalStandardRemarks))
//
//    }

    //@PreAuthorize("hasAuthority('EDITOR_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/international_standard/getApprovedISJustification")
    @ResponseBody
    fun getApprovedISJustification(): MutableList<ISAdoptionProposalJustification>
    {
        return internationalStandardService.getApprovedISJustification()
    }


    @GetMapping("/international_standard/getUserComments")
    fun getUserComments(@RequestParam("id") id: Long):MutableIterable<InternationalStandardRemarks>?
    {
        return internationalStandardService.getUserComments(id)
    }

    //approve International Standard
    //@PreAuthorize("hasAuthority('SAC_SEC_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/international_standard/justificationDecision")
    fun justificationDecision(@RequestBody isJustificationDecision: ISJustificationDecision,internationalStandardRemarks: InternationalStandardRemarks) : List<InternationalStandardTasks>
    {
        return internationalStandardService.justificationDecision(isJustificationDecision,internationalStandardRemarks)
    }




    //@PreAuthorize("hasAuthority('HOP_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/international_standard/getUploadedDraft")
    @ResponseBody
    fun getUploadedDraft(): MutableList<ISUploadedDraft>
    {
        return internationalStandardService.getUploadedDraft()
    }

    //@PreAuthorize("hasAuthority('HOP_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/international_standard/getIsPublishingTasks")
    @ResponseBody
    fun getIsPublishingTasks(): MutableList<ISUploadedDraft>
    {
        return internationalStandardService.getIsPublishingTasks()
    }



    //decision on Adoption Proposal
    //@PreAuthorize("hasAuthority('HOP_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/international_standard/checkRequirements")
    fun checkRequirements(@RequestBody iSDraftDecisions: ISDraftDecisions
    ) : ServerResponse
    {

        return ServerResponse(HttpStatus.OK,"Decision",internationalStandardService.checkRequirements(iSDraftDecisions))

    }

   // @PreAuthorize("hasAuthority('EDITOR_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/international_standard/getApprovedDraft")
    @ResponseBody
    fun getApprovedDraft(): MutableList<ISUploadedDraft>
    {
        return internationalStandardService.getApprovedDraft()
    }

    //approve International Standard

   // @PreAuthorize("hasAuthority('EDITOR_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/international_standard/editStandardDraft")
    @ResponseBody
    fun editStandardDraft(@RequestBody isDraftDto: ISDraftDto): ServerResponse
    {
//        val gson = Gson()
//        KotlinLogging.logger { }.info { "WORKSHOP DRAFT DECISION" + gson.toJson(isDraftDto) }

        return ServerResponse(HttpStatus.OK,"Successfully Edited Workshop Draft",internationalStandardService.editStandardDraft(isDraftDto))
    }

   // @PreAuthorize("hasAuthority('DRAUGHTSMAN_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/international_standard/getEditedDraft")
    @ResponseBody
    fun getEditedDraft(): MutableList<ISUploadedDraft>
    {
        return internationalStandardService.getEditedDraft()
    }


    //@PreAuthorize("hasAuthority('DRAUGHTSMAN_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/international_standard/draughtStandard")
    @ResponseBody
    fun draughtStandard(@RequestBody isDraftDto: ISDraftDto): ServerResponse
    {

        return ServerResponse(HttpStatus.OK,"Successfully Draughted Workshop Draft",internationalStandardService.draughtStandard(isDraftDto))
    }

    @PostMapping("/international_standard/assignProofReader")
    @ResponseBody
    fun assignProofReader(@RequestBody isDraftDto: ISDraftDto): ServerResponse
    {

        return ServerResponse(HttpStatus.OK,"Successfully Draughted Workshop Draft",internationalStandardService.assignProofReader(isDraftDto))
    }

    //@PreAuthorize("hasAuthority('PROOFREADER_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/international_standard/getDraughtedDraft")
    @ResponseBody
    fun getDraughtedDraft(): MutableList<ISUploadedDraft>
    {
        return internationalStandardService.getDraughtedDraft()
    }

   // @PreAuthorize("hasAuthority('PROOFREADER_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/international_standard/proofReadStandard")
    @ResponseBody
    fun proofReadStandard(@RequestBody isDraftDto: ISDraftDto): ServerResponse
    {

        return ServerResponse(HttpStatus.OK,"Successfully Draughted Workshop Draft",internationalStandardService.proofReadStandard(isDraftDto))
    }


   // @PreAuthorize("hasAuthority('HOP_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/international_standard/getProofReadDraft")
    @ResponseBody
    fun getProofReadDraft(): MutableList<ISUploadedDraft>
    {
        return internationalStandardService.getProofReadDraft()
    }

    //decision on Adoption Proposal
    //@PreAuthorize("hasAuthority('HOP_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/international_standard/approveProofReadStandard")
    fun approveProofReadStandard(@RequestBody iSDraftDecisions: ISHopDecision
    ) : ServerResponse
    {

        return ServerResponse(HttpStatus.OK,"Decision",internationalStandardService.approveProofReadStandard(iSDraftDecisions))

    }


    //@PreAuthorize("hasAuthority('EDITOR_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/international_standard/getApprovedProofReadDraft")
    @ResponseBody
    fun getApprovedProofReadDraft(): MutableList<ISUploadedDraft>
    {
        return internationalStandardService.getApprovedProofReadDraft()
    }

    //decision on Adoption Proposal
   // @PreAuthorize("hasAuthority('EDITOR_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
//    @PostMapping("/international_standard/approveEditedStandard")
//    fun approveEditedStandard(@RequestBody iSDraftDecisions: ISDraftDecisions
//    ) : ServerResponse
//    {
//
//        return ServerResponse(HttpStatus.OK,"Decision",internationalStandardService.approveEditedStandard(iSDraftDecisions))
//
//    }

    @PostMapping("/international_standard/approveProofReadLevel")
    fun approveProofReadLevel(@RequestBody iSDraftDecisions: ISDecisions
    ) : ServerResponse
    {

        return ServerResponse(HttpStatus.OK,"Decision",internationalStandardService.approveProofReadLevel(iSDraftDecisions))

    }


    //@PreAuthorize("hasAuthority('SAC_SEC_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/international_standard/getApprovedEditedDraft")
    @ResponseBody
    fun getApprovedEditedDraft(): MutableList<ISUploadedDraft>
    {
        return internationalStandardService.getApprovedEditedDraft()
    }

    //SAC Decision
    //@PreAuthorize("hasAuthority('SAC_SEC_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/international_standard/approveInternationalStandard")
    fun approveInternationalStandard(@RequestBody iSDraftDecisions: ISDraftDecisionsStd
    ) : ServerResponse
    {
        val gson = Gson()
        KotlinLogging.logger { }.info { "WORKSHOP DRAFT DECISION" + gson.toJson(iSDraftDecisions) }

        return ServerResponse(HttpStatus.OK,"Decision",internationalStandardService.approveInternationalStandard(iSDraftDecisions))

    }

    //decision on Adoption Proposal
   // @PreAuthorize("hasAuthority('HOP_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/international_standard/uploadInternationalStandard")
    fun uploadInternationalStandard(@RequestBody iStandardUploadDto: IStandardUploadDto
    ) : ServerResponse
    {

        return ServerResponse(HttpStatus.OK,"Standard Uploaded",internationalStandardService.uploadInternationalStandard(iStandardUploadDto))

    }

    @GetMapping("/international_standard/getCommitteeList")
    @ResponseBody
    fun getStakeHoldersList(@RequestParam("draftId") draftId: Long): MutableList<EmailList>?
    {

        return internationalStandardService.getStakeHoldersList(draftId)
    }

   // @PreAuthorize("hasAuthority('HO_SIC_SD_READ') or hasAuthority('HOD_SIC_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/international_standard/getStandardForGazettement")
    @ResponseBody
    fun getStandardForGazettement(): MutableList<ISUploadedDraft>
    {
        return internationalStandardService.getStandardForGazettement()
    }

    @GetMapping("/anonymous/international_standard/getCompanyStandards")
    @ResponseBody
    fun getCompanyStandards(): MutableList<Standard>
    {
        return internationalStandardService.getCompanyStandards()
    }

    @GetMapping("/anonymous/international_standard/getInternationalStandards")
    @ResponseBody
    fun getInternationalStandards(): MutableList<Standard>
    {
        return internationalStandardService.getInternationalStandards()
    }

    @GetMapping("/anonymous/international_standard/getStandards")
    @ResponseBody
    fun getStandards(): MutableList<Standard>
    {
        return internationalStandardService.getStandards()
    }

    //********************************************************** process upload Gazette Notice **********************************************************
    //@PreAuthorize("hasAuthority('HO_SIC_SD_MODIFY') or hasAuthority('HOD_SIC_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/international_standard/uploadGazetteNotice")
    @ResponseBody
    fun uploadGazetteNotice(@RequestBody standardGazetteDto: StandardGazetteDto): ServerResponse
    {
        val standard= Standard().apply {
            id=standardGazetteDto.id
            description=standardGazetteDto.description
        }
        return ServerResponse(HttpStatus.OK,"Successfully uploaded Gazette Notice",internationalStandardService.uploadGazetteNotice(standard))
    }


    @GetMapping("/international_standard/getPRNumber")
    @ResponseBody
    fun getPRNumber(): String
    {
        return internationalStandardService.getPRNumber();
    }
    @GetMapping("/international_standard/getISNumber")
    @ResponseBody
    fun getISNumber(): String
    {
        return internationalStandardService.getISNumber();
    }
    @GetMapping("/international_standard/getRQNumber")
    @ResponseBody
    fun getRQNumber(): String
    {
        return internationalStandardService.getRQNumber();
    }

}
