package com.apollo.standardsdevelopment.models

import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name = "PublicReviewDraft")
class PublicReviewDraft {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    var id: Long = 0

    @Column(name = "PRD_NAME")
    @Basic
    var PrdName: String? = null


    @Column(name = "PRD_DRAFT_BY")
    @Basic
    var PrdraftBy: String? = null


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

        other as PublicReviewDraft

        if (id != other.id) return false
        if (PrdName != other.PrdName) return false
        if (PrdraftBy != other.PrdraftBy) return false
        if (createdOn != other.createdOn) return false
        if (modifiedOn != other.modifiedOn) return false
        if (deletedOn != other.deletedOn) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (PrdName?.hashCode() ?: 0)
        result = 31 * result + (PrdraftBy?.hashCode() ?: 0)
        result = 31 * result + (createdOn?.hashCode() ?: 0)
        result = 31 * result + (modifiedOn?.hashCode() ?: 0)
        result = 31 * result + (deletedOn?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "PublicReviewDraft(id=$id, PrdName=$PrdName, PrdraftBy=$PrdraftBy" +
                "createdOn=$createdOn, modifiedOn=$modifiedOn, deletedOn=$deletedOn)"
    }

}