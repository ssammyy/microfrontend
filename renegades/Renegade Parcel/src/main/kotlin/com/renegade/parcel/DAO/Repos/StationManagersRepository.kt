package com.renegade.parcel.DAO.Repos

import com.renegade.parcel.DAO.Models.StationManager
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StationManagersRepository : JpaRepository<StationManager, Long> {
    // Custom queries can be added here if needed
}
