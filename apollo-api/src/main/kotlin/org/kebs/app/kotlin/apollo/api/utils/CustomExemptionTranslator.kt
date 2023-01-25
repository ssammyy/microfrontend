package org.kebs.app.kotlin.apollo.api.utils;

import com.fasterxml.jackson.databind.exc.InvalidFormatException
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.stereotype.Component

@Component
class CustomExemptionTranslator {
    fun translate(ex: HttpMessageNotReadableException): Map<String, String> {
        val errors = mutableMapOf<String, String>()
        when (ex.cause) {
            is InvalidFormatException -> {
                val iex = ex.cause as InvalidFormatException
                if (!iex.path.isEmpty()) {
                    errors[iex.path.get(0).fieldName] = iex.originalMessage
                    errors["${iex.path.get(0).fieldName}_value"] = iex.value.toString()
                } else {
                    errors[iex.location.sourceDescription()] = iex.value.toString()
                }

            }
            else -> {
                errors.put("body", ex.cause?.message ?: "Unknown")
            }
        }
        return errors
    }
}
