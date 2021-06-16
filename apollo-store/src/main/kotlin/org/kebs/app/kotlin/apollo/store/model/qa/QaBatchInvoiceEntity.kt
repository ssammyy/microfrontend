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

    @Column(name = "TOTAL_AMOUNT")
    @Basic
    var totalAmount: BigDecimal? = null

    @Column(name = "CREATION_DATE")
    @Basic
    var creationDate: Date? = null

    @Column(name = "USER_ID")
    @Basic
    var userId: Long? = null

    @Column(name = "PLANT_ID")
    @Basic
    var plantId: Long? = null

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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as QaBatchInvoiceEntity
        return id == that.id && totalAmount == that.totalAmount
                && userId == that.userId
                && plantId == that.plantId
                && creationDate == that.creationDate
                && paidDate == that.paidDate
                && paidStatus == that.paidStatus
                && receiptNo == that.receiptNo && invoiceNumber == that.invoiceNumber && description == that.description && status == that.status && varField1 == that.varField1 && varField2 == that.varField2 && varField3 == that.varField3 && varField4 == that.varField4 && varField5 == that.varField5 && varField6 == that.varField6 && varField7 == that.varField7 && varField8 == that.varField8 && varField9 == that.varField9 && varField10 == that.varField10 && createdBy == that.createdBy && createdOn == that.createdOn && modifiedBy == that.modifiedBy && modifiedOn == that.modifiedOn && deleteBy == that.deleteBy && deletedOn == that.deletedOn
    }

    override fun hashCode(): Int {
        return Objects.hash(
            id,
            paidDate,
            creationDate,
            plantId,
            invoiceNumber,
            userId,
            paidStatus,
            receiptNo,
            totalAmount,
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