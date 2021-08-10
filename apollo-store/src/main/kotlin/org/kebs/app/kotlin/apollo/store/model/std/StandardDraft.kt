package org.kebs.app.kotlin.apollo.store.model.std

import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name="SD_STANDARD_DRAFT")
class StandardDraft {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    var id: Long =0

    @Column(name="TITLE")
    @Basic
    var title:String?=null

    @Column(name="REQUESTOR_ID")
    @Basic
    var requestorId:String?= null

    @Column(name="STANDARD_OFFICER_ID")
    @Basic
    var standardOfficerId:String?= null

    @Column(name="VERSION_NUMBER")
    @Basic
    var versionNumber:Long?=0

    @Transient
    @JsonProperty("taskId")
    var taskId:String?=null

    @Column(name="SUBMISSION_DATE")
    @Basic
    var submission_date: Timestamp?=null
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StandardDraft

        if (id != other.id) return false
        if (title != other.title) return false
        if (requestorId != other.requestorId) return false
        if (standardOfficerId != other.standardOfficerId) return false
        if (versionNumber != other.versionNumber) return false
        if (taskId != other.taskId) return false
        if (submission_date != other.submission_date) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (title?.hashCode() ?: 0)
        result = 31 * result + (requestorId?.hashCode() ?: 0)
        result = 31 * result + (standardOfficerId?.hashCode() ?: 0)
        result = 31 * result + (versionNumber?.hashCode() ?: 0)
        result = 31 * result + (taskId?.hashCode() ?: 0)
        result = 31 * result + (submission_date?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "StandardDraft(id=$id, title=$title, requestorId=$requestorId, standardOfficerId=$standardOfficerId, versionNumber=$versionNumber, taskId=$taskId, submission_date=$submission_date)"
    }


}
