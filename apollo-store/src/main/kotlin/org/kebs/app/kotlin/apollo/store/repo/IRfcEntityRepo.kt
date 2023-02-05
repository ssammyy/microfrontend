package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.pvc.RfcEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.hazelcast.repository.HazelcastRepository

interface IRfcEntityRepo : HazelcastRepository<RfcEntity, Long> {
    fun findByUcrNumber(ucrNumber: String): RfcEntity?
    fun findByCocNumber(cocNumber: String): RfcEntity?
    fun findByRfcNumberAndStatus(rfcNumber: String, status: Int): RfcEntity?
    fun findFirstByUcrNumberOrderByVersionDesc(ucrNumber: String): RfcEntity?
    fun findFirstByRfcNumberOrderByVersionDesc(rfcNumber: String): RfcEntity?
    fun findByRfcNumberAndVersion(rfcNumber: String, version: Long): RfcEntity?
    fun findByReviewStatus(status: Int, page: Pageable): Page<RfcEntity>
    fun findByRfcNumberContainsAndReviewStatus(keyword: String, status: Int, page: Pageable): Page<RfcEntity>
    fun findByPvocTimelineDataId(pvocTimelineDataId: Long): RfcEntity?

}