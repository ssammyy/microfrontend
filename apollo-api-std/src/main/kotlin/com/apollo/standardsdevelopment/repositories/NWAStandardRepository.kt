package com.apollo.standardsdevelopment.repositories

import com.apollo.standardsdevelopment.models.NWAStandard
import org.springframework.data.jpa.repository.JpaRepository

interface NWAStandardRepository : JpaRepository<NWAStandard, Long> {
}
