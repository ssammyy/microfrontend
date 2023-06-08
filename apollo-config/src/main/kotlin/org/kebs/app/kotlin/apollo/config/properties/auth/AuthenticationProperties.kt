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

package org.kebs.app.kotlin.apollo.config.properties.auth

import com.ulisesbocchio.jasyptspringboot.annotation.EncryptablePropertySource

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration


@Configuration
@EncryptablePropertySource("file:\${CONFIG_PATH}/auth.properties")
class AuthenticationProperties {


    @Value("\${org.kebs.app.kotlin.apollo.config.auth.requires.no.authentication.post}")
    var requiresNoAuthenticationPost: String? = null


    @Value("\${org.kebs.app.kotlin.apollo.config.auth.jwt.refresh.token.expiration.ms}")
    val refreshTokenExpirationMs: Long = 0


    @Value("\${org.kebs.app.kotlin.apollo.config.auth.custom.refresh.token.header}")
    val refreshTokenHeader: String? = null

    @Value("\${org.kebs.app.kotlin.apollo.config.auth.jwt.expiry.padding}")
    val jwtExpiryPadding: Long = 5L

    @Value("\${org.kebs.app.kotlin.apollo.auth.api.login.url}")
    val apiLoginUrl: String? = null

    @Value("\${org.kebs.app.kotlin.apollo.auth.ldap.url}")
    val ldapUrls: String? = null

    @Value("\${org.kebs.app.kotlin.apollo.auth.ldap.base}")
    val ldapBase: String? = null

    @Value("\${org.kebs.app.kotlin.apollo.auth.prefixldap.username}")
    val ldapUsername: String? = null

    @Value("\${org.kebs.app.kotlin.apollo.auth.ldap.password}")
    val ldapPassword: String? = null


    @Value("\${org.kebs.app.kotlin.apollo.auth.ldap.dn.patterns}")
    val dnPatterns: String? = null

    @Value("\${org.kebs.app.kotlin.apollo.auth.ldap.search.base}")
    val searchBase: String? = null

    @Value("\${org.kebs.app.kotlin.apollo.auth.ldap.search.filter}")
    val searchFilter: String? = null

    @Value("\${org.kebs.app.kotlin.apollo.auth.ldap.group.search.base}")
    val groupSearchBase: String? = null

    @Value("\${org.kebs.app.kotlin.apollo.auth.ldap.group.search.filter}")
    val groupSearchFilter: String? = null

    @Value("\${org.kebs.app.kotlin.apollo.auth.ldap.password.attribute}")
    val passwordAttribute: String? = null

    @Value("\${org.kebs.app.kotlin.apollo.auth.custom.unauthorized.message}")
    val unauthorizedMessage: String? = null

    @Value("\${org.kebs.app.kotlin.apollo.auth.custom.authorization.header}")
    val authorizationHeader: String? = null

    @Value("\${org.kebs.app.kotlin.apollo.auth.jwt.bearer.prefix}")
    val bearerPrefix: String? = null

    @Value("\${org.kebs.app.kotlin.apollo.auth.custom.missing.jwt.err}")
    val missingJwtErr: String? = null

    @Value("\${org.kebs.app.kotlin.apollo.auth.custom.expired.jwt.err}")
    val expiredJwtErr: String? = null

    @Value("\${org.kebs.app.kotlin.apollo.auth.custom.missing.bearer.err}")
    val missingBearerErr: String? = null

    @Value("\${org.kebs.app.kotlin.apollo.auth.custom.user.not.found.err}")
    val userNotFoundErr: String? = null

    @Value("\${org.kebs.app.kotlin.apollo.auth.custom.claims.id}")
    val claimsId: String? = null

    @Value("\${org.kebs.app.kotlin.apollo.auth.custom.claims.email}")
    val claimsEmail: String? = null

    @Value("\${org.kebs.app.kotlin.apollo.auth.custom.claims.role}")
    val claimsRole: String? = null

    @Value("\${org.kebs.app.kotlin.apollo.auth.custom.user.disabled}")
    val userDisabled: String? = null

    @Value("\${org.kebs.app.kotlin.apollo.auth.custom.invalid.credentials}")
    val invalidCredentials: String? = null

    @Value("\${org.kebs.app.kotlin.apollo.auth.custom.type.create}")
    val typeCreate: String? = null

    @Value("\${org.kebs.app.kotlin.apollo.auth.custom.type.update}")
    val typeUpdate: String? = null

    @Value("\${org.kebs.app.kotlin.apollo.auth.custom.default.role}")
    val defaultRole: String? = null

    @Value("\${org.kebs.app.kotlin.apollo.auth.custom.password.dnt.match}")
    val passwordDntMatch: String? = null

    @Value("\${org.kebs.app.kotlin.apollo.auth.custom.password.created}")
    val passwordCreated: String? = null

    @Value("\${org.kebs.app.kotlin.apollo.auth.custom.employee.created}")
    val employeeCreated: String? = null

    @Value("\${org.kebs.app.kotlin.apollo.auth.custom.details.received.response}")
    val detailsReceivedResponse: String? = null

    @Value("\${org.kebs.app.kotlin.apollo.auth.jwt.authorities.key}")
    val jwtAuthoritiesKey: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.auth.jwt.secret}")
    val jwtSecret: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.auth.jwt.expiration.ms}")
    val jwtExpirationMs: Long = 0

    @Value("\${org.kebs.app.kotlin.apollo.auth.jwt.issuer}")
    var jwtIssuer: String? = null

    @Value("\${org.kebs.app.kotlin.apollo.auth.jwt.bearer.delimiter}")
    var bearerDelimiter: String? = null

    @Value("\${org.kebs.app.kotlin.apollo.auth.requires.authentication}")
    var requiresAuthentication: String? = null

    @Value("\${org.kebs.app.kotlin.apollo.auth.requires.no.authentication}")
    var requiresNoAuthentication: String? = null

    @Value("\${org.kebs.app.kotlin.apollo.auth.requires.no.authentication.apollo.api}")
    var requiresNoAuthenticationApolloApi: String? = null

    @Value("\${org.kebs.app.kotlin.apollo.auth.requires.no.authentication.apollo.api.token}")
    var requiresNoAuthenticationApolloApiToken: String? = null

    @Value("\${org.kebs.app.kotlin.apollo.auth.requires.no.authentication.cros}")
    var requiresNoAuthenticationCros: String? = null

    @Value("\${org.kebs.app.kotlin.apollo.auth.home.page}")
    val homePage: String? = null

    @Value("\${org.kebs.app.kotlin.apollo.auth.logout.url}")
    val logoutUrl: String? = null

    @Value("\${org.kebs.app.kotlin.apollo.auth.logout.success.url}")
    val logoutSuccessUrl: String? = null

    @Value("\${org.kebs.app.kotlin.apollo.auth.jwt.ip.key}")
    val jwtIpKey: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.auth.jwt.ip.internal.key}")
    val jwtIpInternalKey: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.auth.jwt.agent.key}")
    val jwtAgentKey: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.auth.jwt.full.name.key}")
    val jwtFullNameKey: String = ""

    @Value("\${org.kebs.app.kotlin.apollo.auth.jwt.user.type.key}")
    val jwtUserTypeKey: String = ""


}
