package org.kebs.app.kotlin.apollo.api.handlers.di

import org.kebs.app.kotlin.apollo.api.payload.extractPage
import org.kebs.app.kotlin.apollo.api.service.DestinationInspectionService
import org.springframework.stereotype.Component
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.paramOrNull

@Component
class OtherDocumentHandler(val diService: DestinationInspectionService) {

    fun listIdfDocuments(req: ServerRequest): ServerResponse {
        val dateCreated = req.param("date")
        val status = req.param("status").orElse("all")
        val keywords = req.paramOrNull("keywords")
        return ServerResponse
            .ok()
            .body(diService.listIncompleteIdfDocuments(status, dateCreated.orElse(null), extractPage(req), keywords))
    }

    fun listManifestDocuments(req: ServerRequest): ServerResponse {
        val dateCreated = req.param("date")
        val status = req.param("status").orElse("all")
        val keywords = req.paramOrNull("keywords")
        return ServerResponse.ok()
            .body(diService.listManifestDocuments(status, dateCreated.orElse(null), extractPage(req), keywords))
    }
}