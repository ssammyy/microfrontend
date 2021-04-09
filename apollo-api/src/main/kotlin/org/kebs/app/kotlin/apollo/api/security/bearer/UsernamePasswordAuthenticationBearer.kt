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

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.security.jwt.JwtTokenService


import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.stereotype.Component
import java.text.ParseException

@Component
class UsernamePasswordAuthenticationBearer(
        private val jwtTokenService: JwtTokenService
) {

    fun create(authToken: String): Authentication {
        try {
            val subject: String? = jwtTokenService.getUsernameFromToken(authToken)
            var authorities: MutableList<GrantedAuthority>? = null
            jwtTokenService.extractRolesFromToken(authToken)
                    ?.let { auth ->
                        authorities = auth
                    }
            return UsernamePasswordAuthenticationToken(subject, authToken, authorities)
        } catch (e: ParseException) {
            KotlinLogging.logger { }.error(e.message)
            KotlinLogging.logger { }.trace(e.message, e)
            throw e
        }


    }
}