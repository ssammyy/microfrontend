package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.PvocQueriesDataEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface IPvocQueriesDataRepository : HazelcastRepository<PvocQueriesDataEntity, Long> {
    fun findByRfcNumberOrCocNumberOrUcrNumberOrInvoiceNumber(rfcNumber: String, cocNumber: String, ucrNumber: String, invoiceNumber: String): List<PvocQueriesDataEntity>?
}