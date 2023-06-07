package org.kebs.app.kotlin.apollo.store.repo


import org.kebs.app.kotlin.apollo.store.model.CorsEntity
import org.kebs.app.kotlin.apollo.store.model.CorsItemsEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository
import java.util.*

interface CorsEntityRepository : HazelcastRepository<CorsEntity, Long> {
    override fun findById(id: Long): Optional<CorsEntity>
    fun findByUcrNumber(ucrNumber: String): List<CorsEntity>?
    fun findAllByChasisNumber(chasisNumber: String): CorsEntity?

}

interface ICorsItemsEntityRepository : HazelcastRepository<CorsItemsEntity, Long>{
   fun findAllByCdId(cdId: Long) : List<CorsItemsEntity>?
}