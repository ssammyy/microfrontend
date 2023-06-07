package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "CFG_KEBS_CD_STATUSES_VALUES")
class CdStatusesValuesEntity : Serializable {
    @Column(name = "ID")
    @Id
    var id: Long = 0

    @Column(name = "NOT_ASSIGNED_NAME")
    @Basic
    var notAssignedName: String? = null

    @Column(name = "ASSIGNED_STATUS")
    @Basic
    var assignedStatus: String? = null

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

    @Column(name = "SERVICE_MAP_ID")
    @Basic
    var serviceMapId: Int? = null

    @Column(name = "INITIAL_STATUS")
    @Basic
    var initialStatus: String? = null

    @Column(name = "ADD_REMARKS_STATUS")
    @Basic
    var addRemarksStatus: String? = null

    @Column(name = "REJECT_REMARKS_STATUS")
    @Basic
    var rejectRemarksStatus: String? = null

    @Column(name = "APPROVE_REMARKS_STATUS")
    @Basic
    var approveRemarksStatus: String? = null

    @Column(name = "TARGET_REMARKS_STATUS")
    @Basic
    var targetRemarksStatus: String? = null

    @Column(name = "SUPERVISOR_TARGET_PROCESS")
    @Basic
    var supervisorTargetProcess: String? = null

    @Column(name = "APPROVE_PROCESS")
    @Basic
    var approveProcess: String? = null

    @Column(name = "REJECT_PROCESS")
    @Basic
    var rejectProcess: String? = null

    @Column(name = "ADD_PROCESS")
    @Basic
    var addProcess: String? = null

    @Column(name = "OFFICER_TARGET_PROCESS")
    @Basic
    var officerTargetProcess: String? = null

    @Column(name = "DEFER_PROCESS")
    @Basic
    var deferProcess: String? = null

    @Column(name = "DEFER_STATUS")
    @Basic
    var deferStatus: String? = null

    @Column(name = "OFFICER_BLACKLIST_PROCESS")
    @Basic
    var officerBlacklistProcess: String? = null

    @Column(name = "OFFICER_BLACKLIST_STATUS")
    @Basic
    var officerBlacklistStatus: String? = null

    @Column(name = "PROCESS_REJECTION_PROCESS")
    @Basic
    var processRejectionProcess: String? = null

    @Column(name = "PROCESS_REJECTION_STATUS")
    @Basic
    var processRejectionStatus: String? = null

    @Column(name = "DI_SUPERVISOR_ROLE_ID")
    @Basic
    var diSupervisorRoleId: Long? = null

    @Column(name = "DI_OFFICER_ROLE_ID")
    @Basic
    var diOfficerRoleId: Long? = null

    @Column(name = "STATUS_ONE")
    @Basic
    var statusOne: Int? = null

    @Column(name = "STATUS_ZERO")
    @Basic
    var statusZero: Int? = null

    @JoinColumn(name = "DI_OFFICER_USER_TYPE_ID", referencedColumnName = "ID")
    @ManyToOne
    var diOfficerUserTypeId: UserTypesEntity? = null

    @Column(name = "SUPERVISOR_BLACKLIST_STATUS")
    @Basic
    var supervisorBlacklistStatus: String? = null

    @Column(name = "MINISTRY_ASSIGNMENT")
    @Basic
    var ministryAssignment: String? = null

    @Column(name = "MINISTRY_ASSIGNMENT_STATUS")
    @Basic
    var ministryAssignmentStatus: String? = null

    @Column(name = "BLACKLIST_REJECTED_STATUS")
    @Basic
    var blacklistRejectedStatus: String? = null

    @Column(name = "BLACKLIST_APPROVED_STATUS")
    @Basic
    var blacklistApprovedStatus: String? = null

    @Column(name = "TARGETING_REJECTED_STATUS")
    @Basic
    var targetingRejectedStatus: String? = null

    @Column(name = "TARGETING_APPROVED_STATUS")
    @Basic
    var targetingApprovedStatus: String? = null

    @Column(name = "BLACKLIST_PROCESS")
    @Basic
    var blacklistProcess: String? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as CdStatusesValuesEntity
        return id == that.id &&
                notAssignedName == that.notAssignedName &&
                assignedStatus == that.assignedStatus &&
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
                serviceMapId == that.serviceMapId &&
                initialStatus == that.initialStatus &&
                addRemarksStatus == that.addRemarksStatus &&
                rejectRemarksStatus == that.rejectRemarksStatus &&
                approveRemarksStatus == that.approveRemarksStatus &&
                targetRemarksStatus == that.targetRemarksStatus &&
                supervisorTargetProcess == that.supervisorTargetProcess &&
                approveProcess == that.approveProcess &&
                rejectProcess == that.rejectProcess &&
                addProcess == that.addProcess &&
                officerTargetProcess == that.officerTargetProcess &&
                deferProcess == that.deferProcess &&
                deferStatus == that.deferStatus &&
                officerBlacklistProcess == that.officerBlacklistProcess &&
                officerBlacklistStatus == that.officerBlacklistStatus &&
                processRejectionProcess == that.processRejectionProcess &&
                processRejectionStatus == that.processRejectionStatus &&
                diSupervisorRoleId == that.diSupervisorRoleId &&
                diOfficerRoleId == that.diOfficerRoleId &&
                statusOne == that.statusOne &&
                statusZero == that.statusZero &&
                supervisorBlacklistStatus == that.supervisorBlacklistStatus &&
                ministryAssignment == that.ministryAssignment &&
                ministryAssignmentStatus == that.ministryAssignmentStatus &&
                blacklistRejectedStatus == that.blacklistRejectedStatus &&
                blacklistApprovedStatus == that.blacklistApprovedStatus &&
                targetingRejectedStatus == that.targetingRejectedStatus &&
                targetingApprovedStatus == that.targetingApprovedStatus &&
                blacklistProcess == that.blacklistProcess
    }

    override fun hashCode(): Int {
        return Objects.hash(id, notAssignedName, assignedStatus, description, status, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn, serviceMapId, initialStatus, addRemarksStatus, rejectRemarksStatus, approveRemarksStatus, targetRemarksStatus, supervisorTargetProcess, approveProcess, rejectProcess, addProcess, officerTargetProcess, deferProcess, deferStatus, officerBlacklistProcess, officerBlacklistStatus, processRejectionProcess, processRejectionStatus, diSupervisorRoleId, diOfficerRoleId, statusOne, statusZero, supervisorBlacklistStatus, ministryAssignment, ministryAssignmentStatus, blacklistRejectedStatus, blacklistApprovedStatus, targetingRejectedStatus, targetingApprovedStatus, blacklistProcess)
    }
}