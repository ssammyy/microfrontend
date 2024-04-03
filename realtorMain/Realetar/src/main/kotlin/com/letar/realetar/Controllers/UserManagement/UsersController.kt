package com.letar.realetar.Controllers.UserManagement

import com.letar.realetar.Models.Users
import com.letar.realetar.Repositories.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
import javax.validation.Valid

@RestController
@RequestMapping("realetar-v1/users")
class UsersController(
   private val userRepository: UserRepository
) {
    @PostMapping("/create")
    fun createUser(@Valid @RequestBody user: Users): ResponseEntity<Any> {
//        val userFound  = user.username?.let { userRepository.findByUsername(it) }
        val userFound = user.username?.let { userRepository.findByUsername(it) }
        if (userFound?.username.equals(user.username))
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body("user name already in use")


        val createdUser = userRepository.save(user)
        return ResponseEntity.created(URI("/users/${createdUser.id}")).body(createdUser)
    }


    @PutMapping("/update/{id}")
    fun updateUser(@PathVariable id: Long, @Valid @RequestBody updatedUser: Users): ResponseEntity<Users> {

        val user = userRepository.findById(id)
        return if (user.isPresent) {
            val existingUser = user.get()
            existingUser.apply {
                username = updatedUser.username
                firstName = updatedUser.firstName
                SecondName = updatedUser.SecondName
                phoneNumber = updatedUser.phoneNumber
                password = updatedUser.password
                gender = updatedUser.gender
                role = updatedUser.role
            }
            val savedUser = userRepository.save(existingUser)
            ResponseEntity.ok(savedUser)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("/purge/{id}")
    fun deleteUser(@PathVariable id: Long): ResponseEntity<Void> {
        return if (userRepository.existsById(id)) {
            userRepository.deleteById(id)
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}