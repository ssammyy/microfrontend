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

package org.kebs.app.kotlin.apollo.api.security.bearer


import org.kebs.app.kotlin.apollo.api.security.jwt.JwtTokenService

import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import java.util.function.Function

@Component
class ServerHttpBearerAuthenticationConverter(
        private val tokenProvider: JwtTokenService,
        private val authorizationPayloadExtractorService: AuthorizationPayloadExtractorService,
        private val usernamePasswordAuthenticationBearer: UsernamePasswordAuthenticationBearer
) : Function<ServerWebExchange, Authentication> {
    override fun apply(t: ServerWebExchange): Authentication {
        authorizationPayloadExtractorService.extract(t)
                ?.let { header ->
                    tokenProvider.isolateBearerValue(header)
                            ?.let { token ->
                                tokenProvider.validatedToken(token)
                                        ?.let { validated ->
                                            return usernamePasswordAuthenticationBearer.create(validated)
                                        }
                                        ?: throw UsernameNotFoundException("Invalid Token")

                            }
                            ?: throw UsernameNotFoundException("Invalid Token")

                }
                ?: throw UsernameNotFoundException("Header not found")


    }


}