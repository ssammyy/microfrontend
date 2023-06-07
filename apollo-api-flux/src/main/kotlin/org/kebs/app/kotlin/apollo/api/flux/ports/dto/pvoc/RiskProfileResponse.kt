package org.kebs.app.kotlin.apollo.api.flux.ports.dto.pvoc

import java.sql.Date


class RiskProfileResponse {
    var hsCode: String? = null
    var brandName: String? = null
    var productDescription: String? = null
    var countryOfSupply: String? = null
    var manufacturer: String? = null
    var traderName: String? = null
    var importerName: String? = null
    var exporterName: String? = null
    var riskLevel: String? = null
    var riskDescription: String? = null
    var remarks: String? = null
    var categorizationDate: Date? = null
}