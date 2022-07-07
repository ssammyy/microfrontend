package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.pvc.PvocQueryResponseEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.stereotype.Repository

@Repository
interface IPvocQueryResponseRepository : HazelcastRepository<PvocQueryResponseEntity, Long> {
    fun countAllBySerialNumberStartsWith(prefix: String?): Long
}