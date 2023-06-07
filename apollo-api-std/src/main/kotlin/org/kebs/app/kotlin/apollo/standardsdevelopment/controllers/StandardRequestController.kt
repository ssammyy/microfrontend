package org.kebs.app.kotlin.apollo.standardsdevelopment.controllers

import org.kebs.app.kotlin.apollo.standardsdevelopment.dto.Decision
import org.kebs.app.kotlin.apollo.standardsdevelopment.dto.ID
import org.kebs.app.kotlin.apollo.standardsdevelopment.dto.ServerResponse
import org.kebs.app.kotlin.apollo.standardsdevelopment.dto.TaskDetails
import org.kebs.app.kotlin.apollo.standardsdevelopment.models.*
import org.kebs.app.kotlin.apollo.standardsdevelopment.services.StandardRequestService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/standard")
class StandardRequestController(val standardRequestService: StandardRequestService) {

    //********************************************************** deployment endpoints **********************************************************
    @PostMapping("/deploy")
    fun deployWorkflow(): ServerResponse {
        standardRequestService.deployProcessDefinition()
        return ServerResponse(HttpStatus.OK,"Successfully deployed server", HttpStatus.OK)
    }

    //********************************************************** process upload standard request **********************************************************
    @PostMapping("/request")
    @ResponseBody
    fun requestForStandard(@RequestBody standardRequest: StandardRequest): ServerResponse{
        //return ResponseEntity(standardRequestService.requestForStandard(standardRequest), HttpStatus.OK)
        return ServerResponse(HttpStatus.OK,"Successfully uploaded standard request",standardRequestService.requestForStandard(standardRequest))
    }

    @GetMapping("/getProducts")
    @ResponseBody
    fun getProducts(): MutableList<Product>
    {
        return standardRequestService.getProducts()
    }

   /* @GetMapping("/getProducts")
    @ResponseBody
    fun getProducts(): ServerResponse
    {
         return ServerResponse(HttpStatus.OK,"Successfully returned products",standardRequestService.getProducts());
    }*/

    @GetMapping("/getProductCategories/{productId}")
    @ResponseBody
    fun getProductCategories(@PathVariable("productId") productId: String?): MutableList<ProductSubCategory>
    {
        return standardRequestService.getProductCategories(productId)
    }

    /*@PostMapping("/getProductCategories")
    @ResponseBody
    fun getProductCategories(@RequestBody id: ID): ServerResponse
    {
        return ServerResponse(HttpStatus.OK,"Successfully returned product categories",standardRequestService.getProductCategories(id));
    }*/


    @GetMapping("/getDepartments")
    @ResponseBody
    fun getDepartments(): MutableList<Department>
    {
        return standardRequestService.getDepartments()
    }

    /*@GetMapping("/getDepartments")
    @ResponseBody
    fun getDepartments(): ServerResponse
    {
        return ServerResponse(HttpStatus.OK,"Successfully returned departments",standardRequestService.getDepartments());
    }*/

    @GetMapping("/getTechnicalCommittee")
    @ResponseBody
    fun getTechnicalCommittee(): MutableList<TechnicalCommittee>
    {
        return standardRequestService.getTechnicalCommittee()
    }

    /*@GetMapping("/getTechnicalCommittee")
    @ResponseBody
    fun getTechnicalCommittee(): ServerResponse
    {
        return ServerResponse(HttpStatus.OK,"Successfully returned technical committee",standardRequestService.getTechnicalCommittee());
    }*/

    @GetMapping("/getHOFTasks")
    fun getHOFTasks():List<TaskDetails>
    {
        return standardRequestService.getHOFTasks()
    }

    @PostMapping("/process")
    @ResponseBody
    fun checkState( @RequestBody id: ID): ServerResponse {
        return ServerResponse(HttpStatus.OK,"Successfully returned process history",standardRequestService.checkProcessHistory(id))
    }

    @PostMapping("/hof/review")
    @ResponseBody
    fun hofReview(@RequestBody id: ID): ServerResponse
    {
        return ServerResponse(HttpStatus.OK,"Successfully completed HOF review",standardRequestService.hofReview(id))
    }

    @GetMapping("/getTCSECTasks")
    fun getTCSECTasks():List<TaskDetails>
    {
        return standardRequestService.getTCSECTasks()
    }


    @PostMapping("/uploadNWI")
    @ResponseBody
    fun uploadNWI(@RequestBody standardNWI: StandardNWI): ServerResponse
    {
        return ServerResponse(HttpStatus.OK,"Upload new work item",standardRequestService.uploadNWI(standardNWI))
    }

    @GetMapping("/getTCTasks")
    fun getTCTasks():List<TaskDetails>
    {
        return standardRequestService.getTCTasks()
    }


    @PostMapping("/decisionOnNWI")
    @ResponseBody
    fun decisionOnNWI(@RequestBody decision: Decision): ServerResponse
    {
        return ServerResponse(HttpStatus.OK,"Decision on New Work Item by the TC",standardRequestService.decisionOnNWI(decision))
    }


    @GetMapping("/tc-sec/tasks")
    fun getTCSecTasks(): List<TaskDetails> {
        return standardRequestService.getTCSecTasks()
    }

    @PostMapping("/uploadJustification")
    @ResponseBody
    fun uploadJustification(@RequestBody standardJustification: StandardJustification): ServerResponse {
        return ServerResponse(HttpStatus.OK,"Successfully uploaded justification by the TC",standardRequestService.uploadJustification(standardJustification))
    }

    @GetMapping("/spc-sec/tasks")
    fun getSPCSecTasks(): List<TaskDetails> {
        return standardRequestService.getSPCSecTasks()
    }

    @PostMapping("/decisionOnJustification")
    @ResponseBody
    fun decisionOnJustification(@RequestBody decision: Decision): ServerResponse
    {
        return ServerResponse(HttpStatus.OK,"Submitted decision on justification",standardRequestService.decisionOnJustification(decision))
    }

    @GetMapping("/tc-sec/tasks/workplan")
    fun getTCSecTasksWorkPlan(): List<TaskDetails> {
        return standardRequestService.getTCSecTasksWorkPlan()
    }

    @PostMapping("/uploadWorkPlan")
    @ResponseBody
    fun uploadWorkPlan(@RequestBody standardWorkPlan: StandardWorkPlan): ServerResponse {
        return ServerResponse(HttpStatus.OK,"Successfully uploaded workplan",standardRequestService.uploadWorkPlan(standardWorkPlan))
    }


}
