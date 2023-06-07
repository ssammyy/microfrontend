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

package org.kebs.app.kotlin.apollo.api.flux.security.jwt

import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.reactor.mono
import mu.KotlinLogging

import org.kebs.app.kotlin.apollo.api.flux.ports.provided.dao.HttpExceptionFactory
import org.kebs.app.kotlin.apollo.api.flux.ports.provided.dao.extractUsernameAndPassword
import org.kebs.app.kotlin.apollo.common.dto.LoginRequest
import org.springframework.http.HttpHeaders
import org.springframework.http.codec.json.AbstractJackson2Decoder
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.security.web.server.context.ServerSecurityContextRepository
import org.springframework.stereotype.Component
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.validation.Errors
import org.springframework.validation.Validator
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono


@Component
class CustomSecurityContextRepository(
    private val tokenService: JwtTokenService,
    private val authenticationManager: ReactiveAuthenticationManager,
    private val validator: Validator,
    private val jacksonDecoder: AbstractJackson2Decoder

) : ServerSecurityContextRepository {


    override fun save(exchange: ServerWebExchange?, context: SecurityContext?): Mono<Void> = mono {


//        val exchange = context?.exchange ?: return@mono notAuthorized
        /*val authHeader = exchange?.request?.headers?.getFirst(HttpHeaders.AUTHORIZATION) ?: return@mono null

        KotlinLogging.logger { }.debug("Before try-catch")

        try {


            if (!authHeader.startsWith("Bearer ")) {
                KotlinLogging.logger { }.info("No Header")
                return@mono null
            }



            SecurityContextHolder.getContext()?.authentication?.name
                .let {
                    tokenService.isolateBearerValue(authHeader)
                        ?.let { authToken ->
                            val user = tokenService.getUsernameFromToken(authToken)
                            val roles = tokenService.extractRolesFromToken(authToken)

                            val auth: Authentication = UsernamePasswordAuthenticationToken(user, null, roles)
                            authenticationManager.authenticate(auth).map { authentication -> SecurityContextImpl(authentication) }.awaitSingle()

                        }
                }


        } catch (e: Exception) {
            KotlinLogging.logger { }.error(e.message, e)
            KotlinLogging.logger { }.debug(e.message, e)
}
        */
//
        exchange?.let { extractUsernameAndPassword(it, jacksonDecoder) }?.let { loginRequest ->
            val errors: Errors = BeanPropertyBindingResult(loginRequest, LoginRequest::class.java.name)
            validator.validate(loginRequest, errors)

            if (errors.allErrors.isNotEmpty()) {
                var message = ""
                errors.fieldErrors.forEach { message += it }
                throw HttpExceptionFactory.badRequest(message)
            }

            val auth: Authentication = UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password)
            context?.authentication = auth
        } ?: KotlinLogging.logger { }.error("No Login Payload Found")

        return@mono null

    }

    override fun load(swe: ServerWebExchange?): Mono<SecurityContext> = mono {
        val request = swe?.request
        val authHeader = request?.headers?.getFirst(HttpHeaders.AUTHORIZATION)
        SecurityContextHolder.getContext()?.authentication?.name
            .let {
                return@mono tokenService.isolateBearerValue(authHeader)
                    ?.let { authToken ->
                        val user = tokenService.getUsernameFromToken(authToken)
                        val roles = tokenService.extractRolesFromToken(authToken)

                        val auth: Authentication = UsernamePasswordAuthenticationToken(user, null, roles)
                        authenticationManager.authenticate(auth).map { authentication -> SecurityContextImpl(authentication) }.awaitSingle()

                    }
            }


    }
}
