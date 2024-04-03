package com.letar.realetar.Models

import com.fasterxml.jackson.annotation.JsonBackReference
import javax.persistence.*

@Entity
@Table(name = "purchase_line")
 class PurchaseLine : BaseEntity() {

   @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_id")
    var purchaseHeader: PurchaseHeader? = null // Many-to-one relationship with PurchaseHeader

    var lineNumber: Int? = null
    var itemId: Int? = null
    var quantity: Int? = null
    var quantityReceived: Double? = null
    var unitCost: Double? = null
    var lineAmount: Double? = null
    var amountIncludingVAT: Double? = null
}
