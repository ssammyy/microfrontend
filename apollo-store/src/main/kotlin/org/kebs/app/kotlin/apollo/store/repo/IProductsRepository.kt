package org.kebs.app.kotlin.apollo.store.repo

import org.kebs.app.kotlin.apollo.store.model.ProductsEntity
import org.springframework.data.hazelcast.repository.HazelcastRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface IProductsRepository: HazelcastRepository<ProductsEntity, Long> {

    fun findByProductCategoryId(productCategoryId : Long): List<ProductsEntity>
    fun findByStatusOrderByName(status: Int): List<ProductsEntity>?

//    fun findById(id : Long) : ProductsEntity?

}