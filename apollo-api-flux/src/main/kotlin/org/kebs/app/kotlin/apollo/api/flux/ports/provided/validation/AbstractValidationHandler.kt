package org.kebs.app.kotlin.apollo.api.flux.ports.provided.validation

import org.springframework.http.MediaType
import org.springframework.validation.Errors
import org.springframework.validation.FieldError
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait

abstract class AbstractValidationHandler {
    suspend fun onValidationErrors(errors: Errors): ServerResponse {
        val errorMap = mutableMapOf<String, String?>()
        errors.allErrors.forEach { error ->
            error?.let { e ->
                val fieldName = (e as FieldError).field
                val errorMessage = e.getDefaultMessage()
                errorMap[fieldName] = errorMessage
            }

        }
        return ServerResponse
            .badRequest()
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValueAndAwait(errorMap)


    }

    suspend fun onErrors(message: String?) =
        ServerResponse
            .badRequest()
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValueAndAwait(message ?: "UNKNOWN_ERROR")
}