package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.MinistryInspectionStatusEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository

interface IMinistryInspectionStatusRepo : HazelcastRepository<MinistryInspectionStatusEntity, Long> {
    fun findAllByIdNotNull() : List<MinistryInspectionStatusEntity>?
}