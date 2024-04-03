package com.ngumo.inventoryapi.Controllers.Auth.Users

import com.ngumo.inventoryapi.Dao.Models.User
import com.ngumo.inventoryapi.Dao.Repositories.UserRepository
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import java.net.URI
import javax.validation.Valid

@RestController
@RequestMapping("inv-v1/users")
class UsersController(
   private val userRepository: UserRepository
) {
    @PostMapping("/create")
    fun createUser(@Valid @RequestBody user: User): ResponseEntity<User> {
        val authentication: Authentication? = SecurityContextHolder.getContext().authentication
        val createdBy: String? = authentication?.name
        println("created by $createdBy")

        user.createdBy = createdBy.toString()
        val createdUser = userRepository.save(user)
        return ResponseEntity.created(URI("/users/${createdUser.id}")).body(createdUser)
    }
    @PutMapping("/update/{id}")
    fun updateUser(@PathVariable id: Long, @Valid @RequestBody updatedUser: User): ResponseEntity<User> {
        println("this is the id" + id)
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