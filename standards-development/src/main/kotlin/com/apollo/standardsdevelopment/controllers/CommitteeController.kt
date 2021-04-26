package com.apollo.standardsdevelopment.controllers

import com.apollo.standardsdevelopment.dto.ProcessInstanceResponse
import com.apollo.standardsdevelopment.dto.TaskDetails
import com.apollo.standardsdevelopment.models.*
import com.apollo.standardsdevelopment.services.CommitteeService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/committee")

class CommitteeController(val committeeService: CommitteeService) {
    //********************************************************** deployment endpoints **********************************************************
    @PostMapping("/deploy_committee")
    fun deployWorkflow() {
        committeeService.deployProcessDefinition()
    }

    @PostMapping("/prepareNWI")
    fun prepareNWI(@RequestBody committeeNWI: CommitteeNWI): ProcessInstanceResponse {
        return committeeService.prepareNWI(committeeNWI)
    }

    @PostMapping("/approveNWI/{taskId}/{approved}")
    fun approve(@PathVariable("taskId") taskId: String, @PathVariable("approved") approved: Boolean) {
        committeeService.approveNWI(taskId, approved)
    }

    @PostMapping("/uploaddrafts/{taskId}")
    fun uploadDrafts(@RequestBody committeeDrafts: CommitteeDrafts, @PathVariable("taskId") taskId: String?) {
        committeeService.uploadDrafts(committeeDrafts, taskId)
    }

    @PostMapping("/preparePD/{taskId}")
    fun preparePD(@RequestBody committeePD: CommitteePD, @PathVariable("taskId") taskId: String?) {
        return committeeService.preparePD(committeePD, taskId)
    }

    @PostMapping("/uploaddraftspd/{taskId}")
    fun uploadDraftsPD(@RequestBody committeeDraftsPD: CommitteeDraftsPD, @PathVariable("taskId") taskId: String?) {
        committeeService.uploadDraftsPD(committeeDraftsPD, taskId)
    }

    @PostMapping("/prepareCD/{taskId}")
    fun prepareCD(@RequestBody committeeCD: CommitteeCD, @PathVariable("taskId") taskId: String?) {
        committeeService.prepareCD(committeeCD, taskId)
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
}
