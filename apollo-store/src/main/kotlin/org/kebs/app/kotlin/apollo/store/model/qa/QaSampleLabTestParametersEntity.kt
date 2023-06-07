package org.kebs.app.kotlin.apollo.store.model.qa

import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_QA_SAMPLE_LAB_TEST_PARAMETERS")
class QaSampleLabTestParametersEntity : Serializable {
    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_KEBS_QA_SAMPLE_LAB_TEST_PARAMETERS_SEQ_GEN", allocationSize = 1, sequenceName = "DAT_KEBS_QA_SAMPLE_LAB_TEST_PARAMETERS_SEQ")
    @GeneratedValue(generator = "DAT_KEBS_QA_SAMPLE_LAB_TEST_PARAMETERS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long? = 0

    @Column(name = "ORDER_ID")
    @Basic
    var orderId: String? = null

    @Column(name = "SAMPLE_NUMBER")
    @Basic
    var sampleNumber: String? = null

    @Column(name = "TEST")
    @Basic
    var test: String? = null

    @Column(name = "MATRIX")
    @Basic
    var matrix: String? = null

    @Column(name = "METHOD")
    @Basic
    var method: String? = null

    @Column(name = "TEST_GROUP")
    @Basic
    var testGroup: String? = null

    @Column(name = "PRIORITY")
    @Basic
    var priority: String? = null

    @Column(name = "CURRENT_DEPARTMENT")
    @Basic
    var currentDepartment: String? = null

    @Column(name = "LAST_DEPARTMENT")
    @Basic
    var lastDepartment: String? = null

    @Column(name = "RE_DEPARTMENT")
    @Basic
    var reDepartment: String? = null

    @Column(name = "TEST_PRICE")
    @Basic
    var testPrice: String? = null

    @Column(name = "CUSTOMER_TEST_PRICE")
    @Basic
    var customerTestPrice: String? = null

    @Column(name = "DUE_DATE")
    @Basic
    var dueDate: String? = null

    @Column(name = "DUE_DATE_FLAG")
    @Basic
    var dueDateFlag: String? = null

    @Column(name = "PREP_DUE_DATE")
    @Basic
    var prepDueDate: String? = null

    @Column(name = "PREP_METHOD")
    @Basic
    var prepMethod: String? = null

    @Column(name = "ANALYSIS_DUE_DATE")
    @Basic
    var analysisDueDate: String? = null

    @Column(name = "ANALYSIS_TIME")
    @Basic
    var analysisTime: String? = null

    @Column(name = "ANALYSIS_EMPLOYEE")
    @Basic
    var analysisEmployee: String? = null

    @Column(name = "KEEP_TEST")
    @Basic
    var keepTest: String? = null

    @Column(name = "CANCELLED")
    @Basic
    var cancelled: String? = null

    @Column(name = "HAS_RESULTS")
    @Basic
    var hasResults: String? = null

    @Column(name = "SUPPLY_RECONCILED")
    @Basic
    var supplyReconciled: String? = null

    @Column(name = "CUSTOM_PARAMS")
    @Basic
    var customParams: String? = null

    @Column(name = "PRESERVATIVE")
    @Basic
    var preservative: String? = null

    @Column(name = "BOTTLE_TYPE")
    @Basic
    var bottleType: String? = null

    @Column(name = "STORAGE_LOCATION")
    @Basic
    var storageLocation: String? = null

    @Column(name = "SAMPLE_DETAILS_USER1")
    @Basic
    var sampleDetailsUser1: String? = null

    @Column(name = "SAMPLE_DETAILS_USER2")
    @Basic
    var sampleDetailsUser2: String? = null

    @Column(name = "SAMPLE_DETAILS_USER3")
    @Basic
    var sampleDetailsUser3: String? = null

    @Column(name = "SAMPLE_DETAILS_USER4")
    @Basic
    var sampleDetailsUser4: String? = null

    @Column(name = "TS")
    @Basic
    var ts: String? = null

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
        val that = other as QaSampleLabTestParametersEntity
        return id == that.id &&
                orderId == that.orderId &&
                sampleNumber == that.sampleNumber &&
                test == that.test &&
                matrix == that.matrix &&
                method == that.method &&
                testGroup == that.testGroup &&
                priority == that.priority &&
                currentDepartment == that.currentDepartment &&
                lastDepartment == that.lastDepartment &&
                reDepartment == that.reDepartment &&
                testPrice == that.testPrice &&
                customerTestPrice == that.customerTestPrice &&
                dueDate == that.dueDate &&
                dueDateFlag == that.dueDateFlag &&
                prepDueDate == that.prepDueDate &&
                prepMethod == that.prepMethod &&
                analysisDueDate == that.analysisDueDate &&
                analysisTime == that.analysisTime &&
                analysisEmployee == that.analysisEmployee &&
                keepTest == that.keepTest &&
                cancelled == that.cancelled &&
                hasResults == that.hasResults &&
                supplyReconciled == that.supplyReconciled &&
                customParams == that.customParams &&
                preservative == that.preservative &&
                bottleType == that.bottleType &&
                storageLocation == that.storageLocation &&
                sampleDetailsUser1 == that.sampleDetailsUser1 &&
                sampleDetailsUser2 == that.sampleDetailsUser2 &&
                sampleDetailsUser3 == that.sampleDetailsUser3 &&
                sampleDetailsUser4 == that.sampleDetailsUser4 &&
                ts == that.ts &&
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
            orderId,
            sampleNumber,
            test,
            matrix,
            method,
            testGroup,
            priority,
            currentDepartment,
            lastDepartment,
            reDepartment,
            testPrice,
            customerTestPrice,
            dueDate,
            dueDateFlag,
            prepDueDate,
            prepMethod,
            analysisDueDate,
            analysisTime,
            analysisEmployee,
            keepTest,
            cancelled,
            hasResults,
            supplyReconciled,
            customParams,
            preservative,
            bottleType,
            storageLocation,
            sampleDetailsUser1,
            sampleDetailsUser2,
            sampleDetailsUser3,
            sampleDetailsUser4,
            ts,
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