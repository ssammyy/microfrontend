package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.FactoryInspectionReportEntity
import org.kebs.app.kotlin.apollo.store.model.PermitApplicationEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository

interface IFactoryInspectionReportRepository: HazelcastRepository<FactoryInspectionReportEntity, Long> {
    fun findByPermitApplicationId(id: PermitApplicationEntity?): FactoryInspectionReportEntity?
}