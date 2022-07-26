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
import org.springframework.stereotype.Repository

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
interface IWorkPlanGenerateRepository : HazelcastRepository<MsWorkPlanGeneratedEntity, Long> {
    override fun findAll( pageable: Pageable): Page<MsWorkPlanGeneratedEntity>
    fun findAllByOrderByIdDesc( pageable: Pageable): Page<MsWorkPlanGeneratedEntity>
    fun findByWorkPlanYearId(workPlanYearId: Long,pageable: Pageable): Page<MsWorkPlanGeneratedEntity>?
    fun findByWorkPlanYearIdAndMsProcessEndedStatus(workPlanYearId: Long,msProcessEndedStatus: Int,pageable: Pageable): Page<MsWorkPlanGeneratedEntity>?

    fun findByWorkPlanYearIdAndOfficerIdAndUserTaskId(
        workPlanYearId: Long,
        officerId: Long,
        userTaskId: Long,
        pageable: Pageable
    ): Page<MsWorkPlanGeneratedEntity>?

    fun findByWorkPlanYearIdAndHofAssignedAndUserTaskId(
        workPlanYearId: Long,
        hofAssigned: Long,
        userTaskId: Long,
        pageable: Pageable
    ): Page<MsWorkPlanGeneratedEntity>?

    fun findByWorkPlanYearIdAndHodRmAssignedAndUserTaskId(
        workPlanYearId: Long,
        hodRmAssigned: Long,
        userTaskId: Long,
        pageable: Pageable
    ): Page<MsWorkPlanGeneratedEntity>?

    fun findByWorkPlanYearIdAndUserTaskId(
        workPlanYearId: Long,
        userTaskId: Long,
        pageable: Pageable
    ): Page<MsWorkPlanGeneratedEntity>?

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

    fun findByWorkPlanGeneratedID(workPlanGeneratedID: Long):MsInspectionInvestigationReportEntity?
//    fun findByUserId(userId: UsersEntity): List<WorkplanEntity>?
//    fun findByUserId(userId: UsersEntity, pages: Pageable?): Page<WorkplanEntity>?
}

@Repository
interface IMSSeizureDeclarationRepository : HazelcastRepository<MsSeizureDeclarationEntity, Long> {
    override fun findAll( pageable: Pageable): Page<MsSeizureDeclarationEntity>

    fun findByWorkPlanGeneratedID(workPlanGeneratedID: Long): MsSeizureDeclarationEntity?
//    fun findByUserId(userId: UsersEntity): List<WorkplanEntity>?
//    fun findByUserId(userId: UsersEntity, pages: Pageable?): Page<WorkplanEntity>?
}

@Repository
interface IDataReportRepository : HazelcastRepository<MsDataReportEntity, Long> {
    override fun findAll( pageable: Pageable): Page<MsDataReportEntity>

    fun findByWorkPlanGeneratedID(workPlanGeneratedID: Long): MsDataReportEntity?
//    fun findByUserId(userId: UsersEntity): List<WorkplanEntity>?
//    fun findByUserId(userId: UsersEntity, pages: Pageable?): Page<WorkplanEntity>?
}

@Repository
interface ISampleCollectionViewRepository : HazelcastRepository<MsSampleCollectionEntityView, Long> {
    override fun findAll(pageable: Pageable): Page<MsSampleCollectionEntityView>

    fun findBySampleCollectionId(sampleCollectionId: Long): List<MsSampleCollectionEntityView>
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
