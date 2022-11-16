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
interface IComplaintRepository : HazelcastRepository<ComplaintEntity, Long>, JpaSpecificationExecutor<ComplaintEntity> {
    override fun findAll(): List<ComplaintEntity>
    fun findAllByOrderByIdDesc(pageable: Pageable): Page<ComplaintEntity>
    fun findAllByMsComplaintEndedStatusOrderByIdDesc(msComplaintEndedStatus: Int, pageable: Pageable): Page<ComplaintEntity>
    fun findByHodAssigned(hodAssigned: Long): List<ComplaintEntity>
    fun findByHodAssigned(hodAssigned: Long,pageable: Pageable): Page<ComplaintEntity>
    fun findByHodAssignedAndUserTaskId(hodAssigned: Long,userTaskID: Long,pageable: Pageable): Page<ComplaintEntity>
    fun findByHofAssigned(hofAssigned: Long): List<ComplaintEntity>
    fun findByHofAssigned(hofAssigned: Long,pageable: Pageable): Page<ComplaintEntity>
    fun findByHofAssignedAndUserTaskId(hofAssigned: Long,userTaskID: Long,pageable: Pageable): Page<ComplaintEntity>
    fun findByAssignedIo(assignedIo: Long): List<ComplaintEntity>
    fun findByAssignedIo(assignedIo: Long,pageable: Pageable): Page<ComplaintEntity>
    fun findByAssignedIoAndUserTaskId(assignedIo: Long,userTaskID: Long,pageable: Pageable): Page<ComplaintEntity>

    @Query(
        "SELECT cp.* FROM  DAT_KEBS_MS_COMPLAINT cp, DAT_KEBS_MS_COMPLAINT_LOCATION cpl\n" +
                "WHERE cp.id = cpl.COMPLAINT_ID and  cp.USER_TASK_ID =:userTaskID  and cpl.REGION = :regionID and cpl.COUNTY = :countID",
        nativeQuery = true
    )
    fun findByUserTaskId(
        @Param("userTaskID") userTaskID: Long,
        @Param("regionID") regionID: Long,
        @Param("countID") countID: Long,
    ): List<ComplaintEntity>

    @Query(
        "SELECT cp.* FROM  DAT_KEBS_MS_COMPLAINT cp, DAT_KEBS_MS_COMPLAINT_LOCATION cpl\n" +
                "WHERE cp.id = cpl.COMPLAINT_ID and  cp.USER_TASK_ID IS NOT NULL and cpl.REGION = :regionID and cpl.COUNTY = :countID",
        nativeQuery = true
    )
    fun findOngoingTask(
        @Param("regionID") regionID: Long,
        @Param("countID") countID: Long,
    ): List<ComplaintEntity>

    @Query(
        "SELECT cp.* FROM  DAT_KEBS_MS_COMPLAINT cp, DAT_KEBS_MS_COMPLAINT_LOCATION cpl\n" +
                "WHERE cp.id = cpl.COMPLAINT_ID  and  cp.USER_TASK_ID =:userTaskID and  cp.MS_PROCESS_ID =:msProcessID AND cpl.REGION = :regionID and cpl.COUNTY = :countID",
        nativeQuery = true
    )
    fun findNewComplaint(
        @Param("userTaskID") userTaskID: Long,
        @Param("msProcessID") msProcessID: Long,
        @Param("regionID") regionID: Long,
        @Param("countID") countID: Long,
    ): List<ComplaintEntity>

    //    fun findByIdAndMsProcessStatus(id: Long, msProcessStatus: Int, pageable: Pageable):  Page<ComplaintEntity>?
    fun findByIdAndMsProcessStatus(id: Long, msProcessStatus: Int): ComplaintEntity?
    fun findByUuid(uuid: String): ComplaintEntity?
    fun findByReferenceNumber(referenceNumber: String): ComplaintEntity?

//    fun findAllByUserNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(userName: String?, email: String?, firstName: String?, lastName: String?): List<ComplaintEntity>?

//    fun findAllByReferenceNumberContainingIgnoreCaseOrTransactionDateContainingIgnoreCase(referenceNumber: String?, transactionDate: Date?): List<ComplaintEntity>?
}

@Repository
interface IComplaintAcceptWorkFlowRepository : HazelcastRepository<ComplaintAcceptWorkFlowEntity, Long>

@Repository
interface IComplaintCompanyRepository : HazelcastRepository<ComplaintCompanyEntity, Long>

@Repository
interface IComplaintDocumentsRepository : HazelcastRepository<ComplaintDocumentsEntity, Long>{
    fun findByComplaintId(complaintId: Long): ComplaintDocumentsEntity?
}

@Repository
interface IComplaintCustomersRepository : HazelcastRepository<ComplaintCustomersEntity, Long>{
    fun findByComplaintId(complaintId: Long): ComplaintCustomersEntity?
}

@Repository
interface IComplaintHandlersRepository : HazelcastRepository<ComplaintHandlersEntity, Long>

@Repository
interface IComplaintKebsRemarksRepository : HazelcastRepository<ComplaintKebsRemarksEntity, Long>

@Repository
interface IComplaintOfficerRepository : HazelcastRepository<ComplaintOfficerEntity, Long>

@Repository
interface IComplaintRemarksRepository : HazelcastRepository<ComplaintRemarksEntity, Long>

@Repository
interface IComplaintRemediesRepository : HazelcastRepository<ComplaintRemediesEntity, Long>

@Repository
interface IComplaintWitnessRepository : HazelcastRepository<ComplaintWitnessEntity, Long>

@Repository
interface IComplaintLocationRepository : HazelcastRepository<ComplaintLocationEntity, Long>{
    fun findByComplaintId(complaintId: Long): ComplaintLocationEntity?
}

@Repository
interface IMsTaskNotificationsRepository : HazelcastRepository<MsTaskNotificationsEntity, Long>{
    fun findAllByNotificationType(notificationType: String):  List<MsTaskNotificationsEntity>?
    fun findAllByReadStatus(readStatus: Int): List<MsTaskNotificationsEntity>?
}

@Repository
interface IPredefinedResourcesRequiredRepository : HazelcastRepository<PredefinedResourcesRequiredEntity, Long>{
    fun findAllByStatus(status: Long): PredefinedResourcesRequiredEntity?
}

@Repository
interface IMsRemarksComplaintRepository : HazelcastRepository<MsRemarksEntity, Long>{
    fun findAllByComplaintId(complaintId: Long): List<MsRemarksEntity>?
    fun findAllByComplaintIdOrderByIdAsc(complaintId: Long): List<MsRemarksEntity>?
    fun findAllByFuelInspectionId(fuelInspectionId: Long): List<MsRemarksEntity>?
    fun findAllByFuelInspectionIdOrderByIdAsc(fuelInspectionId: Long): List<MsRemarksEntity>?
    fun findAllByWorkPlanId(workPlanId: Long): List<MsRemarksEntity>?
    fun findAllByWorkPlanIdOrderByIdAsc(workPlanId: Long): List<MsRemarksEntity>?
}

@Repository
interface IMsProcessNamesRepository : HazelcastRepository<MsProcessNamesEntity, Long>{
    fun findAllByComplaintStatus(complaintStatus: Int): List<MsProcessNamesEntity>?
    fun findAllByFuelStatus(fuelStatus: Int): List<MsProcessNamesEntity>?
    fun findAllByWorkPlanStatus(workPlanStatus: Int): List<MsProcessNamesEntity>?
    fun findByComplaintStatusAndId(complaintStatus: Int, id: Long): MsProcessNamesEntity?
    fun findByFuelStatusAndId(fuelStatus: Int, id: Long): MsProcessNamesEntity?
    fun findByWorkPlanStatusAndId(workPlanStatus: Int, id: Long): MsProcessNamesEntity?
}
