package org.kebs.app.kotlin.apollo.store.model.PvocResponses

import org.springframework.stereotype.Component

@Component
class CocItemsList {
    var cocItems: MutableList<CocItemsResponse> = ArrayList<CocItemsResponse>()
}