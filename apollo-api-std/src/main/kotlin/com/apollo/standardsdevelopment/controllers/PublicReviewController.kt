package com.apollo.standardsdevelopment.controllers

import com.apollo.standardsdevelopment.dto.ProcessInstanceResponse
import com.apollo.standardsdevelopment.dto.TaskDetails
import com.apollo.standardsdevelopment.models.*
import com.apollo.standardsdevelopment.services.PublicReviewService
import org.springframework.web.bind.annotation.*

import org.springframework.web.bind.annotation.PathVariable

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.http.ResponseEntity


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
        return publicReviewService.getPublicReviewDraftById(id);
    }


}
