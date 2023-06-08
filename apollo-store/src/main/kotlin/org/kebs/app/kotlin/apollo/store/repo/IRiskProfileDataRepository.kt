package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.RiskProfileDataEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface IRiskProfileDataRepository :HazelcastRepository<RiskProfileDataEntity, Long> {
//    fun findByCategorizationDate(categorizationDate: Date): Optional<RiskProfileDataEntity>
fun findByCategorizationDate(categorizationDate: Date): MutableList<RiskProfileDataEntity>
}
