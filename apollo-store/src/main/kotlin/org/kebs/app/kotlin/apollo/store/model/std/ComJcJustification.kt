package org.kebs.app.kotlin.apollo.store.model.std

import java.sql.Timestamp
import javax.persistence.*
import kotlin.jvm.Transient

@Entity
@Table(name="SD_COM_JC_JUSTIFICATION")
class ComJcJustification {
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    var id:Long=0

    @Column(name="MEETING_DATE")
    @Basic
    var meetingDate : String?=null

    @Column(name="PROJECT_LEADER")
    @Basic
    var projectLeader : String?=null

    @Column(name="REASON")
    @Basic
    var reason : String?=null


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

    @Column(name="DATE_PREPARED")
    @Basic
    var datePrepared: Timestamp?=null

    @Transient
    @Column(name = "ACCENT_TO")
    @Basic
    var accentTo: Boolean = false
    @Column(name = "TASKID")
    @Basic
    var taskId: String? = null
}
