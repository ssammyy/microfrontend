package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.pvc.RfcEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository

interface IRfcEntityRepo : HazelcastRepository<RfcEntity, Long> {
    fun findByUcrNumber(ucrNumber: String): RfcEntity?
    fun findByCocNumber(cocNumber: String): RfcEntity?
    fun findByRfcNumber(rfcNumber: String): RfcEntity?
    fun findByPvocTimelineDataId(pvocTimelineDataId: Long): RfcEntity?

}