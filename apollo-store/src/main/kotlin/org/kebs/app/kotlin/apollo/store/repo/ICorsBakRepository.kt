package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.CorsBakEntity
import org.kebs.app.kotlin.apollo.store.model.di.ConsignmentDocumentDetailsEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ICorsBakRepository : HazelcastRepository<CorsBakEntity, Long> {
    fun findByChasisNumber(chasisNumber: String): CorsBakEntity?
    fun findByCorNumber(corNumber: String): CorsBakEntity?
    fun findByDocumentsTypeAndReviewStatus(documentsType: String, status: Int, page: Pageable): Page<CorsBakEntity>
    fun findByChasisNumberAndVersion(chasisNumber: String, version: Long?): CorsBakEntity?
    fun findFirstByChasisNumberIsNotNullAndConsignmentDocIdIsNotNull(): CorsBakEntity?
    fun findByConsignmentDocId(entity: ConsignmentDocumentDetailsEntity?): CorsBakEntity?

    @Query(value = "select count(*) as cc from DAT_KEBS_CORS_BAK where to_char(COR_ISSUE_DATE,'YYYY')=:gYear", nativeQuery = true)
    fun countAllByYearGenerate(gYear: Long): Long
}