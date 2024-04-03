package com.letar.realetar.Services

import com.letar.realetar.DataClasses.VendorsDto
import com.letar.realetar.Models.Vendor
import com.letar.realetar.Models.VendorItemList
import com.letar.realetar.Repositories.VendorRepository
import org.springframework.stereotype.Service

@Service
class VendorService(private val vendorRepository: VendorRepository) {

    fun getAllVendors(): List<Vendor> {

        return vendorRepository.findAll()
    }

    fun getVendorById(id: Long): Vendor? {
        return vendorRepository.findById(id).orElse(null)
    }

    fun createVendor(request: VendorsDto.CreateVendorRequest): Vendor {
        val itemList = mutableListOf<VendorItemList>()

        val vendorEntity = Vendor()
        vendorEntity.name = request.name
        vendorEntity.location = request.location
        vendorEntity.contactPerson = request.contactPerson
        vendorEntity.contact = request.contact
        vendorEntity.balance = request.balance
        vendorEntity.invoiceAmounts = request.invoiceAmounts
        vendorEntity.outstandingOrders = request.outstandingOrders
        vendorEntity.accountNo = request.accountNo
        val items = request.items.map { itemRequest ->
            val itemEntity = VendorItemList()
            itemEntity.vendor=vendorEntity
            itemEntity.name= itemRequest.name
            itemEntity.itemCode = itemRequest.itemCode
            itemEntity.description = itemRequest.description
            itemEntity.unitPrice = itemRequest.unitPrice
            itemList.add(itemEntity)
        }
        vendorEntity.items = itemList

        return vendorRepository.save(vendorEntity)
    }

    fun updateVendor(vendor: Vendor): Vendor {
        return vendorRepository.save(vendor)
    }

    fun deleteVendor(id: Long) {
        vendorRepository.deleteById(id)
    }
}
