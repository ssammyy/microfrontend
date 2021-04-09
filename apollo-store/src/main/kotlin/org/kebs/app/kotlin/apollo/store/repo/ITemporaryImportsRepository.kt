package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.TemporaryImportsEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.stereotype.Repository

@Repository
interface ITemporaryImportsRepository : HazelcastRepository<TemporaryImportsEntity, Long> {

    fun findByAssignIoId(assignIoId: Long,pageable: Pageable): Page<TemporaryImportsEntity>?

}