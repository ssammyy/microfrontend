package org.kebs.app.kotlin.apollo.api.controllers.stdController


import org.kebs.app.kotlin.apollo.api.ports.provided.dao.std.SchemeMembershipService
import org.kebs.app.kotlin.apollo.common.dto.std.ProcessInstanceResponse
import org.kebs.app.kotlin.apollo.common.dto.std.TaskDetails
import org.kebs.app.kotlin.apollo.store.model.std.SchemeMembershipRequest
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/v1/Scheme_membership")
@CrossOrigin(origins = ["http://localhost:4200"])
class SchemeMembershipController(val schemeMembershipService: SchemeMembershipService) {

    @PostMapping("/deploy")
    fun deployWorkflow(){
        schemeMembershipService.deployProcessDefinition()
    }

    @PostMapping("/join_request_received")
    fun notificationRequest(@RequestBody schemeMembershipRequest: SchemeMembershipRequest): ProcessInstanceResponse? {
        return schemeMembershipRequest?.let { schemeMembershipService.joinRequest(it) }
    }

    //Retrieves hod tasks
    @GetMapping("/hod/tasks")
    fun getTasks() : List<TaskDetails> {
        return schemeMembershipService.getHodTasks() as List<TaskDetails>
    }

    //retrieve Hod list unassigned
    @GetMapping("/getunassigned")
    fun getHodUnassigned(): MutableIterable<SchemeMembershipRequest> =
            schemeMembershipService.getAllUnassignedTasks()

    @PostMapping("/assignTask")
    fun assignTask(@RequestBody schemeMembershipRequest: SchemeMembershipRequest) {
        schemeMembershipService.assignSICOfficer(schemeMembershipRequest)
    }

    //Retrieves hod tasks
    @GetMapping("/sic/tasks")
    fun getSICTasks() : List<TaskDetails> {
        return schemeMembershipService.getSICTasks() as List<TaskDetails>
    }

    @GetMapping("/process/{processId}")
    fun checkState(@PathVariable("processId") processId: String?) {
        schemeMembershipService.checkProcessHistory(processId)
    }
}
