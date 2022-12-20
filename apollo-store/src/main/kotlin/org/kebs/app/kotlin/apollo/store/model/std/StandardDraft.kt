package org.kebs.app.kotlin.apollo.store.model.std

import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name="SD_STANDARD_DRAFT")
class StandardDraft {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    var id: Long = 0

    @Column(name = "TITLE")
    @Basic
    var title: String? = null

    @Column(name = "REQUESTOR_ID")
    @Basic
    var requestorId: String? = null

    @Column(name = "REQUESTOR_NAME")
    @Basic
    var requestorName: String? = null

    @Column(name = "STANDARD_OFFICER_ID")
    @Basic
    var standardOfficerId: String? = null

    @Column(name = "STANDARD_OFFICER_NAME")
    @Basic
    var standardOfficerName: String? = null

    @Column(name = "VERSION_NUMBER")
    @Basic
    var versionNumber: Long? = 0

    @Column(name = "APPROVAL_STATUS")
    @Basic
    var approvalStatus: String? = null

    @Column(name = "APPROVED_BY")
    @Basic
    var approvedBy: String? = null

    @Column(name = "EDITED_STATUS")
    @Basic
    var editedStatus: String? = null

    @Column(name = "EDITED_BY")
    @Basic
    var editedBY: String? = null

    @Column(name = "EDITED_DATE")
    @Basic
    var editedDate: Timestamp? = null

    @Column(name = "PROOFREAD_STATUS")
    @Basic
    var proofreadStatus: String? = null

    @Column(name = "PROOFREAD_BY")
    @Basic
    var proofReadBy: String? = null

    @Column(name = "PROOFREAD_DATE")
    @Basic
    var proofReadDate: Timestamp? = null

    @Column(name = "DRAUGHTING_STATUS")
    @Basic
    var draughtingStatus: String? = null

    @Column(name = "DRAUGHTED_BY")
    @Basic
    var draughtingBy: String? = null

    @Column(name = "DRAUGHTING_DATE")
    @Basic
    var draughtingDate: Timestamp? = null

    @Transient
    @JsonProperty("taskId")
    var taskId: String? = null

    @Column(name = "SUBMISSION_DATE")
    @Basic
    var submission_date: Timestamp? = null

    @Column(name = "STATUS")
    @Basic
    var status: String? = null

    @Column(name = "STANDARD_TYPE")
    @Basic
    var standardType: String? = null

    @Column(name = "DRAFT_ID")
    @Basic
    var draftId: Long = 0






}
