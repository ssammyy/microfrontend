package org.kebs.app.kotlin.apollo.store.repo.std

import org.kebs.app.kotlin.apollo.store.model.std.DataHolder
import org.kebs.app.kotlin.apollo.store.model.std.ProductSubCategory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface ProductSubCategoryRepository:JpaRepository<ProductSubCategory,Long> {

    fun findByProductId(PRODUCT_ID: Long?): MutableList<ProductSubCategory>

    @Query("SELECT p.name FROM ProductSubCategory p WHERE p.id=:id")
    fun findNameById(@Param("id") id: Long?): String

//    @Query("SELECT p.id, p.name, d.name FROM ProductSubCategory p Join Product d ON p.productId=d.id")
//    fun findAllWithDescriptionQuery(): MutableList<String>


    @Query(
        "SELECT t.ID, t.NAME, d.NAME AS tc_Title, t.DESCRIPTION AS v1, e.TC_TITLE AS v2, g.NAME AS v3, t.PRODUCT_ID AS v4, d.TECHNICAL_COMMITTEE_ID AS v5, g.ID AS v6 FROM SD_PRODUCT_SUBCATEGORY t Join SD_PRODUCTS d ON t.PRODUCT_ID=d.ID left JOIN  SD_TECHNICAL_COMMITTEE e ON d.TECHNICAL_COMMITTEE_ID = e.ID left JOIN  SD_DEPARTMENT g ON e.DEPARTMENT_ID = g.ID ORDER BY t.ID DESC",
        nativeQuery = true
    )
    fun findAllWithDescriptionQuery(): List<DataHolder>

}
