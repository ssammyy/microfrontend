package org.kebs.app.kotlin.apollo.store.model.qa

import java.io.Serializable
import java.sql.Date
import java.sql.Time
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_QA_SCHEME_FOR_SUPERVISION")
class QaSchemeForSupervisionEntity : Serializable {
    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_KEBS_QA_SCHEME_FOR_SUPERVISION_SEQ_GEN", allocationSize = 1, sequenceName = "DAT_KEBS_QA_SCHEME_FOR_SUPERVISION_SEQ")
    @GeneratedValue(generator = "DAT_KEBS_QA_SCHEME_FOR_SUPERVISION_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long? = 0

    @Column(name = "PERMIT_ID")
    @Basic
    var permitId: Long? = null

    @Column(name = "PERMIT_REF_NUMBER")
    @Basic
    var permitRefNumber: String? = null

    @Column(name = "PROCESS_FLOW_RAW_MATERIALS_INTAKE")
    @Basic
    var processFlowRawMaterialsIntake: String? = null

    @Column(name = "OPERATIONS_RAW_MATERIALS_INTAKE")
    @Basic
    var operationsRawMaterialsIntake: String? = null

    @Column(name = "QUALITY_CHECKS_RAW_MATERIALS_INTAKE")
    @Basic
    var qualityChecksRawMaterialsIntake: String? = null

    @Column(name = "FREQUENCY_RAW_MATERIALS_INTAKE")
    @Basic
    var frequencyRawMaterialsIntake: String? = null

    @Column(name = "RECORDS_RAW_MATERIALS_INTAKE")
    @Basic
    var recordsRawMaterialsIntake: String? = null

    @Column(name = "PROCESS_FLOW_IN_PROCESS")
    @Basic
    var processFlowInProcess: String? = null

    @Column(name = "OPERATIONS_IN_PROCESS")
    @Basic
    var operationsInProcess: String? = null

    @Column(name = "QUALITY_CHECKS_IN_PROCESS")
    @Basic
    var qualityChecksInProcess: String? = null

    @Column(name = "FREQUENCY_IN_PROCESS")
    @Basic
    var frequencyInProcess: String? = null

    @Column(name = "RECORDS_IN_PROCESS")
    @Basic
    var recordsInProcess: String? = null

    @Column(name = "PROCESS_FLOW_FINAL_PRODUCT")
    @Basic
    var processFlowFinalProduct: String? = null

    @Column(name = "OPERATIONS_FINAL_PRODUCT")
    @Basic
    var operationsFinalProduct: String? = null

    @Column(name = "QUALITY_CHECKS_FINAL_PRODUCT")
    @Basic
    var qualityChecksFinalProduct: String? = null

    @Column(name = "FREQUENCY_FINAL_PRODUCT")
    @Basic
    var frequencyFinalProduct: String? = null

    @Column(name = "RECORDS_FINAL_PRODUCT")
    @Basic
    var recordsFinalProduct: String? = null

    @Column(name = "PROCESS_FLOW_PACKAGING")
    @Basic
    var processFlowPackaging: String? = null

    @Column(name = "OPERATIONS_PACKAGING")
    @Basic
    var operationsPackaging: String? = null

    @Column(name = "QUALITY_CHECKS_PACKAGING")
    @Basic
    var qualityChecksPackaging: String? = null

    @Column(name = "FREQUENCY_PACKAGING")
    @Basic
    var frequencyPackaging: String? = null

    @Column(name = "RECORDS_PACKAGING")
    @Basic
    var recordsPackaging: String? = null

    @Column(name = "PROCESS_FLOW_STORAGE")
    @Basic
    var processFlowStorage: String? = null

    @Column(name = "OPERATIONS_STORAGE")
    @Basic
    var operationsStorage: String? = null

    @Column(name = "QUALITY_CHECKS_STORAGE")
    @Basic
    var qualityChecksStorage: String? = null

    @Column(name = "FREQUENCY_STORAGE")
    @Basic
    var frequencyStorage: String? = null

    @Column(name = "RECORDS_STORAGE")
    @Basic
    var recordsStorage: String? = null

    @Column(name = "PROCESS_FLOW_DISPATCH")
    @Basic
    var processFlowDispatch: String? = null

    @Column(name = "OPERATIONS_DISPATCH")
    @Basic
    var operationsDispatch: String? = null

    @Column(name = "QUALITY_CHECKS_DISPATCH")
    @Basic
    var qualityChecksDispatch: String? = null

    @Column(name = "FREQUENCY_DISPATCH")
    @Basic
    var frequencyDispatch: String? = null

    @Column(name = "RECORDS_DISPATCH")
    @Basic
    var recordsDispatch: String? = null

    @Column(name = "ACCEPTED_REJECTED_STATUS")
    @Basic
    var acceptedRejectedStatus: Int? = null

    @Column(name = "ACCEPTED_REJECTED_BY")
    @Basic
    var acceptedRejectedBy: String? = null

    @Column(name = "ACCEPTED_REJECTED_REASON")
    @Basic
    var acceptedRejectedReason: String? = null

    @Column(name = "ACCEPTED_REJECTED_DATE")
    @Basic
    var acceptedRejectedDate: Date? = null

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
        val that = other as QaSchemeForSupervisionEntity
        return id == that.id &&
                permitId == that.permitId &&
                processFlowRawMaterialsIntake == that.processFlowRawMaterialsIntake &&
                operationsRawMaterialsIntake == that.operationsRawMaterialsIntake &&
                qualityChecksRawMaterialsIntake == that.qualityChecksRawMaterialsIntake &&
                frequencyRawMaterialsIntake == that.frequencyRawMaterialsIntake &&
                recordsRawMaterialsIntake == that.recordsRawMaterialsIntake &&
                processFlowInProcess == that.processFlowInProcess &&
                operationsInProcess == that.operationsInProcess &&
                qualityChecksInProcess == that.qualityChecksInProcess &&
                frequencyInProcess == that.frequencyInProcess &&
                recordsInProcess == that.recordsInProcess &&
                processFlowFinalProduct == that.processFlowFinalProduct &&
                operationsFinalProduct == that.operationsFinalProduct &&
                qualityChecksFinalProduct == that.qualityChecksFinalProduct &&
                frequencyFinalProduct == that.frequencyFinalProduct &&
                recordsFinalProduct == that.recordsFinalProduct &&
                processFlowPackaging == that.processFlowPackaging &&
                operationsPackaging == that.operationsPackaging &&
                qualityChecksPackaging == that.qualityChecksPackaging &&
                frequencyPackaging == that.frequencyPackaging &&
                recordsPackaging == that.recordsPackaging &&
                processFlowStorage == that.processFlowStorage &&
                operationsStorage == that.operationsStorage &&
                qualityChecksStorage == that.qualityChecksStorage &&
                frequencyStorage == that.frequencyStorage &&
                recordsStorage == that.recordsStorage &&
                processFlowDispatch == that.processFlowDispatch &&
                operationsDispatch == that.operationsDispatch &&
                qualityChecksDispatch == that.qualityChecksDispatch &&
                frequencyDispatch == that.frequencyDispatch &&
                recordsDispatch == that.recordsDispatch &&
                acceptedRejectedStatus == that.acceptedRejectedStatus &&
                acceptedRejectedDate == that.acceptedRejectedDate &&
                acceptedRejectedReason == that.acceptedRejectedReason &&
                acceptedRejectedBy == that.acceptedRejectedBy &&
                permitRefNumber == that.permitRefNumber &&
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
        return Objects.hash(
            id,
            permitId,
             permitRefNumber,
            processFlowRawMaterialsIntake,
            operationsRawMaterialsIntake,
            qualityChecksRawMaterialsIntake,
            frequencyRawMaterialsIntake,
            recordsRawMaterialsIntake,
            processFlowInProcess,
            acceptedRejectedReason,
            operationsInProcess,
            qualityChecksInProcess,
            frequencyInProcess,
            recordsInProcess,
            processFlowFinalProduct,
            operationsFinalProduct,
            qualityChecksFinalProduct,
            frequencyFinalProduct,
            recordsFinalProduct,
            processFlowPackaging,
            operationsPackaging,
            qualityChecksPackaging,
            frequencyPackaging,
            recordsPackaging,
            processFlowStorage,
            operationsStorage,
            qualityChecksStorage,
            frequencyStorage,
            recordsStorage,
            processFlowDispatch,
            operationsDispatch,
            qualityChecksDispatch,
            frequencyDispatch,
            recordsDispatch,
            acceptedRejectedStatus,
            acceptedRejectedDate,
            acceptedRejectedBy,
            description,
            status,
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
            createdBy,
            createdOn,
            modifiedBy,
            modifiedOn,
            deleteBy,
            deletedOn
        )
    }
}