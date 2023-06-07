package org.kebs.app.kotlin.apollo.common.dto.kappa.response

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.math.BigDecimal
import java.sql.Timestamp

@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy::class)
class ValidationResponseBody {
    val transactionReferenceCode: String? = null

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")
    val transactionDate: Timestamp? = null
    val institutionCode: String? = null
    val totalAmount: BigDecimal? = null
    val currency: String? = null
    val accountNumber: String? = null
    val accountName: String? = null
    val institutionName: String? = null
}
