package com.letar.realetar.Repositories

import com.letar.realetar.Models.MaterialSchedule
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MaterialScheduleRepository : JpaRepository<MaterialSchedule, Long>{
    fun findAllByItemIdAndReleasedIsFalse(itemId: String): List<MaterialSchedule>


}
