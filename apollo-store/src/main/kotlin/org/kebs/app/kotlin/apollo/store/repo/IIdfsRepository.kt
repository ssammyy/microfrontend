package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.IdfsEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface IIdfsRepository : HazelcastRepository<IdfsEntity, Long> {

    //    fun findFirstByCountryOfSupplyAndStatus(country: String, status: Long): Optional<IdfsEntity>
    fun findFirstByCountryOfSupplyAndStatus(country: String, status: Int): IdfsEntity?
    fun findByIdfNumber(idfNumber: String): IdfsEntity?
    fun findByIdfNumberAndUsedStatus(idfNumber: String, usedStatus: Int): IdfsEntity?
    fun findByIdfNumberAndUcrAndUsedStatus(idfNumber: String, ucr: String, usedStatus: Int): IdfsEntity?
    fun findByCountryOfSupplyAndStatus(countryOfSupply: String, status: Int): List<IdfsEntity>?

}