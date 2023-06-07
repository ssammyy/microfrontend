package org.kebs.app.kotlin.apollo.store.model.ms

import org.kebs.app.kotlin.apollo.store.model.ms.MsComplaintFeedbackViewEntity
import org.kebs.app.kotlin.apollo.store.model.ms.MsReportSubmittedCpViewEntity
import org.kebs.app.kotlin.apollo.store.model.ms.MsAcknowledgementTimelineViewEntity
import org.kebs.app.kotlin.apollo.store.model.ms.MsSampleSubmittedCpViewEntity
import java.io.Serializable
import java.math.BigInteger
import java.sql.Date
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "MS_ACKNOWLEDGEMENT_TIMELINE_VIEW", schema = "APOLLO", catalog = "")
class MsAcknowledgementTimelineViewEntity : Serializable {
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
    var assignedIo: BigInteger? = null

    @Basic
    @Column(name = "ACKNOWLEDGEMENT_TYPE")
    var acknowledgementType: String? = null

    @Basic
    @Column(name = "REGION")
    var region: BigInteger? = null

    @Basic
    @Column(name = "COUNTY")
    var county: BigInteger? = null

    @Basic
    @Column(name = "TOWN")
    var town: BigInteger? = null

    @Basic
    @Column(name = "COMPLAINT_DEPARTMENT")
    var complaintDepartment: BigInteger? = null

    @Basic
    @Column(name = "DIVISION")
    var division: BigInteger? = null

    @Basic
    @Column(name = "TIME_TAKEN_FOR_ACKNOWLEDGEMENT")
    var timeTakenForAcknowledgement: String? = null
}
