package org.kebs.app.kotlin.apollo.api.controllers.stdController


import org.kebs.app.kotlin.apollo.store.model.std.*
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.std.*
import org.kebs.app.kotlin.apollo.common.dto.std.*
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/v1/migration/membershipToTC")
class MembershipToTCController(val membershipToTCService: MembershipToTCService) {

    //********************************************************** deployment endpoints ********************************************//
    @PostMapping("/anonymous/deploy")
    fun deployWorkflow(): ServerResponse {
        membershipToTCService.deployProcessDefinition()
        return ServerResponse(HttpStatus.OK,"Successfully deployed server", HttpStatus.OK)
    }

    //***************************************************** submit justification for formation of TC ***************************//
    @PostMapping("/submitCallForApplication")
    @ResponseBody
    fun submitCallsForTCMembers(@RequestBody callForTCApplication: CallForTCApplication): ServerResponse{
        //return ResponseEntity(standardRequestService.requestForStandard(standardRequest), HttpStatus.OK)
        return ServerResponse(HttpStatus.OK,"Successfully submitted call for applications",membershipToTCService.submitCallsForTCMembers(callForTCApplication))
    }

    @GetMapping("anonymous/getCallForApplications")
    fun getCallForApplications():List<TaskDetails>
    {
        return membershipToTCService.getCallForApplications()
    }


    @PostMapping("/process")
    @ResponseBody
    fun checkState( @RequestBody id: ID): ServerResponse {
        return ServerResponse(HttpStatus.OK,"Successfully returned process history",membershipToTCService.checkProcessHistory(id))
    }


    @PostMapping("anonymous/submitTCMemberApplication")
    @ResponseBody
    fun submitTCMemberApplication(@RequestBody membershipTCApplication: MembershipTCApplication): ServerResponse{
        return ServerResponse(HttpStatus.OK,"Successfully application to be TC Member",membershipToTCService.submitTCMemberApplication(membershipTCApplication))
    }

    @GetMapping("/getApplicationsForReview")
    fun getApplicationsForReview():List<TaskDetails>
    {
        return membershipToTCService.getApplicationsForReview()
    }

    @PostMapping("/decisionOnApplicantRecommendation")
    @ResponseBody
    fun decisionOnApplicantRecommendation(@RequestBody decisionFeedback: DecisionFeedback): ServerResponse
    {
        return ServerResponse(HttpStatus.OK,"Decision on applications for membership to TC",membershipToTCService.decisionOnApplicantRecommendation(decisionFeedback))
    }

    @GetMapping("/getRecommendationsFromHOF")
    fun getRecommendationsFromHOF():List<TaskDetails>
    {
        return membershipToTCService.getRecommendationsFromHOF()
    }

    @GetMapping("completeSPCReview/{taskId}")
    @ResponseBody
    fun completeSPCReview(@PathVariable("taskId") taskId: String)
    {
        return membershipToTCService.completeSPCReview(taskId);
    }

    @GetMapping("/getRecommendationsFromSPC")
    fun getRecommendationsFromSPC():List<TaskDetails>
    {
        return membershipToTCService.getRecommendationsFromSPC()
    }

    @PostMapping("/decisionOnSPCRecommendation")
    @ResponseBody
    fun decisionOnSPCRecommendation(@RequestBody decisionFeedback: DecisionFeedback): ServerResponse
    {
        return ServerResponse(HttpStatus.OK,"Decision on SPC recommendations",membershipToTCService.decisionOnSPCRecommendation(decisionFeedback))
    }

    @GetMapping("/getTCMemberCreationTasks")
    fun getTCMemberCreationTasks():List<TaskDetails>
    {
        return membershipToTCService.getTCMemberCreationTasks()
    }

    @PostMapping("/saveTCMember")
    @ResponseBody
    fun submitTCMemberApplication(@RequestBody technicalCommitteMember: TechnicalCommitteMember): ServerResponse{
        return ServerResponse(HttpStatus.OK,"Successfully uploaded TC Member",membershipToTCService.saveTCMember(technicalCommitteMember))
    }

}
