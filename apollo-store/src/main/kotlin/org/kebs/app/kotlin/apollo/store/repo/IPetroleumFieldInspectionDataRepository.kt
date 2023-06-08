package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.PetroleumFieldInspectionDataEntity
import org.kebs.app.kotlin.apollo.store.model.PetroleumInstallationInspectionEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository

interface IPetroleumFieldInspectionDataRepository: HazelcastRepository<PetroleumFieldInspectionDataEntity, Long> {
    fun findByInstallationInspectionData(installationInspectionData: PetroleumInstallationInspectionEntity): PetroleumFieldInspectionDataEntity?
}