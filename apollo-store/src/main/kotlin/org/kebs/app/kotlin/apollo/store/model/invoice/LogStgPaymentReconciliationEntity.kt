package org.kebs.app.kotlin.apollo.store.model.invoice

import java.io.Serializable
import java.sql.Date
import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name = "LOG_STG_PAYMENT_RECONCILIATION")
class LogStgPaymentReconciliationEntity : Serializable {
    @Column(name = "ID")
    @SequenceGenerator(name = "LOG_STG_PAYMENT_RECONCILIATION_SEQ_GEN", sequenceName = "LOG_STG_PAYMENT_RECONCILIATION_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "LOG_STG_PAYMENT_RECONCILIATION_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long = 0

    @Basic
    @Column(name = "PAYMENT_RECONCILIATION_ID")
    var paymentReconciliationId: Long? = null

    @Basic
    @Column(name = "INVOICE_ID")
    var invoiceId: Long? = null

    @Basic
    @Column(name = "ACCOUNT_NAME")
    var accountName: String? = null

    @Basic
    @Column(name = "ACCOUNT_NUMBER")
    var accountNumber: String? = null

    @Basic
    @Column(name = "CURRENCY")
    var currency: String? = null

    @Basic
    @Column(name = "STATUS_CODE")
    var statusCode: String? = null

    @Basic
    @Column(name = "STATUS_DESCRIPTION")
    var statusDescription: String? = null

    @Basic
    @Column(name = "ADDITIONAL_INFORMATION")
    var additionalInformation: String? = null

    @Basic
    @Column(name = "INVOICE_AMOUNT")
    var invoiceAmount: java.math.BigDecimal? = null

    @Basic
    @Column(name = "PAID_AMOUNT")
    var paidAmount: java.math.BigDecimal? = null

    @Basic
    @Column(name = "OUTSTANDING_AMOUNT")
    var outstandingAmount: java.math.BigDecimal? = null

    @Basic
    @Column(name = "TRANSACTION_ID")
    var transactionId: String? = null

    @Basic
    @Column(name = "TRANSACTION_DATE")
    var transactionDate: Date? = null

    @Basic
    @Column(name = "CUSTOMER_NAME")
    var customerName: String? = null

    @Basic
    @Column(name = "PAYMENT_SOURCE")
    var paymentSource: String? = null

    @Basic
    @Column(name = "EXTRAS")
    var extras: String? = null

    @Basic
    @Column(name = "INVOICE_DATE")
    var invoiceDate: Date? = null

    @Basic
    @Column(name = "DESCRIPTION")
    var description: String? = null

    @Basic
    @Column(name = "STATUS")
    var status: Int? = null

    @Basic
    @Column(name = "DESCRIPTIONS")
    var descriptions: String? = null

    @Basic
    @Column(name = "VAR_FIELD_1")
    var varField1: String? = null

    @Basic
    @Column(name = "VAR_FIELD_2")
    var varField2: String? = null

    @Basic
    @Column(name = "VAR_FIELD_3")
    var varField3: String? = null

    @Basic
    @Column(name = "VAR_FIELD_4")
    var varField4: String? = null

    @Basic
    @Column(name = "VAR_FIELD_5")
    var varField5: String? = null

    @Basic
    @Column(name = "VAR_FIELD_6")
    var varField6: String? = null

    @Basic
    @Column(name = "VAR_FIELD_7")
    var varField7: String? = null

    @Basic
    @Column(name = "VAR_FIELD_8")
    var varField8: String? = null

    @Basic
    @Column(name = "VAR_FIELD_9")
    var varField9: String? = null

    @Basic
    @Column(name = "VAR_FIELD_10")
    var varField10: String? = null

    @Basic
    @Column(name = "CREATED_BY")
    var createdBy: String? = null

    @Basic
    @Column(name = "CREATED_ON")
    var createdOn: Timestamp? = null

    @Basic
    @Column(name = "MODIFIED_BY")
    var modifiedBy: String? = null

    @Basic
    @Column(name = "MODIFIED_ON")
    var modifiedOn: Timestamp? = null

    @Basic
    @Column(name = "UPDATE_BY")
    var updateBy: String? = null

    @Basic
    @Column(name = "UPDATED_ON")
    var updatedOn: Timestamp? = null

    @Basic
    @Column(name = "DELETE_BY")
    var deleteBy: String? = null

    @Basic
    @Column(name = "DELETED_ON")
    var deletedOn: Timestamp? = null

    @Basic
    @Column(name = "VERSION")
    var version: Long? = null

    @Basic
    @Column(name = "REFERENCE_CODE")
    var referenceCode: String? = null

    @Basic
    @Column(name = "ACTUAL_AMOUNT")
    var actualAmount: java.math.BigDecimal? = null

    @Basic
    @Column(name = "PAYMENT_STARTED")
    var paymentStarted: Int? = null

    @Basic
    @Column(name = "SAGE_INVOICE_NUMBER")
    var sageInvoiceNumber: String? = null

    @Basic
    @Column(name = "INVOICE_TAX_AMOUNT")
    var invoiceTaxAmount: java.math.BigDecimal? = null

    @Basic
    @Column(name = "PAYMENT_TRANSACTION_DATE")
    var paymentTransactionDate: Timestamp? = null
}
