package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.CfgArrivalPointEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository

interface IArrivalPointRepository : HazelcastRepository<CfgArrivalPointEntity, Long> {
    fun findByAcronymName(acronymName: String) : CfgArrivalPointEntity?
}