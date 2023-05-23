package org.kebs.app.kotlin.apollo.store.repo.std

import org.kebs.app.kotlin.apollo.store.model.std.PdWithUserName
import org.kebs.app.kotlin.apollo.store.model.std.PrdWithUserName
import org.kebs.app.kotlin.apollo.store.model.std.PublicReviewDraft
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository

interface PublicReviewDraftRepository : JpaRepository<PublicReviewDraft, Long> {
    @Query(
        value = "SELECT p.ID, cast(p.CREATED_ON as varchar(200)) AS CreatedOn, p.PRD_NAME, p.KS_NUMBER,p.VAR_FIELD_1, p.CD_ID, v.PROPOSAL_TITLE, v.CIRCULATION_DATE,v.CLOSING_DATE, v.ORGANIZATION , (t.FIRST_NAME || ' ' || t.LAST_NAME) AS PRD_BY, p.STATUS,NVL(c2.Count_P_ID,0) AS NUMBER_OF_COMMENTS FROM SD_PUBLIC_REVIEW_DRAFTS p Join DAT_KEBS_USERS t on p.PRD_BY = t.ID  join SD_COMMITTEE_CD z on p.CD_ID=z.ID  join COMMITTEEPD a on z.PD_ID=a.ID join SD_NWI v on a.NWI_ID=v.ID    left outer join (select COUNT(c.PRD_ID) Count_P_ID, PRD_ID from SD_COMMENTS c where c.STATUS=1 group by PRD_ID) c2 on p.id = c2.PRD_ID ORDER BY p.ID DESC ",
        nativeQuery = true
    )
    fun findPublicReviewDraft(@Param("tcSecId") tcSecId: String): MutableList<PrdWithUserName>

    @Query(
        value = "SELECT p.ID, cast(p.CREATED_ON as varchar(200)) AS CreatedOn, p.PRD_NAME, p.KS_NUMBER,p.VAR_FIELD_1, p.CD_ID, (t.FIRST_NAME || ' ' || t.LAST_NAME) AS PRD_BY, p.STATUS,NVL(c2.Count_P_ID,0) AS NUMBER_OF_COMMENTS FROM SD_PUBLIC_REVIEW_DRAFTS p Join DAT_KEBS_USERS t on p.PRD_BY = t.ID left outer join (select COUNT(c.PRD_ID) Count_P_ID, PRD_ID from SD_COMMENTS c where c.STATUS=1 group by PRD_ID) c2 on p.id = c2.PRD_ID WHERE p.VAR_FIELD_3='Approved' and p.PRD_BY=:tcSecId",
        nativeQuery = true
    )
    fun findApprovedPublicReviewDraft(@Param("tcSecId") tcSecId: String): MutableList<PrdWithUserName>

}
