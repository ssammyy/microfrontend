package org.kebs.app.kotlin.apollo.store.model.std

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name = "SD_NWA_WORKSHOP_DRAFT")
class NWAWorkShopDraft : Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    var id: Long = 0


    @Column(name = "TITLE")
    @Basic
    var title: String? = null

    @Column(name = "REMARKS")
    @Basic
    var remarks: String? = null

    @Column(name = "SCOPE")
    @Basic
    var scope: String? = null

    @Column(name = "NORMATIVE_REFERENCE")
    @Basic
    var normativeReference: String? = null

    @Column(name = "SYMBOLS_ABBREVIATED_TERMS")
    @Basic
    var symbolsAbbreviatedTerms: String? = null

    @Column(name = "CLAUSE")
    @Basic
    var clause: String? = null

    @Column(name = "SPECIAL")
    @Basic
    var special: String? = null
    @Column(name="DATE_WD_PREPARED")
    @Basic
    var dateWdPrepared: Timestamp?=null

    @Column(name = "KNWA_NUMBER")
    @Basic
    var ksNumber: String? = null

    @Column(name = "ASSIGNED_TO")
    @Basic
    var assignedTo: Long? = null

    @Column(name = "PROCESS_ID")
    @Basic
    var processId: String? = null

    @Transient
    @Column(name = "ACCENT_TO")
    @Basic
    var accentTo: Boolean = false
    @Column(name = "TASKID")
    @Basic
    var taskId: String? = null


}
