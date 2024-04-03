package com.renegade.parcel.DAO.Repos

import com.renegade.parcel.DAO.Models.Parcel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ParcelRepository : JpaRepository<Parcel, Long> {
    // Custom queries can be added here if needed
}
