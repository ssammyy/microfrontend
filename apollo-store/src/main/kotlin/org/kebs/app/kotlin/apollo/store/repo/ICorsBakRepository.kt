package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.ConsignmentDocumentEntity
import org.kebs.app.kotlin.apollo.store.model.CorsBakEntity
import org.kebs.app.kotlin.apollo.store.model.di.ConsignmentDocumentDetailsEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.stereotype.Repository

@Repository
interface ICorsBakRepository : HazelcastRepository<CorsBakEntity, Long> {
    fun findByChasisNumber(chasisNumber: String): CorsBakEntity?
    fun findFirstByChasisNumberIsNotNull(): CorsBakEntity?
    fun findByConsignmentDocId(entity: ConsignmentDocumentDetailsEntity?): CorsBakEntity?
}