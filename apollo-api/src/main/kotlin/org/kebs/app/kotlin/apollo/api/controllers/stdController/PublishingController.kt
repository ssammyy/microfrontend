package org.kebs.app.kotlin.apollo.api.controllers.stdController


import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.std.DraftDocumentService
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.std.DraughtDocumentService
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.std.EditorDocumentService
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.std.PublishingService
import org.kebs.app.kotlin.apollo.api.ports.provided.makeAnyNotBeNull
import org.kebs.app.kotlin.apollo.common.dto.std.*
import org.kebs.app.kotlin.apollo.store.model.std.DatKebsSdStandardsEntity
import org.kebs.app.kotlin.apollo.store.model.std.DecisionFeedback
import org.kebs.app.kotlin.apollo.store.model.std.StandardDraft
import org.kebs.app.kotlin.apollo.store.repo.std.StandardDraftRepository
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
@RequestMapping("api/v1/migration/publishing")
class PublishingController(
    val publishingService: PublishingService,
    val commonDaoServices: CommonDaoServices,
    val draftDocumentService: DraftDocumentService,
    val editorDocumentService: EditorDocumentService,
    private val standardDraftRepository: StandardDraftRepository,
    val draughtDocumentService: DraughtDocumentService
) {

    //********************************************************** deployment endpoints **********************************************************
    @PostMapping("/deploy")
    fun deployWorkflow(): ServerResponse {
        publishingService.deployProcessDefinition()
        return ServerResponse(HttpStatus.OK, "Successfully deployed server", HttpStatus.OK)
    }

    //********************************************************** process submit draft standard **********************************************************
    @PostMapping("/submit")
    @ResponseBody
    fun requestForStandard(@RequestBody standardDraft: StandardDraft): ServerResponse{
        //return ResponseEntity(standardRequestService.requestForStandard(standardRequest), HttpStatus.OK)
        return ServerResponse(HttpStatus.OK,"Successfully submitted draft standard",publishingService.sumbitDraftStandard(standardDraft))
    }

    @GetMapping("/getHOPTasks")
    fun getHOPTasks():List<TaskDetails>
    {
        return publishingService.getHOPTasks()
    }

    @PostMapping("/decisionOnKSDraft")
    @ResponseBody
    fun decisionOnKSDraft(@RequestBody decision: Decision): ServerResponse
    {
        return ServerResponse(HttpStatus.OK,"Decision on KS draft by HOP",publishingService.decisionOnKSDraft(decision))
    }

    @PostMapping("/uploadRejectionFeedback")
    @ResponseBody
    fun uploadFeedbackOnDraft(@RequestBody decisionFeedback: DecisionFeedback): ServerResponse {
        return ServerResponse(HttpStatus.OK,"Successfully uploaded feedback on draft",publishingService.uploadFeedbackOnDraft(decisionFeedback))
    }

    @GetMapping("/getEditorTasks")
    fun getEditorTasks():List<TaskDetails>
    {
        return publishingService.getEditorTasks()
    }

    @PostMapping("/finishEditingDraft")
    @ResponseBody
    fun editDraftStandard(@RequestBody standardDraft: StandardDraft): ServerResponse {
        return ServerResponse(HttpStatus.OK,"Finished editing draft",publishingService.editDraftStandard(standardDraft))
    }

    @GetMapping("/getProofReaderTasks")
    fun getProofreaderTasks():List<TaskDetails>
    {
        return publishingService.getProofreaderTasks()
    }

    @PostMapping("/finishedProofReading")
    @ResponseBody
    fun decisionOnProofReading(@RequestBody decision: Decision): ServerResponse {
        return ServerResponse(HttpStatus.OK,"Finished checking proof reading",publishingService.decisionOnProofReading(decision))
    }


    @GetMapping("/getDraughtsmanTasks")
    fun getDraughtsmanTasks():List<TaskDetails>
    {
        return publishingService.getDraughtsmanTasks()
    }

    @PostMapping("/uploadDraughtChanges")
    @ResponseBody
    fun uploadDraftStandard(@RequestBody standardDraft: StandardDraft): ServerResponse {
        return ServerResponse(
            HttpStatus.OK,
            "Finished draughting changes",
            publishingService.uploadDraftStandard(standardDraft)
        )
    }

    @PostMapping("/approvedDraftStandard")
    @ResponseBody
    fun approveDraughtChange(@RequestBody standardDraft: StandardDraft): ServerResponse {
        return ServerResponse(
            HttpStatus.OK,
            "Check draught standard and approve change",
            publishingService.approveDraughtChange(standardDraft)
        )
    }

    @PostMapping("/file-upload")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun uploadFilesDraftStandard(
        @RequestParam("draftStandardID") draftStandardID: Long,
        @RequestParam("docFile") docFile: List<MultipartFile>,
        model: Model
    ): CommonDaoServices.MessageSuccessFailDTO {

        val loggedInUser = commonDaoServices.loggedInUserDetails()
        val nwaJustification = standardDraftRepository.findByIdOrNull(draftStandardID)
            ?: throw Exception("DRAFT DOCUMENT ID DOES NOT EXIST")

        docFile.forEach { u ->
            val upload = DatKebsSdStandardsEntity()
            with(upload) {
                sdDocumentId = nwaJustification.id
                documentTypeDef = "DraftDocument"

            }
            publishingService.uploadSDFile(
                upload,
                u,
                "UPLOADS",
                loggedInUser,
                "Draft Standard Publishing"
            )
        }

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.message = "Document Uploaded successfully"

        return sm
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
                "DraftDocument" -> {
                    draftDocumentService.store(file, itemId)
                }
                "EditorDocuments" -> {
                    draftDocumentService.store(file, itemId)
                }
                "DraughtDocuments" -> {
                    draftDocumentService.store(file, itemId)
                }
            }


            message ="Uploaded the file successfully: ${file.originalFilename}"
            ResponseEntity.status(HttpStatus.OK).body(ResponseMessage(message))
        }
        catch (e: Exception){
            message ="Could not upload the file: ${file.originalFilename}!"
            ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(ResponseMessage(message))
        }
    }



    @GetMapping("list/files/{type}/{itemId}")
    fun getListFiles(@PathVariable type: String?,@PathVariable("itemId") itemId: String): ResponseEntity<List<ResponseFile>> {

        var files: List<ResponseFile>? = null
        when (type) {
            "DraftDocument" -> {

                files = draftDocumentService.getAllFiles(itemId).map { dbFile ->
                    val fileDownloadUri = ServletUriComponentsBuilder
                            .fromCurrentContextPath()
                            .path("/publishing/files/")
                            .path(dbFile.id)
                            .toUriString()

                    ResponseFile(
                            dbFile.name,
                            fileDownloadUri,
                            dbFile.type,
                            dbFile.data.size)

                }.collect(Collectors.toList())
            }
            "EditorDocuments" -> {

                files = editorDocumentService.getAllFiles(itemId).map { dbFile ->
                    val fileDownloadUri = ServletUriComponentsBuilder
                            .fromCurrentContextPath()
                            .path("/publishing/files/")
                            .path(dbFile.id)
                            .toUriString()

                    ResponseFile(
                            dbFile.name,
                            fileDownloadUri,
                            dbFile.type,
                            dbFile.data.size)

                }.collect(Collectors.toList())
            }
            "DraughtDocuments" -> {

                files = draughtDocumentService.getAllFiles(itemId).map { dbFile ->
                    val fileDownloadUri = ServletUriComponentsBuilder
                            .fromCurrentContextPath()
                            .path("/publishing/files/")
                            .path(dbFile.id)
                            .toUriString()

                    ResponseFile(
                            dbFile.name,
                            fileDownloadUri,
                            dbFile.type,
                            dbFile.data.size)

                }.collect(Collectors.toList())
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body(files)
    }

    @GetMapping("/files/{id}")
    fun getFile(@PathVariable id: String?): ResponseEntity<ByteArray?>? {
        val fileDB = draftDocumentService.getFile(id!!)
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB!!.name + "\"")
                .body(fileDB.data)
    }
    // View DI SDT Uploaded document
    @GetMapping("/view/draftStandard")
    fun viewDiJustificationFile(
        response: HttpServletResponse,
        @RequestParam("draftStandardId") diDocumentId: Long
    ) {
        val fileUploaded = draftDocumentService.findUploadedDIFileBYId(diDocumentId)
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


}
