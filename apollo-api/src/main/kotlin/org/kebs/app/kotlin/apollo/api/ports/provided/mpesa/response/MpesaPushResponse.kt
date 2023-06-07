package org.kebs.app.kotlin.apollo.api.ports.provided.mpesa.response

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import org.codehaus.jackson.annotate.JsonProperty

@JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy::class)
class MpesaPushResponse {
    @JsonProperty("MerchantRequestID")
    val merchantRequestID: String? = null

    @JsonProperty("CheckoutRequestID")
    val checkoutRequestID: String? = null

    @JsonProperty("ResponseCode")
    val responseCode: String? = null

    @JsonProperty("ResponseDescription")
    val responseDescription: String? = null

    @JsonProperty("DefaultError")
    val defaultError: String? = null

    @JsonProperty("CustomerMessage")
    val customerMessage: String? = null

    @JsonProperty("ResultCode")
    val resultCode: String? = null

    @JsonProperty("ResultDesc")
    val resultDesc: String? = null
}