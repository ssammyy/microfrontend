package org.kebs.app.kotlin.apollo.store.model.PvocResponses

import org.springframework.stereotype.Component
import java.sql.Timestamp
import javax.persistence.Basic
import javax.persistence.Column

@Component
class IdfsResponse {

    var idfNumber: String? = null
    var ucr: String? = null
    var importerName: String? = null
    var importerAddress: String? = null
    var importerEmail: String? = null
    var importerTelephoneNumber: String? = null
    var importerContactName: String? = null
    var importerFax: String? = null
    var sellerName: String? = null
    var sellerAddress: String? = null
    var sellerEmail: String? = null
    var sellerTelephoneNumber: String? = null
    var sellerContactName: String? = null
    var sellerFax: String? = null
    var countryOfSupply: String? = null
    var portOfDischarge: String? = null
    var portOfCustomsClearance: String? = null
    var modeOfTransport: String? = null
    var comesa: String? = null
    var invoiceNumber: String? = null
    var invoiceDate: Timestamp? = null
    var currency: String? = null
    var exchangeRate: Long = 0
    var fobValue: Long = 0
    var freight: Long = 0
    var insurance: Long? = null
    var otherCharges: Long = 0
    var total: Long = 0
    var observations: String? = null
}