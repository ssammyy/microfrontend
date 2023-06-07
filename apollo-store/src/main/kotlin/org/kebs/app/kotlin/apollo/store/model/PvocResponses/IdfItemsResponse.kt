package org.kebs.app.kotlin.apollo.store.model.PvocResponses

import org.springframework.stereotype.Component
import javax.persistence.Basic
import javax.persistence.Column

@Component
class IdfItemsResponse {

    var idfNumber: String? = null
    var itemDescription: String? = null
    var hsCode: String? = null
    var unitOfMeasure: String? = null
    var quantity: Long = 0
    var newUsed: String? = null
    var applicableStandard: String? = null
    var itemCost: Long = 0
}