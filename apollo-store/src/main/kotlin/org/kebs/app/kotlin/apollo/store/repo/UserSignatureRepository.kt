package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import org.kebs.app.kotlin.apollo.store.model.UsersSignatureEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface UserSignatureRepository : HazelcastRepository<UsersSignatureEntity, Long>,
    JpaSpecificationExecutor<UsersSignatureEntity> {

    fun findByUserId(userId: Long): UsersSignatureEntity?



}


