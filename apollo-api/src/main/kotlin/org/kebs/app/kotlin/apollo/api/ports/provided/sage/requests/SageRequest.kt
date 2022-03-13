package org.kebs.app.kotlin.apollo.api.ports.provided.sage.requests

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import org.kebs.app.kotlin.apollo.store.model.CdDemandNoteEntity
import org.kebs.app.kotlin.apollo.store.model.di.CdDemandNoteItemsDetailsEntity
import java.math.BigDecimal
import java.sql.Date

class RequestItems {
    @JsonProperty("ProductName")
    var productName: String? = null

    @JsonProperty("MRate")
    var rate: String? = null

    @JsonProperty("CFValue")
    var cfValue: BigDecimal? = null

    companion object {
        fun fromEntity(item: CdDemandNoteItemsDetailsEntity): RequestItems {
            return RequestItems().apply {
                cfValue = item.cfvalue
                productName = item.product
                rate = item.rate
            }
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
                emailAddress = dn.address
                entryNo = dn.entryNo ?: ""
                courier = ""
                otherInfo = dn.currency
                totalAmount = dn.totalAmount ?: BigDecimal.ZERO
            }
            // Only applicable to JKA
            if ("JKA".equals(dn.shippingAgent, true)) {
                req.entryPoint = dn.entryPoint ?: "KSM"
                req.courier = dn.courier ?: ""
            }
            return req
        }
    }
}