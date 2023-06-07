package org.kebs.app.kotlin.apollo.store.repo.std

import org.kebs.app.kotlin.apollo.store.model.std.RankingCriteria
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface RankingCriteriaRepository : JpaRepository<RankingCriteria, Long> {

    @Query("SELECT r.rank FROM RankingCriteria r WHERE r.departmentId=:departmentId AND r.committeeId=:committeeId")
    fun findRankByAll(@Param("departmentId") DEPARTMENT_ID: Long?,
                      @Param("committeeId") COMMITTEE_ID: Long?
                      ): Long

}
