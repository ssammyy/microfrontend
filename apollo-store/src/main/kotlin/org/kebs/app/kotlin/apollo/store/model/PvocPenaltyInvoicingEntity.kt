package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.sql.Time
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

    @get:Column(name = "INVOICE_NUMBER")
    @get:Basic
    var invoiceNumber: String? = null
    @get:Column(name = "SOLD_TO")
    @get:Basic
    var soldTo: String? = null
    @get:Column(name = "INVOICE_DATE")
    @get:Basic
    var invoiceDate: Time? = null
    @get:Column(name = "ORDER_DATE")
    @get:Basic
    var orderDate: Timestamp? = null
    @get:Column(name = "ORDER_NUMBER")
    @get:Basic
    var orderNumber: String? = null
    @get:Column(name = "CUSTOMER_NUMBER")
    @get:Basic
    var customerNumber: String? = null
    @get:Column(name = "PO_NUMBER")
    @get:Basic
    var poNumber: String? = null
    @get:Column(name = "SHIP_VIA")
    @get:Basic
    var shipVia: String? = null
    @get:Column(name = "TERMS_CODE")
    @get:Basic
    var termsCode: String? = null
    @get:Column(name = "DESCRIPTION")
    @get:Basic
    var description: String? = null
    @get:Column(name = "DUE_DATE")
    @get:Basic
    var dueDate: Timestamp? = null
    @get:Column(name = "PENALTY_DUE")
    @get:Basic
    var penaltyDue: Long = 0
    @get:Column(name = "UNIT_PRICE")
    @get:Basic
    var unitPrice: Long = 0
    @get:Column(name = "UNIT_OF_MEASURE")
    @get:Basic
    var unitOfMeasure: String? = null
    @get:Column(name = "CURRENCY")
    @get:Basic
    var currency: String? = null
    @get:Column(name = "SUBTOTAL_BEFORE_TAXES")
    @get:Basic
    var subtotalBeforeTaxes: Long? = null
    @get:Column(name = "TOTAL_TAXES")
    @get:Basic
    var totalTaxes: Long = 0
    @get:Column(name = "TOTAL_AMOUNT")
    @get:Basic
    var totalAmount: Long = 0
    @get:Column(name = "ACCOUNT_NAME")
    @get:Basic
    var accountName: String? = null
    @get:Column(name = "BANK_NAME")
    @get:Basic
    var bankName: String? = null
    @get:Column(name = "BRANCH")
    @get:Basic
    var branch: String? = null
    @get:Column(name = "KEBS_ACCOUNT_NUMBER")
    @get:Basic
    var kebsAccountNumber: String? = null
    @get:Column(name = "USD_ACCOUNT_NUMBER")
    @get:Basic
    var usdAccountNumber: String? = null
    @get:Column(name = "BANK_CODE")
    @get:Basic
    var bankCode: Long = 0
    @get:Column(name = "SWIFT_CODE")
    @get:Basic
    var swiftCode: String? = null
    @get:Column(name = "VAT_NUMBER")
    @get:Basic
    var vatNumber: String? = null
    @get:Column(name = "PIN_NUMBER")
    @get:Basic
    var pinNumber: String? = null
    @get:Column(name = "STATUS")
    @get:Basic
    var status: Long? = null
    @get:Column(name = "VAR_FIELD_1")
    @get:Basic
    var varField1: String? = null
    @get:Column(name = "VAR_FIELD_2")
    @get:Basic
    var varField2: String? = null
    @get:Column(name = "VAR_FIELD_3")
    @get:Basic
    var varField3: String? = null
    @get:Column(name = "VAR_FIELD_4")
    @get:Basic
    var varField4: String? = null
    @get:Column(name = "VAR_FIELD_5")
    @get:Basic
    var varField5: String? = null
    @get:Column(name = "VAR_FIELD_6")
    @get:Basic
    var varField6: String? = null
    @get:Column(name = "VAR_FIELD_7")
    @get:Basic
    var varField7: String? = null
    @get:Column(name = "VAR_FIELD_8")
    @get:Basic
    var varField8: String? = null
    @get:Column(name = "VAR_FIELD_9")
    @get:Basic
    var varField9: String? = null
    @get:Column(name = "VAR_FIELD_10")
    @get:Basic
    var varField10: String? = null
    @get:Column(name = "CREATED_BY")
    @get:Basic
    var createdBy: String? = null
    @get:Column(name = "CREATED_ON")
    @get:Basic
    var createdOn: Timestamp? = null
    @get:Column(name = "MODIFIED_BY")
    @get:Basic
    var modifiedBy: String? = null
    @get:Column(name = "MODIFIED_ON")
    @get:Basic
    var modifiedOn: Timestamp? = null
    @get:Column(name = "DELETE_BY")
    @get:Basic
    var deleteBy: String? = null
    @get:Column(name = "DELETED_ON")
    @get:Basic
    var deletedOn: Timestamp? = null
    @get:Column(name = "PARTNER")
    @get:Basic
    var partner: Long? = null
    @get:Column(name = "PAID_STATUS")
    @get:Basic
    var paidStatus: Long? = null
    @get:Column(name = "PAYMENT_DATE")
    @get:Basic
    var paymentDate: Timestamp? = null
    @get:Column(name = "COC_ID")
    @get:Basic
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