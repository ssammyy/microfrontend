package org.kebs.app.kotlin.apollo.store.repo.std


import org.kebs.app.kotlin.apollo.store.model.std.SACSummary
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SACSummaryRepository : JpaRepository<SACSummary, Long> {

    fun findByVarField1(varField1: String): MutableList<SACSummary>
    fun findByVarField2(varField2: String): MutableList<SACSummary>
    fun findByVarField8(varField8: String): MutableList<SACSummary>

}
