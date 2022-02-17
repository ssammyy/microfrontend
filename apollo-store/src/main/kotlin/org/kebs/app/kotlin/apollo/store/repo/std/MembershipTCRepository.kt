package org.kebs.app.kotlin.apollo.store.repo.std

import org.kebs.app.kotlin.apollo.store.model.std.MembershipTCApplication
import org.kebs.app.kotlin.apollo.store.model.std.ProductSubCategory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.sql.ClientInfoStatus

@Repository
interface MembershipTCRepository : JpaRepository<MembershipTCApplication,Long>{

    fun findByIdAndStatus( id: Long?, status: String): MutableList<MembershipTCApplication>

    fun findByStatus(status: String): MutableList<MembershipTCApplication>
    fun findByStatusIsNull(): MutableList<MembershipTCApplication>

}
