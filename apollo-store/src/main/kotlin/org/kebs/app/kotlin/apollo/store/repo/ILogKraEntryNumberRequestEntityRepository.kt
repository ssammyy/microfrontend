package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.model.std.PenaltyDetails
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

    @Query("SELECT * FROM DAT_KEBS_LOG_KRA_ENTRY_NUMBER_REQUEST  WHERE STATUS=0", nativeQuery = true)
    fun getEntryNumberDetails(): MutableList<KraEntryNumberRequestLogEntity>

}

@Repository
interface IKraPaymentDetailsRequestLogEntityRepository:HazelcastRepository<KraPaymentDetailsRequestLogEntity, Long> {

}

@Repository
interface IKraPenaltyDetailsRequestLogEntityRepository:HazelcastRepository<KraPenaltyDetailsRequestLogEntity, Long> {

}