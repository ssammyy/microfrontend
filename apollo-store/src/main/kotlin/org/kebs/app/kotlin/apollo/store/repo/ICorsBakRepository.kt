package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.CorsBakEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.stereotype.Repository

@Repository
interface ICorsBakRepository : HazelcastRepository<CorsBakEntity, Long> {
    fun findByChasisNumber(chasisNumber: String): CorsBakEntity?
}