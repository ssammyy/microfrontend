package com.ngumo.inventoryapi.Dao.Models

import lombok.Data
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Table(name = "TB_SALE_LINES")
@Data
@Entity
class SaleLines : BaseEntity() {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale")
    var sale: Sale ? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item")
    var inventory : Inventory?= null

    var referenceCode: String ?= null
    var itemName: String?= null
    var itemPrice: Long? = null
    var quantity: Long? = null


}