package org.kebs.app.kotlin.apollo.api.controllers.stdController

import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.std.*
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

@RestController
@RequestMapping("api/v1/migration/formationOfTC")
class FormationOfTCController(
    val formationOfTCService: FormationOfTCService,
    val tCRelevantDocumentService: TCRelevantDocumentService,
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


    @PostMapping("/approveJustificationSAC")
    @ResponseBody
    fun approveJustificationSAC(@RequestBody justificationForTC: JustificationForTC): ServerResponse {
        return ServerResponse(
            HttpStatus.OK,
            "Recommendation Approved",
            formationOfTCService.approveJustificationSAC(justificationForTC)
        )
    }

    @PostMapping("/advertiseTcToWebsite")
    @ResponseBody
    fun advertiseTcToWebsite(@RequestBody justificationForTC: JustificationForTC): ServerResponse {
        return ServerResponse(
            HttpStatus.OK,
            "Recommendation Posted To Website",
            formationOfTCService.advertiseTcToWebsite(justificationForTC)
        )
    }


    @PostMapping("/uploadFiles")
    fun uploadFiles(
        @RequestParam("file") file: MultipartFile,
        @RequestParam("itemId") itemId: String,
        @RequestParam("type") type: String
    ): ResponseEntity<ResponseMessage> {
        var message: String? = null



        return try {
            when (type) {
                "TCRelevantDocument" -> {
                    tCRelevantDocumentService.store(file, itemId)
                }
            }


            message = "Uploaded the file successfully: ${file.originalFilename}"
            ResponseEntity.status(HttpStatus.OK).body(ResponseMessage(message))
        } catch (e: Exception) {
            message = "Could not upload the file: ${file.originalFilename}!"
            ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(ResponseMessage(message))
        }
    }


    @GetMapping("list/files/{type}/{itemId}")
    fun getListFiles(
        @PathVariable type: String?,
        @PathVariable("itemId") itemId: String
    ): ResponseEntity<List<ResponseFile>> {

        var files: List<ResponseFile>? = null
        when (type) {
            "DraftDocument" -> {

                files = tCRelevantDocumentService.getAllFiles(itemId).map { dbFile ->
                    val fileDownloadUri = ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .path("/technicalCommittee/files/")
                        .path(dbFile.id)
                        .toUriString()

                    ResponseFile(
                        dbFile.name,
                        fileDownloadUri,
                        dbFile.type,
                        dbFile.data.size
                    )

                }.collect(Collectors.toList())
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body(files)
    }

    @GetMapping("/files/{id}")
    fun getFile(@PathVariable id: String?): ResponseEntity<ByteArray?>? {
        val fileDB = tCRelevantDocumentService.getFile(id!!)
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB!!.name + "\"")
            .body(fileDB.data)
    }

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


}
