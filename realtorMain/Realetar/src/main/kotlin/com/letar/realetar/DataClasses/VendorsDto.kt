package com.letar.realetar.DataClasses

class VendorsDto {
    data class CreateVendorRequest(
        val name: String,
        val location: String,
        val contactPerson: String,
        val contact: String,
        val balance: Double,
        val invoiceAmounts: Double,
        val outstandingOrders: Double,
        val accountNo: String,
        val items: List<ItemRequest>
    )

    data class ItemRequest(
        val name: String,
        val description: String,
        val unitPrice: Double,
        val itemCode: String
    )

}