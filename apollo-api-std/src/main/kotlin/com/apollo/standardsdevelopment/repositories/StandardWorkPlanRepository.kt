package com.apollo.standardsdevelopment.repositories

import com.apollo.standardsdevelopment.models.StandardWorkPlan
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StandardWorkPlanRepository : JpaRepository<StandardWorkPlan,Long>{
}
