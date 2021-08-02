package org.kebs.app.kotlin.apollo.store.repo.std

import org.kebs.app.kotlin.apollo.store.model.std.ProductSubCategory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface ProductSubCategoryRepository:JpaRepository<ProductSubCategory,Long> {

    fun findByProductId( PRODUCT_ID: Long?) : MutableList<ProductSubCategory>

    @Query("SELECT p.name FROM ProductSubCategory p WHERE p.id=:id")
    fun findNameById(@Param("id") id: Long?): String

}
