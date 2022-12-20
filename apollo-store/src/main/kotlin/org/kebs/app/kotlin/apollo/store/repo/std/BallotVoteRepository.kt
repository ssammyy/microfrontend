package org.kebs.app.kotlin.apollo.store.repo.std

import org.kebs.app.kotlin.apollo.store.model.std.BallotVote
import org.kebs.app.kotlin.apollo.store.model.std.VotesTally
import org.kebs.app.kotlin.apollo.store.model.std.VotesWithBallotId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface BallotVoteRepository : JpaRepository<BallotVote, Long> {
    @Query(
        "SELECT t.ID AS BallotId,t.APPROVAL_STATUS,t.STATUS, d.BALLOT_NAME, cast(t.CREATED_ON as varchar(200)) AS CREATED_ON ,t.COMMENTS,(DKUV.FIRST_NAME || ' ' || dku.LAST_NAME) AS COMMENTS_BY, t.USER_ID FROM BALLOT_VOTE t Join BALLOT d ON t.BALLOT_ID = d.ID join DAT_KEBS_USERS DKU on t.USER_ID = DKU.ID join DAT_KEBS_USERS DKUV on t.USER_ID = DKUV.ID WHERE t.STATUS = 1 and t.USER_ID=:loggedInUserId ORDER BY t.ID DESC",
        nativeQuery = true
    )
    fun getUserLoggedInVotes(@Param("loggedInUserId") loggedInUserId: Long): List<VotesWithBallotId>


    @Query(
        "SELECT t.ID,t.APPROVAL_STATUS,t.STATUS, d.BALLOT_NAME, cast(t.CREATED_ON as varchar(200)) AS CREATED_ON ,t.COMMENTS,(DKUV.FIRST_NAME || ' ' || dku.LAST_NAME) AS COMMENTS_BY, t.USER_ID FROM BALLOT_VOTE t Join BALLOT d ON t.BALLOT_ID = d.ID join DAT_KEBS_USERS DKU on t.USER_ID = DKU.ID join DAT_KEBS_USERS DKUV on t.USER_ID = DKUV.ID WHERE t.STATUS = 1 and t.BALLOT_ID=:ballotId ORDER BY t.ID DESC",
        nativeQuery = true
    )
    fun getBallotVotes(@Param("ballotId") ballotId: Long): List<VotesWithBallotId>


    @Query(
        "select v.BALLOT_ID, B.BALLOT_NAME AS ballotName,B.STATUS, count(case when v.APPROVAL_STATUS = '1' then BALLOT_ID end) as Approved, count(case when v.APPROVAL_STATUS = '2' then BALLOT_ID end) as ApprovedWithComments, count(case when v.APPROVAL_STATUS = '3' then BALLOT_ID end) as Disapproved, count(case when v.APPROVAL_STATUS = '4' then BALLOT_ID end) as Abstention from BALLOT_VOTE v join BALLOT B on v.BALLOT_ID = B.ID group by v.BALLOT_ID, B.BALLOT_NAME,B.STATUS",
        nativeQuery = true
    )
    fun getVotesTally(): List<VotesTally>



    fun findByUserIdAndBallotIdAndStatus(userId: Long, ballotId: Long, status: Long): BallotVote?

}
