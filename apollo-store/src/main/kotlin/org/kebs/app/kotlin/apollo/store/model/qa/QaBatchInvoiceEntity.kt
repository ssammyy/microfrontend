package org.kebs.app.kotlin.apollo.store.model.qa

import java.io.Serializable
import java.math.BigDecimal
import java.sql.Date
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_QA_BATCH_INVOICE")
class QaBatchInvoiceEntity : Serializable {
    @Column(name = "ID")
    @Id
    @SequenceGenerator(
        name = "DAT_KEBS_QA_BATCH_INVOICE_SEQ_GEN",
        sequenceName = "DAT_KEBS_QA_BATCH_INVOICE_SEQ",
        allocationSize = 1
    )
    @GeneratedValue(generator = "DAT_KEBS_QA_BATCH_INVOICE_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    var id: Long? = 0

    @Column(name = "INVOICE_NUMBER")
    @Basic
    var invoiceNumber: String? = null

    @Column(name = "SAGE_INVOICE_NUMBER")
    @Basic
    var sageInvoiceNumber: String? = null

    @Column(name = "TOTAL_AMOUNT")
    @Basic
    var totalAmount: BigDecimal? = BigDecimal.ZERO

    @Column(name = "TOTAL_TAX_AMOUNT")
    @Basic
    var totalTaxAmount: BigDecimal? = BigDecimal.ZERO

    @Column(name = "CREATION_DATE")
    @Basic
    var creationDate: Date? = null

    @Column(name = "USER_ID")
    @Basic
    var userId: Long? = null

    @Column(name = "PLANT_ID")
    @Basic
    var plantId: Long? = null

    @Column(name = "INVOICE_BATCH_NUMBER_ID")
    @Basic
    var invoiceBatchNumberId: Long? = null

    @Column(name = "DESCRIPTION")
    @Basic
    var description: String? = null

    @Column(name = "PAID_DATE")
    @Basic
    var paidDate: Timestamp? = null

    @Column(name = "STATUS")
    @Basic
    var status: Int? = null

    @Column(name = "PAID_STATUS")
    @Basic
    var paidStatus: Int? = null

    @Column(name = "SUBMITTED_STATUS")
    @Basic
    var submittedStatus: Int? = null

    @Column(name = "RECEIPT_NO")
    @Basic
    var receiptNo: String? = null

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
