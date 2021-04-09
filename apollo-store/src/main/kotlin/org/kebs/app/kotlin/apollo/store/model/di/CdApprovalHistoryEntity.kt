package org.kebs.app.kotlin.apollo.store.model.di

import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_CD_APPROVAL_HISTORY")
class CdApprovalHistoryEntity: Serializable {
    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_KEBS_CD_APPROVAL_HISTORY_SEQ_GEN", sequenceName = "DAT_KEBS_CD_APPROVAL_HISTORY_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_CD_APPROVAL_HISTORY_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long = 0

    @Column(name = "STAGE_NO")
    @Basic
    var stageNo: String? = null

    @Column(name = "STEP_CODE")
    @Basic
    var stepCode: String? = null

    @Column(name = "MDA_CODE")
    @Basic
    var mdaCode: String? = null

    @Column(name = "ROLE_CODE")
    @Basic
    var roleCode: String? = null

    @Column(name = "DOC_STATUS")
    @Basic
    var docStatus: String? = null

    @Column(name = "USER_ID")
    @Basic
    var userId: String? = null

    @Column(name = "UPDATED_DATE")
    @Basic
    var updatedDate: String? = null

    @Column(name = "PREMISE_INSPECTION")
    @Basic
    var premiseInspection: String? = null

    @Column(name = "EXAMINATION_REQUIRED")
    @Basic
    var examinationRequired: String? = null

    @Column(name = "TECHNICAL_REJECTION")
    @Basic
    var technicalRejection: String? = null

    @Column(name = "DESCRIPTION")
    @Basic
    var description: String? = null

    @Column(name = "STATUS")
    @Basic
    var status: Long? = null

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

    @Column(name = "CD_ID")
    @Basic
    var cdId: Long? = null
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as CdApprovalHistoryEntity
        return id == that.id &&
                stageNo == that.stageNo &&
                stepCode == that.stepCode &&
                mdaCode == that.mdaCode &&
                roleCode == that.roleCode &&
                docStatus == that.docStatus &&
                userId == that.userId &&
                updatedDate == that.updatedDate &&
                premiseInspection == that.premiseInspection &&
                examinationRequired == that.examinationRequired &&
                technicalRejection == that.technicalRejection &&
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
                cdId == that.cdId
    }

    override fun hashCode(): Int {
        return Objects.hash(id, stageNo, stepCode, mdaCode, roleCode, docStatus, userId, updatedDate, premiseInspection, examinationRequired, technicalRejection, description, status, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn, cdId)
    }
}