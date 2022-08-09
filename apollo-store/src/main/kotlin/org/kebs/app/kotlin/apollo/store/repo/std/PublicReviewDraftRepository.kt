package org.kebs.app.kotlin.apollo.store.repo.std

import org.kebs.app.kotlin.apollo.store.model.std.PdWithUserName
import org.kebs.app.kotlin.apollo.store.model.std.PrdWithUserName
import org.kebs.app.kotlin.apollo.store.model.std.PublicReviewDraft
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository

interface PublicReviewDraftRepository : JpaRepository<PublicReviewDraft, Long> {
    @Query(
        value = "SELECT p.ID, cast(p.CREATED_ON as varchar(200)) AS CreatedOn, p.PRD_NAME, p.KS_NUMBER,p.VAR_FIELD_1, p.CD_ID, (t.FIRST_NAME || ' ' || t.LAST_NAME) AS PRD_BY, p.STATUS,NVL(c2.Count_P_ID,0) AS NUMBER_OF_COMMENTS FROM SD_PUBLIC_REVIEW_DRAFTS p Join DAT_KEBS_USERS t on p.PRD_BY = t.ID left outer join (select COUNT(c.PRD_ID) Count_P_ID, PRD_ID from SD_COMMENTS c where c.STATUS=1 group by PRD_ID) c2 on p.id = c2.PRD_ID ",
        nativeQuery = true
    )
    fun findPublicReviewDraft(): MutableList<PrdWithUserName>

    @Query(
        value = "SELECT p.ID, cast(p.CREATED_ON as varchar(200)) AS CreatedOn, p.PRD_NAME, p.KS_NUMBER,p.VAR_FIELD_1, p.CD_ID, (t.FIRST_NAME || ' ' || t.LAST_NAME) AS PRD_BY, p.STATUS,NVL(c2.Count_P_ID,0) AS NUMBER_OF_COMMENTS FROM SD_PUBLIC_REVIEW_DRAFTS p Join DAT_KEBS_USERS t on p.PRD_BY = t.ID left outer join (select COUNT(c.PRD_ID) Count_P_ID, PRD_ID from SD_COMMENTS c where c.STATUS=1 group by PRD_ID) c2 on p.id = c2.PRD_ID WHERE p.VAR_FIELD_3='Approved'",
        nativeQuery = true
    )
    fun findApprovedPublicReviewDraft(): MutableList<PrdWithUserName>

}
