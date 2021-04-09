package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.BroadProductCategoryEntity
import org.kebs.app.kotlin.apollo.store.model.ProductSubcategoryEntity
import org.kebs.app.kotlin.apollo.store.model.ProductsEntity
import org.kebs.app.kotlin.apollo.store.model.StandardsCategoryEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface IBroadProductCategoryRepository : HazelcastRepository<BroadProductCategoryEntity, Long>{
    fun findByDivisionId(divisionId: StandardsCategoryEntity?): MutableList<BroadProductCategoryEntity>
    fun findByStatusOrderByCategory(status: Int): List<BroadProductCategoryEntity>?
}


//var regions: Map<String, Set<String>> = regionsService.getRegions()


@Repository
interface IProductSubcategoryRepository : HazelcastRepository<ProductSubcategoryEntity, Long>{
    fun findByProductId(productId: Long): List<ProductSubcategoryEntity>
    fun findByStatusOrderByName(status: Int): List<ProductSubcategoryEntity>?
}


