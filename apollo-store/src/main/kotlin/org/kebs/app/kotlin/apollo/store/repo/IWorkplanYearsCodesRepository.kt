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
    fun findByUuid(uuid: String) : WorkPlanCreatedEntity?
    fun findByReferenceNumber(referenceNumber: String) : WorkPlanCreatedEntity?
    fun findByReferenceNumberAndWorkPlanRegion(referenceNumber: String,workPlanRegion: Long) : WorkPlanCreatedEntity?
    fun findByUserCreatedId(userCreatedId: UsersEntity, pageable: Pageable): Page<WorkPlanCreatedEntity>?
    fun findByWorkPlanRegion(workPlanRegion: Long, pageable: Pageable): Page<WorkPlanCreatedEntity>?
}
