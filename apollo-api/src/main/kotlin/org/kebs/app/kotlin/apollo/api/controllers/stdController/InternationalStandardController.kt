package org.kebs.app.kotlin.apollo.api.controllers.stdController

import org.kebs.app.kotlin.apollo.api.ports.provided.dao.std.*
import org.kebs.app.kotlin.apollo.common.dto.std.*
import org.kebs.app.kotlin.apollo.store.model.std.*
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin(origins = ["http://localhost:4200"])
@RequestMapping("api/v1/international_standard")
class InternationalStandardController(val internationalStandardService: InternationalStandardService) {
    //********************************************************** deployment endpoints **********************************************************
    @PostMapping("/deploy")
    fun deployWorkflow(): ServerResponse {
        internationalStandardService.deployProcessDefinition()
        return ServerResponse(HttpStatus.OK,"Successfully deployed server", HttpStatus.OK)
    }

//    //********************************************************** deployment endpoints **********************************************************
//    @PostMapping("/startProcessInstance")
//    fun startProcess(): ServerResponse {
//        internationalStandardService.startProcessInstance()
//        return ServerResponse(HttpStatus.OK,"Successfully Started server", HttpStatus.OK)
//    }

    //********************************************************** process upload Justification **********************************************************
    @PreAuthorize("hasAuthority('TC_SEC_SD_MODIFY')")
    @PostMapping("/prepareAdoptionProposal")
    @ResponseBody
    fun prepareAdoptionProposal(@RequestBody iSAdoptionProposal: ISAdoptionProposal): ServerResponse{
        return ServerResponse(HttpStatus.OK,"Successfully uploaded Adoption proposal",internationalStandardService.prepareAdoptionProposal(iSAdoptionProposal))
    }

    //********************************************************** get Stakeholders Tasks **********************************************************
    @PreAuthorize("hasAuthority('STAKEHOLDERS_SD_READ')")
    @GetMapping("/getISProposals")
    fun getISProposals():List<TaskDetails>
    {
        return internationalStandardService.getISProposals()
    }
    //********************************************************** Submit Comments **********************************************************
    @PreAuthorize("hasAuthority('STAKEHOLDERS_SD_MODIFY')")
    @PostMapping("/SubmitAPComments")
    @ResponseBody
    fun submitAPComments(@RequestBody isAdoptionComments: ISAdoptionComments): ServerResponse{
        return ServerResponse(HttpStatus.OK,"Comment Has been submitted",internationalStandardService.submitAPComments(isAdoptionComments))
    }
    //********************************************************** get TC SEC Tasks **********************************************************
    @PreAuthorize("hasAuthority('TC_SEC_SD_READ')")
    @GetMapping("/getTCSECTasks")
    fun getTCSECTasks():List<TaskDetails>
    {
        return internationalStandardService.getTCSECTasks()
    }

    //decision on Adoption Proposal
    @PreAuthorize("hasAuthority('TC_SEC_SD_MODIFY')")
    @PostMapping("/decisionOnProposal")
    fun decisionOnProposal(@RequestBody iSAdoptionProposal: ISAdoptionProposal)
    {
        internationalStandardService.decisionOnProposal(iSAdoptionProposal)
    }

    //********************************************************** get TC SEC Tasks **********************************************************
    @PreAuthorize("hasAuthority('TC_SEC_SD_READ')")
    @GetMapping("/getTCSeCTasks")
    fun getTCSeCTasks():List<TaskDetails>
    {
        return internationalStandardService.getTCSeCTasks()
    }
    //********************************************************** process upload Justification **********************************************************
    @PreAuthorize("hasAuthority('TC_SEC_SD_MODIFY')")
    @PostMapping("/prepareJustification")
    @ResponseBody
    fun prepareJustification(@RequestBody iSAdoptionJustification: ISAdoptionJustification): ServerResponse{
        return ServerResponse(HttpStatus.OK,"Successfully uploaded Justification",internationalStandardService.prepareJustification(iSAdoptionJustification))
    }

    //********************************************************** get SPC SEC Tasks **********************************************************
    @PreAuthorize("hasAuthority('SPC_SEC_SD_READ')")
    @GetMapping("/getSPCSECTasks")
    fun getSPCSECTasks():List<TaskDetails>
    {
        return internationalStandardService.getSPCSECTasks()
    }


    //decision
    @PreAuthorize("hasAuthority('SPC_SEC_SD_MODIFY')")
    @PostMapping("/decisionOnJustification")
    fun decisionOnJustification(@RequestBody iSAdoptionJustification: ISAdoptionJustification)
    {
        internationalStandardService.decisionOnJustification(iSAdoptionJustification)
    }

    //********************************************************** get SPC SEC Tasks **********************************************************
    @PreAuthorize("hasAuthority('SAC_SEC_SD_READ')")
    @GetMapping("/getSACSECTasks")
    fun getSACSECTasks():List<TaskDetails>
    {
        return internationalStandardService.getSACSECTasks()
    }

    //approve International Standard
    @PreAuthorize("hasAuthority('SAC_SEC_SD_MODIFY')")
    @PostMapping("/approveStandard")
    fun approveStandard(@RequestBody iSAdoptionJustification: ISAdoptionJustification)
    {
        internationalStandardService.approveStandard(iSAdoptionJustification)
    }

    //********************************************************** get Head of Publishing Tasks **********************************************************
    @PreAuthorize("hasAuthority('HOP_SD_READ')")
    @GetMapping("/getHOPTasks")
    fun getHOPTasks():List<TaskDetails>
    {
        return internationalStandardService.getHOPTasks()
    }

    //********************************************************** process upload Standard **********************************************************
    @PreAuthorize("hasAuthority('HOP_SD_MODIFY')")
    @PostMapping("/uploadISStandard")
    @ResponseBody
    fun uploadISStandard(@RequestBody iSUploadStandard: ISUploadStandard): ServerResponse
    {
        return ServerResponse(HttpStatus.OK,"Successfully uploaded Standard",internationalStandardService.uploadISStandard(iSUploadStandard))
    }

    //********************************************************** get Head of HO SIC Tasks **********************************************************
    @PreAuthorize("hasAuthority('HO_SIC_SD_READ')")
    @GetMapping("/getHoSiCTasks")
    fun getHoSiCTasks():List<TaskDetails>
    {
        return internationalStandardService.getHoSiCTasks()
    }

    //********************************************************** process upload Gazette Notice **********************************************************
    @PreAuthorize("hasAuthority('HO_SIC_SD_MODIFY')")
    @PostMapping("/uploadGazetteNotice")
    @ResponseBody
    fun uploadGazetteNotice(@RequestBody iSGazetteNotice: ISGazetteNotice): ServerResponse
    {
        return ServerResponse(HttpStatus.OK,"Successfully uploaded Gazette Notice",internationalStandardService.uploadGazetteNotice(iSGazetteNotice))
    }

    //********************************************************** process upload Gazettement Date **********************************************************
    @PreAuthorize("hasAuthority('HO_SIC_SD_MODIFY')")
    @PostMapping("/updateGazettementDate")
    @ResponseBody
    fun updateGazettementDate(@RequestBody iSGazettement: ISGazettement): ServerResponse
    {
        return ServerResponse(HttpStatus.OK,"Successfully uploaded Gazette Notice",internationalStandardService.updateGazettementDate(iSGazettement))
    }

}
