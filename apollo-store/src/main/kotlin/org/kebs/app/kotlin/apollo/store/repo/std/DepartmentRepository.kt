package org.kebs.app.kotlin.apollo.store.repo.std

import org.kebs.app.kotlin.apollo.store.model.std.Department
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface DepartmentRepository : JpaRepository<Department,Long>{
    @Query("SELECT d.name FROM Department d WHERE d.id=:id")
    fun findNameById(@Param("id") id: Long?): String
}
