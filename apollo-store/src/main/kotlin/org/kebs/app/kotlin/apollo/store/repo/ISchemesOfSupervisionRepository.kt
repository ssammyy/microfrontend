package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.SchemesOfSupervisionEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.stereotype.Repository


@Repository
interface ISchemesOfSupervisionRepository : HazelcastRepository<SchemesOfSupervisionEntity, Long>