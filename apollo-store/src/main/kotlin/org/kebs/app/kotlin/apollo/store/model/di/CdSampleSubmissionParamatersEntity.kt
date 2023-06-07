package org.kebs.app.kotlin.apollo.store.model.di

import org.kebs.app.kotlin.apollo.store.model.CdSampleSubmissionItemsEntity
import java.io.Serializable
import java.sql.Date
import java.sql.Time
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_CD_SAMPLE_SUBMISSION_PARAMATERS")
class CdSampleSubmissionParamatersEntity  : Serializable {
    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_KEBS_CD_SAMPLE_SUBMISSION_PARAMATERS_SEQ_GEN", allocationSize = 1, sequenceName = "DAT_KEBS_CD_SAMPLE_SUBMISSION_PARAMATERS_SEQ")
    @GeneratedValue(generator = "DAT_KEBS_CD_SAMPLE_SUBMISSION_PARAMATERS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long = 0

    @Transient
    var confirmLabId: Long? = null

    @Transient
    var confirmSampleId: Long? = null

    @Transient
    var confirmParamId: Long? = null

    @Column(name = "REQUIREMENTS")
    @Basic
    var requirements: String? = null

    @Column(name = "TEST_METHOD_NO")
    @Basic
    var testMethodNo: String? = null

    @Column(name = "LOD")
    @Basic
    var lod: String? = null

    @Column(name = "REPORT_UID")
    @Basic
    var reportUid: String? = null

    @Column(name = "RESULTS")
    @Basic
    var results: String? = null

    @Column(name = "LAB_REF")
    @Basic
    var labRef: String? = null

    @Column(name = "PARAMETER_NAME")
    @Basic
    var parameterName: String? = null

    @Column(name = "REMARKS")
    @Basic
    var remarks: String? = null

    @Column(name = "LAB_RESULTS_STATUS")
    @Basic
    var labResultStatus: Int? = null

    @Column(name = "LAB_RESULTS_COMPLETE_STATUS")
    @Basic
    var labResultCompleteStatus: Int? = null

    @Column(name = "LAB_RESULTS_COMPLETE_REMARKS")
    @Basic
    var labResultCompleteRemarks: String? = null

    @Column(name = "BS_NUMBER")
    @Basic
    var bsNumber: String? = null

    @Column(name = "PURPOSE_OF_TEST")
    @Basic
    var purposeOfTest: String? = null

    @Column(name = "MEASUREMENT_UNCERTAINTY")
    @Basic
    var measurementUncertainty: String? = null

    @Column(name = "TRANSACTION_DATE")
    @Basic
    var transactionDate: Date? = null

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

    @JoinColumn(name = "LABORATORY_ID", referencedColumnName = "ID")
    @ManyToOne
    var laboratoryId: CdLaboratoryEntity? = null

    @JoinColumn(name = "SAMPLE_SUBMISSION_ID", referencedColumnName = "ID")
    @ManyToOne
    var sampleSubmissionId: CdSampleSubmissionItemsEntity? = null


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other  as CdSampleSubmissionParamatersEntity
        return id == that.id &&
                confirmLabId == that.confirmLabId &&
                confirmSampleId == that.confirmSampleId &&
                confirmParamId == that.confirmParamId &&
                parameterName == that.parameterName &&
                purposeOfTest == that.purposeOfTest &&
                bsNumber == that.bsNumber &&
                remarks == that.remarks &&
                measurementUncertainty == that.measurementUncertainty &&

                lod == that.lod &&
                results == that.results &&
                testMethodNo == that.testMethodNo &&
                requirements == that.requirements &&
                reportUid == that.reportUid &&
                labResultStatus == that.labResultStatus &&
                labResultCompleteStatus == that.labResultCompleteStatus &&
                labResultCompleteRemarks == that.labResultCompleteRemarks &&

                transactionDate == that.transactionDate &&
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
                deletedOn == that.deletedOn
    }

    override fun hashCode(): Int {
        return Objects.hash(id,
                lod,
                results,
                testMethodNo,
                requirements,
                reportUid,
                labResultStatus,
                labResultCompleteStatus,
                labResultCompleteRemarks,

                bsNumber, remarks, confirmParamId, confirmLabId, confirmSampleId, parameterName, purposeOfTest, measurementUncertainty, transactionDate, description, status, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn)
    }
}