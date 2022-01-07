package org.kebs.app.kotlin.apollo.api.ports.provided.sage.response

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
    var messageID: String? = null
    var statusCode: String? = null
    var statusDescription: String? = null
}

class Response {
    @JsonProperty("DocumentNo")
    var documentNo: String? = null

    @JsonProperty("ResponseDate")
    var responseDate: Timestamp? = null
}

class RootResponse {
    var header: Header? = null
    var response: Response? = null
}

class PaymentResult {
    @JsonProperty("DemandNoteNo")
    val demandNoteNo: String?=null
    @JsonProperty("PaymentDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
    val paymentDate: Timestamp?=null
    @JsonProperty("CurrencyCode")
    val currencyCode: String?=null
    @JsonProperty("PaymentReferenceNo")
    val paymentReferenceNo: String?=null
    @JsonProperty("PaymentCode")
    val paymentCode: String?=null
    @JsonProperty("PaymentAmount")
    val paymentAmount: BigDecimal?=null
    @JsonProperty("PaymentDescription")
    val paymentDescription: BigDecimal?=null
    @JsonProperty("AdditionalInfo")
    val additionalInfo: BigDecimal?=null
}

class PaymentStatusResult {
    @JsonProperty("header")
    var header: Header?=null
    @JsonProperty("request")
    val response: PaymentResult?=null
}