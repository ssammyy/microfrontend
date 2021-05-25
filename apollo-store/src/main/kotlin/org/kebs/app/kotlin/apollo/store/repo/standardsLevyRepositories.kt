package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.Sl2PaymentsDetailsEntity
import org.kebs.app.kotlin.apollo.store.model.Sl2PaymentsHeaderEntity
import org.kebs.app.kotlin.apollo.store.model.SlVisitUploadsEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface ISl2PaymentsHeaderRepository : HazelcastRepository<Sl2PaymentsHeaderEntity, Long>, JpaSpecificationExecutor<Sl2PaymentsHeaderEntity>

interface ISl2PaymentsDetailsRepository : HazelcastRepository<Sl2PaymentsDetailsEntity, Long>, JpaSpecificationExecutor<Sl2PaymentsDetailsEntity>
interface ISlVisitUploadsRepository : HazelcastRepository<SlVisitUploadsEntity, Long>, JpaSpecificationExecutor<SlVisitUploadsEntity> {
    fun findAllByVisitIdOrderById(visitId: Long): List<SlVisitUploadsEntity>

}
