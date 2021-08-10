package org.kebs.app.kotlin.apollo.store.model.std

import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.Timestamp
import javax.persistence.*
import kotlin.jvm.Transient


@Entity
@Table(name = "SD_NWA_JUSTIFICATION")
class NWAJustification {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @Column(name = "MEETING_DATE")
    @Basic
    val meetingDate: String? = null

    @Column(name = "KNW")
    @Basic
    val knw: String? = null

    @Column(name = "KNW_SEC")
    @Basic
    val knwSecretary: String? = null

    @Column(name = "SL_NUMBER")
    @Basic
    val sl: String? = null

    @Column(name = "REQUEST_NUMBER")
    @Basic
    var requestNumber: String? = null

    @Column(name = "REQUESTED_BY")
    @Basic
    val requestedBy: String? = null

    @Column(name = "ISSUES_ADDRESSED")
    @Basic
    val issuesAddressed: String? = null

    @Column(name = "KNW_ACCEPTANCE_DATE")
    @Basic
    val knwAcceptanceDate: String? = null

    @Column(name = "REFERENCE_MATERIAL")
    @Basic
    val referenceMaterial: String? = null

    @Column(name = "DEPARTMENT")
    @Basic
    val department: String? = null

    @Column(name = "STATUS")
    @Basic
    val status: String? = null

    @Column(name = "REMARKS")
    @Basic
    val remarks: String? = null

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
    val accentTo: Boolean = false
    @Column(name = "TASKID")
    @Basic
    var taskId: String? = null


}
