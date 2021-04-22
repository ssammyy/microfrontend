package com.apollo.standardsdevelopment.repositories

import com.apollo.standardsdevelopment.models.Department
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DepartmentRepository : JpaRepository<Department,Long>{
}
