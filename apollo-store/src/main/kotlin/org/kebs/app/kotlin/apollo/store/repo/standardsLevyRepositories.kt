package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.Sl2PaymentsDetailsEntity
import org.kebs.app.kotlin.apollo.store.model.Sl2PaymentsHeaderEntity
import org.kebs.app.kotlin.apollo.store.model.SlVisitUploadsEntity
import org.kebs.app.kotlin.apollo.store.model.StandardLevySiteVisitRemarks
import org.kebs.app.kotlin.apollo.store.model.registration.CompanyProfileEntity
import org.kebs.app.kotlin.apollo.store.model.std.NWAPreliminaryDraftUploads
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface ISl2PaymentsHeaderRepository : HazelcastRepository<Sl2PaymentsHeaderEntity, Long>, JpaSpecificationExecutor<Sl2PaymentsHeaderEntity>

interface ISl2PaymentsDetailsRepository : HazelcastRepository<Sl2PaymentsDetailsEntity, Long>, JpaSpecificationExecutor<Sl2PaymentsDetailsEntity>
interface ISlVisitUploadsRepository : HazelcastRepository<SlVisitUploadsEntity, Long>, JpaSpecificationExecutor<SlVisitUploadsEntity> {
    fun findAllByVisitIdOrderById(visitId: Long): List<SlVisitUploadsEntity>
    fun findAllByVisitIdAndDocumentTypeIsNotNullOrderById(visitId: Long): List<SlVisitUploadsEntity>
    fun findByVisitId(id: Long): SlVisitUploadsEntity

}
interface StandardLevySiteVisitRemarksRepository : HazelcastRepository<StandardLevySiteVisitRemarks, Long>, JpaSpecificationExecutor<StandardLevySiteVisitRemarks> {
  fun findAllBySiteVisitId(id: Long): List<StandardLevySiteVisitRemarks>?
}
