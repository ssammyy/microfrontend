package org.kebs.app.kotlin.apollo.common.dto.brs.response

import com.google.gson.annotations.SerializedName

class BrsLookUpRecords {

    var status: String? = null

    @SerializedName("registration_number")
    var registrationNumber: String? = null

    @SerializedName("registration_date")
    var registrationDate: java.util.Date? = null

    @SerializedName("postal_address")
    var postalAddress: String? = null

    @SerializedName("physical_address")
    var physicalAddress: String? = null

    @SerializedName("phone_number")
    var phoneNumber: String? = null
    var partners: MutableList<BrsLookupBusinessPartners?> = mutableListOf()

    var id: String? = null
    var email: String? = null

    @SerializedName("kra_pin")
    var kraPin: String? = null

    @SerializedName("business_name")
    var businessName: String? = null
    var branches: MutableList<BrsLookupBusinessBranches?>? = null
}