package org.kebs.app.kotlin.apollo.api.handlers.pvoc

import org.kebs.app.kotlin.apollo.api.payload.extractPage
import org.kebs.app.kotlin.apollo.api.service.PvocMonitoringService
import org.springframework.stereotype.Component
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse

@Component
class PvocMonitoringHandler(private val pvocMonitoringService: PvocMonitoringService) {

    fun listMonitoringIssues(req: ServerRequest): ServerResponse {
        val page = extractPage(req)
        val status = req.pathVariable("monitStatus")
        return ServerResponse.ok().body(pvocMonitoringService.listAgentMonitoring(status.toUpperCase(), page))
    }
}