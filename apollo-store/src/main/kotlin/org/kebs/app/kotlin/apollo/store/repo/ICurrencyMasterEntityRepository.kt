package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.di.DestinationInspectionFeeEntity
import org.kebs.app.kotlin.apollo.store.model.invoice.CurrencyMasterEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository

interface ICurrencyMasterEntityRepository : HazelcastRepository<CurrencyMasterEntity, Long> {
    fun findByStatus(status: Int): List<CurrencyMasterEntity>?
}