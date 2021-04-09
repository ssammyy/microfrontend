package org.kebs.app.kotlin.apollo.store.model

import org.kebs.app.kotlin.apollo.store.model.PvocCoiTimelinesDataEntity
import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_PVOC_COI_TIMELINES_DATA")
class PvocCoiTimelinesDataEntity : Serializable {
    @Column(name = "ID")
    @GeneratedValue
    @Id
    var id: Long? = null
        private set

    @Column(name = "COI_NUMBER")
    @Basic
    var coiNumber: String? = null

    @Column(name = "UCR_NUMBER")
    @Basic
    var ucrNumber: String? = null

    @Column(name = "RFC_DATE")
    @Basic
    var rfcDate: Timestamp? = null

    @Column(name = "DATE_OF_INSPECTION")
    @Basic
    var dateOfInspection: Timestamp? = null

    @Column(name = "COI_ISSUE_DATE")
    @Basic
    var coiIssueDate: Timestamp? = null

    @Column(name = "REQUEST_DATE_OF_INSPECTION")
    @Basic
    var requestDateOfInspection: Timestamp? = null

    @Column(name = "COI_CONFIRMATION_DATE")
    @Basic
    var coiConfirmationDate: Timestamp? = null

    @Column(name = "RFC_TO_INSPECTION_DAYS")
    @Basic
    var rfcToInspectionDays: Long = 0

    @Column(name = "INSPECTION_TO_ISSUANCE_DAYS")
    @Basic
    var inspectionToIssuanceDays: Long = 0

    @Column(name = "RFC_TO_ISSUANCE_DAYS")
    @Basic
    var rfcToIssuanceDays: Long = 0

    @Column(name = "ACC_DOCUMENTS_TO_ISSUANCE_DAYS")
    @Basic
    var accDocumentsToIssuanceDays: Long = 0

    @Column(name = "PAYMENT_TO_ISSUANCE_DAYS")
    @Basic
    var paymentToIssuanceDays: Long = 0

    @Column(name = "FINAL_DOCUMENTS_TO_ISSUANCE_DAYS")
    @Basic
    var finalDocumentsToIssuanceDays: Long = 0

    @Column(name = "ACC_DOCUMENTS_SUBMISSION_DATE")
    @Basic
    var accDocumentsSubmissionDate: Timestamp? = null

    @Column(name = "FINAL_DOCUMENTS_SUBMISSION_DATE")
    @Basic
    var finalDocumentsSubmissionDate: Timestamp? = null

    @Column(name = "PAYMENT_DATE")
    @Basic
    var paymentDate: Timestamp? = null

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

    @Column(name = "HOD_STATUS")
    @Basic
    var hodStatus: String? = null

    @Column(name = "PVOC_MONIT_STATUS")
    @Basic
    var pvocMonitStatus: Long? = null

    @Column(name = "PVOC_MONIT_STARTED_ON")
    @Basic
    var pvocMonitStartedOn: Timestamp? = null

    @Column(name = "PVOC_MONIT_COMPLETED_ON")
    @Basic
    var pvocMonitCompletedOn: Timestamp? = null

    @Column(name = "PVOC_MONIT_PROCESS_INSTANCE_ID")
    @Basic
    var pvocMonitProcessInstanceId: String? = null
    fun setId(id: Long) {
        this.id = id
    }

    fun setId(id: Long?) {
        this.id = id
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as PvocCoiTimelinesDataEntity
        return rfcToInspectionDays == that.rfcToInspectionDays && inspectionToIssuanceDays == that.inspectionToIssuanceDays && rfcToIssuanceDays == that.rfcToIssuanceDays && accDocumentsToIssuanceDays == that.accDocumentsToIssuanceDays && paymentToIssuanceDays == that.paymentToIssuanceDays && finalDocumentsToIssuanceDays == that.finalDocumentsToIssuanceDays && id == that.id && coiNumber == that.coiNumber && ucrNumber == that.ucrNumber && rfcDate == that.rfcDate && dateOfInspection == that.dateOfInspection && coiIssueDate == that.coiIssueDate && requestDateOfInspection == that.requestDateOfInspection && coiConfirmationDate == that.coiConfirmationDate && accDocumentsSubmissionDate == that.accDocumentsSubmissionDate && finalDocumentsSubmissionDate == that.finalDocumentsSubmissionDate && paymentDate == that.paymentDate && status == that.status && varField1 == that.varField1 && varField2 == that.varField2 && varField3 == that.varField3 && varField4 == that.varField4 && varField5 == that.varField5 && varField6 == that.varField6 && varField7 == that.varField7 && varField8 == that.varField8 && varField9 == that.varField9 && varField10 == that.varField10 && createdBy == that.createdBy && createdOn == that.createdOn && modifiedBy == that.modifiedBy && modifiedOn == that.modifiedOn && deleteBy == that.deleteBy && deletedOn == that.deletedOn && hodStatus == that.hodStatus && pvocMonitStatus == that.pvocMonitStatus && pvocMonitStartedOn == that.pvocMonitStartedOn && pvocMonitCompletedOn == that.pvocMonitCompletedOn && pvocMonitProcessInstanceId == that.pvocMonitProcessInstanceId
    }

    override fun hashCode(): Int {
        return Objects.hash(
            id,
            coiNumber,
            ucrNumber,
            rfcDate,
            dateOfInspection,
            coiIssueDate,
            requestDateOfInspection,
            coiConfirmationDate,
            rfcToInspectionDays,
            inspectionToIssuanceDays,
            rfcToIssuanceDays,
            accDocumentsToIssuanceDays,
            paymentToIssuanceDays,
            finalDocumentsToIssuanceDays,
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
            deletedOn,
            hodStatus,
            pvocMonitStatus,
            pvocMonitStartedOn,
            pvocMonitCompletedOn,
            pvocMonitProcessInstanceId
        )
    }
}