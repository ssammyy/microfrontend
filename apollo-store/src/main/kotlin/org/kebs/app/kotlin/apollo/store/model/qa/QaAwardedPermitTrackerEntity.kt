package org.kebs.app.kotlin.apollo.store.model.qa

import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_AWARDED_PERMIT_TRACKER")
class QaAwardedPermitTrackerEntity : Serializable {
    @Column(name = "ID")
    @Id
    @SequenceGenerator(name = "DAT_KEBS_AWARDED_PERMIT_TRACKER_seq_GEN", sequenceName = "DAT_KEBS_AWARDED_PERMIT_TRACKER_seq", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_AWARDED_PERMIT_TRACKER_seq_GEN", strategy = GenerationType.SEQUENCE)
    var id: Long = 0

    @Column(name = "AWARDED_PERMIT_NUMBER")
    @Basic
    var awardedPermitNumber: Long? = null



    @Column(name = "CREATED_ON")
    @Basic
    var createdOn: Timestamp? = null



    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as QaAwardedPermitTrackerEntity
        return id == that.id &&
                awardedPermitNumber == that.awardedPermitNumber &&
                createdOn == that.createdOn
    }


    override fun hashCode(): Int {
        return Objects.hash(
            id,
            awardedPermitNumber,
            createdOn
        )
    }
}
