package com.letar.realetar.Repositories

import com.letar.realetar.Models.Items
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ItemsRepository :JpaRepository<Items, Long>{
    fun findByItemCode(name : String): Items
}