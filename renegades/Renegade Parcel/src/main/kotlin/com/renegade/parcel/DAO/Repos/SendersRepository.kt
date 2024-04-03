package com.renegade.parcel.DAO.Repos

import com.renegade.parcel.DAO.Models.Sender
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SendersRepository : JpaRepository<Sender, Long> {
    // Custom queries can be added here if needed
}
