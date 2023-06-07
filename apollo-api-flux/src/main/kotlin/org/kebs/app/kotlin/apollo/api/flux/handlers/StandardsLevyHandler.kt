package org.kebs.app.kotlin.apollo.api.flux.handlers

import com.google.gson.Gson
import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.flux.ports.provided.dao.kra.StandardsLevyDaoService
import org.kebs.app.kotlin.apollo.api.flux.ports.provided.validation.AbstractValidationHandler
import org.kebs.app.kotlin.apollo.common.dto.kra.request.PinValidationWebRequest
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

/**
 *
 * <H1>StandardsLevyHandler</H1>
 * a handler class For managing application users
 *  @author Kenneth KZM. Muhia
 *  @since 1.0.0
 * Constructor values are to inject spring beans below
 *  @see Validator
 *  @see StandardsLevyDaoService
 *
 *  The class implements @see AbstractValidationHandler
 */
@Component
class StandardsLevyHandler(

    private val validator: Validator,
    private val service: StandardsLevyDaoService,
) : AbstractValidationHandler() {


    /**
     * Endpoint responsible for initiating sending of  entry number detail information to KRA ITax system,
     * iTax system acknowledges KIMS system about the status of the information passed from the KIMS system.
     *
     * @params
     * req – @see ServerRequest for the end-point
     * GET /api/kra/send/entryNumber/{job}/start
     * it extracts the path variable {job} which represents the id of a batchJob
     *
     * Returns:
     * @return ServerResponse which is a String payload of the Job Submission status
     */
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

    /**
     * Endpoint responsible for initiating sending of  penalty information for manufacturers who have not
     * paid Standards Levy entry by a specific date to KRA ITax system,
     * iTax system acknowledges KIMS system about the status of the information passed from the KIMS system.
     *
     * @params
     * req – @see ServerRequest for the end-point
     * GET /api/kra/send/penalty/{job}/start
     * it extracts the path variable {job} which represents the id of a batchJob
     *
     * Returns:
     * @return ServerResponse which is a String payload of the Job Submission status
     */
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

    /**
     * API to be hosted by KIMS system which is going to be called
     * by iTax system to send payment information on
     * Standards Levy payments to KIMS system after successfully
     * generating the Standards Levy e-slip and payment being made on the iTax system
     *
     * @params
     * req – @see ServerRequest for the end-point
     * POST /api/kra/receiveSL2Payment with json payload as below
     * {@link ReceiveSL2PaymentRequest}
     *
     * Returns:
     * @return ServerResponse with payload as @see RequestResult
     */
    suspend fun processReceiveSL2Payment(req: ServerRequest): ServerResponse {

        return try {


            req.awaitBodyOrNull<String>()
                ?.let { stringData ->
                    val gson = Gson()
                    val body = gson.fromJson(stringData, ReceiveSL2PaymentRequest::class.java)
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

    /**
     * API to be hosted by KIMS system which is going to be called
     * for validing PIN entered by users
     * @params
     * req – @see ServerRequest
     * for the end-point
     * POST /api/kra/pinValidation/validate with json payload as below
     * @link ReceiveSL2PaymentRequest
     *
     * Returns:
     * @return ServerResponse with payload as
     *  @link org.kebs.app.kotlin.apollo.common.dto.kra.response.PinValidationResponse
     */
    suspend fun processValidatePin(req: ServerRequest): ServerResponse {
        return try {
            req.awaitBodyOrNull<PinValidationWebRequest>()
                ?.let { body ->
                    val errors: Errors = BeanPropertyBindingResult(body, PinValidationWebRequest::class.java.name)
                    validator.validate(body, errors)
                    if (errors.allErrors.isEmpty()) {
                        val response = service.processPinValidation(body)
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
