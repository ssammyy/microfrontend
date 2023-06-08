package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.ConsignmentDocumentEntity
import org.kebs.app.kotlin.apollo.store.model.TariffsTypesEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.stereotype.Repository

@Repository
interface ITariffsDetailsRepository : HazelcastRepository<TariffsTypesEntity, Long> {

    fun  findByHscodeTariff(hscodeTariff: String) : TariffsTypesEntity?

}