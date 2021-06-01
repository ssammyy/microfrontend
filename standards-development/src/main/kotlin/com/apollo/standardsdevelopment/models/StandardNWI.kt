package org.kebs.app.kotlin.apollo.standardsdevelopment.models

import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name="SD_NWI")
class StandardNWI {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    var id:Long=0

    @Column(name="PROPOSAL_TITLE")
    @Basic
    var proposalTitle:String? =null


    @Column(name="NAME_OF_PROPOSER")
    @Basic
    var nameOfProposer:String? =null

    @Column(name="CREATED_ON")
    @Basic
    var createdOn:Timestamp? =null

    @Column(name="MODIFIED_ON")
    @Basic
    var modifiedOn:Timestamp? =null

    @Column(name="DELETED_ON")
    @Basic
    var deletedOn:Timestamp? =null
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StandardNWI

        if (id != other.id) return false
        if (proposalTitle != other.proposalTitle) return false
        if (nameOfProposer != other.nameOfProposer) return false
        if (createdOn != other.createdOn) return false
        if (modifiedOn != other.modifiedOn) return false
        if (deletedOn != other.deletedOn) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (proposalTitle?.hashCode() ?: 0)
        result = 31 * result + (nameOfProposer?.hashCode() ?: 0)
        result = 31 * result + (createdOn?.hashCode() ?: 0)
        result = 31 * result + (modifiedOn?.hashCode() ?: 0)
        result = 31 * result + (deletedOn?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "StandardNWI(id=$id, proposalTitle=$proposalTitle, nameOfProposer=$nameOfProposer, createdOn=$createdOn, modifiedOn=$modifiedOn, deletedOn=$deletedOn)"
    }


}
