package org.kebs.app.kotlin.apollo.standardsdevelopment.models

import javax.persistence.*

@Entity
@Table(name = "NEP_DOMESTIC_NOTIFICATION")
class NotificationReceived {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "NOTIFICATION_ID")
    @Basic
    val notificationId : Long = 0

    @Column(name = "TC_SECRETARY_ID")
    @Basic
    val tcSecretaryId : Long = 0

    @Column(name = "NEP_OFFICER_ID")
    @Basic
    val nepOfficerId : Long = 0

    @Column(name = "NOTIFICATION_DUE_INDEX")
    @Basic
    val notificationDueIndex : String = ""

    @Column(name = "NOTIFICATION_CATEGORY")
    @Basic
    //(Can be:- Regulation, Conformity Assessment Proceedure, Standard)
    val notificationCategory : String = ""

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
