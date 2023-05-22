package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.pvc.RfcCorEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.stereotype.Repository
import java.sql.Date

@Repository
interface IRfcCorRepository : HazelcastRepository<RfcCorEntity,Long> {
    fun findByRfcDateAndPartner(rfcDate: Date, partner: Long): List<RfcCorEntity>?
    fun findByRfcNumberAndVersion(rfcNumber: String, version: Long): RfcCorEntity?
    fun findByRfcNumberAndStatus(rfcNumber: String, status: Long): RfcCorEntity?
    fun findFirstByRfcNumberOrderByVersionDesc(rfcNumber: String): RfcCorEntity?
    fun findFirstByUcrNumberOrderByVersionDesc(ucrNumber: String): RfcCorEntity?
    fun findByReviewStatusAndStatus(reviewStatus: Int, status: Long, page: Pageable): Page<RfcCorEntity>
    fun findByRfcNumberContainsOrChassisNumberContains(
        keywords: String,
        keywords2: String,
        page: Pageable
    ): Page<RfcCorEntity>
}