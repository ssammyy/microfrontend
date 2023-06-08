package org.kebs.app.kotlin.apollo.store.model.std

import java.sql.Timestamp
import javax.persistence.*
import kotlin.jvm.Transient

@Entity
@Table(name="SD_REVIEW_COMMENTS")
class SDReviewComments {
    @Column(name = "ID")
    @SequenceGenerator(
        name = "SD_REVIEW_COMMENTS_SEQ_GEN",
        allocationSize = 1,
        sequenceName = "SD_REVIEW_COMMENTS_SEQ"
    )
    @GeneratedValue(generator = "SD_REVIEW_COMMENTS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long? = 0

    @Column(name = "REVIEW_ID")
    @Basic
    var reviewId: Long? = null
    @Column(name = "STANDARD_ID")
    @Basic
    var standardId: Long? = null

    @Column(name = "TITLE")
    @Basic
    var title: String? = null

    @Column(name = "STANDARD_NUMBER")
    @Basic
    var standardNumber: String? = null

    @Column(name = "DOCUMENT_TYPE")
    @Basic
    var documentType: String? = null

    @Column(name = "DATE_FORMED")
    @Basic
    var dateFormed: Timestamp? = null

    @Column(name = "CIRCULATION_DATE")
    @Basic
    var circulationDate: Timestamp? = null

    @Column(name = "CLOSING_DATE")
    @Basic
    var closingDate: Timestamp? = null

    @Column(name="NAME_OF_TC_SEC")
    @Basic
    var nameOfTcSecretary: String?=null

    @Column(name="JUSTIFICATION")
    @Basic
    var justification: String?=null

    @Column(name = "EDITION")
    @Basic
    var edition: String? = null

    @Column(name="NAME_OF_RESPONDENT")
    @Basic
    var nameOfRespondent : String?=null

    @Column(name="POSITION_OF_RESPONDENT")
    @Basic
    var positionOfRespondent : String?=null

    @Column(name = "NAME_OF_ORGANIZATION")
    @Basic
    var nameOfOrganization: String? = null

    @Column(name = "DATE_OF_COMMENT")
    @Basic
    var commentTime: Timestamp? = null




}
