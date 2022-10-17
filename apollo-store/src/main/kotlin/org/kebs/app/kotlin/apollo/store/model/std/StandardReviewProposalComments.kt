package org.kebs.app.kotlin.apollo.store.model.std

import java.sql.Timestamp
import javax.persistence.*


@Entity
@Table(name="SD_REVIEW_PROPOSAL_COMMENTS")
class StandardReviewProposalComments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    var id:Long=0

    @Column(name="USER_NAME")
    @Basic
    var userName:String? =null

    @Column(name="ADOPTION_COMMENT")
    @Basic
    var adoptionComment:String? =null

    @Column(name="COMMENT_TIME")
    @Basic
    var commentTime:Timestamp? =null

    @Column(name="PROPOSAL_ID")
    @Basic
    var proposalId:String? =null

    @Column(name="TITLE")
    @Basic
    var title:String? =null

    @Column(name="DOCUMENT_TYPE")
    @Basic
    var documentType:String? =null

    @Column(name="CLAUSE")
    @Basic
    var clause:String? =null

    @Column(name="PARAGRAPH")
    @Basic
    var paragraph:String? =null


    @Column(name="TYPE_OF_COMMENT")
    @Basic
    var typeOfComment:String? =null

    @Column(name="PROPOSED_CHANGE")
    @Basic
    var proposedChange:String? =null




}
