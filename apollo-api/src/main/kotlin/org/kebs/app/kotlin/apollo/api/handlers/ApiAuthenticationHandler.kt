package org.kebs.app.kotlin.apollo.api.handlers

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.ports.provided.sms.SmsServiceImpl
import org.kebs.app.kotlin.apollo.api.security.jwt.JwtTokenService
import org.kebs.app.kotlin.apollo.api.security.service.CustomAuthenticationProvider
import org.kebs.app.kotlin.apollo.common.dto.JwtResponse
import org.kebs.app.kotlin.apollo.common.dto.LoginRequest
import org.kebs.app.kotlin.apollo.common.dto.OtpRequestValuesDto
import org.kebs.app.kotlin.apollo.common.dto.OtpResponseDto
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.common.utils.generateRandomText
import org.kebs.app.kotlin.apollo.config.properties.auth.AuthenticationProperties
import org.kebs.app.kotlin.apollo.store.model.UserVerificationTokensEntity
import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import org.kebs.app.kotlin.apollo.store.repo.IUserRepository
import org.kebs.app.kotlin.apollo.store.repo.IUserVerificationTokensRepository
import org.springframework.http.server.ServletServerHttpRequest
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.body
import java.sql.Date
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Component
class ApiAuthenticationHandler(
    private val tokenService: JwtTokenService,
    private val authenticationProperties: AuthenticationProperties,
    private val authenticationManager: AuthenticationManager,
    private val commonDaoServices: CommonDaoServices,
    private val usersRepo: IUserRepository,
    private val verificationTokensRepo: IUserVerificationTokensRepository,
    private val smsService: SmsServiceImpl,
    private val customAuthenticationProvider: CustomAuthenticationProvider
) {

    fun generateOtp(req: ServerRequest): ServerResponse {
        val reqBody = req.body<OtpRequestValuesDto>()
        KotlinLogging.logger {  }.info { "Username received: ${reqBody.username}" }
        return reqBody.username?.let {
            usersRepo.findByUserName(it)
                ?.let { user ->
//                    val otp = generateTransactionReference(8).toUpperCase()
                    val otp = commonDaoServices.randomNumber(6)
                    val token = generateVerificationToken(otp, user)
                    KotlinLogging.logger { }.info { "Token: ${token.token}" }

//                    val response = sendOtpViaSMS(token)
                    val response = "Success"

                    req.attributes()["username"] = reqBody.username
                    req.attributes()["password"] = reqBody.password


                    val otpResponseDto = OtpResponseDto()
                    otpResponseDto.message = response
                    otpResponseDto.otp = token.token

                    ServerResponse.ok().body(otpResponseDto)
                }
        }
            ?: throw NullValueNotAllowedException("Incorrect username and/or password provided")
    }


    fun sendOtpViaSMS(token: UserVerificationTokensEntity): String {
        val phone = token.userId?.personalContactNumber
        val message = "Your verification code is ${token.token}"
        var smsSent = false
        if (phone != null) {
            smsSent = smsService.sendSms(phone, message)
        }
        if (smsSent) {
            return "OTP successfully sent"
        }
        return "An error occurred, please try again later"
    }

    fun generateVerificationToken(input: String, user: UsersEntity): UserVerificationTokensEntity {
        val tokensEntity = UserVerificationTokensEntity()
        with(tokensEntity) {
            token = input
            userId = user
            status = 10
            createdBy = user.userName
            createdOn = Timestamp.from(Instant.now())
            tokenExpiryDate = Timestamp.from(Instant.now().plus(10, ChronoUnit.MINUTES))
            transactionDate = Date(java.util.Date().time)
        }


        return verificationTokensRepo.save(tokensEntity)
    }

    fun generateTransactionReference(
        length: Int = 12,
        secureRandomAlgorithm: String = "SHA1PRNG",
        messageDigestAlgorithm: String = "SHA-512", prefix: Boolean = false,
    ): String {
        return generateRandomText(length, secureRandomAlgorithm, messageDigestAlgorithm, false)
    }

    fun uiLogin(req: ServerRequest): ServerResponse =
        try {
            val loginRequest = req.body<LoginRequest>()

            val usernamePassAuth = UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password)

            val auth = customAuthenticationProvider.authenticate(usernamePassAuth)

            SecurityContextHolder.getContext().authentication = auth

            usersRepo.findByUserName(auth.name)
                ?.let { user ->
                    val request = ServletServerHttpRequest(req.servletRequest())
                    val token =
                        tokenService.tokenFromAuthentication(auth, commonDaoServices.concatenateName(user), request)

                    val roles = tokenService.extractRolesFromToken(token)?.map { it.authority }
                    val response = JwtResponse(
                        token,
                        user.id,
                        user.userName,
                        user.email,
                        commonDaoServices.concatenateName(user),
                        roles
                    ).apply {
                        /**
                         * TODO: Set expiry padding configuration  check this time stamp is false
                         */
//                        val localDate = LocalDateTime.now().plusMinutes(authenticationProperties.jwtExpirationMs).minusSeconds(20L)
//                        val timestamp: Timestamp = Timestamp.valueOf(localDate)
//                        expiry = timestamp
                        expiry =
                            LocalDateTime.now().plusMinutes(authenticationProperties.jwtExpirationMs).minusSeconds(20L)
                    }

                    /**
                     *SEND OTP TO USER LOGIN throw phone number
                     */
                    val otp = commonDaoServices.randomNumber(6)
                    val tokenValidation = commonDaoServices.generateVerificationToken(
                        otp,
                        user.cellphone ?: throw NullValueNotAllowedException("Valid Cellphone is required")
                    )
                    commonDaoServices.sendOtpViaSMS(tokenValidation)

                    ServerResponse.ok().body(response)
                }
                ?: throw NullValueNotAllowedException("Empty authentication after authentication attempt")

        } catch (e: Exception) {
            e.printStackTrace()
            KotlinLogging.logger { }.error(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "Unknown Error")
        }

//    fun uiLogin(req: ServerRequest): ServerResponse =
//        try {
//            val loginRequest = req.body<LoginRequest>()
//            KotlinLogging.logger { }.info("username ${loginRequest.username} ${loginRequest.password}")
//            val authentication = authenticationManager.authenticate(UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password))
//            authentication
//                ?.let { auth ->
//                    SecurityContextHolder.getContext().authentication = auth
//
//                    usersRepo.findByUserName(auth.name)
//                        ?.let { user ->
//
//                            val request = ServletServerHttpRequest(req.servletRequest())
//                            val token = tokenService.tokenFromAuthentication(auth, commonDaoServices.concatenateName(user), request)
//
//                            val roles = tokenService.extractRolesFromToken(token)?.map { it.authority }
//
//                            val response = JwtResponse(token, user.id, user.userName, user.email, commonDaoServices.concatenateName(user), roles)
//                            ServerResponse.ok().body(response)
//                        }
//                        ?: throw NullValueNotAllowedException("Empty authentication after authentication attempt")
//                }
//                ?: throw NullValueNotAllowedException("Empty authentication after authentication attempt")
//        } catch (e: Exception) {
//            KotlinLogging.logger { }.error(e.message, e)
//            KotlinLogging.logger { }.debug(e.message, e)
//            ServerResponse.badRequest().body(e.message ?: "Unknown Error")
//        }

}
