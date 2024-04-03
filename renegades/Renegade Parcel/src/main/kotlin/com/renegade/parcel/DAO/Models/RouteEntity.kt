package com.renegade.parcel.DAO.Models

import jakarta.persistence.*
import lombok.Data
import lombok.EqualsAndHashCode

@Entity
@Table(name = "tb_routes")
@Data
@EqualsAndHashCode(callSuper = true)
class RouteEntity(

    @Column(name = "from", nullable = false)
    var from: String,

    @Column(name = "to", nullable = false)
    var to: String,

    @Column(name = "duration")
    var duration: Int,




) : BaseEntity()
