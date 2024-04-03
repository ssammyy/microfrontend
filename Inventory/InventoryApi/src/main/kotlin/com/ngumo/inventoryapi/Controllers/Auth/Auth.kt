package com.ngumo.inventoryapi.Controllers.Auth

import com.ngumo.inventoryapi.Configurations.Security.JwtUtils
import com.ngumo.inventoryapi.Dao.Models.User
import com.ngumo.inventoryapi.Dao.Repositories.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import javax.validation.Valid
import javax.validation.constraints.NotBlank

@Controller
@RequestMapping("/api/va/auth")
class Auth (
    private val authenticationManager: AuthenticationManager,
    private val userDetailsService: UserDetailsService,

    private val userRepository: UserRepository
) {
//    private val userDetailsService: UserDetailsService? = null
    private val jwtUtils: JwtUtils? = null

    data class LoginRequest(
        @field:NotBlank
        val username: String,

        @field:NotBlank
        val password: String
    )

    data class UserWithToken(val user: User, val token: String)


    @PostMapping("/authenticsate")
    fun login(@Valid @RequestBody loginRequest: LoginRequest): ResponseEntity<*> {
        val user = userRepository.findByUsername(loginRequest.username)
        return if (user != null && user.password == loginRequest.password) {
            val userDetail = userDetailsService?.loadUserByUsername(loginRequest.username)

            val token = jwtUtils?.generateToken(userDetail)
            val userWithToken = token?.let { UserWithToken(user, it) }

            ResponseEntity.ok(userWithToken)
        }
        else
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password")

    }


}