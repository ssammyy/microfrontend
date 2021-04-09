package org.kebs.app.kotlin.apollo.store.model

import org.hibernate.annotations.DynamicUpdate
import java.io.Serializable
import java.math.BigDecimal
import java.sql.Date
import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name = "STG_PAYMENT_RECONCILIATION")
@DynamicUpdate
class StagingPaymentReconciliation : Serializable {
    @Column(name = "ID")
    @Id
    @SequenceGenerator(name = "STG_PAYMENT_RECONCILIATION_SEQ_GEN", sequenceName = "STG_PAYMENT_RECONCILIATION_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "STG_PAYMENT_RECONCILIATION_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    var id: Long? = null

    @Column(name = "INVOICE_ID")
    @Basic
    var invoiceId: Long? = null

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

    @Column(name = "TRANSACTION_ID")
    @Basic
    var transactionId: String? = null

    @Column(name = "INVOICE_AMOUNT")
    @Basic
    var invoiceAmount: BigDecimal? = null

    @Column(name = "PAID_AMOUNT")
    @Basic
    var paidAmount: BigDecimal? = null

    @Column(name = "ACTUAL_AMOUNT")
    @Basic
    var actualAmount: BigDecimal? = null

    @Column(name = "OUTSTANDING_AMOUNT")
    @Basic
    var outstandingAmount: BigDecimal? = null

    @Column(name = "TRANSACTION_DATE")
    @Basic
    var transactionDate: Date? = null

    @Column(name = "PAYMENT_TRANSACTION_DATE")
    @Basic
    var paymentTransactionDate: Timestamp? = null

    @Column(name = "INVOICE_DATE")
    @Basic
    var invoiceDate: Date? = null

    @Column(name = "CUSTOMER_NAME")
    @Basic
    var customerName: String? = null

    @Column(name = "PAYMENT_SOURCE")
    @Basic
    var paymentSource: String? = null

    @Column(name = "EXTRAS")
    @Basic
    var extras: String? = null


    @Column(name = "DESCRIPTION")
    @Basic
    var description: String? = null


    @Column(name = "PAYMENT_TABLES_UPDATED_STATUS")
    @Basic
    var paymentTablesUpdatedStatus: Int? = null

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


    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (invoiceId?.hashCode() ?: 0)
        result = 31 * result + (referenceCode?.hashCode() ?: 0)
        result = 31 * result + (accountName?.hashCode() ?: 0)
        result = 31 * result + (accountNumber?.hashCode() ?: 0)
        result = 31 * result + (currency?.hashCode() ?: 0)
        result = 31 * result + (statusCode?.hashCode() ?: 0)
        result = 31 * result + (statusDescription?.hashCode() ?: 0)
        result = 31 * result + (additionalInformation?.hashCode() ?: 0)
        result = 31 * result + (transactionId?.hashCode() ?: 0)
        result = 31 * result + (actualAmount?.hashCode() ?: 0)
        result = 31 * result + (invoiceAmount?.hashCode() ?: 0)
        result = 31 * result + (paidAmount?.hashCode() ?: 0)
        result = 31 * result + (outstandingAmount?.hashCode() ?: 0)
        result = 31 * result + (transactionDate?.hashCode() ?: 0)
        result = 31 * result + (invoiceDate?.hashCode() ?: 0)
        result = 31 * result + (customerName?.hashCode() ?: 0)
        result = 31 * result + (paymentSource?.hashCode() ?: 0)
        result = 31 * result + (extras?.hashCode() ?: 0)
        result = 31 * result + (description?.hashCode() ?: 0)
        result = 31 * result + (status ?: 0)
        result = 31 * result + (varField1?.hashCode() ?: 0)
        result = 31 * result + (varField2?.hashCode() ?: 0)
        result = 31 * result + (varField3?.hashCode() ?: 0)
        result = 31 * result + (varField4?.hashCode() ?: 0)
        result = 31 * result + (varField5?.hashCode() ?: 0)
        result = 31 * result + (varField6?.hashCode() ?: 0)
        result = 31 * result + (varField7?.hashCode() ?: 0)
        result = 31 * result + (varField8?.hashCode() ?: 0)
        result = 31 * result + (varField9?.hashCode() ?: 0)

        result = 31 * result + (varField10?.hashCode() ?: 0)
        result = 31 * result + (createdBy?.hashCode() ?: 0)
        result = 31 * result + (createdOn?.hashCode() ?: 0)
        result = 31 * result + (modifiedBy?.hashCode() ?: 0)
        result = 31 * result + (modifiedOn?.hashCode() ?: 0)
        result = 31 * result + (deleteBy?.hashCode() ?: 0)
        result = 31 * result + (deletedOn?.hashCode() ?: 0)
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StagingPaymentReconciliation

        if (id != other.id) return false
        if (invoiceId != other.invoiceId) return false
        if (referenceCode != other.referenceCode) return false
        if (accountName != other.accountName) return false
        if (accountNumber != other.accountNumber) return false
        if (currency != other.currency) return false
        if (statusCode != other.statusCode) return false
        if (statusDescription != other.statusDescription) return false
        if (additionalInformation != other.additionalInformation) return false
        if (transactionId != other.transactionId) return false
        if (actualAmount != other.actualAmount) return false
        if (invoiceAmount != other.invoiceAmount) return false
        if (paidAmount != other.paidAmount) return false
        if (outstandingAmount != other.outstandingAmount) return false
        if (transactionDate != other.transactionDate) return false
        if (invoiceDate != other.invoiceDate) return false
        if (customerName != other.customerName) return false
        if (paymentSource != other.paymentSource) return false
        if (extras != other.extras) return false
        if (description != other.description) return false
        if (status != other.status) return false
        if (varField1 != other.varField1) return false
        if (varField2 != other.varField2) return false
        if (varField3 != other.varField3) return false
        if (varField4 != other.varField4) return false
        if (varField5 != other.varField5) return false
        if (varField6 != other.varField6) return false
        if (varField7 != other.varField7) return false
        if (varField8 != other.varField8) return false
        if (varField9 != other.varField9) return false
        if (varField10 != other.varField10) return false
        if (createdBy != other.createdBy) return false
        if (createdOn != other.createdOn) return false
        if (modifiedBy != other.modifiedBy) return false
        if (modifiedOn != other.modifiedOn) return false
        if (deleteBy != other.deleteBy) return false
        if (deletedOn != other.deletedOn) return false

        return true
    }


}