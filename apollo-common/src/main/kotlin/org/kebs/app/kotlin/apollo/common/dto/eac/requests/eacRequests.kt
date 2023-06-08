package org.kebs.app.kotlin.apollo.common.dto.eac.requests

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import java.time.LocalDate


@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
class CertifiedProduct {
    var accessToken: String? = null
    var secret: String? = null
    var productName: String? = null
    var brandName: String? = null
    var countryOfOrigin: String? = null
    var hsCode: String? = null
    var standardGoverning: String? = null

    @JsonDeserialize(using = LocalDateDeserializer::class)
    @JsonSerialize(using = LocalDateSerializer::class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    var dateMarkIssued: LocalDate? = null

    @JsonDeserialize(using = LocalDateDeserializer::class)
    @JsonSerialize(using = LocalDateSerializer::class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    var dateMarkedExpires: LocalDate? = null
    var productDescription: String? = null
    var permitNumber: String? = null
    var productReference: String? = null
    var agency: String? = null
    var regulationStatus: String? = null
    var productState: String? = null

}

data class FilterProductRequest(var filterProduct: String, var filterAgency: String, var regulationStatus: String, var productState: String)

data class TokenRequest(val username: String, val password: String, val secret: String)

