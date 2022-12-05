package org.kebs.app.kotlin.apollo.api.controllers.stdController

import mu.KotlinLogging
import org.json.JSONObject
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.std.CommitteeService
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.std.DraftDocumentService
import org.kebs.app.kotlin.apollo.api.ports.provided.makeAnyNotBeNull
import org.kebs.app.kotlin.apollo.common.dto.std.ServerResponse
import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import org.kebs.app.kotlin.apollo.store.model.std.*
import org.kebs.app.kotlin.apollo.store.repo.std.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.*
import javax.servlet.http.HttpServletResponse


@RestController
//@RequestMapping("/committee")
@CrossOrigin(origins = ["http://localhost:4200"])

@RequestMapping("api/v1/migration/committee")


class CommitteeController(
    val committeeService: CommitteeService,
    val standardNWIRepository: StandardNWIRepository,
    val committeePDRepository: CommitteePDRepository,
    val committeeCDRepository: CommitteeCDRepository,
    val draftDocumentService: DraftDocumentService,
    val commonDaoServices: CommonDaoServices,

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

    @GetMapping("/getAllSdUsers")
    fun getAllSdUsers(): List<UsersEntity> {
        return committeeService.getAllSdUsers()
    }


    @GetMapping("/getAllNwiSApprovedForPd")
    fun getAllNwiSApprovedForPd(): List<StandardNWI> {
        return committeeService.getAllNwiSApprovedForPd()
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

        var docDescription: String

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

        var docDescription: String

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

        var docDescription: String

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
    fun getAllPreliminaryDrafts(): List<PdWithUserName> {
        return committeeService.getAllPd()
    }

    @GetMapping("/getAllCommentsOnPd")
    fun getAllCommentsOnPreliminaryDrafts(@RequestParam("preliminaryDraftId") preliminaryDraftId: Long): List<CommentsWithPdId> {
        return committeeService.getAllCommentsOnPd(preliminaryDraftId)
    }

    @GetMapping("/getAllCommentsOnPdByPdId")
    fun getAllCommentsOnPdByPdId(): List<CommentsWithPdId> {
        return committeeService.getAllCommentsOnPdWithPdName()
    }

    @GetMapping("/getUserLoggedInCommentsOnPD")
    fun getUserLoggedInCommentsOnPD(): List<CommentsWithPdId> {
        return committeeService.getUserLoggedInCommentsOnPD()
    }


    @PostMapping("/makeComment")
    fun makeComment(@RequestParam("docType") docType: String, @RequestBody comments: Comments): ServerResponse {
        return ServerResponse(
            HttpStatus.OK,
            "Comment Approved",
            committeeService.makeComment(comments, docType)
        )
    }

    @PostMapping("/editComment")
    fun editComment(@RequestBody comments: Comments): ServerResponse {
        return ServerResponse(
            HttpStatus.OK,
            "Comment Edited",
            committeeService.editComment(comments)
        )
    }

    @PostMapping("/deleteComment")
    fun deleteComment(@RequestBody comments: Comments): ServerResponse {
        return ServerResponse(
            HttpStatus.OK,
            "Comment Deleted",
            committeeService.deleteComment(comments)
        )
    }

    @GetMapping("/getAllDocumentsOnPd")
    fun getAllPdDocuments(@RequestParam("preliminaryDraftId") preliminaryDraftId: Long): Collection<DatKebsSdStandardsEntity?>? {
        return committeeService.getAllPdDocuments(preliminaryDraftId)
    }

    @GetMapping("/getAllDocumentsOnCd")
    fun getAllCdDocuments(@RequestParam("committeeDraftId") committeeDraftId: Long): Collection<DatKebsSdStandardsEntity?>? {
        return committeeService.getAllCdDocuments(committeeDraftId)
    }

    @GetMapping("/getAllCommentsOnCd")
    fun getAllCommentsOnCommitteeDrafts(@RequestParam("committeeDraftId") committeeDraftId: Long): List<CommentsWithCdId> {
        return committeeService.getAllCommentsOnCd(committeeDraftId)
    }

    @PostMapping("/approveCD")
    fun approveCd(@RequestBody committeeCD: CommitteeCD)
            : ServerResponse {
        return ServerResponse(
            HttpStatus.OK,
            "Committee Draft Approved",
            committeeService.approveCd(committeeCD)
        )
    }


    //committee draft upload minutes
    @PostMapping("/upload/cdMinutes")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun uploadCDMinutes(
        @RequestParam("pdId") pdId: Long,
        @RequestParam("docFile") docFile: List<MultipartFile>,
        @RequestParam("type") type: String,

        model: Model
    ): CommonDaoServices.MessageSuccessFailDTO {

        var docDescription: String

        val application = committeePDRepository.findByIdOrNull(pdId)
            ?: throw Exception("APPLICATION DOES NOT EXIST")
        docFile.forEach { u ->
            val upload = DatKebsSdStandardsEntity()
            with(upload) {
                sdDocumentId = application.id
                documentTypeDef = type

            }
            docDescription = "Minutes For CD"

            committeeService.uploadSDFileCommittee(
                upload,
                u,
                "MINUTES FOR CD",
                application.id,
                docDescription

            )
        }

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.message = "Minutes Uploaded successfully"

        return sm
    }

    //preliminary draft upload other Draft Documents
    @PostMapping("/upload/cdDraftDocuments")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun uploadCdDraftDocuments(
        @RequestParam("pdId") pdId: Long,
        @RequestParam("docFile") docFile: List<MultipartFile>,
        @RequestParam("type") type: String,

        model: Model
    ): CommonDaoServices.MessageSuccessFailDTO {

        var docDescription: String

        val application = committeePDRepository.findByIdOrNull(pdId)
            ?: throw Exception("APPLICATION DOES NOT EXIST")

        docFile.forEach { u ->
            val upload = DatKebsSdStandardsEntity()
            with(upload) {
                sdDocumentId = application.id
                documentTypeDef = type

            }
            docDescription = "Draft Documents For CD"

            committeeService.uploadSDFileCommittee(
                upload,
                u,
                "DRAFT DOCUMENTS FOR CD",
                application.id,
                docDescription

            )
        }

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.message = "Draft Documents Uploaded successfully"


        return sm
    }

    @PostMapping("/uploadCd")
    @ResponseBody
    fun uploadCD(
        @RequestBody committeeCD: CommitteeCD,
        @RequestParam("pdId") pdId: Long
    ): ServerResponse {
        return ServerResponse(
            HttpStatus.OK,
            "Committee Draft Prepared",
            committeeService.uploadCD(committeeCD, pdId)
        )
    }

    //committee draft upload other committee Draft Documents
    @PostMapping("/upload/cd")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun uploadCdDocument(
        @RequestParam("cdID") cdID: Long,
        @RequestParam("docFile") docFile: List<MultipartFile>,
        @RequestParam("type") type: String,

        model: Model
    ): CommonDaoServices.MessageSuccessFailDTO {

        var docDescription: String

        val application = committeeCDRepository.findByIdOrNull(cdID)
            ?: throw Exception("APPLICATION DOES NOT EXIST")

        docFile.forEach { u ->
            val upload = DatKebsSdStandardsEntity()
            with(upload) {
                sdDocumentId = application.id
                documentTypeDef = type

            }
            docDescription = "CD Document"

            committeeService.uploadSDFileCommittee(
                upload,
                u,
                "CD DOCUMENT",
                application.id,
                docDescription

            )
        }

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.message = "Committee Draft Document Uploaded successfully"


        return sm
    }

    @GetMapping("/getAllCds")
    fun getAllCommitteeDrafts(): List<CdWithUserName> {
        return committeeService.getAllCd()
    }

    @GetMapping("/getUserLoggedInCommentsOnCD")
    fun getUserLoggedInCommentsOnCD(): List<CommentsWithCdId> {
        return committeeService.getUserLoggedInCommentsOnCD()
    }

    @GetMapping("/view")
    fun viewFiles(
        response: HttpServletResponse,
        @RequestParam("docId") docId: Long,
        @RequestParam("doctype") doctype: String
    ) {
        val fileUploaded = draftDocumentService.findUploadedDIFileBYIdAndByType(docId, doctype)
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

    @GetMapping("/viewById")
    fun viewFileById(
        response: HttpServletResponse,
        @RequestParam("docId") docId: Long,
    ) {
        val fileUploaded = draftDocumentService.findFile(docId)
        println(fileUploaded)
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

    @GetMapping("/viewByIdB")
    fun getFile(
        response: HttpServletResponse,
        @RequestParam("docId") docId: Long,
    ): MutableMap<String, ByteArray?> {
        val fileDB = draftDocumentService.findFile(docId)

//        return JSONObject.quote(fileDB.document);


        return Collections.singletonMap("body", fileDB.document);


//        return ServletUriComponentsBuilder
//            .fromCurrentContextPath()
//            .path("/files/")
//            .path(fileDB.id.toString())
//            .toUriString()
//        return ResponseEntity.ok()
//            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.name + "\"")
//            .body(fileDB.document)
    }

//    @GetMapping("/files/{id}")
//    fun getFile(@PathVariable id: String?): ResponseEntity<ByteArray?>? {
//        val fileDB: FileDB = storageService.getFile(id)
//        return ResponseEntity.ok()
//            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"")
//            .body(fileDB.getData())
//    }
}

fun encodeBase64(encodeMe: ByteArray?): String {
    val encodedBytes: ByteArray = Base64.getEncoder().encode(encodeMe)
    return String(encodedBytes)
}
