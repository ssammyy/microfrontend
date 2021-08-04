package org.kebs.app.kotlin.apollo.api.ports.provided.sage.requests

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal

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
    var documentDate: String? = null

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

