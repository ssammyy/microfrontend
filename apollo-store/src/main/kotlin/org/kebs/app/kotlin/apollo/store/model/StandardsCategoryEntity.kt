package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "CFG_STANDARDS_CATEGORY")
class StandardsCategoryEntity : Serializable {
    @Column(name = "ID")
    @Id
    var id: Long? = 0

    @Column(name = "STANDARD_CATEGORY")
    @Basic
    var standardCategory: String? = null

    @Column(name = "STANDARD_NICKNAME")
    @Basic
    var standardNickname: String? = null

    @Column(name = "STANDARD_ID")
    @Basic
    var standardId: Long? = null

    @Column(name = "STATUS")
    @Basic
    var status: Int? = null

    @Column(name = "CREATED_BY")
    @Basic
    var createdBy: String? = null

    @Column(name = "MODIFIED_BY")
    @Basic
    var modifiedBy: String? = null

    @Column(name = "DELETED_BY")
    @Basic
    var deletedBy: String? = null

    @Column(name = "MODIFIED_ON")
    @Basic
    var modifiedOn: Timestamp? = null

    @Column(name = "DELETED_ON")
    @Basic
    var deletedOn: Timestamp? = null

    @Column(name = "CREATED_ON")
    @Basic
    var createdOn: Timestamp? = null

//    @JoinColumn(name = "STANDARD_ID", referencedColumnName = "ID")
//    @ManyToOne
//    var standardId: SampleStandardsEntity? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as StandardsCategoryEntity
        return id == that.id &&
                standardId == that.standardId &&
                standardCategory == that.standardCategory &&
                standardNickname == that.standardNickname &&
                status == that.status &&
                createdBy == that.createdBy &&
                modifiedBy == that.modifiedBy &&
                deletedBy == that.deletedBy &&
                modifiedOn == that.modifiedOn &&
                deletedOn == that.deletedOn &&
                createdOn == that.createdOn
    }

    override fun hashCode(): Int {
        return Objects.hash(id, standardCategory, standardNickname, standardId, status, createdBy, modifiedBy, deletedBy, modifiedOn, deletedOn, createdOn)
    }
}