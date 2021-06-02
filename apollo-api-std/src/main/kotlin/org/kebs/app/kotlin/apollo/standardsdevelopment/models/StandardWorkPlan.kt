package org.kebs.app.kotlin.apollo.standardsdevelopment.models

import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name="SD_WORKPLAN")
class StandardWorkPlan {
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id:Long=0

    @Column(name="TITLE")
    @Basic
    var title:String?=null

    @Transient
    @JsonProperty("taskId")
    var taskId:String?=null

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

        other as StandardWorkPlan

        if (id != other.id) return false
        if (title != other.title) return false
        if (createdOn != other.createdOn) return false
        if (modifiedOn != other.modifiedOn) return false
        if (deletedOn != other.deletedOn) return false

        return true
    }



    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (title?.hashCode() ?: 0)
        result = 31 * result + (createdOn?.hashCode() ?: 0)
        result = 31 * result + (modifiedOn?.hashCode() ?: 0)
        result = 31 * result + (deletedOn?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "StandardWorkPlan(id=$id, title=$title, createdOn=$createdOn, modifiedOn=$modifiedOn, deletedOn=$deletedOn)"
    }


}