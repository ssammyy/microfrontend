package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.CoisEntity
import org.kebs.app.kotlin.apollo.store.model.KraEntryNumberRequestLogEntity
import org.kebs.app.kotlin.apollo.store.model.LogKraEntryNumberRequestEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface ILogKraEntryNumberRequestEntityRepository:HazelcastRepository<LogKraEntryNumberRequestEntity, Long> {
    fun findByRequestEntryNumber(requestEntryNumber: String):LogKraEntryNumberRequestEntity?
}

@Repository
interface IKraEntryNumberRequestLogEntityRepository:HazelcastRepository<KraEntryNumberRequestLogEntity, Long> {
    fun findByRequestEntryNumber(requestEntryNumber: String):KraEntryNumberRequestLogEntity?

    @Query("SELECT RESPONSE_RESPONSE_CODE FROM DAT_KEBS_LOG_KRA_ENTRY_NUMBER_REQUEST  WHERE REQUEST_ENTRY_NUMBER=:requestEntryNumber", nativeQuery = true)
    fun findKraResponseCode(requestEntryNumber: String): String

}