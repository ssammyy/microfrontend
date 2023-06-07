/*
 *
 *  $$$$$$$\   $$$$$$\  $$\   $$\        $$$$$$\  $$\       $$$$$$\  $$$$$$$\   $$$$$$\  $$\
 *  $$  __$$\ $$  __$$\ $$ | $$  |      $$  __$$\ $$ |     $$  __$$\ $$  __$$\ $$  __$$\ $$ |
 *  $$ |  $$ |$$ /  \__|$$ |$$  /       $$ /  \__|$$ |     $$ /  $$ |$$ |  $$ |$$ /  $$ |$$ |
 *  $$$$$$$\ |\$$$$\  $$$$$  /        $$ |$$$$\ $$ |     $$ |  $$ |$$$$$$$\ |$$$$$$$$ |$$ |
 *  $$  __$$\  \____$$\ $$  $$<         $$ |\_$$ |$$ |     $$ |  $$ |$$  __$$\ $$  __$$ |$$ |
 *  $$ |  $$ |$$\   $$ |$$ |\$\        $$ |  $$ |$$ |     $$ |  $$ |$$ |  $$ |$$ |  $$ |$$ |
 *  $$$$$$$  |\$$$$  |$$ | \$\       \$$$$  |$$$$$$$$\ $$$$$$  |$$$$$$$  |$$ |  $$ |$$$$$$$$\
 *  \_______/  \______/ \__|  \__|       \______/ \________|\______/ \_______/ \__|  \__|\________|
 *  $$$$$$$$\ $$$$$$$$\  $$$$$$\  $$\   $$\ $$\   $$\  $$$$$$\  $$\       $$$$$$\   $$$$$$\  $$$$$$\ $$$$$$$$\  $$$$$$\
 *  \__$$  __|$$  _____|$$  __$$\ $$ |  $$ |$$$\  $$ |$$  __$$\ $$ |     $$  __$$\ $$  __$$\ \_$$  _|$$  _____|$$  __$$\
 *     $$ |   $$ |      $$ /  \__|$$ |  $$ |$$$$\ $$ |$$ /  $$ |$$ |     $$ /  $$ |$$ /  \__|  $$ |  $$ |      $$ /  \__|
 *     $$ |   $$$$$\    $$ |      $$$$$$$$ |$$ $$\$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |$$$$\   $$ |  $$$$$\    \$$$$\
 *     $$ |   $$  __|   $$ |      $$  __$$ |$$ \$$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |\_$$ |  $$ |  $$  __|    \____$$\
 *     $$ |   $$ |      $$ |  $$\ $$ |  $$ |$$ |\$ |$$ |  $$ |$$ |     $$ |  $$ |$$ |  $$ |  $$ |  $$ |      $$\   $$ |
 *     $$ |   $$$$$$$$\ \$$$$  |$$ |  $$ |$$ | \$ | $$$$$$  |$$$$$$$$\ $$$$$$  |\$$$$  |$$$$$$\ $$$$$$$$\ \$$$$  |
 *     \__|   \________| \______/ \__|  \__|\__|  \__| \______/ \________|\______/  \______/ \______|\________| \______/
 *
 *
 *
 *
 *
 *    Copyright (c) 2020.  BSK
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 */

package org.kebs.app.kotlin.apollo.api.flux.security.jwt.authorization

import kotlinx.coroutines.reactor.mono
import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.flux.security.jwt.JwtTokenService
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.authorization.AuthorizationDecision
import org.springframework.security.authorization.ReactiveAuthorizationManager
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.server.authorization.AuthorizationContext
import reactor.core.publisher.Mono

interface IJWTReactiveAuthorizationManager : ReactiveAuthorizationManager<AuthorizationContext> {
    override fun check(authentication: Mono<Authentication>?, context: AuthorizationContext?): Mono<AuthorizationDecision> = mono {
        val notAuthorized = AuthorizationDecision(false)
        val exchange = context?.exchange ?: return@mono notAuthorized
        val authHeader = exchange.request.headers.getFirst(HttpHeaders.AUTHORIZATION) ?: return@mono notAuthorized

        KotlinLogging.logger { }.debug("Before try-catch")

        try {


            if (!authHeader.startsWith("Bearer ")) {
                KotlinLogging.logger { }.info("No Header")
                return@mono notAuthorized
            }

            val token = getJwtService().isolateBearerValue(authHeader)
//        authentication?.let { mono ->
//            mono.awaitFirstOrNull()?.let { auth ->
//
//                KotlinLogging.logger { }.info("Authenticated as ${auth.name}")
//
//            } ?: return@mono notAuthorized
//        }
            SecurityContextHolder.getContext()?.authentication
                ?.let { KotlinLogging.logger { }.debug("Authenticated as ${it.name}") }
                ?: run {
                    KotlinLogging.logger { }.info("Not Authenticated retying")
                    val user = getJwtService().getUsernameFromToken(token)
                    val roles = getJwtService().extractRolesFromToken(token)

                    val auth: Authentication = UsernamePasswordAuthenticationToken(user, null, roles)
                    SecurityContextHolder.getContext()?.authentication = auth
                    when {
                        auth.isAuthenticated -> {
                            KotlinLogging.logger { }.debug("Authenticated as ${auth.name}")
                        }
                        else -> {
                            KotlinLogging.logger { }.debug("Not Authenticated after retying")
                            return@mono notAuthorized
                        }
                    }
                }


            /**
             * TODO Figure out how to valudate the agent
            token
            ?.let {
            val ip = getJwtService().extractIpFromToken(it)
            val agent = getJwtService().extractUserAgentFromToken(it)
            agent
            ?.let { userAgent ->
            if (!userAgent.equals(exchange.request.headers["User-Agent"])) {
            KotlinLogging.logger { }.info("Agent Mismatch $userAgent vs ${exchange.request.headers["User-Agent"]}")
            return@mono notAuthorized
            }
            }
            ?: return@mono notAuthorized

            ip
            ?.let { ipAddress ->
            if (ipAddress != exchange.request.remoteAddress?.address?.hostAddress) {
            KotlinLogging.logger { }.debug("Ip Mismatch")
            return@mono notAuthorized
            }
            }
            ?: return@mono notAuthorized


            }
            ?: return@mono notAuthorized
            KotlinLogging.logger { }.debug("All clear")
             **/


            KotlinLogging.logger { }.debug("Before Auth")
            return@mono doAuthorization(getJwtService().validatedToken(token))
        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.debug(e.message, e)
            return@mono notAuthorized
        }
    }

    suspend fun getJwtService(): JwtTokenService
    suspend fun doAuthorization(token: String?): AuthorizationDecision
}