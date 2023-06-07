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

interface ISl2PaymentsHeaderRepository : HazelcastRepository<Sl2PaymentsHeaderEntity, Long>, JpaSpecificationExecutor<Sl2PaymentsHeaderEntity>{
    @Query(
        value = "SELECT DISTINCT c.ENTRY_NUMBER as entryNumber,c.ID as companyId,c.NAME as companyName,c.KRA_PIN as kraPin,c.REGISTRATION_NUMBER as registrationNumber,p.STATUS as status  FROM DAT_KEBS_COMPANY_PROFILE c, LOG_SL2_PAYMENTS_HEADER p\n" +
                "WHERE p.REQUEST_HEADER_ENTRY_NO=c.ENTRY_NUMBER AND p.STATUS='1'",
        nativeQuery = true
    )
    fun getLevyPenalty(): MutableList<LevyPenalty>

    @Query(
        value = "SELECT p.ID as id,p.REQUEST_HEADER_ENTRY_NO as entryNumber,p.TRANSACTION_DATE as paymentDate,p.REQUEST_HEADER_TOTAL_PAYMENT_AMT as paymentAmount,p.REQUEST_HEADER_TOTAL_PENALTY_AMT as amountDue,p.REQUEST_HEADER_TOTAL_PENALTY_AMT as penalty,p.REQUEST_HEADER_PAYMENT_SLIP_DATE as levyDueDate," +
                "c.ID as companyId,c.NAME as companyName,c.KRA_PIN as kraPin,c.REGISTRATION_NUMBER as registrationNumber,c.ASSIGN_STATUS as assignStatus " +
                " FROM LOG_SL2_PAYMENTS_HEADER p JOIN DAT_KEBS_COMPANY_PROFILE c ON p.REQUEST_HEADER_ENTRY_NO=c.ENTRY_NUMBER  " +
                "WHERE p.REQUEST_HEADER_ENTRY_NO= :entryNumber ORDER BY p.ID DESC",
        nativeQuery = true
    )
    fun getManufacturesLevyPenalty(@Param("entryNumber") entryNumber: Long?): MutableList<LevyPenalty>
}

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

    @Query(
        value = "SELECT ID as id,SITE_VISIT_ID as siteVisitId,REMARKS as remarks,REMARK_BY as remarkBy,STATUS as status," +
                "ROLE as role,DESCRIPTION as description,cast(DATE_OF_REMARK as varchar(200)) AS dateOfRemark FROM DAT_KEBS_SITE_VISIT_REMARKS  WHERE SITE_VISIT_ID= :id ",
        nativeQuery = true
    )
    fun findCompanyRemarks(@Param("id") id: Long?): List<CompanyRemarks>
}

interface StandardLevyOperationsClosureRepository : HazelcastRepository<StandardLevyOperationsClosure, Long> {
    @Query(value = "SELECT c.NAME as companyName,c.ENTRY_NUMBER as entryNumber,c.KRA_PIN as kraPin,c.REGISTRATION_NUMBER as registrationNumber," +
            "o.ID as id,o.COMPANY_ID as companyId,o.REASON as reason,o.STATUS as status,o.DESCRIPTION as description," +
            "cast(o.DATE_OF_CLOSURE as varchar(200)) AS dateOfClosure FROM DAT_KEBS_CLOSURE_OF_OPERATIONS o JOIN DAT_KEBS_COMPANY_PROFILE c ON o.COMPANY_ID=c.ID WHERE  o.STATUS='0'", nativeQuery = true)
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
