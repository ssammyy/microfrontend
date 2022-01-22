package org.kebs.app.kotlin.apollo.store.repo.std

import org.kebs.app.kotlin.apollo.store.model.std.StandardDraft
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.sql.Timestamp
import javax.transaction.Transactional

@Repository
interface StandardDraftRepository : JpaRepository<StandardDraft, Long> {
    //
//    @Query("SELECT t.title FROM TechnicalCommittee t WHERE t.id=:id")
//    fun findNameById(@Param("id") id: Long?): String
    @Transactional
    @Modifying
    @Query(
        "UPDATE SD_STANDARD_DRAFT s SET s.APPROVED_BY=:approvedBy, s.APPROVAL_STATUS=:approvalStatus, s.STATUS=:status WHERE s.ID=:id ",
        nativeQuery = true
    )
    fun updateDecisonbyHOP(
        @Param("id") id: Long,
        @Param("approvedBy") approvedBy: String,
        @Param("approvalStatus") approvalStatus: String,
        @Param("status") status: String

    ): String

    @Transactional
    @Modifying
    @Query(
        "UPDATE SD_STANDARD_DRAFT s SET s.PROOFREAD_STATUS=:proofreadStatus, s.PROOFREAD_DATE=:proofReadDate,s.PROOFREAD_BY=:proofReadBy, s.STATUS=:status WHERE s.ID=:id ",
        nativeQuery = true
    )
    fun updateProofReadindDecison(
        @Param("id") id: Long,
        @Param("proofreadStatus") proofreadStatus: String,
        @Param("proofReadDate") proofReadDate: Timestamp,
        @Param("proofReadBy") proofReadBy: String,
        @Param("status") status: String

    ): String


}
