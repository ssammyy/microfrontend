package org.kebs.app.kotlin.apollo.store.repo.std

import org.kebs.app.kotlin.apollo.store.model.UserRolesEntity
import org.kebs.app.kotlin.apollo.store.model.std.AllLevyPayments
import org.kebs.app.kotlin.apollo.store.model.std.ReceivedStandards
import org.kebs.app.kotlin.apollo.store.model.std.StandardNWI
import org.kebs.app.kotlin.apollo.store.model.std.StandardRequest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface StandardRequestRepository:JpaRepository<StandardRequest, Long> {
    fun findAllByOrderByIdDesc(): MutableList<StandardRequest>
    fun findAllByStatusAndNwiStatusIsNull(status: String): List<StandardRequest>


    fun findAllById(id: Long): List<StandardRequest>

    @Query(
        value = "SELECT r.REQUEST_NUMBER as requestNumber,r.CREATED_ON as createdON,r.REQUESTOR_NAME as name,r.REQUESTOR_PHONE_NUMBER as phone,r.STATUS as status,u.FIRST_NAME as firstName,u.LAST_NAME as lastName " +
                "FROM SD_STANDARD_REQUEST r LEFT JOIN DAT_KEBS_USERS u ON r.TC_SEC_ASSIGNED=u.ID",nativeQuery = true)
    fun getReceivedStandardsReport(): MutableList<ReceivedStandards>

    @Query(
        value = "SELECT * FROM SD_STANDARD_REQUEST  WHERE LEVEL_OF_STANDARD='Kenya Standard' AND STATUS='Assigned To TC Sec' ORDER BY ID DESC",nativeQuery = true)
    fun getWorkshopStandards(): MutableList<StandardRequest>


}
