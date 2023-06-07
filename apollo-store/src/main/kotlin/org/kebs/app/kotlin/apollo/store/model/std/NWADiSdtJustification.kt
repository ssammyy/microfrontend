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
    var id: Long = 0

    @Column(name = "CDN")
    @Basic
    var cdn: Long? = 0

    @Column(name = "JST_NUMBER")
    @Basic
    var jstNumber: Long? = 0

    @Column(name = "COST")
    @Basic
    var cost: String? = null

    @Column(name = "NUMBER_OF_MEETINGS")
    @Basic
    var numberOfMeetings: String? = null

    @Column(name = "IDENTIFIED_NEED")
    @Basic
    var identifiedNeed: String? = null

    @Column(name = "DATE_APPROVAL_MADE")
    @Basic
    var dateApprovalMade: String? = null

    @Column(name = "DATE_OF_APPROVAL")
    @Basic
    var dateOfApproval: String? = null

    @Column(name="DATE_PREPARED")
    @Basic
    var datePrepared: Timestamp?=null

    @Column(name="CD_APP_NUMBER")
    @Basic
    var cdAppNumber: String? = null

    @Column(name = "REMARKS")
    @Basic
    var remarks: String? = null

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
