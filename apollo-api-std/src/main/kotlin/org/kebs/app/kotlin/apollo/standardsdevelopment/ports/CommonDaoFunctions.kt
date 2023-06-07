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

package org.kebs.app.kotlin.apollo.standardsdevelopment.ports

import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.kebs.app.kotlin.apollo.common.dto.LoginRequest
import org.springframework.core.ResolvableType
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.codec.json.AbstractJackson2Decoder
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

/**
 * This is a shortcut method to quickly create response status exception around the authentication and authorization processes
 */
object HttpExceptionFactory {
    fun badRequest(message: String? = null): ResponseStatusException =

        ResponseStatusException(HttpStatus.BAD_REQUEST, message ?: "Bad Request")

    fun unauthorized(message: String? = null): ResponseStatusException = ResponseStatusException(HttpStatus.UNAUTHORIZED, message ?: "Unauthorized")
    fun forbidden(message: String? = null): ResponseStatusException = ResponseStatusException(HttpStatus.FORBIDDEN, message ?: "Forbidden")
}

/**
 * To be able to parse authentication request payload and extract the @see LoginRequest from the payload asynchronously
 */
suspend fun extractUsernameAndPassword(exchange: ServerWebExchange, jacksonDecoder: AbstractJackson2Decoder): LoginRequest? {
    val dataBuffer = exchange.request.body
    val type = ResolvableType.forClass(LoginRequest::class.java)
    return jacksonDecoder
        .decodeToMono(dataBuffer, type, MediaType.APPLICATION_JSON, mapOf())
        .onErrorResume { Mono.empty<LoginRequest>() }
        .cast(LoginRequest::class.java)
        .awaitFirstOrNull()
}




