package com.ngumo.inventoryapi.Configurations.Security

import com.ngumo.inventoryapi.Dao.Repositories.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service


@Component
class CustomUserDetailsService(private val userRepository: UserRepository) : UserDetailsService {


    override fun loadUserByUsername(username: String): UserDetails {
        println("LOADING USERNAME ::...:::....:::...::....:::....::>....:.>>..")


        val user = userRepository.findByUsername(username)
            ?: throw UsernameNotFoundException("User not found with username: $username")

        return org.springframework.security.core.userdetails.User(
            user.username,
            user.password,
            emptyList() // Provide any additional user authorities/roles if needed
        )
    }
}
