package org.kebs.app.kotlin.apollo.standardsdevelopment.repositories

import org.kebs.app.kotlin.apollo.standardsdevelopment.models.NWAGazettement
import org.springframework.data.jpa.repository.JpaRepository

interface NWAGazettementRepository : JpaRepository<NWAGazettement, Long>
