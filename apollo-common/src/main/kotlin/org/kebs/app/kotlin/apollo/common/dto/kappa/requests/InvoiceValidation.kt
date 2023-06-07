package org.kebs.app.kotlin.apollo.common.dto.kappa.requests

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.math.BigDecimal
import java.sql.Timestamp
import javax.validation.constraints.NotEmpty


@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy::class)
class NotificationRequestValue {
    @NotEmpty(message = "Required field")
    var billReferenceCode: String? = null
    var transactionID: String? = null
    var currency: String? = null
    var totalAmount: BigDecimal? = null

//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    var transactionDate: Timestamp? = null
    var customerName: String? = null


    var extras: Map<String, Any>? = null
    var paymentSource: String? = null
}

@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy::class)
data class ValidationRequestValue(
    @NotEmpty(message = "Required field")
    val billReferenceCode: String,
)
