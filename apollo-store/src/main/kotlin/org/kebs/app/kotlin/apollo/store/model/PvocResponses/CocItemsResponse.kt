package org.kebs.app.kotlin.apollo.store.model.PvocResponses

import org.springframework.stereotype.Component
import javax.persistence.Basic
import javax.persistence.Column

@Component
class CocItemsResponse {

    var cocNumber: String? = null
    var shipmentLineNumber: Long = 0
    var shipmentLineHscode: String? = null
    var shipmentLineQuantity: Long = 0
    var shipmentLineUnitofMeasure: String? = null
    var shipmentLineDescription: String? = null
    var shipmentLineVin: String? = null
    var shipmentLineStickerNumber: String? = null
    var shipmentLineIcs: String? = null
    var shipmentLineStandardsReference: String? = null
    var shipmentLineLicenceReference: String? = null
    var shipmentLineRegistration: String? = null
}