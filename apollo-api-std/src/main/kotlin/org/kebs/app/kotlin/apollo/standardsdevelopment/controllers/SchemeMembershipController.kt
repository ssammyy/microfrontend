package org.kebs.app.kotlin.apollo.standardsdevelopment.controllers

import org.kebs.app.kotlin.apollo.standardsdevelopment.dto.ProcessInstanceResponse
import org.kebs.app.kotlin.apollo.standardsdevelopment.models.SchemeMembershipRequest
import org.kebs.app.kotlin.apollo.standardsdevelopment.services.SchemeMembershipService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/Scheme_membership")
class SchemeMembershipController(val schemeMembershipService: SchemeMembershipService) {

    @PostMapping("/deploy")
    fun deployWorkflow(){
        schemeMembershipService.deployProcessDefinition()
    }

    @PostMapping("/join_request_received")
    fun notificationRequest(@RequestBody schemeMembershipRequest: SchemeMembershipRequest): ProcessInstanceResponse? {
        return schemeMembershipRequest.let { schemeMembershipService.joinRequest(it) }
    }
}
