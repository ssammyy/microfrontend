package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.IdfItemsEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.stereotype.Repository

@Repository
interface IIdfItemsRepository: HazelcastRepository<IdfItemsEntity, Long> {
    fun findByIdfIdAndStatus(idfId:Long, status: Int): MutableList<IdfItemsEntity>

}