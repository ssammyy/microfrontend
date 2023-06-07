package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.CfgKebsPermitPaymentUnitsEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository

interface ICfgKebsPermitPaymentUnitsRepository: HazelcastRepository<CfgKebsPermitPaymentUnitsEntity, Long> {
}