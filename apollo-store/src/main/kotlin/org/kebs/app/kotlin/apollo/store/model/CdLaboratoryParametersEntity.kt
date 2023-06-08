package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_CD_LABORATORY_PARAMETERS")
class CdLaboratoryParametersEntity : Serializable {
    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_KEBS_CD_LABORATORY_PARAMETERS_SEQ_GEN", allocationSize = 1, sequenceName = "DAT_KEBS_CD_LABORATORY_PARAMETERS_SEQ")
    @GeneratedValue(generator = "DAT_KEBS_CD_LABORATORY_PARAMETERS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long = 0

    @Column(name = "LABORATORY_ID")
    @Basic
    var laboratoryId: Long? = null

    @Column(name = "PARAMETERS")
    @Basic
    var parameters: String? = null

    @Column(name = "LABORATORY_NAME")
    @Basic
    var laboratoryName: String? = null

    @Column(name = "ITEM_ID")
    @Basic
    var itemId: Long? = 0

    @Column(name = "PURPOSE_OF_TEST")
    @Basic
    var purposeOfTest: String? = null

    @Column(name = "MEASUREMENT_UNCERTAINTY")
    @Basic
    var measurementUncertainty: String? = null

    @Column(name = "SAMPLE_SUBMISSION_ID")
    @Basic
    var sampleSubmissionId: Long? = null

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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as CdLaboratoryParametersEntity
        return id == that.id &&
                laboratoryId == that.laboratoryId &&
                parameters == that.parameters &&
                sampleSubmissionId == that.sampleSubmissionId &&
                description == that.description &&
                laboratoryName == that.laboratoryName &&
                status == that.status &&
                purposeOfTest == that.purposeOfTest &&
                measurementUncertainty == that.measurementUncertainty &&
                itemId == that.itemId &&
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
        return Objects.hash(id, laboratoryId, itemId, measurementUncertainty, laboratoryName, purposeOfTest, parameters, sampleSubmissionId, description, status, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn)
    }
}