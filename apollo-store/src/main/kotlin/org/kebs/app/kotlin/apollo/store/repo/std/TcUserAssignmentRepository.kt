package org.kebs.app.kotlin.apollo.store.repo.std

import org.kebs.app.kotlin.apollo.store.model.std.DataHolder
import org.kebs.app.kotlin.apollo.store.model.std.TcMembers
import org.kebs.app.kotlin.apollo.store.model.std.TcUserAssignment
import org.kebs.app.kotlin.apollo.store.model.std.UserDetailHolder
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface TcUserAssignmentRepository : JpaRepository<TcUserAssignment, Long> {


}
