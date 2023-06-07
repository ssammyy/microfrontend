package org.kebs.app.kotlin.apollo.common.dto.brs.response

import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName

class BrsLookupBusinessPartners {
    var name: String? = null

    @JsonProperty("id_type")
    var idType: String? = null

    @JsonProperty("id_number")
    var idNumber: String? = null


}