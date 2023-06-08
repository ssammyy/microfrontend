package org.kebs.app.kotlin.apollo.store.repo.std

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.kebs.app.kotlin.apollo.store.model.std.User

@Repository
interface RoleRepository : JpaRepository<User, Long> {
}
