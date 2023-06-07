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
    fun findByCorNumberAndDocumentsType(corNumber: String, documentsType: String): CorsBakEntity?
    fun findByDocumentsTypeAndReviewStatusAndCompliant(
        documentsType: String,
        status: Int,
        compliant: String,
        page: Pageable
    ): Page<CorsBakEntity>

    fun findByDocumentsTypeAndCorNumberContainsAndCompliantOrDocumentsTypeAndChasisNumberContainsAndCompliant(
        documentsType: String,
        corNumber: String,
        compliant1: String,
        docType: String,
        chassisNumber: String,
        compliant2: String,
        page: Pageable
    ): Page<CorsBakEntity>

    fun findByCorNumberContainsAndCompliantOrChasisNumberContainsAndCompliant(
        corNumber: String,
        compliant: String,
        chassisNumber: String,
        compliant2: String,
        page: Pageable
    ): Page<CorsBakEntity>

    fun findByReviewStatus(status: Int, page: Pageable): Page<CorsBakEntity>
    fun findByDocumentsType(documentsType: String, page: Pageable): Page<CorsBakEntity>
    fun findByChasisNumberAndVersion(chasisNumber: String, version: Long?): CorsBakEntity?
    fun findFirstByChasisNumberIsNotNullAndConsignmentDocIdIsNotNull(): CorsBakEntity?
    fun findByConsignmentDocId(entity: ConsignmentDocumentDetailsEntity?): CorsBakEntity?

    @Query(
        value = "select count(*) as cc from DAT_KEBS_CORS_BAK where to_char(COR_ISSUE_DATE,'YYYY')=:gYear",
        nativeQuery = true
    )
    fun countAllByYearGenerate(gYear: Long): Long
}