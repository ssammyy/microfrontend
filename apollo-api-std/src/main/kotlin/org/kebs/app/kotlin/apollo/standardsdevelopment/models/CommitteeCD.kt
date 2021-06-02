package org.kebs.app.kotlin.apollo.standardsdevelopment.models

import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name = "CommitteeCD")
class CommitteeCD {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    var id: Long = 0

    @Column(name = "NWI_ID")
    @Basic
    var nwiID: String? = null

    @Column(name = "PD_ID")
    @Basic
    var pdID: String? = null

    @Column(name = "CD_NAME")
    @Basic
    var cdName: String? = null


    @Column(name = "CD_BY")
    @Basic
    var cdBy: String? = null

    @Column(name = "APPROVED")
    @Basic
    var approved: String? = null


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

        other as CommitteeCD

        if (id != other.id) return false
        if (nwiID != other.nwiID) return false
        if (pdID != other.pdID) return false

        if (cdName != other.cdName) return false
        if (cdBy != other.cdBy) return false
        if (approved != other.approved) return false
        if (createdOn != other.createdOn) return false
        if (modifiedOn != other.modifiedOn) return false
        if (deletedOn != other.deletedOn) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (nwiID?.hashCode() ?: 0)
        result = 31 * result + (pdID?.hashCode() ?: 0)

        result = 31 * result + (cdName?.hashCode() ?: 0)
        result = 31 * result + (cdBy?.hashCode() ?: 0)
        result = 31 * result + (approved?.hashCode() ?: 0)

        result = 31 * result + (createdOn?.hashCode() ?: 0)
        result = 31 * result + (modifiedOn?.hashCode() ?: 0)
        result = 31 * result + (deletedOn?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "CommitteeCD(id=$id, nwiID=$nwiID,pdID=$pdID,cdName=$cdName, cdBy=$cdBy, approved=$approved" +
                "createdOn=$createdOn, modifiedOn=$modifiedOn, deletedOn=$deletedOn)"
    }

}
