package org.kebs.app.kotlin.apollo.api.controllers.stdController

import com.google.gson.Gson
import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.std.*
import org.kebs.app.kotlin.apollo.api.ports.provided.makeAnyNotBeNull
import org.kebs.app.kotlin.apollo.common.dto.std.*
import org.kebs.app.kotlin.apollo.store.model.StandardLevySiteVisitRemarks
import org.kebs.app.kotlin.apollo.store.model.UsersEntity
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

@RequestMapping("api/v1/migration/try")
class CompanyStandardController(val comStandardService: CompanyStandardService,
                                val standardRequestService: StandardRequestService,
                                private val commonDaoServices: CommonDaoServices,
                                private val comJcJustificationRepository: ComJcJustificationRepository,
                                private val comStdDraftRepository: ComStdDraftRepository,
                                private val companyStandardRepository: CompanyStandardRepository

) {
    @PostMapping("/company_standard/deploy")
    fun deployWorkflow(): ServerResponse {
        comStandardService.deployProcessDefinition()
        return ServerResponse(HttpStatus.OK,"Successfully deployed server", HttpStatus.OK)
    }

    @PostMapping("/anonymous/company_standard/request")
    @ResponseBody
    fun requestForStandard(@RequestBody companyStandardRequest: CompanyStandardRequest): ServerResponse{
        return ServerResponse(HttpStatus.OK,"Successfully uploaded standard request",comStandardService.requestForStandard(companyStandardRequest))
    }

    @GetMapping("/anonymous/company_standard/getDepartments")
    @ResponseBody
    fun getDepartments(): MutableList<Department>
    {
        return standardRequestService.getDepartments()
    }

    @PreAuthorize("hasAuthority('HOD_TWO_SD_READ') or hasAuthority('PL_SD_READ')" +
            " or hasAuthority('SPC_SEC_SD_READ') or hasAuthority('JC_SEC_SD_READ') " +
            " or hasAuthority('COM_SEC_SD_READ') or hasAuthority('HOP_SD_READ') or" +
            " hasAuthority('SAC_SEC_SD_READ')  or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')  ")
    @GetMapping("/company_standard/getUserTasks")
    fun getUserTasks():List<StdUserTasks>
    {
        return comStandardService.getUserTasks()
    }

    //********************************************************** process Assign Standard Request **********************************************************
    @PreAuthorize("hasAuthority('HOD_TWO_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/company_standard/assignRequest")
    @ResponseBody
    fun assignRequest(@RequestBody comStdAction: ComStdAction): ServerResponse{
        return ServerResponse(HttpStatus.OK,"Successfully Assigned request to project leader",comStandardService.assignRequest(comStdAction))
    }

    //********************************************************** process Form Joint Committee **********************************************************
    @PreAuthorize("hasAuthority('PL_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/company_standard/formJointCommittee")
    @ResponseBody
    fun formJointCommittee(@RequestBody comStandardJC: ComStandardJC,user: UsersEntity): ServerResponse{
               val gson = Gson()
       KotlinLogging.logger { }.info { "INVOICE CALCULATED" + gson.toJson(comStandardJC) }
        return ServerResponse(HttpStatus.OK,"Successfully Formed Joint Committee",comStandardService.formJointCommittee(comStandardJC,user))
    }

    //********************************************************** process Upload Company Draft **********************************************************
    @PreAuthorize("hasAuthority('PL_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/company_standard/uploadDraft")
    @ResponseBody
    fun uploadDraft(@RequestBody comStdDraft: ComStdDraft): ServerResponse{

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
    @GetMapping("/company_standard/view/comDraft")
    fun viewCompanyDraftFile(
        response: HttpServletResponse,
        @RequestParam("comStdDraftID") comStdDraftID: Long
    ) {
        val fileUploaded = comStandardService.findUploadedCDRFileBYId(comStdDraftID)
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

    //Decision on Company Draft
    @PreAuthorize("hasAuthority('JC_SEC_SD_MODIFY') or hasAuthority('PL_SD_MODIFY')  or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/company_standard/decisionOnCompanyStdDraft")
    fun decisionOnCompanyStdDraft(@RequestBody comDraftDecision: ComDraftDecision,standardComRemarks: StandardComRemarks) : List<StdUserTasks>
    {
        return comStandardService.decisionOnCompanyStdDraft(comDraftDecision,standardComRemarks)
    }

    //Decision on Company Draft COM SEC
    @PreAuthorize("hasAuthority('PL_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/company_standard/decisionOnComStdDraft")
    fun decisionOnComStdDraft(@RequestBody comDraftDecision: ComDraftDecision,standardComRemarks: StandardComRemarks) : List<StdUserTasks>
    {
        return comStandardService.decisionOnCompanyStd(comDraftDecision,standardComRemarks)
    }



    //********************************************************** process Upload Company Standard **********************************************************
    @PreAuthorize("hasAuthority('PL_SD_MODIFY') or hasAuthority('HOP_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/company_standard/uploadComStandard")
    @ResponseBody
    fun uploadComStandard(@RequestBody companyStandard: CompanyStandard,standard: Standard): ServerResponse
    {
        return ServerResponse(HttpStatus.OK,"Successfully uploaded Standard",comStandardService.uploadComStandard(companyStandard,standard))
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
    @GetMapping("/anonymous/company_standard/view/comStandard")
    fun viewStandardFile(
        response: HttpServletResponse,
        @RequestParam("comStdDocumentId") comStdDocumentId: Long
    ) {
        val fileUploaded = comStandardService.findUploadedSTDFileBYId(comStdDocumentId)
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

    @GetMapping("/company_standard/getRQNumber")
    @ResponseBody
    fun getRQNumber(): String
    {
        return comStandardService.getRQNumber()
    }

    @GetMapping("/company_standard/getUserList")
    @ResponseBody
    fun getUserList(): MutableList<UsersEntity> {
        return comStandardService.getUserList()
    }

    @GetMapping("/company_standard/getComStandardRemarks")
    fun getComStandardRemarks(
        response: HttpServletResponse,
        @RequestParam("approvalID") approvalID: Long
    ): List<StandardComRemarks> {
        return comStandardService.getComStandardRemarks(approvalID)
    }

    @GetMapping("/anonymous/company_standard/getAllStandards")
    @ResponseBody
    fun getAllStandards(): MutableList<CompanyStandard>
    {
        return comStandardService.getAllStandards()
    }


}