package org.kebs.app.kotlin.apollo.store.model

import java.sql.Time
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_MS_FUEL_REPORT_APPROVALS")
class MsFuelReportApprovalsEntity {
    @get:Column(name = "ID")
    @get:Id
    var id: Long = 0

    @get:Column(name = "KEBS_OFFICER")
    @get:Basic
    var kebsOfficer: String? = null

    @get:Column(name = "OFFICER_DESIGNATION")
    @get:Basic
    var officerDesignation: String? = null

    @get:Column(name = "CURRENT_APPROVER")
    @get:Basic
    var currentApprover: String? = null

    @get:Column(name = "APPROVER_EMAIL")
    @get:Basic
    var approverEmail: String? = null

    @get:Column(name = "APPROVAL")
    @get:Basic
    var approval: String? = null

    @get:Column(name = "APPROVAL_STATUS")
    @get:Basic
    var approvalStatus: String? = null

    @get:Column(name = "TRANSACTION_DATE")
    @get:Basic
    var transactionDate: Time? = null

    @get:Column(name = "STATUS")
    @get:Basic
    var status: Long? = null

    @get:Column(name = "REMARKS")
    @get:Basic
    var remarks: String? = null

    @get:Column(name = "VAR_FIELD_1")
    @get:Basic
    var varField1: String? = null

    @get:Column(name = "VAR_FIELD_2")
    @get:Basic
    var varField2: String? = null

    @get:Column(name = "VAR_FIELD_3")
    @get:Basic
    var varField3: String? = null

    @get:Column(name = "VAR_FIELD_4")
    @get:Basic
    var varField4: String? = null

    @get:Column(name = "VAR_FIELD_5")
    @get:Basic
    var varField5: String? = null

    @get:Column(name = "VAR_FIELD_6")
    @get:Basic
    var varField6: String? = null

    @get:Column(name = "VAR_FIELD_7")
    @get:Basic
    var varField7: String? = null

    @get:Column(name = "VAR_FIELD_8")
    @get:Basic
    var varField8: String? = null

    @get:Column(name = "VAR_FIELD_9")
    @get:Basic
    var varField9: String? = null

    @get:Column(name = "VAR_FIELD_10")
    @get:Basic
    var varField10: String? = null

    @get:Column(name = "CREATED_BY")
    @get:Basic
    var createdBy: String? = null

    @get:Column(name = "CREATED_ON")
    @get:Basic
    var createdOn: Timestamp? = null

    @get:Column(name = "LAST_MODIFIED_BY")
    @get:Basic
    var lastModifiedBy: String? = null

    @get:Column(name = "LAST_MODIFIED_ON")
    @get:Basic
    var lastModifiedOn: Timestamp? = null

    @get:Column(name = "UPDATE_BY")
    @get:Basic
    var updateBy: String? = null

    @get:Column(name = "UPDATED_ON")
    @get:Basic
    var updatedOn: Timestamp? = null

    @get:Column(name = "DELETE_BY")
    @get:Basic
    var deleteBy: String? = null

    @get:Column(name = "DELETED_ON")
    @get:Basic
    var deletedOn: Timestamp? = null

    @get:Column(name = "VERSION")
    @get:Basic
    var version: Long? = null

    @get:JoinColumn(name = "INSPECTION_ID", referencedColumnName = "ID")
    @get:ManyToOne
    var msFuelInspectionByInspectionId: MsFuelInspectionEntity? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as MsFuelReportApprovalsEntity
        return id == that.id &&
                kebsOfficer == that.kebsOfficer &&
                officerDesignation == that.officerDesignation &&
                currentApprover == that.currentApprover &&
                approverEmail == that.approverEmail &&
                approval == that.approval &&
                approvalStatus == that.approvalStatus &&
                transactionDate == that.transactionDate &&
                status == that.status &&
                remarks == that.remarks &&
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
                lastModifiedBy == that.lastModifiedBy &&
                lastModifiedOn == that.lastModifiedOn &&
                updateBy == that.updateBy &&
                updatedOn == that.updatedOn &&
                deleteBy == that.deleteBy &&
                deletedOn == that.deletedOn &&
                version == that.version
    }

    override fun hashCode(): Int {
        return Objects.hash(id, kebsOfficer, officerDesignation, currentApprover, approverEmail, approval, approvalStatus, transactionDate, status, remarks, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, lastModifiedBy, lastModifiedOn, updateBy, updatedOn, deleteBy, deletedOn, version)
    }

}