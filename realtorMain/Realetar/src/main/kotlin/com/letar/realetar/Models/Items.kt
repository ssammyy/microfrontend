package com.letar.realetar.Models

import com.fasterxml.jackson.annotation.JsonIgnore
import lombok.Data
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToMany
import javax.persistence.ManyToOne
import javax.persistence.Table


@Table(name = "TB_ITEMS")
@Entity
@Data

class Items: BaseEntity() {
    var description : String? = null
    var unitOfMeasure : String? = null
    var itemType : String? = null
    var approxUnitCost : Double? = null
    var quantityInStock : Double? = null
    var itemCode : String? = null
    var defaultVendor : Int? = null
}