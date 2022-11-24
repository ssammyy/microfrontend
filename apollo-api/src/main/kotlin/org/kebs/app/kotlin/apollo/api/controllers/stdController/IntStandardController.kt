package org.kebs.app.kotlin.apollo.api.controllers.stdController

import com.google.gson.Gson
import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.std.*
import org.kebs.app.kotlin.apollo.api.ports.provided.makeAnyNotBeNull
import org.kebs.app.kotlin.apollo.common.dto.std.*
import org.kebs.app.kotlin.apollo.store.model.StagingStandardsLevyManufacturerPenalty
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
@RequestMapping("api/v1/migration/international_standard")
class IntStandardController(
    val internationalStandardService: IntStandardService,
    private val commonDaoServices: CommonDaoServices,
    private val isAdoptionProposalRepository: ISAdoptionProposalRepository,
    private val isAdoptionJustificationRepository: ISAdoptionJustificationRepository,
    private val isStandardUploadsRepository: ISStandardUploadsRepository,
    private val isUploadStandardRepository: ISUploadStandardRepository,
    private val sdisGazetteNoticeUploadsRepository: SDISGazetteNoticeUploadsRepository,
    private val isGazetteNoticeRepository: ISGazetteNoticeRepository
    ) {
    //********************************************************** deployment endpoints **********************************************************
    @PostMapping("/deploy")
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

    //********************************************************** process upload Justification **********************************************************
    @PreAuthorize("hasAuthority('TC_SEC_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/prepareAdoptionProposal")
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
            adoptionAcceptableAsPresented=iSAdoptionProposalDto.adoptionAcceptableAsPresented
            reasonsForNotAcceptance=iSAdoptionProposalDto.reasonsForNotAcceptance
            recommendations=iSAdoptionProposalDto.recommendations
            nameOfRespondent=iSAdoptionProposalDto.nameOfRespondent
            positionOfRespondent=iSAdoptionProposalDto.positionOfRespondent
            nameOfOrganization=iSAdoptionProposalDto.nameOfOrganization
            dateOfApplication=iSAdoptionProposalDto.dateOfApplication
            uploadedBy=iSAdoptionProposalDto.uploadedBy
        }
        return ServerResponse(HttpStatus.OK,"Successfully uploaded Adoption proposal",internationalStandardService.prepareAdoptionProposal(iSAdoptionProposal))
    }

    @PostMapping("/file-upload")
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

    @PreAuthorize("hasAuthority('TC_SEC_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/getProposal")
    @ResponseBody
    fun getProposal(): MutableList<ProposalDetails>
    {
        return internationalStandardService.getProposal()
    }

    //********************************************************** Submit Comments **********************************************************
   //view Proposal Document
    @GetMapping("/view/proposal")
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
    @PostMapping("/SubmitAPComments")
    @ResponseBody
    fun submitAPComments(@RequestBody iSAdoptionProposalComments: ISAdoptionProposalComments  ): ServerResponse{
        val isAdoptionComments= ISAdoptionComments().apply {
            user_id=iSAdoptionProposalComments.user_id
            adoption_proposal_comment=iSAdoptionProposalComments.adoption_proposal_comment
            comment_time=iSAdoptionProposalComments.comment_time
            proposalID=iSAdoptionProposalComments.proposalID
            commentTitle=iSAdoptionProposalComments.commentTitle
            commentDocumentType=iSAdoptionProposalComments.commentDocumentType
            comNameOfOrganization=iSAdoptionProposalComments.comNameOfOrganization
            comClause=iSAdoptionProposalComments.comClause
            comParagraph=iSAdoptionProposalComments.comParagraph
            typeOfComment=iSAdoptionProposalComments.typeOfComment
            proposedChange=iSAdoptionProposalComments.proposedChange
        }

        return ServerResponse(HttpStatus.OK,"Comment Has been submitted",internationalStandardService.submitAPComments(isAdoptionComments))
    }


    @GetMapping("/getAllComments")
    fun getAllComments(@RequestParam("proposalId") proposalId: Long):MutableIterable<ISAdoptionComments>?
    {
        return internationalStandardService.getAllComments(proposalId)
    }

    @PreAuthorize("hasAuthority('TC_SEC_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/getProposalComments")
    fun getProposalComments(@RequestParam("proposalId") proposalId: Long):MutableIterable<ISProposalComments>?
    {
        return internationalStandardService.getProposalComments(proposalId)
    }


    @PreAuthorize("hasAuthority('TC_SEC_SD_READ') or hasAuthority('SPC_SEC_SD_READ')" +
            " or hasAuthority('SAC_SEC_SD_READ') or hasAuthority('HOP_SD_READ') or hasAuthority('EDITOR_SD_READ') or hasAuthority('PROOFREADER_SD_READ') " +
            " or hasAuthority('HO_SIC_SD_READ')  or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN') or hasAuthority('DRAUGHTSMAN_SD_READ')  ")
    @GetMapping("/getUserTasks")
    fun getUserTasks():List<InternationalStandardTasks>
    {
        return internationalStandardService.getUserTasks()
    }


    //decision on Adoption Proposal
    @PreAuthorize("hasAuthority('TC_SEC_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/decisionOnProposal")
    fun decisionOnProposal(@RequestBody iSDecisions: ISTDecisions
    ) : ServerResponse
    {
        val iSAdoptionProposal= ISAdoptionProposal().apply {
            accentTo=iSDecisions.accentTo
        }
        val internationalStandardRemarks= InternationalStandardRemarks().apply {
            proposalId=iSDecisions.proposalId
            remarks=iSDecisions.comments
        }

        val gson = Gson()
        KotlinLogging.logger { }.info { "WORKSHOP DRAFT DECISION" + gson.toJson(iSDecisions) }

        return ServerResponse(HttpStatus.OK,"Decision",internationalStandardService.decisionOnProposal(iSAdoptionProposal,internationalStandardRemarks))

    }

    @PreAuthorize("hasAuthority('TC_SEC_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/getApprovedProposals")
    @ResponseBody
    fun getApprovedProposals(): MutableList<ProposalDetails>
    {
        return internationalStandardService.getApprovedProposals()
    }

    //********************************************************** process upload Justification **********************************************************
    @PreAuthorize("hasAuthority('TC_SEC_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/prepareJustification")
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
        }


        return ServerResponse(HttpStatus.OK,"Successfully uploaded Justification",internationalStandardService.prepareJustification(iSAdoptionJustification))
    }

    @PreAuthorize("hasAuthority('SPC_SEC_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/getISJustification")
    @ResponseBody
    fun getISJustification(): MutableList<ISAdoptionProposalJustification>
    {
        return internationalStandardService.getISJustification()
    }

    //
    @PostMapping("/js-file-upload")
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
    @GetMapping("/view/justification")
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
    @PreAuthorize("hasAuthority('SPC_SEC_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/decisionOnJustification")
    fun decisionOnJustification(@RequestBody iSJustificationDecisions: ISJustificationDecisions
    ) : ServerResponse
    {
        val iSAdoptionJustification= ISAdoptionJustification().apply {
            accentTo=iSJustificationDecisions.accentTo
            id=iSJustificationDecisions.justificationId
        }
        val internationalStandardRemarks= InternationalStandardRemarks().apply {
            proposalId=iSJustificationDecisions.proposalId
            remarks=iSJustificationDecisions.comments
        }

        return ServerResponse(HttpStatus.OK,"Decision",internationalStandardService.decisionOnJustification(iSAdoptionJustification,internationalStandardRemarks))

    }

    @PreAuthorize("hasAuthority('EDITOR_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/getApprovedISJustification")
    @ResponseBody
    fun getApprovedISJustification(): MutableList<ISAdoptionProposalJustification>
    {
        return internationalStandardService.getApprovedISJustification()
    }


    @GetMapping("/getUserComments")
    fun getUserComments(@RequestParam("id") id: Long):MutableIterable<InternationalStandardRemarks>?
    {
        return internationalStandardService.getUserComments(id)
    }

    //approve International Standard
    @PreAuthorize("hasAuthority('SAC_SEC_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/justificationDecision")
    fun justificationDecision(@RequestBody isJustificationDecision: ISJustificationDecision,internationalStandardRemarks: InternationalStandardRemarks) : List<InternationalStandardTasks>
    {
        return internationalStandardService.justificationDecision(isJustificationDecision,internationalStandardRemarks)
    }

    @PreAuthorize("hasAuthority('EDITOR_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/submitDraftForEditing")
    @ResponseBody
    fun submitDraftForEditing(@RequestBody isDraftDto: ISDraftDto): ServerResponse
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
        }

        return ServerResponse(HttpStatus.OK,"Successfully Edited Workshop Draft",internationalStandardService.submitDraftForEditing(iSUploadStandard))
    }

    @PreAuthorize("hasAuthority('HOP_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/getUploadedDraft")
    @ResponseBody
    fun getUploadedDraft(): MutableList<ISUploadedDraft>
    {
        return internationalStandardService.getUploadedDraft()
    }

    //decision on Adoption Proposal
    @PreAuthorize("hasAuthority('HOP_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/checkRequirements")
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
    @GetMapping("/getApprovedDraft")
    @ResponseBody
    fun getApprovedDraft(): MutableList<ISUploadedDraft>
    {
        return internationalStandardService.getApprovedDraft()
    }

    //approve International Standard

    @PreAuthorize("hasAuthority('EDITOR_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/editStandardDraft")
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
        }
        return ServerResponse(HttpStatus.OK,"Successfully Edited Workshop Draft",internationalStandardService.editStandardDraft(iSUploadStandard))
    }

    @PreAuthorize("hasAuthority('DRAUGHTSMAN_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/getEditedDraft")
    @ResponseBody
    fun getEditedDraft(): MutableList<ISUploadedDraft>
    {
        return internationalStandardService.getEditedDraft()
    }


    @PreAuthorize("hasAuthority('DRAUGHTSMAN_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/draughtStandard")
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
        }
        return ServerResponse(HttpStatus.OK,"Successfully Draughted Workshop Draft",internationalStandardService.draughtStandard(iSUploadStandard))
    }

    @PreAuthorize("hasAuthority('PROOFREADER_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/getDraughtedDraft")
    @ResponseBody
    fun getDraughtedDraft(): MutableList<ISUploadedDraft>
    {
        return internationalStandardService.getDraughtedDraft()
    }

    @PreAuthorize("hasAuthority('PROOFREADER_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/proofReadStandard")
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
        }
        return ServerResponse(HttpStatus.OK,"Successfully Proof Read Workshop Draft",internationalStandardService.proofReadStandard(iSUploadStandard))
    }

    @PreAuthorize("hasAuthority('HOP_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/getProofReadDraft")
    @ResponseBody
    fun getProofReadDraft(): MutableList<ISUploadedDraft>
    {
        return internationalStandardService.getProofReadDraft()
    }

    @PreAuthorize("hasAuthority('HOP_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/approveProofReadStandard")
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
    @GetMapping("/getApprovedProofReadDraft")
    @ResponseBody
    fun getApprovedProofReadDraft(): MutableList<ISUploadedDraft>
    {
        return internationalStandardService.getApprovedProofReadDraft()
    }

    //decision on Adoption Proposal
    @PreAuthorize("hasAuthority('EDITOR_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/approveEditedStandard")
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
    @GetMapping("/getApprovedEditedDraft")
    @ResponseBody
    fun getApprovedEditedDraft(): MutableList<ISUploadedDraft>
    {
        return internationalStandardService.getApprovedEditedDraft()
    }

    //SAC Decision
    @PreAuthorize("hasAuthority('SAC_SEC_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/approveInternationalStandard")
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
        }

        return ServerResponse(HttpStatus.OK,"Decision",internationalStandardService.approveInternationalStandard(iSUploadStandard,internationalStandardRemarks,standard))

    }


    @PreAuthorize("hasAuthority('HO_SIC_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/getStandardForGazettement")
    @ResponseBody
    fun getStandardForGazettement(): MutableList<ISUploadedDraft>
    {
        return internationalStandardService.getStandardForGazettement()
    }

    //********************************************************** process upload Gazette Notice **********************************************************
    @PreAuthorize("hasAuthority('HO_SIC_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/uploadGazetteNotice")
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
    @GetMapping("/getPRNumber")
    @ResponseBody
    fun getPRNumber(): String
    {
        return internationalStandardService.getPRNumber();
    }
    @GetMapping("/getISNumber")
    @ResponseBody
    fun getISNumber(): String
    {
        return internationalStandardService.getISNumber();
    }
    @GetMapping("/getRQNumber")
    @ResponseBody
    fun getRQNumber(): String
    {
        return internationalStandardService.getRQNumber();
    }

}
