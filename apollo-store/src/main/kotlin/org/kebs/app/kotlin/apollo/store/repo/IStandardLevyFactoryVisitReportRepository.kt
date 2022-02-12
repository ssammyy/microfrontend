package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.SdlFactoryVisitReportsUploadEntity
import org.kebs.app.kotlin.apollo.store.model.SlUpdatecompanyDetailsEntity
import org.kebs.app.kotlin.apollo.store.model.StandardLevyFactoryVisitReportEntity
import org.kebs.app.kotlin.apollo.store.model.registration.CompanyProfileEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface IStandardLevyFactoryVisitReportRepository: HazelcastRepository<StandardLevyFactoryVisitReportEntity, Long> {
    fun findByManufacturerEntity(manufacturerEntity: Long): StandardLevyFactoryVisitReportEntity?
    fun findFirstByManufacturerEntityAndStatusOrderByIdDesc(manufacturerEntity: Long, status: Int): StandardLevyFactoryVisitReportEntity?
    fun findBySlProcessInstanceId(slProcessInstanceId: String): StandardLevyFactoryVisitReportEntity?

    @Query(
        value = "SELECT *  FROM DAT_STANDARD_LEVY_FACTORY_VISIT_REPORT WHERE SL_PROCESS_STATUS='1'",
        nativeQuery = true
    )
    fun getCompleteTasks(): MutableList<StandardLevyFactoryVisitReportEntity>

}

interface ISdlFactoryVisitReportsUploadEntityRepository : HazelcastRepository<SdlFactoryVisitReportsUploadEntity, Long>{
    fun findByReportId(reportId: Long) : SdlFactoryVisitReportsUploadEntity?
}

interface ISlUpdatecompanyDetailsEntityRepository : HazelcastRepository<SlUpdatecompanyDetailsEntity, Long>{
    //fun findByReportId(reportId: Long) : SdlFactoryVisitReportsUploadEntity?
}

