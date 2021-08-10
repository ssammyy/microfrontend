package org.kebs.app.kotlin.apollo.store.model.std

import javax.persistence.*
import kotlin.jvm.Transient

@Entity
@Table(name = "SD_INFORMATION_TRACKER")
class InformationTracker {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "INFORMATION_TRACKER_ID")
    val informationTrackerId: Long = 0

    @Column(name = "NEP_OFFICER_ID")
    @Basic
    val nepOfficerId: Long? = null

    @Column(name = "FEEDBACK_SENT")
    @Basic
    val feedbackSent: String? = ""

    @Column(name = "REQUESTER_EMAIL")
    @Basic
    var requesterEmail: String? = ""

    @Column(name = "VAR_FIELD_3")
    @Basic
    var varField3: String? = null

    @Column(name = "VAR_FIELD_4")
    @Basic
    var varField4: String? = null

    @Transient
    var taskId: String = ""
}
