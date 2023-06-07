package org.kebs.app.kotlin.apollo.api.controllers.stdController


import org.kebs.app.kotlin.apollo.api.ports.provided.dao.std.SchemeMembershipService
import org.kebs.app.kotlin.apollo.common.dto.std.ServerResponse
import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import org.kebs.app.kotlin.apollo.store.model.std.SchemeMembershipRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin(origins = ["http://localhost:4200"])
@RequestMapping("api/v1/migration")
//@CrossOrigin(origins = ["http://localhost:4200"])
class SchemeMembershipController(val schemeMembershipService: SchemeMembershipService) {

    @PostMapping("/anonymous/schemeMembership/join_request_received")
    @ResponseBody
    fun requestForStandard(@RequestBody schemeMembershipRequest: SchemeMembershipRequest): ServerResponse {
        return schemeMembershipService.joinRequest(schemeMembershipRequest)
    }

    //Retrieves hod tasks
    @GetMapping("/schemeMembership/hod/tasks")
    fun getTasks(): MutableIterable<SchemeMembershipRequest?>? {
        return schemeMembershipService.getHodTasks()
    }
    @GetMapping("/schemeMembership/hod/getHodTasksAssigned")
    fun getTasksAssigned(): List<SchemeMembershipRequest?>? {
        return schemeMembershipService.getHodTasksAssigned()
    }
    @GetMapping("/schemeMembership/hod/getHodTasksUnassigned")
    fun getTasksUnAssigned(): List<SchemeMembershipRequest?>? {
        return schemeMembershipService.getHodTasksUnassigned()
    }

    @GetMapping("schemeMembership/getAllSICOfficers")
    fun getAllTcSec(): List<UsersEntity> {
        return schemeMembershipService.getAllSICOfficers()
    }

    @PostMapping("/schemeMembership/assignTask")
    fun assignTask(@RequestBody schemeMembershipRequest: SchemeMembershipRequest): ServerResponse {
        return ServerResponse(
            HttpStatus.OK,
            "SIC Assigned.",
            schemeMembershipService.assignSICOfficer(schemeMembershipRequest)

        )
    }

    //Retrieves hod tasks
    @GetMapping("/schemeMembership/sic/tasks")
    fun getSICTasks(): List<SchemeMembershipRequest> {
        return schemeMembershipService.getSicLoggedInTasks()
    }

    @PostMapping("/schemeMembership/generateInvoice")
    @ResponseBody
    fun generateInvoice(@RequestBody schemeMembershipRequest: SchemeMembershipRequest): ServerResponse {
        return ServerResponse(
            HttpStatus.OK,
            "Invoice Generated.",
            schemeMembershipService.generateInvoice(schemeMembershipRequest)

        )
    }

    @GetMapping("/schemeMembership/sic/getAllPendingInvoices")
    fun getAllPendingInvoices(): List<SchemeMembershipRequest> {
        return schemeMembershipService.getAllPendingInvoices()
    }

    @PostMapping("/schemeMembership/updatePayment")
    @ResponseBody
    fun updatePayment(@RequestBody schemeMembershipRequest: SchemeMembershipRequest): ServerResponse {
        return ServerResponse(
            HttpStatus.OK,
            "Payment Updated.",
            schemeMembershipService.updatePayment(schemeMembershipRequest)

        )
    }

    @GetMapping("/schemeMembership/sic/getAllPaidInvoices")
    fun getAllPaidInvoices(): List<SchemeMembershipRequest> {
        return schemeMembershipService.getAllPaidInvoices()
    }

    @PostMapping("/schemeMembership/addToWebStore")
    @ResponseBody
    fun addToWebStore(@RequestBody schemeMembershipRequest: SchemeMembershipRequest): ServerResponse {
        return ServerResponse(
            HttpStatus.OK,
            "Added To Web store.",
            schemeMembershipService.addToWebStore(schemeMembershipRequest)

        )
    }


}
