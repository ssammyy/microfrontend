package org.kebs.app.kotlin.apollo.api.handlers.forms

import org.codehaus.jackson.annotate.JsonIgnoreProperties
import javax.validation.constraints.Email

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
    var category: String? = null
    var justification: String? = null
    var contactPersonName: String? = null

    @Email(message = "Email is invalid")
    var contactPersonEmail: String? = null
    var contactPersonPhone: String? = null
    var productDescription: String? = null
    var products: List<WaiverProductDetails>? = null
}