package org.kebs.app.kotlin.apollo.store.model.std

import java.sql.Timestamp
import javax.persistence.*
import kotlin.jvm.Transient

@Entity
@Table(name="SD_ADOPTION_PROPOSAL_COMMENTS")
class ISAdoptionComments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    var id:Long=0

    @Column(name="USER_ID")
    @Basic
    var user_id:String? =null

    @Column(name="ADOPTION_PROPOSAL_COMMENT")
    @Basic
    var adoption_proposal_comment:String? =null

    @Column(name="COMMENT_TIME")
    @Basic
    var comment_time: Timestamp?=null

    @Transient
    var taskId:String?=null

    @Column(name="PROPOSAL_ID")
    @Basic
    var proposalID:Long? =null

    @Column(name="TITLE")
    @Basic
    var commentTitle:String? =null

    @Column(name="DOCUMENT_TYPE")
    @Basic
    var commentDocumentType:String? =null

    @Column(name="ORGANIZATION")
    @Basic
    var comNameOfOrganization:String? =null

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


}
