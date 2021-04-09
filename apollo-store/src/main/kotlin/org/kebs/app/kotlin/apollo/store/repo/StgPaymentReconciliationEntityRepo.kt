package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.StagingPaymentReconciliation
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.stereotype.Repository

@Repository
interface StgPaymentReconciliationEntityRepo : HazelcastRepository<StagingPaymentReconciliation, Long> {
    fun findByInvoiceId(invoiceId: Long) : StagingPaymentReconciliation?
}

