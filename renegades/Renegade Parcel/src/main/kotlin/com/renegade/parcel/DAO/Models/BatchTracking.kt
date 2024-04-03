package com.renegade.parcel.DAO.Models

import jakarta.persistence.*
import lombok.Data
import lombok.EqualsAndHashCode

@Entity
@Table(name = "batch_tracking")
@Data
@EqualsAndHashCode(callSuper = true)
class BatchTracking(

    @Column(name = "dispatched")
    var dispatched: Boolean = false,

    @Column(name = "cleared")
    var cleared: Boolean = false,

    @Column(name = "delivered")
    var delivered: Boolean = false,

    @OneToMany(mappedBy = "batch")
    var parcels: Set<Parcel> = HashSet()

) : BaseEntity()
