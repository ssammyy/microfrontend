package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.*
import org.springframework.data.hazelcast.repository.HazelcastRepository

interface ISampleCollectionDocumentsRepository: HazelcastRepository<SampleCollectionDocumentsEntity, Long> {
    fun findBySampleCollectionEntity(sampleCollectionEntity: CdSampleCollectionEntity): SampleCollectionDocumentsEntity?
}

interface ISampleSubmissionDocumentsRepository: HazelcastRepository<SampleSubmissionDocumentsEntity, Long> {
    fun findBySampleSubmissionEntity(sampleSubmissionEntity: CdSampleSubmissionItemsEntity): SampleSubmissionDocumentsEntity?
}