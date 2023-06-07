package org.kebs.app.kotlin.apollo.store.model.pvc

import com.fasterxml.jackson.annotation.JsonFormat
import java.io.Serializable
import java.math.BigDecimal
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

    @Column(name = "PARTNER_ID", nullable = true)
    @Basic
    var partnerId: Long? = null

    @Column(name = "RECORD_ID", nullable = true)
    @Basic
    var recordId: Long? = null

    @Column(name = "CERT_TYPE", nullable = true, length = 50)
    @Basic
    var certType: String? = null

    @Column(name = "CERT_NUMBER", nullable = true, length = 80)
    @Basic
    var certNumber: String? = null

    @Column(name = "RECORD_YEAR_MONTH", nullable = true, precision = 0)
    @Basic
    var recordYearMonth: String? = null

    @NotEmpty(message = "Required field")
    @Column(name = "UCR_NUMBER", nullable = false, length = 80)
    @Basic
    var ucrNumber: String? = null

    @NotNull(message = "Required field")
    @Column(name = "RFC_DATE", nullable = false)
    @Basic
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    var rfcDate: Timestamp? = null

    @NotNull(message = "Required field")
    @Column(name = "DATE_OF_INSPECTION", nullable = false)
    @Basic
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    var dateOfInspection: Timestamp? = null

    @Column(name = "DOC_ISSUE_DATE", nullable = true)
    @Basic
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    var docIssueDate: Timestamp? = null

    @Column(name = "REQUEST_DATE_OF_INSPECTION", nullable = false)
    @Basic
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    var requestDateOfInspection: Timestamp? = null

    @Column(name = "DOC_CONFIRMATION_DATE", nullable = true)
    @Basic
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    var docConfirmationDate: Timestamp? = null

    @Column(name = "RFC_TO_INSPECTION_DAYS", nullable = false)
    @Basic
    var rfcToInspectionDays: Long = 0

    @Column(name = "RFC_TO_INSPECTION_VIOLATION")
    @Basic
    var rfcToInspectionViolation: Boolean = false

    @Column(name = "INSPECTION_TO_ISSUANCE_DAYS", nullable = false)
    @Basic
    var inspectionToIssuanceDays: Long = 0

    @NotNull(message = "Required field")
    @Column(name = "INSPECTION_TO_ISSUANCE_VIOLATION")
    @Basic
    var inspectionToIssuanceViolation: Boolean = false

    @Column(name = "RFC_TO_ISSUANCE_DAYS", nullable = false)
    @Basic
    var rfcToIssuanceDays: Long = 0

    @NotNull(message = "Required field")
    @Column(name = "RFC_TO_ISSUANCE_VIOLATION")
    @Basic
    var rfcToIssuanceViolation: Boolean = false

    @NotNull(message = "Required field")
    @Column(name = "ACC_DOCUMENTS_TO_ISSUANCE_DAYS", nullable = false)
    @Basic
    var accDocumentsToIssuanceDays: Long = 0

    @NotNull(message = "Required field")
    @Column(name = "ACC_DOCUMENTS_TO_ISSUANCE_VIOLATION")
    @Basic
    var accDocumentsToIssuanceViolation: Boolean = false

    @NotNull(message = "Required field")
    @Column(name = "PAYMENT_TO_ISSUANCE_DAYS", nullable = false)
    @Basic
    var paymentToIssuanceDays: Long = 0

    @NotNull(message = "Required field")
    @Column(name = "PAYMENT_TO_ISSUANCE_VIOLATION")
    @Basic
    var paymentToIssuanceViolation: Boolean = false

    @NotNull(message = "Required field")
    @Column(name = "FINAL_DOCUMENTS_TO_ISSUANCE_DAYS", nullable = false)
    @Basic
    var finalDocumentsToIssuanceDays: Long = 0

    @NotNull(message = "Required field")
    @Column(name = "FINAL_DOCUMENTS_TO_ISSUANCE_VIOLATION")
    @Basic
    var finalDocumentsToIssuanceViolation: Boolean = false

    @Column(name = "ROUTE", nullable = true, length = 10)
    @Basic
    var route: String? = null

    @Column(name = "ACC_DOCUMENTS_SUBMISSION_DATE", nullable = true)
    @Basic
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    var accDocumentsSubmissionDate: Timestamp? = null

    @Column(name = "FINAL_DOCUMENTS_SUBMISSION_DATE", nullable = true)
    @Basic
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    var finalDocumentsSubmissionDate: Timestamp? = null

    @Column(name = "PAYMENT_DATE", nullable = true)
    @Basic
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    var paymentDate: Timestamp? = null

    @Column(name = "ROYALTY_VALUE", precision = 15, scale = 2)
    @Basic
    var royaltyValue: BigDecimal? = null

    @Column(name = "APPLICABLE_PENALTY", precision = 15, scale = 2)
    @Basic
    var applicablePenalty: BigDecimal? = null

    @Column(name = "CURRENCY_CODE")
    @Basic
    var currencyCode: String? = null

    @Column(name = "MONITORING_ID", nullable = true)
    @Basic
    var monitoringId: Long? = null

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
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    var createdOn: Timestamp? = null

    @Column(name = "MODIFIED_BY", nullable = true, length = 100)
    @Basic
    var modifiedBy: String? = null

    @Column(name = "MODIFIED_ON", nullable = true)
    @Basic
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    var modifiedOn: Timestamp? = null

    @Column(name = "DELETE_BY", nullable = true, length = 100)
    @Basic
    var deleteBy: String? = null

    @Column(name = "DELETED_ON", nullable = true)
    @Basic
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    var deletedOn: Timestamp? = null

    @Column(name = "RISK_STATUS")
    @Basic
    var riskStatus: Int? = null


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as PvocTimelinesDataEntity
        return id == that.id && rfcToInspectionDays == that.rfcToInspectionDays && inspectionToIssuanceDays == that.inspectionToIssuanceDays && rfcToIssuanceDays == that.rfcToIssuanceDays && accDocumentsToIssuanceDays == that.accDocumentsToIssuanceDays && paymentToIssuanceDays == that.paymentToIssuanceDays && finalDocumentsToIssuanceDays == that.finalDocumentsToIssuanceDays &&
                certNumber == that.certNumber &&
                ucrNumber == that.ucrNumber &&
                rfcDate == that.rfcDate &&
                dateOfInspection == that.dateOfInspection &&
                docIssueDate == that.docIssueDate &&
                requestDateOfInspection == that.requestDateOfInspection &&
                docConfirmationDate == that.docConfirmationDate &&
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
                deletedOn == that.deletedOn
    }

    override fun hashCode(): Int {
        return Objects.hash(
                id,
                certNumber, certType,
                ucrNumber,
                rfcDate,
                dateOfInspection,
                docIssueDate,
                requestDateOfInspection,
                docConfirmationDate,
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

