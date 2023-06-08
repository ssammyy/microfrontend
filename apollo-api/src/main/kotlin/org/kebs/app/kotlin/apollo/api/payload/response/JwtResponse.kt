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

package org.kebs.app.kotlin.apollo.api.payload.response

import org.kebs.app.kotlin.apollo.config.properties.auth.AuthenticationProperties
import org.springframework.beans.factory.annotation.Autowired


class JwtResponse {
    @Autowired
    lateinit var authenticationProperties: AuthenticationProperties

    var accessToken: String = ""
    var id: Long = 0L
    var username: String = ""
    var email: String = ""
    val roles: MutableList<String> = mutableListOf()
    var tokenType = authenticationProperties.bearerPrefix

}
