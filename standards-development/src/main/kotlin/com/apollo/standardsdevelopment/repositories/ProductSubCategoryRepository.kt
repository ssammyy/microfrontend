package com.apollo.standardsdevelopment.repositories

import com.apollo.standardsdevelopment.models.ProductSubCategory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductSubCategoryRepository:JpaRepository<ProductSubCategory,Long> {
}
