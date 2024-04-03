package com.letar.realetar.Services

import com.letar.realetar.Models.Vendor
import com.letar.realetar.Repositories.MaterialScheduleRepository
import com.letar.realetar.Repositories.PurchaseHeaderRepo
import org.springframework.stereotype.Service

@Service
class MaterialScheduleUpdate(
        private val materialScheduleRepository: MaterialScheduleRepository,
        private val headerRepo: PurchaseHeaderRepo,
) {
    /**
     *function updates prices to recalculate the material costs after a change is introduced within a certain vendor
     * Once the price change is identified on the material schedule, we need to redo the orders that are not released
     */
    fun priceUpdate(newPrice: Double, batchNumber: String, itemId : String, vendor: Vendor ){
        val scheduleItem = materialScheduleRepository.findAllByItemIdAndReleasedIsFalse(itemId)
        for (item in scheduleItem){
            item.updatedPrice = newPrice
            item.vendor = vendor
            materialScheduleRepository.save(item)
        }

        //delete * headers that are not released
        val purchaseHeaders = headerRepo.findAllByDocStatus("open")
        headerRepo.deleteAll(purchaseHeaders)






    }
}