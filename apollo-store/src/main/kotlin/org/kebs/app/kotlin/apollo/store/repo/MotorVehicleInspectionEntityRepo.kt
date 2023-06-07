package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.MotorVehicleInspectionEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository

interface MotorVehicleInspectionEntityRepo : HazelcastRepository<MotorVehicleInspectionEntity, Long> {
}