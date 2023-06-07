package org.kebs.app.kotlin.apollo.api.handlers

import com.nhaarman.mockitokotlin2.any
import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.payload.ApiResponseModel
import org.kebs.app.kotlin.apollo.api.payload.ResponseCodes
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.RegistrationManagementDaoService
import org.kebs.app.kotlin.apollo.api.ports.provided.validation.AbstractValidationHandler
import org.kebs.app.kotlin.apollo.common.dto.*
import org.kebs.app.kotlin.apollo.common.exceptions.InvalidInputException
import org.kebs.app.kotlin.apollo.common.exceptions.InvalidValueException
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.config.properties.auth.AuthenticationProperties
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import org.kebs.app.kotlin.apollo.store.model.std.DatKebsSdStandardsEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.ui.Model
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.validation.Errors
import org.springframework.validation.Validator
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.body

@Component
class RegistrationManagementHandler(
    private val service: RegistrationManagementDaoService,
    private val validator: Validator,
    private val commonDaoServices: CommonDaoServices,
    applicationMapProperties: ApplicationMapProperties,
    private val authenticationProperties: AuthenticationProperties
) : AbstractValidationHandler() {

    final val appId: Int = applicationMapProperties.mapUserRegistration

    /**
     * Endpoint to fetch the details of the users sidebar menu dynamically
     * @param req ServerRequest
     * @return ServerResponse
     */
    @PreAuthorize("isAuthenticated")
    fun handleGetSideBarMenusBasedOnLoggedInUser(req: ServerRequest): ServerResponse {
        return try {
            service.getSideBarMenusBasedOnLoggedInUser()
                ?.let { ServerResponse.ok().body(it) }
                ?: onErrors("We could not complete your request try again later")


        } catch (e: Exception) {
            KotlinLogging.logger { }.debug(e.message, e)
            KotlinLogging.logger { }.error(e.message)
            onErrors(e.message)

        }
    }

    /**
     * Validate the received payload of ValidateTokenRequestDto and send it to backend service for validation
     * @param req ServerRequest
     * @return ServerResponse
     *
     */
    @PreAuthorize("isAuthenticated")
    fun handleProvideCompanyDetailsForUser(req: ServerRequest): ServerResponse {
        return try {
            service.provideCompanyDetailsForUser()
                ?.let { ServerResponse.ok().body(it) }
                ?: onErrors("We could not complete your request try again later")

        } catch (e: Exception) {
            KotlinLogging.logger { }.debug(e.message, e)
            KotlinLogging.logger { }.error(e.message)
            onErrors(e.message)

        }
    }

    /**
     * Validate the received payload of ValidateTokenRequestDto and send it to backend service for validation
     * @param req ServerRequest
     * @return ServerResponse
     *
     */
    @PreAuthorize("isAnonymous")
    fun handleResetUserCredentials(req: ServerRequest): ServerResponse {
        return try {
            val body = req.body<LoginRequest>()
            val errors: Errors = BeanPropertyBindingResult(body, LoginRequest::class.java.name)
            validator.validate(body, errors)
            service.resetUserCredentials(body)
                ?.let { ServerResponse.ok().body(it) }
                ?: onErrors("We could not complete your request try again later")

        } catch (e: Exception) {
            KotlinLogging.logger { }.debug(e.message, e)
            KotlinLogging.logger { }.error(e.message)
            onErrors(e.message)

        }
    }

    fun notificationList(req: ServerRequest): ServerResponse {
        val response = ApiResponseModel()
        commonDaoServices.loggedInUserDetails()
            .let { userDetails ->
                userDetails.email?.let {
                    response.data = commonDaoServices.mapNotificationListDto(commonDaoServices.findAllUserNotification(it))
                    response.message = "Success"
                    response.responseCode = ResponseCodes.SUCCESS_CODE
                } ?: run {
                    response.message = "Email not configured"
                    response.responseCode = ResponseCodes.NOT_FOUND
                }
            }
        return ServerResponse.ok().body(response)
    }

    /**
     * Validate the received payload of ValidateTokenRequestDto and send it to backend service for validation
     * @param req ServerRequest
     * @return ServerResponse
     *
     */
    @PreAuthorize("isAnonymous")
    fun handleValidateTokenFromThePhone(req: ServerRequest): ServerResponse {
        return try {
            val body = req.body<ValidateTokenRequestDto>()
            val errors: Errors = BeanPropertyBindingResult(body, ValidateTokenRequestDto::class.java.name)
            validator.validate(body, errors)
            service.validateTokenFromThePhone(body, req)
                ?.let { ServerResponse.ok().body(it) }
                ?: onErrors("We could not complete your request try again later")

        } catch (e: Exception) {
            KotlinLogging.logger { }.debug(e.message, e)
            KotlinLogging.logger { }.error(e.message)
            onErrors(e.message)

        }
    }

    /**
     * Send OTP given the username as payload
     * @param req ServerRequest
     * @return ServerResponse
     *
     */
    @PreAuthorize("isAnonymous")
    fun handleSendTokenToThePhone(req: ServerRequest): ServerResponse {
        return try {
            val body = req.body<SendTokenRequestDto>()
            val errors: Errors = BeanPropertyBindingResult(body, SendTokenRequestDto::class.java.name)
            validator.validate(body, errors)
            service.sendTokenToThePhone(body)
                ?.let { ServerResponse.ok().body(it) }
                ?: onErrors("We could not complete your request try again later")

        } catch (e: Exception) {
            KotlinLogging.logger { }.debug(e.message, e)
            KotlinLogging.logger { }.error(e.message)
            onErrors(e.message)

        }
    }


    /**
     * Invalidate Token and Logout
     *
     * @param req ServerRequest
     * @return ServerResponse
     */
    @PreAuthorize("isAuthenticated")
    fun handleLogout(req: ServerRequest): ServerResponse {
        return try {
            val header = req.headers().header(authenticationProperties.authorizationHeader ?: "Authorization")[0]
            KotlinLogging.logger { }.trace(header)
            service.logout(header)
                ?.let { ServerResponse.ok().body(it) }
                ?: onErrors("We could not complete your request try again later")

        } catch (e: Exception) {
            KotlinLogging.logger { }.debug(e.message, e)
            KotlinLogging.logger { }.error(e.message)
            onErrors(e.message)

        }
    }

    /**
     * Fetch  the details of the user that has logged in, so as to provide for the user profile updates
     *
     * @param req ServerRequest
     * @return ServerResponse
     */
    @PreAuthorize("isAuthenticated")
    fun handleLoggedInUserUserEntityDtoDetails(req: ServerRequest): ServerResponse {
        return try {
            val header = req.headers().header(authenticationProperties.authorizationHeader ?: "Authorization")[0]
            val userId = req.pathVariable("userId").toLongOrNull()
            KotlinLogging.logger { }.trace(header)
            service.getLoggedInUserUserEntityDtoDetails(
                userId ?: throw NullValueNotAllowedException("Required id is missing")
            )
                ?.let { ServerResponse.ok().body(it) }
                ?: onErrors("We could not complete your request try again later")

        } catch (e: Exception) {
            KotlinLogging.logger { }.debug(e.message, e)
            KotlinLogging.logger { }.error(e.message)
            onErrors(e.message)

        }
    }

    /**
     * Fetch  the details of the user that has logged in, so as to provide for the user profile updates
     *
     * @param req ServerRequest
     * @return ServerResponse
     */
    @PreAuthorize("isAuthenticated")
    fun handleUpdateLoggedInUserUserEntityDtoDetails(req: ServerRequest): ServerResponse {
        return try {
            val body = req.body<UserEntityDto>()
            val header = req.headers().header(authenticationProperties.authorizationHeader ?: "Authorization")[0]
            val userId = req.pathVariable("userId").toLongOrNull()
            if (userId != body.id) throw InvalidValueException("Attempt to perform an operation that is not authorized")
            KotlinLogging.logger { }.trace(header)
            val errors: Errors = BeanPropertyBindingResult(body, UserEntityDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    service.updateLoggedInUserUserEntityDtoDetails(body)
                        ?.let { ServerResponse.ok().body(it) }
                        ?: onErrors("We could not complete your request try again later")

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
     * Add or edit a company
     *
     * @param req ServerRequest
     * @return ServerResponse
     */
    @PreAuthorize("hasAuthority('MODIFY_COMPANY')")
    fun handleUpdateCompanyDetails(req: ServerRequest): ServerResponse {
        return try {
            val map = commonDaoServices.serviceMapDetails(appId)
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
                    service.updateCompanyDetails(body, user, map)
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
     * update a company turnOver value
     *
     * @param req ServerRequest
     * @return ServerResponse
     */

    @PreAuthorize("hasAuthority('QA_MANAGER_MODIFY') or hasAuthority('QA_HOF_MODIFY') or hasAuthority('QA_HOD_MODIFY') or hasAuthority('QA_OFFICER_MODIFY') or hasAuthority('QA_ASSESSORS_MODIFY')")
    fun handleUpdateCompanyTurnOverDetails(req: ServerRequest): ServerResponse {
        return try {
            val map = commonDaoServices.serviceMapDetails(appId)
            val body = req.body<CompanyTurnOverUpdateDto>()

            val errors: Errors = BeanPropertyBindingResult(body, CompanyTurnOverUpdateDto::class.java.name)
            validator.validate(body, errors)
            val user = service.loggedInUserDetails()
            when {
                errors.allErrors.isEmpty() -> {
                    service.updateCompanyTurnOverDetails(body, user, map)
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
     * Add or edit a company branches
     *
     * @param req ServerRequest
     * @return ServerResponse
     */
    @PreAuthorize("hasAuthority('MODIFY_COMPANY')")
    fun handleUpdatePlantEntity(req: ServerRequest): ServerResponse {
        return try {
            val body = req.body<PlantEntityDto>()

            val companyId = req.pathVariable("companyId").toLongOrNull()
            val payloadId = body.id
            if (body.id != null) {
                val id = req.pathVariable("branchId").toLongOrNull()
                if ((id ?: -1L) != (payloadId ?: -1L)) {
                    throw InvalidInputException("Request Denied, record mismatch detected")
                }
            }


            if ((companyId ?: -1L) != (body.companyProfileId ?: -1L)) {
                throw InvalidInputException("Request Denied, record mismatch detected")
            }


            val errors: Errors = BeanPropertyBindingResult(body, PlantEntityDto::class.java.name)
            validator.validate(body, errors)
            val user = service.loggedInUserDetails()
            when {
                errors.allErrors.isEmpty() -> {
                    service.updatePlantEntity(body, user)
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
     * Add or edit a company directors
     *
     * @param req ServerRequest
     * @return ServerResponse
     */
    @PreAuthorize("hasAuthority('MODIFY_COMPANY')")
    fun handleUpdateProfileDirectors(req: ServerRequest): ServerResponse {
        return try {
            val body = req.body<ProfileDirectorsEntityDto>()
            val id = req.pathVariable("directorId").toLongOrNull()
            val companyId = req.pathVariable("companyId").toLongOrNull()
            val payloadId = body.id
            if ((id ?: -1L) != (payloadId ?: -1L)) {
                throw InvalidInputException("Request Denied, record mismatch detected")
            }

            if ((companyId ?: -1L) != (body.companyProfileId ?: -1L)) {
                throw InvalidInputException("Request Denied, record mismatch detected")
            }

            val errors: Errors = BeanPropertyBindingResult(body, ProfileDirectorsEntityDto::class.java.name)
            validator.validate(body, errors)
            val user = service.loggedInUserDetails()
            when {
                errors.allErrors.isEmpty() -> {
                    service.updateProfileDirectors(body, user)
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
     * Add or edit a company branche users
     *
     * @param req ServerRequest
     * @return ServerResponse
     */
    @PreAuthorize("hasAuthority('MODIFY_COMPANY')")
    fun handleUpdateBranchUsers(req: ServerRequest): ServerResponse {
        return try {
            val body = req.body<OrganizationUserEntityDto>()

            val branchId = req.pathVariable("branchId").toLongOrNull()
            val companyId = req.pathVariable("companyId").toLongOrNull()
            val payloadId = body.id
            if ((body.id ?: -1L) > 0L) {
                val id = req.pathVariable("userId").toLongOrNull()
                if ((id ?: -1L) != (payloadId ?: -1L)) {
                    throw InvalidInputException("Request Denied, record mismatch detected")
                }
            }


            if ((companyId ?: -1L) != (body.companyId ?: -1L)) {
                throw InvalidInputException("Request Denied, record mismatch detected")
            }

            if ((branchId ?: -1L) != (body.plantId ?: -1L)) {
                throw InvalidInputException("Request Denied, record mismatch detected")
            }

            val errors: Errors = BeanPropertyBindingResult(body, OrganizationUserEntityDto::class.java.name)
            validator.validate(body, errors)
            val user = service.loggedInUserDetails()
            when {
                errors.allErrors.isEmpty() -> {
                    service.updateBranchUsers(
                        body,
                        user,
                        branchId ?: throw NullValueNotAllowedException("Invalid Branch selected"),
                        companyId ?: throw NullValueNotAllowedException("Invalid Company selected")
                    )
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
    @PreAuthorize("isAuthenticated")
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
     * Fetch all company branches
     *
     * @param req ServerRequest
     * @return ServerResponse
     */
    @PreAuthorize("isAuthenticated")
    fun handleFetchBranchesByCompanyIdAndUserId(req: ServerRequest): ServerResponse {
        return try {
            val body = req.pathVariable("companyId").toLongOrNull()


            val user = service.loggedInUserDetails()


            service.fetchBranchesByCompanyIdAndUserId(
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
     * Fetch a all directors
     *
     * @param req ServerRequest
     * @return ServerResponse
     */
    @PreAuthorize("hasAuthority('LIST_COMPANY')")
    fun handleFetchDirectorsByCompanyIdAndUserId(req: ServerRequest): ServerResponse {
        return try {
            val body = req.pathVariable("companyId").toLongOrNull()


            val user = service.loggedInUserDetails()


            service.fetchDirectorsByCompanyIdAndUserId(
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
    fun handleFetchUsersByCompanyIdAndBranchIdAndUserId(req: ServerRequest): ServerResponse {
        return try {

            val branchId = req.pathVariable("branchId").toLongOrNull()
            val companyId = req.pathVariable("companyId").toLongOrNull()


            val user = service.loggedInUserDetails()


            service.fetchUsersByCompanyIdAndBranchIdAndUserId(
                companyId ?: throw NullValueNotAllowedException("Invalid Company selected"),
                branchId ?: throw NullValueNotAllowedException("Invalid Branch id selected"),
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

    /**
     * Fetch a single Director
     *
     * @param req ServerRequest
     * @return ServerResponse
     */
    @PreAuthorize("hasAuthority('LIST_COMPANY')")
    fun handleFetchDirectorsByIdAndUserId(req: ServerRequest): ServerResponse {
        return try {

            val user = service.loggedInUserDetails()

            val companyId = req.pathVariable("companyId").toLongOrNull()

            service.fetchDirectorsByIdAndUserId(
                companyId ?: throw NullValueNotAllowedException("Valid company required"),
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
     * Fetch a single Branch
     *
     * @param req ServerRequest
     * @return ServerResponse
     */
    @PreAuthorize("hasAuthority('LIST_COMPANY')")
    fun handleFetchBranchesByIdAndUserId(req: ServerRequest): ServerResponse {
        return try {

            val user = service.loggedInUserDetails()
            val companyId = req.pathVariable("companyId").toLongOrNull()

            service.fetchBranchesByIdAndUserId(
                companyId ?: throw NullValueNotAllowedException("Valid company required"),
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
     * Fetch a single User
     *
     * @param req ServerRequest
     * @return ServerResponse
     */
    @PreAuthorize("hasAuthority('LIST_COMPANY')")
    fun handleFetchUserByIdAndUserId(req: ServerRequest): ServerResponse {
        return try {

            val user = service.loggedInUserDetails()
            val companyId = req.pathVariable("companyId").toLongOrNull()
            val userId = req.pathVariable("userId").toLongOrNull()

            service.fetchUserByIdAndUserId(
                companyId ?: throw NullValueNotAllowedException("Valid company required"),
                user.id ?: throw NullValueNotAllowedException("Valid user required"),
                userId ?: throw NullValueNotAllowedException("Valid record required")
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
