package org.kebs.app.kotlin.apollo.store.model.PvocResponses

import org.springframework.stereotype.Component
import javax.persistence.Basic
import javax.persistence.Column

@Component
class RfcCoiItemsResponse {
    var declaredHsCode: String? = null
    var itemQuantity: String? = null
    var productDescription: String? = null
    var ownerPin: String? = null
    var ownerName: String? = null
}