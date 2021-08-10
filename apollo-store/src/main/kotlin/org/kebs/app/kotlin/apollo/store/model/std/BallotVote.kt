package org.kebs.app.kotlin.apollo.store.model.std

import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name = "BALLOT_VOTE")
class BallotVote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    var id: Long = 0

    @Column(name = "BALLOT_ID")
    @Basic
    var ballotId: Long = 0

    @Column(name = "USER_ID")
    @Basic
    var userId: Long = 0

    @Column(name = "APPROVAL_STATUS")
    @Basic
    var approvalStatus: Long = 0


    @Column(name = "COMMENTS")
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

        other as BallotVote

        if (id != other.id) return false
        if (ballotId != other.ballotId) return false
        if (userId != other.userId) return false
        if (approvalStatus != other.approvalStatus) return false
        if (comment != other.comment) return false
        if (createdOn != other.createdOn) return false
        if (modifiedOn != other.modifiedOn) return false
        if (deletedOn != other.deletedOn) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (ballotId?.hashCode() ?: 0)
        result = 31 * result + (userId?.hashCode() ?: 0)
        result = 31 * result + (approvalStatus?.hashCode() ?: 0)
        result = 31 * result + (comment?.hashCode() ?: 0)
        result = 31 * result + (createdOn?.hashCode() ?: 0)
        result = 31 * result + (modifiedOn?.hashCode() ?: 0)
        result = 31 * result + (deletedOn?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "BallotVote(id=$id, ballotId=$ballotId,userId=$userId,approvalStatus=$approvalStatus,comment=$comment" +
                "createdOn=$createdOn, modifiedOn=$modifiedOn, deletedOn=$deletedOn)"
    }

}
