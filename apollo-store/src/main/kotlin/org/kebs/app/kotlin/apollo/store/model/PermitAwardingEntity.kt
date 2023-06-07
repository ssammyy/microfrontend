package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_PERMIT_AWARDING")
class PermitAwardingEntity : Serializable {
    @Column(name = "ID")
    @Id
    @SequenceGenerator(name = "DAT_KEBS_PERMIT_AWARDING_SEQ_GEN", sequenceName = "DAT_KEBS_PERMIT_AWARDING_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_PERMIT_AWARDING_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    var id: Long = 0

    @Column(name = "LAB_REPORTS_ID", nullable = true, precision = 0)
    @Basic
    var labReportsId: Long? = null

    @Column(name = "MANUFACTURER_ID", nullable = true, precision = 0)
    @Basic
    var manufacturerId: Long? = null

    @Column(name = "PERMIT_ID", nullable = true, precision = 0)
    @Basic
    var permitId: Long? = null

    @Column(name = "AWARDED_STATUS", nullable = false, precision = 0)
    @Basic
    var awardedStatus: Int = 0

    @Column(name = "AWARDED_DATE", nullable = true)
    @Basic
    var awardedDate: Timestamp? = null

    @Column(name = "PSC_DEFER_STATUS", nullable = false, precision = 0)
    @Basic
    var pscDeferStatus: Int = 0

    @Column(name = "PSC_DEFER_REMARKS", nullable = true, length = 3800)
    @Basic
    var pscDeferRemarks: String? = null

    @Column(name = "PSC_STATUS", nullable = false, precision = 0)
    @Basic
    var pscStatus: Int = 0

    @Column(name = "PSC_PERMIT_INFO", nullable = true, length = 3800)
    @Basic
    var pscPermitInfo: String? = null

    @Column(name = "PSC_DATE_VIEW", nullable = true)
    @Basic
    var pscDateView: Timestamp? = null

    @Column(name = "PCM_STATUS", nullable = false, precision = 0)
    @Basic
    var pcmStatus: Int = 0

    @Column(name = "PCM_PERMIT_INFO", nullable = true, length = 3800)
    @Basic
    var pcmPermitInfo: String? = null

    @Column(name = "PCM_DEFER_STATUS", nullable = false, precision = 0)
    @Basic
    var pcmDeferStatus: Int = 0

    @Column(name = "PCM_DEFER_REMARKS", nullable = true, length = 3800)
    @Basic
    var pcmDeferRemarks: String? = null

    @Column(name = "PCM_DATE_VIEW", nullable = true)
    @Basic
    var pcmDateView: Timestamp? = null

    @Column(name = "RECEIPT_E_PERMIT", nullable = true, length = 3800)
    @Basic
    var receiptEPermit: String? = null

    @Column(name = "STATUS", nullable = false, precision = 0)
    @Basic
    var status: Int = 0

    @Column(name = "VAR_FIELD_1", nullable = true, length = 350)
    @Basic
    var varField1: String? = null

    @Column(name = "VAR_FIELD_2", nullable = true, length = 350)
    @Basic
    var varField2: String? = null

    @Column(name = "VAR_FIELD_3", nullable = true, length = 350)
    @Basic
    var varField3: String? = null

    @Column(name = "VAR_FIELD_4", nullable = true, length = 350)
    @Basic
    var varField4: String? = null

    @Column(name = "VAR_FIELD_5", nullable = true, length = 350)
    @Basic
    var varField5: String? = null

    @Column(name = "VAR_FIELD_6", nullable = true, length = 350)
    @Basic
    var varField6: String? = null

    @Column(name = "VAR_FIELD_7", nullable = true, length = 350)
    @Basic
    var varField7: String? = null

    @Column(name = "VAR_FIELD_8", nullable = true, length = 350)
    @Basic
    var varField8: String? = null

    @Column(name = "VAR_FIELD_9", nullable = true, length = 350)
    @Basic
    var varField9: String? = null

    @Column(name = "VAR_FIELD_10", nullable = true, length = 350)
    @Basic
    var varField10: String? = null

    @Column(name = "CREATED_BY", nullable = false, length = 100)
    @Basic
    var createdBy: String? = null

    @Column(name = "CREATED_ON", nullable = false)
    @Basic
    var createdOn: Timestamp? = null

    @Column(name = "MODIFIED_BY", nullable = true, length = 100)
    @Basic
    var modifiedBy: String? = null

    @Column(name = "MODIFIED_ON", nullable = true)
    @Basic
    var modifiedOn: Timestamp? = null

    @Column(name = "DELETE_BY", nullable = true, length = 100)
    @Basic
    var deleteBy: String? = null

    @Column(name = "DELETED_ON", nullable = true)
    @Basic
    var deletedOn: Timestamp? = null


//    @JoinColumn(name = "PERMIT_ID", referencedColumnName = "ID")
//    @ManyToOne
//    var permitId: PermitApplicationEntity? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as PermitAwardingEntity
        return id == that.id && awardedStatus == that.awardedStatus && pscDeferStatus == that.pscDeferStatus && pscStatus == that.pscStatus && pcmStatus == that.pcmStatus && pcmDeferStatus == that.pcmDeferStatus && status == that.status &&
                labReportsId == that.labReportsId &&
                manufacturerId == that.manufacturerId &&
                permitId == that.permitId &&
                awardedDate == that.awardedDate &&
                pscDeferRemarks == that.pscDeferRemarks &&
                pscPermitInfo == that.pscPermitInfo &&
                pscDateView == that.pscDateView &&
                pcmPermitInfo == that.pcmPermitInfo &&
                pcmDeferRemarks == that.pcmDeferRemarks &&
                pcmDateView == that.pcmDateView &&
                receiptEPermit == that.receiptEPermit &&
                varField1 == that.varField1 &&
                varField2 == that.varField2 &&
                varField3 == that.varField3 &&
                varField4 == that.varField4 &&
                varField5 == that.varField5 &&
                varField6 == that.varField6 &&
                varField7 == that.varField7 &&
                varField8 == that.varField8 &&
                varField9 == that.varField9 &&
                varField10 == that.varField10 &&
                createdBy == that.createdBy &&
                createdOn == that.createdOn &&
                modifiedBy == that.modifiedBy &&
                modifiedOn == that.modifiedOn &&
                deleteBy == that.deleteBy &&
                deletedOn == that.deletedOn
    }

    override fun hashCode(): Int {
        return Objects.hash(id, labReportsId, manufacturerId, permitId, awardedStatus, awardedDate, pscDeferStatus, pscDeferRemarks, pscStatus, pscPermitInfo, pscDateView, pcmStatus, pcmPermitInfo, pcmDeferStatus, pcmDeferRemarks, pcmDateView, receiptEPermit, status, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn)
    }

}