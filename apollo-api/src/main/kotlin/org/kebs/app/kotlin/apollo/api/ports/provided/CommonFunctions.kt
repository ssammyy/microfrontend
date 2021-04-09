package org.kebs.app.kotlin.apollo.api.ports.provided

import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import java.time.Instant


fun createUserAlert(req: ServerRequest, e: Exception): ServerResponse {

    KotlinLogging.logger { }.error(e.message)
    KotlinLogging.logger { }.debug(e.message, e)


    req.attributes()["exception"] = e
    req.attributes()["message"] = e.message
    req.attributes()["timestamp"] = Instant.now()
    req.attributes()["status"] = HttpStatus.INTERNAL_SERVER_ERROR
    req.attributes()["error"] = HttpStatus.INTERNAL_SERVER_ERROR.name
    req.attributes()["path"] = req.path()
    return ServerResponse.badRequest().render("error", req.attributes())
}

fun makeAnyNotBeNull(anyValue: Any): Any {
    return anyValue
}