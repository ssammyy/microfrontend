package com.renegade.parcel.DAO.Repos

import com.renegade.parcel.DAO.Models.RouteEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RoutesRepository : JpaRepository<RouteEntity, Long> {
    // Custom queries can be added here if needed
}
