package com.renegade.parcel.DAO.Models

import jakarta.persistence.*

import lombok.Data
import lombok.EqualsAndHashCode

@Entity
@Table(name = "item_category")
@Data
@EqualsAndHashCode(callSuper = true)
class ItemCategory(

    @Column(name = "category_name", nullable = false)
    var categoryName: String,

    @Column(name = "charges")
    var charges: Double

) : BaseEntity()
