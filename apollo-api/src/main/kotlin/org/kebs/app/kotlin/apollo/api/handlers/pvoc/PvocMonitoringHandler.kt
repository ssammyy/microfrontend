package org.kebs.app.kotlin.apollo.api.handlers.pvoc

import org.kebs.app.kotlin.apollo.api.payload.extractPage
import org.kebs.app.kotlin.apollo.api.service.ConsignmentCertificatesIssues
import org.kebs.app.kotlin.apollo.api.service.PvocMonitoringService
import org.springframework.stereotype.Component
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.paramOrNull

@Component
class PvocMonitoringHandler(private val pvocMonitoringService: PvocMonitoringService) {

    fun listMonitoringIssues(req: ServerRequest): ServerResponse {
        val page = extractPage(req)
        val status = req.pathVariable("monitStatus")
        return ServerResponse.ok().body(pvocMonitoringService.listAgentMonitoring(status.toUpperCase(), page))
    }

    fun listRfcsCoc(req: ServerRequest): ServerResponse {
        val page = extractPage(req)
        val status = req.paramOrNull("rev_status")
        val keywords = req.paramOrNull("keywords")
        return ServerResponse.ok()
            .body(
                pvocMonitoringService.listRfcCocCoiRequests(
                    keywords,
                    ConsignmentCertificatesIssues.COC.nameDesc,
                    status?.toInt() ?: 0,
                    page
                )
            )
    }

    fun listRfcsCor(req: ServerRequest): ServerResponse {
        val page = extractPage(req)
        val status = req.paramOrNull("rev_status")
        val keywords = req.paramOrNull("keywords")
        return ServerResponse.ok().body(pvocMonitoringService.listRfcCorRequests(keywords, status?.toInt() ?: 0, page))
    }

    fun listRfcsCoi(req: ServerRequest): ServerResponse {
        val page = extractPage(req)
        val status = req.paramOrNull("rev_status")
        val keywords = req.paramOrNull("keywords")
        return ServerResponse.ok()
            .body(
                pvocMonitoringService.listRfcCocCoiRequests(
                    keywords,
                    ConsignmentCertificatesIssues.COI.nameDesc,
                    status?.toInt() ?: 0,
                    page
                )
            )
    }

    fun getRfcForCoiOrCoc(req: ServerRequest): ServerResponse {
        val rfcId = req.pathVariable("rfcId")
        return ServerResponse.ok().body(pvocMonitoringService.getRfcCocCoi(rfcId.toLong()))
    }

    fun getRfcForCor(req: ServerRequest): ServerResponse {
        val rfcId = req.pathVariable("rfcId")
        return ServerResponse.ok().body(pvocMonitoringService.getRfcCor(rfcId.toLong()))
    }

    fun listForeignCoi(req: ServerRequest): ServerResponse {
        val page = extractPage(req)
        val status = req.paramOrNull("rev_status")
        val keywords = req.paramOrNull("keywords")
        return ServerResponse.ok()
            .body(
                pvocMonitoringService.listForeignCocCoi(
                    ConsignmentCertificatesIssues.COI.nameDesc,
                    "F",
                    status?.toInt() ?: 0,
                    page,
                    keywords
                )
            )
    }

    fun listForeignNcr(req: ServerRequest): ServerResponse {
        val page = extractPage(req)
        val status = req.paramOrNull("rev_status")
        val keywords = req.paramOrNull("keywords")
        return ServerResponse.ok()
            .body(
                pvocMonitoringService.listForeignCocCoi(
                    ConsignmentCertificatesIssues.NCR.nameDesc,
                    "F",
                    status?.toInt() ?: 0,
                    page,
                    keywords
                )
            )
    }

    fun listForeignCoc(req: ServerRequest): ServerResponse {
        val page = extractPage(req)
        val status = req.paramOrNull("rev_status")
        val keywords = req.paramOrNull("keywords")
        return ServerResponse.ok()
            .body(
                pvocMonitoringService.listForeignCocCoi(
                    ConsignmentCertificatesIssues.COC.nameDesc,
                    "F",
                    status?.toInt() ?: 0,
                    page,
                    keywords
                )
            )
    }

    fun getForeignCoiOrCoc(req: ServerRequest): ServerResponse {
        val rfcId = req.pathVariable("cocCoiId")
        return ServerResponse.ok().body(pvocMonitoringService.getForeignCoirOrCoc(rfcId.toLong()))
    }

    fun listForeignCor(req: ServerRequest): ServerResponse {
        val page = extractPage(req)
        val status = req.paramOrNull("rev_status")
        val keywords = req.paramOrNull("keywords")
        return ServerResponse.ok().body(
            pvocMonitoringService.listForeignCor(
                ConsignmentCertificatesIssues.COR.nameDesc,
                "F",
                status?.toInt() ?: 0,
                page,
                keywords
            )
        )
    }

    fun listForeignCorNcr(req: ServerRequest): ServerResponse {
        val page = extractPage(req)
        val status = req.paramOrNull("rev_status")
        val keywords = req.paramOrNull("keywords")
        return ServerResponse.ok().body(
            pvocMonitoringService.listForeignCor(
                ConsignmentCertificatesIssues.NCR_COR.nameDesc,
                "F",
                status?.toInt() ?: 0,
                page,
                keywords
            )
        )
    }

    fun getForeignCor(req: ServerRequest): ServerResponse {
        val corId = req.pathVariable("corId")
        return ServerResponse.ok().body(pvocMonitoringService.getForeignCor(corId.toLong()))
    }

}