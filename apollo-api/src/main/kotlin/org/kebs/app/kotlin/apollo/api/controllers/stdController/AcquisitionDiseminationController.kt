package org.kebs.app.kotlin.apollo.api.controllers.stdController


import org.kebs.app.kotlin.apollo.api.ports.provided.dao.std.AquisitonDisseminationService
import org.kebs.app.kotlin.apollo.common.dto.std.ProcessInstanceResponse
import org.kebs.app.kotlin.apollo.common.dto.std.TaskDetails
import org.kebs.app.kotlin.apollo.store.model.std.SdNewRequestEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin(origins = ["http://localhost:4200"])
@RestController

@RequestMapping("api/v1/Acquisition_dissemination")
class AcquisitionDiseminationController(val aquisitonDisseminationService: AquisitonDisseminationService) {
    @PostMapping("/deploy")
    fun deployWorkflow(){
        aquisitonDisseminationService.deployProcessDefinition()
    }

    @PostMapping("/make_request")
    fun notificationRequest(@RequestBody sdNewRequestEntity: SdNewRequestEntity): ProcessInstanceResponse? {
        return sdNewRequestEntity?.let { aquisitonDisseminationService.makeRequest(it) }
    }

    //Retrieves hod tasks
    @GetMapping("/hod/tasks")
    fun getTasks() : List<TaskDetails> {
        return aquisitonDisseminationService.getHodTasks() as List<TaskDetails>
    }

    @PostMapping("/assignTask")
    fun assignTask(@RequestBody sdNewRequestEntity: SdNewRequestEntity) {
        aquisitonDisseminationService.assignSICOfficer(sdNewRequestEntity)
    }

    //Retrieves hod tasks
    @GetMapping("/sic/tasks")
    fun getSICTasks() : List<TaskDetails> {
        return aquisitonDisseminationService.getSICTasks() as List<TaskDetails>
    }

    @PostMapping("/isAvailable")
    fun isAvailableYes(@RequestBody sdNewRequestEntity: SdNewRequestEntity) {
        aquisitonDisseminationService.publicationAvailable(sdNewRequestEntity)
    }

    @PostMapping("/sendEmail")
    fun sendEmail(@RequestBody sdNewRequestEntity: SdNewRequestEntity) {
        aquisitonDisseminationService.sendEmailList(sdNewRequestEntity)
    }
}
