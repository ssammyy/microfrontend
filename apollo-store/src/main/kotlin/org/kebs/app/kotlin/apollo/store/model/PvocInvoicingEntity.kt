package org.kebs.app.kotlin.apollo.store.model

import java.io.Serializable
import java.sql.Date
import java.sql.Timestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "DAT_KEBS_PVOC_INVOICING")
class PvocInvoicingEntity:Serializable {
    @Column(name = "ID", nullable = false, precision = 0)
    @Id
    var id: Long = 0

    @Column(name = "INVOICE_NUMBER", nullable = false, length = 50)
    @Basic
    var invoiceNumber: String? = null

    @Column(name = "SOLD_TO", nullable = false, length = 250)
    @Basic
    var soldTo: String? = null

    @Column(name = "INVOICE_DATE", nullable = false)
    @Basic
    var invoiceDate: Date? = null

    @Column(name = "ORDER_DATE", nullable = false)
    @Basic
    var orderDate: Date? = null

    @Column(name = "ORDER_NUMBER", nullable = false, length = 20)
    @Basic
    var orderNumber: String? = null

    @Column(name = "CUSTOMER_NUMBER", nullable = false, length = 20)
    @Basic
    var customerNumber: String? = null

    @Column(name = "PO_NUMBER", nullable = false, length = 50)
    @Basic
    var poNumber: String? = null

    @Column(name = "SHIP_VIA", nullable = false, length = 30)
    @Basic
    var shipVia: String? = null

    @Column(name = "TERMS_CODE", nullable = false, length = 30)
    @Basic
    var termsCode: String? = null

    @Column(name = "DESCRIPTION", nullable = false, length = 300)
    @Basic
    var description: String? = null

    @Column(name = "DUE_DATE", nullable = false)
    @Basic
    var dueDate: Date? = null

    @Column(name = "DISCOUNT_DATE", nullable = false)
    @Basic
    var discountDate: Date? = null

    @Column(name = "AMOUNT_DUE", nullable = false, precision = 2)
    @Basic
    var amountDue: Long = 0

    @Column(name = "DISCOUNT_AMOUNT", nullable = false, precision = 2)
    @Basic
    var discountAmount: Long = 0

    @Column(name = "UNIT_PRICE", nullable = false, precision = 2)
    @Basic
    var unitPrice: Long = 0

    @Column(name = "UNIT_OF_MEASURE", nullable = true, length = 50)
    @Basic
    var unitOfMeasure: String? = null

    @Column(name = "CURRENCY", nullable = false, length = 10)
    @Basic
    var currency: String? = null

    @Column(name = "SUBTOTAL_BEFORE_TAXES", nullable = true, precision = 2)
    @Basic
    var subtotalBeforeTaxes: Long? = null

    @Column(name = "TOTAL_TAXES", nullable = false, precision = 2)
    @Basic
    var totalTaxes: Long = 0

    @Column(name = "TOTAL_AMOUNT", nullable = false, precision = 2)
    @Basic
    var totalAmount: Long = 0

    @Column(name = "AMOUNT_PAID", nullable = false, precision = 2)
    @Basic
    var amountPaid: Long? = null

    @Column(name = "ACCOUNT_NAME", nullable = false, length = 300)
    @Basic
    var accountName: String? = null

    @Column(name = "BANK_NAME", nullable = false, length = 300)
    @Basic
    var bankName: String? = null

    @Column(name = "BRANCH", nullable = false, length = 300)
    @Basic
    var branch: String? = null

    @Column(name = "KEBS_ACCOUNT_NUMBER", nullable = false, length = 100)
    @Basic
    var kebsAccountNumber: String? = null

    @Column(name = "USD_ACCOUNT_NUMBER", nullable = false, length = 100)
    @Basic
    var usdAccountNumber: String? = null

    @Column(name = "BANK_CODE", nullable = false, precision = 0)
    @Basic
    var bankCode: Long = 0

    @Column(name = "SWIFT_CODE", nullable = false, length = 30)
    @Basic
    var swiftCode: String? = null

    @Column(name = "VAT_NUMBER", nullable = false, length = 30)
    @Basic
    var vatNumber: String? = null

    @Column(name = "PIN_NUMBER", nullable = false, length = 30)
    @Basic
    var pinNumber: String? = null

    @Column(name = "STATUS", nullable = true, precision = 0)
    @Basic
    var status: Long? = null

    @Column(name = "VAR_FIELD_1", nullable = true, length = 350)
    @Basic
    var varField1: String? = null

    @Column(name = "VAR_FIELD_2", nullable = true, length = 350)
    @Basic
    var varField2: String? = null

    @Column(name = "VAR_FIELD_3", nullable = true, length = 350)
    @Basic
    var varField3: String? = null

    @Column(name = "VAR_FIELD_4", nullable = true, length = 350)
    @Basic
    var varField4: String? = null

    @Column(name = "VAR_FIELD_5", nullable = true, length = 350)
    @Basic
    var varField5: String? = null

    @Column(name = "VAR_FIELD_6", nullable = true, length = 350)
    @Basic
    var varField6: String? = null

    @Column(name = "VAR_FIELD_7", nullable = true, length = 350)
    @Basic
    var varField7: String? = null

    @Column(name = "VAR_FIELD_8", nullable = true, length = 350)
    @Basic
    var varField8: String? = null

    @Column(name = "VAR_FIELD_9", nullable = true, length = 350)
    @Basic
    var varField9: String? = null

    @Column(name = "VAR_FIELD_10", nullable = true, length = 350)
    @Basic
    var varField10: String? = null

    @Column(name = "CREATED_BY", nullable = true, length = 100)
    @Basic
    var createdBy: String? = null

    @Column(name = "CREATED_ON", nullable = true)
    @Basic
    var createdOn: Timestamp? = null

    @Column(name = "MODIFIED_BY", nullable = true, length = 100)
    @Basic
    var modifiedBy: String? = null

    @Column(name = "MODIFIED_ON", nullable = true)
    @Basic
    var modifiedOn: Timestamp? = null

    @Column(name = "DELETE_BY", nullable = true, length = 100)
    @Basic
    var deleteBy: String? = null

    @Column(name = "DELETED_ON", nullable = true)
    @Basic
    var deletedOn: Timestamp? = null

    @Column(name = "PAYMENT_DATE", nullable = true)
    @Basic
    var paymentDate: Date? = null

    @Column(name = "PAID_STATUS", nullable = true)
    @Basic
    var paidStatus: Int? = null

    @Column(name = "RECONCIALITION_ID", nullable = true)
    @Basic
    var reconcialitionId: Long? = null

    @Column(name = "PARTNER", nullable = true, precision = 0)
    @Basic
    var partner: Long? = null

    @Column(name = "REASON", nullable = true, precision = 0)
    @Basic
    var reason: String? = null

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as PvocInvoicingEntity
        return id == that.id && amountDue == that.amountDue && discountAmount == that.discountAmount && unitPrice == that.unitPrice && totalTaxes == that.totalTaxes && totalAmount == that.totalAmount && bankCode == that.bankCode &&
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
                discountDate == that.discountDate &&
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
                amountPaid == that.amountPaid &&
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
                paymentDate == that.paymentDate &&
                paidStatus == that.paidStatus &&
                reconcialitionId == that.reconcialitionId &&
                reason == that.reason &&
                partner == that.partner
    }

    override fun hashCode(): Int {
        return Objects.hash(
                id,
                invoiceNumber,
                soldTo,
                invoiceDate,
                orderDate,
                orderNumber,
                customerNumber,
                poNumber,
                shipVia,
                termsCode,
                description,
                dueDate,
                discountDate,
                amountDue,
                discountAmount,
                unitPrice,
                unitOfMeasure,
                currency,
                subtotalBeforeTaxes,
                totalTaxes,
                totalAmount,
                accountName,
                bankName,
                branch,
                kebsAccountNumber,
                usdAccountNumber,
                bankCode,
                amountPaid,
                swiftCode,
                vatNumber,
                pinNumber,
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
                deletedOn,
                paidStatus,
                paymentDate,
                reconcialitionId,
                reason,
                partner
        )
    }
}