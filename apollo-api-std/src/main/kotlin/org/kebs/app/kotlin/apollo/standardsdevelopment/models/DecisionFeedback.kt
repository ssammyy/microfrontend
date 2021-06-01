package org.kebs.app.kotlin.apollo.standardsdevelopment.models

import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name="SD_DECISION_FEEDBACK")
class DecisionFeedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    var id:Long=0

    @Column(name="user_id")
    @Basic
    var user_id:Long=0

    @Column(name="item_id")
    @Basic
    var item_id:String?=null

    @Column(name="status")
    @Basic
    var status:Boolean?=null

    @Column(name="comment")
    @Basic
    var comment:String?=null

    @Transient
    @JsonProperty("taskId")
    var taskId:String?=null

    @Column(name="SUBMISSION_DATE")
    @Basic
    var submission_date: Timestamp?=null
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DecisionFeedback

        if (id != other.id) return false
        if (user_id != other.user_id) return false
        if (item_id != other.item_id) return false
        if (status != other.status) return false
        if (comment != other.comment) return false
        if (submission_date != other.submission_date) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + user_id.hashCode()
        result = 31 * result + (item_id?.hashCode() ?: 0)
        result = 31 * result + (status?.hashCode() ?: 0)
        result = 31 * result + (comment?.hashCode() ?: 0)
        result = 31 * result + (submission_date?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "DecisionFeedback(id=$id, user_id=$user_id, item_id=$item_id, status=$status, comment=$comment, submission_date=$submission_date)"
    }


}
