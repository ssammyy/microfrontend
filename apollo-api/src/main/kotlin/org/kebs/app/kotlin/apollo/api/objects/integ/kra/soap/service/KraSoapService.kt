package org.kebs.app.kotlin.apollo.api.objects.integ.kra.soap.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.oxm.jaxb.Jaxb2Marshaller
import org.kebs.app.kotlin.apollo.api.objects.integ.kra.soap.kraPinIntegrations.ValidatePIN
import org.kebs.app.kotlin.apollo.api.objects.integ.kra.soap.kraPinIntegrations.ValidatePINResponse
import org.springframework.stereotype.Service
import org.springframework.ws.client.core.WebServiceTemplate
import java.lang.Exception

@Service
class KraSoapService() {
    @Autowired
    private val marshaller: Jaxb2Marshaller? = null
    fun getPinStatus(request: ValidatePIN?): ValidatePINResponse? {
        val template = WebServiceTemplate(marshaller)
        var validatePINResponse: ValidatePINResponse? = null
        try {
            validatePINResponse = template.marshalSendAndReceive(
                "https://integrationsqa.kra.go.ke/EPROMIS/KRAiTaxEPromisService",
                request
            ) as ValidatePINResponse
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return validatePINResponse
    }
}