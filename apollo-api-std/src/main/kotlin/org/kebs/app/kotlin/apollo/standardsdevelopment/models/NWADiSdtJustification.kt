package org.kebs.app.kotlin.apollo.standardsdevelopment.models

import javax.persistence.*

@Entity
@Table(name="SD_NWA_DISDT_JUSTIFICATION")
class NWADiSdtJustification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    val id:Long=0


    @Column(name="COST")
    @Basic
    val cost:String? =null

    @Column(name="NUMBER_OF_MEETINGS")
    @Basic
    val numberOfMeetings:String? =null

    @Column(name="IDENTIFIED_NEED")
    @Basic
    val identifiedNeed:String? =null

    @Column(name="DATE_APPROVAL_MADE")
    @Basic
    val dateApprovalMade:String? =null


    @Transient
    val approved : Boolean = false
    val taskId:String?=null
}
