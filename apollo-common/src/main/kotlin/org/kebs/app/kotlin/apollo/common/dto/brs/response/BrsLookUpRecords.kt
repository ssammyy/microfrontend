package org.kebs.app.kotlin.apollo.common.dto.brs.response

import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName

class BrsLookUpRecords {

    var status: String? = null

    @JsonProperty("registration_number")
    var registrationNumber: String? = null

    @JsonProperty("registration_date")
    var registrationDate: String? = null

    @JsonProperty("postal_address")
    var postalAddress: String? = null

    @JsonProperty("physical_address")
    var physicalAddress: String? = null

    @JsonProperty("phone_number")
    var phoneNumber: String? = null

    var partners: MutableList<BrsLookupBusinessPartners?> = mutableListOf()

    var id: String? = null
    var email: String? = null

    @JsonProperty("kra_pin")
    var kraPin: String? = null

    @JsonProperty("business_name")
    var businessName: String? = null

    var branches: MutableList<BrsLookupBusinessBranches?>? = null
}