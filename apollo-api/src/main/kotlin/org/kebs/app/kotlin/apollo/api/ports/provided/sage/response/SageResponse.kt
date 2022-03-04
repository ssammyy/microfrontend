package org.kebs.app.kotlin.apollo.api.ports.provided.sage.response

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.OptBoolean
import java.sql.Timestamp
import javax.validation.Valid
import javax.validation.constraints.NotNull

class SageResponse {
    @JsonProperty("DemandNoteNo")
    @NotNull(message = "Bill reference No is required")
    val demandNoteNo: String? = null

    @JsonProperty("ResponseDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, lenient = OptBoolean.TRUE)
    val responseDate: Timestamp? = null
}

class SagePostingResponseResult {
    @JsonProperty("header")
    @Valid
    @NotNull(message = "Header details are required")
    var header: Header? = null

    @Valid
    @JsonProperty("request")
    @NotNull(message = "Request details are required")
    val response: SageResponse? = null
}