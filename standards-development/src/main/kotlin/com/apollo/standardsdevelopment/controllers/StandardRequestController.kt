package org.kebs.app.kotlin.apollo.standardsdevelopment.controllers

import org.kebs.app.kotlin.apollo.standardsdevelopment.dto.ProcessInstanceResponse
import org.kebs.app.kotlin.apollo.standardsdevelopment.dto.TaskDetails
import org.kebs.app.kotlin.apollo.standardsdevelopment.models.*
import org.kebs.app.kotlin.apollo.standardsdevelopment.services.StandardRequestService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/standard")
class StandardRequestController(val standardRequestService: StandardRequestService) {

    //********************************************************** deployment endpoints **********************************************************
    @PostMapping("/deploy")
    fun deployWorkflow(){
        standardRequestService.deployProcessDefinition()
    }

    //********************************************************** process upload standard request **********************************************************
    @PostMapping("/request")
    fun requestForStandard(@RequestBody standardRequest: StandardRequest): ProcessInstanceResponse{
        return standardRequestService.requestForStandard(standardRequest)
    }


    @GetMapping("/getProducts")
    fun getProducts(): MutableList<Product>
    {
        return standardRequestService.getProducts();
    }


    @GetMapping("/getHOFTasks")
    fun getHOFTasks():List<TaskDetails>
    {
        return standardRequestService.getHOFTasks()
    }

    @GetMapping("/process/{processId}")
    fun checkState(@PathVariable("processId") processId: String?) {
        standardRequestService.checkProcessHistory(processId)
    }

    @GetMapping("/hof/review/{taskId}")
    fun hofReview(@PathVariable("taskId") taskId: String?)
    {
        standardRequestService.hofReview(taskId)
    }

    @GetMapping("/getTCSECTasks")
    fun getTCSECTasks():List<TaskDetails>
    {
        return standardRequestService.getTCSECTasks()
    }


    @PostMapping("/uploadNWI/{taskId}")
    fun uploadNWI(@RequestBody standardNWI: StandardNWI, @PathVariable("taskId") taskId: String?) {
        standardRequestService.uploadNWI(standardNWI, taskId)
    }

    @GetMapping("/getTCTasks")
    fun getTCTasks():List<TaskDetails>
    {
        return standardRequestService.getTCTasks()
    }


    @PostMapping("/decisionOnNWI/{taskId}/{approved}")
    fun decisionOnNWI(@PathVariable("taskId") taskId: String, @PathVariable("approved") approved: Boolean)
    {
        standardRequestService.decisionOnNWI(taskId, approved)
    }


    @GetMapping("/tc-sec/tasks")
    fun getTCSecTasks(): List<TaskDetails> {
        return standardRequestService.getTCSecTasks()
    }

    @PostMapping("/uploadJustification/{taskId}")
    fun uploadJustification(@RequestBody standardJustification: StandardJustification, @PathVariable("taskId") taskId: String?) {
        standardRequestService.uploadJustification(standardJustification, taskId)
    }

    @GetMapping("/spc-sec/tasks")
    fun getSPCSecTasks(): List<TaskDetails> {
        return standardRequestService.getSPCSecTasks()
    }

    @PostMapping("/decisionOnJustification/{taskId}/{spc_approved}")
    fun decisionOnJustification(@PathVariable("taskId") taskId: String, @PathVariable("spc_approved") approved: Boolean)
    {
        standardRequestService.decisionOnJustification(taskId, approved)
    }

    @GetMapping("/tc-sec/tasks/workplan")
    fun getTCSecTasksWorkPlan(): List<TaskDetails> {
        return standardRequestService.getTCSecTasksWorkPlan()
    }

    @PostMapping("/uploadWorkPlan/{taskId}")
    fun uploadWorkPlan(@RequestBody standardWorkPlan: StandardWorkPlan, @PathVariable("taskId") taskId: String?) {
        standardRequestService.uploadWorkPlan(standardWorkPlan, taskId)
    }

}
