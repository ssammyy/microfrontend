package org.kebs.app.kotlin.apollo.store.model

import com.fasterxml.jackson.annotation.JsonFormat
import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@Entity
@Table(name = "DAT_KEBS_PVOC_TIMELINES_DATA")
class PvocTimelinesDataEntity : Serializable {
    @Column(name = "ID", nullable = false, precision = 0)
    @Id
    @SequenceGenerator(name = "DAT_KEBS_PVOC_TIMELINES_DATA_SEQ_GEN", sequenceName = "DAT_KEBS_PVOC_TIMELINES_DATA_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_PVOC_TIMELINES_DATA_SEQ_GEN", strategy = GenerationType.SEQUENCE)

    var id: Long = 0

    @Column(name = "COC_NUMBER", nullable = true, length = 50)
    @Basic
    var cocNumber: String? = null

    @NotEmpty(message = "Required field")
    @Column(name = "UCR_NUMBER", nullable = false, length = 50)
    @Basic
    var ucrNumber: String? = null

    @NotNull(message = "Required field")
    @Column(name = "RFC_DATE", nullable = false)
    @Basic
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    var rfcDate: Timestamp? = null

    @NotNull(message = "Required field")
    @Column(name = "DATE_OF_INSPECTION", nullable = false)
    @Basic
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    var dateOfInspection: Timestamp? = null

    @Column(name = "COC_ISSUE_DATE", nullable = true)
    @Basic
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    var cocIssueDate: Timestamp? = null

    @NotNull(message = "Required field")
    @Column(name = "REQUEST_DATE_OF_INSPECTION", nullable = false)
    @Basic
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    var requestDateOfInspection: Timestamp? = null

    @NotNull(message = "Required field")
    @Column(name = "COC_CONFIRMATION_DATE", nullable = false)
    @Basic
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    var cocConfirmationDate: Timestamp? = null

    @NotNull(message = "Required field")
    @Column(name = "RFC_TO_INSPECTION_DAYS", nullable = false, precision = 0)
    @Basic
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    var rfcToInspectionDays: Long = 0

    @NotNull(message = "Required field")
    @Column(name = "INSPECTION_TO_ISSUANCE_DAYS", nullable = false, precision = 0)
    @Basic
    var inspectionToIssuanceDays: Long = 0

    @NotNull(message = "Required field")
    @Column(name = "RFC_TO_ISSUANCE_DAYS", nullable = false, precision = 0)
    @Basic
    var rfcToIssuanceDays: Long = 0

    @NotNull(message = "Required field")
    @Column(name = "ACC_DOCUMENTS_TO_ISSUANCE_DAYS", nullable = false, precision = 0)
    @Basic
    var accDocumentsToIssuanceDays: Long = 0

    @NotNull(message = "Required field")
    @Column(name = "PAYMENT_TO_ISSUANCE_DAYS", nullable = false, precision = 0)
    @Basic
    var paymentToIssuanceDays: Long = 0

    @NotNull(message = "Required field")
    @Column(name = "FINAL_DOCUMENTS_TO_ISSUANCE_DAYS", nullable = false, precision = 0)
    @Basic
    var finalDocumentsToIssuanceDays: Long = 0

    @Column(name = "ROUTE", nullable = true, length = 10)
    @Basic
    var route: String? = null

    @Column(name = "ACC_DOCUMENTS_SUBMISSION_DATE", nullable = true)
    @Basic
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    var accDocumentsSubmissionDate: Timestamp? = null

    @Column(name = "FINAL_DOCUMENTS_SUBMISSION_DATE", nullable = true)
    @Basic
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    var finalDocumentsSubmissionDate: Timestamp? = null

    @Column(name = "PAYMENT_DATE", nullable = true)
    @Basic
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    var paymentDate: Timestamp? = null

    @Column(name = "STATUS", nullable = true, precision = 0)
    @Basic
    var status: Long? = null

    @Column(name = "VAR_FIELD_1", nullable = true, length = 350)
    @Basic
    var varField1: String? = null

    @Column(name = "VAR_FIELD_2", nullable = true, length = 350)
    @Basic
    var varField2: String? = null

    @Column(name = "VAR_FIELD_3", nullable = true, length = 350)
    @Basic
    var varField3: String? = null

    @Column(name = "VAR_FIELD_4", nullable = true, length = 350)
    @Basic
    var varField4: String? = null

    @Column(name = "VAR_FIELD_5", nullable = true, length = 350)
    @Basic
    var varField5: String? = null

    @Column(name = "VAR_FIELD_6", nullable = true, length = 350)
    @Basic
    var varField6: String? = null

    @Column(name = "VAR_FIELD_7", nullable = true, length = 350)
    @Basic
    var varField7: String? = null

    @Column(name = "VAR_FIELD_8", nullable = true, length = 350)
    @Basic
    var varField8: String? = null

    @Column(name = "VAR_FIELD_9", nullable = true, length = 350)
    @Basic
    var varField9: String? = null

    @Column(name = "VAR_FIELD_10", nullable = true, length = 350)
    @Basic
    var varField10: String? = null

    @Column(name = "CREATED_BY", nullable = true, length = 100)
    @Basic
    var createdBy: String? = null

    @Column(name = "CREATED_ON", nullable = true)
    @Basic
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    var createdOn: Timestamp? = null

    @Column(name = "MODIFIED_BY", nullable = true, length = 100)
    @Basic
    var modifiedBy: String? = null

    @Column(name = "MODIFIED_ON", nullable = true)
    @Basic
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    var modifiedOn: Timestamp? = null

    @Column(name = "DELETE_BY", nullable = true, length = 100)
    @Basic
    var deleteBy: String? = null

    @Column(name = "DELETED_ON", nullable = true)
    @Basic
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    var deletedOn: Timestamp? = null

    @Column(name = "HOD_STATUS", nullable = true)
    @Basic
    var hodStatus: String? = null

    @Column(name = "MPVOC_AGENT", nullable = true)
    @Basic
    var mpvocAgent: Long? = null

    @Column(name = "PVOC_MONIT_STATUS")
    @Basic
    var pvocMonitStatus: Int? = null

    @Column(name = "PVOC_MONIT_STARTED_ON")
    @Basic
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    var pvocMonitStartedOn: Timestamp? = null

    @Column(name = "PVOC_MONIT_COMPLETED_ON")
    @Basic
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    var pvocMonitCompletedOn: Timestamp? = null

    @Column(name = "PVOC_MONIT_PROCESS_INSTANCE_ID")
    @Basic
    var pvocMonitProcessInstanceId: String? = null

    @Column(name = "RISK_STATUS")
    @Basic
    var riskStatus: Int? = null


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as PvocTimelinesDataEntity
        return id == that.id && rfcToInspectionDays == that.rfcToInspectionDays && inspectionToIssuanceDays == that.inspectionToIssuanceDays && rfcToIssuanceDays == that.rfcToIssuanceDays && accDocumentsToIssuanceDays == that.accDocumentsToIssuanceDays && paymentToIssuanceDays == that.paymentToIssuanceDays && finalDocumentsToIssuanceDays == that.finalDocumentsToIssuanceDays &&
                cocNumber == that.cocNumber &&
                ucrNumber == that.ucrNumber &&
                rfcDate == that.rfcDate &&
                dateOfInspection == that.dateOfInspection &&
                cocIssueDate == that.cocIssueDate &&
                requestDateOfInspection == that.requestDateOfInspection &&
                cocConfirmationDate == that.cocConfirmationDate &&
                route == that.route &&
                accDocumentsSubmissionDate == that.accDocumentsSubmissionDate &&
                finalDocumentsSubmissionDate == that.finalDocumentsSubmissionDate &&
                paymentDate == that.paymentDate &&
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
                hodStatus == that.hodStatus &&
                mpvocAgent == that.mpvocAgent &&
                deletedOn == that.deletedOn
    }

    override fun hashCode(): Int {
        return Objects.hash(
            id,
            cocNumber,
            ucrNumber,
            rfcDate,
            dateOfInspection,
            cocIssueDate,
            requestDateOfInspection,
            cocConfirmationDate,
            rfcToInspectionDays,
            inspectionToIssuanceDays,
            rfcToIssuanceDays,
            accDocumentsToIssuanceDays,
            paymentToIssuanceDays,
            finalDocumentsToIssuanceDays,
            route,
            accDocumentsSubmissionDate,
            finalDocumentsSubmissionDate,
            paymentDate,
            hodStatus,
            mpvocAgent,
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

