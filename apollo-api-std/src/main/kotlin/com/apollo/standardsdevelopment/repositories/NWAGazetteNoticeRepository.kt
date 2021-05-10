package com.apollo.standardsdevelopment.repositories

import com.apollo.standardsdevelopment.models.NWAGazetteNotice
import org.springframework.data.jpa.repository.JpaRepository

interface NWAGazetteNoticeRepository : JpaRepository<NWAGazetteNotice, Long> {
}
