package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.CountriesEntity
import org.kebs.app.kotlin.apollo.store.model.UserTypesEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.stereotype.Repository

@Repository
interface ICountriesRepository: HazelcastRepository<CountriesEntity, Long> {
    fun findByStatus(status: Int): List<CountriesEntity>?
}