package org.kebs.app.kotlin.apollo.api.controllers.stdController

import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.std.CommitteeService
import org.kebs.app.kotlin.apollo.common.dto.std.ProcessInstanceResponse
import org.kebs.app.kotlin.apollo.common.dto.std.ServerResponse
import org.kebs.app.kotlin.apollo.common.dto.std.TaskDetails
import org.kebs.app.kotlin.apollo.store.model.std.*
import org.kebs.app.kotlin.apollo.store.repo.std.CommitteeNWIRepository
import org.kebs.app.kotlin.apollo.store.repo.std.CommitteePDRepository
import org.kebs.app.kotlin.apollo.store.repo.std.MembershipTCRepository
import org.kebs.app.kotlin.apollo.store.repo.std.StandardNWIRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile


@RestController
//@RequestMapping("/committee")
@CrossOrigin(origins = ["http://localhost:4200"])

@RequestMapping("api/v1/migration/committee")


class CommitteeController(
    val committeeService: CommitteeService,
    val committeeNWIRepository: CommitteeNWIRepository,
    val standardNWIRepository: StandardNWIRepository,
    val committeePDRepository: CommitteePDRepository,

    ) {

    //********************************************************** deployment endpoints **********************************************************
    @PostMapping("/deploy_committee")
    fun deployWorkflow() {
        committeeService.deployProcessDefinition()
    }

    //get all approved NWIs
    @GetMapping("/getAllNwis")
    fun getAllTechnicalCommittees(): List<ApprovedNwi> {
        return committeeService.getApprovedNwis()
    }

    //preliminary draft upload minutes
    @PostMapping("/upload/minutes")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun uploadPdMinutes(
        @RequestParam("nwiId") nwiId: Long,
        @RequestParam("docFile") docFile: List<MultipartFile>,
        @RequestParam("type") type: String,

        model: Model
    ): CommonDaoServices.MessageSuccessFailDTO {

        var docDescription: String;

        val application = standardNWIRepository.findByIdOrNull(nwiId)
            ?: throw Exception("APPLICATION DOES NOT EXIST")
        docFile.forEach { u ->
            val upload = DatKebsSdStandardsEntity()
            with(upload) {
                sdDocumentId = application.id
                documentTypeDef = type

            }
            docDescription = "Minutes For PD"

            committeeService.uploadSDFileCommittee(
                upload,
                u,
                "MINUTES FOR PD",
                application.id,
                docDescription

            )
        }

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.message = "Minutes Uploaded successfully"

        return sm
    }

    //preliminary draft upload other Draft Documents
    @PostMapping("/upload/draftDocuments")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun uploadPdDraftDocuments(
        @RequestParam("nwiId") nwiId: Long,
        @RequestParam("docFile") docFile: List<MultipartFile>,
        @RequestParam("type") type: String,

        model: Model
    ): CommonDaoServices.MessageSuccessFailDTO {

        var docDescription: String;

        val application = standardNWIRepository.findByIdOrNull(nwiId)
            ?: throw Exception("APPLICATION DOES NOT EXIST")

        docFile.forEach { u ->
            val upload = DatKebsSdStandardsEntity()
            with(upload) {
                sdDocumentId = application.id
                documentTypeDef = type

            }
            docDescription = "Draft Documents For PD"

            committeeService.uploadSDFileCommittee(
                upload,
                u,
                "DRAFT DOCUMENTS FOR PD",
                application.id,
                docDescription

            )
        }

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.message = "Draft Documents Uploaded successfully"


        return sm
    }
    @PostMapping("/uploadPd")
    @ResponseBody
    fun uploadPD(
        @RequestBody committeePD: CommitteePD,
        @RequestParam("nwIId") nwIId: Long
    ): ServerResponse {
        return ServerResponse(
            HttpStatus.OK,
            "Preliminary Draft Prepared",
            committeeService.uploadPD(committeePD, nwIId)
        )
    }

    //preliminary draft upload other Preliminary Draft Documents
    @PostMapping("/upload/pd")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun uploadPdDocument(
        @RequestParam("pdID") pdID: Long,
        @RequestParam("docFile") docFile: List<MultipartFile>,
        @RequestParam("type") type: String,

        model: Model
    ): CommonDaoServices.MessageSuccessFailDTO {

        var docDescription: String;

        val application = committeePDRepository.findByIdOrNull(pdID)
            ?: throw Exception("APPLICATION DOES NOT EXIST")

        docFile.forEach { u ->
            val upload = DatKebsSdStandardsEntity()
            with(upload) {
                sdDocumentId = application.id
                documentTypeDef = type

            }
            docDescription = "PD Document"

            committeeService.uploadSDFileCommittee(
                upload,
                u,
                "PD DOCUMENT",
                application.id,
                docDescription

            )
        }

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.message = "Preliminary Draft Document Uploaded successfully"


        return sm
    }

    @GetMapping("/getAllPds")
    fun getAllPreliminaryDrafts(): List<CommitteePD> {
        return committeeService.getAllPd()
    }


    @PostMapping("/makeCommentOnPd")
    fun makeCommentOnPd(@RequestBody comments: Comments): ServerResponse {
        return ServerResponse( HttpStatus.OK,
            "Comment Approved",
            committeeService.makeComment(comments))
    }

    @PostMapping("/editCommentOnPd")
    fun editCommentOnPd(@RequestBody comments: Comments): ServerResponse {
        return ServerResponse( HttpStatus.OK,
            "Comment Approved",
            committeeService.editComment(comments))
    }

    @PostMapping("/deleteCommentOnPd")
    fun deleteCommentOnPd(@RequestBody comments: Comments): ServerResponse {
        return ServerResponse( HttpStatus.OK,
            "Comment Approved",
            committeeService.deleteComment(comments))
    }




    @PostMapping("/approveNWI/{taskId}/{approved}")
    fun approve(@PathVariable("taskId") taskId: String, @PathVariable("approved") approved: Boolean) {
        committeeService.approveNWI(taskId, approved)
    }

    @PostMapping("/uploaddrafts/{taskId}")
    fun uploadDrafts(@RequestBody committeeDrafts: CommitteeDrafts, @PathVariable("taskId") taskId: String?) {
        committeeService.uploadDrafts(committeeDrafts, taskId)
    }


    @PostMapping("/preparePD")
    fun preparePD(@RequestBody committeePD: CommitteePD): ProcessInstanceResponse {
        return committeeService.preparePD(committeePD)
    }

    @PostMapping("/uploaddraftspd/{taskId}")
    fun uploadDraftsPD(@RequestBody committeeDraftsPD: CommitteeDraftsPD, @PathVariable("taskId") taskId: String?) {
        committeeService.uploadDraftsPD(committeeDraftsPD, taskId)
    }

    @PostMapping("/prepareCD")
    fun prepareCD(@RequestBody committeeCD: CommitteeCD): ProcessInstanceResponse {
        return committeeService.prepareCD(committeeCD)
    }


    @PostMapping("/approveCD/{taskId}/{approved}")
    fun approveCD(@PathVariable("taskId") taskId: String, @PathVariable("approved") approved: Boolean) {
        committeeService.approveCD(taskId, approved)
    }

    @GetMapping("/process/{process_id}")
    fun checkstate(@PathVariable("process_id") process_id: String?) {
        committeeService.checkProcessHistory(process_id)
    }

    @GetMapping("/tcsec/tasks")
    fun getTCSecTasks(): List<TaskDetails> {
        return committeeService.getTCSECTasks()
    }

    @GetMapping("/getnwis")
    fun getNWIs(): MutableList<CommitteeNWI> {
        return committeeService.getNWIs()
    }

    @GetMapping("/getPds")
    fun getPDS(): MutableList<CommitteePD> {
        return committeeService.getPds()
    }

    @GetMapping("/getCds")
    fun getCDS(): MutableList<CommitteeCD> {
        return committeeService.getCds()
    }


    @GetMapping("/pd/{id}")
    fun getPreliminaryDraftById(@PathVariable("id") id: Long): ResponseEntity<CommitteePD?>? {
        return committeeService.getPreliminaryDraftById(id)
    }


}
