package org.kebs.app.kotlin.apollo.store.repo.std

import org.kebs.app.kotlin.apollo.store.model.std.StandardNWI
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface StandardNWIRepository : JpaRepository<StandardNWI, Long> {


    fun findAllByStatus(status: String): List<StandardNWI>

    fun findAllByStatusAndTcSec(status: String, tcSecId: String): List<StandardNWI>


    fun findAllByStatusAndProcessStatusIsNullAndTcSec(status: String, tcSecId: String): List<StandardNWI>

    fun findAllById(id: Long): List<StandardNWI>

    fun findAllByPdStatusAndTcSec(status: String, tcSecId: String): List<StandardNWI>


    fun findAllByPdStatusAndTcSecAndPrPdStatusIsNull(status: String,tcSecId: String): List<StandardNWI>



    @Query(
        "SELECT t1.* FROM SD_NWI t1 WHERE  t1.STATUS='Vote ON NWI' and NOT EXISTS (SELECT t2.NWI_ID from SD_VOTE_ON_NWI t2 where t2.USER_ID=:userId and t2.NWI_ID=t1.ID)",
        nativeQuery = true
    )
    fun getPendingVoting(@Param("userId") userId: Long): List<StandardNWI>


    @Query("SELECT t1.* FROM SD_NWI t1 WHERE t1.STATUS = 'Vote ON NWI'  and NOT EXISTS (SELECT t2.NWI_ID from SD_VOTE_ON_NWI t2 where t2.USER_ID = :userId  and t2.NWI_ID = t1.ID)and t1.TC_ID in (SELECT v.TC_ID FROM SD_TC_USER_ASSIGNMENT v WHERE v.USER_ID=:userId)"
        , nativeQuery = true
    )
    fun getPendingVotingUser(@Param("userId") userId: Long): List<StandardNWI>


}
