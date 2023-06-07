package org.kebs.app.kotlin.apollo.store.model.std

import java.sql.Timestamp
import javax.persistence.*
import kotlin.jvm.Transient

@Entity
@Table(name="SD_COM_DRAFT_COMMENTS")
class ComDraftComments {
    @Column(name = "ID")
    @SequenceGenerator(
        name = "SD_COM_DRAFT_COMMENTS_SEQ_GEN",
        allocationSize = 1,
        sequenceName = "SD_COM_DRAFT_COMMENTS_SEQ"
    )
    @GeneratedValue(generator = "SD_COM_DRAFT_COMMENTS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long? = 0

    @Column(name="NAME")
    @Basic
    var name:String? =null

    @Column(name="DRAFT_COMMENT")
    @Basic
    var draftComment:String? =null

    @Column(name="COMMENT_TIME")
    @Basic
    var commentTime: Timestamp?=null

    @Column(name="REQUEST_ID")
    @Basic
    var requestID:Long? =null

    @Column(name="DRAFT_ID")
    @Basic
    var draftID:Long? =null


    @Column(name="TITLE")
    @Basic
    var commentTitle:String? =null


    @Column(name="DOCUMENT_TYPE")
    @Basic
    var commentDocumentType:String? =null

    @Column(name="ORGANIZATION")
    @Basic
    var nameOfOrganization:String? =null

    @Column(name="CLAUSE")
    @Basic
    var comClause:String? =null

    @Column(name="PARAGRAPH")
    @Basic
    var comParagraph:String? =null

    @Column(name="TYPE_OF_COMMENT")
    @Basic
    var typeOfComment:String? =null

    @Column(name="PROPOSED_CHANGE")
    @Basic
    var proposedChange:String? =null

    @Column(name="ADOPT_STANDARD")
    @Basic
    var adoptStandard:String? =null

    @Column(name="ADOPT_DRAFT")
    @Basic
    var adoptDraft:String? =null

    @Column(name="REASON")
    @Basic
    var reason:String? =null

    @Column(name="RECOMMENDATIONS")
    @Basic
    var recommendations:String? =null

    @Column(name="NAME_OF_RESPONDENT")
    @Basic
    var nameOfRespondent:String? =null

    @Column(name="POSITION_OF_RESPONDENT")
    @Basic
    var positionOfRespondent:String? =null

    @Column(name="UPLOAD_DATE")
    @Basic
    var uploadDate:Timestamp? =null

    @Column(name="EMAIL_OF_RESPONDENT")
    @Basic
    var emailOfRespondent:String? =null

    @Column(name="PHONE_OF_RESPONDENT")
    @Basic
    var phoneOfRespondent:String? =null

    @Column(name="OBSERVATION")
    @Basic
    var observation:String? =null

    @Column(name="SCOPE")
    @Basic
    var scope:String? =null




}
