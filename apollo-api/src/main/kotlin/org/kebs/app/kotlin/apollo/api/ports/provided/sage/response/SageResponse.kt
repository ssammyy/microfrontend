package org.kebs.app.kotlin.apollo.api.ports.provided.sage.response

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.OptBoolean
import java.sql.Timestamp
import javax.validation.Valid
import javax.validation.constraints.NotNull

class SageHeader {
    @JsonProperty("messageID")
    var messageID: String? = null

    @JsonProperty("statusCode")
    @NotNull(message = "Status code is required")
    var statusCode: Int? = null

    @JsonProperty("statusDescription")
    @NotNull(message = "Status description is required")
    var statusDescription: String? = null
}

class SageResponse {
    @JsonProperty("DemandNoteNo")
    @NotNull(message = "Bill reference No is required")
    val demandNoteNo: String? = null

    @JsonProperty("ResponseDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, lenient = OptBoolean.TRUE)
    val responseDate: Timestamp? = null
}

class SagePostingResponseResult {
    @JsonProperty("header")
    @Valid
    @NotNull(message = "Header details are required")
    var header: SageHeader? = null

    @Valid
    @JsonProperty("response")
    @NotNull(message = "Request details are required")
    val response: SageResponse? = null
}

class SageInvoiceResponse {
    @JsonProperty("DocumentNo")
    @NotNull(message = "Bill reference No is required")
    val demandNoteNo: String? = null

    @JsonProperty("ResponseDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, lenient = OptBoolean.TRUE)
    val responseDate: Timestamp? = null
}

class SageInvoicePostingResponseResult {
    @JsonProperty("header")
    @Valid
    @NotNull(message = "Header details are required")
    var header: SageHeader? = null

    @Valid
    @JsonProperty("response")
    @NotNull(message = "Request details are required")
    val response: SageInvoiceResponse? = null
}


class SageCourierResponse {
    @JsonProperty("CustomerCode")
    @NotNull(message = "Customer code is required")
    val customerCode: String? = null

    @JsonProperty("CustomerName")
    val customerName: String? = null

    @JsonProperty("GroupCode")
    val groupCode: String? = null

    @JsonProperty("City")
    val city: String? = null

    @JsonProperty("Country")
    val country: String? = null

    @JsonProperty("TaxNo")
    val taxNo: String? = null

    @JsonProperty("CreditLimit")
    val creditLimit: Double? = null
}

class SageCourierDetailsResponseResult {
    @JsonProperty("header")
    @Valid
    @NotNull(message = "Header details are required")
    var header: SageHeader? = null

    @Valid
    @JsonProperty("response")
    val response: Array<SageCourierResponse>? = null
}

class RevenueLine {
    @JsonProperty("RevenueCode")
    val revenueLineCode: String? = null

    @JsonProperty("RevenueDesc")
    val description: String? = null

}


class SageRevenueLinesResponseResult {
    @JsonProperty("header")
    @Valid
    @NotNull(message = "Header details are required")
    var header: SageHeader? = null

    @Valid
    @JsonProperty("response")
    val response: Array<RevenueLine>? = null
}