package org.kebs.app.kotlin.apollo.api.ports.provided.validation

import org.springframework.http.MediaType
import org.springframework.validation.Errors
import org.springframework.validation.FieldError
import org.springframework.web.servlet.function.ServerResponse


abstract class AbstractValidationHandler {
    fun onValidationErrors(errors: Errors): ServerResponse {
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
            .body(errorMap)


    }

    fun onErrors(message: String?) =
        ServerResponse
            .badRequest()
            .contentType(MediaType.APPLICATION_JSON)
            .body(message ?: "UNKNOWN_ERROR")
}

