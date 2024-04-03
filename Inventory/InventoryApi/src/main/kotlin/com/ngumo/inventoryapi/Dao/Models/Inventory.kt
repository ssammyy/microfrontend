package com.ngumo.inventoryapi.Dao.Models

import lombok.Data
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Data
@Table(name ="TBITEMS")
class Inventory : BaseEntity() {

    var itemName: String ? = null
    var sellingPrice: Long ? = null
    var buyingPrice: Long ? = null
    var quantity: Long ? = null

}