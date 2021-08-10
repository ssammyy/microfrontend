package org.kebs.app.kotlin.apollo.api.controllers.stdController

import com.apollo.standardsdevelopment.dto.*
import org.kebs.app.kotlin.apollo.store.model.std.*
import org.kebs.app.kotlin.apollo.store.repo.std.*
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.std.*
import org.kebs.app.kotlin.apollo.common.dto.std.*
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/adoptionToEAStandard")
class AdoptionToEastAfricaStandardController(val adoptionOfEastAfricaStandardService: AdoptionOfEastAfricaStandardService) {

    //********************************************************** deployment endpoints ********************************************//
    @PostMapping("/deploy")
    fun deployWorkflow(): ServerResponse {
        adoptionOfEastAfricaStandardService.deployProcessDefinition()
        return ServerResponse(HttpStatus.OK,"Successfully deployed server", HttpStatus.OK)
    }

    //***************************************************** submit justification for formation of TC ***************************//
    @PostMapping("/submitSACSummary")
    @ResponseBody
    fun submitSACSummary(@RequestBody sacSummary: SACSummary): ServerResponse{
        //return ResponseEntity(standardRequestService.requestForStandard(standardRequest), HttpStatus.OK)
        return ServerResponse(HttpStatus.OK,"Successfully submitted SAC Summary",adoptionOfEastAfricaStandardService.submitSACSummary(sacSummary))
    }

   @GetMapping("/getSACSummaryTask")
    fun getSACSummaryTask():List<TaskDetails>
    {
        return adoptionOfEastAfricaStandardService.getSACSummaryTask()
    }

    @PostMapping("/process")
    @ResponseBody
    fun checkState( @RequestBody id: ID): ServerResponse {
        return ServerResponse(HttpStatus.OK,"Successfully returned process history",adoptionOfEastAfricaStandardService.checkProcessHistory(id))
    }

    @PostMapping("/decisionOnSACSummary")
    @ResponseBody
    fun decisionOnSACSummary(@RequestBody decisionFeedback: DecisionFeedback): ServerResponse
    {
        return ServerResponse(HttpStatus.OK,"Decision on SAC Summary",adoptionOfEastAfricaStandardService.decisionOnSACSummary(decisionFeedback))
    }


    @GetMapping("/getSACSECTask")
    fun getSACSECTask():List<TaskDetails>
    {
        return adoptionOfEastAfricaStandardService.getSACSECTask()
    }


    @PostMapping("/decisionOnSACSEC")
    @ResponseBody
    fun decisionOnSACSEC(@RequestBody decisionFeedback: DecisionFeedback): ServerResponse
    {
        return ServerResponse(HttpStatus.OK,"Decision on SAC SEC",adoptionOfEastAfricaStandardService.decisionOnSACSEC(decisionFeedback))
    }

}
