package com.ngumo.inventoryapi.Dao.Repositories

import com.ngumo.inventoryapi.Dao.Models.SaleLines
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SaleLinesRepository : JpaRepository<SaleLines, Long> {
}