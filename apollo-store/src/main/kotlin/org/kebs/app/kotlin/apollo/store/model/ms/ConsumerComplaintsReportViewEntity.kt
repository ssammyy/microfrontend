package org.kebs.app.kotlin.apollo.store.model.ms

import org.kebs.app.kotlin.apollo.store.model.ms.ConsumerComplaintsReportViewEntity
import java.io.Serializable
import java.sql.Date
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "CONSUMER_COMPLAINTS_REPORT_VIEW", schema = "APOLLO", catalog = "")
class ConsumerComplaintsReportViewEntity : Serializable {
    @Id
    @Column(name = "ID")
    var id: Long? = null

    @Basic
    @Column(name = "REFERENCE_NUMBER")
    var referenceNumber: String? = null


    @Basic
    @Column(name = "COMPLAINANT")
    var complainant: String? = null

    @Basic
    @Column(name = "NATURE_COMPLAINT")
    var natureComplaint: String? = null

    @Basic
    @Column(name = "SECTOR")
    var sector: String? = null

    @Basic
    @Column(name = "DIVISION")
    var division: String? = null

    @Basic
    @Column(name = "REGION")
    var region: String? = null

    @Basic
    @Column(name = "DATE_RECEIVED")
    var dateReceived: String? = null

    @Basic
    @Column(name = "DATE_ACKNOWLEDGED")
    var dateAcknowledged: String? = null

    @Basic
    @Column(name = "INVESTIGATING_OFFICER")
    var investigatingOfficer: String? = null

    @Basic
    @Column(name = "DATE_COMPLETION_INVESTIGATION")
    var dateCompletionInvestigation: String? = null

    @Basic
    @Column(name = "DATE_FEEDBACK_COMPLAINANT")
    var dateFeedbackComplainant: String? = null

    @Basic
    @Column(name = "RESOLUTION")
    var resolution: String? = null

    @Basic
    @Column(name = "TIME_TAKEN_ACKNOWLEDGE")
    var timeTakenAcknowledge: String? = null

    @Basic
    @Column(name = "ACKNOWLEDGED_WITHIN2_DAYS_RECEIPT")
    var acknowledgedWithin2DaysReceipt: String? = null

    @Basic
    @Column(name = "TIME_TAKEN_PROVIDE_FEEDBACK")
    var timeTakenProvideFeedback: String? = null

    @Basic
    @Column(name = "FEEDBACK_WITHIN5DAYS_COMP_INVESTIGATION")
    var feedbackWithin5DaysCompInvestigation: String? = null

    @Basic
    @Column(name = "TIME_TAKEN_ADDRESS_COMPLAINT")
    var timeTakenAddressComplaint: String? = null

    @Basic
    @Column(name = "ADDRESSED_WITHIN28_DAYS_RECEIPT")
    var addressedWithin28DaysReceipt: String? = null

    @Basic
    @Column(name = "ASSIGNED_IO")
    var assignedIo: Long? = null

    @Basic
    @Column(name = "TRANSACTION_DATE")
    var transactionDate: Date? = null

    @Basic
    @Column(name = "COMPLAINT_DEPARTMENT")
    var complaintDepartment: Long? = null

}
