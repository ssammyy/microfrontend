package org.kebs.app.kotlin.apollo.store.model.std

import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.Timestamp
import javax.persistence.*
import kotlin.jvm.Transient

@Entity
@Table(name="SD_ADOPTION_PROPOSAL_JUSTIFICATION")
class ISAdoptionJustification {

    @Id
    @Column(name="ID")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    var id:Long=0

    @Column(name="MEETING_DATE")
    @Basic
    var meetingDate : String?=null

    @Column(name="TC")
    @Basic
    var tc_id : String?=null

    @Column(name="TC_SEC")
    @Basic
    var tcSec_id : String?=null

    @Column(name="SL_NUMBER")
    @Basic
    var slNumber : String?=null

    @Column(name="REQUEST_NUMBER")
    @Basic
    var requestNumber : String?=null

    @Column(name="REQUESTED_BY")
    @Basic
    var requestedBy : String?=null

    @Column(name="ISSUES_ADDRESSED")
    @Basic
    var issuesAddressed : String?=null

    @Column(name="TC_ACCEPTANCE_DATE")
    @Basic
    var tcAcceptanceDate : String?=null

    @Column(name="REFERENCE_MATERIAL")
    @Basic
    var referenceMaterial : String?=null

    @Column(name="DEPARTMENT")
    @Basic
    var department : String?=null

    @Column(name="STATUS")
    @Basic
    var status : String?=null

    @Column(name="REMARKS")
    @Basic
    var remarks : String?=null

    @Column(name="SUBMISSION_DATE")
    @Basic
    var submissionDate: Timestamp?=null

    @Column(name="TC_COMMITTEE")
    @Basic
    var tcCommittee : String?=null

    @Column(name="DEPARTMENT_NAME")
    @Basic
    var departmentName : String?=null

    @Transient
    @Column(name = "ACCENT_TO")
    @Basic
    var accentTo: Boolean = false
    @Column(name = "TASKID")
    @Basic
    var taskId: String? = null

    @Column(name = "ASSIGNED_TO")
    @Basic
    var assignedTo: Long? = null

    @Column(name = "PROCESS_ID")
    @Basic
    var processId: String? = null
}
