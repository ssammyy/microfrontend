package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.PvocStdMonitoringDataEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.stereotype.Repository

@Repository
interface IPvocStdMonitoringDataRepository : HazelcastRepository<PvocStdMonitoringDataEntity, Long> {
}