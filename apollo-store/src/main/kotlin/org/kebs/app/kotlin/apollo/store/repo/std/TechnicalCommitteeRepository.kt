package org.kebs.app.kotlin.apollo.store.repo.std

import org.kebs.app.kotlin.apollo.store.model.std.TechnicalCommittee
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TechnicalCommitteeRepository : JpaRepository<TechnicalCommittee, Long>{
    fun findByDepartmentId(departmentId: Long?) : MutableList<TechnicalCommittee>

    @Query("SELECT t.title FROM TechnicalCommittee t WHERE t.id=:id")
    fun findNameById(@Param("id") id: Long?): String
}
