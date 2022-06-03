package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.SdlFactoryVisitReportsUploadEntity
import org.kebs.app.kotlin.apollo.store.model.SlUpdatecompanyDetailsEntity
import org.kebs.app.kotlin.apollo.store.model.StandardLevyFactoryVisitReportEntity
import org.kebs.app.kotlin.apollo.store.model.registration.CompanyProfileEntity
import org.kebs.app.kotlin.apollo.store.model.std.CompleteTasksDetailHolder
import org.kebs.app.kotlin.apollo.store.model.std.UserDetailHolder
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface IStandardLevyFactoryVisitReportRepository: HazelcastRepository<StandardLevyFactoryVisitReportEntity, Long> {
    fun findByManufacturerEntity(manufacturerEntity: Long): StandardLevyFactoryVisitReportEntity?
    fun findFirstByManufacturerEntityAndStatusOrderByIdDesc(manufacturerEntity: Long, status: Int): StandardLevyFactoryVisitReportEntity?
    fun findBySlProcessInstanceId(slProcessInstanceId: String): StandardLevyFactoryVisitReportEntity?

    @Query(
        value = "SELECT s.PURPOSE AS PURPOSE,s.PERSON_MET AS PERSON,s.ACTION_TAKEN AS ACTIONTAKEN,s.REMARKS AS REMARKS,s.FEED_BACK_REMARKS AS FEEDBACKREMARKS,s.REPORT_REMARKS AS REPORTREMARKS,s.ID AS VISITID,s.VISIT_DATE AS VISITDATE,s.REPORT_DATE AS REPORTDATE,s.CHEIF_MANAGER_REMARKS AS CHIEFMANAGERREMARKS,s.OFFICERS_FEEDBACK as officersFeedback,s.ASSISTANCE_MANAGER_REMARKS AS ASSISTANCEMANAGERREMARKS,c.ID AS COMPANYID,c.NAME AS COMPANYNAME,c.ENTRY_NUMBER AS ENTRYNUMBER,c.KRA_PIN AS KRAPIN,c.REGISTRATION_NUMBER AS REGISTRATIONNUMBER,c.DIRECTOR_ID_NUMBER AS DIRECTORIDNUMBER,c.POSTAL_ADDRESS AS POSTALADDRESS,c.PHYSICAL_ADDRESS AS PHYSICALADDRESS,c.PLOT_NUMBER AS PLOTNUMBER,c.COMPANY_EMAIL AS COMPANYEMAIL,c.COMPANY_TELEPHONE AS COMPANYTELEPHONE,c.YEARLY_TURNOVER AS YEARLYTURNOVER,c.BUSINESS_LINE_NAME AS BUSINESSLINES,c.BUSINESS_NATURE_NAME AS BUSINESSNATURES,c.BRANCH_NAME AS BRANCHNAME,c.STREET_NAME AS STREETNAME,c.STATUS as status,c.OTHER_BUSINESS_NATURE_TYPE as otherBusinessNatureType  FROM DAT_STANDARD_LEVY_FACTORY_VISIT_REPORT s JOIN DAT_KEBS_COMPANY_PROFILE c ON s.MANUFACTURER_ENTITY = c.ID WHERE s.SL_PROCESS_STATUS='1'",
        nativeQuery = true
    )
    fun getCompleteTasks(): List<CompleteTasksDetailHolder>

}

interface ISdlFactoryVisitReportsUploadEntityRepository : HazelcastRepository<SdlFactoryVisitReportsUploadEntity, Long>{
    fun findByReportId(reportId: Long) : SdlFactoryVisitReportsUploadEntity?
}

interface ISlUpdatecompanyDetailsEntityRepository : HazelcastRepository<SlUpdatecompanyDetailsEntity, Long>{
    //fun findByReportId(reportId: Long) : SdlFactoryVisitReportsUploadEntity?
}

