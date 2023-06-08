package org.kebs.app.kotlin.apollo.store.model.std

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import java.sql.Timestamp
import javax.persistence.*


@Entity
@Table(name = "SD_NWA_PRELIMINARY_DRAFT")
class NWAPreliminaryDraft : Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    var id: Long = 0


    @Column(name = "TITLE")
    @Basic
    var title: String? = null

    @Column(name = "DIJST_NUMBER")
    @Basic
    var diJNumber: Long? = 0

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

    @Column(name="DATE_PD_PREPARED")
    @Basic
    var datePdPrepared: Timestamp?=null

    @Column(name="WORK_SHOP_DATE")
    @Basic
    var workShopDate: Timestamp?=null

    @Column(name = "SPECIAL")
    @Basic
    var special: String? = null

    @Column(name="CD_APP_NUMBER")
    @Basic
    var cdAppNumber: String? = null

    @Column(name = "ASSIGNED_TO")
    @Basic
    var assignedTo: Long? = null

    @Column(name = "STATUS")
    @Basic
    var status: Long? = null

    @Column(name = "PROCESS_ID")
    @Basic
    var processId: String? = null

    @Column(name = "PREPARED_BY")
    @Basic
    var preparedBy: String? = null

    @Column(name = "PREPARED_BY_ID")
    @Basic
    var preparedById: Long? = null

    @Column(name = "JUSTIFICATION_NUMBER")
    @Basic
    var justificationNumber: Long? = null

    @Transient
    @Column(name = "ACCENT_TO")
    @Basic
    var accentTo: String? = null

    @Column(name = "TASKID")
    @Basic
    var taskId: String? = null

    @Column(name = "REQUEST_ID")
    @Basic
    var requestId: Long? = null

    @Column(name = "STANDARD_TYPE")
    @Basic
    var standardType: String? = null




}
