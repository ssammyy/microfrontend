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

import org.kebs.app.kotlin.apollo.store.model.*
import org.kebs.app.kotlin.apollo.store.model.ms.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.sql.Date

@Repository
interface ISurveyItemRepository : HazelcastRepository<SurveyItemsEntity, Long> {
    fun findAllByStatus(status: Int, pages: Pageable?): Page<SurveyItemsEntity>?

}

@Repository
interface IMsTypesRepository : HazelcastRepository<MsTypesEntity, Long> {
    fun findAllByStatus(status: Int): List<MsTypesEntity>?
    fun findByStatus(status: Int): List<MsTypesEntity>?
    fun findByUuid(uuid: String): MsTypesEntity?
    fun findByIdAndStatus(id: Long, status: Int): MsTypesEntity?

}

@Repository
interface ICfgRecommendationRepository : HazelcastRepository<CfgMsRecommendationEntity, Long> {
    fun findAllByStatus(status: Int): List<CfgMsRecommendationEntity>?
//    fun findAllByStatus(status: Int, pages: Pageable?): Page<CfgMsRecommendationEntity>?

}

@Repository
interface IWorkplanRepository : HazelcastRepository<WorkplanEntity, Long> {
    fun findByUserId(userId: UsersEntity): List<WorkplanEntity>?
    fun findByUserId(userId: UsersEntity, pages: Pageable?): Page<WorkplanEntity>?
}

@Repository
interface IOnsiteUploadRepository : HazelcastRepository<MsOnSiteUploadsEntity, Long> {
    fun findByWorkPlanGeneratedID(workPlanGeneratedID: MsWorkPlanGeneratedEntity): MsOnSiteUploadsEntity?
    fun findByWorkPlanGeneratedIDAndStatus(workPlanGeneratedID: MsWorkPlanGeneratedEntity, status: Int, pageable: Pageable): Page<MsOnSiteUploadsEntity>?
    fun findByWorkPlanGeneratedIDAndStatus(workPlanGeneratedID: MsWorkPlanGeneratedEntity, status: Int): List<MsOnSiteUploadsEntity>?
//    fun findByUserId(userId: UsersEntity): List<WorkplanEntity>?
//    fun findByUserId(userId: UsersEntity, pages: Pageable?): Page<WorkplanEntity>?
}

@Repository
interface IPreliminaryReportRepository : HazelcastRepository<MsPreliminaryReportEntity, Long> {
    fun findByWorkPlanGeneratedID(workPlanGeneratedID: Long): MsPreliminaryReportEntity?
    fun findByWorkPlanGeneratedIDAndFinalReportStatus(workPlanGeneratedID: Long,finalReportStatus:Int): MsPreliminaryReportEntity?
    fun findByWorkPlanGeneratedIDAndStatus(workPlanGeneratedID: Long, status: Int, pageable: Pageable): Page<MsPreliminaryReportEntity>?
//    fun findByUserId(userId: UsersEntity): List<WorkplanEntity>?
//    fun findByUserId(userId: UsersEntity, pages: Pageable?): Page<WorkplanEntity>?
}

@Repository
interface IFinalReportRepository : HazelcastRepository<MsFinalReportEntity, Long> {
    fun findByWorkPlanGeneratedID(workPlanGeneratedID: MsWorkPlanGeneratedEntity): MsFinalReportEntity?
    fun findByWorkPlanGeneratedIDAndStatus(workPlanGeneratedID: MsWorkPlanGeneratedEntity, status: Int, pageable: Pageable): Page<MsFinalReportEntity>?
//    fun findByUserId(userId: UsersEntity): List<WorkplanEntity>?
//    fun findByUserId(userId: UsersEntity, pages: Pageable?): Page<WorkplanEntity>?
}

@Repository
interface IChargeSheetRepository : HazelcastRepository<MsChargeSheetEntity, Long> {
//    fun findByUserId(userId: UsersEntity): List<WorkplanEntity>?
    fun findByWorkPlanGeneratedID(workPlanGeneratedID: Long): MsChargeSheetEntity?
}

@Repository
interface IWorkPlanProductsEntityRepository : HazelcastRepository<WorkPlanProductsEntity, Long>{
    fun findByWorkPlanId(workPlanId: Long): List<WorkPlanProductsEntity>?
    fun findByReferenceNo(referenceNo: String): WorkPlanProductsEntity?
}

@Repository
interface IWorkPlanGenerateRepository : HazelcastRepository<MsWorkPlanGeneratedEntity, Long> {
    override fun findAll( pageable: Pageable): Page<MsWorkPlanGeneratedEntity>
    fun findAllByOrderByIdDesc( pageable: Pageable): Page<MsWorkPlanGeneratedEntity>
    fun findByWorkPlanYearId(workPlanYearId: Long,pageable: Pageable): Page<MsWorkPlanGeneratedEntity>?
    fun findByWorkPlanYearIdAndComplaintIdIsNotNull(workPlanYearId: Long,pageable: Pageable): Page<MsWorkPlanGeneratedEntity>?
    fun findByWorkPlanYearIdAndApproved(workPlanYearId: Long, approved: String, pageable: Pageable): Page<MsWorkPlanGeneratedEntity>?
    fun findAllByMsProcessEndedStatus(msProcessEndedStatus: Int): List<MsWorkPlanGeneratedEntity>?
    fun findByWorkPlanYearIdAndMsProcessEndedStatus(workPlanYearId: Long,msProcessEndedStatus: Int,pageable: Pageable): Page<MsWorkPlanGeneratedEntity>
    fun findByWorkPlanYearIdAndMsProcessEndedStatusAndComplaintIdIsNotNull(workPlanYearId: Long,msProcessEndedStatus: Int,pageable: Pageable): Page<MsWorkPlanGeneratedEntity>


    @Query(
        value = "SELECT other_workplans.* from\n" +
                " (\n" +
                "     SELECT a.*,b.COUNTY_ID,b.TOWNS_ID\n" +
                "     FROM DAT_KEBS_MS_WORKPLAN_GENARATED a,DAT_KEBS_MS_WORK_PLAN_COUNTIES_TOWNS b\n" +
                "     WHERE a.id=b.work_plan_id\n" +
                "       --AND a.id<>\n" +
                "       AND a.WORKPLAN_YEAR_ID =:workPlanYearId" +
                "       AND a.OFFICER_ID <>:officerID\n" +
                "       AND a.APPROVED_STATUS=1\n" +
                "       AND a.onsite_end_status!=1\n" +
                "       AND a.COMPLAINT_ID IS NULL\n" +
                "     --AND to_date(to_char(a.time_activity_date,'yyyy-mm-dd'),'yyyy-mm-dd')>=to_date('2023-03-01','yyyy-mm-dd')\n" +
                "     --AND to_date(to_char(a.time_activity_date,'yyyy-mm-dd'),'yyyy-mm-dd')<to_date('2023-03-02','yyyy-mm-dd')\n" +
                " )other_workplans,\n" +
                " (\n" +
                "     SELECT a.*,b.COUNTY_ID,b.TOWNS_ID\n" +
                "     FROM DAT_KEBS_MS_WORKPLAN_GENARATED a,\n" +
                "          DAT_KEBS_MS_WORK_PLAN_COUNTIES_TOWNS b\n" +
                "     WHERE a.id=b.work_plan_id\n" +
                "       AND a.OFFICER_ID =:officerID\n" +
                "     --AND a.id=1347\n" +
                "     --AND to_date(to_char(a.time_activity_date,'yyyy-mm-dd'),'yyyy-mm-dd')>=to_date('2023-03-01','yyyy-mm-dd')\n" +
                "     --AND to_date(to_char(a.time_activity_date,'yyyy-mm-dd'),'yyyy-mm-dd')<to_date('2023-03-02','yyyy-mm-dd')\n" +
                " )lookup\n" +
                "WHERE lookup.county_id=other_workplans.county_id\n" +
                "  AND lookup.towns_id=other_workplans.towns_id\n" +
                "  AND trunc(lookup.time_activity_date)=trunc(other_workplans.time_activity_date)\n",
        nativeQuery = true
    )
    fun getWorkPlanWithSameDetails(
        @Param("workPlanYearId") workPlanYearId: Long,
        @Param("officerID") officerID: Long
    ): List<MsWorkPlanGeneratedEntity>?

    @Query(
        value = "SELECT other_workplans.* from\n" +
                " (\n" +
                "     SELECT a.*,b.COUNTY_ID,b.TOWNS_ID\n" +
                "     FROM DAT_KEBS_MS_WORKPLAN_GENARATED a,DAT_KEBS_MS_WORK_PLAN_COUNTIES_TOWNS b\n" +
                "     WHERE a.id=b.work_plan_id\n" +
                "       --AND a.id<>\n" +
                "       AND a.WORKPLAN_YEAR_ID =:workPlanYearId" +
                "       AND a.OFFICER_ID <>:officerID\n" +
                "       AND a.APPROVED_STATUS=1\n" +
                "       AND a.onsite_end_status!=1\n" +
                "       AND a.COMPLAINT_ID IS NOT NULL\n" +
                "     --AND to_date(to_char(a.time_activity_date,'yyyy-mm-dd'),'yyyy-mm-dd')>=to_date('2023-03-01','yyyy-mm-dd')\n" +
                "     --AND to_date(to_char(a.time_activity_date,'yyyy-mm-dd'),'yyyy-mm-dd')<to_date('2023-03-02','yyyy-mm-dd')\n" +
                " )other_workplans,\n" +
                " (\n" +
                "     SELECT a.*,b.COUNTY_ID,b.TOWNS_ID\n" +
                "     FROM DAT_KEBS_MS_WORKPLAN_GENARATED a,\n" +
                "          DAT_KEBS_MS_WORK_PLAN_COUNTIES_TOWNS b\n" +
                "     WHERE a.id=b.work_plan_id\n" +
                "       AND a.OFFICER_ID =:officerID\n" +
                "     --AND a.id=1347\n" +
                "     --AND to_date(to_char(a.time_activity_date,'yyyy-mm-dd'),'yyyy-mm-dd')>=to_date('2023-03-01','yyyy-mm-dd')\n" +
                "     --AND to_date(to_char(a.time_activity_date,'yyyy-mm-dd'),'yyyy-mm-dd')<to_date('2023-03-02','yyyy-mm-dd')\n" +
                " )lookup\n" +
                "WHERE lookup.county_id=other_workplans.county_id\n" +
                "  AND lookup.towns_id=other_workplans.towns_id\n" +
                "  AND trunc(lookup.time_activity_date)=trunc(other_workplans.time_activity_date)\n",
        nativeQuery = true
    )
    fun getWorkPlanComplaintWithSameDetails(
        @Param("workPlanYearId") workPlanYearId: Long,
        @Param("officerID") officerID: Long
    ): List<MsWorkPlanGeneratedEntity>?

    fun findAllByWorkPlanYearIdAndTimeActivityDateAndApprovedStatus(
        workPlanYearId: Long,
        timeActivityDate: Date,
        approvedStatus: Int,
        pageable: Pageable
    ): Page<MsWorkPlanGeneratedEntity>?



    fun findByWorkPlanYearIdAndOfficerIdAndUserTaskId(
        workPlanYearId: Long,
        officerId: Long,
        userTaskId: Long,
        pageable: Pageable
    ): Page<MsWorkPlanGeneratedEntity>

    fun findByWorkPlanYearIdAndOfficerIdAndUserTaskIdAndComplaintIdIsNotNull(
        workPlanYearId: Long,
        officerId: Long,
        userTaskId: Long,
        pageable: Pageable
    ): Page<MsWorkPlanGeneratedEntity>

    fun findByWorkPlanYearIdAndHofAssignedAndUserTaskId(
        workPlanYearId: Long,
        hofAssigned: Long,
        userTaskId: Long,
        pageable: Pageable
    ): Page<MsWorkPlanGeneratedEntity>

    fun findByWorkPlanYearIdAndHofAssignedAndUserTaskIdAndComplaintIdIsNotNull(
        workPlanYearId: Long,
        hofAssigned: Long,
        userTaskId: Long,
        pageable: Pageable
    ): Page<MsWorkPlanGeneratedEntity>

    fun findByWorkPlanYearIdAndHodRmAssignedAndUserTaskId(
        workPlanYearId: Long,
        hodRmAssigned: Long,
        userTaskId: Long,
        pageable: Pageable
    ): Page<MsWorkPlanGeneratedEntity>

    fun findByWorkPlanYearIdAndHodRmAssignedAndUserTaskIdAndComplaintIdIsNotNull(
        workPlanYearId: Long,
        hodRmAssigned: Long,
        userTaskId: Long,
        pageable: Pageable
    ): Page<MsWorkPlanGeneratedEntity>

    fun findByWorkPlanYearIdAndUserTaskId(
        workPlanYearId: Long,
        userTaskId: Long,
        pageable: Pageable
    ): Page<MsWorkPlanGeneratedEntity>

    fun findByWorkPlanYearIdAndUserTaskIdAndComplaintIdIsNotNull(
        workPlanYearId: Long,
        userTaskId: Long,
        pageable: Pageable
    ): Page<MsWorkPlanGeneratedEntity>

    fun findByUuid(uuid: String): MsWorkPlanGeneratedEntity?
    fun findByReferenceNumber(referenceNumber: String): MsWorkPlanGeneratedEntity?
    fun findByComplaintId(complaintId: Long): MsWorkPlanGeneratedEntity?
//    fun findByUserId(userId: UsersEntity, pages: Pageable?): Page<WorkplanEntity>?
}

//@Repository
//interface IFuelInspectionRepository : HazelcastRepository<MsFuelInspectionEntity, Long> {
//    override fun findAll( pageable: Pageable): Page<MsFuelInspectionEntity>
//    fun findAllByOrderByIdDesc( pageable: Pageable): Page<MsFuelInspectionEntity>
//    fun findAllByOrderByIdAsc(): List<MsFuelInspectionEntity>
//
////    fun findByIdAndMsProcessStatus(id: Long, msProcessStatus: Int):  MsFuelInspectionEntity?
////    fun findByUserId(userId: UsersEntity): List<WorkplanEntity>?
////    fun findByUserId(userId: UsersEntity, pages: Pageable?): Page<WorkplanEntity>?
//}

@Repository
interface IFuelRemediationRepository : HazelcastRepository<MsFuelRemediationEntity, Long> {
    override fun findAll( pageable: Pageable): Page<MsFuelRemediationEntity>
    fun findAllByOrderByIdDesc( pageable: Pageable): Page<MsFuelRemediationEntity>

    fun findByFuelInspectionId(fuelInspectionId: Long):  MsFuelRemediationEntity?
//    fun findByUserId(userId: UsersEntity): List<WorkplanEntity>?
//    fun findByUserId(userId: UsersEntity, pages: Pageable?): Page<WorkplanEntity>?
}

@Repository
interface IFuelRemediationInvoiceRepository : HazelcastRepository<MsFuelRemedyInvoicesEntity, Long> {
    override fun findAll( pageable: Pageable): Page<MsFuelRemedyInvoicesEntity>
    fun findAllByOrderByIdDesc( pageable: Pageable): Page<MsFuelRemedyInvoicesEntity>

    fun findByFuelInspectionId(fuelInspectionId: Long):  MsFuelRemedyInvoicesEntity?

    fun findByInvoiceNumber(invoiceNumber: String):  MsFuelRemedyInvoicesEntity?

    fun findByInvoiceBatchNumberId(invoiceBatchNumberId: Long):  MsFuelRemedyInvoicesEntity?


    fun findFirstByFuelInspectionId(fuelInspectionId: Long): List<MsFuelRemedyInvoicesEntity>

    fun findAllByPaymentStatus(paymentStatus: Int): List<MsFuelRemedyInvoicesEntity>?
//    fun findByUserId(userId: UsersEntity): List<WorkplanEntity>?
//    fun findByUserId(userId: UsersEntity, pages: Pageable?): Page<WorkplanEntity>?
}

@Repository
interface IFuelRemediationChargesRepository : HazelcastRepository<CfgMsFuelRemediationChargesEntity, Long> {
//    override fun findAll( pageable: Pageable): Page<MsFuelRemediationEntity>
//    fun findAllByOrderByIdDesc( pageable: Pageable): Page<MsFuelRemediationEntity>
//
//    fun findByFuelInspectionId(fuelInspectionId: MsFuelInspectionEntity):  MsFuelRemediationEntity?
//    fun findByUserId(userId: UsersEntity): List<WorkplanEntity>?
//    fun findByUserId(userId: UsersEntity, pages: Pageable?): Page<WorkplanEntity>?
}

@Repository
interface IComplaintScheduleRepository : HazelcastRepository<MsComplaintSchedulesEntity, Long> {
    override fun findAll( pageable: Pageable): Page<MsComplaintSchedulesEntity>
    fun findAllByOrderByIdDesc( pageable: Pageable): Page<MsComplaintSchedulesEntity>
    fun findByComplaintIdAndStatus(complaintId: ComplaintEntity, status: Int): MsComplaintSchedulesEntity?
    fun findAllByStatus(status: Int): List<MsComplaintSchedulesEntity>?
//    fun findByUserId(userId: UsersEntity, pages: Pageable?): Page<WorkplanEntity>?
}



@Repository
interface IPreliminaryOutletsRepository : HazelcastRepository<MsPreliminaryReportOutletsVisitedEntity, Long> {
    override fun findAll( pageable: Pageable): Page<MsPreliminaryReportOutletsVisitedEntity>

//    fun findByPreliminaryReportID(preliminaryReportID: Long, pageable: Pageable): Page<MsPreliminaryReportOutletsVisitedEntity>
    fun findByPreliminaryReportID(preliminaryReportID: Long): List<MsPreliminaryReportOutletsVisitedEntity>

    fun findByPreliminaryReportIDAndStatus(preliminaryReportID: Long, status: Int): MsPreliminaryReportOutletsVisitedEntity?
//    fun findByUserId(userId: UsersEntity): List<WorkplanEntity>?
//    fun findByUserId(userId: UsersEntity, pages: Pageable?): Page<WorkplanEntity>?
}

@Repository
interface IMSInvestInspectReportRepository : HazelcastRepository<MsInspectionInvestigationReportEntity, Long> {
    override fun findAll( pageable: Pageable): Page<MsInspectionInvestigationReportEntity>

    fun findByWorkPlanGeneratedIDAndIsPreliminaryReport(workPlanGeneratedID: Long, isPreliminaryReport: Int):MsInspectionInvestigationReportEntity?
    fun findByIsPreliminaryReportAndWorkPlanGeneratedID(workPlanGeneratedID: Long, isPreliminaryReport: Int):List<MsInspectionInvestigationReportEntity>?
    fun findTopByWorkPlanGeneratedIDAndIsPreliminaryReportOrderByIdDesc(workPlanGeneratedID: Long, isPreliminaryReport: Int):MsInspectionInvestigationReportEntity?
    fun findByIsPreliminaryReportAndWorkPlanGeneratedID(isPreliminaryReport: Int, workPlanGeneratedID: Long): List<MsInspectionInvestigationReportEntity>?
//    fun findByWorkPlanGeneratedID(workPlanGeneratedID: Long):MsInspectionInvestigationReportEntity?
//    fun findByUserId(userId: UsersEntity): List<WorkplanEntity>?
//    fun findByUserId(userId: UsersEntity, pages: Pageable?): Page<WorkplanEntity>?
}

@Repository
interface IMSSeizureDeclarationRepository : HazelcastRepository<MsSeizureDeclarationEntity, Long> {
    override fun findAll( pageable: Pageable): Page<MsSeizureDeclarationEntity>

//    fun findByWorkPlanGeneratedID(workPlanGeneratedID: Long): MsSeizureDeclarationEntity?
    fun findByWorkPlanGeneratedID(workPlanGeneratedID: Long): List<MsSeizureDeclarationEntity>?
//    fun findByUserId(userId: UsersEntity): List<WorkplanEntity>?
//    fun findByUserId(userId: UsersEntity, pages: Pageable?): Page<WorkplanEntity>?
}

@Repository
interface IMsSeizureRepository : HazelcastRepository<MsSeizureEntity, Long> {
    override fun findAll( pageable: Pageable): Page<MsSeizureEntity>

    fun findByWorkPlanGeneratedID(workPlanGeneratedID: Long): List<MsSeizureEntity>?
    fun findByWorkPlanGeneratedIDAndMainSeizureId(workPlanGeneratedID: Long, mainSeizureId: Long): List<MsSeizureEntity>?
//    fun findByUserId(userId: UsersEntity): List<WorkplanEntity>?
//    fun findByUserId(userId: UsersEntity, pages: Pageable?): Page<WorkplanEntity>?
}

@Repository
interface IDataReportRepository : HazelcastRepository<MsDataReportEntity, Long> {
    override fun findAll( pageable: Pageable): Page<MsDataReportEntity>

    fun findByWorkPlanGeneratedIDAndId(workPlanGeneratedID: Long, id: Long): MsDataReportEntity?
    fun findByWorkPlanGeneratedID(workPlanGeneratedID: Long): List<MsDataReportEntity>?
//    fun findByUserId(userId: UsersEntity): List<WorkplanEntity>?
//    fun findByUserId(userId: UsersEntity, pages: Pageable?): Page<WorkplanEntity>?
}



@Repository
interface IDataReportParameterRepository : HazelcastRepository<MsDataReportParametersEntity, Long> {
    override fun findAll( pageable: Pageable): Page<MsDataReportParametersEntity>

    fun findByDataReportId(dataReportId: Long, pageable: Pageable): Page<MsDataReportParametersEntity>

    fun findByDataReportId(dataReportId: Long): List<MsDataReportParametersEntity>?
//    fun findByUserId(userId: UsersEntity): List<WorkplanEntity>?
//    fun findByUserId(userId: UsersEntity, pages: Pageable?): Page<WorkplanEntity>?
}

@Repository
interface ISSFRepository : HazelcastRepository<MsSampleSubmissionEntity, Long> {
    override fun findAll( pageable: Pageable): Page<MsSampleSubmissionEntity>

    fun findByWorkPlanGeneratedIDAndId(workPlanGeneratedID: Long, id: Long): MsSampleSubmissionEntity?
    fun findByWorkPlanGeneratedID(workPlanGeneratedID: Long): List<MsSampleSubmissionEntity>?
//    fun findByUserId(userId: UsersEntity): List<WorkplanEntity>?
//    fun findByUserId(userId: UsersEntity, pages: Pageable?): Page<WorkplanEntity>?
}

@Repository
interface ISSFLabParameterRepository : HazelcastRepository<MsLaboratoryParametersEntity, Long> {
    override fun findAll( pageable: Pageable): Page<MsLaboratoryParametersEntity>

    fun findBysampleSubmissionId(sampleSubmissionId: Long, pageable: Pageable): Page<MsLaboratoryParametersEntity>

    fun findBysampleSubmissionId(sampleSubmissionId: Long): List<MsLaboratoryParametersEntity>?
//    fun findByUserId(userId: UsersEntity): List<WorkplanEntity>?
//    fun findByUserId(userId: UsersEntity, pages: Pageable?): Page<WorkplanEntity>?
}




@Repository
interface IWorkPlanApprovalsRepository : HazelcastRepository<WorkPlanApprovalsEntity, Long>

@Repository
interface IWorkPlanItemsRepository : HazelcastRepository<WorkPlanItemsEntity, Long> {
    fun findByWorkPlanId(workPlanId: WorkplanEntity?, pages: Pageable?): Page<WorkPlanItemsEntity>?
}

@Repository
interface IWorkPlanUsersRepository : HazelcastRepository<WorkPlanUsersEntity, Long> {
    fun findByWorkPlanId(workPlanId: WorkplanEntity?, pages: Pageable?): Page<WorkPlanUsersEntity>?
}

@Repository
interface IWorkPlanResourcesRepository : HazelcastRepository<WorkPlanResourcesEntity, Long> {
    fun findByWorkPlanId(workPlanId: WorkplanEntity?, pages: Pageable?): Page<WorkPlanResourcesEntity>?

}

@Repository
interface IWorkPlanBudgetLinesRepository : HazelcastRepository<WorkPlanBudgetLinesEntity, Long> {
    fun findByWorkPlanId(workPlanId: WorkplanEntity?, pages: Pageable?): Page<WorkPlanBudgetLinesEntity>?

}
