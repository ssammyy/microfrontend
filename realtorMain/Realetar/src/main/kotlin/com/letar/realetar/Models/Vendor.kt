package com.letar.realetar.Models

import com.fasterxml.jackson.annotation.JsonBackReference
import lombok.Data
import javax.persistence.*

@Entity
@Table(name = "tb_vendors")
@Data
class Vendor : BaseEntity() {
    var name: String? = null
    var location: String? = null
    var contactPerson: String? = null
    var contact: String? = null
    var balance: Double? = null
    var invoiceAmounts: Double? = null
    var outstandingOrders: Double? = null
    var accountNo: String? = null

    @OneToMany(mappedBy = "vendor", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JsonBackReference
    var items: MutableList<VendorItemList> = mutableListOf()
}
