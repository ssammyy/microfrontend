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
    var id: Long = 0

    @Column(name = "DOC_NAME")
    @Basic
    var proposal_doc_name: String? = null

    @Column(name="DATE_PREPARED")
    @Basic
    var preparedDate: Timestamp?=null

    @Column(name = "PROPOSAL_NUMBER")
    @Basic
    var proposalNumber: String? = null

    @Column(name = "UPLOADED_BY")
    @Basic
    var uploadedBy: String? = null

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

}
