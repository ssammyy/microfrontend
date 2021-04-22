package com.apollo.standardsdevelopment.repositories

import com.apollo.standardsdevelopment.models.StandardRequest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StandardRequestRepository:JpaRepository<StandardRequest,Long> {
}