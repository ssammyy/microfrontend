package org.kebs.app.kotlin.apollo.store.repo.std

import org.kebs.app.kotlin.apollo.store.model.UserRolesEntity
import org.kebs.app.kotlin.apollo.store.model.std.StandardNWI
import org.kebs.app.kotlin.apollo.store.model.std.StandardRequest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StandardRequestRepository:JpaRepository<StandardRequest, Long> {
    fun findAllByOrderByIdDesc(): MutableList<StandardRequest>
    fun findAllByStatusAndNwiStatusIsNull(status: String): List<StandardRequest>

    fun findAllById(id: Long): List<StandardRequest>


}
