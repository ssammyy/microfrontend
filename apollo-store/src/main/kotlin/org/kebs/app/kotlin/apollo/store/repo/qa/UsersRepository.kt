package org.kebs.app.kotlin.apollo.store.repo.qa

import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.stereotype.Repository

@Repository
interface UsersRepository : HazelcastRepository<UsersEntity, Long> {
   fun findByEmail(email : String)

}