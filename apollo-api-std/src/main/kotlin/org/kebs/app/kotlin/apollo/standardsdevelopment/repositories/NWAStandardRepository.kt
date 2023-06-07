package org.kebs.app.kotlin.apollo.standardsdevelopment.repositories

import org.kebs.app.kotlin.apollo.standardsdevelopment.models.NWAStandard
import org.springframework.data.jpa.repository.JpaRepository

interface NWAStandardRepository : JpaRepository<NWAStandard, Long>
