package org.kebs.app.kotlin.apollo.store.model.std

import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name = "SD_NWA_GAZETTE_NOTICE")
class NWAGazetteNotice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    var id: Long = 0

    @Column(name = "KS_NUMBER")
    @Basic
    var ksNumber: String? = null

    @Column(name = "DATE_UPLOADED")
    @Basic
    var dateUploaded: Timestamp?=null

    @Column(name = "DESCRIPTION")
    @Basic
    var description: String? = null

    @Column(name = "ASSIGNED_TO")
    @Basic
    var assignedTo: Long? = null

    @Column(name = "PROCESS_ID")
    @Basic
    var processId: String? = null


    @Transient
    @Column(name = "ACCENT_TO")
    @Basic
    val accentTo: Boolean = false
    @Column(name = "TASKID")
    @Basic
    var taskId: String? = null
}
