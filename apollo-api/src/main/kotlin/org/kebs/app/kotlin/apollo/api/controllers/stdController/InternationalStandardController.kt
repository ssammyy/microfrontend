package org.kebs.app.kotlin.apollo.api.controllers.stdController

import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.std.*
import org.kebs.app.kotlin.apollo.common.dto.std.*
import org.kebs.app.kotlin.apollo.store.model.std.*
import org.kebs.app.kotlin.apollo.store.repo.std.ISAdoptionJustificationRepository
import org.kebs.app.kotlin.apollo.store.repo.std.ISAdoptionProposalRepository
import org.kebs.app.kotlin.apollo.store.repo.std.ISStandardUploadsRepository
import org.kebs.app.kotlin.apollo.store.repo.std.ISUploadStandardRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
//@CrossOrigin(origins = ["http://localhost:4200"])
@RequestMapping("api/v1/migration/international_standard")
class InternationalStandardController(
    val internationalStandardService: InternationalStandardService,
    private val commonDaoServices: CommonDaoServices,
    private val isAdoptionProposalRepository: ISAdoptionProposalRepository,
    private val isAdoptionJustificationRepository: ISAdoptionJustificationRepository,
    private val isStandardUploadsRepository: ISStandardUploadsRepository,
    private val isUploadStandardRepository: ISUploadStandardRepository
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
    @PreAuthorize("hasAuthority('TC_SEC_SD_MODIFY')")
    @PostMapping("/prepareAdoptionProposal")
    @ResponseBody
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun prepareAdoptionProposal(@RequestBody iSAdoptionProposal: ISAdoptionProposal): ServerResponse{
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



    //********************************************************** get Stakeholders Tasks **********************************************************
    //@PreAuthorize("hasAuthority('STAKEHOLDERS_SD_READ')")
    @GetMapping("/getISProposals")
    fun getISProposals():List<TaskDetails>
    {
        return internationalStandardService.getISProposals()
    }
    //********************************************************** Submit Comments **********************************************************
    //@PreAuthorize("hasAuthority('STAKEHOLDERS_SD_MODIFY')")
    @PostMapping("/SubmitAPComments")
    @ResponseBody
    fun submitAPComments(@RequestBody isAdoptionComments: ISAdoptionComments): ServerResponse{
        return ServerResponse(HttpStatus.OK,"Comment Has been submitted",internationalStandardService.submitAPComments(isAdoptionComments))
    }
    //********************************************************** get TC SEC Tasks **********************************************************
    @PreAuthorize("hasAuthority('TC_SEC_SD_READ')")
    @GetMapping("/getTCSECTasks")
    fun getTCSECTasks():List<TaskDetails>
    {
        return internationalStandardService.getTCSECTasks()
    }

    //decision on Adoption Proposal
    @PreAuthorize("hasAuthority('TC_SEC_SD_MODIFY')")
    @PostMapping("/decisionOnProposal")
    fun decisionOnProposal(@RequestBody iSDecision: ISDecision) : List<TaskDetails>
    {
        return internationalStandardService.decisionOnProposal(iSDecision)
    }

    //********************************************************** get TC SEC Tasks **********************************************************
    @PreAuthorize("hasAuthority('TC_SEC_SD_READ')")
    @GetMapping("/getTCSeCTasks")
    fun getTCSeCTasks():List<TaskDetails>
    {
        return internationalStandardService.getTCSeCTasks()
    }
    //********************************************************** process upload Justification **********************************************************
    @PreAuthorize("hasAuthority('TC_SEC_SD_MODIFY')")
    @PostMapping("/prepareJustification")
    @ResponseBody
    fun prepareJustification(@RequestBody iSAdoptionJustification: ISAdoptionJustification): ServerResponse{
        return ServerResponse(HttpStatus.OK,"Successfully uploaded Justification",internationalStandardService.prepareJustification(iSAdoptionJustification))
    }
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

    //********************************************************** get SPC SEC Tasks **********************************************************
    @PreAuthorize("hasAuthority('SPC_SEC_SD_READ')")
    @GetMapping("/getSPCSECTasks")
    fun getSPCSECTasks():List<TaskDetails>
    {
        return internationalStandardService.getSPCSECTasks()
    }


    //decision
    @PreAuthorize("hasAuthority('SPC_SEC_SD_MODIFY')")
    @PostMapping("/decisionOnJustification")
    fun decisionOnJustification(@RequestBody isJustificationDecision: ISJustificationDecision) : List<TaskDetails>
    {
        return internationalStandardService.decisionOnJustification(isJustificationDecision)
    }

    //********************************************************** get SPC SEC Tasks **********************************************************
    @PreAuthorize("hasAuthority('SAC_SEC_SD_READ')")
    @GetMapping("/getSACSECTasks")
    fun getSACSECTasks():List<TaskDetails>
    {
        return internationalStandardService.getSACSECTasks()
    }

    //approve International Standard
    @PreAuthorize("hasAuthority('SAC_SEC_SD_MODIFY')")
    @PostMapping("/approveStandard")
    fun approveStandard(@RequestBody isJustificationDecision: ISJustificationDecision) : List<TaskDetails>
    {
        return internationalStandardService.approveStandard(isJustificationDecision)
    }

    //********************************************************** get Head of Publishing Tasks **********************************************************
    @PreAuthorize("hasAuthority('HOP_SD_READ')")
    @GetMapping("/getHOPTasks")
    fun getHOPTasks():List<TaskDetails>
    {
        return internationalStandardService.getHOPTasks()
    }

    //********************************************************** process upload Standard **********************************************************
    @PreAuthorize("hasAuthority('HOP_SD_MODIFY')")
    @PostMapping("/uploadISStandard")
    @ResponseBody
    fun uploadISStandard(@RequestBody iSUploadStandard: ISUploadStandard): ServerResponse
    {
        return ServerResponse(HttpStatus.OK,"Successfully uploaded Standard",internationalStandardService.uploadISStandard(iSUploadStandard))
    }
    @PostMapping("/std-file-upload")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun uploadISFiles(
        @RequestParam("isStandardID") isStandardID: Long,
        @RequestParam("docFile") docFile: List<MultipartFile>,
        model: Model
    ): CommonDaoServices.MessageSuccessFailDTO {

        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val isStandard = isUploadStandardRepository.findByIdOrNull(isStandardID)?: throw Exception("IS STANDARD DOCUMENT ID DOES NOT EXIST")

        docFile.forEach { u ->
            val upload = ISStandardUploads()
            with(upload) {
                isStdDocumentId = isStandard.id

            }
            internationalStandardService.uploadISFile(
                upload,
                u,
                "UPLOADS",
                loggedInUser,
                "IS STANDARD"
            )
        }

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.message = "Document Uploaded successfully"

        return sm
    }

    //********************************************************** get Head of HO SIC Tasks **********************************************************
    @PreAuthorize("hasAuthority('HO_SIC_SD_READ')")
    @GetMapping("/getHoSiCTasks")
    fun getHoSiCTasks():List<TaskDetails>
    {
        return internationalStandardService.getHoSiCTasks()
    }

    //********************************************************** process upload Gazette Notice **********************************************************
    @PreAuthorize("hasAuthority('HO_SIC_SD_MODIFY')")
    @PostMapping("/uploadGazetteNotice")
    @ResponseBody
    fun uploadGazetteNotice(@RequestBody iSGazetteNotice: ISGazetteNotice): ServerResponse
    {
        return ServerResponse(HttpStatus.OK,"Successfully uploaded Gazette Notice",internationalStandardService.uploadGazetteNotice(iSGazetteNotice))
    }

    //********************************************************** process upload Gazettement Date **********************************************************
    @PreAuthorize("hasAuthority('HO_SIC_SD_MODIFY')")
    @PostMapping("/updateGazettementDate")
    @ResponseBody
    fun updateGazettementDate(@RequestBody iSGazettement: ISGazettement): ServerResponse
    {
        return ServerResponse(HttpStatus.OK,"Successfully uploaded Gazette Notice",internationalStandardService.updateGazettementDate(iSGazettement))
    }
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
