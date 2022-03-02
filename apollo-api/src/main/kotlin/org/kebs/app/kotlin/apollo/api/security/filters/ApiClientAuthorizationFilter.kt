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
import org.kebs.app.kotlin.apollo.api.security.bearer.OauthClientAuthenticationToken
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Component
class ApiClientAuthorizationFilter : OncePerRequestFilter() {

    @Autowired
    lateinit var authenticationManager: AuthenticationManager

    @Autowired
    lateinit var extractorService: AuthorizationPayloadExtractorService


    @Throws(IOException::class, ServletException::class)
    override fun doFilterInternal(
            request: HttpServletRequest, response: HttpServletResponse,
            filterChain: FilterChain
    ) {
        extractorService.extractBasic(request)?.let { token ->
            try {
                KotlinLogging.logger { }.info("Checking basic authentication: ${token.first}")
                val authentication = authenticationManager.authenticate(OauthClientAuthenticationToken(token.first, token.second, "password"))
                SecurityContextHolder.getContext().authentication = authentication
            } catch (ex: Exception) {
                KotlinLogging.logger { }.info("Failed to process basic authentication: ${ex.localizedMessage}")
            }
        }

        filterChain.doFilter(request, response)
    }
}