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

import org.kebs.app.kotlin.apollo.store.model.MsLaboratoryParametersEntity
import org.kebs.app.kotlin.apollo.store.model.MsSampleSubmissionEntity
import org.kebs.app.kotlin.apollo.store.model.MsWorkPlanGeneratedEntity
import org.kebs.app.kotlin.apollo.store.model.WorkplanYearsCodesEntity
import org.kebs.app.kotlin.apollo.store.model.ms.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface IFuelBatchRepository : HazelcastRepository<MsFuelBatchInspectionEntity, Long>, JpaSpecificationExecutor<MsFuelBatchInspectionEntity> {
    override fun findAll(): List<MsFuelBatchInspectionEntity>
    fun findAllByOrderByIdDesc(pageable: Pageable): Page<MsFuelBatchInspectionEntity>?
    fun findAllOrderByIdAsc(): List<MsFuelBatchInspectionEntity>?
    fun findByReferenceNumber(referenceNumber: String): MsFuelBatchInspectionEntity?
    fun findByYearNameIdAndCountyIdAndRegionId(yearNameId: Long, countyId: Long, regionId: Long): MsFuelBatchInspectionEntity?
}

@Repository
interface IWorkplanYearsCodesRepository: HazelcastRepository<WorkplanYearsCodesEntity, Long> {

    fun findByYearNameAndStatus(yearName: String, status: Int) : WorkplanYearsCodesEntity?
    fun findByStatusOrderByYearName(status: Int) : List<WorkplanYearsCodesEntity>?
}

@Repository
interface IFuelInspectionRepository : HazelcastRepository<MsFuelInspectionEntity, Long> {
    override fun findAll( pageable: Pageable): Page<MsFuelInspectionEntity>
    fun findAllByOrderByIdDesc( pageable: Pageable): Page<MsFuelInspectionEntity>
    fun findAllByOrderByIdAsc(): List<MsFuelInspectionEntity>
    fun findAllByBatchId(batchId: Long, pageable: Pageable): Page<MsFuelInspectionEntity>?
    fun findAllByBatchId(batchId: Long): List<MsFuelInspectionEntity>?

    fun findByReferenceNumber(referenceNumber: String): MsFuelInspectionEntity?

//    fun findByIdAndMsProcessStatus(id: Long, msProcessStatus: Int):  MsFuelInspectionEntity?
//    fun findByUserId(userId: UsersEntity): List<WorkplanEntity>?
//    fun findByUserId(userId: UsersEntity, pages: Pageable?): Page<WorkplanEntity>?
}

@Repository
interface IFuelInspectionOfficerRepository : HazelcastRepository<MsFuelInspectionOfficersEntity, Long> {
    override fun findAll( pageable: Pageable): Page<MsFuelInspectionOfficersEntity>
    fun findAllByOrderByIdDesc( pageable: Pageable): Page<MsFuelInspectionOfficersEntity>
    fun findByMsFuelInspectionId(msFuelInspectionId: MsFuelInspectionEntity): MsFuelInspectionOfficersEntity?
    fun findByMsFuelInspectionIdAndStatus(msFuelInspectionId: MsFuelInspectionEntity, status: Int): MsFuelInspectionOfficersEntity?
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

    fun findByWorkPlanGeneratedID(workPlanGeneratedID: Long): MsSampleSubmissionEntity?

    fun findByMsFuelInspectionId(msFuelInspectionId: Long): MsSampleSubmissionEntity?
    fun findBySampleCollectionNumber(sampleCollectionNumber: Long): MsSampleSubmissionEntity?
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
