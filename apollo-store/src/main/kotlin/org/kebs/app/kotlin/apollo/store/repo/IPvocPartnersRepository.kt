package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.PvocPartnersEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.stereotype.Repository

@Repository
interface IPvocPartnersRepository:HazelcastRepository<PvocPartnersEntity,Long> {
    fun findByPartnerRefNoAndStatus(partnerRefNo:String, status:Int): PvocPartnersEntity?
    fun findAllByStatus(status: Int) : List<PvocPartnersEntity>?
}