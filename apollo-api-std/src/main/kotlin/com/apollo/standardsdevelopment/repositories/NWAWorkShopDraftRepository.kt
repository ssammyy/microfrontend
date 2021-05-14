package com.apollo.standardsdevelopment.repositories

import com.apollo.standardsdevelopment.models.NWAWorkShopDraft
import org.springframework.data.jpa.repository.JpaRepository

interface NWAWorkShopDraftRepository : JpaRepository<NWAWorkShopDraft, Long> {
}
