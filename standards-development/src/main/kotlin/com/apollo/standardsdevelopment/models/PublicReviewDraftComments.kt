package com.apollo.standardsdevelopment.models

import java.sql.Timestamp
import javax.persistence.*
import javax.xml.stream.events.Comment

@Entity
@Table(name = "PublicReviewDraftComments")
class PublicReviewDraftComments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    var id: Long = 0

    @Column(name = "PRD_ID")
    @Basic
    var prdId: Long? = 0


    @Column(name = "ROLE_ID")
    @Basic
    var roleId: Long? = 0


    @Column(name = "ROLE_NAME")
    @Basic
    var roleName: String? = null

    @Column(name = "USER_ID")
    @Basic
    var userId: Long? = 0

    @Column(name = "COMMENT")
    @Basic
    var comment: String? = null

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
        if (comment != other.comment) return false
        if (createdOn != other.createdOn) return false
        if (modifiedOn != other.modifiedOn) return false
        if (deletedOn != other.deletedOn) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (prdId?.hashCode() ?: 0)
        result = 31 * result + (roleId?.hashCode() ?: 0)
        result = 31 * result + (roleName?.hashCode() ?: 0)
        result = 31 * result + (userId?.hashCode() ?: 0)
        result = 31 * result + (comment?.hashCode() ?: 0)
        result = 31 * result + (createdOn?.hashCode() ?: 0)
        result = 31 * result + (modifiedOn?.hashCode() ?: 0)
        result = 31 * result + (deletedOn?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "PublicReviewDraftComments(id=$id, prdId=$prdId,roleId=$roleId,roleName=$roleName, userId=$userId,comment=$comment" +
                "createdOn=$createdOn, modifiedOn=$modifiedOn, deletedOn=$deletedOn)"
    }

}