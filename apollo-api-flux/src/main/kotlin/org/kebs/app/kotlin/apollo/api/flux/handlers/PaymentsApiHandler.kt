package org.kebs.app.kotlin.apollo.api.flux.handlers

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.flux.ports.provided.dao.kappa.KappaPaymentsDaoService
import org.kebs.app.kotlin.apollo.api.flux.ports.provided.validation.AbstractValidationHandler
import org.kebs.app.kotlin.apollo.common.dto.kappa.requests.NotificationRequestValue
import org.kebs.app.kotlin.apollo.common.dto.kappa.requests.ValidationRequestValue
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
class PaymentsApiHandler(
    private val validator: Validator,
    private val service: KappaPaymentsDaoService,
) : AbstractValidationHandler() {

    suspend fun processPaymentNotification(req: ServerRequest): ServerResponse {
        return try {
            req.awaitBodyOrNull<NotificationRequestValue>()
                ?.let { body ->
                    val errors: Errors = BeanPropertyBindingResult(body, NotificationRequestValue::class.java.name)
                    validator.validate(body, errors)
                    if (errors.allErrors.isEmpty()) {
                        val response = service.processPaymentNotification(body)
                        ServerResponse.ok().bodyValueAndAwait(response)

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

    suspend fun validateInvoice(req: ServerRequest): ServerResponse {
        return try {
            req.awaitBodyOrNull<ValidationRequestValue>()
                ?.let { body ->
                    val errors: Errors = BeanPropertyBindingResult(body, ValidationRequestValue::class.java.name)
                    validator.validate(body, errors)
                    if (errors.allErrors.isEmpty()) {
                        val response = service.processValidateInvoice(body)
                        ServerResponse.ok().bodyValueAndAwait(response)

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