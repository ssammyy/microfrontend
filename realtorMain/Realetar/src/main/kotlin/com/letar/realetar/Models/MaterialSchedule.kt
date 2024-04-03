package com.letar.realetar.Models

import lombok.Data
import java.time.LocalDate
import java.util.Date
import javax.persistence.*
import javax.swing.text.Element

@Entity
@Data
@Table(name="TB_MATERIAL_SCHEDULE")
class MaterialSchedule: BaseEntity(){
    var structure : String? = null
    var element : String? = null
    var floor : String? = null
    var material : String? = null
    var quantity : Int? = null
    var itemName : String? = null
    var itemId: String? = null
    var startDate : LocalDate? = null
    var orderDate : LocalDate? = null
    var updatedQuantity : Int? = null
    var updatedPrice : Double? = null
    var updatedStartDate : LocalDate? = null
    var released: Boolean? = null
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id")
    var vendor: Vendor? = null;
}