package org.kebs.app.kotlin.apollo.store.model.std

import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name = "PUBLIC_REVIEW_DRAFT_COMMENTS")
class PublicReviewDraftComments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    var id: Long = 0

    @Column(name = "PRD_ID")
    @Basic
    var prdId: Long = 0


    @Column(name = "ROLE_ID")
    @Basic
    var roleId: Long = 0


    @Column(name = "ROLE_NAME")
    @Basic
    var roleName: String? = null

    @Column(name = "USER_ID")
    @Basic
    var userId: Long = 0

    @Column(name = "TITLE")
    @Basic
    var title: String? = null

    @Column(name = "DOCUMENTTYPE")
    @Basic
    var documentType: String? = null

    @Column(name = "CIRCULATIONDATE")
    @Basic
    var circulationDate: String? = null

    @Column(name = "CLOSINGDATE")
    @Basic
    var closingDate: String? = null

    @Column(name = "ORGANIZATION")
    @Basic
    var organization: String? = null

    @Column(name = "CLAUSE")
    @Basic
    var clause: String? = null

    @Column(name = "COMMENT_TYPE")
    @Basic
    var commentType: String? = null

    @Column(name = "COMMENTMADE")
    @Basic
    var comment: String? = null

    @Column(name = "PROPOSED_CHANGE")
    @Basic
    var proposedChange: String? = null

    @Column(name = "OBSERVATIONS")
    @Basic
    var observations: String? = null

    @Column(name = "CREATED_ON")
    @Basic
    var createdOn: Timestamp? = null

    @Column(name = "MODIFIED_ON")
    @Basic
    var modifiedOn: Timestamp? = null

    @Column(name = "DELETED_ON")
    @Basic
    var deletedOn: Timestamp? = null


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PublicReviewDraftComments

        if (id != other.id) return false
        if (prdId != other.prdId) return false
        if (roleId != other.roleId) return false
        if (roleName != other.roleName) return false
        if (userId != other.userId) return false
        if (title != other.title) return false
        if (documentType != other.documentType) return false
        if (circulationDate != other.circulationDate) return false
        if (closingDate != other.closingDate) return false
        if (organization != other.organization) return false
        if (clause != other.clause) return false
        if (commentType != other.commentType) return false
        if (comment != other.comment) return false
        if (proposedChange != other.proposedChange) return false
        if (observations != other.observations) return false
        if (createdOn != other.createdOn) return false
        if (modifiedOn != other.modifiedOn) return false
        if (deletedOn != other.deletedOn) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + prdId.hashCode()
        result = 31 * result + roleId.hashCode()
        result = 31 * result + (roleName?.hashCode() ?: 0)
        result = 31 * result + userId.hashCode()
        result = 31 * result + (documentType?.hashCode() ?: 0)
        result = 31 * result + (circulationDate?.hashCode() ?: 0)
        result = 31 * result + (closingDate?.hashCode() ?: 0)
        result = 31 * result + (organization?.hashCode() ?: 0)
        result = 31 * result + (clause?.hashCode() ?: 0)
        result = 31 * result + (commentType?.hashCode() ?: 0)
        result = 31 * result + (comment?.hashCode() ?: 0)
        result = 31 * result + (proposedChange?.hashCode() ?: 0)
        result = 31 * result + (observations?.hashCode() ?: 0)
        result = 31 * result + (createdOn?.hashCode() ?: 0)
        result = 31 * result + (modifiedOn?.hashCode() ?: 0)
        result = 31 * result + (deletedOn?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "PublicReviewDraftComments(id=$id, prdId=$prdId,roleId=$roleId,roleName=$roleName, userId=$userId," +
                "documentType=$documentType,circulationDate=$circulationDate,closingDate=$closingDate,organization=$organization," +
                "clause=$clause,commentType=$commentType,comment=$comment,proposedChange=$proposedChange,observations=$observations" +
                "createdOn=$createdOn, modifiedOn=$modifiedOn, deletedOn=$deletedOn)"
    }

}
