package com.apollo.standardsdevelopment.repositories

import com.apollo.standardsdevelopment.models.JustificationForTC
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface JustificationForTCRepository:JpaRepository<JustificationForTC,Long> {
}