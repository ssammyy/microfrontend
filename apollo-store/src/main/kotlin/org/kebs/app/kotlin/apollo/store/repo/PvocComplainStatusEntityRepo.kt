package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.PvocComplainStatusEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository

interface PvocComplainStatusEntityRepo : HazelcastRepository<PvocComplainStatusEntity, Long> {
    fun findByStatus(status: Int) : PvocComplainStatusEntity
}