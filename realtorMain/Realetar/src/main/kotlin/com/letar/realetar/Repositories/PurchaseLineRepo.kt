package com.letar.realetar.Repositories

import com.letar.realetar.Models.PurchaseLine
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PurchaseLineRepo: JpaRepository<PurchaseLine, Long> {
}