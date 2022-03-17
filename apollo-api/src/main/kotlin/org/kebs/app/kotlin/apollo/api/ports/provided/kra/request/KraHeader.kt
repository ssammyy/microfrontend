package org.kebs.app.kotlin.apollo.api.ports.provided.kra.request

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.Timestamp
import java.time.Instant
import javax.validation.Valid
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

class KraHeader {
    @NotNull(message = "Required field")
    var loginId: String? = null

    @NotNull(message = "Required field")
    var password: String? = null

    @NotEmpty(message = "Required field")
    var hash: String? = null

    @NotNull(message = "Required field")
//    DD-MM-YYYY’T’HH:MM:SS
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy'T'HH:mm:ss")
    var transmissionDate: Timestamp? = Timestamp.from(Instant.now())

    @NotNull(message = "Required field")
    var noOfRecords: String? = null
}

class KraRequest {
    @JsonProperty("HEADER")
    @NotNull(message = "Required field")
    @Valid
    var header: KraHeader? = null

    @JsonProperty("DETAILS")
    @Valid
    @NotNull(message = "Required field")
    var details: KraDetails? = null
}


class KraDetails{
    @NotEmpty(message = "Required field")
    var entryNumber: String? = null

    @NotEmpty(message = "Required field")
    var kraPin: String? = null

    @NotEmpty(message = "Required field")
    var manufacturerName: String? = null

    @NotNull(message = "Required field")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    var registrationDate: String? = null

     @NotEmpty(message = "Required field")
    var status: String? = null

}