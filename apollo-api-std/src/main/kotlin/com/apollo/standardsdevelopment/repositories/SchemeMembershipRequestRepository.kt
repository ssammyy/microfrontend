package com.apollo.standardsdevelopment.repositories

import com.apollo.standardsdevelopment.models.SchemeMembershipRequest
import org.springframework.data.repository.CrudRepository

interface SchemeMembershipRequestRepository  : CrudRepository<SchemeMembershipRequest, Long> {
}
