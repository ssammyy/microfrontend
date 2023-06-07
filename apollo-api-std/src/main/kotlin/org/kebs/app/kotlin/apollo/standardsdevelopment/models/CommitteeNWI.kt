package org.kebs.app.kotlin.apollo.standardsdevelopment.models

import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name = "CommitteeNWI")
class CommitteeNWI {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    var id: Long = 0

    @Column(name = "SL_NO")
    @Basic
    var slNo: String? = null


    @Column(name = "REFERENCE")
    @Basic
    var reference: String? = null

    @Column(name = "TA")
    @Basic
    var ta: String? = null

    @Column(name = "ED")
    @Basic
    var ed: String? = null

    @Column(name = "TITLE")
    @Basic
    var title: String? = null

    @Column(name = "STAGE_DATE")
    @Basic
    var stage_date: String? = null

    @Column(name = "APPROVED")
    @Basic
    var approved: Long = 0

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

        other as CommitteeNWI

        if (id != other.id) return false
        if (slNo != other.slNo) return false
        if (reference != other.reference) return false
        if (ta != other.ta) return false
        if (ed != other.ed) return false
        if (title != other.title) return false
        if (stage_date != other.stage_date) return false
        if (approved != other.approved) return false

        if (createdOn != other.createdOn) return false
        if (modifiedOn != other.modifiedOn) return false
        if (deletedOn != other.deletedOn) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (slNo?.hashCode() ?: 0)
        result = 31 * result + (reference?.hashCode() ?: 0)
        result = 31 * result + (ta?.hashCode() ?: 0)
        result = 31 * result + (ed?.hashCode() ?: 0)
        result = 31 * result + (title?.hashCode() ?: 0)
        result = 31 * result + (stage_date?.hashCode() ?: 0)
        result = 31 * result + approved.hashCode()

        result = 31 * result + (createdOn?.hashCode() ?: 0)
        result = 31 * result + (modifiedOn?.hashCode() ?: 0)
        result = 31 * result + (deletedOn?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "CommitteeNWI(id=$id, slNo=$slNo, reference=$reference, ta=$ta,ed=$ed,title = $title,stage_date = $stage_date" +
                "approved=$approved,createdOn=$createdOn, modifiedOn=$modifiedOn, deletedOn=$deletedOn)"
    }

}
