package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.IdfItemsEntity
import org.kebs.app.kotlin.apollo.store.model.IdfsEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository

interface IdfsEntityRepository : HazelcastRepository<IdfsEntity, Long> {
    fun findFirstByUcr(ucr : String) : IdfsEntity?
}

interface IdfItemsEntityRepository : HazelcastRepository<IdfItemsEntity, Long> {
    fun findByIdfId(idfId: Long) : List<IdfItemsEntity>?

}

