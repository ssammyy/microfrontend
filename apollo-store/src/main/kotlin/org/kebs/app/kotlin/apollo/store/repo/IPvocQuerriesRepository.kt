package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.PvocQueriesEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.stereotype.Repository
import java.util.*


@Repository
interface IPvocQuerriesRepository:HazelcastRepository<PvocQueriesEntity,Long> {
    fun findByRfcNumberOrCocNumberOrUcrNumberOrInvoiceNumber(rfcNumber:String, cocNumber:String, ucrNumber:String, invoiceNumber:String): Optional<PvocQueriesEntity>
    fun findAllByCocNumber(cocNumber: String) : List<PvocQueriesEntity>?
}