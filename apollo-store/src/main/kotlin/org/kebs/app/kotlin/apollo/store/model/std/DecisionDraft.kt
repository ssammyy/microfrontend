package org.kebs.app.kotlin.apollo.store.model.std

import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.*

@Entity
@Table(name = "SD_DECISION_DRAFT")
class DecisionDraft {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id:Long =0

    @Column(name = "REASON")
    @Basic
    var reason: String? = null

    @Column(name = "DECISION")
    @Basic
    var decision: String? = null


    @Transient
    @JsonProperty("taskId")
    var taskId:String?=null
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DecisionDraft

        if (id != other.id) return false
        if (reason != other.reason) return false
        if (decision != other.decision) return false
        if (taskId != other.taskId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (reason?.hashCode() ?: 0)
        result = 31 * result + (decision?.hashCode() ?: 0)
        result = 31 * result + (taskId?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "DecisionDraft(id=$id, reason=$reason, decision=$decision, taskId=$taskId)"
    }


}
