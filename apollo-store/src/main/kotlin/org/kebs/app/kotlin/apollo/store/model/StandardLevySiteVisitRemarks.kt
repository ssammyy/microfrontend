package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_SITE_VISIT_REMARKS")
class StandardLevySiteVisitRemarks : Serializable {
    @Column(name = "ID")
    @Id
    @SequenceGenerator(
        name = "DAT_KEBS_SITE_VISIT_REMARKS_SEQ_GEN",
        sequenceName = "DAT_KEBS_SITE_VISIT_REMARKS_SEQ",
        allocationSize = 1
    )
    @GeneratedValue(generator = "DAT_KEBS_SITE_VISIT_REMARKS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    var id: Long? = null

    @Column(name = "SITE_VISIT_ID")
    @Basic
    var siteVisitId: Long? = null

    @Column(name = "REMARKS")
    @Basic
    var remarks: String? = null

    @Column(name = "REMARK_BY")
    @Basic
    var remarkBy: String? = null

    @Column(name = "STATUS")
    @Basic
    var status: String? = null

    @Column(name = "ROLE")
    @Basic
    var role: String? = null

    @Column(name = "DESCRIPTION")
    @Basic
    var description: String? = null

    @Column(name = "DATE_OF_REMARK")
    @Basic
    var dateOfRemark: Timestamp? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as StandardLevySiteVisitRemarks
        return id == that.id &&
                siteVisitId == that.siteVisitId &&
                remarks == that.remarks &&
                status == that.status &&
                role == that.role &&
                description == that.description &&
                remarkBy == that.remarkBy &&
                dateOfRemark == that.dateOfRemark

    }
    override fun hashCode(): Int {
        return Objects.hash(
            id,
            siteVisitId,
            remarks,
            status,
            role,
            description,
            remarkBy,
            dateOfRemark
        )
    }
}

