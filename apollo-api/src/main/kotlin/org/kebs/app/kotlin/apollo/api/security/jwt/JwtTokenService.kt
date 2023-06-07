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

package org.kebs.app.kotlin.apollo.api.security.jwt

import io.jsonwebtoken.*
import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.common.dto.CustomResponse
import org.kebs.app.kotlin.apollo.common.exceptions.InvalidValueException
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.config.properties.auth.AuthenticationProperties
import org.kebs.app.kotlin.apollo.store.model.JwtTokensRegistry
import org.kebs.app.kotlin.apollo.store.repo.IJwtTokensRegisterRepository
import org.springframework.http.server.ServerHttpRequest
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component
import org.springframework.web.servlet.function.ServerRequest
import java.sql.Timestamp
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.stream.Collectors


@Component
class JwtTokenService(
        private val authenticationProperties: AuthenticationProperties,
        private val tokenRegistryRepo: IJwtTokensRegisterRepository
) {

    fun validatedToken(authToken: String?): String? {
        var tokenEntity: JwtTokensRegistry? = null
        var result: String? = null
//        runBlocking {
        try {
            Jwts.parser().setSigningKey(authenticationProperties.jwtSecret).parseClaimsJws(authToken)
            authToken
                    ?.let { t ->
                        tokenEntity = tokenRegistryRepo.findByRawToken(t)
                    }
                    ?: throw NullValueNotAllowedException("Token cannot be empty")
            if (tokenEntity?.status == 1) result = authToken else throw InvalidValueException("Token is not valid:Datastore check")
        } catch (e: SignatureException) {
            KotlinLogging.logger { }.warn("Invalid JWT signature: {${e.message}}")
            KotlinLogging.logger { }.trace("Invalid JWT signature: {${e.message}}", e)
            tokenEntity?.status = 25
            tokenEntity?.modifiedBy = e.message
            tokenEntity?.modifiedOn = Timestamp.from(Instant.now())


        } catch (e: MalformedJwtException) {

            KotlinLogging.logger { }.warn("Invalid JWT token: {${e.message}}")
            KotlinLogging.logger { }.trace("Invalid JWT token: {${e.message}}", e)
            tokenEntity?.status = 25
            tokenEntity?.modifiedBy = e.message
            tokenEntity?.modifiedOn = Timestamp.from(Instant.now())

        } catch (e: ExpiredJwtException) {
            KotlinLogging.logger { }.warn("JWT token is expired: {${e.message}}")
            KotlinLogging.logger { }.trace("JWT token is expired: {${e.message}}", e)
            tokenEntity?.status = 25
            tokenEntity?.modifiedBy = e.message
            tokenEntity?.modifiedOn = Timestamp.from(Instant.now())


        } catch (e: UnsupportedJwtException) {

            KotlinLogging.logger { }.warn("JWT token is unsupported: {${e.message}}")
            KotlinLogging.logger { }.trace("JWT token is unsupported: {${e.message}}", e)
            tokenEntity?.status = 25
            tokenEntity?.modifiedBy = e.message
            tokenEntity?.modifiedOn = Timestamp.from(Instant.now())

        } catch (e: IllegalArgumentException) {

            KotlinLogging.logger { }.warn("JWT claims string is empty: {${e.message}}")
            KotlinLogging.logger { }.trace("JWT claims string is empty: {${e.message}}", e)
            tokenEntity?.status = 25
            tokenEntity?.modifiedBy = e.message
            tokenEntity?.modifiedOn = Timestamp.from(Instant.now())

        } catch (e: Exception) {

            KotlinLogging.logger { }.warn("Unknown error: {${e.message}}")
            KotlinLogging.logger { }.trace("JWT claims string is empty: {${e.message}}", e)
            tokenEntity?.status = 25
            tokenEntity?.modifiedBy = e.message
            tokenEntity?.modifiedOn = Timestamp.from(Instant.now())

        }
        tokenEntity?.let { tokenRegistryRepo.save(it) }
//        }


        return result
    }

    fun isolateBearerValue(authHeader: String?): String? {
        return if (authHeader != null && authHeader.startsWith(authenticationProperties.bearerPrefix.toString())) {
            authHeader.replace(authenticationProperties.bearerPrefix.toString(), "")
        } else {
            KotlinLogging.logger { }.warn("couldn't find bearer string, will ignore the header.")
            null
        }

    }


    fun getUsernameFromToken(token: String?): String? {
//        return jasyptStringEncryptor.decrypt(Jwts.parser().setSigningKey(authenticationProperties.jwtSecret).parseClaimsJws(token).body.subject)
        return Jwts.parser().setSigningKey(authenticationProperties.jwtSecret).parseClaimsJws(token).body.subject
    }

    fun extractIpFromToken(token: String) = getAllClaimsFromToken(token)?.body?.get(authenticationProperties.jwtIpKey, String::class.java)
    fun extractUserAgentFromToken(token: String) = getAllClaimsFromToken(token)?.body?.get(authenticationProperties.jwtAgentKey, String::class.java)

    fun extractRolesFromToken(token: String?): MutableList<GrantedAuthority>? {

        val claims = getAllClaimsFromToken(token)
        val roles = claims?.body?.get(authenticationProperties.jwtAuthoritiesKey, String::class.java)
        val authorities = mutableListOf<GrantedAuthority>()
        roles?.let {
            it.split(",").forEach { auth ->
//                authorities.add(SimpleGrantedAuthority(jasyptStringEncryptor.decrypt(auth)))
                authorities.add(SimpleGrantedAuthority(auth))
            }
        }
        return authorities

    }

//    fun getExpirationDateFromToken(token: String?): Date {
//        return Jwts.parser().setSigningKey(authenticationProperties.jwtSecret).parseClaimsJws(token).body.expiration
//    }

    fun getAllClaimsFromToken(token: String?): Jws<Claims>? {
        return try {
            Jwts.parser().setSigningKey(authenticationProperties.jwtSecret).parseClaimsJws(token)
        } catch (e: Exception) {
            KotlinLogging.logger { }.debug(e.message, e)
            KotlinLogging.logger { }.error(e.message)
            null

        }


    }


    fun generateToken(subject: String, credentials: Any?,
                      fullName: String,
                      authorities: Collection<GrantedAuthority>,
                      request: ServerHttpRequest?): String {
        val tokenStarted = Instant.now().atZone(ZoneId.systemDefault())
        val tokenExpired = tokenStarted.plus(authenticationProperties.jwtExpirationMs, ChronoUnit.MINUTES)
        val ip = request?.remoteAddress?.address?.hostAddress
        val forwardedIp = request?.headers?.get("X-FORWARDED-FOR")
        val agent = request?.headers?.get("User-Agent").toString()
        KotlinLogging.logger { }.debug("Token issued at $tokenStarted will expire at $tokenExpired")
        var result = ""


        tokenRegistryRepo.findFirstByUserNameAndStatusAndTokenEndIsAfter(subject, 1, Timestamp.from(Instant.now()))
                ?.let { tokenEntity ->
                    KotlinLogging.logger { }.debug("Token on DB at ${tokenEntity.userName} will expire at ${tokenEntity.tokenEnd}")
                    if (tokenEntity.forwardedIpAddress.equals(forwardedIp.toString(), ignoreCase = true) && ip.equals(tokenEntity.ipAddress)) {
                        tokenEntity.rawToken?.let {
                            result = it
                            KotlinLogging.logger { }.debug("Token is  $it or  $result")
                        }
                                ?: throw NullValueNotAllowedException("Oops empty token")


                    } else {
                        KotlinLogging.logger { }.debug("New Token required as we have different ip info[$forwardedIp vs ${tokenEntity.forwardedIpAddress} or  ${tokenEntity.ipAddress} vs $ip")
                        result = generateAsTokenIsInvalid(subject, tokenStarted, tokenExpired, ip, agent, fullName, forwardedIp, authorities)

                    }
                }
                ?: run {
                    result = generateAsTokenIsInvalid(subject, tokenStarted, tokenExpired, ip, agent, fullName, forwardedIp, authorities)


                }


        return result


    }

    fun generateClientToken(auth: Authentication, request: ServerRequest): String {
        val tokenStarted = Instant.now().atZone(ZoneId.systemDefault())
        val tokenExpired = tokenStarted.plusDays(1)
        val ip = request.remoteAddress().get().address.hostAddress
        val forwardedIp = request.headers().firstHeader("X-FORWARDED-FOR")
        val agent = request.headers().firstHeader("User-Agent")
        KotlinLogging.logger { }.debug("Token issued at $tokenStarted will expire at $tokenExpired")
        val user=auth.principal as User
        return generateAsTokenIsInvalid(user.username as String, tokenStarted, tokenExpired, ip, agent?:"Unknown", "TOKEN", listOf(forwardedIp?:"0.0.0.0"), user.authorities)
    }

    private fun generateAsTokenIsInvalid(
            subject: String,
            tokenStarted: ZonedDateTime,
            tokenExpired: ZonedDateTime,
            ip: String?,
            agent: String,
            fullName: String,
            forwardedIp: List<String>?,
            authorities: Collection<GrantedAuthority>
    ): String {
        var generatedToken = ""
        try {
            generatedToken = Jwts.builder()
                    //                .setSubject(jasyptStringEncryptor.encrypt(subject))

                    .setSubject(subject)
                    //                .setIssuer(jasyptStringEncryptor.encrypt(authenticationProperties.jwtIssuer))
                    .setIssuer(authenticationProperties.jwtIssuer)
                    .signWith(SignatureAlgorithm.HS512, authenticationProperties.jwtSecret)
                    .setIssuedAt(Date.from(tokenStarted.toInstant()))
                    .setExpiration(Date.from(tokenExpired.toInstant()))
                    .claim(authenticationProperties.jwtIpKey, ip)
                    .claim(authenticationProperties.jwtAgentKey, agent)
                    .claim(authenticationProperties.jwtIpInternalKey, forwardedIp)
                    .claim(authenticationProperties.jwtFullNameKey, fullName)
                    .claim(authenticationProperties.jwtIpInternalKey, forwardedIp)
                    .claim(authenticationProperties.jwtAuthoritiesKey, authorities
                            .stream()
                            .map { obj: Any? -> GrantedAuthority::class.java.cast(obj) }
                            //                        .map { obj: GrantedAuthority -> jasyptStringEncryptor.encrypt(obj.authority) }
                            .map { obj: GrantedAuthority -> obj.authority }
                            .collect(Collectors.joining(",")))
                    .compact()

            KotlinLogging.logger { }.debug("during  token generation $generatedToken")
            val jwtTokensRegistry = JwtTokensRegistry()
            with(jwtTokensRegistry) {
                userName = subject
                ipAddress = ip
                userAgent = agent
                forwardedIpAddress = forwardedIp.toString()
                createdBy = ip
                createdOn = Timestamp.from(Instant.now())
                tokenStart = Timestamp.from(tokenStarted.toInstant())
                tokenEnd = Timestamp.from(tokenExpired.toInstant())
                transactionDate = java.sql.Date(Date().time)
                rawToken = generatedToken
                status = 1
            }

            tokenRegistryRepo.save(jwtTokensRegistry)
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
        }
        return generatedToken
    }

//
//    fun getHttpAuthHeaderValue(authentication: Authentication, request: ServerHttpRequest?): String? {
//        return java.lang.String.join(authenticationProperties.bearerDelimiter, authenticationProperties.bearerPrefix, tokenFromAuthentication(authentication, request))
//    }

    fun tokenFromAuthentication(
        authentication: Authentication,
        fullName: String,
        request: ServerHttpRequest?
    ): String? =
//        runBlocking {
        generateToken(
            authentication.name, authentication.credentials,
            fullName, authentication.authorities, request
        )
//    }

    fun destroyTokenOnLogout(header: String?): CustomResponse? {
        header
            ?.let { h ->
                val token = h.replace(authenticationProperties.bearerPrefix.toString(), "")
                KotlinLogging.logger { }.trace(token)
                tokenRegistryRepo.findByRawToken(token)
                    ?.let {
                        it.modifiedOn = Timestamp.from(Instant.now())
                        it.modifiedBy = "Logout Process"
                        it.status = 2

                        tokenRegistryRepo.save(it)
                        return CustomResponse().apply {
                            payload = "Success"
                            status = 200
                            this.response = "00"
                        }
                    }
                    ?: throw NullValueNotAllowedException("Token not found")
            }
            ?: throw NullValueNotAllowedException("Empty token not allowed")

    }
}
