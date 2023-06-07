package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.CocItemsEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.stereotype.Repository

@Repository
interface ICocItemsRepository : HazelcastRepository<CocItemsEntity, Long> {
    fun findByCocId(cocId: Long): List<CocItemsEntity>?
}