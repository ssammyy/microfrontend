package org.kebs.app.kotlin.apollo.common.dto.sage.response

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal
import java.sql.Timestamp
import javax.validation.constraints.NotEmpty


class RequestHeader {
    var serviceName: String? = null
    var messageID: String? = null
    var connectionID: String? = null
    var connectionPassword: String? = null
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