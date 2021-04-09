package org.kebs.app.kotlin.apollo.api.flux.handlers

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.flux.ports.provided.dao.kra.StandardsLevyDaoService
import org.kebs.app.kotlin.apollo.api.flux.ports.provided.validation.AbstractValidationHandler
import org.kebs.app.kotlin.apollo.common.dto.kra.request.ReceiveSL2PaymentRequest
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
class StandardsLevyHandler(

    private val validator: Validator,
    private val service: StandardsLevyDaoService,
) : AbstractValidationHandler() {


    suspend fun processSendEntryNumbers(req: ServerRequest): ServerResponse {
        return try {
            val jobId = req.pathVariable("job")

            ServerResponse.ok().bodyValueAndAwait(service.startJob(jobId))

        } catch (e: Exception) {
            KotlinLogging.logger { }.debug(e.message, e)
            KotlinLogging.logger { }.error(e.message)
            onErrors(e.message)

        }
    }

    suspend fun processSendPenalties(req: ServerRequest): ServerResponse {
        return try {
            val jobId = req.pathVariable("job")

            ServerResponse.ok().bodyValueAndAwait(service.startPenaltyJob(jobId))

        } catch (e: Exception) {
            KotlinLogging.logger { }.debug(e.message, e)
            KotlinLogging.logger { }.error(e.message)
            onErrors(e.message)

        }
    }

    suspend fun processReceiveSL2Payment(req: ServerRequest): ServerResponse {

        return try {
            req.awaitBodyOrNull<ReceiveSL2PaymentRequest>()
                ?.let { body ->
                    val errors: Errors = BeanPropertyBindingResult(body, ReceiveSL2PaymentRequest::class.java.name)
                    validator.validate(body, errors)
                    if (errors.allErrors.isEmpty()) {
                        val response = service.processSl2Payments(body)
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
