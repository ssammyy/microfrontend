package org.kebs.app.kotlin.apollo.store.model

import com.nhaarman.mockitokotlin2.reset
import java.io.Serializable
import java.sql.Date
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_MS_LABORATORY_PARAMETERS")
class MsLaboratoryParametersEntity : Serializable {
    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_KEBS_MS_LABORATORY_PARAMETERS_SEQ_GEN", allocationSize = 1, sequenceName = "DAT_KEBS_MS_LABORATORY_PARAMETERS_SEQ")
    @GeneratedValue(generator = "DAT_KEBS_MS_LABORATORY_PARAMETERS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long = 0

    @Transient
    var processStage: String? = null

    @Column(name = "PARAMETERS")
    @Basic
    var parameters: String? = null

    @Column(name = "LABORATORY_NAME")
    @Basic
    var laboratoryName: String? = null

    @Column(name = "BS_NUMBER")
    @Basic
    var bsNumber: String? = null

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

    @Column(name = "DESCRIPTION")
    @Basic
    var description: String? = null

    @Column(name = "STATUS")
    @Basic
    var status: Int? = null

    @Column(name = "OWNER_LAB_RESULTS_STATUS")
    @Basic
    var ownerLabResultsStatus: Int? = null

    @Column(name = "LAB_RESULTS_STATUS")
    @Basic
    var labResultStatus: Int? = null

    @Column(name = "LAB_RESULT_STATUS_BAD")
    @Basic
    var labResultStatusBad: Int? = null

    @Column(name = "LAB_RESULT_STATUS_BAD_REMARKS")
    @Basic
    var labResultStatusBadRemarks: String? = null

    @Column(name = "LAB_RESULT_STATUS_BAD_BY")
    @Basic
    var labResultStatusBadBy: String? = null

    @Column(name = "LAB_RESULT_STATUS_GOOD_REMARKS")
    @Basic
    var labResultStatusGoodRemarks: String? = null

    @Column(name = "LAB_RESULT_STATUS_GOOD_BY")
    @Basic
    var labResultStatusGoodBy: String? = null

    @Column(name = "LAB_RESULT_STATUS_GOOD_ON")
    @Basic
    var labResultStatusGoodOn: Date? = null

    @Column(name = "LAB_RESULT_STATUS_BAD_ON")
    @Basic
    var labResultStatusBadOn: Date? = null

    @Column(name = "LAB_RESULT_STATUS_GOOD")
    @Basic
    var labResultStatusGood: Int? = null

    @Column(name = "LAB_PASSED_STATUS")
    @Basic
    var labPassedStatus: Int? = null

    @Column(name = "LAB_PASSED_STATUS_BY")
    @Basic
    var labPassedStatusBy: String? = null

    @Column(name = "LAB_PASSED_STATUS_ON")
    @Basic
    var labPassedStatusOn: Date? = null

    @Column(name = "LAB_FAILED_STATUS")
    @Basic
    var labFailedStatus: Int? = null

    @Column(name = "LAB_FAILED_STATUS_BY")
    @Basic
    var labFailedStatusBy: String? = null

    @Column(name = "LAB_FAILED_STATUS_REMARKS")
    @Basic
    var labFailedStatusRemarks: String? = null

    @Column(name = "LAB_PASSED_STATUS_REMARKS")
    @Basic
    var labPassedStatusRemarks: String? = null

    @Column(name = "LAB_FAILED_STATUS_ON")
    @Basic
    var labFailedStatusOn: Date? = null

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

    @JoinColumn(name = "SAMPLE_SUBMISSION_ID", referencedColumnName = "ID")
    @ManyToOne
    var sampleSubmissionId: MsSampleSubmissionEntity? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as MsLaboratoryParametersEntity
        return id == that.id &&
                parameters == that.parameters &&
                labRef == that.labRef &&
                bsNumber == that.bsNumber &&
                lod == that.lod &&
                results == that.results &&
                testMethodNo == that.testMethodNo &&
                requirements == that.requirements &&
                reportUid == that.reportUid &&
                laboratoryName == that.laboratoryName &&
                description == that.description &&

                labResultStatus == that.labResultStatus &&

                labResultStatusGood == that.labResultStatusGood &&
                labResultStatusGoodRemarks == that.labResultStatusGoodRemarks &&
                labResultStatusBad == that.labResultStatusBad &&
                labResultStatusBadOn == that.labResultStatusBadOn &&
                labResultStatusGoodOn == that.labResultStatusGoodOn &&
                labResultStatusBadBy == that.labResultStatusBadBy &&
                labResultStatusGoodBy == that.labResultStatusGoodBy &&
                labResultStatusBadRemarks == that.labResultStatusBadRemarks &&

                labFailedStatusOn == that.labFailedStatusOn &&
                labFailedStatusBy == that.labFailedStatusBy &&
                labFailedStatusRemarks == that.labFailedStatusRemarks &&
                labFailedStatus == that.labFailedStatus &&

                labPassedStatusOn == that.labPassedStatusOn &&
                labPassedStatusBy == that.labPassedStatusBy &&
                labPassedStatusRemarks == that.labPassedStatusRemarks &&
                labPassedStatus == that.labPassedStatus &&

                ownerLabResultsStatus == that.ownerLabResultsStatus &&

                processStage == that.processStage &&
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
        return Objects.hash(id, parameters,
                labRef,
                bsNumber,
                labResultStatus,

                labResultStatusGood,
                labResultStatusGoodRemarks,
                labResultStatusBad,
                labResultStatusBadRemarks,
                labResultStatusBadOn,
                ownerLabResultsStatus,
                labResultStatusGoodOn,
                labResultStatusBadBy,
                labResultStatusGoodBy,

                labFailedStatusOn,
                labFailedStatusBy,
                labFailedStatusRemarks,
                labFailedStatus,

                labPassedStatusOn,
                labPassedStatusBy,
                labPassedStatusRemarks,
                labPassedStatus,
                processStage,
                lod,
                results,
                testMethodNo,
                requirements,
                reportUid,
                laboratoryName, description, status, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn)
    }

}