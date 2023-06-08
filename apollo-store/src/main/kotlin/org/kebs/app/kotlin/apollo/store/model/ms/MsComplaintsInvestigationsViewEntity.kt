package org.kebs.app.kotlin.apollo.store.model.ms

import org.kebs.app.kotlin.apollo.store.model.ms.MsComplaintsInvestigationsViewEntity
import org.kebs.app.kotlin.apollo.store.model.ms.MsPerformanceOfSelectedProductViewEntity
import java.io.Serializable
import java.sql.Date
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "MS_COMPLAINTS_INVESTIGATIONS_VIEW", schema = "APOLLO", catalog = "")
class MsComplaintsInvestigationsViewEntity : Serializable {
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
    @Column(name = "STATUS")
    var status: String? = null

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
    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as MsComplaintsInvestigationsViewEntity
        return referenceNumber == that.referenceNumber && complaintTitle == that.complaintTitle && targetedProducts == that.targetedProducts && transactionDate == that.transactionDate && approvedDate == that.approvedDate && rejectedDate == that.rejectedDate && assignedIo == that.assignedIo && status == that.status && region == that.region && county == that.county && town == that.town && complaintDepartment == that.complaintDepartment && division == that.division && timeTakenForAcknowledgement == that.timeTakenForAcknowledgement
    }

    override fun hashCode(): Int {
        return Objects.hash(
            referenceNumber,
            complaintTitle,
            targetedProducts,
            transactionDate,
            approvedDate,
            rejectedDate,
            assignedIo,
            status,
            region,
            county,
            town,
            complaintDepartment,
            division,
            timeTakenForAcknowledgement
        )
    }
}
