package org.kebs.app.kotlin.apollo.common.dto.sage.response

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal
import java.sql.Timestamp
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull


class RequestHeader {
    @JsonProperty("messageID")
    var messageID: String? = null

    @JsonProperty("statusCode")
    @NotNull(message = "Status code is required")
    var statusCode: String? = null

    @JsonProperty("statusDescription")
    @NotNull(message = "Status description is required")
    var statusDescription: String? = null
}

class RequestBody {
    @JsonProperty("BillReferenceNo")
    var billReferenceNo: String? = null

    @NotEmpty(message = "Required field")
    @JsonProperty("PaymentDate")
    var paymentDate: Timestamp? = null

    @NotEmpty(message = "Required field")
    @JsonProperty("CurrencyCode")
    var currencyCode: String? = null

    @NotEmpty(message = "Required field")
    @JsonProperty("PaymentReferenceNo")
    var paymentReferenceNo: String? = null

    @NotEmpty(message = "Required field")
    @JsonProperty("PaymentCode")
    var paymentCode: String? = null

    @NotEmpty(message = "Required field")
    @JsonProperty("PaymentAmount")
    var paymentAmount: BigDecimal = BigDecimal.ZERO

    @NotEmpty(message = "Required field")
    @JsonProperty("PaymentDescription")
    var paymentDescription: String? = null

    @NotEmpty(message = "Required field")
    @JsonProperty("AdditionalInfo")
    var additionalInfo: String? = null
}

class SageNotificationResponse {
    var header: RequestHeader? = null
    var request: RequestBody? = null
}
