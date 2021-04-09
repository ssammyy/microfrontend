package org.kebs.app.kotlin.apollo.store.model.invoice

import java.io.Serializable
import java.sql.Time
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "LOG_STG_PAYMENT_RECONCILIATION")
class LogStgPaymentReconciliationEntity : Serializable {
    @Column(name = "ID")
    @SequenceGenerator(name = "LOG_STG_PAYMENT_RECONCILIATION_SEQ_GEN", sequenceName = "LOG_STG_PAYMENT_RECONCILIATION_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "LOG_STG_PAYMENT_RECONCILIATION_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long = 0

    @Column(name = "INVOICE_ID")
    @Basic
    var invoiceId: Long = 0

    @Column(name = "REFERENCE_CODE")
    @Basic
    var referenceCode: String? = null

    @Column(name = "ACCOUNT_NAME")
    @Basic
    var accountName: String? = null

    @Column(name = "ACCOUNT_NUMBER")
    @Basic
    var accountNumber: String? = null

    @Column(name = "CURRENCY")
    @Basic
    var currency: String? = null

    @Column(name = "STATUS_CODE")
    @Basic
    var statusCode: String? = null

    @Column(name = "STATUS_DESCRIPTION")
    @Basic
    var statusDescription: String? = null

    @Column(name = "ADDITIONAL_INFORMATION")
    @Basic
    var additionalInformation: String? = null

    @Column(name = "INVOICE_AMOUNT")
    @Basic
    var invoiceAmount: Long = 0

    @Column(name = "PAID_AMOUNT")
    @Basic
    var paidAmount: Long = 0

    @Column(name = "OUTSTANDING_AMOUNT")
    @Basic
    var outstandingAmount: Long = 0

    @Column(name = "TRANSACTION_ID")
    @Basic
    var transactionId: String? = null

    @Column(name = "TRANSACTION_DATE")
    @Basic
    var transactionDate: Time? = null

    @Column(name = "CUSTOMER_NAME")
    @Basic
    var customerName: String? = null

    @Column(name = "PAYMENT_SOURCE")
    @Basic
    var paymentSource: String? = null

    @Column(name = "EXTRAS")
    @Basic
    var extras: String? = null

    @Column(name = "INVOICE_DATE")
    @Basic
    var invoiceDate: Time? = null

    @Column(name = "DESCRIPTION")
    @Basic
    var description: String? = null

    @Column(name = "STATUS")
    @Basic
    var status: Long = 0

    @Column(name = "DESCRIPTIONS")
    @Basic
    var descriptions: String? = null

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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as LogStgPaymentReconciliationEntity
        return id == that.id && invoiceId == that.invoiceId && invoiceAmount == that.invoiceAmount && paidAmount == that.paidAmount && outstandingAmount == that.outstandingAmount && status == that.status &&
                referenceCode == that.referenceCode &&
                accountName == that.accountName &&
                accountNumber == that.accountNumber &&
                currency == that.currency &&
                statusCode == that.statusCode &&
                statusDescription == that.statusDescription &&
                additionalInformation == that.additionalInformation &&
                transactionId == that.transactionId &&
                transactionDate == that.transactionDate &&
                customerName == that.customerName &&
                paymentSource == that.paymentSource &&
                extras == that.extras &&
                invoiceDate == that.invoiceDate &&
                description == that.description &&
                descriptions == that.descriptions &&
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
                updateBy == that.updateBy &&
                updatedOn == that.updatedOn &&
                deleteBy == that.deleteBy &&
                deletedOn == that.deletedOn &&
                version == that.version
    }

    override fun hashCode(): Int {
        return Objects.hash(id, invoiceId, referenceCode, accountName, accountNumber, currency, statusCode, statusDescription, additionalInformation, invoiceAmount, paidAmount, outstandingAmount, transactionId, transactionDate, customerName, paymentSource, extras, invoiceDate, description, status, descriptions, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, modifiedBy, modifiedOn, updateBy, updatedOn, deleteBy, deletedOn, version)
    }
}