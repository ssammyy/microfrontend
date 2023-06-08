package org.kebs.app.kotlin.apollo.api.security.filters

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class CustomUsernamePasswordAuthenticationFilter : UsernamePasswordAuthenticationFilter() {
    override fun attemptAuthentication(request: HttpServletRequest?, response: HttpServletResponse?): Authentication {
        if (!request?.method.equals("POST")) {
            ("Authentication method not supported: ${request?.method}")
        }
        var username = obtainUsername(request) ?: ""
        val password = obtainPassword(request) ?: ""
        val otp = request?.getParameter("otp") ?: ""
        username = "$username||$otp"
//        KotlinLogging.logger { }.info("At filter $username")
        val authRequest = UsernamePasswordAuthenticationToken(username, password)

        setDetails(request, authRequest)
        return this.authenticationManager.authenticate(authRequest)
    }
}