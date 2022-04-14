package org.kebs.app.kotlin.apollo.api.ports.provided.sage.requests

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import org.kebs.app.kotlin.apollo.store.model.CdDemandNoteEntity
import org.kebs.app.kotlin.apollo.store.model.di.CdDemandNoteItemsDetailsEntity
import org.kebs.app.kotlin.apollo.store.model.invoice.BillPayments
import org.kebs.app.kotlin.apollo.store.model.invoice.BillTransactionsEntity
import java.math.BigDecimal
import java.sql.Date
import java.sql.Timestamp

class RequestItems {
    @JsonProperty("ProductName")
    var productName: String? = null

    @JsonProperty("MRate")
    var rate: Double? = null

    @JsonProperty("CFValue")
    var cfValue: BigDecimal? = null

    companion object {
        fun fromEntity(item: CdDemandNoteItemsDetailsEntity): RequestItems {
            val itm = RequestItems().apply {
                cfValue = item.cfvalue
            }
            try {
                itm.rate = item.rate?.toDoubleOrNull()
            } catch (ex: NumberFormatException) {
                itm.rate = 0.0
            }
            itm.productName = item.product
            if ((item.product?.length ?: 0) > 50) {
                itm.productName = item.product?.substring(0, 49)
            }
            return itm
        }

        fun fromList(items: List<CdDemandNoteItemsDetailsEntity>): List<RequestItems> {
            val dtos = mutableListOf<RequestItems>()
            items.forEach { dtos.add(fromEntity(it)) }
            return dtos
        }
    }
}

class SageRequest {
    @JsonProperty("CustomerName")
    var customerName: String? = null

    @JsonProperty("MDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
    var documentDate: Date? = null

    @JsonProperty("ShippingAgent")
    var shippingAgent: String? = null

    @JsonProperty("CustomerTelephone")
    var customerTelephone: String? = null

    @JsonProperty("EmailAddress")
    var emailAddress: String? = null

    @JsonProperty("EntryNo")
    var entryNo: String? = null

    @JsonProperty("EntryPoint")
    var entryPoint: String? = null

    @JsonProperty("Courier")
    var courier: String? = null

    @JsonProperty("OtherInfo")
    var otherInfo: String? = null

    @JsonProperty("TotalAmount")
    var totalAmount: BigDecimal = BigDecimal.ZERO

    companion object {
        fun fromEntity(dn: CdDemandNoteEntity): SageRequest {
            val req = SageRequest().apply {
                customerName = dn.nameImporter
                documentDate = dn.dateGenerated
                shippingAgent = dn.shippingAgent ?: "UNKNOWN"
                customerTelephone = dn.telephone
                emailAddress = when {
                    dn.address?.contains('@') == true -> dn.address
                    else -> "example@example.com"
                }
                entryPoint = dn.entryPoint
                entryNo = dn.entryNo ?: ""
                courier = dn.courier ?: ""
                otherInfo = dn.currency
                totalAmount = dn.totalAmount ?: BigDecimal.ZERO
            }
            return req
        }
    }
}


class InvoiceRequestItems {
    @JsonProperty("RevenueAcc")
    var revenueAcc: String? = null

    @JsonProperty("RevenueAccDesc")
    var revenueAccDesc: String? = null

    @JsonProperty("Taxable")
    var taxable: Int? = null

    @JsonProperty("MAmount")
    var amount: BigDecimal? = null

    @JsonProperty("TaxAmount")
    var taxAmount: BigDecimal? = null

    companion object {
        fun fromEntity(item: BillTransactionsEntity, accountNumber: String, accountDescription: String): InvoiceRequestItems {
            val itm = InvoiceRequestItems().apply {
                amount = item.amount
                taxAmount = BigDecimal.ZERO
                taxable = 0
            }
            itm.revenueAcc = accountNumber
            itm.revenueAccDesc = accountDescription
            if ((item.description?.length ?: 0) > 50) {
                itm.revenueAcc = item.description?.substring(0, 49)
            }
            return itm
        }

        fun fromList(items: List<BillTransactionsEntity>, accountNumber: String, accountDescription: String): List<InvoiceRequestItems> {
            val dtos = mutableListOf<InvoiceRequestItems>()
            items.forEach { dtos.add(fromEntity(it, accountNumber, accountDescription)) }
            return dtos
        }
    }
}

class SageInvoiceRequest {
    @JsonProperty("BatchNo")
    var batchNo: String? = null

    @JsonProperty("DocumentDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
    var documentDate: Timestamp? = null

    @JsonProperty("InvoiceType")
    var invoiceType: Int? = null

    @JsonProperty("CurrencyCode")
    var currencyCode: String? = null

    @JsonProperty("CustomerCode")
    var customerCode: String? = null

    @JsonProperty("CustomerName")
    var customerName: String? = null

    @JsonProperty("InvoiceDesc")
    var invoiceDesc: String? = null

    @JsonProperty("InvoiceAmnt")
    var invoiceAmnt: BigDecimal? = null


    companion object {
        fun fromEntity(dn: BillPayments): SageInvoiceRequest {
            val req = SageInvoiceRequest().apply {
                batchNo = dn.billNumber
                documentDate = dn.billDate
                invoiceType = dn.billType ?: 1
                currencyCode = dn.currencyCode
                customerCode = dn.customerCode
                customerName = dn.customerName
                invoiceDesc = dn.billDescription
                invoiceAmnt = dn.billAmount
            }
            return req
        }
    }
}