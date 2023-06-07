package org.kebs.app.kotlin.apollo.store.repo.std

import org.kebs.app.kotlin.apollo.store.model.std.Ballot
import org.kebs.app.kotlin.apollo.store.model.std.BallotWithUserName
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface BallotingRepository : JpaRepository<Ballot, Long> {


    @Query(
        value = "SELECT p.ID, cast(p.CREATED_ON as varchar(200)) AS CreatedOn, p.BALLOT_NAME,p.APPROVAL_STATUS, p.PRD_ID, (t.FIRST_NAME || ' ' || t.LAST_NAME) AS PRD_BY, p.STATUS,NVL(c2.Count_P_ID,0) AS NUMBER_OF_COMMENTS FROM BALLOT p Join DAT_KEBS_USERS t on p.BALLOT_DRAFT_BY = t.ID left outer join (select COUNT(c.BALLOT_ID) Count_P_ID, BALLOT_ID from BALLOT_VOTE c where c.STATUS=1 group by BALLOT_ID) c2 on p.id = c2.BALLOT_ID ",
        nativeQuery = true
    )
    fun findBallotDraft(): MutableList<BallotWithUserName>

    fun findByApprovalStatusAndVarField2IsNull(status: String?): List<Ballot>

    fun findByVarField2(status: String?): List<Ballot>

    fun findByEditedStatus(status: String?): List<Ballot>



}
