package org.kebs.app.kotlin.apollo.store.model.PvocResponses

import org.springframework.stereotype.Component
import javax.persistence.Basic
import javax.persistence.Column

@Component
class PvocQuerriesResponse {

    var cocNumber: String? = null
    var rfcNumber: String? = null
    var invoiceNumber: String? = null
    var ucrNumber: String? = null
    var kebsQuery: String? = null
    var kebsResponse: String? = null
    var partnerQuery: String? = null
    var partnerResponse: String? = null
    var partnerResponseAnalysis: String? = null
    var conclusion: String? = null
    var linkToUploads: String? = null
}