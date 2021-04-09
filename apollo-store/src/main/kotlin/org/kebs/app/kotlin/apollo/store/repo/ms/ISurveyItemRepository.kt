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
import org.kebs.app.kotlin.apollo.store.model.ms.ComplaintEntity
import org.kebs.app.kotlin.apollo.store.model.ms.MsTypesEntity
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
    fun findByWorkPlanGeneratedID(workPlanGeneratedID: MsWorkPlanGeneratedEntity): MsPreliminaryReportEntity?
    fun findByWorkPlanGeneratedIDAndStatus(workPlanGeneratedID: MsWorkPlanGeneratedEntity, status: Int, pageable: Pageable): Page<MsPreliminaryReportEntity>?
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
//    fun findByUserId(userId: UsersEntity, pages: Pageable?): Page<WorkplanEntity>?
}

@Repository
interface IWorkPlanGenerateRepository : HazelcastRepository<MsWorkPlanGeneratedEntity, Long> {
    override fun findAll( pageable: Pageable): Page<MsWorkPlanGeneratedEntity>
    fun findAllByOrderByIdDesc( pageable: Pageable): Page<MsWorkPlanGeneratedEntity>
    fun findByWorkPlanYearId(workPlanYearId: Long): List<MsWorkPlanGeneratedEntity>?
    fun findByUuid(uuid: String): MsWorkPlanGeneratedEntity?
//    fun findByUserId(userId: UsersEntity, pages: Pageable?): Page<WorkplanEntity>?
}

@Repository
interface IFuelInspectionRepository : HazelcastRepository<MsFuelInspectionEntity, Long> {
    override fun findAll( pageable: Pageable): Page<MsFuelInspectionEntity>
    fun findAllByOrderByIdDesc( pageable: Pageable): Page<MsFuelInspectionEntity>
    fun findAllByOrderByIdAsc(): List<MsFuelInspectionEntity>

//    fun findByIdAndMsProcessStatus(id: Long, msProcessStatus: Int):  MsFuelInspectionEntity?
//    fun findByUserId(userId: UsersEntity): List<WorkplanEntity>?
//    fun findByUserId(userId: UsersEntity, pages: Pageable?): Page<WorkplanEntity>?
}

@Repository
interface IFuelRemediationRepository : HazelcastRepository<MsFuelRemediationEntity, Long> {
    override fun findAll( pageable: Pageable): Page<MsFuelRemediationEntity>
    fun findAllByOrderByIdDesc( pageable: Pageable): Page<MsFuelRemediationEntity>

    fun findByFuelInspectionId(fuelInspectionId: MsFuelInspectionEntity):  MsFuelRemediationEntity?
//    fun findByUserId(userId: UsersEntity): List<WorkplanEntity>?
//    fun findByUserId(userId: UsersEntity, pages: Pageable?): Page<WorkplanEntity>?
}

@Repository
interface IFuelRemediationInvoiceRepository : HazelcastRepository<MsFuelRemedyInvoicesEntity, Long> {
    override fun findAll( pageable: Pageable): Page<MsFuelRemedyInvoicesEntity>
    fun findAllByOrderByIdDesc( pageable: Pageable): Page<MsFuelRemedyInvoicesEntity>

    fun findByFuelInspectionId(fuelInspectionId: MsFuelInspectionEntity):  MsFuelRemedyInvoicesEntity?


    fun findFirstByFuelInspectionId(fuelInspectionId: MsFuelInspectionEntity): List<MsFuelRemedyInvoicesEntity>
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
interface IFuelInspectionOfficerRepository : HazelcastRepository<MsFuelInspectionOfficersEntity, Long> {
    override fun findAll( pageable: Pageable): Page<MsFuelInspectionOfficersEntity>
    fun findAllByOrderByIdDesc( pageable: Pageable): Page<MsFuelInspectionOfficersEntity>
    fun findByMsFuelInspectionId(msFuelInspectionId: MsFuelInspectionEntity): MsFuelInspectionOfficersEntity?
//    fun findByUserId(userId: UsersEntity): List<WorkplanEntity>?
//    fun findByUserId(userId: UsersEntity, pages: Pageable?): Page<WorkplanEntity>?
}

@Repository
interface IMSSampleSubmissionRepository : HazelcastRepository<MsSampleSubmissionEntity, Long> {
    override fun findAll( pageable: Pageable): Page<MsSampleSubmissionEntity>

    fun findByWorkPlanGeneratedID(workPlanGeneratedID: MsWorkPlanGeneratedEntity): MsSampleSubmissionEntity?

    fun findByMsFuelInspectionId(msFuelInspectionId: MsFuelInspectionEntity): MsSampleSubmissionEntity?
//    fun findByUserId(userId: UsersEntity): List<WorkplanEntity>?
//    fun findByUserId(userId: UsersEntity, pages: Pageable?): Page<WorkplanEntity>?
}

@Repository
interface IPreliminaryOutletsRepository : HazelcastRepository<MsPreliminaryReportOutletsVisitedEntity, Long> {
    override fun findAll( pageable: Pageable): Page<MsPreliminaryReportOutletsVisitedEntity>

    fun findByPreliminaryReportID(preliminaryReportID: MsPreliminaryReportEntity, pageable: Pageable): Page<MsPreliminaryReportOutletsVisitedEntity>
    fun findByPreliminaryReportID(preliminaryReportID: MsPreliminaryReportEntity): List<MsPreliminaryReportOutletsVisitedEntity>

    fun findByPreliminaryReportIDAndStatus(preliminaryReportID: MsPreliminaryReportEntity, status: Int): MsPreliminaryReportOutletsVisitedEntity?
//    fun findByUserId(userId: UsersEntity): List<WorkplanEntity>?
//    fun findByUserId(userId: UsersEntity, pages: Pageable?): Page<WorkplanEntity>?
}

@Repository
interface IMSInvestInspectReportRepository : HazelcastRepository<MsInspectionInvestigationReportEntity, Long> {
    override fun findAll( pageable: Pageable): Page<MsInspectionInvestigationReportEntity>
//    fun findByUserId(userId: UsersEntity): List<WorkplanEntity>?
//    fun findByUserId(userId: UsersEntity, pages: Pageable?): Page<WorkplanEntity>?
}

@Repository
interface IMSSeizureDeclarationRepository : HazelcastRepository<MsSeizureDeclarationEntity, Long> {
    override fun findAll( pageable: Pageable): Page<MsSeizureDeclarationEntity>
//    fun findByUserId(userId: UsersEntity): List<WorkplanEntity>?
//    fun findByUserId(userId: UsersEntity, pages: Pageable?): Page<WorkplanEntity>?
}

@Repository
interface IDataReportRepository : HazelcastRepository<MsDataReportEntity, Long> {
    override fun findAll( pageable: Pageable): Page<MsDataReportEntity>

    fun findByWorkPlanGeneratedID(workPlanGeneratedID: MsWorkPlanGeneratedEntity): MsDataReportEntity?
//    fun findByUserId(userId: UsersEntity): List<WorkplanEntity>?
//    fun findByUserId(userId: UsersEntity, pages: Pageable?): Page<WorkplanEntity>?
}

@Repository
interface ISampleCollectionViewRepository : HazelcastRepository<MsSampleCollectionEntityView, Long> {
    override fun findAll(pageable: Pageable): Page<MsSampleCollectionEntityView>

    fun findBySampleCollectionId(sampleCollectionId: Long): List<MsSampleCollectionEntityView>
}

@Repository
interface ISampleCollectionRepository : HazelcastRepository<MsSampleCollectionEntity, Long> {
    override fun findAll( pageable: Pageable): Page<MsSampleCollectionEntity>

//    @Query("SELECT * FROM DAT_KEBS_MS_SAMPLE_COLLECTION a INNER JOIN DAT_KEBS_MS_COLLECTION_PARAMETERS b ON a.ID = b.SAMPLE_COLLECTION_ID where a.ID = $P{ITEM_ID}")
//    fun filterDateRange(@Param("fromDate") fromDate: Time, @Param("toDate") toDate: Time): List<ConsignmentDocumentEntity>?

    fun findByWorkPlanGeneratedID(workPlanGeneratedID: MsWorkPlanGeneratedEntity): MsSampleCollectionEntity?
    fun findByMsFuelInspectionId(msFuelInspectionId: MsFuelInspectionEntity): MsSampleCollectionEntity?
//    fun findByUserId(userId: UsersEntity): List<WorkplanEntity>?
//    fun findByUserId(userId: UsersEntity, pages: Pageable?): Page<WorkplanEntity>?
}

@Repository
interface IDataReportParameterRepository : HazelcastRepository<MsDataReportParametersEntity, Long> {
    override fun findAll( pageable: Pageable): Page<MsDataReportParametersEntity>

    fun findByDataReportId(dataReportId: MsDataReportEntity, pageable: Pageable): Page<MsDataReportParametersEntity>
    fun findByDataReportId(dataReportId: MsDataReportEntity): List<MsDataReportParametersEntity>?
//    fun findByUserId(userId: UsersEntity): List<WorkplanEntity>?
//    fun findByUserId(userId: UsersEntity, pages: Pageable?): Page<WorkplanEntity>?
}

@Repository
interface ISampleCollectParameterRepository : HazelcastRepository<MsCollectionParametersEntity, Long> {
    override fun findAll( pageable: Pageable): Page<MsCollectionParametersEntity>

    fun findBySampleCollectionId(sampleCollectionId: MsSampleCollectionEntity, pageable: Pageable): Page<MsCollectionParametersEntity>
    fun findBySampleCollectionId(sampleCollectionId: MsSampleCollectionEntity): List<MsCollectionParametersEntity>?
//    fun findByUserId(userId: UsersEntity): List<WorkplanEntity>?
//    fun findByUserId(userId: UsersEntity, pages: Pageable?): Page<WorkplanEntity>?
}
@Repository
interface ISampleSubmitParameterRepository : HazelcastRepository<MsLaboratoryParametersEntity, Long> {
//    override fun findAll( pageable: Pageable): Page<MsLaboratoryParametersEntity>?

    fun findBySampleSubmissionId(sampleSubmissionId: MsSampleSubmissionEntity, pageable: Pageable): Page<MsLaboratoryParametersEntity>?
    fun findBySampleSubmissionId(sampleSubmissionId: MsSampleSubmissionEntity): List<MsLaboratoryParametersEntity>?
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