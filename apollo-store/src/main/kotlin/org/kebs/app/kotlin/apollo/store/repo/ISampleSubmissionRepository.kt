package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.CdSampleSubmissionItemsEntity
import org.kebs.app.kotlin.apollo.store.model.PermitApplicationEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.stereotype.Repository

@Repository
interface ISampleSubmissionRepository : HazelcastRepository<CdSampleSubmissionItemsEntity, Long> {
    fun findByItemId(itemId: Long): CdSampleSubmissionItemsEntity?

    fun findByPermitId(permitId: Long): CdSampleSubmissionItemsEntity?
}