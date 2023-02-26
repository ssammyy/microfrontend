package org.kebs.app.kotlin.apollo.api.controllers.stdController

import org.kebs.app.kotlin.apollo.api.ports.provided.dao.std.StdReviewService
import org.kebs.app.kotlin.apollo.common.dto.std.*
import org.kebs.app.kotlin.apollo.store.model.std.*
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/v1/migration")
class stdReviewController(
    val stdReviewService: StdReviewService
)
{
    @PreAuthorize("hasAuthority('TC_SEC_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/standard_review/getStandardsForReview")
    @ResponseBody
    fun getStandardsForReview(): MutableList<ReviewStandards>
    {
        return stdReviewService.getStandardsForReview()
    }

    //********************************************************** Start Review Process **********************************************************
    @PreAuthorize("hasAuthority('TC_SEC_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/standard_review/standardReviewForm")
    @ResponseBody
    fun standardReviewForm(@RequestBody standardReviewDto: StandardReviewDto): ServerResponse
    {
        return ServerResponse(HttpStatus.OK,"Successfully Uploaded Form",stdReviewService.standardReviewForm(standardReviewDto))
    }

    @GetMapping("/standard_review/getProposal")
    @ResponseBody
    fun getProposal(): MutableList<StandardReview>
    {
        return stdReviewService.getProposal()
    }

    @GetMapping("/anonymous/standard_review/getProposals")
    @ResponseBody
    fun getProposals(@RequestParam("reviewId") reviewId: Long): MutableList<StandardReview>
    {
        return stdReviewService.getProposals(reviewId)
    }

    //@PreAuthorize("hasAuthority('STAKEHOLDERS_SD_MODIFY')")
    @PostMapping("/standard_review/submitProposalComments")
    @ResponseBody
    fun submitProposalComments(@RequestBody st: StandardReviewCommentDto): ServerResponse{
        return ServerResponse(HttpStatus.OK,"Successfully Uploaded Form",stdReviewService.submitAPComments(st))

    }

    @PostMapping("/anonymous/standard_review/submitProposalComments")
    @ResponseBody
    fun submitProposalComment(@RequestBody st: StandardReviewCommentDto): ServerResponse{
        return ServerResponse(HttpStatus.OK,"Successfully Uploaded Form",stdReviewService.submitAPComments(st))

    }

    @GetMapping("/standard_review/getProposalsComments")
    @ResponseBody
    fun getProposalsComments(@RequestParam("reviewId") reviewId: Long): MutableList<SDReviewComments>
    {
        return stdReviewService.getProposalsComments(reviewId)
    }

    @PreAuthorize("hasAuthority('TC_SEC_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/standard_review/getStandardsForRecommendation")
    @ResponseBody
    fun getStandardsForRecommendation(): MutableList<StandardReview>
    {
        return stdReviewService.getStandardsForRecommendation()
    }

    @PostMapping("/standard_review/makeRecommendationsOnAdoptionProposal")
    @ResponseBody
    fun makeRecommendationsOnAdoptionProposal(@RequestBody st: StandardReviewRecommendationDto): ServerResponse{
        return ServerResponse(HttpStatus.OK,"Successful Analysis",stdReviewService.makeRecommendationsOnAdoptionProposal(st))

    }

    @PreAuthorize("hasAuthority('TC_SEC_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/standard_review/getStandardsForSpcAction")
    @ResponseBody
    fun getStandardsForSpcAction(): MutableList<StandardReview>
    {
        return stdReviewService.getStandardsForSpcAction()
    }

    @PostMapping("/standard_review/decisionOnStdDraft")
    @ResponseBody
    fun decisionOnStdDraft(@RequestBody sp: SpcStandardReviewCommentDto): ServerResponse{
        return ServerResponse(HttpStatus.OK,"Success",stdReviewService.decisionOnStdDraft(sp))

    }

    @PreAuthorize("hasAuthority('EDITOR_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/standard_review/getStdForEditing")
    @ResponseBody
    fun getStdForEditing(): MutableList<ComStandard>
    {
        return stdReviewService.getStdForEditing()
    }

    @PreAuthorize("hasAuthority('EDITOR_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/standard_review/submitDraftForEditing")
    @ResponseBody
    fun submitDraftForEditing(@RequestBody isDraftDto: CSDraftDto): ServerResponse
    {
        return ServerResponse(
            HttpStatus.OK,"Saved",stdReviewService.
            submitDraftForEditing(isDraftDto))
    }




}