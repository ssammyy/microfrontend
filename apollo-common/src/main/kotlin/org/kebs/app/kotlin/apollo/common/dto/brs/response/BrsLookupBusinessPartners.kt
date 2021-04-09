package org.kebs.app.kotlin.apollo.common.dto.brs.response

import com.google.gson.annotations.SerializedName

class BrsLookupBusinessPartners {
    var name: String? = null

    @SerializedName("id_type")
    var idType: String? = null

    @SerializedName("id_number")
    var idNumber: String? = null


}