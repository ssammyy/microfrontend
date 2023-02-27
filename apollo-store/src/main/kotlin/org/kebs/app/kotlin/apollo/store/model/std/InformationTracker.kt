package org.kebs.app.kotlin.apollo.store.model.std

import javax.persistence.*
import kotlin.jvm.Transient

@Entity
@Table(name = "SD_INFORMATION_TRACKER")
class InformationTracker {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "INFORMATION_TRACKER_ID")
    var informationTrackerId: Long = 0

    @Column(name = "NEP_OFFICER_ID")
    @Basic
    var nepOfficerId: Long? = null

    @Column(name = "ENQUIRY_ID")
    @Basic
    var enquiryId: Long? = null

    @Column(name = "RESPONSE_ID")
    @Basic
    var responseId: Long? = null

    @Column(name = "FEEDBACK_SENT")
    @Basic
    var feedbackSent: String? = ""

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
