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

package org.kebs.app.kotlin.apollo.api.security.filters


import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.security.bearer.AuthorizationPayloadExtractorService
import org.kebs.app.kotlin.apollo.api.security.bearer.UsernamePasswordAuthenticationBearer
import org.kebs.app.kotlin.apollo.api.security.jwt.JwtTokenService
import org.kebs.app.kotlin.apollo.config.properties.auth.AuthenticationProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class JWTAuthorizationFilter : OncePerRequestFilter() {

    @Autowired
    lateinit var tokenService: JwtTokenService

    @Autowired
    lateinit var extractorService: AuthorizationPayloadExtractorService

    @Autowired
    lateinit var authenticationProperties: AuthenticationProperties

    @Throws(IOException::class, ServletException::class)
    override fun doFilterInternal(
            request: HttpServletRequest, response: HttpServletResponse,
            filterChain: FilterChain
    ) {
        extractorService
                .extract(request)
                ?.let { token ->
                    var myToken = token
                    authenticationProperties.bearerPrefix
                            ?.let { prefix ->
                                myToken = token.replace(prefix, "")
                            }

                    val authentication = UsernamePasswordAuthenticationBearer(tokenService).create(myToken)
                    SecurityContextHolder.getContext().authentication = authentication
                    response.addHeader(HttpHeaders.AUTHORIZATION, token)

                }
            filterChain.doFilter(request, response)

    }
}
