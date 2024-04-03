package com.ngumo.inventoryapi.Configurations.Security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtAuthFilter : OncePerRequestFilter() {
    @Autowired
    var customUserDetailsService: CustomUserDetailsService? = null
    @Autowired
    var jwtUtils: JwtUtils? = null
    @Throws(IOException::class, ServletException::class, java.io.IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        val authHeader = request.getHeader(HttpHeaders.AUTHORIZATION)
        val userName: String
        val jwtTOken: String
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            filterChain.doFilter(request, response)
            return
        }
        jwtTOken = authHeader.substring(7)
        userName = jwtUtils!!.extractUserName(jwtTOken)

        if (userName != null && SecurityContextHolder.getContext().authentication == null) {
            val userDetails = customUserDetailsService!!.loadUserByUsername(userName)
            if (jwtUtils!!.isTokenValid(jwtTOken, userDetails)) {
                val usernamePasswordAuthenticationToken =
                    UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                usernamePasswordAuthenticationToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = usernamePasswordAuthenticationToken
            }
        }
        filterChain.doFilter(request, response)
    }
}
