package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.RiskProfileEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface IRiskProfileRepository : HazelcastRepository<RiskProfileEntity, Long> {
    @Query(value = "SELECT * from DAT_KEBS_RISK_PROFILE rp where TO_CHAR(rp.CATEGORIZATION_DATE,'YYYYMMDD')=? and rp.CREATED_BY<>?", nativeQuery = true)
    fun findByCategorizationDateAndCreatedByNotEquals(categorizationDate: String, clientId: String, page: Pageable): Page<RiskProfileEntity>
}