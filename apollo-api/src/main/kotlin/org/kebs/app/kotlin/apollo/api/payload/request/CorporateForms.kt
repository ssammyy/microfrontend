package org.kebs.app.kotlin.apollo.api.payload.request

import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

class CorporateForm {
    @NotNull(message = "Enter corporate PIN")
    @Size(min = 5, max = 80, message = "Please enter a valid PIN")
    var corporateIdentifier: String? = null

    @NotNull(message = "Enter corporate Name")
    var corporateName: String? = null

    @NotNull(message = "Enter corporate Code")
    var groupCode: String? = null

    @NotNull(message = "Please select corporate Type")
    var corporateType: String? = null // COURIER

    @Email(message = "Please add a valid email")
    var corporateEmail: String? = null

    @NotEmpty(message = "Please enter contact names")
    var contactName: String? = null

    @NotEmpty(message = "Please enter contact phone number")
    var contactPhone: String? = null

    @NotEmpty(message = "Please enter corporate phone number")
    var corporatePhone: String? = null

    @Email(message = "Please add a valid email")
    var contactEmail: String? = null
    var isCiakMember: Boolean? = false
    var billingLimitId: Long = 0
    var mouDays: Int? = 0
}

class CorporateStatusUpdateForm {
    var actionCode: String? = null
    var remarks: String? = null
}