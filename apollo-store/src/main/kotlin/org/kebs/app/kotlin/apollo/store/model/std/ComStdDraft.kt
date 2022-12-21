package org.kebs.app.kotlin.apollo.store.model.std

import java.sql.Timestamp
import javax.persistence.*
import kotlin.jvm.Transient

@Entity
@Table(name="SD_COM_STANDARD_DRAFT")
class ComStdDraft {
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    var id:Long=0
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

    @Column(name = "SPECIAL")
    @Basic
    var special: String? = null

    @Column(name="UPLOAD_DATE")
    @Basic
    var uploadDate: Timestamp?=null

    @Column(name="DEADLINE_DATE")
    @Basic
    var deadlineDate: Timestamp?=null

    @Column(name = "DRAFT_NUMBER")
    @Basic
    var draftNumber: String? = null

    @Column(name="UPLOADED_BY")
    @Basic
    var uploadedBy : Long?=null

    @Column(name="CREATED_BY")
    @Basic
    var createdBy : String?=null

    @Column(name = "REMARKS")
    @Basic
    var remarks: String? = null

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

    @Column(name="ASSIGNED_TO")
    @Basic
    var assignedTo:Long?=null

    @Column(name="REQUEST_NUMBER")
    @Basic
    var requestNumber:String?=null

    @Column(name="REQUEST_ID")
    @Basic
    var requestId:Long?=null


    @Column(name="STATUS")
    @Basic
    var status:Long?=null

    @Column(name="COM_STANDARD_NUMBER")
    @Basic
    var comStdNumber:String?=null
}
