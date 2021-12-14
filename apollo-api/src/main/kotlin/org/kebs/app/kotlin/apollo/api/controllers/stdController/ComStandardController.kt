package org.kebs.app.kotlin.apollo.api.controllers.stdController

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.std.*
import org.kebs.app.kotlin.apollo.api.ports.provided.makeAnyNotBeNull
import org.kebs.app.kotlin.apollo.common.dto.std.*
import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import org.kebs.app.kotlin.apollo.store.model.std.*
import org.kebs.app.kotlin.apollo.store.repo.std.ComJcJustificationRepository
import org.kebs.app.kotlin.apollo.store.repo.std.ComStdDraftRepository
import org.kebs.app.kotlin.apollo.store.repo.std.CompanyStandardRepository
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
                             private val companyStandardRepository: CompanyStandardRepository

                             ) {

    //********************************************************** deployment endpoints **********************************************************
    @PostMapping("/company_standard/deploy")
    fun deployWorkflow(): ServerResponse {
        comStandardService.deployProcessDefinition()
        return ServerResponse(HttpStatus.OK,"Successfully deployed server", HttpStatus.OK)
    }
//    ********************************************************** deployment endpoints **********************************************************
//    @PostMapping("/startProcessInstance")
//    fun startProcess(): ServerResponse {
//        comStandardService.startProcessInstance()
//        return ServerResponse(HttpStatus.OK,"Successfully Started server", HttpStatus.OK)
//    }

    //********************************************************** process upload standard request **********************************************************
    @PostMapping("/anonymous/company_standard/request")
    @ResponseBody
    fun requestForStandard(@RequestBody companyStandardRequest: CompanyStandardRequest): ServerResponse{
        return ServerResponse(HttpStatus.OK,"Successfully uploaded standard request",comStandardService.requestForStandard(companyStandardRequest))
    }

//    @GetMapping("/getProducts")
//    @ResponseBody
//    fun getProducts(): MutableList<Product>
//    {
//        return standardRequestService.getProducts()
//    }
//    @GetMapping("/getProductCategories/{productId}")
//    @ResponseBody
//    fun getProductCategories(@PathVariable("productId") productId: String?): MutableList<ProductSubCategory>
//    {
//        return standardRequestService.getProductCategories(productId)
//    }
@GetMapping("/anonymous/company_standard/getDepartments")
@ResponseBody
    fun getDepartments(): MutableList<Department>
    {
        return standardRequestService.getDepartments()
    }

    //********************************************************** get HOD Tasks **********************************************************
    @PreAuthorize("hasAuthority('HOD_TWO_SD_READ')")
    @GetMapping("/company_standard/getHODTasks")
    fun getHODTasks():List<TaskDetails>
    {
        return comStandardService.getHODTasks()
    }

    //********************************************************** process Assign Standard Request **********************************************************
    @PreAuthorize("hasAuthority('HOD_TWO_SD_MODIFY')")
    @PostMapping("/company_standard/assignRequest")
    @ResponseBody
    fun assignRequest(@RequestBody comStdAction: ComStdAction): ServerResponse{
        return ServerResponse(HttpStatus.OK,"Successfully Assigned request to project leader",comStandardService.assignRequest(comStdAction))
    }

    //********************************************************** get Project Leader Tasks **********************************************************
    @PreAuthorize("hasAuthority('PL_SD_READ')")
    @GetMapping("/company_standard/getPlTasks")
    fun getPlTasks():List<TaskDetails>
    {
        return comStandardService.getPlTasks()
    }

    //********************************************************** process Form Joint Committee **********************************************************
    @PreAuthorize("hasAuthority('PL_SD_MODIFY')")
    @PostMapping("/company_standard/formJointCommittee")
    @ResponseBody
    fun formJointCommittee(@RequestBody comStandardJC: ComStandardJC): ServerResponse{
        return ServerResponse(HttpStatus.OK,"Successfully Formed Joint Committee",comStandardService.formJointCommittee(comStandardJC))
    }

    //********************************************************** process upload Justification **********************************************************
    @PreAuthorize("hasAuthority('PL_SD_MODIFY')")
    @PostMapping("/company_standard/prepareJustification")
    @ResponseBody
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun prepareJustification(@RequestBody comJcJustification: ComJcJustification): ServerResponse{
        return ServerResponse(HttpStatus.OK,"Successfully uploaded Justification",comStandardService.prepareJustification(comJcJustification))
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

    //********************************************************** get SPC SEC Tasks **********************************************************
    @PreAuthorize("hasAuthority('SPC_SEC_SD_READ')")
    @GetMapping("/company_standard/getSpcSecTasks")
    fun getSpcSecTasks():List<TaskDetails>
    {
        return comStandardService.getSpcSecTasks()
    }

    //decision
    @PreAuthorize("hasAuthority('SPC_SEC_SD_MODIFY')")
    @PostMapping("/company_standard/decisionOnJustification")
    fun decisionOnJustification(@RequestBody comJustificationDecision: ComJustificationDecision) : List<TaskDetails>
    {
        return comStandardService.decisionOnJustification(comJustificationDecision)
    }



    //approve Justification List
    @PreAuthorize("hasAuthority('SAC_SEC_SD_MODIFY')")
    @PostMapping("/company_standard/approveJustification")
    fun approveJustification(@RequestBody comJustificationDecision: ComJustificationDecision) : List<TaskDetails>
    {
        return comStandardService.approveJustification(comJustificationDecision)
    }

    //********************************************************** process Upload Company Draft **********************************************************
    @PreAuthorize("hasAuthority('PL_SD_MODIFY')")
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
    //********************************************************** get JC SEC Tasks **********************************************************
    @PreAuthorize("hasAuthority('JC_SEC_SD_READ')")
    @GetMapping("/company_standard/getJcSecTasks")
    fun getJcSecTasks():List<TaskDetails>
    {
        return comStandardService.getJcSecTasks()
    }

    @GetMapping("/view/comDraft")
    fun viewComDraftFile(
        response: HttpServletResponse,
        @RequestParam("comDraftDocumentId") comDraftDocumentId: Long
    ) {
        val fileUploaded = comStandardService.findUploadedCDRFileBYId(comDraftDocumentId)
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
    @PreAuthorize("hasAuthority('JC_SEC_SD_MODIFY')")
    @PostMapping("/company_standard/decisionOnCompanyStdDraft")
    fun decisionOnCompanyStdDraft(@RequestBody comDraftDecision: ComDraftDecision) : List<TaskDetails>
    {
        return comStandardService.decisionOnCompanyStdDraft(comDraftDecision)
    }

    //********************************************************** get COM SEC Tasks **********************************************************
    @PreAuthorize("hasAuthority('COM_SEC_SD_READ')")
    @GetMapping("/company_standard/getComSecTasks")
    fun getComSecTasks():List<TaskDetails>
    {
        return comStandardService.getComSecTasks()
    }
    //********************************************************** process Edit Company Standard **********************************************************

    //Decision on Company Draft COM SEC
    @PreAuthorize("hasAuthority('COM_SEC_SD_MODIFY')")
    @PostMapping("/company_standard/decisionOnComStdDraft")
    fun decisionOnComStdDraft(@RequestBody comDraftDecision: ComDraftDecision) : List<TaskDetails>
    {
        return comStandardService.decisionOnCompanyStdDraft(comDraftDecision)
    }

    //********************************************************** get HOP Tasks **********************************************************
    @PreAuthorize("hasAuthority('HOP_SD_READ')")
    @GetMapping("/company_standard/getHopTasks")
    fun getHopTasks():List<TaskDetails>
    {
        return comStandardService.getHopTasks()
    }

    //********************************************************** process Upload Company Standard **********************************************************
    @PreAuthorize("hasAuthority('HOP_SD_MODIFY')")
    @PostMapping("/company_standard/uploadComStandard")
    @ResponseBody
    fun uploadComStandard(@RequestBody companyStandard: CompanyStandard): ServerResponse
    {
        return ServerResponse(HttpStatus.OK,"Successfully uploaded Standard",comStandardService.uploadComStandard(companyStandard))
    }

    @GetMapping("/company_standard/getCSNumber")
    @ResponseBody
    fun getCSNumber(): String
    {
        return comStandardService.getCSNumber();
    }

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

    //********************************************************** get SAC SEC Tasks **********************************************************
    @PreAuthorize("hasAuthority('SAC_SEC_SD_READ')")
    @GetMapping("/company_standard/getSacSecTasks")
    fun getSacSecTasks():List<TaskDetails>
    {
        return comStandardService.getSacSecTasks()
    }

    // View Standard
    @GetMapping("/view/comStandard")
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

    //********************************************************** process Upload Company Standard **********************************************************
    @PreAuthorize("hasAuthority('HOP_SD_MODIFY')")
    @PostMapping("/company_standard/editCompanyStandard")
    @ResponseBody
    fun editCompanyStandard(@RequestBody editCompanyStandard: EditCompanyStandard): ServerResponse
    {
        return ServerResponse(HttpStatus.OK,"Successfully Edited Standard",comStandardService.editCompanyStandard(editCompanyStandard))
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

    @GetMapping("/company_standard/getUserList")
    @ResponseBody
    fun getUserList(): MutableList<UsersEntity> {
        return comStandardService.getUserList()
    }

}
