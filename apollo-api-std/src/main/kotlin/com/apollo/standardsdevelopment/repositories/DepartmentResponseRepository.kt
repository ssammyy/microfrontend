package com.apollo.standardsdevelopment.repositories

import com.apollo.standardsdevelopment.models.DepartmentResponse
import org.springframework.data.repository.CrudRepository

interface DepartmentResponseRepository : CrudRepository<DepartmentResponse, Long> {
}
