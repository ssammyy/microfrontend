package com.apollo.standardsdevelopment.controllers

import com.apollo.standardsdevelopment.dto.ID
import com.apollo.standardsdevelopment.dto.ProcessInstanceResponse
import com.apollo.standardsdevelopment.dto.ServerResponse
import com.apollo.standardsdevelopment.dto.TaskDetails
import com.apollo.standardsdevelopment.models.*
import com.apollo.standardsdevelopment.services.NWAService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/nwa")
class NWAController(val nwaService: NWAService) {

    //********************************************************** deployment endpoints **********************************************************
    @PostMapping("/deploy")
    fun deployWorkflow(): ServerResponse {
        nwaService.deployProcessDefinition()
        return ServerResponse(HttpStatus.OK,"Successfully deployed server", HttpStatus.OK)
    }

    //********************************************************** deployment endpoints **********************************************************
    @PostMapping("/startProcessInstance")
    fun startProcess(): ServerResponse {
        nwaService.startProcessInstance()
        return ServerResponse(HttpStatus.OK,"Successfully Started server", HttpStatus.OK)
    }

    //Get KNW Departments
    @GetMapping("/getKNWDepartments")
    @ResponseBody
    fun getKNWDepartments(): MutableList<Department>
    {
        return nwaService.getKNWDepartments();
    }

    //Get KNW Committee
    @GetMapping("/getKNWCommittee")
    @ResponseBody
    fun getKNWCommittee(): MutableList<TechnicalCommittee>
    {
        return nwaService.getKNWCommittee();
    }
    //********************************************************** process upload Justification **********************************************************
    @PostMapping("/prepareJustification")
    @ResponseBody
    fun prepareJustification(@RequestBody nwaJustification: NWAJustification): ServerResponse{
        return ServerResponse(HttpStatus.OK,"Successfully uploaded Justification",nwaService.prepareJustification(nwaJustification))
    }

    //********************************************************** get spc_sec Tasks **********************************************************
    @GetMapping("/getSpcSecTasks")
    fun getSPCSECTasks():List<TaskDetails>
    {
        return nwaService.getSPCSECTasks()
    }


    //decision
    @PostMapping("/decisionOnJustification")
    fun decisionOnJustification(@RequestBody nwaJustification: NWAJustification)
    {
        nwaService.decisionOnJustification(nwaJustification)
    }


    //********************************************************** get KNW Tasks **********************************************************
    @GetMapping("/knwtasks")
    fun getKNWTask():List<TaskDetails>
    {
        return nwaService.getKNWTasks()
    }

    //********************************************************** process prepare justification for DI-SDT Approval **********************************************************
    @PostMapping("/prepareDiSdtJustification")
    @ResponseBody
    fun prepareDisDtJustification(@RequestBody nwaDiSdtJustification: NWADiSdtJustification): ServerResponse
    {
        return ServerResponse(HttpStatus.OK,"Successfully uploaded DI-SDT Justification",nwaService.prepareDisDtJustification(nwaDiSdtJustification))
    }

    //********************************************************** get di-sdt Tasks **********************************************************
    @GetMapping("/getDiSdtTasks")
    fun getDISDTTasks():List<TaskDetails>
    {
        return nwaService.getDISDTTasks()
    }

    //********************************************************** Decision  on DI-SDT Approval **********************************************************
    @PostMapping("/decisionOnDiSdtJustification")
    fun decisionOnDiSdtJustification(@RequestBody nwaDiSdtJustification: NWADiSdtJustification)
    {
        nwaService.decisionOnDiSdtJustification(nwaDiSdtJustification)
    }

    //********************************************************** process prepare Preliminary Draft **********************************************************
    @PostMapping("/preparePreliminaryDraft")
    @ResponseBody
    fun preparePreliminaryDraft(@RequestBody nwaPreliminaryDraft: NWAPreliminaryDraft): ServerResponse
    {
        return ServerResponse(HttpStatus.OK,"Successfully uploaded Preliminary Draft",nwaService.preparePreliminaryDraft(nwaPreliminaryDraft))
    }

    //********************************************************** Decision  on DI-SDT Approval **********************************************************
    @PostMapping("/decisionOnPd")
    fun decisionOnPD(@RequestBody nwaPreliminaryDraft: NWAPreliminaryDraft)
    {
        nwaService.decisionOnPD(nwaPreliminaryDraft)
    }

    //********************************************************** get Head of Publishing Tasks **********************************************************
    @GetMapping("/getHOPTasks")
    fun getHOPTasks():List<TaskDetails>
    {
        return nwaService.getHOPTasks()
    }

    //********************************************************** process prepare Workshop Draft **********************************************************
    @PostMapping("/editWorkshopDraft")
    @ResponseBody
    fun editWorkshopDraft(@RequestBody nwaWorkShopDraft: NWAWorkShopDraft): ServerResponse
    {
        return ServerResponse(HttpStatus.OK,"Successfully uploaded Workshop Draft",nwaService.editWorkshopDraft(nwaWorkShopDraft))
    }

    //********************************************************** get Head of SAC SEC Tasks **********************************************************
    @GetMapping("/getSacSecTasks")
    fun getSacSecTasks():List<TaskDetails>
    {
        return nwaService.getSacSecTasks()
    }

    //********************************************************** Decision  on Workshop Draft Approval **********************************************************
    @PostMapping("/decisionOnWd")
    fun decisionOnWd(@RequestBody nwaWorkShopDraft: NWAWorkShopDraft)
    {
        nwaService.decisionOnWD(nwaWorkShopDraft)
    }


    //********************************************************** process upload Standard **********************************************************
    @PostMapping("/uploadNwaStandard")
    @ResponseBody
    fun uploadNwaStandard(@RequestBody nWAStandard: NWAStandard): ServerResponse
    {
        return ServerResponse(HttpStatus.OK,"Successfully uploaded Standard",nwaService.uploadNwaStandard(nWAStandard))
    }

    //********************************************************** get Head of HO SIC Tasks **********************************************************
    @GetMapping("/getHoSiCTasks")
    fun getHoSiCTasks():List<TaskDetails>
    {
        return nwaService.getHoSiCTasks()
    }

    //********************************************************** process upload Gazette Notice **********************************************************
    @PostMapping("/uploadGazetteNotice")
    @ResponseBody
    fun uploadGazetteNotice(@RequestBody nWAGazetteNotice: NWAGazetteNotice): ServerResponse
    {
        return ServerResponse(HttpStatus.OK,"Successfully uploaded Gazette Notice",nwaService.uploadGazetteNotice(nWAGazetteNotice))
    }

    //********************************************************** process upload Gazettement Date **********************************************************
    @PostMapping("/updateGazettementDate")
    @ResponseBody
    fun updateGazettementDate(@RequestBody nWAGazettement: NWAGazettement): ServerResponse
    {
        return ServerResponse(HttpStatus.OK,"Successfully uploaded Gazette Notice",nwaService.updateGazettementDate(nWAGazettement))
    }



//    //********************************************************** process prepare justification **********************************************************
//    @PostMapping("/justification")
//    fun prepareJustification(): ProcessInstanceResponse {
//        return nwaService.startProcessByKey()
//    }



//    //prepare Justification
//    @PostMapping("/prepareJustification")
//    fun prepareJustification(@RequestBody nwaJustification: NWAJustification)  {
//        nwaService.prepareJustification(nwaJustification)
//    }


//    @PostMapping("/endtask")
//    fun submitEnquiry(@RequestBody nwaJustification: NWAJustification) {
//        nwaService.closeTask(nwaJustification.taskId)
//    }


    @GetMapping("/process/{processId}")
    fun checkState(@PathVariable("processId") processId: String?) {
        nwaService.checkProcessHistory(processId)
    }

}
