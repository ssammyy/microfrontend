package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.CoisEntity
import org.kebs.app.kotlin.apollo.store.model.LogKraEntryNumberRequestEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.stereotype.Repository

@Repository
interface ILogKraEntryNumberRequestEntityRepository:HazelcastRepository<LogKraEntryNumberRequestEntity, Long> {
    fun findByRequestEntryNumber(requestEntryNumber: String):LogKraEntryNumberRequestEntity?
}