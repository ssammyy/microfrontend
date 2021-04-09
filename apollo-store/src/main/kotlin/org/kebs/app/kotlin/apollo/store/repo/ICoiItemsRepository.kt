package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.CocItemsEntity
import org.kebs.app.kotlin.apollo.store.model.CoiItemsEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.stereotype.Repository

@Repository
interface ICoiItemsRepository : HazelcastRepository<CoiItemsEntity, Long> {
    fun findByCoiId(coiId: Long): List<CoiItemsEntity>?
}