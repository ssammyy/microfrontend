package org.kebs.app.kotlin.apollo.standardsdevelopment.controllers

import org.kebs.app.kotlin.apollo.standardsdevelopment.dto.Decision
import org.kebs.app.kotlin.apollo.standardsdevelopment.dto.ServerResponse
import org.kebs.app.kotlin.apollo.standardsdevelopment.dto.TaskDetails
import org.kebs.app.kotlin.apollo.standardsdevelopment.models.DecisionFeedback
import org.kebs.app.kotlin.apollo.standardsdevelopment.models.JustificationForTC
import org.kebs.app.kotlin.apollo.standardsdevelopment.services.FormationOfTCService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/formationOfTC")
class FormationOfTCController(val formationOfTCService: FormationOfTCService) {

    //********************************************************** deployment endpoints ********************************************//
    @PostMapping("/deploy")
    fun deployWorkflow(): ServerResponse {
        formationOfTCService.deployProcessDefinition()
        return ServerResponse(HttpStatus.OK,"Successfully deployed server", HttpStatus.OK)
    }

    //***************************************************** submit justification for formation of TC ***************************//
    @PostMapping("/submitJustification")
    @ResponseBody
    fun submitJustificationForFormationOfTC(@RequestBody justificationForTC: JustificationForTC): ServerResponse{
        //return ResponseEntity(standardRequestService.requestForStandard(standardRequest), HttpStatus.OK)
        return ServerResponse(HttpStatus.OK,"Successfully submitted justification for formation of TC",formationOfTCService.submitJustificationForFormationOfTC(justificationForTC))
    }

    @GetMapping("/getSPCTasks")
    fun getSPCTasks():List<TaskDetails>
    {
        return formationOfTCService.getSPCTasks()
    }

    @PostMapping("/decisionOnJustificationForTC")
    @ResponseBody
    fun decisionOnJustificationForTC(@RequestBody decision: Decision): ServerResponse
    {
        return ServerResponse(HttpStatus.OK,"Decision on justification for formation of TC",formationOfTCService.decisionOnJustificationForTC(decision))
    }

    @PostMapping("/uploadRejectionFeedback")
    @ResponseBody
    fun uploadFeedbackOnJustification(@RequestBody decisionFeedback: DecisionFeedback): ServerResponse {
        return ServerResponse(HttpStatus.OK,"Successfully uploaded feedback on draft",formationOfTCService.uploadFeedbackOnJustification(decisionFeedback))
    }

    @GetMapping("/getSACTasks")
    fun getSACTasks():List<TaskDetails>
    {
        return formationOfTCService.getSACTasks()
    }

    @PostMapping("/decisionOnSPCFeedback")
    @ResponseBody
    fun decisionOnSPCFeedback(@RequestBody decision: Decision): ServerResponse
    {
        return ServerResponse(HttpStatus.OK,"Decision on SPC feedback",formationOfTCService.decisionOnSPCFeedback(decision))
    }

    @PostMapping("/uploadRejectionFeedbackBySAC")
    @ResponseBody
    fun uploadFeedbackOnJustificationBySAC(@RequestBody decisionFeedback: DecisionFeedback): ServerResponse {
        return ServerResponse(HttpStatus.OK,"Successfully uploaded feedback on SAC",formationOfTCService.uploadFeedbackOnJustification(decisionFeedback))
    }



}
