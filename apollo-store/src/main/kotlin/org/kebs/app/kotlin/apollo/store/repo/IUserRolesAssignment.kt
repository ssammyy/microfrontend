package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.UserRoleAssignmentsEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.stereotype.Repository

@Repository
interface IUserRolesAssignment : HazelcastRepository<UserRoleAssignmentsEntity, Long>  {
    //fun findAllByRoleId()
}