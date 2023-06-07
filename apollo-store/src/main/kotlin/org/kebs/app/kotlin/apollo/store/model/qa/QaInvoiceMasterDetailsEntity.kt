package org.kebs.app.kotlin.apollo.store.model.qa

import java.io.Serializable
import java.math.BigDecimal
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_QA_INVOICE_MASTER_DETAILS")
class QaInvoiceMasterDetailsEntity : Serializable {
    @Column(name = "ID")
    @Id
    @SequenceGenerator(
        name = "DAT_KEBS_QA_INVOICE_MASTER_DETAILS_SEQ_GEN",
        sequenceName = "DAT_KEBS_QA_INVOICE_MASTER_DETAILS_SEQ",
        allocationSize = 1
    )
    @GeneratedValue(generator = "DAT_KEBS_QA_INVOICE_MASTER_DETAILS_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    var id: Long = 0

    @Column(name = "INVOICE_REF")
    @Basic
    var invoiceRef: String? = null

    @Column(name = "USER_ID")
    @Basic
    var userId: Long? = null

    @Column(name = "PERMIT_REF_NUMBER")
    @Basic
    var permitRefNumber: String? = null

    @Column(name = "RECEIPT_NO")
    @Basic
    var receiptNo: String? = null

    @Column(name = "BATCH_INVOICE_NO")
    @Basic
    var batchInvoiceNo: Long? = null

    @Column(name = "PAYMENT_STATUS")
    @Basic
    var paymentStatus: Int? = 0

    @Column(name = "GENERATED_DATE")
    @Basic
    var generatedDate: Timestamp? = null

    @Column(name = "ITEM_COUNT")
    @Basic
    var itemCount: Int? = null

    @Column(name = "TOTAL_AMOUNT")
    @Basic
    var totalAmount: BigDecimal? = null

    @Column(name = "TAX_AMOUNT")
    @Basic
    var taxAmount: BigDecimal? = null

    @Column(name = "SUB_TOTAL_BEFORE_TAX")
    @Basic
    var subTotalBeforeTax: BigDecimal? = null

    @Column(name = "AMOUNT_IN_WORDS")
    @Basic
    var amountInWords: String? = null

    @Column(name = "PERMIT_ID")
    @Basic
    var permitId: Long? = null

    @Column(name = "DESCRIPTION")
    @Basic
    var description: String? = null

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
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as QaInvoiceMasterDetailsEntity
        return id == that.id &&
                invoiceRef == that.invoiceRef &&
                taxAmount == that.taxAmount &&
                batchInvoiceNo == that.batchInvoiceNo &&
                subTotalBeforeTax == that.subTotalBeforeTax &&
                userId == that.userId &&
                permitRefNumber == that.permitRefNumber &&
                receiptNo == that.receiptNo &&
                paymentStatus == that.paymentStatus &&
                generatedDate == that.generatedDate && itemCount == that.itemCount && totalAmount == that.totalAmount && amountInWords == that.amountInWords && permitId == that.permitId && description == that.description && status == that.status && varField1 == that.varField1 && varField2 == that.varField2 && varField3 == that.varField3 && varField4 == that.varField4 && varField5 == that.varField5 && varField6 == that.varField6 && varField7 == that.varField7 && varField8 == that.varField8 && varField9 == that.varField9 && varField10 == that.varField10 && createdBy == that.createdBy && createdOn == that.createdOn && modifiedBy == that.modifiedBy && modifiedOn == that.modifiedOn && deleteBy == that.deleteBy && deletedOn == that.deletedOn
    }

    override fun hashCode(): Int {
        return Objects.hash(
            id,
            invoiceRef,
            userId,
            receiptNo,
            permitRefNumber,
            subTotalBeforeTax,
            batchInvoiceNo,
            taxAmount,
            paymentStatus,
            generatedDate,
            itemCount,
            totalAmount,
            amountInWords,
            permitId,
            description,
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