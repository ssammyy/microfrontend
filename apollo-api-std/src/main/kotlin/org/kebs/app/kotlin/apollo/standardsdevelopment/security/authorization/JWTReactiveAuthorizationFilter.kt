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

package org.kebs.app.kotlin.apollo.standardsdevelopment.security.authorization

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.common.exceptions.InvalidValueException
import org.kebs.app.kotlin.apollo.config.properties.auth.AuthenticationProperties
import org.kebs.app.kotlin.apollo.standardsdevelopment.ports.HttpExceptionFactory.unauthorized
import org.kebs.app.kotlin.apollo.standardsdevelopment.security.jwt.JwtTokenService
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

/**
 * @author Kenneth KZM. Muhia
 * @since 1.0.0
 * Spring Security could be used to give permissions to our clients for use certain endpoints of our API,
 * these permissions could be role based, scope based or both and are called [GrantedAuthority](https://github.com/spring-projects/spring-security/blob/master/core/src/main/java/org/springframework/security/core/GrantedAuthority.java).
 * We give permissions in the authentication process, in the ServerAuthenticationSuccessHandler when we generate the tokens
 * we have to add the roles in form of claims in the JWT (see [JWTServerAuthenticationSuccessHandler](/app/src/src/main/kotlin/org/muhia/app/kotlin/leto/app/security/authentication/JWTServerAuthenticationSuccessHandler.kt)).
 * ReactiveAuthorizationManager (the one who determines if the client has access or not) for each path matcher we configured: one for the excluded paths,
 * one for admin path and another for the rest.
 * Each ReactiveAuthorizationManager is called when an endpoint match, so the `permitAll()` method is a ReactiveAuthorizationManager that always allow access,
 * and the others are our custom ReactiveAuthorizationManagers:
 */
class JWTReactiveAuthorizationFilter(
    private val tokenService: JwtTokenService,
    private val authenticationProperties: AuthenticationProperties
) : WebFilter {

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val authHeader = exchange.request.headers.getFirst(HttpHeaders.AUTHORIZATION) ?: return chain.filter(exchange)

        authenticationProperties.bearerPrefix
            ?.let {
                if (!authHeader.startsWith(it)) {
                    throw unauthorized("Invalid Token provided, ensure to prefix with ${authenticationProperties.bearerPrefix}")
                }
            }
            ?: throw unauthorized("Bearer prefix not set up")

        try {
            tokenService.validatedToken(tokenService.isolateBearerValue(authHeader), exchange.request)
                ?.let { token ->
                    val user = tokenService.getUsernameFromToken(token)
                    val roles = tokenService.extractRolesFromToken(token)
                    val auth = UsernamePasswordAuthenticationToken(user, token, roles)
                    exchange.response.headers.set("Authorization", token)
                    return chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth))

                }
                ?: throw InvalidValueException("Invalid Token")

        } catch (e: Exception) {
            KotlinLogging.logger { }.error("JWT exception: ${e.message}")
            KotlinLogging.logger { }.debug("JWT exception: ${e.message}", e)
        }

        return chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.clearContext())
    }
}
