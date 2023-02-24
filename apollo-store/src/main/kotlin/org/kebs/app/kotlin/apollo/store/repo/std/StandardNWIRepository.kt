package org.kebs.app.kotlin.apollo.store.repo.std

import org.kebs.app.kotlin.apollo.store.model.std.BallotVote
import org.kebs.app.kotlin.apollo.store.model.std.StandardNWI
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StandardNWIRepository : JpaRepository<StandardNWI, Long> {


    fun findAllByStatus(status: String): List<StandardNWI>

    fun findAllByStatusAndTcSec(status: String, tcSecId:String): List<StandardNWI>


    fun findAllByStatusAndProcessStatusIsNullAndTcSec(status: String, tcSecId:String): List<StandardNWI>

    fun findAllById(id: Long): List<StandardNWI>

    fun findAllByPdStatusAndTcSec(status: String, tcSecId:String): List<StandardNWI>




}
