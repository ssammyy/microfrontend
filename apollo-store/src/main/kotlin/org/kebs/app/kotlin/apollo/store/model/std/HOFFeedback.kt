package org.kebs.app.kotlin.apollo.store.model.std

import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name = "SD_HOF_FEEDBACK")
class HOFFeedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    var id: Long = 0

    @Column(name = "SD_REQUEST_ID")
    @Basic
    var sdRequestID: String? = null

    @Column(name = "IS_TC")
    @Basic
    var isTc: String? = null

    @Column(name = "IS_TC_SEC")
    @Basic
    var isTcSec: String? = null

    @Column(name = "SD_OUTPUT")
    @Basic
    var sdOutput: String? = null


    @Column(name = "SD_RESULT")
    @Basic
    var sdResult: String? = null

    @Column(name = "REVIEWED_BY")
    @Basic
    var reviewedBy:  String? = null

    @Column(name = "REASON")
    @Basic
    var reason: String? = null

    @Column(name = "CREATED_ON")
    @Basic
    var createdOn: Timestamp? = null

    @Transient
    @JsonProperty("taskId")
    var taskId: String? = null
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as HOFFeedback

        if (id != other.id) return false
        if (sdRequestID != other.sdRequestID) return false
        if (isTc != other.isTc) return false
        if (isTcSec != other.isTcSec) return false
        if (sdOutput != other.sdOutput) return false
        if (taskId != other.taskId) return false
        if (reviewedBy != other.reviewedBy) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (sdRequestID?.hashCode() ?: 0)
        result = 31 * result + (isTc?.hashCode() ?: 0)
        result = 31 * result + (isTcSec?.hashCode() ?: 0)
        result = 31 * result + (sdOutput?.hashCode() ?: 0)
        result = 31 * result + (taskId?.hashCode() ?: 0)
        result = 31 * result + (reviewedBy?.hashCode() ?: 0)

        return result
    }

    override fun toString(): String {
        return "HOFFeedback(id=$id, sdRequestID=$sdRequestID, isTc=$isTc, isTcSec=$isTcSec, sdOutput=$sdOutput, reviewedBy=$reviewedBy,  taskId=$taskId)"
    }


}
