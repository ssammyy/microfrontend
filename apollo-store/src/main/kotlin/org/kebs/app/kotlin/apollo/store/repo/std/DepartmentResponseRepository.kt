package org.kebs.app.kotlin.apollo.store.repo.std

import org.kebs.app.kotlin.apollo.store.model.std.DepartmentResponse
import org.springframework.data.repository.CrudRepository

interface DepartmentResponseRepository : CrudRepository<DepartmentResponse, Long>
