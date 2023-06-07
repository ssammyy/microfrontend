package org.kebs.app.kotlin.apollo.common.dto.eac.responses

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.sql.Timestamp

@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy::class)
data class RequestResult(
    var message: String? = null,
    var status: String? = null

)

@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy::class)
data class TokenRequestResult(
    var message: TokenResult? = null,
    var status: String? = null

)


@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class TokenResult(

//    @JsonProperty("access_token")
    var accessToken: String? = null,
    var username: String? = null,
//    @JsonProperty("security_notice")
    var securityNotice: String? = null,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "E, dd MMM yyyy HH:mm:ss zzz")
//    @JsonProperty("expiry_date")
    var expiryDate: Timestamp? = null
)
