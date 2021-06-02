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

package org.kebs.app.kotlin.apollo.standardsdevelopment.security.authentication

import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactor.mono
import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.common.dto.JwtResponse
import org.kebs.app.kotlin.apollo.config.properties.auth.AuthenticationProperties
import org.kebs.app.kotlin.apollo.standardsdevelopment.ports.HttpExceptionFactory
import org.kebs.app.kotlin.apollo.standardsdevelopment.security.jwt.JwtTokenService
import org.kebs.app.kotlin.apollo.store.repo.IUserRepository
import org.springframework.core.io.buffer.DefaultDataBufferFactory
import org.springframework.security.core.Authentication
import org.springframework.security.web.server.WebFilterExchange
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Component
class JWTServerAuthenticationSuccessHandler(
    private val tokeService: JwtTokenService,
    private val usersRepo: IUserRepository,
    private val authenticationProperties: AuthenticationProperties
) : ServerAuthenticationSuccessHandler {
    override fun onAuthenticationSuccess(webFilterExchange: WebFilterExchange?, authentication: Authentication?): Mono<Void> = mono {
        KotlinLogging.logger { }.debug("before token")

        val token = authentication
            ?.let {
                val t = tokeService.getHttpAuthHeaderValue(it, webFilterExchange?.exchange?.request)
//                val expiry = tokeService.tokenExpiryDate(t)
                val expires = LocalDateTime.now().plus(authenticationProperties.jwtExpirationMs, ChronoUnit.MINUTES).minus(authenticationProperties.jwtExpiryPadding, ChronoUnit.SECONDS)
                webFilterExchange?.exchange?.response?.headers?.set(authenticationProperties.authorizationHeader ?: "Authorization", t.first)
                webFilterExchange?.exchange?.response?.headers?.set(authenticationProperties.refreshTokenHeader ?: "JWT-Refresh-Token", t.second)

                usersRepo.findByUserName(authentication.name)?.let { user ->
                    JwtResponse(t.first, user.id,user.userName, user.email, "${user.firstName} ${user.lastName}", tokeService.extractRolesFromToken(t.first)?.map { grantedAuthority -> grantedAuthority.authority } ).apply { expiry = expires }
                }


            }
            ?: throw HttpExceptionFactory.unauthorized()
        KotlinLogging.logger { }.debug("after token $token")

        val mono = Mono.just(DefaultDataBufferFactory().wrap(ObjectMapper().writeValueAsBytes(token)))

        return@mono webFilterExchange?.exchange?.response?.writeWith(mono)?.awaitFirstOrNull()


    }
}
