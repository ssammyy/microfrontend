package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.pvc.PvocPartnersCountriesEntity
import org.kebs.app.kotlin.apollo.store.model.pvc.PvocPartnersEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface IPvocPartnersRepository : HazelcastRepository<PvocPartnersEntity, Long> {
    fun findByPartnerRefNoAndStatus(partnerRefNo: String, status: Int): PvocPartnersEntity?
    fun findAllByPartnerCountryAndStatus(
        partnerCountry: PvocPartnersCountriesEntity,
        status: Int
    ): List<PvocPartnersEntity>

    fun findByPartnerRefNo(partnerRefNo: String?): Optional<PvocPartnersEntity>
    fun findByPartnerRefNoContains(partnerRefNo: String?, page: Pageable): Page<PvocPartnersEntity>
    fun findAllByStatus(status: Int): List<PvocPartnersEntity>
    fun findByApiClientId(clientId: Long): PvocPartnersEntity?
}