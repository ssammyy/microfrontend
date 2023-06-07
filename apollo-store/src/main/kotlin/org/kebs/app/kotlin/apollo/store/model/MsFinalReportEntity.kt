package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.sql.Date
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_MS_FINAL_REPORT")
class MsFinalReportEntity : Serializable {
    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_KEBS_MS_FINAL_REPORT_SEQ_GEN", allocationSize = 1, sequenceName = "DAT_KEBS_MS_FINAL_REPORT_SEQ")
    @GeneratedValue(generator = "DAT_KEBS_MS_FINAL_REPORT_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long = 0

    @Column(name = "REMARKS")
    @Basic
    var remarks: String? = null

    @Column(name = "DESCRIPTION")
    @Basic
    var description: String? = null

    @Column(name = "STATUS")
    @Basic
    var status: Int? = null

    @Column(name = "VAR_FIELD_1")
    @Basic
    var varField1: String? = null

    @Column(name = "VAR_FIELD_2")
    @Basic
    var varField2: String? = null

    @Column(name = "VAR_FIELD_3")
    @Basic
    var varField3: String? = null

    @Column(name = "VAR_FIELD_4")
    @Basic
    var varField4: String? = null

    @Column(name = "VAR_FIELD_5")
    @Basic
    var varField5: String? = null

    @Column(name = "VAR_FIELD_6")
    @Basic
    var varField6: String? = null

    @Column(name = "VAR_FIELD_7")
    @Basic
    var varField7: String? = null

    @Column(name = "VAR_FIELD_8")
    @Basic
    var varField8: String? = null

    @Column(name = "VAR_FIELD_9")
    @Basic
    var varField9: String? = null

    @Column(name = "VAR_FIELD_10")
    @Basic
    var varField10: String? = null

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

    @Column(name = "APPROVED_BY_HOD")
    @Basic
    var approvedByHod: String? = null

    @Column(name = "REJECTED_BY_HOD")
    @Basic
    var rejectedByHod: String? = null

    @Column(name = "APPROVED_REMARKS_HOD")
    @Basic
    var approvedRemarksHod: String? = null

    @Column(name = "REJECTED_REMARKS_HOD")
    @Basic
    var rejectedRemarksHod: String? = null

    @Column(name = "REJECTED_ON_HOD")
    @Basic
    var rejectedOnHod: Date? = null

    @Column(name = "APPROVED_ON_HOD")
    @Basic
    var approvedOnHod: Date? = null

    @Column(name = "REJECTED_STATUS_HOD")
    @Basic
    var rejectedStatusHod: Int? = null

    @Column(name = "APPROVED_STATUS_HOD")
    @Basic
    var approvedStatusHod: Int? = null

    @Column(name = "APPROVED_HOD")
    @Basic
    var approvedHod: String? = null

    @Column(name = "REJECTED_HOD")
    @Basic
    var rejectedHod: String? = null

    @Column(name = "APPROVED_BY_HOF")
    @Basic
    var approvedByHof: String? = null

    @Column(name = "REJECTED_BY_HOF")
    @Basic
    var rejectedByHof: String? = null

    @Column(name = "APPROVED_REMARKS_HOF")
    @Basic
    var approvedRemarksHof: String? = null

    @Column(name = "REJECTED_REMARKS_HOF")
    @Basic
    var rejectedRemarksHof: String? = null

    @Column(name = "REJECTED_ON_HOF")
    @Basic
    var rejectedOnHof: Date? = null

    @Column(name = "APPROVED_ON_HOF")
    @Basic
    var approvedOnHof: Date? = null

    @Column(name = "REJECTED_STATUS_HOF")
    @Basic
    var rejectedStatusHof: Int? = null

    @Column(name = "APPROVED_STATUS_HOF")
    @Basic
    var approvedStatusHof: Int? = null

    @Column(name = "APPROVED_HOF")
    @Basic
    var approvedHof: String? = null

    @Column(name = "REJECTED_HOF")
    @Basic
    var rejectedHof: String? = null


    @JoinColumn(name = "MS_WORKPLAN_GENERATED_ID", referencedColumnName = "ID")
    @ManyToOne
    var workPlanGeneratedID: MsWorkPlanGeneratedEntity? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as MsFinalReportEntity
        return id == that.id &&
                remarks == that.remarks &&
                description == that.description &&
                status == that.status &&
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
                deletedOn == that.deletedOn &&
                approvedByHod == that.approvedByHod &&
                rejectedByHod == that.rejectedByHod &&
                approvedRemarksHod == that.approvedRemarksHod &&
                rejectedRemarksHod == that.rejectedRemarksHod &&
                rejectedOnHod == that.rejectedOnHod &&
                approvedOnHod == that.approvedOnHod &&
                rejectedStatusHod == that.rejectedStatusHod &&
                approvedStatusHod == that.approvedStatusHod &&
                approvedHod == that.approvedHod &&
                rejectedHod == that.rejectedHod &&
                approvedByHof == that.approvedByHof &&
                rejectedByHof == that.rejectedByHof &&
                approvedRemarksHof == that.approvedRemarksHof &&
                rejectedRemarksHof == that.rejectedRemarksHof &&
                rejectedOnHof == that.rejectedOnHof &&
                approvedOnHof == that.approvedOnHof &&
                rejectedStatusHof == that.rejectedStatusHof &&
                approvedStatusHof == that.approvedStatusHof &&
                approvedHof == that.approvedHof &&
                rejectedHof == that.rejectedHof
    }

    override fun hashCode(): Int {
        return Objects.hash(id, remarks, description, status, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn, approvedByHod, rejectedByHod, approvedRemarksHod, rejectedRemarksHod, rejectedOnHod, approvedOnHod, rejectedStatusHod, approvedStatusHod, approvedHod, rejectedHod, approvedByHof, rejectedByHof, approvedRemarksHof, rejectedRemarksHof, rejectedOnHof, approvedOnHof, rejectedStatusHof, approvedStatusHof, approvedHof, rejectedHof)
    }
}