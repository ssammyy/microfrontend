package org.kebs.app.kotlin.apollo.store.repo.std

import org.kebs.app.kotlin.apollo.store.model.std.DataHolder
import org.kebs.app.kotlin.apollo.store.model.std.TcMembers
import org.kebs.app.kotlin.apollo.store.model.std.TechnicalCommittee
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface TechnicalCommitteeRepository : JpaRepository<TechnicalCommittee, Long> {
    fun findByDepartmentId(departmentId: Long?): MutableList<TechnicalCommittee>

    fun findAllByAdvertisingStatus(advertisingStatus: String): MutableList<TechnicalCommittee>

    @Query("SELECT t.title FROM TechnicalCommittee t WHERE t.id=:id")
    fun findNameById(@Param("id") id: Long?): String

    @Query(
        "SELECT t.ID, t.TC_TITLE,t.PARENT_COMMITTEE AS V1,t.TECHNICAL_COMMITTEE_NO AS V2, d.NAME, d.ID AS V3, t.USER_ID AS V4, v.FIRST_NAME AS V5, v.LAST_NAME AS V6 FROM SD_TECHNICAL_COMMITTEE t  Left join APOLLO.DAT_KEBS_USERS v on t.USER_ID=v.ID   Join SD_DEPARTMENT d ON t.DEPARTMENT_ID=d.ID ORDER BY t.ID DESC ",
        nativeQuery = true
    )
    fun findAllWithDescriptionQuery(): List<DataHolder>


    @Query(
        "SELECT t.ID As Id, v.ID AS V1, v.FIRST_NAME AS V5, v.LAST_NAME AS V6 FROM SD_TECHNICAL_COMMITTEE t  Left join APOLLO.DAT_KEBS_USERS v on t.USER_ID=v.ID  where t.ID=:id",
        nativeQuery = true
    )
    fun findTcSecQuery(id: Long?): List<DataHolder>


    @Query(
        "SELECT    t.ID, t.TC_TITLE,t.PARENT_COMMITTEE AS V1,t.TECHNICAL_COMMITTEE_NO AS V2, d.NAME, d.ID AS V3, t.USER_ID AS V4, v.FIRST_NAME AS V5, v.LAST_NAME AS V6,NVL(c2.Count_P_ID,0) AS NUMBER_OF_MEMBERS FROM SD_TECHNICAL_COMMITTEE t  Left join APOLLO.DAT_KEBS_USERS v on t.USER_ID=v.ID   Join SD_DEPARTMENT d ON t.DEPARTMENT_ID=d.ID  left outer join (select COUNT(c.TC_ID) Count_P_ID,TC_ID from SD_TC_USER_ASSIGNMENT c where c.STATUS=1 group by TC_ID) c2 on t.ID = c2.TC_ID  ",
        nativeQuery = true
    )
    fun findAllWithCountQuery(): List<DataHolder>


    @Query(
        "SELECT t.ID, t.TC_ID, t.USER_ID, t.ORGANISATION,t.PRINCIPAL, v.FIRST_NAME, v.LAST_NAME, a.TC_TITLE, (cast(c2.LAST_MODIFIED_ON  as varchar(200))) As LAST_MODIFIED_ON  " +
                "FROM SD_TC_USER_ASSIGNMENT t " +
                "JOIN SD_TECHNICAL_COMMITTEE a ON a.ID = t.TC_ID " +
                "JOIN APOLLO.DAT_KEBS_USERS v ON t.USER_ID = v.ID " +
                "LEFT OUTER JOIN (SELECT VAR_FIELD_10, MAX(LAST_MODIFIED_ON) AS LAST_MODIFIED_ON " +
                "                 FROM DAT_KEBS_EMAIL_VERIFICATION_TOKEN " +
                "                 GROUP BY VAR_FIELD_10) c2 " +
                "ON t.USER_ID = c2.VAR_FIELD_10  where t.TC_ID=:tcId ORDER BY t.ID DESC", nativeQuery = true
    )
    fun findTechnicalCommitteeMembers(tcId: Long?): List<TcMembers>


    fun findTechnicalCommitteeByDepartmentId(departmentId: Long?): TechnicalCommittee?
    fun existsTechnicalCommitteeByDepartmentId(departmentId: Long?): Boolean

    fun findAllByOrderByIdDesc(): MutableList<TechnicalCommittee>

    fun findByTechnicalCommitteeNo(technicalCommitteeNo: String?): TechnicalCommittee?


}
