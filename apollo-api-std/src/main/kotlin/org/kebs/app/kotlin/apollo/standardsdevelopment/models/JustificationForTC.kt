package org.kebs.app.kotlin.apollo.standardsdevelopment.models

import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name="SD_JUSTIFICATION_FOR_TC")
class JustificationForTC {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    var id:Long =0

    @Column(name="SUBJECT")
    @Basic
    var subject:String? = null

    @Column(name="PROPOSER")
    @Basic
    var proposer:String? = null

    @Column(name="PURPOSE")
    @Basic
    var purpose:String? = null

    @Column(name = "TARGET_DATE")
    @Basic
    var targetDate: Timestamp? = null
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as JustificationForTC

        if (id != other.id) return false
        if (subject != other.subject) return false
        if (proposer != other.proposer) return false
        if (purpose != other.purpose) return false
        if (targetDate != other.targetDate) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (subject?.hashCode() ?: 0)
        result = 31 * result + (proposer?.hashCode() ?: 0)
        result = 31 * result + (purpose?.hashCode() ?: 0)
        result = 31 * result + (targetDate?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "JustificationForTC(id=$id, subject=$subject, proposer=$proposer, purpose=$purpose, targetDate=$targetDate)"
    }


}
