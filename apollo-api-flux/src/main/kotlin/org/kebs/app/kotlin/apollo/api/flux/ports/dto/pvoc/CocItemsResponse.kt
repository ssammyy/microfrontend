package org.kebs.app.kotlin.apollo.api.flux.ports.dto.pvoc

import org.kebs.app.kotlin.apollo.store.model.CocItemsEntity
import org.kebs.app.kotlin.apollo.store.model.CocsEntity


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
//class CocItems {
//
//    var cocNumber: String? = null
//    var shipmentLineNumber: Long = 0
//    var shipmentLineHscode: String? = null
//    var shipmentLineQuantity: Long = 0
//    var shipmentLineUnitofMeasure: String? = null
//    var shipmentLineDescription: String? = null
//    var shipmentLineVin: String? = null
//    var shipmentLineStickerNumber: String? = null
//    var shipmentLineIcs: String? = null
//    var shipmentLineStandardsReference: String? = null
//    var shipmentLineLicenceReference: String? = null
//    var shipmentLineRegistration: String? = null
//}

class CocWithItems{
    var coc:CocsEntity? = null
    var items: List<CocItemsEntity>? = null
}

class CoiWithItems{
    var coi:CocsEntity? = null
    var items: List<CocItemsEntity>? = null
}
