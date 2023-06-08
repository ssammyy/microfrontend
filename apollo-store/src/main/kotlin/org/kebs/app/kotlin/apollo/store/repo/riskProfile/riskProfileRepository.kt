package org.kebs.app.kotlin.apollo.store.repo.riskProfile


import org.kebs.app.kotlin.apollo.store.model.riskProfile.RiskProfileConsigneeEntity
import org.kebs.app.kotlin.apollo.store.model.riskProfile.RiskProfileConsignorEntity
import org.kebs.app.kotlin.apollo.store.model.riskProfile.RiskProfileExporterEntity
import org.kebs.app.kotlin.apollo.store.model.riskProfile.RiskProfileImporterEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.stereotype.Repository


@Repository
interface IRiskProfileConsigneeRepository : HazelcastRepository<RiskProfileConsigneeEntity, Long>{
    fun findByPinAndStatus(pin: String, status: Int): RiskProfileConsigneeEntity?
}

@Repository
interface IRiskProfileConsignorRepository : HazelcastRepository<RiskProfileConsignorEntity, Long>{
    fun findByPinAndStatus(pin: String, status: Int): RiskProfileConsignorEntity?
}

@Repository
interface IRiskProfileExporterRepository : HazelcastRepository<RiskProfileExporterEntity, Long>{
    fun findByPinAndStatus(pin: String, status: Int): RiskProfileExporterEntity?
}

@Repository
interface IRiskProfileImporterRepository : HazelcastRepository<RiskProfileImporterEntity, Long>{
    fun findByPinAndStatus(pin: String, status: Int): RiskProfileImporterEntity?
}
