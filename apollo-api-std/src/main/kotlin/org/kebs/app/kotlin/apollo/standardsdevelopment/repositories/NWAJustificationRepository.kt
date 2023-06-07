package org.kebs.app.kotlin.apollo.standardsdevelopment.repositories

import org.kebs.app.kotlin.apollo.standardsdevelopment.models.NWAJustification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface NWAJustificationRepository : JpaRepository<NWAJustification,Long>
