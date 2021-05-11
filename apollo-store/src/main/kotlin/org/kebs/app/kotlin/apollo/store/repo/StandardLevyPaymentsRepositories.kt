package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.model.registration.CompanyProfileEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface IStandardLevyPaymentsRepository : HazelcastRepository<StandardLevyPaymentsEntity, Long> {
    fun findByManufacturerEntity(manufacturerEntity: Long, pageable: Pageable): Page<StandardLevyPaymentsEntity>?
    fun findByManufacturerEntityOrderByIdDesc(manufacturerEntity: Long): List<StandardLevyPaymentsEntity>?
    fun findByManufacturerEntity(manufacturerEntity: CompanyProfileEntity): StandardLevyPaymentsEntity?
    fun findAllByOrderByIdDesc(): List<StandardLevyPaymentsEntity>?
//    fun findByOrderById(page: Pageable?): List<StandardLevyPaymentsEntity>?
}

interface IBatchJobDetailsRepository : HazelcastRepository<BatchJobDetails, Long>
interface IBatchJobTypesRepository : HazelcastRepository<BatchJobTypes, Long>

interface IStagingStandardsLevyManufacturerEntryNumberRepository : HazelcastRepository<StagingStandardsLevyManufacturerEntryNumber, Long> {
    fun findAllByStatus(status: Int, page: Pageable?): Page<StagingStandardsLevyManufacturerEntryNumber>?

    @Query(value = "SELECT count(*) FROM STG_STANDARDS_LEVY_MANUFACTURER_ENTRY_NUMBER p WHERE p.STATUS = :status", nativeQuery = true)
    fun findRecordCount(@Param("status") status: Int): Int
}

interface IStagingStandardsLevyManufacturerPenaltyRepository : HazelcastRepository<StagingStandardsLevyManufacturerPenalty, Long> {
    fun findAllByStatus(status: Int, page: Pageable?): Page<StagingStandardsLevyManufacturerPenalty>?

    @Query(value = "SELECT count(*) FROM STG_STANDARDS_LEVY_MANUFACTURER_PENALTY p WHERE p.STATUS = :status", nativeQuery = true)
    fun findRecordCount(@Param("status") status: Int): Int
}
