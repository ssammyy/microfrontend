package org.kebs.app.kotlin.apollo.standardsdevelopment.repositories

import org.kebs.app.kotlin.apollo.standardsdevelopment.models.NWAGazetteNotice
import org.springframework.data.jpa.repository.JpaRepository

interface NWAGazetteNoticeRepository : JpaRepository<NWAGazetteNotice, Long>
