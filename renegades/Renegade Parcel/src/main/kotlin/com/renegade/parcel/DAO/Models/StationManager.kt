package com.renegade.parcel.DAO.Models

import jakarta.persistence.*
import lombok.Data
import lombok.EqualsAndHashCode

@Entity
@Table(name = "station_managers")
@Data
@EqualsAndHashCode(callSuper = true)
class StationManager(

    @Column(name = "name", nullable = false)
    var name: String,

    @Column(name = "phone", nullable = false)
    var phone: String,

    @Column(name = "email")
    var email: String,

    @OneToMany(mappedBy = "id")
    var routes: Set<RouteEntity> = HashSet()

) : BaseEntity()
