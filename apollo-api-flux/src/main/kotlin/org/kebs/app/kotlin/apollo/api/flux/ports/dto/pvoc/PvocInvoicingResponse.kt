package org.kebs.app.kotlin.apollo.api.flux.ports.dto.pvoc

import java.sql.Date
import java.sql.Timestamp


class PvocInvoicingResponse {

    var invoiceNumber: String? = null
    var soldTo: String? = null
    var invoiceDate: Date? = null
    var orderDate: Date? = null
    var orderNumber: String? = null
    var customerNumber: String? = null
    var poNumber: String? = null
    var shipVia: String? = null
    var termsCode: String? = null
    var description: String? = null
    var dueDate: Date? = null
    var discountDate: Date? = null
    var amountDue: Long = 0
    var discountAmount: Long = 0
    var unitPrice: Long = 0
    var unitOfMeasure: String? = null
    var currency: String? = null
    var subtotalBeforeTaxes: Long? = null
    var totalTaxes: Long = 0
    var totalAmount: Long = 0
    var accountName: String? = null
    var bankName: String? = null
    var branch: String? = null
    var kebsAccountNumber: String? = null
    var usdAccountNumber: String? = null
    var bankCode: Long = 0
    var swiftCode: String? = null
    var vatNumber: String? = null
    var pinNumber: String? = null
}