package com.renegade.parcel.DAO.Models

import jakarta.persistence.*
import lombok.Data
import lombok.EqualsAndHashCode

@Entity
@Table(name = "parcel")
@Data
@EqualsAndHashCode(callSuper = true)
class Parcel(

        @Column(name = "description")
    var description: String,

        @Column(name = "weight")
    var weight: Double,

        @Column(name = "price")
    var price: Double,

        @Column(name = "cleared")
    var cleared: Boolean = false,

        @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    var itemCategory: ItemCategory,

        @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    var sender: Sender,

        @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id")
    var route: RouteEntity,

        @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_id")
    var batch: BatchTracking

) : BaseEntity()
