package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.PvocTimelinesDataEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.stereotype.Repository


@Repository
interface IPvocTimelinesDataRepository : HazelcastRepository<PvocTimelinesDataEntity, Long> {
    fun findAllByCocNumberNotNull(pageable: Pageable) : Page<PvocTimelinesDataEntity>?
    fun findByCocNumber(cocNumber : String) : PvocTimelinesDataEntity
}