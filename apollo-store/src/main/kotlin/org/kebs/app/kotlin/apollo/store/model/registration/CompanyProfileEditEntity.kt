package org.kebs.app.kotlin.apollo.store.model.registration

import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_COMPANY_PROFILE_EDIT")
class CompanyProfileEditEntity : Serializable {

    @Id
    @Column(name = "ID")
    @SequenceGenerator(
        name = "DAT_KEBS_COMPANY_PROFILE_EDIT_SEQ_GEN",
        sequenceName = "DAT_KEBS_COMPANY_PROFILE_EDIT_SEQ",
        allocationSize = 1
    )
    @GeneratedValue(generator = "DAT_KEBS_COMPANY_PROFILE_EDIT_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    var id: Long = 0

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
    var closureOfOperations: Int? = null

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

    @Column(name = "UPDATE_BY")
    @Basic
    var updateBy: String? = null

    @Column(name = "UPDATED_ON")
    @Basic
    var updatedOn: Timestamp? = null

    @Column(name = "DELETE_BY")
    @Basic
    var deleteBy: String? = null

    @Column(name = "DELETED_ON")
    @Basic
    var deletedOn: Timestamp? = null

    @Column(name = "VERSION")
    @Basic
    var version: Long? = null

    @Column(name = "MANUFACTURE_ID")
    @Basic
    var manufactureId: Long? = null

    @Column(name = "SL_BPMN_PROCESS_INSTANCE")
    @Basic
    var slBpmnProcessInstance: String? = null

    @Column(name = "USER_TYPE")
    @Basic
    var userType: Long? = null

    @Column(name = "ASSIGNED_TO")
    @Basic
    var assignedTo: Long? = null

    @Column(name = "TASK_TYPE")
    @Basic
    var taskType: Long? = null

    @Column(name = "NAME")
    @Basic
    var name: String? = null

    @Column(name = "KRA_PIN")
    @Basic
    var kraPin: String? = null

    @Column(name = "REGISTRATION_NUMBER")
    @Basic
    var registrationNumber: String? = null

    @Column(name = "ENTRY_NUMBER")
    @Basic
    var entryNumber: String? = null

    @Column(name = "STATUS")
    @Basic
    var status: Long? = null




    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as CompanyProfileEditEntity
        return id == that.id &&
                physicalAddress == that.physicalAddress &&
                postalAddress == that.postalAddress &&
                manufactureId == that.manufactureId &&
                closureOfOperations == that.closureOfOperations &&
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
                slBpmnProcessInstance == that.slBpmnProcessInstance &&
                userType == that.userType &&
                assignedTo == that.assignedTo &&
                taskType == that.taskType &&
                createdBy == that.createdBy &&
                createdOn == that.createdOn &&
                modifiedBy == that.modifiedBy &&
                modifiedOn == that.modifiedOn &&
                deleteBy == that.deleteBy &&
                deletedOn == that.deletedOn &&
                name == that.name &&
                kraPin == that.kraPin &&
                registrationNumber == that.registrationNumber &&
                entryNumber == that.entryNumber &&
                status == that.status &&
                version == that.version &&
                updateBy == that.updateBy &&
                ownership == that.ownership &&
                updatedOn == that.updatedOn
    }
    override fun hashCode(): Int {
        return Objects.hash(
            id,
            postalAddress,
            physicalAddress,
            manufactureId,
            closureOfOperations,
            varField1,
            varField2,
            varField3,
            varField4,
            varField5,
            varField6,
            varField7,
            varField8,
            varField9,
            varField10,
            slBpmnProcessInstance,
            userType,
            assignedTo,
            taskType,
            createdBy,
            createdOn,
            modifiedBy,
            modifiedOn,
            deleteBy,
            deletedOn,
            name,
            kraPin,
            registrationNumber,
            entryNumber,
            status,
            version,
            updateBy,
            ownership,
            updatedOn


        )
    }
}