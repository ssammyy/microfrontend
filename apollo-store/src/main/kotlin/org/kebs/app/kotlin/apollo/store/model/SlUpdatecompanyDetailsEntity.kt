package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_SL_UPDATE_COMPANY_DETAILS")
class SlUpdatecompanyDetailsEntity : Serializable {
    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_KEBS_SL_UPDATE_COMPANY_DETAILS_SEQ_GEN", sequenceName = "DAT_KEBS_SL_UPDATE_COMPANY_DETAILS_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_SL_UPDATE_COMPANY_DETAILS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long? = 0

    @Column(name = "COMPANY_ENTITY_ID")
    @Basic
    var companyEntityId: Long? = null

    @Column(name = "PHYSICAL_ADDRESS")
    @Basic
    var physicalAddress: String? = null

    @Column(name = "POSTAL_ADDRESS")
    @Basic
    var postalAddress: String? = null

    @Column(name = "OWNERSHIP")
    @Basic
    var ownership: String? = null

    @Column(name = "CLOSURE_OF_OPERATIONS")
    @Basic
    var closureOfOperations: String? = null

    @Column(name = "ACTIVE")
    @Basic
    var active: Int? = null

    @Column(name = "CREATED_BY")
    @Basic
    var createdBy: String? = null

    @Column(name = "CREATED_ON")
    @Basic
    var createdOn: Timestamp? = null

    @Column(name = "ASSISTANT_MANAGER_APPROVER")
    @Basic
    var assistantManagerApprover: String? = null

    @Column(name = "ASSISTANT_MANAGER_APPROVED_ON")
    @Basic
    var assistantManagerApprovedOn: Timestamp? = null

    @Column(name = "MANAGER_APPROVER")
    @Basic
    var managerApprover: String? = null

    @Column(name = "DELETED_ON")
    @Basic
    var managerApprovedOn: Timestamp? = null

    @Column(name = "SL_UPDATE_STATUS")
    @Basic
    var slUpdateStatus: Int? = null

    @Column(name = "SL_UPDATE_STARTED_ON")
    @Basic
    var slUpdateStartedOn: Timestamp? = null

    @Column(name = "SL_UPDATE_COMPLETED_ON")
    @Basic
    var slUpdateCompletedOn: Timestamp? = null

    @Column(name = "SL_UPDATE_PROCESS_INSTANCE_ID")
    @Basic
    var slUpdateProcessInstanceId: String? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as SlUpdatecompanyDetailsEntity
        return id == that.id &&
                companyEntityId == that.companyEntityId &&
                physicalAddress == that.physicalAddress &&
                postalAddress == that.postalAddress &&
                ownership == that.ownership &&
                closureOfOperations == that.closureOfOperations &&
                active == that.active &&
                createdBy == that.createdBy &&
                createdOn == that.createdOn &&
                assistantManagerApprover == that.assistantManagerApprover &&
                assistantManagerApprovedOn == that.assistantManagerApprovedOn &&
                managerApprover == that.managerApprover &&
                managerApprovedOn == that.managerApprovedOn &&
                slUpdateStatus == that.slUpdateStatus &&
                slUpdateStartedOn == that.slUpdateStartedOn &&
                slUpdateCompletedOn == that.slUpdateCompletedOn &&
                slUpdateProcessInstanceId == that.slUpdateProcessInstanceId
    }

    override fun hashCode(): Int {
        return Objects.hash(
                id,
                companyEntityId,
                physicalAddress,
                postalAddress,
                ownership,
                closureOfOperations,
                active,
                createdBy,
                createdOn,
                assistantManagerApprover,
                assistantManagerApprovedOn,
                managerApprover,
                managerApprovedOn,
                slUpdateStatus,
                slUpdateStartedOn,
                slUpdateCompletedOn,
                slUpdateProcessInstanceId
        )
    }
}