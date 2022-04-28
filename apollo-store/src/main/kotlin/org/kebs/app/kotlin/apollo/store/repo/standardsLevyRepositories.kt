package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.Sl2PaymentsDetailsEntity
import org.kebs.app.kotlin.apollo.store.model.Sl2PaymentsHeaderEntity
import org.kebs.app.kotlin.apollo.store.model.SlVisitUploadsEntity
import org.kebs.app.kotlin.apollo.store.model.StandardLevySiteVisitRemarks
import org.kebs.app.kotlin.apollo.store.model.std.SiteVisitListHolder
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ISl2PaymentsHeaderRepository : HazelcastRepository<Sl2PaymentsHeaderEntity, Long>, JpaSpecificationExecutor<Sl2PaymentsHeaderEntity>

interface ISl2PaymentsDetailsRepository : HazelcastRepository<Sl2PaymentsDetailsEntity, Long>, JpaSpecificationExecutor<Sl2PaymentsDetailsEntity>
interface ISlVisitUploadsRepository : HazelcastRepository<SlVisitUploadsEntity, Long>, JpaSpecificationExecutor<SlVisitUploadsEntity> {
    fun findAllByVisitIdOrderById(visitId: Long): List<SlVisitUploadsEntity>
    fun findAllByVisitIdAndDocumentTypeIsNotNullOrderById(visitId: Long): List<SlVisitUploadsEntity>
    fun findAllById(id: Long): SlVisitUploadsEntity

    @Query(
        value = "SELECT s.ID as id  FROM DAT_KEBS_SL_VISIT_UPLOADS s WHERE s.VISIT_ID= :id ",
        nativeQuery = true
    )
    fun findAllDocumentId(@Param("id") id: Long?): List<SiteVisitListHolder>

}
interface StandardLevySiteVisitRemarksRepository : HazelcastRepository<StandardLevySiteVisitRemarks, Long>, JpaSpecificationExecutor<StandardLevySiteVisitRemarks> {
  fun findAllBySiteVisitIdOrderByIdDesc(id: Long): List<StandardLevySiteVisitRemarks>?
}
