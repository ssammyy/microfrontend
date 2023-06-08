package org.kebs.app.kotlin.apollo.store.repo.std

import org.kebs.app.kotlin.apollo.store.model.std.JustificationForTC
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface JustificationForTCRepository : JpaRepository<JustificationForTC, Long> {

    @Query(value = "SELECT * FROM SD_COM_STANDARD_REQUEST WHERE STATUS='0'  ", nativeQuery = true)
    fun getComTcJustification(): MutableList<JustificationForTC>

    @Query(value = "SELECT * FROM SD_COM_STANDARD_REQUEST WHERE STATUS='1'  ", nativeQuery = true)
    fun getComApprovedTcJustification(): MutableList<JustificationForTC>

    @Query(value = "SELECT * FROM SD_COM_STANDARD_REQUEST WHERE STATUS='3'  ", nativeQuery = true)
    fun getApprovedSpcComTcJustification(): MutableList<JustificationForTC>

    @Query(value = "SELECT * FROM SD_COM_STANDARD_REQUEST WHERE STATUS='4'  ", nativeQuery = true)
    fun getApprovedComTcJustification(): MutableList<JustificationForTC>

    fun findAllByOrderByIdDesc(): MutableList<JustificationForTC>


    fun findAllByStatus(status: Long): MutableList<JustificationForTC>
}
