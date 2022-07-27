package org.kebs.app.kotlin.apollo.store.repo.std

import org.kebs.app.kotlin.apollo.store.model.std.ApprovedNwi
import org.kebs.app.kotlin.apollo.store.model.std.StandardWorkPlan
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface StandardWorkPlanRepository : JpaRepository<StandardWorkPlan, Long> {

    @Query(
        "SELECT t.ID AS workplan_id,t.STAGE_CODE,t.STAGE_DATE,t.STAGE_MONTH,t.SUB_STAGE,t.TARGET_DATE,t.TITLE,t.REQUEST_NO,d.STATUS," +
                " d.ID, d.CREATED_ON, d.DELETED_ON,d.MODIFIED_ON,d.NAME_OF_PROPOSER,d.PROPOSAL_TITLE, d.CIRCULATION_DATE, d.CLOSING_DATE,d.DATE_OF_PRESENTATION,d.DRAFT_ATTACHED,d.DRAFT_OUTLINE_IMPOSSIBLE,d.LIAISON_ORGANIZATION,d.NAME_OF_TC,d.ORGANIZATION,d.OUTLINE_ATTACHED,d.OUTLINE_SENT_LATER,d.PURPOSE,d.REFERENCE_NUMBER,d.SCOPE,d.SIMILAR_STANDARDS,d.TARGET_DATE AS Target_Dateb, d.LIAISON_ORGANIZATION AS Liason_Organizationb FROM SD_WORKPLAN t Join SD_NWI d ON  SUBSTR(t.REQUEST_NO, 4) = d.REFERENCE_NUMBER ORDER BY t.ID DESC",
        nativeQuery = true
    )
    fun findAllApproved(): List<ApprovedNwi>

    fun findByReferenceNo(referenceNo:String?):MutableList<StandardWorkPlan>
}
