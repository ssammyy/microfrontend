package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.KebsProductCategoriesEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.stereotype.Repository

@Repository
interface IKebsProductCategoriesRepository: HazelcastRepository<KebsProductCategoriesEntity, Long> {
    fun findByBroadProductCategoryId(broadProductCategoryId: Long): List<KebsProductCategoriesEntity>
    fun findByStatusOrderByName(status: Int): List<KebsProductCategoriesEntity>?
}