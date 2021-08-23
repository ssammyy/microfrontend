package org.kebs.app.kotlin.apollo.store.model.std

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import java.sql.Timestamp
import javax.persistence.*
import kotlin.jvm.Transient


@Entity
@Table(name = "SD_NWA_JUSTIFICATION")
class NWAJustification : Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    @Column(name = "MEETING_DATE")
    @Basic
    var meetingDate: String? = null

    @Column(name = "KNW")
    @Basic
    var knw: String? = null

    @Column(name = "KNW_SEC")
    @Basic
    var knwSecretary: String? = null

    @Column(name = "SL_NUMBER")
    @Basic
    var sl: String? = null

    @Column(name = "REQUEST_NUMBER")
    @Basic
    var requestNumber: String? = null

    @Column(name = "REQUESTED_BY")
    @Basic
    var requestedBy: String? = null

    @Column(name = "ISSUES_ADDRESSED")
    @Basic
    var issuesAddressed: String? = null

    @Column(name = "KNW_ACCEPTANCE_DATE")
    @Basic
    var knwAcceptanceDate: String? = null

    @Column(name = "REFERENCE_MATERIAL")
    @Basic
    var referenceMaterial: String? = null

    @Column(name = "DEPARTMENT")
    @Basic
    var department: String? = null

    @Column(name = "STATUS")
    @Basic
    var status: String? = null

    @Column(name = "REMARKS")
    @Basic
    var remarks: String? = null

    @Column(name="SUBMISSION_DATE")
    @Basic
    var submissionDate: Timestamp?=null

    @Transient
    @JsonProperty("KNW_COMMITTEE")
    var knwCommittee: String?=null

    @Transient
    @JsonProperty("DEPARTMENT_NAME")
    var departmentName: String?=null

    @Transient
    @Column(name = "ACCENT_TO")
    @Basic
    var accentTo: Boolean = false

    @Column(name = "TASKID")
    @Basic
    var taskId: String? = null


}
