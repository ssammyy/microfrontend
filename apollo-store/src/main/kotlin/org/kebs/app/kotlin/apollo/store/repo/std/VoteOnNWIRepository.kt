package org.kebs.app.kotlin.apollo.store.repo.std

import org.kebs.app.kotlin.apollo.store.model.std.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface VoteOnNWIRepository : JpaRepository<VoteOnNWI, Long> {

    fun findByUserIdAndNwiIdAndStatus(userId: Long, NwiId: Long, status: Long): VoteOnNWI?
    fun findByUserIdAndStatusOrderByIdDesc(userId: Long, status: Long): List<VoteOnNWI>

    @Query(
        "SELECT t.ID AS NWIId,t.DECISION, t.ORGANIZATION,t.POSITION, t.STATUS, d.PROPOSAL_TITLE, cast(t.CREATED_ON as varchar(200)) AS CREATED_ON ,t.REASON,(DKUV.FIRST_NAME || ' ' || dku.LAST_NAME) AS VOTE_BY, t.USER_ID FROM SD_VOTE_ON_NWI t Join SD_NWI d ON t.NWI_ID = d.ID join DAT_KEBS_USERS DKU on t.USER_ID = DKU.ID join DAT_KEBS_USERS DKUV on t.USER_ID = DKUV.ID WHERE t.STATUS = 1  and t.NWI_ID=:nwiId ORDER BY t.ID DESC",
        nativeQuery = true
    )
    fun getVotesByTcMembers(@Param("nwiId") nwiId: Long): List<VotesWithNWIId>


    @Query(
        "SELECT B.ID AS NWI_ID, B.PROPOSAL_TITLE AS NwiName, B.REFERENCE_NUMBER, B.STATUS, COUNT(CASE WHEN V.DECISION = 'true' THEN NWI_ID END) AS Approved, COUNT(CASE WHEN V.DECISION = 'false' THEN NWI_ID END) AS NotApproved, T.number_of_expected_votes, B.TC_ID FROM SD_NWI B LEFT JOIN SD_VOTE_ON_NWI V ON V.NWI_ID = B.ID LEFT JOIN (SELECT COUNT(USER_ID) AS number_of_expected_votes, TC_ID FROM SD_TC_USER_ASSIGNMENT WHERE PRINCIPAL = 1 GROUP BY TC_ID) T ON T.TC_ID = B.TC_ID WHERE B.TC_SEC = :tcSecId GROUP BY B.ID, B.PROPOSAL_TITLE, B.REFERENCE_NUMBER, B.STATUS, T.number_of_expected_votes, B.TC_ID",
        nativeQuery = true
    )
    fun getVotesTally(@Param("tcSecId") tcSecId: String): List<NwiVotesTally>


    @Query(
        "SELECT B.ID AS NWI_ID, B.PROPOSAL_TITLE AS NwiName, B.REFERENCE_NUMBER, B.STATUS, \n" +
                "       COUNT(CASE WHEN V.DECISION = 'true' THEN NWI_ID END) AS Approved, \n" +
                "       COUNT(CASE WHEN V.DECISION = 'false' THEN NWI_ID END) AS NotApproved, \n" +
                "       T.number_of_expected_votes, B.TC_ID FROM SD_NWI B LEFT JOIN SD_VOTE_ON_NWI V ON V.NWI_ID = B.ID \n" +
                "           LEFT JOIN (SELECT COUNT(USER_ID) AS number_of_expected_votes, TC_ID FROM SD_TC_USER_ASSIGNMENT \n" +
                "WHERE PRINCIPAL = 1 GROUP BY TC_ID) T ON T.TC_ID = B.TC_ID WHERE B.TC_ID = :tcId GROUP BY B.ID, B.PROPOSAL_TITLE, B.REFERENCE_NUMBER, B.STATUS, T.number_of_expected_votes, B.TC_ID",
        nativeQuery = true
    )
    fun getVotesTallyLoggedInMembers(@Param("tcId") tcId: String): List<NwiVotesTally>


}
