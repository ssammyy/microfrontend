package com.ngumo.inventoryapi.Dao.Repositories

import com.ngumo.inventoryapi.Dao.Models.Sale
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SaleRepository : JpaRepository<Sale, Long>{
    fun findByReferenceCode(code : String ): Sale

}