package org.kebs.app.kotlin.apollo.api.handlers

import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import java.time.Instant
import java.time.LocalDateTime

@Component
class ErrorHandler {
    val errorPageView = "error"

    fun jsErrorView(req: ServerRequest) = ServerResponse.ok().render("admin/errors/500")
    fun jsAccessDeniedErrorView(req: ServerRequest) = ServerResponse.ok().render("admin/errors/403")

    fun errorView(req: ServerRequest): ServerResponse {
        try {
            KotlinLogging.logger { }.error("Attributes ${req.attributes()}")
            return ServerResponse.ok().render(errorPageView, req.attributes())

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)


            req.attributes()["exception"] = e
            req.attributes()["message"] = e.message
            req.attributes()["timestamp"] = Instant.now()
            req.attributes()["status"] = HttpStatus.INTERNAL_SERVER_ERROR
            req.attributes()["error"] = HttpStatus.INTERNAL_SERVER_ERROR.name
            req.attributes()["path"] = req.path()
            return ServerResponse.badRequest().render(errorPageView, req.attributes())
        }

    }

    fun accessDeniedView(req: ServerRequest): ServerResponse {
        val response = ServerResponse.status(HttpStatus.FORBIDDEN)
        response.contentType(MediaType.APPLICATION_JSON)
        val errorDto = ErrorDto()
        errorDto.status = HttpStatus.FORBIDDEN
        errorDto.message = "Access denied"
        errorDto.timeStamp = LocalDateTime.now()

        req.attributes()["status"] = errorDto.status
        req.attributes()["message"] = errorDto.message
        req.attributes()["timestamp"] = errorDto.timeStamp

        response.body(errorDto)

        return response.render(errorPageView, req.attributes())
    }

    class ErrorDto {
        var timeStamp: LocalDateTime? = null
        var message: String? = null
        var status: HttpStatus? = null

        override fun toString(): String {
            return "status=$status, message=$message, timestamp=$timeStamp"
        }
    }
}