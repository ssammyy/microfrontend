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

package org.kebs.app.kotlin.apollo.api.flux.security.jwt.authentication


import kotlinx.coroutines.reactor.mono
import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.flux.ports.provided.dao.DaoService
import org.kebs.app.kotlin.apollo.api.flux.ports.provided.dao.HttpExceptionFactory
import org.kebs.app.kotlin.apollo.api.flux.security.jwt.CustomSecurityContextRepository
import org.kebs.app.kotlin.apollo.api.flux.security.jwt.JwtTokenService
import org.kebs.app.kotlin.apollo.config.properties.auth.AuthenticationProperties
import org.springframework.core.io.buffer.DefaultDataBufferFactory
import org.springframework.http.HttpHeaders
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.server.WebFilterExchange
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono


@Component
class JwtServerAuthenticationSuccessHandler(
    private val tokenService: JwtTokenService,
    private val securityContextRepository: CustomSecurityContextRepository,
    private val authenticationProperties: AuthenticationProperties,
    private val daoService: DaoService
) : ServerAuthenticationSuccessHandler {
    override fun onAuthenticationSuccess(webFilterExchange: WebFilterExchange?, authentication: Authentication?): Mono<Void?> = mono {
//        val ip = webFilterExchange?.exchange?.request?.remoteAddress?.address?.hostAddress
//        val forwardedIp = webFilterExchange?.exchange?.request?.headers?.get("X-FORWARDED-FOR")
        KotlinLogging.logger { }.debug("before token")
        var token = authentication
            ?.let { tokenService.getHttpAuthHeaderValue(it, webFilterExchange?.exchange?.request) }
            ?: throw HttpExceptionFactory.unauthorized()
        KotlinLogging.logger { }.debug("after token $token")




        webFilterExchange?.exchange?.response?.headers?.set(HttpHeaders.AUTHORIZATION, token)

        SecurityContextHolder.getContext().authentication = authentication

        token = authenticationProperties.bearerPrefix?.let { token.replace(it, "").trim() } ?: token
        securityContextRepository.load(webFilterExchange?.exchange)

//        val tt = daoService.mapper().writeValueAsBytes(token)

//         return@mono webFilterExchange?.exchange?.response?.writeWith(Mono.just(DefaultDataBufferFactory().wrap(SerializationUtils.serialize(token))))?.block()
//        return@mono webFilterExchange?.exchange?.response?.writeWith(Mono.just(DefaultDataBufferFactory().wrap(daoService.mapper().writeValueAsBytes(token))))?.block()


        val mono = Mono.just(DefaultDataBufferFactory().wrap(token.toByteArray()))

        return@mono webFilterExchange?.exchange?.response?.writeWith(mono)?.block()

    }
}