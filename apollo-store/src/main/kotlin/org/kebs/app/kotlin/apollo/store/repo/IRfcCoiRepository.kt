package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.RfcCoiEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.stereotype.Repository
import java.sql.Date
import java.util.*

@Repository
interface IRfcCoiRepository : HazelcastRepository<RfcCoiEntity, Long>{
    fun findByRfcDateAndPartner(rfcDate: Date, partner: Long): List<RfcCoiEntity>
    fun findByRfcNumber(rfcNumber: String): RfcCoiEntity?
}