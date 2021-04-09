package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.RiskProfileEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.stereotype.Repository
import java.sql.Timestamp
import java.util.*

@Repository
interface IRiskProfileRepository:HazelcastRepository<RiskProfileEntity, Long> {
    fun findByCategorizationDate(categorizationDate:Date):Optional<RiskProfileEntity>
}