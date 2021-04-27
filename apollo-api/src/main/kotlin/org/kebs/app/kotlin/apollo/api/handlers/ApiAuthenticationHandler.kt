package org.kebs.app.kotlin.apollo.api.handlers

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.api.security.jwt.JwtTokenService
import org.kebs.app.kotlin.apollo.common.dto.JwtResponse
import org.kebs.app.kotlin.apollo.common.dto.LoginRequest
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.common.utils.generateRandomText
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
import java.time.temporal.ChronoUnit

@Component
class ApiAuthenticationHandler(
    private val tokenService: JwtTokenService,
    private val authenticationManager: AuthenticationManager,
    private val commonDaoServices: CommonDaoServices,
    private val usersRepo: IUserRepository,
    private val verificationTokensRepo: IUserVerificationTokensRepository
) {

    fun generateOtp(req: ServerRequest): ServerResponse {
        val username = req.body<String>()
        return usersRepo.findByUserName(username)
            ?.let { user ->
                val otp = generateTransactionReference(8).toUpperCase()
                val response = sendOtpViaSMS(generateVerificationToken(otp, user))
                ServerResponse.ok().body(response)


            }
            ?: throw NullValueNotAllowedException("Incorrect username and/or password provided")

    }

    fun sendOtpViaSMS(token: UserVerificationTokensEntity): String {
        val phone = token.userId?.cellphone
        return "Coming soon $phone"
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
            val authentication = authenticationManager.authenticate(UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password))
            authentication
                ?.let { auth ->
                    SecurityContextHolder.getContext().authentication = auth

                    usersRepo.findByUserName(auth.name)
                        ?.let { user ->

                            val request = ServletServerHttpRequest(req.servletRequest())
                            val token = tokenService.tokenFromAuthentication(auth, commonDaoServices.concatenateName(user), request)

                            val roles = tokenService.extractRolesFromToken(token)?.map { it.authority }

                            val response = JwtResponse(token, user.id, user.userName, user.email, commonDaoServices.concatenateName(user), roles)
                            ServerResponse.ok().body(response)
                        }
                        ?: throw NullValueNotAllowedException("Empty authentication after authentication attempt")
                }
                ?: throw NullValueNotAllowedException("Empty authentication after authentication attempt")
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
            KotlinLogging.logger { }.debug(e.message, e)
            ServerResponse.badRequest().body(e.message ?: "Unknown Error")
        }
}
