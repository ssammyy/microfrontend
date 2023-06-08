package org.kebs.app.kotlin.apollo.store.model.std

import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name = "SD_COMMENTS")
class Comments {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @Column(name = "USER_ID")
    @Basic
    var userId: Long = 0

    @Column(name = "PD_ID")
    @Basic
    var pdId: Long = 0

    @Column(name = "CD_ID")
    @Basic
    var cdId: Long = 0

    @Column(name = "RECIPIENT_ID")
    @Basic
    var recipientId: Long = 0

    @Column(name = "TITLE")
    @Basic
    var title: String? = null

    @Column(name = "DOCUMENT_TYPE")
    @Basic
    var documentType: String? = null

    @Column(name = "CIRCULATION_DATE")
    @Basic
    var circulationDate: Timestamp? = null

    @Column(name = "CLOSING_DATE")
    @Basic
    var closingDate: Timestamp? = null

    @Column(name = "ORGANIZATION")
    @Basic
    var organization: String? = null

    @Column(name = "CLAUSE")
    @Basic
    var clause: String? = null

    @Column(name = "PARAGRAPH")
    @Basic
    var paragraph: String? = null

    @Column(name = "COMMENT_TYPE")
    @Basic
    var commentType: String? = null

    @Column(name = "PROPOSED_CHANGE")
    @Basic
    var proposedChange: String? = null


    @Column(name = "OBSERVATION")
    @Basic
    var observation: String? = null

    @Column(name = "STATUS")
    @Basic
    var status: String? = null

    @Column(name = "CREATED_ON")
    @Basic
    var createdOn: Timestamp? = null

    @Column(name = "MODIFIED_ON")
    @Basic
    var modifiedOn: Timestamp? = null

    @Column(name = "DELETED_ON")
    @Basic
    var deletedOn: Timestamp? = null


    @Column(name = "CREATED_BY")
    @Basic
    var createdBy: String? = null

    @Column(name = "MODIFIED_BY")
    @Basic
    var modifiedBy: String? = null

    @Column(name = "DELETE_BY")
    @Basic
    var deleteBy: String? = null

    @Column(name = "COMMENTS_MADE")
    @Basic
    var commentsMade: String? = null

    @Column(name = "PRD_ID")
    @Basic
    var prdId: String? = null

    @Column(name = "VAR_FIELD_5")
    @Basic
    var varField5: String? = null

    @Column(name = "VAR_FIELD_6")
    @Basic
    var varField6: String? = null

    @Column(name = "VAR_FIELD_7")
    @Basic
    var varField7: String? = null

    @Column(name = "VAR_FIELD_8")
    @Basic
    var varField8: String? = null

    @Column(name = "VAR_FIELD_9")
    @Basic
    var varField9: String? = null

    @Column(name = "VAR_FIELD_10")
    @Basic
    var varField10: String? = null
}
