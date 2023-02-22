package org.kebs.app.kotlin.apollo.store.repo.std

import org.kebs.app.kotlin.apollo.store.model.std.SchemeMembershipRequest
import org.springframework.data.repository.CrudRepository

interface SchemeMembershipRequestRepository : CrudRepository<SchemeMembershipRequest, Long> {
    fun findAllByRequestId(requestId: Long): SchemeMembershipRequest

    fun findAllBySicAssignedIdIsNull(): List<SchemeMembershipRequest>

    fun findAllBySicAssignedIdIsNotNull(): List<SchemeMembershipRequest>


    fun findAllBySicAssignedId(sicAssignedId: String): List<SchemeMembershipRequest>

    fun findAllByInvoiceStatus(invoiceStatus:String):List<SchemeMembershipRequest>


    fun findAllByEmailOrPhone(email:String,phone:String):SchemeMembershipRequest?


}
