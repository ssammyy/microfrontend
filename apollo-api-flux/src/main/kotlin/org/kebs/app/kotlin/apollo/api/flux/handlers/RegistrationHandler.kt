package org.kebs.app.kotlin.apollo.api.flux.handlers

import mu.KotlinLogging


import org.kebs.app.kotlin.apollo.api.flux.ports.provided.validation.AbstractValidationHandler
import org.kebs.app.kotlin.apollo.common.dto.LoginRequest
import org.kebs.app.kotlin.apollo.common.exceptions.InvalidValueException
import org.springframework.stereotype.Component
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.validation.Errors
import org.springframework.validation.Validator
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.awaitBodyOrNull
import org.springframework.web.reactive.function.server.bodyValueAndAwait

@Component
class RegistrationHandler(
    private val validator: Validator
) : AbstractValidationHandler() {
    suspend fun homePost(req: ServerRequest): ServerResponse {

        return ServerResponse.ok().bodyValueAndAwait("YOU SHOULD NOT BE HERE")
    }

    suspend fun error(req: ServerRequest): ServerResponse {
        return ServerResponse.badRequest().bodyValueAndAwait("MAY-DAY!!!")
    }

    suspend fun home(req: ServerRequest): ServerResponse {
        return ServerResponse.ok().bodyValueAndAwait("YOU SHOULD NOT BE HERE")
    }

    suspend fun internalResponse(req: ServerRequest): ServerResponse {

        return try {

            req.awaitBodyOrNull<LoginRequest>()
                ?.let { body ->
                    val errors: Errors = BeanPropertyBindingResult(body, LoginRequest::class.java.name)
                    validator.validate(body, errors)
                    if (errors.allErrors.isEmpty()) {

                        body.username?.let { ServerResponse.ok().bodyValueAndAwait(it) } ?: throw InvalidValueException("Invalid request")

                    } else {
                        onValidationErrors(errors)
                    }

                }
                ?: throw InvalidValueException("No Body found")
        } catch (e: Exception) {
            KotlinLogging.logger { }.debug(e.message, e)
            KotlinLogging.logger { }.error(e.message)
            onErrors(e.message)

        }


    }


}


