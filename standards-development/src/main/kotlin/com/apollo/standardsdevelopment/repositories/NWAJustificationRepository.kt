package com.apollo.standardsdevelopment.repositories

import com.apollo.standardsdevelopment.models.NWAJustification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface NWAJustificationRepository : JpaRepository<NWAJustification,Long> {
}