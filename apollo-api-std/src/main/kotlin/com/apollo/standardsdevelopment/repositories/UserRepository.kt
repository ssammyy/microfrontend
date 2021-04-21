package com.apollo.standardsdevelopment.repositories

import com.apollo.standardsdevelopment.models.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User,Long>{
}
