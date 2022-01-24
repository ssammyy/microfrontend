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


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StandardDraft

        if (id != other.id) return false
        if (title != other.title) return false
        if (requestorId != other.requestorId) return false
        if (requestorName != other.requestorName) return false
        if (standardOfficerId != other.standardOfficerId) return false
        if (standardOfficerName != other.standardOfficerName) return false
        if (versionNumber != other.versionNumber) return false

        if (approvalStatus != other.approvalStatus) return false
        if (approvedBy != other.approvedBy) return false

        if (proofReadBy != other.proofReadBy) return false

        if (editedStatus != other.editedStatus) return false
        if (editedBY != other.editedBY) return false
        if (proofreadStatus != other.proofreadStatus) return false
        if (proofReadDate != other.proofReadDate) return false
        if (draughtingStatus != other.draughtingStatus) return false
        if (draughtingBy != other.draughtingBy) return false
        if (draughtingDate != other.draughtingDate) return false


        if (taskId != other.taskId) return false
        if (submission_date != other.submission_date) return false
        if (status != other.status) return false


        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (title?.hashCode() ?: 0)
        result = 31 * result + (requestorId?.hashCode() ?: 0)
        result = 31 * result + (requestorName?.hashCode() ?: 0)
        result = 31 * result + (standardOfficerId?.hashCode() ?: 0)
        result = 31 * result + (standardOfficerName?.hashCode() ?: 0)
        result = 31 * result + (versionNumber?.hashCode() ?: 0)
        result = 31 * result + (approvalStatus?.hashCode() ?: 0)
        result = 31 * result + (approvedBy?.hashCode() ?: 0)

        result = 31 * result + (editedStatus?.hashCode() ?: 0)
        result = 31 * result + (editedBY?.hashCode() ?: 0)
        result = 31 * result + (proofreadStatus?.hashCode() ?: 0)
        result = 31 * result + (proofReadDate?.hashCode() ?: 0)
        result = 31 * result + (draughtingStatus?.hashCode() ?: 0)
        result = 31 * result + (draughtingBy?.hashCode() ?: 0)
        result = 31 * result + (draughtingDate?.hashCode() ?: 0)

        result = 31 * result + (taskId?.hashCode() ?: 0)
        result = 31 * result + (submission_date?.hashCode() ?: 0)
        result = 31 * result + (status?.hashCode() ?: 0)
        result = 31 * result + (proofReadBy?.hashCode() ?: 0)


        return result
    }

    override fun toString(): String {
        return "StandardDraft(id=$id, title=$title, requestorId=$requestorId,requestorName=$requestorName, standardOfficerId=$standardOfficerId,standardOfficerName=$standardOfficerName, versionNumber=$versionNumber, " +
                "approvedBy=$approvedBy,approvalStatus=$approvalStatus, editedStatus=$editedStatus," +
                 "editedBY=$editedBY, proofreadStatus=$proofreadStatus,"+
                "proofReadDate=$proofReadDate, draughtingStatus=$draughtingStatus,"+
                "draughtingBy=$draughtingBy, draughtingDate=$draughtingDate,proofReadBy=$proofReadBy"+
                "taskId=$taskId, submission_date=$submission_date,status=$status)"
    }


}
