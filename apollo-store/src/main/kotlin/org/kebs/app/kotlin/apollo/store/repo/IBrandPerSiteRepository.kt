package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.BrandPerSiteEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository

interface IBrandPerSiteRepository : HazelcastRepository<BrandPerSiteEntity, Long> {
    fun findByBrandId(brandId: Long) : List<BrandPerSiteEntity>
}