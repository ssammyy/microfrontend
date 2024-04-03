package com.ngumo.inventoryapi.Configurations.Security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

abstract class UserPrincipal(
    private val username: String,
    private val password: String,
    private val authorities: Collection<GrantedAuthority>
) : UserDetails {

    override fun getUsername(): String {
        return username
    }

    override fun getPassword(): String {
        return password
    }

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return authorities
    }



}
