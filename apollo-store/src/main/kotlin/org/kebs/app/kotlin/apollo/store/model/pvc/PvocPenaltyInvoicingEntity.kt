package org.kebs.app.kotlin.apollo.store.model.pvc

import java.io.Serializable
import java.sql.Date
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_PVOC_PENALTY_INVOICING")
class PvocPenaltyInvoicingEntity : Serializable {
    @Column(name = "ID")
    @Id
    @SequenceGenerator(name = "DAT_KEBS_PVOC_PENALTY_INVOICING_SEQ_GEN", sequenceName = "DAT_KEBS_PVOC_PENALTY_INVOICING_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "DAT_KEBS_PVOC_PENALTY_INVOICING_SEQ_GEN", strategy = GenerationType.SEQUENCE)
    var id: Long = 0

    @Column(name = "INVOICE_NUMBER")
    @Basic
    var invoiceNumber: String? = null
    @Column(name = "SOLD_TO")
    @Basic
    var soldTo: String? = null

    @Column(name = "INVOICE_DATE")
    @Basic
    var invoiceDate: Date? = null
    @Column(name = "ORDER_DATE")
    @Basic
    var orderDate: Date? = null
    @Column(name = "ORDER_NUMBER")
    @Basic
    var orderNumber: String? = null
    @Column(name = "CUSTOMER_NUMBER")
    @Basic
    var customerNumber: String? = null
    @Column(name = "PO_NUMBER")
    @Basic
    var poNumber: String? = null
    @Column(name = "SHIP_VIA")
    @Basic
    var shipVia: String? = null
    @Column(name = "TERMS_CODE")
    @Basic
    var termsCode: String? = null
    @Column(name = "DESCRIPTION")
    @Basic
    var description: String? = null
    @Column(name = "DUE_DATE")
    @Basic
    var dueDate: Date? = null
    @Column(name = "PENALTY_DUE")
    @Basic
    var penaltyDue: Long = 0
    @Column(name = "UNIT_PRICE")
    @Basic
    var unitPrice: Long = 0
    @Column(name = "UNIT_OF_MEASURE")
    @Basic
    var unitOfMeasure: String? = null
    @Column(name = "CURRENCY")
    @Basic
    var currency: String? = null
    @Column(name = "SUBTOTAL_BEFORE_TAXES")
    @Basic
    var subtotalBeforeTaxes: Long? = null
    @Column(name = "TOTAL_TAXES")
    @Basic
    var totalTaxes: Double = 0.0
    @Column(name = "TOTAL_AMOUNT")
    @Basic
    var totalAmount: Double = 0.0
    @Column(name = "ACCOUNT_NAME")
    @Basic
    var accountName: String? = null
    @Column(name = "BANK_NAME")
    @Basic
    var bankName: String? = null
    @Column(name = "BRANCH")
    @Basic
    var branch: String? = null
    @Column(name = "KEBS_ACCOUNT_NUMBER")
    @Basic
    var kebsAccountNumber: String? = null
    @Column(name = "USD_ACCOUNT_NUMBER")
    @Basic
    var usdAccountNumber: String? = null
    @Column(name = "BANK_CODE")
    @Basic
    var bankCode: Long = 0
    @Column(name = "SWIFT_CODE")
    @Basic
    var swiftCode: String? = null
    @Column(name = "VAT_NUMBER")
    @Basic
    var vatNumber: String? = null
    @Column(name = "PIN_NUMBER")
    @Basic
    var pinNumber: String? = null
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
    @Column(name = "PARTNER")
    @Basic
    var partner: Long? = null
    @Column(name = "PAID_STATUS")
    @Basic
    var paidStatus: Long? = null
    @Column(name = "PAYMENT_DATE")
    @Basic
    var paymentDate: Timestamp? = null
    @Column(name = "COC_ID")
    @Basic
    var cocId: Long? = null

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as PvocPenaltyInvoicingEntity
        return id == that.id && penaltyDue == that.penaltyDue && unitPrice == that.unitPrice && totalTaxes == that.totalTaxes && totalAmount == that.totalAmount && bankCode == that.bankCode &&
                invoiceNumber == that.invoiceNumber &&
                soldTo == that.soldTo &&
                invoiceDate == that.invoiceDate &&
                orderDate == that.orderDate &&
                orderNumber == that.orderNumber &&
                customerNumber == that.customerNumber &&
                poNumber == that.poNumber &&
                shipVia == that.shipVia &&
                termsCode == that.termsCode &&
                description == that.description &&
                dueDate == that.dueDate &&
                unitOfMeasure == that.unitOfMeasure &&
                currency == that.currency &&
                subtotalBeforeTaxes == that.subtotalBeforeTaxes &&
                accountName == that.accountName &&
                bankName == that.bankName &&
                branch == that.branch &&
                kebsAccountNumber == that.kebsAccountNumber &&
                usdAccountNumber == that.usdAccountNumber &&
                swiftCode == that.swiftCode &&
                vatNumber == that.vatNumber &&
                pinNumber == that.pinNumber &&
                status == that.status &&
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
                partner == that.partner &&
                paidStatus == that.paidStatus &&
                cocId == that.cocId &&
                paymentDate == that.paymentDate
    }

    override fun hashCode(): Int {
        return Objects.hash(id, invoiceNumber, soldTo, invoiceDate, orderDate, orderNumber, customerNumber, poNumber, shipVia, termsCode, description, dueDate, penaltyDue, unitPrice, unitOfMeasure, currency, subtotalBeforeTaxes, totalTaxes, totalAmount, accountName, bankName, branch, kebsAccountNumber, usdAccountNumber, bankCode, swiftCode, vatNumber, pinNumber, status, varField1, varField2, varField3, varField4, varField5, varField6, varField7, varField8, varField9, varField10, createdBy, createdOn, modifiedBy, modifiedOn, deleteBy, deletedOn, partner, paidStatus,cocId, paymentDate)
    }
}