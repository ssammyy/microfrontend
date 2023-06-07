package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.invoice.PvocInvoicingEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.stereotype.Repository
import java.sql.Date

@Repository
interface IPvocInvoicingRepository:HazelcastRepository<PvocInvoicingEntity, Long> {
    fun findByInvoiceDateAndPartner(invoiceDate: Date, partner:Long):List<PvocInvoicingEntity>
    fun findFirstByPartner(partner: Long) : PvocInvoicingEntity?
    fun findByReconcialitionId(reconcialitionId: Long) : PvocInvoicingEntity?

}