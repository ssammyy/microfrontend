package com.apollo.standardsdevelopment.controllers

import com.apollo.standardsdevelopment.dto.Decision
import com.apollo.standardsdevelopment.dto.ServerResponse
import com.apollo.standardsdevelopment.dto.TaskDetails
import com.apollo.standardsdevelopment.models.DecisionFeedback
import com.apollo.standardsdevelopment.models.StandardDraft
import com.apollo.standardsdevelopment.models.StandardJustification
import com.apollo.standardsdevelopment.models.StandardRequest
import com.apollo.standardsdevelopment.services.PublishingService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/publishing")
class PublishingController(val publishingService: PublishingService) {

    //********************************************************** deployment endpoints **********************************************************
    @PostMapping("/deploy")
    fun deployWorkflow(): ServerResponse {
        publishingService.deployProcessDefinition()
        return ServerResponse(HttpStatus.OK,"Successfully deployed server", HttpStatus.OK)
    }

    //********************************************************** process submit draft standard **********************************************************
    @PostMapping("/submit")
    @ResponseBody
    fun requestForStandard(@RequestBody standardDraft: StandardDraft): ServerResponse{
        //return ResponseEntity(standardRequestService.requestForStandard(standardRequest), HttpStatus.OK)
        return ServerResponse(HttpStatus.OK,"Successfully submitted draft standard",publishingService.sumbitDraftStandard(standardDraft))
    }

    @GetMapping("/getHOPTasks")
    fun getHOPTasks():List<TaskDetails>
    {
        return publishingService.getHOPTasks()
    }

    @PostMapping("/decisionOnKSDraft")
    @ResponseBody
    fun decisionOnKSDraft(@RequestBody decision: Decision): ServerResponse
    {
        return ServerResponse(HttpStatus.OK,"Decision on KS draft by HOP",publishingService.decisionOnKSDraft(decision))
    }

    @PostMapping("/uploadRejectionFeedback")
    @ResponseBody
    fun uploadFeedbackOnDraft(@RequestBody decisionFeedback: DecisionFeedback): ServerResponse {
        return ServerResponse(HttpStatus.OK,"Successfully uploaded feedback on draft",publishingService.uploadFeedbackOnDraft(decisionFeedback))
    }

    @GetMapping("/getEditorTasks")
    fun getEditorTasks():List<TaskDetails>
    {
        return publishingService.getEditorTasks()
    }

    @PostMapping("/finishEditingDraft")
    @ResponseBody
    fun editDraftStandard(@RequestBody standardDraft: StandardDraft): ServerResponse {
        return ServerResponse(HttpStatus.OK,"Finished editing draft",publishingService.editDraftStandard(standardDraft))
    }

    @GetMapping("/getProofReaderTasks")
    fun getProofreaderTasks():List<TaskDetails>
    {
        return publishingService.getProofreaderTasks()
    }

    @PostMapping("/finishedProofReading")
    @ResponseBody
    fun decisionOnProofReading(@RequestBody decision: Decision): ServerResponse {
        return ServerResponse(HttpStatus.OK,"Finished checking proof reading",publishingService.decisionOnProofReading(decision))
    }

    @PostMapping("/approvedDraftStandard")
    @ResponseBody
    fun approveDraughtChange(@RequestBody standardDraft: StandardDraft): ServerResponse {
        return ServerResponse(HttpStatus.OK,"Check draught standard and approve change",publishingService.approveDraughtChange(standardDraft))
    }

    @GetMapping("/getDraughtsmanTasks")
    fun getDraughtsmanTasks():List<TaskDetails>
    {
        return publishingService.getDraughtsmanTasks()
    }

    @PostMapping("/uploadDraughtChanges")
    @ResponseBody
    fun uploadDraftStandard(@RequestBody standardDraft: StandardDraft): ServerResponse {
        return ServerResponse(HttpStatus.OK,"Finished draughting changes",publishingService.uploadDraftStandard(standardDraft))
    }

}