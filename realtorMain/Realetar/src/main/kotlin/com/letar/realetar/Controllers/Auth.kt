//package com.letar.realetar.Controllers.Auth
//
//import com.letar.realetar.Models.Users
//import com.letar.realetar.Repositories.UserRepository
//import org.springframework.http.HttpStatus
//import org.springframework.http.ResponseEntity
//import org.springframework.web.bind.annotation.PostMapping
//import org.springframework.web.bind.annotation.RequestBody
//import org.springframework.web.bind.annotation.RequestMapping
//import org.springframework.web.bind.annotation.RestController
//
//
//import javax.validation.Valid
//import javax.validation.constraints.NotBlank
//
//@RestController
//@RequestMapping("realetar-v1/auth")
//class Auth(
//    private val userRepository: UserRepository
//
//) {
//    data class LoginRequest(
//        @field:NotBlank
//        val username: String,
//
//        @field:NotBlank
//        val password: String
//    )
//
//    data class UserWithToken(val user: Users, val token: String)
//
//
//    @PostMapping("/login")
//    fun login(@Valid @RequestBody loginRequest: loginRequest): ResponseEntity<*> {
//        val user = userRepository.findByUsername(loginRequest.username)
//        return if (user != null && user.password == loginRequest.password) {
////            val userDetail = userDetailsService?.loadUserByUsername(loginRequest.username)
////
////            val token = jwtUtils?.generateToken(userDetail)
////            val userWithToken = token?.let { UserWithToken(user, it) }
//
//            ResponseEntity.ok(user)
//        }
//        else
//            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password")
//
//    }
//}