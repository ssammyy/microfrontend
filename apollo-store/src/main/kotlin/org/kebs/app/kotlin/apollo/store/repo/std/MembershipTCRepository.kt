package org.kebs.app.kotlin.apollo.store.repo.std

import org.kebs.app.kotlin.apollo.store.model.std.MembershipTCApplication
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MembershipTCRepository : JpaRepository<MembershipTCApplication,Long>{

    fun findByIdAndStatus(id: Long?, status: String): MutableList<MembershipTCApplication>

    fun findByStatus(status: String): MutableList<MembershipTCApplication>
    fun findByVarField10(varField10: String): MembershipTCApplication?

    fun findByStatusIsNullAndApprovedByOrganizationIsNotNullAndApprovedByOrganizationEquals(approvedByOrganisation: String): MutableList<MembershipTCApplication>

}
