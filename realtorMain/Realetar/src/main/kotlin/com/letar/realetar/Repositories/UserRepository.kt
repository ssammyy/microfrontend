package com.letar.realetar.Repositories

import com.letar.realetar.Models.Users
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<Users, Long> {
    fun findByUsername(username: String): Users?
}