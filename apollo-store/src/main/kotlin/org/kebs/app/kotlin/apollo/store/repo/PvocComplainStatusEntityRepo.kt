package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.pvc.PvocComplaintRecommendationEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository

interface PvocComplainRecommendationEntityRepo : HazelcastRepository<PvocComplaintRecommendationEntity, Long> {
    fun findByStatus(status: Int) : List<PvocComplaintRecommendationEntity>
}