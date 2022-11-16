package org.kebs.app.kotlin.apollo.store.repo.std

import org.kebs.app.kotlin.apollo.store.model.std.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface VoteOnNWIRepository : JpaRepository<VoteOnNWI, Long> {

    fun findByUserIdAndNwiIdAndStatus(userId: Long, NwiId: Long, status: Long): VoteOnNWI?
    fun findByUserIdAndStatus(userId: Long, status: Long): List<VoteOnNWI>

    @Query(
        "SELECT t.ID AS NWIId,t.DECISION, t.ORGANIZATION,t.POSITION, t.STATUS, d.PROPOSAL_TITLE, cast(t.CREATED_ON as varchar(200)) AS CREATED_ON ,t.REASON,(DKUV.FIRST_NAME || ' ' || dku.LAST_NAME) AS VOTE_BY, t.USER_ID FROM SD_VOTE_ON_NWI t Join SD_NWI d ON t.NWI_ID = d.ID join DAT_KEBS_USERS DKU on t.USER_ID = DKU.ID join DAT_KEBS_USERS DKUV on t.USER_ID = DKUV.ID WHERE t.STATUS = 1  and t.NWI_ID=:nwiId ORDER BY t.ID DESC",
        nativeQuery = true
    )
    fun getVotesByTcMembers(@Param("nwiId") nwiId: Long): List<VotesWithNWIId>


    @Query(
        "SELECT v.NWI_ID, B.PROPOSAL_TITLE AS NwiName,B.STATUS, count(case when v.DECISION = 'true' then NWI_ID end) as Approved, count(case when v.DECISION = 'false' then NWI_ID end) as NotApproved from SD_VOTE_ON_NWI v join SD_NWI B on v.NWI_ID = B.ID group by v.NWI_ID, B.PROPOSAL_TITLE,B.STATUS",
        nativeQuery = true
    )
    fun getVotesTally(): List<NwiVotesTally>

}
