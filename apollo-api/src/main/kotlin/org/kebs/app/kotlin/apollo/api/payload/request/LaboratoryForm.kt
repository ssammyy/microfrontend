package org.kebs.app.kotlin.apollo.api.payload.request

import javax.validation.constraints.NotEmpty

class LaboratoryForm {
    @NotEmpty(message = "Laboratory Name is required")
    var laboratoryName: String? = null

    @NotEmpty(message = "Laboratory description is required")
    var laboratoryDesc: String? = null

    var status: Int? = null
}