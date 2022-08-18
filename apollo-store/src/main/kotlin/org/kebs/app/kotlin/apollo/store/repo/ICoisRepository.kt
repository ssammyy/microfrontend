package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.CoisEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.stereotype.Repository

@Repository
interface ICoisRepository:HazelcastRepository<CoisEntity, Long> {
    fun findByCoiNumber(coiNumber:String):CoisEntity?
    fun findByUcrNumber(ucrNumber: String): CoisEntity?


}
