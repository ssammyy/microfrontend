package com.apollo.standardsdevelopment.repositories

import com.apollo.standardsdevelopment.models.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository:JpaRepository<Product,Long> {
}
