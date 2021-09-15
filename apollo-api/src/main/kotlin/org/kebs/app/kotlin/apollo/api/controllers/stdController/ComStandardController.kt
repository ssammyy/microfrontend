package org.kebs.app.kotlin.apollo.api.controllers.stdController

import org.kebs.app.kotlin.apollo.api.ports.provided.dao.std.*
import org.kebs.app.kotlin.apollo.common.dto.std.*
import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import org.kebs.app.kotlin.apollo.store.model.std.*
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
//@CrossOrigin(origins = ["http://localhost:4200"])
@RequestMapping("api/v1/company_standard")
class ComStandardController (val comStandardService: ComStandardService,
                             val standardRequestService: StandardRequestService

) {

    //********************************************************** deployment endpoints **********************************************************
    @PostMapping("/deploy")
    fun deployWorkflow(): ServerResponse {
        comStandardService.deployProcessDefinition()
        return ServerResponse(HttpStatus.OK,"Successfully deployed server", HttpStatus.OK)
    }
//    ********************************************************** deployment endpoints **********************************************************
//    @PostMapping("/startProcessInstance")
//    fun startProcess(): ServerResponse {
//        comStandardService.startProcessInstance()
//        return ServerResponse(HttpStatus.OK,"Successfully Started server", HttpStatus.OK)
//    }

    //********************************************************** process upload standard request **********************************************************
    @PostMapping("/request")
    @ResponseBody
    fun requestForStandard(@RequestBody companyStandardRequest: CompanyStandardRequest): ServerResponse{
        return ServerResponse(HttpStatus.OK,"Successfully uploaded standard request",comStandardService.requestForStandard(companyStandardRequest))
    }

//    @GetMapping("/getProducts")
//    @ResponseBody
//    fun getProducts(): MutableList<Product>
//    {
//        return standardRequestService.getProducts()
//    }
//    @GetMapping("/getProductCategories/{productId}")
//    @ResponseBody
//    fun getProductCategories(@PathVariable("productId") productId: String?): MutableList<ProductSubCategory>
//    {
//        return standardRequestService.getProductCategories(productId)
//    }
@GetMapping("/anonymous/getDepartments")
@ResponseBody
    fun getDepartments(): MutableList<Department>
    {
        return standardRequestService.getDepartments()
    }

    //********************************************************** get HOD Tasks **********************************************************
    @PreAuthorize("hasAuthority('HOD_TWO_SD_READ')")
    @GetMapping("/getHODTasks")
    fun getHODTasks():List<TaskDetails>
    {
        return comStandardService.getHODTasks()
    }

    //********************************************************** process Assign Standard Request **********************************************************
    @PreAuthorize("hasAuthority('HOD_TWO_SD_MODIFY')")
    @PostMapping("/assignRequest")
    @ResponseBody
    fun assignRequest(@RequestBody comStdAction: ComStdAction): ServerResponse{
        return ServerResponse(HttpStatus.OK,"Successfully Assigned request to project leader",comStandardService.assignRequest(comStdAction))
    }

    //********************************************************** get Project Leader Tasks **********************************************************
    @PreAuthorize("hasAuthority('PL_SD_READ')")
    @GetMapping("/getPlTasks")
    fun getPlTasks():List<TaskDetails>
    {
        return comStandardService.getPlTasks()
    }

    //********************************************************** process upload Justification **********************************************************
    @PreAuthorize("hasAuthority('PL_SD_MODIFY')")
    @PostMapping("/prepareJustification")
    @ResponseBody
    fun prepareJustification(@RequestBody comJcJustification: ComJcJustification): ServerResponse{
        return ServerResponse(HttpStatus.OK,"Successfully uploaded Justification",comStandardService.prepareJustification(comJcJustification))
    }

    //********************************************************** get SPC SEC Tasks **********************************************************
    @PreAuthorize("hasAuthority('SPC_SEC_SD_READ')")
    @GetMapping("/getSpcSecTasks")
    fun getSpcSecTasks():List<TaskDetails>
    {
        return comStandardService.getSpcSecTasks()
    }

    //decision
    @PreAuthorize("hasAuthority('SPC_SEC_SD_MODIFY')")
    @PostMapping("/decisionOnJustification")
    fun decisionOnJustification(@RequestBody comJcJustification: ComJcJustification)
    {
        comStandardService.decisionOnJustification(comJcJustification)
    }

    //********************************************************** get SAC SEC Tasks **********************************************************
    @PreAuthorize("hasAuthority('SAC_SEC_SD_READ')")
    @GetMapping("/getSacSecTasks")
    fun getSacSecTasks():List<TaskDetails>
    {
        return comStandardService.getSacSecTasks()
    }

    //approve Justification List
    @PreAuthorize("hasAuthority('SAC_SEC_SD_MODIFY')")
    @PostMapping("/approveJustification")
    fun approveJustification(@RequestBody comJcJustification: ComJcJustification)
    {
        comStandardService.approveJustification(comJcJustification)
    }

    //********************************************************** process Upload Company Draft **********************************************************
    @PreAuthorize("hasAuthority('PL_SD_MODIFY')")
    @PostMapping("/uploadDraft")
    @ResponseBody
    fun uploadDraft(@RequestBody comStdDraft: ComStdDraft): ServerResponse{
        return ServerResponse(HttpStatus.OK,"Successfully uploaded Justification",comStandardService.uploadDraft(comStdDraft))
    }

    //********************************************************** get JC SEC Tasks **********************************************************
    @PreAuthorize("hasAuthority('JC_SEC_SD_READ')")
    @GetMapping("/getJcSecTasks")
    fun getJcSecTasks():List<TaskDetails>
    {
        return comStandardService.getJcSecTasks()
    }

    //Decision on Company Draft
    @PreAuthorize("hasAuthority('JC_SEC_SD_MODIFY')")
    @PostMapping("/decisionOnCompanyStdDraft")
    fun decisionOnCompanyStdDraft(@RequestBody comStdDraft: ComStdDraft)
    {
        comStandardService.decisionOnCompanyStdDraft(comStdDraft)
    }

    //********************************************************** get COM SEC Tasks **********************************************************
    @PreAuthorize("hasAuthority('COM_SEC_SD_READ')")
    @GetMapping("/getComSecTasks")
    fun getComSecTasks():List<TaskDetails>
    {
        return comStandardService.getComSecTasks()
    }

    //********************************************************** process upload Company Standard **********************************************************
    @PreAuthorize("hasAuthority('COM_SEC_SD_MODIFY')")
    @PostMapping("/uploadComStandard")
    @ResponseBody
    fun uploadComStandard(@RequestBody companyStandard: CompanyStandard): ServerResponse
    {
        return ServerResponse(HttpStatus.OK,"Successfully uploaded Standard",comStandardService.uploadComStandard(companyStandard))
    }

    //********************************************************** get HOP Tasks **********************************************************
    @PreAuthorize("hasAuthority('HOP_SD_READ')")
    @GetMapping("/getHopTasks")
    fun getHopTasks():List<TaskDetails>
    {
        return comStandardService.getHopTasks()
    }

    @GetMapping("/process/{processId}")
    fun checkState(@PathVariable("processId") processId: String?) {
        comStandardService.checkProcessHistory(processId)
    }

    @GetMapping("/getRQNumber")
    @ResponseBody
    fun getRQNumber(): String
    {
        return comStandardService.getRQNumber()
    }

    @GetMapping("/getUserList")
    @ResponseBody
    fun getUserList(): MutableList<UsersEntity> {
        return comStandardService.getUserList()
    }

}
