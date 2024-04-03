package com.ngumo.inventoryapi.Dao.Repositories

import com.ngumo.inventoryapi.Dao.Models.Debt
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DebtRepository: JpaRepository<Debt, Long> {
}