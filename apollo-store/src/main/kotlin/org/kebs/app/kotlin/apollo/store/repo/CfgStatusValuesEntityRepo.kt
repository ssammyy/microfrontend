package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.CfgStatusValuesEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository

interface CfgStatusValuesEntityRepo : HazelcastRepository<CfgStatusValuesEntity, Long> {
    fun findByStatus(status : Long) : CfgStatusValuesEntity?
}