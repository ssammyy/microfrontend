package com.apollo.standardsdevelopment.repositories

import com.apollo.standardsdevelopment.models.StandardJustification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StandardJustificationRepository : JpaRepository<StandardJustification,Long> {
}
