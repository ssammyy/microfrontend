package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.QaWorkplanEntity
import org.kebs.app.kotlin.apollo.store.model.UsersEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.hazelcast.repository.HazelcastRepository

interface IQaWorkplanRepository: HazelcastRepository<QaWorkplanEntity, Long> {
    fun findAllByUserIdOrderById(user: UsersEntity, pageable: Pageable): Page<QaWorkplanEntity>?
    fun findByUserId(userId: UsersEntity): List<QaWorkplanEntity>?
}