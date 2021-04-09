package org.kebs.app.kotlin.apollo.store.repo.qa

import org.kebs.app.kotlin.apollo.store.model.qa.FirmDetailsEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.stereotype.Repository

@Repository
interface FirmDetailRepo  : HazelcastRepository<FirmDetailsEntity, Long> {
}