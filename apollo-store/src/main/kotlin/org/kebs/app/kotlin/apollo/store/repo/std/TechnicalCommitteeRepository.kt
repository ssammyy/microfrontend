package org.kebs.app.kotlin.apollo.store.repo.std

import org.kebs.app.kotlin.apollo.store.model.std.DataHolder
import org.kebs.app.kotlin.apollo.store.model.std.TechnicalCommittee
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface TechnicalCommitteeRepository : JpaRepository<TechnicalCommittee, Long> {
    fun findByDepartmentId(departmentId: Long?): MutableList<TechnicalCommittee>

    @Query("SELECT t.title FROM TechnicalCommittee t WHERE t.id=:id")
    fun findNameById(@Param("id") id: Long?): String

    @Query(
        "SELECT t.ID, t.TC_TITLE,t.PARENT_COMMITTEE AS V1,t.TECHNICAL_COMMITTEE_NO AS V2, d.NAME, d.ID AS V3 FROM SD_TECHNICAL_COMMITTEE t Join SD_DEPARTMENT d ON t.DEPARTMENT_ID=d.ID ORDER BY t.ID DESC ",
        nativeQuery = true
    )
    fun findAllWithDescriptionQuery(): List<DataHolder>

    fun findTechnicalCommitteeByDepartmentId(departmentId: Long?): TechnicalCommittee?
    fun existsTechnicalCommitteeByDepartmentId(departmentId: Long?): Boolean

    fun findAllByOrderByIdDesc(): MutableList<TechnicalCommittee>

    fun findByTechnicalCommitteeNo(technicalCommitteeNo: String?): MutableList<TechnicalCommittee>


}
