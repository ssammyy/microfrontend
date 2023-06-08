package org.kebs.app.kotlin.apollo.api.flux.ports.dto.pvoc

import javax.validation.constraints.NotEmpty


class PvocQueriesResponse {

    var cocNumber: String? = null
    var rfcNumber: String? = null
    var invoiceNumber: String? = null

    @NotEmpty(message = "Required field")
    var ucrNumber: String? = null
    var kebsQuery: String? = null
    var kebsResponse: String? = null
    var partnerQuery: String? = null
    var partnerResponse: String? = null
    var partnerResponseAnalysis: String? = null
    var conclusion: String? = null
    var linkToUploads: String? = null
}