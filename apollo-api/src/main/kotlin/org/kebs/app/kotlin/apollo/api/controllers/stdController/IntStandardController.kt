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

    @GetMapping("/international_standard/findStandardStakeholders")
    @ResponseBody
    fun findStandardStakeholders(): List<UserDetailHolder>? {
        return internationalStandardService.findStandardStakeholders()
    }


    //********************************************************** process upload Justification **********************************************************
    @PreAuthorize("hasAuthority('TC_SEC_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/international_standard/prepareAdoptionProposal")
    @ResponseBody
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun prepareAdoptionProposal(@RequestBody iSAdoptionProposalDto: ISAdoptionProposalDto): ServerResponse{

        val iSAdoptionProposal=ISAdoptionProposal().apply {
            proposal_doc_name=iSAdoptionProposalDto.proposal_doc_name
            circulationDate=iSAdoptionProposalDto.circulationDate
            closingDate=iSAdoptionProposalDto.closingDate
            tcSecName=iSAdoptionProposalDto.tcSecName
            title=iSAdoptionProposalDto.title
            scope=iSAdoptionProposalDto.scope
            iStandardNumber=iSAdoptionProposalDto.iStandardNumber
            //stakeholdersList= iSAdoptionProposalDto.stakeholdersList?.let { commonDaoServices.convertClassToJson(it) }
            addStakeholdersList= iSAdoptionProposalDto.addStakeholdersList?.let { commonDaoServices.convertClassToJson(it) }
//            adoptionAcceptableAsPresented=iSAdoptionProposalDto.adoptionAcceptableAsPresented
//            reasonsForNotAcceptance=iSAdoptionProposalDto.reasonsForNotAcceptance
//            recommendations=iSAdoptionProposalDto.recommendations
//            nameOfRespondent=iSAdoptionProposalDto.nameOfRespondent
//            positionOfRespondent=iSAdoptionProposalDto.positionOfRespondent
//            nameOfOrganization=iSAdoptionProposalDto.nameOfOrganization
//            dateOfApplication=iSAdoptionProposalDto.dateOfApplication
            uploadedBy=iSAdoptionProposalDto.uploadedBy
        }
        val stakeholders = iSAdoptionProposalDto.stakeholdersList
//        val gson = Gson()
//        KotlinLogging.logger { }.info { "Request Proposal:" + gson.toJson(iSAdoptionProposalDto) }
        return ServerResponse(HttpStatus.OK,"Successfully uploaded Adoption proposal",internationalStandardService.prepareAdoptionProposal(iSAdoptionProposal,stakeholders))
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
    fun getSessionProposals(@RequestParam("proposalId") proposalId: Long): MutableList<ProposalDetails>
    {
        return internationalStandardService.getProposals(proposalId)
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
    fun submitDraftComments(@RequestBody comDraftCommentDto: ComDraftCommentDto
    ) : ServerResponse
    {

        val comDraftComments= ComDraftComments().apply {
            draftComment=comDraftCommentDto.comment
            commentTitle=comDraftCommentDto.commentTitle
            commentDocumentType=comDraftCommentDto.commentDocumentType
            comClause=comDraftCommentDto.clause
            comParagraph=comDraftCommentDto.paragraph
            typeOfComment=comDraftCommentDto.typeOfComment
            proposedChange=comDraftCommentDto.proposedChange
            requestID=comDraftCommentDto.requestID
            draftID=comDraftCommentDto.draftID
            recommendations=comDraftCommentDto.recommendations
            nameOfRespondent=comDraftCommentDto.nameOfRespondent
            positionOfRespondent=comDraftCommentDto.positionOfRespondent
            nameOfOrganization=comDraftCommentDto.nameOfOrganization
            adoptStandard=comDraftCommentDto.adoptStandard
            adoptDraft=comDraftCommentDto.adoptDraft
            reason=comDraftCommentDto.reason
            uploadDate=comDraftCommentDto.uploadDate
            emailOfRespondent=comDraftCommentDto.emailOfRespondent
            phoneOfRespondent=comDraftCommentDto.phoneOfRespondent
            observation=comDraftCommentDto.observation
        }

        return ServerResponse(HttpStatus.OK,"Comment Updated",internationalStandardService.submitDraftComments(comDraftComments))

    }


    @GetMapping("/international_standard/getAllComment")
    fun getAllComment(@RequestParam("proposalId") proposalId: Long):MutableIterable<ISAdoptionComments>?
    {
        return internationalStandardService.getAllComments(proposalId)
    }

    @PreAuthorize("hasAuthority('TC_SEC_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/international_standard/getAllComments")
    fun getAllComments(@RequestParam("proposalId") proposalId: Long):MutableIterable<ISProposalComments>?
    {
        return internationalStandardService.getProposalComments(proposalId)
    }

    @PreAuthorize("hasAuthority('TC_SEC_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/international_standard/getProposalComments")
    fun getProposalComments(@RequestParam("proposalId") proposalId: Long):MutableIterable<ISProposalComments>?
    {
        return internationalStandardService.getProposalComments(proposalId)
    }


    @PreAuthorize("hasAuthority('TC_SEC_SD_READ') or hasAuthority('SPC_SEC_SD_READ')" +
            " or hasAuthority('SAC_SEC_SD_READ') or hasAuthority('HOP_SD_READ') or hasAuthority('EDITOR_SD_READ') or hasAuthority('PROOFREADER_SD_READ') " +
            " or hasAuthority('HO_SIC_SD_READ')  or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN') or hasAuthority('DRAUGHTSMAN_SD_READ')  ")
    @GetMapping("/international_standard/getUserTasks")
    fun getUserTasks():List<InternationalStandardTasks>
    {
        return internationalStandardService.getUserTasks()
    }


    //decision on Adoption Proposal
    @PreAuthorize("hasAuthority('TC_SEC_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
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

    @PreAuthorize("hasAuthority('TC_SEC_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/international_standard/getApprovedProposals")
    @ResponseBody
    fun getApprovedProposals(): MutableList<ProposalDetails>
    {
        return internationalStandardService.getApprovedProposals()
    }

    //********************************************************** process upload Justification **********************************************************
    @PreAuthorize("hasAuthority('TC_SEC_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/international_standard/prepareJustification")
    @ResponseBody
    fun prepareJustification(@RequestBody iSAdoptionJustifications: ISAdoptionJustifications): ServerResponse{
        val iSAdoptionJustification=ISAdoptionJustification().apply {
            meetingDate=iSAdoptionJustifications.meetingDate
            tcSec_id=iSAdoptionJustifications.tcSec_id
            slNumber=iSAdoptionJustifications.slNumber
            edition=iSAdoptionJustifications.edition
            requestedBy=iSAdoptionJustifications.requestedBy
            issuesAddressed=iSAdoptionJustifications.issuesAddressed
            tcAcceptanceDate=iSAdoptionJustifications.tcAcceptanceDate
            referenceMaterial=iSAdoptionJustifications.referenceMaterial
            department=iSAdoptionJustifications.department
            status=iSAdoptionJustifications.status
            positiveVotes=iSAdoptionJustifications.positiveVotes
            negativeVotes=iSAdoptionJustifications.negativeVotes
            remarks=iSAdoptionJustifications.remarks
            proposalId=iSAdoptionJustifications.proposalId
            draftId=iSAdoptionJustifications.draftId
        }


        return ServerResponse(HttpStatus.OK,"Successfully uploaded Justification",internationalStandardService.prepareJustification(iSAdoptionJustification))
    }

    @PreAuthorize("hasAuthority('SPC_SEC_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/international_standard/getISJustification")
    @ResponseBody
    fun getISJustification(): MutableList<ProposalDetails>
    {
        return internationalStandardService.getISJustification()
    }

    //decision on Adoption Proposal
    @PreAuthorize("hasAuthority('TC_SEC_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
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


    @PreAuthorize("hasAuthority('EDITOR_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/international_standard/submitDraftForEditing")
    @ResponseBody
    fun submitDraftForEditing(@RequestBody isDraftDto: CSDraftDto): ServerResponse
    {
        val companyStandard= CompanyStandard().apply {
            comStdNumber=isDraftDto.comStdNumber
            title=isDraftDto.title
            scope=isDraftDto.scope
            normativeReference=isDraftDto.normativeReference
            symbolsAbbreviatedTerms=isDraftDto.symbolsAbbreviatedTerms
            clause=isDraftDto.clause
            documentType=isDraftDto.documentType
            preparedBy=isDraftDto.preparedBy
            documentType=isDraftDto.docName
            special=isDraftDto.special
            requestId=isDraftDto.requestId
            draftId=isDraftDto.draftId
            departmentId=isDraftDto.departmentId
            subject=isDraftDto.subject
            description=isDraftDto.description
            status=1

        }

//        val gson = Gson()
//        KotlinLogging.logger { }.info { "Editing" + gson.toJson(isDraftDto) }

        return ServerResponse(HttpStatus.OK,"Successfully Edited Draft",internationalStandardService.submitDraftForEditing(companyStandard))
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

    @PreAuthorize("hasAuthority('EDITOR_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
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
    @PreAuthorize("hasAuthority('SAC_SEC_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/international_standard/justificationDecision")
    fun justificationDecision(@RequestBody isJustificationDecision: ISJustificationDecision,internationalStandardRemarks: InternationalStandardRemarks) : List<InternationalStandardTasks>
    {
        return internationalStandardService.justificationDecision(isJustificationDecision,internationalStandardRemarks)
    }




    @PreAuthorize("hasAuthority('HOP_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/international_standard/getUploadedDraft")
    @ResponseBody
    fun getUploadedDraft(): MutableList<ISUploadedDraft>
    {
        return internationalStandardService.getUploadedDraft()
    }

    @PreAuthorize("hasAuthority('HOP_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/international_standard/getIsPublishingTasks")
    @ResponseBody
    fun getIsPublishingTasks(): MutableList<ISUploadedDraft>
    {
        return internationalStandardService.getIsPublishingTasks()
    }



    //decision on Adoption Proposal
    @PreAuthorize("hasAuthority('HOP_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/international_standard/checkRequirements")
    fun checkRequirements(@RequestBody iSDraftDecisions: ISDraftDecisions
    ) : ServerResponse
    {
        val iSUploadStandard= ISUploadStandard().apply {
            accentTo=iSDraftDecisions.accentTo
            justificationNo=iSDraftDecisions.justificationId
            id=iSDraftDecisions.draftId
        }
        val internationalStandardRemarks= InternationalStandardRemarks().apply {
            proposalId=iSDraftDecisions.proposalId
            remarks=iSDraftDecisions.comments
        }

        return ServerResponse(HttpStatus.OK,"Decision",internationalStandardService.checkRequirements(iSUploadStandard,internationalStandardRemarks))

    }

    @PreAuthorize("hasAuthority('EDITOR_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/international_standard/getApprovedDraft")
    @ResponseBody
    fun getApprovedDraft(): MutableList<ISUploadedDraft>
    {
        return internationalStandardService.getApprovedDraft()
    }

    //approve International Standard

    @PreAuthorize("hasAuthority('EDITOR_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/international_standard/editStandardDraft")
    @ResponseBody
    fun editStandardDraft(@RequestBody isDraftDto: ISDraftDto): ServerResponse
    {

        val iSUploadStandard= ISUploadStandard().apply {
            proposalId=isDraftDto.proposalId
            justificationNo=isDraftDto.justificationNo
            id=isDraftDto.id
            title=isDraftDto.title
            scope=isDraftDto.scope
            normativeReference=isDraftDto.normativeReference
            symbolsAbbreviatedTerms=isDraftDto.symbolsAbbreviatedTerms
            clause=isDraftDto.clause
            documentType=isDraftDto.docName
            iSNumber=isDraftDto.standardNumber
            special=isDraftDto.special
            draughting=isDraftDto.draughting
        }

//              val gson = Gson()
//        KotlinLogging.logger { }.info { "Editing" + gson.toJson(isDraftDto) }
//        return "Response"
        return ServerResponse(HttpStatus.OK,"Successfully Edited Workshop Draft",internationalStandardService.editStandardDraft(iSUploadStandard))
    }

    @PreAuthorize("hasAuthority('DRAUGHTSMAN_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/international_standard/getEditedDraft")
    @ResponseBody
    fun getEditedDraft(): MutableList<ISUploadedDraft>
    {
        return internationalStandardService.getEditedDraft()
    }


    @PreAuthorize("hasAuthority('DRAUGHTSMAN_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/international_standard/draughtStandard")
    @ResponseBody
    fun draughtStandard(@RequestBody isDraftDto: ISDraftDto): ServerResponse
    {

        val iSUploadStandard= ISUploadStandard().apply {
            proposalId=isDraftDto.proposalId
            justificationNo=isDraftDto.justificationNo
            id=isDraftDto.id
            title=isDraftDto.title
            scope=isDraftDto.scope
            normativeReference=isDraftDto.normativeReference
            symbolsAbbreviatedTerms=isDraftDto.symbolsAbbreviatedTerms
            clause=isDraftDto.clause
            documentType=isDraftDto.docName
            iSNumber=isDraftDto.standardNumber
            special=isDraftDto.special
        }
        return ServerResponse(HttpStatus.OK,"Successfully Draughted Workshop Draft",internationalStandardService.draughtStandard(iSUploadStandard))
    }

    @PreAuthorize("hasAuthority('PROOFREADER_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/international_standard/getDraughtedDraft")
    @ResponseBody
    fun getDraughtedDraft(): MutableList<ISUploadedDraft>
    {
        return internationalStandardService.getDraughtedDraft()
    }

    @PreAuthorize("hasAuthority('PROOFREADER_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/international_standard/proofReadStandard")
    @ResponseBody
    fun proofReadStandard(@RequestBody isDraftDto: ISDraftDto): ServerResponse
    {

        val iSUploadStandard= ISUploadStandard().apply {
            proposalId=isDraftDto.proposalId
            justificationNo=isDraftDto.justificationNo
            id=isDraftDto.id
            title=isDraftDto.title
            scope=isDraftDto.scope
            normativeReference=isDraftDto.normativeReference
            symbolsAbbreviatedTerms=isDraftDto.symbolsAbbreviatedTerms
            clause=isDraftDto.clause
            documentType=isDraftDto.docName
            iSNumber=isDraftDto.standardNumber
            special=isDraftDto.special
        }
        return ServerResponse(HttpStatus.OK,"Successfully Proof Read Workshop Draft",internationalStandardService.proofReadStandard(iSUploadStandard))
    }

    @PreAuthorize("hasAuthority('HOP_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/international_standard/getProofReadDraft")
    @ResponseBody
    fun getProofReadDraft(): MutableList<ISUploadedDraft>
    {
        return internationalStandardService.getProofReadDraft()
    }

    @PreAuthorize("hasAuthority('HOP_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/international_standard/approveProofReadStandard")
    fun approveProofReadStandard(@RequestBody iSDraftDecisions: ISDraftDecisions
    ) : ServerResponse
    {
        val iSUploadStandard= ISUploadStandard().apply {
            accentTo=iSDraftDecisions.accentTo
            justificationNo=iSDraftDecisions.justificationId
            id=iSDraftDecisions.draftId
        }
        val internationalStandardRemarks= InternationalStandardRemarks().apply {
            proposalId=iSDraftDecisions.proposalId
            remarks=iSDraftDecisions.comments
        }

        return ServerResponse(HttpStatus.OK,"Decision",internationalStandardService.approveProofReadStandard(iSUploadStandard,internationalStandardRemarks))

    }

    @PreAuthorize("hasAuthority('EDITOR_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/international_standard/getApprovedProofReadDraft")
    @ResponseBody
    fun getApprovedProofReadDraft(): MutableList<ISUploadedDraft>
    {
        return internationalStandardService.getApprovedProofReadDraft()
    }

    //decision on Adoption Proposal
    @PreAuthorize("hasAuthority('EDITOR_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/international_standard/approveEditedStandard")
    fun approveEditedStandard(@RequestBody iSDraftDecisions: ISDraftDecisions
    ) : ServerResponse
    {
        val iSUploadStandard= ISUploadStandard().apply {
            accentTo=iSDraftDecisions.accentTo
            justificationNo=iSDraftDecisions.justificationId
            id=iSDraftDecisions.draftId
        }
        val internationalStandardRemarks= InternationalStandardRemarks().apply {
            proposalId=iSDraftDecisions.proposalId
            remarks=iSDraftDecisions.comments
        }

        return ServerResponse(HttpStatus.OK,"Decision",internationalStandardService.approveEditedStandard(iSUploadStandard,internationalStandardRemarks))

    }

    @PreAuthorize("hasAuthority('SAC_SEC_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/international_standard/getApprovedEditedDraft")
    @ResponseBody
    fun getApprovedEditedDraft(): MutableList<ISUploadedDraft>
    {
        return internationalStandardService.getApprovedEditedDraft()
    }

    //SAC Decision
    @PreAuthorize("hasAuthority('SAC_SEC_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/international_standard/approveInternationalStandard")
    fun approveInternationalStandard(@RequestBody iSDraftDecisions: ISDraftDecisionsStd
    ) : ServerResponse
    {
        val iSUploadStandard= ISUploadStandard().apply {
            accentTo=iSDraftDecisions.accentTo
            justificationNo=iSDraftDecisions.justificationId
            id=iSDraftDecisions.draftId

        }
        val internationalStandardRemarks= InternationalStandardRemarks().apply {
            proposalId=iSDraftDecisions.proposalId
            remarks=iSDraftDecisions.comments
        }

        val standard= Standard().apply {
            title=iSDraftDecisions.title
            normativeReference=iSDraftDecisions.normativeReference
            symbolsAbbreviatedTerms=iSDraftDecisions.symbolsAbbreviatedTerms
            clause=iSDraftDecisions.clause
            scope=iSDraftDecisions.scope
            special=iSDraftDecisions.special
            standardNumber=iSDraftDecisions.standardNumber
        }



        return ServerResponse(HttpStatus.OK,"Decision",internationalStandardService.approveInternationalStandard(iSUploadStandard,internationalStandardRemarks,standard))

    }


    @PreAuthorize("hasAuthority('HO_SIC_SD_READ') or hasAuthority('HOD_SIC_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/international_standard/getStandardForGazettement")
    @ResponseBody
    fun getStandardForGazettement(): MutableList<ISUploadedDraft>
    {
        return internationalStandardService.getStandardForGazettement()
    }

    //********************************************************** process upload Gazette Notice **********************************************************
    @PreAuthorize("hasAuthority('HO_SIC_SD_MODIFY') or hasAuthority('HOD_SIC_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
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





//    //********************************************************** process upload Standard **********************************************************
//    @PreAuthorize("hasAuthority('HOP_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
//    @PostMapping("/uploadISStandard")
//    @ResponseBody
//    fun uploadISStandard(@RequestBody iSUploadStandard: ISUploadStandard,isJustificationDecision: ISJustificationDecision,
//                         internationalStandardRemarks: InternationalStandardRemarks,standard: Standard): ServerResponse
//    {
//        return ServerResponse(HttpStatus.OK,"Successfully uploaded Standard",internationalStandardService.uploadISStandard(iSUploadStandard,isJustificationDecision,internationalStandardRemarks,standard))
//    }
//
//    @PostMapping("/std-file-upload")
//    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
//    fun uploadISFiles(
//        @RequestParam("isStandardID") isStandardID: Long,
//        @RequestParam("docFile") docFile: List<MultipartFile>,
//        model: Model
//    ): CommonDaoServices.MessageSuccessFailDTO {
//
//        val loggedInUser = commonDaoServices.loggedInUserDetails()
//        val isStandard = isUploadStandardRepository.findByIdOrNull(isStandardID)?: throw Exception("IS STANDARD DOCUMENT ID DOES NOT EXIST")
//
//        docFile.forEach { u ->
//            val upload = ISStandardUploads()
//            with(upload) {
//                isStdDocumentId = isStandard.id
//
//            }
//            internationalStandardService.uploadISDFile(
//                upload,
//                u,
//                "UPLOADS",
//                loggedInUser,
//                "IS STANDARD"
//            )
//        }
//
//        val sm = CommonDaoServices.MessageSuccessFailDTO()
//        sm.message = "Document Uploaded successfully"
//
//        return sm
//    }
//
//
//    //view IS Justification Document
//    @GetMapping("/view/iStandard")
//    fun viewStandardFile(
//        response: HttpServletResponse,
//        @RequestParam("isStdDocumentId") isStdDocumentId: Long
//    ) {
//        val fileUploaded = internationalStandardService.findUploadedSTFileBYId(isStdDocumentId)
//        val fileDoc = commonDaoServices.mapClass(fileUploaded)
//        response.contentType = "application/pdf"
////                    response.setHeader("Content-Length", pdfReportStream.size().toString())
//        response.addHeader("Content-Disposition", "inline; filename=${fileDoc.name}")
//        response.outputStream
//            .let { responseOutputStream ->
//                responseOutputStream.write(fileDoc.document?.let { makeAnyNotBeNull(it) } as ByteArray)
//                responseOutputStream.close()
//            }
//
//        KotlinLogging.logger { }.info("VIEW FILE SUCCESSFUL")
//
//    }



//    //upload gazzette notice document
//    @PostMapping("/gzt-file-upload")
//    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
//    fun uploadISGFiles(
//        @RequestParam("isStandardID") isStandardID: Long,
//        @RequestParam("docFile") docFile: List<MultipartFile>,
//        model: Model
//    ): CommonDaoServices.MessageSuccessFailDTO {
//
//        val loggedInUser = commonDaoServices.loggedInUserDetails()
//        val isGazette = isGazetteNoticeRepository.findByIdOrNull(isStandardID)?: throw Exception("IS GAZETTE DOCUMENT ID DOES NOT EXIST")
//
//        docFile.forEach { u ->
//            val upload = SDISGazetteNoticeUploads()
//            with(upload) {
//                isGnDocumentId = isGazette.id
//
//            }
//            internationalStandardService.uploadISGFile(
//                upload,
//                u,
//                "UPLOADS",
//                loggedInUser,
//                "IS STANDARD"
//            )
//        }
//
//        val sm = CommonDaoServices.MessageSuccessFailDTO()
//        sm.message = "Document Uploaded successfully"
//
//        return sm
//    }



    //********************************************************** process upload Gazettement Date **********************************************************
//    @PreAuthorize("hasAuthority('HO_SIC_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
//    @PostMapping("/updateGazettementDate")
//    @ResponseBody
//    fun updateGazettementDate(@RequestBody iSGazettement: ISGazettement): ServerResponse
//    {
//        return ServerResponse(HttpStatus.OK,"Successfully uploaded Gazette Notice",internationalStandardService.updateGazettementDate(iSGazettement))
//    }
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
