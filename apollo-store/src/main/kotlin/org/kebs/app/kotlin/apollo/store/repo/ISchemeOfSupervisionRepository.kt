package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.SchemeOfSupervisionEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository

interface ISchemeOfSupervisionRepository: HazelcastRepository<SchemeOfSupervisionEntity, Long> {
}