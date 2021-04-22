package com.apollo.standardsdevelopment.models

import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name="SD_JUSTIFICATION")
class StandardJustification {
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    val id:Long=0

    @Column(name="TITLE")
    @Basic
    val title:String?=null

    @Column(name="SCOPE")
    @Basic
    val scope:String?=null

    @Column(name="PURPOSE")
    @Basic
    val purpose:String?=null

    @Column(name="CREATED_ON")
    @Basic
    var createdOn: Timestamp? =null

    @Column(name="MODIFIED_ON")
    @Basic
    var modifiedOn: Timestamp? =null

    @Column(name="DELETED_ON")
    @Basic
    var deletedOn: Timestamp? =null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StandardJustification

        if (id != other.id) return false
        if (title != other.title) return false
        if (scope != other.scope) return false
        if (purpose != other.purpose) return false
        if (createdOn != other.createdOn) return false
        if (modifiedOn != other.modifiedOn) return false
        if (deletedOn != other.deletedOn) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (title?.hashCode() ?: 0)
        result = 31 * result + (scope?.hashCode() ?: 0)
        result = 31 * result + (purpose?.hashCode() ?: 0)
        result = 31 * result + (createdOn?.hashCode() ?: 0)
        result = 31 * result + (modifiedOn?.hashCode() ?: 0)
        result = 31 * result + (deletedOn?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "StandardJustification(id=$id, title=$title, scope=$scope, purpose=$purpose, createdOn=$createdOn, modifiedOn=$modifiedOn, deletedOn=$deletedOn)"
    }


}
