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

    @Column(name = "SAGE_INVOICE_NUMBER")
    @Basic
    var sageInvoiceNumber: String? = null

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

    @Column(name = "INVOICE_TAX_AMOUNT")
    @Basic
    var invoiceTaxAmount: BigDecimal? = null

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

    @Basic
    @Column(name = "VERSION")
    var version: Long? = null


    @Basic
    @Column(name = "PAYMENT_STARTED")
    var paymentStarted: Int? = null

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

}
