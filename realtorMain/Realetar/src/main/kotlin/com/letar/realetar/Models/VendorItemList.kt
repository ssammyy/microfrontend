package com.letar.realetar.Models

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonManagedReference
import javax.persistence.*

@Entity
@Table(name = "tb_vendor_item_list")
 class VendorItemList: BaseEntity() {
    var name: String? = null
    var description: String? = null
    var unitPrice: Double? = null
    var itemCode: String? = null
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id")
    @JsonManagedReference
    var vendor: Vendor? = null
    @ManyToOne(fetch = FetchType.LAZY )
    @JoinColumn(name = "item_id")
    var item : Items? = null



}
