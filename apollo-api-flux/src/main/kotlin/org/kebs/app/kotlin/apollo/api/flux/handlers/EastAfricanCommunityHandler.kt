package org.kebs.app.kotlin.apollo.api.flux.handlers

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.flux.ports.provided.dao.EastAfricanCommunityDao
import org.kebs.app.kotlin.apollo.api.flux.ports.provided.dao.kra.StandardsLevyDaoService
import org.kebs.app.kotlin.apollo.api.flux.ports.provided.validation.AbstractValidationHandler
import org.springframework.stereotype.Component
import org.springframework.validation.Validator
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait

/**
 *
 * <H1>EastAfricanCommunityHandler</H1>
 * a handler class For processing requests for the EAC integration
 *  @author Kenneth KZM. Muhia
 *  @since 1.0.0
 * Constructor values are to inject spring beans below
 *  @see Validator
 *  @see StandardsLevyDaoService
 *
 *  The class implements @see AbstractValidationHandler
 */
@Component
class EastAfricanCommunityHandler(
    private val service: EastAfricanCommunityDao
) : AbstractValidationHandler() {

    /**
     * Extract payload from request and submit it to the data access object
     * Input validation is performed at this time in case of errors return @link org.springframework.http.HttpStatus.BAD_REQUEST otherwise
     * @link org.springframework.http.HttpStatus.OK confirming job submission
     * @param req  Request received on the API
     */
    suspend fun postProduct(req: ServerRequest): ServerResponse {
        return try {
            val jobId = req.pathVariable("job")

            ServerResponse.ok().bodyValueAndAwait(service.startProductPostJob(jobId))

        } catch (e: Exception) {
            KotlinLogging.logger { }.debug(e.message, e)
            KotlinLogging.logger { }.error(e.message)
            onErrors(e.message)

        }
    }
}
