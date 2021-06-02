package org.kebs.app.kotlin.apollo.api.security.service

import mu.KotlinLogging
import org.jasypt.encryption.StringEncryptor
import org.kebs.app.kotlin.apollo.api.ports.provided.dao.CommonDaoServices
import org.kebs.app.kotlin.apollo.common.exceptions.NullValueNotAllowedException
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component


@Component
class CustomAuthenticationProvider(
    private val commonDaoServices: CommonDaoServices,
    private val jasyptStringEncryptor: StringEncryptor,
    private val customUserDetailsService: CustomUserDetailsService
) : AuthenticationProvider {

    @Throws(AuthenticationException::class)
    override fun authenticate(authentication: Authentication): Authentication {
        val username = authentication.name
        val password = authentication.credentials as String

        val user = commonDaoServices.findUserByUserName(username)

        val passwordMatch = BCryptPasswordEncoder().matches(password, user.credentials)

        if (!passwordMatch) {
            throw NullValueNotAllowedException("Invalid username or password")
        }

        val userRoles = user.id?.let { customUserDetailsService.getUserRoles(it) }

        val authorities = userRoles?.let { customUserDetailsService.getAuthorities(it) }

        return UsernamePasswordAuthenticationToken(username, password, authorities)
    }

    override fun supports(arg0: Class<*>?): Boolean {
        return true
    }
}