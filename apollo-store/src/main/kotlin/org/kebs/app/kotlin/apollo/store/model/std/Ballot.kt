package org.kebs.app.kotlin.apollo.store.model.std

import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name = "BALLOT")
class Ballot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    var id: Long = 0

    @Column(name = "PR_ID")
    @Basic
    var prID: Long =0

    @Column(name = "BALLOT_NAME")
    @Basic
    var ballotName: String? = null

    @Column(name = "BALLOT_DRAFT_BY")
    @Basic
    var ballotDraftBy: String? = null


    @Column(name = "BALLOT_PATH")
    @Basic
    var ballotPath: String? = null


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

        other as Ballot

        if (id != other.id) return false
        if (prID != other.prID) return false
        if (ballotName != other.ballotName) return false
        if (ballotDraftBy != other.ballotDraftBy) return false
        if (ballotPath != other.ballotPath) return false
        if (createdOn != other.createdOn) return false
        if (modifiedOn != other.modifiedOn) return false
        if (deletedOn != other.deletedOn) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (prID.hashCode())
        result = 31 * result + (ballotName?.hashCode() ?: 0)
        result = 31 * result + (ballotDraftBy?.hashCode() ?: 0)
        result = 31 * result + (ballotPath?.hashCode() ?: 0)
        result = 31 * result + (createdOn?.hashCode() ?: 0)
        result = 31 * result + (modifiedOn?.hashCode() ?: 0)
        result = 31 * result + (deletedOn?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "Ballot(id=$id, prID=$prID,ballotName=$ballotName,ballotDraftBy=$ballotDraftBy,ballotPath=$ballotPath" +
                "createdOn=$createdOn, modifiedOn=$modifiedOn, deletedOn=$deletedOn)"
    }

}
