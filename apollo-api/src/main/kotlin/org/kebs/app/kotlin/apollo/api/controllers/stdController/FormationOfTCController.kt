package org.kebs.app.kotlin.apollo.api.controllers.stdController

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.std.*
import org.kebs.app.kotlin.apollo.api.ports.provided.makeAnyNotBeNull
import org.kebs.app.kotlin.apollo.common.dto.std.*
import org.kebs.app.kotlin.apollo.store.model.std.*
import org.kebs.app.kotlin.apollo.store.repo.std.JustificationForTCRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.util.stream.Collectors
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("api/v1/migration/formationOfTC")
class FormationOfTCController(
    val formationOfTCService: FormationOfTCService,
    val draftDocumentService: DraftDocumentService,
    val commonDaoServices: CommonDaoServices,

    private val justificationForTCRepository: JustificationForTCRepository,

    ) {

    //********************************************************** deployment endpoints ********************************************//
    @PostMapping("/deploy")
    fun deployWorkflow(): ServerResponse {
        formationOfTCService.deployProcessDefinition()
        return ServerResponse(HttpStatus.OK, "Successfully deployed server", HttpStatus.OK)
    }

    //***************************************************** submit justification for formation of TC ***************************//
    @PostMapping("/submitJustification")
    @ResponseBody
    fun submitJustificationForFormationOfTC(@RequestBody justificationForTC: JustificationForTC): ServerResponse {
        //return ResponseEntity(standardRequestService.requestForStandard(standardRequest), HttpStatus.OK)
        return ServerResponse(
            HttpStatus.OK,
            "Successfully submitted justification for formation of TC",
            formationOfTCService.submitJustificationForFormationOfTC(justificationForTC)
        )
    }
    @GetMapping("/getAllJustifications")
    fun getAllJustifications(): List<JustificationForTC> {
        return formationOfTCService.getAllJustifications()
    }

    @GetMapping("/getAllHofJustifications")
    fun getAllHofJustifications(): List<JustificationForTC> {
        return formationOfTCService.getAllHofJustifications()
    }

    @PostMapping("/approveJustification")
    @ResponseBody
    fun approveJustificationForTC(@RequestBody justificationForTC: JustificationForTC): ServerResponse {
        return ServerResponse(
            HttpStatus.OK,
            "Recommendation Approved",
            formationOfTCService.approveJustification(justificationForTC)
        )
    }

    @PostMapping("/editJustification")
    @ResponseBody
    fun editJustification(@RequestBody justificationForTC: JustificationForTC): ServerResponse {
        return ServerResponse(
            HttpStatus.OK,
            "Recommendation Edited",
            formationOfTCService.editJustificationForFormationOfTC(justificationForTC)
        )
    }

    @PostMapping("/rejectJustification")
    @ResponseBody
    fun rejectJustificationForTC(@RequestBody justificationForTC: JustificationForTC): ServerResponse {
        return ServerResponse(
            HttpStatus.OK,
            "Recommendation Rejected",
            formationOfTCService.rejectJustification(justificationForTC)
        )
    }

    @GetMapping("/getAllSpcJustifications")
    fun getAllSpcJustifications(): List<JustificationForTC> {
        return formationOfTCService.getAllSpcJustifications()
    }

    @GetMapping("/getAllJustificationsRejectedBySpc")
    fun getAllJustificationsRejectedBySpc(): List<JustificationForTC> {
        return formationOfTCService.getAllJustificationsRejectedBySpc()
    }

    @PostMapping("/approveJustificationSPC")
    @ResponseBody
    fun approveJustificationSPCForTC(@RequestBody justificationForTC: JustificationForTC): ServerResponse {
        return ServerResponse(
            HttpStatus.OK,
            "Recommendation Approved",
            formationOfTCService.approveJustificationSPC(justificationForTC)
        )
    }

    @PostMapping("/rejectJustificationSPC")
    @ResponseBody
    fun rejectJustificationSPC(@RequestBody justificationForTC: JustificationForTC): ServerResponse {
        return ServerResponse(
            HttpStatus.OK,
            "Recommendation Rejected",
            formationOfTCService.rejectJustificationSPC(justificationForTC)
        )
    }

    @GetMapping("/sacGetAllApprovedJustificationsBySpc")
    fun sacGetAllApprovedJustificationsBySpc(): List<JustificationForTC> {
        return formationOfTCService.sacGetAllApprovedJustificationsBySpc()
    }

    @GetMapping("/sacGetAllRejectedJustificationsBySpc")
    fun sacGetAllRejectedJustificationsBySpc(): List<JustificationForTC> {
        return formationOfTCService.sacGetAllRejectedJustificationsBySpc()
    }


    @PostMapping("/approveJustificationSAC")
    @ResponseBody
    fun approveJustificationSAC(@RequestBody justificationForTC: JustificationForTC): ServerResponse {
        return ServerResponse(
            HttpStatus.OK,
            "Recommendation Approved",
            formationOfTCService.approveJustificationSAC(justificationForTC)
        )
    }

    @PostMapping("/rejectJustificationSAC")
    @ResponseBody
    fun rejectJustificationSAC(@RequestBody justificationForTC: JustificationForTC): ServerResponse {
        return ServerResponse(
            HttpStatus.OK,
            "Recommendation Rejected",
            formationOfTCService.rejectJustificationSAC(justificationForTC)
        )
    }

    @GetMapping("/sacGetAllForWebsite")
    fun sacGetAllForWebsite(): List<JustificationForTC> {
        return formationOfTCService.sacGetAllForWebsite()
    }

    @GetMapping("/sacGetAllRejected")
    fun sacGetAllRejected(): List<JustificationForTC> {
        return formationOfTCService.sacGetAllRejected()
    }

//    @PostMapping("/advertiseTcToWebsite")
//    @ResponseBody
//    fun advertiseTcToWebsite(@RequestBody justificationForTC: JustificationForTC): ServerResponse {
//        return ServerResponse(
//            HttpStatus.OK,
//            "Recommendation Posted To Website",
//            formationOfTCService.advertiseTcToWebsite(justificationForTC)
//        )
//    }
    @PostMapping("/uploadAdditionalDocs")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun uploadDocs(
        @RequestParam("proposalId") proposalId: String,
        @RequestParam("docFile") docFile: List<MultipartFile>,
        @RequestParam("type") type: String,
        @RequestParam("docDescription") docDescription: String,

        model: Model
    ): CommonDaoServices.MessageSuccessFailDTO {


        val retrievedProposal = justificationForTCRepository.findByIdOrNull(proposalId.toLong())
            ?: throw Exception("APPLICATION DOES NOT EXIST")
        docFile.forEach { u ->
            val upload = DatKebsSdStandardsEntity()
            with(upload) {
                sdDocumentId = retrievedProposal.id
                documentTypeDef = type

            }

            formationOfTCService.uploadAdditionalDocumentsForProposal(
                upload,
                u,
                "Formation Of Tc Additional Documents",
                retrievedProposal.id,
                docDescription

            )
        }

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.message = "Documents Uploaded successfully"

        return sm
    }

    @GetMapping("/getAdditionalDocuments")
    fun getAdditionalDocuments(
        @RequestParam("proposalId") proposalId: Long,
    ): Collection<DatKebsSdStandardsEntity?>? {
        return formationOfTCService.getDocuments(proposalId)
    }

    @GetMapping("/viewById")
    fun viewFileById(
        response: HttpServletResponse,
        @RequestParam("docId") docId: Long,
    ) {
        val fileUploaded = draftDocumentService.findFile(docId)
        val fileDoc = fileUploaded.let { commonDaoServices.mapClass(it) }
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


    @GetMapping("/getAllApprovedJustifications")
    fun getAllApprovedJustifications(): List<JustificationForTC> {
        return formationOfTCService.getAllApprovedJustifications()
    }

    @GetMapping("/getAllRejectedJustifications")
    fun getAllRejectedJustifications(): List<JustificationForTC> {
        return formationOfTCService.getAllRejectedJustifications()
    }


}
