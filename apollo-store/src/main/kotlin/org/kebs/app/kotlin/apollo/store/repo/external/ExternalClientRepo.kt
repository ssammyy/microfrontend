package org.kebs.app.kotlin.apollo.store.repo.external

import org.kebs.app.kotlin.apollo.store.model.external.ApiClientEvents
import org.kebs.app.kotlin.apollo.store.model.external.SystemApiClient
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.hazelcast.repository.HazelcastRepository
import java.util.*

interface ApiClientRepo: HazelcastRepository<SystemApiClient, Long> {
    fun findByClientId(clientId: String): Optional<SystemApiClient>
    fun findByClientNameContains(clientName: String, page: Pageable): Page<SystemApiClient>
}

interface ApiClientEventsRepo: HazelcastRepository<ApiClientEvents, Long> {
    fun findByClientId(clientId: String?, pg: Pageable): Page<ApiClientEvents>

}