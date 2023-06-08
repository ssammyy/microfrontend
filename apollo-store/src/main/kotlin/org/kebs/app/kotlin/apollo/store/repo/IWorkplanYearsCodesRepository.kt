package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import org.kebs.app.kotlin.apollo.store.model.WorkPlanCreatedEntity
import org.kebs.app.kotlin.apollo.store.model.WorkplanYearsCodesEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.hazelcast.repository.HazelcastRepository



interface IWorkPlanCreatedRepository: HazelcastRepository<WorkPlanCreatedEntity, Long> {
    override fun findAll(): List<WorkPlanCreatedEntity>
    fun findByUserCreatedIdAndYearNameId(userCreatedId: UsersEntity, yearNameId: WorkplanYearsCodesEntity) : WorkPlanCreatedEntity?
    fun findByUserCreatedIdAndYearNameIdAndComplaintStatus(userCreatedId: UsersEntity, yearNameId: WorkplanYearsCodesEntity,complaintStatus: Int) : WorkPlanCreatedEntity?
    fun findByUserCreatedIdAndYearNameIdAndWorkPlanStatus(userCreatedId: UsersEntity, yearNameId: WorkplanYearsCodesEntity, workPlanStatus: Int) : WorkPlanCreatedEntity?
    fun findByUuid(uuid: String) : WorkPlanCreatedEntity?
    fun findByReferenceNumber(referenceNumber: String) : WorkPlanCreatedEntity?
    fun findByReferenceNumberAndWorkPlanRegion(referenceNumber: String,workPlanRegion: Long) : WorkPlanCreatedEntity?
    fun findByUserCreatedId(userCreatedId: UsersEntity, pageable: Pageable): Page<WorkPlanCreatedEntity>?
    fun findAllByComplaintStatus(complaintStatus: Int, pageable: Pageable): Page<WorkPlanCreatedEntity>?
    fun findAllByWorkPlanStatus(workPlanStatus: Int, pageable: Pageable): Page<WorkPlanCreatedEntity>?
    fun findByUserCreatedIdAndWorkPlanStatus(userCreatedId: UsersEntity, pageable: Pageable, workPlanStatus: Int): Page<WorkPlanCreatedEntity>?
    fun findByUserCreatedIdAndComplaintStatus(userCreatedId: UsersEntity, pageable: Pageable, complaintStatus: Int): Page<WorkPlanCreatedEntity>?
    fun findByUserCreatedIdAndBatchClosed(userCreatedId: UsersEntity,batchClosed: Int, pageable: Pageable): Page<WorkPlanCreatedEntity>?
    fun findByUserCreatedIdAndBatchClosedAndWorkPlanStatus(userCreatedId: UsersEntity,batchClosed: Int,workPlanStatus: Int, pageable: Pageable): Page<WorkPlanCreatedEntity>?
    fun findByUserCreatedIdAndBatchClosedAndComplaintStatus(userCreatedId: UsersEntity,batchClosed: Int,complaintStatus: Int, pageable: Pageable): Page<WorkPlanCreatedEntity>?
    fun findByWorkPlanRegion(workPlanRegion: Long, pageable: Pageable): Page<WorkPlanCreatedEntity>?
    fun findByWorkPlanRegionAndComplaintStatus(workPlanRegion: Long, pageable: Pageable,complaintStatus: Int): Page<WorkPlanCreatedEntity>?
    fun findByWorkPlanRegionAndWorkPlanStatus(workPlanRegion: Long, pageable: Pageable,workPlanStatus: Int): Page<WorkPlanCreatedEntity>?
    fun findByWorkPlanRegionAndBatchClosed(workPlanRegion: Long,batchClosed: Int, pageable: Pageable): Page<WorkPlanCreatedEntity>?
    fun findByWorkPlanRegionAndBatchClosedAndComplaintStatus(workPlanRegion: Long,batchClosed: Int,complaintStatus: Int,pageable: Pageable): Page<WorkPlanCreatedEntity>?
    fun findByWorkPlanRegionAndBatchClosedAndWorkPlanStatus(workPlanRegion: Long,batchClosed: Int,workPlanStatus: Int,pageable: Pageable): Page<WorkPlanCreatedEntity>?
    fun findAllByBatchClosed(batchClosed: Int, pageable: Pageable): Page<WorkPlanCreatedEntity>?
    fun findAllByBatchClosedAndComplaintStatus(batchClosed: Int,complaintStatus: Int, pageable: Pageable): Page<WorkPlanCreatedEntity>?
    fun findAllByBatchClosedAndWorkPlanStatus(batchClosed: Int,workPlanStatus: Int, pageable: Pageable): Page<WorkPlanCreatedEntity>?
}
