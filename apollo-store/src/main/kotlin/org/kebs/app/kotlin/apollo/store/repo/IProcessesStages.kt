package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.ProcessesStagesEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository

interface IProcessesStagesRepository : HazelcastRepository<ProcessesStagesEntity, Long> {
    fun findByServiceMapId(serviceMapId: Int): ProcessesStagesEntity?
}