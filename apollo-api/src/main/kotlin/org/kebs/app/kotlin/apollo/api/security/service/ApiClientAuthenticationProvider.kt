package org.kebs.app.kotlin.apollo.api.security.service

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
        val username = authentication.name
        val password = authentication.credentials as String
        this.clientService.authenticateClient(username, password)?.let {
            val user=this.customDetailsService.loadUserByUsername(username)
            return UsernamePasswordAuthenticationToken(user.username, password, user.authorities)
        }?:throw NullValueNotAllowedException("Invalid client id and/or secret")
    }

    override fun supports(arg0: Class<*>?): Boolean {
        return arg0?.equals(UsernamePasswordAuthenticationToken::class.java)?:false
    }
}