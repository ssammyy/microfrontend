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

@RequestMapping("api/v1/migration")
class ComStandardController (val comStandardService: ComStandardService,
                             val standardRequestService: StandardRequestService,
                             private val commonDaoServices: CommonDaoServices,
                             private val comJcJustificationRepository: ComJcJustificationRepository,
                             private val comStdDraftRepository: ComStdDraftRepository,
                             private val companyStandardRepository: CompanyStandardRepository,
                             private val comStandardRequestUploadsRepository: ComStandardRequestUploadsRepository,
                             private val comStandardRequestRepository: ComStandardRequestRepository,
                             private val standardRepository: StandardRepository,

                             ) {

    //********************************************************** process upload standard request **********************************************************
    @PostMapping("/anonymous/company_standard/request")
    @ResponseBody
    fun requestForStandard(@RequestBody isCompanyStdRequestDto: ISCompanyStdRequestDto): ServerResponse{
        val companyStandardRequest= CompanyStandardRequest().apply {
            companyName=isCompanyStdRequestDto.companyName
            tcId=isCompanyStdRequestDto.tcId
            productId=isCompanyStdRequestDto.productId
            productSubCategoryId=isCompanyStdRequestDto.productSubCategoryId
            companyPhone=isCompanyStdRequestDto.companyPhone
            companyEmail=isCompanyStdRequestDto.companyEmail
            departmentId=isCompanyStdRequestDto.departmentId
            subject=isCompanyStdRequestDto.subject
            description=isCompanyStdRequestDto.description
            contactOneFullName=isCompanyStdRequestDto.contactOneFullName
            contactOneTelephone=isCompanyStdRequestDto.contactOneTelephone
            contactOneEmail=isCompanyStdRequestDto.contactOneEmail
            contactTwoFullName=isCompanyStdRequestDto.contactTwoFullName
            contactTwoTelephone=isCompanyStdRequestDto.contactTwoTelephone
            contactTwoEmail=isCompanyStdRequestDto.contactTwoEmail
            contactThreeFullName=isCompanyStdRequestDto.contactThreeFullName
            contactThreeTelephone=isCompanyStdRequestDto.contactThreeTelephone
            contactThreeEmail=isCompanyStdRequestDto.contactThreeEmail
        }
        return ServerResponse(HttpStatus.OK,"Successfully Submitted",comStandardService.requestForStandard(companyStandardRequest))
    }

    @PostMapping("/anonymous/company_standard/commitmentLetter")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun uploadCommitmentLetter(
        @RequestParam("comStdRequestID") comStdRequestID: Long,
        @RequestParam("docFile") docFile: List<MultipartFile>,
        model: Model
    ): CommonDaoServices.MessageSuccessFailDTO {


        val comStdRequest = comStandardRequestRepository.findByIdOrNull(comStdRequestID)?: throw Exception("REQUEST ID DOES NOT EXIST")

        docFile.forEach { u ->
            val upload = ComStandardRequestUploads()
            with(upload) {
                comStdRequestId = comStdRequest.id

            }
            comStandardService.uploadCommitmentLetter(
                upload,
                u,
                "UPLOADS",
                "Standard Request",
                "Commitment Letter"
            )
        }

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.message = "Document Uploaded successfully"

        return sm
    }

    //View Site Visit Report Document
    @GetMapping("/company_standard/view/commitmentLetter")
    fun viewPDFile(
        response: HttpServletResponse,
        @RequestParam("comStdRequestID") comStdRequestID: Long
    ) {

        val fileUploaded = comStandardService.findUploadedReportFileBYId(comStdRequestID)
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

    @PreAuthorize("hasAuthority('HOP_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/company_standard/getCompanyStandardRequest")
    @ResponseBody
    fun getCompanyStandardRequest(): MutableList<ComStdRequest>
    {
        return comStandardService.getCompanyStandardRequest()
    }


    @GetMapping("/company_standard/getCompanyStandardRequestProcess")
    @ResponseBody
    fun getCompanyStandardRequestProcess(): MutableList<ComStdRequest>
    {
        return comStandardService.getCompanyStandardRequestProcess()
    }



@GetMapping("/anonymous/company_standard/getDepartments")
@ResponseBody
    fun getDepartments(): MutableList<Department>
    {
        return standardRequestService.getDepartments()
    }



    @PreAuthorize("hasAuthority('HOP_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/company_standard/getAssignedCompanyStandardRequest")
    @ResponseBody
    fun getAssignedCompanyStandardRequest(): MutableList<ComStdRequest>
    {
        return comStandardService.getAssignedCompanyStandardRequest()
    }

    @PreAuthorize("hasAuthority('HOD_TWO_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/company_standard/assignRequest")
    @ResponseBody
    fun assignRequest(@RequestBody isCompanyStdRequestDto: ISCompanyStdRequestDto): ServerResponse{
        val companyStandardRequest= CompanyStandardRequest().apply {
            assignedTo=isCompanyStdRequestDto.assignedTo
            id=isCompanyStdRequestDto.requestId
        }
        return ServerResponse(HttpStatus.OK,"Successfully Assigned request to project leader",comStandardService.assignRequest(companyStandardRequest))
    }

    @PreAuthorize("hasAuthority('TC_SEC_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/company_standard/formJointCommittee")
    @ResponseBody
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun formJointCommittee(@RequestBody jointCommitteeDto: JointCommitteeDto): ServerResponse{
        val comStandardJointCommittee=ComStandardJointCommittee().apply {
            name= jointCommitteeDto.name?.let { commonDaoServices.convertClassToJson(it) }
            requestId=jointCommitteeDto.requestId
        }
        val detailBody = jointCommitteeDto.names


        return ServerResponse(HttpStatus.OK,"Successfully Submitted",comStandardService.formJointCommittee(comStandardJointCommittee,detailBody))
    }

    //********************************************************** process Assign Standard Request **********************************************************
    @PreAuthorize("hasAuthority('HOD_TWO_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/company_standard/submitJustificationForFormationOfTC")
    @ResponseBody
    fun submitJustificationForFormationOfTC(@RequestBody comTcJustificationDto: ComTcJustificationDto): ServerResponse{
        val justificationForTC= JustificationForTC().apply {
            proposer=comTcJustificationDto.proposer
            purpose=comTcJustificationDto.purpose
            subject=comTcJustificationDto.subject
            scope=comTcJustificationDto.scope
            targetDate=comTcJustificationDto.targetDate
            proposedRepresentation=comTcJustificationDto.proposedRepresentation
            programmeOfWork=comTcJustificationDto.programmeOfWork
            organization=comTcJustificationDto.organization
            liaisonOrganization=comTcJustificationDto.liaisonOrganization
            dateOfPresentation=comTcJustificationDto.dateOfPresentation
            nameOfTC=comTcJustificationDto.nameOfTC
            referenceNumber=comTcJustificationDto.referenceNumber
            comRequestId=comTcJustificationDto.comRequestId
        }
        return ServerResponse(HttpStatus.OK,"Successfully Submitted",comStandardService.submitJustificationForFormationOfTC(justificationForTC))
    }

    @PreAuthorize("hasAuthority('HOF_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/company_standard/getComTcJustification")
    @ResponseBody
    fun getComTcJustification(): MutableList<JustificationForTC>
    {
        return comStandardService.getComTcJustification()
    }

    //decision on Adoption Proposal
    @PreAuthorize("hasAuthority('HOF_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/company_standard/approveJustification")
    fun approveJustification(@RequestBody comJustificationDecisions: ComJustificationDecisions
    ) : ServerResponse
    {
        val justificationForTC= JustificationForTC().apply {
            accentTo=comJustificationDecisions.accentTo
            id=comJustificationDecisions.id
        }
        val companyStandardRemarks= CompanyStandardRemarks().apply {
            requestId=comJustificationDecisions.requestId
            remarks=comJustificationDecisions.comments
        }

        return ServerResponse(HttpStatus.OK,"Decision",comStandardService.approveJustification(justificationForTC,companyStandardRemarks))

    }

    @PreAuthorize("hasAuthority('SPC_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/company_standard/getComApprovedTcJustification")
    @ResponseBody
    fun getComApprovedTcJustification(): MutableList<JustificationForTC>
    {
        return comStandardService.getComApprovedTcJustification()
    }

    //decision on Adoption Proposal
    @PreAuthorize("hasAuthority('SPC_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/company_standard/approveSpcJustification")
    fun approveSpcJustification(@RequestBody comJustificationDecisions: ComJustificationDecisions
    ) : ServerResponse
    {
        val justificationForTC= JustificationForTC().apply {
            accentTo=comJustificationDecisions.accentTo
            id=comJustificationDecisions.id
        }
        val companyStandardRemarks= CompanyStandardRemarks().apply {
            requestId=comJustificationDecisions.requestId
            remarks=comJustificationDecisions.comments
        }

        return ServerResponse(HttpStatus.OK,"Decision",comStandardService.approveSpcJustification(justificationForTC,companyStandardRemarks))

    }

    @PreAuthorize("hasAuthority('SAC_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/company_standard/getApprovedSpcComTcJustification")
    @ResponseBody
    fun getApprovedSpcComTcJustification(): MutableList<JustificationForTC>
    {
        return comStandardService.getApprovedSpcComTcJustification()
    }

    //decision on Adoption Proposal
    @PreAuthorize("hasAuthority('SAC_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/company_standard/approveSacJustification")
    fun approveSacJustification(@RequestBody comJustificationDecisions: ComJustificationDecisions
    ) : ServerResponse
    {
        val justificationForTC= JustificationForTC().apply {
            accentTo=comJustificationDecisions.accentTo
            id=comJustificationDecisions.id
        }
        val companyStandardRemarks= CompanyStandardRemarks().apply {
            requestId=comJustificationDecisions.requestId
            remarks=comJustificationDecisions.comments
        }

        return ServerResponse(HttpStatus.OK,"Decision",comStandardService.approveSacJustification(justificationForTC,companyStandardRemarks))

    }

    @PreAuthorize("hasAuthority('PL_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/company_standard/getApprovedComTcJustification")
    @ResponseBody
    fun getApprovedComTcJustification(): MutableList<JustificationForTC>
    {
        return comStandardService.getApprovedComTcJustification()
    }


    @PostMapping("/company_standard/file-upload")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun uploadFiles(
        @RequestParam("comJustificationID") comJustificationID: Long,
        @RequestParam("docFile") docFile: List<MultipartFile>,
        model: Model
    ): CommonDaoServices.MessageSuccessFailDTO {

        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val comJustification = comJcJustificationRepository.findByIdOrNull(comJustificationID)?: throw Exception("JUSTIFICATION DOCUMENT ID DOES NOT EXIST")

        docFile.forEach { u ->
            val upload = ComJcJustificationUploads()
            with(upload) {
                comJCDocumentId = comJustification.id

            }
            comStandardService.uploadJCFile(
                upload,
                u,
                "UPLOADS",
                loggedInUser,
                "Joint Committee Justification"
            )
        }

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.message = "Document Uploaded successfully"

        return sm
    }




    //********************************************************** process Upload Company Draft **********************************************************
    @PreAuthorize("hasAuthority('PL_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/company_standard/uploadDraft")
    @ResponseBody
    fun uploadDraft(@RequestBody comStdDraftDto: ComStdDraftDto): ServerResponse{

        val comStdDraft= ComStdDraft().apply {
            title=comStdDraftDto.title
//            scope=comStdDraftDto.scope
//            normativeReference=comStdDraftDto.normativeReference
//            symbolsAbbreviatedTerms=comStdDraftDto.symbolsAbbreviatedTerms
//            clause=comStdDraftDto.clause
//            special=comStdDraftDto.special
            requestNumber=comStdDraftDto.requestNumber
            requestId=comStdDraftDto.requestId

            departmentId=comStdDraftDto.departmentId
            subject=comStdDraftDto.subject
            description=comStdDraftDto.description
            contactOneFullName=comStdDraftDto.contactOneFullName
            contactOneTelephone=comStdDraftDto.contactOneTelephone
            contactOneEmail=comStdDraftDto.contactOneEmail
            contactTwoFullName=comStdDraftDto.contactTwoFullName
            contactTwoTelephone=comStdDraftDto.contactTwoTelephone
            contactTwoEmail=comStdDraftDto.contactTwoEmail
            contactThreeFullName=comStdDraftDto.contactThreeFullName
            contactThreeTelephone=comStdDraftDto.contactThreeTelephone
            contactThreeEmail=comStdDraftDto.contactThreeEmail
            companyName=comStdDraftDto.companyName
            companyPhone=comStdDraftDto.companyPhone
        }


        //val gson = Gson()
        //KotlinLogging.logger { }.info { "WORKSHOP DRAFT" + gson.toJson(comStdDraftDto) }
        return ServerResponse(HttpStatus.OK,"Successfully uploaded Justification",comStandardService.uploadDraft(comStdDraft))
    }

    @PostMapping("/company_standard/draft-file-upload")
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
            comStandardService.uploadDrFile(
                upload,
                u,
                "UPLOADS",
                loggedInUser,
                "COMPANY STANDARD DRAFT"
            )
        }

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.message = "Document Uploaded successfully"

        return sm
    }

    //@PreAuthorize("hasAuthority('PL_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/anonymous/company_standard/getUploadedStdDraftForComment")
    @ResponseBody
    fun getUploadedStdDraftForComment(@RequestParam("comDraftID") comDraftID: Long): MutableList<ComStdDraft>
    {

        return comStandardService.getUploadedStdDraftForComment(comDraftID)
    }

    @GetMapping("/company_standard/getUploadedSDraftForComment")
    @ResponseBody
    fun getUploadedSDraftForComment(@RequestParam("comDraftID") comDraftID: Long): MutableList<ComStdDraft>
    {

        return comStandardService.getUploadedStdDraftForComment(comDraftID)
    }

    @PreAuthorize("hasAuthority('PL_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/company_standard/getUploadedStdDraft")
    @ResponseBody
    fun getUploadedStdDraft(): MutableList<ComStdDraft>
    {
        return comStandardService.getUploadedStdDraft()
    }

    @GetMapping("/company_standard/getDRNumber")
    @ResponseBody
    fun getDRNumber(): String
    {
        return comStandardService.getDRNumber();
    }

    @GetMapping("/company_standard/view/attached")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun downloadFileDocument(
        response: HttpServletResponse,
        @RequestParam("comDraftDocumentId") comDraftDocumentId: Long
    ) {
        val fileUploaded = comStandardService.findUploadedFileBYId(comDraftDocumentId)
        val mappedFileClass = commonDaoServices.mapClass(fileUploaded)
        commonDaoServices.downloadFile(response, mappedFileClass)
    }

    @GetMapping("/company_standard/viewCompanyDraft")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun viewCompanyDraft(
        response: HttpServletResponse,
        @RequestParam("comStdDraftID") comStdDraftID: Long
    ) {
        val fileUploaded = comStandardService.viewCompanyDraft(comStdDraftID)
        val mappedFileClass = commonDaoServices.mapClass(fileUploaded)
        commonDaoServices.downloadFile(response, mappedFileClass)
    }

    @GetMapping("/anonymous/company_standard/getDraftDocumentList")
    fun getDraftDocumentList(
        response: HttpServletResponse,
        @RequestParam("comStdDraftID") comStdDraftID: Long
    ): List<SiteVisitListHolder> {
        return comStandardService.getDraftDocumentList(comStdDraftID)
    }



    @GetMapping("/anonymous/company_standard/view/comDraft")
    fun viewCompanyDraftFile(
        response: HttpServletResponse,
        @RequestParam("comStdDraftID") comStdDraftID: Long
    ) {
        val fileUploaded = comStandardService.findUploadedCDRFileBYId(comStdDraftID)
        val fileDoc = commonDaoServices.mapClass(fileUploaded)
        response.contentType = "application/octet-stream"
//                    response.setHeader("Content-Length", pdfReportStream.size().toString())
        response.addHeader("Content-Disposition", "inline; filename=${fileDoc.name}")
        response.outputStream
            .let { responseOutputStream ->
                responseOutputStream.write(fileDoc.document?.let { makeAnyNotBeNull(it) } as ByteArray)
                responseOutputStream.close()
            }

        KotlinLogging.logger { }.info("VIEW FILE SUCCESSFUL")

    }


    @PreAuthorize("hasAuthority('JC_SEC_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/company_standard/commentOnDraft")
    fun commentOnDraft(@RequestBody comStdDraftDecisionDto: ComStdDraftDecisionDto
    ) : ServerResponse
    {

        val companyStandardRemarks= CompanyStandardRemarks().apply {
            requestId=comStdDraftDecisionDto.requestId
            remarks=comStdDraftDecisionDto.comments
        }

        return ServerResponse(HttpStatus.OK,"Decision",comStandardService.commentOnDraft(companyStandardRemarks))

    }


    @GetMapping("/company_standard/getAllComments")
    fun getAllComments(@RequestParam("requestId") requestId: Long):MutableIterable<CompanyStandardRemarks>?
    {
        return comStandardService.getAllComments(requestId)
    }

    @PostMapping("/company_standard/submitDraftComment")
    fun submitDraftComment(@RequestBody comDraftCommentDto: List<ComDraftCommentDto>
    ) : ServerResponse
    {

        return ServerResponse(HttpStatus.OK,"Comment Updated",comStandardService.submitDraftComments(comDraftCommentDto))

    }


    @PostMapping("/anonymous/company_standard/submitDraftComments")
    fun submitDraftComments(@RequestBody comDraftCommentDto: List<ComDraftCommentDto>
    ) : ServerResponse
    {

        return ServerResponse(HttpStatus.OK,"Comment Updated",comStandardService.submitDraftComments(comDraftCommentDto))

    }

    @GetMapping("/anonymous/company_standard/getDraftCommentList")
    fun getDraftCommentList(
        response: HttpServletResponse,
        @RequestParam("draftID") draftID: Long
    ): List<SiteVisitListHolder> {
        return comStandardService.getDraftCommentList(draftID)
    }

    @GetMapping("/anonymous/company_standard/getDraftComments")
    fun getDraftComments(@RequestParam("draftID") draftID: Long):MutableIterable<ComDraftComments>?
    {
        return comStandardService.getDraftComments(draftID)
    }

    //decision on Adoption Proposal
    @PreAuthorize("hasAuthority('JC_SEC_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/company_standard/decisionOnStdDraft")
    fun decisionOnStdDraft(@RequestBody comStdDraftDecisionDto: ComStdDraftDecisionDto
    ) : ServerResponse
    {
        val comStdDraft= ComStdDraft().apply {

            accentTo=comStdDraftDecisionDto.accentTo
            id= comStdDraftDecisionDto.id!!
            title=comStdDraftDecisionDto.title
            scope=comStdDraftDecisionDto.scope
            normativeReference=comStdDraftDecisionDto.normativeReference
            symbolsAbbreviatedTerms=comStdDraftDecisionDto.symbolsAbbreviatedTerms
            clause=comStdDraftDecisionDto.clause
            special=comStdDraftDecisionDto.special
            requestNumber=comStdDraftDecisionDto.requestNumber
            companyName=comStdDraftDecisionDto.companyName
            companyPhone=comStdDraftDecisionDto.companyPhone
            contactOneFullName=comStdDraftDecisionDto.contactOneFullName
            contactOneTelephone=comStdDraftDecisionDto.contactOneTelephone
            contactOneEmail=comStdDraftDecisionDto.contactOneEmail
        }
        val companyStandardRemarks= CompanyStandardRemarks().apply {
            requestId=comStdDraftDecisionDto.requestId
            remarks=comStdDraftDecisionDto.comments
        }

//        val gson = Gson()
//        KotlinLogging.logger { }.info { "WORKSHOP DRAFT" + gson.toJson(comStdDraftDecisionDto) }

        return ServerResponse(HttpStatus.OK,"Decision",comStandardService.decisionOnStdDraft(comStdDraft,companyStandardRemarks))

    }
    //*************************************************** process Edit Company Standard **********************************************************



    @GetMapping("/anonymous/company_standard/getApprovedStdDraft")
    @ResponseBody
    fun getApprovedStdDraft(@RequestParam("comDraftID") comDraftID: Long): MutableList<ComStdDraft>
    {

        return comStandardService.getApprovedStdDraft(comDraftID)
    }

    //decision on Adoption Proposal
   // @PreAuthorize("hasAuthority('JC_SEC_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/anonymous/company_standard/decisionOnComStdDraft")
    fun decisionOnComStdDraft(@RequestBody comStdDraftDecisionDto: ComStdDraftDecisionDto
    ) : ServerResponse
    {
        val comStdDraft= ComStdDraft().apply {
            accentTo=comStdDraftDecisionDto.accentTo
            id= comStdDraftDecisionDto.id!!
            title=comStdDraftDecisionDto.title
            scope=comStdDraftDecisionDto.scope
            normativeReference=comStdDraftDecisionDto.normativeReference
            symbolsAbbreviatedTerms=comStdDraftDecisionDto.symbolsAbbreviatedTerms
            clause=comStdDraftDecisionDto.clause
            special=comStdDraftDecisionDto.special
            requestNumber=comStdDraftDecisionDto.requestNumber
            companyName=comStdDraftDecisionDto.companyName
            companyPhone=comStdDraftDecisionDto.companyPhone
            contactOneFullName=comStdDraftDecisionDto.contactOneFullName
            contactOneEmail=comStdDraftDecisionDto.contactOneEmail
            contactOneTelephone=comStdDraftDecisionDto.contactOneTelephone
            departmentId=comStdDraftDecisionDto.departmentId
            subject=comStdDraftDecisionDto.subject
            description=comStdDraftDecisionDto.description
            contactTwoFullName=comStdDraftDecisionDto.contactTwoFullName
            contactTwoTelephone=comStdDraftDecisionDto.contactTwoTelephone
            contactTwoEmail=comStdDraftDecisionDto.contactTwoEmail
            contactThreeFullName=comStdDraftDecisionDto.contactThreeFullName
            contactThreeTelephone=comStdDraftDecisionDto.contactThreeTelephone
            contactThreeEmail=comStdDraftDecisionDto.contactThreeEmail
        }
        val companyStandardRemarks= CompanyStandardRemarks().apply {
            requestId=comStdDraftDecisionDto.requestId
            remarks=comStdDraftDecisionDto.comments
        }

        return ServerResponse(HttpStatus.OK,"Decision",comStandardService.decisionOnComStdDraft(comStdDraft,companyStandardRemarks))

    }

    @PreAuthorize("hasAuthority('EDITOR_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/company_standard/getStdDraftForEditing")
    @ResponseBody
    fun getStdDraftForEditing(): MutableList<ComStandard>
    {
        return comStandardService.getStdDraftForEditing()
    }

    @PreAuthorize("hasAuthority('EDITOR_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/company_standard/submitDraftForEditing")
    @ResponseBody
    fun submitDraftForEditing(@RequestBody isDraftDto: CSDraftDto): ServerResponse
    {
        val companyStandard= CompanyStandard().apply {
            id=isDraftDto.id
            requestNumber=isDraftDto.requestNumber
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
            contactOneFullName=isDraftDto.contactOneFullName
            contactOneTelephone=isDraftDto.contactOneTelephone
            contactOneEmail=isDraftDto.contactOneEmail
            contactTwoFullName=isDraftDto.contactTwoFullName
            contactTwoTelephone=isDraftDto.contactTwoTelephone
            contactTwoEmail=isDraftDto.contactTwoEmail
            contactThreeFullName=isDraftDto.contactThreeFullName
            contactThreeTelephone=isDraftDto.contactThreeTelephone
            contactThreeEmail=isDraftDto.contactThreeEmail
            companyName=isDraftDto.companyName
            companyPhone=isDraftDto.companyPhone
            status=1

        }

//        val gson = Gson()
//        KotlinLogging.logger { }.info { "Editing" + gson.toJson(isDraftDto) }

        return ServerResponse(HttpStatus.OK,"Successfully Edited Draft",comStandardService.submitDraftForEditing(companyStandard))
    }

    @PreAuthorize("hasAuthority('HOP_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/company_standard/getUploadedDraft")
    @ResponseBody
    fun getUploadedDraft(): MutableList<COMUploadedDraft>
    {
        return comStandardService.getUploadedDraft()
    }

    @PreAuthorize("hasAuthority('HOP_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/company_standard/getComStdPublishing")
    @ResponseBody
    fun getComStdPublishing(): MutableList<ComStandard>
    {
        return comStandardService.getComStdPublishing()
    }

    @PreAuthorize("hasAuthority('SAC_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/company_standard/getAppStdPublishing")
    @ResponseBody
    fun getAppStdPublishing(): MutableList<ComStandard>
    {
        return comStandardService.getAppStdPublishing()
    }

    @PreAuthorize("hasAuthority('HOP_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/company_standard/getAppStd")
    @ResponseBody
    fun getAppStd(): MutableList<ComStandard>
    {
        return comStandardService.getAppStd()
    }


    //decision on Adoption Proposal
    @PreAuthorize("hasAuthority('HOP_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/company_standard/checkRequirements")
    fun checkRequirements(@RequestBody iSDraftDecisions: ISDraftDecisions
    ) : ServerResponse
    {
        val companyStandard= CompanyStandard().apply {
            accentTo=iSDraftDecisions.accentTo
            draftId=iSDraftDecisions.draftId
            id=iSDraftDecisions.id
        }
        val companyStandardRemarks= CompanyStandardRemarks().apply {
            requestId=iSDraftDecisions.requestId
            remarks=iSDraftDecisions.comments
        }

        return ServerResponse(HttpStatus.OK,"Decision",comStandardService.checkRequirements(companyStandard,companyStandardRemarks))

    }

    @PreAuthorize("hasAuthority('EDITOR_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/company_standard/getApprovedDraft")
    @ResponseBody
    fun getApprovedEditedDraft(): MutableList<COMUploadedDraft>
    {
        return comStandardService.getApprovedEditedDraft()
    }

    @PreAuthorize("hasAuthority('EDITOR_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/company_standard/editStandardDraft")
    @ResponseBody
    fun editStandardDraft(@RequestBody isDraftDto: CSDraftDto): ServerResponse
    {
        val draught= isDraftDto.draughting

        var statusNm: Long? = 0

         statusNm = if (draught == "Yes") {
                    4
                } else {
                    5
                }

        val companyStandard= CompanyStandard().apply {
            draughting=isDraftDto.draughting
            id=isDraftDto.id
            requestNumber=isDraftDto.requestNumber
            comStdNumber=isDraftDto.comStdNumber
            title=isDraftDto.title
            scope=isDraftDto.scope
            normativeReference=isDraftDto.normativeReference
            symbolsAbbreviatedTerms=isDraftDto.symbolsAbbreviatedTerms
            clause=isDraftDto.clause
            preparedBy=isDraftDto.preparedBy
            documentType=isDraftDto.docName
            special=isDraftDto.special
            requestId=isDraftDto.requestId
            draftId=isDraftDto.draftId
            departmentId=isDraftDto.departmentId
            subject=isDraftDto.subject
            description=isDraftDto.description
            contactOneFullName=isDraftDto.contactOneFullName
            contactOneTelephone=isDraftDto.contactOneTelephone
            contactOneEmail=isDraftDto.contactOneEmail
            contactTwoFullName=isDraftDto.contactTwoFullName
            contactTwoTelephone=isDraftDto.contactTwoTelephone
            contactTwoEmail=isDraftDto.contactTwoEmail
            contactThreeFullName=isDraftDto.contactThreeFullName
            contactThreeTelephone=isDraftDto.contactThreeTelephone
            contactThreeEmail=isDraftDto.contactThreeEmail
            companyName=isDraftDto.companyName
            companyPhone=isDraftDto.companyPhone
            status=statusNm
            standardType=isDraftDto.standardType


        }

        return ServerResponse(HttpStatus.OK,"Successfully Edited Workshop Draft",comStandardService.editStandardDraft(companyStandard))
    }

    @PreAuthorize("hasAuthority('DRAUGHTSMAN_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/company_standard/getComEditedDraft")
    @ResponseBody
    fun getComEditedDraft(): MutableList<COMUploadedDraft>
    {
        return comStandardService.getComEditedDraft()
    }

    @PreAuthorize("hasAuthority('DRAUGHTSMAN_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/company_standard/draughtStandard")
    @ResponseBody
    fun draughtStandard(@RequestBody isDraftDto: CSDraftDto): ServerResponse
    {

        val companyStandard= CompanyStandard().apply {

            draughting="Yes"
            id=isDraftDto.id
            requestNumber=isDraftDto.requestNumber
            comStdNumber=isDraftDto.comStdNumber
            title=isDraftDto.title
            scope=isDraftDto.scope
            normativeReference=isDraftDto.normativeReference
            symbolsAbbreviatedTerms=isDraftDto.symbolsAbbreviatedTerms
            clause=isDraftDto.clause
            preparedBy=isDraftDto.preparedBy
            documentType=isDraftDto.docName
            special=isDraftDto.special
            requestId=isDraftDto.requestId
            draftId=isDraftDto.draftId
            departmentId=isDraftDto.departmentId
            subject=isDraftDto.subject
            description=isDraftDto.description
            contactOneFullName=isDraftDto.contactOneFullName
            contactOneTelephone=isDraftDto.contactOneTelephone
            contactOneEmail=isDraftDto.contactOneEmail
            contactTwoFullName=isDraftDto.contactTwoFullName
            contactTwoTelephone=isDraftDto.contactTwoTelephone
            contactTwoEmail=isDraftDto.contactTwoEmail
            contactThreeFullName=isDraftDto.contactThreeFullName
            contactThreeTelephone=isDraftDto.contactThreeTelephone
            contactThreeEmail=isDraftDto.contactThreeEmail
            companyName=isDraftDto.companyName
            companyPhone=isDraftDto.companyPhone
            status=5
        }

        return ServerResponse(HttpStatus.OK,"Successfully Draughted Workshop Draft",comStandardService.draughtStandard(companyStandard))
    }

    @PreAuthorize("hasAuthority('PROOFREADER_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/company_standard/getDraughtedDraft")
    @ResponseBody
    fun getDraughtedDraft(): MutableList<COMUploadedDraft>
    {
        return comStandardService.getDraughtedDraft()
    }

    @PreAuthorize("hasAuthority('PROOFREADER_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/company_standard/proofReadStandard")
    @ResponseBody
    fun proofReadStandard(@RequestBody isDraftDto: CSDraftDto): ServerResponse
    {

        val companyStandard= CompanyStandard().apply {
            draughting=isDraftDto.draughting
            id=isDraftDto.id
            requestNumber=isDraftDto.requestNumber
            comStdNumber=isDraftDto.comStdNumber
            documentType=isDraftDto.docName
            requestId=isDraftDto.requestId
            draftId=isDraftDto.draftId
            departmentId=isDraftDto.departmentId
            subject=isDraftDto.subject
            description=isDraftDto.description
            contactOneFullName=isDraftDto.contactOneFullName
            contactOneTelephone=isDraftDto.contactOneTelephone
            contactOneEmail=isDraftDto.contactOneEmail
            contactTwoFullName=isDraftDto.contactTwoFullName
            contactTwoTelephone=isDraftDto.contactTwoTelephone
            contactTwoEmail=isDraftDto.contactTwoEmail
            contactThreeFullName=isDraftDto.contactThreeFullName
            contactThreeTelephone=isDraftDto.contactThreeTelephone
            contactThreeEmail=isDraftDto.contactThreeEmail
            companyName=isDraftDto.companyName
            companyPhone=isDraftDto.companyPhone
            status=6

        }
        return ServerResponse(HttpStatus.OK,"Successfully Proof Read Workshop Draft",comStandardService.proofReadStandard(companyStandard))
    }

    @PreAuthorize("hasAuthority('HOP_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/company_standard/getProofReadDraft")
    @ResponseBody
    fun getProofReadDraft(): MutableList<COMUploadedDraft>
    {
        return comStandardService.getProofReadDraft()
    }

    @PreAuthorize("hasAuthority('HOP_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/company_standard/approveProofReadStandard")
    fun approveProofReadStandard(@RequestBody iSDraftDecisions: ISDraftDecisions
    ) : ServerResponse
    {
        val companyStandard= CompanyStandard().apply {
            accentTo=iSDraftDecisions.accentTo
            id=iSDraftDecisions.id
            draftId=iSDraftDecisions.draftId
        }
        val companyStandardRemarks= CompanyStandardRemarks().apply {
            requestId=iSDraftDecisions.requestId
            remarks=iSDraftDecisions.comments
        }

//        val gson = Gson()
//        KotlinLogging.logger { }.info { "Status Check" + gson.toJson(iSDraftDecisions) }
        return ServerResponse(HttpStatus.OK,"Decision",comStandardService.approveProofReadStandard(companyStandard,companyStandardRemarks))

    }

    @PreAuthorize("hasAuthority('EDITOR_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/company_standard/getApprovedProofReadDraft")
    @ResponseBody
    fun getApprovedProofReadDraft(): MutableList<COMUploadedDraft>
    {
        return comStandardService.getApprovedProofReadDraft()
    }

    //decision on Adoption Proposal
    @PreAuthorize("hasAuthority('EDITOR_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/company_standard/approveEditedStandard")
    fun approveEditedStandard(@RequestBody iSDraftDecisions: ISDraftDecisions
    ) : ServerResponse
    {
        val companyStandard= CompanyStandard().apply {
            accentTo=iSDraftDecisions.accentTo
            id=iSDraftDecisions.id
            draftId=iSDraftDecisions.draftId

        }
        val companyStandardRemarks= CompanyStandardRemarks().apply {
            requestId=iSDraftDecisions.requestId
            remarks=iSDraftDecisions.comments
        }

               // val gson = Gson()
       // KotlinLogging.logger { }.info { "Status Check" + gson.toJson(iSDraftDecisions) }


        return ServerResponse(HttpStatus.OK,"Decision",comStandardService.approveEditedStandard(companyStandard,companyStandardRemarks))

    }

    @PreAuthorize("hasAuthority('SAC_SEC_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/company_standard/getApprovedCompanyStdDraft")
    @ResponseBody
    fun getApprovedCompanyStdDraft(): MutableList<COMUploadedDraft>
    {
        return comStandardService.getApprovedCompanyStdDraft()
    }

    //SAC Decision
    @PreAuthorize("hasAuthority('SAC_SEC_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/company_standard/rejectCompanyStandard")
    fun rejectCompanyStandard(@RequestBody iSDraftDecisions: ISDraftDecisionsStd
    ) : ServerResponse
    {
        val companyStandard= CompanyStandard().apply {
            accentTo=iSDraftDecisions.accentTo
            id=iSDraftDecisions.id
            draftId=iSDraftDecisions.draftId

        }
        val companyStandardRemarks= CompanyStandardRemarks().apply {
            requestId=iSDraftDecisions.requestId
            remarks=iSDraftDecisions.comments
        }


        return ServerResponse(HttpStatus.OK,"Decision",comStandardService.rejectCompanyStandard(companyStandard,companyStandardRemarks))

    }


    @PreAuthorize("hasAuthority('SAC_SEC_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/company_standard/approveCompanyStandard")
    fun approveCompanyStandard(@RequestBody iSDraftDecisions: ISDraftDecisionsStd
    ) : ServerResponse
    {
        val companyStandard= CompanyStandard().apply {
            accentTo=iSDraftDecisions.accentTo
            id=iSDraftDecisions.id
            draftId=iSDraftDecisions.draftId

        }
        val companyStandardRemarks= CompanyStandardRemarks().apply {
            requestId=iSDraftDecisions.requestId
            remarks=iSDraftDecisions.comments
        }

        val standard= Standard().apply {
            title=iSDraftDecisions.title
            standardNumber=iSDraftDecisions.standardNumber
            comStdId=iSDraftDecisions.id
        }



        return ServerResponse(HttpStatus.OK,"Decision",comStandardService.approveCompanyStandard(companyStandard,companyStandardRemarks,standard))

    }

    //Upload Standard
    @PostMapping("/company_standard/uploadStandardDoc")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun uploadStandardDoc(
        @RequestParam("standardID") standardID: Long,
        @RequestParam("docFile") docFile: List<MultipartFile>,
        model: Model
    ): CommonDaoServices.MessageSuccessFailDTO {

        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val standard = standardRepository.findByIdOrNull(standardID)?: throw Exception("STANDARD DOCUMENT ID DOES NOT EXIST")

        docFile.forEach { u ->
            val upload = DatKebsSdStandardsEntity()
            with(upload) {
                sdDocumentId = standard.id

            }
            comStandardService.uploadCompanyStandardUpload(
                upload,
                u,
                "COMPANY STANDARD",
                loggedInUser,
                "COMPANY STANDARD"
            )
        }

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.message = "Document Uploaded successfully"

        return sm
    }




    @GetMapping("/company_standard/getCSNumber")
    @ResponseBody
    fun getCSNumber(): String
    {
        return comStandardService.getCSNumber();
    }
    // upload Workshop draft
    @PostMapping("/company_standard/std-file-upload")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun uploadStandardFiles(
        @RequestParam("comStandardID") comStandardID: Long,
        @RequestParam("docFile") docFile: List<MultipartFile>,
        model: Model
    ): CommonDaoServices.MessageSuccessFailDTO {

        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val compStandard = companyStandardRepository.findByIdOrNull(comStandardID)?: throw Exception("STANDARD DOCUMENT ID DOES NOT EXIST")

        docFile.forEach { u ->
            val upload = ComStandardUploads()
            with(upload) {
                comStdDocumentId = compStandard.id

            }
            comStandardService.uploadSTDFile(
                upload,
                u,
                "UPLOADS",
                loggedInUser,
                "COMPANY STANDARD"
            )
        }

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.message = "Document Uploaded successfully"

        return sm
    }





    // View Standard
    @GetMapping("/company_standard/view/comStandard")
    fun viewStandardFile(
        response: HttpServletResponse,
        @RequestParam("comStandardID") comStandardID: Long
    ) {
        val fileUploaded = comStandardService.findUploadedSTDFileBYId(comStandardID)
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



    @PostMapping("/company_standard/std-efile-upload")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun editSTDFile(
        @RequestParam("comStandardID") comStandardID: Long,
        @RequestParam("docFile") docFile: List<MultipartFile>,
        model: Model
    ): CommonDaoServices.MessageSuccessFailDTO {

        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val compStandard = companyStandardRepository.findByIdOrNull(comStandardID)?: throw Exception("STANDARD DOCUMENT ID DOES NOT EXIST")

        docFile.forEach { u ->
            val upload = ComStandardUploads()
            with(upload) {
                comStdDocumentId = compStandard.id

            }
            comStandardService.uploadSTDFile(
                upload,
                u,
                "UPLOADS",
                loggedInUser,
                "COMPANY STANDARD"
            )
        }

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.message = "Document Uploaded successfully"

        return sm
    }

    @GetMapping("/company_standard/process/{processId}")
    fun checkState(@PathVariable("processId") processId: String?) {
        comStandardService.checkProcessHistory(processId)
    }

    @GetMapping("/company_standard/getRQNumber")
    @ResponseBody
    fun getRQNumber(): String
    {
        return comStandardService.getRQNumber()
    }

    @GetMapping("/company_standard/getUsers")
    @ResponseBody
    fun getUsers(): MutableList<UserHolder> {
        return comStandardService.getUsers()
    }

    // View Standard
    @GetMapping("/anonymous/company_standard/viewStandard")
    fun viewStandard(
        response: HttpServletResponse,
        @RequestParam("standardId") standardId: Long
    ) {
        val fileUploaded = comStandardService.findUploadedStandard(standardId)
        val fileDoc = commonDaoServices.mapClass(fileUploaded)
        response.contentType = "application/pdf"
        response.addHeader("Content-Disposition", "inline; filename=${fileDoc.name}")
        response.outputStream
            .let { responseOutputStream ->
                responseOutputStream.write(fileDoc.document?.let { makeAnyNotBeNull(it) } as ByteArray)
                responseOutputStream.close()
            }

        KotlinLogging.logger { }.info("VIEW FILE SUCCESSFUL")

    }

}
