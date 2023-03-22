package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.SdlFactoryVisitReportsUploadEntity
import org.kebs.app.kotlin.apollo.store.model.SlUpdatecompanyDetailsEntity
import org.kebs.app.kotlin.apollo.store.model.StandardLevyFactoryVisitReportEntity
import org.kebs.app.kotlin.apollo.store.model.StdLevyEntryNoDataMigrationEntity
import org.kebs.app.kotlin.apollo.store.model.registration.CompanyProfileEntity
import org.kebs.app.kotlin.apollo.store.model.std.*
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.sql.Date
import java.time.LocalDate
import java.time.Month

interface IStandardLevyFactoryVisitReportRepository: HazelcastRepository<StandardLevyFactoryVisitReportEntity, Long> {
    fun findByManufacturerEntity(manufacturerEntity: Long): StandardLevyFactoryVisitReportEntity?
    fun findFirstByManufacturerEntityAndStatusOrderByIdDesc(manufacturerEntity: Long, status: Int): StandardLevyFactoryVisitReportEntity?
    fun findBySlProcessInstanceId(slProcessInstanceId: String): StandardLevyFactoryVisitReportEntity?

    @Query(
        value = "SELECT s.PURPOSE AS PURPOSE,s.PERSON_MET AS PERSON,s.ACTION_TAKEN AS ACTIONTAKEN,s.REMARKS AS REMARKS,s.FEED_BACK_REMARKS AS FEEDBACKREMARKS,s.REPORT_REMARKS AS REPORTREMARKS,s.ID AS VISITID,s.VISIT_DATE AS VISITDATE,s.REPORT_DATE AS REPORTDATE,s.CHEIF_MANAGER_REMARKS AS CHIEFMANAGERREMARKS,s.OFFICERS_FEEDBACK as officersFeedback,s.ASSISTANCE_MANAGER_REMARKS AS ASSISTANCEMANAGERREMARKS,c.ID AS COMPANYID,c.NAME AS COMPANYNAME,c.ENTRY_NUMBER AS ENTRYNUMBER,c.KRA_PIN AS KRAPIN,c.REGISTRATION_NUMBER AS REGISTRATIONNUMBER,c.DIRECTOR_ID_NUMBER AS DIRECTORIDNUMBER,c.POSTAL_ADDRESS AS POSTALADDRESS,c.PHYSICAL_ADDRESS AS PHYSICALADDRESS,c.PLOT_NUMBER AS PLOTNUMBER,c.COMPANY_EMAIL AS COMPANYEMAIL,c.COMPANY_TELEPHONE AS COMPANYTELEPHONE,c.YEARLY_TURNOVER AS YEARLYTURNOVER,c.BUSINESS_LINE_NAME AS BUSINESSLINES,c.BUSINESS_NATURE_NAME AS BUSINESSNATURES,c.BRANCH_NAME AS BRANCHNAME,c.STREET_NAME AS STREETNAME,c.STATUS as status,c.OTHER_BUSINESS_NATURE_TYPE as otherBusinessNatureType  FROM DAT_STANDARD_LEVY_FACTORY_VISIT_REPORT s JOIN DAT_KEBS_COMPANY_PROFILE c ON s.MANUFACTURER_ENTITY = c.ID WHERE s.SL_PROCESS_STATUS='1'",
        nativeQuery = true
    )
    fun getCompleteTasks(): List<CompleteTasksDetailHolder>

    @Query(
        value = "SELECT c.NAME AS companyName,c.ENTRY_NUMBER AS entryNumber,c.KRA_PIN AS kraPin," +
                "s.ID as id,s.CREATED_BY as officerName,s.VISIT_DATE AS dateOfVisit,s.PURPOSE AS purpose," +
                "r.REGION AS region,l.NAME as businessLine FROM DAT_STANDARD_LEVY_FACTORY_VISIT_REPORT s " +
                "JOIN DAT_KEBS_COMPANY_PROFILE c ON s.MANUFACTURER_ENTITY = c.ID " +
                "LEFT JOIN CFG_KEBS_REGIONS r ON c.REGION=r.ID LEFT JOIN CFG_KEBS_BUSINESS_LINES l on l.ID=c.BUSINESS_LINES",
        nativeQuery = true
    )
    fun getSiteVisits(): MutableList<SiteVisits>

    @Query(
        value = "SELECT c.NAME AS companyName,c.ENTRY_NUMBER AS entryNumber,c.KRA_PIN AS kraPin," +
                "s.ID as id,s.CREATED_BY as officerName,s.VISIT_DATE AS dateOfVisit,s.PURPOSE AS purpose," +
                "r.REGION AS region,l.NAME as businessLine FROM DAT_STANDARD_LEVY_FACTORY_VISIT_REPORT s " +
                "JOIN DAT_KEBS_COMPANY_PROFILE c ON s.MANUFACTURER_ENTITY = c.ID " +
                "LEFT JOIN CFG_KEBS_REGIONS r ON c.REGION=r.ID LEFT JOIN CFG_KEBS_BUSINESS_LINES l on l.ID=c.BUSINESS_LINES " +
                "WHERE (:startDate is null or s.VISIT_DATE >=TO_DATE(:startDate)) and (:endDate is null or s.VISIT_DATE <=TO_DATE(:endDate)) " +
                "and (:businessLines is null or c.BUSINESS_LINES =TO_NUMBER(:businessLines)) and (:region is null or c.REGION =TO_NUMBER(:region))",
        nativeQuery = true
    )
    fun getSiteVisitsFilter(
        @Param("startDate") startDate: Date?,
        @Param("endDate") endDate: Date?,
        @Param("businessLines") businessLines: Long?,
        @Param("region") region: Long?
    ): MutableList<SiteVisits>



}

interface ISdlFactoryVisitReportsUploadEntityRepository : HazelcastRepository<SdlFactoryVisitReportsUploadEntity, Long>{
    fun findByReportId(reportId: Long) : SdlFactoryVisitReportsUploadEntity?
}

interface ISlUpdatecompanyDetailsEntityRepository : HazelcastRepository<SlUpdatecompanyDetailsEntity, Long>{
    //fun findByReportId(reportId: Long) : SdlFactoryVisitReportsUploadEntity?
}

interface StdLevyEntryNoDataMigrationEntityRepository : HazelcastRepository<StdLevyEntryNoDataMigrationEntity, Long>{
    @Query(value = "SELECT max(ENTRY_COUNT)  FROM SL_ENTRY_NO_DATA_MIGRATION", nativeQuery = true)
    fun getMaxEntryNo(): Long

    @Query(value = "SELECT ENTRY_NUMBERS  FROM SL_ENTRY_NO_DATA_MIGRATION WHERE KRA_PIN=:kraPin", nativeQuery = true)
    fun getEntryNo(@Param("kraPin") kraPin: String?): Long?

}
interface StdLevyHistoricalPaymentsRepository : HazelcastRepository<StdLevyHistoricalPayments, Long>{

    @Query(value = "SELECT *  FROM STANDARD_LEVY_HISTORICAL_PAYMENTS ", nativeQuery = true)
    fun getLevyHistoricalPayments(): MutableList<StdLevyHistoricalPayments>

    @Query(value = "SELECT *  FROM STANDARD_LEVY_HISTORICAL_PAYMENTS WHERE  " +
            " (:periodFrom is null or PERIOD_FROM >=TO_DATE(:periodFrom))  and " +
            "(:periodTo is null or PERIOD_TO >=TO_DATE(:periodTo)) and" +
            "(:company is null or  COMPANY_NAME LIKE  '%'||TO_CHAR(:company)||'%') and" +
            "(:kraPin is null or  KRA_PIN = TO_CHAR(:kraPin))", nativeQuery = true)
    fun getLevyHistoricalPaymentsFilter(
        @Param("periodFrom") periodFrom: Date?,
        @Param("periodTo") periodTo: Date?,
        @Param("company") company: String?,
        @Param("kraPin") kraPin: String?
    ): MutableList<StdLevyHistoricalPayments>


    @Query(value = "SELECT KRA_PIN  FROM STANDARD_LEVY_HISTORICAL_PAYMENTS WHERE KRA_PIN=:kraPin AND EXTRACT(YEAR FROM PERIOD_TO) =:toCheckYear AND EXTRACT(MONTH FROM PERIOD_TO) =:prevMonth   ", nativeQuery = true)
    fun getPaymentStatus(
        @Param("kraPin") kraPin: String?,
        @Param("toCheckYear") toCheckYear: Int?,
        @Param("prevMonth") prevMonth: Int?,

    ): String?

}


