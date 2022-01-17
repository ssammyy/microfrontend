package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.pvc.PvocCoiTimelinesDataEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface IPvocCoiTimelinesDataEntityRepo : HazelcastRepository<PvocCoiTimelinesDataEntity, Long> {
    fun findAllByCoiNumberNotNull(pageable: Pageable) : Page<PvocCoiTimelinesDataEntity>?
}