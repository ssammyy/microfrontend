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

    @Column(name = "ADOPTION_LINK")
    @Basic
    var adoptionProposalLink: String? = null

    @Column(name = "CIRCULATION_DATE")
    @Basic
    var circulationDate: Timestamp? = null

    @Column(name = "CLOSING_DATE")
    @Basic
    var closingDate: Timestamp? = null

    @Column(name = "TC_SEC_NAME")
    @Basic
    var tcSecName: String? = null

    @Column(name = "TC_SEC_EMAIL")
    @Basic
    var tcSecEmail: String? = null

    @Column(name = "STAKEHOLDER_LIST")
    @Basic
    var stakeholdersList: String? = null

    @Column(name = "OTHER_STAKEHOLDER_LIST")
    @Basic
    var addStakeholdersList: String? = null


    @Column(name="DATE_PREPARED")
    @Basic
    var preparedDate: Timestamp?=null

    @Column(name = "PROPOSAL_NUMBER")
    @Basic
    var proposalNumber: String? = null

    @Column(name = "TITLE")
    @Basic
    var title: String? = null

    @Column(name = "SCOPE")
    @Basic
    var scope: String? = null

    @Column(name = "ADOPTION_ACCEPTABLE_AS_PRESENTED")
    @Basic
    var adoptionAcceptableAsPresented: String? = null


    @Column(name = "REASONS_FOR_NOT_ACCEPTANCE")
    @Basic
    var reasonsForNotAcceptance: String? = null

    @Column(name = "RECOMMENDATIONS")
    @Basic
    var recommendations: String? = null

    @Column(name = "NAME_OF_RESPONDENT")
    @Basic
    var nameOfRespondent: String? = null

    @Column(name = "POSITION_OF_RESPONDENT")
    @Basic
    var positionOfRespondent: String? = null

    @Column(name = "NAME_OF_ORGANIZATION")
    @Basic
    var nameOfOrganization: String? = null

    @Column(name = "DATE_OF_APPLICATION")
    @Basic
    var dateOfApplication: String? = null

    @Column(name = "UPLOADED_BY")
    @Basic
    var uploadedBy: String? = null

    @Column(name = "REMARKS")
    @Basic
    var remarks: String? = null

    @Column(name = "REQUEST_ID")
    @Basic
    var requestId: Long? = null

    @Transient
    @Column(name = "ACCENT_TO")
    @Basic
    var accentTo: String? = null
    @Column(name = "TASKID")
    @Basic
    var taskId: String? = null

    @Column(name = "ASSIGNED_TO")
    @Basic
    var assignedTo: Long? = null

    @Column(name = "STATUS")
    @Basic
    var status: Long? = null

    @Column(name = "TC_SEC_ASSIGNED")
    @Basic
    var tcSecAssigned: String? = null

    @Column(name = "STANDARD_NUMBER")
    @Basic
    var iStandardNumber: String? = null

    @Column(name="DEADLINE_DATE")
    @Basic
    var deadlineDate: Timestamp?=null

    @Column(name = "NUMBER_OF_COMMENTS")
    @Basic
    var noOfComments: Long? = null

    @Column(name = "ADOPT")
    @Basic
    var adopt: Long? = null

    @Column(name = "NOT_ADOPT")
    @Basic
    var notAdopt: Long? = null

}
