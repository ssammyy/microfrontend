package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.pvc.RfcEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.hazelcast.repository.HazelcastRepository

interface IRfcEntityRepo : HazelcastRepository<RfcEntity, Long> {
    fun findByUcrNumber(ucrNumber: String): RfcEntity?
    fun findByCocNumber(cocNumber: String): RfcEntity?
    fun findByRfcNumber(rfcNumber: String): RfcEntity?
    fun findByRfcDocumentTypeAndReviewStatus(docType: String, status: Int, page: Pageable): Page<RfcEntity>
    fun findByPvocTimelineDataId(pvocTimelineDataId: Long): RfcEntity?

}