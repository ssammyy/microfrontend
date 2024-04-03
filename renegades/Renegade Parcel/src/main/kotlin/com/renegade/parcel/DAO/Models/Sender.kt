package com.renegade.parcel.DAO.Models

import jakarta.persistence.*
import lombok.Data
import lombok.EqualsAndHashCode

@Entity
@Table(name = "senders")
@Data
@EqualsAndHashCode(callSuper = true)
class Sender(

    @Column(name = "names", nullable = false)
    var names: String,

    @Column(name = "phone_number", nullable = false)
    var phoneNumber: String,

    @Column(name = "email")
    var email: String,

    @OneToMany(mappedBy = "sender")
    var parcels: Set<Parcel> = HashSet()

) : BaseEntity()
