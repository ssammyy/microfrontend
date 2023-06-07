package org.kebs.app.kotlin.apollo.store.repo.std


import org.kebs.app.kotlin.apollo.store.model.std.DataHolder
import org.kebs.app.kotlin.apollo.store.model.std.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository : JpaRepository<Product, Long> {
    fun findByTechnicalCommitteeId(technicalCommitteeId: Long?): MutableList<Product>

    @Query("SELECT p.name FROM Product p WHERE p.id=:id")
    fun findNameById(@Param("id") id: Long?): String

//    @Query("SELECT p.id, p.name, d.title FROM Product p Join TechnicalCommittee d ON p.technicalCommitteeId=d.id")
//    fun findAllWithDescriptionQuery(): MutableList<String>

    @Query(
        "SELECT t.ID, t.NAME, d.TC_TITLE, t.DESCRIPTION AS v1,  e.NAME AS v2, t.TECHNICAL_COMMITTEE_ID as v3, e.ID as v4  FROM SD_PRODUCTS t Join SD_TECHNICAL_COMMITTEE d ON t.TECHNICAL_COMMITTEE_ID=d.ID LEFT Join SD_DEPARTMENT e ON d.DEPARTMENT_ID=e.ID ORDER BY t.ID DESC ",
        nativeQuery = true
    )
    fun findAllWithDescriptionQuery(): List<DataHolder>

    fun existsProductByTechnicalCommitteeId(technicalCommitteeId: Long?):Boolean


}
