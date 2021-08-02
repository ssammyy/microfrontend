package org.kebs.app.kotlin.apollo.store.model.std

import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.Timestamp
import javax.persistence.*


@Entity
@Table(name = "SD_NWA_PRELIMINARY_DRAFT")
class NWAPreliminaryDraft {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    var id: Long = 0

    @Transient
    @JsonProperty("TASKID")
    var taskId: String? = null

    @Column(name = "TITLE")
    @Basic
    var title: String? = null

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

    @Column(name = "SPECIAL")
    @Basic
    var special: String? = null

    @Transient
    @Column(name = "ACCENT_TO")
    @Basic
    val accentTo: Boolean = false


}
