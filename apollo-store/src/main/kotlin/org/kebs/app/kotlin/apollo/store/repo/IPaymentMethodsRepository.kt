package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.PaymentMethodsEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.stereotype.Repository

@Repository
interface IPaymentMethodsRepository: HazelcastRepository<PaymentMethodsEntity, Long>{
    fun findByMethodAndStatus(methd: String, status: Long): List<PaymentMethodsEntity>
}