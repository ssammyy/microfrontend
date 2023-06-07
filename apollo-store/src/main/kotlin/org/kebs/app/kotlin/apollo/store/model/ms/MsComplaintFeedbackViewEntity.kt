package org.kebs.app.kotlin.apollo.store.model.ms

import org.kebs.app.kotlin.apollo.store.model.ms.MsComplaintFeedbackViewEntity
import org.kebs.app.kotlin.apollo.store.model.ms.MsReportSubmittedCpViewEntity
import org.kebs.app.kotlin.apollo.store.model.ms.MsAcknowledgementTimelineViewEntity
import org.kebs.app.kotlin.apollo.store.model.ms.MsSampleSubmittedCpViewEntity
import java.io.Serializable
import java.sql.Date
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "MS_COMPLAINT_FEEDBACK_VIEW", schema = "APOLLO", catalog = "")
class MsComplaintFeedbackViewEntity : Serializable {
    @Id
    @Column(name = "REFERENCE_NUMBER")
    var referenceNumber: String? = null

    @Basic
    @Column(name = "COMPLAINT_TITLE")
    var complaintTitle: String? = null

    @Basic
    @Column(name = "TARGETED_PRODUCTS")
    var targetedProducts: String? = null

    @Basic
    @Column(name = "TRANSACTION_DATE")
    var transactionDate: Date? = null

    @Basic
    @Column(name = "APPROVED_DATE")
    var approvedDate: Date? = null

    @Basic
    @Column(name = "REJECTED_DATE")
    var rejectedDate: Date? = null

    @Basic
    @Column(name = "ASSIGNED_IO")
    var assignedIo: Long? = null

    @Basic
    @Column(name = "MS_PROCESS_ID")
    var msProcessId: Long? = null

    @Basic
    @Column(name = "ACKNOWLEDGEMENT_TYPE")
    var acknowledgementType: String? = null

    @Basic
    @Column(name = "REGION")
    var region: Long? = null

    @Basic
    @Column(name = "COUNTY")
    var county: Long? = null

    @Basic
    @Column(name = "TOWN")
    var town: Long? = null

    @Basic
    @Column(name = "COMPLAINT_DEPARTMENT")
    var complaintDepartment: Long? = null

    @Basic
    @Column(name = "DIVISION")
    var division: Long? = null

    @Basic
    @Column(name = "TIME_TAKEN_FOR_ACKNOWLEDGEMENT")
    var timeTakenForAcknowledgement: String? = null

    @Basic
    @Column(name = "FEEDBACK_SENT")
    var feedbackSent: String? = null

    @Basic
    @Column(name = "TIME_TAKEN_FOR_FEEDBACK_SENT")
    var timeTakenForFeedbackSent: String? = null


}
