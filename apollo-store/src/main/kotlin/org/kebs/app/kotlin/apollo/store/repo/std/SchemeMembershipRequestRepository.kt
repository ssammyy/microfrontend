package org.kebs.app.kotlin.apollo.store.repo.std

import org.kebs.app.kotlin.apollo.store.model.std.SchemeMembershipRequest
import org.springframework.data.repository.CrudRepository

interface SchemeMembershipRequestRepository : CrudRepository<SchemeMembershipRequest, Long>
