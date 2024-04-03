package com.ngumo.inventoryapi.DTOs

import lombok.Data
import java.util.*

@Data
class SaleDto {
    val referenceCode: String ?= null
    val saleTotal: Long ?= null
    val paymentMethod: String ?= null
    val isCleared: Boolean ?= null
    val isCredit: Boolean ?= null
    val saleMadeBy: Long ?= null
    val customerName: String ?= null
    val customerTel: String ?= null
    val creditAmount: Long ?= null
    val proposedDateOfPayment: Date?=null
    val items : List<ItemDto> = listOf()
}
@Data
class ItemDto{
    val itemName: String ?= null
    val id: Long ?= null
    val newQuan: Long ?= null
    val amount: Long ?= null

}