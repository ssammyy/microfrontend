package org.kebs.app.kotlin.apollo.store.repo.qa

import org.kebs.app.kotlin.apollo.store.model.qa.PermitTypesEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.stereotype.Repository

@Repository
interface PermitTypeRepository  : HazelcastRepository<PermitTypesEntity, Long> {
}