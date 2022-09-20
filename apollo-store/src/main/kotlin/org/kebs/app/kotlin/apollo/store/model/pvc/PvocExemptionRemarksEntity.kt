package org.kebs.app.kotlin.apollo.store.model.pvc

import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_PVOC_EXEMPTION_REMARKS")
class PvocExemptionRemarksEntity : Serializable {
    @Column(name = "ID", nullable = false, precision = 0)
    @Id
    @SequenceGenerator(name = "DAT_KEBS_PVOC_EXEMPTION_REMARKS_SEQ_GEN", sequenceName = "DAT_KEBS_PVOC_EXEMPTION_REMARKS_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_PVOC_EXEMPTION_REMARKS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    var id: Long? = 0

    @Column(name = "STATUS")
    @Basic
    var status: Int? = null

    @Column(name = "REMARKS")
    @Basic
    var remarks: String? = null

    @Column(name = "REVIEW_STAGE")
    @Basic
    var reviewStage: String? = null

    @Column(name = "APPROVAL_STATUS")
    @Basic
    var approvalStatus: String? = null

    @Column(name = "CREATED_BY")
    @Basic
    var createdBy: String? = null

    @Column(name = "CREATED_ON")
    @Basic
    var createdOn: Timestamp? = null

    @Column(name = "MODIFIED_BY")
    @Basic
    var modifiedBy: String? = null

    @Column(name = "MODIFIED_ON")
    @Basic
    var modifiedOn: Timestamp? = null

    @Column(name = "DELETE_BY")
    @Basic
    var deleteBy: String? = null

    @Column(name = "DELETED_ON")
    @Basic
    var deletedOn: Timestamp? = null

    @Column(name = "EXEMPTION_ID")
    @Basic
    var exemptionId: Long? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as PvocExemptionRemarksEntity
        return id == that.id &&
                status == that.status &&
                remarks == that.remarks &&
                createdBy == that.createdBy &&
                createdOn == that.createdOn &&
                modifiedBy == that.modifiedBy &&
                modifiedOn == that.modifiedOn &&
                deleteBy == that.deleteBy &&
                deletedOn == that.deletedOn &&
                exemptionId == that.exemptionId
    }

    override fun hashCode(): Int {
        return Objects.hash(id, status, remarks, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn, exemptionId)
    }

}