package org.kebs.app.kotlin.apollo.store.model.std

import java.io.Serializable
import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name = "SD_NWA_DISDT_JUSTIFICATION")
class NWADiSdtJustification : Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    val id: Long = 0

    @Column(name = "CDN")
    @Basic
    var cdn: Long? = 0

    @Column(name = "COST")
    @Basic
    val cost: String? = null

    @Column(name = "NUMBER_OF_MEETINGS")
    @Basic
    val numberOfMeetings: String? = null

    @Column(name = "IDENTIFIED_NEED")
    @Basic
    val identifiedNeed: String? = null

    @Column(name = "DATE_APPROVAL_MADE")
    @Basic
    val dateApprovalMade: String? = null

    @Column(name = "DATE_OF_APPROVAL")
    @Basic
    val dateOfApproval: String? = null

    @Column(name="DATE_PREPARED")
    @Basic
    var datePrepared: Timestamp?=null

    @Column(name="CD_APP_NUMBER")
    @Basic
    var cdAppNumber: String? = null

    @Transient
    @Column(name = "ACCENT_TO")
    @Basic
    var accentTo: Boolean = false
    @Column(name = "TASKID")
    @Basic
    var taskId: String? = null
}
