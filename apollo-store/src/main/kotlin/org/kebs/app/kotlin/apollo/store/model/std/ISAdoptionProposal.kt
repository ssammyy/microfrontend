package org.kebs.app.kotlin.apollo.store.model.std

import java.sql.Timestamp
import javax.persistence.*
import kotlin.jvm.Transient

@Entity
@Table(name = "SD_ADOPTION_PROPOSAL")
class ISAdoptionProposal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    val id: Long = 0

    @Column(name = "DOC_NAME")
    @Basic
    val proposal_doc_name: String? = null

    @Column(name="SUBMISSION_DATE")
    @Basic
    var submissionDate: Timestamp?=null

    @Column(name = "UPLOADED_BY")
    @Basic
    val uploadedBy: String? = null

    @Transient
    @Column(name = "ACCENT_TO")
    @Basic
    val accentTo: Boolean = false
    @Column(name = "TASKID")
    @Basic
    var taskId: String? = null

}
