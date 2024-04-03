package com.ngumo.inventoryapi.Dao.Repositories

import com.ngumo.inventoryapi.Dao.Models.Inventory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository



@Repository
interface InventoryRepository : JpaRepository<Inventory, Long> {

}