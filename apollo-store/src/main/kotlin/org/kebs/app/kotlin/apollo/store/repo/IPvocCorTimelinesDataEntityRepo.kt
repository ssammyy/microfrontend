package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.PvocCorTimelinesDataEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.hazelcast.repository.HazelcastRepository

interface IPvocCorTimelinesDataEntityRepo : HazelcastRepository<PvocCorTimelinesDataEntity, Long> {
    fun findByUcrNumber(ucrNumber: String) : PvocCorTimelinesDataEntity?
    fun findAllByUcrNumberNotNull(pageable: PageRequest) : Page<PvocCorTimelinesDataEntity>?
}