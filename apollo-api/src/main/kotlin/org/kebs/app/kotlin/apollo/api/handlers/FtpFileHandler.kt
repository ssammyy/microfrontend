package org.kebs.app.kotlin.apollo.api.handlers

import okhttp3.internal.toLongOrDefault
import org.kebs.app.kotlin.apollo.api.payload.extractPage
import org.kebs.app.kotlin.apollo.api.service.FileStorageService
import org.springframework.stereotype.Component
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse

@Component
class FtpFileHandler(val fileStorage: FileStorageService) {


    fun loadStats(req: ServerRequest): ServerResponse {
        val dateCreated = req.param("date")
        return ServerResponse
                .ok()
                .body(fileStorage.todaysFileStats(dateCreated.orElse("")))
    }

    fun loadFileContent(req: ServerRequest): ServerResponse {
        val messageId = req.pathVariable("messageId")
        return ServerResponse
                .ok()
                .body(fileStorage.loadFilesById(messageId.toLongOrDefault(0L)))
    }

    fun resendFileViaSftp(req: ServerRequest): ServerResponse {
        val messageId = req.pathVariable("messageId")
        return ServerResponse
                .ok()
                .body(fileStorage.resendFile(messageId.toLongOrDefault(0L)))
    }

    fun listIncompleteIdfDocuments(req: ServerRequest): ServerResponse {
        val dateCreated = req.param("date")
        val status = req.param("status").orElse("")
        return ServerResponse
                .ok()
                .body(fileStorage.listIncompleteIdfDocuments(dateCreated.orElse(""), status, extractPage(req)))
    }

    fun listFilesByStatus(req: ServerRequest): ServerResponse {
        val status = req.param("fileStatus")
        val dateCreated = req.param("date")
        val flowDirection = req.param("flowDirection")
        val fileName = req.param("exchangeFile")
        val page = extractPage(req)
        return ServerResponse
                .ok()
                .body(fileStorage.loadFilesByStatus(status, fileName, dateCreated.orElse(""), flowDirection.orElse(null), page))
    }
}