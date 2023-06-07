package org.kebs.app.kotlin.apollo.standardsdevelopment.controllers

import org.kebs.app.kotlin.apollo.standardsdevelopment.dto.ProcessInstanceResponse
import org.kebs.app.kotlin.apollo.standardsdevelopment.dto.TaskDetails
import org.kebs.app.kotlin.apollo.standardsdevelopment.models.PublicReviewDraft
import org.kebs.app.kotlin.apollo.standardsdevelopment.models.PublicReviewDraftComments
import org.kebs.app.kotlin.apollo.standardsdevelopment.services.PublicReviewService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@CrossOrigin(origins = ["http://localhost:4200"])

@RestController
@RequestMapping("/publicreview")

class PublicReviewController(val publicReviewService: PublicReviewService) {
    //********************************************************** deployment endpoints **********************************************************
    @PostMapping("/deploy_publicreview")
    fun deployWorkflow() {
        publicReviewService.deployProcessDefinition()
    }

    @PostMapping("/preparepr")
    fun preparePublicReview(@RequestBody publicReviewDraft: PublicReviewDraft): ProcessInstanceResponse {
        return publicReviewService.preparePublicReview(publicReviewDraft)
    }


    @PostMapping("/commentpr")
    fun makeCommentOnDraft(@RequestBody publicReviewDraftComments: PublicReviewDraftComments): ProcessInstanceResponse {
        return publicReviewService.makeCommentOnDraft(publicReviewDraftComments)
    }

    @GetMapping("/getprs")
    fun getPrs(): MutableList<PublicReviewDraft> {
        return publicReviewService.getPrs()
    }

    @GetMapping("/getprcomments")
    fun getPrComments(): MutableList<PublicReviewDraftComments> {
        return publicReviewService.getPrComments()
    }

    @GetMapping("/getTcTasks")
    fun getSacSecTasks(): List<TaskDetails> {
        return publicReviewService.getTCTasks()
    }

    @GetMapping("/publicreviewdraft/{id}")
    fun getPublicReviewDraftById(@PathVariable("id") id: Long): ResponseEntity<PublicReviewDraft?>? {
        return publicReviewService.getPublicReviewDraftById(id)
    }


}
