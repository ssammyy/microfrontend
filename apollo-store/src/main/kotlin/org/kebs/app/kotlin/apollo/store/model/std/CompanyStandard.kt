package org.kebs.app.kotlin.apollo.store.model.std

import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name="SD_COM_STANDARD")
class CompanyStandard {
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

    @Column(name="COMPANY_STANDARD_NUMBER")
    @Basic
    var comStdNumber:String? =null

    @Transient
    @Column(name = "ACCENT_TO")
    @Basic
    var accentTo: String? = null

    @Column(name = "TASKID")
    @Basic
    var taskId: String? = null

    @Column(name = "PROCESS_ID")
    @Basic
    var processId: String? = null

    @Column(name = "REQUEST_NUMBER")
    @Basic
    var requestNumber: String? = null

    @Column(name = "STATUS")
    @Basic
    var status: Long? = null

    @Column(name = "UPLOAD_DATE")
    @Basic
    var uploadDate: Timestamp? = null

    @Column(name = "PREPARED_BY")
    @Basic
    var preparedBy: String? = null

    @Column(name = "DOCUMENT_TYPE")
    @Basic
    var documentType: String? = null

    @Column(name = "DRAFT_ID")
    @Basic
    var draftId: Long? = null

    @Column(name = "DRAUGHTING")
    @Basic
    var draughting: String? = null

    @Column(name = "REQUEST_ID")
    @Basic
    var requestId: Long? = null
}
