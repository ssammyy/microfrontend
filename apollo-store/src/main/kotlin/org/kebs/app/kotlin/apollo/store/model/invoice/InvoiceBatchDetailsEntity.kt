package org.kebs.app.kotlin.apollo.store.model.invoice

import java.io.Serializable
import java.math.BigDecimal
import java.sql.Date
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_INVOICE_BATCH_DETAILS")
class InvoiceBatchDetailsEntity : Serializable {
    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_KEBS_INVOICE_BATCH_DETAILS_SEQ_GEN", sequenceName = "DAT_KEBS_INVOICE_BATCH_DETAILS_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_INVOICE_BATCH_DETAILS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long = 0

    @Column(name = "BATCH_NUMBER")
    @Basic
    var batchNumber: String? = null

    @Column(name = "SAGE_INVOICE_NUMBER")
    @Basic
    var sageInvoiceNumber: String? = null

    @Column(name = "TOTAL_AMOUNT")
    @Basic
    var totalAmount: BigDecimal? = null

    @Column(name = "TOTAL_TAX_AMOUNT")
    @Basic
    var totalTaxAmount: BigDecimal? = null

    @Column(name = "DESCRIPTION")
    @Basic
    var description: String? = null

    @Column(name = "TABLE_SOURCE")
    @Basic
    var tableSource: String? = null

    @Column(name = "RECEIPT_NUMBER")
    @Basic
    var receiptNumber: String? = null

    @Column(name = "PAID_AMOUNT")
    @Basic
    var paidAmount: BigDecimal? = null

    @Column(name = "RECEIPT_DATE")
    @Basic
    var receiptDate: Timestamp? = null


    @Column(name = "STATUS")
    @Basic
    var status: Int? = null

    @Column(name = "PAYMENT_STARTED")
    @Basic
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
