package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.SdlFactoryVisitReportsUploadEntity
import org.kebs.app.kotlin.apollo.store.model.StandardLevyFactoryVisitReportEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository

interface IStandardLevyFactoryVisitReportRepository: HazelcastRepository<StandardLevyFactoryVisitReportEntity, Long> {
    fun findByManufacturerEntity(manufacturerEntity: Long): StandardLevyFactoryVisitReportEntity?
    fun findFirstByManufacturerEntityAndStatusOrderByIdDesc(manufacturerEntity: Long, status: Int): StandardLevyFactoryVisitReportEntity?
}

interface ISdlFactoryVisitReportsUploadEntityRepository : HazelcastRepository<SdlFactoryVisitReportsUploadEntity, Long>{
    fun findByReportId(reportId: Long) : SdlFactoryVisitReportsUploadEntity?
}
