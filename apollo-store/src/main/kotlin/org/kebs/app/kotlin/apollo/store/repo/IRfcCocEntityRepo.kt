package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.RfcCocEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository

interface IRfcCocEntityRepo : HazelcastRepository<RfcCocEntity, Long>{
    fun findByUcrNumber(ucrNumber : String) : RfcCocEntity?
    fun findByCocNumber(cocNumber : String) : RfcCocEntity?
    fun findByRfcNumber(rfcNumber: String) : RfcCocEntity?
    fun findByPvocTimelineDataId(pvocTimelineDataId : Long) : RfcCocEntity?

}