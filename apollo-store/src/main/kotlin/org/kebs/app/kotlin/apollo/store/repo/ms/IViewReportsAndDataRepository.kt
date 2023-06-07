/*
 *
 *  *
 *  *
 *  *  *    Copyright (c) ${YEAR}.   BSK Global Technologies
 *  *  *
 *  *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *    you may not use this file except in compliance with the License.
 *  *  *    You may obtain a copy of the License at
 *  *  *
 *  *  *       http://www.apache.org/licenses/LICENSE-2.0
 *  *  *
 *  *  *    Unless required by applicable law or agreed to in writing, software
 *  *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *   See the License for the specific language governing permissions and
 *  *  *   limitations under the License.
 *  *
 *
 */

package org.kebs.app.kotlin.apollo.store.repo.ms

import mu.KotlinLogging
import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.model.ms.*
import org.kebs.app.kotlin.apollo.store.model.qa.PermitApplicationsEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ISampleCollectionViewRepository : HazelcastRepository<MsSampleCollectionView, Long> {
    override fun findAll(pageable: Pageable): Page<MsSampleCollectionView>

    fun findBySampleCollectionId(sampleCollectionId: Long): List<MsSampleCollectionView>
}

@Repository
interface IMsFieldReportViewRepository : HazelcastRepository<MsFieldReportView, String> {
    override fun findAll(pageable: Pageable): Page<MsFieldReportView>


    @Query(
        value = "SELECT  nvl(TO_CHAR(rep.MS_WORKPLAN_GENERATED_ID),'N/A') AS MS_WORKPLAN_GENERATED_ID,\n" +
                "        nvl(TO_CHAR(rep.CREATED_USER_ID),'N/A') AS CREATED_USER_ID,\n" +
                "        nvl(TO_CHAR(rep.ID),'N/A') AS ID,\n" +
                "        nvl(rep.REPORT_REFERENCE,'N/A') AS REPORT_REFERENCE,\n" +
                "        nvl(rep.REPORT_TO,'N/A') AS REPORT_TO,\n" +
                "        nvl(rep.REPORT_THROUGH,'N/A') AS REPORT_THROUGH,\n" +
                "        nvl(rep.REPORT_FROM,'N/A') AS REPORT_FROM,\n" +
                "        nvl(rep.REPORT_SUBJECT,'N/A') AS REPORT_SUBJECT,\n" +
                "        nvl(rep.REPORT_TITLE,'N/A') AS REPORT_TITLE,\n" +
                "        nvl(TO_CHAR(TRUNC(rep.REPORT_DATE),'DD/MM/YYYY'),'N/A') AS REPORT_DATE,\n" +
                "        nvl(rep.REPORT_REGION,'N/A') AS REPORT_REGION,\n" +
                "        nvl(rep.REPORT_DEPARTMENT,'N/A') AS REPORT_DEPARTMENT,\n" +
                "        nvl(rep.REPORT_FUNCTION,'N/A') AS REPORT_FUNCTION,\n" +
                "        nvl(rep.BACKGROUND_INFORMATION,'N/A') AS BACKGROUND_INFORMATION,\n" +
                "        nvl(rep.OBJECTIVE_INVESTIGATION,'N/A') AS OBJECTIVE_INVESTIGATION,\n" +
                "        nvl(TO_CHAR(TRUNC(rep.START_DATE_INVESTIGATION_INSPECTION),'DD/MM/YYYY'),'N/A') AS START_DATE_INVESTIGATION_INSPECTION,\n" +
                "        nvl(TO_CHAR(TRUNC(rep.END_DATE_INVESTIGATION_INSPECTION),'DD/MM/YYYY'),'N/A') AS END_DATE_INVESTIGATION_INSPECTION,\n" +
                "        nvl(rep.METHODOLOGY_EMPLOYED,'N/A') AS METHODOLOGY_EMPLOYED,\n" +
                "        nvl(rep.SUMMARY_OF_FINDINGS,'N/A') AS SUMMARY_OF_FINDINGS,\n" +
                "        nvl(rep.ADDITIONAL_INFORMATION,'N/A') AS ADDITIONAL_INFORMATION,\n" +
                "        nvl(rep.CONCLUSION,'N/A') AS CONCLUSION,\n" +
                "        nvl(rep.RECOMMENDATIONS,'N/A') AS RECOMMENDATIONS,\n" +
                "        nvl(rep.STATUS_ACTIVITY,'N/A') AS STATUS_ACTIVITY,\n" +
                "        nvl(rep.FINAL_REMARK_HOD,'N/A') AS FINAL_REMARK_HOD,\n" +
                "        nvl(rep.KEBS_INSPECTORS,'N/A') AS KEBS_INSPECTORS,\n" +
                "        nvl(rep.VAR_FIELD_1,'N/A') AS VAR_FIELD_1,\n" +
                "        nvl(TO_CHAR(TRUNC(rep.CREATED_ON),'DD/MM/YYYY'),'N/A') AS CREATED_ON,\n" +
                "        nvl(rep.CREATED_BY,'N/A') AS CREATED_BY,\n" +
                "        nvl(rep.MODIFIED_BY,'N/A') AS MODIFIED_BY,\n" +
                "        nvl(TO_CHAR(TRUNC(rep.MODIFIED_ON),'DD/MM/YYYY'),'N/A') AS MODIFIED_ON,\n" +
                "        nvl(rep.REPORT_CLASSIFICATION,'N/A') AS REPORT_CLASSIFICATION,\n" +
                "       nvl(rep.CHANGES_MADE,'N/A') AS CHANGES_MADE \n" +
                "FROM DAT_KEBS_MS_INSPECTION_INVESTIGATION_REPORT rep WHERE TO_CHAR(rep.MS_WORKPLAN_GENERATED_ID)=:msWorkplanGeneratedId",
        nativeQuery = true
    )
    fun findMsWorkplanGeneratedId(@Param("msWorkplanGeneratedId") msWorkplanGeneratedId: String): List<MsFieldReportView>

    fun findByMsWorkplanGeneratedId(msWorkPlanGeneratedId: String): List<MsFieldReportView>
}

@Repository
interface IOutletVisitedAndSummaryOfFindingsViewRepository : HazelcastRepository<OutletVisitedAndSummaryOfFindingsViewEntity, String> {
    override fun findAll(pageable: Pageable): Page<OutletVisitedAndSummaryOfFindingsViewEntity>

    fun findByMsWorkplanGeneratedId(msWorkplanGeneratedId: String): List<OutletVisitedAndSummaryOfFindingsViewEntity>

    @Query(
        value = "SELECT nvl(TO_CHAR(dat.INSPECTION_DATE),'N/A') AS INSPECTION_DATE,\n" +
                "       nvl(TO_CHAR(dat.OUTLET_NAME),'N/A') AS OUTLET_NAME,\n" +
                "       nvl(TO_CHAR(dat.OUTLET_DETAILS), 'N/A') AS OUTLET_DETAILS,\n" +
                "       nvl(TO_CHAR(dat.PHONE_NUMBER), 'N/A') AS PHONE_NUMBER,\n" +
                "       nvl(TO_CHAR(dat.EMAIL_ADDRESS), 'N/A') AS EMAIL_ADDRESS,\n" +
                "       nvl(TO_CHAR(dat.PERSON_MET), 'N/A') AS PERSON_MET,\n" +
                "       nvl(TO_CHAR(dat.SUMMARY_FINDINGS_ACTIONS_TAKEN), 'N/A') AS SUMMARY_FINDINGS_ACTIONS_TAKEN,\n" +
                "       nvl(TO_CHAR(dat.REMARKS), 'N/A') AS REMARKS,\n" +
                "       nvl(TO_CHAR(dat.MS_WORKPLAN_GENERATED_ID), 'N/A') AS MS_WORKPLAN_GENERATED_ID,\n" +
                "       nvl(TO_CHAR(dat.ID), 'N/A') AS ID\n" +
                "FROM APOLLO.DAT_KEBS_MS_DATA_REPORT dat WHERE TO_CHAR(dat.MS_WORKPLAN_GENERATED_ID)=:msWorkplanGeneratedId",
        nativeQuery = true
    )
    fun findMsWorkplanGeneratedId(@Param("msWorkplanGeneratedId") msWorkplanGeneratedId: String): List<OutletVisitedAndSummaryOfFindingsViewEntity>
}

@Repository
interface ISummaryOfSamplesDrawnViewRepository : HazelcastRepository<SummaryOfSamplesDrawnViewEntity, String> {
    override fun findAll(pageable: Pageable): Page<SummaryOfSamplesDrawnViewEntity>

    fun findByMsWorkplanGeneratedId(msWorkplanGeneratedId: String): List<SummaryOfSamplesDrawnViewEntity>

    @Query(
        value = "SELECT nvl(TO_CHAR(szd.FILE_REF_NUMBER),'N/A') AS FILE_REF_NUMBER,\n" +
                "       nvl(TO_CHAR(a.OUTLET_NAME),'N/A') AS OUTLET_NAME,\n" +
                "       nvl(TO_CHAR(szd.DATA_REPORT_ID),'N/A') AS DATA_REPORT_ID,\n" +
                "       nvl(TO_CHAR(szd.NAME_PRODUCT), 'N/A') AS PRODUCT_NAME,\n" +
                "       nvl(TO_CHAR(szd.LB_ID_TRADE_MARK), 'N/A') AS PRODUCT_BRAND,\n" +
                "       nvl(TO_CHAR(szd.ADDRESS), 'N/A') AS ADDRESS,\n" +
                "       nvl(TO_CHAR(szd.COUNTRY_OF_ORIGIN), 'N/A') AS COUNTRY_OF_ORIGIN,\n" +
                "       nvl(TO_CHAR(szd.LB_ID_EXPIRY_DATE), 'N/A') AS EXPIRY_DATE,\n" +
                "       nvl(TO_CHAR(szd.LB_ID_BATCH_NO), 'N/A') AS BATCH_NUMBER,\n" +
                "       nvl(TO_CHAR(szd.MS_WORKPLAN_GENERATED_ID), 'N/A') AS MS_WORKPLAN_GENERATED_ID,\n" +
                "       nvl(TO_CHAR(szd.SENDERS_NAME), 'N/A') AS SENDERS_NAME,\n" +
                "       nvl(TO_CHAR(szd.ID), 'N/A') AS ID,\n" +
                "       nvl(TO_CHAR(szd.SAMPLE_COLLECTION_DATE), 'N/A') AS SAMPLE_COLLECTION_DATE,\n" +
                "       nvl(TO_CHAR(szd.SENDERS_DATE), 'N/A') AS DATE_SUBMITTED\n" +
                "FROM APOLLO.DAT_KEBS_MS_SAMPLE_SUBMISSION szd\n" +
                "JOIN APOLLO.DAT_KEBS_MS_DATA_REPORT a ON szd.DATA_REPORT_ID = a.ID WHERE TO_CHAR(szd.MS_WORKPLAN_GENERATED_ID)=:msWorkplanGeneratedId",
        nativeQuery = true
    )
    fun findMsWorkplanGeneratedId(@Param("msWorkplanGeneratedId") msWorkplanGeneratedId: String): List<SummaryOfSamplesDrawnViewEntity>
}


@Repository
interface IMsPerformanceOfSelectedProductViewRepository : HazelcastRepository<MsPerformanceOfSelectedProductViewEntity, String>, JpaSpecificationExecutor<MsPerformanceOfSelectedProductViewEntity> {
    override fun findAll(pageable: Pageable): Page<MsPerformanceOfSelectedProductViewEntity>

    fun findAllByResultsAnalysisIsNotNull(pageable: Pageable): Page<MsPerformanceOfSelectedProductViewEntity>

    fun findByResultsAnalysisIsNotNull(): List<MsPerformanceOfSelectedProductViewEntity>
}

@Repository
interface IMsSeizedGoodsViewRepository : HazelcastRepository<MsSeizedGoodsViewEntity, String>, JpaSpecificationExecutor<MsSeizedGoodsViewEntity> {
    override fun findAll(pageable: Pageable): Page<MsSeizedGoodsViewEntity>

//    fun findAllByResultsAnalysisIsNotNull(pageable: Pageable): Page<MsSeizedGoodsViewEntity>
//
//    fun findByResultsAnalysisIsNotNull(): List<MsSeizedGoodsViewEntity>
}

@Repository
interface IMsComplaintsInvestigationsViewRepository : HazelcastRepository<MsComplaintsInvestigationsViewEntity, String> {
    override fun findAll(pageable: Pageable): Page<MsComplaintsInvestigationsViewEntity>

//    fun findByMsWorkplanGeneratedId(msWorkPlanGeneratedId: String): List<MsPerformanceOfSelectedProductViewEntity>
}

@Repository
interface IMsSampleSubmissionViewRepository : HazelcastRepository<MsSampleSubmissionView, Long> {
    override fun findAll(pageable: Pageable): Page<MsSampleSubmissionView>

    fun findAllById(id: String): List<MsSampleSubmissionView>
}

@Repository
interface IMsAcknowledgementTimelineViewRepository : HazelcastRepository<MsAcknowledgementTimelineViewEntity, Long> {
    override fun findAll(pageable: Pageable): Page<MsAcknowledgementTimelineViewEntity>

    fun findAllByReferenceNumber(referenceNumber: String): List<MsAcknowledgementTimelineViewEntity>
}

@Repository
interface IConsumerComplaintsReportViewRepository : HazelcastRepository<ConsumerComplaintsReportViewEntity, Long> {
    override fun findAll(pageable: Pageable): Page<ConsumerComplaintsReportViewEntity>

    fun findAllByReferenceNumber(referenceNumber: String): List<ConsumerComplaintsReportViewEntity>

    @Query(
        value = "SELECT a.* from APOLLO.CONSUMER_COMPLAINTS_REPORT_VIEW a where\n" +
                "    (:startDate is null or a.TRANSACTION_DATE >=TO_DATE(:startDate)) and (:endDate is null or a.TRANSACTION_DATE <=TO_DATE(:endDate))\n" +
                "    and (:assignIO is null or a.ASSIGNED_IO =TO_NUMBER(:assignIO)) and\n" +
                "    a.ASSIGNED_IO IN :selectedOfficers" +
                "    and (:refNumber is null or a.REFERENCE_NUMBER =:refNumber) and\n" +
                "   (:sectorID is null or a.COMPLAINT_DEPARTMENT =TO_NUMBER(:sectorID)) and\n" +
                "   (:departmentID is null or a.DIVISION =:departmentID) and\n" +
                "    a.DIVISION IN :selectedDivisions and\n" +
                "   (:regionID is null or a.REGION =:regionID)",

        nativeQuery = true
    )

    fun findFilteredConsumerComplaintReport(
        @Param("startDate") startDate: Date?,
        @Param("endDate") endDate: Date?,
        @Param("assignIO") assignIO: Long?,
        @Param("selectedOfficers") selectedOfficers: Array<Long>?,
        @Param("refNumber") refNumber: String?,
        @Param("sectorID") sectorID: Long?,
        @Param("departmentID") departmentID: String?,
        @Param("selectedDivisions") selectedDivisions: Array<String>?,
        @Param("regionID") regionID: String?
    ): List<ConsumerComplaintsReportViewEntity>?
    @Query(
        value = "SELECT a.* from APOLLO.CONSUMER_COMPLAINTS_REPORT_VIEW a where\n" +
                "    (:startDate is null or a.TRANSACTION_DATE >=TO_DATE(:startDate)) and (:endDate is null or a.TRANSACTION_DATE <=TO_DATE(:endDate))\n" +
                "    and (:assignIO is null or a.ASSIGNED_IO =TO_NUMBER(:assignIO)) and\n" +
                "    a.ASSIGNED_IO IN :selectedOfficers" +
                "    and (:refNumber is null or a.REFERENCE_NUMBER =:refNumber) and\n" +
                "   (:sectorID is null or a.COMPLAINT_DEPARTMENT =TO_NUMBER(:sectorID)) and\n" +
                "   (:departmentID is null or a.DIVISION =:departmentID) and\n" +
                "   (:regionID is null or a.REGION =:regionID)",

        nativeQuery = true
    )
    fun findFilteredConsumerComplaintReportWithSelectedOfficer(
        @Param("startDate") startDate: Date?,
        @Param("endDate") endDate: Date?,
        @Param("assignIO") assignIO: Long?,
        @Param("selectedOfficers") selectedOfficers: Array<Long>?,
        @Param("refNumber") refNumber: String?,
        @Param("sectorID") sectorID: Long?,
        @Param("departmentID") departmentID: String?,
        @Param("regionID") regionID: String?
    ): List<ConsumerComplaintsReportViewEntity>?

    @Query(
        value = "SELECT a.* from APOLLO.CONSUMER_COMPLAINTS_REPORT_VIEW a where\n" +
                "    (:startDate is null or a.TRANSACTION_DATE >=TO_DATE(:startDate)) and (:endDate is null or a.TRANSACTION_DATE <=TO_DATE(:endDate))\n" +
                "    and (:assignIO is null or a.ASSIGNED_IO =TO_NUMBER(:assignIO)) and\n" +
                "     (:refNumber is null or a.REFERENCE_NUMBER =:refNumber) and\n" +
                "   (:sectorID is null or a.COMPLAINT_DEPARTMENT =TO_NUMBER(:sectorID)) and\n" +
                "   (:departmentID is null or a.DIVISION =:departmentID) and\n" +
                "    a.DIVISION IN :selectedDivisions and\n" +
                "   (:regionID is null or a.REGION =:regionID)",

        nativeQuery = true
    )
    fun findFilteredConsumerComplaintReportWithSelectedDivisions(
        @Param("startDate") startDate: Date?,
        @Param("endDate") endDate: Date?,
        @Param("assignIO") assignIO: Long?,
        @Param("refNumber") refNumber: String?,
        @Param("sectorID") sectorID: Long?,
        @Param("departmentID") departmentID: String?,
        @Param("selectedDivisions") selectedDivisions: Array<String>?,
        @Param("regionID") regionID: String?
    ): List<ConsumerComplaintsReportViewEntity>?

    @Query(
        value = "SELECT a.* from APOLLO.CONSUMER_COMPLAINTS_REPORT_VIEW a where\n" +
                "    (:startDate is null or a.TRANSACTION_DATE >=TO_DATE(:startDate)) and (:endDate is null or a.TRANSACTION_DATE <=TO_DATE(:endDate))\n" +
                "    and (:assignIO is null or a.ASSIGNED_IO =TO_NUMBER(:assignIO)) and\n" +
                "    (:refNumber is null or a.REFERENCE_NUMBER =:refNumber) and\n" +
                "   (:sectorID is null or a.COMPLAINT_DEPARTMENT =TO_NUMBER(:sectorID)) and\n" +
                "   (:departmentID is null or a.DIVISION =:departmentID) and\n" +
                "   (:regionID is null or a.REGION =:regionID)",

        nativeQuery = true
    )
    fun findFilteredConsumerComplaintReportWithBothEmpty(
        @Param("startDate") startDate: Date?,
        @Param("endDate") endDate: Date?,
        @Param("assignIO") assignIO: Long?,
        @Param("refNumber") refNumber: String?,
        @Param("sectorID") sectorID: Long?,
        @Param("departmentID") departmentID: String?,
        @Param("regionID") regionID: String?

    ): List<ConsumerComplaintsReportViewEntity>?

}




@Repository
interface IMsSeizedGoodsReportViewRepository : HazelcastRepository<MsSeizedGoodsReportViewEntity, Long> {
    override fun findAll(pageable: Pageable): Page<MsSeizedGoodsReportViewEntity>

    @Query(
        value = "SELECT a.* from APOLLO.MS_SEIZED_GOODS_REPORT_VIEW a where\n" +
                "    (:startDate is null or a.DATE_OF_SEIZURE_AS_DATE >=TO_DATE(:startDate)) and (:endDate is null or a.DATE_OF_SEIZURE_AS_DATE <=TO_DATE(:endDate))\n" +
                "    AND (:sector is null or a.SECTOR =:sector) AND (:brand is null or a.BRAND =:brand) AND (:marketCentre is null or a.MARKET_CENTRE =:marketCentre)\n" +
                "    AND (:nameOutlet is null or a.NAME_OUTLET =:nameOutlet) AND (:officerID is null or a.OFFICER =:officerID) AND (:productsDueForDestruction is null or a.PRODUCTS_DUE_FOR_DESTRUCTION =:productsDueForDestruction)",

        nativeQuery = true
    )
    fun findFilteredSeizedGoodsReport(
        @Param("startDate") startDate: Date?,
        @Param("endDate") endDate: Date?,
        @Param("sector") sector: String?,
        @Param("brand") brand: String?,
        @Param("marketCentre") marketCentre: String?,
        @Param("nameOutlet") nameOutlet: String?,
        @Param("productsDueForDestruction") productsDueForDestruction: String?,
        @Param("officerID") officerID: String?,
    ): List<MsSeizedGoodsReportViewEntity>?

    @Query(
        value = "SELECT a.* from APOLLO.MS_SEIZED_GOODS_REPORT_VIEW a where\n" +
                "    (:startDate is null or a.DATE_OF_SEIZURE_AS_DATE >=TO_DATE(:startDate)) and (:endDate is null or a.DATE_OF_SEIZURE_AS_DATE <=TO_DATE(:endDate))\n" +
                "    AND (:sector is null or a.SECTOR =:sector) AND (:brand is null or a.BRAND =:brand) AND (:marketCentre is null or a.MARKET_CENTRE =:marketCentre)\n" +
                "    AND (:nameOutlet is null or a.NAME_OUTLET =:nameOutlet) AND (:officerID is null or a.OFFICER =:officerID) AND (:productsDueForDestruction is null or a.PRODUCTS_DUE_FOR_DESTRUCTION =:productsDueForDestruction)\n" +
                "    AND ( a.OFFICER IN :selectedOfficers)",
        nativeQuery = true
    )
    fun findFilteredSeizedGoodsReportWithMultipleOfficers(
        @Param("startDate") startDate: Date?,
        @Param("endDate") endDate: Date?,
        @Param("sector") sector: String?,
        @Param("brand") brand: String?,
        @Param("marketCentre") marketCentre: String?,
        @Param("nameOutlet") nameOutlet: String?,
        @Param("productsDueForDestruction") productsDueForDestruction: String?,
        @Param("officerID") officerID: String?,
        @Param("selectedOfficers") selectedOfficers: Array<String>?,
    ): List<MsSeizedGoodsReportViewEntity>?

    fun findByMsWorkplanGeneratedId(msWorkplanGeneratedId: String): List<MsSeizedGoodsReportViewEntity>

    @Query(
        value = "SELECT c.ID,\n" +
                "       nvl(TO_CHAR(TRUNC(c.DATE_SEIZURE),'DD/MM/YYYY'),'N/A') AS DATEOF_SEIZURE,\n" +
                "       nvl(TO_CHAR(b.MS_WORKPLAN_GENERATED_ID),'N/A') AS MS_WORKPLAN_GENERATED_ID,\n" +
                "       nvl(b.MARKET_TOWN_CENTER,'N/A') AS MARKET_CENTRE,\n" +
                "       nvl(b.PRODUCT_FIELD,'N/A') AS PRODUCT_FIELD,\n" +
                "       nvl(b.NAME_OF_OUTLET,'N/A') AS NAME_OUTLET,\n" +
                "       nvl(c.DESCRIPTION_PRODUCTS_SEIZED,'N/A') AS DESCRIPTION_PRODUCTS_SEIZED,\n" +
                "       nvl(c.BRAND,'N/A') AS BRAND,\n" +
                "       nvl(c.SECTOR,'N/A') AS SECTOR,\n" +
                "       nvl(c.QUANTITY,'N/A') AS QUANTITY,\n" +
                "       nvl(c.UNIT,'N/A') AS UNIT,\n" +
                "       nvl(c.ESTIMATED_COST,'N/A') AS ESTIMATED_COST ,\n" +
                "       nvl(c.CURRENT_LOCATION,'N/A') AS CURRENT_LOCATION_SEIZED_PRODUCTS ,\n" +
                "       nvl(c.PRODUCTS_DESTRUCTION,'N/A') AS PRODUCTS_DUE_FOR_DESTRUCTION ,\n" +
                "       nvl(c.PRODUCTS_RELEASE,'N/A') AS PRODUCTS_DUE_FOR_RELEASE,\n" +
                "       nvl(TO_CHAR(TRUNC(c.DATE_DESTRUCTED),'DD/MM/YYYY'),'N/A') AS DATEOF_DESTRUCTED,\n" +
                "       nvl(TO_CHAR(TRUNC(c.DATE_RELEASE),'DD/MM/YYYY'),'N/A') AS DATEOF_RELEASE,\n" +
                "       c.DATE_SEIZURE AS DATE_SEIZURE,\n" +
                "       c.DATE_DESTRUCTED AS DATE_DESTRUCTED,\n" +
                "       c.DATE_RELEASE AS DATE_RELEASE\n" +
                "FROM DAT_KEBS_MS_WORKPLAN_GENARATED a\n" +
                "         JOIN  DAT_KEBS_MS_SEIZURE_DECLARATION b ON a.id= b.MS_WORKPLAN_GENERATED_ID\n" +
                "         JOIN  DAT_MS_SEIZURE c ON a.id= c.WORKPLAN_GENERATED_ID AND c.MAIN_SEIZURE_ID = b.ID " +
                " WHERE TO_CHAR(a.ID)=:msWorkplanGeneratedId",
        nativeQuery = true
    )
    fun findMsWorkplanGeneratedId( @Param("msWorkplanGeneratedId") msWorkplanGeneratedId: String): List<MsSeizedGoodsReportViewEntity>
}



@Repository
interface ISubmittedSamplesSummaryReportViewRepository : HazelcastRepository<SubmittedSamplesSummaryReportViewEntity, Long> {
    override fun findAll(pageable: Pageable): Page<SubmittedSamplesSummaryReportViewEntity>

    @Query(
        value = "SELECT a.* from APOLLO.SUBMITTED_SAMPLES_SUMMARY_REPORT_VIEW a where\n" +
                "    (:startDate is null or a.INSPECTION_DATE_AS_DATE >=TO_DATE(:startDate)) and (:endDate is null or a.INSPECTION_DATE_AS_DATE <=TO_DATE(:endDate))\n" +
                "    AND (:sampleReferences is null or a.BS_NUMBER =:sampleReferences) and (:assignIO is null or a.OFFICER_NAME = :assignIO)\n" +
                "    AND (:sectorID is null or a.DEPARTMENT =:sectorID)\n"+
                "    AND (:function is null or a.FUNCTION =:function)\n"+
                "    AND (:outletName is null or a.OUTLET_NAME =:outletName)\n"+
                "    AND (:nameProduct is null or a.NAME_PRODUCT =:nameProduct)\n",
        nativeQuery = true
    )
    fun findFilteredSubmittedSamplesSummaryReport(
        @Param("startDate") startDate: Date?,
        @Param("endDate") endDate: Date?,
        @Param("sampleReferences") sampleReferences: String?,
        @Param("assignIO") assignIO: String?,
        @Param("sectorID") sectorID: String?,
        @Param("nameProduct") nameProduct: String?,
        @Param("function") function: String?,
        @Param("outletName") outletName: String?,
    ): List<SubmittedSamplesSummaryReportViewEntity>?
    @Query(
        value = "SELECT a.* from APOLLO.SUBMITTED_SAMPLES_SUMMARY_REPORT_VIEW a where\n" +
                "    (:startDate is null or a.INSPECTION_DATE_AS_DATE >=TO_DATE(:startDate)) and (:endDate is null or a.INSPECTION_DATE_AS_DATE <=TO_DATE(:endDate))\n" +
                "    AND (:sampleReferences is null or a.BS_NUMBER =:sampleReferences) and (:assignIO is null or a.OFFICER_NAME = :assignIO)\n" +
                "    AND (:sectorID is null or a.DEPARTMENT =:sectorID)\n"+
                "    AND (:function is null or a.FUNCTION =:function)\n"+
                "    AND (:outletName is null or a.OUTLET_NAME =:outletName)\n"+
                "    AND (:nameProduct is null or a.NAME_PRODUCT =:nameProduct)\n"+
                "    AND ( a.OFFICER_NAME IN :selectedOfficers)\n"+
                "    AND ( a.FUNCTION IN :selectedDivisions)",
        nativeQuery = true
    )
    fun findFilteredSubmittedSamplesSummaryReportWithBothPresent(
        @Param("startDate") startDate: Date?,
        @Param("endDate") endDate: Date?,
        @Param("sampleReferences") sampleReferences: String?,
        @Param("assignIO") assignIO: String?,
        @Param("sectorID") sectorID: String?,
        @Param("nameProduct") nameProduct: String?,
        @Param("function") function: String?,
        @Param("outletName") outletName: String?,
        @Param("selectedOfficers") selectedOfficers: Array<String>?,
        @Param("selectedDivisions") selectedDivisions: Array<String>?
    ): List<SubmittedSamplesSummaryReportViewEntity>?
    @Query(
        value = "SELECT a.* from APOLLO.SUBMITTED_SAMPLES_SUMMARY_REPORT_VIEW a where\n" +
                "    (:startDate is null or a.INSPECTION_DATE_AS_DATE >=TO_DATE(:startDate)) and (:endDate is null or a.INSPECTION_DATE_AS_DATE <=TO_DATE(:endDate))\n" +
                "    AND (:sampleReferences is null or a.BS_NUMBER =:sampleReferences) and (:assignIO is null or a.OFFICER_NAME = :assignIO)\n" +
                "    AND (:sectorID is null or a.DEPARTMENT =:sectorID)\n"+
                "    AND (:function is null or a.FUNCTION =:function)\n"+
                "    AND (:outletName is null or a.OUTLET_NAME =:outletName)\n"+
                "    AND (:nameProduct is null or a.NAME_PRODUCT =:nameProduct)\n"+
                "    AND ( a.OFFICER_NAME IN :selectedOfficers)\n",
        nativeQuery = true
    )
    fun findFilteredSubmittedSamplesSummaryReportWithOfficers(
        @Param("startDate") startDate: Date?,
        @Param("endDate") endDate: Date?,
        @Param("sampleReferences") sampleReferences: String?,
        @Param("assignIO") assignIO: String?,
        @Param("sectorID") sectorID: String?,
        @Param("nameProduct") nameProduct: String?,
        @Param("function") function: String?,
        @Param("outletName") outletName: String?,
        @Param("selectedOfficers") selectedOfficers: Array<String>?
    ): List<SubmittedSamplesSummaryReportViewEntity>?
    @Query(
        value = "SELECT a.* from APOLLO.SUBMITTED_SAMPLES_SUMMARY_REPORT_VIEW a where\n" +
                "    (:startDate is null or a.INSPECTION_DATE_AS_DATE >=TO_DATE(:startDate)) and (:endDate is null or a.INSPECTION_DATE_AS_DATE <=TO_DATE(:endDate))\n" +
                "    AND (:sampleReferences is null or a.BS_NUMBER =:sampleReferences) and (:assignIO is null or a.OFFICER_NAME = :assignIO)\n" +
                "    AND (:sectorID is null or a.DEPARTMENT =:sectorID)\n"+
                "    AND (:function is null or a.FUNCTION =:function)\n"+
                "    AND (:outletName is null or a.OUTLET_NAME =:outletName)\n"+
                "    AND (:nameProduct is null or a.NAME_PRODUCT =:nameProduct)\n"+
                "    AND ( a.FUNCTION IN :selectedDivisions)",
        nativeQuery = true
    )
    fun findFilteredSubmittedSamplesSummaryReportWithDivisions(
        @Param("startDate") startDate: Date?,
        @Param("endDate") endDate: Date?,
        @Param("sampleReferences") sampleReferences: String?,
        @Param("assignIO") assignIO: String?,
        @Param("sectorID") sectorID: String?,
        @Param("nameProduct") nameProduct: String?,
        @Param("function") function: String?,
        @Param("outletName") outletName: String?,
        @Param("selectedDivisions") selectedDivisions: Array<String>?
    ): List<SubmittedSamplesSummaryReportViewEntity>?
}

@Repository
interface IWorkPlanMonitoringSameTaskDateEntityRepository : HazelcastRepository<WorkPlanMonitoringSameTaskDateEntity, Long> {
    override fun findAll(pageable: Pageable): Page<WorkPlanMonitoringSameTaskDateEntity>

    fun findAllByWorkplanYearIdAndTimeActivityDateAndCountyIdAndRegionIdAndTownsId(workplanYearId: Long, timeActivityDate:java.sql.Date,countyId:Long,regionId: Long,townsId:Long ): List<WorkPlanMonitoringSameTaskDateEntity>
}

@Repository
interface IFieldInspectionSummaryReportViewRepository : HazelcastRepository<FieldInspectionSummaryReportViewEntity, Long> {
    override fun findAll(pageable: Pageable): Page<FieldInspectionSummaryReportViewEntity>

    @Query(
        value = "SELECT a.* from APOLLO.FIELD_INSPECTION_SUMMARY_REPORT_VIEW a where\n" +
                "    (:startDate is null or a.INSPECTION_DATE >=TO_DATE(:startDate)) and (:endDate is null or a.INSPECTION_DATE <=TO_DATE(:endDate))\n" +
                "    and (:assignIO is null or a.OFFICER_ID =TO_NUMBER(:assignIO)) AND (:sectorID is null or a.COMPLAINT_DEPARTMENT =TO_NUMBER(:sectorID))\n" +
                "AND (:outletName is null or a.NAME_OUTLET =:outletName)\n" +
                "AND (:divisionName is null or a.DIVISION =:divisionName)",
        nativeQuery = true
    )
    fun findFilteredFieldInspectionSummaryReport(
        @Param("startDate") startDate: Date?,
        @Param("endDate") endDate: Date?,
        @Param("assignIO") assignIO: Long?,
        @Param("sectorID") sectorID: Long?,
        @Param("outletName") outletName: String?,
        @Param("divisionName") divisionName: String?
    ): List<FieldInspectionSummaryReportViewEntity>?

    @Query(
        value = "SELECT a.* from APOLLO.FIELD_INSPECTION_SUMMARY_REPORT_VIEW a where\n" +
                "    (:startDate is null or a.INSPECTION_DATE >=TO_DATE(:startDate)) and (:endDate is null or a.INSPECTION_DATE <=TO_DATE(:endDate))\n" +
                "    and (:assignIO is null or a.OFFICER_ID =TO_NUMBER(:assignIO)) AND (:sectorID is null or a.COMPLAINT_DEPARTMENT =TO_NUMBER(:sectorID))\n" +
                "AND (:outletName is null or a.NAME_OUTLET =:outletName)\n" +
                "AND (:divisionName is null or a.DIVISION =:divisionName)" +
                "AND ( a.DIVISION IN :selectedDivisions)"+
                "AND ( a.OFFICER_ID IN :selectedOfficers)",
        nativeQuery = true
    )
    fun findFilteredFieldInspectionSummaryReportWithBothPresent(
        @Param("startDate") startDate: Date?,
        @Param("endDate") endDate: Date?,
        @Param("assignIO") assignIO: Long?,
        @Param("sectorID") sectorID: Long?,
        @Param("outletName") outletName: String?,
        @Param("divisionName") divisionName: String?,
        @Param("selectedOfficers") selectedOfficers: Array<Long>?,
        @Param("selectedDivisions") selectedDivisions: Array<String>?
    ): List<FieldInspectionSummaryReportViewEntity>?

    @Query(
        value = "SELECT a.* from APOLLO.FIELD_INSPECTION_SUMMARY_REPORT_VIEW a where\n" +
                "    (:startDate is null or a.INSPECTION_DATE >=TO_DATE(:startDate)) and (:endDate is null or a.INSPECTION_DATE <=TO_DATE(:endDate))\n" +
                "    and (:assignIO is null or a.OFFICER_ID =TO_NUMBER(:assignIO)) AND (:sectorID is null or a.COMPLAINT_DEPARTMENT =TO_NUMBER(:sectorID))\n" +
                "AND (:outletName is null or a.NAME_OUTLET =:outletName)\n" +
                "AND (:divisionName is null or a.DIVISION =:divisionName)" +
                "AND ( a.DIVISION IN :selectedDivisions)",

        nativeQuery = true
    )
    fun findFilteredFieldInspectionSummaryReportWithDivisions(
        @Param("startDate") startDate: Date?,
        @Param("endDate") endDate: Date?,
        @Param("assignIO") assignIO: Long?,
        @Param("sectorID") sectorID: Long?,
        @Param("outletName") outletName: String?,
        @Param("divisionName") divisionName: String?,
        @Param("selectedDivisions") selectedDivisions: Array<String>?
    ): List<FieldInspectionSummaryReportViewEntity>?

    @Query(
        value = "SELECT a.* from APOLLO.FIELD_INSPECTION_SUMMARY_REPORT_VIEW a where\n" +
                "    (:startDate is null or a.INSPECTION_DATE >=TO_DATE(:startDate)) and (:endDate is null or a.INSPECTION_DATE <=TO_DATE(:endDate))\n" +
                "    and (:assignIO is null or a.OFFICER_ID =TO_NUMBER(:assignIO)) AND (:sectorID is null or a.COMPLAINT_DEPARTMENT =TO_NUMBER(:sectorID))\n" +
                "AND (:outletName is null or a.NAME_OUTLET =:outletName)\n" +
                "AND (:divisionName is null or a.DIVISION =:divisionName)" +
                "AND ( a.OFFICER_ID IN :selectedOfficers)",

        nativeQuery = true
    )
    fun findFilteredFieldInspectionSummaryReportWithOfficers(
        @Param("startDate") startDate: Date?,
        @Param("endDate") endDate: Date?,
        @Param("assignIO") assignIO: Long?,
        @Param("sectorID") sectorID: Long?,
        @Param("outletName") outletName: String?,
        @Param("divisionName") divisionName: String?,
        @Param("selectedOfficers") selectedOfficers: Array<Long>?,
    ): List<FieldInspectionSummaryReportViewEntity>?
}


@Repository
interface IWorkPlanMonitoringToolReportViewRepository : HazelcastRepository<WorkPlanMonitoringToolEntity, Long> {
    override fun findAll(pageable: Pageable): Page<WorkPlanMonitoringToolEntity>

    @Query(
        value = "SELECT a.* from APOLLO.WORK_PLAN_MONITORING_TOOL a WHERE\n" +
                "    (:startDate is null or a.TIME_ACTIVITY_DATE >=TO_DATE(:startDate)) and (:endDate is null or a.TIME_ACTIVITY_END_DATE <=TO_DATE(:endDate))\n" +
                "and (:assignIO is null or a.OFFICER_ID =TO_NUMBER(:assignIO)) AND (:sectorID is null or a.COMPLAINT_DEPARTMENT =TO_NUMBER(:sectorID))",
        nativeQuery = true
    )
    fun findFilteredWorkPlanMonitoringToolReport(
        @Param("startDate") startDate: Date?,
        @Param("endDate") endDate: Date?,
        @Param("assignIO") assignIO: Long?,
        @Param("sectorID") sectorID: Long?
    ): List<WorkPlanMonitoringToolEntity>?
}

@Repository
interface IMsComplaintFeedbackViewRepository : HazelcastRepository<MsComplaintFeedbackViewEntity, Long>, JpaSpecificationExecutor<MsComplaintFeedbackViewEntity> {
    override fun findAll(pageable: Pageable): Page<MsComplaintFeedbackViewEntity>

    fun findAllByReferenceNumber(acknowledgementType: String): List<MsComplaintFeedbackViewEntity>
    fun findAllByAcknowledgementTypeIsNot(acknowledgementType: String,pageable: Pageable): Page<MsComplaintFeedbackViewEntity>
    fun findAllByFeedbackSent(feedbackSent: String,pageable: Pageable): Page<MsComplaintFeedbackViewEntity>
}

@Repository
interface IMsReportSubmittedCpViewRepository : HazelcastRepository<MsReportSubmittedCpViewEntity, Long> {
    override fun findAll(pageable: Pageable): Page<MsReportSubmittedCpViewEntity>

    fun findAllByReferenceNumber(referenceNumber: String): List<MsReportSubmittedCpViewEntity>
}

@Repository
interface IMsSampleSubmittedCpViewRepository : HazelcastRepository<MsSampleSubmittedCpViewEntity, Long> {
    override fun findAll(pageable: Pageable): Page<MsSampleSubmittedCpViewEntity>

    fun findAllByReferenceNumber(referenceNumber: String): List<MsSampleSubmittedCpViewEntity>
}

@Repository
interface IMsComplaintPdfGenerationViewRepository : HazelcastRepository<MsComplaintPdfGenerationEntityView, Long> {
    override fun findAll(pageable: Pageable): Page<MsComplaintPdfGenerationEntityView>

    fun findAllByReferenceNumber(referenceNumber: String): List<MsComplaintPdfGenerationEntityView>
}

@Repository
interface IMsAllocatedTasksCpViewRepository : HazelcastRepository<MsAllocatedTasksCpViewEntity, Long> {
    override fun findAll(pageable: Pageable): Page<MsAllocatedTasksCpViewEntity>

    fun countByAssignedIo(assignedIo: Long): Long
    fun countByAssignedIoAndMsComplaintEndedStatus(assignedIo: Long,msComplaintEndedStatus:Long): Long
    fun countByAssignedIoAndTaskOverDue(assignedIo: Long, taskOverDue: String): Long
    fun findAllByReferenceNumber(referenceNumber: String): List<MsAllocatedTasksCpViewEntity>
    fun findAllByAssignedIo(assignedIo: Long,pageable: Pageable): Page<MsAllocatedTasksCpViewEntity>
    fun findAllByAssignedIoAndTaskOverDue(assignedIo: Long,taskOverDue: String,pageable: Pageable): Page<MsAllocatedTasksCpViewEntity>
}

@Repository
interface IMsAllocatedTasksWpViewRepository : HazelcastRepository<MsAllocatedTasksWpViewEntity, Long> {
    override fun findAll(pageable: Pageable): Page<MsAllocatedTasksWpViewEntity>

    fun countByOfficerIdAndComplaintIdIsNotNull(officerId: Long): Long
    fun countByOfficerIdAndComplaintIdIsNull(officerId: Long): Long
    fun countByOfficerIdAndTaskOverDueAndComplaintIdIsNotNull(officerId: Long, taskOverDue: String): Long
    fun countByOfficerIdAndTaskOverDueAndComplaintIdIsNull(officerId: Long, taskOverDue: String): Long
    fun findAllByReferenceNumber(referenceNumber: String): List<MsAllocatedTasksWpViewEntity>
    fun findAllByOfficerIdAndTaskOverDueAndComplaintIdIsNotNull(assignedIo: Long,askOverDue: String, pageable: Pageable): Page<MsAllocatedTasksWpViewEntity>
    fun findAllByOfficerIdAndComplaintIdIsNotNull(assignedIo: Long,pageable: Pageable): Page<MsAllocatedTasksWpViewEntity>
    fun findAllByOfficerIdAndComplaintIdIsNull(assignedIo: Long,pageable: Pageable): Page<MsAllocatedTasksWpViewEntity>
    fun findAllByOfficerIdAndTaskOverDueAndComplaintIdIsNull(assignedIo: Long,askOverDue: String, pageable: Pageable): Page<MsAllocatedTasksWpViewEntity>
}

interface IMsTasksPendingAllocationCpViewRepository : HazelcastRepository<MsTasksPendingAllocationCpViewEntity, Long> {
    override fun findAll(pageable: Pageable): Page<MsTasksPendingAllocationCpViewEntity>

    fun countByHodAssignedIsNullAndMsComplaintEndedStatus(msComplaintEndedStatus: Long): Long
    fun countByHodAssignedIsNullAndMsComplaintEndedStatusIsNull(): Long
    fun countByHodAssignedAndMsComplaintEndedStatusIsNullAndHofAssignedIsNull(hodAssigned: Long): Long
    fun countByHodAssignedIsNotNullAndMsComplaintEndedStatusIsNullAndHofAssignedIsNull(): Long
    fun countByHodAssignedIsNotNullAndMsComplaintEndedStatusIsNullAndHofAssignedIsNotNullAndAssignedIoIsNull(): Long
    fun countByHofAssignedAndMsComplaintEndedStatusIsNullAndAssignedIoIsNull(hofAssigned: Long): Long

    fun findAllByHodAssignedAndMsComplaintEndedStatusIsNullAndHofAssignedIsNull(hodAssigned: Long, pageable: Pageable): Page<MsTasksPendingAllocationCpViewEntity>
    fun findAllByHofAssignedAndMsComplaintEndedStatusIsNullAndAssignedIoIsNull(hofAssigned: Long, pageable: Pageable): Page<MsTasksPendingAllocationCpViewEntity>
    fun findAllByHodAssignedIsNullAndMsComplaintEndedStatusIsNull(hofAssigned: Long, pageable: Pageable): Page<MsTasksPendingAllocationCpViewEntity>
    fun findAllByHodAssignedIsNullOrHofAssignedIsNullOrAssignedIoIsNullAndMsComplaintEndedStatusIsNull(pageable: Pageable): Page<MsTasksPendingAllocationCpViewEntity>
}

@Repository
interface IMsTasksPendingAllocationWpViewRepository : HazelcastRepository<MsTasksPendingAllocationWpViewEntity, Long> {
    override fun findAll(pageable: Pageable): Page<MsTasksPendingAllocationWpViewEntity>

    fun countByOfficerId(officerId: Long): Long
    fun countByTaskOverDueAndComplaintIdIsNotNullAndUserTaskId(taskOverDue: String,userTaskId: Long): Long
    fun countByTaskOverDueAndComplaintIdIsNotNull(taskOverDue: String): Long
    fun countByTaskOverDueAndComplaintIdIsNullAndUserTaskId(taskOverDue: String,userTaskId: Long): Long
    fun countByTaskOverDueAndComplaintIdIsNull(taskOverDue: String): Long
    fun countByReportPendingReviewAndComplaintIdIsNotNull(reportPendingReview: Int): Long
    fun countByReportPendingReviewAndComplaintIdIsNull(reportPendingReview: Int): Long
    fun findAllByReportPendingReviewAndComplaintIdIsNotNull(reportPendingReview: Int, pageable: Pageable): Page<MsTasksPendingAllocationWpViewEntity>
    fun findAllByTaskOverDueAndComplaintIdIsNotNull(taskOverDue: String, pageable: Pageable): Page<MsTasksPendingAllocationWpViewEntity>
    fun findAllByTaskOverDueAndComplaintIdIsNull(taskOverDue: String, pageable: Pageable): Page<MsTasksPendingAllocationWpViewEntity>
    fun findAllByReportPendingReviewAndComplaintIdIsNull(reportPendingReview: Int, pageable: Pageable): Page<MsTasksPendingAllocationWpViewEntity>
    fun countByOfficerIdAndTaskOverDueAndComplaintIdIsNotNull(officerId: Long, taskOverDue: String): Long
    fun countByOfficerIdAndTaskOverDueAndComplaintIdIsNull(officerId: Long, taskOverDue: String): Long
    fun findAllByReferenceNumber(referenceNumber: String): List<MsTasksPendingAllocationWpViewEntity>
}

@Repository
interface IWorkPlanViewUcrNumberItemsRepository : HazelcastRepository<WorkPlanViewUcrNumberItemsEntity, Long> {
    override fun findAll(pageable: Pageable): Page<WorkPlanViewUcrNumberItemsEntity>

    fun findAllByUcrNumber(ucrNumber: String): List<WorkPlanViewUcrNumberItemsEntity>?

}
