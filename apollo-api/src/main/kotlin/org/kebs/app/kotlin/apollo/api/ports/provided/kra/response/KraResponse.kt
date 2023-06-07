package org.kebs.app.kotlin.apollo.api.ports.provided.kra.response

import com.fasterxml.jackson.annotation.JsonProperty

class KraResponse {

    @JsonProperty("status")
    var status: String? = null

    @JsonProperty("responseCode")
    var responseCode: String? = null

    @JsonProperty("message")
    var message: String? = null

}