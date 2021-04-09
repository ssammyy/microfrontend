package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.RfcGoodsEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.stereotype.Repository
import java.sql.Date
import java.util.*

@Repository
interface IRfcGoodsRepository : HazelcastRepository<RfcGoodsEntity, Long> {

    fun findByRfcDateAndPartner(rfcDate: Date, partner: Long): List<RfcGoodsEntity>
//    fun findByRfcDateAndPartner(rfcDate: Date, partner: Long):List<RfcGoodsEntity>
}