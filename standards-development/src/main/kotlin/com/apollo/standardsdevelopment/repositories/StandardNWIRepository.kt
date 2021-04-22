package com.apollo.standardsdevelopment.repositories

import com.apollo.standardsdevelopment.models.StandardNWI
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StandardNWIRepository:JpaRepository<StandardNWI,Long> {
}
