package com.letar.realetar.Models

import lombok.Data
import javax.persistence.*

@Entity
@Data
@Table(name = "TB_USERS")
class Users : BaseEntity() {
    var username: String ? = null
    var firstName: String ? = null
    var SecondName: String ? = null
    var phoneNumber: String ? = null
    var password: String ? = null
    var gender: String ? = null
    var role: String ? = null
}