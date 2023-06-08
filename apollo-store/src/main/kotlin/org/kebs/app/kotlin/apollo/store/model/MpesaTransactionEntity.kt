package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.math.BigDecimal
import java.sql.Date
import java.sql.Time
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_MPESA_TRANSACTION")
class MpesaTransactionEntity : Serializable{
    @Column(name = "ID")
    @SequenceGenerator(name = "DAT_KEBS_MPESA_TRANSACTION_SEQ_GEN", allocationSize = 1, sequenceName = "DAT_KEBS_MPESA_TRANSACTION_SEQ")
    @GeneratedValue(generator = "DAT_KEBS_MPESA_TRANSACTION_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long? = 0

    @Column(name = "AMOUNT")
    @Basic
    var amount: BigDecimal? = null

    @Column(name = "MPESARECEIPTNUMBER")
    @Basic
    var mpesareceiptnumber: String? = null

    @Column(name = "TRANSACTIONDATE")
    @Basic
    var transactiondate: Date? = null

    @Column(name = "PHONENUMBER")
    @Basic
    var phonenumber: String? = null

    @Column(name = "STATUS")
    @Basic
    var status: Int? = null

    @Column(name = "USED_TRANSACTION_REFERENCE")
    @Basic
    var usedTransactionReference: Int? = null

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

    @Column(name = "DELETE_BY")
    @Basic
    var deleteBy: String? = null

    @Column(name = "DELETED_ON")
    @Basic
    var deletedOn: Timestamp? = null

    @Column(name = "MERCHANT_REQUEST_ID")
    @Basic
    var merchantRequestId: String? = null

    @Column(name = "CHECKOUT_REQUEST_ID")
    @Basic
    var checkoutRequestId: String? = null

    @Column(name = "INVOICE_ID")
    @Basic
    var invoiceId: String? = null

    @Column(name = "INVOICE_SOURCE")
    @Basic
    var invoiceSource: String? = null

    @Column(name = "RESULT_CODE")
    @Basic
    var resultCode: String? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as MpesaTransactionEntity
        return id == that.id &&
                amount == that.amount &&
                mpesareceiptnumber == that.mpesareceiptnumber &&
                usedTransactionReference == that.usedTransactionReference &&
                transactiondate == that.transactiondate &&
                phonenumber == that.phonenumber &&
                status == that.status &&
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
                deleteBy == that.deleteBy &&
                deletedOn == that.deletedOn &&
                merchantRequestId == that.merchantRequestId &&
                checkoutRequestId == that.checkoutRequestId &&
                invoiceId == that.invoiceId &&
                invoiceSource == that.invoiceSource
    }

    override fun hashCode(): Int {
        return Objects.hash(id, amount, mpesareceiptnumber, usedTransactionReference, transactiondate, phonenumber, status, descriptions, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn, merchantRequestId, checkoutRequestId, invoiceId, invoiceSource)
    }
}