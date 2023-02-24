package org.kebs.app.kotlin.apollo.store.repo.std

import org.kebs.app.kotlin.apollo.store.model.std.SchemeMembershipRequest
import org.kebs.app.kotlin.apollo.store.model.std.StandardAcquisitionRequest
import org.springframework.data.repository.CrudRepository

interface StandardAcquisitionRepository : CrudRepository<StandardAcquisitionRequest, Long> {
    fun findAllByRequestId(requestId: Long): StandardAcquisitionRequest

    fun findAllByEncryptedId(encryptedId: String): StandardAcquisitionRequest?


    fun findAllBySicAssignedIdIsNullAndApprovedByEmployerIsNotNull(): List<StandardAcquisitionRequest>

    fun findAllBySicAssignedIdIsNull(): List<StandardAcquisitionRequest>

    fun findAllBySicAssignedIdIsNotNull(): List<StandardAcquisitionRequest>


    fun findAllBySicAssignedId(sicAssignedId: String): List<StandardAcquisitionRequest>



    fun findAllByEmailAndPhoneAndStandardNameRequested(email:String,phone:String,standardNameRequested:String):StandardAcquisitionRequest?


}
