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
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface IFuelBatchRepository : HazelcastRepository<MsFuelBatchInspectionEntity, Long>, JpaSpecificationExecutor<MsFuelBatchInspectionEntity> {
    override fun findAll(): List<MsFuelBatchInspectionEntity>
    fun findAllByOrderByIdDesc(pageable: Pageable): Page<MsFuelBatchInspectionEntity>?
//    fun findAllOrderByIdAsc(): List<MsFuelBatchInspectionEntity>?
    fun findByReferenceNumber(referenceNumber: String): MsFuelBatchInspectionEntity?
    fun findByReferenceNumberAndRegionId(referenceNumber: String, regionId: Long): MsFuelBatchInspectionEntity?
    fun findByReferenceNumberAndRegionIdAndCountyId(referenceNumber: String, regionId: Long, countyId: Long): MsFuelBatchInspectionEntity?
    fun findByYearNameIdAndCountyIdAndRegionId(yearNameId: Long, countyId: Long, regionId: Long): MsFuelBatchInspectionEntity?
    fun findByYearNameIdAndMonthNameId(yearNameId: Long, monthNameId: Long): MsFuelBatchInspectionEntity?
    fun findTopByYearNameIdAndMonthNameId(yearNameId: Long, monthNameId: Long): MsFuelBatchInspectionEntity?
    fun findByYearNameIdAndCountyIdAndTownIdAndRegionId(yearNameId: Long, countyId: Long, townId: Long, regionId: Long): MsFuelBatchInspectionEntity?
    fun findByYearNameIdAndCountyIdAndTownIdAndRegionIdAndMonthNameId(
        yearNameId: Long,
        countyId: Long,
        townId: Long,
        regionId: Long,
        monthNameId: Long
    ): MsFuelBatchInspectionEntity?
    fun findAllByCountyIdAndRegionId(countyId: Long, regionId: Long, pageable: Pageable): Page<MsFuelBatchInspectionEntity>
    fun findAllByCountyIdAndRegionIdAndBatchClosed(countyId: Long, regionId: Long,batchClosed: Int, pageable: Pageable): Page<MsFuelBatchInspectionEntity>
    fun findAllByRegionId(regionId: Long, pageable: Pageable): Page<MsFuelBatchInspectionEntity>
    fun findAllByRegionIdAndBatchClosed(regionId: Long, batchClosed: Int, pageable: Pageable): Page<MsFuelBatchInspectionEntity>
}

@Repository
interface IMsRemarksFuelInspectionRepository : HazelcastRepository<MsRemarksEntity, Long>{
    fun findAllByFuelInspectionId(fuelInspectionId: Long): List<MsRemarksEntity>?
}

@Repository
interface IMsFuelTeamsRepository : HazelcastRepository<MsFuelTeamsEntity, Long>{
    fun findAllByFuelBatchId(fuelBatchId: Long): List<MsFuelTeamsEntity>?
    fun findAllByFuelBatchId(fuelBatchId: Long,pageable: Pageable): Page<MsFuelTeamsEntity>?
    fun findAllByFuelBatchIdAndAssignedOfficerID(fuelBatchId: Long,assignedOfficerID: Long, pageable: Pageable): Page<MsFuelTeamsEntity>?
    fun findByReferenceNumber(referenceNumber: String): MsFuelTeamsEntity?
}

@Repository
interface IMsFuelTeamsCountyEntityRepository : HazelcastRepository<MsFuelTeamsCountyEntity, Long>{
    fun findAllByTeamId(teamId: Long): List<MsFuelTeamsCountyEntity>?
    fun findAllByTeamId(teamId: Long,pageable: Pageable): Page<MsFuelTeamsCountyEntity>?
    fun findByReferenceNo(referenceNumber: String): MsFuelTeamsCountyEntity?
}

@Repository
interface IMsFuelInspectionRapidTestProductsEntityRepository : HazelcastRepository<MsFuelInspectionRapidTestProductsEntity, Long>{
    fun findAllByFuelInspectionId(teamId: Long): List<MsFuelInspectionRapidTestProductsEntity>?
}

@Repository
interface IWorkplanYearsCodesRepository: HazelcastRepository<WorkplanYearsCodesEntity, Long> {

    fun findByYearNameAndStatus(yearName: String, status: Int) : WorkplanYearsCodesEntity?
    fun findByStatusOrderByYearName(status: Int) : List<WorkplanYearsCodesEntity>?
}

@Repository
interface IWorkplanMonthlyCodesRepository: HazelcastRepository<WorkplanMonthlyCodesEntity, Long> {

    fun findByMonthlyNameAndStatus(monthlyName: String, status: Int) : WorkplanMonthlyCodesEntity?
    fun findByStatusOrderByMonthlyName(status: Int) : List<WorkplanMonthlyCodesEntity>?
}

@Repository
interface IFuelInspectionRepository : HazelcastRepository<MsFuelInspectionEntity, Long> {
    override fun findAll( pageable: Pageable): Page<MsFuelInspectionEntity>
    fun findAllByOrderByIdDesc( pageable: Pageable): Page<MsFuelInspectionEntity>
    fun findAllByOrderByIdAsc(): List<MsFuelInspectionEntity>
    fun findAllByBatchId(batchId: Long, pageable: Pageable): Page<MsFuelInspectionEntity>?
    fun findAllByBatchIdAndMsTeamsIdAndMsCountyId(batchId: Long,teamsId: Long,countyId: Long, pageable: Pageable): Page<MsFuelInspectionEntity>?
    fun findAllByBatchId(batchId: Long): List<MsFuelInspectionEntity>?

    fun findByReferenceNumber(referenceNumber: String): MsFuelInspectionEntity?

    @Query(
        "SELECT DISTINCT fi.* FROM DAT_KEBS_MS_FUEL_INSPECTION fi\n" +
                "JOIN DAT_KEBS_MS_FUEL_INSPECTION_OFFICERS fio ON fi.ID = fio.INSPECTION_ID\n" +
                "WHERE  fi.BATCH_ID =:batchId AND fi.MS_TEAMS_ID =:teamID AND fi.MS_COUNTY_ID =:countyID AND  fio.ASSIGNED_IO =:assignedIoID",
        nativeQuery = true
    )
    fun findAllByBatchIdAndTeamsIDAndCountyIDAndAssignOfficer(
        @Param("batchId") batchId: Long,
        @Param("teamID") teamID: Long,
        @Param("countyID") countyID: Long,
        @Param("assignedIoID") assignedIoID: Long
    ): List<MsFuelInspectionEntity>?

    @Query(
        "SELECT DISTINCT fi.* FROM DAT_KEBS_MS_FUEL_INSPECTION fi\n" +
                "JOIN DAT_KEBS_MS_FUEL_INSPECTION_OFFICERS fio ON fi.ID = fio.INSPECTION_ID\n" +
                "WHERE  fi.BATCH_ID =:batchId AND  fio.ASSIGNED_IO =:assignedIoID",
        nativeQuery = true
    )
    fun findAllByBatchIdAndAssignOfficer(
        @Param("batchId") batchId: Long,
        @Param("assignedIoID") assignedIoID: Long
    ): List<MsFuelInspectionEntity>?

//    fun findByIdAndMsProcessStatus(id: Long, msProcessStatus: Int):  MsFuelInspectionEntity?
//    fun findByUserId(userId: UsersEntity): List<WorkplanEntity>?
//    fun findByUserId(userId: UsersEntity, pages: Pageable?): Page<WorkplanEntity>?
}

@Repository
interface IFuelInspectionOfficerRepository : HazelcastRepository<MsFuelInspectionOfficersEntity, Long> {
    override fun findAll( pageable: Pageable): Page<MsFuelInspectionOfficersEntity>
    fun findAllByOrderByIdDesc( pageable: Pageable): Page<MsFuelInspectionOfficersEntity>
    fun findByMsFuelInspectionId(msFuelInspectionId: Long): MsFuelInspectionOfficersEntity?
    fun findByMsFuelInspectionIdAndStatus(msFuelInspectionId: Long, status: Int): MsFuelInspectionOfficersEntity?
//    fun findByUserId(userId: UsersEntity): List<WorkplanEntity>?
//    fun findByUserId(userId: UsersEntity, pages: Pageable?): Page<WorkplanEntity>?
}

@Repository
interface ISampleCollectionRepository : HazelcastRepository<MsSampleCollectionEntity, Long> {
    override fun findAll( pageable: Pageable): Page<MsSampleCollectionEntity>

//    @Query("SELECT * FROM DAT_KEBS_MS_SAMPLE_COLLECTION a INNER JOIN DAT_KEBS_MS_COLLECTION_PARAMETERS b ON a.ID = b.SAMPLE_COLLECTION_ID where a.ID = $P{ITEM_ID}")
//    fun filterDateRange(@Param("fromDate") fromDate: Time, @Param("toDate") toDate: Time): List<ConsignmentDocumentEntity>?

    fun findByWorkPlanGeneratedID(workPlanGeneratedID: Long): MsSampleCollectionEntity?
    fun findByMsFuelInspectionId(msFuelInspectionId: Long): MsSampleCollectionEntity?
//    fun findByUserId(userId: UsersEntity): List<WorkplanEntity>?
//    fun findByUserId(userId: UsersEntity, pages: Pageable?): Page<WorkplanEntity>?
}

@Repository
interface IMSSampleSubmissionRepository : HazelcastRepository<MsSampleSubmissionEntity, Long> {
    override fun findAll( pageable: Pageable): Page<MsSampleSubmissionEntity>

    fun findByWorkPlanGeneratedID(workPlanGeneratedID: Long): List<MsSampleSubmissionEntity>?
    fun findByWorkPlanGeneratedIDAndId(workPlanGeneratedID: Long,id: Long): MsSampleSubmissionEntity

    fun findByMsFuelInspectionId(msFuelInspectionId: Long): List<MsSampleSubmissionEntity>?

    fun findBySampleCollectionNumber(sampleCollectionNumber: Long): MsSampleSubmissionEntity?
    fun findBySampleCollectionNumberAndId(sampleCollectionNumber: Long, id: Long): MsSampleSubmissionEntity?
//    fun findByUserId(userId: UsersEntity): List<WorkplanEntity>?
//    fun findByUserId(userId: UsersEntity, pages: Pageable?): Page<WorkplanEntity>?
}

@Repository
interface ISampleSubmitParameterRepository : HazelcastRepository<MsLaboratoryParametersEntity, Long> {
//    override fun findAll( pageable: Pageable): Page<MsLaboratoryParametersEntity>?

    fun findBySampleSubmissionId(sampleSubmissionId: Long, pageable: Pageable): Page<MsLaboratoryParametersEntity>?
    fun findBySampleSubmissionId(sampleSubmissionId: Long): List<MsLaboratoryParametersEntity>?
//    fun findByUserId(userId: UsersEntity): List<WorkplanEntity>?
//    fun findByUserId(userId: UsersEntity, pages: Pageable?): Page<WorkplanEntity>?
}

@Repository
interface ISampleCollectParameterRepository : HazelcastRepository<MsCollectionParametersEntity, Long> {
    override fun findAll( pageable: Pageable): Page<MsCollectionParametersEntity>

    fun findBySampleCollectionId(sampleCollectionId: Long, pageable: Pageable): Page<MsCollectionParametersEntity>
    fun findBySampleCollectionId(sampleCollectionId: Long): List<MsCollectionParametersEntity>?
//    fun findByUserId(userId: UsersEntity): List<WorkplanEntity>?
//    fun findByUserId(userId: UsersEntity, pages: Pageable?): Page<WorkplanEntity>?
}

@Repository
interface IMsUploadsRepository : HazelcastRepository<MsUploadsEntity, Long> {
    override fun findAll( pageable: Pageable): Page<MsUploadsEntity>

    fun findAllByMsComplaintIdAndComplaintUploads(msComplaintId: Long, complaintUploads: Int): List<MsUploadsEntity>?
    fun findAllByMsWorkplanGeneratedIdAndWorkPlanUploads(msWorkplanGeneratedId: Long, workPlanUploads: Int): List<MsUploadsEntity>?
    fun findAllByMsFuelInspectionIdAndFuelPlanUploads(msFuelInspectionId: Long, fuelPlanUploads: Int): List<MsUploadsEntity>?

}
