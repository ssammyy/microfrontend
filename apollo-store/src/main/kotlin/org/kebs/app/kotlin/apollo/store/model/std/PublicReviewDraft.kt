package org.kebs.app.kotlin.apollo.store.model.std

import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name = "PUBLIC_REVIEW_DRAFT")
class PublicReviewDraft {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    var id: Long = 0

    @Column(name = "PRD_NAME")
    @Basic
    var prdName: String? = null


    @Column(name = "PRD_DRAFT_BY")
    @Basic
    var prdraftBy: String? = null


    @Column(name = "PRD_PATH")
    @Basic
    var prdpath: String? = null


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
        if (prdName != other.prdName) return false
        if (prdraftBy != other.prdraftBy) return false
        if (prdpath != other.prdpath) return false
        if (createdOn != other.createdOn) return false
        if (modifiedOn != other.modifiedOn) return false
        if (deletedOn != other.deletedOn) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (prdName?.hashCode() ?: 0)
        result = 31 * result + (prdraftBy?.hashCode() ?: 0)
        result = 31 * result + (prdpath?.hashCode() ?: 0)

        result = 31 * result + (createdOn?.hashCode() ?: 0)
        result = 31 * result + (modifiedOn?.hashCode() ?: 0)
        result = 31 * result + (deletedOn?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "PublicReviewDraft(id=$id, prdName=$prdName, prdraftBy=$prdraftBy" +
                "prdpath=$prdpath,createdOn=$createdOn, modifiedOn=$modifiedOn, deletedOn=$deletedOn)"
    }

}
