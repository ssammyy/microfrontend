package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.di.CdImporterDetailsEntity
import org.kebs.app.kotlin.apollo.store.model.ImporterContactDetailsEntity
import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.stereotype.Repository

@Repository
interface IImporterContactRepository  : HazelcastRepository<ImporterContactDetailsEntity, Long> {
    fun findByUserIdAndStatus(userId: UsersEntity, status: Int): ImporterContactDetailsEntity?
}

@Repository
interface IImporterRepository  : HazelcastRepository<CdImporterDetailsEntity, Long> {


}