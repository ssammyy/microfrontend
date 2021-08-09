package org.kebs.app.kotlin.apollo.api.handlers.forms

import org.codehaus.jackson.annotate.JsonIgnoreProperties
import javax.validation.constraints.Email
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern

class WaiverProductDetails {
    var productDescription: String = ""
    var hsCode: String = ""
    var productUnit: Double = 0.0
    var quantity: Int = 0
    var countryOfOrigin: String = ""
    var currency: String = "KES"
    var totalAmount: Double = 0.0
}

class UploadedDocuments {
    var uploadId: Long? = null
    var documentName: String? = null
    var contentType: String? = null
    var bytes: ByteArray? = null
}

@JsonIgnoreProperties(ignoreUnknown = true)
class WaiverApplication {
    var applicantName: String? = null

    @NotNull(message = "Telephone number is required")
    @Pattern(regexp = "^\\d{7,15}$", message = "Phone number should contain only numbers")
    var telephoneNumber: String? = null
    var postalAddress: String? = null

    @Email(message = "Email address is invalid")
    var emailAddress: String? = null

    @Pattern(regexp = "^[\\d\\w]{5,25}$", message = "KRA PIN is required to have numbers and letters only")
    var kraPin: String? = null
    var category: String? = null
    var justification: String? = null
    var contactPersonName: String? = null

    @Email(message = "Email is invalid")
    var contactPersonEmail: String? = null
    var contactPersonPhone: String? = null
    var productDescription: String? = null
    var products: List<WaiverProductDetails>? = null
}