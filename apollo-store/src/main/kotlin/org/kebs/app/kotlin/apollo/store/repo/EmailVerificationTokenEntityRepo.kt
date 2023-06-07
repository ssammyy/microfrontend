package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.EmailVerificationTokenEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository

interface EmailVerificationTokenEntityRepo : HazelcastRepository<EmailVerificationTokenEntity, Long> {
    fun findFirstByTokenOrderByIdDesc(token: String): EmailVerificationTokenEntity?
    fun findFirstByTokenAndStatusOrderByIdDesc(token: String, status: Int): EmailVerificationTokenEntity?
    fun findFirstByToken(token: String): EmailVerificationTokenEntity?
}