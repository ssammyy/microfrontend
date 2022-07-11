package org.kebs.app.kotlin.apollo.api.handlers

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.introspect.VisibilityChecker
import com.google.gson.Gson
import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.kra.StandardsLevyDaoService
import org.kebs.app.kotlin.apollo.api.ports.provided.validation.AbstractValidationHandler
import org.kebs.app.kotlin.apollo.common.dto.kra.request.RootKra
import org.kebs.app.kotlin.apollo.common.exceptions.ExpectedDataNotFound
import org.springframework.stereotype.Component
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.validation.Errors
import org.springframework.validation.Validator
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.body


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
    private val commonDaoServices: CommonDaoServices,
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
//    fun processSendEntryNumbers(req: ServerRequest): ServerResponse {
//        return try {
//            val jobId = req.pathVariable("job")
//
//            ServerResponse.ok().body(service.startJob(jobId))
//
//        } catch (e: Exception) {
//            KotlinLogging.logger { }.debug(e.message, e)
//            KotlinLogging.logger { }.error(e.message)
//            onErrors(e.message)
//
//        }
//    }

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
//     fun processSendPenalties(req: ServerRequest): ServerResponse {
//        return try {
//            val jobId = req.pathVariable("job")
//
//            ServerResponse.ok().body(service.startPenaltyJob(jobId))
//
//        } catch (e: Exception) {
//            KotlinLogging.logger { }.debug(e.message, e)
//            KotlinLogging.logger { }.error(e.message)
//            onErrors(e.message)
//
//        }
//    }

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
     fun processReceiveSL2Payment(req: ServerRequest): ServerResponse {

        return try {

            val stringData = req.body<String>()

            val mapper = ObjectMapper()
            mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
//            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
//            mapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
            val removedString = commonDaoServices.removeQuotesAndUnescape(stringData)
            val body: RootKra = mapper.readValue(removedString, RootKra::class.java)
            KotlinLogging.logger { }.info { "Payment Body 2 ${body}" }
            val errors: Errors = BeanPropertyBindingResult(body, RootKra::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {

                    val requestBody = body.request?: throw ExpectedDataNotFound("Missing request value")
                    KotlinLogging.logger { }.info { "Payment Body 4 ${requestBody}" }
                    val response = service.processSl2Payments(requestBody)
                    ServerResponse.ok().body(response)
                }
                else -> {
                    onValidationErrors(errors)
                }
            }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
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
//     fun processValidatePin(req: ServerRequest): ServerResponse {
//        return try {
//            req.body<PinValidationWebRequest>()
//                .let { body ->
//                    val errors: Errors = BeanPropertyBindingResult(body, PinValidationWebRequest::class.java.name)
//                    validator.validate(body, errors)
//                    if (errors.allErrors.isEmpty()) {
//                        val response = service.processPinValidation(body)
//                        ServerResponse.ok().body(response)
//
//                    } else {
//                        onValidationErrors(errors)
//                    }
//
//                }
//                ?: throw InvalidValueException("No Body found")
//
//        } catch (e: Exception) {
//            KotlinLogging.logger { }.debug(e.message, e)
//            KotlinLogging.logger { }.error(e.message)
//            onErrors(e.message)
//        }
//    }

}
