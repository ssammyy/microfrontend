package org.kebs.app.kotlin.apollo.api.handlers

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.RegistrationManagementDaoService
import org.kebs.app.kotlin.apollo.api.ports.provided.validation.AbstractValidationHandler
import org.kebs.app.kotlin.apollo.common.dto.UserCompanyEntityDto
import org.kebs.app.kotlin.apollo.common.exceptions.InvalidInputException
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.validation.Errors
import org.springframework.validation.Validator
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.body

@Component
class RegistrationManagementHandler(
    private val service: RegistrationManagementDaoService,
    private val validator: Validator
) : AbstractValidationHandler() {
    /**
     * Add or edit a company
     *
     * @param req ServerRequest
     * @return ServerResponse
     */
    @PreAuthorize("hasAuthority('MODIFY_COMPANY')")
    fun handleUpdateCompanyDetails(req: ServerRequest): ServerResponse {
        return try {
            val body = req.body<UserCompanyEntityDto>()
            val id = req.pathVariable("companyId").toLongOrNull()
            val payloadId = body.id
            if ((id ?: -1L) != (payloadId ?: -1L)) {
                throw InvalidInputException("Request Denied, record mismatch detected")
            }

            val errors: Errors = BeanPropertyBindingResult(body, UserCompanyEntityDto::class.java.name)
            validator.validate(body, errors)
            val user = service.loggedInUserDetails()
            when {
                errors.allErrors.isEmpty() -> {
                    service.updateCompanyDetails(body, user)
                        ?.let { ServerResponse.ok().body(it) }
                        ?: onErrors("We could not process your request at the moment")

                }
                else -> {
                    onValidationErrors(errors)
                }
            }

        } catch (e: Exception) {
            KotlinLogging.logger { }.debug(e.message, e)
            KotlinLogging.logger { }.error(e.message)
            onErrors(e.message)
        }
    }

    /**
     * Fetch a single company
     *
     * @param req ServerRequest
     * @return ServerResponse
     */
    @PreAuthorize("hasAuthority('LIST_COMPANY')")
    fun handleFetchCompanyById(req: ServerRequest): ServerResponse {
        return try {
            val body = req.pathVariable("companyId").toLongOrNull()


            val user = service.loggedInUserDetails()


            service.fetchCompanyById(
                body ?: throw NullValueNotAllowedException("Invalid record id provided"),
                user.id ?: throw NullValueNotAllowedException("Valid user required")
            )
                ?.let { ServerResponse.ok().body(it) }
                ?: onErrors("We could not process your request at the moment")


        } catch (e: Exception) {
            KotlinLogging.logger { }.debug(e.message, e)
            KotlinLogging.logger { }.error(e.message)
            onErrors(e.message)
        }
    }

    /**
     * Fetch a single company
     *
     * @param req ServerRequest
     * @return ServerResponse
     */
    @PreAuthorize("hasAuthority('LIST_COMPANY')")
    fun handleFetchCompaniesByUserId(req: ServerRequest): ServerResponse {
        return try {

            val user = service.loggedInUserDetails()

            service.fetchCompaniesByUserId(
                user.id ?: throw NullValueNotAllowedException("Valid user required")
            )
                ?.let { ServerResponse.ok().body(it) }
                ?: onErrors("We could not process your request at the moment")


        } catch (e: Exception) {
            KotlinLogging.logger { }.debug(e.message, e)
            KotlinLogging.logger { }.error(e.message)
            onErrors(e.message)
        }
    }

}