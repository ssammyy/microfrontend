package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_PVOC_TIMELINES")
class PvocTimelinesEntity:Serializable {
    @Column(name = "ID", nullable = false, precision = 0)
    @Id
    var id: Long = 0

    @Column(name = "COC_NUMBER", nullable = true, length = 50)
    @Basic
    var cocNumber: String? = null

    @Column(name = "UCR_NUMBER", nullable = false, length = 50)
    @Basic
    var ucrNumber: String? = null

    @Column(name = "RFC_DATE", nullable = false)
    @Basic
    var rfcDate: Timestamp? = null

    @Column(name = "DATE_OF_INSPECTION", nullable = false)
    @Basic
    var dateOfInspection: Timestamp? = null

    @Column(name = "COC_ISSUE_DATE", nullable = true)
    @Basic
    var cocIssueDate: Timestamp? = null

    @Column(name = "REQUEST_DATE_OF_INSPECTION", nullable = false)
    @Basic
    var requestDateOfInspection: Timestamp? = null

    @Column(name = "COC_CONFIRMATION_DATE", nullable = false)
    @Basic
    var cocConfirmationDate: Timestamp? = null

    @Column(name = "RFC_TO_INSPECTION_DAYS", nullable = false, precision = 0)
    @Basic
    var rfcToInspectionDays: Long = 0

    @Column(name = "INSPECTION_TO_ISSUANCE_DAYS", nullable = false, precision = 0)
    @Basic
    var inspectionToIssuanceDays: Long = 0

    @Column(name = "RFC_TO_ISSUANCE_DAYS", nullable = false, precision = 0)
    @Basic
    var rfcToIssuanceDays: Long = 0

    @Column(name = "ACC_DOCUMENTS_TO_ISSUANCE_DAYS", nullable = false, precision = 0)
    @Basic
    var accDocumentsToIssuanceDays: Long = 0

    @Column(name = "PAYMENT_TO_ISSUANCE_DAYS", nullable = false, precision = 0)
    @Basic
    var paymentToIssuanceDays: Long = 0

    @Column(name = "FINAL_DOCUMENTS_TO_ISSUANCE_DAYS", nullable = false, precision = 0)
    @Basic
    var finalDocumentsToIssuanceDays: Long = 0

    @Column(name = "ROUTE", nullable = true, length = 10)
    @Basic
    var route: String? = null

    @Column(name = "ACC_DOCUMENTS_SUBMISSION_DATE", nullable = true)
    @Basic
    var accDocumentsSubmissionDate: Timestamp? = null

    @Column(name = "FINAL_DOCUMENTS_SUBMISSION_DATE", nullable = true)
    @Basic
    var finalDocumentsSubmissionDate: Timestamp? = null

    @Column(name = "PAYMENT_DATE", nullable = true)
    @Basic
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
    var createdOn: Timestamp? = null

    @Column(name = "MODIFIED_BY", nullable = true, length = 100)
    @Basic
    var modifiedBy: String? = null

    @Column(name = "MODIFIED_ON", nullable = true)
    @Basic
    var modifiedOn: Timestamp? = null

    @Column(name = "DELETE_BY", nullable = true, length = 100)
    @Basic
    var deleteBy: String? = null

    @Column(name = "DELETED_ON", nullable = true)
    @Basic
    var deletedOn: Timestamp? = null

    @Column(name = "PVOC_MONIT_STATUS")
    @Basic
    var pvocMonitStatus: Int? = null

    @Column(name = "PVOC_MONIT_STARTED_ON")
    @Basic
    var pvocMonitStartedOn: Timestamp? = null

    @Column(name = "PVOC_MONIT_COMPLETED_ON")
    @Basic
    var pvocMonitCompletedOn: Timestamp? = null

    @Column(name = "PVOC_MONIT_PROCESS_INSTANCE_ID")
    @Basic
    var pvocMonitProcessInstanceId: String? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as PvocTimelinesEntity
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