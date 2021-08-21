package org.kebs.app.kotlin.apollo.store.model.std

import javax.persistence.*
import kotlin.jvm.Transient

@Entity
@Table(name="SD_ADOPTION_PROPOSAL_JUSTIFICATION")
class ISAdoptionJustification {

    @Id
    @Column(name="ID")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    val id:Long=0

    @Column(name="MEETING_DATE")
    @Basic
    val meetingDate : String?=null

    @Column(name="TC")
    @Basic
    val tc_id : String?=null

    @Column(name="TC_SEC")
    @Basic
    val tcSec_id : String?=null

    @Column(name="SL_NUMBER")
    @Basic
    val slNumber : String?=null

    @Column(name="REQUEST_NUMBER")
    @Basic
    val requestNumber : String?=null

    @Column(name="REQUESTED_BY")
    @Basic
    val requestedBy : String?=null

    @Column(name="ISSUES_ADDRESSED")
    @Basic
    val issuesAddressed : String?=null

    @Column(name="TC_ACCEPTANCE_DATE")
    @Basic
    val tcAcceptanceDate : String?=null

    @Column(name="REFERENCE_MATERIAL")
    @Basic
    val referenceMaterial : String?=null

    @Column(name="DEPARTMENT")
    @Basic
    val department : String?=null

    @Column(name="STATUS")
    @Basic
    val status : String?=null

    @Column(name="REMARKS")
    @Basic
    val remarks : String?=null


    @Transient
    @Column(name = "ACCENT_TO")
    @Basic
    val accentTo: Boolean = false
    @Column(name = "TASKID")
    @Basic
    var taskId: String? = null
}
