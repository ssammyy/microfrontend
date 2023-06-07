package org.kebs.app.kotlin.apollo.store.model.std

import javax.persistence.*
import kotlin.jvm.Transient

@Entity
@Table(name = "NEP_NOTIFICATION_CREATED")
class NEPNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "NEP_NOTIFICATION_ID")
    @Basic
    val notificationId: Long = 0

    @Column(name = "TC_NOTIFICATION_ID")
    @Basic
    val tcNotificationID: Long = 0

    @Column(name = "NEP_OFFICER_ID")
    @Basic
    val nepOfficerID: String = ""

    @Column(name = "NOTIFICATION_DOCUMENT_INDEX")
    @Basic
    val notificationDocumentIndex: String = ""

    @Column(name = "IS_APPROVED")
    @Basic
    val isApproved: Int = 0

    @Transient
    val taskID: String = ""
}
