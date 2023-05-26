package org.kebs.app.kotlin.apollo.store.repo.std

import org.kebs.app.kotlin.apollo.store.model.UserRolesEntity
import org.kebs.app.kotlin.apollo.store.model.std.TcUserAssignment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.security.Principal
import java.util.*

@Repository
interface TcUserAssignmentRepository : JpaRepository<TcUserAssignment, Long> {

    fun findByOrganizationAndTcIdAndPrincipalIsNotNull(organization: String, tcId: Long): Optional<TcUserAssignment>

    fun findAllByUserId(userId:Long):Optional<TcUserAssignment>

    fun findByTcIdAndPrincipal(tcId:Long,principal: String):List<TcUserAssignment>?

    fun findByUserId(userId:Long):List<TcUserAssignment>?



}
