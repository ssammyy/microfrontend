package com.apollo.standardsdevelopment.repositories

import com.apollo.standardsdevelopment.models.NWAGazettement
import org.springframework.data.jpa.repository.JpaRepository

interface NWAGazettementRepository : JpaRepository<NWAGazettement, Long> {
}
