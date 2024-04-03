package com.letar.realetar.Repositories

import com.letar.realetar.Models.VendorItemList
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface VendorItemListRepo : JpaRepository<VendorItemList, Long> {
    fun findByItemCode(item: String): List<VendorItemList>

}