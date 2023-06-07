package org.kebs.app.kotlin.apollo.store.repo

import com.hazelcast.core.Hazelcast
import org.kebs.app.kotlin.apollo.store.model.CdSampleCollectionEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.hazelcast.repository.HazelcastRepository

interface ICdSampleCollectionEntityRepo : HazelcastRepository<CdSampleCollectionEntity, Long> {
    fun findAllByCountryOfOriginNotAndCocNumberIsNullAndUcrNumberIsNull(countryOfOrigin: String, pageable: Pageable) : Page<CdSampleCollectionEntity>?
}