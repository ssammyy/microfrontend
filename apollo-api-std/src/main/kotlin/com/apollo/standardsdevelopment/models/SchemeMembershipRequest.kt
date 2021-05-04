package com.apollo.standardsdevelopment.models

import javax.persistence.*

@Entity
@Table(name = "SCHEME_MEMBERSHIP_REQUEST")
class SchemeMembershipRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "REQUEST_ID")
    @Basic
    val requestId : Long = 0

    @Column(name = "NAME")
    @Basic
    val name : String = ""

    @Column(name = "DESIGNATION_OCCUPATION")
    @Basic
    val designationOccupation : String = ""

    @Column(name = "ADDRESS")
    @Basic
    val address : String = ""

    @Column(name = "EMAIL")
    @Basic
    val email : String = ""

    @Column(name = "PHONE")
    @Basic
    val phone : String = ""

    @Column(name = "DATE")
    @Basic
    val date : String = ""

    @Column(name = "VAR_FIELD_1")
    @Basic
    var varField1: String? = null

    @Column(name = "VAR_FIELD_2")
    @Basic
    var varField2: String? = null

    @Column(name = "VAR_FIELD_3")
    @Basic
    var varField3: String? = null

    @Column(name = "VAR_FIELD_4")
    @Basic
    var varField4: String? = null
}
