package org.kebs.app.kotlin.apollo.api.ports.provided.sage.response

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.OptBoolean
import java.math.BigDecimal
import java.sql.Timestamp
import javax.validation.Valid
import javax.validation.constraints.NotNull

class Header {
    @JsonProperty("messageID")
    var messageID: String? = null

    @JsonProperty("statusCode")
    @NotNull(message = "Status code is required")
    var statusCode: String? = null

    @JsonProperty("statusDescription")
    @NotNull(message = "Status description is required")
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
    @JsonProperty("BillReferenceNo")
    @NotNull(message = "Bill reference No is required")
    val demandNoteNo: String? = null

    @JsonProperty("PaymentDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, lenient = OptBoolean.TRUE)
    val paymentDate: Timestamp? = null

    @JsonProperty("CurrencyCode")
    val currencyCode: String? = null

    @JsonProperty("PaymentReferenceNo")
    val paymentReferenceNo: String? = null

    @JsonProperty("PaymentCode")
    val paymentCode: String? = null

    @JsonProperty("PaymentAmount")
    val paymentAmount: BigDecimal? = null

    @JsonProperty("PaymentDescription")
    val paymentDescription: String? = null

    @JsonProperty("AdditionalInfo")
    val additionalInfo: String? = null
}

class PaymentStatusResult {
    @JsonProperty("header")
    @Valid
    @NotNull(message = "Header details are required")
    var header: Header? = null

    @Valid
    @JsonProperty("request")
    @NotNull(message = "Request details are required")
    val response: PaymentResult? = null
}