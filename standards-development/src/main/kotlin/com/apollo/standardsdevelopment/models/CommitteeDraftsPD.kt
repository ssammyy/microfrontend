package com.apollo.standardsdevelopment.models
import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name = "CommitteeDraftPD")
class CommitteeDraftsPD {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    var id: Long = 0

    @Column(name = "PD_DRAFT_NAME")
    @Basic
    var PdDraftName: String? = null


    @Column(name = "PD_DRAFT_BY")
    @Basic
    var PddraftBy: String? = null


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

        other as CommitteeDraftsPD

        if (id != other.id) return false
        if (PdDraftName != other.PdDraftName) return false
        if (PddraftBy != other.PddraftBy) return false
        if (createdOn != other.createdOn) return false
        if (modifiedOn != other.modifiedOn) return false
        if (deletedOn != other.deletedOn) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (PdDraftName?.hashCode() ?: 0)
        result = 31 * result + (PddraftBy?.hashCode() ?: 0)
        result = 31 * result + (createdOn?.hashCode() ?: 0)
        result = 31 * result + (modifiedOn?.hashCode() ?: 0)
        result = 31 * result + (deletedOn?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "CommitteeDraftsPD(id=$id, PdDraftName=$PdDraftName, Pd_draftBy=$PddraftBy" +
                "createdOn=$createdOn, modifiedOn=$modifiedOn, deletedOn=$deletedOn)"
    }

}