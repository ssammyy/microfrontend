package org.kebs.app.kotlin.apollo.standardsdevelopment.models

import javax.persistence.*

@Entity
@Table(name = "SD_USER")
class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    var id: Long =0

    @Column(name="NAME")
    @Basic
    var name: String?= null

    @Column(name="PHONENUMBER")
    @Basic
    var phoneNumber:String?=null

    @Column(name="EMAIL")
    @Basic
    var email:String?= null

    @OneToMany
    @JoinColumn(name="USER_ID")
    var standardRequest:Set<StandardRequest>?=null
}
