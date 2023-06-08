package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.pvc.RfcItemEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.stereotype.Repository

@Repository
interface IRfcItemsRepository : HazelcastRepository<RfcItemEntity, Long> {
    //    fun findByRfcId(rfcId:Long): Optional<RfcCoiItemsEntity>
    fun findByRfcId(rfcId: Long): MutableList<RfcItemEntity>
}