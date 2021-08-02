package org.kebs.app.kotlin.apollo.store.model.std

import javax.persistence.*
import kotlin.jvm.Transient

@Entity
@Table(name = "SD_SCHEME_MEMBERSHIP_REQUEST")
class SchemeMembershipRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "REQUEST_ID")
    @Basic
    val requestId : Long = 0

    @Column(name = "REQUESTER_NAME")
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

    @Column(name = "REQUEST_DATE")
    @Basic
    val date : String = ""

    @Column(name = "ACCOUNT_TYPE")
    @Basic
    val account_type : String = ""

    @Column(name = "SIC_ASSIGNED_ID")
    @Basic
    val sic_assigned_id : String? =""

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

    @Transient
    var taskID: String = ""
    var processID: String = ""
}
