/*
 *
 *  *
 *  *
 *  *  *    Copyright (c) ${YEAR}.   BSK Global Technologies
 *  *  *
 *  *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *    you may not use this file except in compliance with the License.
 *  *  *    You may obtain a copy of the License at
 *  *  *
 *  *  *       http://www.apache.org/licenses/LICENSE-2.0
 *  *  *
 *  *  *    Unless required by applicable law or agreed to in writing, software
 *  *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *   See the License for the specific language governing permissions and
 *  *  *   limitations under the License.
 *  *
 *
 */

package org.kebs.app.kotlin.apollo.api.handlers

import mu.KotlinLogging
import org.apache.commons.lang3.StringUtils
import org.kebs.app.kotlin.apollo.adaptor.kafka.producer.service.SendToKafkaQueue
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.RegistrationDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.SystemsAdminDaoService
import org.kebs.app.kotlin.apollo.api.ports.provided.validation.AbstractValidationHandler
import org.kebs.app.kotlin.apollo.common.dto.*
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.common.exceptions.PasswordsMismatchException
import org.kebs.app.kotlin.apollo.common.exceptions.ServiceMapNotFoundException
import org.kebs.app.kotlin.apollo.common.utils.generateRandomText
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.repo.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler
import org.springframework.stereotype.Component
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.validation.Errors
import org.springframework.validation.Validator
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.ServerResponse.ok
import org.springframework.web.servlet.function.body
import org.springframework.web.servlet.function.paramOrNull
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.io.Serializable
import java.time.Instant


@Component
class RegistrationHandler(
    private val applicationMapProperties: ApplicationMapProperties,
    private val systemsAdminDaoService: SystemsAdminDaoService,

    private val sendToKafkaQueue: SendToKafkaQueue,
    private val countriesRepository: ICountriesRepository,
    private val serviceMapsRepository: IServiceMapsRepository,
    private val businessLinesRepository: IBusinessLinesRepository,
    private val commonDaoServices: CommonDaoServices,
    private val businessNatureRepository: IBusinessNatureRepository,
    private val contactTypesRepository: IContactTypesRepository,
    private val userTypesRepo: IUserTypesEntityRepository,
    private val SubSectionsLevel1Repo: ISubSectionsLevel1Repository,
    private val SubSectionsLevel2Repo: ISubSectionsLevel2Repository,
    private val titlesRepository: ITitlesRepository,
    private val divisionsRepo: IDivisionsRepository,
    private val departmentsRepo: IDepartmentsRepository,
    private val directorateRepo: IDirectoratesRepository,
    private val designationRepository: IDesignationsRepository,
    private val sectionsRepo: ISectionsRepository,
    private val subRegionsRepository: ISubRegionsRepository,
    private val regionsRepository: IRegionsRepository,
    private val usersRepo: IUserRepository,
    private val userProfilesRepo: IUserProfilesRepository,
    private val userVerificationTokensRepository: IUserVerificationTokensRepository,
    private val daoServices: RegistrationDaoServices,
    private val validator: Validator
) : AbstractValidationHandler() {
    @Value("\${user.profile.employee.userTypeID}")
    lateinit var employeeUserTypeId: String

    @Value("\${user.profile.manufacturer.userTypeID}")
    lateinit var manufacturerUserTypeId: String

    @Value("\${user.profile.importer.userTypeID}")
    lateinit var importerUserTypeId: String

    @Value("\${user.profile.employee.userType}")
    lateinit var employeeUserType: String

    @Value("\${user.profile.manufacturer.userType}")
    lateinit var manufacturerUserType: String

    @Value("\${user.profile.importer.userType}")
    lateinit var importerUserType: String

    @Value("\${common.active.status}")
    lateinit var activeStatus: String

    @Value("\${common.inactive.status}")
    lateinit var inActiveStatus: String


    private final val newUserProfilePage = "admin/employee-registration"


    final val appId: Int = applicationMapProperties.mapUserRegistration

    /***********************************************************************************
     * NEW REGISTRATIONION HANDLERS
     ***********************************************************************************/


    fun signUpAllUsers(req: ServerRequest): ServerResponse {
        try {
            val map = commonDaoServices.serviceMapDetails(appId)
            val dto = req.body<UserEntityDto>()
            dto.id = -1L
            dto.userName = dto.userPinIdNumber
            dto.userRegNo =
                "KEBS${generateRandomText(5, map.secureRandom, map.messageDigestAlgorithm, true).toUpperCase()}"
            systemsAdminDaoService.updateUserDetails(dto)?.let {
                return ok().body(it)
            }
                ?: throw NullValueNotAllowedException("Update failed")

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
            KotlinLogging.logger { }.debug(e.message, e)
            return ServerResponse.badRequest().body(e.message ?: "Unknown Error")
        }
    }


    fun signUpAllUsersVerification(req: ServerRequest): ServerResponse {
        return try {
            val dto = req.body<UserPasswordVerificationValuesDto>()
            when (StringUtils.equals(dto.password, dto.passwordConfirmation)) {
                true -> {
                    ok().body(daoServices.validateVerificationToken(dto, appId))
                }
                else -> throw PasswordsMismatchException("Passwords and Confirmation do not match")
            }

        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "Unknown Error")
        }
    }

    fun signUpUserRestPassword(req: ServerRequest): ServerResponse {
        return try {
            val dto = req.body<UserPasswordResetValuesDto>()
            val user = dto.emailUsername?.let { commonDaoServices.findUserByUserName(it) }
                ?: throw NullValueNotAllowedException("User with user name ${dto.emailUsername} do not exist")
            ok().body(
                systemsAdminDaoService.userRegistrationMailSending(
                    user,
                    null,
                    applicationMapProperties.mapUserPasswordResetNotification
                )
            )


        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "Unknown Error")
        }
    }

    /***********************************************************************************
     * OLD REGISTRATIONION HANDLERS
     ***********************************************************************************/

    fun submitAuthorizeUserAccount(req: ServerRequest): ServerResponse =
        req.paramOrNull("appId")
            ?.let { mapId ->
                daoServices.extractServiceMapFromAppId(mapId)
                    ?.let { map ->
                        req.paramOrNull("messageKey")
                            ?.let { token ->
                                daoServices.findServiceRequestByTransactionRef(token)
                                    ?.let {
                                        var sr = it
                                        daoServices.valVerificatToken(sr, token)
                                            .let {
                                                userVerificationTokensRepository.findByTokenAndStatus(
                                                    token,
                                                    map.successStatus
                                                )
                                                    ?.let { v ->
                                                        v.userId
                                                            ?.let { user ->
                                                                val credentials = req.paramOrNull("credentials")
                                                                val confirmCredentials =
                                                                    req.paramOrNull("confirmCredentials")
                                                                when (StringUtils.equals(
                                                                    credentials,
                                                                    confirmCredentials
                                                                )) {

                                                                    true -> {
                                                                        user.credentials =
                                                                            BCryptPasswordEncoder().encode(credentials)
                                                                        user.confirmCredentials =
                                                                            BCryptPasswordEncoder().encode(
                                                                                confirmCredentials
                                                                            )
                                                                        daoServices.resetUserPassword(
                                                                            map,
                                                                            user,
                                                                            true,
                                                                            sr
                                                                        )?.let { requestsEntity -> sr = requestsEntity }
//                                                                                                        daoServices.completeTask(proc, sr)
                                                                        sr.status?.let { status ->
                                                                            return when {
                                                                                status > map.activeStatus -> {
                                                                                    req.attributes()["message"] =
                                                                                        "Account authorization completed successfully"
                                                                                    req.attributes()["closeLink"] = "/"
                                                                                    req.attributes()["activateAccountLink"] =
                                                                                        ""
                                                                                    req.attributes()["activateAccountLinkText"] =
                                                                                        ""
                                                                                    req.attributes()["otherActionLink"] =
                                                                                        ""
                                                                                    req.attributes()["otherActionLinkText"] =
                                                                                        ""
                                                                                    ok().render(
                                                                                        "/auth/register-success-view",
                                                                                        req.attributes()
                                                                                    )
                                                                                }
                                                                                else -> {
                                                                                    ok().render(
                                                                                        "/auth/authorize-account-confirmation",
                                                                                        req.attributes()
                                                                                    )
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                    else -> throw PasswordsMismatchException("Passwords and Confirmation do not match")
                                                                }
                                                            }
                                                    }
                                                    ?: throw Exception("Expired Verification Token Received")
                                            }
//                                                            daoServices.extractUserFromValidationToken(sr.transactionReference, map)

                                    }
                                    ?: throw NullValueNotAllowedException("Received request with empty token")

                            }
                            ?: throw NullValueNotAllowedException("Received request with empty token")
                    }
                    ?: throw ServiceMapNotFoundException("Missing application mapping for [id=$appId], recheck configuration")

            }
            ?: throw NullValueNotAllowedException("Received request with empty appId")

    private val redirectAttributes: RedirectAttributes? = null
    private fun loadManufacturerUIComponents(s: ServiceMapsEntity): MutableMap<String, List<Serializable>?> {

        return mutableMapOf(
            Pair("businessLines", businessLinesRepository.findByStatus(s.activeStatus)),
//                Pair("businessNatures", businessNatureRepository.findByStatus(s.activeStatus)),
            Pair("contactTypes", contactTypesRepository.findByStatus(s.activeStatus)),
            Pair("userTypes", userTypesRepo.findByStatus(s.activeStatus))
        )


    }

    private fun loadImporterExporterUIComponents(s: ServiceMapsEntity): MutableMap<String, Any> {
        return mutableMapOf(
            Pair("userTypes", userTypesRepo.findByStatus(s.activeStatus))
        )
    }

    private fun loadMapPropertiesUIComponents(s: ServiceMapsEntity): MutableMap<String, Any> {
        return mutableMapOf(
            Pair("mapUserTypeEmployee", applicationMapProperties.mapUserTypeEmployee),
            Pair("mapUserTypeManufacture", applicationMapProperties.mapUserTypeManufacture),
            Pair("mapUserTypeImporter", applicationMapProperties.mapUserTypeImporter)
        )
    }


    private fun loadEmployeeUIComponents(s: ServiceMapsEntity): MutableMap<String, Any?> {

        return mutableMapOf(
            Pair("titles", titlesRepository.findByStatus(s.activeStatus)),
//                Pair("subRegions", subRegionsRepository.findByStatusOrderBySubRegion(s.activeStatus)),
            Pair("regions", regionsRepository.findByStatusOrderByRegion(s.activeStatus)),
//                Pair("divisions", divisionsRepo.findByStatusOrderByDivision(s.activeStatus)),
            Pair("directorates", directorateRepo.findByStatusOrderByDirectorate(s.activeStatus)),
//                Pair("departments", departmentsRepo.findByStatusOrderByDepartment(s.activeStatus)),
            Pair("designations", designationRepository.findByStatus(s.activeStatus)),
            Pair("userTypes", userTypesRepo.findByStatus(s.activeStatus))
//                Pair("subSectionsL1", SubSectionsLevel1Repo.findByStatusOrderBySubSection(s.activeStatus)),
//                Pair("subSectionsL2", SubSectionsLevel2Repo.findByStatusOrderBySubSection(s.activeStatus)),
//                Pair("sections", sectionsRepo.findByStatusOrderBySection(s.activeStatus))
        )


    }

    fun signOut(req: ServerRequest): ServerResponse =
        SecurityContextHolder.getContext().authentication
            ?.let { auth ->
                SecurityContextLogoutHandler().logout(req.servletRequest(), null, auth)
                req.session().invalidate()
                for (cookie in req.cookies()) {
                    cookie.setValue(null)
                }
                ok().render("redirect:/auth/login?logout")
            }
            ?: ok().render("redirect:/auth/login?logout")


    fun loginPageView(req: ServerRequest): ServerResponse = ok().render("/auth/login")

    fun userNewFormView(req: ServerRequest): ServerResponse {
        serviceMapsRepository.findByIdAndStatus(appId, activeStatus.toInt())
            ?.let { map ->

                req.attributes()["usersEntity"] = UsersEntity()
                req.attributes()["userProfilesEntity"] = UserProfilesEntity()
                req.attributes()["map"] = map
                req.attributes().putAll(loadEmployeeUIComponents(map))

                return ok().render(newUserProfilePage, req.attributes())
            }
            ?: throw Exception("Missing application mapping for [id=$appId], recheck configuration")

    }

    fun signUpEmployeeView(req: ServerRequest): ServerResponse =
        serviceMapsRepository.findByIdAndStatus(appId, 1)
            ?.let { map ->
                req.attributes().putAll(loadEmployeeUIComponents(map))
                req.attributes()["employeesEntity"] = EmployeesEntity()
                req.attributes()["userProfilesEntity"] = UserProfilesEntity()
                req.attributes()["usersEntity"] = UsersEntity()
                req.attributes()["map"] = map

                return ok().render("/admin/employee-registration", req.attributes())
            }
            ?: throw Exception("Missing application mapping for [id=$appId], recheck configuration")

    fun registrationSuccessNotificationView(req: ServerRequest): ServerResponse =
        serviceMapsRepository.findByIdAndStatus(appId, 1)
            ?.let {
                req.attributes()["message"] =
                    "Successfully submitted registration details, check your mail for activation details"
                req.attributes()["closeLink"] = "/"
                req.attributes()["activateAccountLink"] = ""
                req.attributes()["activateAccountLinkText"] = ""
                req.attributes()["otherActionLink"] = ""
                req.attributes()["otherActionLinkText"] = ""
                return ok().render("/auth/register-success-view", req.attributes())
            }
            ?: throw ServiceMapNotFoundException("Missing application mapping for [id=$appId], recheck configuration")


    fun successNotificationView(req: ServerRequest): ServerResponse =
        serviceMapsRepository.findByIdAndStatus(appId, 1)
            ?.let {
                req.attributes()["message"] = req.paramOrNull("message")
                req.attributes()["closeLink"] = req.paramOrNull("closeLink")
                req.attributes()["activateAccountLink"] = ""
                req.attributes()["activateAccountLinkText"] = ""
                req.attributes()["otherActionLink"] = ""
                req.attributes()["otherActionLinkText"] = ""
                return ServerResponse.ok().render("/auth/register-success-view", req.attributes())
            }
            ?: throw ServiceMapNotFoundException("Missing application mapping for [id=$appId], recheck configuration")

//    fun authorizeActivateAccount(req: ServerRequest): ServerResponse {
//        applicationMapProperties.mapUserActivation
//                ?.let { mapId ->
//                    serviceMapsRepository.findByIdAndStatus(mapId, 1)
//                            ?.let { map ->
//                                req.attributes()["usersEntity"] = UsersEntity()
//                                req.attributes()["typeName"] = "ACTIVATEUSER"
//                                req.attributes()["verificationTokensEntity"] = UserVerificationTokensEntity()
//                                req.attributes()["map"] = map
//                            }
//                }
//
//        return ok().render("/auth/user-update-profile", req.attributes())
//    }

    fun submitPasswordReset(req: ServerRequest): ServerResponse {
        req.paramOrNull("mapId")
            ?.let { mapId ->
                daoServices.extractServiceMapFromAppId(mapId)
                    ?.let { map ->
                        req.paramOrNull("messageKey")
                            ?.let { token ->
                                daoServices.findServiceRequestByTransactionRef(token)
                                    ?.let {
                                        var sr = it

                                        req.paramOrNull("message")
                                            ?.let { userId ->
                                                daoServices.findUserById(userId)
                                                    ?.let { user ->

                                                        val credentials = req.paramOrNull("credentials")
                                                        val confirmCredentials = req.paramOrNull("confirmCredentials")
                                                        when (StringUtils.equals(credentials, confirmCredentials)) {

                                                            true -> {
                                                                user.credentials =
                                                                    BCryptPasswordEncoder().encode(credentials)
                                                                user.confirmCredentials =
                                                                    BCryptPasswordEncoder().encode(confirmCredentials)
                                                                daoServices.resetUserPassword(map, user, true, sr)
                                                                    ?.let { requestsEntity -> sr = requestsEntity }

                                                                sr.status?.let { status ->
                                                                    return when {
                                                                        status > map.activeStatus -> {
                                                                            req.attributes()["message"] =
                                                                                "Account authorization completed successfully"
                                                                            req.attributes()["closeLink"] = "/"
                                                                            req.attributes()["activateAccountLink"] = ""
                                                                            req.attributes()["activateAccountLinkText"] =
                                                                                ""
                                                                            req.attributes()["otherActionLink"] = ""
                                                                            req.attributes()["otherActionLinkText"] = ""
                                                                            ok().render(
                                                                                "/auth/register-success-view",
                                                                                req.attributes()
                                                                            )

                                                                        }
                                                                        else -> {
                                                                            req.attributes()["token"] = token
                                                                            req.attributes()["map"] = map
                                                                            req.attributes()["usersEntity"] = user
                                                                            ok().render(
                                                                                "auth/confirm-password-reset.html",
                                                                                req.attributes()
                                                                            )

                                                                        }
                                                                    }

                                                                }


                                                            }
                                                            else -> throw PasswordsMismatchException("Passwords and Confirmation do not match")
                                                        }


                                                    }
                                                    ?: throw NullValueNotAllowedException("Invalid User Request")


                                            }
                                            ?: throw NullValueNotAllowedException("Received request with empty message")
                                    }
                                    ?: throw NullValueNotAllowedException("Received request with empty token")

                            }
                            ?: throw NullValueNotAllowedException("Received request with empty token")
                    }
                    ?: throw ServiceMapNotFoundException("Missing application mapping for [id=$appId], recheck configuration")

            }
            ?: throw NullValueNotAllowedException("Received request with empty appId")

    }

    fun submitCredentialsUserAccount(
        user: UsersEntity,
        map: ServiceMapsEntity,
        sr: ServiceRequestsEntity,
        req: ServerRequest
    ): ServerResponse? {
        var sr = sr
        var myRender: ServerResponse? = null
        daoServices.processInstance(sr)
            ?.let { proc ->
                val credentials = req.paramOrNull("credentials")
                val confirmCredentials = req.paramOrNull("confirmCredentials")
                when (StringUtils.equals(credentials, confirmCredentials)) {
                    true -> {
                        user.credentials = BCryptPasswordEncoder().encode(credentials)
                        user.confirmCredentials = BCryptPasswordEncoder().encode(confirmCredentials)
                        daoServices.resetUserPassword(map, user, true, sr)
                            ?.let { requestsEntity -> sr = requestsEntity }
                        daoServices.completeTask(proc, sr)
                        sr.status?.let { status ->
                            when {
                                status > map.activeStatus -> {
                                    req.attributes()["message"] = "Account Activated successfully"
                                    req.attributes()["closeLink"] = "/"
                                    req.attributes()["activateAccountLink"] = ""
                                    req.attributes()["activateAccountLinkText"] = ""
                                    req.attributes()["otherActionLink"] = ""
                                    req.attributes()["otherActionLinkText"] = ""
                                    myRender = ok().render("/auth/register-success-view", req.attributes())

                                }
                                else -> {
                                    myRender = ok().render("/auth/authorize-account-confirmation", req.attributes())

                                }
                            }

                        }


                    }
                    else -> throw PasswordsMismatchException("Passwords and Confirmation do not match")
                }
            }

        return myRender
    }


    fun forgotPasswordAction(req: ServerRequest): ServerResponse {
        req.paramOrNull("appId")
            ?.let { appId ->
                req.paramOrNull("email")
                    ?.let { email ->
                        daoServices.extractUserFromEmail(email)
                            ?.let { user ->

                                user.email?.let { e ->
                                    daoServices.findNotificationByToken(
                                        req.paramOrNull("token"),
                                        appId,
                                        mutableListOf(e),
                                        user
                                    )
                                        ?.let { buffers ->
                                            buffers.forEach { buffer ->
                                                /**
                                                 * TODO: Make topic a field on the Buffer table
                                                 */
                                                buffer.varField1?.let { topic ->
                                                    sendToKafkaQueue.submitAsyncRequestToBus(buffer, topic)
                                                }
                                            }
                                        }


                                }


                                req.attributes()["continue"] = true

                            }
                            ?: throw NullValueNotAllowedException("Invalid Request")

                        return ok().render("auth/forgot-pass-new", req.attributes())

                    }
                    ?: throw NullValueNotAllowedException("Provide a valid email")
            }
            ?: throw NullValueNotAllowedException("Invalid App Id")


    }

    fun forgotPasswordView(req: ServerRequest): ServerResponse {

        req.attributes()["usersEntity"] = UsersEntity()
        return ok().render("auth/forgot-pass-new", req.attributes())
    }

    fun forgotPasswordOtp(req: ServerRequest): ServerResponse {

        req.attributes()["userVerificationTokensEntity"] = UserVerificationTokensEntity()
        return ok().render("auth/otp-verification", req.attributes())
    }

    fun resetPasswordOtp(req: ServerRequest): ServerResponse {

        req.attributes()["userVerificationTokensEntity"] = UserVerificationTokensEntity()
        return ok().render("auth/reset-password", req.attributes())
    }

    fun confirmOtpView(req: ServerRequest): ServerResponse {
        return ok().render("auth/otp-confirmation")
    }

    fun resetPasswordView(req: ServerRequest): ServerResponse {
        daoServices.extractServiceMapFromAppId(req.paramOrNull("appId") ?: "128")
            ?.let { map ->
                req.paramOrNull("token")
                    ?.let { token ->
                        daoServices.findServiceRequestByTransactionRef(token)
                            ?.let { sr ->
                                usersRepo.findByIdOrNull(sr.requestId)
                                    ?.let { user ->
                                        req.attributes()["token"] = token
                                        req.attributes()["map"] = map
                                        req.attributes()["usersEntity"] = user
                                        return ok().render("/auth/confirm-password-reset", req.attributes())

                                    }
                                    ?: throw NullValueNotAllowedException("Invalid Request")


                            }
                            ?: throw NullValueNotAllowedException("Invalid Request")


                    }
                    ?: throw NullValueNotAllowedException("Received request with empty token")

            }
            ?: throw ServiceMapNotFoundException("Missing application mapping for [id=$appId], recheck configuration")

    }


    fun authorizeUAccountView(req: ServerRequest): ServerResponse =
        req.pathVariable("userName").let { userName ->
            req.attributes()["usersEntity"] = commonDaoServices.findUserByUserName(userName)
            ok().render("/auth/authorize-account-confirmation", req.attributes())
        }

    fun authorizeUserAccountView(req: ServerRequest): ServerResponse =
        req.paramOrNull("appId")
            ?.let { mapId ->
                daoServices.extractServiceMapFromAppId(mapId)
                    ?.let { map ->
                        req.paramOrNull("token")
                            ?.let { token ->
                                userVerificationTokensRepository.findByTokenAndStatus(token, map.initStatus)
                                    ?.let { verificationToken ->
                                        verificationToken.tokenExpiryDate?.let { expiry ->
                                            when {
                                                expiry.toInstant().isAfter(Instant.now()) -> {
                                                    daoServices.findServiceRequestByTransactionRef(token)
                                                        ?.let { sr ->
//                                                                                    daoServices.validateVerificationToken(sr, token)
//                                                                                    daoServices.extractUserFromValidationToken(sr.transactionReference, map)
//                                                                                            ?.let { user ->
                                                            req.attributes()["token"] = token
                                                            req.attributes()["usersEntity"] = verificationToken.userId
                                                            req.attributes()["map"] = map
                                                            return ok().render(
                                                                "/auth/authorize-account-confirmation",
                                                                req.attributes()
                                                            )
//                                                                                            }
//                                                                                            ?: throw NullValueNotAllowedException("Invalid User Request")

                                                        }
                                                        ?: throw NullValueNotAllowedException("Invalid request")
                                                }
                                                else -> {
                                                    throw Exception("Invalid Validation Request")
                                                }
                                            }

                                        }


                                    }
                                    ?: throw Exception("Invalid Requests")

                            }
                            ?: throw NullValueNotAllowedException("Received request with empty token")

                    }
                    ?: throw ServiceMapNotFoundException("Missing application mapping for [id=$appId], recheck configuration")

            }
            ?: throw NullValueNotAllowedException("Received request with empty appId")

//    fun activateUserAccountView(req: ServerRequest): ServerResponse =
//            req.paramOrNull("appId")
//                    ?.let { mapId ->
//                        serviceMapsRepository.findByIdAndStatus(mapId.toIntOrNull(), 1)
//                                ?.let { map ->
//                                    val token = req.param("token")
//
//                                    userVerificationTokensRepository.findByTokenAndStatus(token.get(), map.initStatus)
//                                            ?.let { verificationToken ->
//                                                verificationToken.tokenExpiryDate?.let { expiry ->
//                                                    when {
//                                                        expiry.toInstant().isAfter(Instant.now()) -> {
//                                                            usersRepo.findByIdOrNull(verificationToken.userId?.id)
//                                                                    ?.let { usersEntity ->
//                                                                        req.attributes()["token"] = verificationToken.token
//                                                                        req.attributes()["map"] = map
//                                                                        req.attributes()["usersEntity"] = usersEntity
//                                                                        return ok().render("/auth/confirm-account-activation", req.attributes())
//                                                                    }
//
//                                                        }
//                                                        else -> {
//                                                            throw Exception("Invalid Validation Request")
//                                                        }
//                                                    }
//
//                                                }
//
//
//                                            }
//                                            ?: throw Exception("Invalid Requests")
//
//
//                                }
//                                ?: throw Exception("Missing application mapping for [id=$appId], recheck configuration")
//
//
//                    }
//                    ?: throw Exception("Missing application mapping for [id=$appId], recheck configuration")


//    fun submitActivateUserAccount(req: ServerRequest): ServerResponse =
//            req.paramOrNull("mapId")
//                    ?.let { mapId ->
//                        serviceMapsRepository.findByIdAndStatus(mapId.toIntOrNull(), 1)
//                                ?.let { _ ->
//                                    val token = req.param("token")
//
//                                    token.ifPresent {
//                                        daoServices.findServiceRequestByTransactionRef(it)
//                                                ?.let { sr ->
//                                                    daoServices.validateVerificationToken(sr, it)
//                                                    req.attributes()["message"] = "Successfully submitted account verification request, check your mail for activation details"
//                                                    req.attributes()["closeLink"] = "/"
//                                                    req.attributes()["activateAccountLink"] = ""
//                                                    req.attributes()["activateAccountLinkText"] = ""
//                                                    req.attributes()["otherActionLink"] = ""
//                                                    req.attributes()["otherActionLinkText"] = ""
//
//                                                }
//                                                ?: throw NullValueNotAllowedException("ServiceRequest with ref $it not found")
//
//                                    }
//
//                                    return ok().render("/auth/register-success-view", req.attributes())
//
//                                }
//                                ?: throw Exception("Missing application mapping for [id=$appId], recheck configuration")
//                    }
//                    ?: throw Exception("Missing application mapping for [id=$appId], recheck configuration")


    fun registrationFailureNotificationView(req: ServerRequest): ServerResponse =
        serviceMapsRepository.findByIdAndStatus(appId, 1)
            ?.let {
                req.attributes()["message"] = "Registration failed! Try again later"
                req.attributes()["closeLink"] = "/"
                req.attributes()["otherActionLink"] = "/api/auth/signup/employee"
                req.attributes()["otherActionLinkText"] = "Register Another Employee?"
                return ok().render("/auth/register-failure-view", req.attributes())
            }
            ?: throw Exception("Missing application mapping for [id=$appId], recheck configuration")

//    fun signupManufacturerView(req: ServerRequest): ServerResponse =
//            serviceMapsRepository.findByIdAndStatus(appId, 1)
//                    ?.let { map ->
//                        req.attributes()["map"] = map
//                        req.attributes().putAll(loadManufacturerUIComponents(map))
//                        req.attributes()["manufacturersEntity"] = ManufacturersEntity()
//                        req.attributes()["manufacturerAddressesEntity"] = ManufacturerAddressesEntity()
//                        req.attributes()["usersEntity"] = UsersEntity()
//                        req.attributes()["contactDetailsEntity"] = ManufacturerContactsEntity()
//                        req.attributes()["yearlyTurnoverEntity"] = ManufacturePaymentDetailsEntity()
//                        return ok().render("/auth/register-manufacturer", req.attributes())
//                    }
//                    ?: throw Exception("Missing application mapping for [id=$appId], recheck configuration")


    fun updateUserView(req: ServerRequest): ServerResponse =
        serviceMapsRepository.findByIdAndStatus(appId, 1)
            ?.let { map ->
                req.paramOrNull("userId")
                    ?.let { userId ->
                        usersRepo.findByIdOrNull(userId.toLong())
                            ?.let { usersEntity ->
                                countriesRepository.findAll()
                                    .let { countries ->
                                        /**
                                         * For Employee Update User Profile
                                         * */
//                                                            userProfilesRepo.findByUserIdAndStatus(usersEntity, map.inactiveStatus)
//                                                                    ?.let {userProfilesEntity ->
                                        req.attributes()["employeesEntity"] = EmployeesEntity()
                                        req.attributes()["userProfilesEntity"] = UserProfilesEntity()
//                                                                        KotlinLogging.logger { }.info { "User DIRECTORATE before update:  = ${userProfilesEntity.directorateId?.directorate}" }
                                        req.attributes().putAll(loadEmployeeUIComponents(map))
//                                                                        req.attributes()["departmentsByDirectorate"] = userProfilesEntity.directorateId?.let { departmentsRepo.findByDirectorateIdAndStatus(it) }
//                                                                    }
                                        /**
                                         * For Importer and Exporter Update profile
                                         * */
                                        req.attributes()["map"] = map
                                        req.attributes()["countries"] = countries
                                        req.attributes().putAll(loadImporterExporterUIComponents(map))
                                        req.attributes()["importerContactDetailsEntity"] =
                                            ImporterContactDetailsEntity()

                                        /**
                                         * Only Load the application map properties Configuration Details
                                         * */
                                        req.attributes().putAll(loadMapPropertiesUIComponents(map))
                                        /**
                                         * Only Load the logged in user Details
                                         * */
                                        req.attributes()["usersEntity"] = usersEntity

                                        /**
                                         * For Manufacturer Update profile
                                         * */
                                        req.attributes().putAll(loadManufacturerUIComponents(map))
                                        req.attributes()["manufacturersEntity"] = ManufacturersEntity()
                                        req.attributes()["stdLevyNotificationFormEntity"] =
                                            StdLevyNotificationFormEntity()
                                        req.attributes()["manufacturerAddressesEntity"] = ManufacturerAddressesEntity()
                                        req.attributes()["manufacturerContactEntity"] = ManufacturerContactsEntity()
                                        req.attributes()["yearlyTurnoverEntity"] = ManufacturePaymentDetailsEntity()
                                        return ok().render("/auth/user-update-profile", req.attributes())
                                    }
                            }
                    }

            }
            ?: throw Exception("Missing application mapping for [id=$appId], recheck configuration")


    fun signUpAllUsersView(req: ServerRequest): ServerResponse {
        req.attributes()["usersEntity"] = UsersEntity()
        return ok().render("/auth/user-registration", req.attributes())
    }


    @PreAuthorize("isAnonymous()")
    fun handleValidateAgainstBrs(req: ServerRequest): ServerResponse {
        return try {
            val body = req.body<BrsConfirmationRequest>()

            val errors: Errors = BeanPropertyBindingResult(body, BrsConfirmationRequest::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    systemsAdminDaoService.validateBrsNumber(body)
                        ?.let { ok().body(it) }
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

    @PreAuthorize("isAnonymous()")
    fun handleSendValidationTokenToCellphoneNumber(req: ServerRequest): ServerResponse {
        return try {
            val body = req.body<ValidatePhoneNumberRegistrationTokenRequestDto>()

            val errors: Errors = BeanPropertyBindingResult(body, ValidatePhoneNumberRegistrationTokenRequestDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    systemsAdminDaoService.sendValidationTokenToCellphoneNumber(body)
                        ?.let { ok().body(it) }
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
    @PreAuthorize("isAnonymous()")
    fun handleValidatePhoneNumberAndToken(req: ServerRequest): ServerResponse {
        return try {
            val body = req.body<ValidatePhoneNumberRegistrationTokenRequestDto>()

            val errors: Errors = BeanPropertyBindingResult(body, ValidatePhoneNumberRegistrationTokenRequestDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    systemsAdminDaoService.validatePhoneNumberAndToken(body, req)
                        ?.let { ok().body(it) }
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

    @PreAuthorize("isAnonymous()")
    fun resetPasswordHandleValidatePhoneNumberAndToken(req: ServerRequest): ServerResponse {
        return try {
            val body = req.body<ValidatePhoneNumberTokenRequestDto>()

            val errors: Errors = BeanPropertyBindingResult(body, ValidatePhoneNumberTokenRequestDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    systemsAdminDaoService.resetPasswordValidatePhoneNumberAndToken(body, req)
                        ?.let { ok().body(it) }
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

//    @PreAuthorize("isAnonymous()")
    fun handleValidatePhoneNumberAndTokenSecure(req: ServerRequest): ServerResponse {
        return try {
            val body = req.body<ValidateTokenRequestDto>()
            println(body)

            val errors: Errors = BeanPropertyBindingResult(body, ValidateTokenRequestDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    val data = ValidatePhoneNumberTokenRequestDto(commonDaoServices.loggedInUserDetailsEmail().cellphone?:"", commonDaoServices.loggedInUserDetailsEmail().email?:"",body.token)
                    systemsAdminDaoService.validatePhoneNumberAndTokenB(data, req)
                        ?.let { ok().body(it) }
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
    @PreAuthorize("isAnonymous()")
    fun handleRegisterCompany(req: ServerRequest): ServerResponse {
        return try {
            val body = req.body<RegistrationPayloadDto>()

            val errors: Errors = BeanPropertyBindingResult(body, RegistrationPayloadDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    systemsAdminDaoService.registerCompany(body)
                        ?.let { ok().body(it) }
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

    @PreAuthorize("isAnonymous()")
    fun handleRegisterTivet(req: ServerRequest): ServerResponse {
        return try {
            val body = req.body<RegistrationTivetPayloadDto>()

            val errors: Errors = BeanPropertyBindingResult(body, RegistrationTivetPayloadDto::class.java.name)
            validator.validate(body, errors)
            when {
                errors.allErrors.isEmpty() -> {
                    systemsAdminDaoService.registerTivet(body)
                        ?.let { ok().body(it) }
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


}
