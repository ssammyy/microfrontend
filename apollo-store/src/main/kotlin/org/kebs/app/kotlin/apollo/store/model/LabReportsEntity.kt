package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_LAB_REPORTS")
class LabReportsEntity: Serializable {
    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_KEBS_LAB_REPORTS_SEQ_GEN", sequenceName = "DAT_KEBS_LAB_REPORTS_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_LAB_REPORTS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long = 0

    @Column(name = "STATUS")
    @Basic
    var status: Int? = null

    @Column(name = "LAB_REPORT_COMPLETENESS_STATUS")
    @Basic
    var labReportCompletenessStatuss: Int? = null

    /*
    @JoinColumn(name = "PERMIT", referencedColumnName = "ID")
    @ManyToOne
    var permit: PermitApplicationEntity? = null
    */

    @Column(name = "PERMIT")
    @Basic
    var permit: Long? = null

    @JoinColumn(name = "SAMPLE_SUBMISSION_ENTITY", referencedColumnName = "ID")
    @ManyToOne
    var sampleSubmissionEntity: CdSampleSubmissionItemsEntity? = null

    @Column(name = "LAB_NAME")
    @Basic
    var labName: String? = null

    @Column(name = "LAB_REPORT_FILE_PATH")
    @Basic
    var labReportFilePath: String? = null

    @Column(name = "APPROVAL_STATUS")
    @Basic
    var approvalStatus: Long? = null

    @Column(name = "DEFFERAL_STATUS")
    @Basic
    var defferalStatus: Long? = null

    @Column(name = "REJECTION_STATUS")
    @Basic
    var rejectionStatus: Long? = null

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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as LabReportsEntity
        return id == that.id &&
                status == that.status &&
                permit == that.permit &&
                labReportCompletenessStatuss == that.labReportCompletenessStatuss &&
                labName == that.labName &&
                labReportFilePath == that.labReportFilePath &&
                approvalStatus == that.approvalStatus &&
                defferalStatus == that.defferalStatus &&
                sampleSubmissionEntity == that.sampleSubmissionEntity &&
                rejectionStatus == that.rejectionStatus &&
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
        return Objects.hash(id, status, labName, labReportCompletenessStatuss, permit, sampleSubmissionEntity, labReportFilePath, approvalStatus, defferalStatus, rejectionStatus, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn)
    }
}