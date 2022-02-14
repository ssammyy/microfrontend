package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.pvc.PvocPartnerTypeEntity
import org.kebs.app.kotlin.apollo.store.model.pvc.PvocTimelineConfigurations
import org.kebs.app.kotlin.apollo.store.model.pvc.PvocTimelinePenalties
import org.kebs.app.kotlin.apollo.store.model.pvc.PvocTimelinesDataEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.stereotype.Repository

@Repository
interface IPvocTimelinePenaltiesRepository : HazelcastRepository<PvocTimelinePenalties, Long> {
    fun findByPartnerTypeAndRoute(partnerType: PvocPartnerTypeEntity?, route: String): PvocTimelinePenalties?
    fun findByStatus(status: Int): List<PvocTimelinePenalties>
}

@Repository
interface IPvocTimelinesConfigurationRepository : HazelcastRepository<PvocTimelineConfigurations, Long> {
    fun findByDocumentType(docType: String): PvocTimelineConfigurations?
    fun findByStatus(status: Int): List<PvocTimelineConfigurations>
}

@Repository
interface IPvocTimelinesDataRepository : HazelcastRepository<PvocTimelinesDataEntity, Long> {
    fun findAllByCertNumberNotNull(pageable: Pageable): Page<PvocTimelinesDataEntity>?
    fun findByCertNumberAndCertType(certNumber: String, certType: String): PvocTimelinesDataEntity
    fun findByRecordYearMonthAndPartnerId(yearMonth: String, partnerId: Long): List<PvocTimelinesDataEntity>
}