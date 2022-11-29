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
interface ISampleCollectionViewRepository : HazelcastRepository<MsSampleCollectionView, Long> {
    override fun findAll(pageable: Pageable): Page<MsSampleCollectionView>

    fun findBySampleCollectionId(sampleCollectionId: Long): List<MsSampleCollectionView>
}

@Repository
interface IMsFieldReportViewRepository : HazelcastRepository<MsFieldReportView, String> {
    override fun findAll(pageable: Pageable): Page<MsFieldReportView>

    fun findByMsWorkplanGeneratedId(msWorkPlanGeneratedId: String): List<MsFieldReportView>
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
interface IMsComplaintFeedbackViewRepository : HazelcastRepository<MsComplaintFeedbackViewEntity, Long> {
    override fun findAll(pageable: Pageable): Page<MsComplaintFeedbackViewEntity>

    fun findAllByReferenceNumber(referenceNumber: String): List<MsComplaintFeedbackViewEntity>
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
interface IMsAllocatedTasksCpViewRepository : HazelcastRepository<MsAllocatedTasksCpViewEntity, Long> {
    override fun findAll(pageable: Pageable): Page<MsAllocatedTasksCpViewEntity>

    fun countByAssignedIo(assignedIo: Long): Long
    fun countByAssignedIoAndMsComplaintEndedStatus(assignedIo: Long,msComplaintEndedStatus:Long): Long
    fun countByAssignedIoAndTaskOverDue(assignedIo: Long, taskOverDue: String): Long
    fun findAllByReferenceNumber(referenceNumber: String): List<MsAllocatedTasksCpViewEntity>
}

@Repository
interface IMsAllocatedTasksWpViewRepository : HazelcastRepository<MsAllocatedTasksWpViewEntity, Long> {
    override fun findAll(pageable: Pageable): Page<MsAllocatedTasksWpViewEntity>

    fun countByOfficerIdAndComplaintIdIsNotNull(officerId: Long): Long
    fun countByOfficerIdAndComplaintIdIsNull(officerId: Long): Long
    fun countByOfficerIdAndTaskOverDueAndComplaintIdIsNotNull(officerId: Long, taskOverDue: String): Long
    fun countByOfficerIdAndTaskOverDueAndComplaintIdIsNull(officerId: Long, taskOverDue: String): Long
    fun findAllByReferenceNumber(referenceNumber: String): List<MsAllocatedTasksWpViewEntity>
}

interface IMsTasksPendingAllocationCpViewRepository : HazelcastRepository<MsTasksPendingAllocationCpViewEntity, Long> {
    override fun findAll(pageable: Pageable): Page<MsTasksPendingAllocationCpViewEntity>

    fun countByHodAssignedIsNullAndMsComplaintEndedStatus(msComplaintEndedStatus: Long): Long
    fun countByHodAssignedIsNullAndMsComplaintEndedStatusIsNull(): Long
    fun countByHodAssignedAndMsComplaintEndedStatusIsNullAndHofAssignedIsNull(hodAssigned: Long): Long
    fun countByHodAssignedIsNotNullAndMsComplaintEndedStatusIsNullAndHofAssignedIsNull(): Long
    fun countByHodAssignedIsNotNullAndMsComplaintEndedStatusIsNullAndHofAssignedIsNotNullAndAssignedIoIsNull(): Long
    fun countByHofAssignedAndMsComplaintEndedStatusIsNullAndAssignedIoIsNull(hofAssigned: Long): Long

}

@Repository
interface IMsTasksPendingAllocationWpViewRepository : HazelcastRepository<MsTasksPendingAllocationWpViewEntity, Long> {
    override fun findAll(pageable: Pageable): Page<MsTasksPendingAllocationWpViewEntity>

    fun countByOfficerId(officerId: Long): Long
    fun countByOfficerIdAndTaskOverDueAndComplaintIdIsNotNull(officerId: Long, taskOverDue: String): Long
    fun countByOfficerIdAndTaskOverDueAndComplaintIdIsNull(officerId: Long, taskOverDue: String): Long
    fun findAllByReferenceNumber(referenceNumber: String): List<MsTasksPendingAllocationWpViewEntity>
}