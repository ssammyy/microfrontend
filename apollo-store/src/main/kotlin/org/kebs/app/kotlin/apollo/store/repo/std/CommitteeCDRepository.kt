package org.kebs.app.kotlin.apollo.store.repo.std

import org.kebs.app.kotlin.apollo.store.model.std.CdWithUserName
import org.kebs.app.kotlin.apollo.store.model.std.CommitteeCD
import org.kebs.app.kotlin.apollo.store.model.std.PdWithUserName
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface CommitteeCDRepository : JpaRepository<CommitteeCD, Long> {

    @Query(
        value = "SELECT  p.ID AS CdID, cast(p.CREATED_ON as varchar(200)) AS CreatedOn ,p.CD_NAME as CdName, p.APPROVED as ApprovalStatus,p.KS_NUMBER, p.PD_ID as PdId, (t.FIRST_NAME || ' ' || t.LAST_NAME) AS CdBy, p.STATUS As Status, NVL(c2.Count_C_ID,0) AS NumberOfComments FROM SD_COMMITTEE_CD p Join DAT_KEBS_USERS t on p.CD_BY = t.ID left outer join (select COUNT(c.CD_ID) Count_C_ID, CD_ID from SD_COMMENTS c where c.STATUS=1 and c.CD_ID!=0 group by CD_ID) c2 on p.id = c2.CD_ID where p.CD_BY=:tcSecId ",
        nativeQuery = true
    )
    fun findCommitteeDraft(@Param("tcSecId") tcSecId: String): MutableList<CdWithUserName>

    @Query(
        value = "SELECT  p.ID AS CdID, cast(p.CREATED_ON as varchar(200)) AS CreatedOn ,p.CD_NAME as CdName, p.APPROVED as ApprovalStatus,p.KS_NUMBER, p.PD_ID as PdId, (t.FIRST_NAME || ' ' || t.LAST_NAME) AS CdBy, p.STATUS As Status, NVL(c2.Count_C_ID,0) AS NumberOfComments FROM SD_COMMITTEE_CD p Join DAT_KEBS_USERS t on p.CD_BY = t.ID left outer join (select COUNT(c.CD_ID) Count_C_ID, CD_ID from SD_COMMENTS c where c.STATUS=1 and c.CD_ID!=0 group by CD_ID) c2 on p.id = c2.CD_ID where p.APPROVED='Approved' and p.CD_BY=:tcSecId",
        nativeQuery = true
    )
    fun findApprovedCommitteeDraft(@Param("tcSecId") tcSecId: String): MutableList<CdWithUserName>
}
