package com.apollo.standardsdevelopment.models

import javax.persistence.*

@Entity
@Table(name = "SD_NEP_NOTIFICATION")
class NationalEnquiryPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "NOTIFICATION_ID")
    @Basic
    val notificationId : Long = 0

    @Column(name = "NAME")
    @Basic
    val requesterName : String = ""

    @Column(name = "EMAIL")
    @Basic
    val requesterEmail : String =""

    @Column(name = "PHONE")
    @Basic
    val requesterPhone : String =""

    @Column(name = "INSTITUTION")
    @Basic
    val requesterInstitution : String = ""

    @Column(name = "COUNTRY")
    @Basic
    val requesterCountry : String = ""

    @Column(name = "SUBJECT")
    @Basic
    val requesterSubject : String = ""

    @Column(name = "COMMENT")
    @Basic
    val requesterComment : String = ""
}
