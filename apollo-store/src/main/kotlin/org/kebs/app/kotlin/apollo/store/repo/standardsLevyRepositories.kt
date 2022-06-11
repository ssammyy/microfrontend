package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.model.registration.CompanyProfileEntity
import org.kebs.app.kotlin.apollo.store.model.Sl2PaymentsDetailsEntity
import org.kebs.app.kotlin.apollo.store.model.Sl2PaymentsHeaderEntity
import org.kebs.app.kotlin.apollo.store.model.SlVisitUploadsEntity
import org.kebs.app.kotlin.apollo.store.model.StandardLevySiteVisitRemarks
import org.kebs.app.kotlin.apollo.store.model.std.*
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
interface StandardLevySiteVisitRemarksRepository : HazelcastRepository<StandardLevySiteVisitRemarks, Long> {
  fun findAllBySiteVisitIdOrderByIdDesc(id: Long): List<StandardLevySiteVisitRemarks>?
}

interface StandardLevyOperationsClosureRepository : HazelcastRepository<StandardLevyOperationsClosure, Long> {
    @Query(value = "SELECT c.NAME as companyName,c.ENTRY_NUMBER as entryNumber,c.KRA_PIN as kraPin,c.REGISTRATION_NUMBER as registrationNumber,o.ID as id,o.COMPANY_ID as companyId,o.REASON as reason,o.STATUS as status,o.DESCRIPTION as description,o.DATE_OF_CLOSURE as dateOfClosure FROM DAT_KEBS_CLOSURE_OF_OPERATIONS o JOIN DAT_KEBS_COMPANY_PROFILE c ON o.COMPANY_ID=c.ID WHERE  o.STATUS='0'", nativeQuery = true)
    fun getCompanyClosureRequest(): MutableList<CompanyClosureList>
}

interface StandardLevyOperationsSuspensionRepository : HazelcastRepository<StandardLevyOperationsSuspension, Long> {

    @Query(value = "SELECT  c.NAME as companyName,c.ENTRY_NUMBER as entryNumber,c.KRA_PIN as kraPin,c.REGISTRATION_NUMBER as registrationNumber,s.ID as id,s.COMPANY_ID as companyId,s.REASON as reason,s.STATUS as status,s.DESCRIPTION as description,s.DATE_OF_SUSPENSION as dateOfSuspension FROM DAT_KEBS_SUSPENSION_OF_OPERATIONS s JOIN DAT_KEBS_COMPANY_PROFILE c ON s.COMPANY_ID=c.ID WHERE  s.STATUS IN('0','2')", nativeQuery = true)
    fun getCompanySuspensionRequest(): MutableList<CompanySuspensionList>
}

interface SlWindingUpReportUploadsEntityRepository : HazelcastRepository<SlWindingUpReportUploadsEntity, Long> {
    fun findAllById(id: Long): SlWindingUpReportUploadsEntity

    @Query(
        value = "SELECT w.ID as id  FROM DAT_KEBS_WU_REPORT_UPLOADS w WHERE w.CLOSURE_ID= :id ",
        nativeQuery = true
    )
    fun findAllDocumentId(@Param("id") id: Long?): List<WindingUpReportListHolder>
}
