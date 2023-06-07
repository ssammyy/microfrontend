package org.kebs.app.kotlin.apollo.api.controllers.kraControllers

import org.springframework.web.bind.annotation.RestController
import org.springframework.beans.factory.annotation.Autowired
import org.kebs.app.kotlin.apollo.api.objects.integ.kra.soap.service.KraSoapService
import org.springframework.web.bind.annotation.PostMapping
import org.kebs.app.kotlin.apollo.api.objects.integ.kra.soap.kraPinIntegrations.ValidatePIN
import org.kebs.app.kotlin.apollo.api.objects.integ.kra.soap.kraPinIntegrations.ValidatePINResponse
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@RestController
@RequestMapping("/api/kra-pin/") ///api/kra-pin/getPinStatus
class KraSoapClientController {
    @Autowired
    private val client: KraSoapService? = null
    @PostMapping("getPinStatus")
    fun invokeSoapClientToGetLoanStatus(@RequestBody request: ValidatePIN?): ValidatePINResponse? {
        return client?.getPinStatus(request)
    }
}