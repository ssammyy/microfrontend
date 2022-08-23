package org.kebs.app.kotlin.apollo.api.controllers.stdController

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

    //@PreAuthorize("hasAuthority('STAKEHOLDERS_SD_READ')")
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
    fun submitAPComments(@RequestBody isAdoptionComments: ISAdoptionComments): ServerResponse{
        return ServerResponse(HttpStatus.OK,"Comment Has been submitted",internationalStandardService.submitAPComments(isAdoptionComments))
    }

    @GetMapping("/getAllComments")
    fun getAllComments(@RequestParam("proposalId") proposalId: Long):MutableIterable<ISAdoptionComments>
    {
        return internationalStandardService.getAllComments(proposalId)
    }


    @PreAuthorize("hasAuthority('TC_SEC_SD_READ') or hasAuthority('SPC_SEC_SD_READ')" +
            " or hasAuthority('SAC_SEC_SD_READ') or hasAuthority('HOP_SD_READ') " +
            " or hasAuthority('HO_SIC_SD_READ')  or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')  ")
    @GetMapping("/getUserTasks")
    fun getUserTasks():List<InternationalStandardTasks>
    {
        return internationalStandardService.getUserTasks()
    }


    //decision on Adoption Proposal
    @PreAuthorize("hasAuthority('TC_SEC_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/decisionOnProposal")
    fun decisionOnProposal(@RequestBody iSDecision: ISDecision,internationalStandardRemarks: InternationalStandardRemarks) : List<InternationalStandardTasks>
    {
        return internationalStandardService.decisionOnProposal(iSDecision,internationalStandardRemarks)
    }

    //********************************************************** process upload Justification **********************************************************
    @PreAuthorize("hasAuthority('TC_SEC_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/prepareJustification")
    @ResponseBody
    fun prepareJustification(@RequestBody iSAdoptionJustification: ISAdoptionJustification): ServerResponse{
        return ServerResponse(HttpStatus.OK,"Successfully uploaded Justification",internationalStandardService.prepareJustification(iSAdoptionJustification))
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

    //decision
    @PreAuthorize("hasAuthority('SPC_SEC_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/decisionOnJustification")
    fun decisionOnJustification(@RequestBody isJustificationDecision: ISJustificationDecision,internationalStandardRemarks: InternationalStandardRemarks) : List<InternationalStandardTasks>
    {
        return internationalStandardService.decisionOnJustification(isJustificationDecision,internationalStandardRemarks)
    }



    //approve International Standard
    @PreAuthorize("hasAuthority('SAC_SEC_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/justificationDecision")
    fun justificationDecision(@RequestBody isJustificationDecision: ISJustificationDecision,internationalStandardRemarks: InternationalStandardRemarks) : List<InternationalStandardTasks>
    {
        return internationalStandardService.justificationDecision(isJustificationDecision,internationalStandardRemarks)
    }



    //********************************************************** process upload Standard **********************************************************
    @PreAuthorize("hasAuthority('HOP_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
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
            internationalStandardService.uploadISDFile(
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


    //view IS Justification Document
    @GetMapping("/view/iStandard")
    fun viewStandardFile(
        response: HttpServletResponse,
        @RequestParam("isStdDocumentId") isStdDocumentId: Long
    ) {
        val fileUploaded = internationalStandardService.findUploadedSTFileBYId(isStdDocumentId)
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

    //********************************************************** process upload Gazette Notice **********************************************************
    @PreAuthorize("hasAuthority('HO_SIC_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/uploadGazetteNotice")
    @ResponseBody
    fun uploadGazetteNotice(@RequestBody iSGazetteNotice: ISGazetteNotice): ServerResponse
    {
        return ServerResponse(HttpStatus.OK,"Successfully uploaded Gazette Notice",internationalStandardService.uploadGazetteNotice(iSGazetteNotice))
    }

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
    @PreAuthorize("hasAuthority('HO_SIC_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
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
