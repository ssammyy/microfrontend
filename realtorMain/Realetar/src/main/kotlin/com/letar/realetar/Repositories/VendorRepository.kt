package com.letar.realetar.Repositories

import com.letar.realetar.Models.Vendor
import org.springframework.data.jpa.repository.JpaRepository

interface VendorRepository : JpaRepository<Vendor, Long> {
    // You can add custom queries if needed


}
