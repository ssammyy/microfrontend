package org.kebs.app.kotlin.apollo.common.dto.kappa.response

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.math.BigDecimal
import java.sql.Timestamp
import java.time.Instant

@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy::class)
class NotificationResponseValue {
    var statusCode: String? = null
    var statusDescription: String? = null
    var billReferenceCode: String? = null
    var responseDate: Timestamp? = Timestamp.from(Instant.now())

}


@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy::class)
class ValidationResponseValues {
    var statusCode: String? = null
    var statusDescription: String? = null
    var totalAmount: BigDecimal? = null
    var additionalInfo: String? = null
    var accountName: String? = null
    var accountNumber: String? = null
    var billReferenceCode: String? = null
    var currency: String? = null

    //    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")
    var responseDate: Timestamp? = null
    var extras: String? = null

}

