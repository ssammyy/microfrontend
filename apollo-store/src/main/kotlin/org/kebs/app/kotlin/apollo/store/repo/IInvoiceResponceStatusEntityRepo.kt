package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.InvoiceResponceStatusEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository

interface IInvoiceResponceStatusEntityRepo : HazelcastRepository<InvoiceResponceStatusEntity, Long> {
    fun findByStatus(status : Int) : InvoiceResponceStatusEntity?
}
