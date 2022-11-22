package org.kebs.app.kotlin.apollo.store.model.std

import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name="SD_IS_STANDARD_TB")
class ISUploadStandard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    var id:Long=0

    @Column(name="TITLE")
    @Basic
    var title:String? =null

    @Column(name="SCOPE")
    @Basic
    var scope:String? =null

    @Column(name="NORMATIVE_REFERENCE")
    @Basic
    var normativeReference:String? =null

    @Column(name="SYMBOLS_ABBREVIATED_TERMS")
    @Basic
    var symbolsAbbreviatedTerms:String? =null

    @Column(name="CLAUSE")
    @Basic
    var clause:String? =null

    @Column(name="SPECIAL")
    @Basic
    var special:String? =null

    @Column(name="INTERNATIONAL_STANDARD_NUMBER")
    @Basic
    var iSNumber:String? =null

    @Column(name="UPLOAD_DATE")
    @Basic
    var uploadDate: Timestamp?=null

    @Transient
    @Column(name = "ACCENT_TO")
    @Basic
    var accentTo: String? = null

    @Column(name = "TASK_ID")
    @Basic
    var taskId: String? = null

    @Column(name = "ASSIGNED_TO")
    @Basic
    var assignedTo: Long? = null

    @Column(name = "PROCESS_ID")
    @Basic
    var processId: String? = null

    @Column(name = "JUSTIFICATION_NUMBER")
    @Basic
    var justificationNo: Long? = null

    @Column(name = "PROPOSAL_ID")
    @Basic
    var proposalId: Long? = null

    @Column(name = "STATUS")
    @Basic
    var status: Long? = null
}
