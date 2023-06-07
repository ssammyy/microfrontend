package org.kebs.app.kotlin.apollo.api.controllers.stdController

import org.kebs.app.kotlin.apollo.api.ports.provided.dao.std.StandardReviewFormService
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.std.StandardReviewService
import org.kebs.app.kotlin.apollo.common.dto.std.*
import org.kebs.app.kotlin.apollo.store.model.std.*
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.util.stream.Collectors


@RestController
@RequestMapping("api/v1/migration/standard_review/feel")
class StandardReviewController(
    val standardReviewService: StandardReviewService,
    val standardReviewFormService: StandardReviewFormService
    ) {

    //********************************************************** deployment endpoints **********************************************************
    @PostMapping("/deploy")
    fun deployWorkflow(): ServerResponse {
        standardReviewService.deployProcessDefinition()
        return ServerResponse(HttpStatus.OK,"Successfully deployed server", HttpStatus.OK)
    }

    //********************************************************** get SPC SEC Tasks **********************************************************
    @PreAuthorize("hasAuthority('SPC_SEC_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/getSpcSecTasks")
    fun getSpcSecTasks():List<StandardReviewTasks>
    {
        return standardReviewService.getSpcSecTasks()
    }

    //********************************************************** get HOP Tasks **********************************************************
    @PreAuthorize("hasAuthority('HOP_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/getHopTasks")
    fun getHopTasks():List<StandardReviewTasks>
    {
        return standardReviewService.getHopTasks()
    }

    //********************************************************** get SAC SEC Tasks **********************************************************
    @PreAuthorize("hasAuthority('SAC_SEC_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/getSacSecTasks")
    fun getSacSecTasks():List<StandardReviewTasks>
    {
        return standardReviewService.getSacSecTasks()
    }
    //********************************************************** get HO SIC Tasks **********************************************************
    @PreAuthorize("hasAuthority('HO_SIC_SD') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/getHoSicTasks")
    fun getHoSicTasks():List<StandardReviewTasks>
    {
        return standardReviewService.getHoSicTasks()
    }


    //********************************************************** get Editor Tasks **********************************************************
    @PreAuthorize("hasAuthority('EDITOR_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/getEditorTasks")
    fun getEditorTasks():List<StandardReviewTasks>
    {
        return standardReviewService.getEditorTasks()
    }

    //********************************************************** get Proof-Reader Tasks **********************************************************
    @PreAuthorize("hasAuthority('PROOFREADER_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/getProofReaderTasks")
    fun getProofReaderTasks():List<StandardReviewTasks>
    {
        return standardReviewService.getProofReaderTasks()
    }

    //********************************************************** get Draughts-Man Tasks **********************************************************
    @PreAuthorize("hasAuthority('DRAUGHTSMAN_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/getDraughtsManTasks")
    fun getDraughtsManTasks():List<StandardReviewTasks>
    {
        return standardReviewService.getDraughtsManTasks()
    }

    //********************************************************** get Draughts-Man Tasks **********************************************************
    @PreAuthorize("hasAuthority('TC_SEC_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/getTcSecTasks")
    fun getTcSecTasks():List<StandardReviewTasks>
    {
        return standardReviewService.getTcSecTasks()
    }

    @PreAuthorize("hasAuthority('TC_SEC_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/getStandardsForReview")
    @ResponseBody
    fun getStandardsForReview(): MutableList<ReviewStandards>
    {
        return standardReviewService.getStandardsForReview()
    }

    //********************************************************** Start Review Process **********************************************************
    @PreAuthorize("hasAuthority('TC_SEC_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/standardReviewForm")
    @ResponseBody
    fun standardReviewForm(@RequestBody standardReview: StandardReview): ServerResponse
    {
        return ServerResponse(HttpStatus.OK,"Successfully Uploaded Form",standardReviewService.standardReviewForm(standardReview))
    }

    @PreAuthorize("hasAuthority('TC_SEC_SD_READ') or hasAuthority('SPC_SEC_SD_READ')" +
            " or hasAuthority('STAKEHOLDERS_SD_READ') or hasAuthority('SAC_SEC_SD_READ')" +
            " or hasAuthority('TC_SEC_SD_READ') or hasAuthority('HOP_SD_READ') or" +
            " hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')  ")
    @GetMapping("/getUserTasks")
    fun getUserTasks():List<StandardReviewTasks>
    {
        return standardReviewService.getUserTasks()
    }

   // @PreAuthorize("hasAuthority('TC_SEC_SD_READ') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @GetMapping("/getStandardsProposalForComment")
    @ResponseBody
    fun getStandardsProposalForComment(): MutableList<ReviewStandards>
    {
        return standardReviewService.getStandardsProposalForComment()
    }

    //@PreAuthorize("hasAuthority('STAKEHOLDERS_SD_MODIFY')")
    @PostMapping("/SubmitAPComments")
    @ResponseBody
    fun submitAPComments(@RequestBody standardReviewProposalComments: StandardReviewProposalComments): ServerResponse{
        return ServerResponse(HttpStatus.OK,"Comment Has been submitted",standardReviewService.submitAPComments(standardReviewProposalComments))
    }

    @GetMapping("/getStandardsProposalComments")
    fun getStandardsProposalComments(@RequestParam("proposalId") proposalId: Long):MutableIterable<ReviewStandards>?
    {
        return standardReviewService.getStandardsProposalComments(proposalId)
    }

    //@PreAuthorize("hasAuthority('STAKEHOLDERS_SD_MODIFY')")
    @PostMapping("/makeRecommendationsOnAdoptionProposal")
    @ResponseBody
    fun makeRecommendationsOnAdoptionProposal(@RequestBody standardReviewProposalRecommendations: StandardReviewProposalRecommendations): ServerResponse {
        return ServerResponse(
            HttpStatus.OK,
            "Comment Has been submitted",
            standardReviewService.makeRecommendationsOnAdoptionProposal(standardReviewProposalRecommendations)
        )
    }

    @GetMapping("/getUserComments")
    fun getUserComments(@RequestParam("id") id: Long):MutableIterable<ReviewStandardRemarks>?
    {
        return standardReviewService.getUserComments(id)
    }

    //decision on Adoption Recommendation
    // @PreAuthorize("hasAuthority('TC_SEC_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/decisionOnRecommendation")
    fun decisionOnRecommendation(@RequestBody reviewDecision: ReviewDecision,
                                 reviewStandardRemarks: ReviewStandardRemarks) : List<StandardReviewTasks>
    {
        return standardReviewService.decisionOnRecommendation(reviewDecision,reviewStandardRemarks)
    }

    //Level Two Decision
    @PostMapping("/levelUpDecisionOnRecommendations")
    fun levelUpDecisionOnRecommendations(
        @RequestBody reviewDecision: ReviewDecision,
        reviewStandardRemarks: ReviewStandardRemarks,
        standard: Standard
    ): List<StandardReviewTasks> {
        return standardReviewService.levelUpDecisionOnRecommendations(
            reviewDecision,
            reviewStandardRemarks,
            standard
        )
    }

    //********************************************************** Submit Review Comments **********************************************************
    //@PreAuthorize("hasAuthority('STAKEHOLDERS_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/updateGazette")
    @ResponseBody
    fun updateGazette(@RequestBody reviewStandardRemarks: ReviewStandardRemarks,
                      standard:Standard,reviewDecision: ReviewDecision): ServerResponse
    {
        return ServerResponse(HttpStatus.OK,"Successfully Submitted Comments",standardReviewService.updateGazette(reviewStandardRemarks,standard,reviewDecision))
    }

    @PostMapping("/updateGazettementDate")
    @ResponseBody
    fun updateGazettementDate(@RequestBody nWAGazettement: NWAGazettement,gazzettementDecision: GazzettementDecision): ServerResponse
    {
        return ServerResponse(HttpStatus.OK,"Successfully Submitted Comments",standardReviewService.updateGazettementDate(nWAGazettement,gazzettementDecision))
    }

    @PostMapping("/submitDraftForEditing")
    @ResponseBody
    fun submitDraftForEditing(@RequestBody iSAdoptionJustification: ISAdoptionJustification): ServerResponse
    {
        return ServerResponse(HttpStatus.OK,"Successfully Submitted Comments",standardReviewService.submitDraftForEditing(iSAdoptionJustification))
    }

    //Level Two Decision
    @PostMapping("/checkRequirements")
    fun checkRequirements(
        @RequestBody reviewDecision: ReviewDecision,
        reviewStandardRemarks: ReviewStandardRemarks
    ): List<StandardReviewTasks> {
        return standardReviewService.checkRequirements(
            reviewDecision,
            reviewStandardRemarks
        )
    }

    @PostMapping("/editStandardDraft")
    @ResponseBody
    fun editStandardDraft(@RequestBody iSAdoptionJustification: ISAdoptionJustification,
                          iSDraftStdUpload:ISDraftStdUpload): ServerResponse
    {
        return ServerResponse(HttpStatus.OK,"Successfully Submitted Comments",standardReviewService.editStandardDraft(iSAdoptionJustification,iSDraftStdUpload))
    }

    @PostMapping("/draftStandard")
    @ResponseBody
    fun draftStandard(@RequestBody iSAdoptionJustification: ISAdoptionJustification,
                      iSDraftStdUpload:ISDraftStdUpload): ServerResponse
    {
        return ServerResponse(HttpStatus.OK,"Successfully Submitted Comments",standardReviewService.draftStandard(iSAdoptionJustification,iSDraftStdUpload))
    }

    @PostMapping("/proofReadStandard")
    @ResponseBody
    fun proofReadStandard(@RequestBody iSAdoptionJustification: ISAdoptionJustification,
                      iSDraftStdUpload:ISDraftStdUpload): ServerResponse
    {
        return ServerResponse(HttpStatus.OK,"Successfully Submitted Comments",standardReviewService.proofReadStandard(iSAdoptionJustification,iSDraftStdUpload))
    }

    @PostMapping("/checkStandardDraft")
    fun checkStandardDraft(
        @RequestBody reviewDecision: ReviewDecision,
        reviewStandardRemarks: ReviewStandardRemarks
    ): List<StandardReviewTasks> {
        return standardReviewService.checkStandardDraft(
            reviewDecision,
            reviewStandardRemarks
        )
    }



    //********************************************************** Make Recommendations **********************************************************
    @PreAuthorize("hasAuthority('TC_SEC_SD_MODIFY') or hasAuthority('STANDARDS_DEVELOPMENT_FULL_ADMIN')")
    @PostMapping("/reviewRecommendations")
    @ResponseBody
    fun reviewRecommendations(@RequestBody standardReviewRecommendations: StandardReviewRecommendations): ServerResponse
    {
        return ServerResponse(HttpStatus.OK,"Successfully Submitted Recommendations",standardReviewService.reviewRecommendations(standardReviewRecommendations))
    }

}
