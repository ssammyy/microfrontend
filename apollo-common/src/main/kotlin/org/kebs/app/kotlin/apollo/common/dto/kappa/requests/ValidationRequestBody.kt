package org.kebs.app.kotlin.apollo.common.dto.kappa.requests

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.sql.Timestamp
import java.time.Instant

@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy::class)
data class ValidationRequestBody(
    val transactionReferenceCode: String,

    val institutionCode: String,
) {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")
    val transactionDate: Timestamp? = Timestamp.from(Instant.now())

}
