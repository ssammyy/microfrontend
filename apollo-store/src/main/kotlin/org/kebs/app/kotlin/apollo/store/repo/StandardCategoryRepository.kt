package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.DepartmentsEntity
import org.kebs.app.kotlin.apollo.store.model.StandardsCategoryEntity
import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.stereotype.Repository

@Repository
interface IStandardCategoryRepository : HazelcastRepository<StandardsCategoryEntity, Long> {
    fun findByStatusOrderByStandardCategory(status: Int): List<StandardsCategoryEntity>?
//    fun findByStatus(status: Int): List<StandardsCategoryEntity>?

}