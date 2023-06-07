package org.kebs.app.kotlin.apollo.store.repo.std


import org.kebs.app.kotlin.apollo.store.model.std.SACSummary
import org.kebs.app.kotlin.apollo.store.model.std.SACSummaryHolder
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface SACSummaryRepository : JpaRepository<SACSummary, Long> {

    fun findByVarField1(varField1: String): MutableList<SACSummary>
    fun findByVarField2(varField2: String): MutableList<SACSummary>
    fun findByVarField8(varField8: String): MutableList<SACSummary>



    @Query(
        "SELECT t.ID,\n" +
                "       t.BACKGROUND_INFORMATION             AS backgroundInformation,\n" +
                "       t.ISSUES_ADDRESSED                   AS issuesAddressed,\n" +
                "       t.KS,\n" +
                "       t.REQUESTED_BY                       AS requestedBy,\n" +
                "       t.SL,\n" +
                "       t.APPROVAL_STATUS                    AS approvalStatus,\n" +
                "       t.FEEDBACK,\n" +
                "       t.DATE_OF_APPROVAL                   AS dateOfApproval,\n" +
                "       t.EAC_GAZETTE,\n" +
                "       t.AUTHENTIC_TEXT,\n" +
                "       t.VAR_FIELD_1                        AS varField1,\n" +
                "       t.TITLE,\n" +
                "       t.REFERENCE_MATERIAL                 AS referenceMaterial,\n" +
                "       t.EDITION,\n" +
                "       d.TC_TITLE                           AS TechnicalCommittee,\n" +
                "       e.NAME                               AS DepartmentName,\n" +
                "       (u.FIRST_NAME || ' ' || u.LAST_NAME) AS RequestedByName\n" +
                "FROM SD_SAC_SUMMARY t\n" +
                "         Join SD_TECHNICAL_COMMITTEE d ON t.TECHNICAL_COMMITTEE = d.ID\n" +
                "         LEFT Join SD_DEPARTMENT e ON t.DEPARTMENT = e.ID\n" +
                "         join DAT_KEBS_USERS u ON u.ID = t.REQUESTED_BY\n" +
                "WHERE t.VAR_FIELD_1 = 'Approved' ",
        nativeQuery = true
    )
    fun findAllWithDescriptionQuery(): List<SACSummaryHolder>




}
