package com.letar.realetar.Services

import com.letar.realetar.Models.*
import com.letar.realetar.Repositories.*
import com.letar.realetar.Utilities.Logging
import lombok.extern.slf4j.Slf4j
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
@Slf4j
class OrderService(
        private val itemsRepository: ItemsRepository,
        private val vendorRepository: VendorRepository,
        private val vendorItemListrepo: VendorItemListRepo,
        private val purchaseHeaderRepo: PurchaseHeaderRepo,
        private val purchaseLineRepo: PurchaseLineRepo
) {

    /**
     * Function generates order based on the uploaded material schedule.
     * This allows the breakdown of cheapest sellers etc...
     */

    fun generateOrders(materialScheduleList: List<MaterialSchedule>) {
//      val purchaseHeader = PurchaseHeader()
        for (schedule in materialScheduleList) {
            var orderDate: LocalDate? = null
            var selectedVendor: Int? = null
            val mapp = mutableMapOf<Double, Int>()
            val item = schedule.itemId?.let { itemsRepository.findByItemCode(it) }
            val sumTotalArg = mutableListOf<Double>()
            val cheapestArg = mutableListOf<Double>()

            if (item != null) {
                if (item.defaultVendor !== null) {
                    selectedVendor = item.defaultVendor
                } else {
                    val itemList = vendorItemListrepo.findByItemCode(item.id.toString())
                    for (itemInList in itemList) {
                        itemInList.unitPrice?.let { cheapestArg.add(it) }
                        itemInList.vendor?.let { mapp.put(itemInList.unitPrice!!, it.id.toInt()) }
                    }
                    val cheapest = cheapestArg.minOrNull()
                    selectedVendor = mapp.get(cheapest)
                }
                val purchaseLine = PurchaseLine()
                val vendor = selectedVendor?.let { vendorRepository.findById(it.toLong()) }
                orderDate = schedule.startDate?.minusDays(7)
                val purchaseHeader = orderDate?.let {
                    vendor?.let { it1 ->
                        purchaseHeaderRepo.findByVendorAndDocDate(
                                it1.get(), it)
                    }
                }
                if (purchaseHeader !== null) {
                    purchaseLine.purchaseHeader = purchaseHeader
                } else {
                    val purchaseHeaderNew = PurchaseHeader()
                    purchaseHeaderNew.docDate = orderDate
                    purchaseHeaderNew.vendor = vendor?.get()
                    purchaseHeaderNew.expectedDeliveryDate = schedule.startDate?.minusDays(1)
                    purchaseHeaderNew.totalAmount = 0.0
                    purchaseHeaderNew.amountIncludingVAT = 0.0
                    purchaseHeaderRepo.save(purchaseHeaderNew)
                    purchaseLine.purchaseHeader = purchaseHeaderNew
                }
                val cost = item.approxUnitCost?.times(.16)
                val amountWithVat = schedule.quantity?.let { cost?.times(it) }
                purchaseLine.itemId = item.id.toInt()
                purchaseLine.amountIncludingVAT = amountWithVat
                purchaseLine.quantity = schedule.quantity
                purchaseLine.lineAmount = schedule.quantity?.let { item.approxUnitCost?.times(it) }
                purchaseLine.unitCost = cost
                purchaseLine.quantityReceived = 0.0
                amountWithVat?.let { sumTotalArg.add(it) }
                purchaseLineRepo.save(purchaseLine)
            }
        }
    }
}