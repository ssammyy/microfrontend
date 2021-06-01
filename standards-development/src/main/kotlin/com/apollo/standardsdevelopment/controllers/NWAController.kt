package org.kebs.app.kotlin.apollo.standardsdevelopment.controllers

import org.kebs.app.kotlin.apollo.standardsdevelopment.dto.ProcessInstanceResponse
import org.kebs.app.kotlin.apollo.standardsdevelopment.dto.TaskDetails
import org.kebs.app.kotlin.apollo.standardsdevelopment.models.NWAJustification
import org.kebs.app.kotlin.apollo.standardsdevelopment.services.NWAService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/nwa")
class NWAController(val nwaService: NWAService) {
    //********************************************************** deployment endpoints **********************************************************
    @PostMapping("/deploy")
    fun deployWorkflow(){
        nwaService.deployProcessDefinition()
    }

    //********************************************************** process prepare justification **********************************************************
    @PostMapping("/justification")
    fun prepareJustification(): ProcessInstanceResponse {
        return nwaService.startProcessByKey()
    }

    //get KNW Tasks
    @GetMapping("/knwtasks")
    fun getKNWTask():List<TaskDetails>
    {
        return nwaService.getKNWTasks()
    }

    //prepare Justification
    @PostMapping("/prepareJustification")
    fun prepareJustification(@RequestBody nwaJustification: NWAJustification)  {
        nwaService.prepareJustification(nwaJustification)
    }

    //decision
    @PostMapping("/decisionOnJustification")
    fun decisionOnJustification(@RequestBody nwaJustification: NWAJustification)
    {
        nwaService.decisionOnJustification(nwaJustification)
    }

    @GetMapping("/process/{processId}")
    fun checkState(@PathVariable("processId") processId: String?) {
        nwaService.checkProcessHistory(processId)
    }

}
