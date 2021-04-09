package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.DmarkForeignApplicationsEntity
import org.kebs.app.kotlin.apollo.store.model.PermitApplicationEntity
import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.stereotype.Repository


@Repository
interface IDmarkForeignApplicationsRepository : HazelcastRepository<DmarkForeignApplicationsEntity, Long>{
    fun findBySaveReasonAndStatus(type: String, status: Int): List<DmarkForeignApplicationsEntity>
    fun findByStatus(status: Int): List<DmarkForeignApplicationsEntity>
    fun findByStatusAndUserId(status: Int?, userId: UsersEntity): List<DmarkForeignApplicationsEntity>
}