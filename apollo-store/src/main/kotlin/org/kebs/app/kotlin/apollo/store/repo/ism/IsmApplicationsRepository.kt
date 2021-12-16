package org.kebs.app.kotlin.apollo.store.repo.ism

import org.kebs.app.kotlin.apollo.store.model.ism.IsmApplications
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.hazelcast.repository.HazelcastRepository
import java.util.*

interface IsmApplicationsRepository: HazelcastRepository<IsmApplications, Long> {
    fun findByUcrNumber(ucrNumber: String?): Optional<IsmApplications>
    fun findByRequestApproved(requestApproved: Int?, page: Pageable): Page<IsmApplications>
}