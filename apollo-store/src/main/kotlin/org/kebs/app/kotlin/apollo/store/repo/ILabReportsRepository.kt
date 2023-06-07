package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.CdSampleSubmissionItemsEntity
import org.kebs.app.kotlin.apollo.store.model.LabReportsEntity
import org.kebs.app.kotlin.apollo.store.model.PermitApplicationEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository

interface ILabReportsRepository: HazelcastRepository<LabReportsEntity, Long> {
    fun findByPermit(permit: Long): LabReportsEntity?
    fun findBySampleSubmissionEntity(sample: CdSampleSubmissionItemsEntity): LabReportsEntity?
}