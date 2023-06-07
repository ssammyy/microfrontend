package org.kebs.app.kotlin.apollo.api.ports.provided.sage.requests

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal
import java.sql.Date
import java.sql.Timestamp

// import com.fasterxml.jackson.databind.ObjectMapper; // version 2.11.1
// import com.fasterxml.jackson.annotation.JsonProperty; // version 2.11.1
/* ObjectMapper om = new ObjectMapper();
Root root = om.readValue(myJsonString), Root.class); */
class Header {
    var serviceName: String? = null
    var messageID: String? = null
    var connectionID: String? = null
    var connectionPassword: String? = null
}




class Request {
    @JsonProperty("DocumentNo")
    var documentNo: String? = null

    @JsonProperty("DocumentDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
    var documentDate: Date? = null

    @JsonProperty("DocType")
    var docType: Int? = null

    @JsonProperty("CurrencyCode")
    var currencyCode: String? = null

    @JsonProperty("CustomerCode")
    var customerCode: String? = null

    @JsonProperty("CustomerName")
    var customerName: String? = null

    @JsonProperty("InvoiceDesc")
    var invoiceDesc: String? = null

    @JsonProperty("RevenueAcc")
    var revenueAcc: String? = null

    @JsonProperty("RevenueAccDesc")
    var revenueAccDesc: String? = null

    @JsonProperty("Taxable")
    var taxable: Int? = null

    @JsonProperty("InvoiceAmnt")
    var invoiceAmnt: BigDecimal = BigDecimal.ZERO
}

class RootRequest {
    var header: Header? = null
    var request: Request? = null
}



class PaymentStatusBody {
    @JsonProperty("DocumentNo")
    var documentNo: String? = null

    @JsonProperty("PaymentReferenceNo")
    var paymentReferenceNo: String? = null
}

class PaymentStatusRequest {
    @JsonProperty("header")
    var header: Header? = null
    @JsonProperty("request")
    var request: PaymentStatusBody? = null
}