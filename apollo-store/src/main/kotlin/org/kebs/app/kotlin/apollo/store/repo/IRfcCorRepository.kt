package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.RfcCorEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.stereotype.Repository
import java.sql.Date
import java.util.*

@Repository
interface IRfcCorRepository : HazelcastRepository<RfcCorEntity,Long> {
    fun findByRfcDateAndPartner(rfcDate: Date, partner: Long): List<RfcCorEntity>?
}