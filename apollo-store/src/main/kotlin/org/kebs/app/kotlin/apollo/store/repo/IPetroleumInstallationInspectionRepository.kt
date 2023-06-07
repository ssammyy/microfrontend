package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.PermitApplicationEntity
import org.kebs.app.kotlin.apollo.store.model.PetroleumInstallationInspectionEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository

interface IPetroleumInstallationInspectionRepository: HazelcastRepository<PetroleumInstallationInspectionEntity, Long> {
    fun findByIdIsIn(ids : List<Long>) : List<PetroleumInstallationInspectionEntity>
}