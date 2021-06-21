package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.SampleStandardsEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.stereotype.Repository


@Repository
interface ISampleStandardsRepository : HazelcastRepository<SampleStandardsEntity, Long>{
    fun findBySubCategoryId(subCategoryId: Long?): SampleStandardsEntity?
    fun findBySubCategoryIdOrderByCreatedOn(subCategoryId: Long?): List<SampleStandardsEntity>?
    fun findByStatusOrderByStandardTitle(status: Int): List<SampleStandardsEntity>?
    fun findBySubCategoryIdAndStatus(subCategoryId: Long, status: Int): SampleStandardsEntity
}