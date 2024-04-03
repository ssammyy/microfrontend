package com.renegade.parcel.DAO.Repos

import com.renegade.parcel.DAO.Models.BatchTracking
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BatchTrackingRepository : JpaRepository<BatchTracking, Long> {
    // Custom queries can be added here if needed
}
