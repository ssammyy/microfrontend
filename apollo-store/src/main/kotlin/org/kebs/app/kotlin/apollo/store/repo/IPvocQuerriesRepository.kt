package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.pvc.PvocQueriesEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.stereotype.Repository


@Repository
interface IPvocQuerriesRepository:HazelcastRepository<PvocQueriesEntity,Long> {
    //    fun findByRfcNumberOrCocNumberOrUcrNumberOrInvoiceNumber(rfcNumber:String, cocNumber:String, ucrNumber:String, invoiceNumber:String): Optional<PvocQueriesEntity>
    fun findAllByCertNumber(cocNumber: String): List<PvocQueriesEntity>?
    fun findAllBySerialNumber(cocNumber: String): PvocQueriesEntity?
    fun findByCertNumberOrRfcNumber(cocNumber: String, rfcNumber: String): List<PvocQueriesEntity>?
    fun countAllBySerialNumberStartsWith(prefix: String): Long
    fun findAllByPartnerIdAndConclusionStatus(partnerId: Long, conclusionStatus: Int, pg: Pageable): Page<PvocQueriesEntity>
}