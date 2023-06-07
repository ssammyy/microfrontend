package org.kebs.app.kotlin.apollo.api.handlers.di

import org.kebs.app.kotlin.apollo.api.payload.extractPage
import org.kebs.app.kotlin.apollo.api.service.ConsignmentCertificatesIssues
import org.kebs.app.kotlin.apollo.api.service.PvocMonitoringService
import org.springframework.stereotype.Component
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.paramOrNull

@Component
class CertificateHandler(val pvocMonitoringService: PvocMonitoringService) {

    fun listCocCertificates(req: ServerRequest): ServerResponse {
        val page = extractPage(req)
        val category = req.paramOrNull("category")
        val keywords = req.paramOrNull("keywords")
        return ServerResponse.ok().body(
            pvocMonitoringService.listForeignCocCoi(
                ConsignmentCertificatesIssues.COC.nameDesc,
                category,
                null,
                page,
                keywords
            )
        )
    }

    fun listCoiCertificates(req: ServerRequest): ServerResponse {
        val page = extractPage(req)
        val category = req.paramOrNull("category")
        val keywords = req.paramOrNull("keywords")
        return ServerResponse.ok().body(
            pvocMonitoringService.listForeignCocCoi(
                ConsignmentCertificatesIssues.COI.nameDesc,
                category,
                null,
                page,
                keywords
            )
        )
    }

    fun listCorCertificates(req: ServerRequest): ServerResponse {
        val page = extractPage(req)
        val category = req.paramOrNull("category")
        val keywords = req.paramOrNull("keywords")
        return ServerResponse.ok().body(
            pvocMonitoringService.listForeignCor(
                ConsignmentCertificatesIssues.COR.nameDesc,
                category,
                null,
                page,
                keywords
            )
        )
    }

    fun listNcrCertificates(req: ServerRequest): ServerResponse {
        val page = extractPage(req)
        val category = req.paramOrNull("category")
        val keywords = req.paramOrNull("keywords")
        return ServerResponse.ok().body(
            pvocMonitoringService.listForeignCocCoi(
                ConsignmentCertificatesIssues.NCR.nameDesc,
                category,
                null,
                page,
                keywords
            )
        )
    }

    fun listNcrCorCertificates(req: ServerRequest): ServerResponse {
        val page = extractPage(req)
        val category = req.paramOrNull("category")
        val keywords = req.paramOrNull("keywords")
        return ServerResponse.ok().body(
            pvocMonitoringService.listForeignCor(
                ConsignmentCertificatesIssues.NCR_COR.nameDesc,
                category,
                null,
                page,
                keywords
            )
        )
    }
}