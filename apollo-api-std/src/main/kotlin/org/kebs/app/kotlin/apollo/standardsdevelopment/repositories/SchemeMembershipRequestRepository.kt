package org.kebs.app.kotlin.apollo.standardsdevelopment.repositories

import org.kebs.app.kotlin.apollo.standardsdevelopment.models.SchemeMembershipRequest
import org.springframework.data.repository.CrudRepository

interface SchemeMembershipRequestRepository  : CrudRepository<SchemeMembershipRequest, Long>