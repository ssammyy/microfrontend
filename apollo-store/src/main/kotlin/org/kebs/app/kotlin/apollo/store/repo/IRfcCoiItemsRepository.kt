package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.RfcCoiItemsEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface IRfcCoiItemsRepository : HazelcastRepository<RfcCoiItemsEntity, Long> {
    //    fun findByRfcId(rfcId:Long): Optional<RfcCoiItemsEntity>
    fun findByRfcId(rfcId: Long): MutableList<RfcCoiItemsEntity>
}