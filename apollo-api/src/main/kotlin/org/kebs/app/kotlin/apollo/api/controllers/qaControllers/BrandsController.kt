package org.kebs.app.kotlin.apollo.api.controllers.qaControllers

import com.google.gson.Gson
import com.google.gson.JsonObject
import mu.KotlinLogging

import org.kebs.app.kotlin.apollo.store.model.CdLaboratoryParametersEntity
import org.kebs.app.kotlin.apollo.store.repo.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.math.BigDecimal

@RestController
class BrandsController(
        private val productSubCategoryRepo: IProductSubcategoryRepository,
        private val standardsCategoryRepository: IStandardCategoryRepository,
        private val sampleStandardsRepository: ISampleStandardsRepository,
        private val broadProductCategoryRepository: IBroadProductCategoryRepository,
        private val productsRepo: IProductsRepository,

        private val  productCategoriesRepository:IKebsProductCategoriesRepository,
        private val permitRepo: IPermitRepository
) {
    /**
     * find by division id
     */
    @GetMapping("/populate-broad-category/{id}",  produces = [MediaType.APPLICATION_JSON_VALUE])
    fun populateBroadCategory(@PathVariable("id") id: Long): String? {
        val gson = Gson()
        return gson.toJson(broadProductCategoryRepository.findByDivisionId(standardsCategoryRepository.findByIdOrNull(id)))
    }

    /**
     * find by broad category id
     */
    @GetMapping("/populate-product-category/{id}",  produces = [MediaType.APPLICATION_JSON_VALUE])
    fun populateProductCategory(@PathVariable("id") id: Long): String? {
        val gson = Gson()
        return gson.toJson(productCategoriesRepository.findByBroadProductCategoryId(id))
//        return gson.toJson(broadProductCategoryRepository.findByIdOrNull(1L)?.id?.let {productsRepo.findByBroadProductCategoryId(it)})
    }

    /**
     * find by product category
     */

    @GetMapping("/populate-product-categories/{id}",  produces = [MediaType.APPLICATION_JSON_VALUE])
    fun findByProductCaId(@PathVariable("id") id: Long): String? {
        val gson = Gson()
        return gson.toJson(productsRepo.findByProductCategoryId(id))
    }

    /**
     * find by product id
     */
    @GetMapping("/populate-sub-category/{id}",  produces = [MediaType.APPLICATION_JSON_VALUE])
    fun populateP(@PathVariable("id") id: Long): String? {
        val gson = Gson()
        return gson.toJson(productSubCategoryRepo.findByProductId(id))
    }

    /**
     * find by sub category id
     */
    @GetMapping("/populate-standards/{id}",  produces = [MediaType.APPLICATION_JSON_VALUE])
    fun populateStandards(@PathVariable("id") id: Long): String? {
        val gson = Gson()
        return gson.toJson(sampleStandardsRepository.findBySubCategoryId(id))
    }

    @PostMapping("/load-manufacturer-permits/{manufacturer}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun populateManufacturerPermits(@PathVariable("manufacturer")manufacturer: String): String {
        KotlinLogging.logger {}.info { manufacturer }
        val gson = Gson()
        return gson.toJson(permitRepo.findByManufacturerName(manufacturer))
    }

    /**
     * find by broad category id
     */


//    @PostMapping("/mpesa-stkpush",  produces = [MediaType.APPLICATION_JSON_VALUE])
//    fun payPermitIncoice(
//    ): String? {
//        val gson = Gson()
//        val jMpesa = JsonObject()
//        jMpesa.addProperty("ACCESS_TOKEN", mpesaService.authenticateTokens())
//        jMpesa.addProperty("Amount", amount)
//        jMpesa.addProperty("PhoneNumber", mpesaService.sanitizePhoneNumber(phoneNumber) )
//
//        val mySTKPUSH = mpesaService.STKPUSH(jMpesa)
//        KotlinLogging.logger { }.info { "My mpesa response details:  = $mySTKPUSH" }
//
//        return gson.toJson(mySTKPUSH)
////        return gson.toJson(broadProductCategoryRepository.findByIdOrNull(1L)?.id?.let {productsRepo.findByBroadProductCategoryId(it)})
//    }

}