package com.letar.realetar.Repositories

import com.letar.realetar.Models.PurchaseHeader
import com.letar.realetar.Models.Vendor
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface PurchaseHeaderRepo: JpaRepository<PurchaseHeader, Long> {
    fun findByVendorAndDocDate(vendor: Vendor, date: LocalDate): PurchaseHeader?
    fun findAllByDocStatus(status : String): List<PurchaseHeader>
}