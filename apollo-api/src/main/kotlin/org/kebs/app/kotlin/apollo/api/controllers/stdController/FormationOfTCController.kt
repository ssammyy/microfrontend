package org.kebs.app.kotlin.apollo.api.controllers.stdController

import org.kebs.app.kotlin.apollo.store.model.std.*
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.std.*
import org.kebs.app.kotlin.apollo.common.dto.std.*
import org.kebs.app.kotlin.apollo.store.model.std.JustificationForTC
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.util.stream.Collectors

@RestController
@RequestMapping("api/v1/migration/formationOfTC")
class FormationOfTCController(
    val formationOfTCService: FormationOfTCService,
    val tCRelevantDocumentService: TCRelevantDocumentService
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

    @GetMapping("/getSPCTasks")
    fun getSPCTasks(): List<TaskDetails> {
        return formationOfTCService.getSPCTasks()
    }

    @PostMapping("/decisionOnJustificationForTC")
    @ResponseBody
    fun decisionOnJustificationForTC(@RequestBody decisionFeedback: DecisionFeedback): ServerResponse {
        return ServerResponse(
            HttpStatus.OK,
            "Decision on justification for formation of TC",
            formationOfTCService.decisionOnJustificationForTC(decisionFeedback)
        )
    }

    @PostMapping("/uploadRejectionFeedback")
    @ResponseBody
    fun uploadFeedbackOnJustification(@RequestBody decisionFeedback: DecisionFeedback): ServerResponse {
        return ServerResponse(
            HttpStatus.OK,
            "Successfully uploaded feedback on draft",
            formationOfTCService.uploadFeedbackOnJustification(decisionFeedback)
        )
    }

    @GetMapping("/getSACTasks")
    fun getSACTasks(): List<TaskDetails> {
        return formationOfTCService.getSACTasks()
    }

    @PostMapping("/decisionOnSPCFeedback")
    @ResponseBody
    fun decisionOnSPCFeedback(@RequestBody decisionFeedback: DecisionFeedback): ServerResponse {
        return ServerResponse(
            HttpStatus.OK,
            "Decision on SPC feedback",
            formationOfTCService.decisionOnSPCFeedback(decisionFeedback)
        )
    }

    @PostMapping("/uploadRejectionFeedbackBySAC")
    @ResponseBody
    fun uploadFeedbackOnJustificationBySAC(@RequestBody decisionFeedback: DecisionFeedback): ServerResponse {
        return ServerResponse(
            HttpStatus.OK,
            "Successfully uploaded feedback on SAC",
            formationOfTCService.uploadFeedbackOnJustification(decisionFeedback)
        )
    }


    @PostMapping("/process")
    @ResponseBody
    fun checkState(@RequestBody id: ID): ServerResponse {
        return ServerResponse(
            HttpStatus.OK,
            "Successfully returned process history",
            formationOfTCService.checkProcessHistory(id)
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


}
