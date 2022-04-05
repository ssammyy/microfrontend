package org.kebs.app.kotlin.apollo.api.controllers.stdController

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.std.AdoptionOfEastAfricaStandardService
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.std.DraftDocumentService
import org.kebs.app.kotlin.apollo.api.ports.provided.makeAnyNotBeNull
import org.kebs.app.kotlin.apollo.common.dto.std.ID
import org.kebs.app.kotlin.apollo.common.dto.std.ServerResponse
import org.kebs.app.kotlin.apollo.common.dto.std.TaskDetails
import org.kebs.app.kotlin.apollo.store.model.std.DatKebsSdStandardsEntity
import org.kebs.app.kotlin.apollo.store.model.std.DecisionFeedback
import org.kebs.app.kotlin.apollo.store.model.std.SACSummary
import org.kebs.app.kotlin.apollo.store.repo.std.SACSummaryRepository
import org.springframework.http.HttpStatus
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("api/v1/migration/adoptionToEAStandard")
class AdoptionToEastAfricaStandardController(
    val adoptionOfEastAfricaStandardService: AdoptionOfEastAfricaStandardService,
    private val sacSummaryRepository: SACSummaryRepository,
    val commonDaoServices: CommonDaoServices,
    val draftDocumentService: DraftDocumentService,


    ) {

    //********************************************************** deployment endpoints ********************************************//
    @PostMapping("/deploy")
    fun deployWorkflow(): ServerResponse {
        adoptionOfEastAfricaStandardService.deployProcessDefinition()
        return ServerResponse(HttpStatus.OK, "Successfully deployed server", HttpStatus.OK)
    }

    //***************************************************** submit justification for formation of TC ***************************//
    @PostMapping("/submitSACSummary")
    @ResponseBody
    fun submitSACSummary(@RequestBody sacSummary: SACSummary): ServerResponse{
        //return ResponseEntity(standardRequestService.requestForStandard(standardRequest), HttpStatus.OK)
        return ServerResponse(HttpStatus.OK,"Successfully submitted SAC Summary",adoptionOfEastAfricaStandardService.submitSACSummary(sacSummary))
    }

    @GetMapping("/getTcSecSummaryTask")
    fun getTcSecSummaryTasks():List<TaskDetails>
    {
        return adoptionOfEastAfricaStandardService.getTcSecSummaryTask()
    }

    @PostMapping("/decisionByTCSecOnSACSummary")
    @ResponseBody
    fun decisionByTcSecOnSACSummary(
        @RequestBody decisionFeedback: DecisionFeedback,
        @RequestParam("decisionId") decisionId: Long
    ): ServerResponse {
        return ServerResponse(
            HttpStatus.OK,
            "Decision By TC SEC on SAC Summary",
            adoptionOfEastAfricaStandardService.approveSACSummary(decisionFeedback, decisionId)
        )
    }


   @GetMapping("/getSACSummaryTask")
    fun getSACSummaryTask():List<TaskDetails>
    {
        return adoptionOfEastAfricaStandardService.getSACSummaryTask()
    }




    @PostMapping("/process")
    @ResponseBody
    fun checkState( @RequestBody id: ID): ServerResponse {
        return ServerResponse(HttpStatus.OK,"Successfully returned process history",adoptionOfEastAfricaStandardService.checkProcessHistory(id))
    }

    @PostMapping("/decisionOnSACSummary")
    @ResponseBody
    fun decisionOnSACSummary(
        @RequestBody decisionFeedback: DecisionFeedback,
        @RequestParam("decisionId") decisionId: Long
    ): ServerResponse {
        return ServerResponse(
            HttpStatus.OK,
            "Decision on SAC Summary",
            adoptionOfEastAfricaStandardService.decisionOnSACSummary(decisionFeedback, decisionId)
        )
    }


    @GetMapping("/getSACSECTask")
    fun getSACSECTask():List<TaskDetails>
    {
        return adoptionOfEastAfricaStandardService.getSACSECTask()
    }


    @PostMapping("/decisionOnSACSEC")
    @ResponseBody
    fun decisionOnSACSEC(
        @RequestBody decisionFeedback: DecisionFeedback,
        @RequestParam("decisionId") decisionId: Long
    ): ServerResponse {
        return ServerResponse(
            HttpStatus.OK,
            "Decision on SAC SEC",
            adoptionOfEastAfricaStandardService.decisionOnSACSEC(decisionFeedback, decisionId)
        )
    }

    @PostMapping("/file-upload")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun uploadFilesDraftStandard(
        @RequestParam("docId") applicationID: Long,
        @RequestParam("docFile") docFile: List<MultipartFile>,
        @RequestParam("type") type: String,
        @RequestParam("docName") docName: String,

        model: Model
    ): CommonDaoServices.MessageSuccessFailDTO {

        var docDescription: String
        val loggedInUser = commonDaoServices.loggedInUserDetails()

//        val application = sacSummaryRepository.findById(callForTCApplicationId)
//            ?: throw Exception("APPLICATION DOES NOT EXIST")
        val application = sacSummaryRepository.findById(applicationID).orElse(null);

        docFile.forEach { u ->
            val upload = DatKebsSdStandardsEntity()
            with(upload) {
                sdDocumentId = application.id
                documentTypeDef = type

            }
            docDescription = "$ CV"

            adoptionOfEastAfricaStandardService.uploadSDFile(
                upload,
                u,
                "UPLOADS",
                loggedInUser,
                docDescription

            )
        }

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.message = "Document Uploaded successfully"

        return sm
    }

    // View DI SDT Uploaded document
    @GetMapping("/view")
    fun viewDiJustificationFile(
        response: HttpServletResponse,
        @RequestParam("draftStandardId") diDocumentId: Long,
        @RequestParam("doctype") doctype: String
    ) {
        val fileUploaded = draftDocumentService.findUploadedDIFileBYIdAndByType(diDocumentId, doctype)
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
