package org.kebs.app.kotlin.apollo.api.controllers.stdController

import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.std.*
import org.kebs.app.kotlin.apollo.common.dto.std.*
import org.kebs.app.kotlin.apollo.store.model.std.*
import org.kebs.app.kotlin.apollo.store.repo.std.CommitteeCDRepository
import org.kebs.app.kotlin.apollo.store.repo.std.PublicReviewDraftRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile


@CrossOrigin(origins = ["http://localhost:4200"])

@RestController
@RequestMapping("api/v1/migration/publicReview")

class PublicReviewController(
    val publicReviewService: PublicReviewService,
    val publicReviewDraftRepository: PublicReviewDraftRepository,
    val committeeService: CommitteeService,
    val committeeCDRepository: CommitteeCDRepository,


    ) {
    //********************************************************** deployment endpoints **********************************************************
    @PostMapping("/deploy_publicreview")
    fun deployWorkflow() {
        publicReviewService.deployProcessDefinition()
    }

    @GetMapping("/getAllApprovedCds")
    fun getAllCommitteeDrafts(): List<CdWithUserName> {
        return publicReviewService.getAllApprovedCd()
    }

    //committee draft upload minutes
    @PostMapping("/upload/prdMinutes")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun uploadPRDMinutes(
        @RequestParam("cdId") cdId: Long,
        @RequestParam("docFile") docFile: List<MultipartFile>,
        @RequestParam("type") type: String,

        model: Model
    ): CommonDaoServices.MessageSuccessFailDTO {

        var docDescription: String;

        val application = committeeCDRepository.findByIdOrNull(cdId)
            ?: throw Exception("APPLICATION DOES NOT EXIST")
        docFile.forEach { u ->
            val upload = DatKebsSdStandardsEntity()
            with(upload) {
                sdDocumentId = application.id
                documentTypeDef = type

            }
            docDescription = "Minutes For PRD"

            committeeService.uploadSDFileCommittee(
                upload,
                u,
                "MINUTES FOR PRD",
                application.id,
                docDescription

            )
        }

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.message = "Minutes Uploaded successfully"

        return sm
    }

    //public review draft upload other Draft Documents
    @PostMapping("/upload/prdDraftDocuments")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun uploadPrdDraftDocuments(
        @RequestParam("cdId") cdId: Long,
        @RequestParam("docFile") docFile: List<MultipartFile>,
        @RequestParam("type") type: String,

        model: Model
    ): CommonDaoServices.MessageSuccessFailDTO {

        var docDescription: String;

        val application = committeeCDRepository.findByIdOrNull(cdId)
            ?: throw Exception("APPLICATION DOES NOT EXIST")

        docFile.forEach { u ->
            val upload = DatKebsSdStandardsEntity()
            with(upload) {
                sdDocumentId = application.id
                documentTypeDef = type

            }
            docDescription = "Draft Documents For PRD"

            committeeService.uploadSDFileCommittee(
                upload,
                u,
                "DRAFT DOCUMENTS FOR PRD",
                application.id,
                docDescription

            )
        }

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.message = "Draft Documents Uploaded successfully"


        return sm
    }


    @PostMapping("/preparepr")
    fun preparePublicReview(
        @RequestBody publicReviewDraft: PublicReviewDraft, @RequestParam("cdId") cdId: Long
    ): ServerResponse {
        return ServerResponse(
            HttpStatus.OK,
            "Preliminary Draft Prepared",
            publicReviewService.uploadPRD(publicReviewDraft, cdId)
        )
    }

    //public review draft upload other Public Review Draft Document
    @PostMapping("/upload/prd")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun uploadPrdDocument(
        @RequestParam("prdId") prdId: Long,
        @RequestParam("docFile") docFile: List<MultipartFile>,
        @RequestParam("type") type: String,

        model: Model
    ): CommonDaoServices.MessageSuccessFailDTO {

        var docDescription: String;

        val application = publicReviewDraftRepository.findByIdOrNull(prdId)
            ?: throw Exception("APPLICATION DOES NOT EXIST")

        docFile.forEach { u ->
            val upload = DatKebsSdStandardsEntity()
            with(upload) {
                sdDocumentId = application.id
                documentTypeDef = type

            }
            docDescription = "PRD Document"

            committeeService.uploadSDFileCommittee(
                upload,
                u,
                "PRD DOCUMENT",
                application.id,
                docDescription

            )
        }

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.message = "Preliminary Draft Document Uploaded successfully"

        return sm
    }

    //public review draft upload other Public Review Draft Document
    @PostMapping("/upload/editedPrd")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun uploadEditedPrdDocument(
        @RequestParam("prdId") prdId: Long,
        @RequestParam("docFile") docFile: List<MultipartFile>,
        @RequestParam("type") type: String,

        model: Model
    ): CommonDaoServices.MessageSuccessFailDTO {

        var docDescription: String;

        val application = publicReviewDraftRepository.findByIdOrNull(prdId)
            ?: throw Exception("APPLICATION DOES NOT EXIST")

        docFile.forEach { u ->
            val upload = DatKebsSdStandardsEntity()
            with(upload) {
                sdDocumentId = application.id
                documentTypeDef = type

            }
            docDescription = "EDITED PRD Document"

            committeeService.uploadSDFileCommittee(
                upload,
                u,
                "PRD DOCUMENT",
                application.id,
                docDescription

            )
        }

        val sm = CommonDaoServices.MessageSuccessFailDTO()
        sm.message = "Edited Preliminary Draft Document Uploaded successfully"

        return sm
    }


    @GetMapping("/getAllPrds")
    fun getPrs(): MutableList<PrdWithUserName> {
        return publicReviewService.getAllPrd()
    }

    @GetMapping("/getAllDocumentsOnPrd")
    fun getAllPrdDocuments(@RequestParam("publicReviewDraftId") publicReviewDraftId: Long): Collection<DatKebsSdStandardsEntity?>? {
        return publicReviewService.getAllPrdDocuments(publicReviewDraftId)
    }

    @PostMapping("/sendToOrganisation")
    fun sendToOrganisation(@RequestBody publicReviewDraft: PublicReviewDraft)
            : ServerResponse {
        return ServerResponse(
            HttpStatus.OK,
            "Public Review Draft Sent To Organisation",
            publicReviewService.sendToOrganisation(publicReviewDraft)
        )
    }
    @PostMapping("/sendPublicReview")
    @ResponseBody
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    fun sendPublicReview(@RequestBody publicReviewDto: PublicReviewDto): ServerResponse{

        return ServerResponse(HttpStatus.OK,"Successfully uploaded Adoption proposal",publicReviewService.sendPublicReview(publicReviewDto))
    }

    @PostMapping("/sendToDepartments")
    fun sendToDepartments(
        @RequestBody department: Department,
        @RequestParam("publicReviewDraftId") publicReviewDraftId: Long
    )
            : ServerResponse {
        return ServerResponse(
            HttpStatus.OK,
            "Public Review Draft Sent To Departments",
            publicReviewService.sendToDepartments(department, publicReviewDraftId)
        )
    }

    @PostMapping("/postToWebsite")
    fun postToWebsite(@RequestBody publicReviewDraft: PublicReviewDraft)
            : ServerResponse {
        return ServerResponse(
            HttpStatus.OK,
            "Public Review Draft Posted To Website",
            publicReviewService.postToWebsite(publicReviewDraft)
        )
    }

    @PostMapping("/anonymous/makeComment")
    fun makeCommentUnLoggedInUsers(@RequestBody comments: Comments): ServerResponse {
        return ServerResponse(
            HttpStatus.OK,
            "Comment Approved",
            publicReviewService.makeCommentUnLoggedInUsers(comments)
        )
    }


    @GetMapping("/getAllCommentsOnPrd")
    fun getAllCommentsOnPrd(@RequestParam("publicReviewDraftId") publicReviewDraftId: Long): List<CommentsWithPrdId> {
        return publicReviewService.getAllCommentsOnPrd(publicReviewDraftId)
    }

    @GetMapping("/getUserLoggedInCommentsOnPrd")
    fun getUserLoggedInCommentsOnCD(): List<CommentsWithPrdId> {
        return publicReviewService.getUserLoggedInCommentsOnCD()
    }

    @PostMapping("/editPrd")
    fun editPrd(@RequestParam("publicReviewDraftId") publicReviewDraftId: Long, @RequestBody publicReviewDraft: PublicReviewDraft)
            : ServerResponse {
        return ServerResponse(
            HttpStatus.OK,
            "Public Review Draft Edited",
            publicReviewService.uploadEditedDraft(publicReviewDraft,publicReviewDraftId)
        )
    }

    @PostMapping("/approvePrd")
    fun approvePrd(@RequestBody publicReviewDraft: PublicReviewDraft)
            : ServerResponse {
        return ServerResponse(
            HttpStatus.OK,
            "Public Review Draft Approved",
            publicReviewService.approvePrd(publicReviewDraft)
        )
    }


    @GetMapping("/getAllApprovedPrds")
    fun getApprovedPrs(): MutableList<PrdWithUserName> {
        return publicReviewService.getAllPrdApproved()
    }



}
