package com.ngumo.inventoryapi.Utils

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class Commons(
        val userDetailsService: UserDetailsService

) {
    fun getLoggedInUser(): UserDetails {
        var userDetails: UserDetails? = null
        val authentication = SecurityContextHolder.getContext().authentication
        if (authentication?.isAuthenticated == true && authentication.principal is UserDetails) {
            userDetails = authentication.principal as UserDetails
            return userDetails
        } else {
            throw IllegalStateException("User is not authenticated ")
        }
    }
}