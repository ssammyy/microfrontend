package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.CocContainersEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.stereotype.Repository

@Repository
interface ICocContainersRepository : HazelcastRepository<CocContainersEntity, Long> {
    fun findByCocId(coiId: Long): List<CocContainersEntity>
}