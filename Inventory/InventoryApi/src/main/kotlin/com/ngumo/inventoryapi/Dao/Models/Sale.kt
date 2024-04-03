package com.ngumo.inventoryapi.Dao.Models

import lombok.Data
import javax.persistence.*

@Entity
@Data
@Table(name = "TB_SALE")
class Sale: BaseEntity() {

    var referenceCode: String ?= null
    var cartQuantity: Long ?= null
    var saleTotal: Long ?= null
    var paymentMethod: String ?= null
    var isCleared: Boolean ?= null
    var isCredit: Boolean ?= null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "madeBy")
    var user: User? = null
    var customerName: String?= null
    var customerTel: String?= null
    var creditAmount: Long?= null



}