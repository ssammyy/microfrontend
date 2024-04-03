package com.ngumo.inventoryapi.Dao.Repositories

import com.ngumo.inventoryapi.Dao.Models.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(username: String): User?
}