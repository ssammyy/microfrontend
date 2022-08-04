package org.kebs.app.kotlin.apollo.store.repo.std

import org.kebs.app.kotlin.apollo.store.model.std.CommitteePD
import org.kebs.app.kotlin.apollo.store.model.std.DatKebsSdStandardsEntity
import org.kebs.app.kotlin.apollo.store.model.std.PdWithUserName
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface CommitteePDRepository : JpaRepository<CommitteePD, Long> {

    @Query(
        value = "SELECT p.ID, p.CREATED_ON, p.PD_NAME, p.NWI_ID, (t.FIRST_NAME || ' ' || t.LAST_NAME) AS PD_BY, p.STATUS,c2.Count_P_ID AS NUMBER_OF_COMMENTS FROM CommitteePD p Join DAT_KEBS_USERS t on p.PD_BY = t.ID left outer join (select COUNT(c.PD_ID) Count_P_ID, PD_ID from SD_COMMENTS c where c.STATUS=1 group by PD_ID) c2 on p.id = c2.PD_ID ",
        nativeQuery = true
    )
    fun findPreliminaryDraft(): MutableList<PdWithUserName>

}
