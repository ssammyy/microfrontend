package org.kebs.app.kotlin.apollo.store.repo.std


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

}
