package org.kebs.app.kotlin.apollo.api.security.service

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.api.security.bearer.OauthClientAuthenticationToken
import org.kebs.app.kotlin.apollo.api.service.ApiClientService
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.stereotype.Component


@Component
class ApiClientAuthenticationProvider(
        private val customDetailsService: ApiClientDetailsService,
        private val clientService: ApiClientService,
) : AuthenticationProvider {

    @Throws(AuthenticationException::class)
    override fun authenticate(authentication: Authentication): Authentication {
        KotlinLogging.logger {  }.debug("Client authentication provider: ${authentication.principal}")
        val username = authentication.name
        val password = authentication.credentials as String
        this.clientService.authenticateClient(username, password)?.let {
            val user=this.customDetailsService.loadUserByUsername(username)
            return UsernamePasswordAuthenticationToken(user, password, user.authorities)
        }?:throw NullValueNotAllowedException("Invalid client id and/or secret")
    }

    override fun supports(arg0: Class<*>?): Boolean {
        KotlinLogging.logger {  }.info("Client authentication provider: ${arg0?.name}")
        return arg0?.equals(OauthClientAuthenticationToken::class.java)?:false
    }
}