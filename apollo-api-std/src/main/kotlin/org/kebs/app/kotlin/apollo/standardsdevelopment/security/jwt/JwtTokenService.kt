/*
 * $$$$$$$\   $$$$$$\  $$\   $$\        $$$$$$\  $$\       $$$$$$\  $$$$$$$\   $$$$$$\  $$\
 * $$  __$$\ $$  __$$\ $$ | $$  |      $$  __$$\ $$ |     $$  __$$\ $$  __$$\ $$  __$$\ $$ |
 * $$ |  $$ |$$ /  \__|$$ |$$  /       $$ /  \__|$$ |     $$ /  $$ |$$ |  $$ |$$ /  $$ |$$ |
 * $$$$$$$\ |\$$$\  $$$$$  /        $$ |$$$$\ $$ |     $$ |  $$ |$$$$$$$\ |$$$$$$$$ |$$ |
 * $$  __$$\  \____$$\ $$  $$<         $$ |\_$$ |$$ |     $$ |  $$ |$$  __$$\ $$  __$$ |$$ |
 * $$ |  $$ |$$\   $$ |$$ |\$\        $$ |  $$ |$$ |     $$ |  $$ |$$ |  $$ |$$ |  $$ |$$ |
 * $$$$$$$  |\$$$  |$$ | \$\       \$$$  |$$$$$$$$\ $$$$$$  |$$$$$$$  |$$ |  $$ |$$$$$$$$\
 * \_______/  \______/ \__|  \__|       \______/ \________|\______/ \_______/ \__|  \__|\________|
 * $$$$$$$$\ $$$$$$$$\  $$$$$$\  $$\   $$\ $$\   $$\  $$$$$$\  $$\       $$$$$$\   $$$$$$\  $$$$$$\ $$$$$$$$\  $$$$$$\
 * \__$$  __|$$  _____|$$  __$$\ $$ |  $$ |$$$\  $$ |$$  __$$\ $$ |     $$  __$$\ $$  __$$\ \_$$  _|$$  _____|$$  __$$\
 *    $$ |   $$ |      $$ /  \__|$$ |  $$ |$$$$\ $$ |$$ /  $$ |$$ |     $$ /  $$ |$$ /  \__|  $$ |  $$ |      $$ /  \__|
 *    $$ |   $$$$$\    $$ |      $$$$$$$$ |$$ $$\$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |$$$$\   $$ |  $$$$$\    \$$$\
 *    $$ |   $$  __|   $$ |      $$  __$$ |$$ \$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |\_$$ |  $$ |  $$  __|    \____$$\
 *    $$ |   $$ |      $$ |  $$\ $$ |  $$ |$$ |\$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |  $$ |  $$ |  $$ |      $$\   $$ |
 *    $$ |   $$$$$$$$\ \$$$  |$$ |  $$ |$$ | \$ | $$$$$$  |$$$$$$$$\ $$$$$$  |\$$$  |$$$$$$\ $$$$$$$$\ \$$$  |
 *    \__|   \________| \______/ \__|  \__|\__|  \__| \______/ \________|\______/  \______/ \______|\________| \______/
 *
 *
 *
 *
 *
 *   Copyright (c) 2020.  BSK
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

package org.kebs.app.kotlin.apollo.standardsdevelopment.security.jwt

import io.jsonwebtoken.*
import kotlinx.coroutines.reactor.mono
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.common.dto.CustomResponse
import org.kebs.app.kotlin.apollo.common.exceptions.InvalidValueException
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.kebs.app.kotlin.apollo.config.properties.auth.AuthenticationProperties
import org.kebs.app.kotlin.apollo.store.model.JwtTokensRegistry
import org.kebs.app.kotlin.apollo.store.repo.IJwtTokensRegisterRepository
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.sql.Timestamp
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.stream.Collectors

@Service
class JwtTokenService(
    private val authenticationProperties: AuthenticationProperties,
    private val tokenRegistryRepo: IJwtTokensRegisterRepository
) {
    /**
     *
     * To authorize the rest, we are checking the validity of the JWT that comes in every request. The validity for us is:
     * 1. If the token is expired.
     * 2. If token was given by us (is signed with our signature).
     * 3. Was it generated by the same host and client?
     * 4. Is it disabled on the database
     * @param authToken The token received via oAuth
     * @return either the validate token after the steps above or an empty string if it fails
     */
    fun validatedToken(authToken: String?, request: ServerHttpRequest? = null): String? {
        var tokenEntity: JwtTokensRegistry? = null
        var result: String? = null
        val ip = request?.remoteAddress?.address?.hostAddress
        val forwardedIp = request?.headers?.get("X-FORWARDED-FOR")
        val agent = request?.headers?.get("User-Agent").toString()
        runBlocking {
            try {
                Jwts.parser().setSigningKey(authenticationProperties.jwtSecret).parseClaimsJws(authToken)
                authToken
                    ?.let { t ->
                        tokenEntity = tokenRegistryRepo.findByRawToken(t)
                    }
                    ?: throw NullValueNotAllowedException("Token cannot be empty")
                if (tokenEntity?.status == 1) result = authToken else throw InvalidValueException("Token is not valid:Datastore check")
                if (!tokenEntity?.forwardedIpAddress.equals(forwardedIp.toString(), ignoreCase = true) && !ip.equals(tokenEntity?.ipAddress) && agent != tokenEntity?.userAgent) {
                    throw InvalidValueException("Token is not valid:Datastore check, possibly breached")
                }
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
        }


        return result
    }

    /**
     *
     * When provided with the authorization header remove any prefixes as per the config file and return the token
     * 1. Does the authorization header begin with the prefix set up
     * 2. is the token blank or null.
     * @param authHeader The raw Authorization Header values
     * @return either the validate token after the steps above or an empty string if it fails
     */
    fun isolateBearerValue(authHeader: String?): String? {
        return if (authHeader != null && authHeader.startsWith(authenticationProperties.bearerPrefix.toString())) {
            authHeader.replace(authenticationProperties.bearerPrefix.toString(), "")
        } else {
            KotlinLogging.logger { }.warn("couldn't find bearer string, will ignore the header.")
            null
        }

    }

    /**
     *
     * Extract the username from the valid token, by checking that it is as we signed it
     * 1. Does the authorization header begin with the prefix set up
     * 2. is the token blank or null.
     * @param token The token
     * @return either the username used to generate the token or an empty string if any issue is encountered
     */
    fun getUsernameFromToken(token: String?): String? {
//        return jasyptStringEncryptor.decrypt(Jwts.parser().setSigningKey(authenticationProperties.jwtSecret).parseClaimsJws(token).body.subject)
        return Jwts.parser().setSigningKey(authenticationProperties.jwtSecret).parseClaimsJws(token).body.subject
    }


    /**
     *
     * Extract the user's roles from the valid token, by checking that it is as we signed it
     * 1. Does the authorization header begin with the prefix set up
     * 2. is the token blank or null.
     * @param token The token
     * @return either the list of valid GrantedAuthority or or an empty list if any issue is encountered
     */
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


    /**
     * Extract whatever clains that are available on the token, validate token by checking that it is as we signed it
     *
     * @param token The token
     * @return either the all the claims  or an empty list if any issue is encountered
     */
    fun getAllClaimsFromToken(token: String?): Jws<Claims>? {
        return try {
            Jwts.parser().setSigningKey(authenticationProperties.jwtSecret).parseClaimsJws(token)
        } catch (e: Exception) {
            KotlinLogging.logger { }.debug(e.message, e)
            KotlinLogging.logger { }.error(e.message)
            null

        }


    }


    /**
     * When we get a request to provide a token we check that the user does not already have a valid token, if it exists on the DB provide that
     * after validating that it was generated by the same host and agent
     *
     * @param subject The username to be used to generate the token
     * @param credentials Placeholder for the credentials but normally we just put the token here
     * @param authorities User's authorizations
     * @return either the token  or an empty string if any issue is encountered
     */

    suspend fun generateToken(subject: String, credentials: Any?, authorities: List<String>, request: ServerHttpRequest?): Pair<String, String> {
        val tokenStarted = Instant.now().atZone(ZoneId.systemDefault())
        val tokenExpired = tokenStarted.plus(authenticationProperties.jwtExpirationMs, ChronoUnit.MINUTES)
        val refreshTokenExpired = tokenStarted.plus(authenticationProperties.refreshTokenExpirationMs, ChronoUnit.MINUTES)
        val ip = request?.remoteAddress?.address?.hostAddress
        val forwardedIp = request?.headers?.get("X-FORWARDED-FOR")
        val agent = request?.headers?.get("User-Agent").toString()
        KotlinLogging.logger { }.debug("Token issued at $tokenStarted will expire at $tokenExpired")
        var result: Pair<String, String> = Pair("", "")


        tokenRegistryRepo.findFirstByUserNameAndStatusAndTokenEndIsAfter(subject, 1, Timestamp.from(Instant.now()))
            ?.let { tokenEntity ->
                KotlinLogging.logger { }.debug("Token on DB at ${tokenEntity.userName} will expire at ${tokenEntity.tokenEnd}")
                if (tokenEntity.forwardedIpAddress.equals(forwardedIp.toString(), ignoreCase = true) && ip.equals(tokenEntity.ipAddress)) {
                    tokenEntity.rawToken
                        ?.let {
                            result = Pair(it, tokenEntity.refreshToken ?: throw InvalidValueException("Refresh Token not set"))
                            KotlinLogging.logger { }.debug("Token is  $it or  $result")
                        }
                        ?: throw NullValueNotAllowedException("Oops empty token")


                } else {
                    KotlinLogging.logger { }.debug("New Token required as we have different ip info[$forwardedIp vs ${tokenEntity.forwardedIpAddress} or  ${tokenEntity.ipAddress} vs $ip")
                    result = generateAsTokenIsInvalid(subject, tokenStarted, tokenExpired, ip, agent, forwardedIp, authorities, refreshTokenExpired)

                }
            }
            ?: run {
                result = generateAsTokenIsInvalid(subject, tokenStarted, tokenExpired, ip, agent, forwardedIp, authorities, refreshTokenExpired)


            }


        return result


    }

    /**
     * Internale function that generates and stores the token and saves it to the database
     *
     * @param subject The username that owns the token
     * @param tokenStarted The timestamp when the token was generated
     * @param tokenExpired The timestamp when the token will expire
     * @param ip The Ip address used to request for the token
     * @param agent The user agent  used to request for the token
     * @param forwardedIp In case the user is behind a proxy what is their internal IP
     * @param authorities A list of authorizations provided to the user
     *
     * @return either the all the claims  or an empty list if any issue is encountered
     */
    private suspend fun generateAsTokenIsInvalid(
        subject: String,
        tokenStarted: ZonedDateTime,
        tokenExpired: ZonedDateTime,
        ip: String?,
        agent: String,
        forwardedIp: List<String>?,
        authorities: List<String>,
        refreshTokenExpired: ZonedDateTime,
    ): Pair<String, String> {
        var generatedToken = ""
        var generatedRefreshToken = ""
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
//                .claim(authenticationProperties.jwtAgentKey, agent)
//                .claim(authenticationProperties.jwtIpInternalKey, forwardedIp)
                .claim(authenticationProperties.jwtAuthoritiesKey, authorities.map { it }.stream().collect(Collectors.joining(",")))
                .compact()
            generatedRefreshToken = Jwts.builder()
                //                .setSubject(jasyptStringEncryptor.encrypt(subject))

                .setSubject(subject)
                //                .setIssuer(jasyptStringEncryptor.encrypt(authenticationProperties.jwtIssuer))
                .setIssuer(authenticationProperties.jwtIssuer)
                .signWith(SignatureAlgorithm.HS512, authenticationProperties.jwtSecret)
                .setIssuedAt(Date.from(tokenStarted.toInstant()))
                .setExpiration(Date.from(refreshTokenExpired.toInstant()))
                .claim(authenticationProperties.jwtIpKey, ip)
//                .claim(authenticationProperties.jwtAgentKey, agent)
//                .claim(authenticationProperties.jwtIpInternalKey, forwardedIp)
                .claim(authenticationProperties.jwtAuthoritiesKey, authorities.map { it }.stream().collect(Collectors.joining(",")))
                .compact()

            KotlinLogging.logger { }.debug("during  token generation $generatedToken")
            val jwtTokensRegistry = JwtTokensRegistry().apply {
                userName = subject
                ipAddress = ip
                userAgent = agent
                forwardedIpAddress = forwardedIp.toString()
                createdBy = ip
                createdOn = Timestamp.from(Instant.now())
                tokenStart = Timestamp.from(tokenStarted.toInstant())
                tokenEnd = Timestamp.from(tokenExpired.toInstant())
                refreshTokenExpiry = Timestamp.from(refreshTokenExpired.toInstant())
                refreshToken = generatedRefreshToken

                rawToken = generatedToken
                status = 1
                refreshTokenStatus = 1
            }

            tokenRegistryRepo.save(jwtTokensRegistry)
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
        }
        return Pair(generatedToken, generatedRefreshToken)
    }

    /**
     * Merge token and prefix when provided with a valid authentication
     *
     * @param authentication The validated authentication
     * @param request The ServerHttpRequest that sent the request useful when we are checking for header values
     *
     * @return either the entire string that will be on the header  or an empty list if any issue is encountered
     */
    fun getHttpAuthHeaderValue(authentication: Authentication, request: ServerHttpRequest?): Pair<String, String> {
        val pair = tokenFromAuthentication(authentication, request)
        return Pair("${authenticationProperties.bearerPrefix}${pair.first}", pair.second)

    }

    /**
     * Internal function used to provide a token it relies on the generateToken function to do the actual ...
     *
     * @param authentication The validated authentication
     * @param request The ServerHttpRequest that sent the request useful when we are checking for header values
     *
     * @return either the all the token  or an empty list if any issue is encountered
     */
    fun tokenFromAuthentication(authentication: Authentication, request: ServerHttpRequest?): Pair<String, String> = runBlocking {
        val roles = authentication.authorities.map { it.authority }
        generateToken(authentication.name, authentication.credentials, roles, request)
    }

    /**
     * Invalidate the token in response to a user logout request
     * @param headerValue the authorization header
     */
    fun destroyTokenOnLogout(headerValue: String?): Mono<CustomResponse> = mono {
        headerValue
            ?.let { header ->
                val token = header.replace(authenticationProperties.bearerPrefix.toString(), "")
                KotlinLogging.logger { }.trace(token)
                tokenRegistryRepo.findByRawToken(token)
                    ?.let {

                        it.modifiedOn = Timestamp.from(Instant.now())
                        it.modifiedBy = "Logout Process"
                        it.status = 2

                        tokenRegistryRepo.save(it)
                        return@mono CustomResponse().apply {
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
