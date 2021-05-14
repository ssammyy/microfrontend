package com.apollo.standardsdevelopment.repositories

import com.apollo.standardsdevelopment.models.NWAJustification
import com.apollo.standardsdevelopment.models.NWAPreliminaryDraft
import org.springframework.data.jpa.repository.JpaRepository

interface NWAPreliminaryDraftRepository : JpaRepository<NWAPreliminaryDraft, Long> {
}
