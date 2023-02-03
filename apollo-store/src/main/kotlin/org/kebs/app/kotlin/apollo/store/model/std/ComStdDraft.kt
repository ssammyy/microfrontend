package org.kebs.app.kotlin.apollo.store.model.std

import java.sql.Timestamp
import javax.persistence.*
import kotlin.jvm.Transient

@Entity
@Table(name="SD_COM_STD_DRAFT")
class ComStdDraft {
    @Column(name = "ID")
    @SequenceGenerator(
        name = "SD_COM_STD_DRAFT_SEQ_GEN",
        allocationSize = 1,
        sequenceName = "SD_COM_STD_DRAFT_SEQ"
    )
    @GeneratedValue(generator = "SD_COM_STD_DRAFT_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long? = 0
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
    @Column(name = "TASK_ID")
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

    @Column(name="DEPARTMENT")
    @Basic
    var departmentId: Long?=null

    @Column(name = "SUBJECT")
    @Basic
    var subject: String? = null

    @Column(name = "DESCRIPTION")
    @Basic
    var description: String? = null

    @Column(name="CONTACT_ONE_FULL_NAME")
    @Basic
    var contactOneFullName: String?=null

    @Column(name="CONTACT_ONE_TELEPHONE")
    @Basic
    var contactOneTelephone: String?=null

    @Column(name="CONTACT_ONE_EMAIL")
    @Basic
    var contactOneEmail: String?=null

    @Column(name="CONTACT_TWO_FULL_NAME")
    @Basic
    var contactTwoFullName: String?=null

    @Column(name="CONTACT_TWO_TELEPHONE")
    @Basic
    var contactTwoTelephone: String?=null

    @Column(name="CONTACT_TWO_EMAIL")
    @Basic
    var contactTwoEmail: String?=null

    @Column(name="CONTACT_THREE_FULL_NAME")
    @Basic
    var contactThreeFullName: String?=null

    @Column(name="CONTACT_THREE_TELEPHONE")
    @Basic
    var contactThreeTelephone: String?=null

    @Column(name="CONTACT_THREE_EMAIL")
    @Basic
    var contactThreeEmail: String?=null

    @Column(name="COMPANY_NAME")
    @Basic
    var companyName: String?=null

    @Column(name="COMPANY_PHONE")
    @Basic
    var companyPhone: String?=null

    @Column(name="COMMENT_COUNT")
    @Basic
    var commentCount : Long?=null

    @Column(name="PROPOSAL_ID")
    @Basic
    var proposalId : Long?=null

    @Column(name="STANDARD_TYPE")
    @Basic
    var standardType: String?=null


}
